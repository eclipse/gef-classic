package com.ibm.etools.gef.internal.resources;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.lang.ref.*;

abstract public class ResourceRecord {

Object key;
Finalizer finalizer = null;
WeakReference reference;
int users;

public void decrementUsers(){
	users--;
	if (users == 0)
		finalizer = null;
}

public void incrementUsers(){
	if (users == 0 && reference != null)
		finalizer = (Finalizer)reference.get();
	users++;
}

abstract void dispose(Object resource);

protected Object getResource(){
	if (reference == null || reference.get() == null)
		recreate();
	return finalizer.getResource();
}

abstract void recreate();

protected class Finalizer {
	Object resource;
	public Finalizer(Object resource){
		this.resource = resource;
	}

	protected void finalize(){
		ResourceRecord.this.dispose(resource);
	}
	protected Object getResource(){
		return resource;
	}
}

protected void setFinalizer(Finalizer f){
	finalizer = f;
	reference = new WeakReference(f);
}

}