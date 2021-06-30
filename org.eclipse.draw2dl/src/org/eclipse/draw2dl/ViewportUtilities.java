/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.draw2dl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to support working with {@link org.eclipse.draw2dl.Viewport}s.
 * 
 * @author Philip Ritzkopf
 * @author Alexander Nyssen
 * 
 * @since 3.6
 * 
 */
public final class ViewportUtilities {

	private ViewportUtilities() {
		// provides only static utility functions and should not be accessed in
		// any other way than static.
	}

	/**
	 * Returns all enclosing {@link org.eclipse.draw2dl.Viewport}s for a given {@link org.eclipse.draw2dl.IFigure},
	 * beginning with its direct enclosing {@link org.eclipse.draw2dl.Viewport} up the root
	 * {@link org.eclipse.draw2dl.Viewport} in the figure's parent hierarchy.
	 * 
	 * @param figure
	 * @return A list of {@link org.eclipse.draw2dl.Viewport}s representing the figure's enclosing
	 *         {@link org.eclipse.draw2dl.Viewport} path, where the nearest enclosing
	 *         {@link org.eclipse.draw2dl.Viewport} as the first element and the root
	 *         {@link org.eclipse.draw2dl.Viewport} as the last element. In case there is no
	 *         enclosing {@link org.eclipse.draw2dl.Viewport}, an empty list is returned.
	 */
	public static List getEnclosingViewportsPath(org.eclipse.draw2dl.IFigure figure) {
		org.eclipse.draw2dl.Viewport nearestEnclosingViewport = getNearestEnclosingViewport(figure);
		if (nearestEnclosingViewport == null) {
			return new ArrayList();
		}
		org.eclipse.draw2dl.Viewport rootViewport = getRootViewport(figure);
		return getViewportsPath(nearestEnclosingViewport, rootViewport, true);
	}

	/**
	 * Returns a list containing the provided leaf {@link org.eclipse.draw2dl.Viewport} as the first
	 * element, and all its enclosing {@link org.eclipse.draw2dl.Viewport}s up to the root
	 * {@link org.eclipse.draw2dl.Viewport}, where the root {@link org.eclipse.draw2dl.Viewport} forms the last element
	 * of the list.
	 * 
	 * @param leafViewport
	 *            The {@link org.eclipse.draw2dl.Viewport}, whose parent hierarchy is processed.
	 * @param rootViewport
	 *            an ancestor of the given leafViewport, which marks the end
	 *            point of the hierarchy to be processed.
	 * @return A list of {@link org.eclipse.draw2dl.Viewport}s containing the leaf {@link org.eclipse.draw2dl.Viewport}
	 *         as the first element, the root {@link org.eclipse.draw2dl.Viewport} as the last and
	 *         in between all enclosing {@link org.eclipse.draw2dl.Viewport}s of the leaf
	 *         {@link org.eclipse.draw2dl.Viewport} up to the root. Returns an empty list in case
	 *         leaf or root {@link org.eclipse.draw2dl.Viewport} are null or in case the root
	 *         viewport is not an ancestor of the leaf {@link org.eclipse.draw2dl.Viewport}.
	 */
	public static List getViewportsPath(final org.eclipse.draw2dl.Viewport leafViewport,
			final org.eclipse.draw2dl.Viewport rootViewport) {
		return getViewportsPath(leafViewport, rootViewport, true);
	}

