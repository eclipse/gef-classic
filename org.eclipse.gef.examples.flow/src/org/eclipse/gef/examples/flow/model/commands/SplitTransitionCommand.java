package org.eclipse.gef.examples.flow.model.commands;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.flow.model.*;

/**
 * SplitTransitionCommand
 * @author Daniel Lee
 */
public class SplitTransitionCommand extends Command {

private StructuredActivity parent;
private Activity oldSource;
private Activity oldTarget;
private Transition transition;

private Activity newActivity;
private Transition newIncomingTransition;
private Transition newOutgoingTransition;

/**
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	oldSource.removeOutput(transition);
	oldTarget.removeInput(transition);
	parent.addChild(newActivity);
	newIncomingTransition = new Transition(oldSource, newActivity);
	newOutgoingTransition = new Transition(newActivity, oldTarget);
}

/**
 * @see org.eclipse.gef.commands.Command#redo()
 */
public void redo() {
	oldSource.addOutput(newIncomingTransition);
	oldTarget.addInput(newOutgoingTransition);
	newActivity.addInput(newIncomingTransition);
	newActivity.addOutput(newOutgoingTransition);
	parent.addChild(newActivity);
	oldSource.removeOutput(transition);
	oldTarget.removeInput(transition);
}

/**
 * Sets the parent Activity. The new Activity will be added as a child to the parent.
 * @param activity the parent
 */
public void setParent(StructuredActivity activity) {
	parent = activity;
}

/**
 * Sets the transition to be "split". 
 * @param transition the transition to be "split".
 */
public void setTransition(Transition transition) {
	this.transition = transition;
	oldSource = transition.source;
	oldTarget = transition.target;	
}

/**
 * Sets the Activity to be added.
 * @param activity the new activity
 */
public void setNewActivity(Activity activity) {
	newActivity = activity;
	newActivity.setName("a " + (parent.getChildren().size() + 1));
}

/**
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	oldSource.removeOutput(newIncomingTransition);
	oldTarget.removeInput(newOutgoingTransition);
	newActivity.removeInput(newIncomingTransition);
	newActivity.removeOutput(newOutgoingTransition);
	parent.removeChild(newActivity);
	oldSource.addOutput(transition);
	oldTarget.addInput(transition);	
}

}
