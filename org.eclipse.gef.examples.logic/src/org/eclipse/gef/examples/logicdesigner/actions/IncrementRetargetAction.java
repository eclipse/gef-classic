package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.ui.actions.RetargetAction;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.LogicPlugin;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author hudsonr
 * @since 2.1
 */
public class IncrementRetargetAction extends RetargetAction {

/**
 * Constructor for IncrementRetargetAction.
 * @param actionID
 * @param label
 */
public IncrementRetargetAction() {
	super(IncrementDecrementAction.INCREMENT,
		LogicMessages.IncrementDecrementAction_Increment_ActionLabelText);
	setToolTipText(LogicMessages.IncrementDecrementAction_Increment_ActionToolTipText);
	setImageDescriptor(
	ImageDescriptor.createFromFile(LogicPlugin.class,"icons/plus.gif")); //$NON-NLS-1$
}

}
