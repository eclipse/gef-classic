package org.eclipse.gef.ui.palette;

import org.eclipse.draw2d.*;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.Image;

/**
 * A PaletteEditPart that has a {@link PaletteTemplateEntry} for its model and
 * a {@link Label} for its figure.  This is used for dragging template objects 
 * from the palette to a GraphicalViewer to create new objects.
 * 
 * @author Eric Bordeau
 */
public class TemplateEditPart
	extends PaletteEditPart
{

private static Border BORDER = new MarginBorder(3,0,3,0);

public TemplateEditPart(PaletteTemplateEntry entry) {
	super(entry);
}

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart (){
		public void getDescription(AccessibleEvent e) {
			e.result = getTemplateEntry().getDescription();
		}

		public void getName(AccessibleEvent e) {
			e.result = getTemplateEntry().getLabel();
		}

		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_LISTITEM;
		}
	};
}

public IFigure createFigure() {
	return new DetailedLabelFigure();
}

private PaletteTemplateEntry getTemplateEntry() {
	return (PaletteTemplateEntry)getModel();
}

private PaletteViewer getPaletteViewer() {
	return (PaletteViewer)getViewer();
}

protected void refreshVisuals() {
	DetailedLabelFigure fig = (DetailedLabelFigure)getFigure();
	PaletteEntry entry = getPaletteEntry();
	fig.setText(entry.getLabel());
	boolean large = getPreferenceSource().useLargeIcons();
	Image icon;
	if (large) {
		icon = entry.getLargeIcon();
	} else {
		icon = entry.getSmallIcon();
	}
	fig.setImage(icon);
	fig.setLayoutMode(getPreferenceSource().getLayoutSetting());
	super.refreshVisuals();
}

public void setFocus(boolean value) {
	super.setFocus(value);
	DetailedLabelFigure label = (DetailedLabelFigure)getFigure();
	if (value)
		label.setSelected(DetailedLabelFigure.SELECTED_WITH_FOCUS);
	else
		label.setSelected(DetailedLabelFigure.SELECTED_WITHOUT_FOCUS);
}

public void setSelected(int value) {
	super.setSelected(value);
	DetailedLabelFigure label = (DetailedLabelFigure)getFigure();
	if( value == SELECTED_PRIMARY )
		label.setSelected(DetailedLabelFigure.SELECTED_WITH_FOCUS);
	else if( value == SELECTED )
		label.setSelected(DetailedLabelFigure.SELECTED_WITHOUT_FOCUS);
	else {
		label.setSelected(DetailedLabelFigure.NOT_SELECTED);
	}		
}

}
