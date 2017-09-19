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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.IMethod;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackChildNode implements ICallStackNode {

    private IMethod method;

    private Collection<CallStackChildNode> children;

    public CallStackChildNode(IMethod method) {
        this.method = method;
        this.children = new ArrayList<>();
    }

    public CallStackChildNode() {
        this.method = null;
        this.children = new ArrayList<>();
    }

    public IMethod getMethod() {
        return method;
    }

    public Collection<CallStackChildNode> getChildren() {
        return children;
    }

    public String toPattern() {
        String packageName = method.getParent().getParent().getParent().getElementName();
        String className = method.getParent().getElementName();
        String methodName = method.getElementName();
        return packageName + "." + className + "#" + methodName;
    }

}
