package swt.experimentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;

public class Draw2dLineTest {

public static void main(String[] args) {
	Shell shell = new Shell();
	shell.setLayout(new FillLayout());
	
	FigureCanvas canvas = new FigureCanvas(shell);
	Figure contents = new Figure();
	Polyline conn = new Polyline();
	conn.addPoint(new Point(74, 78));
	conn.addPoint(new Point(250, 255));
	contents.add(conn);
	canvas.setContents(contents);
	
	final Canvas canvas2 = new Canvas(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
	canvas2.setBackground(new Color(null, 255, 255, 255));
	canvas2.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			GC gc = new GC(canvas2);
			Rectangle rect = e.gc.getClipping();
			gc.setClipping(e.gc.getClipping());
			Image image = new Image(null, 1024,1024);
			GC offscreen = new GC(image);
			offscreen.setLineWidth(1);
			offscreen.setBackground(gc.getBackground());
			offscreen.setForeground(gc.getForeground());
			offscreen.fillRectangle(0, 0, 1024, 1024);
			offscreen.setClipping(74,78,251-74,256-78);
			offscreen.drawPolyline(new int[] {74, 78, 250, 255});
			offscreen.dispose();
			gc.drawImage(image, rect.x, rect.y, rect.width, rect.height,
				rect.x, rect.y, rect.width, rect.height);
			image.dispose();
			gc.dispose();
		}
	});

	shell.open();
	Display display = Display.getDefault();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
	display.dispose();

}

}
