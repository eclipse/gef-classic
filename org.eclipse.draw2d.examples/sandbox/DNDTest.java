import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DNDTest {

public static void main(String[] args) {
	Shell shell = new Shell();
	shell.setLayout(new FillLayout());

	Canvas dragCanvas = new Canvas(shell, SWT.NONE);
	dragCanvas.setBackground(new Color(null, 255, 255, 255));
	dragCanvas.addMouseListener(new MouseListener() {
		public void mouseDoubleClick(MouseEvent event) {}
		public void mouseDown(MouseEvent event) {
			System.out.println("Mouse Down");
		}
		public void mouseUp(MouseEvent event) {
			System.out.println("Mouse Up");
		}
	});
	DragSource ds = new DragSource(dragCanvas, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
	ds.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	ds.addDragListener(new DragSourceListener() {
		public void dragStart(DragSourceEvent event) {
			System.out.println("Drag Start");
		}
		public void dragSetData(DragSourceEvent event) {
			event.data = "Now it works";
		}
		public void dragFinished(DragSourceEvent event) {
			System.out.println("Drag Finished");
		}
	});

	Canvas dropCanvas = new Canvas(shell, SWT.NONE);
	dropCanvas.setBackground(new Color(null, 128, 128, 128));
	DropTarget dt = new DropTarget(dropCanvas, DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
	dt.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	dt.addDropListener(new DropTargetListener() {
		public void dragEnter(DropTargetEvent event) {}
		public void dragLeave(DropTargetEvent event) {}
		public void dragOperationChanged(DropTargetEvent event) {}
		public void dragOver(DropTargetEvent event) {}
		public void drop(DropTargetEvent event) {
			System.out.println("Drop");
		}
		public void dropAccept(DropTargetEvent event) {}
	});
	
	shell.setSize(300, 200);
	shell.open();
	Display display = Display.getDefault();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
