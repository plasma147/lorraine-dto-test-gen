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
package uk.co.optimisticpanda.gtest.dto.propertyaccess.rflc;

import uk.co.optimisticpanda.gtest.dto.DTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Andy Lee
 * 
 */
public class AllReflectionPropertyAccessTests {

	/**
	 * @return suite
	 */
	public static Test suite() {
		TestSuite suite = new DTestSuite(AllReflectionPropertyAccessTests.class);
		// $JUnit-BEGIN$
		suite.addTestSuite(ReflectionExpressionTest.class);
		suite.addTestSuite(PrivateFieldHelperTest.class);
		// $JUnit-END$
		return suite;
	}

}