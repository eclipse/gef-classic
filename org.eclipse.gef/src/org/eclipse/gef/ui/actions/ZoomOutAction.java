package org.eclipse.gef.ui.actions;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

/**
 * @author danlee
 */
public class ZoomOutAction extends ZoomAction {

/**
 * Constructor for ZoomOutAction.
 * @param editor
 */
public ZoomOutAction(ZoomManager zoomManager) {
	super(GEFMessages.ZoomOut_Label, InternalImages.DESC_ZOOM_OUT, zoomManager);
	setId(GEFActionConstants.ZOOM_OUT);
	setToolTipText(GEFMessages.ZoomOut_Tooltip);
}

/**
 * @see org.eclipse.jface.action.IAction#run()
 */
public void run() {
	zoomManager.zoomOut();
}

/**
 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
 */
public void zoomChanged(double zoom) {
	setEnabled(zoomManager.canZoomOut());
}


}