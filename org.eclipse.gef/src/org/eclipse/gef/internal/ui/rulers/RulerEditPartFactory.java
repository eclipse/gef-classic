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
package org.eclipse.gef.internal.ui.rulers;

import org.eclipse.gef.*;
import org.eclipse.gef.ui.parts.RulerProvider;

/**
 * @author Pratik Shah
 */
public class RulerEditPartFactory 
	implements EditPartFactory 
{
	
protected GraphicalViewer diagramViewer;
	
public RulerEditPartFactory(GraphicalViewer primaryViewer) {
	diagramViewer = primaryViewer;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
 */
public EditPart createEditPart(EditPart parentEditPart, Object model) {
	// the model can be null when the contents of the root edit part are set to null
	EditPart part = null;
	if (isRuler(model)) {
		part = createRulerEditPart(parentEditPart, model);
	} else if (model != null) {
		part = createGuideEditPart(parentEditPart, model);
	}
	return part;
}

protected EditPart createGuideEditPart(EditPart parentEditPart, Object model) {
	return new GuideEditPart(model);
}

protected EditPart createRulerEditPart(EditPart parentEditPart, Object model) {
	return new RulerEditPart(model);
}

protected Object getHorizontalRuler() {
	Object ruler = null;
	RulerProvider provider = 
			(RulerProvider)diagramViewer.getProperty(RulerProvider.HORIZONTAL);
	if (provider != null) {
		ruler = provider.getRuler();
	}
	return ruler;
}

protected Object getVerticalRuler() {
	Object ruler = null;
	RulerProvider provider = 
			(RulerProvider)diagramViewer.getProperty(RulerProvider.VERTICAL);
	if (provider != null) {
		ruler = provider.getRuler();
	}
	return ruler;
}

protected boolean isRuler(Object model) {
	boolean result = false;
	if (model != null) {
		result = model == getHorizontalRuler() || model == getVerticalRuler();
	}
	return result;
}

}