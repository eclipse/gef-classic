package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;

/**
 * @author Eric Bordeau, Pratik Shah
 */
public class TemplateEditPart
	extends PaletteEditPart
{

private static final Border BORDER = new MarginBorder(1, 1, 1, 2);

/**
 * Constructor
 * 
 * @param	entry	The model entry */
public TemplateEditPart(PaletteTemplateEntry entry) {
	super(entry);
}

/** * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#createAccessible() */
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

/** * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure() */
public IFigure createFigure() {
	IFigure fig = new DetailedLabelFigure();
	fig.setRequestFocusEnabled(true);
	fig.setBorder(BORDER);
	return fig;
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#getDragTracker(Request)
 */
public DragTracker getDragTracker(Request request) {
	return new SingleSelectionTracker() {
		protected boolean handleButtonDown(int button) {
			getFigure().requestFocus();
			return super.handleButtonDown(button);
		}
	};
}

private PaletteTemplateEntry getTemplateEntry() {
	return (PaletteTemplateEntry)getModel();
}

/** * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals() */
protected void refreshVisuals() {
	DetailedLabelFigure fig = (DetailedLabelFigure)getFigure();
	PaletteEntry entry = getPaletteEntry();
	fig.setName(entry.getLabel());
	fig.setDescription(entry.getDescription());
	if (getPreferenceSource().useLargeIcons())
		setImageDescriptor(entry.getLargeIcon());
	else
		setImageDescriptor(entry.getSmallIcon());
	fig.setLayoutMode(getPreferenceSource().getLayoutSetting());
	super.refreshVisuals();
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	DetailedLabelFigure fig = (DetailedLabelFigure)getFigure();
	fig.setImage(image);
}

/** * @see org.eclipse.gef.EditPart#setSelected(int) */
public void setSelected(int value) {
	super.setSelected(value);
	DetailedLabelFigure label = (DetailedLabelFigure)getFigure();
	if (value == SELECTED_PRIMARY) {
		label.requestFocus();
		label.setSelected(true);
	} else {
		label.setSelected(false);
	}		
}

}
