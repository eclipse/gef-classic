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
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.DragCursors;
import org.eclipse.ui.internal.Workbench;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.Internal;
import org.eclipse.gef.ui.views.palette.PaletteView;

/**
 * The FlyoutPaletteComposite is used to show a flyout palette alongside another control.
 * The flyout palette will only be visible when the PaletteView is not.  This class is
 * intended to be used in conjunction with 
 * 
 * {@link  org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette}.
 * @author Pratik Shah
 * @since 3.0
 */
public class FlyoutPaletteComposite
	extends Composite
{
	
private static final String PROPERTY_PALETTE_WIDTH
		= "org.eclipse.gef.ui.palette.fpa.paletteWidth"; //$NON-NLS-1$
private static final String PROPERTY_STATE
		= "org.eclipse.gef.ui.palette.fpa.state"; //$NON-NLS-1$
private static final String PROPERTY_DOCK_LOCATION
		= "org.eclipse.gef.ui.palette.fpa.dock"; //$NON-NLS-1$

private static final int DEFAULT_PALETTE_SIZE = 125;
private static final int MIN_PALETTE_SIZE = 20;
private static final int MAX_PALETTE_SIZE = 500;

private static final int STATE_HIDDEN = 8;
private static final int STATE_EXPANDED = 1;
private static final int STATE_COLLAPSED = 2;
private static final int STATE_PINNED_OPEN = 4;

private static final Image LEFT_ARROW = new Image(null, ImageDescriptor.createFromFile(
		Internal.class, "icons/palette_left.gif").getImageData()); //$NON-NLS-1$
private static final Image RIGHT_ARROW = new Image(null, ImageDescriptor.createFromFile(
		Internal.class, "icons/palette_right.gif").getImageData()); //$NON-NLS-1$

private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
private Composite paletteContainer;
private PaletteViewer pViewer, externalViewer;
private Control graphicalControl, sash;
private PaletteViewerProvider provider;
private FlyoutPreferences prefs;
private int dock = PositionConstants.EAST;
private int paletteState = -1;
private int paletteWidth = DEFAULT_PALETTE_SIZE;
private int minWidth = MIN_PALETTE_SIZE;

private IPerspectiveListener perspectiveListener = new IPerspectiveListener() {
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
 * Constructor
 * 
 * @param	parent		The parent Composite
 * @param	style		The style of the widget to construct
 * @param	page		The current workbench page
 * @param	pvProvider	The provider that is to be used to create the flyout palette
 * @param	preferences	To save/retrieve the preferences for the flyout
 */
public FlyoutPaletteComposite(Composite parent, int style, IWorkbenchPage page,
		PaletteViewerProvider pvProvider, FlyoutPreferences preferences) {
	super(parent, style & SWT.BORDER);
	provider = pvProvider;
	prefs = preferences;
	sash = createSash();
	paletteContainer = createPaletteContainer();
	hookIntoWorkbench(page.getWorkbenchWindow());

	// Initialize the state properly
	int defaultState = prefs.getPaletteState();
	setPaletteWidth(prefs.getPaletteWidth());
	setDockLocation(prefs.getDockLocation());
	IViewPart part = page.findView(PaletteView.ID);
	if (part == null) {
		if (defaultState == STATE_COLLAPSED || defaultState == STATE_PINNED_OPEN)
			setState(defaultState);
		else
			setState(STATE_COLLAPSED);
	} else
		setState(STATE_HIDDEN);

	addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			Rectangle area = getClientArea();
			/*
			 * @TODO:Pratik
			 * Sometimes, the editor is resized to 1,1 or 0,0 (depending on the platform)
			 * when the editor is closed or maximized.  We have to ignore such resizes.
			 * See Bug# 62748
			 */
			if (area.width < minWidth)
				return;
			layout(true);
		}
	});

	listeners.addPropertyChangeListener(new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			String property = evt.getPropertyName();
			if (property.equals(PROPERTY_PALETTE_WIDTH))
				prefs.setPaletteWidth(paletteWidth);
			else if (property.equals(PROPERTY_DOCK_LOCATION))
				prefs.setDockLocation(dock);
			else if (property.equals(PROPERTY_STATE))
				if (paletteState == STATE_COLLAPSED || paletteState == STATE_PINNED_OPEN)
					prefs.setPaletteState(paletteState);
		}
	});
}

