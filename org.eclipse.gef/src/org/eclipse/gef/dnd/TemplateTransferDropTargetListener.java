package org.eclipse.gef.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

/**
 * This listener handles object creation via drag and drop.
 * 
 * @author Eric Bordeau
 */
abstract public class TemplateTransferDropTargetListener
	extends AbstractTransferDropTargetListener 
{

public TemplateTransferDropTargetListener(EditPartViewer viewer) {
	super(viewer, TemplateTransfer.getInstance());
}

protected Request createTargetRequest() {
	//Look at the data on templatetransfer.
	//Create factory
	CreateRequest request = new CreateRequest();
	request.setFactory(getFactory(TemplateTransfer.getInstance().getTemplate()));
	return request;
}

protected CreateRequest getCreateRequest() {
	return ((CreateRequest)getTargetRequest());
}

abstract protected CreateRequest.Factory getFactory(Object template);

protected boolean handleDragEnter() {
	getCurrentEvent().detail = DND.DROP_COPY;
	return true;
}

protected void updateTargetRequest() {
	CreateRequest request = getCreateRequest();
	request.setLocation(getDropLocation());
}

}