	/**
	 * Returns a list containing the provided leaf {@link org.eclipse.draw2dl.Viewport} as the first
	 * element, and all its enclosing {@link org.eclipse.draw2dl.Viewport}s up to the root
	 * {@link org.eclipse.draw2dl.Viewport}. The root {@link org.eclipse.draw2dl.Viewport} forms the last element of the
	 * list, in case includeRootViewport is set to true, otherwise the viewport
	 * directly nested below the root viewport will be the last in the list.
	 * 
	 * @param leafViewport
	 *            The {@link org.eclipse.draw2dl.Viewport}, whose parent hierarchy is processed.
	 * @param rootViewport
	 *            an ancestor of the given leafViewport, which marks the end
	 *            point of the hierarchy to be processed.
	 * @param includeRootViewport
	 *            whether the provided rootViewport should be included in the
	 *            list of returned viewports (as the last one) or not.
	 * @return A list of {@link org.eclipse.draw2dl.Viewport}s containing the leaf {@link org.eclipse.draw2dl.Viewport}
	 *         as the first element, the root {@link org.eclipse.draw2dl.Viewport} as the last and
	 *         in between all enclosing {@link org.eclipse.draw2dl.Viewport}s of the leaf
	 *         {@link org.eclipse.draw2dl.Viewport} up to the root. Returns an empty list in case
	 *         leaf or root {@link org.eclipse.draw2dl.Viewport} are null or in case the root
	 *         viewport is not an ancestor of the leaf {@link org.eclipse.draw2dl.Viewport}.
	 */
	public static List getViewportsPath(final org.eclipse.draw2dl.Viewport leafViewport,
                                        final org.eclipse.draw2dl.Viewport rootViewport, boolean includeRootViewport) {
		if (leafViewport == null || rootViewport == null) {
			return Collections.EMPTY_LIST;
		}

		// search all enclosing viewports of leaf viewport up to root viewport
		// (or until no enclosing viewport can be found)
		List nestedViewports = new ArrayList();
		org.eclipse.draw2dl.Viewport currentViewport = leafViewport;
		do {
			nestedViewports.add(currentViewport);
			currentViewport = ViewportUtilities
					.getNearestEnclosingViewport(currentViewport);
		} while (currentViewport != null && currentViewport != rootViewport);

		// check if root viewport is an ancestor of the given leaf viewport
		if (currentViewport != null) {
			if (includeRootViewport) {
				nestedViewports.add(currentViewport);
			}
			return nestedViewports;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Returns the nearest common enclosing {@link org.eclipse.draw2dl.Viewport} for two given
	 * {@link Figure}s.
	 * 
	 * @param firstFigure
	 * @param secondFigure
	 * @return The nearest common {@link org.eclipse.draw2dl.Viewport} of the two given figures, or
	 *         null if no common enclosing {@link org.eclipse.draw2dl.Viewport} could be found.
	 */
	public static org.eclipse.draw2dl.Viewport getNearestCommonViewport(org.eclipse.draw2dl.IFigure firstFigure,
                                                                        org.eclipse.draw2dl.IFigure secondFigure) {
		return getNearestViewport(FigureUtilities.findCommonAncestor(
				firstFigure, secondFigure));
	}

	/**
	 * Returns the upper most enclosing {@link org.eclipse.draw2dl.Viewport} for the given
	 * {@link org.eclipse.draw2dl.IFigure}.
	 * 
	 * @param figure
	 * @return The upper most enclosing {@link org.eclipse.draw2dl.Viewport} or null if there is no
	 *         enclosing {@link org.eclipse.draw2dl.Viewport} for the given {@link org.eclipse.draw2dl.IFigure},
	 */
	public static org.eclipse.draw2dl.Viewport getRootViewport(final org.eclipse.draw2dl.IFigure figure) {
		org.eclipse.draw2dl.Viewport currentViewport = getNearestViewport(figure);
		while (getNearestEnclosingViewport(currentViewport) != null) {
			currentViewport = getNearestEnclosingViewport(currentViewport);
		}
		return currentViewport;
	}

	/**
	 * Returns the given figure in case it is a {@link org.eclipse.draw2dl.Viewport} itself,
	 * otherwise its nearest enclosing {@link org.eclipse.draw2dl.Viewport}.
	 * 
	 * @param figure
	 * @return The given figure in case it is a {@link org.eclipse.draw2dl.Viewport} itself,
	 *         otherwise the nearest enclosing {@link org.eclipse.draw2dl.Viewport} or null if there
	 *         is no nearest enclosing {@link org.eclipse.draw2dl.Viewport}.
	 */
	public static org.eclipse.draw2dl.Viewport getNearestViewport(final org.eclipse.draw2dl.IFigure figure) {
		if (figure == null) {
			return null;
		}
		if (figure instanceof org.eclipse.draw2dl.Viewport) {
			return (org.eclipse.draw2dl.Viewport) figure;
		} else {
			return getNearestEnclosingViewport(figure);
		}
	}

	/**
	 * Returns the nearest enclosing {@link org.eclipse.draw2dl.Viewport} of a given {@link org.eclipse.draw2dl.IFigure}
	 * by walking up the figure's hierarchy.
	 * 
	 * @param figure
	 * @return The nearest enclosing {@link org.eclipse.draw2dl.Viewport} of the given figure, or
	 *         null if none could be found.
	 */
	public static org.eclipse.draw2dl.Viewport getNearestEnclosingViewport(final org.eclipse.draw2dl.IFigure figure) {
		if (figure == null) {
			return null;
		}
		org.eclipse.draw2dl.Viewport viewport = null;
		IFigure currentFigure = figure;
		while (currentFigure.getParent() != null) {
			if (currentFigure.getParent() instanceof org.eclipse.draw2dl.Viewport) {
				viewport = (Viewport) currentFigure.getParent();
				break;
			}
			currentFigure = currentFigure.getParent();
		}
		return viewport;
	}
}