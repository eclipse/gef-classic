package org.eclipse.gef.commands;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * An Abstract implementation of {@link Command}.
 * @author hudsonr
 * @since 2.0 */
public abstract class AbstractCommand
	implements Command
{

private String label;

private String debugLabel;

/**
 * Constructs a Command with no label.
 */
public AbstractCommand () { }

/**
 * Constructs a Command with the specified label.
 * @param label the Command's label */
public AbstractCommand (String label) {
	setLabel(label);
}

/**
 * @see org.eclipse.gef.commands.Command#canExecute() */
public boolean canExecute() {
	return true;
}
/** * @see org.eclipse.gef.commands.Command#canUndo() */
public boolean canUndo() {
	return true;
}

/** * @see org.eclipse.gef.commands.Command#chain(Command) */
public Command chain(Command command) {
	if (command == null)
		return this;
	class ChainedCompoundCommand
		extends CompoundCommand
	{
		public Command chain(Command c) {
			add(c);
			return this;
		}
	}
	CompoundCommand result = new ChainedCompoundCommand();
	result.setDebugLabel("Chained Commands"); //$NON-NLS-1$
	result.add(this);
	result.add(command);
	return result;
}

/**
 * Does nothing by default.
 * @see org.eclipse.gef.commands.Command#dispose() */
public void dispose() { }

/** * Does nothing by default.
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() { }

/**
 * @return an untranslated String used for debug purposes only
 */
public String getDebugLabel() {
	return debugLabel + ' ' + getLabel();
}

/**
 * @see org.eclipse.gef.commands.Command#getLabel()
 */
public String getLabel() {
	return label;
}

/**
 * Calls <code>execute()</code> by default.
 * @see org.eclipse.gef.commands.Command#redo() */
public void redo() {
	execute();
}

/**
 * Sets the debug label for this command
 * @param label a description used for debugging only */
public void setDebugLabel(String label) {
	debugLabel = label;
}

/**
 * Sets the label for this Command.
 * @param label the label */
public void setLabel(String label) {
	this.label = label;
}

/**
 * Does nothing by default.
 * @see org.eclipse.gef.commands.Command#undo() */
public void undo() { }

}