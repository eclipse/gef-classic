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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.CursorProvider;

/**
 * Convenience class for creating providers over system cursors.
 */
public class SystemCursorProvider implements CursorProvider {
	private final int which;

	public SystemCursorProvider(int which) {
		this.which = which;
	}

	/**
	 * Returns the matching standard platform {@link Cursor} for the given constant,
	 * which should be one of the {@link Cursor} constants specified in class
	 * {@link SWT}. This {@link Cursor} should not be free'd because it was
	 * allocated by the system, not the application. A value of {@code null} will be
	 * returned if the supplied constant is not an {@link SWT} cursor constant.
	 * Note: This method should be invoked from within the UI thread if possible, as
	 * it will attempt to create a new Display instance, if not!
	 *
	 * @return the corresponding {@link Cursor} or {@code null}
	 */
	@Override
	public Cursor get() {
		Display display = Display.getCurrent();
		if (display != null) {
			return display.getSystemCursor(which);
		}
		display = Display.getDefault();
		return display.syncCall(() -> Display.getCurrent().getSystemCursor(which));
	}
}
