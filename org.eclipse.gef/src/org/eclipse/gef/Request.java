package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * An Object used to communicate with EditParts. Request encapsulates the information
 * EditParts need to perform various functions. Requests are used for obtaining commands,
 * showing feedback, and performing generic operations.
 */
public class Request {

private Object type;

/**
 * Constructs an empty Request */
public Request() { }

/**
 * Constructs a Request with the specified <i>type</i>
 * @param type the Request type
 * @see #getType()
 */
public Request(Object type) {
	setType(type);
}

/**
 * Returns the type of the request. The type is often used as a quick way to filter
 * recognized Requests. Once the type is identified, the Request is usually cast to a more
 * specific subclass containing additional data.
 * @return the Request <i>type</i>
 */
public Object getType() {
	return type;
}

/**
 * Sets the type of the Request.
 * @param type the Request type
 */
public void setType(Object type) {
	this.type = type;
}

}