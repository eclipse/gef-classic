package com.ibm.etools.gef.palette.test;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import com.ibm.etools.gef.ui.palette.*;
import com.ibm.etools.gef.palette.*;
import java.util.*;
//import com.ibm.etools.gef.examples.logicdesigner.model.*;
import org.eclipse.jface.resource.ImageDescriptor;
import java.util.List;

public class TestPaletteContainer implements PaletteListener{

Canvas canvas = null;
Shell sh = new Shell();
Canvas panel = null;
Canvas paletteCanvas = null;
protected PaletteViewer slp = null;
Label label = null;
Button model1=null, model2=null, model3=null;

public static void main(String args[]){
	TestPaletteContainer slpt = new TestPaletteContainer();
	slpt.startIt();
}

protected void startIt(){
	canvas = new Canvas(sh,SWT.NO_REDRAW_RESIZE);

	sh.addControlListener (new org.eclipse.swt.events.ControlAdapter () {
		public void controlResized (org.eclipse.swt.events.ControlEvent e) {
			org.eclipse.swt.graphics.Rectangle area=sh.getClientArea();
			canvas.setBounds(area);
			int ht = 25;
			panel.setSize(new Point(area.width,ht-5));
			paletteCanvas.setBounds(area.x, area.y+ht,area.width,(area.height-ht));
		}
	});
	
	addContents(canvas);
	
	sh.setSize(200,600);
	sh.open();
	Display display = Display.getDefault();
	while(!sh.isDisposed())
		if(!display.readAndDispatch())display.sleep();
}

protected void addContents(Canvas canvas){

	panel = new Canvas(canvas,SWT.NO_REDRAW_RESIZE);
	paletteCanvas = new Canvas(canvas, SWT.NO_REDRAW_RESIZE);
	panel.setLayout(new org.eclipse.swt.layout.FillLayout());
	slp = new PaletteViewerImpl();
	slp.setControl(paletteCanvas);
	slp.setPaletteRoot(createLogicPalette2());

	label = new Label(panel,SWT.NONE);
	label.setText("Start the Palette!");

	slp.addPaletteListener(this);

	model1 = new Button(panel,SWT.PUSH);
	model1.setText("Model 1");
	model1.addMouseListener(new MouseListener(){
		public void mouseUp(MouseEvent me){
			slp.setPaletteRoot(createLogicPalette1());
		}
		public void mouseDown(MouseEvent me){;}
		public void mouseDoubleClick(MouseEvent me){;}
	});
	
	model2 = new Button(panel,SWT.PUSH);
	model2.setText("Model 2");
	model2.addMouseListener(new MouseListener(){
		public void mouseUp(MouseEvent me){
			slp.setPaletteRoot(createLogicPalette2());
		}
		public void mouseDown(MouseEvent me){;}
		public void mouseDoubleClick(MouseEvent me){;}
	});
	
}

public void newDefaultEntry(PaletteEvent event){
	PaletteEntry entry=(PaletteEntry)event.getEntry();
	String text = entry==null?"null":entry.getLabel();
	label.setText("<  "+text+"  >");
}

public void entrySelected(PaletteEvent event){
	PaletteEntry entry=(PaletteEntry)event.getEntry();
	String text = entry==null?"null":entry.getLabel();
	label.setText("[  "+text+"  ]");
}

/** 
 * GC
 * CG
 * CCG
 * GGC
 */
public PaletteRoot createLogicPalette1(){
	List rootChildren = new ArrayList();
	DefaultPaletteRoot paletteRoot = new DefaultPaletteRoot(rootChildren);
	
	DefaultPaletteCategory cat1 = new DefaultPaletteCategory("Category1");
	List cat1Children = new java.util.ArrayList();
	cat1.setChildren(cat1Children);

	DefaultPaletteEntry pe1 = new DefaultPaletteEntry("LED", "Create an LED", 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/circuit16.gif").createImage(), 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/circuit16.gif").createImage());
	cat1Children.add(pe1);
	
	DefaultPaletteCategory cat1cat1 = new DefaultPaletteCategory("Category1-Sub1");
	List cat1cat1Children = new java.util.ArrayList();
	cat1cat1.setChildren(cat1cat1Children);
	
	pe1 = new DefaultPaletteEntry("Circuit", "Create an Circuit", 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/circuit16.gif").createImage(), 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/circuit16.gif").createImage());
	cat1cat1Children.add(pe1);
	pe1 = new DefaultPaletteEntry("Circuit", "Create an Circuit", 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/circuit16.gif").createImage(), 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/circuit16.gif").createImage());
	cat1cat1Children.add(pe1);
	DefaultPaletteGroup cat1cat1grp1 = new DefaultPaletteGroup("Group1");
	List cat1cat1grp1Children = new java.util.ArrayList();
	cat1cat1grp1.setChildren(cat1cat1grp1Children);
	pe1 = new DefaultPaletteEntry("LED", "Create an LED", 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/ledicon16.gif").createImage(), 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/ledicon16.gif").createImage());
	cat1cat1grp1Children.add(pe1);
	pe1 = new DefaultPaletteEntry("LED", "Create an LED", 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/ledicon16.gif").createImage(), 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/ledicon16.gif").createImage());
	cat1cat1grp1Children.add(pe1);
	cat1cat1Children.add(cat1cat1grp1);
	cat1Children.add(cat1cat1);


	DefaultPaletteCategory cat1cat1cat1grp1cat1 = new DefaultPaletteCategory("Category1");
	List cat1cat1cat1grp1cat1Children = new java.util.ArrayList();
	cat1cat1cat1grp1cat1.setChildren(cat1cat1cat1grp1cat1Children);
	pe1 = new DefaultPaletteEntry("AND", "Create an AND", 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/and16.gif").createImage(), 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/and16.gif").createImage());
	cat1cat1cat1grp1cat1Children.add(pe1);
	cat1cat1grp1Children.add(cat1cat1cat1grp1cat1);


	pe1 = new DefaultPaletteEntry("XOR", "Create an XOR", 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/xor16.gif").createImage(), 
		ImageDescriptor.createFromFile(TestSliderPalette.class,"icons/xor16.gif").createImage());
	cat1Children.add(pe1);

	rootChildren.add(cat1);	
	return paletteRoot;
}

public PaletteRoot createLogicPalette2(){
	return null;
}

}


