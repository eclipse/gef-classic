package swt.experimentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class FocusTest {

public static void main(String[] args) {
	Display display = Display.getDefault();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	
	final Canvas canvas = new Canvas(shell, SWT.H_SCROLL | SWT.V_SCROLL);
	canvas.getVerticalBar().setVisible(false);
	canvas.getHorizontalBar().setVisible(false);
	Text text = new Text(canvas, SWT.SINGLE);
	text.setSize(100, 20);
	text.addVerifyListener(new VerifyListener() {
		public void verifyText(VerifyEvent e) {
			canvas.getVerticalBar().setVisible(false);
		}
	});
	text.addFocusListener(new FocusAdapter() {
		public void focusLost(FocusEvent e) {
			System.out.println("Focus Lost");
		}
	});
	
	shell.setSize(600, 500);
	shell.open();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
