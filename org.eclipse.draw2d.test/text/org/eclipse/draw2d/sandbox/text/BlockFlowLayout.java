package org.eclipse.draw2d.sandbox.text;

import java.util.*;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class BlockFlowLayout
	extends AbstractFlowContainerLayout
{

protected LineBox
	previousLine = null;

protected BlockBox blockBox = new BlockBox();

public Dimension calculatePreferredSize(IFigure f){
	return null;
}

public void addToCurrentLine(BlockInfo block){
	super.addToCurrentLine(block);
	//Also add the block to this block, since lines are temporary.
	blockBox.add(block);
}

protected void cleanup(){
	currentLine = previousLine = null;
}

protected void createNewLine(){
	currentLine = new LineBox();
	setupLine(currentLine);
}

protected void endBlock(){
	context.addToCurrentLine(blockBox);
	context.endLine();
	Vector v = flowFigure.getFragments();
	v.clear();
	v.addElement(blockBox);
}

public void endLine(){
	//If there is no current line, state is equivalent to new line
	if (currentLine == null) return;

	if (currentLine.hasChildren())
		layoutLine();
	else
		return;
	LineBox box = currentLine;
	currentLine = previousLine;
	previousLine = box;

	setupLine(getCurrentLine());
}

/**
 * Adjust all fragments in the current line to have the same baseline.
 * Do any additional adjustments, such as horizontal alignment.
 */
protected void layoutLine(){
	LineBox line = getCurrentLine();
	line.commit();
}

protected void flush(){
	if (currentLine != null) currentLine.commit();
	endBlock();
}

protected void preLayout(){
	setupBlock();
	//Probably could setup current and previous line here, or just previous
}

/**
 * sets up the single block that contains all of the lines.
 */
protected void setupBlock(){
	//Remove all current Fragments
	blockBox.clear();

	//Ask for a new line, in case we are in the middle of a line
	context.endLine();
	LineBox line = context.getCurrentLine();

	//Setup the one fragment for this Block with the correct X and available width
	blockBox.setRecommendedWidth(line.getRemainingWidth());
	blockBox.y = line.getInnerTop();
	blockBox.x = context.getCurrentX();
}

/**
 * Override to setup the line's x, remaining, and available width
 */
protected void setupLine(LineBox box){
	box.clear();
	box.x = blockBox.getInnerLeft();
	box.setRecommendedWidth(blockBox.getInnerWidth());
	if (previousLine == null){
		box.y = blockBox.getInnerTop();
	} else {
		box.y = previousLine.bottom();
	}
	box.validate();
}

}