package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;

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

protected CreateRequest.Factory getFactory(Object template) {
	if (template instanceof String)
		return new LogicTemplateFactory((String)template);
	return null;
}

}
