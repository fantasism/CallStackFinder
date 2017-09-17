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

import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackFinderParameter {

    private IRunnableContext context;

    private IJavaSearchScope scope;

    /**
     * コンストラクタ
     * <p>
     * コンストラクタの説明
     * </p>
     * @param pattern
     * @param requestor
     */
    public CallStackFinderParameter(IRunnableContext context, IJavaSearchScope scope) {
        this.context = context;
        this.scope = scope;
    }

    /**
     * contextを取得します。
     * @return context
     */
    public IRunnableContext getContext() {
        return context;
    }

    /**
     * scopeを取得します。
     * @return scope
     */
    public IJavaSearchScope getScope() {
        return scope;
    }

}
