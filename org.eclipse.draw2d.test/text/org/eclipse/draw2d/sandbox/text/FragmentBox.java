package org.eclipse.draw2d.sandbox.text;


public class FragmentBox
	extends BlockInfo
{
protected int ascent;
public int getAscent(){
	return ascent;
}

public int getDescent(){
	return getHeight()-getAscent();
}

public void setAscent(int a){ ascent = a;}
public void setHeight(int h){ height = h; }
public void setWidth (int w){ width = w;}

}