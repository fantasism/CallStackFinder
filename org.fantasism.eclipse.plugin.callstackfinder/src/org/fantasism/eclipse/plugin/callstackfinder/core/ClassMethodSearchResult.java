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

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class ClassMethodSearchResult {

    /** パッケージ名 */
    private String packageName;

    /** クラス名 */
    private String className;

    /** メソッド名 */
    private String methodName;

    /**
     * パッケージ名を取得します。
     * @return パッケージ名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * パッケージ名を設定します。
     * @param packageName パッケージ名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * クラス名を取得します。
     * @return クラス名
     */
    public String getClassName() {
        return className;
    }

    /**
     * クラス名を設定します。
     * @param className クラス名
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * メソッド名を取得します。
     * @return メソッド名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * メソッド名を設定します。
     * @param methodName メソッド名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String toString() {
        return this.packageName + "." + this.className + "#" + this.methodName;
    }

}
