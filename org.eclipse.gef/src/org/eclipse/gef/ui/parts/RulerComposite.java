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
package org.eclipse.gef.ui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.jface.util.Assert;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;

import org.eclipse.gef.*;
import org.eclipse.gef.internal.ui.rulers.RulerEditPartFactory;
import org.eclipse.gef.internal.ui.rulers.RulerRootEditPart;

/**
 * @author Pratik Shah
 */
public class RulerComposite
	extends Composite
{
	
private GraphicalViewer left, top;
private FigureCanvas editor;
private GraphicalViewer diagramViewer;
private boolean layingOut = false;
private Font font;

public RulerComposite(Composite parent, int style) {
	super(parent, style);
}

private void setRuler(RulerProvider provider, int orientation) {
	Object ruler = null;
	if (provider != null) {
		ruler = provider.getRuler();
	}
	GraphicalViewer container = getRulerContainer(orientation);
	if (container.getContents() != ruler) {
		container.setContents(ruler);
		layout(true);
	}
}

private GraphicalViewer createRulerContainer(int orientation) {
	final boolean isHorizontal = orientation == PositionConstants.NORTH 
			|| orientation == PositionConstants.SOUTH;
	ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer() {
		public void appendSelection(EditPart editpart) {
			boolean setFocus = editpart != focusPart;
			super.appendSelection(editpart);
			if (setFocus) {
				setFocus(editpart);
			}
		}
	};
	viewer.setRootEditPart(new RulerRootEditPart(isHorizontal));
	if (diagramViewer != null) {
		viewer.setEditPartFactory(new RulerEditPartFactory(diagramViewer));	
	}
	viewer.createControl(this);
	viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
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
	if (editor != null) {
		if (isHorizontal) {
			canvas.getViewport().setHorizontalRangeModel(
					editor.getViewport().getHorizontalRangeModel());
		} else {
			canvas.getViewport().setVerticalRangeModel(
					editor.getViewport().getVerticalRangeModel());
		}
	}
	return viewer;
}

public void dispose() {
	super.dispose();
	if (font != null) {
		font.dispose();
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
	if (!layingOut) {
		checkWidget();
		layingOut = true;
		doLayout();	
		layingOut = false;
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
		left.getControl().setBounds(0, topHeight, leftWidth, editorSize.y - hBarHeight);
	}
	if (top != null) {
		top.getControl().setBounds(leftWidth, 0, editorSize.x - vBarWidth, topHeight);
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
 * RulerProvider.HORIZONTAL or RulerProvider.VERTICAL) as a property on the viewer (it
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
	((GraphicalEditPart)diagramViewer.getRootEditPart()).getFigure()
			.setBorder(new EditorBorder());

	if (left != null) {
		left.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
		getFigureCanvas(left).getViewport().setVerticalRangeModel(
				editor.getViewport().getVerticalRangeModel());
	}
	if (top != null) {
		top.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
		getFigureCanvas(top).getViewport().setHorizontalRangeModel(
				editor.getViewport().getHorizontalRangeModel());
	}

	// layout whenever the scrollbars are shown or hidden, and whenever the RulerComposite
	// is resized
	Listener layoutListener = new Listener() {
		public void handleEvent(Event event) {
			layout(true);
		}
	};
	addListener(SWT.Resize, layoutListener);
	editor.getHorizontalBar().addListener(SWT.Show, layoutListener);
	editor.getHorizontalBar().addListener(SWT.Hide, layoutListener);
	editor.getVerticalBar().addListener(SWT.Show, layoutListener);
	editor.getVerticalBar().addListener(SWT.Hide, layoutListener);
	
	diagramViewer.addPropertyChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			String property = evt.getPropertyName();
			if (RulerProvider.HORIZONTAL.equals(property)) {
				setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.HORIZONTAL), 
						PositionConstants.NORTH);
			} else if (RulerProvider.VERTICAL.equals(property)) {
				setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.VERTICAL),
						PositionConstants.WEST);
			}
		}
	});
	setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.HORIZONTAL), 
			PositionConstants.NORTH);
	setRuler((RulerProvider)diagramViewer.getProperty(RulerProvider.VERTICAL), 
			PositionConstants.WEST);
	((GraphicalEditPart)getRulerContainer(PositionConstants.NORTH).getRootEditPart())
			.getFigure().setBorder(new RulerBorder(true));
	((GraphicalEditPart)getRulerContainer(PositionConstants.WEST).getRootEditPart())
			.getFigure().setBorder(new RulerBorder(false));
	diagramViewer.getEditDomain().addViewer(getRulerContainer(PositionConstants.NORTH));
	diagramViewer.getEditDomain().addViewer(getRulerContainer(PositionConstants.WEST));
}

public static class EditorBorder
	extends AbstractBorder
{
	public Insets getInsets(IFigure figure) {
		return new Insets(1, 1, 0, 0);
	}
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		graphics.drawLine(
				figure.getBounds().getTopLeft(), figure.getBounds().getTopRight());
		graphics.drawLine(
				figure.getBounds().getTopLeft(), figure.getBounds().getBottomLeft());
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