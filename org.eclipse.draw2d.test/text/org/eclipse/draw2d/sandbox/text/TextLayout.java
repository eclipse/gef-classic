package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.geometry.*;
import java.util.*;

import org.eclipse.swt.graphics.Font;

public class TextLayout
	extends TextFlowLayout
{

public TextLayout(){}

protected TextFragmentBox getFragment(int i, Vector fragments){
	if (fragments.size() > i)
		return (TextFragmentBox)fragments.elementAt(i);
	return new TextFragmentBox();
}

protected float getAverageCharWidth(TextFragmentBox fragment){
	if (fragment.width != 0 && fragment.length != 0)
		return fragment.width/(float)fragment.length;
	return 0.0f;
}

protected void insertFragment(TextFragmentBox fragment, int i, Vector fragments){
	if (fragments.size() <= i)
		fragments.add(fragment);
	else
		fragments.set(i, fragment);
}

protected void layout(){

	 Vector fragments	= flowFigure.getFragments();//Reuse the previous Vector of fragments
	 String s         = ((TextFigure)flowFigure).getText();
	   Font f         = flowFigure.getFont();
	    int i         = 0; //The index of the current fragment;
	    int offset    = 0; //The current offset in the <B>orginial</B> text, not s
	  float prevAvgCharWidth;
	LineBox currentLine;
	TextFragmentBox fragment;

	do {
		fragment		= null;
		prevAvgCharWidth	= 0f;
		fragment 		= getFragment(i, fragments);
		fragment.offset	= offset;
		prevAvgCharWidth	= getAverageCharWidth(fragment);
		
		//This loop is done at most twice.
		//The second time through, a context.endLine()
		//was requested, and the loop will break.
		while (true){
			currentLine 	= context.getCurrentLine();
			fragment.length	=
				FlowUtilities.getTextForSpace(s,f,
					currentLine.getRemainingWidth(),
					prevAvgCharWidth);
			
			FlowUtilities.setupFragment(fragment, f, s);
			if (fragment.width < currentLine.getRemainingWidth() ||
				!currentLine.hasChildren()) break;
			context.endLine();
		}
		fragment.x = context.getCurrentX();
		context.addToCurrentLine(fragment);
		insertFragment(fragment, i, fragments);
		s = s.substring(fragment.length);
		offset += fragment.length;
		if (s.length() > 0) context.endLine();
		i++;
	} while (s.length() >0 && fragment.length != 0);

	//Remove the remaining unused fragments.
	while (i < fragments.size())
		fragments.removeElementAt(i++);
}

}


