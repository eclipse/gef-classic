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
package org.eclipse.gef.examples.ediagram.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.draw2d.geometry.Point;

/**
 * @author Pratik Shah
 * @since 3.1
 * @model abstract="true"
 */
public interface Node
	extends EObject{
	
	/**
	 * @model
	 */
	Point getLocation();
	
	/**
	 * @model default="-1"
	 */
	int getWidth();

	/**
	 * Sets the value of the '{@link org.eclipse.gef.examples.ediagram.model.Node#getWidth <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Width</em>' attribute.
	 * @see #getWidth()
	 * @generated
	 */
	void setWidth(int value);

	/**
	 * Sets the value of the '{@link org.eclipse.gef.examples.ediagram.model.Node#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(Point value);

	/**
	 * @model
	 */
	Diagram getDiagram();

	/**
	 * Sets the value of the '{@link org.eclipse.gef.examples.ediagram.model.Node#getDiagram <em>Diagram</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagram</em>' container reference.
	 * @see #getDiagram()
	 * @generated
	 */
	void setDiagram(Diagram value);

	/**
	 * @model type="Link" opposite="target" lower="0"
	 */
	EList getIncomingConnections(); 
	
	/**
	 * @model type="Link" containment="true" opposite="source" lower="0"
	 */
	EList getOutgoingConnections();
	
}