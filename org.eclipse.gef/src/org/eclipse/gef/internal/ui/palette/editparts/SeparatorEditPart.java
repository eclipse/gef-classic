package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

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
	return new SeparatorFigure();
}

/**
 * Figure for the separator
 * 
 * @author Pratik Shah
 */
static class SeparatorFigure extends Figure {
	/**
	 * Constructor
	 */
	public SeparatorFigure() {
		setSize(5, 5);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		return new Dimension(wHint, getSize().height);
	}
	
	static final Insets CROP = new Insets(0, 4, 0, 5);

	/**
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCropped(CROP);
		g.setBackgroundColor(ColorConstants.button);
		g.fillRectangle(r);
		r.resize(-2,-2);
		g.setForegroundColor(ColorConstants.buttonLightest);
		g.drawLine(r.x + 1, r.y + 1, r.right(), r.y + 1);
		g.drawLine(r.x + 1, r.y + 1, r.x + 1, r.bottom());
		g.setForegroundColor(ColorConstants.buttonDarker);
		g.drawLine(r.right(), r.y + 1, r.right(), r.bottom());
		g.drawLine(r.x + 1, r.bottom(), r.right(), r.bottom());
	}
}

}
