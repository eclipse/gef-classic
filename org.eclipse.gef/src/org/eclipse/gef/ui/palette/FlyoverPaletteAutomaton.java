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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;

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

import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;
import org.eclipse.gef.ui.views.palette.PaletteView;

/**
 * @author Pratik Shah
 */
public class FlyoverPaletteAutomaton 
	extends Composite
{
	
public static final int DEFAULT_PALETTE_SIZE = 125;
	
private static final int FLYOVER_EXPANDED = 1;
private static final int FLYOVER_COLLAPSED = 2;
private static final int IN_EDITOR = 4;
private static final int IN_VIEW = 8;

private Sash sash;
private int paletteState = IN_VIEW;
private PaletteViewer pViewer;
private Control graphicalControl;
private PaletteViewerProvider provider;
private int fixedSize = DEFAULT_PALETTE_SIZE;

/**
 * PropertyChangeSupport
 */
protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

public static final String PROPERTY_FIXEDSIZE = "org.eclipse.gef.ui.palette.fpa.fixedsize"; //$NON-NLS-1$
private static final int MIN_PALETTE_SIZE = 55;

public FlyoverPaletteAutomaton(Composite parent, int style, IWorkbenchPage page) {
	super(parent, style & SWT.BORDER);
	sash = new Sash(this);

	addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			layout();
		}
	});

	IViewPart part = page.findView(PaletteView.ID);
	if (part == null)
		// Cannot invoke setState() just yet
		paletteState = FLYOVER_COLLAPSED;
	providePartService(page.getWorkbenchWindow().getPartService());
}

private IPartListener2 partListener = new IPartListener2() {
	public void partActivated(IWorkbenchPartReference ref) {
	}
	public void partBroughtToTop(IWorkbenchPartReference ref) {
	}
	public void partClosed(IWorkbenchPartReference ref) {
		if (ref.getId().equals(PaletteView.ID))
			handlePaletteViewClosed(ref);
	}
	public void partDeactivated(IWorkbenchPartReference ref) {
	}
	public void partOpened(IWorkbenchPartReference ref) {
		if (ref.getId().equals(PaletteView.ID))
			handlePaletteViewOpened(ref);
	}
	public void partHidden(IWorkbenchPartReference ref) {
	}
	public void partVisible(IWorkbenchPartReference ref) {
	}
	public void partInputChanged(IWorkbenchPartReference ref) {
	}
};

public void addFixedSizeChangeListener(PropertyChangeListener listener) {
	listeners.addPropertyChangeListener(listener);
}

protected void fireSizeChanged(int oldValue, int newValue) {
	listeners.firePropertyChange(PROPERTY_FIXEDSIZE, oldValue, newValue);
}

protected int getFixedSize() {
	return fixedSize;
}

protected PaletteViewerProvider getPaletteViewerProvider() {
	return provider;
}

protected void handlePaletteViewClosed(IWorkbenchPartReference ref) {
	setState(FLYOVER_COLLAPSED);
}

protected void handlePaletteViewOpened(IWorkbenchPartReference ref) {
	setState(IN_VIEW);
}

protected boolean isInState(int state) {
	return (paletteState & state) != 0;
}

public void layout(boolean changed) {
	Rectangle area = getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	int sashWidth = 15; //sash.getBounds().width;
	if (isInState(IN_VIEW) && graphicalControl != null  && !graphicalControl.isDisposed()) {
		graphicalControl.moveAbove(sash);
		graphicalControl.setBounds(area);
	} else if (isInState(FLYOVER_COLLAPSED)) {
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
	} else if (isInState(IN_EDITOR)) {
		pViewer.getControl().setBounds(area.x, area.y, fixedSize, area.height);
		sash.setBounds(area.x + fixedSize, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + fixedSize + sashWidth, area.y,
				area.width - sashWidth - fixedSize, area.height);		
	}
}

protected void providePartService(final IPartService partService) {
	partService.addPartListener(partListener);
	addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			partService.removePartListener(partListener);
		}
	});
}

public void removeFixedSizeChangeListener(PropertyChangeListener listener) {
	listeners.removePropertyChangeListener(listener);
}

