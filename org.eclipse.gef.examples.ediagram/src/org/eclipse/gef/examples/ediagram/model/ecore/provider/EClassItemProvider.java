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
 * $Id: EClassItemProvider.java,v 1.3 2005/03/29 23:58:13 pshah Exp $
 * /
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.ecore.provider;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
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
 * {@link org.eclipse.gef.examples.ediagram.model.ecore.EClass}object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class EClassItemProvider extends EClassifierItemProvider
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
	public EClassItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addAbstractPropertyDescriptor(object);
			addInterfacePropertyDescriptor(object);
//			addESuperTypesPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Abstract feature. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addAbstractPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_abstract_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_abstract_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_Abstract(), true,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Interface feature. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addInterfacePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_interface_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_interface_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_Interface(), true,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the ESuper Types feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addESuperTypesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eSuperTypes_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eSuperTypes_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_ESuperTypes(), true));
	}

	/**
	 * This adds a property descriptor for the EAll Attributes feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEAllAttributesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eAllAttributes_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eAllAttributes_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EAllAttributes(), false));
	}

	/**
	 * This adds a property descriptor for the EAll References feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEAllReferencesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eAllReferences_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eAllReferences_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EAllReferences(), false));
	}

	/**
	 * This adds a property descriptor for the EReferences feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEReferencesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eReferences_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eReferences_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EReferences(), false));
	}

	/**
	 * This adds a property descriptor for the EAttributes feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEAttributesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eAttributes_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eAttributes_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EAttributes(), false));
	}

	/**
	 * This adds a property descriptor for the EAll Containments feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEAllContainmentsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eAllContainments_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eAllContainments_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EAllContainments(), false));
	}

	/**
	 * This adds a property descriptor for the EAll Operations feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEAllOperationsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eAllOperations_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eAllOperations_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EAllOperations(), false));
	}

	/**
	 * This adds a property descriptor for the EAll Structural Features feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEAllStructuralFeaturesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_EClass_eAllStructuralFeatures_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EClass_eAllStructuralFeatures_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EAllStructuralFeatures(), false));
	}

	/**
	 * This adds a property descriptor for the EAll Super Types feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEAllSuperTypesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eAllSuperTypes_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eAllSuperTypes_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EAllSuperTypes(), false));
	}

	/**
	 * This adds a property descriptor for the EID Attribute feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEIDAttributePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EClass_eIDAttribute_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EClass_eIDAttribute_feature", "_UI_EClass_type"),
				EcorePackage.eINSTANCE.getEClass_EIDAttribute(), false));
	}

	/**
	 * This specifies how to implement {@link #getChildren}and is used to deduce an
	 * appropriate feature for an {@link org.eclipse.emf.edit.command.AddCommand},
	 * {@link org.eclipse.emf.edit.command.RemoveCommand}or
	 * {@link org.eclipse.emf.edit.command.MoveCommand}in {@link #createCommand}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(EcorePackage.eINSTANCE.getEClass_EOperations());
			childrenFeatures.add(EcorePackage.eINSTANCE.getEClass_EStructuralFeatures());
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to
		// use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns EClass.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getImage(Object object) {
		return getResourceLocator().getImage("full/obj16/EClass");
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getText(Object object) {
		EClass eClass = (EClass) object;
		StringBuffer result = new StringBuffer();
		if (eClass.getName() != null) {
			result.append(eClass.getName());
		}
		if (!eClass.getESuperTypes().isEmpty()) {
			result.append(" -> ");
			for (Iterator i = eClass.getESuperTypes().iterator(); i.hasNext();) {
				EClass eSuperType = (EClass) i.next();
				result.append(eSuperType.getName());
				if (i.hasNext()) {
					result.append(", ");
				}
			}
		}
		if (eClass.getInstanceClassName() != null) {
			result.append(" <");
			result.append(eClass.getInstanceClassName());
			result.append(">");
		}

		return result.toString();
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

		switch (notification.getFeatureID(EClass.class)) {
			case EcorePackage.ECLASS__ABSTRACT :
			case EcorePackage.ECLASS__INTERFACE :
			case EcorePackage.ECLASS__ESUPER_TYPES :
			case EcorePackage.ECLASS__EREFERENCES :
			case EcorePackage.ECLASS__EATTRIBUTES :
				fireNotifyChanged(new ViewerNotification(notification, notification
						.getNotifier(), false, true));
				return;
			case EcorePackage.ECLASS__EOPERATIONS :
			case EcorePackage.ECLASS__ESTRUCTURAL_FEATURES :
				fireNotifyChanged(new ViewerNotification(notification, notification
						.getNotifier(), true, false));
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

		newChildDescriptors.add(createChildParameter(EcorePackage.eINSTANCE
				.getEClass_EOperations(), EcoreFactory.eINSTANCE.createEOperation()));

		newChildDescriptors.add(createChildParameter(EcorePackage.eINSTANCE
				.getEClass_EStructuralFeatures(), EcoreFactory.eINSTANCE
				.createEAttribute()));

		newChildDescriptors.add(createChildParameter(EcorePackage.eINSTANCE
				.getEClass_EStructuralFeatures(), EcoreFactory.eINSTANCE
				.createEReference()));
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
