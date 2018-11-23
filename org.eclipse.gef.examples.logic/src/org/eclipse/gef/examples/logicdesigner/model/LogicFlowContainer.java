/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LogicFlowContainer extends LogicDiagram {

	private class LayoutLabelProvider extends
			org.eclipse.jface.viewers.LabelProvider {

		public LayoutLabelProvider() {
			super();
		}

		public String getText(Object element) {
			if (element instanceof Integer) {
				Integer integer = (Integer) element;
				if (LAYOUT_SINGLE_ROW.intValue() == integer.intValue())
					return LogicMessages.PropertyDescriptor_LogicFlowContainer_SingleColumnLayout;
				if (LAYOUT_MULTI_ROW.intValue() == integer.intValue())
					return LogicMessages.PropertyDescriptor_LogicFlowContainer_MultiRowLayout;
			}
			return super.getText(element);
		}
	}

	public static String ID_LAYOUT = "layout"; //$NON-NLS-1$

	public static Integer LAYOUT_MULTI_ROW = new Integer(0);
	public static Integer LAYOUT_SINGLE_ROW = new Integer(1);
	static final long serialVersionUID = 1;

	protected Integer layout = LAYOUT_MULTI_ROW;

	public Integer getLayout() {
		if (layout == null) {
			return LAYOUT_MULTI_ROW;
		}
		return layout;
	}

	/**
	 * Returns <code>null</code> for this model. Returns normal descriptors for
	 * all subclasses.
	 * 
	 * @return Array of property descriptors.
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (getClass().equals(LogicFlowContainer.class)) {
			ComboBoxPropertyDescriptor cbd = new ComboBoxPropertyDescriptor(
					ID_LAYOUT,
					LogicMessages.PropertyDescriptor_LogicFlowContainer_Layout,
					new String[] {
							LogicMessages.PropertyDescriptor_LogicFlowContainer_MultiRowLayout,
							LogicMessages.PropertyDescriptor_LogicFlowContainer_SingleColumnLayout });
			cbd.setLabelProvider(new LayoutLabelProvider());
			return new IPropertyDescriptor[] { cbd, descriptors[0],
					descriptors[1] };
		}
		return super.getPropertyDescriptors();
	}

	public Object getPropertyValue(Object propName) {
		if (propName.equals(ID_LAYOUT))
			return layout;
		return super.getPropertyValue(propName);
	}

	public void setLayout(Integer newLayout) {
		Integer oldLayout = layout;
		layout = newLayout;
		firePropertyChange(ID_LAYOUT, oldLayout, layout);
	}

	public void setPropertyValue(Object id, Object value) {
		if (ID_LAYOUT.equals(id))
			setLayout((Integer) value);
		else
			super.setPropertyValue(id, value);
	}

	public String toString() {
		return LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Label;
	}

}
