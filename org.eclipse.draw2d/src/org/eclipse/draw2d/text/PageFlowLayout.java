package org.eclipse.draw2d.text;

public class PageFlowLayout
	extends BlockFlowLayout
{

public PageFlowLayout(FlowPage page) {
	super(page);
}

protected void endBlock() { }

public void postValidate() { }

/**
 * Setup blockBox to the initial bounds of the Page
 */
protected void setupBlock() {
	//Remove all current Fragments
	blockBox.clear();

	//Setup the one fragment for this Block with the correct X and available width
	blockBox.setRecommendedWidth(((FlowPage)getFlowFigure()).getRecommendedWidth());
	blockBox.x = 0;
}

}