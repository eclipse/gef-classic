/*******************************************************************************
 * Copyright (c) 2024 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Alois Zoitl - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d;

/**
 * Lightweight Container which just draws the children according to the given
 * layout.
 *
 * This Container does not maintain any graphics state and delegates that to its
 * children. This can save memory and increase drawing performance for larger
 * graphs with deep nested figures.
 *
 * A Container can not have any border and does not draw any background.
 *
 * This class is currently in development its API may
 * change: @noreference, @noextend and @noinstantiate
 *
 * @since 3.16
 */
public class Container extends Figure {

	public Container(LayoutManager manager) {
		setLayoutManager(manager);
	}

	@Override
	public final void paint(Graphics graphics) {
		getChildren().stream().filter(IFigure::isVisible).forEach(child -> child.paint(graphics));
	}

	@Override
	public final void setLayoutManager(LayoutManager manager) {
		super.setLayoutManager(manager);
	}

}
