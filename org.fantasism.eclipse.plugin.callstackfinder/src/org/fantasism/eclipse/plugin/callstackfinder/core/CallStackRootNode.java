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

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackRootNode implements ICallStackNode {

    private String packageName;

    private String className;

    private String methodName;

    private Collection<CallStackChildNode> childlen;

    public CallStackRootNode(String packageName, String className, String methodName) {
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.childlen = new ArrayList<CallStackChildNode>();
    }

    /* (非 Javadoc)
     * @see org.fantasism.eclipse.plugin.findcaller.core.IFindingCallerNode#getChildren()
     */
    @Override
    public Collection<CallStackChildNode> getChildren() {
        return this.childlen;
    }

    /**
     * packageNameを取得します。
     * @return packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * classNameを取得します。
     * @return className
     */
    public String getClassName() {
        return className;
    }

    /**
     * methodNameを取得します。
     * @return methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * childlenを取得します。
     * @return childlen
     */
    public Collection<CallStackChildNode> getChildlen() {
        return childlen;
    }

    public String toPattern() {
        return this.packageName + "." + this.className + "." + this.methodName;
    }

}
