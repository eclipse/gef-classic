package org.eclipse.draw2d.examples.printing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PrintFigureOperation;
import org.eclipse.draw2d.PrintOperation;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;

/**
 * @author danlee
 */
public class PrintExample {

static FigureCanvas canvas;
static Figure printFigure;
static Figure internalPrintFigure;
static int count = 1;

public static void main(String[] args) {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	
	shell.setLayout(new GridLayout(1,false)); 
	
	Button printButton = new Button(shell,SWT.PUSH);
	printButton.setText("Print it");
	printButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

	GridData canvasData = new GridData(GridData.FILL_BOTH);
	canvasData.horizontalSpan =2;
	canvas = new FigureCanvas(shell);
	canvas.setLayoutData(canvasData);
	
	LightweightSystem lws = new LightweightSystem(canvas);

	final Figure printFigure = new Figure ();
	printFigure.setMinimumSize(new Dimension(5,5));
	addAllFigures(printFigure);	
	lws.setContents(printFigure);
	
	printButton.addMouseListener(new MouseListener() {
		public void mouseDoubleClick(MouseEvent e) {
		}

		public void mouseDown(MouseEvent e) {
			printIt(printFigure);
		}
		public void mouseUp(MouseEvent e) {
		}
	});
	shell.setSize(300,500);
	shell.open();
	
	while (!shell.isDisposed())
		if (!d.readAndDispatch())
			d.sleep();
		
}

static void addAllFigures(IFigure parent){
	Font fonts[] = {
					new Font(null,"Helvetica",16,SWT.NONE),
					new Font(null,"Times New Roman",24,SWT.BOLD),
					new Font(null,"Perpetua",48,SWT.NONE)
	};
	
	parent.setLayoutManager(new FlowLayout());
	org.eclipse.draw2d.Button button = new org.eclipse.draw2d.Button("Button");
	RectangleFigure rf = new RectangleFigure();
	rf.setSize(50,50);
	Ellipse e = new Ellipse();
	e.setSize(50,50);
	RoundedRectangle rr = new RoundedRectangle();
	rr.setSize(50,50);
	
	for(int i = 0; i < fonts.length; i++) {
		org.eclipse.draw2d.Label label = new org.eclipse.draw2d.Label(fonts[i].getFontData()[0].getName());
		label.setFont(fonts[i]);
		label.setBorder(new LineBorder());
		parent.add(label);
	}
	Figure rect1 = new RectangleFigure();
	rect1.setSize(50,50);
	Figure rect2 = new RectangleFigure();
	rect2.setSize(50,50);
	
	PolylineConnection c = new PolylineConnection();
	ChopboxAnchor chop = new ChopboxAnchor(rect1);
	c.setSourceAnchor(chop);
	c.setTargetAnchor(new ChopboxAnchor(rect2));
	
	Label icon = new Label(new Image(null, PrintExample.class.getResourceAsStream("brazil.ico")));
	
	parent.add(rf);
	parent.add(e);
	parent.add(rr);
	parent.add(button);
	parent.add(rect1);
	parent.add(rect2);
	parent.add(icon);
	parent.add(c);
}

static private void printIt(IFigure fig){
	Printer p = new Printer();
	PrintOperation op = new PrintFigureOperation(p, fig);
	op.setPrintMargin(new Insets(0,0,0,0));
	op.run("Test");  // "Test" is the print job name
	p.dispose();
}

}