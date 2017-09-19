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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.fantasism.eclipse.plugin.callstackfinder.Activator;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackSearchRequestor extends SearchRequestor {

    private ICallStackNode node;

    private Set<ICallStackNode> callStackSet;

    public CallStackSearchRequestor(ICallStackNode node) {
        this.node = node;
        this.callStackSet = new HashSet<ICallStackNode>();
    }

    @Override
    public void acceptSearchMatch(SearchMatch match) throws CoreException {
        Object element = match.getElement();

        if (element instanceof IMethod) {
            CallStackChildNode childNode = new CallStackChildNode((IMethod) element);

            if (callStackSet.contains(childNode)) {
                Activator.getConsoleStream().println("CALL STACK FINDER:[SKIP(RECURSIVE)]" + element);
            } else {
                Activator.getConsoleStream().println("CALL STACK FINDER:[OK]" + element);
                node.getChildren().add(childNode);
            }
        } else {
            Activator.getConsoleStream().println("CALL STACK FINDER:[SKIP(NOT METHOD)]" + element);
        }
    }

}
