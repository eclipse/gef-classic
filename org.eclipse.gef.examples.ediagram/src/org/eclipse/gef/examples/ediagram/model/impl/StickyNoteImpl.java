/**
 * <copyright>
 * </copyright>
 *
 * $Id: StickyNoteImpl.java,v 1.1 2004/11/11 06:03:50 pshah Exp $
 */
package org.eclipse.gef.examples.ediagram.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.StickyNote;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sticky Note</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.gef.examples.ediagram.model.impl.StickyNoteImpl#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StickyNoteImpl extends NodeImpl implements StickyNote
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = ""; //$NON-NLS-1$

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StickyNoteImpl()
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
		return ModelPackage.eINSTANCE.getStickyNote();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setText(String newText)
	{
		String oldText = text;
		text = newText;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.STICKY_NOTE__TEXT, oldText, text));
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
				case ModelPackage.STICKY_NOTE__DIAGRAM:
					if (eContainer != null)
						msgs = eBasicRemoveFromContainer(msgs);
					return eBasicSetContainer(otherEnd, ModelPackage.STICKY_NOTE__DIAGRAM, msgs);
				case ModelPackage.STICKY_NOTE__INCOMING_CONNECTIONS:
					return ((InternalEList)getIncomingConnections()).basicAdd(otherEnd, msgs);
				case ModelPackage.STICKY_NOTE__OUTGOING_CONNECTIONS:
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
				case ModelPackage.STICKY_NOTE__DIAGRAM:
					return eBasicSetContainer(null, ModelPackage.STICKY_NOTE__DIAGRAM, msgs);
				case ModelPackage.STICKY_NOTE__INCOMING_CONNECTIONS:
					return ((InternalEList)getIncomingConnections()).basicRemove(otherEnd, msgs);
				case ModelPackage.STICKY_NOTE__OUTGOING_CONNECTIONS:
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
				case ModelPackage.STICKY_NOTE__DIAGRAM:
					return ((InternalEObject)eContainer).eInverseRemove(this, ModelPackage.DIAGRAM__CONTENTS, Diagram.class, msgs);
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
			case ModelPackage.STICKY_NOTE__LOCATION:
				return getLocation();
			case ModelPackage.STICKY_NOTE__DIAGRAM:
				return getDiagram();
			case ModelPackage.STICKY_NOTE__INCOMING_CONNECTIONS:
				return getIncomingConnections();
			case ModelPackage.STICKY_NOTE__OUTGOING_CONNECTIONS:
				return getOutgoingConnections();
			case ModelPackage.STICKY_NOTE__WIDTH:
				return new Integer(getWidth());
			case ModelPackage.STICKY_NOTE__TEXT:
				return getText();
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
			case ModelPackage.STICKY_NOTE__LOCATION:
				setLocation((Point)newValue);
				return;
			case ModelPackage.STICKY_NOTE__DIAGRAM:
				setDiagram((Diagram)newValue);
				return;
			case ModelPackage.STICKY_NOTE__INCOMING_CONNECTIONS:
				getIncomingConnections().clear();
				getIncomingConnections().addAll((Collection)newValue);
				return;
			case ModelPackage.STICKY_NOTE__OUTGOING_CONNECTIONS:
				getOutgoingConnections().clear();
				getOutgoingConnections().addAll((Collection)newValue);
				return;
			case ModelPackage.STICKY_NOTE__WIDTH:
				setWidth(((Integer)newValue).intValue());
				return;
			case ModelPackage.STICKY_NOTE__TEXT:
				setText((String)newValue);
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
			case ModelPackage.STICKY_NOTE__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case ModelPackage.STICKY_NOTE__DIAGRAM:
				setDiagram((Diagram)null);
				return;
			case ModelPackage.STICKY_NOTE__INCOMING_CONNECTIONS:
				getIncomingConnections().clear();
				return;
			case ModelPackage.STICKY_NOTE__OUTGOING_CONNECTIONS:
				getOutgoingConnections().clear();
				return;
			case ModelPackage.STICKY_NOTE__WIDTH:
				setWidth(WIDTH_EDEFAULT);
				return;
			case ModelPackage.STICKY_NOTE__TEXT:
				setText(TEXT_EDEFAULT);
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
			case ModelPackage.STICKY_NOTE__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case ModelPackage.STICKY_NOTE__DIAGRAM:
				return getDiagram() != null;
			case ModelPackage.STICKY_NOTE__INCOMING_CONNECTIONS:
				return incomingConnections != null && !incomingConnections.isEmpty();
			case ModelPackage.STICKY_NOTE__OUTGOING_CONNECTIONS:
				return outgoingConnections != null && !outgoingConnections.isEmpty();
			case ModelPackage.STICKY_NOTE__WIDTH:
				return width != WIDTH_EDEFAULT;
			case ModelPackage.STICKY_NOTE__TEXT:
				return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
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
		result.append(" (text: "); //$NON-NLS-1$
		result.append(text);
		result.append(')');
		return result.toString();
	}

} //StickyNoteImpl
