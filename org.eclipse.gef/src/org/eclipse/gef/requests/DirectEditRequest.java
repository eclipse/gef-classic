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
package org.eclipse.gef.requests;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.gef.RequestConstants;

/**
 * A request to perform direct editing on the receiver of the Request.
 * @author hudsonr
 * @since 2.0 */
public class DirectEditRequest extends LocationRequest {

private Object feature;
private CellEditor celleditor;

/**
 * Constructor for DirectEditRequest.
 */
public DirectEditRequest() {
	super(RequestConstants.REQ_DIRECT_EDIT);
}

/**
 * Constructor for DirectEditRequest.
 * @param type
 */
public DirectEditRequest(Object type) {
	super(type);
}

/**
 * If the EditPart supports direct editing of multiple features, this parameter can be
 * used to discriminate among them.
 */
public Object getDirectEditFeature(){
	return feature;
}

public CellEditor getCellEditor(){
	return celleditor;
}

public void setCellEditor(CellEditor celleditor) {
	this.celleditor = celleditor;
}

public void setDirectEditFeature(Object feature){
	this.feature = feature;
}

}
