package org.eclipse.gef.ui.actions;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;

/**
 * @author danlee
 */
public class ZoomInAction extends ZoomAction {

/**
 * Constructor for ZoomInAction.
 * @param zoomManager the zoom manager
 */
public ZoomInAction(ZoomManager zoomManager) {
	super(GEFMessages.ZoomIn_Label, InternalImages.DESC_ZOOM_IN, zoomManager);
	setToolTipText(GEFMessages.ZoomIn_Tooltip);
	setId(GEFActionConstants.ZOOM_IN);
}

/**
 * @see org.eclipse.jface.action.IAction#run()
 */
public void run() {
	zoomManager.zoomIn();
}

/**
 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
 */
public void zoomChanged(double zoom) {
	setEnabled(zoomManager.canZoomIn());
}


}