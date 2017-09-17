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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jdt.core.IMethod;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackResultWriter {

    private String filePath;
    private BufferedWriter writer;

    public CallStackResultWriter(String basePath) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDhhmmss");
        this.filePath = basePath + "\\FindingCaller_" + sdf.format(new Date()) + ".tsv";
    }

    public void write(CallStackRootNode rootNode) throws IOException {

        String currentPath = rootNode.getPackageName() + "." + rootNode.getClassName() + "#" + rootNode.getMethodName();

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.filePath, true), "UTF-8"));

            if (rootNode.getChildlen().size() > 0) {

                for (CallStackChildNode childNode : rootNode.getChildren()) {
                    write(childNode, currentPath);
                }

            } else {
                this.writer.write(currentPath + "\r\n");
            }

        } finally {
            if (writer != null) {
                writer.close();
            } else {
                // 処理なし
            }
            this.writer = null;
        }
    }

    private void write(CallStackChildNode node, String parentText) throws IOException {

        String currentPath = parentText + "\t" + toClassPath(node.getMethod());
        if (node.getChildren().size() > 0) {

            for (CallStackChildNode childNode : node.getChildren()) {
                write(childNode, currentPath);
            }

        } else {
            this.writer.write(currentPath + "\r\n");
        }

    }

    private String toClassPath(IMethod method) {
        return method.getParent().getParent().getParent().getElementName() + "." + method.getParent().getElementName() + "#" + method.getElementName();
    }

    /**
     * filePathを取得します。
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }

}
