package swt.transforms;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;

public class RegionTransforms {

static float angle = 4;

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	shell.setFont(new Font(display, "Arial", 12, 0));

	final Region region = new Region();
	region.add(10, 10, 100, 150);
	

	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			Transform t = new Transform(display);			
			t.rotate(-15);
			gc.setTransform(t);
			t.dispose();
			
			Region clipping = new Region();
			gc.setClipping(region);
			System.out.println("original " + region.getBounds());
			gc.getClipping(clipping);
			System.out.println("transformed " + clipping.getBounds());
			clipping.dispose();
			
			gc.setTransform(t = new Transform(display));
			t.dispose();
			gc.setBackground(ColorConstants.blue);
			gc.fillRectangle(e.x, e.y, e.width, e.height);
			
			t.dispose();
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