/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * </copyright>
 * 
 * $Id: EStringToStringMapEntryItemProvider.java,v 1.4 2005/03/30 21:27:11 pshah Exp $
 * /
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.model.ecore.provider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.gef.examples.ediagram.EDiagramPlugin;

/**
 * This is the item provider adpater for a {@link java.util.Map.Entry}object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class EStringToStringMapEntryItemProvider extends ItemProviderAdapter
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
	public EStringToStringMapEntryItemProvider(AdapterFactory adapterFactory) {
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

			addKeyPropertyDescriptor(object);
			addValuePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Key feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected void addKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_EStringToStringMapEntry_key_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EStringToStringMapEntry_key_feature",
						"_UI_EStringToStringMapEntry_type"), EcorePackage.eINSTANCE
						.getEStringToStringMapEntry_Key(), false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE));
	}

	/**
	 * This adds a property descriptor for the Value feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_EStringToStringMapEntry_value_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EStringToStringMapEntry_value_feature",
						"_UI_EStringToStringMapEntry_type"), EcorePackage.eINSTANCE
						.getEStringToStringMapEntry_Value(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE));
	}

	/**
	 * This returns EStringToStringMapEntry.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public Object getImage(Object object) {
		return getResourceLocator().getImage("full/obj16/EStringToStringMapEntry");
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText(Object object) {
		Map.Entry eStringToStringMapEntry = (Map.Entry) object;
		return "" + eStringToStringMapEntry.getKey() + " -> "
				+ eStringToStringMapEntry.getValue();
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

		switch (notification.getFeatureID(Map.Entry.class)) {
			case EcorePackage.ESTRING_TO_STRING_MAP_ENTRY__KEY :
			case EcorePackage.ESTRING_TO_STRING_MAP_ENTRY__VALUE :
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
