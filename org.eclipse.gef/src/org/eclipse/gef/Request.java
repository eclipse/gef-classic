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
package org.eclipse.gef;

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