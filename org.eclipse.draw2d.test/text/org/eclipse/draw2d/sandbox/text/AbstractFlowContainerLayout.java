package org.eclipse.draw2d.sandbox.text;

import java.util.*;
import org.eclipse.draw2d.*;

public abstract class AbstractFlowContainerLayout
	extends TextFlowLayout
	implements IFlowContext
{

protected LineBox currentLine;


public void addToCurrentLine(BlockInfo block){
	getCurrentLine().add(block);
}

/**
 * Implement to clean up any unneeded state variables.
 */
abstract protected void cleanup();
/**
 * Used by getCurrentLine().
 */
abstract protected void createNewLine();
/**
 * Called after layoutChildren() when all children have been layed out.
 * This method exists to flush the last line.
 */
abstract protected void flush();

final public LineBox getCurrentLine(){
	if (currentLine == null)
		createNewLine();
	return currentLine;
}

public int getCurrentX(){
	return getCurrentLine().right();
}

protected void layout(){
	preLayout();
	layoutChildren();
	flush();
	cleanup();
}

/**
 * Layout all children.
 */
protected void layoutChildren(){
	List children = flowFigure.getChildren();
	for (int i=0; i<children.size(); i++){
		Figure f = (Figure)children.get(i);
		f.invalidate();
		f.validate();
	}
}

/**
 * Called before layoutChildren() to setup any necessary state.
 */
abstract protected void preLayout();

}