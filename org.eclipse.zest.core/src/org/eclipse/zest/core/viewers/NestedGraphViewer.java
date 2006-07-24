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
package org.eclipse.mylar.zest.core.viewers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.INestedGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelEntityFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphNodeEditPart;
import org.eclipse.mylar.zest.core.widgets.BreadCrumbBar;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ian Bull
 */
public class NestedGraphViewer extends AbstractStructuredGraphViewer 
	implements IDoubleClickListener, ControlListener, DisposeListener {

	private Composite parent = null;
	private NestedGraphViewerImpl viewer = null;
	private INestedGraphModelFactory modelFactory = null;
	private NestedGraphModel model;
	private BreadCrumbBar breadCrumbBar;
	private TreeRootViewer treeViewer;
	private SashForm sashForm;
	private GridData sashData;
	
	// to prevent endless loop when setting selection in the two viewers
	private boolean settingViewerSelection = false;
	private boolean settingTreeSelection = false;
	
	/**
	 * Initializes the viewer with the given styles.
	 * @param parent
	 * @param style the styles (which also get passed to the viewer impl
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#NODES_HIGHLIGHT_ADJACENT
	 * @see ZestStyles#NO_SCROLLBARS
	 * @see ZestStyles#ZOOM_EXPAND
	 * @see ZestStyles#ZOOM_FAKE
	 * @see ZestStyles#ZOOM_REAL
	 */
	public NestedGraphViewer(Composite parent, int style) {
		super(style);
		this.parent = parent;
		this.parent.addDisposeListener(this);
				
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		parent.setLayout(layout);
		parent.addControlListener(this);

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		this.breadCrumbBar = new BreadCrumbBar(parent, SWT.SHADOW_ETCHED_IN);
		breadCrumbBar.setLayoutData(gridData);

		this.sashForm = new SashForm(parent, style | /*SWT.SMOOTH | */SWT.BORDER);
		sashForm.setOrientation(SWT.HORIZONTAL);
		sashData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_FILL, true, true);
		sashData.widthHint = 800;
		sashData.heightHint = 300;
		sashForm.setLayoutData(sashData);
		
		treeViewer = new TreeRootViewer(sashForm, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//treeViewer.setSorter(new ViewerSorter());
		treeViewer.addDoubleClickListener(this);
		treeViewer.setUseHashlookup(true);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setSelectionToWidget(event.getSelection(), true);
			}
		});
		
		this.viewer = new NestedGraphViewerImpl(sashForm, style, breadCrumbBar, treeViewer);
		
		this.viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setSelectionToTreeViewer(event.getSelection(), true);
			}
		});
		
		hookControl( this.viewer.getControl() );
		
		sashForm.setWeights(new int[]{1, 4});
	}
	
	
	/**
	 * Gets the styles for this structuredViewer
	 * @return
	 */
	public int getStyle() {
		return this.viewer.getStyle();
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ContentViewer#setContentProvider(org.eclipse.jface.viewers.IContentProvider)
	 */
	public void setContentProvider(IContentProvider contentProvider) {
		if (contentProvider instanceof INestedGraphContentProvider) {
			super.setContentProvider(contentProvider);
			treeViewer.setContentProvider(contentProvider);
		} else if ( contentProvider instanceof INestedGraphEntityContentProvider ) {
			super.setContentProvider(contentProvider);
			treeViewer.setContentProvider(contentProvider);
		} else {
			throw new IllegalArgumentException("Invalid content provider, only INestedGraphContentProvider and INestedGraphEntityContentProvider are supported.");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#setLabelProvider(org.eclipse.jface.viewers.IBaseLabelProvider)
	 */
	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		super.setLabelProvider(labelProvider);
		treeViewer.setLabelProvider(labelProvider);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	protected void inputChanged(Object input, Object oldInput) {
		boolean highlightAdjacentNodes = ZestStyles.checkStyle(viewer.getStyle(), ZestStyles.NODES_HIGHLIGHT_ADJACENT);

//		// TODO - NestedGraphContentProvider		
//		if (getContentProvider() instanceof INestedGraphContentProvider) {
//			modelFactory = new NestedGraphModelFactory(this, highlightAdjacentNodes);
//		}
//		else 
		if (getContentProvider() instanceof INestedGraphEntityContentProvider) {
			modelFactory = new NestedGraphModelEntityFactory(this, highlightAdjacentNodes);
		}
		//DebugPrint.println("Input Is: " + input);
		model = modelFactory.createModelFromContentProvider(input);
		model.setNodeStyle(getNodeStyle());
		model.setConnectionStyle(getConnectionStyle());
		treeViewer.setRootDataItem(model.getRootNode());
		treeViewer.setInput(getInput());
		
		// sets the model contents
		viewer.setContents(model);

		treeViewer.expandToLevel(2);
	}
	
	protected NestedGraphViewerImpl getViewer() {
		return viewer;
	}
	
	// StructuredViewer implemented methods
	
	protected Widget doFindInputItem(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Widget doFindItem(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
		// TODO Auto-generated method stub

	}

	protected void internalRefresh(Object element) {
		// TODO Auto-generated method stub

	}

	public void reveal(Object element) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the selected model elements as a List.
	 * @return List
	 */
	protected List getSelectionFromWidget() {
		Widget[] items = viewer.getSelectedModelElements();
		ArrayList list = new ArrayList(items.length);
		for (int i = 0; i < items.length; i++) {
			Widget item = items[i];
			Object e = item.getData();
			if (e != null)
				list.add(e);
		}
		return list;
	}

	/**
	 * Sets the selection to the viewer.  The list contains the data objects 
	 * which are used to retrieve the NestedGraphModelNode elements.  These in turn 
	 * are used to get the associated edit part.
	 * @param list	the list of objects to select
	 */
	protected void setSelectionToWidget(List list, boolean reveal) {
		if (settingTreeSelection)
			return;
		
        if (list == null) {
            viewer.setSelection(new StructuredSelection());
            return;
        }
        
		ArrayList editPartList = new ArrayList(list.size());
		for ( int i = 0; i < list.size(); i++ ) {
			Object data = list.get(i);
			NestedGraphModelNode node = (NestedGraphModelNode)model.getInternalNode(data);
			EditPart editPart = (EditPart)viewer.getEditPartRegistry().get(node);
			if (editPart != null) {
				// ensure that all the parents are showing their children
				if (reveal && (node.getParent() != null)) {
					for (NestedGraphModelNode parentNode = node.getCastedParent(); parentNode != null; parentNode = parentNode.getCastedParent()) {
						parentNode.setChildrenVisible(true);
					}
				}
				editPartList.add(i, editPart);
			}
		}
		settingViewerSelection = true;
		viewer.setSelection(new StructuredSelection(editPartList));
		settingViewerSelection = false;
	}
	
	/**
	 * Sets the selection to the tree viewer. The list contains the NestedGraphNodeEditPart objects.
	 * The NestedGraphModelNode objects are gotten from the edit part and then
	 * the data object for the node is retrieved and put in a StructuredSelection and passed
	 * to the TreeViewer.  
	 * @param list	the list of objects to select
	 */
	protected void setSelectionToTreeViewer(ISelection selection, boolean reveal) {
		if (settingViewerSelection)
			return;
		
		if (selection instanceof IStructuredSelection) {
			List list = ((IStructuredSelection)selection).toList();
			ArrayList treeList = new ArrayList(list.size());
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				Object data = null;
				if (obj instanceof NestedGraphNodeEditPart) {
					NestedGraphNodeEditPart editPart = (NestedGraphNodeEditPart)obj;
					NestedGraphModelNode node = editPart.getCastedModel();
					data = node.getData();
				} else if (obj instanceof NestedGraphEditPart) {
					NestedGraphEditPart editPart = (NestedGraphEditPart)obj;
					NestedGraphModelNode node = editPart.getCastedModel().getCurrentNode();
					data = node.getData();
				}
				if (data != null) {
					treeList.add(data);
				}
			}
			settingTreeSelection = true;
			treeViewer.setSelection(new StructuredSelection(treeList), reveal);
			settingTreeSelection = false;
		}
	}	
	
	public Control getControl() {
		return viewer.getControl();
	}
	
	// Control Listener methods
	public void controlMoved(ControlEvent e) {}
	
	
	
	public void controlResized(ControlEvent e) {
		int width = parent.getSize().x - 4;
		int height = parent.getSize().y - 4;
		int bcHeight = 24;
		
		breadCrumbBar.setLayoutWidth(width);
		breadCrumbBar.setSize(width, bcHeight);
		breadCrumbBar.layout(true, true);

		
		sashData.widthHint = width;
		sashData.minimumWidth = width - 4;
		sashData.heightHint = height - bcHeight - 18;
		sashData.minimumHeight = height - bcHeight - 18;
		sashForm.layout(true, true);

		
		
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	public void doubleClick(DoubleClickEvent event) {
		ISelection selection = event.getSelection();
		if (!selection.isEmpty() && (selection instanceof IStructuredSelection)) {
			List list = ((IStructuredSelection)selection).toList();
			Object data = list.get(0);
			NestedGraphModelNode node = (NestedGraphModelNode)model.getInternalNode(data);
			if ((node != null) && (node.getChildren().size() > 0) && !node.equals(model.getCurrentNode())) {  
				model.setCurrentNode(node);
				viewer.fireModelUpdate();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		breadCrumbBar.dispose();
	}

	/**
	 * @param algorithm
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
		viewer.setLayoutAlgorithm(algorithm);
	}
	
}
