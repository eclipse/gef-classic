/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.palette;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.Workbench;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToggleButton;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.gef.ui.views.palette.PaletteView;

/**
 * @author Pratik Shah
 */
public class FlyoutPaletteComposite 
	extends Composite
{
	
public static final int DEFAULT_PALETTE_SIZE = 125;
public static final String PROPERTY_FIXEDSIZE 
		= "org.eclipse.gef.ui.palette.fpa.fixedsize"; //$NON-NLS-1$
public static final String PROPERTY_DEFAULT_STATE
		= "org.eclipse.gef.ui.palette.fpa.initState"; //$NON-NLS-1$

protected static final int MIN_PALETTE_SIZE = 60;
protected static final int MAX_PALETTE_SIZE = 500;
protected static final int FLYOVER_EXPANDED = 1;
public static final int FLYOVER_COLLAPSED = 2;
public static final int FLYOVER_PINNED_OPEN = 4;
protected static final int IN_VIEW = 8;

protected Sash sash;
protected PaletteViewer pViewer, externalViewer;
protected Control graphicalControl;
protected PaletteViewerProvider provider;
protected int dock = PositionConstants.EAST;
protected int paletteState = -1;
protected int defaultState = FLYOVER_COLLAPSED;
private int fixedSize = DEFAULT_PALETTE_SIZE;

/*
 * @TODO:Pratik    perhaps you can make Sash a Composite
 */
protected IPerspectiveListener perspectiveListener = new IPerspectiveListener() {
	public void perspectiveActivated(IWorkbenchPage page, 
			IPerspectiveDescriptor perspective) {
		handlePerspectiveActivated(page, perspective);
	}
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
		handlePerspectiveChanged(page, perspective, changeId);
	}
};

/*
 * @TODO:Pratik  break up this class into a two-class (maybe more?) hierarchy.  one would 
 * simply provide the flyover in the editor.  the other one would hook up to work with
 * the palette view.  AutoHidingPalette and FlyoutPaletteComposite.  Also, maybe you can
 * have a CollapsiblePalette (no flyover), and as the root of the hierarchy, a generic
 * CollapsibleControl (where you can have any control in place of the palette viewer).
 */

/**
 * PropertyChangeSupport
 */
protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

public FlyoutPaletteComposite(Composite parent, int style, IWorkbenchPage page,
		PaletteViewerProvider pvProvider) {
	super(parent, style & SWT.BORDER);
	Assert.isNotNull(pvProvider);
	provider = pvProvider;
	sash = createSash();
	hookIntoWorkbench(page.getWorkbenchWindow());

	addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			Rectangle area = getClientArea();
			if (fixedSize > area.width / 2) {
				setFixedSize(area.width / 2);
			}
			layout();
		}
	});

	IViewPart part = page.findView(PaletteView.ID);
	if (part == null)
		setState(defaultState);
	else
		setState(IN_VIEW);	
}

public void addPropertyChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
}

protected final void addListenerToCtrlHierarchy(Control parent, int eventType, 
		Listener listener) {
	parent.addListener(eventType, listener);
	if (!(parent instanceof Composite))
		return;
	Control[] children = ((Composite)parent).getChildren();
	for (int i = 0; i < children.length; i++) {
		addListenerToCtrlHierarchy(children[i], eventType, listener);
	}
}

protected Sash createSash() {
	return new Sash(this);
}

protected void firePropertyChanged(String property, int oldValue, int newValue) {
	listeners.firePropertyChange(property, oldValue, newValue);
}

protected final int getFixedSize() {
	return fixedSize;
}

/*
 * @TODO:Pratik  handleEditorMaximized and handleEditorMinimized are never invoked
 * because currently there's no mechanism in the platform to detect these actions.
 */
protected void handleEditorMaximized() {
	if (isInState(IN_VIEW))
		setState(defaultState);
}

protected void handleEditorMinimized() {
	handlePerspectiveActivated(
			Workbench.getInstance().getActiveWorkbenchWindow().getActivePage(), null);
}

