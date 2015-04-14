/*
 * Copyright 2009 Andy Lee.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package uk.co.optimisticpanda.gtest.dto.defaultfill;

import java.util.HashMap;
import java.util.Map;

import uk.co.optimisticpanda.gtest.dto.edit.IEdit;

/**
 * This is used to store {@link IEdit}s used for filling newly created dtos with
 * default data
 * <p>
 * IEdits can be registered at different levels as they are looked up in the
 * following order:
 * <ol>
 * <li>property path (ognl/el style notation)
 * <li>The class, the name and the property type
 * <li>The property name and the property type.
 * <li>The property type
 * <li>{@link IValueGenerator#NOT_COVERED}
 * </ol>
 * </p>
 * 
 * @author Andy Lee
 * */
public class ValueGeneratorCacheImpl implements ValueGeneratorCache {

	protected final Map<String, IValueGenerator<?>> propertyDepthCache;
	protected final Map<ClassPropertyNameKey, IValueGenerator<?>> classAndPropertyNameAndTypeCache;
	protected final Map<PropertyNameAndPropertyTypeKey, IValueGenerator<?>> propertyNameAndTypeCache;
	protected final Map<String, IValueGenerator<?>> typeCache;

	/**
	 * A default constructor that initialises all the caches
	 * */
	public ValueGeneratorCacheImpl() {
		propertyDepthCache = new HashMap<String, IValueGenerator<?>>();
		classAndPropertyNameAndTypeCache = new HashMap<ClassPropertyNameKey, IValueGenerator<?>>();
		propertyNameAndTypeCache = new HashMap<PropertyNameAndPropertyTypeKey, IValueGenerator<?>>();
		typeCache = new HashMap<String, IValueGenerator<?>>();
	}

	/**
	 * Register a generator for properties based on an ognl or el type path from the
	 * root dto.
	 * 
	 * @param propertyDepth
	 *            an el type expression
	 * @param valueGen
	 *            the value generator to register with this path
	 * */
	public void registerAPropertyDepthGenerator(String propertyDepth, IValueGenerator<?> valueGen) {
		propertyDepthCache.put(propertyDepth, valueGen);
	}

	/**
	 * Register a generator for all named properties with the specified parent class
	 * and of the specified property type.
	 * 
	 * @param owningClass
	 *            The class that owns the property that the edit is being
	 *            registered against.
	 * @param propertyName
	 *            The name of the property that the edit is being registered
	 *            against
	 * @param valueGenerator
	 *            the value generator being registered
	 * */
	public void registerAClassNamePropertyNameGenerator(Class<?> owningClass, String propertyName, IValueGenerator<?> valueGenerator) {
		ClassPropertyNameKey key = new ClassPropertyNameKey(owningClass, propertyName);
		classAndPropertyNameAndTypeCache.put(key, valueGenerator);
	}

	/**
	 * Register a generator for all properties of a specific name and type.
	 * 
	 * @param propertyName
	 *            the name of the property the edit is being registered against
	 * @param propertyType
	 *            the type of the property
	 * @param valueGenerator
	 *            the value generator to register
	 * */
	public void registerAPropertyNameAndTypeGenerator(String propertyName, Class<?> propertyType, IValueGenerator<?> valueGenerator) {
		PropertyNameAndPropertyTypeKey key = new PropertyNameAndPropertyTypeKey(propertyName, propertyType);
		propertyNameAndTypeCache.put(key, valueGenerator);
	}

	/**
	 * Register a generator for all properties of a specific type.
	 * 
	 * @param mode
	 *            How this type and value generator should be registered
	 * @param propertyType
	 *            The type of the property to register an edit against
	 * @param valueGenerator
	 *            the value generator to register
	 * */
	public void registerATypeGenerator(RegisterTypeMode mode, Class<?> propertyType, IValueGenerator<?> valueGenerator) {
		typeCache.put(propertyType.getName(), valueGenerator);
		switch (mode) {
		case ALL_INTERFACES:
			registerInterfacesAgainstCache(propertyType, valueGenerator);
			break;
		case ALL_PARENTS:
			registerParentsAgainstCache(propertyType, valueGenerator);
			break;
		case ALL:
			registerParentsAgainstCache(propertyType, valueGenerator);
			registerInterfacesAgainstCache(propertyType, valueGenerator);
			break;
		case SINGLE_TYPE:
			break;
		default:
			throw new IllegalArgumentException("Case not known for:" + mode.name());
		}
	}

