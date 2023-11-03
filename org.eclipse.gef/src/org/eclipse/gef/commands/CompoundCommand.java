/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * An aggregation of multiple <code>Commands</code>. A
 * <code>CompoundCommand</code> is executable if all of its contained Commands
 * are executable, and it has at least one contained Command. The same is true
 * for undo. When undo is called, the contained Commands are undone in the
 * reverse order in which they were executed.
 * <P>
 * An empty CompoundCommand is <em>not</em> executable.
 * <P>
 * A CompoundCommand can be {@link #unwrap() unwrapped}. Unwrapping returns the
 * simplest equivalent form of the CompoundCommand. So, if a CompoundCommand
 * contains just one Command, that Command is returned.
 */
public class CompoundCommand extends Command {

	private final List<Command> commandList = new ArrayList<>();

	/**
	 * Constructs an empty CompoundCommand
	 *
	 * @since 2.0
	 */
	public CompoundCommand() {
	}

	/**
	 * Constructs an empty CompoundCommand with the specified label.
	 *
	 * @param label the label for the Command
	 */
	public CompoundCommand(String label) {
		super(label);
	}

	/**
	 * Adds the specified command if it is not <code>null</code>.
	 *
	 * @param command <code>null</code> or a Command
	 */
	public void add(Command command) {
		if (command != null) {
			commandList.add(command);
		}
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canRedo()
	 *
	 * @since 3.10
	 */
	@Override
	public boolean canRedo() {
		if (commandList.isEmpty()) {
			return false;
		}
		for (Command cmd : commandList) {
			if (cmd == null) {
				return false;
			}
			if (!cmd.canRedo()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (commandList.isEmpty()) {
			return false;
		}
		for (Command cmd : commandList) {
			if (cmd == null) {
				return false;
			}
			if (!cmd.canExecute()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (commandList.isEmpty()) {
			return false;
		}
		for (Command cmd : commandList) {
			if (cmd == null) {
				return false;
			}
			if (!cmd.canUndo()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Disposes all contained Commands.
	 *
	 * @see org.eclipse.gef.commands.Command#dispose()
	 */
	@Override
	public void dispose() {
		commandList.forEach(Command::dispose);
	}

	/**
	 * Execute the command.For a compound command this means executing all of the
	 * commands that it contains.
	 */
	@Override
	public void execute() {
		commandList.forEach(Command::execute);
	}

	/**
	 * This is useful when implementing
	 * {@link org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)} to
	 * display the Command's nested structure.
	 *
	 * @return returns the Commands as an array of Objects.
	 */
	public Object[] getChildren() {
		return commandList.toArray();
	}

	/**
	 * @return the List of contained Commands
	 */
	public List<? extends Command> getCommands() {
		return commandList;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#getLabel()
	 */
	@Override
	public String getLabel() {
		String label = super.getLabel();
		if (label == null && commandList.isEmpty()) {
			return null;
		}
		if (label != null) {
			return label;
		}
		return commandList.get(0).getLabel();
	}

	/**
	 * @return <code>true</code> if the CompoundCommand is empty
	 */
	public boolean isEmpty() {
		return commandList.isEmpty();
	}

	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		commandList.forEach(Command::redo);
	}

	/**
	 * @return the number of contained Commands
	 */
	public int size() {
		return commandList.size();
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		for (int i = commandList.size() - 1; i >= 0; i--) {
			commandList.get(i).undo();
		}
	}

	/**
	 * Returns the simplest form of this Command that is equivalent. This is useful
	 * for removing unnecessary nesting of Commands.
	 *
	 * @return the simplest form of this Command that is equivalent
	 */
	public Command unwrap() {
		return switch (commandList.size()) {
		case 0 -> UnexecutableCommand.INSTANCE;
		case 1 -> commandList.get(0);
		default -> this;
		};
	}

}
