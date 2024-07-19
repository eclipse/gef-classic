/*******************************************************************************
 * Copyright (c) 2024 IBM Corporation and others.
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

package org.eclipse.draw2d;

import java.util.function.Supplier;

import org.eclipse.swt.graphics.Cursor;

/**
 * This interface is intended to be used by Draw2D to handle dynamic DPI
 * changes, requiring a refresh of the underlying cursor resource. A call to
 * {@link #get()} must always return a cursor object whose resolution matches
 * the current display zoom.
 *
 * @since 3.17
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface CursorProvider extends Supplier<Cursor> {
}
