package org.eclipse.gef.dnd;

import org.eclipse.gef.*;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.DND;

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

protected void handleDragEnter() {
	getCurrentEvent().detail = DND.DROP_COPY;
}

protected void handleDrop() {
	super.handleDrop();
	selectAddedObject();
}

private void selectAddedObject() {
	Object model = getCreateRequest().getNewObject();
	if (model == null)
		return;
	EditPartViewer viewer = getViewer();
	Object editpart = viewer.getEditPartRegistry().get(model);
	if (editpart instanceof EditPart){
		getViewer().flush();
		viewer.select((EditPart)editpart);
	}
}

protected void updateTargetRequest() {
	CreateRequest request = getCreateRequest();
	request.setLocation(getDropLocation());
}

}
