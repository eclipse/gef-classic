package org.eclipse.gef.palette;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.requests.CreationFactory;

/**
 * A combination of a {@link PaletteTemplateEntry} and {@link ToolEntry}.  The entry will
 * be rendered as a ToolEntry, but it will also be possible to use the entry as a
 * DragSource in the same way as a template.
 * @author hudsonr
 */
public class CombinedTemplateCreationEntry extends CreationToolEntry {

private Object template;

/**
 * Constructs a combined CreationTool and Template Entry with the given parameters.
 * @param label the label
 * @param shortDesc the descriptoin
 * @param factory the CreationFactory
 * @param iconSmall the small icon
 * @param iconLarge the large icon
 */
public CombinedTemplateCreationEntry(
  String label,
  String shortDesc,
  Object template,
  CreationFactory factory,
  ImageDescriptor iconSmall,
  ImageDescriptor iconLarge) {
	super(label, shortDesc, factory, iconSmall, iconLarge);
	setTemplate(template);
}

/**
 * Returns the template object.
 * @return Object the template
 */
public Object getTemplate() {
	return template;
}

/**
 * Sets the template.
 * @param template The template
 */
public void setTemplate(Object template) {
	this.template = template;
}

}
