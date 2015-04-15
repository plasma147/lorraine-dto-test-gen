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

/**
 * @author Andy Lee
 * 
 * @param <D>
 *            the type of object this generator creates.
 */
public interface IValueGenerator<D> {

	/**
	 * A value generator instance that is used as a null object, it does not generate
	 * anything and throws an {@link UnsupportedOperationException} if {@link #generate()} is called.
	 */
	@SuppressWarnings("unchecked")
	public static final IValueGenerator NOT_COVERED = new IValueGenerator() {

		@Override
		public Object generate() {
			throw new UnsupportedOperationException("This generator does not generate values");
		}

	};

	/**
	 * @return a new item
	 */
	D generate();

}