private final void addListenerToCtrlHierarchy(Control parent, int eventType, 
		Listener listener) {
	parent.addListener(eventType, listener);
	if (!(parent instanceof Composite))
		return;
	Control[] children = ((Composite)parent).getChildren();
	for (int i = 0; i < children.length; i++) {
		addListenerToCtrlHierarchy(children[i], eventType, listener);
	}
}

private Control createFlyoutControlButton(Composite parent) {
	return new ButtonCanvas(parent);
}

private Composite createPaletteContainer() {
	return new PaletteContainer(this, SWT.NONE);
}

private Control createSash() {
	return new Sash(this);
}

private Control createTitle(Composite parent, boolean isHorizontal) {
	return new TitleCanvas(parent, isHorizontal);
}

/*
 * @TODO:Pratik  handleEditorMaximized and handleEditorMinimized are never invoked
 * because currently there's no mechanism in the platform to detect these actions.
 * See Bug# 58190
 */
private void handleEditorMaximized() {
	if (isInState(STATE_HIDDEN))
		setState(prefs.getPaletteState());
}

private void handleEditorMinimized() {
	handlePerspectiveActivated(
			Workbench.getInstance().getActiveWorkbenchWindow().getActivePage(), null);
}

private void handlePerspectiveActivated(IWorkbenchPage page, 
		IPerspectiveDescriptor perspective) {
	IViewPart view = page.findView(PaletteView.ID);
	if (view == null && isInState(STATE_HIDDEN))
		setState(prefs.getPaletteState());
	if (view != null && !isInState(STATE_HIDDEN))
		setState(STATE_HIDDEN);
}

private void handlePerspectiveChanged(IWorkbenchPage page, 
		IPerspectiveDescriptor perspective, String changeId) {
	if (changeId.equals(IWorkbenchPage.CHANGE_VIEW_SHOW) 
			|| changeId.equals(IWorkbenchPage.CHANGE_VIEW_HIDE))
		handlePerspectiveActivated(page, perspective);
}

// Will return false if ancestor or descendant is null, and true if both are null
private final boolean isDescendantOf(Control ancestor, Control descendant) {
	if (descendant == null)
		return false;
	if (ancestor == descendant)
		return true;
	return isDescendantOf(ancestor, descendant.getParent());
}

public boolean isInState(int state) {
	return (paletteState & state) != 0;
}

/**
 * @see	Composite#layout(boolean)
 */
public void layout(boolean changed) {
	if (graphicalControl == null || graphicalControl.isDisposed())
		return;
	
	Rectangle area = getClientArea();
	if (area.width == 0 || area.height == 0) return;
	
	int sashWidth = sash.computeSize(-1, -1).x;
	int pWidth = paletteWidth;
	int maxWidth = Math.min(area.width / 2, MAX_PALETTE_SIZE);
	maxWidth = Math.max(maxWidth, minWidth);
	pWidth = Math.max(pWidth, minWidth);
	pWidth = Math.min(pWidth, maxWidth);
	
	setRedraw(false);
	if (isInState(STATE_HIDDEN)) {
		sash.setVisible(false);
		paletteContainer.setVisible(false);
		graphicalControl.setBounds(area);
	} else if (dock == PositionConstants.EAST)
		layoutComponentsEast(area, sashWidth, pWidth);
	else
		layoutComponentsWest(area, sashWidth, pWidth);
	setRedraw(true);
	update();
}

