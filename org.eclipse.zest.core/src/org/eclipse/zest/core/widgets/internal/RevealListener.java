/*******************************************************************************
 * Copyright 2005 CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.swt.widgets.Control;

/**
 *
 * A Listener to indicate when a view has become visible.
 *
 * @author Ian Bull
 *
 */
public interface RevealListener {

	public void revealed(Control c);

}
