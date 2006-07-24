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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.SpringGraphViewer;


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
 * @author Ian Bull
 * @author Chris Callendar
 */
public class SampleView extends ViewPart {
	
	private SpringGraphViewer viewer;
	private Action addNodeAction;
	private Action deleteNodeAction;
	private Action pauseAction;
	private Action resumeAction;
	private Action stopAction;
	private Action restartAction;
	private Action minAction;
	private Action defaultAction;
	private Action maxAction;
	private Action noneAction;
	private Action randomAction;
	private Action zoomInAction;
	private Action zoomOutAction;
	private Action directedGraphToggle;
	
	private ZestImages images;
	
	/**
	 * The constructor.
	 */
	public SampleView() {
		this.images = new ZestImages();
	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new SpringGraphViewer(parent,  ZestStyles.PANNING | 
				ZestStyles.NO_OVERLAPPING_NODES | ZestStyles.ENFORCE_BOUNDS);
		
		viewer.setNodeStyle(ZestStyles.NODES_HIGHLIGHT_ADJACENT);
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		
		viewer.setContentProvider(new GraphContentProvider() );
		viewer.setLabelProvider(new DecoratingLabelProvider(new GraphLabelProvider(), 
					PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator()));
		
		viewer.setInput(getViewSite());
		
		makeActions();
		contributeToActionBars();
		hookContextMenu();
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
				
				boolean deleteEnabled = (viewer.getSelectedNode() != null);
				deleteNodeAction.setEnabled(deleteEnabled);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(minAction);
		manager.add(defaultAction);
		manager.add(maxAction);
		manager.add(randomAction);
		manager.add(noneAction);
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addNodeAction);
		//manager.add(deleteNodeAction);
		manager.add(new Separator());
		manager.add(pauseAction);
		manager.add(resumeAction);
		manager.add(new Separator());
		manager.add(stopAction);
		manager.add(restartAction);
		manager.add(directedGraphToggle);
		
