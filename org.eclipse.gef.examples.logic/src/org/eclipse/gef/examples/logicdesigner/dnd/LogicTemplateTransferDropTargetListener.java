package org.eclipse.gef.examples.logicdesigner.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.logicdesigner.model.LogicElementFactory;

/**
 * This listener handles logic templates that are dropped onto
 * the logic editor.
 * 
 * @author Eric Bordeau
 */
public class LogicTemplateTransferDropTargetListener
	extends TemplateTransferDropTargetListener 
{

public LogicTemplateTransferDropTargetListener(EditPartViewer viewer) {
	super(viewer);
}

protected CreationFactory getFactory(Object template) {
	if (template instanceof String)
		return new LogicElementFactory((String)template);
	return null;
}

}
