package org.eclipse.gef.ui.palette.customize;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * <code>PaletteCustomizationAction</code>s are used to manipulate the
 * palette model.  They can enable and disable themselves when needed.
 * 
 * <p> 
 * This class is mainly a result of code-factoring.
 * </p>
 * 
 * @author Pratik Shah
 */
public abstract class PaletteCustomizationAction
	extends Action
{

/**
 * Call this method to have the action update its state and enable or disable itself.
 */
public abstract void update();

/**
 * @see org.eclipse.jface.action.Action#setImageDescriptor(ImageDescriptor)
 */
public void setImageDescriptor(ImageDescriptor newImage) {
	super.setImageDescriptor(newImage);
	setHoverImageDescriptor(newImage);
}


}
