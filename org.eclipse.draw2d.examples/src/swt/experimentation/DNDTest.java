package swt.experimentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
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
	shell.open();
	Display display = Display.getDefault();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
