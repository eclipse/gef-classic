package org.eclipse.draw2d.examples.scrollpane;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.examples.*;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The scrollpane example shows how draw2d uses getPreferredSize() to determine when
 * scrollbars should be displayed.  One window shows a view of a fixed-sized figure.  The
 * other shows a FlowLayout that wraps from left-to-right, and then top-to-bottom.
 * @author hudsonr
 */
public class ScrollPaneExample extends AbstractExample {

int count = 1;

public static void main(String[] args) {
	new ScrollPaneExample().run();
}

protected IFigure getContents() {
	Figure mainPanel = new Figure();
	mainPanel.add(
		newScrollingFrame("Flow layouts",
			ExampleUtil.createToolbarLayout()));

	Figure fixedSize = new Figure();
	fixedSize.setBorder(new LineBorder());
	fixedSize.setPreferredSize(100,100);
	mainPanel.add(
		newScrollingFrame("fixed 100x100",
			fixedSize));

	return mainPanel;
}

private InternalFrame newScrollingFrame(String title, IFigure contents){
	InternalFrame frame = new InternalFrame();
	frame.setLabel(title);
	ScrollPane scrollpane = new ScrollPane();
	scrollpane.setBorder(new GroupBoxBorder("scrollpane"));
//	scrollpane.setScrollBarVisibility(scrollpane.ALWAYS);
	scrollpane.getViewport().setContentsTracksWidth(true);
	scrollpane.setContents(contents);
	frame.add(scrollpane);
	frame.setBounds(new Rectangle(count*20, count*20, 200,140));
	return frame;
}

}
