package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.gef.requests.CreateRequest.Factory;

import org.eclipse.gef.examples.logicdesigner.model.LogicElementFactory;

import org.eclipse.ui.IEditorPart;

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
protected Factory getFactory(Object template) {
	return new LogicElementFactory((String)template);
}

}
