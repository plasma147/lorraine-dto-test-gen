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

import junit.framework.Test;
import junit.framework.TestSuite;
import uk.co.optimisticpanda.gtest.dto.DTestSuite;

/**
 * @author Andy Lee
 *
 */
public class AllDefaultFillTests {

    /**
     * @return suite
     */
    public static Test suite() {
        TestSuite suite = new DTestSuite(AllDefaultFillTests.class);
        //$JUnit-BEGIN$
        suite.addTestSuite(ValueGeneratorCacheTest.class);
        suite.addTestSuite(InstanceGeneratorBuilderTest.class);
        //$JUnit-END$
        return suite;
    }

}