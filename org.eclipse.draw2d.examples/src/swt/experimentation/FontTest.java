package swt.experimentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FontTest {

public static void main(String[] args) {
	Shell shell = new Shell();
	shell.setLayout(new FillLayout());
	
	Canvas canvas = new Canvas(shell, SWT.NONE);
	GC gc = new GC(canvas);
	FontData data = gc.getFont().getFontData()[0];
	data.setHeight(2);
	Font f = new Font(Display.getDefault(), data);
	gc.setFont(f);
	gc.getFontMetrics();
		
	shell.open();
	Display display = Display.getDefault();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
