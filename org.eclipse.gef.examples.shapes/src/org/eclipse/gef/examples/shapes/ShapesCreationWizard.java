/*******************************************************************************
 * Copyright (c) 2004, 2024 Elias Volanakis and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.shapes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import org.eclipse.gef.examples.shapes.model.ShapesDiagram;

/**
 * Create new new .shape-file. Those files can be used with the ShapesEditor
 * (see plugin.xml).
 *
 * @author Elias Volanakis
 */
public class ShapesCreationWizard extends Wizard implements INewWizard {

	private static int fileCount = 1;
	private CreationPage page1;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		// add pages to this wizard
		addPage(page1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// create pages for this wizard
		page1 = new CreationPage(workbench, selection);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return page1.finish();
	}

	/**
	 * This WizardPage can create an empty .shapes file for the ShapesEditor.
	 */
	private class CreationPage extends WizardNewFileCreationPage {
		private static final String DEFAULT_EXTENSION = ".shapes"; //$NON-NLS-1$
		private final IWorkbench workbench;

		/**
		 * Create a new wizard page instance.
		 *
		 * @param workbench the current workbench
		 * @param selection the current object selection
		 * @see ShapesCreationWizard#init(IWorkbench, IStructuredSelection)
		 */
		CreationPage(IWorkbench workbench, IStructuredSelection selection) {
			super("shapeCreationPage1", selection); //$NON-NLS-1$
			this.workbench = workbench;
			setTitle(
					MessageFormat.format(ShapesExampleMessages.ShapesCreationWizard_CreateANewFile, DEFAULT_EXTENSION));
			setDescription(
					MessageFormat.format(ShapesExampleMessages.ShapesCreationWizard_CreateANewFile, DEFAULT_EXTENSION));
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createControl(org
		 * .eclipse.swt.widgets.Composite)
		 */
		@Override
		public void createControl(Composite parent) {
			super.createControl(parent);
			setFileName("shapesExample" + fileCount + DEFAULT_EXTENSION); //$NON-NLS-1$
			setPageComplete(validatePage());
		}

		/** Return a new ShapesDiagram instance. */
		private Object createDefaultContent() {
			return new ShapesDiagram();
		}

		/**
		 * This method will be invoked, when the "Finish" button is pressed.
		 *
		 * @see ShapesCreationWizard#performFinish()
		 */
		boolean finish() {
			// create a new file, result != null if successful
			IFile newFile = createNewFile();
			fileCount++;

			// open newly created file in the editor
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			if (newFile != null && page != null) {
				try {
					IDE.openEditor(page, newFile, true);
				} catch (PartInitException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
		 */
		@Override
		protected InputStream getInitialContents() {
			ByteArrayInputStream bais = null;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);) {
				// argument must be Serializable
				oos.writeObject(createDefaultContent());
				oos.flush();
				bais = new ByteArrayInputStream(baos.toByteArray());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return bais;
		}

		/**
		 * Return true, if the file name entered in this page is valid.
		 */
		private boolean validateFilename() {
			if (getFileName() != null && getFileName().endsWith(DEFAULT_EXTENSION)) {
				return true;
			}
			setErrorMessage(MessageFormat.format(ShapesExampleMessages.ShapesCreationWizard_FileNameMustEndWith,
					DEFAULT_EXTENSION));
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
		 */
		@Override
		protected boolean validatePage() {
			return super.validatePage() && validateFilename();
		}
	}
}
