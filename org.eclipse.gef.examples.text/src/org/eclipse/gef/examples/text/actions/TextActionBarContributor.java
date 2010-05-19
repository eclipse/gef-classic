/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.actions;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;


/**
 * @since 3.1
 */
public class TextActionBarContributor extends ActionBarContributor {

/**
 * @see ActionBarContributor#buildActions()
 */
protected void buildActions() {
	addRetargetAction(new StyleRetargetAction(TextActionConstants.STYLE_BOLD));
	addRetargetAction(new StyleRetargetAction(TextActionConstants.STYLE_ITALIC));
	addRetargetAction(new StyleRetargetAction(TextActionConstants.STYLE_UNDERLINE));
	addRetargetAction(new StyleRetargetAction(TextActionConstants.BLOCK_ALIGN_CENTER));
	addRetargetAction(new StyleRetargetAction(TextActionConstants.BLOCK_ALIGN_LEFT));
	addRetargetAction(new StyleRetargetAction(TextActionConstants.BLOCK_ALIGN_RIGHT));
	addRetargetAction(new StyleRetargetAction(TextActionConstants.BLOCK_LTR));
	addRetargetAction(new StyleRetargetAction(TextActionConstants.BLOCK_RTL));
}

public void contributeToToolBar(IToolBarManager toolbar) {
	toolbar.add(new ChangeFontContributionItem(getPage()));
	toolbar.add(new ResizeFontContributionItem(getPage()));
	toolbar.add(new Separator());
	toolbar.add(getAction(TextActionConstants.STYLE_BOLD));
	toolbar.add(getAction(TextActionConstants.STYLE_ITALIC));
	toolbar.add(getAction(TextActionConstants.STYLE_UNDERLINE));
	toolbar.add(new Separator());
	toolbar.add(getAction(TextActionConstants.BLOCK_ALIGN_LEFT));
	toolbar.add(getAction(TextActionConstants.BLOCK_ALIGN_CENTER));
	toolbar.add(getAction(TextActionConstants.BLOCK_ALIGN_RIGHT));
	toolbar.add(new Separator());
	toolbar.add(getAction(TextActionConstants.BLOCK_LTR));
	toolbar.add(getAction(TextActionConstants.BLOCK_RTL));
}

/**
 * @see ActionBarContributor#declareGlobalActionKeys()
 */
protected void declareGlobalActionKeys() {
}

}