		manager.add(new Separator());	
		if (zoomInAction != null) {
			manager.add(zoomInAction);
		}
		if (zoomOutAction != null) {
			manager.add(zoomOutAction);
		}
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addNodeAction);
		manager.add(deleteNodeAction);
		manager.add(directedGraphToggle);
		manager.add(new Separator());
		manager.add(pauseAction);
		manager.add(resumeAction);
		manager.add(new Separator());
		manager.add(stopAction);
		manager.add(restartAction);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	/**
	 * Creates the menu and toolbar actions.
	 */
	private void makeActions() {
		// Create an action that adds directed graph edges to the graph
		directedGraphToggle = new Action( ) {
			public void run() {
				int currentStyle = viewer.getNodeStyle();
				int newStyle = currentStyle ^ ZestStyles.NODES_HIGHLIGHT_ADJACENT;
				
				viewer.setNodeStyle( newStyle );
			}
		
		};
		directedGraphToggle.setText("Directed Graph");
		directedGraphToggle.setToolTipText("Tottle Directed Graph");
		directedGraphToggle.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_DIRECTED_GRAPH));
		
		
		
		// create an action that adds a new node to the graph
		addNodeAction = new Action() {
			private int currentNamesIndex = 0;
			/** Create a new node and add a connection from it to a random node. */
			public void run() {
				if (currentNamesIndex >= GraphContentProvider.NEWNAMES.length) {
					throw new RuntimeException("Error - out of new names :(");
				}
				String newNode = GraphContentProvider.NEWNAMES[currentNamesIndex++];
				String randomNode = GraphContentProvider.NAMES[(int)(Math.random()* GraphContentProvider.NAMES.length)];
				
				viewer.addNode(newNode);
				viewer.addRelationship(null, randomNode, newNode);
			}
		};
		addNodeAction.setText("Add Node");
		addNodeAction.setToolTipText("Add a new node");
		addNodeAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_ADD_NODE));
		addNodeAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_ADD_NODE_DISABLED));
		
		// create the action that deletes the selected node
		deleteNodeAction = new Action() {
			public void run() {
				Object selectedNode = viewer.getSelectedNode();
				if (selectedNode != null) {
					viewer.getViewer().removeNode(selectedNode);
				}
			}
		};
		deleteNodeAction.setText("Delete Node");
		deleteNodeAction.setToolTipText("Delete the selected node");
		deleteNodeAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_DELETE));
		deleteNodeAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_DELETE_DISABLED));
		
		// create the action that pauses/resumes the layout algorithm
		pauseAction = new Action() {
			public void run() {
				viewer.getViewer().pauseLayoutAlgorithm();
				pauseAction.setEnabled(false);
				resumeAction.setEnabled(true);
			}
		};
		pauseAction.setText("Pause");
		pauseAction.setToolTipText("Pause the layout algorithm");
		pauseAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_PAUSE));
		pauseAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_PAUSE_DISABLED));

		// create the action that resumes the layout algorithm
		resumeAction = new Action() {
			public void run() {
				viewer.getViewer().resumeLayoutAlgorithm();
				pauseAction.setEnabled(true);
				resumeAction.setEnabled(false);
			}
		};
		resumeAction.setText("Resume");
		resumeAction.setToolTipText("Resume the layout algorithm");
		resumeAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_RESUME));
		resumeAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_RESUME_DISABLED));
		resumeAction.setEnabled(false);	// initially disabled
		
		// create the action that stops the layout algorithm
		stopAction = new Action() {
			public void run() {
				viewer.reveal(viewer.getSelectedNode());
//				viewer.getViewer().stopLayoutAlgorithm();
//				stopAction.setEnabled(false);
//				restartAction.setEnabled(true);
//				pauseAction.setEnabled(false);
//				resumeAction.setEnabled(false);
			}
		};
		stopAction.setText("Stop");
		stopAction.setToolTipText("Stops the layout algorithm");
		stopAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_STOP));
		stopAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_STOP_DISABLED));

		// create the action that restarts the layout algorithm
		restartAction = new Action() {
			public void run() {
				viewer.getViewer().restartLayoutAlgorithm();
				stopAction.setEnabled(true);
				restartAction.setEnabled(false);
				pauseAction.setEnabled(true);
				resumeAction.setEnabled(false);
				
			}
		};
		restartAction.setText("Restart");
		restartAction.setToolTipText("Restarts the layout algorithm");
		restartAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_PLAY));
		restartAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_PLAY_DISABLED));
		restartAction.setEnabled(false);	// initially disabled
		
		minAction = new Action() {
			public void run() { updateWeights(1); }
		};
		minAction.setText("Min Spring Length");

		defaultAction = new Action() {
			public void run() { updateWeights(0.5); }
		};
		defaultAction.setText("Default Spring Length");	

		maxAction = new Action() {
			public void run() { updateWeights(0); }
		};
		maxAction.setText("Max Spring Length");	
		
		randomAction = new Action() {
			public void run() { updateWeights(Math.random()); }
		};
		randomAction.setText("Random Spring Tension");
		
		noneAction = new Action() {
			public void run() { updateWeights(-1); }
		};
		noneAction.setText("No Spring Tension");
		
		
		zoomInAction = new Action( ) {
			public void run() { viewer.zoomIn(); }
		};
		
		zoomInAction.setText("Zoom In");
		zoomInAction.setToolTipText("Zoom In");
		zoomInAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_ZOOM_IN));
		zoomInAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_ZOOM_IN_DISABLED));
		

		zoomOutAction = new Action( ) {
			public void run() { viewer.zoomOut(); }
		}; 
		zoomOutAction.setText("Zoom Out");
		zoomOutAction.setToolTipText("Zoom Out");
		zoomOutAction.setImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_ZOOM_OUT));
		zoomOutAction.setDisabledImageDescriptor(images.getImageDescriptor(ZestImages.IMG_ZEST_ZOOM_OUT_DISABLED));

	}
	
	/**
	 * Updates all the connections with the given weight.
	 * @param weight
	 */
	private void updateWeights(double weight) {
		for (int i = 1; i <= GraphContentProvider.NAMES.length; i++) {
			viewer.updateRelationshipWeight(""+i, weight);
		}
	}
	
}