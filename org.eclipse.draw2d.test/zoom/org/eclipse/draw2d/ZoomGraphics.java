package org.eclipse.draw2d;

import java.util.*;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Color;
import org.eclipse.draw2d.geometry.*;

public class ZoomGraphics
	extends Graphics
{

protected static class State {
	private float zoom;
	private float appliedX;
	private float appliedY;
	private Font font;

	protected State(){}
	protected State(float zoom, float x, float y, Font font) {
		this.zoom = zoom;
		this.appliedX = x;
		this.appliedY = y;
		this.font = font;
	}
	
	protected void setValues(float zoom, float x, float y, Font font) {
		this.zoom = zoom;
		this.appliedX = x;
		this.appliedY = y;
		this.font = font;
	}
}

private static class FontHeightCache {
	Font font;
	int height;
}

private static final Rectangle TEMP = new Rectangle();
private static final Point PT = new Point();
private static Map fontCache = new HashMap();

private FontHeightCache localCache = new FontHeightCache();
private FontHeightCache targetCache = new FontHeightCache();
private Graphics graphics;
private Font localFont;
private float fractionalX;
private float fractionalY;
private float zoom = 1.0f;
private List stack = new ArrayList();
private int stackPointer = 0;

public ZoomGraphics(Graphics g){
	graphics = g;
	localFont = g.getFont();
}

public void clipRect(Rectangle r) {
	graphics.clipRect(zoomRect(r));
}

public void drawArc(int x, int y, int w, int h, int offset, int sweep) {
	graphics.drawArc(zoomRect(x,y,w,h), offset, sweep);
}

public void fillArc(int x, int y, int w, int h, int offset, int sweep) {
	graphics.drawArc(zoomRect(x,y,w,h), offset, sweep);
}

public void drawFocus(int x, int y, int w, int h) {
	graphics.drawFocus(zoomRect(x,y,w,h));
}

public void drawImage(Image srcImage, int x, int y) {
	org.eclipse.swt.graphics.Rectangle size = srcImage.getBounds();
	graphics.drawImage(srcImage, 0, 0, size.width, size.height,
		(int)(x*zoom), (int)(y*zoom),
		(int)(size.width*zoom), (int)(size.height*zoom));
}

public void drawImage(Image srcImage,
	int sx, int sy, int sw, int sh,
	int tx, int ty, int tw, int th)
{
	//"t" == target rectangle, "s" = source
	Rectangle t = zoomRect(tx, ty, tw, th);
	graphics.drawImage(srcImage, sx,sy,sw,sh, t.x, t.y, t.width, t.height);
}

public void drawLine(int x1, int y1, int x2, int y2) {
	graphics.drawLine(
		(int)(x1 * zoom),
		(int)(y1 * zoom),
		(int)(x2 * zoom),
		(int)(y2 * zoom));
}

public void drawOval(int x, int y, int w, int h) {
	graphics.drawOval(zoomRect(x,y,w,h));
}

public void fillOval(int x, int y, int w, int h) {
	graphics.fillOval(zoomFillRect(x,y,w,h));
}

public void drawPolygon(PointList points) {
	graphics.drawPolygon(zoomPointList(points));
}

public void fillPolygon(PointList points) {
	graphics.fillPolygon(zoomPointList(points));
}

public void drawPolyline(PointList points) {
	graphics.drawPolyline(zoomPointList(points));
}

public void drawRectangle(int x, int y, int w, int h) {
	graphics.drawRectangle(zoomRect(x,y,w,h));
}

public void fillRectangle(int x, int y, int w, int h) {
	graphics.fillRectangle(zoomFillRect(x,y,w,h));
}

public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	graphics.drawRoundRectangle(zoomRect(r.x, r.y, r.width, r.height),
		(int)(arcWidth * zoom),
		(int)(arcHeight * zoom));
}

public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
	graphics.fillRoundRectangle(zoomFillRect(r.x, r.y, r.width, r.height),
		(int)(arcWidth * zoom),
		(int)(arcHeight * zoom));
}

public void drawString(String s, int x, int y) {
	graphics.drawString(s, zoomTextPoint(x, y));
}

public void fillString(String s, int x, int y) {
	graphics.fillString(s, zoomTextPoint(x, y));
}

public void drawText(String s, int x, int y) {
	graphics.drawText(s, zoomTextPoint(x, y));
}

public void fillText(String s, int x, int y) {
	graphics.fillText(s, zoomTextPoint(x, y));
}

public int getAdvanceWidth(char c) {
	FigureUtilities.setFont(getLocalFont());
	return FigureUtilities.getGC().getAdvanceWidth(c);
}

public Color getBackgroundColor() {
	return graphics.getBackgroundColor();
}

public int getCharWidth(char c) {
	FigureUtilities.setFont(getLocalFont());
	return FigureUtilities.getGC().getCharWidth(c);
}

public Rectangle getClip(Rectangle rect) {
	graphics.getClip(rect);
	int x = (int)(rect.x / zoom);
	int y = (int)(rect.y / zoom);
	rect.width = (int)Math.ceil(rect.right()/zoom) - x;
	rect.height = (int)Math.ceil(rect.bottom()/zoom) - y;
	rect.x = x;
	rect.y = y;
	return rect;
}

public Font getFont() {
	return getLocalFont();
}

public FontMetrics getFontMetrics() {
	return FigureUtilities.getFontMetrics(localFont);
}