private final void layoutComponentsEast(Rectangle area, int sashWidth, int pWidth) {
	if (isInState(STATE_COLLAPSED)) {
		paletteContainer.setVisible(false);
		sash.setBounds(area.x + area.width - sashWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
		sash.setVisible(true);
	} else if (isInState(STATE_EXPANDED)) {
		paletteContainer.moveAbove(graphicalControl);
		sash.moveAbove(paletteContainer);
		paletteContainer.setBounds(area.x + area.width - pWidth, area.y, 
				pWidth, area.height);
		sash.setBounds(area.x + area.width - pWidth - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth, area.height);
		sash.setVisible(true);
		paletteContainer.setVisible(true);
	} else if (isInState(STATE_PINNED_OPEN)) {
		paletteContainer.setBounds(area.x + area.width - pWidth, area.y, 
				pWidth, area.height);
		sash.setBounds(area.x + area.width - pWidth - sashWidth, area.y, sashWidth, 
				area.height);
		graphicalControl.setBounds(area.x, area.y, area.width - sashWidth - pWidth, 
				area.height);		
		sash.setVisible(true);
		paletteContainer.setVisible(true);
	}
}

private final void layoutComponentsWest(Rectangle area, int sashWidth, int pWidth) {
	if (isInState(STATE_COLLAPSED)) {
		sash.setVisible(true);
		paletteContainer.setVisible(false);
		sash.setBounds(area.x, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y,
				area.width - sashWidth, area.height);
	} else if (isInState(STATE_EXPANDED)) {
		sash.setVisible(true);
		paletteContainer.setVisible(true);
		paletteContainer.moveAbove(graphicalControl);
		sash.moveAbove(paletteContainer);
		paletteContainer.setBounds(area.x, area.y, pWidth, area.height);
		sash.setBounds(area.x + pWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + sashWidth, area.y, 
				area.width - sashWidth, area.height);
	} else if (isInState(STATE_PINNED_OPEN)) {
		sash.setVisible(true);
		paletteContainer.setVisible(true);
		paletteContainer.setBounds(area.x, area.y, pWidth, area.height);
		sash.setBounds(area.x + pWidth, area.y, sashWidth, area.height);
		graphicalControl.setBounds(area.x + pWidth + sashWidth, area.y,
				area.width - sashWidth - pWidth, area.height);		
	}	
}

private void hookIntoWorkbench(final IWorkbenchWindow window) {
	window.addPerspectiveListener(perspectiveListener);
	addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			window.removePerspectiveListener(perspectiveListener);
			perspectiveListener = null;
		}
	});
}

/**
 * If an external palette viewer is provided, palette settings (the ones that are
 * captured in {@link PaletteViewer#saveState(IMemento)}) will be maintained when
 * switching between the two viewers.  Providing an external viewer, although
 * recommended, is optional.
 * 
 * @param	viewer	The external palette viewer used in the PaletteView
 */
public void setExternalViewer(PaletteViewer viewer) {
	externalViewer = viewer;
	if (externalViewer != null && pViewer != null) {
		transferState(pViewer, externalViewer);
	}
}

private void setDockLocation(int position) {
	if (position != PositionConstants.EAST && position != PositionConstants.WEST)
		return;
	if (position != dock) {
		int oldPosition = dock;
		dock = position;
		listeners.firePropertyChange(PROPERTY_DOCK_LOCATION, oldPosition, dock);
		if (pViewer != null)
			layout(true);
	}
}

private final void setPaletteWidth(int newSize) {
	if (paletteWidth != newSize) {
		int oldValue = paletteWidth;
		paletteWidth = newSize;
		listeners.firePropertyChange(PROPERTY_PALETTE_WIDTH, oldValue, paletteWidth);
		if (pViewer != null)
			layout(true);
	}
}

/**
 * Sets the graphical control along the side of which the palette is to be displayed.
 * This method should only be invoked once.
 */
