/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.wizard;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.eclipse.gef.examples.ediagram.EDiagramPlugin;
import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.ModelFactory;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "ediagram". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewEDiagramWizard extends Wizard implements INewWizard {
	private NewEDiagramFilePage filePage;
	private SelectEcoreFilesPage ecorePage;
	private ISelection selection;

	/**
	 * Constructor for NewEDiagramWizard.
	 */
	public NewEDiagramWizard() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle("New EDiagram Wizard");
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		addPage(filePage = new NewEDiagramFilePage(selection));
		addPage(ecorePage = new SelectEcoreFilesPage("EDiagram Wizard"));
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String containerName = filePage.getContainerName();
		final String fileName = filePage.getFileName();
		final String[] ecoreFiles = ecorePage.getECoreFileNames();
		IRunnableWithProgress op = new WorkspaceModifyOperation() {
			public void execute(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, ecoreFiles, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */
	private void doFinish(String containerName, String fileName, String[] ecoreFiles,
			IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (resource == null || !resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		ResourceSetImpl rsrcSet = new ResourceSetImpl();
		URI uri = URI.createURI(file.getFullPath().toString());
		Resource rsrc = null;
		if (file.exists())
			throwCoreException("File \"" + file.getFullPath() + "\" already exists");
		rsrc = rsrcSet.createResource(uri);
		Diagram diagram = ModelFactory.eINSTANCE.createDiagram();
		for (int i = 0; i < ecoreFiles.length; i++) {
			String filePath = ecoreFiles[i];
			URI ecoreUri = URI.createFileURI(filePath);
			Resource ecoreResource = null;
			try {
				ecoreResource = rsrcSet.getResource(ecoreUri, true);
			} catch (Exception e) {
			}
			if (ecoreResource == null) {
				ecoreResource = rsrcSet.createResource(ecoreUri);
				EPackage pckg = EcoreFactory.eINSTANCE.createEPackage();
				pckg.setName("changeMe");
				ecoreResource.getContents().add(pckg);
				try {
					ecoreResource.save(Collections.EMPTY_MAP);
				} catch (IOException ioe) {
					EDiagramPlugin.INSTANCE.log(ioe);
				}
			}
			List contents = ecoreResource.getContents();
			for (int j = 0; j < contents.size(); j++) {
				diagram.getImports().add(contents.get(j));
			}
		}
		if (diagram.getImports().size() == 0)
			throwCoreException("No valid EPackages could be loaded.  At least one " +
					"EPackage needs to exist for the .ediagram file to be created.");
		rsrc.getContents().add(diagram);
		try {
			rsrc.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			EDiagramPlugin.INSTANCE.log(e);
		}
		
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
	
	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "org.eclipse.gef.examples.ediagram", IStatus.OK, 
					message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}