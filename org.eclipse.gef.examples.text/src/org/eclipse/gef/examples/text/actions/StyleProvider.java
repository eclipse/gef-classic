/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

package org.eclipse.gef.examples.text.actions;

/**
 * @since 3.1
 */
public interface StyleProvider {

	void addStyleListener(StyleListener listener);

	Object getStyle(String styleID);

	Object getStyleState(String styleID);

	void removeStyleListener(StyleListener listener);

	void setStyle(String styleID, Object newValue);

}
