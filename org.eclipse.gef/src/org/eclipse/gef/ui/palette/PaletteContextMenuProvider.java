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

/*
 * @TODO:Pratik
 * Figure out how the actions for the menu should be created.  The way it works for
 * editors is that the editor holds on to all the actions, and the menu provider for the
 * editor gets them from the editor and adds them to the menu.  Here, maybe the
 * paletteviewer can hold on to all the actions.  Right now, new actions are created every
 * time the menu pops up.
 */	

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
		menu.add(new Separator());
	} else if (selectedPart instanceof CategoryEditPart) {
		menu.add(new PinCategoryAction(
				((CategoryEditPart)selectedPart).getCategoryFigure()));
		menu.add(new Separator());
	}
	menu.add(new LayoutAction(getPaletteViewer().getPaletteViewerPreferencesSource()));
	menu.add(new ChangeIconSizeAction(
			getPaletteViewer().getPaletteViewerPreferencesSource()));
	menu.add(new Separator());
	if (getPaletteViewer().getCustomizer() != null) {
		menu.add(new CustomizeAction(getPaletteViewer()));
	}
	menu.add(new SettingsAction(getPaletteViewer()));
}

}
