/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.tools;

import java.util.*;

import org.eclipse.swt.widgets.Display;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.TargetRequest;

abstract public class TargetingTool
	extends AbstractTool
{

private static final int FLAG_LOCK_TARGET = AbstractTool.MAX_FLAG << 1;
private static final int FLAG_TARGET_FEEDBACK = AbstractTool.MAX_FLAG << 2;
protected static final int MAX_FLAG = FLAG_TARGET_FEEDBACK;

private Request targetRequest;
private EditPart targetEditPart;
private AutoexposeHelper exposeHelper;

protected Request createTargetRequest(){
	Request request = new Request();
	request.setType(getCommandName());
	return request;
}

public void deactivate() {
	if (isHoverActive())
		resetHover();
	if (isTargetLocked())
		unlockTargetEditPart();
	eraseTargetFeedback();
	targetEditPart = null;
	targetRequest = null;
	super.deactivate();
}

protected void doAutoexpose() {
	if (exposeHelper == null)
		return;
	if (exposeHelper.step(getLocation())) {
		handleAutoexpose();
		Display.getCurrent().asyncExec(new QueuedAutoexpose());
	} else
		setAutoexposeHelper(null);
}

/**
 * Erase feedback indicating that the viewer object is no longer 
 * the target of a drag.
 */
protected void eraseTargetFeedback() {
	if (!isShowingTargetFeedback())
		return;
	setFlag(FLAG_TARGET_FEEDBACK, false);
	if (getTargetEditPart() != null)
		getTargetEditPart().eraseTargetFeedback(getTargetRequest());
}

protected Command getCommand(){
	if (getTargetEditPart() == null)
		return null;
	return getTargetEditPart().
		getCommand(getTargetRequest());
}

/**
 * Returns a List of EditParts that should be excluded from
 * the possible Targets for this tools operations
 * Example, when dragging an object, the object should be excluded
 * to prevent you from dropping the object inside itself.
 */
protected Collection getExclusionSet(){
	return Collections.EMPTY_LIST;
}

protected EditPartViewer.Conditional getTargetingConditional() {
	return new EditPartViewer.Conditional() {
		public boolean evaluate(EditPart editpart) {
			return editpart.getTargetEditPart(getTargetRequest()) != null;
		}
	};
}

protected EditPart getTargetEditPart(){
	return targetEditPart;
}

protected Request getTargetRequest(){
	if (targetRequest == null)
		setTargetRequest(createTargetRequest());
	return targetRequest;
}

protected void handleAutoexpose() { }

protected boolean handleEnteredEditPart() {
	updateTargetRequest();
	showTargetFeedback();
	return true;
}

protected boolean handleExitingEditPart() {
	resetHover();
	eraseTargetFeedback();
	return true;
}

protected boolean handleHoverStop(){return false;}

protected boolean handleInvalidInput(){
	eraseTargetFeedback();
	setCurrentCommand(UnexecutableCommand.INSTANCE);
	return true;
}

protected final void handleLeavingEditPart() throws Exception {}

protected boolean handleViewerExited() {
	setTargetEditPart(null);
	return true;
}

protected boolean isShowingTargetFeedback(){
	return getFlag(FLAG_TARGET_FEEDBACK);
}

protected boolean isTargetLocked(){
	return getFlag(FLAG_LOCK_TARGET);
}

protected void lockTargetEditPart(EditPart editpart) {
	if (editpart == null)  {
		unlockTargetEditPart();
		return;
	}
	setFlag(FLAG_LOCK_TARGET, true);
	setTargetEditPart(editpart);
}

void resetHover(){
	if (isHoverActive())
		handleHoverStop();
	setHoverActive(false);
}

class QueuedAutoexpose implements Runnable {
	public void run() {
		if (exposeHelper != null)
			doAutoexpose();
	}
}

protected void setAutoexposeHelper(AutoexposeHelper helper) {
	exposeHelper = helper;
	if (exposeHelper == null)
		return;
	Display.getCurrent().asyncExec(new QueuedAutoexpose());
}

protected void setTargetEditPart(EditPart editpart){
	if (editpart != targetEditPart){
		if (targetEditPart != null){
			debug("Leaving:\t" + targetEditPart);//$NON-NLS-1$
			handleExitingEditPart();
		}
		targetEditPart = editpart;
		if (getTargetRequest() instanceof TargetRequest)
			((TargetRequest)getTargetRequest()).setTargetEditPart(targetEditPart);
		debug("Entering:\t" + targetEditPart);//$NON-NLS-1$
		handleEnteredEditPart();
	}
}

protected void setTargetRequest(Request req){
	targetRequest = req;
}

protected void showTargetFeedback() {
	if (getTargetEditPart() != null)
		getTargetEditPart().showTargetFeedback(getTargetRequest());
	setFlag(FLAG_TARGET_FEEDBACK, true);
}

protected void unlockTargetEditPart() {
	setFlag(FLAG_LOCK_TARGET, false);
	updateTargetUnderMouse();
}

/**
 * Updates the active {@link AutoexposeHelper}. Does nothing if there is still an active
 * helper. Otherwise, obtains a new helper (possible <code>null</code>) at the current
 * mouse location and calls {@link #setAutoexposeHelper(AutoexposeHelper)}.
 */
protected void updateAutoexposeHelper() {
	if (exposeHelper != null)
		return;
	AutoexposeHelper.Search search;
	search = new AutoexposeHelper.Search(getLocation());
	getCurrentViewer()
		.findObjectAtExcluding(getLocation(), Collections.EMPTY_LIST, search);
	setAutoexposeHelper(search.result);
}

/**
 * Subclasses should override to update the target request.
 */
protected void updateTargetRequest() { }

/**
 * Returns <code>true</code> if the target has changed.
 */
protected boolean updateTargetUnderMouse() {
	if (!isTargetLocked()) {
		Collection exclude = getExclusionSet();
		EditPart editPart = getCurrentViewer().findObjectAtExcluding(
			getLocation(),
			exclude,
			getTargetingConditional());
		if (editPart != null)
			editPart = editPart.getTargetEditPart(getTargetRequest());
		boolean changed = getTargetEditPart() != editPart;
		setTargetEditPart(editPart);
		return changed;
	} else
		return false;
}

}