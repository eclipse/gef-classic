/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.internal.dot;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.zest.DotUiMessages;

/**
 * This is a Zest Graph new wizard. Its role is to create a new Zest Graph
 * subclass in the provided container. If the container resource (a folder or a
 * project) is selected in the workspace when the wizard is opened, it will
 * accept it as the target container. The wizard creates one file with the
 * extension "java".
 * 
 * @author Fabian Steeg (fsteeg)
 */
public final class ZestGraphWizard extends Wizard implements INewWizard {
	private static final String DOES_NOT_EXIST = DotUiMessages.ZestGraphWizard_0;
	private static final String CREATING = DotUiMessages.ZestGraphWizard_1;
	private static final String ERROR = DotUiMessages.ZestGraphWizard_2;
	private static final String OPENING_FILE = DotUiMessages.ZestGraphWizard_3;
	private static final String PLUGIN_ID = "org.eclipse.zest.dot.ui"; //$NON-NLS-1$
	private static final String RUNNING_FILE = DotUiMessages.ZestGraphWizard_4;
	private ZestGraphWizardPageTemplateSelection templatePage;
	private ZestGraphWizardPageCustomize customizationPage;
	private ISelection selection;
	private static final String DEFAULT_GRAPH_NAME = "SimpleGraph"; //$NON-NLS-1$
	private static final String DEFAULT_DOT_GRAPH = "digraph " //$NON-NLS-1$
			+ DEFAULT_GRAPH_NAME + " {\n\t1; 2; \n\t1->2 \n}"; //$NON-NLS-1$
	private String dotText = DEFAULT_DOT_GRAPH;

	/** Create a new ZestGraphWizard. */
	public ZestGraphWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		customizationPage = new ZestGraphWizardPageCustomize();
		templatePage = new ZestGraphWizardPageTemplateSelection(selection);
		addPage(templatePage);
		addPage(customizationPage);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(final IWorkbench workbench,
			final IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		final String containerName = templatePage.getContainerName();
		final String fileName = templatePage.getFileName();
		IRunnableWithProgress op = createRunnable(containerName, fileName);
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), ERROR,
					realException.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param text
	 *            The DOT text that this wizard should use to generate the Zest
	 *            graph
	 */
	public void setDotText(final String text) {
		this.dotText = text;
	}

	/**
	 * @return The DOT text that this wizard will use to generate the Zest graph
	 */
	public String getDotText() {
		return dotText;
	}

	/**
	 * @param file
	 *            The Zest graph Java source file to run
	 * @param monitor
	 *            The progress monitor
	 */
	static void launchJavaApplication(final IFile file,
			final IProgressMonitor monitor) {
		if (file == null) {
			throw new IllegalArgumentException(
					DotUiMessages.ZestGraphWizard_5);
		}
		monitor.setTaskName(RUNNING_FILE);
		IProject project = file.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = mgr
				.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		try {
			ILaunchConfigurationWorkingCopy copy = type.newInstance(null,
					file.getName());
			/* Un-comment to avoid adding to the launch history: */
			// copy.setAttribute(IDebugUIConstants.ATTR_PRIVATE, true);
			setClassToLaunch(file, copy);
			copy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					javaProject.getProject().getName());
			ILaunchConfiguration configuration = copy.doSave();
			DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		monitor.worked(1);
	}

	private static void setClassToLaunch(final IFile file,
			final ILaunchConfigurationWorkingCopy copy) {
		String location = file.getLocation().toString();
		/*
		 * TODO there must be a cleaner way to get the fully qualified classname
		 * for a Java source file in a Java project...
		 */
		if (!(location.contains("org") && location.contains(".java"))) { //$NON-NLS-1$//$NON-NLS-2$
			return;
		}
		String className = location.substring(location.indexOf("org"), //$NON-NLS-1$
				location.indexOf(".java")).replaceAll("/", "."); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		copy.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
				className);
	}

	private IRunnableWithProgress createRunnable(final String containerName,
			final String fileName) {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(final IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		return op;
	}

	private void doFinish(final String containerName, final String fileName,
			final IProgressMonitor monitor) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(DOES_NOT_EXIST + ": " + containerName); //$NON-NLS-1$
		}
		final IContainer container = (IContainer) resource;
		createFile(container, fileName, monitor);
		refreshContainer(container, monitor);
		IFile file = container.getFile(new Path(fileName));
		openFile(container, file, monitor);
	}

	private void createFile(final IContainer container, final String fileName,
			final IProgressMonitor monitor) {
		monitor.beginTask(CREATING + " " + fileName, 4); //$NON-NLS-1$
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				new DotImport(templatePage.getDotText())
						.newGraphSubclass(container);
			}
		});
		monitor.worked(1);
	}

	private void openFile(final IContainer container, final IFile file,
			final IProgressMonitor monitor) {
		monitor.setTaskName(OPENING_FILE);
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					container.refreshLocal(1, monitor);
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
					launchJavaApplication(file, monitor);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
		monitor.worked(1);
	}

	private void refreshContainer(final IContainer container,
			final IProgressMonitor monitor) {
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					container.refreshLocal(1, monitor);
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		});
		monitor.worked(1);
	}

	private void throwCoreException(final String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK,
				message, null);
		throw new CoreException(status);
	}
}
