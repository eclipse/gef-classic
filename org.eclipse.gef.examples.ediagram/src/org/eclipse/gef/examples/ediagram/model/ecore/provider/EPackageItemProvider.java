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
 * $Id: EPackageItemProvider.java,v 1.3 2005/03/29 23:58:13 pshah Exp $
 * /
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.ecore.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
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
 * {@link org.eclipse.gef.examples.ediagram.model.ecore.EPackage}object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class EPackageItemProvider extends ENamedElementItemProvider
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
	public EPackageItemProvider(AdapterFactory adapterFactory) {
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

			addESuperPackagePropertyDescriptor(object);
			addESubPackagesPropertyDescriptor(object);
			addEClassifiersPropertyDescriptor(object);
			addNsURIPropertyDescriptor(object);
			addNsPrefixPropertyDescriptor(object);
			addEFactoryInstancePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	protected void addEClassifiersPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), "EClassifiers", "EClassifiers in this EPackage",
				EcorePackage.eINSTANCE.getEPackage_EClassifiers(), true));
	}

	protected void addESubPackagesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), "ESubPackages", "Subpackages of this EPackage",
				EcorePackage.eINSTANCE.getEPackage_ESubpackages(), true));
	}

	protected void addESuperPackagePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), "ESuperPackage", "Superpackage of this EPackage",
				EcorePackage.eINSTANCE.getEPackage_ESuperPackage(), false));
	}

	/**
	 * This adds a property descriptor for the Ns URI feature. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNsURIPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EPackage_nsURI_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EPackage_nsURI_feature", "_UI_EPackage_type"),
				EcorePackage.eINSTANCE.getEPackage_NsURI(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Ns Prefix feature. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNsPrefixPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EPackage_nsPrefix_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EPackage_nsPrefix_feature", "_UI_EPackage_type"),
				EcorePackage.eINSTANCE.getEPackage_NsPrefix(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the EFactory Instance feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEFactoryInstancePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(), getString("_UI_EPackage_eFactoryInstance_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_EPackage_eFactoryInstance_feature", "_UI_EPackage_type"),
				EcorePackage.eINSTANCE.getEPackage_EFactoryInstance(), true));
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
			childrenFeatures.add(EcorePackage.eINSTANCE.getEPackage_EClassifiers());
			childrenFeatures.add(EcorePackage.eINSTANCE.getEPackage_ESubpackages());
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
	 * This returns EPackage.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getImage(Object object) {
		return getResourceLocator().getImage("full/obj16/EPackage");
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getText(Object object) {
		EPackage ePackage = (EPackage) object;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(ePackage.getName());
		return stringBuffer.toString();
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

		switch (notification.getFeatureID(EPackage.class)) {
			case EcorePackage.EPACKAGE__NS_URI :
			case EcorePackage.EPACKAGE__NS_PREFIX :
			case EcorePackage.EPACKAGE__EFACTORY_INSTANCE :
				fireNotifyChanged(new ViewerNotification(notification, notification
						.getNotifier(), false, true));
				return;
			case EcorePackage.EPACKAGE__ECLASSIFIERS :
			case EcorePackage.EPACKAGE__ESUBPACKAGES :
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
				.getEPackage_EClassifiers(), EcoreFactory.eINSTANCE.createEClass()));

		newChildDescriptors.add(createChildParameter(EcorePackage.eINSTANCE
				.getEPackage_EClassifiers(), EcoreFactory.eINSTANCE.createEDataType()));

		newChildDescriptors.add(createChildParameter(EcorePackage.eINSTANCE
				.getEPackage_EClassifiers(), EcoreFactory.eINSTANCE.createEEnum()));

		newChildDescriptors.add(createChildParameter(EcorePackage.eINSTANCE
				.getEPackage_ESubpackages(), EcoreFactory.eINSTANCE.createEPackage()));
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
