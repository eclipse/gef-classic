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

import java.beans.PropertyChangeEvent;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
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
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToggleButton;
import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.internal.GEFMessages;
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
public static final String PROPERTY_STATE
		= "org.eclipse.gef.ui.palette.fpa.state"; //$NON-NLS-1$
public static final String PROPERTY_DOCK_LOCATION
		= "org.eclipse.gef.ui.palette.fpa.dock"; //$NON-NLS-1$

protected static final int MIN_PALETTE_SIZE = 20;
protected static final int MAX_PALETTE_SIZE = 500;
public static final int IN_VIEW = 8;
public static final int FLYOUT_EXPANDED = 1;
public static final int FLYOUT_COLLAPSED = 2;
public static final int FLYOUT_PINNED_OPEN = 4;

protected Composite sash, paletteContainer;
protected PaletteViewer pViewer, externalViewer;
protected Control graphicalControl;
protected PaletteViewerProvider provider;
protected int dock = PositionConstants.EAST;
protected int paletteState = -1;
protected int defaultState = FLYOUT_COLLAPSED;
private int fixedSize = DEFAULT_PALETTE_SIZE;
private int minWidth = MIN_PALETTE_SIZE;

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
	paletteContainer = createPaletteContainer();
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

protected Composite createPaletteContainer() {
	return new PaletteContainer(this, SWT.NONE);
}

protected FigureCanvas createPinButton(Composite parent) {
	FigureCanvas figCanvas = new FigureCanvas(parent);
	figCanvas.setCursor(SharedCursors.ARROW);
	figCanvas.setScrollBarVisibility(FigureCanvas.NEVER);
	figCanvas.setLayoutData(new RowData());
	ImageFigure fig = new ImageFigure(DrawerFigure.PIN);
	fig.setAlignment(PositionConstants.NORTH_WEST);
	final ToggleButton b = new ToggleButton(fig);
	b.setRolloverEnabled(true);
	b.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	b.setLayoutManager(new StackLayout());
	b.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			if (isInState(FLYOUT_PINNED_OPEN) && !b.isSelected())
				setState(FLYOUT_COLLAPSED);
			else if (isInState(FLYOUT_EXPANDED) && b.isSelected())
				setState(FLYOUT_PINNED_OPEN);
			else if (isInState(FLYOUT_COLLAPSED) && b.isSelected())
				setState(FLYOUT_PINNED_OPEN);
		}
	});
	final PropertyChangeListener listener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(PROPERTY_STATE))
				if (isInState(IN_VIEW | FLYOUT_COLLAPSED | FLYOUT_EXPANDED))
					b.setSelected(false);
				else if (isInState(FLYOUT_PINNED_OPEN))
					b.setSelected(true);
		}
	};
	addPropertyChangeListener(listener);
	figCanvas.setContents(b);
	figCanvas.setBounds(1, 1, 13, 13);
	figCanvas.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			removePropertyChangeListener(listener);
		}
	});
	return figCanvas;
}

protected Composite createSash() {
	return new Sash(this);
}

protected FigureCanvas createTitle(Composite parent, boolean isHorizontal) {
	/*
	 * @TODO:Pratik   there is a bug in figure canvas that gives it scroll bars
	 * even though you have specified NEVER
	 */
	FigureCanvas figCanvas = new FigureCanvas(parent);
	figCanvas.setCursor(SharedCursors.SIZEALL);
	figCanvas.setScrollBarVisibility(FigureCanvas.NEVER);
	figCanvas.setLayoutData(new RowData());
	figCanvas.setContents(new DragFigure(isHorizontal));
	new TitleDragManager(figCanvas);
	MenuManager manager = new MenuManager();
	final IAction resizeAction = new ResizeAction(); 
	manager.add(resizeAction);
	manager.add(new DockAction());
	figCanvas.setMenu(manager.createContextMenu(figCanvas));
	manager.addMenuListener(new IMenuListener() {
		public void menuAboutToShow(IMenuManager manager) {
			resizeAction.setEnabled(resizeAction.isEnabled());
		}
	});
	return figCanvas;
}

public final int getDockLocation() {
	return dock;
}

