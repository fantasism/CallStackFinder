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
import org.fantasism.eclipse.plugin.callstackfinder.core.ClassMethodFinder;
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
    private Text txtOutputClassMethodFile;
    private Text txtInputClassMethodFile;
    private Text txtOutputCallStackFile;

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
            Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
            label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
            toolkit.adapt(label, true, true);
        }
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
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
            Button btnExecute = toolkit.createButton(container, "クラスメソッド抽出", SWT.NONE);
            btnExecute.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {

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

                        ClassMethodFinder searcher = new ClassMethodFinder(txtSearchKeyword.getText(), txtOutputDir.getText());
                        window.run(true, true, searcher);

                        outputFilePath = searcher.getOutputFilePath();

                    } catch (RuntimeException e1) {
                        throw e1;

                    } catch (Exception e1) {
                        throw new RuntimeException(e1);

                    } finally {
                        txtOutputClassMethodFile.setText(outputFilePath);
                    }

                }
            });
            btnExecute.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
        }
        {
            Label lblOutputClassMethodFile = toolkit.createLabel(container, "出力ファイル", SWT.NONE);
            lblOutputClassMethodFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }
        {
            txtOutputClassMethodFile = toolkit.createText(container, "New Text", SWT.READ_ONLY);
            txtOutputClassMethodFile.setEditable(true);
            txtOutputClassMethodFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
            txtOutputClassMethodFile.setText("");
            txtOutputClassMethodFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            Button btnOpenOutputClassMethodFile = toolkit.createButton(container, "開く", SWT.NONE);
            btnOpenOutputClassMethodFile.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {
                    try {
                        Desktop.getDesktop().open(new File(txtOutputClassMethodFile.getText()));
                    } catch (IOException e1) {
                        // TODO 自動生成された catch ブロック
                        e1.printStackTrace();
                    }
                }
            });
            btnOpenOutputClassMethodFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        }
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        {
            Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
            label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
            toolkit.adapt(label, true, true);
        }
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        {
            Label lblInputClassMethodFile = toolkit.createLabel(container, "クラスメソッドファイル", SWT.NONE);
            lblInputClassMethodFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }
        {
            txtInputClassMethodFile = toolkit.createText(container, "New Text", SWT.NONE);
            txtInputClassMethodFile.setText("");
            txtInputClassMethodFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        new Label(container, SWT.NONE);
        {
            Button btnNewButton = toolkit.createButton(container, "呼出階層抽出", SWT.NONE);
            btnNewButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {
                    String outputFilePath = "";

                    if ((new File(txtOutputDir.getText())).isDirectory()) {
                        // 処理なし
                    } else {
                        // TODO エラー
                        return;
                    }

                    if ((new File(txtInputClassMethodFile.getText())).isFile()) {
                        // 処理なし
                    } else {
                        // TODO エラー
                        return;
                    }

                    try {
                        IWorkbench workbench = PlatformUI.getWorkbench();
                        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();

                        CallStackFinder searcher = new CallStackFinder(txtInputClassMethodFile.getText(), txtOutputDir.getText());
                        window.run(true, true, searcher);

                        outputFilePath = searcher.getOutputFilePath();

                    } catch (RuntimeException e1) {
                        throw e1;

                    } catch (Exception e1) {
                        throw new RuntimeException(e1);

                    } finally {
                        txtOutputCallStackFile.setText(outputFilePath);
                    }

                }
            });
            btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
        }
        {
            Label lblNewLabel_1 = toolkit.createLabel(container, "出力ファイル", SWT.NONE);
            lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }
        {
            txtOutputCallStackFile = toolkit.createText(container, "New Text", SWT.READ_ONLY);
            txtOutputCallStackFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
            txtOutputCallStackFile.setEditable(true);
            txtOutputCallStackFile.setText("");
            txtOutputCallStackFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            Button btnOpenOutputCallStackFile = toolkit.createButton(container, "開く", SWT.NONE);
            btnOpenOutputCallStackFile.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseUp(MouseEvent e) {
                    try {
                        Desktop.getDesktop().open(new File(txtOutputCallStackFile.getText()));
                    } catch (IOException e1) {
                        // TODO 自動生成された catch ブロック
                        e1.printStackTrace();
                    }
                }
            });
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
