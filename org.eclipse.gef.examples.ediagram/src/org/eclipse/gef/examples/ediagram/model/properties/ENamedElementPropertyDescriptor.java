/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     E.D.Willink - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.properties;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.views.properties.PropertyDescriptor;


public class ENamedElementPropertyDescriptor extends PropertyDescriptor
{
private final ResourceSet resourceSet;
private final Class typeClass;

public ENamedElementPropertyDescriptor(PropertyId propertyId, Resource resource, Class typeClass) {
	super(propertyId, propertyId.getDisplayName());
	setCategory(propertyId.getCategoryName());
	this.typeClass = typeClass;
	resourceSet = resource.getResourceSet();
}

/**
 * The <code>ENamedElementPropertyDescriptor</code> implementation of this 
 * <code>IPropertyDescriptor</code> method creates and returns a new
 * <code>DialogCellEditor</code> to manage a <code>ENamedElementListSelectionDialog</code>.
 * <p>
 * The editor is configured with the current validator if there is one.
 * </p>
 */
public CellEditor createPropertyEditor(Composite parent) {
	CellEditor editor = new DialogCellEditor(parent) {
		protected Object openDialogBox(Control cellEditorWindow) {
			ENamedElementListSelectionDialog dialog =
				new ENamedElementListSelectionDialog(cellEditorWindow.getShell(), resourceSet, typeClass);
			dialog.open();
	        Object[] results = dialog.getResult();
	        return results != null ? results[0] : null;
		}
		
	    protected void updateContents(Object value) {
	        Label label = getDefaultLabel();
	        if (label == null)
	            return;
	        String text = "";//$NON-NLS-1$
	        if (value != null)
	            text = getLabelProvider().getText(value);
	        label.setText(text);
	    }
	};
    if (getValidator() != null)
        editor.setValidator(getValidator());
    return editor;
}

public Object fromModel(ENamedElement eNamedElement) {
	return eNamedElement;
}

public ILabelProvider getLabelProvider() {
    if (!isLabelProviderSet())
    	setLabelProvider(new ENamedElementLabelProvider());
    return super.getLabelProvider();
}

public ENamedElement toModel(Object object) {
	return (ENamedElement) object;
}

}