package org.eclipse.gef.ui.palette;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.palette.PaletteSeparator;

/**
 * EditPart for the PaletteSeparator
 * 
 * @author Pratik Shah
 */
public class SeparatorEditPart extends PaletteEditPart {

/**
 * Constructor
 * 
 * @param separator The PaletteSeparator for which this EditPart is being
 * created
 */
public SeparatorEditPart(PaletteSeparator separator) {
	super(separator);
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
protected IFigure createFigure() {
	Figure figure = new SeparatorFigure();
	figure.setSize(0, 7);
	figure.setBorder(new SeparatorBorder());
	return figure;
}


private class SeparatorFigure extends Figure {
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(wHint, getSize().height);
	}
}

}
