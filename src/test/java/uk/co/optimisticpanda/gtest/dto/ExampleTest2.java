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

import junit.framework.TestCase;
import uk.co.optimisticpanda.gtest.dto.defaultfill.DefaultValueGenerator;
import uk.co.optimisticpanda.gtest.dto.defaultfill.ValueGenerator;
import uk.co.optimisticpanda.gtest.dto.defaultfill.enggen.DtoGenerationEngine;
import uk.co.optimisticpanda.gtest.dto.defaultfill.insgen.InstanceGenerator;
import uk.co.optimisticpanda.gtest.dto.test.utils.TestDto3;

/**
 * @author Andy Lee
 * 
 */
public class ExampleTest2 extends TestCase {

	/**
	 * @throws Exception
	 */
	public void testBasicExampleGeneration() throws Exception {
		//Register the utils context to use ognl for introspection
		TestUtilsContext.useOgnl();
		
		//Build the intance generator (which is responsible for creating new dtos of a specific type).
		InstanceGenerator<TestDto3> generator = InstanceGenerator.create(TestDto3.class);
		
		//Create a generation engine that is responsible for producing the dtos and applying visitors to them.
		DtoGenerationEngine<TestDto3> engine = new DtoGenerationEngine<>(generator);
		
		//Create and return 5 dtos
		List<TestDto3> dtos = engine.collect(5);
		assertEquals(5, dtos.size());
		
		//When no ValueGeneratorCache is specified then a default cache will be used.
		//This provides default values for primitive types, in the case of strings the value "DEFAULT"
		assertEquals("DEFAULT", dtos.get(0).getName());
		assertEquals("DEFAULT", dtos.get(0).getDescription());
	}

	/**
	 * @throws Exception
	 */
	public void testExample() throws Exception {
		//Register the utils context to use ognl for introspection
		TestUtilsContext.useOgnl();
		
		//Create a value generator cache
		ValueGenerator cache = new DefaultValueGenerator(); //
		
		//register the string generator against properties of type string called name.
		//Note you can register generators against different criteria (See ValueGeneratorCache.java) 
		cache.registerAPropertyNameAndTypeGenerator("name", String.class, () -> "DEFAULT_EXAMPLE_NAME");
		
		//Build the generator with the custom cache.
		InstanceGenerator<TestDto3> generator = InstanceGenerator.create(TestDto3.class, cache);
		
		//Create a generation engine that is responsible for producing the dtos and applying visitors to them.
		DtoGenerationEngine<TestDto3> engine = new DtoGenerationEngine<TestDto3>(generator);
		
		//Create and return 1 dto
		List<TestDto3> dtos = engine.collect(1);
		assertEquals(1, dtos.size());
		
		//Name has been set to the non-default value
		assertEquals("DEFAULT_EXAMPLE_NAME", dtos.get(0).getName());
		//Description is still set to the default value
		assertEquals("DEFAULT", dtos.get(0).getDescription());
	}
	
}
