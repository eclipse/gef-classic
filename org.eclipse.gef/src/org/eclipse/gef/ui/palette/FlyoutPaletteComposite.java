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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tracker;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.Workbench;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.PositionConstants;
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

protected static final int MIN_PALETTE_SIZE = 20;
protected static final int MAX_PALETTE_SIZE = 500;
protected static final int IN_VIEW = 8;
protected static final int FLYOVER_EXPANDED = 1;
public static final int FLYOVER_COLLAPSED = 2;
public static final int FLYOVER_PINNED_OPEN = 4;

protected Sash sash;
protected PaletteViewer pViewer, externalViewer;
protected Control graphicalControl;
protected PaletteViewerProvider provider;
protected int dock = PositionConstants.EAST;
protected int paletteState = -1;
protected int defaultState = FLYOVER_COLLAPSED;
private int fixedSize = DEFAULT_PALETTE_SIZE;
private int minWidth = MIN_PALETTE_SIZE;

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
			// Sometimes, the editor is resized to 1,1 or 0,0 (depending on the platform)
			// when the editor is closed or maximized.  We have to ignore such resizes.
			if (area.width < minWidth)
				return;
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
			|| changeId.equals(IWorkbenchPage.CHANGE_VIEW_HIDE))
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
	
	int sashWidth = 15; //sash.getBounds().width; @TODO:Pratik
	int paletteWidth = fixedSize;
	int maxWidth = Math.min(area.width / 2, MAX_PALETTE_SIZE);
	maxWidth = Math.max(maxWidth, minWidth);
	paletteWidth = Math.max(paletteWidth, minWidth);
	paletteWidth = Math.min(paletteWidth, maxWidth);
	
	setRedraw(false);
	if (isInState(IN_VIEW)) {
		sash.setVisible(false);
		graphicalControl.setBounds(area);
	} else if (dock == PositionConstants.EAST)
		layoutComponentsEast(area, sashWidth, paletteWidth);
	else
		layoutComponentsWest(area, sashWidth, paletteWidth);
	setRedraw(true);
	update();
}

/*
 * @TODO:Pratik  Perhaps also separate this class out.  not have this as a composite?
 */

protected final void layoutComponentsEast(Rectangle area, int sashWidth, 
		int paletteWidth) {
	if (isInState(FLYOVER_COLLAPSED)) {
		sash.setVisible(true);
		pViewer.getControl().setVisible(false);
		sash.setBounds(area.x + area.width - sashWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_EXPANDED)) {
		sash.setVisible(true);
		pViewer.getControl().setVisible(true);
		pViewer.getControl().moveAbove(graphicalControl);
		sash.moveAbove(pViewer.getControl());
		pViewer.getControl().setBounds(area.x + area.width - paletteWidth, area.y, 
				paletteWidth, area.height);
		sash.setBounds(area.x + area.width - paletteWidth - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_PINNED_OPEN)) {
		sash.setVisible(true);
		pViewer.getControl().setVisible(true);
		pViewer.getControl().setBounds(area.x + area.width - paletteWidth, area.y, 
				paletteWidth, area.height);
		sash.setBounds(area.x + area.width - paletteWidth - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth - paletteWidth, 
				area.height);		
	}
}

protected final void layoutComponentsWest(Rectangle area, int sashWidth, 
		int paletteWidth) {
	if (isInState(FLYOVER_COLLAPSED)) {
		sash.setVisible(true);
		pViewer.getControl().setVisible(false);
		sash.setBounds(area.x, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y,
				area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_EXPANDED)) {
		sash.setVisible(true);
		pViewer.getControl().setVisible(true);
		pViewer.getControl().moveAbove(graphicalControl);
		sash.moveAbove(pViewer.getControl());
		pViewer.getControl().setBounds(area.x, area.y, paletteWidth, area.height);
		sash.setBounds(area.x + paletteWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y, 
				area.width - sashWidth, area.height);
	} else if (isInState(FLYOVER_PINNED_OPEN)) {
		sash.setVisible(true);
		pViewer.getControl().setVisible(true);
		pViewer.getControl().setBounds(area.x, area.y, paletteWidth, area.height);
		sash.setBounds(area.x + paletteWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + paletteWidth + sashWidth, area.y,
				area.width - sashWidth - paletteWidth, area.height);		
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
	if (externalViewer != null && pViewer != null) {
		transferState(pViewer, externalViewer);
	}
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
	// @TODO:Pratik   persist the dock location.  fire notification for it.
	if (position != PositionConstants.EAST && position != PositionConstants.WEST)
		return;
	if (position != dock) {
		dock = position;
		layout();
	}
}

public final void setFixedSize(int newSize) {
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
		case FLYOVER_COLLAPSED:
		case FLYOVER_PINNED_OPEN:
			if (pViewer == null) {
				pViewer = provider.createPaletteViewer(this);
				if (externalViewer != null) {
					transferState(externalViewer, pViewer);
				}
				minWidth = Math.max(pViewer.getControl().computeSize(0, 0).x, 
						MIN_PALETTE_SIZE);
			}
			break;
		case IN_VIEW:
			if (pViewer == null)
				break;
			if (externalViewer != null) {
				provider.getEditDomain().setPaletteViewer(externalViewer);
				transferState(pViewer, externalViewer);
			}
			if (provider.getEditDomain().getPaletteViewer() == pViewer)
				provider.getEditDomain().setPaletteViewer(null);
			if (pViewer.getControl() != null && !pViewer.getControl().isDisposed())
				pViewer.getControl().dispose();
			pViewer = null;
	}
	layout();
	sash.updateState();
}

