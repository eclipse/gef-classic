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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
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
import org.eclipse.ui.internal.DragCursors;
import org.eclipse.ui.internal.Workbench;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
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

private static final String PROPERTY_PALETTE_WIDTH
		= "org.eclipse.gef.ui.palette.fpa.paletteWidth"; //$NON-NLS-1$
private static final String PROPERTY_STATE
		= "org.eclipse.gef.ui.palette.fpa.state"; //$NON-NLS-1$
private static final String PROPERTY_DOCK_LOCATION
		= "org.eclipse.gef.ui.palette.fpa.dock"; //$NON-NLS-1$

private static final int DEFAULT_PALETTE_SIZE = 125;
private static final int MIN_PALETTE_SIZE = 20;
private static final int MAX_PALETTE_SIZE = 500;

public static final int STATE_HIDDEN = 8;
public static final int STATE_EXPANDED = 1;
public static final int STATE_COLLAPSED = 2;
public static final int STATE_PINNED_OPEN = 4;

private Composite sash, paletteContainer;
private PaletteViewer pViewer, externalViewer;
private Control graphicalControl;
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
 * PropertyChangeSupport
 */
private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

public FlyoutPaletteComposite(Composite parent, int style, IWorkbenchPage page,
		PaletteViewerProvider pvProvider, FlyoutPreferences preferences) {
	super(parent, style & SWT.BORDER);
	provider = pvProvider;
	sash = createSash();
	paletteContainer = createPaletteContainer();
	hookIntoWorkbench(page.getWorkbenchWindow());

	prefs = preferences;
	int defaultState = prefs.getPaletteState();
	setPaletteWidth(prefs.getPaletteWidth());
	setDockLocation(prefs.getDockLocation());

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
			layout();
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

	IViewPart part = page.findView(PaletteView.ID);
	if (part == null) {
		if (defaultState == STATE_COLLAPSED || defaultState == STATE_PINNED_OPEN)
			setState(defaultState);
		else
			setState(STATE_COLLAPSED);
	} else
		setState(STATE_HIDDEN);
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

private Composite createPaletteContainer() {
	return new PaletteContainer(this, SWT.NONE);
}

private Control createPinButton(Composite parent) {
	final LightweightSystem lws = new LightweightSystem();
	Canvas canvas = new Canvas(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND) {
		public Point computeSize(int wHint, int hHint, boolean changed) {
			Dimension size = lws.getRootFigure().getPreferredSize(wHint, hHint);
			size.union(new Dimension(wHint, hHint));
			return new org.eclipse.swt.graphics.Point(size.width, size.height);
		}
	};
	lws.setControl(canvas);
	canvas.setCursor(SharedCursors.ARROW);
	canvas.setLayoutData(new RowData());
	ImageFigure fig = new ImageFigure(DrawerFigure.PIN);
	final ToggleButton b = new ToggleButton(fig);
	b.setRolloverEnabled(true);
	b.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	b.setLayoutManager(new StackLayout());
	b.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			if (isInState(STATE_PINNED_OPEN) && !b.isSelected())
				setState(STATE_COLLAPSED);
			else if (isInState(STATE_EXPANDED) && b.isSelected())
				setState(STATE_PINNED_OPEN);
			else if (isInState(STATE_COLLAPSED) && b.isSelected())
				setState(STATE_PINNED_OPEN);
		}
	});
	final PropertyChangeListener listener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(PROPERTY_STATE))
				if (isInState(STATE_HIDDEN | STATE_COLLAPSED | STATE_EXPANDED))
					b.setSelected(false);
				else if (isInState(STATE_PINNED_OPEN))
					b.setSelected(true);
		}
	};
	listeners.addPropertyChangeListener(listener);
	lws.setContents(b);
	canvas.setBounds(1, 1, 13, 13);
	canvas.addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			listeners.removePropertyChangeListener(listener);
		}
	});
	return canvas;
}

private Composite createSash() {
	return new Sash(this);
}

private Control createTitle(Composite parent, boolean isHorizontal) {
	IFigure contents;
	if (!isHorizontal)
		contents = new DragFigure();
	else
		contents = new TitleLabel();
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
	
	final LightweightSystem lws = new LightweightSystem();
	Canvas canvas = new Canvas(parent, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND) {
		public Point computeSize(int wHint, int hHint, boolean changed) {
			Dimension size = lws.getRootFigure().getPreferredSize(wHint, hHint);
			size.union(new Dimension(wHint, hHint));
			return new org.eclipse.swt.graphics.Point(size.width, size.height);
		}
	};
	lws.setControl(canvas);
	lws.setContents(contents);
	canvas.setCursor(SharedCursors.SIZEALL);
	canvas.setLayoutData(new RowData());
	new TitleDragManager(canvas);
	MenuManager manager = new MenuManager();
	final IAction resizeAction = new ResizeAction(); 
	manager.add(resizeAction);
	manager.add(new DockAction());
	canvas.setMenu(manager.createContextMenu(canvas));
	manager.addMenuListener(new IMenuListener() {
		public void menuAboutToShow(IMenuManager mgr) {
			resizeAction.setEnabled(resizeAction.isEnabled());
		}
	});
	canvas.addMouseTrackListener(new MouseTrackAdapter() {
		public void mouseHover(MouseEvent e) {
			if (isInState(STATE_COLLAPSED))
				setState(STATE_EXPANDED);
		}
	});
	return canvas;
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
			layout();
	}
}

