/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.parts;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;


public class LabelDirectEditManager 
	extends DirectEditManager 
{

public LabelDirectEditManager(GraphicalEditPart source, Class editorType,
		CellEditorLocator locator) {
	super(source, editorType, locator);
}

/**
 * Creates the cell editor on the given composite.  The cell editor is created by 
 * instantiating the cell editor type passed into this DirectEditManager's constuctor.
 * @param composite the composite to create the cell editor on
 * @return the newly created cell editor
 */
protected CellEditor createCellEditorOn(Composite composite) {
	return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP) {
		public boolean isPasteEnabled() {
			boolean result = false;
			if (text != null && !text.isDisposed()) {
				Clipboard cb = new Clipboard(Display.getDefault());
				TransferData[] transferTypes = cb.getAvailableTypes();
				for (int i = 0; i < transferTypes.length; i++) {
					if (TextTransfer.getInstance().isSupportedType(transferTypes[i])) {
						result = true;
						break;
					}
				}
				cb.dispose();
			}
			return result; 
		}
	};
}

/**
 * Used to determine the initial text of the cell editor and to determine the font size
 * of the text.
 * @return the figure being edited
 */
protected IFigure getDirectEditFigure() {
	return ((BaseEditPart)getEditPart()).getDirectEditFigure();
}

/**
 * @return the initial value of the text shown in the cell editor for direct-editing;
 * cannot return <code>null</code>
 */
protected String getInitialText() {
	return ((BaseEditPart)getEditPart()).getDirectEditText();
}

protected void initCellEditor() {
//	super.initCellEditor();
	Text text = (Text)getCellEditor().getControl();
	text.setText(getInitialText());
	text.setFont(getDirectEditFigure().getFont());
	text.selectAll();
}

}
