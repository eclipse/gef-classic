package org.eclipse.gef.palette;

/**
 * A PaletteEntry that uses a template object for drag and drop operations.  
 * 
 * @author Eric Bordeau
 */
public interface TemplateEntry extends PaletteEntry {

Object getTemplate();

}