private final void setPaletteWidth(int newSize) {
	if (paletteWidth != newSize) {
		int oldValue = paletteWidth;
		paletteWidth = newSize;
		listeners.firePropertyChange(PROPERTY_PALETTE_WIDTH, oldValue, paletteWidth);
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
	layout();
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
		button = createPinButton(this);
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
				layout();
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
		
		title.setVisible(true);
		button.setVisible(true);
		Rectangle area = getClientArea();
		// 1 pixel margin all around
		area.x += 1;
		area.y += 1;
		area.width -= 2;
		area.height -= 2;
		if (button.getVisible()) {
			button.setBounds(area.x, area.y, area.width - 1, area.width - 1);
			// 5-pixel spacing
			area.y += area.width + 5;
		}
		if (title.getVisible()) {
			title.setBounds(area.x, area.y, area.width, 
					title.computeSize(-1, -1).y);
		}
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
	
	/*
	 * @TODO:Pratik   ALT+TAB while dragging puts this drag manager in an invalid state
	 */
	private class SashDragManager 
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
			correctState = isInState(STATE_EXPANDED | STATE_PINNED_OPEN);
			origX = event.x;
		}
		public void mouseMove(MouseEvent e) {
			if (dragging && correctState)
				handleSashDragged(e.x - origX);
		}
		public void mouseUp(MouseEvent me) {
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
		implements Listener {
	protected boolean switchDock = false;
	protected boolean dragging = false;
	public TitleDragManager(Control ctrl) {
		ctrl.addListener(SWT.DragDetect, this);
		ctrl.addMouseListener(this);
	}
	public void handleEvent(Event event) {
		switchDock = false;
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
						switchDock = isDescendantOf(graphicalControl, ctrl);
						boolean invalid = false;
						if (!switchDock)
							invalid = !isDescendantOf(FlyoutPaletteComposite.this, ctrl);
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
		} else
			/*
			 * @TODO:Pratik   does this work on GTK?
			 */
			dragging = true;
		tracker.dispose();
	}
	public void mouseUp(MouseEvent me) {
		if (me.button != 1 || dragging) {
			dragging = false;
			return;
		}
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
			buttonSize.x = Math.max(height, buttonSize.x);
			// leave some space on the right of the button
			int buttonX = area.width - buttonSize.x - 1;
			button.setBounds(buttonX, 0, buttonSize.x, height);
			// leave some space to the left and right of the title
			title.setBounds(0, 0, buttonX, height);
			area.y += height;
			area.height -= height;
		}
		pViewer.getControl().setBounds(area);
	}
	protected void updateState() {
		if (isInState(STATE_PINNED_OPEN)) {
			title.setVisible(true);
			button.setVisible(true);
		} else {
			title.setVisible(false);
			button.setVisible(false);
		}
		layout();
	}
}

private class DragFigure 
		extends ImageFigure {
	public DragFigure() {
		setFont(JFaceResources.getBannerFont());
		updateImage();
		FlyoutPaletteComposite.this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				getImage().dispose();
			}
		});
	}	
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		if (hasFocus())
			graphics.drawFocus(0, 0, bounds.width - 1, bounds.height - 1);
	}
	protected void updateImage() {
		if (getImage() != null)
			getImage().dispose();
		Image img = null;
		TitleLabel fig = new TitleLabel();
		fig.setOpaque(true);
		fig.setBackgroundColor(ColorConstants.button);
		Dimension imageSize = fig.getActualPreferredSize();
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
	private static final int H_GAP = 4;
	private static final int LINE_LENGTH = 20;
	protected static final Border BORDER = new MarginBorder(0, 3, 0, 3);
	public TitleLabel() {
		super(GEFMessages.Palette_Label);
		setFont(JFaceResources.getBannerFont());
		setBorder(BORDER);
		Label tooltip = new Label(getText());
		tooltip.setBorder(BORDER);
		setToolTip(tooltip);
	}
	public Dimension getActualPreferredSize() {
		return super.getPreferredSize(-1, -1).getExpanded(
				(LINE_LENGTH  + H_GAP) * 2, 0);
	}
	public IFigure getToolTip() {
		if (isTextTruncated())
			return super.getToolTip();
		return null;
	}
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		org.eclipse.draw2d.geometry.Rectangle area = getClientArea();
		org.eclipse.draw2d.geometry.Rectangle textBounds = getTextBounds();
		// We reduce the width by 1 because FigureUtilities grows it by 1 unnecessarily
		textBounds.width--;
		int lineWidth = Math.min((area.width - textBounds.width - H_GAP * 2) / 2, 
				LINE_LENGTH); 
		if (lineWidth > 6) {
			graphics.setForegroundColor(ColorConstants.buttonLightest);
			int centerY = area.height / 2;
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
		}
		if (hasFocus())
			graphics.drawFocus(0, 0, bounds.width - 1, bounds.height - 1);
	}	
}

}