package org.eclipse.draw2d.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.Thumbnail;

/**
 * This example demonstrates an overview window
 * @author hudsonr
 */
public class ThumbnailExample {

private static Figure contents;
private static Shell mainShell, overviewShell;
private static Dimension offset = new Dimension();

public static void main(String[] args) {
	Display display = new Display();
	
	mainShell = new Shell(display);
	mainShell.setText("Source Shell");
	mainShell.setLayout(new FillLayout());
	FigureCanvas mainCanvas = new FigureCanvas(mainShell);
	mainCanvas.setContents(getContents());
	
	overviewShell = new Shell(display, SWT.TITLE| SWT.RESIZE | SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
	overviewShell.setText("Overview Shell");
	overviewShell.setLayout(new FillLayout());
	LightweightSystem overviewLWS = new LightweightSystem(overviewShell);
	overviewLWS.setContents(createThumbnail(getContents()));

	mainShell.setSize(600, 600);
	mainShell.open();
	overviewShell.setSize(200, 200);
	overviewShell.open();

	while (!mainShell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
	mainShell.dispose();
	overviewShell.dispose();
}


protected static Figure getContents() {
	if (contents == null)
		contents = createContents();
	return contents;
}

private static Figure createContents() {	
	Figure contents = new Figure();
	contents.setBorder(new LineBorder());
	contents.setLayoutManager(new XYLayout());
	final Figure figure1 = new RectangleFigure();
	figure1.setBackgroundColor(ColorConstants.green);
	figure1.setBounds(new Rectangle(50, 50, 200, 200));
	figure1.addMouseListener(new MouseListener.Stub() {
		public void mousePressed(MouseEvent event) {
			offset.width = event.x - figure1.getLocation().x;
			offset.height = event.y - figure1.getLocation().y;
		}
		public void mouseReleased(MouseEvent event) {
			offset.width = 0;
			offset.height = 0;
		}
	});
	figure1.addMouseMotionListener(new MouseMotionListener.Stub() {
		public void mouseDragged(MouseEvent event) {
			Rectangle rect = figure1.getBounds().getCopy();
			rect.x = event.x - offset.width;
			rect.y = event.y - offset.height;
			figure1.setBounds(rect);
		}
	});
	contents.add(figure1);
	final Figure figure2 = new RectangleFigure();
	figure2.setBackgroundColor(ColorConstants.blue);
	figure2.setBounds(new Rectangle(350, 350, 150, 200));
	figure2.addMouseListener(new MouseListener.Stub() {
		public void mousePressed(MouseEvent event) {
			offset.width = event.x - figure2.getLocation().x;
			offset.height = event.y - figure2.getLocation().y;
		}
		public void mouseReleased(MouseEvent event) {
			offset.width = 0;
			offset.height = 0;
		}
	});
	figure2.addMouseMotionListener(new MouseMotionListener.Stub() {
		public void mouseDragged(MouseEvent event) {
			Rectangle rect = figure2.getBounds().getCopy();
			rect.x = event.x - offset.width;
			rect.y = event.y - offset.height;
			figure2.setBounds(rect);
		}
	});
	contents.add(figure2);
	return contents;
}

protected static Figure createThumbnail(Figure source) {
	Thumbnail thumbnail = new Thumbnail();
	thumbnail.setBorder(new GroupBoxBorder("Overview Figure"));
	thumbnail.setSource(source);
	return thumbnail;
}

}