protected void handlePerspectiveActivated(IWorkbenchPage page, 
		IPerspectiveDescriptor perspective) {
	IViewPart view = page.findView(PaletteView.ID);
	if (view == null && isInState(IN_VIEW))
		setState(defaultState);
	if (view != null && !isInState(IN_VIEW))
		setState(IN_VIEW);
}

protected void handlePerspectiveChanged(IWorkbenchPage page, 
		IPerspectiveDescriptor perspective, String changeId) {
	if (changeId.equals(IWorkbenchPage.CHANGE_VIEW_SHOW) 
			|| changeId.equals(IWorkbenchPage.CHANGE_VIEW_HIDE));
		handlePerspectiveActivated(page, perspective);
}

// Will return false if ancestor or descendant is null
protected final boolean isDescendantOf(Control ancestor, Control descendant) {
	if (descendant == null)
		return false;
	if (ancestor == descendant)
		return true;
	return isDescendantOf(ancestor, descendant.getParent());
}

protected boolean isInState(int state) {
	return (paletteState & state) != 0;
}

public void layout(boolean changed) {
	if (graphicalControl == null || graphicalControl.isDisposed())
		return;
	
	Rectangle area = getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	setRedraw(false);
	int sashWidth = 15; //sash.getBounds().width;
	if (isInState(IN_VIEW)) {
		graphicalControl.moveAbove(sash);
		graphicalControl.setBounds(area);
	} else if (dock == PositionConstants.EAST)
		layoutComponentsEast(area, sashWidth);
	else
		layoutComponentsWest(area, sashWidth);
	setRedraw(true);
	update();
}

protected final void layoutComponentsEast(Rectangle area, int sashWidth) {
	if (isInState(FLYOVER_COLLAPSED)) {
		graphicalControl.moveAbove(sash);
		sash.setBounds(area.x + area.width - sashWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_EXPANDED)) {
		pViewer.getControl().moveAbove(graphicalControl);
		sash.moveAbove(pViewer.getControl());
		pViewer.getControl().setBounds(area.x + area.width - fixedSize, area.y, fixedSize, 
				area.height);
		sash.setBounds(area.x + area.width - fixedSize - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_PINNED_OPEN)) {
		pViewer.getControl().setBounds(area.x + area.width - fixedSize, area.y, fixedSize, 
				area.height);
		sash.setBounds(area.x + area.width - fixedSize - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth - fixedSize, 
				area.height);		
	}
}

protected final void layoutComponentsWest(Rectangle area, int sashWidth) {
	if (isInState(FLYOVER_COLLAPSED)) {
		graphicalControl.moveAbove(sash);
		sash.setBounds(area.x, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y,
				area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_EXPANDED)) {
		pViewer.getControl().moveAbove(graphicalControl);
		sash.moveAbove(pViewer.getControl());
		pViewer.getControl().setBounds(area.x, area.y, fixedSize, area.height);
		sash.setBounds(area.x + fixedSize, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y, 
				area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_PINNED_OPEN)) {
		pViewer.getControl().setBounds(area.x, area.y, fixedSize, area.height);
		sash.setBounds(area.x + fixedSize, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + fixedSize + sashWidth, area.y,
				area.width - sashWidth - fixedSize, area.height);		
	}	
}

protected void hookIntoWorkbench(final IWorkbenchWindow window) {
	window.addPerspectiveListener(perspectiveListener);
	addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			window.removePerspectiveListener(perspectiveListener);
			perspectiveListener = null;
		}
	});
}

public void removePropertyChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

public void setExternalViewer(PaletteViewer viewer) {
	externalViewer = viewer;
	if (externalViewer != null && pViewer != null)
		externalViewer.applyState(pViewer.captureState());
}

