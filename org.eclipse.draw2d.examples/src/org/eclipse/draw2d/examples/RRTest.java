package org.eclipse.draw2d.examples;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

public class RRTest {

public static void main(String[] args) {
	Shell shell = new Shell();
	shell.setLayout(new FillLayout());
	
	FigureCanvas canvas = new FigureCanvas(shell);
	Figure contents = new Figure();
	contents.setBackgroundColor(ColorConstants.white);
	contents.setOpaque(true);
	
	RoundedRectangle rr1 = new RoundedRectangle();
	rr1.setCornerDimensions(new Dimension(25, 25));
	rr1.setBackgroundColor(ColorConstants.lightGray);
	rr1.setOpaque(true);
	rr1.setBounds(new Rectangle(10, 10, 100, 50));
	contents.add(rr1);
	
	RoundedRectangle rr2 = new RoundedRectangle();
	rr2.setCornerDimensions(new Dimension(24, 24));
	rr2.setBackgroundColor(ColorConstants.lightGray);
	rr2.setOpaque(true);
	rr2.setBounds(new Rectangle(120, 10, 100, 50));
	contents.add(rr2);
	
	canvas.setContents(contents);
	
	shell.open();
	Display display = Display.getDefault();
	while(!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
	display.dispose();
}

}
