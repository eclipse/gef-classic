package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IEditorPart;


import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;

/**
 * @author danlee
 */
public class ZoomInAction extends EditorPartAction {

/**
 * Constructor for ZoomInAction.
 * @param editor
 */
public ZoomInAction(IEditorPart editor) {
	super(editor);
	setText(GEFMessages.ZoomIn_Label);
	setId(GEFActionConstants.ZOOM_IN);
	setToolTipText(GEFMessages.ZoomIn_Tooltip);
	setImageDescriptor(InternalImages.DESC_ZOOM_IN);
	setHoverImageDescriptor(getImageDescriptor());
	setActionDefinitionId(GEFActionConstants.ZOOM_IN);
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
	manager.zoomIn();
}

}