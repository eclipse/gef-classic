package org.eclipse.gef.dnd;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.palette.TemplateEntry;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

/**
 * This class will allow for dragging template objects from the 
 * {@link org.eclipse.gef.ui.palette.PaletteViewer} to the 
 * {@link org.eclipse.gef.GraphicalViewer}.
 * 
 * @author Eric Bordeau
 */
public class TemplateTransferDragSourceListener extends AbstractTransferDragSourceListener {

public TemplateTransferDragSourceListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

/**
 * Cancel the drag if the selected item isn't a PaletteTemplateEntry.
 */
public void dragStart(DragSourceEvent event) {
	Object template = getTemplate();
	if (template == null)
		event.doit = false;
	TemplateTransfer.getInstance().setTemplate(template);
}

/**
 * @see org.eclipse.gef.dnd.AbstractTransferDragSourceListener#dragFinished(DragSourceEvent)
 */
public void dragFinished(DragSourceEvent event) {
	TemplateTransfer.getInstance().setTemplate(null);
}

/**
 * Get the Template from the selected PaletteTemplateEntry and set
 * it as the event data to be dropped.
 */
public void dragSetData(DragSourceEvent event) {
	event.data = getTemplate();
}

protected Object getTemplate(){
	List selection = getViewer().getSelectedEditParts();
	if (selection.size() == 1) {
		EditPart editpart = (EditPart)getViewer().getSelectedEditParts().get(0);
		if (editpart.getModel() instanceof TemplateEntry)
			return ((TemplateEntry)editpart.getModel()).getTemplate();
	}
	return null;
}

}