public void setGraphicalControl(Control graphicalViewer) {
	Assert.isTrue(graphicalViewer.getParent() == this);
	Assert.isTrue(graphicalControl == null);
	Assert.isTrue(graphicalViewer != null);
	graphicalControl = graphicalViewer;
	addListenerToCtrlHierarchy(graphicalControl, SWT.MouseEnter, new Listener() {
		public void handleEvent(Event event) {
			if (!isInState(STATE_EXPANDED))
				return;
			Display.getCurrent().timerExec(250, new Runnable() {
				public void run() {
					if (isDescendantOf(graphicalControl, 
							Display.getCurrent().getCursorControl())
							&& isInState(STATE_EXPANDED))
						setState(STATE_COLLAPSED);
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
			if (isInState(STATE_EXPANDED))
				setState(STATE_COLLAPSED);
			return false;
		}
	});
}

private void setState(int newState) {
	if (paletteState == newState)
		return;
	int oldState = paletteState;
	paletteState = newState;
	switch (paletteState) {
		case STATE_EXPANDED:
		case STATE_COLLAPSED:
		case STATE_PINNED_OPEN:
			if (pViewer == null) {
				pViewer = provider.createPaletteViewer(paletteContainer);
				if (externalViewer != null)
					transferState(externalViewer, pViewer);
				minWidth = Math.max(pViewer.getControl().computeSize(0, 0).x, 
						MIN_PALETTE_SIZE);
			}
			break;
		case STATE_HIDDEN:
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
	layout(true);
	listeners.firePropertyChange(PROPERTY_STATE, oldState, newState);
}

private void transferState(PaletteViewer src, PaletteViewer dest) {
	IMemento memento = XMLMemento.createWriteRoot("paletteState"); //$NON-NLS-1$
	src.saveState(memento);
	dest.restoreState(memento);
}

public interface FlyoutPreferences {
	public int getDockLocation();
	public int getPaletteState();
	public int getPaletteWidth();
	public void setDockLocation(int location);
	public void setPaletteState(int state);
	public void setPaletteWidth(int width);
}

private class Sash extends Composite {
	private Control button, title;
	public Sash(Composite parent) {
		super(parent, SWT.NONE);
		button = createFlyoutControlButton(this);
		title = createTitle(this, false);
		new SashDragManager();
		
		addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseHover(MouseEvent e) {
				if (isInState(STATE_COLLAPSED))
					setState(STATE_EXPANDED);
			}
		});

		addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				paintSash(event.gc);
			}
		});
		
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				layout(true);
			}
		});
		
		listeners.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(PROPERTY_STATE))
					updateState();
			}
		});
	}
	public Point computeSize(int wHint, int hHint, boolean changed) {
		if (isInState(STATE_PINNED_OPEN))
			return new Point(6, 1);
		Point buttonSize = button.computeSize(wHint, hHint);
		Point titleSize = title.computeSize(wHint, hHint);
		return new Point(Math.max(buttonSize.x, titleSize.x) + 2, 
				buttonSize.y + titleSize.y + 7);
	}
	private void handleSashDragged(int shiftAmount) {
		int newSize = paletteContainer.getBounds().width + 
				(dock == PositionConstants.EAST ? -shiftAmount : shiftAmount);
		setPaletteWidth(newSize);
	}
	public void layout(boolean changed) {
		if (isInState(STATE_PINNED_OPEN)) {
			title.setVisible(false);
			button.setVisible(false);
			return;
		}
		
		boolean stealFocus = !button.getVisible();
		title.setVisible(true);
			button.setVisible(true);
		Rectangle area = getClientArea();
		// 1 pixel margin all around to draw the raised border
		area.x += 1;
		area.y += 1;
		area.width -= 2;
		area.height -= 2;
		if (button.getVisible()) {
			button.setBounds(area.x, area.y, area.width, area.width);
			// 5-pixel spacing
			area.y += area.width + 3;
		}
		if (title.getVisible()) {
			title.setBounds(area.x, area.y, area.width, 
					title.computeSize(-1, -1).y);
		}
		if (stealFocus)
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					button.setFocus();
				}
			});
	}
	private void paintSash(GC gc) {
		Rectangle bounds = getBounds();
		gc.setForeground(ColorConstants.buttonLightest);
		gc.drawLine(0, 0, bounds.width, 0);
		gc.drawLine(0, 0, 0, bounds.height);
		gc.setForeground(ColorConstants.buttonDarker);
		gc.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
		gc.drawLine(0, bounds.height - 1, bounds.width - 1, bounds.height - 1);
	}
	private void updateState() {
		setCursor(isInState(STATE_EXPANDED | STATE_PINNED_OPEN) 
				? SharedCursors.SIZEW : null);
	}
	
	private class SashDragManager 
			extends MouseAdapter 
			implements MouseMoveListener, Listener {
		protected boolean dragging = false;
		protected boolean correctState = false;
		protected int origX;
		protected Listener keyListener = new Listener() {
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.ALT || event.keyCode == SWT.ESC) {
					dragging = false;
					Display.getCurrent().removeFilter(SWT.KeyDown, this);
				}
				event.doit = false;
				event.type = SWT.None;
			}
		};
		public SashDragManager() {
			Sash.this.addListener(SWT.DragDetect, this);
			Sash.this.addMouseMoveListener(this);
			Sash.this.addMouseListener(this);
		}
		public void handleEvent(Event event) {
			dragging = true;
			correctState = isInState(STATE_EXPANDED | STATE_PINNED_OPEN);
			origX = event.x;
			Display.getCurrent().addFilter(SWT.KeyDown, keyListener);
		}
		public void mouseMove(MouseEvent e) {
			if (dragging && correctState)
				handleSashDragged(e.x - origX);
		}
		public void mouseUp(MouseEvent me) {
			Display.getCurrent().removeFilter(SWT.KeyDown, keyListener);
			if (!dragging && me.button == 1)
				if (isInState(STATE_COLLAPSED))
					setState(STATE_EXPANDED);
				else if (isInState(STATE_EXPANDED))
					setState(STATE_COLLAPSED);
			dragging = false;
			correctState = false;
		}
	}
}

private class DockAction 
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

