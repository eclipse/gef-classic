package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.IMenuManager;
import com.ibm.etools.gef.EditPartViewer;

public interface ContextMenuProvider {

void buildContextMenu(IMenuManager menu, EditPartViewer viewer);

}