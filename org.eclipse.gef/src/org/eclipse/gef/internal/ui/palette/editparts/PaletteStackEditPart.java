/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import java.beans.PropertyChangeEvent;

import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.jface.action.MenuManager;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Container;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.actions.SetActivePaletteToolAction;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;

/**
 * The EditPart for a PaletteStack to be used on the toolbar.
 *
 * @author Whitney Sorenson
 * @since 3.0
 */
public class PaletteStackEditPart extends PaletteEditPart implements IPaletteStackEditPart {

	private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);

	// listen to changes of clickable tool figure
	private final ChangeListener clickableListener = event -> {
		if (event.getPropertyName().equals(ButtonModel.MOUSEOVER_PROPERTY)) {
			this.arrowFigure.getModel().setMouseOver(this.activeFigure.getModel().isMouseOver());
		} else if (event.getPropertyName().equals(ButtonModel.ARMED_PROPERTY)) {
			this.arrowFigure.getModel().setArmed(this.activeFigure.getModel().isArmed());
		}
	};

	// listen to changes of arrow figure
	private final ChangeListener clickableArrowListener = event -> {
		if (event.getPropertyName().equals(ButtonModel.MOUSEOVER_PROPERTY)) {
			this.activeFigure.getModel().setMouseOver(this.arrowFigure.getModel().isMouseOver());
		}
		if (event.getPropertyName().equals(ButtonModel.ARMED_PROPERTY)) {
			this.activeFigure.getModel().setArmed(this.arrowFigure.getModel().isArmed());
		}
	};

	// listen to see if arrow is pressed
	private final ActionListener actionListener = event -> openMenu();

	// listen to see if active tool is changed in palette
	private final PaletteListener paletteListener = (palette, tool) -> {
		if (getStack().getChildren().contains(tool)) {
			if (!this.arrowFigure.getModel().isSelected()) {
				this.arrowFigure.getModel().setSelected(true);
			}
			if (!getStack().getActiveEntry().equals(tool)) {
				getStack().setActiveEntry(tool);
			}
		} else {
			this.arrowFigure.getModel().setSelected(false);
		}
	};

	private Clickable activeFigure;
	private RolloverArrow arrowFigure;
	private Figure contentsFigure;
	private Menu menu;

	/**
	 * Creates a new PaletteStackEditPart with the given PaletteStack as its model.
	 *
	 * @param model the PaletteStack to associate with this EditPart.
	 */
	public PaletteStackEditPart(PaletteStack model) {
		super(model);
	}

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		// in case the model is out of sync
		checkActiveEntrySync();
		getViewer().addPaletteListener(paletteListener);
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
		Clickable clickable = null;

		if (newValue != null) {
			part = (GraphicalEditPart) getViewer().getEditPartForModel(newValue);
			clickable = (Clickable) part.getFigure();
			clickable.setVisible(true);
			clickable.addChangeListener(clickableListener);
			activeFigure = clickable;
		} else {
			activeFigure = null;
		}

		if (oldValue != null) {
			part = (GraphicalEditPart) getViewer().getEditPartForModel(oldValue);
			// if part is null, its no longer a child.
			if (part != null) {
				clickable = (Clickable) part.getFigure();
				clickable.setVisible(false);
				clickable.removeChangeListener(clickableListener);
			}
		}
	}

	private void checkActiveEntrySync() {
		if (activeFigure == null) {
			activeEntryChanged(null, getStack().getActiveEntry());
		}
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	public IFigure createFigure() {

		Figure figure = new Container(new BorderLayout()) {
			@Override
			public Dimension getPreferredSize(int wHint, int hHint) {
				if (PaletteStackEditPart.this.getChildren().isEmpty()) {
					return EMPTY_DIMENSION;
				}
				return super.getPreferredSize(wHint, hHint);
			}
		};

		StackLayout stackLayout = new StackLayout();
		// make it so the stack layout does not allow the invisible figures to
		// contribute
		// to its bounds
		stackLayout.setObserveVisibility(true);
		contentsFigure = new Container(stackLayout);
		figure.add(contentsFigure, BorderLayout.CENTER);

		arrowFigure = new RolloverArrow();
		arrowFigure.addChangeListener(clickableArrowListener);
		arrowFigure.addActionListener(actionListener);
		figure.add(arrowFigure, BorderLayout.RIGHT);

		return figure;
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		if (activeFigure != null) {
			activeFigure.removeChangeListener(clickableListener);
		}
		arrowFigure.removeActionListener(actionListener);
		arrowFigure.removeChangeListener(clickableArrowListener);
		getViewer().removePaletteListener(paletteListener);
		super.deactivate();
	}

	/**
	 * @see org.eclipse.gef.EditPart#eraseTargetFeedback(org.eclipse.gef.Request)
	 */
	@Override
	public void eraseTargetFeedback(Request request) {
		getChildren().forEach(ep -> ep.eraseTargetFeedback(request));
		super.eraseTargetFeedback(request);
	}

	/**
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	@Override
	public IFigure getContentPane() {
		return contentsFigure;
	}

	private PaletteStack getStack() {
		return (PaletteStack) getModel();
	}

	/**
	 * Opens the menu to display the choices for the active entry.
	 */
	@Override
	public void openMenu() {
		MenuManager menuManager = new MenuManager();

		getChildren().forEach(part -> {
			PaletteEntry entry = part.getModel();
			menuManager.add(new SetActivePaletteToolAction(getViewer(), entry.getLabel(), entry.getSmallIcon(),
					getStack().getActiveEntry().equals(entry), (ToolEntry) entry));
		});

		menu = menuManager.createContextMenu(getViewer().getControl());

		// make the menu open below the figure
		Rectangle figureBounds = getFigure().getBounds().getCopy();
		getFigure().translateToAbsolute(figureBounds);

		Point menuLocation = getViewer().getControl().toDisplay(figureBounds.getBottomLeft().x,
				figureBounds.getBottomLeft().y);

		// remove feedback from the arrow Figure and children figures
		arrowFigure.getModel().setMouseOver(false);
		eraseTargetFeedback(new Request(RequestConstants.REQ_SELECTION));

		menu.setLocation(menuLocation);
		menu.addMenuListener(new StackMenuListener(menu, getViewer().getControl().getDisplay()));
		menu.setVisible(true);
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PaletteStack.PROPERTY_ACTIVE_ENTRY)) {
			activeEntryChanged(event.getOldValue(), event.getNewValue());
		} else {
			super.propertyChange(event);
		}
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	@Override
	protected void refreshChildren() {
		super.refreshChildren();
		checkActiveEntrySync();
		getChildren().stream().filter(ep -> !ep.getFigure().equals(activeFigure))
				.forEach(ep -> ep.getFigure().setVisible(false));
	}

	/**
	 * @see org.eclipse.gef.EditPart#showTargetFeedback(org.eclipse.gef.Request)
	 */
	@Override
	public void showTargetFeedback(Request request) {
		// if menu is showing, don't show feedback. this is a fix
		// for the occasion when show is called after forced erase
		if (menu != null && !menu.isDisposed() && menu.isVisible()) {
			return;
		}
		getChildren().forEach(part -> part.showTargetFeedback(request));
		super.showTargetFeedback(request);
	}

	@Override
	public PaletteEditPart getActiveEntry() {
		return (PaletteEditPart) getViewer().getEditPartForModel(getStack().getActiveEntry());
	}

}