private class ResizeAction extends Action {
	public ResizeAction() {
		super(PaletteMessages.RESIZE_LABEL);
	}
	public boolean isEnabled() {
		return !isInState(STATE_COLLAPSED);
	}
	public void run() {
		final Tracker tracker = new Tracker(FlyoutPaletteComposite.this, 
				SWT.RIGHT | SWT.LEFT);
		Rectangle[] rects = new Rectangle[1];
		rects[0] = sash.getBounds();
		tracker.setCursor(SharedCursors.SIZEE);
		tracker.setRectangles(rects);
		tracker.setStippled(true);
		if (tracker.open()) {
			int deltaX = sash.getBounds().x - tracker.getRectangles()[0].x;
			if (dock == PositionConstants.WEST)
				deltaX = -deltaX;
			setPaletteWidth(paletteContainer.getBounds().width + deltaX);
		}
		tracker.dispose();
	}
}

private class TitleDragManager
		extends MouseAdapter
		implements Listener, MouseTrackListener {
	protected boolean switchDock = false;
	protected boolean dragging = false;
	protected int threshold;
	public TitleDragManager(Control ctrl) {
		ctrl.addListener(SWT.DragDetect, this);
		ctrl.addMouseListener(this);
		ctrl.addMouseTrackListener(this);
	}
	public void handleEvent(Event event) {
		dragging = true;
		switchDock = false;
		if (dock == PositionConstants.EAST)
			threshold = Integer.MAX_VALUE / 2;
		else
			threshold = -1;
		final int switchThreshold = FlyoutPaletteComposite.this.getSize().x / 2;
		Rectangle bounds = sash.getBounds();
		if (paletteContainer.getVisible())
			bounds = bounds.union(paletteContainer.getBounds());
		final Rectangle origBounds = bounds;
		final Tracker tracker = new Tracker(FlyoutPaletteComposite.this, SWT.NULL);
		tracker.setRectangles(new Rectangle[] {origBounds});
		tracker.setStippled(true);
		tracker.addListener(SWT.Move, new Listener() {
			public void handleEvent(final Event evt) {
				Display.getCurrent().syncExec(new Runnable() {
					public void run() {
						Control ctrl = Display.getCurrent().getCursorControl();
						Point pt = FlyoutPaletteComposite.this.toControl(evt.x, evt.y); 
						switchDock = isDescendantOf(graphicalControl, ctrl) 
								&& ((dock == PositionConstants.WEST && pt.x > threshold - 10)
								|| (dock == PositionConstants.EAST && pt.x < threshold + 10));
						boolean invalid = false;
						if (!switchDock)
							invalid = !isDescendantOf(FlyoutPaletteComposite.this, ctrl);
						if (switchDock) {
							if (dock == PositionConstants.WEST) {
								threshold = Math.max(threshold, pt.x);
								threshold = Math.min(threshold, switchThreshold);
							} else {
								threshold = Math.min(threshold, pt.x);
								threshold = Math.max(threshold, switchThreshold);
							}
						}
						Rectangle placeHolder = origBounds;
						if (switchDock) {
							if (dock == PositionConstants.EAST) {
								placeHolder = new Rectangle(0, 0,
										origBounds.width, origBounds.height);
							} else {
								placeHolder = new Rectangle(
										FlyoutPaletteComposite.this.getBounds().width 
										- origBounds.width, 0, origBounds.width, 
										origBounds.height);
							}
						}
						// update the cursor
						Cursor cursor;
						if (invalid)
							cursor = DragCursors.getCursor(DragCursors.INVALID);
						else if ((!switchDock && dock == PositionConstants.EAST)
								|| (switchDock && dock == PositionConstants.WEST))
							cursor = DragCursors.getCursor(DragCursors.RIGHT);
						else
							cursor = DragCursors.getCursor(DragCursors.LEFT);
						tracker.setCursor(cursor);
						// update the rectangle only if it has changed
						if (!tracker.getRectangles()[0].equals(placeHolder))
							tracker.setRectangles(new Rectangle[] {placeHolder});
					}
				});
			}
		});
		if (tracker.open()) {
			if (switchDock)
				setDockLocation(PositionConstants.EAST_WEST & ~dock);
			// mouse up is received by the tracker and by this listener, so we set dragging
			// to be false
			dragging = false;
		}
		tracker.dispose();
	}
	public void mouseEnter(MouseEvent e) {}
	public void mouseExit(MouseEvent e) {}
	public void mouseHover(MouseEvent e) {
		/*
		 * @TODO:Pratik   Mouse hover events are received if the hover occurs just before 
		 * you finish or cancel the drag.  Open a bugzilla about it?
		 */
		if (isInState(STATE_COLLAPSED))
			setState(STATE_EXPANDED);
	}
	public void mouseUp(MouseEvent me) {
		if (me.button != 1)
			return;
		if (isInState(STATE_COLLAPSED))
			setState(STATE_EXPANDED);
		else if (isInState(STATE_EXPANDED))
			setState(STATE_COLLAPSED);
	}
}

