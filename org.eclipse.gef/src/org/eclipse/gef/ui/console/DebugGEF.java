package org.eclipse.gef.ui.console;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.gef.GEF;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.ImageDescriptor;
import java.util.List;
import java.util.ArrayList;

public class DebugGEF extends ViewPart{

protected Text text;
protected List actions = null;

public DebugGEF(){
	super();
}

public void createPartControl(Composite parent){
	text = new Text(parent, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
	text.setFont( new org.eclipse.swt.graphics.Font(parent.getDisplay(),"Arial",7, SWT.NATIVE)); //$NON-NLS-1$
	text.setText("GEF Debug"); //$NON-NLS-1$
	GEF.setConsole(text);
	makeActions();
	fillActions();
}

public void dispose(){
	GEF.setConsole(null);
	super.dispose();
}

protected void fillActions(){
	IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
	for(int i=0;i<actions.size();i++){
		mgr.add((IAction)actions.get(i));
	}
}

private ImageDescriptor getDescriptor(String image){
	return ImageDescriptor.createFromFile(DebugGEF.class,image);
}

protected void makeActions(){
	if( actions == null ) actions = new ArrayList();
	DebugGEFAction action = null;
	action = new DebugGEFAction(DebugGEFAction.DEBUG_DND,
		getDescriptor("icons/debugDND.gif")); //$NON-NLS-1$
	action.setToolTipText("Drag and Drop"); //$NON-NLS-1$
	action.setChecked(GEF.DebugDND);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_STATES,
		getDescriptor("icons/debugStates.gif")); //$NON-NLS-1$
	action.setToolTipText("States"); //$NON-NLS-1$
	action.setChecked(GEF.DebugToolStates);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_FEEDBACK,
		getDescriptor("icons/debugFeedback.gif")); //$NON-NLS-1$
	action.setToolTipText("Feedback"); //$NON-NLS-1$
	action.setChecked(GEF.DebugFeedback);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_PAINTING,
		getDescriptor("icons/debugPaint.gif")); //$NON-NLS-1$
	action.setToolTipText("Painting Messages"); //$NON-NLS-1$
	action.setChecked(GEF.DebugPainting);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_EDITPARTS,
		getDescriptor("icons/debugEditParts.gif")); //$NON-NLS-1$
	action.setToolTipText("EditPart Messages"); //$NON-NLS-1$
	action.setChecked(GEF.DebugEditParts);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_EVENTS,
		getDescriptor("icons/debugEvents.gif")); //$NON-NLS-1$
	action.setToolTipText("Event Messages"); //$NON-NLS-1$
	action.setChecked(GEF.DebugEvents);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_TOOLS,
		getDescriptor("icons/debugTools.gif")); //$NON-NLS-1$
	action.setToolTipText("Tool Messages"); //$NON-NLS-1$
	action.setChecked(GEF.DebugTools);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_GLOBAL,
		getDescriptor("icons/debugGlobal.gif")); //$NON-NLS-1$
	action.setToolTipText("Global Messages"); //$NON-NLS-1$
	action.setChecked(GEF.GlobalDebug);
	actions.add(action);
	action = new DebugGEFAction(DebugGEFAction.DEBUG_CLEAR,
		getDescriptor("icons/debugClear.gif")); //$NON-NLS-1$
	action.setToolTipText("Clears Debug Messages"); //$NON-NLS-1$
	actions.add(action);
}

public void setFocus(){
	if( text != null )
		text.setFocus();
}

}