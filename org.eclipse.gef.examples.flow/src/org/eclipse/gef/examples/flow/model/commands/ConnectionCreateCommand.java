/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.Transition;

/**
 * @author Daniel Lee
 */
public class ConnectionCreateCommand extends Command {

	/** The Transition between source and target Activities **/
	protected Transition transition;
	/** The source Activity **/
	protected Activity source;
	/** The target Activity **/
	protected Activity target;

	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (source.equals(target))
			return false;

		// Check for existence of connection already
		return source.getOutgoingTransitions().stream().noneMatch(t -> t.target.equals(target));
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		transition = new Transition(source, target);
	}

	/**
	 * Returns the source Activity
	 * 
	 * @return the source
	 */
	public Activity getSource() {
		return source;
	}

	/**
	 * Returns the target Activity
	 * 
	 * @return the target
	 */
	public Activity getTarget() {
		return target;
	}

	/**
	 * Returns the Transistion between the source and target Activities
	 * 
	 * @return the transistion
	 */
	public Transition getTransition() {
		return transition;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		source.addOutput(transition);
		target.addInput(transition);
	}

	/**
	 * Sets the source Activity
	 * 
	 * @param activity the source Activity
	 */
	public void setSource(Activity activity) {
		source = activity;
	}

	/**
	 * Sets the Transistion between the source and target Activities
	 * 
	 * @param transition the transistion
	 */
	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	/**
	 * Sets the target Activity
	 * 
	 * @param activity the target
	 */
	public void setTarget(Activity activity) {
		target = activity;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		source.removeOutput(transition);
		target.removeInput(transition);
	}

}