private class PaletteContainer extends Composite {
	protected Control button, title;
	public PaletteContainer(Composite parent, int style) {
		super(parent, style);
		createComponents();

		listeners.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(PROPERTY_STATE))
					updateState();
				else if (evt.getPropertyName().equals(PROPERTY_DOCK_LOCATION))
					if (getVisible())
						layout(true);
			}
		});
		
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				layout(true);
			}
		});
		
		updateState();
	}
	protected void createComponents() {
		title = createTitle(this, true);
		button = createFlyoutControlButton(this);
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
			buttonSize.x = Math.max(height, buttonSize.x);
			if (dock == PositionConstants.EAST) {
				int buttonX = area.width - buttonSize.x;
				button.setBounds(buttonX, 0, buttonSize.x, height);
				// leave some space between the button and the title
				title.setBounds(0, 0, buttonX - 2, height);
			} else {
				int titleX = buttonSize.x + 2;
				button.setBounds(0, 0, buttonSize.x, height);
				title.setBounds(titleX, 0, area.width - titleX, height);
			}
			area.y += height;
			area.height -= height;
		}
		pViewer.getControl().setBounds(area);
	}
	protected void updateState() {
		if (isInState(STATE_PINNED_OPEN)) {
			title.setVisible(true);
			if (!button.getVisible()) {
				button.setVisible(true);
				button.setFocus();
			}
		} else {
			title.setVisible(false);
			button.setVisible(false);
		}
		layout(true);
	}
}

private class DragFigure 
		extends ImageFigure {
	public DragFigure() {
		FlyoutPaletteComposite.this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (getImage() != null)
					getImage().dispose();
			}
		});
	}
	protected void paintFigure(Graphics graphics) {
		if (getImage() == null)
			updateImage();
		super.paintFigure(graphics);
		if (hasFocus())
			graphics.drawFocus(0, 0, bounds.width - 1, bounds.height - 1);
	}
	public void setFont(Font f) {
		if (f != font) {
			super.setFont(f);
			updateImage();
		}
	}
	protected void updateImage() {
		if (getImage() != null)
			getImage().dispose();
		/*
		 * @TODO:Pratik  Add this as a utility method to ImageUtilities or 
		 * FigureUtilities?
		 */
		Image img = null;
		IFigure fig = new TitleLabel(false);
		fig.setFont(getFont());
		fig.setOpaque(true);
		fig.setBackgroundColor(ColorConstants.button);
		// This is a hack.  TitleLabel does not return a proper preferred size, since
		// its getInsets() method depends on its current size.  To make it work properly,
		// we first make the size really big.
		fig.setSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Dimension imageSize = fig.getPreferredSize(-1, -1);
		fig.setSize(imageSize);
		img = new Image(null, imageSize.width, imageSize.height);
		GC gc = new GC(img);
		Graphics graphics = new SWTGraphics(gc);
		fig.paint(graphics);
		graphics.dispose();
		gc.dispose();
		setImage(ImageUtilities.createRotatedImage(img));
		img.dispose();
	}
}