public void setFixedSize(int newSize) {
	if (fixedSize != newSize && newSize >= MIN_PALETTE_SIZE) {
		int oldValue = fixedSize;
		fixedSize = newSize;
		fireSizeChanged(oldValue, fixedSize);
		if (pViewer != null)
			layout();
	}
}

// should only be invoked once
public void setGraphicalControl(Control graphicalViewer) {
	Assert.isTrue(graphicalViewer.getParent() == this);
	graphicalControl = graphicalViewer;
}

public void setPaletteViewerProvider(PaletteViewerProvider pvProvider) {
	if (getPaletteViewerProvider() != null)
		throw new RuntimeException("setPaletteViewerProvider() should only be invoked once.");
	provider = pvProvider;
}

protected void setState(int newState) {
	if (paletteState == newState)
		return;
	paletteState = newState;
	switch (paletteState) {
		case FLYOVER_EXPANDED:
			Display.getCurrent().timerExec(250, new Runnable() {
				public void run() {
					if (!isInState(FLYOVER_EXPANDED))
						return;
					Point pt = Display.getCurrent().getCursorLocation();
					pt = toControl(pt);
					if (sash.getBounds().contains(pt)
							|| pViewer.getControl().getBounds().contains(pt))
						Display.getCurrent().timerExec(250, this);
					else
						setState(FLYOVER_COLLAPSED);
				}
			});
		case FLYOVER_COLLAPSED:
		case IN_EDITOR:
			if (pViewer == null)
				pViewer = getPaletteViewerProvider().createPaletteViewer(this);
			break;
		case IN_VIEW:
			if (pViewer == null)
				break; // this shouldn't happen
			getPaletteViewerProvider().destroyPaletteViewer(pViewer);
			pViewer = null;
	}
	sash.updateState();
	layout();
}

/*
 * @TODO:Pratik  Don't forget to restrict the palette from getting smaller than a certain
 * size.
 */
protected class Sash extends Composite {
	protected FigureCanvas figCanvas;
	protected ToggleButton b;
	protected Image img;
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
		figCanvas = new FigureCanvas(this);
		ImageFigure fig = new ImageFigure(DrawerFigure.PIN);
		fig.setAlignment(PositionConstants.NORTH_WEST);
		figCanvas.setCursor(SharedCursors.ARROW);
		b = new ToggleButton(fig);
		b.setRolloverEnabled(true);
		b.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
		b.setLayoutManager(new StackLayout());
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (isInState(IN_EDITOR) && !b.isSelected())
					setState(FLYOVER_COLLAPSED);
				else if (isInState(FLYOVER_EXPANDED) && b.isSelected())
					setState(IN_EDITOR);
				else if (isInState(FLYOVER_COLLAPSED) && b.isSelected())
					setState(IN_EDITOR);
			}
		});
		figCanvas.setContents(b);
		figCanvas.setBounds(1, 1, 13, 13);
	}
	protected void handleSashDragged(int shiftAmount) {
		setFixedSize(getFixedSize() + shiftAmount);
	}
	protected void paintSash(GC gc) {
		SWTGraphics graphics = new SWTGraphics(gc);
		Rectangle bounds = getBounds();
		if (img == null) {
			img = ImageUtilities.createRotatedImageOfString(
					"Palette", graphics.getFont(),
					graphics.getForegroundColor(), graphics.getBackgroundColor());
		}
		graphics.drawImage(img, 3, bounds.height / 2 - img.getBounds().height / 2);
		graphics.setForegroundColor(ColorConstants.buttonLightest);
		graphics.drawLine(0, 0, bounds.width - 1, 0);
		graphics.drawLine(0, 0, 0, bounds.height - 1);
		graphics.setForegroundColor(ColorConstants.buttonDarker);
		graphics.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
		graphics.drawLine(0, bounds.height - 1, bounds.width - 1, bounds.height - 1);
		graphics.dispose();
	}
	protected void updateState() {
		setCursor(isInState(FLYOVER_EXPANDED | IN_EDITOR) ? SharedCursors.SIZEW : null);
		if (isInState(IN_VIEW))
			b.setSelected(false);
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
			dragging = isInState(FLYOVER_EXPANDED | IN_EDITOR);
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