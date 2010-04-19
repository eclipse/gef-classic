/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

/**
 * Zest project builder. Imports the DOT files in a dedicated folder
 * ('templates') to Zest graph in a source folder. These can be run to view the
 * Graph. Effectively this implements a basic Zest authoring environment using
 * DOT as a DSL, as upon saving the DOT file, the same Zest application can be
 * relaunched, showing the Zest graph created from the changed DOT file.
 * @author Fabian Steeg (fsteeg)
 */
public final class ZestBuilder extends IncrementalProjectBuilder {

    static final String BUILDER_ID = "org.eclipse.zest.dot.ui.builder";
    private static final String MARKER_TYPE = "org.eclipse.zest.dot.ui.problem";

    private class SampleDeltaVisitor implements IResourceDeltaVisitor {
        /**
         * {@inheritDoc}
         * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
         */
        public boolean visit(final IResourceDelta delta) {
            IResource resource = delta.getResource();
            switch (delta.getKind()) {
            case IResourceDelta.ADDED:
                checkDotFile(resource);
                break;
            case IResourceDelta.REMOVED:
                break;
            case IResourceDelta.CHANGED:
                checkDotFile(resource);
                break;
            default:
                break;
            }
            // return true to continue visiting children.
            return true;
        }
    }

    private class SampleResourceVisitor implements IResourceVisitor {
        public boolean visit(final IResource resource) {
            checkDotFile(resource);
            // return true to continue visiting children.
            return true;
        }
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int,
     *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
     */
    @SuppressWarnings( "unchecked" )
    // raw type required by API
    protected IProject[] build(final int kind, final Map args,
            final IProgressMonitor monitor) throws CoreException {
        if (kind == FULL_BUILD) {
            fullBuild(monitor);
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild(monitor);
            } else {
                incrementalBuild(delta, monitor);
            }
        }
        return null;
    }

    private void checkDotFile(final IResource resource) {
        if (resource instanceof IFile
                && resource.getFileExtension().equals("dot")) {
            IFile dotFile = (IFile) resource;
            deleteMarkers(dotFile);
            List<String> errors = new DotImport(dotFile).getErrors();
            /*
             * If parsing the current DOT file causes errors, we add warning
             * markers:
             */
            if (errors.size() > 0) {
                addMarker(dotFile, errors.get(0), 1, IMarker.SEVERITY_WARNING);
            } else {
                /*
                 * If the DOT can be parsed, we import it into the package in
                 * the src-gen folder:
                 */
                importToGeneratedSourceFolder(dotFile);
            }
        }
    }

    private void importToGeneratedSourceFolder(final IFile dotFile) {
        try {
            IJavaProject javaProject = JavaCore.create(dotFile.getProject());
            String sourceGenPath = "/" + javaProject.getElementName() + "/"
                    + ZestProjectWizard.SRC_GEN;
            IPackageFragmentRoot packageRoot = javaProject
                    .findPackageFragmentRoot(new Path(sourceGenPath));
            IPackageFragment targetPackage = packageRoot
                    .getPackageFragment(ZestProjectWizard.PACKAGE);
            IResource targetFolder = targetPackage.getCorrespondingResource();
            new DotImport(dotFile).newGraphSubclass((IContainer) targetFolder);
            targetFolder.refreshLocal(1, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void addMarker(final IFile file, final String message,
            final int lineNumber, final int severity) {
        try {
            IMarker marker = file.createMarker(MARKER_TYPE);
            marker.setAttribute(IMarker.MESSAGE, message);
            marker.setAttribute(IMarker.SEVERITY, severity);
            marker.setAttribute(IMarker.LINE_NUMBER, lineNumber == -1
                    ? 1 : lineNumber);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void deleteMarkers(final IFile file) {
        try {
            file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void fullBuild(final IProgressMonitor monitor) throws CoreException {
        try {
            getProject().accept(new SampleResourceVisitor());
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void incrementalBuild(final IResourceDelta delta,
            final IProgressMonitor monitor) throws CoreException {
        delta.accept(new SampleDeltaVisitor());
    }
}
