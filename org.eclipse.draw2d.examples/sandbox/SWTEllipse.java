import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
public class SWTEllipse {

static int W = 55;
static int H = 36;

static int C = 11;

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell();
	
	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			e.gc.setBackground(ColorConstants.yellow);
			e.gc.fillRectangle(20-1,20-1,W+2, H+2);

			e.gc.setLineWidth(4);
			e.gc.setBackground(ColorConstants.white);
			e.gc.setForeground(ColorConstants.black);
			e.gc.fillRoundRectangle(20,20,W+1,H+1, C+8, C+8);

			e.gc.setLineWidth(8);
			e.gc.drawRoundRectangle(20+4,20+4,W-7,H-7, C, C);

//			e.gc.setLineWidth(7);
//			e.gc.drawRectangle(100+3,20+3,W-6,H-6);

			e.gc.setForeground(ColorConstants.yellow);
			e.gc.setLineWidth(1);
//			e.gc.drawRectangle(20,20,W,H);
		}
	});
	
	shell.setSize(400,300);
	shell.open();
	
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();

}

}
