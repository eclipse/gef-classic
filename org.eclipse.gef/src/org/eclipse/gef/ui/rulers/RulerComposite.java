/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;

import org.eclipse.gef.*;
import org.eclipse.gef.internal.ui.rulers.*;
import org.eclipse.gef.rulers.*;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

/**
 * @author Pratik Shah
 */
public class RulerComposite
	extends Composite
{
	
private EditDomain rulerEditDomain;
private GraphicalViewer left, top;
private FigureCanvas editor;
private GraphicalViewer diagramViewer;
private Font font;
private Listener layoutListener;
private PropertyChangeListener propertyListener;
private boolean layingOut = false;
private boolean isRulerVisible = true;

public RulerComposite(Composite parent, int style) {
	super(parent, style);
}

private GraphicalViewer createRulerContainer(int orientation) {
	final boolean isHorizontal = orientation == PositionConstants.NORTH 
			|| orientation == PositionConstants.SOUTH;
	ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer() {
		public void appendSelection(EditPart editpart) {
			if (editpart instanceof RootEditPart)
				editpart = ((RootEditPart)editpart).getContents();
			setFocus(editpart);
			super.appendSelection(editpart);
		}
		public Handle findHandleAt(org.eclipse.draw2d.geometry.Point p) {
			final GraphicalEditPart gep = 
					(GraphicalEditPart)findObjectAtExcluding(p, new ArrayList());
			if (gep == null || !(gep instanceof GuideEditPart))
				return null;
			return new Handle() {
				public DragTracker getDragTracker() {
					return ((GuideEditPart)gep).getDragTracker(null);
				}
				public org.eclipse.draw2d.geometry.Point getAccessibleLocation() {
					return null;
				}
			};
		}
		public void reveal(EditPart part) {
			// there's no need to reveal rulers (that causes undesired scrolling to
			// the origin of the ruler)
			if (part instanceof GuideEditPart)
				super.reveal(part);
		}
		public void setContents(EditPart editpart) {
			super.setContents(editpart);
			setFocus(getContents());
		}
	};
	viewer.setRootEditPart(new RulerRootEditPart(isHorizontal));
	viewer.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
	viewer.createControl(this);
	viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer) {
		public boolean keyPressed(KeyEvent event) {
			/*
			 * @TODO:Pratik     the correct way to handle this is by having a "Delete
			 * Guide" action and having the DEL key associated with it.  then, 
			 * performStroke() will take care of it.  just like in the logic editor.
			 */
			if (event.keyCode == SWT.DEL) {
				if (getFocusEditPart() instanceof GuideEditPart) {
					RulerEditPart parent = (RulerEditPart)getFocusEditPart().getParent();
					getViewer().getEditDomain().getCommandStack().execute(
							parent.getRulerProvider().getDeleteGuideCommand(
							getFocusEditPart().getModel()));
					return true;
				} else {
					return false;
				}
			} else if (((event.stateMask & SWT.ALT) != 0) 
					&& (event.keyCode == SWT.ARROW_UP)) {
				// ALT + UP_ARROW pressed
				EditPart parent = getFocusEditPart().getParent();
				if (parent instanceof RulerEditPart)
					navigateTo(getFocusEditPart().getParent(), event);
				return true;
			}
			return super.keyPressed(event);
		}
	});
	((GraphicalEditPart)viewer.getRootEditPart()).getFigure()
			.setBorder(new RulerBorder(isHorizontal));
	viewer.setProperty(GraphicalViewer.class.toString(), diagramViewer);
	FigureCanvas canvas = getFigureCanvas(viewer);
	canvas.setScrollBarVisibility(FigureCanvas.NEVER);
	if (font == null) {
		FontData[] data = canvas.getFont().getFontData();
		for (int i = 0; i < data.length; i++) {
			data[i].height -= 1;
		}
		font = new Font(Display.getCurrent(), data);
	}
	canvas.setFont(font);
	if (isHorizontal) {
		canvas.getViewport().setHorizontalRangeModel(
				editor.getViewport().getHorizontalRangeModel());
	} else {
		canvas.getViewport().setVerticalRangeModel(
				editor.getViewport().getVerticalRangeModel());
	}
	
	if (rulerEditDomain == null) {
		rulerEditDomain = new EditDomain();
		rulerEditDomain.setCommandStack(diagramViewer.getEditDomain().getCommandStack());
	}
	rulerEditDomain.addViewer(viewer);
	return viewer;
}

public void dispose() {
	if (diagramViewer != null) {
		diagramViewer.removePropertyChangeListener(propertyListener);
		editor.getHorizontalBar().removeListener(SWT.Show, layoutListener);
		editor.getHorizontalBar().removeListener(SWT.Hide, layoutListener);
		editor.getVerticalBar().removeListener(SWT.Show, layoutListener);
		editor.getVerticalBar().removeListener(SWT.Hide, layoutListener);
	}
	super.dispose();
	if (font != null) {
		font.dispose();
	}
}

private void doLayout() {
	int leftWidth, rightWidth, topHeight, bottomHeight;
	leftWidth = left == null ? 0 
			: left.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
	rightWidth = 0; 
	topHeight = top == null ? 0 
			: top.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
	bottomHeight = 0; 

	Point size = getSize();
	Point editorSize = new Point(size.x - (leftWidth + rightWidth), 
						   size.y - (topHeight + bottomHeight));
	if (!editor.getSize().equals(editorSize)) {
		editor.setSize(editorSize);		
	}
	Point editorLocation = new Point(leftWidth, topHeight);
	if (!editor.getLocation().equals(editorLocation)) {
		editor.setLocation(editorLocation);
	}

	int vBarWidth = 0, hBarHeight = 0;
	if (editor.getVerticalBar().getVisible()) {
		vBarWidth = editor.computeTrim(0, 0, 0, 0).width;
	}
	if (editor.getHorizontalBar().getVisible()) {
		hBarHeight = editor.computeTrim(0, 0, 0, 0).height;
	}
	
	if (left != null) {
		left.getControl().setBounds(0, topHeight - 1, leftWidth, editorSize.y - hBarHeight);
	}
	if (top != null) {
		top.getControl().setBounds(leftWidth - 1, 0, editorSize.x - vBarWidth, topHeight);
	}
}

