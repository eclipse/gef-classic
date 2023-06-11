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
 * Command that handles the reconnection of source Activities.
 * 
 * @author Daniel Lee
 */
public class ReconnectSourceCommand extends Command {

	/** source Activity **/
	protected Activity source;
	/** target Activity **/
	protected Activity target;
	/** transition between source and target **/
	protected Transition transition;
	/** previous source prior to command execution **/
	protected Activity oldSource;

	/**
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (transition.target.equals(source))
			return false;

		return source.getOutgoingTransitions().stream()
				.noneMatch(t -> t.target.equals(target) && !t.source.equals(oldSource));
	}

	/**
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (source != null) {
			oldSource.removeOutput(transition);
			transition.source = source;
			source.addOutput(transition);
		}
	}

	/**
	 * Returns the source Activity associated with this command
	 * 
	 * @return the source Activity
	 */
	public Activity getSource() {
		return source;
	}

	/**
	 * Returns the target Activity associated with this command
	 * 
	 * @return the target Activity
	 */
	public Activity getTarget() {
		return target;
	}

	/**
	 * Returns the Transition associated with this command
	 * 
	 * @return the Transition
	 */
	public Transition getTransition() {
		return transition;
	}

	/**
	 * Sets the source Activity associated with this command
	 * 
	 * @param activity the source Activity
	 */
	public void setSource(Activity activity) {
		source = activity;
	}

	/**
	 * Sets the target Activity assoicated with this command
	 * 
	 * @param activity the target Activity
	 */
	public void setTarget(Activity activity) {
		target = activity;
	}

	/**
	 * Sets the transition associated with this
	 * 
	 * @param trans the transition
	 */
	public void setTransition(Transition trans) {
		transition = trans;
		target = trans.target;
		oldSource = trans.source;
	}

	/**
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		source.removeOutput(transition);
		transition.source = oldSource;
		oldSource.addOutput(transition);
	}

}
