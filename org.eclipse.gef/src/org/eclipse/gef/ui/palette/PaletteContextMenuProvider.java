package org.eclipse.gef.ui.palette;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.parts.ContextMenuProvider;
import org.eclipse.jface.action.IMenuManager;

/**
 * Provides the context menu for a palette
 * 
 * @author Pratik Shah
 */
public class PaletteContextMenuProvider
	implements ContextMenuProvider
{

private PaletteViewerImpl paletteViewer;

/**
 * Constructor
 * 
 * @param palette The palette for which the context menu has to be created
 */
public PaletteContextMenuProvider(PaletteViewerImpl palette) {
	this.paletteViewer = palette;
}

/**
 * This is the actual method that builds the context menu.
 * 
 * @param	menu	The IMenuManager to which actions for the palette's context
 * 					menu can be added
 * @param	viewer	This parameter is ignored
 * 
 * @see org.eclipse.gef.ui.parts.ContextMenuProvider#buildContextMenu(IMenuManager, EditPartViewer)
 */
public void buildContextMenu(IMenuManager menu, EditPartViewer viewer) {
	if (paletteViewer.getCustomizer() != null)
		menu.add(new CustomizeAction(paletteViewer));
}

}
