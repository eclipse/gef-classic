package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.beans.*;

import org.eclipse.jface.viewers.*;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.palette.*;
import com.ibm.etools.gef.ui.parts.PaletteViewerKeyHandler;

public class PaletteViewerImpl 
	extends com.ibm.etools.gef.ui.parts.GraphicalViewerImpl
	implements PaletteViewer
{

List paletteListeners = new ArrayList();
ButtonGroup buttonGroup = null;
PaletteEntry selectedEntry = null;
PaletteRoot paletteRoot = null;

public void addPaletteListener(PaletteListener paletteListener){
	if(paletteListeners!=null)
		paletteListeners.add(paletteListener);
}

public void addSelectionChangedListener(ISelectionChangedListener listener){;}

protected void createDefaultRoot(){
	setRootEditPart(new PaletteRootEditPart());
}

protected void firePaletteSelectionChanged(){
	if(paletteListeners==null)return;
	PaletteEvent event = new PaletteEvent(this, selectedEntry);
	for(int listener=0; listener<paletteListeners.size(); listener++)
		((PaletteListener)paletteListeners.get(listener)).entrySelected(event);
}

public ButtonGroup getButtonGroup(){
	if(buttonGroup==null)
		buttonGroup = new ButtonGroup();
	return buttonGroup;
}

public EditPartFactory getEditPartFactory(){
	if (super.getEditPartFactory() == null)
		setEditPartFactory(new PaletteEditPartFactory());
	return super.getEditPartFactory();
}

public ISelection getSelection(){return StructuredSelection.EMPTY;}

protected void init(){
	setEditDomain(new DefaultEditDomain(null));
	setKeyHandler(new PaletteViewerKeyHandler(this));
}

public PaletteToolEntry getSelectedEntry(){
	return (PaletteToolEntry)selectedEntry;
}

public void removePaletteListener(PaletteListener paletteListener){
	paletteListeners.remove(paletteListener);
}

public void removeSelectionChangedListener(ISelectionChangedListener listener){;}

public void setPaletteRoot(PaletteRoot root){
	paletteRoot = root;
	if( paletteRoot != null){
		EditPart palette = getEditPartFactory().createEditPart(getRootEditPart(), root);
		getRootEditPart().setContents(palette);
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

public void setSelection(PaletteEntry entry){
	if (selectedEntry == entry)
		return;
	if (entry == null){
		getButtonGroup().setSelected(getButtonGroup().getDefault());
		if (getButtonGroup().getSelected() == null){
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

}


