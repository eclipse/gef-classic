package org.eclipse.draw2d.parts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

/**
 * A Thumbnail is a Figure that displays an image of its source Figure at a 
 * smaller size. The Thumbnail will maintain the aspect ratio of the source 
 * Figure.
 * 
 * @author Eric Bordeau */
public class Thumbnail extends Figure {

private IFigure sourceFigure;
private boolean isDirty;
private float scaleX;
private float scaleY;
private Dimension targetSize = new Dimension(0, 0);
private Image thumbnailImage;
private Dimension thumbnailImageSize;
private ThumbnailUpdater updater = new ThumbnailUpdater();

/**
 * This updates the Thumbnail by breaking the thumbnail {@link Image} into
 * several tiles and updating each tile individually.   */
class ThumbnailUpdater implements Runnable {
	protected int MAX_BUFFER_SIZE = 256;

	private boolean isRunning = false;
	private boolean isActive = true;
	private int hTiles, vTiles;
	private Dimension tileSize;
	private Image sourceBuffer;
	private Dimension sourceBufferSize;
	private GC sourceGC, thumbnailGC;
	private Graphics sourceGraphics;
	private int currentHTile, currentVTile;
	
	/**
	 * Stops the updater and disposes of any resources.	 */
	public void deactivate() {
		setActive(false);
		stop();
		if (thumbnailImage != null) {
			thumbnailImage.dispose();
			thumbnailImage = null;
			thumbnailImageSize = null;
		}
	}
	
	/**
	 * Returns the current horizontal tile index.	 * @return current horizontal tile index.	 */
	protected int getCurrentHTile() {
		return currentHTile;
	}

	/**
	 * Returns the current vertical tile index.
	 * @return current vertical tile index.
	 */
	protected int getCurrentVTile() {
		return currentVTile;
	}
	
	/**
	 * Returns <code>true</code> if this ThumbnailUpdater is active.  An inactive
	 * updater has disposed of its {@link Image}.  The updater may be active and 
	 * not currently running.  	 * @return <code>true</code> if this ThumbnailUpdater is active	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Returns <code>true</code> if this is currently running and updating at
	 * least one tile on the thumbnail {@link Image}.  	 * @return <code>true</code> if this is currently running	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * Resets the number of vertical and horizontal tiles, as well as the tile
	 * size and current tile index.	 */
	public void resetTileValues() {
		hTiles = (int)Math.ceil((float)sourceFigure.getSize().width / (float)MAX_BUFFER_SIZE);
		vTiles = (int)Math.ceil((float)sourceFigure.getSize().height / (float)MAX_BUFFER_SIZE);
		
		tileSize = new Dimension((int)Math.ceil((float)sourceFigure.getSize().width / (float)hTiles),
								(int)Math.ceil((float)sourceFigure.getSize().height / (float)vTiles));
		
		currentHTile = 0;
		currentVTile = 0;
	}
	
	/**
	 * Restarts the updater.	 */
	public void restart() {
		stop();
		start();
	}
	
