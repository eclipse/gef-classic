package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.HashMap;
import java.util.Map;

import com.ibm.etools.draw2d.geometry.Rectangle;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;

/**
 * @version 	1.0
 * @author
 */
class ScaledGraphics
	extends SWTGraphics {

Map fontCache = new HashMap();
Font logicalFont;

private double scale = 1.0;

ScaledGraphics(GC gc){
	super(gc);
}

public void drawLine(int x1,int y1,int x2,int y2){
	x1 *= scale;
	x2 *= scale;
	y1 *= scale;
	y2 *= scale;
	super.drawLine(x1, y1, x2, y2);
}

public void drawRectangle(int x,int y,int width,int height){
	int right = (int)((x + width)*scale);
	int bottom= (int)((y + height)*scale);
	x *= scale;
	y *= scale;
	super.drawRectangle(x, y , right-x, bottom-y);
}

/** 
 * @deprecated use {@link #drawText(String, int, int)} and 
 * {@link #fillText(String, int, int)}
 */
public void drawText(String s,int x,int y,Graphics.TransparencyFlag transparent){
	if (transparent.toBoolean())
		drawText(s, x, y);
	else
		fillText(s, x, y);
}

public void drawText(String s, int x, int y) {
	x *= scale;
	y *= scale;
	super.drawText(s, x, y);
}

public void fillText(String s, int x, int y) {
	x *= scale;
	y *= scale;
	super.fillText(s, x, y);
}

public void fillRectangle(int x,int y,int width,int height){
	int right = (int)((x + width)*scale);
	int bottom= (int)((y + height)*scale);
	x *= scale;
	y *= scale;
	super.fillRectangle(x, y , right-x, bottom-y);
}

private Font getCachedFont(FontData data){
	Font f = (Font)fontCache.get(data);
	if (f == null){
		f = new Font(null, data);
		fontCache.put(data, f);
	}
	return f;
}

public void scale(double value){
	FontData data = getFont().getFontData()[0];
	data.height *= scale;
	setFont(getCachedFont(data));
}

public void setFont(Font f){
	logicalFont = f;
	FontData data = getFont().getFontData()[0];
	data.height *= scale;
	setFont(getCachedFont(data));
}

}
