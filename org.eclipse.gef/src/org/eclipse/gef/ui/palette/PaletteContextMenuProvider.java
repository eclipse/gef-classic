package org.eclipse.gef.ui.palette;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Provides the context menu for a palette
 * 
 * @author Pratik Shah
 */
public class PaletteContextMenuProvider
	extends ContextMenuProvider
{

/**
 * Constructor
 * 
 * @param palette the palette viewer for which the context menu has to be created
 * @param registry the action registry
 */
public PaletteContextMenuProvider(PaletteViewerImpl palette, ActionRegistry registry) {
	super(palette, registry);
}

/**
 * Returns the palette viewer.
 * @return the palette viewer
 */
protected PaletteViewerImpl getPaletteViewer() {
	return (PaletteViewerImpl)getViewer();
}

/**
 * This is the actual method that builds the context menu.
 * 
 * @param menu The IMenuManager to which actions for the palette's context menu can be
 * 				added
 * @see org.eclipse.gef.ui.parts.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
 */
public void buildContextMenu(IMenuManager menu) {
	Object selectedPart = getPaletteViewer().getSelectedEditParts().get(0);
	if (selectedPart instanceof TemplateEditPart) {
		menu.add(getActionRegistry().getAction(IWorkbenchActionConstants.COPY));
	}
	if (getPaletteViewer().getCustomizer() != null)
		menu.add(new LayoutAction(getPaletteViewer().getPaletteViewerPreferencesSource()));
		menu.add(new IconSizeChangeAction(getPaletteViewer().getPaletteViewerPreferencesSource()));
		menu.add(new Separator());
		menu.add(new CustomizeAction(getPaletteViewer()));
}

}
