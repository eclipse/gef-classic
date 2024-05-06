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
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.internal;

import java.util.Objects;
import java.util.logging.Level;

/**
 * This interface is used to support logging of error messages to a generic
 * back-end and not just the native Java logger. It's primarily function is to
 * send messages to the Eclipse logging framework without adding it as a
 * dependency to Draw2D.
 *
 * By default, all log messages are simply forwarded to the Java logging
 * framework. A custom context can be set by calling
 * {@link #setContext(LoggerContext)}.
 *
 * @since 3.16
 */
public abstract class Logger {
	private static LoggerContext CONTEXT = new LoggerContext.Stub();

	/**
	 * Logs a message with severity {@code info} using this logger.
	 *
	 * @param message   the message to log
	 * @param throwable an optional throwable to associate with this status
	 */
	public abstract void info(String message, Throwable throwable);

	/**
	 * Logs a message with severity {@code warning} using this logger.
	 *
	 * @param message   the message to log
	 * @param throwable an optional throwable to associate with this status
	 */
	public abstract void warn(String message, Throwable throwable);

	/**
	 * Logs a message with severity {@code error} using this logger.
	 *
	 * @param message   the message to log
	 * @param throwable an optional throwable to associate with this status
	 */
	public abstract void error(String message, Throwable throwable);

	/**
	 * Convenience message for calling {@code info(message, null)}.
	 *
	 * @param message The message to log.
	 */
	public final void info(String message) {
		info(message, null);
	}

	/**
	 * Convenience message for calling {@code warn(message, null)}.
	 *
	 * @param message The message to log.
	 */
	public final void warn(String message) {
		warn(message, null);
	}

	/**
	 * Convenience message for calling {@code error(message, null)}.
	 *
	 * @param message The message to log.
	 */
	public final void error(String message) {
		error(message, null);
	}

	/**
	 * Updates the logging implementation to which all messages are delegated to. An
	 * exception is thrown if the argument is {@code null}.
	 *
	 * @param context The new logger context to use. Must not be {@code null}.
	 * @since 3.16
	 * @throws NullPointerException If the provided logger context is {@code null}.
	 */
	public static void setContext(LoggerContext context) throws NullPointerException {
		Objects.requireNonNull(context);
		CONTEXT = context;
	}

	/**
	 * Find or create a logger for a named subsystem. If a logger has already been
	 * created with the given name it is returned. Otherwise a new logger is
	 * created.
	 *
	 * @param clazz The logger namespace.
	 * @return a suitable logger.
	 */
	public static Logger getLogger(Class<?> clazz) {
		return CONTEXT.getLogger(clazz);
	}

	/**
	 * Default implementation that forwards all messages to the native Java logger.
	 *
	 * @since 3.16
	 */
	public static final class Stub extends Logger {
		private final java.util.logging.Logger logger;

		public Stub(Class<?> clazz) {
			logger = java.util.logging.Logger.getLogger(clazz.getName());
		}

		@Override
		public void info(String message, Throwable throwable) {
			logger.log(Level.INFO, message, throwable);
		}

		@Override
		public void warn(String message, Throwable throwable) {
			logger.log(Level.WARNING, message, throwable);
		}

		@Override
		public void error(String message, Throwable throwable) {
			logger.log(Level.SEVERE, message, throwable);
		}
	}
}