public final int getFixedSize() {
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

public boolean isInState(int state) {
	return (paletteState & state) != 0;
}

public void layout(boolean changed) {
	if (graphicalControl == null || graphicalControl.isDisposed())
		return;
	
	Rectangle area = getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	int sashWidth = sash.computeSize(-1, -1).x;
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

protected final void layoutComponentsEast(Rectangle area, int sashWidth, 
		int paletteWidth) {
	if (isInState(FLYOUT_COLLAPSED)) {
		sash.setVisible(true);
		paletteContainer.setVisible(false);
		sash.setBounds(area.x + area.width - sashWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
	} else if (isInState(FLYOUT_EXPANDED)) {
		sash.setVisible(true);
		paletteContainer.setVisible(true);
		paletteContainer.moveAbove(graphicalControl);
		sash.moveAbove(paletteContainer);
		paletteContainer.setBounds(area.x + area.width - paletteWidth, area.y, 
				paletteWidth, area.height);
		sash.setBounds(area.x + area.width - paletteWidth - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
	} else if (isInState(FLYOUT_PINNED_OPEN)) {
		sash.setVisible(true);
		paletteContainer.setVisible(true);
		paletteContainer.setBounds(area.x + area.width - paletteWidth, area.y, 
				paletteWidth, area.height);
		sash.setBounds(area.x + area.width - paletteWidth - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth - paletteWidth, 
				area.height);		
	}
}

protected final void layoutComponentsWest(Rectangle area, int sashWidth, 
		int paletteWidth) {
	if (isInState(FLYOUT_COLLAPSED)) {
		sash.setVisible(true);
		paletteContainer.setVisible(false);
		sash.setBounds(area.x, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y,
				area.width - sashWidth, area.height);
	} else if (isInState(FLYOUT_EXPANDED)) {
		sash.setVisible(true);
		paletteContainer.setVisible(true);
		paletteContainer.moveAbove(graphicalControl);
		sash.moveAbove(paletteContainer);
		paletteContainer.setBounds(area.x, area.y, paletteWidth, area.height);
		sash.setBounds(area.x + paletteWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y, 
				area.width - sashWidth, area.height);
	} else if (isInState(FLYOUT_PINNED_OPEN)) {
		sash.setVisible(true);
		paletteContainer.setVisible(true);
		paletteContainer.setBounds(area.x, area.y, paletteWidth, area.height);
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
	if (state != FLYOUT_COLLAPSED && state != FLYOUT_PINNED_OPEN)
		return;
	if (defaultState != state) {
		if (isInState(defaultState))
			setState(state);
		defaultState = state;
	}
}

public void setDockLocation(int position) {
	if (position != PositionConstants.EAST && position != PositionConstants.WEST)
		return;
	if (position != dock) {
		int oldPosition = dock;
		dock = position;
		listeners.firePropertyChange(PROPERTY_DOCK_LOCATION, oldPosition, dock);
		layout();
	}
}

public final void setFixedSize(int newSize) {
	if (fixedSize != newSize) {
		int oldValue = fixedSize;
		fixedSize = newSize;
		listeners.firePropertyChange(PROPERTY_FIXEDSIZE, oldValue, fixedSize);
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
			if (!isInState(FLYOUT_EXPANDED))
				return;
			Display.getCurrent().timerExec(250, new Runnable() {
				public void run() {
					if (isDescendantOf(graphicalControl, 
							Display.getCurrent().getCursorControl()) 
							&& isInState(FLYOUT_EXPANDED))
						setState(FLYOUT_COLLAPSED);
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
			if (isInState(FLYOUT_EXPANDED))
				setState(FLYOUT_COLLAPSED);
			return false;
		}
	});
}

protected void setState(int newState) {
	if (paletteState == newState)
		return;
	int oldState = paletteState;
	paletteState = newState;
	switch (paletteState) {
		case FLYOUT_EXPANDED:
		case FLYOUT_COLLAPSED:
		case FLYOUT_PINNED_OPEN:
			if (pViewer == null) {
				pViewer = provider.createPaletteViewer(paletteContainer);
				if (externalViewer != null)
					transferState(externalViewer, pViewer);
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
	listeners.firePropertyChange(PROPERTY_STATE, newState, oldState);
}

protected void transferState(PaletteViewer src, PaletteViewer dest) {
	IMemento memento = XMLMemento.createWriteRoot("paletteState"); //$NON-NLS-1$
	src.saveState(memento);
	dest.restoreState(memento);
}

protected class Sash extends Composite {
	protected FigureCanvas button, title;
	public Sash(Composite parent) {
		super(parent, SWT.NONE);

		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.wrap = false;
		layout.marginBottom = 1;
		layout.marginTop = 1;
		layout.marginLeft = 1;
		layout.marginRight = 1;
		layout.spacing = 5;
		setLayout(layout);
		button = createPinButton(this);
		title = createTitle(this, false);
		new SashDragManager();
		
		addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseHover(MouseEvent e) {
				if (isInState(FLYOUT_COLLAPSED))
					setState(FLYOUT_EXPANDED);
			}
		});

		addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				paintSash(event.gc);
			}
		});
		
		FlyoutPaletteComposite.this.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(PROPERTY_STATE))
					updateState();
			}
		});
	}
	public Point computeSize(int wHint, int hHint, boolean changed) {
		if (isInState(FLYOUT_PINNED_OPEN))
			return new Point(6, 1);
		return super.computeSize(wHint, hHint, changed);
	}
	protected void handleSashDragged(int shiftAmount) {
		int newSize = paletteContainer.getBounds().width + 
				(dock == PositionConstants.EAST ? -shiftAmount : shiftAmount);
		setFixedSize(newSize);
	}
	protected void paintSash(GC gc) {
		Rectangle bounds = getBounds();
		gc.setForeground(ColorConstants.buttonLightest);
		gc.drawLine(0, 0, bounds.width, 0);
		gc.drawLine(0, 0, 0, bounds.height);
		gc.setForeground(ColorConstants.buttonDarker);
		gc.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
		gc.drawLine(0, bounds.height - 1, bounds.width - 1, bounds.height - 1);
	}
	protected void updateState() {
		setCursor(isInState(FLYOUT_EXPANDED | FLYOUT_PINNED_OPEN) 
				? SharedCursors.SIZEW : null);
		if (isInState(FLYOUT_PINNED_OPEN)) {
			title.setVisible(false);
			button.setVisible(false);
		} else {
			title.setVisible(true);
			button.setVisible(true);
		}
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
			correctState = isInState(FLYOUT_EXPANDED | FLYOUT_PINNED_OPEN);
			origX = event.x;
		}
		public void mouseMove(MouseEvent e) {
			if (dragging && correctState)
				handleSashDragged(e.x - origX);
		}
		public void mouseUp(MouseEvent me) {
			if (!dragging && me.button == 1)
				if (isInState(FLYOUT_COLLAPSED))
					setState(FLYOUT_EXPANDED);
				else if (isInState(FLYOUT_EXPANDED))
					setState(FLYOUT_COLLAPSED);
			dragging = false;
			correctState = false;
		}
	}
}

