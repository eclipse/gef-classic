package zoom.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;

public class ComposedZoomTest {

public static void main(String args[]){
	Display d = new Display();
	final Shell shell = new Shell(d);
	shell.setSize(400, 290);
	LightweightSystem lws = new LightweightSystem(shell);
	
	Figure fig = new Figure();
	fig.setLayoutManager(new ToolbarLayout());

	ZoomFigure zf = new ZoomFigure();
	zf.setPreferredSize(500,500);
	fig.add(zf);

	lws.setContents(fig);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

}
