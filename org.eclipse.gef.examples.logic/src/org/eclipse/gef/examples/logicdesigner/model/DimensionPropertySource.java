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
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class DimensionPropertySource 
	implements IPropertySource {

public static String ID_WIDTH = "width";  //$NON-NLS-1$
public static String ID_HEIGHT = "height";//$NON-NLS-1$
protected static IPropertyDescriptor[] descriptors;

static{
	PropertyDescriptor widthProp =
		new TextPropertyDescriptor(ID_WIDTH,
			LogicMessages.DimensionPropertySource_Property_Width_Label);
	widthProp.setValidator(LogicNumberCellEditorValidator.instance());
	PropertyDescriptor heightProp = 
		new TextPropertyDescriptor(ID_HEIGHT,
			LogicMessages.DimensionPropertySource_Property_Height_Label);
	heightProp.setValidator(LogicNumberCellEditorValidator.instance());
	descriptors = new IPropertyDescriptor[] {widthProp,heightProp};
}

protected Dimension dimension = null;

public DimensionPropertySource(Dimension dimension){
	this.dimension = dimension.getCopy();
}

public Object getEditableValue(){
	return dimension.getCopy();
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