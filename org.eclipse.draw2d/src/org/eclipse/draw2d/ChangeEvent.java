package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


public class ChangeEvent
	extends java.util.EventObject
{

private String property;

public ChangeEvent(Object source){
	super(source);
}

public ChangeEvent(Object source, String property){
	super(source);
	setPropertyName(property);
}

public String getPropertyName(){
	return property;
}

protected void setPropertyName(String string){
	property = string;
}

}