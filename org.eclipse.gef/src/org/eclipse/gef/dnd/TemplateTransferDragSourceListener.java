package org.eclipse.gef.dnd;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.palette.TemplateEntry;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

/**
 * Allows a single {@link TemplateEntry TemplateEntry} to be dragged from an
 * EditPartViewer. The TemplateEntry's <i>template</i> object is the data that is being
 * transfered to the <code>DropTarget</code>.
 * @since 2.1
 * @author Eric Bordeau
 */
public class TemplateTransferDragSourceListener
	extends AbstractTransferDragSourceListener
{

/**
 * @deprecated
 * @param viewer viewer
 * @param xfer xfer
 */
public TemplateTransferDragSourceListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

/**
 * Constructs a new listener for the specified EditPartViewer. The provided Viewer should
 * be one that is displaying a Palette. The TemplateTransferDragSourceListener will only
 * be enabled when a single EditPart is selected, and the EditPart's model is a
 * {@link TemplateEntry}.
 * @param viewer the EditPartViewer that is the drag source
 */
public TemplateTransferDragSourceListener(EditPartViewer viewer) {
	super(viewer, TemplateTransfer.getInstance());
}

/**
 * @see org.eclipse.gef.dnd.AbstractTransferDragSourceListener#dragFinished(DragSourceEvent)
 */
public void dragFinished(DragSourceEvent event) {
	TemplateTransfer.getInstance().setTemplate(null);
}

/**
 * Get the <i>template</i> from the selected {@link TemplateEntry} and sets it as the
 * event data to be dropped.
 * @param event the DragSourceEvent
 */
public void dragSetData(DragSourceEvent event) {
	event.data = getTemplate();
}

/**
 * Cancels the drag if the selected item does not represent a TemplateEntry.
 * @see DragSourceListener#dragStart(DragSourceEvent)
 */
public void dragStart(DragSourceEvent event) {
	Object template = getTemplate();
	if (template == null)
		event.doit = false;
	TemplateTransfer.getInstance().setTemplate(template);
}

/**
 * A helper method that returns <code>null</code> or the <i>template</i> Object from the
 * currently selected EditPart.
 * @return the template */
protected Object getTemplate() {
	List selection = getViewer().getSelectedEditParts();
	if (selection.size() == 1) {
		EditPart editpart = (EditPart)getViewer().getSelectedEditParts().get(0);
		if (editpart.getModel() instanceof TemplateEntry)
			return ((TemplateEntry)editpart.getModel()).getTemplate();
	}
	return null;
}

}
