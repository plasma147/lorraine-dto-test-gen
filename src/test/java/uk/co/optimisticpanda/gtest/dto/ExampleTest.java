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
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static uk.co.optimisticpanda.gtest.dto.condition.Conditions.index;
import static uk.co.optimisticpanda.gtest.dto.condition.Conditions.not;
import static uk.co.optimisticpanda.gtest.dto.condition.Conditions.valueOf;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import uk.co.optimisticpanda.gtest.dto.rule.Edit;
import uk.co.optimisticpanda.gtest.dto.rulebuilder.impl.Edits;
/**
 * @author Andy Lee A class showing basic usage
 */
public class ExampleTest extends TestCase {

	public void testExample() {

		// Configure the TestUtilsContext to use a PropertySupportFactory
		// This is used in introspection.
		TestUtilsContext.useOgnl();

		// RuleUtils provide easy access to built in edit and conditions
		RuleUtils utils = new RuleUtils();

		// Rules are created from a rule factory and need an edit to start.
		Edits.doThis(utils.increment("name", "sample-"));

		// Each subsequent chained method call return interfaces that only
		// allows the writer to call methods in the correct order.
		Edits.doThis(utils.increment("name", "sample-")) // 
				.andThen(utils.set("date", new Date(System.currentTimeMillis())));

		// --------------------------------------------------------------------
		// Define some edits.
		Edit<SampleDto> edit1 = Edits.doThis(utils.<SampleDto>increment("name", "sample-")) //
				.andThen(utils.set("date", new Date(System.currentTimeMillis()))) //
				.where(index().is(3)) //
				.or(not(index().isOdd())) //
				.build();

		Edit<SampleDto> edit2 = Edits.doThis(utils.<SampleDto>set("name", "CHANGED")) //
				.where(valueOf("name").is("sample-3")) //
				.build();

		System.out.println("Edit1:\t" + edit1);
		System.out.println("Edit2:\t" + edit2);

		// Add the rules to a data editor
		IDataEditor<SampleDto> editor = new SimpleDataEditor<SampleDto>() //
				.addEdit(edit1) //
				.addEdit(edit2); //

		// Perform the actual editing
		List<SampleDto> dtos = range(0, 5)
			.mapToObj(i -> editor.edit(i, new SampleDto()))
			.collect(toList());

		// See the output
		dtos.forEach(System.out::println);
	}

	static class SampleDto {
		private String name;
		private Date date;

		@Override
		public String toString() {
			return "name:" + name + "\tdate:" + date;
		}

	}

}
