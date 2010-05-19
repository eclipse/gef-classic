/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.dnd;

import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;

import org.eclipse.gef.examples.logicdesigner.edit.NativeDropRequest;

public class TextTransferDropTargetListener extends
		AbstractTransferDropTargetListener {

	public TextTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}

	protected Request createTargetRequest() {
		return new NativeDropRequest();
	}

	protected NativeDropRequest getNativeDropRequest() {
		return (NativeDropRequest) getTargetRequest();
	}

	protected void updateTargetRequest() {
		getNativeDropRequest().setData(getCurrentEvent().data);
	}

}
