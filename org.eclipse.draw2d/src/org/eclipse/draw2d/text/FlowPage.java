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
	setRecommendedWidth(w - getInsets().getWidth());
	validate();
	return pageSize.getExpanded(getInsets().getWidth(), getInsets().getHeight());
}

int getRecommendedWidth() {
	return recommendedWidth;
}

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