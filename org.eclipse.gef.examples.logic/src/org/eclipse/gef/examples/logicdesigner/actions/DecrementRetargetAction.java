package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.LogicPlugin;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author hudsonr
 * @since 2.1
 */
public class DecrementRetargetAction extends RetargetAction {

/**
 * Constructor for IncrementRetargetAction.
 * @param actionID
 * @param label
 */
public DecrementRetargetAction() {
	super(IncrementDecrementAction.DECREMENT,
		LogicMessages.IncrementDecrementAction_Decrement_ActionLabelText);
	setToolTipText(LogicMessages.IncrementDecrementAction_Decrement_ActionToolTipText);
	setImageDescriptor(ImageDescriptor
		.createFromFile(LogicPlugin.class,"icons/minus.gif")); //$NON-NLS-1$
}

}
