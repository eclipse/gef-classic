package org.eclipse.gef.ui.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;

class TreeViewerTransferDragListener extends AbstractTransferDragSourceListener {

private List modelSelection;

public TreeViewerTransferDragListener(EditPartViewer viewer) {
	super(viewer, TreeViewerTransfer.getInstance());
}

/**
 * @deprecated */
public TreeViewerTransferDragListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

public void dragSetData(DragSourceEvent event) {
	event.data = getViewer().getSelectedEditParts();
}

public void dragStart(DragSourceEvent event) {
	TreeViewerTransfer.getInstance().setViewer(getViewer());
	List selection = getViewer().getSelectedEditParts();
	TreeViewerTransfer.getInstance().setObject(selection);
	saveModelSelection(selection);
}

public void dragFinished(DragSourceEvent event) {
	TreeViewerTransfer.getInstance().setObject(null);
	TreeViewerTransfer.getInstance().setViewer(null);
	revertModelSelection();
}

protected void revertModelSelection() {
	List list = new ArrayList();
	for (int i = 0; i < modelSelection.size(); i++) {
		list.add(getViewer().getEditPartRegistry().get(modelSelection.get(i)));
	}
	getViewer().setSelection(new StructuredSelection(list));
}

protected void saveModelSelection(List editPartSelection) {
	modelSelection = new ArrayList();
	for (int i = 0; i < editPartSelection.size(); i++) {
		EditPart editpart = (EditPart)editPartSelection.get(i);
		modelSelection.add(editpart.getModel());
	}
}

}
