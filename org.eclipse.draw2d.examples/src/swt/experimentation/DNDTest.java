package swt.experimentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DNDTest {

public static void main(String[] args) {
	Shell shell = new Shell();
	shell.setLayout(new FillLayout());
	Canvas canvas = new Canvas(shell, SWT.NONE);
	DragSource ds = new DragSource(canvas, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
	ds.addDragListener(new DragSourceListener() {
		public void dragStart(DragSourceEvent event) {
			System.out.println("Drag Start");
		}
		public void dragSetData(DragSourceEvent event) {
			System.out.println("Drag Set Data");
		}
		public void dragFinished(DragSourceEvent event) {
			System.out.println("Drag Finished");
		}
	});
	canvas.addMouseListener(new MouseListener() {
		public void mouseDoubleClick(MouseEvent event) {}
		public void mouseDown(MouseEvent event) {
			System.out.println("Mouse Down");
		}
		public void mouseUp(MouseEvent event) {
			System.out.println("Mouse Up");
		}
	});
	shell.open();
	Display display = Display.getDefault();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
