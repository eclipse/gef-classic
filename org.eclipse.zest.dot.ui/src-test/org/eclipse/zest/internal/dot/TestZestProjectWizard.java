/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.internal.dot;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.internal.dot.ZestNature;
import org.eclipse.zest.internal.dot.ZestProjectWizard;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link ZestProjectWizard}. Tests if the no-config usage of the wizard works, ie.
 * start the and hit finish.
 * @author Fabian Steeg (fsteeg)
 */
public final class TestZestProjectWizard {
    private IProject project;

    @Before
    public void setup() {
        ProjectHelper.assertProjectDoesntExist(ProjectHelper.PROJECT_PATH);
    }

    @After
    public void cleanup() {
        ProjectHelper.deleteProject(project);
    }

    @Test
    public void zestGraphCreation() {
        /* Run the wizard and return the name of the new project: */
        runWizard();
        /* Then we test the wizard results: */
        testProjectCreation();
        testBuilder();
        testProjectNature();
    }

    private void testBuilder() {
        List<IPath> paths = ZestProjectWizard.pathsToGeneratedGraphs();
        for (IPath path : paths) {
            IResource generatedZestFile = project.findMember(path);
            String shouldExist = "Zest graph created by project builder should exist: " + path;
            Assert.assertNotNull(shouldExist, generatedZestFile);
            Assert.assertTrue(shouldExist, generatedZestFile.exists());
        }
    }

    private void testProjectCreation() {
        String shouldExist = "Created project should exist";
        Assert.assertNotNull(shouldExist, project);
        Assert.assertTrue(shouldExist, project.exists());
    }

    private void testProjectNature() {
        try {
            IProjectNature nature = project.getNature(ZestNature.NATURE_ID);
            Assert.assertNotNull("Zest nature should be present on new Zest project", nature);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void runWizard() {
        ZestProjectWizard wizard = new ZestProjectWizard();
        WizardDialog dialog = createDialog(wizard);
        dialog.create();
        dialog.setBlockOnOpen(false);
        dialog.open();
        wizard.performFinish();
        project = getNewProject(wizard);
    }

    private IProject getNewProject(ZestProjectWizard wizard) {
        IJavaElement createdElement = wizard.getCreatedProject();
        try {
            return createdElement.getCorrespondingResource().getProject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private WizardDialog createDialog(final ZestProjectWizard wizard) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        wizard.init(workbench, null);
        WizardDialog dialog = new WizardDialog(workbench.getActiveWorkbenchWindow().getShell(),
                wizard);
        return dialog;
    }
}