private static class TitleLabel extends Label {
	protected static final Border BORDER = new MarginBorder(0, 3, 0, 3);
	protected static final Border TOOL_TIP_BORDER = new MarginBorder(0, 2, 0, 2);
	private static final int H_GAP = 4;
	private static final int LINE_LENGTH = 20;
	private static final int MIN_LINE_LENGTH = 6;
	private boolean horizontal;
	public TitleLabel(boolean isHorizontal) {
		super(GEFMessages.Palette_Label);
		horizontal = isHorizontal;
		setLabelAlignment(PositionConstants.LEFT);
		setBorder(BORDER);
		Label tooltip = new Label(getText());
		tooltip.setBorder(TOOL_TIP_BORDER);
		setToolTip(tooltip);
	}
	public Insets getInsets() {
		Insets insets = super.getInsets();
		Dimension diff = getBounds().getCropped(insets).getSize()
				.getDifference(getTextBounds().getSize());
		if (diff.width > 0) {
			insets = new Insets(insets);
			int width = Math.min(LINE_LENGTH + H_GAP, diff.width / 2);
			insets.left += width;
			insets.right += width;
		}
		return insets;
	}
	public IFigure getToolTip() {
		if (isTextTruncated())
			return super.getToolTip();
		return null;
	}
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		if (hasFocus())
			graphics.drawFocus(0, 0, bounds.width - 1, bounds.height - 1);
		org.eclipse.draw2d.geometry.Rectangle area = getBounds().getCopy().crop(super.getInsets());
		org.eclipse.draw2d.geometry.Rectangle textBounds = getTextBounds();
		// We reduce the width by 1 because FigureUtilities grows it by 1 unnecessarily
		textBounds.width--;
		int lineWidth = Math.min((area.width - textBounds.width - H_GAP * 2) / 2, 
				LINE_LENGTH); 
		if (lineWidth >= MIN_LINE_LENGTH) {
			int centerY = area.height / 2;
			graphics.setForegroundColor(ColorConstants.buttonLightest);
			graphics.drawLine(textBounds.x - H_GAP - lineWidth, centerY - 3, 
					textBounds.x - H_GAP, centerY - 3);
			graphics.drawLine(textBounds.x - H_GAP - lineWidth, centerY + 2, 
					textBounds.x - H_GAP, centerY + 2);
			graphics.drawLine(textBounds.right() + H_GAP, centerY - 3, 
					textBounds.right() + H_GAP + lineWidth, centerY - 3);
			graphics.drawLine(textBounds.right() + H_GAP, centerY + 2, 
					textBounds.right() + H_GAP + lineWidth, centerY + 2);
			graphics.setForegroundColor(ColorConstants.buttonDarker);
			graphics.drawLine(textBounds.x - H_GAP - lineWidth, centerY + 3, 
					textBounds.x - H_GAP, centerY + 3);
			graphics.drawLine(textBounds.x - H_GAP - lineWidth, centerY - 2, 
					textBounds.x - H_GAP, centerY - 2);
			graphics.drawLine(textBounds.right() + H_GAP, centerY - 2, 
					textBounds.right() + H_GAP + lineWidth, centerY - 2);
			graphics.drawLine(textBounds.right() + H_GAP, centerY + 3, 
					textBounds.right() + H_GAP + lineWidth, centerY + 3);
			if (horizontal) {
				graphics.drawPoint(textBounds.x - H_GAP, centerY + 2);
				graphics.drawPoint(textBounds.x - H_GAP, centerY - 3);
				graphics.drawPoint(textBounds.right() + H_GAP + lineWidth, centerY - 3);
				graphics.drawPoint(textBounds.right() + H_GAP + lineWidth, centerY + 2);
				graphics.setForegroundColor(ColorConstants.buttonLightest);
				graphics.drawPoint(textBounds.x - H_GAP - lineWidth, centerY - 2);
				graphics.drawPoint(textBounds.x - H_GAP - lineWidth, centerY + 3);
				graphics.drawPoint(textBounds.right() + H_GAP, centerY - 2);
				graphics.drawPoint(textBounds.right() + H_GAP, centerY + 3);
			} else {
				graphics.drawPoint(textBounds.x - H_GAP - lineWidth, centerY + 2);
				graphics.drawPoint(textBounds.x - H_GAP - lineWidth, centerY - 3);
				graphics.drawPoint(textBounds.right() + H_GAP, centerY - 3);
				graphics.drawPoint(textBounds.right() + H_GAP, centerY + 2);
				graphics.setForegroundColor(ColorConstants.buttonLightest);
				graphics.drawPoint(textBounds.x - H_GAP, centerY - 2);
				graphics.drawPoint(textBounds.x - H_GAP, centerY + 3);
				graphics.drawPoint(textBounds.right() + H_GAP + lineWidth, centerY - 2);
				graphics.drawPoint(textBounds.right() + H_GAP + lineWidth, centerY + 3);
			}
		}
	}
}

