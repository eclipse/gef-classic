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
package org.eclipse.gef.ui.palette;

import java.beans.*;
import java.util.*;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.*;
import org.eclipse.gef.internal.ui.palette.editparts.PaletteRootEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.palette.customize.PaletteCustomizerDialog;
import org.eclipse.gef.ui.parts.PaletteViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

public class PaletteViewer
	extends ScrollingGraphicalViewer
{

private class PreferenceListener
	implements PropertyChangeListener
{
	private Font font = null;
	void disposeFont() {
		if (font != null) {
			font.dispose();
			font = null;
		}
	}
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();
		EditPart root = getRootEditPart().getContents();
		if (property.equals(PaletteViewerPreferences.PREFERENCE_FONT)) {
			disposeFont();
			font = new Font(Display.getCurrent(), 
					getPaletteViewerPreferences().getFontData());
			IFigure fig = ((GraphicalEditPart)root).getFigure();
			fig.setFont(font);
			fig.invalidateTree();
		} else if (property.equals(PaletteViewerPreferences.PREFERENCE_LAYOUT) 
				|| property.equals(PaletteViewerPreferences.PREFERENCE_AUTO_COLLAPSE)
				|| property.equals(DefaultPaletteViewerPreferences
					.convertLayoutToPreferenceName(getPaletteViewerPreferences()
					.getLayoutSetting()))) {
			refreshAllEditParts(root);
		}
	}
	private void refreshAllEditParts(EditPart part) {
		part.refresh();
		List children = part.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			EditPart child = (EditPart) iter.next();
			refreshAllEditParts(child);
		}
	}
}

private static final PaletteViewerPreferences PREFERENCE_STORE = 
				new DefaultPaletteViewerPreferences();
private ToolEntry activeEntry = null;

private PaletteCustomizer customizer = null;
private PaletteCustomizerDialog customizerDialog = null;
private boolean globalScrollbar = false;
private List paletteListeners = new ArrayList();
private PaletteRoot paletteRoot = null;
private PreferenceListener prefListener = new PreferenceListener();
private PaletteViewerPreferences prefs = PREFERENCE_STORE;

public PaletteViewer() {
	setEditDomain(new EditDomain());
	setKeyHandler(new PaletteViewerKeyHandler(this));
	setEditPartFactory(new PaletteEditPartFactory());
}

public void addPaletteListener(PaletteListener paletteListener) {
	if (paletteListeners != null)
		paletteListeners.add(paletteListener);
}

protected void createDefaultRoot() {
	setRootEditPart(new PaletteRootEditPart());
}

/**
 * Indicates that the palette should scroll using a native vertical scrollbar as opposed
 * to individual lightweight buttons that appear dynamically on each drawer. The default
 * settings is <code>false</code>. Enabling this setting requires additional horizontal
 * screen space for the scrollbar. Therefore, its use is discouraged.
 * 
 * <P> This setting must be changed prior to calling {@link
 * ScrollingGraphicalViewer#createControl(Composite)}.  After the control is created,
 * changing this setting will have no effect.
 * @param value <code>true</code> if a vertical scrollbar should be displayed
 */
public void enableVerticalScrollbar(boolean value) {
	this.globalScrollbar = true;
}

protected void fireModeChanged() {
	if (paletteListeners == null)
		return;
	for (int listener = 0; listener < paletteListeners.size(); listener++)
		((PaletteListener) paletteListeners.get(listener))
			.activeToolChanged(this, activeEntry);
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
	                                                   paletteRoot);
	}
	return customizerDialog;
}

public ToolEntry getActiveTool() {
	return activeEntry;
}

/**
 * @return The PaletteViewerPreferences that this palette is using to store its
 * 			preferences.  If none has been set, it returns the default one (which
 * 			uses the GEF preference store).
 */
public PaletteViewerPreferences getPaletteViewerPreferences() {
	return prefs;
}

private ToolEntryEditPart getToolEntryEditPart(ToolEntry entry) {
	return (ToolEntryEditPart)getEditPartRegistry().get(entry);
}

/**
 * @see org.eclipse.gef.EditPartViewer#dispose()
 */
protected void handleDispose(DisposeEvent e) {
	super.handleDispose(e);
	prefListener.disposeFont();
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#hookControl()
 */
protected void hookControl() {
	super.hookControl();
	FigureCanvas canvas = getFigureCanvas();
	canvas.getViewport().setContentsTracksWidth(true);
	canvas.getViewport().setContentsTracksHeight(!globalScrollbar);
	canvas.setHorizontalScrollBarVisibility(FigureCanvas.NEVER);
	canvas.setVerticalScrollBarVisibility(
		globalScrollbar ? FigureCanvas.ALWAYS : FigureCanvas.NEVER);
	if (prefs != null)
		prefs.addPropertyChangeListener(prefListener);
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

public void setActiveTool(ToolEntry newMode) {
	if (newMode == null)
		newMode = paletteRoot.getDefaultEntry();
	if (activeEntry != null) {
		getToolEntryEditPart(activeEntry).setToolSelected(false);
	}
	activeEntry = newMode;
	if (activeEntry != null) {
		ToolEntryEditPart editpart = getToolEntryEditPart(activeEntry);
		editpart.setToolSelected(true);
		select(editpart);
		}
	fireModeChanged();
	}

public void setPaletteRoot(PaletteRoot root) {
	paletteRoot = root;
	if (paletteRoot != null) {
		EditPart palette =
			getEditPartFactory().createEditPart(getRootEditPart(), root);
		getRootEditPart().setContents(palette);
	}
}

/**
 * This palette will use the given PaletteViewerPreferences to store all its preferences.
 * <p>
 * NOTE: This method should be invoked by a client only once (before the first time 
 * {@link #getPaletteViewerPreferencesSource()} is invoked).  Trying to invoke this method
 * after that could lead to problems where some preferences would still be stored in the
 * old preference store.
 * 
 * @param	prefs	The PaletteViewerPreferences that is to be used to store all the
 * 					preferences for this palette.
 */
public void setPaletteViewerPreferences(PaletteViewerPreferences prefs) {
	if (this.prefs != null)
		this.prefs.removePropertyChangeListener(prefListener);
	this.prefs = prefs;
	if (getControl() != null && !getControl().isDisposed())
		this.prefs.addPropertyChangeListener(prefListener);
}

/**
 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#unhookControl()
 */
protected void unhookControl() {
	super.unhookControl();
	if (prefs != null)
		prefs.removePropertyChangeListener(prefListener);
}

}
