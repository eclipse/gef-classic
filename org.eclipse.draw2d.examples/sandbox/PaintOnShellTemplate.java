import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PaintOnShellTemplate {

public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	
	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			
		}
	});
	
	shell.setSize(500, 380);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

}