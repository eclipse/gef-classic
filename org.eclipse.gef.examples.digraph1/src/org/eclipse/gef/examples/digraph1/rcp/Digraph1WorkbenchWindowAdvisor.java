/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef.examples.digraph1.rcp;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.part.FileInPlaceEditorInput;

/**
 * A Workbench Window Advisor for the Directed Graph Example Editor as an RCP
 * application.
 * 
 * @author Anthony Hunter
 */
public class Digraph1WorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	/**
	 * Constructor for Digraph1WorkbenchWindowAdvisor.
	 * 
	 * @param configurer
	 *            the workbench window configurer.
	 */
	public Digraph1WorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/*
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowCreate()
	 */
	@Override
	public void postWindowCreate() {
		try {
			/**
			 * Open a digraph editor on a file. The file does not exist, but
			 * since our editor does not read or write any data we are ok.
			 */
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
					new Path("/project/file.digraph1")); //$NON-NLS-1$
			getWindowConfigurer().getWindow().getActivePage().openEditor(
					new FileInPlaceEditorInput(file),
					"org.eclipse.gef.examples.digraph1"); //$NON-NLS-1$
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		super.postWindowCreate();
	}

	/*
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
	 */
	@Override
	public void preWindowOpen() {
		getWindowConfigurer().setInitialSize(new Point(450, 300));
		getWindowConfigurer().setShowCoolBar(false);
		getWindowConfigurer().setShowStatusLine(false);
		getWindowConfigurer().setShowMenuBar(false);
	}

}
