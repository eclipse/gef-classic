package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.ui.IEditorPart;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.logicdesigner.model.LogicElementFactory;

/**
 * @author Eric Bordeau
 */
public class LogicPasteTemplateAction extends PasteTemplateAction {

/**
 * Constructor for LogicPasteTemplateAction.
 * @param editor
 */
public LogicPasteTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * @see org.eclipse.gef.ui.actions.PasteTemplateAction#getFactory(java.lang.Object)
 */
protected CreationFactory getFactory(Object template) {
	return new LogicElementFactory((String)template);
}

/**
 * 
 * @see org.eclipse.gef.examples.logicdesigner.actions.PasteTemplateAction#getPasteLocation()
 */
protected Point getPasteLocation() {
	return new Point(10, 10);
}

}
