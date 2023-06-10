/*******************************************************************************
 * Copyright (c) 2008, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.editparts.IPinnableEditPart;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;

/**
 * The EditPart for a pinnable PaletteStack to be used in a drawer or group.
 * Some of this code has been take from <code>PaletteStackEditPart</code>, but
 * they are significantly different to warrant two editpart classes.
 * 
 * @author Whitney Sorenson, crevells
 * @since 3.4
 */
public class PinnablePaletteStackEditPart extends PaletteEditPart implements IPaletteStackEditPart, IPinnableEditPart {

	// listen to see if active tool is changed in the palette
	private PaletteListener paletteListener = new PaletteListener() {

		public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
			if (!getFigure().isPinnedOpen() && getStack().getChildren().contains(tool)) {
				if (!getStack().getActiveEntry().equals(tool)) {
					getStack().setActiveEntry(tool);
				}
			}
			if (!getFigure().isPinnedOpen()) {
				getFigure().setExpanded(false);
			}
		}
	};

	/**
	 * Creates a new PaletteStackEditPart with the given PaletteStack as its model.
	 * 
	 * @param model the PaletteStack to associate with this EditPart.
	 */
	public PinnablePaletteStackEditPart(PaletteStack model) {
		super(model);
	}

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		// in case the model is out of sync
		checkActiveEntrySync();
		getPaletteViewer().addPaletteListener(paletteListener);
		super.activate();
	}

	/**
	 * Called when the active entry has changed.
	 * 
	 * @param oldValue the old model value (can be null)
	 * @param newValue the new model value (can be null)
	 */
	private void activeEntryChanged(Object oldValue, Object newValue) {
		GraphicalEditPart part = null;
		IFigure oldFigure = null;
		IFigure newFigure = null;
		int index = -1;

		if (oldValue != null) {
			part = (GraphicalEditPart) getViewer().getEditPartRegistry().get(oldValue);
			// if part is null, its no longer a child.
			if (part != null) {
				oldFigure = part.getFigure();

				// preserve the original order of the palette stack children
				index = getModelChildren().indexOf(part.getModel());
			}
		}

		if (newValue != null) {
			part = (GraphicalEditPart) getViewer().getEditPartRegistry().get(newValue);
			newFigure = part.getFigure();
		}

		getFigure().activeEntryChanged(oldFigure, index, newFigure);
	}

	private void checkActiveEntrySync() {
		if (getFigure().getActiveFigure() == null)
			activeEntryChanged(null, getStack().getActiveEntry());
	}

	@Override
	public IFigure createFigure() {
		return new PinnablePaletteStackFigure();
	}

	@Override
	public PinnablePaletteStackFigure getFigure() {
		return (PinnablePaletteStackFigure) super.getFigure();
	}

	@Override
	public void deactivate() {
		getPaletteViewer().removePaletteListener(paletteListener);
		super.deactivate();
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		getChildren().forEach(part -> part.eraseTargetFeedback(request));
		super.eraseTargetFeedback(request);
	}

	@Override
	public IFigure getContentPane() {
		// not completely accurate, but is there any other way?
		return getFigure().getContentPane();
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
		getFigure().getContentPane(child).remove(child);
	}

	@Override
	protected void addChild(EditPart childEP, int index) {
		index = updateIndexBasedOnActiveFigure(index, childEP);
		super.addChild(childEP, index);
	}

	@Override
	protected void reorderChild(EditPart childEP, int index) {
		IFigure childFigure = ((GraphicalEditPart) childEP).getFigure();
		if (childFigure == getFigure().getActiveFigure()) {
			// no need to reorder figures if this is the active figure
			reorderChildInChildrenList((PaletteEditPart) childEP, index);
		} else {
			removeChildVisual(childEP);
			reorderChildInChildrenList((PaletteEditPart) childEP, index);
			index = updateIndexBasedOnActiveFigure(index, childEP);
			addChildVisual(childEP, index);
		}
	}

	private void reorderChildInChildrenList(PaletteEditPart childEP, int index) {
		@SuppressWarnings("unchecked")
		List<PaletteEditPart> children = (List<PaletteEditPart>) getChildren();
		children.remove(childEP);
		children.add(index, childEP);
	}

	private int updateIndexBasedOnActiveFigure(int index, EditPart childEP) {
		for (int i = 0; i < index; i++) {
			PaletteEditPart ep = getChildren().get(i);
			if (ep.getFigure() == getFigure().getActiveFigure()) {
				return index - 1;
			}
		}
		return index;
	}

	private PaletteStack getStack() {
		return (PaletteStack) getModel();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PaletteStack.PROPERTY_ACTIVE_ENTRY))
			activeEntryChanged(event.getOldValue(), event.getNewValue());
		else
			super.propertyChange(event);
	}

	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		checkActiveEntrySync();
	}

	@Override
	protected void refreshVisuals() {
		getFigure().setLayoutMode(getLayoutSetting());
	}

	public void openMenu() {
		setExpanded(true);
	}

	public void setExpanded(boolean value) {
		getFigure().setExpanded(value);
	}

	public boolean isExpanded() {
		return getFigure().isExpanded();
	}

	public boolean canBePinned() {
		return isExpanded();
	}

	public boolean isPinnedOpen() {
		return getFigure().isPinnedOpen();
	}

	public void setPinnedOpen(boolean pinned) {
		getFigure().setPinned(pinned);
	}

	public PaletteEditPart getActiveEntry() {
		return (PaletteEditPart) getViewer().getEditPartRegistry().get(getStack().getActiveEntry());
	}

}
