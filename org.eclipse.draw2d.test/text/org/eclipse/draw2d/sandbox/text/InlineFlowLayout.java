package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.*;
import java.util.*;

public class InlineFlowLayout
	extends AbstractFlowContainerLayout
{

public void addToCurrentLine(BlockInfo block){
	getCurrentLine().add(block);
	flowFigure.getFragments().add(currentLine);
}

protected void createNewLine(){
	currentLine = new LineBox();
	setupLine(currentLine);
}

protected void cleanup(){
	currentLine = null;
}

protected void flush(){
	if (currentLine != null) context.addToCurrentLine(currentLine);
}

public void endLine(){
	if (currentLine == null) return;
	//If nothing was ever placed in the line, ignore it.
	if (currentLine.hasChildren()){
		context.addToCurrentLine(currentLine);
		context.endLine();
	}
	currentLine = null;
}

public void preLayout(){
	flowFigure.getFragments().clear();
}

protected void setupLine(LineBox line){
	LineBox parent = context.getCurrentLine();
	line.setLocation(context.getCurrentX(), parent.getInnerTop());
	line.setRecommendedWidth(parent.getRemainingWidth());
}

}