package org.eclipse.draw2d.text;

public interface FlowContext {

/**
 * Adds the given box into the current line.
 * @param box the FlowBox to add */
void addToCurrentLine(FlowBox box);

/**
 * The current line should be committed if it is occupied, and then set to
 * <code>null</code>. Otherwise, do nothing.
 */
void endLine();

/**
 * Obtains the current line, creating a new line if there is no current line.
 * @return the current line */
LineBox getCurrentLine();

int getCurrentY();

/**
 * @return <code>true</code> if the current line contains any fragments */
boolean isCurrentLineOccupied();

}