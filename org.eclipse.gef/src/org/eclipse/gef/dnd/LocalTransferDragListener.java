package org.eclipse.gef.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.viewers.StructuredSelection;

public class LocalTransferDragListener extends AbstractTransferDragSourceListener {

private List modelSelection;

public LocalTransferDragListener(EditPartViewer viewer) {
	super(viewer, LocalTransfer.getInstance());
}

/**
 * @deprecated */
public LocalTransferDragListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

public void dragSetData(DragSourceEvent event) {
	event.data = getViewer().getSelectedEditParts();
}

public void dragStart(DragSourceEvent event) {
	LocalTransfer.getInstance().setViewer(getViewer());
	List selection = getViewer().getSelectedEditParts();
	LocalTransfer.getInstance().setObject(selection);
	saveModelSelection(selection);
}

public void dragFinished(DragSourceEvent event) {
	LocalTransfer.getInstance().setObject(null);
	LocalTransfer.getInstance().setViewer(null);
	revertModelSelection();
}

protected void revertModelSelection() {
	List list = new ArrayList();
	for (int i = 0; i < modelSelection.size(); i++) {
		list.add(getViewer().getEditPartRegistry().get(modelSelection.get(i)));
	}
	IStructuredSelection selection = new StructuredSelection(list);
	getViewer().setSelection(selection);
}

protected void saveModelSelection(List editPartSelection) {
	modelSelection = new ArrayList();
	for (int i = 0; i < editPartSelection.size(); i++) {
		EditPart editpart = (EditPart)editPartSelection.get(i);
		modelSelection.add(editpart.getModel());
	}
}

}
