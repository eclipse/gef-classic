/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.gef.examples.text;

/**
 * @since 3.1
 */
public interface AppendableCommand {

/**
 * Returns <code>true</code> if the command has pending, uncommitted changes which can be
 * executed. If this method return <code>true</code>, {@link #executePending()} may be
 * called later.  Otherwise, {@link #flushPending()} should be called to free up any 
 * @since 3.1
 * @return <code>true</code> if pending execution can continue
 */
boolean canExecutePending();

/**
 * Executes any pending changes for this command.  Immediately after calling this method,
 * {@link #canExecutePending()} should return <code>false</code>.
 * @since 3.1
 */
void executePending();

/**
 * Flushes any pending changes which were not executed. This method should be called to
 * free up memory consumed by changes for which {@link #canExecutePending()} returned
 * <code>false</code>.  Also, calling this method resets any pending changes so that
 * pending changes coming afterwards may be applied. This method may be called regardless
 * of the return value for {@link #canExecutePending()}.
 * @since 3.1
 */
void flushPending();

}
