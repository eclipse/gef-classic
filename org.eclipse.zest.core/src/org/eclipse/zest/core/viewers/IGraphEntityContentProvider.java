/*******************************************************************************
 * Copyright 2005, 2024 CHISEL Group, University of Victoria, Victoria, BC,
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
package org.eclipse.zest.core.viewers;

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 *
 * @author Ian Bull
 *
 */
public interface IGraphEntityContentProvider extends IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement);

	/**
	 * Gets the elements this object is connected to
	 *
	 * @param entity
	 */
	public Object[] getConnectedTo(Object entity);

}
