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

import uk.co.optimisticpanda.gtest.dto.edit.CombinedEditor;
import uk.co.optimisticpanda.gtest.dto.edit.Editor;

class EditBuilder<D> {

	private final CombinedEditor<D> edit;

	public EditBuilder(Editor<D> edit ) {
		if(edit == null){
			throw new IllegalArgumentException("Edit must not be null");
		}
		this.edit = new CombinedEditor<D>(edit);
	}

	public void and(Editor<D> andEdit){
		this.edit.addEdit(andEdit);
	}

	public Editor<D> build(){
		return this.edit;
	}
}