	/**
	 * Updates the current tile on the Thumbnail.  An area of the source Figure
	 * is painted to an {@link Image}.  That Image is then drawn on the 
	 * Thumbnail.  Scaling of the source Image is done inside
	 * {@link GC#drawImage(Image, int, int, int, int, int, int, int, int)} since
	 * the source and target sizes are different.  The current tile indexes are
	 * incremented and if more updating is necesary, this {@link Runnable} is 
	 * called again in a {@link Display#timerExec(int, Runnable)}.  If no more
	 * updating is required, {@link #stop()} is called.	 */
	public void run() {
		if (!isActive() || !isRunning())
			return;
		int v = getCurrentVTile();
		int sy1 = v * tileSize.height;
		int sy2 = Math.min((v + 1) * tileSize.height, sourceFigure.getSize().height);
		
		int ty1 = (int)Math.round(sy1 * getScaleY());
		int ty2 = Math.min((int)Math.round(sy2 * getScaleY()), targetSize.height);
		
		int h = getCurrentHTile();
		int sx1 = h * tileSize.width;
		int sx2 = Math.min((h + 1) * tileSize.width, sourceFigure.getSize().width);
		int tx1 = (int)Math.round(sx1 * getScaleX());
		int tx2 = Math.min((int)Math.round(sx2 * getScaleX()), targetSize.width);
		org.eclipse.draw2d.geometry.Point p = sourceFigure.getBounds().getLocation();
		Rectangle rect = new Rectangle(sx1 + p.x, sy1 + p.y, sx2 - sx1, sy2 - sy1);
		sourceGraphics.pushState();
		sourceGraphics.translate(-sx1 - p.x, -sy1 - p.y);
		sourceGraphics.setClip(rect);
		sourceGraphics.fillRectangle(rect);
		sourceFigure.paint(sourceGraphics);
		if ((tx2 - tx1 > 0) && (ty2 - ty1 > 0))
			thumbnailGC.drawImage(sourceBuffer, 0, 0, sx2 - sx1, sy2 - sy1, tx1, ty1, tx2 - tx1, ty2 - ty1);
		sourceGraphics.popState();
		repaint();
		
		if (getCurrentHTile() < (hTiles - 1))
			setCurrentHTile(getCurrentHTile() + 1);
		else {
			setCurrentHTile(0);
			if (getCurrentVTile() < (vTiles - 1))
				setCurrentVTile(getCurrentVTile() + 1);
			else
				setCurrentVTile(0);
		}
		
		if (getCurrentHTile() != 0 || getCurrentVTile() != 0)
			Display.getCurrent().timerExec(10, this);
		else if (isDirty()) {
			setDirty(false);
			Display.getCurrent().timerExec(10, this);
		} else
			stop();
	}
	
	/**
	 * Sets the active flag.	 * @param value The active value	 */
	public void setActive(boolean value) {
		isActive = value;
	}
	
	/**
	 * Sets the current horizontal tile index.	 * @param count current horizontal tile index	 */
	protected void setCurrentHTile(int count) {
		currentHTile = count;
	}
	
	/**
	 * Sets the current vertical tile index.
	 * @param count current vertical tile index
	 */
	protected void setCurrentVTile(int count) {
		currentVTile = count;
	}
	
	/**
	 * Starts this updater.  This method initializes all the necessary resources
	 * and puts this {@link Runnable} on the asynch queue.  If this updater is
	 * not active or is already running, this method just returns.	 */
	public void start() {
		if (!isActive() || isRunning())
			return;
		
		isRunning = true;
		setDirty(false);		
		resetTileValues();
		
		if (!tileSize.equals(sourceBufferSize)) {
			if (sourceBuffer != null)
				sourceBuffer.dispose();
			sourceBuffer = new Image(Display.getDefault(), tileSize.width, tileSize.height);
			sourceBufferSize = new Dimension(tileSize);
		}
		sourceGC = new GC(sourceBuffer);
		sourceGraphics = new SWTGraphics(sourceGC);
		Color color = sourceFigure.getForegroundColor();
		if (color != null)
			sourceGraphics.setForegroundColor(color);
		color = sourceFigure.getBackgroundColor();
		if (color != null)
			sourceGraphics.setBackgroundColor(color);
		sourceGraphics.setFont(sourceFigure.getFont());
	
		setScales(targetSize.width / (float)sourceFigure.getSize().width,
			     targetSize.height / (float)sourceFigure.getSize().height);

		if (!targetSize.equals(thumbnailImageSize)) {
			if (thumbnailImage != null)
				thumbnailImage.dispose();
			thumbnailImage = new Image(Display.getDefault(), targetSize.width, targetSize.height);
			thumbnailImageSize = new Dimension(targetSize);
		}
		thumbnailGC = new GC(thumbnailImage);
		Display.getCurrent().asyncExec(this);
	}
	
