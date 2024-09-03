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

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.zest.core.widgets.Graph;

/**
 * This annotation is used for testing the example classes, in order to indicate
 * which test covers which class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { METHOD })
public @interface Snippet {
	/**
	 * @return The class under test.
	 */
	Class<?> type();

	/**
	 * @return The name of the static field containing the {@link Graph}.
	 */
	String field() default "g";

	/**
	 * @return Return {@code true} when nodes should be placed randomly (if
	 *         supported by the layout).
	 */
	boolean random() default false;
}
