package org.eclipse.draw2d.parts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

public class Thumbnail extends Figure {

private IFigure sourceFigure;
private boolean isDirty;
private float scaleX;
private float scaleY;
private Dimension targetSize = new Dimension(0, 0);
private Image thumbnailImage;
private Dimension thumbnailImageSize;
private ThumbnailUpdater updater = new ThumbnailUpdater();


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
	
	public void deactivate() {
		setActive(false);
		stop();
		if (thumbnailImage != null) {
			thumbnailImage.dispose();
			thumbnailImage = null;
			thumbnailImageSize = null;
		}
	}

	protected int getCurrentHTile() {
		return currentHTile;
	}

	protected int getCurrentVTile() {
		return currentVTile;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void resetTileValues() {
		hTiles = (int)Math.ceil((float)sourceFigure.getSize().width/(float)MAX_BUFFER_SIZE);
		vTiles = (int)Math.ceil((float)sourceFigure.getSize().height/(float)MAX_BUFFER_SIZE);
		
		tileSize = new Dimension((int)Math.ceil((float)sourceFigure.getSize().width/(float)hTiles),
								(int)Math.ceil((float)sourceFigure.getSize().height/(float)vTiles));
		
		currentHTile = 0;
		currentVTile = 0;
	}
	
	public void restart() {
		stop();
		start();
	}
	
	public void run() {
		if (!isActive() || !isRunning())
			return;
		int v = getCurrentVTile();
		int sy1 = v * tileSize.height;
		int sy2 = Math.min((v+1) * tileSize.height, sourceFigure.getSize().height);
		
		int ty1 = (int)Math.round(sy1 * getScaleY());
		int ty2 = Math.min((int)Math.round(sy2 * getScaleY()),targetSize.height);
		
		int h = getCurrentHTile();
		int sx1 = h * tileSize.width;
		int sx2 = Math.min((h+1) * tileSize.width, sourceFigure.getSize().width);
		int tx1 = (int)Math.round(sx1 * getScaleX());
		int tx2 = Math.min((int)Math.round(sx2 * getScaleX()),targetSize.width);
		org.eclipse.draw2d.geometry.Point p = sourceFigure.getBounds().getLocation();
		Rectangle rect = new Rectangle(sx1+p.x, sy1+p.y, sx2-sx1, sy2-sy1);
		sourceGraphics.pushState();
		sourceGraphics.translate(-sx1-p.x, -sy1-p.y);
		sourceGraphics.setClip(rect);
		sourceGraphics.fillRectangle(rect);
		sourceFigure.paint(sourceGraphics);
		if ((tx2-tx1 > 0) && (ty2-ty1 > 0))
			thumbnailGC.drawImage(sourceBuffer, 0, 0, sx2-sx1, sy2-sy1, tx1, ty1, tx2-tx1, ty2-ty1);
		sourceGraphics.popState();
		repaint();
		
		if (getCurrentHTile() < (hTiles-1))
			setCurrentHTile(getCurrentHTile()+1);
		else {
			setCurrentHTile(0);
			if (getCurrentVTile() < (vTiles-1))
				setCurrentVTile(getCurrentVTile()+1);
			else
				setCurrentVTile(0);
		}
		
		if (getCurrentHTile() != 0 || getCurrentVTile() != 0)
			Display.getCurrent().timerExec(10, this);
		else if (isDirty()) {
			setDirty(false);
			Display.getCurrent().timerExec(10, this);
		}
		else
			stop();
	}
	
	public void setActive(boolean value) {
		isActive = value;
	}
	
	protected void setCurrentHTile(int count) {
		currentHTile = count;
	}
	
	protected void setCurrentVTile(int count) {
		currentVTile = count;
	}
	
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



public Thumbnail() {
	super();	
}

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
		width  = Math.max(size.width,  (int)(size.height*sourceSize.width/(float)sourceSize.height+0.5));
		height = Math.max(size.height, (int)(size.width*sourceSize.height/(float)sourceSize.width+0.5));
	} 
	else {
		width  = Math.min(size.width,  (int)(size.height*sourceSize.width/(float)sourceSize.height+0.5));
		height = Math.min(size.height, (int)(size.width*sourceSize.height/(float)sourceSize.width+0.5));
	}
	size.width  = width;
	size.height = height;
	return size.expand(borderSize);
}

public void deactivate() {
	updater.deactivate();
}

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

protected float getScaleX() {
	return scaleX;
}

protected float getScaleY() {
	return scaleY;
}

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

protected boolean isDirty() {
	return isDirty;
}

protected void paintFigure(Graphics graphics) {
	Image thumbnail = getThumbnailImage();
	if (thumbnail == null)
		return;
	graphics.drawImage(thumbnail, getClientArea().getLocation());
}

public void setDirty(boolean value) {
	isDirty = value;
}

protected void setScales(float x, float y) {
	scaleX = x;
	scaleY = y;
}

public void setSource(IFigure fig) {
	sourceFigure = fig; 
	setScales((float)getSize().width/(float)sourceFigure.getSize().width,
			(float)getSize().height/(float)sourceFigure.getSize().height);
	new ThumbnailUpdateManager(sourceFigure, this);
	sourceFigure.addFigureListener(new FigureListener() {
		public void figureMoved(IFigure fig) {
			setDirty(true);
			revalidate();
		}
	});
}

}
