package org.eclipse.draw2d.sandbox.text;

import java.util.*;
import org.eclipse.draw2d.geometry.*;

public class PageFlowLayout
	extends BlockFlowLayout
{

protected void endBlock(){
	//Update the page to be the size of the blockbox
	Vector v = flowFigure.getFragments();
	v.clear();
	v.addElement(blockBox);
}

public void postValidate(){
}

/**
 * Setup blockBox to the initial bounds of the Page
 */
protected void setupBlock(){
	//Remove all current Fragments
	blockBox.clear();

	//Setup the one fragment for this Block with the correct X and available width
	blockBox.setRecommendedWidth(flowFigure.getBounds().width);
	blockBox.x = flowFigure.getBounds().x;
}

}