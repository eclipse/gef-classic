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

package org.eclipse.gef.internal;

/**
 * The Eclipse/RCP specific logger-context for Draw2D.
 *
 * @since 3.16
 */
public final class LoggerContext implements org.eclipse.draw2d.internal.LoggerContext {
	@Override
	public Logger getLogger(Class<?> clazz) {
		return new Logger(clazz);
	}
}