public void setDefaultState(int state) {
	if (state != FLYOVER_COLLAPSED && state != FLYOVER_PINNED_OPEN)
		return;
	if (defaultState != state) {
		int oldValue = defaultState;
		defaultState = state;
		if (isInState(oldValue))
			setState(defaultState);
		firePropertyChanged(PROPERTY_DEFAULT_STATE, oldValue, defaultState);
	}
}

public void setDockLocation(int position) {
	if (position != PositionConstants.EAST && position != PositionConstants.WEST)
		return;
	if (position != dock) {
		dock = position;
		layout();
	}
}

public final void setFixedSize(int newSize) {
	int width = getClientArea().width / 2;
	if (width > MIN_PALETTE_SIZE && newSize > width)
		newSize = width;
	if (newSize < MIN_PALETTE_SIZE)
		newSize = MIN_PALETTE_SIZE;
	if (newSize > MAX_PALETTE_SIZE)
		newSize = MAX_PALETTE_SIZE;
	if (fixedSize != newSize) {
		int oldValue = fixedSize;
		fixedSize = newSize;
		firePropertyChanged(PROPERTY_FIXEDSIZE, oldValue, fixedSize);
		if (pViewer != null)
			layout();
	}
}

// should only be invoked once
public void setGraphicalControl(Control graphicalViewer) {
	Assert.isTrue(graphicalViewer.getParent() == this);
	Assert.isTrue(graphicalControl == null);
	Assert.isTrue(graphicalViewer != null);
	graphicalControl = graphicalViewer;
	addListenerToCtrlHierarchy(graphicalControl, SWT.MouseEnter, new Listener() {
		public void handleEvent(Event event) {
			if (!isInState(FLYOVER_EXPANDED))
				return;
			Display.getCurrent().timerExec(250, new Runnable() {
				public void run() {
					if (isDescendantOf(graphicalControl, 
							Display.getCurrent().getCursorControl()) 
							&& isInState(FLYOVER_EXPANDED))
						setState(FLYOVER_COLLAPSED);
				}
			});
		}
	});
}

/**
 * If the auto-hide feature of the palette is to work properly, this method should be
 * called before any other drop target listeners are added to the graphical viewer.
 */
public void hookDropTargetListener(GraphicalViewer viewer) {
	viewer.addDropTargetListener(new TransferDropTargetListener() {
		public void dragEnter(DropTargetEvent event) {
		}
		public void dragLeave(DropTargetEvent event) {
		}
		public void dragOperationChanged(DropTargetEvent event) {
		}
		public void dragOver(DropTargetEvent event) {
		}
		public void drop(DropTargetEvent event) {
		}
		public void dropAccept(DropTargetEvent event) {
		}
		public Transfer getTransfer() {
			return TemplateTransfer.getInstance();
		}
		public boolean isEnabled(DropTargetEvent event) {
			if (isInState(FLYOVER_EXPANDED))
				setState(FLYOVER_COLLAPSED);
			return false;
		}
	});
}

protected void setState(int newState) {
	if (paletteState == newState)
		return;
	paletteState = newState;
	if (isInState(FLYOVER_COLLAPSED | FLYOVER_PINNED_OPEN))
		setDefaultState(newState);
	switch (paletteState) {
		case FLYOVER_EXPANDED:
//			Display.getCurrent().timerExec(250, new Runnable() {
//				public void run() {
//					if (!isInState(FLYOVER_EXPANDED))
//						return;
//					if (isDescendantOf(graphicalControl, 
//							Display.getCurrent().getCursorControl()))
//						setState(FLYOVER_COLLAPSED);
//					else
//						Display.getCurrent().timerExec(250, this);
//				}
//			});
		case FLYOVER_COLLAPSED:
		case FLYOVER_PINNED_OPEN:
			if (pViewer == null) {
				pViewer = provider.createPaletteViewer(this);
				if (externalViewer != null)
					pViewer.applyState(externalViewer.captureState());
			}
			break;
		case IN_VIEW:
			if (pViewer == null)
				break;
			if (externalViewer != null) {
				provider.getEditDomain().setPaletteViewer(externalViewer);
				externalViewer.applyState(pViewer.captureState());
			}
			if (provider.getEditDomain().getPaletteViewer() == pViewer)
				provider.getEditDomain().setPaletteViewer(null);
			if (pViewer.getControl() != null && !pViewer.getControl().isDisposed())
				pViewer.getControl().dispose();
			pViewer = null;
	}
	sash.updateState();
	layout();
}

