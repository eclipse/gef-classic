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
 * $Id: ReferenceViewItemProvider.java,v 1.2 2004/12/07 19:07:09 pshah Exp $
 */
package org.eclipse.gef.examples.ediagram.model.provider;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptorDecorator;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.gef.examples.ediagram.EDiagramPlugin;
import org.eclipse.gef.examples.ediagram.model.ModelPackage;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;

/**
 * This is the item provider adpater for a {@link org.eclipse.gef.examples.ediagram.model.ReferenceView} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ReferenceViewItemProvider
	extends LinkItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = ""; //$NON-NLS-1$

	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceViewItemProvider(AdapterFactory adapterFactory)
	{
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public List getPropertyDescriptors(Object object)
	{
		if (itemPropertyDescriptors == null)
		{
			super.getPropertyDescriptors(object);

			addEReferencePropertyDescriptor(object);
			addEOppositePropertyDescriptor(object);
			addOppositeShownPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the EReference feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addEReferencePropertyDescriptor(Object object)
	{
		EReference ref = ((ReferenceView)object).getEReference();
		ItemProviderAdapter provider = (ItemProviderAdapter)
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory()
				.adapt(ref, IItemPropertySource.class);
		List descriptors = provider.getPropertyDescriptors(ref);
		for (Iterator iter = descriptors.iterator(); iter.hasNext();) {
			ItemPropertyDescriptor descriptor = (ItemPropertyDescriptor) iter.next();
			itemPropertyDescriptors.add(
					new ItemPropertyDescriptorDecorator(ref, descriptor) {
						public String getCategory(Object thisObject) {
							return "EReference";
						}
					});
		}
	}
	
	protected void addEOppositePropertyDescriptor(Object object) {
		ReferenceView refView = (ReferenceView)object;
		EReference ref = refView.getEReference().getEOpposite();
		if (ref != null && refView.isOppositeShown()) {
			ItemProviderAdapter provider = (ItemProviderAdapter)
					((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory()
					.adapt(ref, IItemPropertySource.class);
			List descriptors = provider.getPropertyDescriptors(ref);
			for (Iterator iter = descriptors.iterator(); iter.hasNext();) {
				ItemPropertyDescriptor descriptor = (ItemPropertyDescriptor) iter.next();
				itemPropertyDescriptors.add(
						new ItemPropertyDescriptorDecorator(ref, descriptor) {
							public String getCategory(Object thisObject) {
								return "EOpposite";
							}
							public String getId(Object thisObject) {
								return super.getId(thisObject) + this;
							}
						});
			}
		}		
	}

	/**
	 * This adds a property descriptor for the Opposite Shown feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOppositeShownPropertyDescriptor(Object object)
	{
		itemPropertyDescriptors.add
			(new ItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ReferenceView_oppositeShown_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_ReferenceView_oppositeShown_feature", "_UI_ReferenceView_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 ModelPackage.eINSTANCE.getReferenceView_OppositeShown(),
				 true,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE));
	}

	/**
	 * This returns ReferenceView.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getImage(Object object)
	{
		return getResourceLocator().getImage("full/obj16/ReferenceView"); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getText(Object object)
	{
		// Copied from EReferenceItemProvider
	    EReference eReference = ((ReferenceView)object).getEReference();
	    StringBuffer result = new StringBuffer();
	    result.append(eReference.getName());
	    if (eReference.getEType() != null)
	    {
	      result.append(" : ");
	      result.append(eReference.getEType().getName());
	    }
	    return result.toString();
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void notifyChanged(Notification notification)
	{
		updateChildren(notification);

		switch (notification.getFeatureID(ReferenceView.class))
		{
			case ModelPackage.REFERENCE_VIEW__EREFERENCE:
			case ModelPackage.REFERENCE_VIEW__OPPOSITE_SHOWN:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing all of the children that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object)
	{
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceLocator getResourceLocator()
	{
		return EDiagramPlugin.INSTANCE;
	}

}
