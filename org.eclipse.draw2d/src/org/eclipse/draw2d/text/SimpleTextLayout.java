package org.eclipse.draw2d.text;

import java.util.List;

import org.eclipse.swt.graphics.Font;

/**
 * @author hudsonr
 * @since 2.1
 */
public class SimpleTextLayout extends TextLayout {

private static final String[] DELIMITERS = {
	"\r\n", //$NON-NLS-1$
	 "\n", //$NON-NLS-1$
	 "\r"};//$NON-NLS-1$

private static int result;
private static int delimeterLength;

public SimpleTextLayout(TextFlow flow) {
	super (flow);
}

/**
 * @see org.eclipse.draw2d.text.FlowFigureLayout#layout()
 */
protected void layout() {
	TextFlow textFlow = (TextFlow)getFlowFigure();
	String text = ((TextFlow) textFlow).getText();
	List fragments = textFlow.getFragments();
	Font font = textFlow.getFont();
	TextFragmentBox fragment;
	int i = 0;
	int offset = 0;
	
	while (offset < text.length()) {
		nextLineBreak(text, offset);
		fragment = getFragment(i++, fragments);
		fragment.length = result - offset;
		fragment.offset = offset;
		FlowUtilities.setupFragment(fragment, font, text);
		context.getCurrentLine().add(fragment);
		context.endLine();
		offset = result + delimeterLength;
	}
	//Remove the remaining unused fragments.
	while (i < fragments.size())
		fragments.remove(i++);
}

private int nextLineBreak(String text, int offset) {
	result = text.length();
	delimeterLength = 0;
	int current;
	for (int i = 0; i < DELIMITERS.length; i++) {
		current = text.indexOf(DELIMITERS[i], offset);
		if (current != -1 && current < result) {
			result = current;
			delimeterLength = DELIMITERS[i].length();
		}
	}
	return result;
}

}
