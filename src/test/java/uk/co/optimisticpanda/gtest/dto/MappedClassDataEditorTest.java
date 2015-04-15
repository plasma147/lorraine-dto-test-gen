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

import java.util.Arrays;

import uk.co.optimisticpanda.gtest.dto.MappedClassDataEditor;
import uk.co.optimisticpanda.gtest.dto.condition.AlwaysCondition;
import uk.co.optimisticpanda.gtest.dto.edit.IEdit;
import uk.co.optimisticpanda.gtest.dto.edit.SetValueEdit;
import uk.co.optimisticpanda.gtest.dto.rule.BaseRule;
import uk.co.optimisticpanda.gtest.dto.rule.IRule;
import uk.co.optimisticpanda.gtest.dto.test.utils.TestDto1;
import uk.co.optimisticpanda.gtest.dto.test.utils.TestDto2;


import junit.framework.TestCase;

/**
 * @author Andy Lee
 *
 */
public class MappedClassDataEditorTest extends TestCase{

    /**
     * 
     */
    public static final String CHANGE_FOR_DTO1 = "DTO1";
    /**
     * 
     */
    public static final String CHANGE_FOR_DTO2 = "DTO2";
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestUtilsContext.useOgnl();
    }

    /**
     * 
     */
    public void testMappedClassDataEditor(){
        IEdit<TestDto1> editForDto1 = new SetValueEdit<TestDto1>("name",CHANGE_FOR_DTO1);
        IEdit<TestDto2> editForDto2 = new SetValueEdit<TestDto2>("name",CHANGE_FOR_DTO2);
        
        IRule<TestDto1> rule1 = new BaseRule<TestDto1>(editForDto1, AlwaysCondition.INSTANCE);
        IRule<TestDto2> rule2 = new BaseRule<TestDto2>(editForDto2, AlwaysCondition.INSTANCE);

        MappedClassDataEditor editor = new MappedClassDataEditor();
        editor.addRuleForClass(TestDto1.class ,rule1);
        editor.addRuleForClass(TestDto2.class ,rule2);
        
        TestDto1 dto1 = new TestDto1("name");
        TestDto2 dto2 = new TestDto2("name", "description");
        
        editor.edit(Arrays.asList(dto1, dto2));
        
        assertEquals(CHANGE_FOR_DTO1, dto1.getName());
        assertEquals(CHANGE_FOR_DTO2, dto2.getName());

    }
   
    
}