package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.draw2d.geometry.*;

public class Label
	extends Figure
	implements PositionConstants
{

private static String ELLIPSIS = "..."; //$NON-NLS-1$

private Image icon;
private String text = "";//$NON-NLS-1$
private String subStringText;

private Dimension
	textSize,
	subStringTextSize,
	iconSize = new Dimension(0,0);

private Point iconLocation, textLocation;

private int
	textAlignment = CENTER,
	iconAlignment = CENTER,
	labelAlignment = CENTER,
	textPlacement = EAST,
	iconTextGap = 4;

/**
 * Construct an empty Label.
 * 
 * @since 2.0
 */
public Label(){}

/**
 * Construct a Label with passed String as its 
 * text.
 * 
 * @since 2.0
 */
public Label(String s){
	setText(s);
}

/**
 * Construct a Label with passed Image as its 
 * icon.
 * 
 * @since 2.0
 */
public Label(Image i){
	setIcon(i);
}

/**
 * Construct a Label with passed String as text and
 * passed Image as its icon.
 * 
 * @since 2.0
 */
public Label(String s, Image i) {
	setText(s);
	setIcon(i);
}

private void alignOnHeight(Point loc, Dimension size, int alignment) {
	Insets insets = getInsets();
	switch(alignment) {
		case TOP:
			loc.y = insets.top;
			break;
		case BOTTOM:
			loc.y = bounds.height - size.height - insets.bottom;
			break;
		default:
			loc.y = (bounds.height-size.height)/2;
	}
}

private void alignOnWidth(Point loc, Dimension size, int alignment) {
	Insets insets = getInsets();
	switch(alignment) {
		case LEFT:
			loc.x = insets.left;
			break;
		case RIGHT:
			loc.x = bounds.width - size.width - insets.right;
			break;
		default: 
			loc.x = (bounds.width - size.width)/2;
	}
}

private void calculateAlignment() {
	switch(textPlacement) {
		case EAST:
		case WEST:
			alignOnHeight(textLocation, getTextSize(), textAlignment);
			alignOnHeight(iconLocation, iconSize, iconAlignment);
			break;
		case NORTH:
		case SOUTH:
			alignOnWidth(textLocation, getSubStringTextSize(), textAlignment);
			alignOnWidth(iconLocation, iconSize, iconAlignment);
			break;
	}
}

/**
 * Calculates the size of the Label using the passed Dimension
 * as the size of the Label's text.
 * 
 * @param txtSize Pre-calculated size of the Label's text.
 * @since 2.0
 */ 
protected Dimension calculateLabelSize(Dimension txtSize){
	int gap = iconTextGap;
	if (getIcon() == null || getText().equals("")) gap = 0;//$NON-NLS-1$
	Dimension d = new Dimension(0,0);
	if (textPlacement == WEST || textPlacement == EAST){
		d.width = iconSize.width + gap + txtSize.width;
		d.height= Math.max(iconSize.height,txtSize.height);
	} else {
		d.width = Math.max(iconSize.width, txtSize.width);
		d.height= iconSize.height + gap + txtSize.height;
	}
	return d;
}

private void calculateLocations(){
	textLocation = new Point();
	iconLocation = new Point();
	
	calculatePlacement();
	calculateAlignment();
	Dimension offset = getSize().getDifference(getPreferredSize());
	offset.width += getTextSize().width - getSubStringTextSize().width;
	switch(labelAlignment){
		case CENTER: offset.scale(0.5f); break;
		case LEFT: offset.scale(0.0f); break;
		case RIGHT: offset.scale(1.0f); break;
		case TOP: offset.height=0; offset.scale(0.5f); break;
		case BOTTOM: offset.height=offset.height*2; offset.scale(0.5f); break;
		default: offset.scale(0.5f); break;
	}
		
	switch(textPlacement) {
		case EAST:
		case WEST: offset.height = 0; break;
		case NORTH:
		case SOUTH: offset.width = 0; break;
	}

	textLocation.translate(offset);
	iconLocation.translate(offset);
}

private void calculatePlacement() {
	int gap = iconTextGap;
	if (icon == null || text.equals("")	) gap = 0;//$NON-NLS-1$
	Insets insets = getInsets();
	
	switch(textPlacement) {
	case EAST:
		iconLocation.x = insets.left;
		textLocation.x = iconSize.width + gap + insets.left;	
		break;
	case WEST:
		textLocation.x = insets.left;
		iconLocation.x = getSubStringTextSize().width + gap + insets.left;
		break;
	case NORTH:
		textLocation.y = insets.top;
		iconLocation.y = getTextSize().height + gap + insets.top;
		break;
	case SOUTH:
		textLocation.y = iconSize.height + gap + insets.top;
		iconLocation.y = insets.top;
	}
}

/**
 * Calculates the size of the Label's text size. The 
 * text size calculated takes into consideration if the Label's
 * text is currently truncated. If text size without considering
 * current truncation is desired, use calculateTextSize().
 * 
 * @since 2.0
 */
protected Dimension calculateSubStringTextSize(){
	return FigureUtilities.getTextExtents(getSubStringText(),getFont());
}

/**
 * Calculates and returns the size of the Label's text. Note that 
 * this Dimension is calculated using the Label's full text, regardless
 * of whether or not its text is currently truncated. If text size
 * considering current truncation is desired, use calculateSubStringSize()
 * 
 * @since 2.0
 */
protected Dimension calculateTextSize(){
	return FigureUtilities.getTextExtents(getText(), getFont());
}

private void clearLocations(){
	iconLocation = textLocation = null;
}

/**
 * Returns the Label's icon
 * 
 * @since 2.0
 */
public Image getIcon(){return icon;}

/**
 * Returns the current alignment of the Label's icon.
 * Default is {@link PositionConstants PositionConstants.CENTER}
 * 
 * @since 2.0
 */
public int getIconAlignment(){return iconAlignment;}

/**
 * Returns the bounds of the Label's icon.
 * 
 * @since 2.0
 */
public Rectangle getIconBounds(){
	Rectangle bounds = getBounds();
	return new Rectangle(
		bounds.getLocation().translate(getIconLocation()),
		iconSize);
}

/**
 * Returns the location of the Label's icon relative to the Label
 * 
 * @since 2.0
 */
protected Point getIconLocation(){
	if (iconLocation == null)
		calculateLocations();
	return iconLocation;
}

/**
 * Returns the gap in pixels between the Label's icon and
 * its text.
 * 
 * @since 2.0
 */
public int getIconTextGap(){return iconTextGap;}

public Dimension getMinimumSize(){
	if (minSize != null)
		return minSize;
	Dimension size = new Dimension();
	if (getLayoutManager() != null)
		size.copyFrom(getLayoutManager().getMinimumSize(this));
	
	Dimension labelSize = calculateLabelSize( FigureUtilities.getTextExtents(ELLIPSIS,getFont()));
	Insets insets = getInsets();
	labelSize.expand(insets.getWidth(),insets.getHeight());
	minSize = size.getUnioned(labelSize);
	return minSize;	
}

public Dimension getPreferredSize(){
	if (prefSize != null)
		return prefSize;
	Dimension size = new Dimension();
	if (getLayoutManager() != null)
		size.copyFrom(getLayoutManager().getPreferredSize(this));

	Dimension labelSize = calculateLabelSize(getTextSize());
	Insets insets = getInsets();
	labelSize.expand(insets.getWidth(),insets.getHeight());
	prefSize = size.getUnioned(labelSize);
	return prefSize;
}

/**
 * Calculates the amount of the Label's current text will fit
 * in the Label, including an elipsis "..." if truncation is
 * required. Returns this text. 
 * 
 * @since 2.0
 */
public String getSubStringText(){
	if (subStringText != null)
		return subStringText;
	
	subStringText = text;
	int widthShrink = getPreferredSize().width - getSize().width;
	if(widthShrink <= 0)
		return subStringText;
	
	Dimension effectiveSize = getTextSize().getExpanded(-widthShrink,0);
	Font currentFont = getFont();
	int dotsWidth = FigureUtilities.getTextWidth(ELLIPSIS, currentFont);
	
	if(effectiveSize.width < dotsWidth )
		effectiveSize.width = dotsWidth;
	
	int subStringLength = FigureUtilities.getLargestSubstringConfinedTo(text,
												  currentFont,
												  effectiveSize.width - dotsWidth);
	subStringText = new String(text.substring(0,subStringLength) + ELLIPSIS);
	return subStringText;
}

/**
 * Returns the size of the Label's current text. If the text is
 * currently truncated, the truncated text with its ellipsis 
 * is used to calculate the size.
 * 
 * @since 2.0
 */
protected Dimension getSubStringTextSize(){
	if (subStringTextSize == null)
		subStringTextSize = calculateSubStringTextSize();
	return subStringTextSize;	
}

/**
 * Returns the text of the Label. Note that this is the complete
 * text of the Label, regardless of if it is currently being 
 * truncated. Call getSubStringText() to return the Label's current
 * text contents with truncation considered.
 * 
 * @since 2.0
 */
public String getText(){return text;}

/**
 * Returns the current alignment of the Label's text.
 * Default text alignment is {@link PositionConstants PositionConstants.CENTER}
 */
public int getTextAlignment(){return textAlignment;}

/**
 * Returns the bounds of the Label's text. Note that the
 * bounds are calculated using the Label's complete text
 * regardless of whether the Label's text is currently
 * truncated.
 * 
 * @since 2.0
 */
public Rectangle getTextBounds(){
	Rectangle bounds = getBounds();
	return new Rectangle(
		bounds.getLocation().translate(getTextLocation()),
		textSize);
}

/**
 * Returns the location of the Label's text relative to the Label
 * 
 * @since 2.0
 */
protected Point getTextLocation(){
	if (textLocation != null) return textLocation;
	calculateLocations();
	return textLocation;
}

/**
 * Returns the current placement of the Label's text
 * relative to its icon.
 * Default text placement is {@link PositionConstants PositionConstants.EAST}
 * 
 * @since 2.0
 */ 
public int getTextPlacement(){return textPlacement;}

/**
 * Returns the size of the Label's complete text. Note
 * that the text used to make this calculation is the 
 * Label's full text, regardless of whether the Label's
 * text is currently being truncated and is displaying 
 * an ellipsis. If the size considering current truncation
 * is desired, call getSubStringTextSize().
 * 
 * @since 2.0
 */
protected Dimension getTextSize(){
	if (textSize == null)
		textSize = calculateTextSize();
	return textSize;
}

public void invalidate(){
	prefSize = null;
	clearLocations();
	textSize = null;
	subStringTextSize = null;
	subStringText = null;
	super.invalidate();
}

/**
 * Returns true if the Label's text is currently
 * truncated and is displaying an ellipsis, false
 * otherwise.
 * 
 * @since 2.0
 */
public boolean isTextTruncated(){
	return !getSubStringText().equals(getText());
}

protected void paintFigure(Graphics graphics){
	if (isOpaque())
		super.paintFigure(graphics);
	Rectangle bounds = getBounds();
	graphics.translate(bounds.x, bounds.y);
	if (icon != null)
		graphics.drawImage(icon, getIconLocation());
	if( !isEnabled() ){
		graphics.translate(1,1);
		graphics.setForegroundColor(ColorConstants.buttonLightest);
		graphics.drawText(getSubStringText(), getTextLocation());
		graphics.translate(-1,-1);
		graphics.setForegroundColor(ColorConstants.buttonDarker);
	}
	graphics.drawText(getSubStringText(), getTextLocation());
	graphics.translate(-bounds.x, -bounds.y);
}

/**
 * Sets the Label's icon to the passed image
 * 
 * @param image The desired icon for the Label
 * 
 * @since 2.0
 */
public void setIcon(Image image){
	if (icon == image) return;
	icon = image;
	//Call repaint, in case the image dimensions are the same.
	repaint();
	if (icon == null)
		setIconDimension(new Dimension());
	else
		setIconDimension(new Dimension(image));
}

/**
 * Sets the icon alignment relative to the Label's alignment to the passed value.
 * Default is org.eclipse.draw2d.PositionConstants.CENTER
 * 
 * @param align The desired icon alignment
 * Valid values are integer constants CENTER,TOP,BOTTOM,LEFT,RIGHT
 * in {@link PositionConstants}
 * 
 * @since 2.0
 */
public void setIconAlignment (int align){
	if (iconAlignment == align)
		return;
	iconAlignment =align;
	clearLocations();
	repaint();
}

/**
 * Sets the Label's icon size to the passed Dimension.
 * 
 * @since 2.0
 */
public void setIconDimension(Dimension d){
	if (d.equals(iconSize)) return;
	iconSize = d;
	revalidate();
}

/**
 * Sets the gap in pixels between the Label's icon 
 * and text to the passed value. Default is 4.
 * 
 * @since 2.0
 */
public void setIconTextGap(int gap){
	if (iconTextGap == gap) return;
	iconTextGap = gap;
	revalidate();
}

/**
 * Sets the Label's alignment to the passed value.
 * Default is org.eclipse.draw2d.PositionConstants.CENTER
 * 
 * @param align The desired label alignment. Valid values are
 * the integer constants TOP, CENTER, BOTTOM, RIGHT, LEFT in
 * {@link PositionConstants}
 */
public void setLabelAlignment(int align){
	if( labelAlignment == align) return;
	labelAlignment = align;
	clearLocations();
	repaint();
}

/**
 * Sets the Label's text to the passed String.
 * 
 * @since 2.0
 */
public void setText(String s){
	//"text" will never be null.
	if (s == null)
		s = "";//$NON-NLS-1$
	if (text.equals(s)) return;
	text = s;
	revalidate();
	repaint();  //If the new text does not cause a new size, we still need to paint.
}

/**
 * Sets the text alignment of the Label relative to the label alignment
 * Default is org.eclipse.draw2d.PositionConstants.CENTER
 * 
 * @param align The desired text alignment. Valid values are
 * the integer constants TOP, CENTER, BOTTOM, RIGHT, LEFT in
 * {@link PositionConstants}
 * 
 * @since 2.0
 */
public void setTextAlignment(int align){
	if (textAlignment == align) return;
	textAlignment = align;
	clearLocations();
	repaint();
}

/**
 * Sets the text placement of the Label relative to its icon.
 * Default is org.eclipse.draw2d.PositionConstants.EAST
 * 
 * @param where The desired text placement. Valid values are
 * the integer constants NORTH, SOUTH, EAST, WEST in
 * {@link PositionConstants}
 * 
 * @since 2.0
 */
public void setTextPlacement (int where){
	if (textPlacement == where)
		return;
	textPlacement = where;
	revalidate();
	repaint();
}

}