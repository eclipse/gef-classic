package org.eclipse.gef.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.Disposable;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;

/**
 * @author hudsonr
 */
abstract class ZoomAction
	extends Action
	implements ZoomListener, Disposable
{

protected ZoomManager zoomManager;

/**
 * @param zoomManager
 */
public ZoomAction(String text, ImageDescriptor image, ZoomManager zoomManager) {
	super(text, image);
	this.zoomManager = zoomManager;
	zoomManager.addZoomListener(this);
}

/**
 * @see org.eclipse.gef.Disposable#dispose()
 */
public void dispose() {
	zoomManager.removeZoomListener(this);
}

}
