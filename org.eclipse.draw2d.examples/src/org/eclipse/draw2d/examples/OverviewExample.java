package org.eclipse.draw2d.examples;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.Dock;
import org.eclipse.draw2d.parts.DockLocator;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class OverviewExample {

private static Figure leftContents;
private static Dimension offset = new Dimension();
private static Thumbnail thumbnail;

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout(2, true));
	FigureCanvas leftCanvas = new FigureCanvas(shell);
	leftCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
	leftCanvas.setContents(getLeftContents());
	FigureCanvas rightCanvas = new FigureCanvas(shell);
	rightCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
	rightCanvas.setContents(getRightContents());
	shell.setSize(600, 300);
	shell.open();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}


protected static Figure getLeftContents() {
	leftContents = new Figure();
	leftContents.setBorder(new LineBorder());
	leftContents.setLayoutManager(new XYLayout());
	Figure figure1 = new RectangleFigure();
	figure1.setBackgroundColor(ColorConstants.green);
	figure1.setBounds(new Rectangle(50, 50, 200, 200));
	leftContents.add(figure1);
	Figure figure2 = new RectangleFigure();
	figure2.setBackgroundColor(ColorConstants.blue);
	figure2.setBounds(new Rectangle(350, 350, 150, 200));
	leftContents.add(figure2);
	return leftContents;
}

protected static Figure getRightContents() {
	LayeredPane topLayers = new LayeredPane();

	Layer scrollPaneLayer = new Layer();
	LayeredPane innerLayers = new LayeredPane();

	Layer layer = new Layer();
	layer.setLayoutManager(new StackLayout());
	innerLayers.add(layer, "primary layer");

	ScrollPane pane = new ScrollPane();
	pane.setViewport(new Viewport(true));
	pane.setView(innerLayers);

	scrollPaneLayer.setLayoutManager(new StackLayout());
	scrollPaneLayer.add(pane);
	topLayers.add(scrollPaneLayer);

	Layer dockLayer = new Layer();
	dockLayer.setLayoutManager(new DelegatingLayout());
	thumbnail = new ScrollableThumbnail(pane.getViewport());
	thumbnail.setSource(leftContents);
	Dock dock = new Dock(pane.getViewport(), thumbnail);
	dock.setTitle("Overview");
	dockLayer.add(dock, new DockLocator());
	topLayers.add(dockLayer);

	return topLayers;
}

}
