package org.eclipse.gef.examples.flow.parts;

import org.eclipse.draw2d.ColorConstants;

import org.eclipse.gef.examples.flow.figures.SubgraphFigure;

/**
 * @author hudsonr
 */
public class ParallelActivityPart extends StructuredActivityPart {

/**
 * @see org.eclipse.gef.EditPart#setSelected(int)
 */
public void setSelected(int value) {
	super.setSelected(value);
	SubgraphFigure sf = (SubgraphFigure)getFigure();
	if (value == SELECTED_NONE) {
		sf.getHeader().setForegroundColor(null);
		sf.getFooter().setForegroundColor(null);
	} else {
		sf.getHeader().setForegroundColor(ColorConstants.menuForegroundSelected);
		sf.getFooter().setForegroundColor(ColorConstants.menuForegroundSelected);
	}
}

}
