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

package org.eclipse.gef.examples.text;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.GEFActionConstants;

import org.eclipse.gef.examples.text.actions.StyleRetargetAction;

/**
 * @since 3.1
 */
public class TextActionBarContributor extends ActionBarContributor {

/**
 * @see ActionBarContributor#buildActions()
 */
protected void buildActions() {
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.STYLE_BOLD));
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.STYLE_ITALIC));
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.STYLE_UNDERLINE));
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.BLOCK_ALIGN_CENTER));
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.BLOCK_ALIGN_LEFT));
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.BLOCK_ALIGN_RIGHT));
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.BLOCK_LTR));
	addRetargetAction(new StyleRetargetAction(GEFActionConstants.BLOCK_RTL));
}

public void contributeToToolBar(IToolBarManager toolbar) {
	toolbar.add(getAction(GEFActionConstants.STYLE_BOLD));
	toolbar.add(getAction(GEFActionConstants.STYLE_ITALIC));
	toolbar.add(getAction(GEFActionConstants.STYLE_UNDERLINE));
	toolbar.add(new Separator());
	toolbar.add(getAction(GEFActionConstants.BLOCK_ALIGN_LEFT));
	toolbar.add(getAction(GEFActionConstants.BLOCK_ALIGN_CENTER));
	toolbar.add(getAction(GEFActionConstants.BLOCK_ALIGN_RIGHT));
	toolbar.add(new Separator());
	toolbar.add(getAction(GEFActionConstants.BLOCK_LTR));
	toolbar.add(getAction(GEFActionConstants.BLOCK_RTL));
}

/**
 * @see ActionBarContributor#declareGlobalActionKeys()
 */
protected void declareGlobalActionKeys() {
}

}
