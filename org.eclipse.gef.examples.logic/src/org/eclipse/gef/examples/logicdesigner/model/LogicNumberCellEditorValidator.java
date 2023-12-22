/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.jface.viewers.ICellEditorValidator;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LogicNumberCellEditorValidator implements ICellEditorValidator {

	private static LogicNumberCellEditorValidator instance;

	public static LogicNumberCellEditorValidator instance() {
		if (instance == null) {
			instance = new LogicNumberCellEditorValidator();
		}
		return instance;
	}

	@Override
	public String isValid(Object value) {
		try {
			Integer.parseInt((String) value);
			return null;
		} catch (NumberFormatException exc) {
			return LogicMessages.CellEditorValidator_NotANumberMessage;
		}
	}

}
