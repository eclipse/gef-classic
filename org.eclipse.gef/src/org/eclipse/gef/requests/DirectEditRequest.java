package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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
