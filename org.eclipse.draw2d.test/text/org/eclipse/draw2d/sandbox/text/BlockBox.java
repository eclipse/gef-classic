package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.geometry.*;

public class BlockBox
	extends CompositeBox
{

public int getInnerLeft(){
	return x;
}

public int getInnerWidth(){
	return recommendedWidth;
}

}