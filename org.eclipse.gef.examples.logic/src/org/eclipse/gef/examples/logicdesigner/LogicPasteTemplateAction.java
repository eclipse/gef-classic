package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.gef.requests.CreateRequest.Factory;
import org.eclipse.ui.IEditorPart;

/**
 * 
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
	return new LogicTemplateFactory((String)template);
}

}
