/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.DisposeListener;
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
 * A RulerComposite is used to show rulers to the north and west of the control of a
 * given {@link #setGraphicalViewer(ScrollingGraphicalViewer) graphical viewer}.
 * 
 * @author Pratik Shah
 * @since 3.0
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
private boolean needToLayout = false;
private Runnable runnable = new Runnable() {
	public void run() {
		layout(false);
	}
};

/**
 * Constructor
 * 
 * @param parent	a widget which will be the parent of the new instance (cannot be null)
 * @param style		the style of widget to construct
 * @see	Composite#Composite(org.eclipse.swt.widgets.Composite, int)
 */
public RulerComposite(Composite parent, int style) {
	super(parent, style);
	addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			disposeResources();
		}
	});
}

private GraphicalViewer createRulerContainer(int orientation) {
	ScrollingGraphicalViewer viewer = new RulerViewer();
	final boolean isHorizontal = orientation == PositionConstants.NORTH 
			|| orientation == PositionConstants.SOUTH;

	// Finish initializing the viewer
	viewer.setRootEditPart(new RulerRootEditPart(isHorizontal));
	viewer.setEditPartFactory(new RulerEditPartFactory(diagramViewer));
	viewer.createControl(this);
	((GraphicalEditPart)viewer.getRootEditPart()).getFigure()
			.setBorder(new RulerBorder(isHorizontal));
	viewer.setProperty(GraphicalViewer.class.toString(), diagramViewer);
	
	// Configure the viewer's control
	FigureCanvas canvas = (FigureCanvas)viewer.getControl();
	canvas.setScrollBarVisibility(FigureCanvas.NEVER);
	if (font == null) {
		FontData[] data = canvas.getFont().getFontData();
		for (int i = 0; i < data.length; i++) {
			data[i].setHeight(data[i].getHeight() - 1);
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

	// Add the viewer to the rulerEditDomain
	if (rulerEditDomain == null) {
		rulerEditDomain = new EditDomain();
		rulerEditDomain.setCommandStack(diagramViewer.getEditDomain().getCommandStack());
	}
	rulerEditDomain.addViewer(viewer);
	
	return viewer;
}

private void disposeResources() {
	if (diagramViewer != null)
		diagramViewer.removePropertyChangeListener(propertyListener);
	if (font != null)
		font.dispose();
	// layoutListener is not being removed from the scroll bars because they are already
	// disposed at this point.
}

private void disposeRulerViewer(GraphicalViewer viewer) {
	if (viewer == null)
		return;
	/*
	 * There's a tie from the editor's range model to the RulerViewport (via a listener) 
	 * to the RulerRootEditPart to the RulerViewer.  Break this tie so that the viewer 
	 * doesn't leak and can be garbage collected.
	 */
	RangeModel rModel = new DefaultRangeModel();
	Viewport port = ((FigureCanvas)viewer.getControl()).getViewport();
	port.setHorizontalRangeModel(rModel);
	port.setVerticalRangeModel(rModel);
	rulerEditDomain.removeViewer(viewer);
	viewer.getControl().dispose();
}

private void doLayout() {
	if (left == null && top == null) {
		Rectangle area = getClientArea();
		if (!editor.getBounds().equals(area))
			editor.setBounds(area);
		return;
	}
	
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
	if (!editor.getSize().equals(editorSize))
		editor.setSize(editorSize);		
	Point editorLocation = new Point(leftWidth, topHeight);
	if (!editor.getLocation().equals(editorLocation))
		editor.setLocation(editorLocation);

	int vBarWidth = 0, hBarHeight = 0;
	if (editor.getVerticalBar().getVisible())
		vBarWidth = editor.computeTrim(0, 0, 0, 0).width;
	if (editor.getHorizontalBar().getVisible())
		hBarHeight = editor.computeTrim(0, 0, 0, 0).height;
	
	if (left != null) {
		Rectangle leftBounds = new Rectangle(
				0, topHeight - 1, leftWidth, editorSize.y - hBarHeight);
		if (!left.getControl().getBounds().equals(leftBounds))
			left.getControl().setBounds(leftBounds);
	}
	if (top != null) {
		Rectangle topBounds = new Rectangle(
				leftWidth - 1, 0, editorSize.x - vBarWidth, topHeight);
		if (!top.getControl().getBounds().equals(topBounds))
			top.getControl().setBounds(topBounds);
	}
}

private GraphicalViewer getRulerContainer(int orientation) {
	GraphicalViewer result = null;
	switch(orientation) {
		case PositionConstants.NORTH:
			result = top;
			break;
		case PositionConstants.WEST:
			result = left;
	}
	return result;
}

/**
 * @see org.eclipse.swt.widgets.Composite#layout(boolean)
 */
public void layout(boolean change) {
	if (!layingOut && !isDisposed()) {
		checkWidget();
		if (change || needToLayout) {
			needToLayout = false;
			layingOut = true;
			doLayout();	
			layingOut = false;			
		}
	}
}

/**
 * Creates rulers for the given graphical viewer.
 * <p>
 * The primaryViewer or its Control cannot be <code>null</code>.  The primaryViewer's
 * Control should be a FigureCanvas and a child of this Composite.  This method should
 * only be invoked once.
 * <p>
 * To create ruler(s), simply add the RulerProvider(s) (with the right key: 
 * RulerProvider.PROPERTY_HORIZONTAL_RULER or RulerProvider.PROPERTY_VERTICAL_RULER) 
 * as a property on the given viewer (it can be done after this method is invoked).
 * 
 * @param	primaryViewer	The graphical viewer for which the rulers have to be created
 */
public void setGraphicalViewer(ScrollingGraphicalViewer primaryViewer) {
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
			// @TODO:Pratik  If you use Display.asyncExec(runnable) here, some flashing
			// occurs.  You can see it when the palette is in the editor, and you hit
			// the button to show/hide it.
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
				setRuler((RulerProvider)diagramViewer
						.getProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER), 
					PositionConstants.NORTH);
			} else if (RulerProvider.PROPERTY_VERTICAL_RULER.equals(property)) {
				setRuler((RulerProvider)diagramViewer
						.getProperty(RulerProvider.PROPERTY_VERTICAL_RULER),
					PositionConstants.WEST);
			} else if (RulerProvider.PROPERTY_RULER_VISIBILITY.equals(property))
				setRulerVisibility(((Boolean)diagramViewer.getProperty(
						RulerProvider.PROPERTY_RULER_VISIBILITY)).booleanValue());
		}
	};
	diagramViewer.addPropertyChangeListener(propertyListener);
	Boolean rulerVisibility = (Boolean)diagramViewer
			.getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
	if (rulerVisibility != null)
		setRulerVisibility(rulerVisibility.booleanValue());
	setRuler((RulerProvider)diagramViewer.getProperty(
			RulerProvider.PROPERTY_HORIZONTAL_RULER), PositionConstants.NORTH);
	setRuler((RulerProvider)diagramViewer.getProperty(
			RulerProvider.PROPERTY_VERTICAL_RULER), PositionConstants.WEST);
}

