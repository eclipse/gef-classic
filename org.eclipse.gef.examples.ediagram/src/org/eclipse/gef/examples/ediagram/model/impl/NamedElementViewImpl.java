/**
 * <copyright>
 *******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************
 * </copyright>
 *
 * $Id: NamedElementViewImpl.java,v 1.2 2004/12/07 19:07:09 pshah Exp $
 */
package org.eclipse.gef.examples.ediagram.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Named Element View</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gef.examples.ediagram.model.impl.NamedElementViewImpl#getENamedElement <em>ENamed Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NamedElementViewImpl extends NodeImpl implements NamedElementView
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = ""; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getENamedElement() <em>ENamed Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getENamedElement()
	 * @generated
	 * @ordered
	 */
	protected ENamedElement eNamedElement = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NamedElementViewImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass()
	{
		return ModelPackage.eINSTANCE.getNamedElementView();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ENamedElement getENamedElement()
	{
		if (eNamedElement != null && eNamedElement.eIsProxy())
		{
			ENamedElement oldENamedElement = eNamedElement;
			eNamedElement = (ENamedElement)eResolveProxy((InternalEObject)eNamedElement);
			if (eNamedElement != oldENamedElement)
			{
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.NAMED_ELEMENT_VIEW__ENAMED_ELEMENT, oldENamedElement, eNamedElement));
			}
		}
		return eNamedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ENamedElement basicGetENamedElement()
	{
		return eNamedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setENamedElement(ENamedElement newENamedElement)
	{
		ENamedElement oldENamedElement = eNamedElement;
		eNamedElement = newENamedElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.NAMED_ELEMENT_VIEW__ENAMED_ELEMENT, oldENamedElement, eNamedElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs)
	{
		if (featureID >= 0)
		{
			switch (eDerivedStructuralFeatureID(featureID, baseClass))
			{
				case ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM, msgs);
				case ModelPackage.NAMED_ELEMENT_VIEW__INCOMING_CONNECTIONS:
					return ((InternalEList)getIncomingConnections()).basicAdd(otherEnd, msgs);
				case ModelPackage.NAMED_ELEMENT_VIEW__OUTGOING_CONNECTIONS:
					return ((InternalEList)getOutgoingConnections()).basicAdd(otherEnd, msgs);
				default:
					return eDynamicInverseAdd(otherEnd, featureID, baseClass, msgs);
			}
		}
		if (eContainer != null)
			msgs = eBasicRemoveFromContainer(msgs);
		return eBasicSetContainer(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs)
	{
		if (featureID >= 0)
		{
			switch (eDerivedStructuralFeatureID(featureID, baseClass))
			{
				case ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM:
					return eBasicSetContainer(null, ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM, msgs);
				case ModelPackage.NAMED_ELEMENT_VIEW__INCOMING_CONNECTIONS:
					return ((InternalEList)getIncomingConnections()).basicRemove(otherEnd, msgs);
				case ModelPackage.NAMED_ELEMENT_VIEW__OUTGOING_CONNECTIONS:
					return ((InternalEList)getOutgoingConnections()).basicRemove(otherEnd, msgs);
				default:
					return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
			}
		}
		return eBasicSetContainer(null, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eBasicRemoveFromContainer(NotificationChain msgs)
	{
		if (eContainerFeatureID >= 0)
		{
			switch (eContainerFeatureID)
			{
				case ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM:
					return eContainer.eInverseRemove(this, ModelPackage.DIAGRAM__CONTENTS, Diagram.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return eContainer.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve)
	{
		switch (eDerivedStructuralFeatureID(eFeature))
		{
			case ModelPackage.NAMED_ELEMENT_VIEW__LOCATION:
				return getLocation();
			case ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM:
				return getDiagram();
			case ModelPackage.NAMED_ELEMENT_VIEW__INCOMING_CONNECTIONS:
				return getIncomingConnections();
			case ModelPackage.NAMED_ELEMENT_VIEW__OUTGOING_CONNECTIONS:
				return getOutgoingConnections();
			case ModelPackage.NAMED_ELEMENT_VIEW__WIDTH:
				return new Integer(getWidth());
			case ModelPackage.NAMED_ELEMENT_VIEW__ENAMED_ELEMENT:
				if (resolve) return getENamedElement();
				return basicGetENamedElement();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue)
	{
		switch (eDerivedStructuralFeatureID(eFeature))
		{
			case ModelPackage.NAMED_ELEMENT_VIEW__LOCATION:
				setLocation((Point)newValue);
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM:
				setDiagram((Diagram)newValue);
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__INCOMING_CONNECTIONS:
				getIncomingConnections().clear();
				getIncomingConnections().addAll((Collection)newValue);
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__OUTGOING_CONNECTIONS:
				getOutgoingConnections().clear();
				getOutgoingConnections().addAll((Collection)newValue);
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__WIDTH:
				setWidth(((Integer)newValue).intValue());
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__ENAMED_ELEMENT:
				setENamedElement((ENamedElement)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature)
	{
		switch (eDerivedStructuralFeatureID(eFeature))
		{
			case ModelPackage.NAMED_ELEMENT_VIEW__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM:
				setDiagram((Diagram)null);
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__INCOMING_CONNECTIONS:
				getIncomingConnections().clear();
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__OUTGOING_CONNECTIONS:
				getOutgoingConnections().clear();
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__WIDTH:
				setWidth(WIDTH_EDEFAULT);
				return;
			case ModelPackage.NAMED_ELEMENT_VIEW__ENAMED_ELEMENT:
				setENamedElement((ENamedElement)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature)
	{
		switch (eDerivedStructuralFeatureID(eFeature))
		{
			case ModelPackage.NAMED_ELEMENT_VIEW__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case ModelPackage.NAMED_ELEMENT_VIEW__DIAGRAM:
				return getDiagram() != null;
			case ModelPackage.NAMED_ELEMENT_VIEW__INCOMING_CONNECTIONS:
				return incomingConnections != null && !incomingConnections.isEmpty();
			case ModelPackage.NAMED_ELEMENT_VIEW__OUTGOING_CONNECTIONS:
				return outgoingConnections != null && !outgoingConnections.isEmpty();
			case ModelPackage.NAMED_ELEMENT_VIEW__WIDTH:
				return width != WIDTH_EDEFAULT;
			case ModelPackage.NAMED_ELEMENT_VIEW__ENAMED_ELEMENT:
				return eNamedElement != null;
		}
		return eDynamicIsSet(eFeature);
	}

} //NamedElementViewImpl
