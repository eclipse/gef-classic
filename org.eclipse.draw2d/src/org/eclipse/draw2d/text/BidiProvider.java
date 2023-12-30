/*******************************************************************************
 * Copyright (c) 2023 IBM Corporation and others.
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

package org.eclipse.draw2d.text;

import java.text.Bidi;

/**
 * <p>
 * An abstract class for service providers that provide concrete implementations
 * of the {@link java.text.Bidi Bidi} class.
 * </p>
 *
 * Example:
 *
 * <pre>
 * import com.ibm.icu.text.Bidi;
 *
 * public class CustomBidiProvider implements BidiProvider {
 * 	&#64;Override
 * 	public boolean requiresBidi(char[] text, int start, int limit) {
 * 		return Bidi.requiresBidi(text, start, limit);
 * 	}
 * }
 * </pre>
 *
 * @since 3.15
 */
public interface BidiProvider {

	/**
	 * Return {@code true} if the specified text requires bidi analysis. If this
	 * returns {@code false}, the text will display left-to-right. Clients can then
	 * avoid constructing a Bidi object. Text in the Arabic Presentation Forms area
	 * of Unicode is presumed to already be shaped and ordered for display, and so
	 * will not cause this function to return {@code true}.
	 *
	 * @param text  the text containing the characters to test
	 * @param start the start of the range of characters to test
	 * @param limit the limit of the range of characters to test
	 * @return true if the range of characters requires bidi analysis
	 * @see Bidi#requiresBidi
	 */
	boolean requiresBidi(char[] text, int start, int limit);

	/**
	 * Default implementation of {@link BidiProvider}, backed by {@link Bidi}.
	 *
	 * @since 3.15
	 */
	static final class DefaultBidiProvider implements BidiProvider {
		@Override
		public boolean requiresBidi(char[] text, int start, int limit) {
			return Bidi.requiresBidi(text, start, limit);
		}
	}
}
