package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.editparts.DrawerEditPart;

/**
 * Provides the context menu for a palette
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
public PaletteContextMenuProvider(PaletteViewerImpl palette) {
	super(palette);
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
	menu.add(new Separator(GEFActionConstants.GROUP_COPY));
	menu.add(new Separator(GEFActionConstants.MB_ADDITIONS));
	menu.add(new Separator(GEFActionConstants.GROUP_VIEW));
	menu.add(new Separator(GEFActionConstants.GROUP_REST));

	Object selectedPart = getPaletteViewer().getSelectedEditParts().get(0);
	if (selectedPart instanceof DrawerEditPart) {
		menu.appendToGroup(GEFActionConstants.MB_ADDITIONS, 
							new PinDrawerAction((DrawerEditPart)selectedPart));
	}
	menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new FolderLayoutAction(
			getPaletteViewer().getPaletteViewerPreferencesSource()));
	menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new ListLayoutAction(
			getPaletteViewer().getPaletteViewerPreferencesSource()));
	menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new IconsLayoutAction(
			getPaletteViewer().getPaletteViewerPreferencesSource()));
	menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new DetailsLayoutAction(
			getPaletteViewer().getPaletteViewerPreferencesSource()));
	menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new Separator());
	menu.appendToGroup(GEFActionConstants.GROUP_VIEW, new ChangeIconSizeAction(
			getPaletteViewer().getPaletteViewerPreferencesSource()));
	if (getPaletteViewer().getCustomizer() != null) {
		menu.appendToGroup(GEFActionConstants.GROUP_REST, 
							new CustomizeAction(getPaletteViewer()));
	}
	menu.appendToGroup(GEFActionConstants.GROUP_REST, 
						new SettingsAction(getPaletteViewer()));
}

}
