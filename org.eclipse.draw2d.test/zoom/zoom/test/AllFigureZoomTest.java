package zoom.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class AllFigureZoomTest {

public static void main(String args[]){
	Display d = new Display();
	final Shell shell = new Shell(d);
	shell.setSize(400, 290);
	LightweightSystem lws = new LightweightSystem(shell);
	
	Figure fig = new Figure();
	fig.setLayoutManager(new ToolbarLayout());

	final ScrollBar bar = new ScrollBar();
	final Label l = new Label("«Zoom»");

	l.setBorder(new SchemeBorder(ButtonBorder.SCHEMES.BUTTON_SCROLLBAR));
	bar.setThumb(l);
	bar.setHorizontal(true);
	bar.setMaximum(200);
	bar.setMinimum(0);
	bar.setExtent(25);

	final ZoomWrapper panel = new ZoomWrapper();
	panel.setBorder(new GroupBoxBorder("Zooming figure"));
	panel.setMinimumSize(new Dimension(5,5));
	panel.setPreferredSize(999, 999);
	fig.add(bar);
	fig.add(panel);

	bar.addPropertyChangeListener("value", new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			float z = (bar.getValue()+10) * 0.02f;
			panel.setZoom(z);
		}
	});

	addAllFigure(panel);

	bar.setValue(40);

	lws.setContents(fig);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

static void addAllFigure(IFigure parent){
	parent.setLayoutManager(new FlowLayout());
	Button b = new Button("aewofiawef");
	b.setBackgroundColor(ColorConstants.orange);
	b.setOpaque(true);
	b.setRolloverEnabled(true);
	parent.add(b);
	parent.add(new AndGate());
	parent.add(new Button("foo"));
	parent.add(new Button("foo"));
	parent.add(new Button("foo"));
	parent.add(new ScrollBar());
}

}
