/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.rulers;

import org.eclipse.gef.*;

import org.eclipse.gef.examples.logicdesigner.model.*;

/**
 * @author Pratik Shah
 */
public class RulerEditPartFactory 
	implements EditPartFactory 
{
	
private GraphicalViewer diagramViewer;
	
public RulerEditPartFactory(GraphicalViewer primaryViewer) {
	diagramViewer = primaryViewer;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
 */
public EditPart createEditPart(EditPart parentEditPart, Object model) {
	EditPart part = null;
	if (model instanceof Guide) {
		part = createGuideEditPart(parentEditPart, model);
	} else if (model instanceof Ruler) {
		part = createRulerEditPart(parentEditPart, model);
	}
	return part;
}

/**
 * @param parentEditPart
 * @param model
 * @return
 */
private EditPart createGuideEditPart(EditPart parentEditPart, Object model) {
	return new GuideEditPart((Guide)model, diagramViewer);
}

/**
 * @param parentEditPart
 * @param model
 * @return
 */
private EditPart createRulerEditPart(EditPart parentEditPart, Object model) {
	return new RulerEditPart((Ruler)model, diagramViewer);
}

}
