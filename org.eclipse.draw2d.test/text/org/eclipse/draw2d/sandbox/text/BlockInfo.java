package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.*;

public class BlockInfo
	extends Rectangle
{

protected IFigure owner;

public int getAscent(){return height;}
public int getDescent(){return 0;}
public int getHeight(){return height;}

public Rectangle getBounds(){
	return this;
}

public IFigure getOwner(){return owner;}

/** @deprecated */
public boolean isEmpty(){
	return super.isEmpty();
}

public void setOwner(IFigure f){owner = f;}

public void makeBaseline(int value){
	y = (value-getAscent());
}

/**@deprecated wrong spelling.*/
protected final void getDecent() throws IllegalAccessException{
	throw new RuntimeException("Wrong spelling of BlockInfo::getDescent()");//$NON-NLS-1$
}

}