package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.swt.graphics.Image;

public class PaletteCategory 
	extends PaletteContainer 
{

public static final String PROPERTY_INITIAL_STATUS = "Initial status"; //$NON-NLS-1$

public static final int INITIAL_STATUS_EXPANDED = 0;
public static final int INITIAL_STATUS_COLLAPSED = 1;
public static final int INITIAL_STATUS_PINNED_OPEN = 2;

private int initialStatus;

public PaletteCategory(String label){
	this(label, (Image)null);
}

public PaletteCategory(String label, Image icon, List children){
	this(label, icon);
	addAll(children);
}

public PaletteCategory(String label, List children){
	this(label, null, children);
}

public PaletteCategory(String label,Image icon){
	super(label, icon);
	setType(PALETTE_TYPE_CATEGORY);
}

public int getInitialStatus() {
	return initialStatus;
}

public boolean isInitiallyOpen() {
	return (getInitialStatus() == INITIAL_STATUS_EXPANDED 
			|| getInitialStatus() == INITIAL_STATUS_PINNED_OPEN);
}

public boolean isInitiallyPinned() {
	return (getInitialStatus() == INITIAL_STATUS_PINNED_OPEN);
}

public void setInitialStatus(int status) {
	if (getInitialStatus() == status) {
		return;
	}
	int oldStatus = initialStatus;
	initialStatus = status;
	listeners.firePropertyChange(PROPERTY_INITIAL_STATUS, oldStatus, status);
}
	
}