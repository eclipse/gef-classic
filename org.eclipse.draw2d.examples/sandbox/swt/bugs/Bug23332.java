package swt.bugs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Bug23332 {

public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	final StyledText text = new StyledText(shell, SWT.MULTI  | SWT.WRAP);
	text.setText(System.getProperties().toString() + System.getProperties()+ System.getProperties()+ System.getProperties());
	shell.addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event event) {
			System.out.println("RESIZE: " + shell.getBounds() + ", "
					+ shell.getClientArea());
			if (shell.getClientArea().isEmpty()) {
				long start = System.currentTimeMillis();
				text.setBounds(shell.getClientArea());
				long end = System.currentTimeMillis();
				System.out.println("Wasted time:" + (end - start));
			} else
				text.setBounds(shell.getClientArea());
			System.out.println(shell.getClientArea());
		}
	});
	shell.setSize(200, 200);
	shell.open();
	shell.setMinimized(true);
	System.out.println("BOUNDS+CLIENT: " + shell.getBounds() + ", "
			+ shell.getClientArea());
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

}