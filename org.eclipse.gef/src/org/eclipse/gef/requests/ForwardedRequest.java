package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.*;

/**
 * A Request to be forwarded to another EditPart.
 */
public class ForwardedRequest
	extends Request
{

private EditPart sender;

/**
 * Creates a ForwardRequest with the given type.
 *
 * @param type The type of Request.
 */
public ForwardedRequest(Object type){
	setType(type);
}

/**
 * Creates a ForwardRequest with the given type and
 * sets the sender.
 *
 * @param type   The type of Request.
 * @param sender The EditPart that forwarded this Request.
 */
public ForwardedRequest(Object type, EditPart sender){
	setType(type);
	this.sender = sender;
}

/**
 * Returns the EditPart that forwarded this Request.
 *
 * @return The EditPart that forwarded this Request.
 */
public EditPart getSender(){
	return sender;
}

}