package org.eclipse.draw2d;

/**
 * A helper used in depth-first searches of a figure subgraph.
 * @author hudsonr
 * @since 2.1
 */
public interface TreeSearch {

/**
 * Returns <code>true</code> if the given figure is accepted by the search.  This
 * @param figure the current figure in the traversal * @return <code>true</code> if the figure is accepted */
boolean accept(IFigure figure);

/**
 * Returns <code>true</code> the figure and all of its contained figures should be pruned
 * from the search.
 * @param figure the current figure in the traversal * @return <code>true</code> if the subgraph should be pruned */
boolean prune(IFigure figure);

}
