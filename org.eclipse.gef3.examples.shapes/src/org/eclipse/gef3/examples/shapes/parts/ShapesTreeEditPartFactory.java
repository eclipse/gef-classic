/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
�* All rights reserved. This program and the accompanying materials
�* are made available under the terms of the Eclipse Public License v1.0
�* which accompanies this distribution, and is available at
�* http://www.eclipse.org/legal/epl-v10.html
�*
�* Contributors:
�*����Elias Volanakis - initial API and implementation
�*******************************************************************************/
package org.eclipse.gef3.examples.shapes.parts;

import org.eclipse.gef3.EditPart;
import org.eclipse.gef3.EditPartFactory;

import org.eclipse.gef3.examples.shapes.model.Shape;
import org.eclipse.gef3.examples.shapes.model.ShapesDiagram;

/**
 * Factory that maps model elements to TreeEditParts. TreeEditParts are used in
 * the outline view of the ShapesEditor.
 * 
 * @author Elias Volanakis
 */
public class ShapesTreeEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef3.EditPartFactory#createEditPart(org.eclipse.gef3.EditPart,
	 * java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Shape) {
			return new ShapeTreeEditPart((Shape) model);
		}
		if (model instanceof ShapesDiagram) {
			return new DiagramTreeEditPart((ShapesDiagram) model);
		}
		return null; // will not show an entry for the corresponding model
						// instance
	}

}
