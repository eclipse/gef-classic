package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author hudsonr
 * Created on Jul 23, 2003
 */
public class SubgraphFigure extends Figure {

IFigure contents;
IFigure footer;
IFigure header;

public SubgraphFigure() {
	contents = new Figure();
}

public IFigure getFooter() {
	return footer;
}

public IFigure getHeader() {
	return header;
}

public void setBounds(Rectangle rect) {
	super.setBounds(rect);
	contents.setBounds(bounds);
}

public void setFooter(IFigure figure) {
	footer = figure;
}

public void setHeader(IFigure figure) {
	header = figure;
}

}