protected void transferState(PaletteViewer src, PaletteViewer dest) {
	IMemento memento = XMLMemento.createWriteRoot("paletteState"); //$NON-NLS-1$
	src.saveState(memento);
	dest.restoreState(memento);
}

protected class Sash extends Composite {
	protected ToggleButton b;
	protected MenuManager manager;
	protected IAction resizeAction;
	public Sash(Composite parent) {
		super(parent, SWT.NONE);

		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = false;
		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 1;
		layout.marginRight = 1;
		layout.spacing = 5;
		setLayout(layout);
		createButton();
		createTitle();
		new SashDragManager();
		
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
		figCanvas.setCursor(SharedCursors.ARROW);
		figCanvas.setScrollBarVisibility(FigureCanvas.NEVER);
		figCanvas.setLayoutData(new RowData());
		ImageFigure fig = new ImageFigure(DrawerFigure.PIN);
		fig.setAlignment(PositionConstants.NORTH_WEST);
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
	protected void createTitle() {
		FigureCanvas figCanvas = new FigureCanvas(this);
		figCanvas.setCursor(SharedCursors.SIZEALL);
		figCanvas.setScrollBarVisibility(FigureCanvas.NEVER);
		figCanvas.setLayoutData(new RowData());
		figCanvas.setContents(new DragFigure(SWT.VERTICAL));
		new TitleDragManager(figCanvas);
		manager = new MenuManager();
		manager.add(resizeAction = new ResizeAction());
		manager.add(new DockAction());
		figCanvas.setMenu(manager.createContextMenu(figCanvas));
	}
	protected void handleSashDragged(int shiftAmount) {
		int newSize = pViewer.getControl().getBounds().width + 
				(dock == PositionConstants.EAST ? -shiftAmount : shiftAmount);
		setFixedSize(newSize);
	}
	protected void paintSash(GC gc) {
		Rectangle bounds = getBounds();
		gc.setForeground(ColorConstants.buttonLightest);
		gc.drawRectangle(bounds);
		gc.setForeground(ColorConstants.buttonDarker);
		if (dock == PositionConstants.EAST) {
			gc.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
			gc.drawLine(0, bounds.height - 1, bounds.width - 1, bounds.height - 1);
		} else {
			gc.drawLine(0, 0, 0, bounds.height - 1);
			gc.drawLine(0, 0, bounds.width - 1, 0);
		}
	}
	protected void updateState() {
		setCursor(isInState(FLYOVER_EXPANDED | FLYOVER_PINNED_OPEN) 
				? SharedCursors.SIZEW : null);
		if (isInState(IN_VIEW))
			b.setSelected(false);
		if (isInState(FLYOVER_PINNED_OPEN))
			b.setSelected(true);
		resizeAction.setEnabled(resizeAction.isEnabled());
	}
	
	protected class SashDragManager 
			extends MouseAdapter 
			implements MouseMoveListener, Listener {
		protected boolean dragging = false;
		protected boolean correctState = false;
		protected int origX;
		public SashDragManager() {
			Sash.this.addListener(SWT.DragDetect, this);
			Sash.this.addMouseMoveListener(this);
			Sash.this.addMouseListener(this);
		}
		public void handleEvent(Event event) {
			dragging = true;
			correctState = isInState(FLYOVER_EXPANDED | FLYOVER_PINNED_OPEN);
			origX = event.x;
		}
		public void mouseMove(MouseEvent e) {
			if (dragging && correctState)
				handleSashDragged(e.x - origX);
		}
		public void mouseUp(MouseEvent me) {
			if (!dragging && me.button == 1)
				if (isInState(FLYOVER_COLLAPSED))
					setState(FLYOVER_EXPANDED);
				else if (isInState(FLYOVER_EXPANDED))
					setState(FLYOVER_COLLAPSED);
			dragging = false;
			correctState = false;
		}
	}
	
	/*
	 * @TODO:Pratik   with these drag managers, if you lose drag without
	 * receiving mouse up, everything will break.  it can happen when debugging
	 * if it comes across a breakpoint.  talk to randy and find out what valid end
	 * user scenarios are there.
	 */
	
	protected class TitleDragManager 
			extends MouseAdapter 
			implements MouseMoveListener, Listener, KeyListener {
		protected boolean dragging = false;
		protected boolean dragCancelled = false;
		protected boolean switchDock = false;
		protected Rectangle origBounds;
		public TitleDragManager(Control ctrl) {
			ctrl.addListener(SWT.DragDetect, this);
			ctrl.addMouseMoveListener(this);
			ctrl.addMouseListener(this);
			ctrl.addKeyListener(this);
		}
		public void handleEvent(Event event) {
			dragging = true;
			dragCancelled = false;
			switchDock = false;
			origBounds = Sash.this.getBounds();
			if (pViewer != null && pViewer.getControl().isVisible())
				origBounds = origBounds.union(pViewer.getControl().getBounds());
		}
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.ESC)
				dragCancelled = true;
		}
		public void keyReleased(KeyEvent e) {}
		public void mouseMove(MouseEvent e) {
			if (!dragging)
				return;
			Control ctrl = Display.getCurrent().getCursorControl();
			switchDock = !isDescendantOf(Sash.this, ctrl);
			if (pViewer != null && pViewer.getControl().isVisible()) {
				switchDock &= isDescendantOf(pViewer.getControl(), ctrl);
			}
			Rectangle placeHolder = origBounds;
			if (switchDock) {
				if (dock == PositionConstants.EAST) {
					placeHolder = new Rectangle(0, 0,
							origBounds.width, origBounds.height);
				} else {
					placeHolder = new Rectangle(
							FlyoutPaletteComposite.this.getBounds().width 
							- origBounds.width, 0, origBounds.width, origBounds.height);
				}
			}
			// @TODO:Pratik  need to draw an outline at placeholder
		}
		public void mouseUp(MouseEvent me) {
			if (dragging) {
				if (!dragCancelled && switchDock)
					if (dock == PositionConstants.EAST)
						setDockLocation(PositionConstants.WEST);
					else
						setDockLocation(PositionConstants.EAST);
			} else if (isInState(FLYOVER_COLLAPSED) && me.button == 1)
				// if this was just a simple click (no dragging) and the palette's 
				// collapsed, expand it
				setState(FLYOVER_EXPANDED);
			else if (isInState(FLYOVER_EXPANDED) && me.button == 1)
				// if this was just a simple click (no dragging) and the palette's
				// expanded, collapse it
				setState(FLYOVER_COLLAPSED);
			dragging = false;
			dragCancelled = false;
			switchDock = false;
			origBounds = null;
		}
	}
	
	protected class DockAction 
		extends Action 
		implements IMenuCreator
	{
		private Menu menu;
		public DockAction() {
			setMenuCreator(this);
			setText(PaletteMessages.DOCK_LABEL);
		}		
		private void addActionToMenu(Menu parent, IAction action) {
			ActionContributionItem item = new ActionContributionItem(action);
			item.fill(parent, -1);
		}
		public void dispose() {
			// Menu should already be disposed, but this is a precaution
			if (menu != null  && !menu.isDisposed()) {
				menu.dispose();
			}
			menu = null;
		}
		public Menu getMenu(Control parent) {
			return null;
		}
		public Menu getMenu(Menu parent) {
			if (menu == null) {
				menu = new Menu(parent);
				addActionToMenu(menu, new ChangeDockAction(
						PaletteMessages.LEFT_LABEL, PositionConstants.WEST));
				addActionToMenu(menu, new ChangeDockAction(
						PaletteMessages.RIGHT_LABEL, PositionConstants.EAST));
			}
			return menu;
		}
		private class ChangeDockAction extends Action {
			private int position;
			public ChangeDockAction(String text, int position) {
				super(text, IAction.AS_RADIO_BUTTON);
				this.position = position;
			}
			public boolean isChecked() {
				return dock == position;
			}
			public void run() {
				setDockLocation(position);
				setChecked(false);
			}
		}
	}
	
	protected class ResizeAction extends Action {
		public ResizeAction() {
			super(PaletteMessages.RESIZE_LABEL);
		}
		public boolean isEnabled() {
			return !isInState(FLYOVER_COLLAPSED);
		}
		public void run() {
			final Tracker tracker = 
					new Tracker(FlyoutPaletteComposite.this, SWT.LEFT | SWT.RIGHT);
			Rectangle[] rects = new Rectangle[1];
			rects[0] = Sash.this.getBounds();
//			System.out.println("original bounds: " + rects[0]);
			tracker.setCursor(SharedCursors.SIZEE);
			tracker.setRectangles(rects);
			tracker.setStippled(true);
//			tracker.addControlListener(new ControlListener() {
//				public void controlMoved(ControlEvent e) {
//					Rectangle[] rects2 = tracker.getRectangles();
//					System.out.println("sash bounds: " + Sash.this.getBounds()); //$NON-NLS-1$
//					System.out.println("tracker bounds: " + tracker.getRectangles()[0]);
//					int deltaX = Sash.this.getBounds().x - tracker.getRectangles()[0].x;
//					if (dock == PositionConstants.WEST)
//						deltaX = -deltaX;
//					System.out.println("deltaX: " + deltaX);
//					setFixedSize(pViewer.getControl().getBounds().width + deltaX);
//					System.out.println("==========");
//				}
//				public void controlResized(ControlEvent e) {
//				}
//			});
			if (tracker.open()) {
				int deltaX = Sash.this.getBounds().x - tracker.getRectangles()[0].x;
				if (dock == PositionConstants.WEST)
					deltaX = -deltaX;
				setFixedSize(pViewer.getControl().getBounds().width + deltaX);
			}
			tracker.dispose();
		}
	}
}

}