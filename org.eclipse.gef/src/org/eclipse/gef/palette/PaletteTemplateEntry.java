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
public static final String PALETTE_TYPE_TEMPLATE = "$Palette Template";

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