protected class Sash extends Composite {
	protected Image img;
	protected ToggleButton b;
	public Sash(Composite parent) {
		super(parent, SWT.NONE);
		setFont(JFaceResources.getBannerFont());

		createButton();
		new DragManager();
		
		addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseHover(MouseEvent e) {
				if (isInState(FLYOVER_COLLAPSED))
					setState(FLYOVER_EXPANDED);
			}
		});

		addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				paintSash(event.gc);
			}
		});
	}
	protected void createButton() {
		FigureCanvas figCanvas = new FigureCanvas(this);
		ImageFigure fig = new ImageFigure(DrawerFigure.PIN);
		fig.setAlignment(PositionConstants.NORTH_WEST);
		figCanvas.setCursor(SharedCursors.ARROW);
		b = new ToggleButton(fig);
		b.setRolloverEnabled(true);
		b.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
		b.setLayoutManager(new StackLayout());
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (isInState(FLYOVER_PINNED_OPEN) && !b.isSelected())
					setState(FLYOVER_COLLAPSED);
				else if (isInState(FLYOVER_EXPANDED) && b.isSelected())
					setState(FLYOVER_PINNED_OPEN);
				else if (isInState(FLYOVER_COLLAPSED) && b.isSelected())
					setState(FLYOVER_PINNED_OPEN);
			}
		});
		figCanvas.setContents(b);
		figCanvas.setBounds(1, 1, 13, 13);
	}
	protected void handleSashDragged(int shiftAmount) {
		int newSize = fixedSize + 
				(dock == PositionConstants.EAST ? -shiftAmount : shiftAmount);
		setFixedSize(newSize);
	}
	protected void paintSash(GC gc) {
		SWTGraphics graphics = new SWTGraphics(gc);
		Rectangle bounds = getBounds();
		if (img == null) {
			img = ImageUtilities.createRotatedImageOfString(
					"Palette", graphics.getFont(),
					graphics.getForegroundColor(), graphics.getBackgroundColor());
		}
		graphics.setForegroundColor(ColorConstants.buttonLightest);
		graphics.drawImage(img, 3, bounds.height / 2 - img.getBounds().height / 2);
		graphics.drawLine(0, 0, bounds.width - 1, 0);
		graphics.drawLine(0, 0, 0, bounds.height - 1);
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		graphics.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
		graphics.drawLine(0, bounds.height - 1, bounds.width - 1, bounds.height - 1);
		graphics.dispose();
	}
	protected void updateState() {
		setCursor(isInState(FLYOVER_EXPANDED | FLYOVER_PINNED_OPEN) 
				? SharedCursors.SIZEW : null);
		if (isInState(IN_VIEW))
			b.setSelected(false);
		if (isInState(FLYOVER_PINNED_OPEN))
			b.setSelected(true);
	}
	protected class DragManager 
			extends MouseAdapter 
			implements MouseMoveListener, Listener {
		private boolean dragging = false;
		private int origX;
		public DragManager() {
			Sash.this.addListener(SWT.DragDetect, this);
			Sash.this.addMouseMoveListener(this);
			Sash.this.addMouseListener(this);
		}
		public void handleEvent(Event event) {
			dragging = isInState(FLYOVER_EXPANDED | FLYOVER_PINNED_OPEN);
			origX = event.x;
		}
		public void mouseMove(MouseEvent e) {
			if (dragging)
				handleSashDragged(e.x - origX);
		}
		public void mouseUp(MouseEvent me) {
			dragging = false;
			if (isInState(FLYOVER_COLLAPSED))
				setState(FLYOVER_EXPANDED);
		}
	}
}

}