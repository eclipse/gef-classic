package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ButtonGroup;

import org.eclipse.gef.*;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;

public class PaletteViewerImpl
	extends org.eclipse.gef.ui.parts.GraphicalViewerImpl
	implements PaletteViewer 
{

PaletteCustomizer customizer = null;
PaletteCustomizerDialog customizerDialog = null;
List paletteListeners = new ArrayList();
ButtonGroup buttonGroup = null;
PaletteEntry selectedEntry = null;
PaletteRoot paletteRoot = null;
PaletteViewerPreferences prefs = null;

private boolean controlHooked = false;

private PropertyChangeListener prefListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		EditPart root = getRootEditPart().getContents();
		traverseEditParts(root);
	}
	private void traverseEditParts(EditPart part) {
		part.refresh();
		List children = part.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EditPart child = (EditPart) iter.next();
			traverseEditParts(child);
		}
	}
};

public PaletteViewerImpl() {
	setEditDomain(new DefaultEditDomain(null));
	setKeyHandler(new PaletteViewerKeyHandler(this));
	setPaletteViewerPreferencesSource(
		new DefaultPaletteViewerPreferences(
			GEFPlugin.getDefault().getPreferenceStore()));

}

public void addPaletteListener(PaletteListener paletteListener) {
	if (paletteListeners != null)
		paletteListeners.add(paletteListener);
}

protected void createDefaultRoot() {
	setRootEditPart(new PaletteRootEditPart());
}

/**
 * @see org.eclipse.gef.EditPartViewer#dispose()
 */
public void dispose() {
	super.dispose();
	prefs.dispose();
}

protected void firePaletteSelectionChanged() {
	if (paletteListeners == null)
		return;
	PaletteEvent event = new PaletteEvent(this, selectedEntry);
	for (int listener = 0; listener < paletteListeners.size(); listener++)
		((PaletteListener) paletteListeners.get(listener)).entrySelected(
			event);
}

public ButtonGroup getButtonGroup() {
	if (buttonGroup == null)
		buttonGroup = new ButtonGroup();
	return buttonGroup;
}

/**
 * Returns the customizer.
 * @return PaletteCustomizer
 */
public PaletteCustomizer getCustomizer() {
	return customizer;
}

public PaletteCustomizerDialog getCustomizerDialog() {
	if (customizerDialog == null) {
		customizerDialog = new PaletteCustomizerDialog(getControl().getShell(),
	                                                   getCustomizer(),
	                                                   prefs,
	                                                   paletteRoot);
	}
	return customizerDialog;
}

public EditPartFactory getEditPartFactory() {
	if (super.getEditPartFactory() == null)
		setEditPartFactory(new PaletteEditPartFactory());
	return super.getEditPartFactory();
}

public PaletteViewerPreferences getPaletteViewerPreferencesSource() {
	return prefs;
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#hookControl()
 */
protected void hookControl() {
	super.hookControl();
	if (prefs != null) {
		prefs.addPropertyChangeListener(prefListener);
	}
	controlHooked = true;
}

public PaletteToolEntry getSelectedEntry() {
	return (PaletteToolEntry) selectedEntry;
}

public void removePaletteListener(PaletteListener paletteListener) {
	paletteListeners.remove(paletteListener);
}

/**
 * Sets the customizer.
 * @param customizer The customizer to set
 */
public void setCustomizer(PaletteCustomizer customizer) {
	this.customizer = customizer;
}

public void setPaletteRoot(PaletteRoot root) {
	paletteRoot = root;
	if (paletteRoot != null) {
		EditPart palette =
			getEditPartFactory().createEditPart(getRootEditPart(), root);
		getRootEditPart().setContents(palette);
	}
}

public void setPaletteViewerPreferencesSource(PaletteViewerPreferences prefs) {
	if (prefs == null) {
		return;
	}

	if (this.prefs != null) {
		this.prefs.removePropertyChangeListener(prefListener);
	}

	this.prefs = prefs;
	if (controlHooked) {
		this.prefs.addPropertyChangeListener(prefListener);
	}
}

/*
public void setSelection(ISelection newSelection){
	IStructuredSelection selected = (IStructuredSelection)newSelection;
	if(selected==null){
		if(getButtonGroup().getSelected()!=null)
			getButtonGroup().getSelected().setSelected(false);
	}else{
		Object entry = selected.getFirstElement();
		if(entry instanceof PaletteToolEntry)
			setSelection((PaletteEntry)entry);
	}
}*/

public void setSelection(PaletteEntry entry) {
	if (selectedEntry == entry)
		return;
	if (entry == null) {
		getButtonGroup().setSelected(null);
		selectedEntry = null;
		getButtonGroup().setSelected(getButtonGroup().getDefault());
		if (getButtonGroup().getSelected() == null) {
			selectedEntry = null;
			firePaletteSelectionChanged();
		}
	} else {
		selectedEntry = entry;
		EntryEditPart ep = (EntryEditPart)getEditPartRegistry().get(entry);
		ep.select();
		firePaletteSelectionChanged();
	}
}

/**
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#unhookControl()
 */
protected void unhookControl() {
	super.unhookControl();
	if (prefs != null) {
		prefs.removePropertyChangeListener(prefListener);
	}
	controlHooked = false;
}

}
