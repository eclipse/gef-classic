package org.eclipse.gef.ui.palette;

import org.eclipse.draw2d.*;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.palette.TemplateEntry;
import org.eclipse.swt.accessibility.*;

/**
 * A PaletteEditPart that has a {@link TemplateEntry} for its model and
 * a {@link Label} for its figure.  This is used for dragging template objects 
 * from the palette to a GraphicalViewer to create new objects.
 * 
 * @author Eric Bordeau
 */
public class TemplateEditPart
	extends PaletteEditPart
{

private static Border BORDER = new MarginBorder(3,0,3,0);

public TemplateEditPart(TemplateEntry entry) {
	setModel(entry);
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
	SelectableLabel label = new SelectableLabel();
	label.setBorder(BORDER);
	label.setLabelAlignment(label.LEFT);
	return label;
}

private TemplateEntry getTemplateEntry() {
	return (TemplateEntry)getModel();
}

private PaletteViewer getPaletteViewer() {
	return (PaletteViewer)getViewer();
}

protected void refreshVisuals() {
	TemplateEntry entry = getTemplateEntry();
	SelectableLabel label = (SelectableLabel)getFigure();

	label.setText(entry.getLabel());
	label.setIcon(entry.getSmallIcon());
	
	String desc = entry.getDescription();
	if(desc != null && !desc.equals("") && !desc.equals(entry.getLabel())) //$NON-NLS-1$
		label.setToolTip(new Label(desc));
}

public void setFocus(boolean value) {
	super.setFocus(value);
	SelectableLabel label = (SelectableLabel)getFigure();
	if (value)
		label.requestFocus();
}

public void setSelected(int value) {
	super.setSelected(value);
	SelectableLabel label = (SelectableLabel)getFigure();
	label.setSelected(value == SELECTED_PRIMARY);
}

}
