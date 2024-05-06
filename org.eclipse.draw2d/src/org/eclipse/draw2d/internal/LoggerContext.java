/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
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

package org.eclipse.draw2d.internal;

/**
 * This class is used to maintain the logger namespace for each context. Context
 * hereby describes the logging framework (i.e. the backend) to which all
 * messages are delegated to.
 *
 * @since 3.16
 */
public interface LoggerContext {
	/**
	 * Find or create a logger for a named subsystem. If a logger has already been
	 * created with the given name it is returned. Otherwise a new logger is
	 * created.
	 *
	 * @param clazz The logger namespace.
	 * @return a suitable logger.
	 */
	Logger getLogger(Class<?> clazz);

	/**
	 * Default implementation using the native Java logging framework as context.
	 *
	 * @since 3.16
	 */
	static final class Stub implements LoggerContext {
		@Override
		public Logger getLogger(Class<?> clazz) {
			return new Logger.Stub(clazz);
		}
	}
}
