/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.internal.dot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.zest.DotUiMessages;
import org.osgi.framework.Bundle;

/**
 * Create a Java project, copy some resources and setup the classpath.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public final class ZestProjectWizard extends Wizard implements
		IExecutableExtension, INewWizard {
	/*
	 * The name of the generated files depends on the DOT graph names of the
	 * sample graphs that are copied from resources/project/templates to the new
	 * project.
	 */
	private static final String SAMPLE_GRAPH_JAVA = "SampleGraph.java"; //$NON-NLS-1$
	private static final String SAMPLE_ANIMATION_JAVA = "SampleAnimation.java"; //$NON-NLS-1$
	static final String PACKAGE = "org.eclipse.zest.dot"; //$NON-NLS-1$
	static final String SRC_GEN = "src-gen"; //$NON-NLS-1$
	private static final String RESOURCES = "resources/project"; //$NON-NLS-1$
	private static final String TEMPLATES = "templates"; //$NON-NLS-1$

	private WizardNewProjectCreationPage mainPage;
	private JavaCapabilityConfigurationPage javaPage;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement,
	 *      java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(final IConfigurationElement cfig,
			final String propertyName, final Object data) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(final IWorkbench workbench,
			final IStructuredSelection currentSelection) {
		super.setWindowTitle(DotUiMessages.ZestProjectWizard_0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		mainPage = new WizardNewProjectCreationPage(DotUiMessages.ZestProjectWizard_1);
		mainPage.setTitle(DotUiMessages.ZestProjectWizard_2);
		mainPage.setDescription(DotUiMessages.ZestProjectWizard_3);
		mainPage.setInitialProjectName(DotUiMessages.ZestProjectWizard_4);
		addPage(mainPage);
		javaPage = new JavaCapabilityConfigurationPage() {
			public void setVisible(final boolean visible) {
				updatePage();
				super.setVisible(visible);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		try {
			/*
			 * We first show the graph view to see the Zest representation of
			 * the new DOT file:
			 */
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().showView(ZestGraphView.ID);
			/*
			 * Set up the runnable that calls the project creation code
			 * asynchronously:
			 */
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(final IProgressMonitor monitor) {
					getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							setupProject();
						}
					});
				}
			};
			/* Run the runnable on the wizard container: */
			getContainer().run(true, true, runnable);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void setupProject() {
		try {
			createSimpleJavaProject();
			IJavaElement javaElement = javaPage.getJavaProject();
			IPath path = javaElement.getPath();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource newProject = root.findMember(path);
			File outRoot = new File(newProject.getLocationURI());
			/*
			 * We copy the required resources from this bundle to the new
			 * project and setup the project's classpath (which uses the copied
			 * resources):
			 */
			DotFileUtils.copyAllFiles(resourcesDirectory(), outRoot);
			newProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			setupProjectClasspath(javaElement, root, newProject);
			runGeneratedZestGraphs(javaElement);
			openDotFiles(javaElement);
		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The project-relative path to the sample Zest graph generated from
	 *         the sample DOT file copied into the new project.
	 */
	static List<IPath> pathsToGeneratedGraphs() {
		return Arrays.asList(pathTo(SAMPLE_GRAPH_JAVA),
				pathTo(SAMPLE_ANIMATION_JAVA));
	}

	/**
	 * @return The Java project creted by this wizard
	 */
	IJavaElement getCreatedProject() {
		return javaPage.getJavaProject();
	}

	private void createSimpleJavaProject() throws InterruptedException,
			CoreException, InvocationTargetException {
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InvocationTargetException,
					InterruptedException {
				monitor = monitor == null ? new NullProgressMonitor() : monitor;
				monitor.beginTask(DotUiMessages.ZestProjectWizard_5, 3);
				IProject project = mainPage.getProjectHandle();
				IPath locationPath = mainPage.getLocationPath();
				IProjectDescription desc = create(project, locationPath);
				project.create(desc, new SubProgressMonitor(monitor, 1));
				project.open(new SubProgressMonitor(monitor, 1));
				updatePage();
				javaPage.configureJavaProject(new SubProgressMonitor(monitor, 1));
			}
		};
		try {
			getContainer().run(false, true, op);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private IProjectDescription create(final IProject project,
			final IPath locationPath) {
		IProjectDescription desc = project.getWorkspace()
				.newProjectDescription(project.getName());
		if (!mainPage.useDefaults()) {
			desc.setLocation(locationPath);
		}
		return desc;
	}

	private void updatePage() {
		IJavaProject jproject = JavaCore.create(mainPage.getProjectHandle());
		if (!jproject.equals(javaPage.getJavaProject())) {
			IClasspathEntry[] buildPath = {
					JavaCore.newSourceEntry(jproject.getPath().append("src")), //$NON-NLS-1$
					JavaRuntime.getDefaultJREContainerEntry() };
			IPath outputLocation = jproject.getPath().append("out"); //$NON-NLS-1$
			javaPage.init(jproject, outputLocation, buildPath, false);
		}
	}

	private static IPath pathTo(final String name) {
		return new Path(ZestProjectWizard.SRC_GEN + "/" //$NON-NLS-1$
				+ ZestProjectWizard.PACKAGE.replaceAll("\\.", "/") + "/" + name); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	private void runGeneratedZestGraphs(final IJavaElement javaElement)
			throws JavaModelException {
		List<IPath> graphs = pathsToGeneratedGraphs();
		for (IPath graph : graphs) {
			IProject project = (IProject) javaElement
					.getCorrespondingResource();
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
			ZestGraphWizard.launchJavaApplication(member,
					new NullProgressMonitor());
		}
	}

	private void openDotFiles(final IJavaElement javaElement)
			throws CoreException {
		IProject project = (IProject) javaElement.getCorrespondingResource();
		IFolder templatesFolder = (IFolder) project.findMember(new Path(
				TEMPLATES));
		IResource[] members = templatesFolder.members();
		for (IResource r : members) {
			IFile file = (IFile) r;
			/*
			 * The external editor for *.dot files causes trouble in the
			 * majority of cases (Microsoft Office, Open Office, NeoOffice
			 * etc.), so we only open the new files if an editor is set up in
			 * Eclipse:
			 */
			if (IDE.getDefaultEditor(file) != null) {
				IDE.openEditor(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage(), file);
			} else {
				return;
			}
		}
	}

	private File resourcesDirectory() throws IOException, URISyntaxException {
		Bundle bundle = DotUiActivator.getDefault().getBundle();
		URL resourcesFolderUrl = FileLocator.find(bundle, new Path(RESOURCES),
				Collections.EMPTY_MAP);
		if (resourcesFolderUrl == null) {
			throw new IllegalStateException(String.format(
					DotUiMessages.ZestProjectWizard_6, RESOURCES, bundle));
		}
		URL fileURL = FileLocator.toFileURL(resourcesFolderUrl);
		if (fileURL.toString().equals(resourcesFolderUrl.toString())) {
			throw new IllegalStateException(DotUiMessages.ZestProjectWizard_7 + ": " + fileURL); //$NON-NLS-2$
		}
		File resourcesDirectory = new File(fileURL.toURI());
		return resourcesDirectory;
	}

	private void setupProjectClasspath(final IJavaElement javaElement,
			final IWorkspaceRoot root, final IResource newProject) {
		try {
			IClasspathEntry[] classpath = javaElement.getJavaProject()
					.getRawClasspath();
			/*
			 * We will add two items to the classpath: a src-gen source folder
			 * and the Zest plugin dependencies (to get the required SWT and
			 * Zest dependencies into the newly created project).
			 */
			IClasspathEntry[] newClasspath = new IClasspathEntry[classpath.length + 3];
			IProject project = (IProject) newProject;
			IFolder sourceGenFolder = project.getFolder(SRC_GEN);
			sourceGenFolder.create(true, true, null);
			createPackage(project, sourceGenFolder);
			/* Copy over the existing classpath entries: */
			for (int i = 0; i < classpath.length; i++) {
				newClasspath[i] = classpath[i];
			}
			IFolder templatesFolder = (IFolder) project.findMember(new Path(
					TEMPLATES));
			newClasspath[newClasspath.length - 3] = JavaCore
					.newSourceEntry(templatesFolder.getFullPath());
			newClasspath[newClasspath.length - 2] = JavaCore
					.newSourceEntry(sourceGenFolder.getFullPath());
			newClasspath[newClasspath.length - 1] = JavaCore
					.newContainerEntry(new Path(
							"org.eclipse.pde.core.requiredPlugins")); //$NON-NLS-1$
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

	private void createPackage(final IProject project,
			final IFolder sourceGenFolder) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragmentRoot newPackage = javaProject
				.getPackageFragmentRoot(sourceGenFolder);
		newPackage.createPackageFragment(PACKAGE, true,
				new NullProgressMonitor());
	}

}
