import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

public class ShellTestor {

static int x,y;

public static void main(String[] args) {
	Display d = Display.getDefault();
	final Shell shell = new Shell(d, SWT.H_SCROLL |SWT.V_SCROLL| SWT.DIALOG_TRIM);
	
	Text text = new Text(shell, SWT.BORDER);
	text.setBounds(100,10,80,23);
	text.setText("foobar");
	
	shell.getVerticalBar().addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			int dy = shell.getVerticalBar().getSelection() - y;
			shell.scroll(0, 0, x, dy, 200, 200, true);
			y = shell.getVerticalBar().getSelection();
		}
	});
	
	shell.setSize(600,500);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();

}

}
