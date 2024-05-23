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

package org.eclipse.zest.tests.utils;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Utility class for traversing all widgets of a given composite. Entry point of
 * this visitor is the {@code traverse()} method. The visitor traverses the
 * widget in a depth-first fashion, starting with the first child of each
 * composite.<br>
 * The visitor is interrupted if {@code false} is returned by the
 * {@link #visit(Widget)} method.<br>
 * Example:
 *
 * <pre>
 * new WidgetVisitor() {
 *   &commat;Override
 *   public boolean visit(Widget w) {
 *     ...
 *     return true;
 *   }
 * }.traverse(widget);
 * </pre>
 */
public interface WidgetVisitor {
	/**
	 * This method is called for each widget that is traversed via
	 * {@link #traverse(Widget)}.
	 *
	 * @param w the widget to traverse
	 * @return {@code false}, if the traversal should be interrupted, otherwise
	 *         {@code true}.
	 */
	boolean visit(Widget w);

	/**
	 * If the given widget is an instance of {@link Composite}, it recursively
	 * traverses its children. The traversal continues until all widgets have been
	 * visited or a visit operation returns {@code false}.
	 *
	 * @param w the widget to traverse
	 * @return {@code true} if the traversal completes without interruption,
	 *         otherwise {@code false}.
	 */
	default boolean traverse(Widget w) {
		if (visit(w) && w instanceof Composite composite) {
			for (Control child : composite.getChildren()) {
				if (!traverse(child)) {
					return false;
				}
			}
		}
		return true;
	}
}
