package org.eclipse.gef.palette;

import org.eclipse.swt.graphics.Image;

/**
 * @see org.eclipse.gef.palette.TemplateEntry
 * 
 * @author Eric Bordeau
 */
public class DefaultTemplateEntry 
	extends DefaultPaletteEntry 
	implements TemplateEntry 
{

private Object template;

public DefaultTemplateEntry(Object template, String label) {
	super(label);
	setTemplate(template);
}

public DefaultTemplateEntry(Object template, String label, String shortDesc) {
	super(label, shortDesc);
	setTemplate(template);
}

public DefaultTemplateEntry(Object template, String label, String shortDesc, Image iconSmall, Image iconLarge) {
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
