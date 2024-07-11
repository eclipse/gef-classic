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
package org.eclipse.zest.examples.cloudio.application.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.zest.examples.cloudio.application.data.TypeCollector;

/**
 *
 * @author sschwieb
 *
 */
public class LoadStopWordsAction extends AbstractTagCloudAction {

	@Override
	public void run(IAction action) {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		dialog.setText("Select a stopwor file, containing one word per line...");
		String sourceFile = dialog.open();
		if (sourceFile == null) {
			return;
		}
		TypeCollector.setStopwords(sourceFile);
	}

}
