/*
 * Created on Oct 24, 2003
 */
package org.eclipse.gef.examples.logicdesigner.rulers;

import org.eclipse.gef.*;

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
