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

import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.RequestConstants;

/**
 * A Request to create a new object.
 */
public class CreateRequest
	extends org.eclipse.gef.Request
	implements DropRequest
{

private Object newObject;

private Dimension size;
private Point location;

private CreationFactory creationFactory;

/**
 * Creates a CreateRequest with the default type.
 */
public CreateRequest(){
	setType(RequestConstants.REQ_CREATE);
}

/**
 * Creates a CreateRequest with the given type.
 *
 * @param type The type of request.
 */
public CreateRequest(Object type){
	setType(type);
}

protected CreationFactory getFactory(){
	return creationFactory;
}

/**
 * Returns the location of the object to be created.
 */
public Point getLocation(){
	return location;
}

public Object getNewObject(){
	if (newObject == null)
		newObject = getFactory().getNewObject();
	return newObject;
}

public Object getNewObjectType(){
	return getFactory().getObjectType();
}

/**
 * Returns the size of the object to be created.
 */
public Dimension getSize(){
	return size;
}

/**
 * Sets the factory to be used when creating the new object.
 */
public void setFactory(CreationFactory factory){
	creationFactory = factory;
}

/**
 * Sets the location where the new object will be placed.
 *
 * @param location The location.
 */
public void setLocation(Point location){
	this.location = location;
}

/**
 * Sets the size of the new object.
 *
 * @param size The size.
 */
public void setSize(Dimension size){
	this.size = size;
}

}