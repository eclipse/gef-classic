package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.geometry.*;
import java.util.Vector;

public class CompositeBox
	extends BlockInfo
{

protected Vector children = new Vector();
protected boolean invalid = true;
protected int recommendedWidth;

public void add(BlockInfo block){
	children.add(block);
	unionInfo(block);
}

public void clear(){
	children.clear();
	invalidate();
}

public int getAscent(){
	return getBounds().height;
}

public Rectangle getBounds(){
	validate();
	return this;
}

public Vector getFragments(){
	return children;
}

public int getHeight(){
	validate();
	return height;
}

public int getInnerTop(){
	validate();
	return y;
}

public boolean hasChildren(){
	return !children.isEmpty();
}

protected void invalidate(){
	invalid = true;
}

protected void resetInfo(){
	width = height = 0;
}

public void setRecommendedWidth(int w){
	recommendedWidth = w;
}

protected void unionInfo(BlockInfo blockInfo){
	union (blockInfo);
}

public void validate(){
	if (!invalid) return;
	invalid = false;
	resetInfo();
	for (int i=0; i< children.size(); i++)
		unionInfo((BlockInfo)children.elementAt(i));
}

}