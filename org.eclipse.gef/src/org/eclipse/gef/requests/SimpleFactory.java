/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
package org.eclipse.gef.requests;

/**
 * A simple CreationFactory that takes a Class in the constructor and creates a
 * new instance of this Class in {@link #getNewObject()}.
 *
 * @author hudsonr
 * @since 2.1
 */
public class SimpleFactory implements CreationFactory {

	private Class type;

	/**
	 * Creates a SimpleFactory.
	 *
	 * @param aClass The class to be instantiated using this factory.
	 */
	public SimpleFactory(Class aClass) {
		type = aClass;
	}

	/**
	 * Create the new object.
	 *
	 * @return The newly created object.
	 */
	@Override
	public Object getNewObject() {
		try {
			return type.newInstance();
		} catch (Exception exc) {
			return null;
		}
	}

	/**
	 * Returns the type of object this factory creates.
	 *
	 * @return The type of object this factory creates.
	 */
	@Override
	public Object getObjectType() {
		return type;
	}

}
