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
package uk.co.optimisticpanda.gtest.dto.rulebuilder.impl;

import static uk.co.optimisticpanda.gtest.dto.condition.CombinedCondition.BoolOp.AND;
import static uk.co.optimisticpanda.gtest.dto.condition.CombinedCondition.BoolOp.OR;
import uk.co.optimisticpanda.gtest.dto.condition.CombinedCondition;
import uk.co.optimisticpanda.gtest.dto.condition.ICondition;

class ConditionBuilder<D> {
	private ICondition condition;

	public ConditionBuilder(ICondition condition) {
		if(condition == null){
			throw new IllegalArgumentException("Condition must not be null");
		}
		this.condition = condition;
	}

	public void and(ICondition condition) {
		this.condition = new CombinedCondition(AND, this.condition, condition);
	}

	public void or(ICondition condition) {
		this.condition = new CombinedCondition(OR, this.condition, condition);
	}

	public ICondition build() {
		return condition;
	}
}
