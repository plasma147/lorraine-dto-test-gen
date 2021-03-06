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
package uk.co.optimisticpanda.gtest.dto.condition;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This condition wraps multiple conditions and returns the combined effects of its
 * {@link Condition#isValid(int, Object)} methods.
 * <p>
 * This Condition is configured by the use of {@link BoolOp} to determine whether
 * all {@link Condition}s need to be valid or just one for this CombinedCondition to
 * be valid.
 * </p>
 * 
 * @author Andy Lee
 */
class CombinedCondition implements Condition {

	/**
	 * An enum that is used to determine how the effects of the conditions are
	 * combined.
	 * 
	 */
	enum BoolOp {
		/**
		 * Used in configuration to specify that all conditions must return true
		 * for the {@link CombinedCondition} to return true.
		 */
		AND,
		/**
		 * Used in configuration to specify that only one condition must return true for the
		 * {@link CombinedCondition} matcher to return true.
		 */
		OR;

		private boolean getDefaultResultValue() {
			return this == AND;
		}
	}

	private final List<Condition> conditions;
	private final BoolOp operation;

	/**
	 * Create a {@link CombinedCondition} from a variable number of {@link Condition}es
	 * 
	 * @param operation
	 *            see {@link BoolOp}.
	 * @param condition
	 *            A variable number of conditions that are to be wrapped in this
	 *            CombinedCondition.
	 */
	CombinedCondition(BoolOp operation, Condition... condition) {
		this.operation = operation;
		this.conditions = Arrays.asList(condition);
	}

	/**
	 * @see uk.co.optimisticpanda.gtest.dto.condition.Condition#isValid(int,
	 *      java.lang.Object)
	 */
	@Override
	public <D> boolean isValid(int index, D dataItem) {
		boolean result = operation.getDefaultResultValue();
		for (Condition condition : conditions) {
			switch (operation) {
			case AND:
				result &= condition.isValid(index, dataItem);
				break;
			case OR:
				result |= condition.isValid(index, dataItem);
				break;
			default:
				throw new IllegalStateException();
			}
		}
		return result;
	}

	/**
	 * A human readable representation of this {@link Condition}.
	 */
	@Override
	public String toString() {
		Iterator<Condition> iterator = conditions.iterator();
		StringBuilder builder = new StringBuilder("(" + iterator.next() + ") ");
		while (iterator.hasNext()) {
			builder.append(operation + " (" + iterator.next() + ") ");
		}
		return builder.toString();
	}
}
