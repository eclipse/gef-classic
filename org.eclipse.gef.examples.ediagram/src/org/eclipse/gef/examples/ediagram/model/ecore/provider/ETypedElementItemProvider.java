/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * </copyright>
 * 
 * $Id: ETypedElementItemProvider.java,v 1.3 2005/03/29 23:58:13 pshah Exp $
 * /
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.ecore.provider;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.gef.examples.ediagram.EDiagramPlugin;

/**
 * This is the item provider adpater for a
 * {@link org.eclipse.gef.examples.ediagram.model.ecore.ETypedElement}object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ETypedElementItemProvider extends ENamedElementItemProvider
		implements
			IEditingDomainItemProvider,
			IStructuredItemContentProvider,
			ITreeItemContentProvider,
			IItemLabelProvider,
			IItemPropertySource
{
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ETypedElementItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addOrderedPropertyDescriptor(object);
			addUniquePropertyDescriptor(object);
			addLowerBoundPropertyDescriptor(object);
			addUpperBoundPropertyDescriptor(object);
			addManyPropertyDescriptor(object);
			addRequiredPropertyDescriptor(object);
			addETypePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Ordered feature. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addOrderedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_ETypedElement_ordered_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_ETypedElement_ordered_feature", "_UI_ETypedElement_type"),
				EcorePackage.eINSTANCE.getETypedElement_Ordered(), true,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Unique feature. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addUniquePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_ETypedElement_unique_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_ETypedElement_unique_feature", "_UI_ETypedElement_type"),
				EcorePackage.eINSTANCE.getETypedElement_Unique(), true,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Lower Bound feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLowerBoundPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(new ItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_ETypedElement_lowerBound_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_ETypedElement_lowerBound_feature",
								"_UI_ETypedElement_type"), EcorePackage.eINSTANCE
								.getETypedElement_LowerBound(), true,
						ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Upper Bound feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addUpperBoundPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(new ItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_ETypedElement_upperBound_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_ETypedElement_upperBound_feature",
								"_UI_ETypedElement_type"), EcorePackage.eINSTANCE
								.getETypedElement_UpperBound(), true,
						ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Many feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addManyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_ETypedElement_many_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_ETypedElement_many_feature", "_UI_ETypedElement_type"),
				EcorePackage.eINSTANCE.getETypedElement_Many(), false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Required feature. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addRequiredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_ETypedElement_required_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_ETypedElement_required_feature", "_UI_ETypedElement_type"),
				EcorePackage.eINSTANCE.getETypedElement_Required(), false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the EType feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addETypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getString("_UI_ETypedElement_eType_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ETypedElement_eType_feature", "_UI_ETypedElement_type"),
				EcorePackage.eINSTANCE.getETypedElement_EType(), true) {
			public Collection getChoiceOfValues(Object object) {
				// Filter out types that aren't permitted.
				//
				Collection result = super.getChoiceOfValues(object);

				for (Iterator i = EcorePackage.eINSTANCE.getEClassifiers().iterator(); i
						.hasNext();) {
					Object classifier = i.next();
					if (!result.contains(classifier)) {
						result.add(classifier);
					}
				}

				if (object instanceof EAttribute) {
					for (Iterator i = result.iterator(); i.hasNext();) {
						if (i.next() instanceof EClass) {
							i.remove();
						}
					}
				} else if (object instanceof EReference) {
					for (Iterator i = result.iterator(); i.hasNext();) {
						if (i.next() instanceof EDataType) {
							i.remove();
						}
					}
				}

				return result;
			}
		});
	}

	/**
	 * This returns ETypedElement.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getImage(Object object) {
		return getResourceLocator().getImage("full/obj16/ETypedElement");
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText(Object object) {
		String label = ((ETypedElement) object).getName();
		return label == null || label.length() == 0
				? getString("_UI_ETypedElement_type")
				: getString("_UI_ETypedElement_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren}to update any
	 * cached children and by creating a viewer notification, which it passes to
	 * {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ETypedElement.class)) {
			case EcorePackage.ETYPED_ELEMENT__ORDERED :
			case EcorePackage.ETYPED_ELEMENT__UNIQUE :
			case EcorePackage.ETYPED_ELEMENT__LOWER_BOUND :
			case EcorePackage.ETYPED_ELEMENT__UPPER_BOUND :
			case EcorePackage.ETYPED_ELEMENT__MANY :
			case EcorePackage.ETYPED_ELEMENT__REQUIRED :
			case EcorePackage.ETYPED_ELEMENT__ETYPE :
				fireNotifyChanged(new ViewerNotification(notification, notification
						.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of
	 * {@link org.eclipse.emf.edit.command.CommandParameter}s describing all of the
	 * children that can be created under this object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void collectNewChildDescriptors(Collection newChildDescriptors,
			Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return EDiagramPlugin.INSTANCE;
	}

}
