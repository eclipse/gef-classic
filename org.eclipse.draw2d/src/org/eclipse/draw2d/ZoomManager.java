package org.eclipse.draw2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class will manage the zoom for a GraphicalEditor.  Objects interested in zoom
 * changes should register as a ZoomListener with this class.
 * 
 * @author Eric Bordeau
 */
public class ZoomManager {

private List listeners = new ArrayList();
private double zoom = 1.0;
private double[] zoomSettings = { .25, .5, .75, 1.0, 1.25, 1.50, 1.75, 2.0, 2.25, 2.50, 
									2.75, 3.00 };
private int settingPointer = 3;
private ScalableFreeformLayeredPane pane;
private FreeformViewport viewport;

public ZoomManager(ScalableFreeformLayeredPane pane, FreeformViewport viewport) {
	this.pane = pane;
	this.viewport = viewport;
}	

/**
 * Adds the given ZoomListener to this ZoomManager's list of listeners.
 * @param listener the ZoomListener to be added
 */
public void addZoomListener(ZoomListener listener) {
	listeners.add(listener);
}

/**
 * Notifies listeners that the zoom level has changed.
 */
protected void fireZoomChanged() {
	Iterator iter = listeners.iterator();
	while (iter.hasNext())
		((ZoomListener)iter.next()).zoomChanged(zoom);
}

/**
 * Returns the current zoom level.
 * @return double the zoom level
 */
public double getZoom() {
	return zoom;
}

/**
 * Removes the given ZoomListener from this ZoomManager's list of listeners.
 * @param listener the ZoomListener to be removed
 */
public void removeZoomListener(ZoomListener listener) {
	listeners.remove(listener);
}

/**
 * Sets the zoom level to the given value.
 * @param zoom the new zoom level
 */
public void setZoom(double zoom) {
	if (this.zoom == zoom)
		return;
	this.zoom = zoom;
	fireZoomChanged();
}

public void zoomIn() {
	if( settingPointer < zoomSettings.length-1 ) {
		settingPointer++;
		zoom = zoomSettings[settingPointer];
		pane.setZoom(zoom);
		viewport.setViewLocation(500,500);
		fireZoomChanged();
	}	
}	

public void zoomOut() {
	if( settingPointer > 0 ) {
		settingPointer--;
		zoom = zoomSettings[settingPointer];
		pane.setZoom(zoom);
		fireZoomChanged();
	}	
}

}