public Color getForegroundColor() {
	return graphics.getForegroundColor();
}

public int getLineStyle() {
	return graphics.getLineStyle();
}

public int getLineWidth() {
	return graphics.getLineWidth();
}

private Font getLocalFont(){
	return localFont;
}

public Dimension getStringExtent(String s) {
	FigureUtilities.setFont(getLocalFont());
	org.eclipse.swt.graphics.Point p = FigureUtilities
		.getGC()
		.stringExtent(s);
	return new Dimension(p.x, p.y);
}

public Dimension getTextExtent(String s) {
	FigureUtilities.setFont(getLocalFont());
	org.eclipse.swt.graphics.Point p = FigureUtilities
		.getGC()
		.textExtent(s);
	return new Dimension(p.x, p.y);
}

public boolean getXORMode() {
	return graphics.getXORMode();
}

public void popState() {
	graphics.popState();
	stackPointer--;
	restoreLocalState((State)stack.get(stackPointer));
}

public void pushState() {
	State s;
	if(stack.size() > stackPointer) {
		s = (State)stack.get(stackPointer);
		s.setValues(zoom, fractionalX, fractionalY, getLocalFont());
	} else {
		stack.add(new State(zoom, fractionalX, fractionalY, getLocalFont()));
	}
	stackPointer++;

	graphics.pushState();
}

private void restoreLocalState(State state) {
	this.fractionalX = state.appliedX;
	this.fractionalY = state.appliedY;
	setScale(state.zoom);
	setLocalFont(state.font);
}

public void restoreState() {
	graphics.restoreState();
	restoreLocalState((State)stack.get(stackPointer - 1));
}

public void scale(float amount) {
	setScale(zoom * amount);
}

void setScale(float value){
	if (zoom == value)
		return;
	this.zoom = value;
	graphics.setFont(zoomFont(getLocalFont()));
}

Font getCachedFont(FontData data){
	Font font = (Font)fontCache.get(new Integer(data.getHeight()));
	if (font != null)
		return font;
	font = new Font(null, data);
	fontCache.put(new Integer(data.getHeight()), font);
	return font;
}

public void setBackgroundColor(Color rgb) {
	graphics.setBackgroundColor(rgb);
}

public void setClip(Rectangle r) {
	graphics.setClip(zoomRect(r));
}

public void setFont(Font f) {
	setLocalFont(f);
}

public void setForegroundColor(Color rgb) {
	graphics.setForegroundColor(rgb);
}

public void setLineStyle(int style) {
	graphics.setLineStyle(style);
}

public void setLineWidth(int width) {
	graphics.setLineWidth(width);
}

private void setLocalFont(Font f){
	localFont = f;
	graphics.setFont(zoomFont(f));
}

public void setXORMode(boolean b) {
	graphics.setXORMode(b);
}

public void translate(int dx, int dy){
	//fractionalX/Y is the fractional part left over from previous translates that gets lost
	//in the integer approximation.
	float dxFloat = dx * zoom + fractionalX;
	float dyFloat = dy * zoom + fractionalY;
	int dxRounded = (int)dxFloat;
	int dyRounded = (int)dyFloat;
	fractionalX = dxFloat - dxRounded;
	fractionalY = dyFloat - dyRounded;
	graphics.translate(dxRounded, dyRounded);
}

private Point zoomTextPoint(int x, int y){
	if (localCache.font != localFont){
		//Font is different, re-calculate its height
		FontMetrics metric = FigureUtilities.getFontMetrics(localFont); 
		localCache.height = metric.getHeight() - metric.getDescent();
		localCache.font = localFont;
	}
	if (targetCache.font != graphics.getFont()){
		FontMetrics metric = graphics.getFontMetrics();
		targetCache.font = graphics.getFont();
		targetCache.height = metric.getHeight() - metric.getDescent();
	}
	return new Point((int)x*zoom,
		(int)(y + localCache.height - 1)*zoom - targetCache.height + 1);
}

private PointList zoomPointList(PointList points){
	PointList scaled = new PointList(points.size());
	for (int i = 0; i < points.size(); i++) {
		scaled.addPoint(points.getPoint(i).scale(zoom));
	}
	return scaled;
}

private Rectangle zoomFillRect(int x, int y, int w, int h){
	TEMP.x = (int)(x * zoom);
	TEMP.y = (int)(y * zoom);
	TEMP.width = (int)((x+w - 1) * zoom) - TEMP.x + 1;
	TEMP.height = (int)((y+h - 1) * zoom) - TEMP.y + 1;
	return TEMP;
}

private Font zoomFont(Font f) {
	FontData data = getLocalFont().getFontData()[0];
	data =
		new FontData(
			data.getName(),
			(int) (data.getHeight() * zoom),
			data.getStyle());
	return getCachedFont(data);
}

private Rectangle zoomRect(int x, int y, int w, int h){
	TEMP.x = (int)(x * zoom);
	TEMP.y = (int)(y * zoom);
	TEMP.width = (int)((x+w) * zoom) - TEMP.x;
	TEMP.height = (int)((y+h) * zoom) - TEMP.y;
	return TEMP;
}

private Rectangle zoomRect(Rectangle r){
	TEMP.x = (int)(r.x * zoom);
	TEMP.y = (int)(r.y * zoom);
	TEMP.width = (int)(r.right() * zoom) - TEMP.x;
	TEMP.height = (int)(r.bottom() * zoom) - TEMP.y;
	return TEMP;
}

}
