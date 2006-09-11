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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.ZoomManager;
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
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.INestedGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelEntityFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedGraphModelNode;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.NestedGraphViewerImpl;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphNodeEditPart;
import org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts.NestedGraphRootEditPart;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.swt.SWT;
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
public class NestedGraphViewer extends AbstractStructuredGraphViewer implements IDoubleClickListener, ControlListener,
		DisposeListener, PropertyChangeListener {

	private Composite parent = null;

	private NestedGraphViewerImpl viewer = null;

	private INestedGraphModelFactory modelFactory = null;

	private NestedGraphModel model;

	private List focusListeners;

	// to prevent endless loop when setting selection in the two viewers
	private boolean settingViewerSelection = false;

	private boolean settingTreeSelection = false;

	/**
	 * Initializes the viewer with the given styles.
	 * 
	 * @param parent
	 * @param style
	 *            the styles (which also get passed to the viewer impl
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
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayoutData(data);
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		parent.setLayout(layout);
		parent.addControlListener(this);

		this.viewer = new NestedGraphViewerImpl(parent, style  );
		data = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.viewer.getControl().setLayoutData(data);

		this.viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setSelectionToTreeViewer(event.getSelection(), true);
			}
		});

		hookControl(this.viewer.getControl());

		focusListeners = new LinkedList();
	}

	/**
	 * Gets the styles for this structuredViewer
	 * 
	 * @return
	 */
	public int getStyle() {
		return this.viewer.getStyle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#setContentProvider(org.eclipse.jface.viewers.IContentProvider)
	 */
	public void setContentProvider(IContentProvider contentProvider) {
		if (contentProvider instanceof INestedGraphContentProvider) {
			super.setContentProvider(contentProvider);
		} else if (contentProvider instanceof INestedGraphEntityContentProvider) {
			super.setContentProvider(contentProvider);
		} else {
			throw new IllegalArgumentException(
					"Invalid content provider, only INestedGraphContentProvider and INestedGraphEntityContentProvider are supported.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setLabelProvider(org.eclipse.jface.viewers.IBaseLabelProvider)
	 */
	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		super.setLabelProvider(labelProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object,
	 *      java.lang.Object)
	 */
	protected void inputChanged(Object input, Object oldInput) {
		boolean highlightAdjacentNodes = ZestStyles.checkStyle(viewer.getStyle(), ZestStyles.NODES_HIGHLIGHT_ADJACENT);

		// // TODO - NestedGraphContentProvider
		// if (getContentProvider() instanceof INestedGraphContentProvider) {
		// modelFactory = new NestedGraphModelFactory(this,
		// highlightAdjacentNodes);
		// }
		// else
		if (model != null) {
			// remove this as a listener from the old model.
			model.removePropertyChangeListener(this);
		}
		if (getContentProvider() instanceof INestedGraphEntityContentProvider) {
			modelFactory = new NestedGraphModelEntityFactory(this, highlightAdjacentNodes);
		}
		// DebugPrint.println("Input Is: " + input);
		//@tag bug(153348-NestedStyle(fix)) : add connection style and node style.
		//@tag bug(154412-ClearStatic(fix)) : the factory now returns a generic GraphModel so that this method can be abstracted into AbstractStylingModelFactory.
		model = (NestedGraphModel)modelFactory.createModelFromContentProvider(input, getNodeStyle(), getConnectionStyle());
		model.setNodeStyle(getNodeStyle());
		model.setConnectionStyle(getConnectionStyle());
		model.addPropertyChangeListener(this);

		// sets the model contents
		viewer.setContents(model);

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
	 * 
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
	 * Sets the selection to the viewer. The list contains the data objects
	 * which are used to retrieve the NestedGraphModelNode elements. These in
	 * turn are used to get the associated edit part.
	 * 
	 * @param list
	 *            the list of objects to select
	 */
	protected void setSelectionToWidget(List list, boolean reveal) {
		if (settingTreeSelection)
			return;

		if (list == null) {
			viewer.setSelection(new StructuredSelection());
			return;
		}

		ArrayList editPartList = new ArrayList(list.size());
		for (int i = 0; i < list.size(); i++) {
			Object data = list.get(i);
			NestedGraphModelNode node = (NestedGraphModelNode) model.getInternalNode(data);
			EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(node);
			if (editPart != null) {
				// ensure that all the parents are showing their children
				if (reveal && (node.getParent() != null)) {
					for (NestedGraphModelNode parentNode = node.getCastedParent(); parentNode != null; parentNode = parentNode
							.getCastedParent()) {
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
	 * Sets the selection to the tree viewer. The list contains the
	 * NestedGraphNodeEditPart objects. The NestedGraphModelNode objects are
	 * gotten from the edit part and then the data object for the node is
	 * retrieved and put in a StructuredSelection and passed to the TreeViewer.
	 * 
	 * @param list
	 *            the list of objects to select
	 */
	protected void setSelectionToTreeViewer(ISelection selection, boolean reveal) {
		if (settingViewerSelection)
			return;

		if (selection instanceof IStructuredSelection) {
			List list = ((IStructuredSelection) selection).toList();
			ArrayList treeList = new ArrayList(list.size());
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				Object data = null;
				if (obj instanceof NestedGraphNodeEditPart) {
					NestedGraphNodeEditPart editPart = (NestedGraphNodeEditPart) obj;
					NestedGraphModelNode node = editPart.getCastedModel();
					data = node.getData();
				} else if (obj instanceof NestedGraphEditPart) {
					NestedGraphEditPart editPart = (NestedGraphEditPart) obj;
					NestedGraphModelNode node = editPart.getCastedModel().getCurrentNode();
					data = node.getData();
				}
				if (data != null) {
					treeList.add(data);
				}
			}
			settingTreeSelection = true;
			settingTreeSelection = false;
		}
	}

	public Control getControl() {
		return viewer.getControl();
	}

	// Control Listener methods
	public void controlMoved(ControlEvent e) {
	}

	public void controlResized(ControlEvent e) {
		((Composite) getControl()).layout(true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
	public void doubleClick(DoubleClickEvent event) {
		ISelection selection = event.getSelection();
		if (!selection.isEmpty() && (selection instanceof IStructuredSelection)) {
			List list = ((IStructuredSelection) selection).toList();
			Object data = list.get(0);
			NestedGraphModelNode node = (NestedGraphModelNode) model.getInternalNode(data);
			if ((node != null) && (node.getChildren().size() > 0) && !node.equals(model.getCurrentNode())) {
				model.setCurrentNode(node);
				viewer.fireModelUpdate();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent e) {
		if ( focusListeners != null)
			focusListeners.clear();
	}

	/**
	 * This implementation ignores the run boolean.
	 * @param algorithm
	 * @param run ignored by this implementation.
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean run) {
		viewer.setLayoutAlgorithm(algorithm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	// @tag bug(151889-ViewCoupling) : use a listener to update UI elements that
	// want to know about focus changes.
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == model) {
			if (evt.getPropertyName().equals(GraphModel.NODE_FOCUS_PROP)) {
				IGraphModelNode old = (IGraphModelNode) evt.getOldValue();
				IGraphModelNode current = (IGraphModelNode) evt.getNewValue();
				Object externOld = (old != null) ? old.getExternalNode() : null;
				Object externCurrent = null;
				if (current != model.getRootNode()) {
					externCurrent = (current != null) ? current.getExternalNode() : null;
				}
				fireFocusChanged(externOld, externCurrent);
			}
		}
	}

	/**
	 * Add a listener for when focus (root-level) nodes change in this viewer.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addFocusListener(INestedGraphFocusChangedListener listener) {
		// @tag performance(synchronization) : consider a write-behind method if
		// concurrent modifications happen from non-display thread changes in
		// the model.
		if (!focusListeners.contains(listener))
			focusListeners.add(listener);
	}

	/**
	 * Removes the given listener from the list of focus change listeners.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
	public void removeFocusListener(INestedGraphFocusChangedListener listener) {
		// @tag performance(synchronization) : consider a write-behind method if
		// concurrent modifications happen from non-display thread changes in
		// the model.
		focusListeners.remove(listener);
	}

	private void fireFocusChanged(Object externOld, Object externCurrent) {
		// @tag performance(synchronization) : consider a write-behind method if
		// concurrent modifications happen from non-display thread changes in
		// the model.
		for (Iterator i = focusListeners.iterator(); i.hasNext();) {
			((INestedGraphFocusChangedListener) i.next()).focusChanged(externOld, externCurrent);
		}
	}

	/**
	 * Move forward in graph view navigation.
	 * 
	 */
	public void goForward() {
		// the model supports it, so we may as well expose it.
		viewer.goForward();
	}

	/**
	 * Move backward in graph view navigation.
	 * 
	 */
	public void goBackward() {
		viewer.goBack();
	}

	/**
	 * Move to the parent in graph view navigation.
	 * 
	 */
	public void goUp() {
		viewer.goUp();
	}

	/**
	 * @return true iff the model can navigate forward.
	 */
	public boolean canGoBackward() {
		return model.hasBackNode();
	}

	/**
	 * Sets the focus to the given node, if it exists in the model. If null is
	 * supplied, the top-level node is set as the focus.
	 * 
	 * @param node
	 *            the node to focus on.
	 */
	public void setFocus(Object node) {
		if (node == null) {
			viewer.setCurrentNode(model.getRootNode());
		} else {
			viewer.setCurrentNode((NestedGraphModelNode) model.getInternalNode(node));
		}
	}

	/**
	 * Returns the current node of focus. If null is returned, the client can assume
	 * the top-level node of the graph.
	 * @return the current node of focus.
	 */
	public Object getFocus() {
		IGraphModelNode node = model.getCurrentNode();
		if (node == model.getRootNode()) {
			return null;
		} else if (node != null) {
			return node.getExternalNode();
		}
		return null;
	}
	/**
	 * 
	 * @return true iff the model can navigate backward.
	 */
	public boolean canGoForward() {
		return model.hasForwardNode();
	}

	/**
	 * 
	 * @return true iff the current focus node has a parent.
	 */
	public boolean focusHasParent() {
		return model.hasParentNode();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.viewers.AbstractZoomableViewer#getZoomManager()
	 */
	protected ZoomManager getZoomManager() {
		return ((NestedGraphRootEditPart)viewer.getRootEditPart()).getZoomManager();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.viewers.AbstractStructuredGraphViewer#applyLayout()
	 */
	public void applyLayout() {
		//do nothing. Nested viewers have internal layouts.
		//@tag zest.check : should we actually layout the current node?
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.viewers.AbstractStructuredGraphViewer#getModel()
	 */
	protected GraphModel getModel() {
		return model;
	}

}
