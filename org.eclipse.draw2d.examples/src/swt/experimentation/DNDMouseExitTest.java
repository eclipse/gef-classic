package swt.experimentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class DNDMouseExitTest {

public static void main(String[] args) {
	Shell shell = new Shell();
	shell.setLayout(new FillLayout());
	shell.addListener(SWT.MouseExit, new Listener(){
		public void handleEvent(Event event) {
			System.out.println("exit");
		}
	});
	shell.addMouseListener(new MouseAdapter() {
		public void mouseDown(MouseEvent e) {
			System.out.println("down");
		}
		public void mouseUp(MouseEvent e) {
			System.out.println("up");
		}
	});
	DragSource ds = new DragSource(shell, DND.DROP_COPY);
	ds.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	ds.addDragListener(new DragSourceAdapter() {
		public void dragStart(DragSourceEvent event) {
			System.out.println("Drag Start");
		}
	});
	shell.open();
	Display display = Display.getDefault();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
