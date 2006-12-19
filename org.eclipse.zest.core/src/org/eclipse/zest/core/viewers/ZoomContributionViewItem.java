/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.viewers;

import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;

/**
 * A contribution item that adds a combo to a toolbar or coolbar, or a
 * list of zooms to a menu. Can only be used for one toolbar, coolbar, or
 * menu.
 * 
 * In order to use this item, let your workbench part implement IZoomableWorkbenchPart.
 * If the workbench part then supplies a viewer that is zoomable, the combo
 * or menu created by this item will be enabled.
 * @author Del Myers
 *
 */
//@tag zest.bug.156286-Zooming.fix : create a contribution item that can set zooming on Zest views.
public class ZoomContributionViewItem extends ContributionItem implements ZoomListener {
	/**
	 * Zooms to fit the width.
	 */
	public static final String FIT_WIDTH = ZoomManager.FIT_WIDTH;
	/**
	 * Zooms to fit the height.
	 */
	public static final String FIT_HEIGHT = ZoomManager.FIT_HEIGHT;
	/**
	 * Zooms to fit entirely within the viewport.
	 */
	public static final String FIT_ALL = ZoomManager.FIT_ALL;
	
	
	private String[] zoomLevels;
	private ZoomManager zoomManager;
	private Combo combo;
	private Menu fMenu;
	private ZoomablePartListener partListener;
	//@tag zest.bug.159667-ZoomDispose : cache the part service so that we can properly dispose this item.
	private IPartService partSevice;
	
	private class ZoomablePartListener implements IPartListener {
		public void partActivated(IWorkbenchPart part) {
			if (part instanceof IZoomableWorkbenchPart) {
				if (zoomManager != null) {
					zoomManager.removeZoomListener(ZoomContributionViewItem.this);
				}
				zoomManager = ((IZoomableWorkbenchPart)part).getZoomableViewer().getZoomManager();
				if (zoomManager != null) {
					zoomManager.addZoomListener(ZoomContributionViewItem.this);
				}
				refresh(true);
			}
		}
		public void partDeactivated(IWorkbenchPart part) {
			if (part instanceof IZoomableWorkbenchPart) {
				if (zoomManager != null) {
					zoomManager.removeZoomListener(ZoomContributionViewItem.this);
				}
				zoomManager = ((IZoomableWorkbenchPart)part).getZoomableViewer().getZoomManager();
				refresh(true);
			}
		}
	
		public void partBroughtToTop(IWorkbenchPart part) {}
		public void partClosed(IWorkbenchPart part) {}
		
		public void partOpened(IWorkbenchPart part) {}
		
	}
	
	
	public ZoomContributionViewItem(IPartService partService) {
		this(partService, new String[] {"20%", "50%", "100%", "120%", "150%", "175%", "200%"});
	}
	
	/**
	 * Creates a new contribution item that will work on the given part
	 * service.initialZooms will be used to populate the combo or the menu.
	 * Valid values for initialZooms are percentage numbers (e.g., "100%"),
	 * or FIT_WIDTH, FIT_HEIGHT, FIT_ALL.
	 * 
	 * @param partService service used to see whether the view is zoomable.
	 * @param initialZooms the initial zoom values.
	 */
	public ZoomContributionViewItem(IPartService partService, String[] initialZooms) {
		this.zoomLevels = initialZooms;
		this.partListener = new ZoomablePartListener();
		this.partSevice = partService;
		partService.addPartListener(partListener);
		//@tag zest.bug.159647 : if we are in the ui thread initialize the part listener.
		if (Display.getCurrent() != null) {
			IWorkbenchPart part = partService.getActivePart();
			partListener.partActivated(part);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.Menu, int)
	 */
	public void fill(Menu menu, int index) {
		this.fMenu = menu;
		for (int i = 0; i < zoomLevels.length; i++) {
			final MenuItem item = new MenuItem(fMenu, SWT.RADIO);
			if( zoomManager.getZoomAsText().equals(zoomLevels[i])) {
				item.setSelection(true);
			}
			item.setText(zoomLevels[i]);
			item.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					doZoom(item.getText());
				}
			});
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.CoolBar, int)
	 */
	public void fill(CoolBar parent, int index) {
		CoolItem item = new CoolItem(parent, SWT.DROP_DOWN);
		Combo combo = createCombo(parent);
		item.setControl(combo);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionItem#fill(org.eclipse.swt.widgets.ToolBar, int)
	 */
	public void fill(ToolBar parent, int index) {
		ToolItem item = new ToolItem(parent, SWT.DROP_DOWN);
		Combo combo = createCombo(parent);
		item.setControl(combo);
	}
	
	private Combo createCombo(Composite parent) {
		this.combo = new Combo(parent, SWT.DROP_DOWN);
		this.combo.setItems(zoomLevels);
		this.combo.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				int selection = combo.getSelectionIndex();
				if (selection >0) {
					doZoom(combo.getItem(selection));
				} else {
					doZoom(combo.getItem(0));
				}
			}
		});
		return this.combo;
	}
	
	private void doZoom(String zoom) {
		if (zoomManager != null) {
			zoomManager.setZoomAsText(zoom);
		}
	}
	
	private void refresh(boolean rebuild) {
		//
		if (combo != null && !combo.isDisposed()) {
			refreshCombo(rebuild);
		} else if (fMenu != null && fMenu.isDisposed()) {
			refreshMenu(rebuild);
		}
	}
	
	/**
	 * @param rebuild
	 */
	private void refreshMenu(boolean rebuild) {
		fMenu.setEnabled(false);
		if (zoomManager == null) {
			return;
		}
		if (rebuild) {
			zoomLevels = zoomManager.getZoomLevelsAsText();
			MenuItem[] oldItems = fMenu.getItems();
			for (int i = 0; i < oldItems.length; i++) {
				oldItems[i].dispose();
			}
			for (int i = 0; i < zoomLevels.length;i++) {
				MenuItem item = new MenuItem(fMenu, SWT.RADIO);
				item.setText(zoomLevels[i]);
				item.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						MenuItem source = (MenuItem)e.getSource();
						doZoom(source.getText());
					}
				});
			}
		}
		MenuItem[] items = fMenu.getItems();
		String zoom = zoomManager.getZoomAsText();
		for (int i = 0; i < items.length; i++) {
			items[i].setSelection(false);
			if (zoom.equalsIgnoreCase(items[i].getText())) {
				items[i].setSelection(true);
			}
		}
		fMenu.setEnabled(true);
	}

	/**
	 * @param rebuild
	 */
	private void refreshCombo(boolean rebuild) {
		combo.setEnabled(false);
		if (zoomManager == null) {
			return;
		}
		if (rebuild) {
			combo.setItems(zoomManager.getZoomLevelsAsText());
		}	
		String zoom = zoomManager.getZoomAsText();
		int index = combo.indexOf(zoom);
		if (index > 0) {
			combo.select(index);
		}
		combo.setEnabled(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
	 */
	public void zoomChanged(double z) {
		refresh(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ContributionItem#dispose()
	 */
	
	public void dispose() {
		if (combo != null) {
			combo = null;
		}
		if (fMenu != null) {
			fMenu = null;
		}
//		@tag zest.bug.159667-ZoomDispose : make sure that we no longer listen to the part service.
		partSevice.removePartListener(partListener);
		super.dispose();
	}
}