private void setRuler(RulerProvider provider, int orientation) {
	Object ruler = null;
	if (isRulerVisible && provider != null)
		// provider.getRuler() might return null (at least the API does not prevent that)
		ruler = provider.getRuler();
	
	if (ruler == null) {
		// Ruler is not visible or is not present
		setRulerContainer(null, orientation);
		// Layout right-away to prevent an empty control from showing
		layout(true);
		return;
	}
	
	GraphicalViewer container = getRulerContainer(orientation);
	if (container == null) {
		container = createRulerContainer(orientation);
		setRulerContainer(container, orientation);
	}
	if (container.getContents() != ruler) {
		container.setContents(ruler);
		needToLayout = true;
		Display.getCurrent().asyncExec(runnable);
	}
}

private void setRulerContainer(GraphicalViewer container, int orientation) {
	if (orientation == PositionConstants.NORTH) {
		if (top == container)
			return;
		disposeRulerViewer(top);
		top = container;		
	} else if (orientation == PositionConstants.WEST) {
		if (left == container)
			return;
		disposeRulerViewer(left);
		left = container;		
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

private static class RulerBorder
	extends AbstractBorder
{
	private static final Insets H_INSETS = new Insets(0, 1, 0, 0);
	private static final Insets V_INSETS = new Insets(1, 0, 0, 0);
	private boolean horizontal;
	/**
	 * Constructor
	 * 
	 * @param isHorizontal	whether or not the ruler being bordered is horizontal or not
	 */
	public RulerBorder(boolean isHorizontal) {
		horizontal = isHorizontal;
	}
	/**
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		return horizontal ? H_INSETS : V_INSETS;
	}
	/**
	 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		if (horizontal) {
			graphics.drawLine(figure.getBounds().getTopLeft(), 
					figure.getBounds().getBottomLeft()
					.translate(new org.eclipse.draw2d.geometry.Point(0, -4)));
		} else {
			graphics.drawLine(figure.getBounds().getTopLeft(), 
					figure.getBounds().getTopRight()
					.translate(new org.eclipse.draw2d.geometry.Point(-4, 0)));
		}
	}
}

/**
 * Custom graphical viewer intended to be used for rulers.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
private static class RulerViewer
	extends ScrollingGraphicalViewer
{
	/**
	 * Constructor
	 */
	public RulerViewer() {
		super();
		init();
	}
	/**
	 * @see org.eclipse.gef.EditPartViewer#appendSelection(org.eclipse.gef.EditPart)
	 */
	public void appendSelection(EditPart editpart) {
		if (editpart instanceof RootEditPart)
			editpart = ((RootEditPart)editpart).getContents();
		setFocus(editpart);
		super.appendSelection(editpart);
	}
	/**
	 * @see org.eclipse.gef.GraphicalViewer#findHandleAt(org.eclipse.draw2d.geometry.Point)
	 */
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
	/**
	 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#init()
	 */
	protected void init() {
		setContextMenu(new RulerContextMenuProvider(this));
		setKeyHandler(new RulerKeyHandler(this));
	}
	/**
	 * Requests to reveal a ruler are ignored since that causes undesired scrolling to
	 * the origin of the ruler
	 * 
	 * @see org.eclipse.gef.EditPartViewer#reveal(org.eclipse.gef.EditPart)
	 */
	public void reveal(EditPart part) {
		if (part != getContents())
			super.reveal(part);
	}
	/**
	 * @see org.eclipse.gef.EditPartViewer#setContents(org.eclipse.gef.EditPart)
	 */
	public void setContents(EditPart editpart) {
		super.setContents(editpart);
		setFocus(getContents());
	}
	/**
	 * Custom KeyHandler intended to be used with a RulerViewer
	 * 
	 * @author Pratik Shah
	 * @since 3.0
	 */
	protected static class RulerKeyHandler extends GraphicalViewerKeyHandler {
		/**
		 * Constructor
		 * 
		 * @param viewer	The viewer for which this handler processes keyboard input
		 */
		public RulerKeyHandler(GraphicalViewer viewer) {
			super(viewer);
		}
		/**
		 * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
		 */
		public boolean keyPressed(KeyEvent event) {
			if (event.keyCode == SWT.DEL) {
				// If a guide has focus, delete it
				if (getFocusEditPart() instanceof GuideEditPart) {
					RulerEditPart parent = 
							(RulerEditPart)getFocusEditPart().getParent();
					getViewer().getEditDomain().getCommandStack().execute(
							parent.getRulerProvider().getDeleteGuideCommand(
							getFocusEditPart().getModel()));
					event.doit = false;
					return true;
				}
				return false;
			} else if (((event.stateMask & SWT.ALT) != 0)
					&& (event.keyCode == SWT.ARROW_UP)) {
				// ALT + UP_ARROW pressed
				// If a guide has focus, give focus to the ruler
				EditPart parent = getFocusEditPart().getParent();
				if (parent instanceof RulerEditPart)
					navigateTo(getFocusEditPart().getParent(), event);
				return true;
			}
			return super.keyPressed(event);
		}
	}
}

}