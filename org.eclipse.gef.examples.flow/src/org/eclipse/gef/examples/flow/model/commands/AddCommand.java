package org.eclipse.gef.examples.flow.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.StructuredActivity;

/**
 * AddCommand
 * @author Daniel Lee
 */
public class AddCommand extends Command {

private Activity child;
private StructuredActivity parent;
private int index = -1;

/**
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	if( index < 0 )
		parent.addChild(child);
	else
		parent.addChild(child,index);
}

/**
 * Returns the StructuredActivity that is the parent
 * @return the parent
 */
public StructuredActivity getParent() {
	return parent;
}

/**
 * Sets the child to the passed Activity
 * @param subpart the child
 */
public void setChild(Activity newChild) {
	child = newChild;
}

/**
 * Sets the index of the child
 * @param i the child index
 */
public void setIndex(int i){
	index = i;
}

/**
 * Sets the parent to the passed StructuredActiivty
 * @param newParent the parent
 */
public void setParent(StructuredActivity newParent) {
	parent = newParent;
}

/**
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	parent.removeChild(child);
}

}
