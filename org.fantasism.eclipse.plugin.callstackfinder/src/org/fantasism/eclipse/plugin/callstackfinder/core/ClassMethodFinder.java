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

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.fantasism.eclipse.plugin.callstackfinder.Activator;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class ClassMethodFinder implements IRunnableWithProgress {

    public static final int DEFAULT_MONITOR_TICKS = 100;

    private String outputFilePath;

    private String searchKeyword;
    private String outputDirPath;

    private IJavaSearchScope scope;
    private SearchEngine engine;
    private SearchParticipant[] participants;
    private int monitorTicks;

    public ClassMethodFinder(String searchKeyword, String outputDirPath) {
        this.searchKeyword = searchKeyword;
        this.outputDirPath = outputDirPath;
        this.outputFilePath = "";

        this.scope = SearchEngine.createWorkspaceScope();
        this.engine = new SearchEngine();

        this.participants = new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()};
        this.monitorTicks = DEFAULT_MONITOR_TICKS;
    }

    /* (非 Javadoc)
     * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.beginTask("FINDING CLASS METHOD...", this.monitorTicks);

        try {
            ClassMethodResultWriter writer = new ClassMethodResultWriter(this.outputDirPath);

            try {
                Activator.getConsoleStream().println("[BEGIN]CLASS METHOD FINDER");

                // キーワードに該当するクラスメソッドを検索する
                List<ClassMethodSearchResult> infos = searchClassMethod(monitor);

                writer.write(infos);

                Activator.getConsoleStream().println("[END  ]CLASS METHOD FINDER");

            } catch (Exception e) {
                e.printStackTrace(new PrintStream(Activator.getConsoleStream()));

            }

            this.outputFilePath = writer.getFilePath();

        } finally {
            monitor.done();
        }
    }

    private List<ClassMethodSearchResult> searchClassMethod(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException, CoreException {

        List<ClassMethodSearchResult> infos = new ArrayList<ClassMethodSearchResult>();

        SearchPattern pattern = SearchPattern.createPattern(this.searchKeyword, IJavaSearchConstants.METHOD,
                IJavaSearchConstants.DECLARATIONS, SearchPattern.R_PATTERN_MATCH|SearchPattern.R_CASE_SENSITIVE );

        IProgressMonitor subMonitor = new SubProgressMonitor(monitor, this.monitorTicks);
        try {
            ClassMethodSearchRequestor requestor = new ClassMethodSearchRequestor(infos);

            engine.search(pattern, this.participants, this.scope, requestor, subMonitor);

        } finally {
            subMonitor.done();
        }

        return infos;

    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

}
