package org.eclipse.gef.examples.flow.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.examples.flow.parts.DummyLayout;

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
	contents.setLayoutManager(new DummyLayout());
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
	dim.width = getFooter().getPreferredSize().width;
	dim.width += getInsets().getWidth();
	dim.height = 50;
	return dim;
}

public void setBounds(Rectangle rect) {
	super.setBounds(rect);
	rect = Rectangle.SINGLETON;
	getClientArea(rect);
	contents.setBounds(rect);
	Dimension size = footer.getPreferredSize();
	footer.setLocation(rect.getBottomLeft().translate(0, -size.height));
	footer.setSize(size);
	
	size = header.getPreferredSize();
	header.setSize(size);
	header.setLocation(rect.getLocation());
}

}
