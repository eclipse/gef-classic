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

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylar.zest.core.viewers.INestedGraphEntityContentProvider;


/**
 * 
 * 
 * @author Chris Callendar
 */
public class JavaHierarchyContentProvider extends StandardJavaElementContentProvider 
	implements INestedGraphEntityContentProvider {

	private IJavaElement[] rootElements = null;
	
	/**
	 * 
	 */
	public JavaHierarchyContentProvider() {
		super(true);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.ui.StandardJavaElementContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object parent) {
		return rootElements;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.ui.StandardJavaElementContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object element) {
		Object[] children = super.getChildren(element);
		if (children == null) {
			children = new Object[0];
		} else if (children.length > 0){
			ArrayList list = new ArrayList();
			for (int i = 0; i < children.length; i++) {
				Object child = children[i];
				if ((child instanceof IProject) ||
					(child instanceof IPackageFragmentRoot) || 
					(child instanceof IType) || 
					(child instanceof IFile) || 
					(child instanceof IClassFile) || 
					(child instanceof ICompilationUnit) || 
					(child instanceof IMethod)) {
					if (!(child instanceof JarPackageFragmentRoot)) {
						list.add(child);
					}
				} else if (child instanceof IPackageFragment) {
					try {
						if (((IPackageFragment)child).containsJavaResources()) {
							list.add(child);
						}
					} catch (JavaModelException jme) {
					}
				}

			}
			children = new Object[list.size()];
			children = (Object[])list.toArray(children);
		}
		return children;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.ui.StandardJavaElementContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		Object parent = super.getParent(element);
		return parent;
	}

	/**
	 * At the moment this gets a few random connections between the
	 * given entity and it's siblings.
	 * @see ca.uvic.cs.zest.viewers.IGraphEntityContentProvider#getConnectedTo(java.lang.Object)
	 */
	public Object[] getConnectedTo(Object entity) {
		Object[] conn = new Object[0];
		Object parent = getParent(entity);
		if (parent != null) {
			Object[] children = getChildren(parent);
			if (children.length > 1) {
				int max = (int)(Math.random() * (children.length - 1)); 
				conn = new Object[max];
				int index = 0;
				for (int i = 0; i < children.length && index < conn.length; i++) {
					if (children[i] != entity) {
						conn[index] = children[i];
						index++;
					}
				}
			}
		}
		return conn;
	}

	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.viewers.IGraphEntityContentProvider#getWeight(java.lang.Object, java.lang.Object)
	 */
	public double getWeight(Object entity1, Object entity2) {
		// TODO Auto-generated method stub
		return 0.2;
	}
	
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		super.inputChanged(viewer, oldInput, newInput);

		if ( newInput != null ) {
			IProject[] projects = (IProject[]) newInput;
			
			//IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			rootElements = new IJavaElement[projects.length];
			for (int i = 0; i < rootElements.length; i++) {
				rootElements[i] = JavaCore.create(projects[i]);
			}
		}
	}
	
	
	
	

}
