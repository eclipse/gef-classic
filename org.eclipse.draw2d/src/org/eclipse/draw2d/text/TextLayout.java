package org.eclipse.draw2d.text;

import java.util.List;

/**
 * @author hudsonr
 * @since 2.1
 */
public abstract class TextLayout extends FlowFigureLayout {

/**
 * Reuses an existing <code>TextFragmentBox</code>, or creates a new one.
 * @param i the index * @param fragments the original list of fragments * @return a TextFragmentBox */
protected TextFragmentBox getFragment(int i, List fragments){
	if (fragments.size() > i)
		return (TextFragmentBox)fragments.get(i);
	TextFragmentBox box = new TextFragmentBox();
	fragments.add(box);
	return box;
}

public TextLayout(TextFlow flow) {
	super(flow);
}	

}
