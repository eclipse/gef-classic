package org.eclipse.draw2d;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class will manage the zoom for a GraphicalEditor.  Objects interested in zoom
 * changes should register as a ZoomListener with this class.
 * 
 * @author Eric Bordeau
 */
public class ZoomManager {

private List listeners = new ArrayList();

private double maxZoom = 16.0;
private double minZoom = .16;
private double multiplier = 1.0;
private ScalableFreeformLayeredPane pane;
private Viewport viewport;
private double zoom = 1.0;
private double[] zoomLevels = { .16, .25, .33, .5, .66, 1.0, 2.0, 3.0, 4.0, 5.0,
								  	6.0, 7.0, 8.0, 12.0, 16.0 };

DecimalFormat format = new DecimalFormat("####%"); //$NON-NLS-1$

/**
 * Creates a new ZoomManager
 * @param pane The ScalableFreeformLayeredPane associated with this ZoomManager
 * @param viewport The Viewport assoicated with this viewport
 */
public ZoomManager(ScalableFreeformLayeredPane pane, Viewport viewport) {
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

private Point calculateViewLocation(Rectangle zoomRect, double ratio) {
	Point viewLocation = new Point();
	viewLocation.x = (int)(zoomRect.x / ratio);
	viewLocation.y = (int)(zoomRect.y / ratio);
	return viewLocation;
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
 * Returns the maxZoom.
 * @return double
 */
public double getMaxZoom() {
	return maxZoom;
}

/**
 * Returns the minZoom.
 * @return double
 */
public double getMinZoom() {
	return minZoom;
}

/**
 * Returns the mutltiplier. This value is used to use zoom levels internally that are
 * proportionally different than those displayed to the user. e.g. with a multiplier value
 * of 2.0, the zoom level 1.0 will be displayed as "200%".
 * @return double The multiplier
 */
public double getMultiplier() {
	return multiplier;
}

/**
 * Returns the zoom level that is one level higher than the current level. If zoom level
 * is at maximum, returns the maximum.
 * 
 * @return double The next zoom level
 */
public double getNextZoomLevel() {
	double nextZoom = zoom;
	boolean found = false;
	for (int i = 0; !found; i++) {
		if (zoomLevels[i] > zoom || i == zoomLevels.length - 1) {
			found = true;
			nextZoom = zoomLevels[i];
		}	
	}
	return nextZoom;
}

/**
 * Returns the pane.
 * @return ScalableFreeformLayeredPane
 */
public ScalableFreeformLayeredPane getPane() {
	return pane;
}

/**
 * Returns the zoom level that is one level higher than the current level. If zoom level
 * is at maximum, returns the maximum.
 * 
 * @return double The previous zoom level
 */
public double getPreviousZoomLevel() {
	double prevZoom = zoom;
	boolean found = false;
	for (int i = zoomLevels.length - 1; !found; i--) {
		if (zoomLevels[i] < zoom || i == 0) {
			found = true;
			prevZoom = zoomLevels[i];
		}	
	}
	return prevZoom;
}

/**
 * Returns the viewport.
 * @return Viewport
 */
public Viewport getViewport() {
	return viewport;
}

/**
 * Returns the current zoom level.
 * @return double the zoom level
 */
public double getZoom() {
	return zoom;
}

/**
 * Returns the current zoom level as a percentage formatted String
 * @return String The current zoom level as a String
 */
public String getZoomAsText() {
	String newItem = format.format(zoom * multiplier);
	return newItem;
}	

/**
 * Returns the zoomLevels.
 * @return double[]
 */
public double[] getZoomLevels() {
	return zoomLevels;
}

/**
 * Returns the list of zoom levels as Strings in percent notation
 * @return List The list of zoom levels */
public String[] getZoomLevelsAsText() {
	String[] zoomLevelStrings = new String[zoomLevels.length];
	for (int i = 0; i < zoomLevels.length; i++) {
		zoomLevelStrings[i] = format.format(zoomLevels[i]);
	}
	return zoomLevelStrings;
}

/**
 * Removes the given ZoomListener from this ZoomManager's list of listeners.
 * @param listener the ZoomListener to be removed
 */
public void removeZoomListener(ZoomListener listener) {
	listeners.remove(listener);
}

/**
 * Sets the maxZoom.
 * @param maxZoom The maxZoom to set
 */
public void setMaxZoom(double maxZoom) {
	this.maxZoom = maxZoom;
}

/**
 * Sets the minZoom.
 * @param minZoom The minZoom to set
 */
public void setMinZoom(double minZoom) {
	this.minZoom = minZoom;
}

/**
 * Sets the mutltiplier. This value is used to use zoom levels internally that are
 * proportionally different than those displayed to the user. e.g. with a multiplier value
 * of 2.0, the zoom level 1.0 will be displayed as "200%".
 * 
 * @param multiplier The mutltiplier to set
 */
public void setMultiplier(double multiplier) {
	this.multiplier = multiplier;
}

/**
 * Sets the Viewport's view associated with this ZoomManager to the passed Point
 * @param p The new location for the Viewport's view.
 */
public void setViewLocation(Point p) {
	viewport.setViewLocation(p.x, p.y);
	
}

/**
 * Sets the zoom level to the given value.
 * @param zoom the new zoom level
 * @return true if the zoom was applied, false if not
 */
public boolean setZoom(double zoom) {
	if (this.zoom == zoom)
		return false;
	if (zoom >= minZoom && zoom <= maxZoom) {
		this.zoom = zoom;
		pane.setZoom(zoom);
		fireZoomChanged();
		return true;
	}
	return false;		
}

/**
 * Sets zoom to the passed string. The string must be composed of numeric characters only
 * with the exception of a decimal point and a '%' as the last character.
 * @param zoomString The new zoom level */
public void setZoom(String zoomString) {		
	try {
		//Trim off the '%'
		if (zoomString.charAt(zoomString.length() - 1) == '%')
			zoomString = zoomString.substring(0, zoomString.length() - 1);
		double newZoom = Double.parseDouble(zoomString) / 100;
		setZoom(newZoom / multiplier);
	} catch (Exception e) {
		Display.getCurrent().beep();
	}
}

/**
 * Sets the zoomLevels.
 * @param zoomLevels The zoomLevels to set
 */
public void setZoomLevels(double[] zoomLevels) {
	this.zoomLevels = zoomLevels;
}

/**
 * Sets the zoom level to be one level higher
 */
public void zoomIn() {
	setZoom(getNextZoomLevel());
}

/**
 * Performs an animated zoom-out from the region defined by rect. The new zoom level will
 * be one level lower than the current zoom level, where the zoom levels are defined in
 * zoomLevels.
 * @param rect The region to zoom-out from */
public void zoomOutFrom(Rectangle rect) {
	performAnimatedZoom(rect, false, 10);
}

/**
 * Performs an animated zoom-in to the region defined by rect. The new zoom level will be
 * one level higher than the current zoom level, where the zoom levels are defined in
 * zoomLevels.
 * @param rect The region to zoom-in to */
public void zoomInTo(Rectangle rect) {
	performAnimatedZoom(rect, true, 10);
}

private void performAnimatedZoom(Rectangle rect, boolean zoomIn, int iterationCount) {
	double finalRatio;
	double zoomIncrement;
	
	if (zoomIn) {
		finalRatio = zoom / getNextZoomLevel();
		zoomIncrement = (getNextZoomLevel() - zoom) / iterationCount;
	} else {
		finalRatio = zoom / getPreviousZoomLevel();
		zoomIncrement = (getPreviousZoomLevel() - zoom) / iterationCount;
	}
	
	getPane().translateToRelative(rect);
	Point originalViewLocation = getViewport().getViewLocation();
	Point finalViewLocation = calculateViewLocation(rect, finalRatio);
	
	double xIncrement = (double)(finalViewLocation.x - originalViewLocation.x) 
									/ iterationCount;
	double yIncrement = (double)(finalViewLocation.y - originalViewLocation.y) 
									/ iterationCount;
	
	double originalZoom = zoom;
	double currentRatio;
	Point currentViewLocation = new Point();
	for (int i = 1; i < iterationCount; i++) {
		currentViewLocation.x = (int)((double)originalViewLocation.x 
										+ (double)(xIncrement * i));
		currentViewLocation.y = (int)((double)originalViewLocation.y 
										+ (double)(yIncrement * i));
		setZoom(originalZoom + zoomIncrement * i);
		getViewport().validate();
		setViewLocation(currentViewLocation);
		currentRatio = originalZoom / (originalZoom + zoomIncrement * i);
		getViewport().getUpdateManager().performUpdate();
	}
	
	if (zoomIn)
		setZoom(getNextZoomLevel());
	else
		setZoom(getPreviousZoomLevel());
	
	getViewport().validate();
	setViewLocation(finalViewLocation);	
}

/**
 * Sets the zoom level to be one level lower
 */
public void zoomOut() {
	setZoom(getPreviousZoomLevel());
}

}