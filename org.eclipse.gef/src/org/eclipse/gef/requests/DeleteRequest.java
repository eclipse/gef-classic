package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.gef.*;

/**
 * A Request to delete EditPart(s).
 */
public class DeleteRequest
	extends GroupRequest
{

private List contributions = new ArrayList();

/**
 * Creates a DeleteRequest with the default type.
 */
public DeleteRequest(){
	setType(RequestConstants.REQ_DELETE);
}

/**
 * Creates a DeleteRequest with the given type.
 *
 * @param type The type of request.
 */
public DeleteRequest(Object type) {
	setType(type);
}

/**
 * Sets the EditParts making the request.
 *
 * @param list The list of EditParts.
 */
public void setContributions(List list){
	contributions = list;
}

/**
 * Adds an EditPart to the list of contributions.
 *
 * @param id The EditPart to be added.
 */
public void addContribution(Object id) {
	contributions.add(id);
}

/**
 * Checks to see if the given EditPart is in the list of 
 * contributions.
 *
 * @return <code>true</code> if id is in the list of contributions.
 */
public boolean containsContribution(Object id) {
	return contributions.contains(id);
}

/**
 * Returns the list of contributions.
 *
 * @return The list of contributions.
 */
public List getContributions() {
	return contributions;
}

}