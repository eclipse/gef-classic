package org.eclipse.draw2d.text;

public class TextFragmentBox
	extends FlowBox
{
public int offset;
public int length;
private int ascent;

public TextFragmentBox(){}

public int getAscent(){
	return ascent;
}

public void setAscent(int a){
	ascent = a;
}

public void setHeight(int h){
	height = h;
}

public void setWidth (int w){
	width = w;
}

}