package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 Encapsulates the information EditParts need to perform various functions.
 Using specific Request types when communicating with EditParts defines a
 clear contract between the Requester and the receiver of the Request.
 */
public class Request {

private Object type;

public Request(){}

/**
 * Constructs a Request with the type <B>type</b>
 */
public Request(Object type){
	setType(type);
}

/**
 * returns the type of the request
 */
public Object getType(){
	return type;
}

/**
 * Sets the type of the request
 * @param type the new Request type
 */
public void setType(Object type){
	this.type = type;
}

}