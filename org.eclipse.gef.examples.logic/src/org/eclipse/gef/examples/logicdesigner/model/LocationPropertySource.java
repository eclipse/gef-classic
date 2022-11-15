/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LocationPropertySource implements IPropertySource {

	public static final String ID_XPOS = "xPos"; //$NON-NLS-1$
	public static final String ID_YPOS = "yPos"; //$NON-NLS-1$
	protected static IPropertyDescriptor[] descriptors;

	static {
		PropertyDescriptor xProp = new TextPropertyDescriptor(ID_XPOS,
				LogicMessages.LocationPropertySource_Property_X_Label);
		xProp.setValidator(LogicNumberCellEditorValidator.instance());
		PropertyDescriptor yProp = new TextPropertyDescriptor(ID_YPOS,
				LogicMessages.LocationPropertySource_Property_Y_Label);
		yProp.setValidator(LogicNumberCellEditorValidator.instance());
		descriptors = new IPropertyDescriptor[] { xProp, yProp };
	}

	protected Point point = null;

	public LocationPropertySource(Point point) {
		this.point = point.getCopy();
	}

	@Override
	public Object getEditableValue() {
		return point.getCopy();
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object propName) {
		if (ID_XPOS.equals(propName)) {
			return Integer.toString(point.x);
		}
		if (ID_YPOS.equals(propName)) {
			return Integer.toString(point.y);
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object propName) {
		return ID_XPOS.equals(propName) || ID_YPOS.equals(propName);
	}

	@Override
	public void resetPropertyValue(Object propName) {
	}

	@Override
	public void setPropertyValue(Object propName, Object value) {
		if (ID_XPOS.equals(propName)) {
			point.x = Integer.parseInt((String) value);
		}
		if (ID_YPOS.equals(propName)) {
			point.y = Integer.parseInt((String) value);
		}
	}

	@Override
	public String toString() {
		return new String("[" + point.x + "," + point.y + "]");//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	}

}
