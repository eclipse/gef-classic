package org.eclipse.draw2d.widgets;

import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;

/**
 * A widget for displaying a multi-line string. The label will have a vertical or
 * horizontal scrollbar when needed. Unlike the platform Label, this label is focusable
 * and accessible to screen-readers.
 * 
 * @author hudsonr
 */
public final class MultiLineLabel extends FigureCanvas {

private TextFlow textFlow;

class FocusableViewport extends Viewport {
	FocusableViewport() {
		super(true);
	}
	
	{
		setFocusTraversable(true);
		setBorder(new MarginBorder(2));
	}
	
	public void handleFocusGained(FocusEvent event) {
		super.handleFocusGained(event);
		repaint();
	}

	public void handleFocusLost(FocusEvent event) {
		super.handleFocusLost(event);
		repaint();
	}

	protected void paintBorder(Graphics graphics) {
		super.paintBorder(graphics);
		if (hasFocus()) {
			graphics.setForegroundColor(ColorConstants.black);
			graphics.setBackgroundColor(ColorConstants.white);
			graphics.drawFocus(getBounds().getResized(-1,-1));
		}
	}
}

/**
 * @param parent
 */
public MultiLineLabel(Composite parent) {
	super(parent);
	setViewport(new FocusableViewport());
	FlowPage page = new FlowPage();
	textFlow = new TextFlow();
	page.add(textFlow);
	setContents(page);
	getViewport().setContentsTracksWidth(true);
	getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_LABEL;
		}
	});
	getAccessible().addAccessibleListener(new AccessibleAdapter() {
		public void getName(AccessibleEvent e) {
			e.result = getText();
		}
	});
}

public String getText() {
	return textFlow.getText();
}

/**
 * @see org.eclipse.swt.widgets.Canvas#setFont(org.eclipse.swt.graphics.Font)
 */
public void setFont(Font font) {
	super.setFont(font);
	textFlow.revalidate();
}

public void setText(String text) {
	textFlow.setText(text);
}

}
