package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContextMenuProvider;
import org.eclipse.gef.ui.parts.GraphicalEditor;

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
 * @param palette The palette for which the context menu has to be created
 */
public PaletteContextMenuProvider(GraphicalEditor editor, PaletteViewerImpl palette) {
	super(editor, palette);
}

protected PaletteViewerImpl getPaletteViewer() {
	return (PaletteViewerImpl)getViewer();
}

/**
 * This is the actual method that builds the context menu.
 * 
 * @param	menu	The IMenuManager to which actions for the palette's context
 * 					menu can be added
 * @see org.eclipse.gef.ui.parts.ContextMenuProvider#buildContextMenu(org.eclipse.jface.
 * action.IMenuManager)
 */
public void buildContextMenu(IMenuManager menu) {
	Object selectedPart = getPaletteViewer().getSelectedEditParts().get(0);
	if (selectedPart instanceof TemplateEditPart) {
		ActionRegistry registry = (ActionRegistry)getEditor().getAdapter(ActionRegistry.class);
		menu.add(registry.getAction(IWorkbenchActionConstants.COPY));
	}
	if (getPaletteViewer().getCustomizer() != null)
		menu.add(new LayoutAction(getPaletteViewer().getPaletteViewerPreferencesSource()));
		menu.add(new IconSizeChangeAction(getPaletteViewer().getPaletteViewerPreferencesSource()));
		menu.add(new Separator());
		menu.add(new CustomizeAction(getPaletteViewer()));
}

/**
 * @see org.eclipse.gef.ui.parts.ContextMenuProvider#registerContextMenu(org.eclipse.jface.action.MenuManager)
 */
protected void registerContextMenu(MenuManager manager) {}

}
