package org.eclipse.gef.examples.flow.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.examples.flow.figures.ParallelActivityFigure;
import org.eclipse.gef.examples.flow.figures.SubgraphFigure;

/**
 * @author hudsonr
 */
public class ParallelActivityPart extends StructuredActivityPart {

protected IFigure createFigure() {
	return new ParallelActivityFigure();
}

/**
 * @see org.eclipse.gef.EditPart#setSelected(int)
 */
public void setSelected(int value) {
	super.setSelected(value);
	SubgraphFigure sf = (SubgraphFigure)getFigure();
	sf.setSelected(value != SELECTED_NONE);
}

}
