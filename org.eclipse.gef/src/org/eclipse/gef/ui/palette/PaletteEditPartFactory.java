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

public class PaletteEditPartFactory
	implements EditPartFactory
{

protected EditPart createDrawerEditPart(EditPart parentEditPart, Object model){
	return new DrawerEditPart((PaletteDrawer)model);
}

public EditPart createEditPart(EditPart parentEditPart, Object model) {
	if (model instanceof PaletteRoot)
		return createMainPaletteEditPart(parentEditPart, model);
	if (model instanceof PaletteContainer)
		if (PaletteDrawer.PALETTE_TYPE_DRAWER.equals(((PaletteContainer)model).getType()))
			return createDrawerEditPart(parentEditPart, model);
	if (model instanceof PaletteContainer)
		if (PaletteGroup.PALETTE_TYPE_GROUP.equals(((PaletteContainer) model).getType())
				|| PaletteContainer.PALETTE_TYPE_UNKNOWN.equals(((PaletteContainer)model).getType()))
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
 * Method createSeparatorEditPart.
 * @param parentEditPart
 * @param model
 * @return EditPart
 */
private EditPart createSeparatorEditPart(
	EditPart parentEditPart,
	Object model) {
	return new SeparatorEditPart((PaletteSeparator)model);
}


protected EditPart createEntryEditPart(EditPart parentEditPart, Object model){
	return new ToolEntryEditPart((PaletteEntry)model);
}

protected EditPart createGroupEditPart(EditPart parentEditPart, Object model){
	return new GroupEditPart((PaletteContainer)model);
}

protected EditPart createMainPaletteEditPart(EditPart parentEditPart, Object model){
	return new SliderPaletteEditPart((PaletteRoot)model);
}

protected EditPart createTemplateEditPart(EditPart parentEditPart, Object model) {
	return new TemplateEditPart((PaletteTemplateEntry)model);
}

}