package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.*;

class ImageGraphicsSource
//	implements GraphicsSource
{
/*
private Image imageBuffer;
private Dimension imageSize = new Dimension(0,0);
private GC        imageGC;

private GraphicsSource chainedSource;
private Graphics       chainedGraphics;

public ImageGraphicsSource(){}

public void flushGraphics(Rectangle region){
	GC gc = new GC(control);
	Rectangle r = new Rectangle(0,0,
		controlSize.width,controlSize.height).intersect(region);
	if (r.isEmpty()) return;
	gc.drawImage(getImage(),r.x,r.y,r.width,r.height,r.x,r.y,r.width,r.height);
	gc.dispose();
	imageGC.dispose();
	imageGC=null;
}

public Graphics getGraphics(Rectangle region){
	Graphics g = new SWTGraphics(getImageGC(region.getSize()));
	g.translate(region.getLocation());
	g.setClip(region);
	chainedGraphics = chainedSource.getGraphics(region);
	g.clipRect(chainedGraphics.getClip(Rectangle.SINGLETON));
	initializeGraphics(g);
	return g;
}

protected Image getImage(){
	if (imageBuffer == null)
		imageBuffer = new Image(null,
			Math.max(controlSize.width,1),
			Math.max(controlSize.height,1));
	return imageBuffer;
}

protected GC getImageGC(Dimension size){
	if (imageGC == null)
		imageGC = new GC(getImage());
	GC gc = new GC();
	imageGC.setBackground(gc.getBackground());
	imageGC.setForeground(gc.getForeground());
	imageGC.setFont(gc.getFont());
	imageGC.setLineStyle(gc.getLineStyle());
	imageGC.setLineWidth(gc.getLineWidth());
	imageGC.setXORMode(gc.getXORMode());
	gc.dispose();
	return imageGC;
}

void initializeGraphics(Graphics imageGC){
	imageGC.setBackgroundColor(chainedGraphics.getBackgroundColor());
	imageGC.setForegroundColor(chainedGraphics.getForegroundColor());
	imageGC.setFont(chainedGraphics.getFont());
	imageGC.setLineStyle(chainedGraphics.getLineStyle());
	imageGC.setLineWidth(chainedGraphics.getLineWidth());
	imageGC.setXORMode(chainedGraphics.getXORMode());
}
*/
}

