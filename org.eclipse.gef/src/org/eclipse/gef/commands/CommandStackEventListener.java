/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

/**
 * A listener interface for receiving notification before and after commands are
 * executed, undone, or redone.
 *
 * @since 3.1
 */
public interface CommandStackEventListener {

	/**
	 * Sent when an event occurs on the command stack.
	 * {@link CommandStackEvent#getDetail()} can be used to identify the type of
	 * event which has occurred.
	 *
	 * @since 3.1
	 * @param event the event
	 */
	void stackChanged(CommandStackEvent event);

}
