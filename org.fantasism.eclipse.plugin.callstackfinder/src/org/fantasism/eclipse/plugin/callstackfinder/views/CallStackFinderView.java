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

package org.fantasism.eclipse.plugin.callstackfinder.views;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.fantasism.eclipse.plugin.callstackfinder.Activator;
import org.fantasism.eclipse.plugin.callstackfinder.core.CallStackFinder;
import org.fantasism.eclipse.plugin.callstackfinder.preferences.PreferenceConstants;

/**
 * TODO クラスの概要
 * <p>
 * TODO クラスの説明
 * </p>
 * @author Takahide Ohsuka, FANTASISM.
 */
public class CallStackFinderView extends ViewPart {

    public static final String ID = "org.fantasism.eclipse.plugin.findingcaller.views.ExecuteFindingCallerView"; //$NON-NLS-1$
    private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
    private Text txtSearchKeyword;
    private Text txtOutputDir;
    private Text txtOutputFile;
    private Text txtExecResult;

    public CallStackFinderView() {
    }

    /**
     * Create contents of the view part.
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        Composite container = toolkit.createComposite(parent, SWT.NONE);
        toolkit.paintBordersFor(container);
        container.setLayout(new GridLayout(3, false));
        {
            Label lblSearchKeyword = toolkit.createLabel(container, "キーワード", SWT.NONE);
            lblSearchKeyword.setToolTipText("クラス・メソッドを表すキーワードを指定してください。\r\n例）org.fantasism.*.*Dao.count*");
            lblSearchKeyword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }
        {
            txtSearchKeyword = toolkit.createText(container, "New Text", SWT.NONE);
            txtSearchKeyword.setText("");
            txtSearchKeyword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        new Label(container, SWT.NONE);
        {
            Label lblOutputDir = toolkit.createLabel(container, "出力先ディレクトリ", SWT.NONE);
            lblOutputDir.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }
        {
            txtOutputDir = toolkit.createText(container, "New Text", SWT.NONE);
            txtOutputDir.setText(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_DEFAULT_OUTPUT_DIR));
            txtOutputDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            Button btnChoiceOutputDir = toolkit.createButton(container, "選択", SWT.NONE);
            btnChoiceOutputDir.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {
                    DirectoryDialog dialog = new DirectoryDialog(txtOutputDir.getShell(), SWT.OPEN);
                    String choice = dialog.open();

                    if (choice != null) {
                        txtOutputDir.setText(choice);
                    } else {
                        // 処理なし
                    }
                }
            });
            btnChoiceOutputDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        }
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        {
            Button btnExecute = toolkit.createButton(container, "実行", SWT.NONE);
            btnExecute.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {
                    txtExecResult.setText(new Date() + " 実行を開始しました。\r\n");

                    String outputFilePath = "";

                    if ((new File(txtOutputDir.getText())).isDirectory()) {
                        // 処理なし
                    } else {
                        // TODO エラー
                        return;
                    }

                    try {
                        IWorkbench workbench = PlatformUI.getWorkbench();
                        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

                        CallStackFinder searcher = new CallStackFinder(txtSearchKeyword.getText(), txtOutputDir.getText());
                        window.run(true, true, searcher);

                        outputFilePath = searcher.getOutputFilePath();

                        txtExecResult.setText(txtExecResult.getText() + new Date() + " 実行が完了しました。\r\n");

                    } catch (InterruptedException e1) {
                        txtExecResult.setText(txtExecResult.getText() + new Date() + " 実行をキャンセルしました。\r\n");

                    } catch (Exception e1) {
                        txtExecResult.setText(txtExecResult.getText() + new Date() + " 実行中に予期せぬエラーが発生し、処理を中断しました。\r\n");
                        e1.printStackTrace();

                    } finally {
                        txtOutputFile.setText(outputFilePath);
                    }
                }
            });
            btnExecute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        }
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        {
            Label lblOutputFile = toolkit.createLabel(container, "出力ファイル", SWT.NONE);
            lblOutputFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }
        {
            txtOutputFile = toolkit.createText(container, "New Text", SWT.NONE);
            txtOutputFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
            txtOutputFile.setText("");
            txtOutputFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            Button btnOpenOutputFile = toolkit.createButton(container, "開く", SWT.NONE);
            btnOpenOutputFile.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {
                    try {
                        Desktop.getDesktop().open(new File(txtOutputFile.getText()));
                    } catch (IOException e1) {
                        // TODO 自動生成された catch ブロック
                        e1.printStackTrace();
                    }
                }
            });
            btnOpenOutputFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        }
        {
            Label lblExecResult = toolkit.createLabel(container, "実行結果", SWT.NONE);
            lblExecResult.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }
        {
            txtExecResult = toolkit.createText(container, "New Text", SWT.MULTI);
            txtExecResult.setText("");
            txtExecResult.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
            txtExecResult.setEditable(false);
            txtExecResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        }

        createActions();
        initializeToolBar();
        initializeMenu();
    }

    public void dispose() {
        toolkit.dispose();
        super.dispose();
    }

    /**
     * Create the actions.
     */
    private void createActions() {
        // Create the actions
    }

    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {
        IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
    }

    /**
     * Initialize the menu.
     */
    private void initializeMenu() {
        IMenuManager manager = getViewSite().getActionBars().getMenuManager();
    }

    @Override
    public void setFocus() {
        // Set the focus
    }

}
