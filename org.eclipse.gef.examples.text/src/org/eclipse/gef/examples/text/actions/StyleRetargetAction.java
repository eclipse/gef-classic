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

package org.eclipse.gef.examples.text.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * @since 3.1
 */
public class StyleRetargetAction extends RetargetAction {
	
public StyleRetargetAction(String styleID) {
	super(styleID, "", IAction.AS_CHECK_BOX);
	StyleAction.configureStyleAction(this);
}

}
