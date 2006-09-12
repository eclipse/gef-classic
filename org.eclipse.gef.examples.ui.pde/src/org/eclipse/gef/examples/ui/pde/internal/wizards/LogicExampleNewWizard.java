/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ui.pde.internal.wizards;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.examples.ui.pde.internal.GefExamplesPlugin;
import org.eclipse.gef.examples.ui.pde.internal.l10n.Messages;

public class LogicExampleNewWizard
    extends ProjectUnzipperNewWizard {

    /**
     * Constructor
     */
    public LogicExampleNewWizard() {
        super(
            "LogicExampleNewWizard", //$NON-NLS-1$
            Messages.LogicExample_createProjectPage_title,
            Messages.LogicExample_createProjectPage_desc,
            "org.eclipse.gef.examples.logic", //$NON-NLS-1$  
            FileLocator.find(GefExamplesPlugin.getDefault().getBundle(),
                new Path("examples/logic.zip"), null)); //$NON-NLS-1$
    }
}
