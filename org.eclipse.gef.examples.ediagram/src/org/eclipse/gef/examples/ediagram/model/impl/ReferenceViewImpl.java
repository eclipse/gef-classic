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
 * $Id: ReferenceViewImpl.java,v 1.3 2005/01/17 22:29:09 pshah Exp $
 */
package org.eclipse.gef.examples.ediagram.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference View</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gef.examples.ediagram.model.impl.ReferenceViewImpl#getEReference <em>EReference</em>}</li>
 *   <li>{@link org.eclipse.gef.examples.ediagram.model.impl.ReferenceViewImpl#isOppositeShown <em>Opposite Shown</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceViewImpl extends LinkImpl implements ReferenceView
{
	/**
	 * The cached value of the '{@link #getEReference() <em>EReference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEReference()
	 * @generated
	 * @ordered
	 */
	protected EReference eReference = null;

	/**
	 * The default value of the '{@link #isOppositeShown() <em>Opposite Shown</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOppositeShown()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPPOSITE_SHOWN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOppositeShown() <em>Opposite Shown</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOppositeShown()
	 * @generated
	 * @ordered
	 */
	protected boolean oppositeShown = OPPOSITE_SHOWN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReferenceViewImpl()
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
		return ModelPackage.eINSTANCE.getReferenceView();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEReference()
	{
		if (eReference != null && eReference.eIsProxy())
		{
			EReference oldEReference = eReference;
			eReference = (EReference)eResolveProxy((InternalEObject)eReference);
			if (eReference != oldEReference)
			{
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.REFERENCE_VIEW__EREFERENCE, oldEReference, eReference));
			}
		}
		return eReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference basicGetEReference()
	{
		return eReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEReference(EReference newEReference)
	{
		EReference oldEReference = eReference;
		eReference = newEReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REFERENCE_VIEW__EREFERENCE, oldEReference, eReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOppositeShown()
	{
		return oppositeShown;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOppositeShown(boolean newOppositeShown)
	{
		boolean oldOppositeShown = oppositeShown;
		oppositeShown = newOppositeShown;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REFERENCE_VIEW__OPPOSITE_SHOWN, oldOppositeShown, oppositeShown));
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
				case ModelPackage.REFERENCE_VIEW__SOURCE:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, ModelPackage.REFERENCE_VIEW__SOURCE, msgs);
				case ModelPackage.REFERENCE_VIEW__TARGET:
					if (target != null)
						msgs = ((InternalEObject)target).eInverseRemove(this, ModelPackage.NODE__INCOMING_CONNECTIONS, Node.class, msgs);
					return basicSetTarget((Node)otherEnd, msgs);
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
				case ModelPackage.REFERENCE_VIEW__SOURCE:
					return eBasicSetContainer(null, ModelPackage.REFERENCE_VIEW__SOURCE, msgs);
				case ModelPackage.REFERENCE_VIEW__TARGET:
					return basicSetTarget(null, msgs);
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
				case ModelPackage.REFERENCE_VIEW__SOURCE:
					return eContainer.eInverseRemove(this, ModelPackage.NODE__OUTGOING_CONNECTIONS, Node.class, msgs);
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
			case ModelPackage.REFERENCE_VIEW__SOURCE:
				return getSource();
			case ModelPackage.REFERENCE_VIEW__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case ModelPackage.REFERENCE_VIEW__BENDPOINTS:
				return getBendpoints();
			case ModelPackage.REFERENCE_VIEW__EREFERENCE:
				if (resolve) return getEReference();
				return basicGetEReference();
			case ModelPackage.REFERENCE_VIEW__OPPOSITE_SHOWN:
				return isOppositeShown() ? Boolean.TRUE : Boolean.FALSE;
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
			case ModelPackage.REFERENCE_VIEW__SOURCE:
				setSource((Node)newValue);
				return;
			case ModelPackage.REFERENCE_VIEW__TARGET:
				setTarget((Node)newValue);
				return;
			case ModelPackage.REFERENCE_VIEW__BENDPOINTS:
				getBendpoints().clear();
				getBendpoints().addAll((Collection)newValue);
				return;
			case ModelPackage.REFERENCE_VIEW__EREFERENCE:
				setEReference((EReference)newValue);
				return;
			case ModelPackage.REFERENCE_VIEW__OPPOSITE_SHOWN:
				setOppositeShown(((Boolean)newValue).booleanValue());
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
			case ModelPackage.REFERENCE_VIEW__SOURCE:
				setSource((Node)null);
				return;
			case ModelPackage.REFERENCE_VIEW__TARGET:
				setTarget((Node)null);
				return;
			case ModelPackage.REFERENCE_VIEW__BENDPOINTS:
				getBendpoints().clear();
				return;
			case ModelPackage.REFERENCE_VIEW__EREFERENCE:
				setEReference((EReference)null);
				return;
			case ModelPackage.REFERENCE_VIEW__OPPOSITE_SHOWN:
				setOppositeShown(OPPOSITE_SHOWN_EDEFAULT);
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
			case ModelPackage.REFERENCE_VIEW__SOURCE:
				return getSource() != null;
			case ModelPackage.REFERENCE_VIEW__TARGET:
				return target != null;
			case ModelPackage.REFERENCE_VIEW__BENDPOINTS:
				return bendpoints != null && !bendpoints.isEmpty();
			case ModelPackage.REFERENCE_VIEW__EREFERENCE:
				return eReference != null;
			case ModelPackage.REFERENCE_VIEW__OPPOSITE_SHOWN:
				return oppositeShown != OPPOSITE_SHOWN_EDEFAULT;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString()
	{
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (oppositeShown: "); //$NON-NLS-1$
		result.append(oppositeShown);
		result.append(')');
		return result.toString();
	}

} //ReferenceViewImpl
