package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

abstract public class SimpleOutput
	extends LogicSubpart
{

static final long serialVersionUID = 1;

private static int count;
public static String TERMINAL_OUT = "OUT";  //$NON-NLS-1$

public String getNewID(){
	return Integer.toString(count++);
}

public Object getPropertyValue(Object propName) {
	if( ID_SIZE.equals(propName)){
		return new String("("+getSize().width+","+getSize().height+")");//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	}
	return super.getPropertyValue(propName);
}

abstract public boolean getResult();

public Dimension getSize(){
	return new Dimension(-1, -1);
}

public void removeOutput(Wire w){
	outputs.remove(w);
}

/**
 * Nulls out any changes to this and its subclasses as
 * they are of fixed size.
 */
public void setPropertyValue(Object id, Object value){
	if(ID_SIZE.equals(id)) 
		super.setPropertyValue(id,new Dimension(getSize()));
	else
		super.setPropertyValue(id,value);
}

public void update(){
	setOutput(TERMINAL_OUT, getResult());
}

}