class StackMenuListener implements MenuListener {

	private Menu menu;
	private final Display d;

	/**
	 * Creates a new listener to listen to the menu that it used to select the
	 * active tool on a stack. Disposes the stack with an asyncExec after hidden is
	 * called.
	 */
	StackMenuListener(Menu menu, Display d) {
		this.menu = menu;
		this.d = d;
	}

	/**
	 * @see org.eclipse.swt.events.MenuListener#menuShown(org.eclipse.swt.events.MenuEvent)
	 */
	@Override
	public void menuShown(MenuEvent e) {
		// currently nothing to do here
	}

	/**
	 * @see org.eclipse.swt.events.MenuListener#menuHidden(org.eclipse.swt.events.MenuEvent)
	 */
	@Override
	public void menuHidden(MenuEvent e) {
		d.asyncExec(() -> {
			if (menu != null) {
				if (!menu.isDisposed()) {
					menu.dispose();
				}
				menu = null;
			}
		});
	}

}

class RolloverArrow extends Clickable {

	private static final Border BORDER_TOGGLE = new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR);

	/**
	 * Creates a new Clickable that paints a triangle figure.
	 */
	RolloverArrow() {
		setRolloverEnabled(true);
		setBorder(BORDER_TOGGLE);
		setBackgroundColor(ColorConstants.black);
		setOpaque(false);
		setPreferredSize(11, -1);
	}

	/**
	 * @return false so that the focus rectangle is not drawn.
	 */
	@Override
	public boolean hasFocus() {
		return false;
	}

	@Override
	public void paintFigure(Graphics graphics) {
		Rectangle rect = getClientArea();

		graphics.translate(getLocation());

		// fill the arrow
		int[] points = new int[8];

		points[0] = 3;
		points[1] = rect.height / 2;
		points[2] = 8;
		points[3] = rect.height / 2;
		points[4] = 5;
		points[5] = 3 + rect.height / 2;
		points[6] = 3;
		points[7] = rect.height / 2;

		graphics.fillPolygon(points);

		graphics.translate(getLocation().getNegated());
	}

}
