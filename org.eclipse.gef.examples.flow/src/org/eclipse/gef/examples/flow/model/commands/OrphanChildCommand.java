package org.eclipse.gef.examples.flow.model.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.StructuredActivity;

/**
 * OrphanChildCommand
 * @author Daniel Lee
 */
public class OrphanChildCommand extends Command {

private StructuredActivity parent;
private Activity child;
private int index;

/**
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	List children = parent.getChildren();
	index = children.indexOf(child);
	parent.removeChild(child);
}

/**
 * Sets the child to the passed Activity
 * @param child the child
 */
public void setChild(Activity child) {
	this.child = child;
}

/**
 * Sets the parent to the passed StructuredActivity
 * @param parent the parent
 */
public void setParent(StructuredActivity parent) { 
	this.parent = parent;
}

/**
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	parent.addChild(child, index);
}

}
