package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.geometry.*;

import java.util.*;

public class FakeFlowLayout
	extends TextFlowLayout
{

private final int [] spans, ascents;
private double scale;

public FakeFlowLayout(int spans[], int ascents[], double scale){
	this.spans = spans;
	this.scale = scale;
	this.ascents = ascents;
}

protected void layout(){
//	Rectangle newBounds = new Rectangle();
	Vector fragments = flowFigure.getFragments();
	int i;

	FragmentBox block;
	for (i=0; i < spans.length; i++){
		block = null;
		if (fragments.size() > i)
			block = (FragmentBox)fragments.elementAt(i);
		if (block == null)
			block = new FragmentBox();
		positionBlock(block, spans[i], ascents[i]);
		if (fragments.size() <= i)
			fragments.add(block);
		else
			fragments.set(i, block);
		context.addToCurrentLine(block);
	}
}

protected void positionBlock(FragmentBox block, int width, int ascent){
	//Setting the ascent
	block.height = (int)(30*scale);
	block.setAscent(ascent);
	block.width = width;
	LineBox line = context.getCurrentLine();
	
	if (width > line.getRemainingWidth() && line.hasChildren()){	
		context.endLine();
		line = context.getCurrentLine();
	}
	
	block.x = context.getCurrentX();
}

}