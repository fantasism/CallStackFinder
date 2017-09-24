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
import java.util.List;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class ClassMethodResultWriter {

    private String filePath;
    private BufferedWriter writer;

    public ClassMethodResultWriter(String basePath) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDhhmmss");
        this.filePath = basePath + "\\ClassMethodList_" + sdf.format(new Date()) + ".tsv";
    }

    public void write(List<ClassMethodSearchResult> searchResultList) throws IOException {

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.filePath, true), "UTF-8"));

            for (ClassMethodSearchResult searchResult : searchResultList) {
                this.writer.write(searchResult.toString() + "\r\n");
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

    /**
     * filePathを取得します。
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }

}
