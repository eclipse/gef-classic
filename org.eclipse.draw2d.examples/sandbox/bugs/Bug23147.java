package bugs;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug23147 {

static Map map;
	
static class Key {
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("disposing Key");
	}
}

static class Value {
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("disposing Value");
	}
}

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell();
	
	shell.addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			e.gc.setLineWidth(2);
			e.gc.drawRectangle(100,100,200,150);
			e.gc.drawRoundRectangle(100,100,200,150, 00, 0);
//			e.gc.drawText(Integer.toString(map.size()), 10, 10);
		}
	});

	buildMap();
	
	shell.setSize(400,300);
	shell.open();
	
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

/**
 * @since 3.1
 * 
 */
private static void buildMap() {
	map = new WeakHashMap();
	map.put(new Key(), new Value());
}

}