package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.ui.actions.AlignmentAction;

public class LogicActionBarContributor
	extends org.eclipse.gef.ui.actions.ActionBarContributor
{
	
protected void declareActions(){
	super.declareActions();
	toolbarActions.add(IncrementDecrementAction.INCREMENT);
	toolbarActions.add(IncrementDecrementAction.DECREMENT);

	toolbarActions.add(SEPARATOR);
	toolbarActions.add(AlignmentAction.ID_ALIGN_LEFT);
	toolbarActions.add(AlignmentAction.ID_ALIGN_CENTER);
	toolbarActions.add(AlignmentAction.ID_ALIGN_RIGHT);

	toolbarActions.add(SEPARATOR);
	toolbarActions.add(AlignmentAction.ID_ALIGN_TOP);
	toolbarActions.add(AlignmentAction.ID_ALIGN_MIDDLE);
	toolbarActions.add(AlignmentAction.ID_ALIGN_BOTTOM);
}

}
