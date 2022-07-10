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
package org.eclipse.gef.util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

/**
 * Utility class comprising functions related to {@link EditPart}s
 * 
 * @author Alexander Nyssen
 * @author Philip Ritzkopf
 * 
 * @since 3.6
 * 
 */
public final class EditPartUtilities {

	private EditPartUtilities() {
		// provides only static utility functions and should not be accessed in
		// any other way than static.
	}

	/**
	 * Returns the transitive child edit part set of the given parent
	 * <code>GraphicalEditPart</code>.
	 * 
	 * @param parentEditPart
	 *            the parent graphical edit part for which to retrieve the
	 *            transitive child edit part set.
	 * 
	 * @return the transitive child edit part set
	 */
	public static LinkedHashSet<GraphicalEditPart> getAllChildren(
			GraphicalEditPart parentEditPart) {
		LinkedHashSet<GraphicalEditPart> transitiveChildren = new LinkedHashSet<>();
		List<GraphicalEditPart> children = parentEditPart.getChildren();
		transitiveChildren.addAll(children);
		children.forEach(
				child -> transitiveChildren.addAll(getAllChildren(child)));
		return transitiveChildren;
	}

	/**
	 * Returns the transitive nested connection edit parts.
	 * 
	 * @param graphicalEditPart
	 *            the graphical edit part
	 * 
	 * @return the transitive nested connection edit parts
	 */
	public static HashSet getAllNestedConnectionEditParts(
			GraphicalEditPart graphicalEditPart) {
		HashSet transitiveConnections = new HashSet();
		getAllChildren(graphicalEditPart).forEach(child -> {
			transitiveConnections.addAll(child.getSourceConnections());
			transitiveConnections.addAll(child.getTargetConnections());
		});
		return transitiveConnections;
	}

	/**
	 * Returns the set of <code>ConnectionEditPart</code>s that are linked to
	 * the child edit parts of the given <code>GraphicalEditPart</code>.
	 * 
	 * @param graphicalEditPart
	 *            the graphical edit part
	 * 
	 * @return the set of child <code>ConnectionEditPart</code>s for the given
	 *         <code>GraphicalEditPart</code>
	 */
	public static HashSet getNestedConnectionEditParts(
			GraphicalEditPart graphicalEditPart) {
		HashSet edges = new HashSet();
		for (EditPart child : graphicalEditPart.getChildren()) {
			if (child instanceof GraphicalEditPart) {
				GraphicalEditPart childEditPart = (GraphicalEditPart) child;
				edges.addAll(childEditPart.getSourceConnections());
				edges.addAll(childEditPart.getTargetConnections());
			}
		}
		return edges;
	}
}
