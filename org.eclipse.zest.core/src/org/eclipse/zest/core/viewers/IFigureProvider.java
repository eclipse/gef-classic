package org.eclipse.zest.core.viewers;

import org.eclipse.draw2d.IFigure;

/**
 * Allows a user to create a figure for an element in 
 * graph model.  To use this interface, it should 
 * be implemented and passed to {@link GraphViewer#setLabelProvider()}
 */
public interface IFigureProvider {

	/**
	 * Creates a custom figure for a graph model element
	 */
	public IFigure getFigure(Object element);
}
