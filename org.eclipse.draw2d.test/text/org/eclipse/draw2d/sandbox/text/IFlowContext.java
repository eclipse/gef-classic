package org.eclipse.draw2d.sandbox.text;

public interface IFlowContext {

public void addToCurrentLine(BlockInfo box);
public void endLine();
public LineBox getCurrentLine();
public int getCurrentX();

public boolean isContextChanged();
}