protected class DockAction 
		extends Action 
		implements IMenuCreator {
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
		return !isInState(FLYOUT_COLLAPSED);
	}
	public void run() {
		/*
		 * @TODO:Pratik   the tracker is off if you are using the mouse and move it
		 * too quickly
		 */
		final Tracker tracker = 
				new Tracker(FlyoutPaletteComposite.this, SWT.LEFT | SWT.RIGHT);
		Rectangle[] rects = new Rectangle[1];
		rects[0] = sash.getBounds();
	//	System.out.println("original bounds: " + rects[0]);
		tracker.setCursor(SharedCursors.SIZEE);
		tracker.setRectangles(rects);
		tracker.setStippled(true);
	//	tracker.addControlListener(new ControlListener() {
	//		public void controlMoved(ControlEvent e) {
	//			Rectangle[] rects2 = tracker.getRectangles();
	//			System.out.println("sash bounds: " + Sash.this.getBounds()); //$NON-NLS-1$
	//			System.out.println("tracker bounds: " + tracker.getRectangles()[0]);
	//			int deltaX = Sash.this.getBounds().x - tracker.getRectangles()[0].x;
	//			if (dock == PositionConstants.WEST)
	//				deltaX = -deltaX;
	//			System.out.println("deltaX: " + deltaX);
	//			setFixedSize(pViewer.getControl().getBounds().width + deltaX);
	//			System.out.println("==========");
	//		}
	//		public void controlResized(ControlEvent e) {
	//		}
	//	});
		if (tracker.open()) {
			int deltaX = sash.getBounds().x - tracker.getRectangles()[0].x;
			if (dock == PositionConstants.WEST)
				deltaX = -deltaX;
			setFixedSize(paletteContainer.getBounds().width + deltaX);
		}
		tracker.dispose();
	}
}

/*
 * @TODO:Pratik   ALT+TAB while dragging puts these drag managers in invalid state
 */
protected class TitleDragManager 
		extends MouseAdapter 
		implements MouseMoveListener, Listener, KeyListener {
	protected boolean dragging = false;
	protected boolean dragCancelled = false;
	protected boolean switchDock = false;
	protected Control control;
	protected Rectangle origBounds;
	public TitleDragManager(Control ctrl) {
		ctrl.addListener(SWT.DragDetect, this);
		ctrl.addMouseMoveListener(this);
		ctrl.addMouseListener(this);
		ctrl.addKeyListener(this);
		control = ctrl;
	}
	public void handleEvent(Event event) {
		dragging = true;
		dragCancelled = false;
		switchDock = false;
		origBounds = sash.getBounds().union(paletteContainer.getBounds());
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
		switchDock = !isDescendantOf(sash, ctrl) 
				&& !isDescendantOf(paletteContainer, ctrl);
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
		} else if (isInState(FLYOUT_COLLAPSED) && me.button == 1)
			// if this was just a simple click (no dragging) and the palette's 
			// collapsed, expand it
			setState(FLYOUT_EXPANDED);
		else if (isInState(FLYOUT_EXPANDED) && me.button == 1)
			// if this was just a simple click (no dragging) and the palette's
			// expanded, collapse it
			setState(FLYOUT_COLLAPSED);
		dragging = false;
		dragCancelled = false;
		switchDock = false;
		origBounds = null;
	}
}

