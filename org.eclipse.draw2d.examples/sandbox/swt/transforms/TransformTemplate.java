package swt.transforms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TransformTemplate {

static float angle = 4;

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	shell.setFont(new Font(display, "Arial", 12, 0));

	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			e.gc.drawRoundRectangle(15, 15, 100, 100, 20, 20);
			Transform transform = new Transform(display);
			transform.translate(5, 5);
			e.gc.setTransform(transform);
			transform.dispose();
			//e.gc.drawRoundRectangle(10, 10, 100, 100, 20, 20);
			e.gc.setAlpha(120);
			e.gc.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
			e.gc.drawRoundRectangle(10, 10, 100, 100, 20, 20);
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