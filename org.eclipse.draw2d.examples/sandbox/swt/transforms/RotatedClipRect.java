package swt.transforms;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;

public class RotatedClipRect {

static float angle = 4;

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	shell.setFont(new Font(display, "Arial", 12, 0));

	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			Transform t = new Transform(display);
			t.translate(60, 60);
			t.rotate(angle);
			gc.setTransform(t);
			gc.setClipping(-20, -20, 40, 40);
			
			gc.setBackground(ColorConstants.blue);
			gc.setForeground(ColorConstants.yellow);
			gc.fillGradientRectangle(-100, -100, 200, 200, true);
			angle += 0.3;
		}
	});
	
	shell.setSize(200, 200);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

}