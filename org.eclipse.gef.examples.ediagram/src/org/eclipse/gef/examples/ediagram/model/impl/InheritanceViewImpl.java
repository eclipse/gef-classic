/**
 * <copyright>
 * </copyright>
 *
 * $Id: InheritanceViewImpl.java,v 1.1 2004/11/11 06:03:50 pshah Exp $
 */
package org.eclipse.gef.examples.ediagram.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.Node;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inheritance View</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class InheritanceViewImpl extends LinkImpl implements InheritanceView
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = ""; //$NON-NLS-1$

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InheritanceViewImpl()
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
		return ModelPackage.eINSTANCE.getInheritanceView();
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
				case ModelPackage.INHERITANCE_VIEW__SOURCE:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, ModelPackage.INHERITANCE_VIEW__SOURCE, msgs);
				case ModelPackage.INHERITANCE_VIEW__TARGET:
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
				case ModelPackage.INHERITANCE_VIEW__SOURCE:
					return eBasicSetContainer(null, ModelPackage.INHERITANCE_VIEW__SOURCE, msgs);
				case ModelPackage.INHERITANCE_VIEW__TARGET:
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
				case ModelPackage.INHERITANCE_VIEW__SOURCE:
					return ((InternalEObject)eContainer).eInverseRemove(this, ModelPackage.NODE__OUTGOING_CONNECTIONS, Node.class, msgs);
				default:
					return eDynamicBasicRemoveFromContainer(msgs);
			}
		}
		return ((InternalEObject)eContainer).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - eContainerFeatureID, null, msgs);
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
			case ModelPackage.INHERITANCE_VIEW__SOURCE:
				return getSource();
			case ModelPackage.INHERITANCE_VIEW__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case ModelPackage.INHERITANCE_VIEW__BENDPOINTS:
				return getBendpoints();
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
			case ModelPackage.INHERITANCE_VIEW__SOURCE:
				setSource((Node)newValue);
				return;
			case ModelPackage.INHERITANCE_VIEW__TARGET:
				setTarget((Node)newValue);
				return;
			case ModelPackage.INHERITANCE_VIEW__BENDPOINTS:
				getBendpoints().clear();
				getBendpoints().addAll((Collection)newValue);
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
			case ModelPackage.INHERITANCE_VIEW__SOURCE:
				setSource((Node)null);
				return;
			case ModelPackage.INHERITANCE_VIEW__TARGET:
				setTarget((Node)null);
				return;
			case ModelPackage.INHERITANCE_VIEW__BENDPOINTS:
				getBendpoints().clear();
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
			case ModelPackage.INHERITANCE_VIEW__SOURCE:
				return getSource() != null;
			case ModelPackage.INHERITANCE_VIEW__TARGET:
				return target != null;
			case ModelPackage.INHERITANCE_VIEW__BENDPOINTS:
				return bendpoints != null && !bendpoints.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //InheritanceViewImpl
