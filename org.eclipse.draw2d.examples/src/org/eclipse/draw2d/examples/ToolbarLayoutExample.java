package org.eclipse.draw2d.examples;

import org.eclipse.draw2d.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ToolbarLayoutExample extends AbstractExample {

public static void main(String[] args) {
	new ToolbarLayoutExample().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	getFigureCanvas().getViewport().setContentsTracksWidth(true);
	
	Figure container = new Figure();
	ToolbarLayout tbl = new ToolbarLayout(false);
	tbl.setSpacing(2);
	container.setLayoutManager(tbl);
	
	Figure title = new Figure();
	title.setLayoutManager(new BorderLayout());
	title.setBorder(new LineBorder());
	title.add(new Button("f oaiejfiuhiuhiuhiuho aiewapkpfoakewpofk jfoaiewj "), BorderLayout.CENTER);
	RectangleFigure rf = new RectangleFigure();
	rf.setBackgroundColor(ColorConstants.yellow);
	rf.setOutline(false);
	rf.setSize(25,14);
	title.add(rf, BorderLayout.RIGHT);
	container.add(title);
	return container;
}

}
