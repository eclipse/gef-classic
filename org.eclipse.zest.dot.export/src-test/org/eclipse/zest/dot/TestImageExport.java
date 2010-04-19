/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.zest.core.widgets.Graph;

/**
 * Tests for the {@link DotExport} class.
 * @author Fabian Steeg (fsteeg)
 */
public class TestImageExport extends TestDotTemplate {
    /* Path to the local Graphviz folder containing the dot executable file: */
    private static final String DOT_DIR = "C:\\Program Files (x86)\\Graphviz2.20\\bin";

    /* Typical locations on other OS: */
    // private static final String DOT_DIR = "/usr/local/bin";
    // private static final String DOT_DIR = "/opt/local/bin"; // MacPorts

    @Override
    protected void testDotGeneration(final Graph graph) {
        /*
         * The DotExport class wraps the simple DotTemplate class, so when we test DotExport, we also run the
         * test in the test superclass:
         */
        super.testDotGeneration(graph);
        File image = new DotExport(graph).toImage(new File(DOT_DIR), "pdf");
        Assert.assertNotNull("Image must not be null", image);
        System.out.println("Created image: " + image);
        Assert.assertTrue("Image must exist", image.exists());
    }

}
