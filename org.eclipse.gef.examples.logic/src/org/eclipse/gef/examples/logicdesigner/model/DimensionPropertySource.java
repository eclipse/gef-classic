package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class DimensionPropertySource 
	implements IPropertySource {

public static String ID_WIDTH = "width";  //$NON-NLS-1$
public static String ID_HEIGHT = "height";//$NON-NLS-1$
protected static IPropertyDescriptor[] descriptors;

static{
	descriptors = new IPropertyDescriptor[] {
		new TextPropertyDescriptor(ID_WIDTH,LogicMessages.DimensionPropertySource_Property_Width_Label),
		new TextPropertyDescriptor(ID_HEIGHT,LogicMessages.DimensionPropertySource_Property_Height_Label)
	};
}

protected Dimension dimension = null;

public DimensionPropertySource(Dimension dimension){
	this.dimension = new Dimension(dimension);
}

public Object getEditableValue(){
	return this;
}

public Object getPropertyValue(Object propName){
	return getPropertyValue((String)propName);
}

public Object getPropertyValue(String propName){
	if(ID_HEIGHT.equals(propName)){
		return new String(new Integer(dimension.height).toString());
	}
	if(ID_WIDTH.equals(propName)){
		return new String(new Integer(dimension.width).toString());
	}
	return null;
}

public Dimension getValue(){
	return new Dimension(dimension);
}

public void setPropertyValue(Object propName, Object value){
	setPropertyValue((String)propName, value);
}

public void setPropertyValue(String propName, Object value){
	if(ID_HEIGHT.equals(propName)){
		Integer newInt = new Integer((String)value);
		dimension.height = newInt.intValue();
	}
	if(ID_WIDTH.equals(propName)){
		Integer newInt = new Integer((String)value);
		dimension.width = newInt.intValue();
	}
}

public IPropertyDescriptor[] getPropertyDescriptors(){
	return descriptors;
}

public void resetPropertyValue(String propName){
}

public void resetPropertyValue(Object propName){
}

public boolean isPropertySet(Object propName){
	return true;
}

public boolean isPropertySet(String propName){
	if(ID_HEIGHT.equals(propName) || ID_WIDTH.equals(propName))return true;
	return false;
}

public String toString(){
	return new String("("+dimension.width+","+dimension.height+")");//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

}