package com.ibm.etools.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.viewers.CellEditor;
import com.ibm.etools.gef.Request;
import com.ibm.etools.gef.RequestConstants;

public class DirectEditRequest extends Request {

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
 * If the EditPart supports direct editing of multiple features, this parameter can be used to
 * discriminate among them.
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
