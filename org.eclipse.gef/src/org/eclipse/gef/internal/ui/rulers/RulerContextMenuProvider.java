/*******************************************************************************
 * Copyright (c) 2004, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.rulers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.CreateGuideAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * @author Pratik Shah
 */
public class RulerContextMenuProvider extends ContextMenuProvider {
	public RulerContextMenuProvider(EditPartViewer viewer) {
		super(viewer);
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		menu.appendToGroup(IWorkbenchActionConstants.GROUP_ADD, new CreateGuideAction(getViewer()));
	}

}
