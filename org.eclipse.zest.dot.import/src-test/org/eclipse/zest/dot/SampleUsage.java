/*******************************************************************************
 * Copyright (c) 2010 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.tests.dot.TestImageExport;
import org.junit.Test;

/**
 * Overall API sample usage.
 * @author Fabian Steeg (fsteeg)
 */
public class SampleUsage {
    @Test
    public void sampleUsage(){
        
        /******************************************************************
         * Import ********************************************************/
        Shell shell = new Shell();
        /* The DOT input, can be given as a String, File or IFile: */
        DotImport importer = new DotImport("digraph Simple { 1;2; 1->2 }");
        /* Compile the DOT input to a Zest graph subclass: */
        File file = importer.newGraphSubclass();
        /* Or create a Zest graph instance in a parent, with a style: */
        Graph graph = importer.newGraphInstance(shell, SWT.NONE);
        
        /******************************************************************
         * Export ********************************************************/
        /* For some Zest graph, we create the exporter: */
        DotExport exporter = new DotExport(graph);
        /* Export the Zest graph to DOT: */
        String dot = exporter.toDotString();
        /* Or to an image file, via a given Graph installation: */
        File image = exporter.toImage(TestImageExport.dotBinDir(), "pdf");
        //File image = exporter.toImage("/opt/local/bin", "pdf"); // set dir and uncomment
        /*****************************************************************/
        
        //TestGraphInstanceDotImport.open(shell); // sets title, layout, and size, opens the shell
        System.out.println(graph);
        System.out.println(file);
        System.out.println(dot);
        System.out.println(image);
    }
}
