/*******************************************************************************
 * Copyright 2005-2010, 2023 CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *               Mateusz Matela
 ******************************************************************************/
package org.eclipse.zest.core.widgets;

/**
 * @since 2.0
 */
public interface LayoutFilter {

	public boolean isObjectFiltered(GraphItem item);

}
