package org.eclipse.draw2d.text;

import java.util.List;

import org.eclipse.draw2d.geometry.*;

public class FlowPage
	extends BlockFlow
{

private Dimension pageSize = new Dimension();
private int recommendedWidth;

/**
 * @see org.eclipse.draw2d.text.BlockFlow#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return new PageFlowLayout(this);
}

/**
 * @see org.eclipse.draw2d.Figure#getMinimumSize()
 */
public Dimension getMinimumSize(int w, int h) {
	return getPreferredSize(w, h);
}

public Dimension getPreferredSize(int w, int h) {
	int width = w;
	if (w >= 0) {
		width = Math.max(0, w - getInsets().getWidth());
	}
	setRecommendedWidth(width);
	validate();
	return pageSize.getExpanded(getInsets().getWidth(), getInsets().getHeight());
}

int getRecommendedWidth() {
	return recommendedWidth;
}

///**
// * @see org.eclipse.draw2d.Figure#paintChildren(Graphics)
// */
//protected void paintChildren(Graphics graphics) {
//	//Displacement must be a function of the current vertical aligment.
//	int displacement = (getClientArea(Rectangle.SINGLETON).height - pageSize.height) / 2;
//	//If displacement is 0, just call super.paintChidlren() and return
//	graphics.translate(0, displacement);
//	graphics.pushState();
//	super.paintChildren(graphics);
//	graphics.popState();
//	graphics.restoreState();
//}

public void postValidate(){
	Rectangle r = getBlockBox().toRectangle();
	pageSize.width = r.width;
	pageSize.height = r.height;
	List v = getChildren();
	for (int i=0; i<v.size(); i++)
		((FlowFigure)v.get(i)).postValidate();
}

/**
 * @see org.eclipse.draw2d.text.FlowFigure#setBounds(Rectangle)
 */
public void setBounds(Rectangle r) {
	super.setBounds(r);
	setRecommendedWidth(r.width - getInsets().getWidth());
}

private void setRecommendedWidth(int width) {
	if (recommendedWidth == width)
		return;
	recommendedWidth = width;
	invalidate();
}

public void validate() {
	if (isValid())
		return;
	super.validate();
	postValidate();
}

}