package org.eclipse.gef.ui.palette;

import org.eclipse.draw2d.*;

/**
 * A label that can be selected and focused similar to a
 * {@link Clickable} but without the button-like behavior.
 * 
 * @author Eric Bordeau
 */
class SelectableLabel
	extends Label
{

private boolean selected = false;

public SelectableLabel(){
	setRequestFocusEnabled(true);
}

public void handleFocusGained(FocusEvent event) {
	refresh();
}

public void handleFocusLost(FocusEvent event) {
	refresh();
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
}

protected void paintFigure(Graphics graphics) {
	if (selected)
		graphics.fillRectangle(getTextBounds().getExpanded(2,2));

	super.paintFigure(graphics);
	if (hasFocus()) {
		graphics.setForegroundColor(ColorConstants.black);
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.drawFocus(getTextBounds().getExpanded(2,2));
	}
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
	if (selected){
		requestFocus();
		setForegroundColor(ColorConstants.menuForegroundSelected);
		setBackgroundColor(ColorConstants.menuBackgroundSelected);
	} else {
		setForegroundColor(null);
		setBackgroundColor(null);
	}
}

void refresh(){
	if (selected && hasFocus()){
		setForegroundColor(ColorConstants.menuForegroundSelected);
		setBackgroundColor(ColorConstants.menuBackgroundSelected);
	} else if (selected) {
		setForegroundColor(null);
		setBackgroundColor(ColorConstants.button);
	} else {
		setForegroundColor(null);
		setBackgroundColor(null);
	}
}

}
