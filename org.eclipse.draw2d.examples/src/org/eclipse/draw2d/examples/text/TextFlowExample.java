/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	protected static IFigure createAlignmentParagraph() {
		BlockFlow block = new BlockFlow();
		block.setHorizontalAligment(PositionConstants.RIGHT);
		block.add(new TextFlow(
				"An inline flow figure's aligment is determined by the block flow in which it resides. This block is aligned using ")); //$NON-NLS-1$
		TextFlow text = new TextFlow("PositionConstants.RIGHT"); //$NON-NLS-1$
		text.setFont(COURIER);
		block.add(text);
		block.add(new TextFlow(", which results in right text aligment")); //$NON-NLS-1$
		return block;
	}

	protected static IFigure createBaselineParagraph() {
		BlockFlow block = new BlockFlow();

		String[] message = { "Text fragments ", "with different ", "Font sizes will ", "have their ", "baseline ", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				"vertically ", "aligned ", "within ", "the current ", "line" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		for (int i = 0; i < message.length; i++) {
			TextFlow tf = new TextFlow(message[i]);
			// This is a resource leak.
			tf.setFont(new Font(null, "Helvetica", i + 8, 0)); //$NON-NLS-1$
			block.add(tf);
		}

		return block;
	}

	protected static IFigure createBlockParagraph() {
		BlockFlow blockFlow = new BlockFlow();
		blockFlow.setFont(COURIER);
		TextFlow contents = new TextFlow();
		contents.setLayoutManager(new SimpleTextLayout(contents));
		contents.setText("""
				/**
				 * The SimpleTextLayout only breaks at newlines
				 * It can be used to render source code.\r\
				 */\r
				public void foo() {
				    //TABs are not handled currently, only spaces.
				    System.out.println("foo")
				}"""); //$NON-NLS-1$
		blockFlow.add(contents);
		return blockFlow;
	}

	protected static IFigure createNestedInlineParagraph() {
		BlockFlow blockFlow = new BlockFlow();
		TextFlow text;
		blockFlow.add(new TextFlow(
				"A paragraph can contain multiple children, including" + " nested structure using InlineFlows. ")); //$NON-NLS-1$ //$NON-NLS-2$

		InlineFlow inline = new InlineFlow();
		inline.setForegroundColor(ColorConstants.darkBlue);
		inline.add(new TextFlow( // $NON-NLS-1$
				"This TextFlow is inside an InlineFlow with a blue " + "foreground color. Children can have ")); //$NON-NLS-1$ //$NON-NLS-2$
		text = new TextFlow("Bold "); //$NON-NLS-1$
		text.setFont(BOLD);
		inline.add(text);

		inline.add(new TextFlow("or ")); //$NON-NLS-1$

		text = new TextFlow("Italic "); //$NON-NLS-1$
		text.setFont(ITALICS);
		inline.add(text);

		inline.add(new TextFlow("Font, or override other inherited attributes.")); //$NON-NLS-1$
		blockFlow.add(inline);
		return blockFlow;
	}

	protected static IFigure createParagraph() {
		BlockFlow blockFlow = new BlockFlow();
		TextFlow contents = new TextFlow();
		contents.setText("""
				A paragraph is created by using a BlockFlow figure. A\s\
				paragraph usually wraps to the width of the current page.\s\
				To see this, try resizing the example's window.\s\
				The contents of this paragraph were created using a TextFlow figure\s\
				in its default layout, which breaks at whitespace.\s\
				a block of text that doesn't wrap can be created by using the\s\
				SimpleTextLayout on a TextFlow"""); // $NON-NLS-1$ //$NON-NLS-1$

		blockFlow.add(contents);
		return blockFlow;
	}

	protected static IFigure createTitle() {
		BlockFlow blockFlow = new BlockFlow();
		TextFlow contents = new TextFlow("Draw2D TextFlow Example"); //$NON-NLS-1$
		contents.setFont(HEADING_1);
		blockFlow.add(contents);
		return blockFlow;
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#createContents()
	 */
	@Override
	protected IFigure createContents() {
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
	@Override
	protected void setFigureCanvas(FigureCanvas canvas) {
		super.setFigureCanvas(canvas);
		canvas.getViewport().setContentsTracksWidth(true);
	}

}
