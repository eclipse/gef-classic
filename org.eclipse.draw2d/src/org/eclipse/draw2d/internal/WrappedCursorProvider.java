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

package org.eclipse.draw2d.internal;

import java.util.Objects;

import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.CursorProvider;

/**
 * Convenience class convert a cursor into a cursor provider. Use to bridge the
 * gap between the methods that still expect a {@link Cursor} and methods that
 * expect a {@link CursorProvider}.
 */
public class WrappedCursorProvider implements CursorProvider {
	private final Cursor cursor;

	public WrappedCursorProvider(Cursor cursor) {
		this.cursor = cursor;
	}

	@Override
	public Cursor get() {
		return cursor;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof WrappedCursorProvider other) {
			return Objects.equals(cursor, other.cursor);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cursor);
	}
}