private GraphicalViewer getRulerContainer(int orientation) {
	GraphicalViewer result = null;
	switch(orientation) {
		case PositionConstants.NORTH:
			if (top == null) {
				top = createRulerContainer(orientation);
			}
			result = top;
			break;
		case PositionConstants.WEST:
			if (left == null) {
				left = createRulerContainer(orientation);
			}
			result = left;
	}
	return result;
}

/* (non-Javadoc)
 * @see org.eclipse.swt.widgets.Composite#layout(boolean)
 */
public void layout(boolean change) {
	if (!layingOut && !isDisposed()) {
		checkWidget();
		layingOut = true;
		doLayout();	
		layingOut = false;
	}
}

private FigureCanvas getFigureCanvas(GraphicalViewer viewer) {
	return (FigureCanvas)viewer.getControl();
}

/**
 * The primaryViewer or its Control cannot be <code>null</code>.  The primaryViewer's
 * Control should be a FigureCanvas and a child of this Composite.  This method should
 * only be invoked once.
 * <p>
 * To create ruler(s), simply add the RulerProvider(s) (with the right key: 
 * RulerProvider.PROPERTY_HORIZONTAL_RULER or RulerProvider.PROPERTY_VERTICAL_RULER) as a property on the viewer (it
 * can be done after this method is invoked).
 * 
 * @param	primaryViewer	The GraphicalViewer for which the rulers have to be created.
 */
public void setGraphicalViewer(GraphicalViewer primaryViewer) {
	// pre-conditions
	Assert.isNotNull(primaryViewer);
	Assert.isNotNull(primaryViewer.getControl());
	Assert.isTrue(diagramViewer == null);
	
	diagramViewer = primaryViewer;
	editor = (FigureCanvas)diagramViewer.getControl();

	// layout whenever the scrollbars are shown or hidden, and whenever the RulerComposite
	// is resized
	layoutListener = new Listener() {
		public void handleEvent(Event event) {
			layout(true);
		}
	};
	addListener(SWT.Resize, layoutListener);
	editor.getHorizontalBar().addListener(SWT.Show, layoutListener);
	editor.getHorizontalBar().addListener(SWT.Hide, layoutListener);
	editor.getVerticalBar().addListener(SWT.Show, layoutListener);
	editor.getVerticalBar().addListener(SWT.Hide, layoutListener);

	propertyListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			String property = evt.getPropertyName();
			if (RulerProvider.PROPERTY_HORIZONTAL_RULER.equals(property)) {
				setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER), 
					PositionConstants.NORTH);
			} else if (RulerProvider.PROPERTY_VERTICAL_RULER.equals(property)) {
				setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER),
					PositionConstants.WEST);
			} else if (RulerProvider.PROPERTY_RULER_VISIBILITY.equals(property))
				setRulerVisibility(((Boolean)diagramViewer
						.getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY)).booleanValue());
		}
	};
	diagramViewer.addPropertyChangeListener(propertyListener);
	Boolean rulerVisibility = (Boolean)diagramViewer
			.getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
	if (rulerVisibility != null)
		setRulerVisibility(rulerVisibility.booleanValue());
	setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER), 
			PositionConstants.NORTH);
	setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER), 
			PositionConstants.WEST);
}

private void setRuler(RulerProvider provider, int orientation) {
	Object ruler = null;
	if (isRulerVisible && provider != null) {
		ruler = provider.getRuler();
	}
	GraphicalViewer container = getRulerContainer(orientation);
	if (container.getContents() != ruler) {
		container.setContents(ruler);
		Display.getCurrent().asyncExec(new Runnable() {
			public void run() {
				layout(true);
			}
		});
	}
}

private void setRulerVisibility(boolean isVisible) {
	if (isRulerVisible != isVisible) {
		isRulerVisible = isVisible;
		if (diagramViewer != null) {
			setRuler((RulerProvider)diagramViewer.getProperty(
					RulerProvider.PROPERTY_HORIZONTAL_RULER), PositionConstants.NORTH);
			setRuler((RulerProvider)diagramViewer.getProperty(
					RulerProvider.PROPERTY_VERTICAL_RULER), PositionConstants.WEST);
		}
	}
}

public static class RulerBorder
	extends AbstractBorder
{
	private static final Insets H_INSETS = new Insets(0, 1, 0, 0);
	private static final Insets V_INSETS = new Insets(1, 0, 0, 0);
	private boolean horizontal;
	public RulerBorder(boolean isHorizontal) {
		horizontal = isHorizontal;
	}
	public Insets getInsets(IFigure figure) {
		return horizontal ? H_INSETS : V_INSETS;
	}
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		if (horizontal) {
			graphics.drawLine(figure.getBounds().getTopLeft(), 
					figure.getBounds().getBottomLeft()
					.translate(new org.eclipse.draw2d.geometry.Point(0, -3)));
		} else {
			graphics.drawLine(figure.getBounds().getTopLeft(), 
					figure.getBounds().getTopRight()
					.translate(new org.eclipse.draw2d.geometry.Point(-3, 0)));
		}
	}
}

}