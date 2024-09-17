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
package org.eclipse.zest.examples.cloudio.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 *
 * @author sschwieb
 *
 */
public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addStandaloneView("org.eclipse.zest.cloudio.sample.tagcloud", false, IPageLayout.TOP, 0.95f,
				layout.getEditorArea());
		layout.setFixed(true);
		layout.setEditorAreaVisible(false);
	}

}
