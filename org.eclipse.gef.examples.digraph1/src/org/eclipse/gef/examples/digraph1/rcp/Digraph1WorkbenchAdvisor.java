/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.gef.examples.digraph1.rcp;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * A Workbench Advisor for the Directed Graph Example Editor as an RCP
 * application.
 *
 * @author Anthony Hunter
 */
public class Digraph1WorkbenchAdvisor extends WorkbenchAdvisor {

	/*
	 * @see
	 * org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.
	 * eclipse.ui.application.IWorkbenchWindowConfigurer)
	 */
	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new Digraph1WorkbenchWindowAdvisor(configurer);
	}

	/*
	 * @see
	 * org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return "org.eclipse.gef.examples.digraph1"; //$NON-NLS-1$
	}

}