	/**
	 * Stops this updater.  Also disposes of resources (except the thumbnail
	 * image which is still needed for painting).	 */
	public void stop() {
		isRunning = false;
		if (sourceGC != null) {
			sourceGC.dispose();
			sourceGC = null;
		}
		if (thumbnailGC != null) {
			thumbnailGC.dispose();
			thumbnailGC = null;
		}
		sourceGraphics = null;
		if (sourceBuffer != null) {
			sourceBuffer.dispose();
			sourceBuffer = null;
		}
		sourceBufferSize = null;
		// Don't dispose of the thumbnail image since it is needed to paint the 
		// figure when the source is not dirty (i.e. showing/hiding the dock).
	}
}

/**
 * Creates a new Thumbnail.  The source Figure must be set separately if you
 * use this constructor. */
public Thumbnail() {
	super();	
}

/**
 * Creates a new Thumbnail with the given IFigure as its source figure.
 *  * @param fig The source figure */
public Thumbnail(IFigure fig) {
	this();
	setSource(fig);
}

private Dimension adjustToAspectRatio(Dimension size, boolean adjustToMaxDimension) {
	Dimension sourceSize = sourceFigure.getSize();
	Dimension borderSize = new Dimension(getInsets().getWidth(), getInsets().getHeight());
	size.expand(borderSize.getNegated());
	int width, height;
	if (adjustToMaxDimension) {
		width  = Math.max(size.width, (int)(size.height * sourceSize.width / (float)sourceSize.height + 0.5));
		height = Math.max(size.height, (int)(size.width * sourceSize.height / (float)sourceSize.width + 0.5));
	} else {
		width  = Math.min(size.width,  (int)(size.height * sourceSize.width / (float)sourceSize.height + 0.5));
		height = Math.min(size.height, (int)(size.width * sourceSize.height / (float)sourceSize.width + 0.5));
	}
	size.width  = width;
	size.height = height;
	return size.expand(borderSize);
}

/**
 * Deactivates this Thumbnail and as well as its {@link ThumbnailUpdater}. */
public void deactivate() {
	updater.deactivate();
}

/**
 * Returns the preferred size of this Thumbnail.  The preferred size will be 
 * calculated in a way that maintains the source Figure's aspect ratio.
 * 
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size */
public Dimension getPreferredSize(int wHint, int hHint) {
	if (prefSize == null)
		return adjustToAspectRatio(getBounds().getSize(), false);
		
	Dimension preferredSize = adjustToAspectRatio(prefSize.getCopy(), true);
	
	if (maxSize == null)
		return preferredSize;
	
	Dimension maximumSize = adjustToAspectRatio(maxSize.getCopy(), true);
	if (preferredSize.contains(maximumSize))
		return maximumSize;
	else
		return preferredSize;
}

/**
 * Returns the scale factor on the X-axis. * @return X scale */
protected float getScaleX() {
	return scaleX;
}

/**
 * Returns the scale factor on the Y-axis.
 * @return Y scale
 */
protected float getScaleY() {
	return scaleY;
}

/**
 * Returns the scaled Image of the source Figure.  If the Image needs to be 
 * updated, the {@link ThumbnailUpdater} will notified.
 *  * @return The thumbnail image */
protected Image getThumbnailImage() {
	Dimension oldSize = targetSize;
	targetSize = getPreferredSize();
	targetSize.expand(new Dimension(getInsets().getWidth(), getInsets().getHeight()).negate());
	
	if ((isDirty()) && !updater.isRunning())
		updater.start();
	else if (oldSize != null && !targetSize.equals(oldSize)) {
		revalidate();
		updater.restart();
	}
		
	return thumbnailImage;
}

/**
 * Returns <code>true</code> if the source figure has changed.
 *  * @return <code>true</code> if the source figure has changed */
protected boolean isDirty() {
	return isDirty;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics) */
protected void paintFigure(Graphics graphics) {
	Image thumbnail = getThumbnailImage();
	if (thumbnail == null)
		return;
	graphics.drawImage(thumbnail, getClientArea().getLocation());
}

/**
 * Sets the dirty flag.
 *  * @param value The dirty value */
public void setDirty(boolean value) {
	isDirty = value;
}

/**
 * Sets the X and Y scales for the Thumbnail.  These scales represent the ratio
 * between the source figure and the Thumbnail.   * @param x The X scale * @param y The Y scale */
protected void setScales(float x, float y) {
	scaleX = x;
	scaleY = y;
}

/**
 * Sets the source Figure.  Also sets the scales and creates the necessary
 * update manager.
 *  * @param fig The source figure */
public void setSource(IFigure fig) {
	sourceFigure = fig; 
	setScales((float)getSize().width / (float)sourceFigure.getSize().width,
			(float)getSize().height / (float)sourceFigure.getSize().height);
	new ThumbnailUpdateManager(sourceFigure, this);
	sourceFigure.addFigureListener(new FigureListener() {
		public void figureMoved(IFigure fig) {
			setDirty(true);
			revalidate();
		}
	});
}

}
