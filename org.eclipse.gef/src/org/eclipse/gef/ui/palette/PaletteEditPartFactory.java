/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.palette;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.internal.ui.palette.editparts.*;

/**
 * Factory to create EditParts for different PaletteEntries.
 * 
 * @author Pratik Shah */
public class PaletteEditPartFactory
	implements EditPartFactory
{

/**
 * Create DrawerEditPart - edit part for PaletteDrawer
 * 
 * @param parentEditPart	the parent of the new editpart to be created
 * @param model				the PaletteDrawer
 * @return the newly created EditPart
 */
protected EditPart createDrawerEditPart(EditPart parentEditPart, Object model) {
	return new DrawerEditPart((PaletteDrawer)model);
}

/**
 * @see org.eclipse.gef.EditPartFactory#createEditPart(EditPart, Object)
 */
public EditPart createEditPart(EditPart parentEditPart, Object model) {
	if (model instanceof PaletteRoot)
		return createMainPaletteEditPart(parentEditPart, model);
	if (model instanceof PaletteContainer)
		if (PaletteDrawer.PALETTE_TYPE_DRAWER.equals(((PaletteContainer)model).getType()))
			return createDrawerEditPart(parentEditPart, model);
	if (model instanceof PaletteContainer)
		if (PaletteGroup.PALETTE_TYPE_GROUP.equals(((PaletteContainer) model).getType())
				|| PaletteContainer.PALETTE_TYPE_UNKNOWN.equals(
				((PaletteContainer)model).getType()))
			return createGroupEditPart(parentEditPart, model);
	if (model instanceof PaletteTemplateEntry)
		return createTemplateEditPart(parentEditPart, model);
	if (model instanceof PaletteSeparator)
		return createSeparatorEditPart(parentEditPart, model);
	if (model instanceof PaletteEntry)
		return createEntryEditPart(parentEditPart, model);
	return null;
}

/**
 * Create SeparatorEditPart - edit part for PaletteSeparator
 * 
 * @param parentEditPart	the parent of the new editpart to be created
 * @param model				the PaletteSeparator
 * @return the newly created EditPart
 */
protected EditPart createSeparatorEditPart(EditPart parentEditPart,	Object model) {
	return new SeparatorEditPart((PaletteSeparator)model);
}

/**
 * Create ToolEntryEditPart - edit part for ToolEntry
 * 
 * @param parentEditPart	the parent of the new editpart to be created
 * @param model				the ToolEntry
 * @return the newly created EditPart
 */
protected EditPart createEntryEditPart(EditPart parentEditPart, Object model) {
	return new ToolEntryEditPart((PaletteEntry)model);
}

/**
 * Create GroupEditPart - edit part for PaletteGroup
 * 
 * @param parentEditPart	the parent of the new editpart to be created
 * @param model				the PaletteGroup
 * @return the newly created EditPart
 */
protected EditPart createGroupEditPart(EditPart parentEditPart, Object model) {
	return new GroupEditPart((PaletteContainer)model);
}

/**
 * Create SliderPaletteEditPart - edit part for PaletteRoot
 * 
 * @param parentEditPart	the parent of the new editpart to be created
 * @param model				the PaletteRoot
 * @return the newly created EditPart
 */
protected EditPart createMainPaletteEditPart(EditPart parentEditPart, Object model) {
	return new SliderPaletteEditPart((PaletteRoot)model);
}

/**
 * Create TemplateEditPart - edit part for PaletteTemplateEntry
 * 
 * @param parentEditPart	the parent of the new editpart to be created
 * @param model				the PaletteTemplateEntry
 * @return the newly created EditPart
 */
protected EditPart createTemplateEditPart(EditPart parentEditPart, Object model) {
	return new TemplateEditPart((PaletteTemplateEntry)model);
}

}