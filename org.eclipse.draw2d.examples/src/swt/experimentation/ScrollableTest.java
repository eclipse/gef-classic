package swt.experimentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ScrollableTest {

public static void main(String[] args) {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	shell.setLayout(new FillLayout(SWT.VERTICAL));

	final Canvas canvas = new Canvas(shell, SWT.H_SCROLL | SWT.V_SCROLL);

	canvas.addControlListener(new ControlListener() {
		public void controlMoved(ControlEvent e) {
			System.out.println("moved");
		}

		public void controlResized(ControlEvent e) {
			System.out.println("resized");
		}
	});

	final Button button = new Button(shell, SWT.TOGGLE);
	button.setText("show vertical scrollbar");
	button.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			canvas.getVerticalBar().setVisible(button.getSelection());
		}
	});

	shell.setSize(600,500);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();

}

}
