package org.eclipse.draw2d.text;

import org.eclipse.draw2d.geometry.*;

public class FlowPage
	extends BlockFlow
{

private int pageWidth;
//private Rectangle pageBounds = new Rectangle();

/**
 * @see org.eclipse.draw2d.text.BlockFlow#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return new PageFlowLayout(this);
}

/**
 * @see org.eclipse.draw2d.Figure#getMinimumSize()
 */
public Dimension getMinimumSize() {
	Dimension d = getBounds().getSize();
	d.height = 1;
	return d;
}

int getPageWidth() {
	return pageWidth;
}

public Dimension getPreferredSize(int w, int h) {
	if (w == -1)
		w = Integer.MAX_VALUE;
	setPageWidth(w);
	validate();
	return getBounds().getSize();
}

private void setPageWidth(int width) {
	if (pageWidth == width)
		return;
	pageWidth = width;
	invalidate();
}

public void validate() {
	if (isValid())
		return;
	super.validate();
	postValidate();
}

}