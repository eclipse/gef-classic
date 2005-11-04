/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.tests;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.TimeLineViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class shows a Timeline view.
 * 
 * @author Chris Callendar
 */
public class SampleTimeLineView extends ViewPart {
	
	private TimeLineViewer viewer;
	//private Action addNodeAction;
	
	
	/**
	 * The constructor.
	 */
	public SampleTimeLineView() {

	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TimeLineViewer(parent, ZestStyles.NONE );
		
//		viewer.setContentProvider(new GraphContentProvider() );
//		viewer.setLabelProvider(new DecoratingLabelProvider(new GraphLabelProvider(), 
//					PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
		
		viewer.setInput(getViewSite());
		
//		makeActions();
//		contributeToActionBars();
//		hookContextMenu();
	}
	
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleTimeLineView.this.fillContextMenu(manager);
				
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}
	
	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void fillLocalPullDown(IMenuManager manager) {
		//manager.add(noneAction);
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		//manager.add(addNodeAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		//manager.add(addNodeAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	
}