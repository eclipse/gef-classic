/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.palette;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @see org.eclipse.gef.palette.PaletteTemplateEntry
 * 
 * @author Eric Bordeau
 */
public class PaletteTemplateEntry 
	extends PaletteEntry
{

private Object template;
public static final String PALETTE_TYPE_TEMPLATE = "$Palette Template";  //$NON-NLS-1$

public PaletteTemplateEntry(
  String label,
  String shortDesc,
  Object template,
  ImageDescriptor iconSmall,
  ImageDescriptor iconLarge) {
	super(label, shortDesc, iconSmall, iconLarge, PALETTE_TYPE_TEMPLATE);
	setTemplate(template);
}

public Object getTemplate() {
	return template;
}

public void setTemplate(Object template) {
	this.template = template;
}

}
