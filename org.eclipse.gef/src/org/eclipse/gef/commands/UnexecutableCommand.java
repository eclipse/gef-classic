package org.eclipse.gef.commands;

/**
 * A Command which can never be executed
 */
public class UnexecutableCommand
	extends AbstractCommand
{

/**
 * The singleton instance
 */
public static final UnexecutableCommand INSTANCE = new UnexecutableCommand();

private UnexecutableCommand() { }

/** * @return <code>false</code>
 */
public boolean canExecute() {
	return false;
}

/**
 * @return <code>false</code>
 */
public boolean canUndo() {
	return false;
}

}
