package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
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
	
	private static final Insets CROP = new Insets(1, 4, 2, 5);

	/**
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
	protected void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCropped(CROP);
		if (getBackgroundColor() == ColorConstants.listBackground) {
			g.setForegroundColor(ColorConstants.button);
			g.drawLine(r.getTopLeft(), r.getTopRight());
			g.drawLine(r.getBottomLeft(), r.getBottomRight());
		} else {
			g.setForegroundColor(FigureUtilities.mixColors(
						ColorConstants.buttonLightest, ColorConstants.button));
			g.drawLine(r.getBottomLeft(), r.getTopLeft());
			g.drawLine(r.getTopLeft(), r.getTopRight());
			g.setForegroundColor(FigureUtilities.mixColors(
						ColorConstants.button, ColorConstants.buttonDarker));
			g.drawLine(r.getBottomLeft(), r.getBottomRight());
			g.drawLine(r.getBottomRight(), r.getTopRight());
		}
	}
}

}
