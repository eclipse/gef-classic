/*
 * Copyright (c) 2023 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Philipp Bauer - initial implementation
 */

package org.eclipse.zest.core.widgets;

import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;

/**
 * Super Interface for drag support for zest graphs @see{Graph}
 *
 * @since 1.9
 */
public interface GraphDragSupport extends MouseMotionListener, MouseListener {

}
