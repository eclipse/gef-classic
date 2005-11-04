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

import org.eclipse.jface.viewers.Viewer;

import org.eclipse.mylar.zest.core.viewers.INestedGraphEntityContentProvider;

/**
 * Sample nested graph provider which uses the entities as
 * the primary way of generating the content.  The entities are represented in 
 * a binary tree in this example.
 * 
 * @author Chris Callendar
 */
public class NestedGraphEntityContentProvider implements INestedGraphEntityContentProvider {

	private final String[] ENTITIES = new String[] { "Figure", "Shape", "Layer", "RectangleFigure", "Ellipse", "FreeformLayer", "LayeredPane", "NestedFigure", "ScaledFigure" };
	
	private final int CHILDREN = 2;
	//private Random random = null;

	public NestedGraphEntityContentProvider() {
		//random = new Random();
	}

	/**
	 * Returns the root elements.
	 */
	public final Object[] getElements(Object inputElement) {
		return new String[] { ENTITIES[0] };
	}
	
	public Object[] getConnectedTo(Object entity) {
		Object[] cons = new Object[0];
		int[] siblings = getSiblingIndeces(getIndex(entity));
		if ((siblings.length > 0) && (siblings[siblings.length-1] < ENTITIES.length)) {
			cons = new String[siblings.length];
			for (int i = 0; i < cons.length; i++) {
				cons[i] = ENTITIES[siblings[i]];
			}
		}
		return cons;
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.viewers.INestedGraphEntityContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object node) {
		Object[] children = new Object[0];
		int parent = getIndex(node);
		if (parent >= 0) {
			int start = (parent * CHILDREN) + 1;
			children = new String[Math.max(0, Math.min(CHILDREN, ENTITIES.length - start))];
			for (int i = 0; i < children.length; i++) {
				children[i] = ENTITIES[start + i]; 
			}
		}
		return children;
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.viewers.INestedGraphEntityContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object node) {
		Object[] children = getChildren(node);
		return ((children != null) && (children.length > 0));
	}
	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.viewers.INestedGraphEntityContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object node) {
		Object parent = null;
		for (int i = 1; i < ENTITIES.length; i++) {
			if (ENTITIES[i].equals(node)) {
				int index = (i-1) / CHILDREN;
				parent = ENTITIES[index];
				break;
			}
		}
		return parent;
	}

	public double getWeight(Object entity1, Object entity2) {
		return 0.2D;
	}
	
	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}

	private int getIndex(Object obj) {
		int index = -1;
		for (int i = 0; i < ENTITIES.length; i++) {
			if (ENTITIES[i].equals(obj)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	private int[] getSiblingIndeces(int index) {
		int[] indeces = new int[0];
		int parent = getParentIndex(index);
		if (parent >= 0) {
			indeces = new int[CHILDREN-1];
			int j = 0;
			for (int i = 0; i < CHILDREN; i++) {
				int sib = (parent * CHILDREN) + (i + 1);
				if (sib != index) {
					indeces[j++] = sib;
				}
			}
		}
		return indeces;
	}
	
	private int getParentIndex(int index) {
		int parent = -1;
		if (index > 0) {
			parent = (index-1) / CHILDREN;
		}
		return parent;
	}
	
}
