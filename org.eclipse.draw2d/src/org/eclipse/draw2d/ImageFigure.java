package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Pratik Shah
 */
public class ImageFigure 
	extends Figure 
{

private Image img;
private Dimension size;
private int alignment;

public ImageFigure(){
	setAlignment(PositionConstants.CENTER);
}

public ImageFigure(Image image){
	this();
	setImage(image);
}

public Image getImage(){
	return img;
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension size = new Dimension();
	if( getImage() != null ){
		org.eclipse.swt.graphics.Rectangle imgSize = getImage().getBounds();
		size.width = imgSize.width;
		size.height = imgSize.height;
	}
	return size;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics graphics) {
	int x, y;
	Rectangle area = getClientArea();
	switch (alignment & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.NORTH:
			y = area.y;
			break;
		case PositionConstants.SOUTH:
			y = area.y + area.height - size.height;
			break;
		default:
			y = (area.height - size.height) / 2 + area.y;
			break;
	}
	switch (alignment & PositionConstants.EAST_WEST) {
		case PositionConstants.EAST:
			x = area.x;
			break;
		case PositionConstants.WEST:
			x = area.x + area.width - size.width;
			break;
		default:
			x = (area.width - size.width) / 2 + area.x;
			break;
	}
	graphics.drawImage(getImage(), x, y);
}

public void setAlignment(int flag) {
	alignment = flag;
}

public void setImage(Image image){
	img = image;
	size = new Rectangle(image.getBounds()).getSize();
	revalidate();
	repaint();
}

}
