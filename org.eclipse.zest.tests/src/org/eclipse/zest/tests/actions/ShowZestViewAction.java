/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.tests.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Shows the View which is defined in the action definition id of the IAction.
 * 
 * @author irbull
 * @see IWorkbenchWindowActionDelegate
 */
public class ShowZestViewAction implements IWorkbenchWindowActionDelegate {

	/**
	 * Shows the view whose unique ID is set in the action definition id.
	 * @param action the action
	 * @see IAction#setActionDefinitionId(java.lang.String)
	 */
	public void run(IAction action) {
		final IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (ww == null) {
			return;
		}
		
		final IWorkbenchPage activePage = ww.getActivePage();
		if (activePage == null) {
			return;
		}
		
		try {
			String actionID = action.getId();
			if ( actionID.equals( "org.eclipse.mylar.zest.tests.actions.NestedGraphViewerAction") ) 
				activePage.showView("org.eclipse.mylar.zest.tests.nestedgraphviewer");
			else if ( actionID.equals( "org.eclipse.mylar.zest.tests.actions.SpingGraphViewerAction"))
				activePage.showView("org.eclipse.mylar.zest.tests.springgraphviewer");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}
	
}