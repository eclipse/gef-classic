package bidi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class RTL_StyledText {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(SWT.SHELL_TRIM | SWT.RIGHT_TO_LEFT);

	shell.setLayout(new GridLayout());
	new StyledText(shell, SWT.RIGHT_TO_LEFT | SWT.MULTI)
		.setLayoutData(new GridData(GridData.FILL_BOTH));
	shell.getChildren()[0].setFont(new Font(null, "", 20, 0));
	
	shell.setSize(400, 300);
	shell.open();

	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}