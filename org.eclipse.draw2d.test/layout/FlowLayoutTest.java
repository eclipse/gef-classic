import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class FlowLayoutTest {

public static void main(String args[]){
	Display d = new Display();
	final Shell shell = new Shell(d);
	shell.setSize(400, 290);
	LightweightSystem lws = new LightweightSystem(shell);
	
	Figure fig = new Figure();
	
	Figure flowTest = new Figure(){
		protected boolean useLocalCoordinates() {
			return true;
		}
	};
	fig.add(flowTest);

	flowTest.setLayoutManager(new FlowLayout());
	flowTest.setBorder(new GroupBoxBorder("flow layout"));
	flowTest.setBounds(new Rectangle(50,90,160,100));
	for (int i=0; i<10; i++){
		flowTest.add(new Label("foooo"));
	}

	lws.setContents(fig);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

}