private class ButtonCanvas extends Canvas {
	private LightweightSystem lws;
	public ButtonCanvas(Composite parent) {
		super(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
		init();
		provideAccSupport();
	}
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Dimension size = lws.getRootFigure().getPreferredSize(wHint, hHint);
		size.union(new Dimension(wHint, hHint));
		return new org.eclipse.swt.graphics.Point(size.width, size.height);
	}
	private Image getButtonImage() {
		if (isInState(STATE_EXPANDED | STATE_PINNED_OPEN))
			return dock == PositionConstants.WEST ? LEFT_ARROW : RIGHT_ARROW;
		return dock == PositionConstants.WEST ? RIGHT_ARROW : LEFT_ARROW;
	}
	private String getButtonTooltipText() {
		if (isInState(STATE_COLLAPSED))
			return PaletteMessages.PALETTE_SHOW;
		return PaletteMessages.PALETTE_HIDE;
	}
	private void init() {
		setCursor(SharedCursors.ARROW);
		lws = new LightweightSystem();
		lws.setControl(this);
		final ImageButton b = new ImageButton(getButtonImage());
		b.setRolloverEnabled(true);
		b.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (isInState(STATE_COLLAPSED))
					setState(STATE_PINNED_OPEN);
				else
					setState(STATE_COLLAPSED);
			}
		});
		listeners.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(PROPERTY_STATE)) {
					b.setImage(getButtonImage());
					setToolTipText(getButtonTooltipText());
					// @TODO:Pratik  also need to toggle accessibility listeners
				} else if (evt.getPropertyName().equals(PROPERTY_DOCK_LOCATION))
					b.setImage(getButtonImage());
			}
		});
		lws.setContents(b);
	}
	private void provideAccSupport() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getDescription(AccessibleEvent e) {
				e.result = PaletteMessages.ACC_DESC_PALETTE_BUTTON;
			}
			public void getHelp(AccessibleEvent e) {
				getDescription(e);
			}
			public void getName(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});
		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_PUSHBUTTON;
			}
		});
	}
	private class ImageButton extends Button {
		public ImageButton(Image img) {
			super();
			setContents(new ImageFigure(img));
		}
		public void setImage(Image img) {
			((ImageFigure)getChildren().get(0)).setImage(img);
		}
	}
}

private class TitleCanvas extends Canvas {
	private LightweightSystem lws;
	public TitleCanvas(Composite parent, boolean horizontal) {
		super(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
		init(horizontal);
		provideAccSupport();
	}
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Dimension size = lws.getRootFigure().getPreferredSize(wHint, hHint);
		size.union(new Dimension(wHint, hHint));
		return new org.eclipse.swt.graphics.Point(size.width, size.height);
	}
	private void init(boolean isHorizontal) {
		IFigure fig;
		if (isHorizontal)
			fig = new TitleLabel(true);
		else
			fig = new DragFigure();
		final IFigure contents = fig;
		contents.setFont(JFaceResources.getBannerFont());
		contents.setRequestFocusEnabled(true);
		contents.setFocusTraversable(true);
		contents.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
				fe.gainer.repaint();
			}
			public void focusLost(FocusEvent fe) {
				fe.loser.repaint();
			}
		});
		final IPropertyChangeListener fontListener = new IPropertyChangeListener() {
			public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
				if (JFaceResources.BANNER_FONT.equals(event.getProperty()))
					contents.setFont(JFaceResources.getBannerFont());
				if (isVisible()) {
					redraw();
					/*
					 * If this canvas is in the sash, we want the FlyoutPaletteComposite
					 * to layout (which will cause the sash to be resized and laid out).  
					 * However, if this canvas is in the paletteContainer, the 
					 * paletteContainer's bounds won't change, and hence it won't layout.
					 * Thus, we also invoke getParent().layout().
					 */
					FlyoutPaletteComposite.this.layout(true);
					getParent().layout(true);
				}
			}
		};
		JFaceResources.getFontRegistry().addListener(fontListener);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				JFaceResources.getFontRegistry().removeListener(fontListener);
			}
		});
		
		lws = new LightweightSystem();
		lws.setControl(this);
		lws.setContents(contents);
		setCursor(SharedCursors.SIZEALL);
		new TitleDragManager(this);
		MenuManager manager = new MenuManager();
		final IAction resizeAction = new ResizeAction(); 
		manager.add(resizeAction);
		manager.add(new DockAction());
		setMenu(manager.createContextMenu(this));
		manager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				resizeAction.setEnabled(resizeAction.isEnabled());
			}
		});
	}
	private void provideAccSupport() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getDescription(AccessibleEvent e) {
				e.result = PaletteMessages.ACC_DESC_PALETTE_TITLE;
			}
			public void getHelp(AccessibleEvent e) {
				getDescription(e);
			}
			public void getName(AccessibleEvent e) {
				e.result = GEFMessages.Palette_Label;
			}
		});
		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_SLIDER;
			}
		});
	}
}

}