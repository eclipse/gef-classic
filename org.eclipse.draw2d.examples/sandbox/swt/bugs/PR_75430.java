package swt.bugs;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class PR_75430 {
public static void main(String[] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	StackLayout layout = new StackLayout();
	shell.setLayout(layout);
	final Canvas parent = new Canvas(shell, SWT.BORDER);
	layout.topControl = parent;
	Composite child = new Composite(parent, SWT.NONE);
	child.setBackground(new Color(null, 255, 0, 0));
	child.setBounds(100, 100, 50, 50);
	shell.setSize(250, 250);
	shell.open();
	display.timerExec(1000, new Runnable() {
		public void run() {
			parent.redraw();
			parent.update();
    		parent.scroll(0, -20, 0, 0, 250, 250, true);
    		parent.update();
			GC gc = new GC (parent);
			gc.setBackground(new Color(null, 200, 230, 240));
			gc.fillRectangle(0, 0, 250, 230);
			gc.drawRectangle (0, 20, 249, 229);
			gc.dispose ();
    		display.timerExec(1000, this);
		}
	});
    while (!shell.isDisposed())
        if (!display.readAndDispatch())
        	display.sleep();
}
}
