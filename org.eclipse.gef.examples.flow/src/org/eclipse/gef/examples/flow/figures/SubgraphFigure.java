package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
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

public SubgraphFigure(IFigure header, IFigure footer) {
	contents = new Figure();
	add(contents);
	add(this.header = header);
	add(this.footer = footer);
}

public IFigure getContents() {
	return contents;
}

public IFigure getFooter() {
	return footer;
}

public IFigure getHeader() {
	return header;
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension dim = new Dimension();
	dim.width = Math.max(wHint, header.getPreferredSize().width);
	dim.height = hHint;
	return dim;
}

public void setBounds(Rectangle rect) {
	super.setBounds(rect);
	contents.setBounds(bounds);
	Dimension size = footer.getPreferredSize();
	footer.setLocation(rect.getBottomLeft().translate(0, -size.height));
	footer.setSize(size);
	
	size = header.getPreferredSize();
	header.setSize(size);
	header.setLocation(rect.getLocation());
}

}
