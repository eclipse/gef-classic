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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;

/**
 * EXPERIMENTAL - This works only when running as a plain Java application, not
 * when running as a Plug-in (see comments below). Even if this was working, it
 * would be restricted as it depends on Java 6 (and is therefore currently
 * excluded).
 * <p/>
 * Import DOT to a Zest Graph instance via the Java compiler API.
 * @author Fabian Steeg (fsteeg)
 */
final class GraphCreatorViaJavaCompilerApi implements IGraphCreator {

    /**
     * {@inheritDoc}
     * @see org.eclipse.zest.dot.IGraphCreator#create(org.eclipse.swt.widgets.Composite,
     *      int)
     */
    public Graph create(final Composite parent, final int style, final String dot) {
        /*
         * The trick we use to avoid nasty classloader trouble when reloading
         * newer versions of the same class: we give each generated class a
         * unique name (as this is unused, it has been removed from the API):
         */
        DotImport dotImport = new DotImport(dot/*, System.currentTimeMillis() + ""*/);
        File zestFile = dotImport.newGraphSubclass();
        URL outputDirUrl = compileWithJavaCompiler(zestFile, dotImport.getName());
        Graph graph =
                ExperimentalDotImport.loadGraph(dotImport.getName(), outputDirUrl, parent, style);
        return graph;
    }

    private URL compileWithJavaCompiler(final File zestFile, final String name) {
        /* Create and set up the compiler: */
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        ClassloadingFileManager manager =
                new ClassloadingFileManager(jc.getStandardFileManager(null, null, null));
        File outputDir = zestFile.getParentFile();
        List<String> options = new ArrayList<String>();
        /*
         * TODO: A possible approach to solve the problems when running as a
         * plug-in would be to provide the complete classpath here, as an option
         * for the compiler. However, I don't see a proper way to get the
         * complete classpath of the running plug-in. It seems to always involve
         * internal API, which would be no real solution (see also the JDT
         * compiler variant and comments there), and in particular would also
         * require Java 6.
         */
        options.add("-d");
        options.add(outputDir.getAbsolutePath());
        /* Compile the generated Zest graph: */
        jc.getTask(null, manager, null, options, null, manager.getJavaFileObjects(zestFile)).call();
        try {
            manager.close();
            /* Return the URL of the folder where the compiled class file is: */
            return outputDir.toURI().toURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static final class ClassloadingFileManager extends ForwardingJavaFileManager<JavaFileManager> {

        private StandardJavaFileManager m;

        public ClassloadingFileManager(final StandardJavaFileManager fileManager) {
            super(fileManager);
            this.m = fileManager;
        }

        @Override
        public ClassLoader getClassLoader(final JavaFileManager.Location location) {
            /*
             * I was hoping this would do the trick, but it doesn't. Doing this
             * here was as far as I got consulting sources like
             * http://stackoverflow.com/questions/274474/ and
             * http://www.ibm.com/developerworks/java/library/j-jcomp/index.html
             */
            return Thread.currentThread().getContextClassLoader();

        }

        public Iterable<? extends JavaFileObject> getJavaFileObjects(final File... files) {
            return m.getJavaFileObjects(files);
        }

    }
}
