package swt.bugs;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug45640 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell();
	shell.setLayout(new GridLayout());
	final Button b1 = new Button(shell, 0);
	b1.setText ("Leak some pens");
	
	b1.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Image image = new Image(null, 100, 100);
			GC gc = new GC(image);
			gc.setLineWidth(1);
			gc.dispose();
			image.dispose();
		}
	});
	
	shell.setSize(400,300);
	shell.open();
	
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}