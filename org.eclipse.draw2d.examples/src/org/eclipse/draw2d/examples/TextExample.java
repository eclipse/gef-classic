package org.eclipse.draw2d.examples;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;

/**
 * 
 * Created on :Nov 15, 2002
 * @author hudsonr
 * @since 2.0
 */
public class TextExample extends AbstractExample {

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	FlowPage page = new FlowPage();
	
	page.add(
		new TextFlow("faoewi jfoiewa jfoiewajfoiwajefoi jewaoifewaf j"));
	
	return page;
}

public static void main(String[] args) {
	new TextExample().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#setFigureCanvas(FigureCanvas)
 */
protected void setFigureCanvas(FigureCanvas canvas) {
	super.setFigureCanvas(canvas);
	canvas.getViewport().setContentsTracksWidth(true);
}

}
