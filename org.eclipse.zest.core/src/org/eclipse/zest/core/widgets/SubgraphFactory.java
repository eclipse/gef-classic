/*******************************************************************************
 * Copyright (c) 2009-2010, 2024 Mateusz Matela and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Mateusz Matela - initial API and implementation
 *               Ian Bull
 ******************************************************************************/
package org.eclipse.zest.core.widgets;

import org.eclipse.zest.layouts.interfaces.LayoutContext;
import org.eclipse.zest.layouts.interfaces.NodeLayout;
import org.eclipse.zest.layouts.interfaces.SubgraphLayout;

/**
 * Factory used by {@link Graph} to create subgraphs. One instance of
 * SubgraphFactory can be used with multiple graphs unless explicitly stated
 * otherwise.
 *
 * @since 1.14
 */
public interface SubgraphFactory {
	SubgraphLayout createSubgraph(NodeLayout[] nodes, LayoutContext context);
}
