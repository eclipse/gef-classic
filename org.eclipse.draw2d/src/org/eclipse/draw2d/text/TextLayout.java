package org.eclipse.draw2d.text;

import java.util.*;

import org.eclipse.swt.graphics.Font;

/**
 * The layout for {@link TextFlow}.
 * @author hudsonr
 * @since 2.1 */
public class TextLayout
	extends FlowFigureLayout
{

/**
 * Consturcts a new TextLayout on the specified TextFlow.
 * @param flow the TextFlow */
public TextLayout(TextFlow flow) {
	super(flow);
}

/**
 * Reuses an existing <code>TextFragmentBox</code>, or creates a new one.
 * @param i the index * @param fragments the original list of fragments * @return a TextFragmentBox */
protected TextFragmentBox getFragment(int i, List fragments){
	if (fragments.size() > i)
		return (TextFragmentBox)fragments.get(i);
	return new TextFragmentBox();
}

protected float getAverageCharWidth(TextFragmentBox fragment){
	if (fragment.width != 0 && fragment.length != 0)
		return fragment.width/(float)fragment.length;
	return 0.0f;
}

protected void insertFragment(TextFragmentBox fragment, int i, List fragments){
	if (fragments.size() <= i)
		fragments.add(fragment);
	//This was a reused fragment, it is already in the list
//	else
//		fragments.set(i, fragment);
}

protected void layout(){
	TextFlow flowFigure = (TextFlow)getFlowFigure();
	
	   List fragments = flowFigure.getFragments();//Reuse the previous List of fragments
	 String s         = ((TextFlow)flowFigure).getText();
	   Font f         = flowFigure.getFont();
	    int i         = 0; //The index of the current fragment;
	    int offset    = 0; //The current offset in the ORIGINAL text, not s
	  float prevAvgCharWidth;
	LineBox currentLine;
	TextFragmentBox fragment;

	do {
		fragment = null;
		prevAvgCharWidth = 0f;
		fragment = getFragment(i, fragments);
		fragment.offset	= offset;
		prevAvgCharWidth = getAverageCharWidth(fragment);
		
		//This loop is done at most twice.
		//The second time through, a context.endLine()
		//was requested, and the loop will break.
		while (true){
			currentLine = context.getCurrentLine();
			fragment.length	= FlowUtilities.getTextForSpace(
				s,
				f,
				currentLine.getAvailableWidth(),
				prevAvgCharWidth);
			
			FlowUtilities.setupFragment(fragment, f, s);
			if (fragment.width < currentLine.getAvailableWidth()
			  || !context.isCurrentLineOccupied())
				break;
			context.endLine();
		}
		//fragment.x = context.getCurrentX();
		context.addToCurrentLine(fragment);
		insertFragment(fragment, i, fragments);
		s = s.substring(fragment.length);
		offset += fragment.length;
		if (s.length() > 0)
			context.endLine();
		i++;
	} while (s.length() > 0 && fragment.length != 0);

	//Remove the remaining unused fragments.
	while (i < fragments.size())
		fragments.remove(i++);
}

}


