package org.eclipse.gef.dnd;

import org.eclipse.gef.*;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.DND;

/**
 * Performs a native Drop using the {@link TemplateTransfer}. The Drop is performed by
 * using a {@link CreateRequest} to obtain a <code>Command</code> from the targeted
 * <code>EditPart</code>.
 * <P>
 * This class is <code>abstract</code>. Subclasses are responsible for providing the
 * appropriate <code>Factory</code> object based on the template that is being dragged.
 * @since 2.1
 * @author Eric Bordeau
 */
public abstract class TemplateTransferDropTargetListener
	extends AbstractTransferDropTargetListener 
{

/**
 * Constructs a listener on the specified viewer.
 * @param viewer the EditPartViewer
 */
public TemplateTransferDropTargetListener(EditPartViewer viewer) {
	super(viewer, TemplateTransfer.getInstance());
}

/**
 *  * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest() */
protected Request createTargetRequest() {
	//Look at the data on templatetransfer.
	//Create factory
	CreateRequest request = new CreateRequest();
	request.setFactory(getFactory(TemplateTransfer.getInstance().getTemplate()));
	return request;
}

/**
 * A helper method that casts the target Request to a CreateRequest.
 * @return CreateRequest */
protected final CreateRequest getCreateRequest() {
	return ((CreateRequest)getTargetRequest());
}

/**
 * Returns the appropriate Factory object to be used for the specified template. This
 * Factory is used on the CreateRequest that is sent to the target EditPart.
 * @param template the template Object * @return a Factory */
protected abstract CreateRequest.Factory getFactory(Object template);

/**
 * The purpose of a template is to be copied. Therefore, the drop operation can't be
 * anything but <code>DND.DROP_COPY</code>.
 * @see AbstractTransferDropTargetListener#handleDragOperationChanged()
 */
protected void handleDragOperationChanged() {
	getCurrentEvent().detail = DND.DROP_COPY;
	super.handleDragOperationChanged();
}

/**
 * The purpose of a template is to be copied. Therefore, the Drop operation is set to
 * <code>DND.DROP_COPY</code> by default.
 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDragOver()
 */
protected void handleDragOver() {
	getCurrentEvent().detail = DND.DROP_COPY;
	getCurrentEvent().feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
	super.handleDragOver();
}

/**
 * Overridden to select the created object.
 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop() */
protected void handleDrop() {
	super.handleDrop();
	selectAddedObject();
}

private void selectAddedObject() {
	Object model = getCreateRequest().getNewObject();
	if (model == null)
		return;
	EditPartViewer viewer = getViewer();
	viewer.getControl().forceFocus();
	Object obj = viewer.getEditPartRegistry().get(model);
	if (obj instanceof EditPart) {
		EditPart editpart = (EditPart)obj;
		if (!editpart.isSelectable())
			return;
		//Force a layout first.
		getViewer().flush();
		viewer.select(editpart);
	}
}

/**
 * Assumes that the target request is a {@link CreateRequest}. 
 */
protected void updateTargetRequest() {
	CreateRequest request = getCreateRequest();
	request.setLocation(getDropLocation());
}

}
