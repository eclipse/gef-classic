package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.*;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Implementation of providing the drawing capabilities of SWT's GC class
 * in draw2d.
 */
public class SWTGraphics
	extends Graphics
{

protected static class State
	implements Cloneable
{
	public Color
		bgColor,
		fgColor;
	public int clipX,clipY,clipW,clipH; //X and Y are absolute here.
	public Font font;  //Fonts are immutable, shared references are safe
	public int
		lineWidth,
		lineStyle,
		dx, dy;
	public boolean xor;

	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}

	public void copyFrom(State state){
		bgColor = state.bgColor;
		fgColor = state.fgColor;
		lineStyle = state.lineStyle;
		lineWidth = state.lineWidth;
		dx = state.dx;
		dy = state.dy;
		font = state.font;
		clipX = state.clipX;
		clipY = state.clipY;
		clipW = state.clipW;
		clipH = state.clipH;
		xor = state.xor;
	}
}

public static boolean debug = false;
private final State appliedState = new State();
private final State currentState = new State();

private GC gc;

final private Rectangle relativeClip;

private List stack = new ArrayList();
private int stackPointer = 0;

private int translateX = 0;
private int translateY = 0;

public SWTGraphics(GC gc){
	gc.setLineWidth(1);
	this.gc = gc;
	//No translation necessary because translation is <0,0> at construction.
	relativeClip = new Rectangle(gc.getClipping());
	init();
}

final protected void checkFill(){
	if(!appliedState.bgColor.equals(currentState.bgColor))
		gc.setBackground(appliedState.bgColor = currentState.bgColor);
	checkGC();
}

final protected void checkGC(){
	if(appliedState.xor != currentState.xor)
		gc.setXORMode(appliedState.xor = currentState.xor);
	if(appliedState.clipX != currentState.clipX ||
		appliedState.clipY != currentState.clipY || 
		appliedState.clipW != currentState.clipW || 
		appliedState.clipH != currentState.clipH){
		gc.setClipping(appliedState.clipX = currentState.clipX,
						appliedState.clipY = currentState.clipY,
						appliedState.clipW = currentState.clipW,
						appliedState.clipH = currentState.clipH);
	}
}

final protected void checkPaint(){
	checkGC();
	if(!appliedState.fgColor.equals(currentState.fgColor))
		gc.setForeground(appliedState.fgColor = currentState.fgColor);
	if(appliedState.lineStyle != currentState.lineStyle)
		gc.setLineStyle(appliedState.lineStyle = currentState.lineStyle);
	if(appliedState.lineWidth != currentState.lineWidth)
		gc.setLineWidth(appliedState.lineWidth = currentState.lineWidth);
	if(!appliedState.bgColor.equals(currentState.bgColor))
		gc.setBackground(appliedState.bgColor = currentState.bgColor);
}

final protected void checkText(){
	checkPaint();
	checkFill();
	if(!appliedState.font.equals(currentState.font))
		gc.setFont(appliedState.font = currentState.font);
}

public void clipRect(Rectangle rect){
	relativeClip.intersect(rect);
	setClipAbsolute(relativeClip.x+translateX,
			    relativeClip.y+translateY,
			    relativeClip.width,
			    relativeClip.height);
}

public void dispose() {
	while(stackPointer > 0) {
		popState();
	}
}

public void drawArc(int x, int y, int width, int height, int offset, int length){
	checkPaint();
	gc.drawArc(x + translateX, y + translateY, width, height, offset, length);
}

public void drawFocus(int x, int y, int w, int h){
	checkPaint();
	checkFill();
	gc.drawFocus(x+translateX, y+translateY, w+1, h+1);
}

public void drawImage(Image srcImage, int x, int y){
	checkGC();
	gc.drawImage(srcImage, x + translateX, y + translateY);
}

public void drawImage(Image srcImage, int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2){
	checkGC();
	gc.drawImage(srcImage,x1,y1,w1,h1,x2+translateX,y2+translateY,w2,h2);
}

public void drawLine(int x1, int y1, int x2, int y2){
	checkPaint();
	gc.drawLine(x1 + translateX, y1 + translateY,
			x2 + translateX, y2 + translateY);
}

public void drawOval(int x, int y, int width, int height){
	checkPaint();
	gc.drawOval(x + translateX, y + translateY, width, height);
}

public void drawPolygon(PointList points){
	checkPaint();
	try{
		points.translate(translateX, translateY);
		gc.drawPolygon(points.toIntArray());
	} finally {
		points.translate(-translateX, -translateY);
	}
}

public void drawPolyline(PointList points){
	checkPaint();
	try{
		points.translate(translateX, translateY);
		int array[] = points.toIntArray();
		gc.drawPolyline(array);
		if (getLineWidth() == 1 && array.length >=2){
			int x = array[array.length-2];
			int y = array[array.length-1];
			gc.drawLine(x, y, x, y);
		}
	} finally {
		points.translate(-translateX, -translateY);
	}
}

public void drawRectangle(int x, int y, int width, int height){
	checkPaint();
	gc.drawRectangle(x + translateX, y + translateY, width, height);
}

public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight){
	checkPaint();
	gc.drawRoundRectangle(r.x + translateX, r.y + translateY, r.width, r.height, arcWidth, arcHeight);
}

public void drawString(String s, int x, int y){
	checkText();
	gc.drawString(s, x + translateX, y + translateY, true);
}

public void drawText(String s, int x, int y) {
	checkText();
	gc.drawText(s, x + translateX, y + translateY, true);
}

