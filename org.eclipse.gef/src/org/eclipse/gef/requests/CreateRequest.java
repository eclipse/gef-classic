package org.eclipse.gef.requests;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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