package org.eclipse.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.Request;

public class NativeDropRequest
	extends Request
{

private Object data;

public static final String ID = "$Native Drop Request";//$NON-NLS-1$

public NativeDropRequest() {
	super(ID);
}

public NativeDropRequest(Object type) {
	super(type);
}

public Object getData(){
	return data;
}

public void setData(Object data){
	this.data = data;
}

}
