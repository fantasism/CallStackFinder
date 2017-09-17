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

package org.fantasism.eclipse.plugin.callstackfinder.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackSearchRequestor extends SearchRequestor {

    private ICallStackNode node;

    public CallStackSearchRequestor(ICallStackNode node) {
        this.node = node;
    }

    @Override
    public void acceptSearchMatch(SearchMatch match) throws CoreException {
        Object element = match.getElement();
        if (element instanceof IMethod && element instanceof IJavaElement)
        {
            CallStackChildNode childNode = new CallStackChildNode((IMethod) element);

            node.getChildren().add(childNode);
        }
    }

}
