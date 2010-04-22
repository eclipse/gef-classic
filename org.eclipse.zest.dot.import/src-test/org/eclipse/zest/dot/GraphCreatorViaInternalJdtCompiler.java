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
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.builder.ProblemFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.widgets.Graph;

/**
 * EXPERIMENTAL - NOT REALLY WORKING YET
 * <p/>
 * Import DOT to a Zest Graph instance via the internal Eclipse JDT compiler.
 * @author Fabian Steeg (fsteeg)
 */
/*
 * The downside of this solution is it uses internal API; upside is it works
 * with Java 5 (contrary to the other solution, based on the Java compiler API);
 * as I have recently learned using internal API is not allowed for projects
 * taking part in the release train, this is not really an option either. 
 * Currently used for Graph instance import: GraphCreatorInterpreter implementation
 * of IGraphCreator.
 */
final class GraphCreatorViaInternalJdtCompiler implements IGraphCreator {

    /**
     * {@inheritDoc}
     * @see org.eclipse.zest.dot.IGraphCreator#create(org.eclipse.swt.widgets.Composite,
     *      int)
     */
    public Graph create(final Composite parent, final int style, final String dot) {
        String graphName = new DotImport(dot).getName();
        File zestFile = new DotImport(dot).newGraphSubclass();
        URL url = compileWithInternalJdtCompiler(zestFile, graphName);
        Graph graph = ExperimentalDotImport.loadGraph(graphName, url, parent, style);
        return graph;
    }

    private URL compileWithInternalJdtCompiler(final File zestFile, final String graphName) {
        /*
         * TODO we need to set up the environment here. Here, we basically hit
         * the same issue as when running with the Java compiler API: we need
         * the classpath
         */
        INameEnvironment nameEnvironment = new FileSystem(new String[0], new String[0], "UTF-8");
        CompilerOptions compilerOptions = new CompilerOptions();
        compilerOptions.generateClassFiles = true;
        compilerOptions.verbose = true;
        org.eclipse.jdt.internal.compiler.Compiler compiler =
                new org.eclipse.jdt.internal.compiler.Compiler(nameEnvironment,
                        DefaultErrorHandlingPolicies.proceedWithAllProblems(), compilerOptions,
                        new ICompilerRequestor() {
                            public void acceptResult(final CompilationResult result) {
                                CategorizedProblem[] errors = result.getErrors();
                                for (CategorizedProblem categorizedProblem : errors) {
                                    System.out.println(String.format("%s: '%s' (%s, line %s)",
                                            categorizedProblem.getMarkerType(), categorizedProblem
                                                    .getMessage(), new String(categorizedProblem
                                                    .getOriginatingFileName()), categorizedProblem
                                                    .getSourceLineNumber()));
                                }

                            }
                        }, ProblemFactory.getProblemFactory(Locale.getDefault()));

        compiler.compile(new ICompilationUnit[] { new ICompilationUnit() {
            public char[] getFileName() {
                return zestFile.getAbsolutePath().toCharArray();
            }
            public char[][] getPackageName() {
                return null;
            }
            public char[] getMainTypeName() {
                return graphName.toCharArray();
            }
            public char[] getContents() {
                return read(zestFile).toCharArray();
            }
        } });
        try {
            URL url = zestFile.getParentFile().toURI().toURL();
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String read(final File dotFile) {
        try {
            Scanner scanner = new Scanner(dotFile);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine() + "\n");
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
