package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.*;
import org.eclipse.gef.ui.palette.editparts.*;
import org.eclipse.gef.palette.*;

public class PaletteEditPartFactory
	implements EditPartFactory
{

protected EditPart createCategoryEditPart(EditPart parentEditPart, Object model){
	return new CategoryEditPart((PaletteCategory)model);
}

public EditPart createEditPart(EditPart parentEditPart, Object model){
	if( model instanceof PaletteRoot )
		return createMainPaletteEditPart(parentEditPart, model);
	if( model instanceof PaletteContainer )
		if(PaletteContainer.PALETTE_TYPE_CATEGORY.equals(((PaletteContainer)model).getType()))
			return createCategoryEditPart(parentEditPart, model);
	if( model instanceof PaletteContainer )
		if(	PaletteContainer.PALETTE_TYPE_GROUP.equals(((PaletteContainer)model).getType()) ||
			PaletteContainer.PALETTE_TYPE_UNKNOWN.equals(((PaletteContainer)model).getType()))
			return createGroupEditPart(parentEditPart, model);
	if( model instanceof PaletteTemplateEntry)
		return createTemplateEditPart(parentEditPart, model);
	if( model instanceof PaletteSeparator )
		return createSeparatorEditPart(parentEditPart, model);
	if( model instanceof PaletteEntry )
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
	return new EntryEditPart((PaletteEntry)model);
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