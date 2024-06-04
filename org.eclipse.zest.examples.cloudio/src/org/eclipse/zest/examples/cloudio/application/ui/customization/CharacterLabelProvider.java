/*******************************************************************************
 * Copyright (c) 2011, 2024 Stephan Schwiebert and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Stephan Schwiebert - initial API and implementation
 ******************************************************************************/
package org.eclipse.zest.examples.cloudio.application.ui.customization;

import org.eclipse.swt.graphics.Color;

import org.eclipse.zest.examples.cloudio.application.data.Type;
import org.eclipse.zest.examples.cloudio.application.ui.TypeLabelProvider;

/**
 * An example to demonstrate how to modify a label provider
 *
 * @author sschwieb
 *
 */
public class CharacterLabelProvider extends TypeLabelProvider {

	@Override
	public Color getColor(Object element) {
		Type t = (Type) element;
		char firstChar = Character.toLowerCase(t.getString().charAt(0));
		if (firstChar < 'g') {
			return colorList.get(2);
		}
		if (firstChar < 'm') {
			return colorList.get(1);
		}
		if (firstChar < 's') {
			return colorList.get(0);
		}
		return colorList.get(3);
	}

}
