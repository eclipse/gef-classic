package com.ibm.etools.gef.internal.resources;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Cursor;

public class CursorRecord
	extends ResourceRecord
{

public CursorRecord(int which){
	key = new Integer(which);
}

public Cursor getCursor(){
	return (Cursor)getResource();
}

protected void dispose(Object resource){
	((Cursor)resource).dispose();
}

protected void recreate(){
	Integer i = (Integer)key;
	Cursor c = new Cursor(null, i.intValue());
	setFinalizer(new Finalizer(c));
}

}