package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.internal.Internal;

/**
 * @author Pratik Shah
 */
public class PinCategoryAction extends Action {
	
private CategoryFigure fig;

/**
 * Constructor for PinCategoryAction.
 * @param fig
 */
public PinCategoryAction(CategoryFigure fig) {
	this.fig = fig;
//	setImageDescriptor(ImageDescriptor.createFromFile(
//			Internal.class, "icons/pin_view.gif")); //$NON-NLS-1$
	setEnabled(fig.isExpanded());
	String text;
	if (!fig.isExpanded()) {
		text = PaletteMessages.CATEGORY_PIN;
	} else {
		text = fig.isPinnedOpen() ? PaletteMessages.CATEGORY_UNPIN
	                              : PaletteMessages.CATEGORY_PIN;
	}
	setText(text);
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	fig.setPinned(!fig.isPinnedOpen());
}

}
