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

public PaletteTemplateEntry(Object template, String label) {
	super(label);
	setTemplate(template);
}

public PaletteTemplateEntry(Object template, String label, String shortDesc) {
	super(label, shortDesc);
	setTemplate(template);
}

public PaletteTemplateEntry(Object template, String label, String shortDesc, ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
	super(label, shortDesc, iconSmall, iconLarge);
	setTemplate(template);
}

public Object getTemplate() {
	return template;
}

public void setTemplate(Object template) {
	this.template = template;
}

}
