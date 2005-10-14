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

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.NestedGraphViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 * 
 * @author irbull
 * @author ccallendar
 */
public class SampleNestedView extends ViewPart {
	
	private NestedGraphViewer nestedViewer;
	//private Action deleteNodeAction;
	//private ZestImages images;
	
	/**
	 * The constructor.
	 */
	public SampleNestedView() {
		//this.images = new ZestImages();
	}
	
	
	class MyResourceChangedListener implements IResourceChangeListener {

		NestedGraphViewer _viewer;
		public MyResourceChangedListener( NestedGraphViewer viewer ) {
			_viewer = viewer;
			
		}
		public void resourceChanged(IResourceChangeEvent event) {
			//TODO: Why do I need to to async this? [irbull]
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					System.out.println("Runnable called");
					if (_viewer.getContentProvider() != null) {
						// must have a content provider
						_viewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects() );
					}
				}
			});
		}
		
	}
	
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		nestedViewer = new NestedGraphViewer(parent, 
				ZestStyles.HIGHLIGHT_ADJACENT_NODES | /* ZestStyles.NO_OVERLAPPING_NODES | */ 
				ZestStyles.DIRECTED_GRAPH | ZestStyles.ZOOM_FAKE | ZestStyles.ENFORCE_BOUNDS);
		
		//NestedGraphEntityContentProvider contentProvider = new NestedGraphEntityContentProvider();
		JavaHierarchyContentProvider contentProvider = new JavaHierarchyContentProvider();
		nestedViewer.setContentProvider(contentProvider);

//		DecoratingLabelProvider labelProvider = new DecoratingLabelProvider(new NestedGraphLabelProvider(), 
//					PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator());

		DecoratingLabelProvider labelProvider = new DecoratingLabelProvider(new WorkbenchLabelProvider(),
				PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator());
		
		nestedViewer.setLabelProvider(labelProvider);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new MyResourceChangedListener(nestedViewer) );
		nestedViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		
		
		makeActions();
		//contributeToActionBars();
		//hookContextMenu();
	}
	
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
		nestedViewer.getControl().setFocus();
	}
	
	protected void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleNestedView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(nestedViewer.getControl());
		nestedViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, nestedViewer);
	}
	
	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	protected void fillLocalPullDown(IMenuManager manager) {
	}
	
	protected void fillLocalToolBar(IToolBarManager manager) {
		manager.add(new Separator());	
	}

	protected void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator());

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	
	/**
	 * Creates the menu and toolbar actions.
	 */
	protected void makeActions() {

	}
	
}