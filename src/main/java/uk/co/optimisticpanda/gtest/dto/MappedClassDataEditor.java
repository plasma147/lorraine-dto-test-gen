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
package uk.co.optimisticpanda.gtest.dto;

import java.util.List;

import uk.co.optimisticpanda.gtest.dto.rule.Edit;
import uk.co.optimisticpanda.gtest.dto.util.FunctionUtils;
import uk.co.optimisticpanda.gtest.dto.util.MapOfLists;

/**
 * A {@link IDataEditor} that can be used to edit multiple types of dto. Rules
 * are keyed on the class of the dto that its is applicable for.
 * 
 * @author Andy Lee
 */
public class MappedClassDataEditor implements IDataEditor<Object> {

	MapOfLists<Class<?>, Edit<? extends Object>> map = new MapOfLists<Class<?>, Edit<? extends Object>>();

	/**
	 * Create a new MappedClassDataEditor
	 */
	public MappedClassDataEditor() {
		//do nothing apart from instantiation
	}

	/**
	 * see {@link IDataEditor#edit(List)}
	 * 
	 * @param testData
	 *            the collection of dtos to apply the rules to.
	 */
	public void edit(List<Object> testData) {
		testData.stream()
					.map(FunctionUtils.indexed())
					.forEach(i -> edit(i.index, i.item));
	}

	/**
	 * see {@link IDataEditor#edit(int, Object)}
	 * 
	 * @param index
	 *            the current index of the dto in the list
	 * @param dataItem
	 *            the dto to apply the rule to.
	 */
	@SuppressWarnings("unchecked")
	public Object edit(int index, Object dataItem) {
		List<Edit<? extends Object>> rules = getEdits(dataItem);
		rules.stream()
			.map(r -> ((Edit<Object>) r))
			.filter(r -> r.isValid(index, dataItem))
			.forEach(r-> r.edit(index, dataItem));
		return dataItem;
	}

	/**
	 * This is protected to enable the use of other strategies when retrieving
	 * rules. For instance a strategy for dealing with proxied objects.
	 * 
	 * @param dataItem
	 *            the dto that the returned rules will be applicable for.
	 * @return The applicable rules for this dto.
	 * */
	protected List<Edit<? extends Object>> getEdits(Object dataItem) {
		return map.get(dataItem.getClass());
	}

	/**
	 * @param <D>
	 *            the type of the data item this rule will be stored against.
	 * @param clazz
	 *            the clazz of the dto.
	 * @param edit
	 *            the rule to store against this type of dto.
	 */
	public <D> void addEditForClass(Class<D> clazz, Edit<D> edit) {
		map.putA(clazz, edit);
	}
}
