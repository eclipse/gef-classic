package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.*;

/**
 * A Checkbox is a toggle figure which toggles between the checked and unchecked figures
 * to simulate a check box. A check box contains a text label to represent it.
 */
public final class CheckBox 
	extends Toggle 
{

private Label label = null;

static final Image
	UNCHECKED = new Image(null,
		CheckBox.class.getResourceAsStream("images/checkboxenabledoff.gif")), //$NON-NLS-1$
	CHECKED = new Image(null,
		CheckBox.class.getResourceAsStream("images/checkboxenabledon.gif")); //$NON-NLS-1$

/**
 * Constructs a CheckBox with no text.
 * 
 * @since 2.0
 */
public CheckBox() {
	this(""); //$NON-NLS-1$
}

/**
 * Constructs a CheckBox with the passed text in its label.
 * @param text The label text
 * @since 2.0
 */
public CheckBox(String text) {
	setContents(label = new Label(text, UNCHECKED));
}

/**
 * Adjusts CheckBox's icon depending on selection status.
 * 
 * @since 2.0
 */
protected void handleSelectionChanged() {
	if (isSelected())
		label.setIcon(CHECKED);
	else
		label.setIcon(UNCHECKED);
}

/**
 * Initializes this Clickable by setting a default model and adding a clickable event
 * handler for that model. Also adds a ChangeListener to update icon when  selection
 * status changes.
 * 
 * @since 2.0
 */
protected void init() {
	super.init();
	addChangeListener(new ChangeListener () {
		public void handleStateChanged(ChangeEvent changeEvent) { 
			if (changeEvent.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY))
				handleSelectionChanged();
		}
	});
}

}