	/** 
	 * Register a generator against a single type 
	 */
	@Override
	public void registerATypeGenerator(Class<?> propertyType, IValueGenerator<?> valueGenerator) {
		registerATypeGenerator(RegisterTypeMode.SINGLE_TYPE, propertyType, valueGenerator);
	}

	private void registerParentsAgainstCache(Class<?> propertyType, IValueGenerator<?> generator) {
		if (propertyType != null && propertyType.getSuperclass() != null) {
			Class<?> parentClass = propertyType.getSuperclass();
			typeCache.put(parentClass.getName(), generator);
			registerParentsAgainstCache(parentClass, generator);
		}
	}

	private void registerInterfacesAgainstCache(Class<?> propertyType, IValueGenerator<?> generator) {
		if (propertyType != null && propertyType.getInterfaces() != null) {
			Class<?>[] interfaces = propertyType.getInterfaces();
			for (Class<?> interfaceClass: interfaces) {
				typeCache.put(interfaceClass.getName(), generator);
				registerParentsAgainstCache(interfaceClass, generator);
			}
		}
	}

	/**
	 * Return the value generator applicable for this key. See class javadoc for
	 * the strategy used to retrieve the value generator.
	 * 
	 * @param key
	 *            the key to look up a generator for
	 * @return a valueGenerator
	 * 
	 * */
	public IValueGenerator<?> lookUpGenerator(ValueGeneratorCacheKey key) {

		IValueGenerator<?> valueGenerator = propertyDepthCache.get(key.getPropertyPath());
		if (valueGenerator != null)
			return valueGenerator;

		valueGenerator = classAndPropertyNameAndTypeCache.get(new ClassPropertyNameKey(key));
		if (valueGenerator != null)
			return valueGenerator;

		valueGenerator = propertyNameAndTypeCache.get(new PropertyNameAndPropertyTypeKey(key));
		if (valueGenerator != null)
			return valueGenerator;

		valueGenerator = typeCache.get(key.getPropertyTypeName());
		if (valueGenerator != null)
			return valueGenerator;

		return IValueGenerator.NOT_COVERED;
	}

	/**
	 * Clears the cache of all registered property values
	 */
	public void clear() {
		propertyDepthCache.clear();
		classAndPropertyNameAndTypeCache.clear();
		propertyNameAndTypeCache.clear();
		typeCache.clear();
	}

	// ----------------------KEYS---------------------------

	private static class PropertyNameAndPropertyTypeKey {

		private final String propertyName;
		private final Class<?> propertyType;

		public PropertyNameAndPropertyTypeKey(String propertyName, Class<?> propertyType) {
			this.propertyName = propertyName;
			this.propertyType = propertyType;
		}

		public PropertyNameAndPropertyTypeKey(ValueGeneratorCacheKey key) {
			this(key.getPropertyName(), key.getPropertyType());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
			result = prime * result + ((propertyType == null) ? 0 : propertyType.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PropertyNameAndPropertyTypeKey other = (PropertyNameAndPropertyTypeKey) obj;
			if (propertyName == null) {
				if (other.propertyName != null)
					return false;
			} else if (!propertyName.equals(other.propertyName))
				return false;
			if (propertyType == null) {
				if (other.propertyType != null)
					return false;
			} else if (!propertyType.equals(other.propertyType))
				return false;
			return true;
		}

	}

	private static class ClassPropertyNameKey {

		private final Class<?> owningClass;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((owningClass == null) ? 0 : owningClass.hashCode());
			result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ClassPropertyNameKey other = (ClassPropertyNameKey) obj;
			if (owningClass == null) {
				if (other.owningClass != null)
					return false;
			} else if (!owningClass.equals(other.owningClass))
				return false;
			if (propertyName == null) {
				if (other.propertyName != null)
					return false;
			} else if (!propertyName.equals(other.propertyName))
				return false;
			return true;
		}

		private final String propertyName;

		public ClassPropertyNameKey(Class<?> owningClass, String propertyName) {
			this.owningClass = owningClass;
			this.propertyName = propertyName;
		}

		public ClassPropertyNameKey(ValueGeneratorCacheKey key) {
			this.owningClass = key.getOwningClass();
			this.propertyName = key.getPropertyName();
		}

	}

}
