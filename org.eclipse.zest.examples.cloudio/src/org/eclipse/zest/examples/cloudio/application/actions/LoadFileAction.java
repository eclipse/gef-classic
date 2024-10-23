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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.zest.cloudio.TagCloudViewer;
import org.eclipse.zest.examples.cloudio.application.Messages;
import org.eclipse.zest.examples.cloudio.application.data.Type;
import org.eclipse.zest.examples.cloudio.application.data.TypeCollector;

/**
 *
 * @author sschwieb
 *
 */
public class LoadFileAction extends AbstractTagCloudAction {

	@Override
	public void run(IAction action) {
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);

		dialog.setText(Messages.LoadFileAction_Text);
		String sourceFile = dialog.open();
		if (sourceFile == null) {
			return;
		}
		ProgressMonitorDialog pd = new ProgressMonitorDialog(getShell());
		try {
			List<Type> types = TypeCollector.getData(new File(sourceFile), StandardCharsets.UTF_8);
			pd.setBlockOnOpen(false);
			pd.open();
			pd.getProgressMonitor().beginTask(Messages.LoadFileAction_BeginTask, 200);
			TagCloudViewer viewer = getViewer();
			viewer.setInput(types, pd.getProgressMonitor());
			// viewer.getCloud().layoutCloud(pd.getProgressMonitor(), false);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pd.close();
		}
	}

}
