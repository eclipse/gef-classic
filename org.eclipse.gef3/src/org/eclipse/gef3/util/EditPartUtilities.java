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
package org.eclipse.gef3.util;

import java.util.*;

import org.eclipse.gef3.ConnectionEditPart;
import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.GraphicalEditPart;

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
	public static Set<EditPart> getAllChildren(GraphicalEditPart parentEditPart) {
		Set<EditPart> transitiveChildren = new LinkedHashSet<>();
		List<EditPart> children = parentEditPart.getChildren();
		transitiveChildren.addAll(children);
		for (Iterator<EditPart> iterator = children.iterator(); iterator.hasNext();) {
			GraphicalEditPart child = (GraphicalEditPart) iterator.next();
			transitiveChildren.addAll(getAllChildren(child));
		}
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
	public static Set<ConnectionEditPart> getAllNestedConnectionEditParts(
			GraphicalEditPart graphicalEditPart) {
		Set<ConnectionEditPart> transitiveConnections = new HashSet<>();
		Set<EditPart> transitiveChildren = getAllChildren(graphicalEditPart);
		for (Iterator<EditPart> iterator = transitiveChildren.iterator(); iterator
				.hasNext();) {
			EditPart child = iterator.next();
			if (child instanceof GraphicalEditPart) {
				GraphicalEditPart gep = (GraphicalEditPart)child;
				transitiveConnections.addAll(gep.getSourceConnections());
				transitiveConnections.addAll(gep.getTargetConnections());
			}
		}
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
	public static Set<ConnectionEditPart> getNestedConnectionEditParts(
			GraphicalEditPart graphicalEditPart) {
		Set<ConnectionEditPart> edges = new HashSet<>();
		List<EditPart> children = graphicalEditPart.getChildren();
		for (Iterator<EditPart> iterator = children.iterator(); iterator.hasNext();) {
			EditPart child = iterator.next();
			if (child instanceof GraphicalEditPart) {
				GraphicalEditPart childEditPart = (GraphicalEditPart) child;
				edges.addAll(childEditPart.getSourceConnections());
				edges.addAll(childEditPart.getTargetConnections());
			}
		}
		return edges;
	}
}
