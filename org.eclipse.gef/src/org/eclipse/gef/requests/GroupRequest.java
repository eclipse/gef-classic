package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.gef.EditPart;

/**
 * A Request from multiple EditParts.
 */
public class GroupRequest
	extends org.eclipse.gef.Request
{

List parts;

/**
 * Creates a GroupRequest with the given type.
 *
 * @param type The type of Request.
 */
public GroupRequest(Object type){
	setType(type);
}

/**
 * Default constructor.
 */
public GroupRequest(){}

/**
 * Returns a List containing the EditParts making this Request.
 *
 * @return A List containing the EditParts making this Request.
 */
public List getEditParts(){
	return parts;
}

/**
 * Sets the EditParts making this Request to the given List.
 *
 * @param list The List of EditParts.
 */
public void setEditParts(List list){
	parts = list;
}

/**
 * A helper method to set the given EditPart as the requester.
 *
 * @param part The EditPart making the request.
 */
public void setEditParts(EditPart part){
	parts = new ArrayList();
	parts.add(part);
}


}