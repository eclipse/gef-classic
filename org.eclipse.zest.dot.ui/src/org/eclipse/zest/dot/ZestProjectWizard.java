/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.dot;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.jdt.internal.ui.wizards.JavaProjectWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.zest.dot.internal.DotFileUtils;

/**
 * Create a Java project, copy some resources and setup the classpath.
 * @author Fabian Steeg (fsteeg)
 */
/* TODO use non-internal wizard and pages */
@SuppressWarnings( "restriction" )
public final class ZestProjectWizard extends JavaProjectWizard {
    /*
     * The name of the generated files depends on the DOT graph names of the sample graphs that are copied
     * from resources/project/templates to the new project.
     */
    private static final String SAMPLE_GRAPH_JAVA = "SampleGraph.java";
    private static final String SAMPLE_ANIMATION_JAVA = "SampleAnimation.java";
    static final String PACKAGE = "org.eclipse.zest.dot";
    static final String SRC_GEN = "src-gen";
    private static final String RESOURCES = "resources/project";
    private static final String TEMPLATES = "templates";

    /**
     * {@inheritDoc}
     * @see org.eclipse.jdt.internal.ui.wizards.NewElementWizard#init(org.eclipse.ui.IWorkbench,
     *      org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection currentSelection) {
        super.setWindowTitle("New Zest Project");
        super.init(workbench, currentSelection);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jdt.internal.ui.wizards.JavaProjectWizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        boolean performFinish = super.performFinish();
        try {
            IJavaElement javaElement = getCreatedElement();
            IPath path = javaElement.getPath();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IResource newProject = root.findMember(path);
            File outRoot = new File(newProject.getLocationURI());
            /*
             * We copy the required resources from this bundle to the new project and setup the project's
             * classpath (which uses the copied resources):
             */
            DotFileUtils.copyAllFiles(resourcesDirectory(), outRoot);
            setupProjectClasspath(javaElement, root, newProject);
            newProject.refreshLocal(IResource.DEPTH_INFINITE, null);
            openDotFiles(javaElement);
            runGeneratedZestGraphs(javaElement);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return performFinish;
    }

    /**
     * @return The project-relative path to the sample Zest graph generated from the sample DOT file copied
     *         into the new project.
     */
    static List<IPath> pathsToGeneratedGraphs() {
        return Arrays.asList(pathTo(SAMPLE_GRAPH_JAVA), pathTo(SAMPLE_ANIMATION_JAVA));
    }

    private static IPath pathTo(final String name) {
        return new Path(ZestProjectWizard.SRC_GEN + "/" + ZestProjectWizard.PACKAGE.replaceAll("\\.", "/")
                + "/" + name);
    }

    private void runGeneratedZestGraphs(final IJavaElement javaElement) throws JavaModelException {
        List<IPath> graphs = pathsToGeneratedGraphs();
        for (IPath graph : graphs) {
            IProject project = (IProject) javaElement.getCorrespondingResource();
            IFile member = (IFile) project.findMember(graph);
            /* We give the builder some time to generate files, etc. */
            long waited = 0;
            long timeout = 10000;
            while ((member == null || !member.exists()) && waited < timeout) {
                try {
                    int millis = 100;
                    Thread.sleep(millis);
                    waited += millis;
                    member = (IFile) project.findMember(graph);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ZestGraphWizard.launchJavaApplication(member, new NullProgressMonitor());
        }
    }

    private void openDotFiles(final IJavaElement javaElement) throws CoreException {
        IProject project = (IProject) javaElement.getCorrespondingResource();
        IFolder templatesFolder = (IFolder) project.findMember(new Path(TEMPLATES));
        IResource[] members = templatesFolder.members();
        for (IResource r : members) {
            IFile file = (IFile) r;
            IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
        }
    }

    private File resourcesDirectory() throws IOException, URISyntaxException {
        URL resourcesFolderUrl =
                FileLocator.find(DotUiActivator.getDefault().getBundle(), new Path(RESOURCES),
                        Collections.EMPTY_MAP);
        URL fileURL = FileLocator.toFileURL(resourcesFolderUrl);
        File resourcesDirectory = new File(fileURL.toURI());
        return resourcesDirectory;
    }

    private void setupProjectClasspath(final IJavaElement javaElement, final IWorkspaceRoot root,
            final IResource newProject) {
        try {
            IClasspathEntry[] classpath = javaElement.getJavaProject().getRawClasspath();
            /*
             * We will add two items to the classpath: a src-gen source folder and the Zest plugin
             * dependencies (to get the required SWT and Zest dependencies into the newly created project).
             */
            IClasspathEntry[] newClasspath = new ClasspathEntry[classpath.length + 2];
            IProject project = (IProject) newProject;
            IFolder sourceGenFolder = project.getFolder(SRC_GEN);
            sourceGenFolder.create(true, true, null);
            createPackage(project, sourceGenFolder);
            /* Copy over the existing classpath entries: */
            for (int i = 0; i < classpath.length; i++) {
                newClasspath[i] = classpath[i];
            }
            newClasspath[newClasspath.length - 2] = JavaCore.newSourceEntry(sourceGenFolder.getFullPath());
            newClasspath[newClasspath.length - 1] =
                    JavaCore.newContainerEntry(new Path("org.eclipse.pde.core.requiredPlugins"));
            /* Set the updated classpath: */
            javaElement.getJavaProject().setRawClasspath(newClasspath, null);
            /* Activate the Zest project nature: */
            ToggleNatureAction.toggleNature(project);
        } catch (JavaModelException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void createPackage(final IProject project, final IFolder sourceGenFolder)
            throws JavaModelException {
        IJavaProject javaProject = JavaCore.create(project);
        IPackageFragmentRoot newPackage = javaProject.getPackageFragmentRoot(sourceGenFolder);
        newPackage.createPackageFragment(PACKAGE, true, new NullProgressMonitor());
    }
}