public void fillArc(int x, int y, int width, int height, int offset, int length){
	checkFill();
	gc.fillArc(x + translateX, y + translateY, width, height, offset, length);
}

public void fillGradient(int x, int y, int w, int h, boolean vertical) {
	checkFill();
	checkPaint();
	gc.fillGradientRectangle(x + translateX, y + translateY, w, h, vertical);
}


public void fillOval(int x, int y, int width, int height){
	checkFill();
	gc.fillOval(x + translateX, y + translateY, width, height);
}

public void fillPolygon(PointList points){
	checkFill();
	try{
		points.translate(translateX, translateY);
		gc.fillPolygon(points.toIntArray());
	} finally {
		points.translate(-translateX, -translateY);
	}
}

public void fillRectangle(int x, int y, int width, int height){
	checkFill();
	gc.fillRectangle(x + translateX, y + translateY, width, height);
}

public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight){
	checkFill();
	gc.fillRoundRectangle(r.x + translateX, r.y + translateY, r.width, r.height, arcWidth, arcHeight);
}

public void fillString(String s, int x, int y) {
	checkText();
	gc.drawString(s, x + translateX, y + translateY, false);
}

public void fillText(String s, int x, int y) {
	checkText();
	gc.drawText(s, x + translateX, y + translateY, false);
}

public Color getBackgroundColor(){
	return currentState.bgColor;
}

public Rectangle getClip(Rectangle rect){
	rect.setBounds(relativeClip);
	return rect;
}

public Font getFont(){
	return currentState.font;
}

public FontMetrics getFontMetrics(){
	checkText();
	return gc.getFontMetrics();
}

public Color getForegroundColor(){
	return currentState.fgColor;
}

public int getLineStyle(){
	return currentState.lineStyle;
}

public int getLineWidth(){
	return currentState.lineWidth;
}

public boolean getXORMode(){
	return currentState.xor;
}

protected void init(){
//Current translation is assumed to be 0,0.
	currentState.bgColor = appliedState.bgColor = gc.getBackground();
	currentState.fgColor = appliedState.fgColor = gc.getForeground();
	currentState.font = appliedState.font = gc.getFont();
	currentState.lineWidth = appliedState.lineWidth = gc.getLineWidth();
	currentState.lineStyle = appliedState.lineStyle = gc.getLineStyle();
	currentState.clipX = appliedState.clipX = relativeClip.x;
	currentState.clipY = appliedState.clipY = relativeClip.y;
	currentState.clipW = appliedState.clipW = relativeClip.width;
	currentState.clipH = appliedState.clipH = relativeClip.height;
	currentState.xor = appliedState.xor = gc.getXORMode();
}

public void popState(){
	stackPointer--;
	restoreState((State)stack.get(stackPointer));
}

public void pushState(){
	try {
		State s;
		if(stack.size() > stackPointer) {
			s = (State)stack.get(stackPointer);
			s.copyFrom(currentState);
		} else {
			stack.add(currentState.clone());
		}
		stackPointer++;
	} catch (CloneNotSupportedException e){
		throw new RuntimeException(e.getMessage());
	}
}

public void restoreState(){
	restoreState((State)stack.get(stackPointer - 1));
}

protected void restoreState(State s){
	setBackgroundColor(s.bgColor);
	setForegroundColor(s.fgColor);
	setLineStyle(s.lineStyle);
	setLineWidth(s.lineWidth);
	setFont(s.font);
	setXORMode(s.xor);
	setClipAbsolute(s.clipX, s.clipY, s.clipW, s.clipH);

	translateX = currentState.dx = s.dx;
	translateY = currentState.dy = s.dy;

	relativeClip.x = s.clipX - translateX;
	relativeClip.y = s.clipY - translateY;
	relativeClip.width = s.clipW;
	relativeClip.height = s.clipH;
}

public void scale(double factor){
}

public void setBackgroundColor(Color color){
	if (currentState.bgColor.equals(color))
		return;
	currentState.bgColor = color;
}

public void setClip(Rectangle rect){
	relativeClip.x = rect.x;
	relativeClip.y = rect.y;
	relativeClip.width = rect.width;
	relativeClip.height= rect.height;

	setClipAbsolute(rect.x+translateX,
			    rect.y+translateY,
			    rect.width,
			    rect.height);
}

protected void setClipAbsolute(int x, int y, int w, int h){
	if (currentState.clipW == w &&
	    currentState.clipH == h &&
	    currentState.clipX == x &&
	    currentState.clipY == y) return;

	currentState.clipX = x;
	currentState.clipY = y;
	currentState.clipW = w;
	currentState.clipH = h;
}

public void setFont(Font f){
	if (currentState.font == f) return;
	currentState.font = f;
}

public void setForegroundColor(Color color){
	if (currentState.fgColor.equals(color))
		return;
	currentState.fgColor = color;
}

public void setLineStyle(int style){
	if (currentState.lineStyle == style) return;
	currentState.lineStyle = style;
}

public void setLineWidth(int width){
	if (currentState.lineWidth == width) return;
	currentState.lineWidth = width;
}

protected void setTranslation(int x, int y){
	translateX = currentState.dx = x;
	translateY = currentState.dy = y;
}

public void setXORMode(boolean b){
	if (currentState.xor == b) return;
	currentState.xor = b;
}

public void translate(int x, int y){
	setTranslation(translateX + x, translateY + y);
	relativeClip.x -= x;
	relativeClip.y -= y;
}

}