package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import java.util.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;

public class FlowDemo
{

static int KEYS_TYPED = 0;
static boolean PERFORMANCE = false;
static FigureCanvas canvas;
static TextFigure target;
static FlowPage page;
static Font
	regularFont = new Font(Display.getDefault(), "Arial", 15, SWT.NORMAL),
	boldFont = new Font(Display.getDefault(), "Comic Sans MS", 16, SWT.BOLD);

public static void main(String[] args) {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	shell.setLayout(new GridLayout());

	canvas = new FigureCanvas(shell);
	canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
	canvas.setScrollBarVisibility(canvas.ALWAYS);
	shell.open();

	canvas.addKeyListener(new KeyAdapter(){
		public void keyPressed(KeyEvent e){
			addText(e.character);
		}
	});

	page = new FlowPage();
	populatePage();
	canvas.setContents(page);

	while (!shell.isDisposed())
		if (!d.readAndDispatch())
			d.sleep();
}

static protected void addText(char c){
	if ((c <= 'Z' && c >='A') ||
	    (c <= 'z' && c >='a') ||
	    (c ==' '))
	if (target != null){
		target.setText(target.getText()+c);
		if (PERFORMANCE && (KEYS_TYPED % 10 == 0)){
			System.out.println(KEYS_TYPED + " keys typed " + FlowPage.VALIDATIONS + " paints and layouts");
		}
		KEYS_TYPED++;
	}
}

static public void populatePage(){

	target = new TextFigure("Normal text.");
	page.add(target);

	for (int i=0; i<30; i++){
		FlowFigure ff = FlowDemoFactory.newInline();
		ff.add(new TextFigure("A light blue flow"));
		ff.add(new FakeFlow());
		ff.setBackgroundColor(ColorConstants.lightBlue);
		page.add(ff);
	
		FlowFigure inline = FlowDemoFactory.newInline();
		inline.setBackgroundColor(ColorConstants.yellow);
		FlowDemoFactory.addSentences(inline,5);
		ff.add(inline);
		
		FlowFigure block = FlowDemoFactory.newBlock();
		FlowDemoFactory.addFontSizes(block, 10,30);
		page.add(block);
	}
}

}