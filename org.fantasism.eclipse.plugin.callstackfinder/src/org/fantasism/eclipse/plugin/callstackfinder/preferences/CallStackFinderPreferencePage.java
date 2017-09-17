/*
 * Copyright (C) 2017 FANTASISM All Rights Reserved.
 *
 * Licensed to CallStackFinder under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. CallStackFinder licenses this file to you
 * under the Eclipse Public License, Version 1.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.fantasism.eclipse.plugin.callstackfinder.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.fantasism.eclipse.plugin.callstackfinder.Activator;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackFinderPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

    public CallStackFinderPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	public void createFieldEditors() {
	    {
	        DirectoryFieldEditor directoryFieldEditor = new FindingCallerDirectoryFieldEditor(PreferenceConstants.P_DEFAULT_OUTPUT_DIR, "デフォルトの出力先ディレクトリ", getFieldEditorParent());
	        addField(directoryFieldEditor);
	    }
	}

	public class FindingCallerDirectoryFieldEditor extends DirectoryFieldEditor {

	    public FindingCallerDirectoryFieldEditor(String name, String labelText, Composite parent) {
	        super(name, labelText, parent);
	    }

	    protected void createControl(Composite parent) {
            setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
	        super.createControl(parent);
	    }

	}

	public void init(IWorkbench workbench) {
	}

}