/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class EDiagramImages
{

public static final String PACKAGE = "package_obj.gif"; //$NON-NLS-1$
public static final String CLASS = "class_obj.gif"; //$NON-NLS-1$
public static final String INTERFACE = "int_obj.gif"; //$NON-NLS-1$
public static final String REFERENCE = "EReference.gif"; //$NON-NLS-1$
public static final String INHERITANCE = "hierarchy_co.gif"; //$NON-NLS-1$
public static final String ATTRIBUTE = "EAttribute.gif"; //$NON-NLS-1$
public static final String OPERATION = "EOperation.gif"; //$NON-NLS-1$
public static final String FIELD_PRIVATE = "field_private_obj.gif"; //$NON-NLS-1$
public static final String FIELD_PROTECTED = "field_protected_obj.gif"; //$NON-NLS-1$
public static final String FIELD_PUBLIC = "field_public_obj.gif"; //$NON-NLS-1$
public static final String FIELD_DEFAULT = "field_default_obj.gif"; //$NON-NLS-1$
public static final String METHOD_PRIVATE = "methpri_obj.gif"; //$NON-NLS-1$
public static final String METHOD_PROTECTED = "methpro_obj.gif"; //$NON-NLS-1$
public static final String METHOD_PUBLIC = "methpub_obj.gif"; //$NON-NLS-1$
public static final String METHOD_DEFAULT = "methdef_obj.gif"; //$NON-NLS-1$
public static final String STICKY_NOTE = "sticky_note.gif"; //$NON-NLS-1$
public static final String CONNECTION = "connection16.gif"; //$NON-NLS-1$
public static final String EXPAND_ALL = "expandall.gif"; //$NON-NLS-1$
public static final String TWO_WAY_REFERENCE = "2EReferences.gif"; //$NON-NLS-1$
public static final String DATATYPE = "EDataType.gif"; //$NON-NLS-1$
public static final String ENUM = "EEnum.gif"; //$NON-NLS-1$
public static final String ENUM_LITERAL = "EEnumLiteral.gif"; //$NON-NLS-1$

private static final Map stringToImage = new HashMap();

private EDiagramImages() {}

/**
 * Clients should not dispose the Image returned.
 * 
 * @param key  the key (one of the constants defined in this class)
 * @return the Image associated with the given key
 */
public static Image getImage(String key) {
	Image img = (Image)stringToImage.get(key);
	if (img == null) {
		img = getImageDescriptor(key).createImage();
		stringToImage.put(key, img);
	}
	return img;
}

public static ImageDescriptor getImageDescriptor(String key) {
	return ImageDescriptor.createFromFile(EDiagramImages.class, key);
}

}