package org.eclipse.gef.ui.actions;

import org.eclipse.draw2d.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.SharedImages;
import org.eclipse.ui.IEditorPart;

/**
 * Increments or decrements the zoom level on the current editor.
 * 
 * @author Eric Bordeau
 */
public class ZoomAction extends EditorPartAction {

public static final String ZOOM_IN = "$zoom in"; //$NON-NLS-1$
public static final String ZOOM_OUT = "$zoom out"; //$NON-NLS-1$

/**
 * Constructor for ZoomAction.
 * @param editor
 */
public ZoomAction(IEditorPart editor, boolean zoom_in) {
	super(editor);
	if (zoom_in) {
		setText(GEFMessages.ZoomAction_ZoomIn_ActionLabelText);
		setId(ZOOM_IN);
		setToolTipText(GEFMessages.ZoomAction_ZoomIn_ActionToolTipText);
		setImageDescriptor(SharedImages.DESC_ZOOM_IN);
	} else {
		setText(GEFMessages.ZoomAction_ZoomOut_ActionLabelText);
		setId(ZOOM_OUT);
		setToolTipText(GEFMessages.ZoomAction_ZoomOut_ActionToolTipText);
		setImageDescriptor(SharedImages.DESC_ZOOM_OUT);
	}
	setHoverImageDescriptor(getImageDescriptor());
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return true;
}

protected ZoomManager getZoomManager() {
	return (ZoomManager)getEditorPart().getAdapter(ZoomManager.class);
}

public void run() {
	ZoomManager manager = getZoomManager();
	double newZoom;
	if (ZOOM_IN.equals(getId()))
		newZoom = manager.getZoom() * 1.5;
	else 
		newZoom = manager.getZoom() / 1.5;
	manager.setZoom(newZoom);
}

}
