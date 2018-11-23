/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.text;

import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.examples.AbstractExample;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.draw2d.text.SimpleTextLayout;
import org.eclipse.draw2d.text.TextFlow;

/**
 * This example shows the basic ways that the draw2d.text figures can be used.
 * 
 * @author hudsonr
 * @since 2.0
 */
public class TextFlowExample extends AbstractExample {

static protected IFigure createAlignmentParagraph() {
	BlockFlow block = new BlockFlow();
	block.setHorizontalAligment(PositionConstants.RIGHT);
	block.add(new TextFlow("An inline flow figure's aligment is determined "
	  + "by the block flow in which it resides. This block is aligned using "));
	TextFlow text = new TextFlow("PositionConstants.RIGHT");
	text.setFont(COURIER);
	block.add(text);
	block.add(new TextFlow(", which results in right text aligment"));
	return block;
}

static protected IFigure createBaselineParagraph() {
	BlockFlow block = new BlockFlow();

	String message[] = {
		"Text fragments ",
		"with different ",
		"Font sizes will ",
		"have their ",
		"baseline ",
		"vertically ",
		"aligned ",
		"within ",
		"the current ",
		"line"
	};

	for (int i=0; i<message.length; i++){
		TextFlow tf = new TextFlow(message[i]);
		//This is a resource leak.
		tf.setFont(new Font(null, "Helvetica", i+8, 0));
		block.add(tf);
	}

	return block;	
}

static protected IFigure createBlockParagraph() {
	BlockFlow blockFlow = new BlockFlow();
	blockFlow.setFont(COURIER);
	TextFlow contents = new TextFlow();
	contents.setLayoutManager(new SimpleTextLayout(contents));
	contents.setText(
	  "/**\n" +
	  " * The SimpleTextLayout only breaks at newlines\n" +
	  " * It can be used to render source code.\r" +
	  " */\r\n" +
	  "public void foo() {\n" + 
	  "    //TABs are not handled currently, only spaces.\n" + 
	  "    System.out.println(\"foo\")\n" +
	  "}"
	);
	blockFlow.add(contents);
	return blockFlow;
}

static protected IFigure createNestedInlineParagraph() {
	BlockFlow blockFlow = new BlockFlow();
	TextFlow text;
	blockFlow.add(new TextFlow("A paragraph can contain multiple children, including"
	  + " nested structure using InlineFlows. "));
	
	InlineFlow inline = new InlineFlow();
	inline.setForegroundColor(ColorConstants.darkBlue);
	inline.add(new TextFlow("This TextFlow is inside an InlineFlow with a blue "
	  + "foreground color. Children can have "));
	text = new TextFlow("Bold ");
	text.setFont(BOLD);
	inline.add(text);
	
	inline.add(new TextFlow("or "));

	text = new TextFlow("Italic ");
	text.setFont(ITALICS);
	inline.add(text);

	inline.add(new TextFlow("Font, or override other inherited attributes."));
	blockFlow.add(inline);
	return blockFlow;
}

static protected IFigure createParagraph() {
	BlockFlow blockFlow = new BlockFlow();
	TextFlow contents = new TextFlow();
	contents.setText("A paragraph is created by using a BlockFlow figure. A "
	  + "paragraph usually wraps to the width of the current page. "
	  + "To see this, try resizing the example's window. "
	  + "The contents of this paragraph were created using a TextFlow figure "
	  + "in its default layout, which breaks at whitespace. "
	  + "a block of text that doesn't wrap can be created by using the "
	  + "SimpleTextLayout on a TextFlow");
	
	blockFlow.add(contents);
	return blockFlow;
}

static protected IFigure createTitle() {
	BlockFlow blockFlow = new BlockFlow();
	TextFlow contents = new TextFlow("Draw2D TextFlow Example");
	contents.setFont(HEADING_1);
	blockFlow.add(contents);
	return blockFlow;
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	FlowPage page = new FlowPage();
	page.setOpaque(true);
	page.setBackgroundColor(ColorConstants.white);
	page.add(createTitle());
	page.add(createParagraph());
	page.add(createBlockParagraph());
	page.add(createNestedInlineParagraph());
	page.add(createBaselineParagraph());
	page.add(createAlignmentParagraph());
	return page;
}

public static void main(String[] args) {
	new TextFlowExample().run();
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#setFigureCanvas(FigureCanvas)
 */
protected void setFigureCanvas(FigureCanvas canvas) {
	super.setFigureCanvas(canvas);
//	canvas.setVerticalScrollBarVisibility(canvas.ALWAYS);
	canvas.getViewport().setContentsTracksWidth(true);
}

}
