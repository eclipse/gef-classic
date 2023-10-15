/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

package org.eclipse.gef.examples.text.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.actions.RetargetAction;

/**
 * @since 3.1
 */
public class StyleRetargetAction extends RetargetAction {

	public StyleRetargetAction(String styleID) {
		super(styleID, "", IAction.AS_CHECK_BOX);
		BooleanStyleAction.configureStyleAction(this);
	}

}
