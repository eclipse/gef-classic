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
package org.eclipse.gef.examples.ui.pde.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.eclipse.gef.examples.ui.pde.internal.l10n.Messages";//$NON-NLS-1$

    private Messages() {
        // Do not instantiate
    }

    public static String LogicExample_createProjectPage_title;
    public static String LogicExample_createProjectPage_desc;
    public static String FlowExample_createProjectPage_title;
    public static String FlowExample_createProjectPage_desc;
    public static String ShapesExample_createProjectPage_title;
    public static String ShapesExample_createProjectPage_desc;
    public static String TextExample_createProjectPage_title;
    public static String TextExample_createProjectPage_desc;
    public static String monitor_creatingProject;
    public static String monitor_unzippingProject;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}