package swt.experimentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class EnterExitTest {

public static void main(String[] args) {

	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	shell.setLayout(new FillLayout(SWT.VERTICAL));

	final Button button1 = new Button(shell, SWT.TOGGLE);
	button1.setText("Button 1");

	final Button button2 = new Button(shell, SWT.TOGGLE);
	button2.setText("Button 2");

	button1.addMouseTrackListener(new MouseTrackAdapter() {
		public void mouseEnter(MouseEvent e) {
			System.out.println("mouse entered button 1");
		}
	});
	
	button2.addMouseTrackListener(new MouseTrackAdapter() {
		public void mouseExit(MouseEvent e) {
			System.out.println("mouse exited button 2");
		}
	});

	shell.setSize(600,500);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();

}

}