protected class PaletteContainer extends Composite {
	protected FigureCanvas button, title;
	public PaletteContainer(Composite parent, int style) {
		super(parent, style);
		createComponents();

		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(PROPERTY_STATE))
					updateState();
			}
		});
		
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				layout();
			}
		});
		
		updateState();
	}
	protected void createComponents() {
		title = createTitle(this, true);
		button = createPinButton(this);
	}
	public void layout(boolean changed) {
		if (pViewer == null || pViewer.getControl() == null 
				|| pViewer.getControl().isDisposed())
			return;
		
		Rectangle area = getClientArea();
		if (title.getVisible()) {
			Point titleSize = title.computeSize(-1, -1);
			Point buttonSize = button.computeSize(-1, -1);
			int height = Math.max(titleSize.y, buttonSize.y);
			button.setBounds(area.width - buttonSize.x - 1, 0, buttonSize.x, height);
			int remainingWidth = button.getBounds().x - 2;
			int x = 0;
			if (remainingWidth < titleSize.x)
				x = (remainingWidth - titleSize.x) / 2;
			title.setBounds(x, 0, remainingWidth - x, height);
			area.y += height;
			area.height -= height;
		}
		pViewer.getControl().setBounds(area);
	}
	protected void updateState() {
		if (isInState(FLYOUT_PINNED_OPEN)) {
			title.setVisible(true);
			button.setVisible(true);
		} else {
			title.setVisible(false);
			button.setVisible(false);
		}
		layout();
	}
}

protected static class DragFigure 
	extends ImageFigure 
{
	protected static final int MARGIN_SPACE = 0;
	protected static final int H_GAP = 4;
	protected static final int LINE_LENGTH = 30;
	
	public DragFigure(boolean isHorizontal) {
		// @TODO:Pratik  what if the banner font changes.  update?  or ignore?
		setFont(JFaceResources.getBannerFont());
		setRequestFocusEnabled(true);
		setFocusTraversable(true);
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
				repaint();
			}
			public void focusLost(FocusEvent fe) {
				repaint();
			}
		});
		
		Image image = createImage();
		if (!isHorizontal)
			image = ImageUtilities.createRotatedImage(image);
		setImage(image);
	}	
	protected Image createImage() {
		Image img = null;
		Dimension imageSize = FigureUtilities.getStringExtents(
				GEFMessages.Palette_Label, getFont());
		imageSize.expand(H_GAP * 2 + MARGIN_SPACE * 2 + LINE_LENGTH * 2 - 1, 
				MARGIN_SPACE * 2);
		img = new Image(null, imageSize.width, imageSize.height);
		GC gc = new GC(img);
		gc.setBackground(ColorConstants.button);
		gc.fillRectangle(0, 0, imageSize.width, imageSize.height);
		gc.setFont(getFont());
		gc.drawText(GEFMessages.Palette_Label, MARGIN_SPACE + LINE_LENGTH + H_GAP, 
				MARGIN_SPACE);
		gc.setForeground(ColorConstants.buttonLightest);
		int centerY = imageSize.height / 2;
		gc.drawLine(MARGIN_SPACE, centerY - 3, MARGIN_SPACE + LINE_LENGTH, centerY - 3);
		gc.drawLine(MARGIN_SPACE, centerY + 2, MARGIN_SPACE + LINE_LENGTH, centerY + 2);
		gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY - 3, 
				imageSize.width - MARGIN_SPACE, centerY - 3);
		gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY + 2, 
				imageSize.width - MARGIN_SPACE, centerY + 2);
		gc.setForeground(ColorConstants.buttonDarker);
		gc.drawLine(MARGIN_SPACE, centerY + 3, MARGIN_SPACE + LINE_LENGTH, centerY + 3);
		gc.drawLine(MARGIN_SPACE, centerY - 2, MARGIN_SPACE + LINE_LENGTH, centerY - 2);
		gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY - 2, 
				imageSize.width - MARGIN_SPACE, centerY - 2);
		gc.drawLine(imageSize.width - MARGIN_SPACE - LINE_LENGTH, centerY + 3, 
				imageSize.width - MARGIN_SPACE, centerY + 3);
		gc.dispose();
		return img;
	}	
	//@TODO:Pratik   is this okay?
	protected void finalize() throws Throwable {
		getImage().dispose();
	}
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		if (hasFocus())
			graphics.drawFocus(0, 0, bounds.width - 1, bounds.height - 1);
	}	
}

}