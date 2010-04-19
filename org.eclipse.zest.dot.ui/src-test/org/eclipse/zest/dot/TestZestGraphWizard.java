/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link ZestGraphWizard}. Tests if the no-config usage of the
 * wizard works, ie. start the wizard on an existing container, and hit finish.
 * @author Fabian Steeg (fsteeg)
 */
public final class TestZestGraphWizard {

    private IProject project;

    @Before
    public void setup() {
        ProjectHelper.assertProjectDoesntExist(ProjectHelper.PROJECT_PATH);
        project = ProjectHelper.createProject(ProjectHelper.PROJECT_NAME);

    }

    @After
    public void cleanup() {
        ProjectHelper.deleteProject(project);
    }

    @Test
    public void zestGraphCreation() {
        /* Run the wizard and return the label of the generated graph: */
        String graphLabel = runWizard();
        String sourceFileName = graphLabel.replaceAll(" ", "") + ".java";
        IPath zestGraphPath = new Path(ProjectHelper.PROJECT_PATH.toString()
                + "/" + sourceFileName);
        /* Assert the Zest graph file was both generated and opened: */
        testGraphExists(zestGraphPath);
        testGraphOpenInEditor(zestGraphPath);
    }

    private void testGraphOpenInEditor(final IPath zestGraphPath) {
        FileEditorInput fileInput = (FileEditorInput) PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor()
                .getEditorInput();
        IPath editorInputPath = fileInput.getFile().getProjectRelativePath();
        Assert
                .assertTrue(
                        "Editor input of the active editor should be the generated file",
                        zestGraphPath.toString().endsWith(
                                editorInputPath.toString()));
    }

    private void testGraphExists(final IPath zestGraphPath) {
        Assert.assertTrue("Generated Java file must exist: "
                + zestGraphPath.toFile(), zestGraphPath.toFile().exists());
    }

    private String runWizard() {
        ZestGraphWizard wizard = new ZestGraphWizard();
        IWorkbench workbench = PlatformUI.getWorkbench();
        wizard.init(workbench, (IStructuredSelection) workbench
                .getActiveWorkbenchWindow().getSelectionService()
                .getSelection());
        WizardDialog dialog = createDialog(wizard, workbench);
        ZestGraphWizardPageTemplateSelection page = (ZestGraphWizardPageTemplateSelection) wizard
                .getPages()[0];
        page.setContainerText(ProjectHelper.PROJECT_NAME);
        dialog.setBlockOnOpen(false);
        dialog.open();
        wizard.performFinish();
        String label = page.getGraphName();
        return label;
    }

    private WizardDialog createDialog(final ZestGraphWizard wizard,
            final IWorkbench workbench) {
        WizardDialog dialog = new WizardDialog(workbench
                .getActiveWorkbenchWindow().getShell(), wizard);
        dialog.create();
        return dialog;
    }
}
