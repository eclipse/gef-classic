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

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;

/**
 * @author hudsonr
 * Created on Mar 6, 2003
 */
class DelayedDirectEditHelper implements Runnable {

private EditPartViewer viewer;
private EditPart part;
private Request req;
private FocusListener focus;
private MouseListener mouse;
private KeyListener key;
private static DelayedDirectEditHelper activeHelper;

/**
 * Constructs a new helper and starts it immediately.  If another helper is active, it is
 * aborted and neither helper will run.
 */
public DelayedDirectEditHelper(
  EditPartViewer viewer,
  Request request,
  EditPart receiver) {
	this.req = request;
	this.viewer = viewer;
	this.part = receiver;
	if (activeHelper != null)
		activeHelper = null;
	else {
		hookControl(viewer.getControl());
		activeHelper = this;
		Display.getCurrent().timerExec(400, this);
	}
}

/**
 * The edit is canceled by setting the active helper to <code>null</code>.
 */
void abort() {
	activeHelper = null;
}

void hookControl(Control control) {
	control.addFocusListener(focus = new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			abort();
		}
	});
	control.addKeyListener(key = new KeyListener() {
		public void keyPressed(KeyEvent e) {
			abort();
		}
		public void keyReleased(KeyEvent e) {
			abort();
		}
	});
	
	control.addMouseListener(mouse = new MouseAdapter() {
		public void mouseDoubleClick(MouseEvent e) {
			abort();
		}
		public void mouseDown(MouseEvent e) {
			abort();
		}
	});
}

/**
 * If this helper has not been aborted, the target editpart will be sent the request.
 */
public void run() {
	if (activeHelper == this
		&& part.isActive()
		&& viewer.getControl() != null
		&& !viewer.getControl().isDisposed())
	{
		viewer.getControl().removeFocusListener(focus);
		viewer.getControl().removeMouseListener(mouse);
		viewer.getControl().removeKeyListener(key);
		part.performRequest(req);
	}
	activeHelper = null;
}

}
