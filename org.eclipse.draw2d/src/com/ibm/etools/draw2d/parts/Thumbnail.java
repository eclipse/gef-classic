package com.ibm.etools.draw2d.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.graphics.Color;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;
import com.ibm.etools.draw2d.util.ImageCache;

public class Thumbnail 
	extends Figure 
{

private boolean sourceDirty;
private float scaleX = 1, scaleY = 1;
private IFigure sourceFigure;	
private Image originalImage, scaledImage;
private Dimension sourceSize, targetSize;

/**
 * Provides for a scaled image representation of a Figure.
 */
public Thumbnail(){
	super();
}

public Thumbnail( IFigure fig ){
	this();
	setSource(fig);
}

private Dimension adjustToAspectRatio( Dimension size, boolean adjustToMaxDimension ){
	Dimension sourceSize = sourceFigure.getSize();
	Dimension borderSize = new Dimension( getInsets().getWidth(), 
						    	  getInsets().getHeight() );
	size.expand( borderSize.getNegated() );
	int width, height;
	if( adjustToMaxDimension ){
		width = Math.max( size.width, (int)
					(size.height * sourceSize.width / (float)sourceSize.height + 0.5) );
		height = Math.max( size.height, (int)
					(size.width * sourceSize.height / (float)sourceSize.width + 0.5) );
	} else {
		width = Math.min( size.width, (int)
					(size.height * sourceSize.width / (float)sourceSize.height + 0.5) );
		height = Math.min( size.height, (int)
					(size.width * sourceSize.height / (float)sourceSize.width + 0.5) );
	}
	size.width = width;
	size.height = height;
	return size.expand( borderSize );
}

protected void finalize(){
	ImageCache.checkin( originalImage );
	ImageCache.checkin( scaledImage );
}

public Dimension getPreferredSize(){
	if( prefSize == null )
		return adjustToAspectRatio( getBounds().getSize(), false );
		
	Dimension preferredSize = adjustToAspectRatio( prefSize.getCopy(), true );
	
	if( maxSize == null)
		return preferredSize;
	
	Dimension maximumSize = adjustToAspectRatio( maxSize.getCopy(), true );
	if( preferredSize.greaterThan( maximumSize ) )
		return maximumSize;
	else
		return preferredSize;
}

float getScaleX(){
	return scaleX;
}

float getScaleY(){
	return scaleY;
}

void resetOriginalImage(){
	sourceSize = sourceFigure.getBounds().getSize();
	originalImage = ImageCache.checkout( sourceSize, this );
}	

void resetScaledImage(){
	// Calculate the width and height of the target rectangle
	// (based on the scale factor)
	Rectangle sourceRect = sourceFigure.getBounds();
	Dimension oldSize = targetSize;
	targetSize = getPreferredSize();
	targetSize.expand( new Dimension( getInsets().getWidth(),
						    getInsets().getHeight() ).negate() );
						    
	
	if( targetSize.width <= 0 || targetSize.height <= 0 )
		return;
		
	if( oldSize != null && !targetSize.equals( oldSize ) )
		setSourceDirty( true );

	setScales( targetSize.width / (float)sourceRect.width,
		     targetSize.height/ (float)sourceRect.height );

	scaledImage = ImageCache.checkinCheckout( scaledImage, targetSize, this );
}

public void paintFigure( Graphics g ){
	// Reset the two images (this will set sourceDirty to true,
	// if necessary)
	resetOriginalImage();
	resetScaledImage();

	// If sourceDirty is true, ask the sourceFigure to paint on 
	// originalImage, and store a scaled-down version of that image
	// in scaledImage
	if( sourceDirty ){
		GC gc = new GC( originalImage );
		SWTGraphics graphics = new SWTGraphics( gc );
		Color color = sourceFigure.getForegroundColor();
		if( color != null )
			graphics.setForegroundColor( color );
		color = sourceFigure.getBackgroundColor();
		if( color != null )
			graphics.setBackgroundColor( color );
		graphics.setFont( sourceFigure.getFont() );
		graphics.translate( sourceFigure.getBounds().getLocation().negate() );
		sourceFigure.paint( graphics );
		gc.dispose();
		
		gc = new GC( scaledImage );
		gc.drawImage( originalImage, 0, 0, sourceSize.width, sourceSize.height, 
				  0, 0, targetSize.width, targetSize.height );	
		gc.dispose();		
	}

	// Check in the originalImage
	ImageCache.checkin( originalImage );
	originalImage = null;
	
	// Draw the scaledImage on this figure, and set sourceDirty to false
	g.drawImage( scaledImage, getClientArea().getLocation() );
	setSourceDirty( false );			 
}

void setScales( float x, float y ){
	scaleX = x;
	scaleY = y;
}

public void setSource( IFigure fig ){
	sourceFigure = fig;
	new ThumbnailUpdateManager(sourceFigure, this);
	sourceFigure.addFigureListener( new FigureListener(){
		public void figureMoved( IFigure fig ){
			revalidate();
		}
	});
}

void setSourceDirty( boolean newValue ){
	sourceDirty = newValue;
}

}