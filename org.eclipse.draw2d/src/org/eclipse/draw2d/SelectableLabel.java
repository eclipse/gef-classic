package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * A label that can be selected and focused similar to a
 * {@link Clickable} but without the button-like behavior.
 * 
 * @author Eric Bordeau
 */
public class SelectableLabel extends Label {

private boolean selected = false;

/**
 * Constructor for SelectableLabel.
 */
public SelectableLabel() {
	super();
}

/**
 * Creates a label with the given text.
 * @param s The label text.
 */
public SelectableLabel(String s) {
	super(s);
}

/**
 * Creates a label with the given image.
 * @param i The label icon.
 */
public SelectableLabel(Image i) {
	super(i);
}

/**
 * Creates a label with the given text and image.
 * @param s The label text.
 * @param i The label icon.
 */
public SelectableLabel(String s, Image i) {
	super(s, i);
}

public void handleFocusGained(FocusEvent event) {
	repaint();
}

public void handleFocusLost(FocusEvent event) {
	repaint();
}

/**
 * Returns whether this label is in a selected state
 * or not. The model is the one which holds all this state
 * based information.
 *
 * @return  Selected state of this label.
 * @see  #setSelected(boolean)
 * @since 2.0
 */
public boolean isSelected() {
	return selected;
}

protected void paintBorder(Graphics graphics) {
	super.paintBorder(graphics);
	if (hasFocus()) {
		graphics.setForegroundColor(ColorConstants.black);
		graphics.setBackgroundColor(ColorConstants.white);

		Rectangle textBounds = getTextBounds();
		textBounds.setSize(getSubStringTextSize());
		graphics.drawFocus(textBounds.x-2, textBounds.y, 
							textBounds.width+3, textBounds.height);
	}
}

protected void paintFigure(Graphics graphics) {
	if (isSelected()) {
		Rectangle textBounds = getTextBounds();
		textBounds.setSize(getSubStringTextSize());
		graphics.fillRectangle(textBounds.x-2, textBounds.y, textBounds.width+4, textBounds.height+1);
	}
	Rectangle bounds = getBounds();
	graphics.translate(bounds.x, bounds.y);
	if (getIcon() != null)
		graphics.drawImage(getIcon(), getIconLocation());
	if (!isEnabled()) {
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
 * Sets the selected state of this label. Since the model
 * is reposnsible for all state based information, it is 
 * informed of the state change. Extending classes can choose
 * selection information, if they do not represent any selection.
 *
 * @param value  New selected state of this label.
 * @see  #isSelected()
 * @since 2.0
 */
public void setSelected(boolean value) {
	if (selected == value)
		return;
	selected = value;
	if (selected) {
		requestFocus();
		setBackgroundColor(ColorConstants.menuBackgroundSelected);
		setForegroundColor(ColorConstants.menuForegroundSelected);
	}
	else {
		setBackgroundColor(ColorConstants.menuBackground);
		setForegroundColor(ColorConstants.menuForeground);
	}
	repaint();
}

}
