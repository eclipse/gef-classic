package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IEditorPart;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.ScalableFreeformRootEditPart;

import org.eclipse.draw2d.ZoomManager;

/**
 * @author danlee
 */
public class ZoomOutAction extends EditorPartAction {

/**
 * Constructor for ZoomOutAction.
 * @param editor
 */
public ZoomOutAction(IEditorPart editor) {
	super(editor);
	setText(GEFMessages.ZoomOut_Label);
	setId(GEFActionConstants.ZOOM_OUT);
	setToolTipText(GEFMessages.ZoomOut_Tooltip);
	setImageDescriptor(InternalImages.DESC_ZOOM_OUT);
	setHoverImageDescriptor(getImageDescriptor());
	setActionDefinitionId(GEFActionConstants.ZOOM_OUT);
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return true;
}

/**
 * @see org.eclipse.jface.action.IAction#run()
 */
public void run() {
	GraphicalViewer viewer = (GraphicalViewer)
								getEditorPart().getAdapter(GraphicalViewer.class);
	if (viewer == null)
		return;
	ZoomManager manager = ((ScalableFreeformRootEditPart)
							viewer.getRootEditPart()).getZoomManager();
	manager.zoomOut();
}

}