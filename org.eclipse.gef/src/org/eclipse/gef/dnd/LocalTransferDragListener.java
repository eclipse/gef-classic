package org.eclipse.gef.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;

public class LocalTransferDragListener extends AbstractTransferDragSourceListener {

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
	LocalTransfer.getInstance().setObject(getViewer().getSelectedEditParts());
}

public void dragFinished(DragSourceEvent event) {
	LocalTransfer.getInstance().setObject(null);
	LocalTransfer.getInstance().setViewer(null);
}

}
