/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model;

import org.eclipse.emf.ecore.EReference;

/**
 * @author Pratik Shah
 * @since 3.1
 * @model
 */
public interface ReferenceView 
	extends Link{

	/**
	 * @model 
	 */
	EReference getEReference();

	/**
	 * Sets the value of the '{@link org.eclipse.gef.examples.ediagram.model.ReferenceView#getEReference <em>EReference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>EReference</em>' reference.
	 * @see #getEReference()
	 * @generated
	 */
	void setEReference(EReference value);

	/**
	 * @model
	 */
	boolean isOppositeShown();

	/**
	 * Sets the value of the '{@link org.eclipse.gef.examples.ediagram.model.ReferenceView#isOppositeShown <em>Opposite Shown</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Opposite Shown</em>' attribute.
	 * @see #isOppositeShown()
	 * @generated
	 */
	void setOppositeShown(boolean value);

}
