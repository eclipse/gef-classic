package org.eclipse.gef.palette.test;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.palette.*;
import java.util.*;
//import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.jface.resource.ImageDescriptor;

public class TestSliderPalette implements PaletteListener{

Canvas canvas = null;
Shell sh = new Shell();
Canvas panel = null;
Canvas paletteCanvas = null;
protected PaletteViewer slp = null;
Label label = null;
Button model1=null, model2=null, model3=null;

public static void main(String args[]){
	TestSliderPalette slpt = new TestSliderPalette();
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

	panel = new Canvas(canvas,SWT.NO_REDRAW_RESIZE | SWT.BORDER);
	panel.setBackground(org.eclipse.draw2d.ColorConstants.white);
	paletteCanvas = new Canvas(canvas, SWT.NO_REDRAW_RESIZE | SWT.BORDER);
	paletteCanvas.setBackground(org.eclipse.draw2d.ColorConstants.buttonLightest);
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


static public PaletteRoot createLogicPalette1() {
// Create the palette
java.util.List categories = new ArrayList();
DefaultPaletteRoot palette = new DefaultPaletteRoot(categories);

// Create the control group
DefaultPaletteGroup controlGroup = new DefaultPaletteGroup("Control Group");
java.util.List controlEntries = new ArrayList();
	ImageDescriptor imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	controlEntries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	controlEntries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
controlGroup.setChildren(controlEntries);
//palette.setControlGroup(controlGroup);
categories.add(controlGroup);

DefaultPaletteCategory category = new DefaultPaletteCategory("Default");
categories.add(category);
java.util.List groups = new ArrayList();
DefaultPaletteGroup group;
{
	group = new DefaultPaletteGroup("Simple Parts");
	groups.add(group);
	java.util.List entries = new ArrayList();
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	//palette.setDefaultEntry((PaletteEntry)entries.get(1));
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	group.setChildren(entries);
}
category.setChildren(groups);
palette.setChildren(categories);
return palette;
}


static public PaletteRoot createLogicPalette2() {
// Create the palette
java.util.List categories = new ArrayList();
DefaultPaletteRoot palette = new DefaultPaletteRoot(categories);

// Create the control group
DefaultPaletteGroup controlGroup = new DefaultPaletteGroup("Control Group");
java.util.List controlEntries = new ArrayList();
	ImageDescriptor imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	DefaultPaletteEntry defEntry = new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage());
	defEntry.setDefault(true);
	controlEntries.add(defEntry);
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	controlEntries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
controlGroup.setChildren(controlEntries);
//palette.setControlGroup(controlGroup);
categories.add(controlGroup);

DefaultPaletteCategory category = new DefaultPaletteCategory("Default");
categories.add(category);
java.util.List groups = new ArrayList();
DefaultPaletteGroup group;
{
	group = new DefaultPaletteGroup("Simple Parts");
	groups.add(group);
	java.util.List entries = new ArrayList();
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	//palette.setDefaultEntry((PaletteEntry)entries.get(2));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	group.setChildren(entries);
}
{
	group = new DefaultPaletteGroup("Complex Parts");
	groups.add(group);
	java.util.List entries = new ArrayList();
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/halfadder16.gif");
	entries.add(new DefaultPaletteEntry(
		"HalfAdder",
		"Create a half-adder",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/fulladder16.gif");
	entries.add(new DefaultPaletteEntry(
		"FullAdder",
		"Create a full-adder",
		imageDesc.createImage(),
		imageDesc.createImage()));
	group.setChildren(entries);
}
category.setChildren(groups);
DefaultPaletteCategory newcategory = new DefaultPaletteCategory("NewOption");
categories.add(newcategory);
java.util.List newgroups = new ArrayList();
{
	group = new DefaultPaletteGroup("Simple Parts");
	newgroups.add(group);
	java.util.List entries = new ArrayList();
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	group.setChildren(entries);
}
newcategory.setChildren(newgroups);
DefaultPaletteCategory newcategory1 = new DefaultPaletteCategory("Very NewOption");
categories.add(newcategory1);
newgroups = new ArrayList();
{
	group = new DefaultPaletteGroup("Simple Parts");
	newgroups.add(group);
	java.util.List entries = new ArrayList();
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/ledicon16.gif");
	entries.add(new DefaultPaletteEntry(
		"LED", "Create an LED",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/circuit16.gif");
	entries.add(new DefaultPaletteEntry(
		"Circuit", "Create a circuit",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/or16.gif");
	entries.add(new DefaultPaletteEntry(
		"OR Gate", "Create an OR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	imageDesc = ImageDescriptor.createFromFile
		(TestSliderPalette.class,"icons/xor16.gif");
	entries.add(new DefaultPaletteEntry(
		"XOR Gate", "Create an XOR gate",
		imageDesc.createImage(),
		imageDesc.createImage()));
	group.setChildren(entries);
}
newcategory1.setChildren(newgroups);
palette.setChildren(categories);
return palette;
}

}


