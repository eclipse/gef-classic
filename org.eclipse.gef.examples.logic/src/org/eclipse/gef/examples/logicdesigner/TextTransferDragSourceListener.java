package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.ui.palette.EntryEditPart;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public class TextTransferDragSourceListener
	extends AbstractTransferDragSourceListener
{

public TextTransferDragSourceListener(EditPartViewer viewer) {
	super(viewer, TextTransfer.getInstance());
}

public TextTransferDragSourceListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

public void dragSetData(DragSourceEvent event) {
	EntryEditPart part = (EntryEditPart)getViewer().getSelectedEditParts().get(0);
	PaletteEntry entry = (PaletteEntry)part.getModel();
	event.data = entry.getLabel();
}

}
