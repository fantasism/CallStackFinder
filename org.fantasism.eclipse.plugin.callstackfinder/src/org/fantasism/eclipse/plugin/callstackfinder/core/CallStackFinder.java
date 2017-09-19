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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class CallStackFinder implements IRunnableWithProgress {

    public static final int DEFAULT_MONITOR_TICKS = 100;

    private String outputFilePath;

    private String searchKeyword;
    private String outputDirPath;

    private IJavaSearchScope scope;
    private SearchEngine engine;
    private SearchParticipant[] participants;
    private int monitorTicks;

    public CallStackFinder(String searchKeyword, String outputDirPath) {
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
        monitor.beginTask("CallStackFinder...", this.monitorTicks);

        try {
            CallStackResultWriter writer = new CallStackResultWriter(outputDirPath);

            BufferedReader br = null;
            try {
                Activator.getConsoleStream().println("CLASS METHOD FINDER:[BEGIN]");

                // キーワードに該当するクラスメソッドを検索する
                List<ClassMethodSearchResult> infos = searchClassMethod(monitor);

                Activator.getConsoleStream().println("CLASS METHOD FINDER:[END]");

                // 該当したクラスメソッドの呼出階層を検索する
                for (ClassMethodSearchResult classMethod : infos) {
                    try {
                        Activator.getConsoleStream().println("CALL STACK FINDER:[BEGIN]" + classMethod);
                        CallStackRootNode rootNode = searchCallStackRoot(monitor, classMethod);

                        writer.write(rootNode);
                        Activator.getConsoleStream().println("CALL STACK FINDER:[END]" + classMethod);

                    } catch (CoreException e) {
                        Activator.getConsoleStream().println("CALL STACK FINDER:[ERROR]" + classMethod);
                        e.printStackTrace(new PrintStream(Activator.getConsoleStream()));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace(new PrintStream(Activator.getConsoleStream()));

            } finally {
                if (br != null) {
                    br.close();
                } else {
                    // 処理なし
                }
            }

            this.outputFilePath = writer.getFilePath();

        } catch (FileNotFoundException e) {
            throw new InvocationTargetException(e);

        } catch (IOException e) {
            throw new InvocationTargetException(e);

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

    private CallStackRootNode searchCallStackRoot(IProgressMonitor monitor, ClassMethodSearchResult classMethos) throws InvocationTargetException, InterruptedException, CoreException {

        CallStackRootNode rootNode = new CallStackRootNode(classMethos.getPackageName(), classMethos.getClassName(), classMethos.getMethodName());

        SearchPattern pattern = SearchPattern.createPattern(rootNode.toPattern(), IJavaSearchConstants.METHOD,
                IJavaSearchConstants.REFERENCES | IJavaSearchConstants.RETURN_TYPE_REFERENCE, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE | SearchPattern.R_ERASURE_MATCH);

        search(monitor, pattern, rootNode);

        for (CallStackChildNode childNode : rootNode.getChildren()) {
            searchCallStackChild(monitor, childNode);
        }

        return rootNode;

    }

    private void searchCallStackChild(IProgressMonitor monitor, CallStackChildNode node) throws InvocationTargetException, InterruptedException, CoreException {

        SearchPattern pattern = SearchPattern.createPattern(node.getMethod(), IJavaSearchConstants.REFERENCES| IJavaSearchConstants.RETURN_TYPE_REFERENCE, SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE | SearchPattern.R_ERASURE_MATCH);

        search(monitor, pattern, node);

        for (CallStackChildNode childNode : node.getChildren()) {
            searchCallStackChild(monitor, childNode);
        }

    }

    private void search(IProgressMonitor monitor, SearchPattern pattern, ICallStackNode node) throws CoreException {

        IProgressMonitor subMonitor = new SubProgressMonitor(monitor, this.monitorTicks);
        try {
            CallStackSearchRequestor requestor = new CallStackSearchRequestor(node);

            engine.search(pattern, this.participants, this.scope, requestor, subMonitor);

        } finally {
            subMonitor.done();
        }

    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

}
