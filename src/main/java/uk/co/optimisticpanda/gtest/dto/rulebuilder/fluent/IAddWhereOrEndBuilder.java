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
package uk.co.optimisticpanda.gtest.dto.rulebuilder.fluent;

import uk.co.optimisticpanda.gtest.dto.rule.IRule;

/**
 * An interface used by the fluent builder.
 * 
 * @param <D>
 *            Type that we are building a rule for.
 * @author Andy Lee
 */
public interface IAddWhereOrEndBuilder<D> extends IAddWhereBuilder<D> {

	/**
	 * build the rule
	 * 
	 * @return the created rule
	 */
	IRule<D> build();

	/**
	 * set the label. multiple calls overwrites the currently set label.
	 * 
	 * @param label
	 *            the label which describes this rule
	 * @return this to allow chaining.
	 */
	IAddWhereOrEndBuilder<D> setLabel(String label);

}