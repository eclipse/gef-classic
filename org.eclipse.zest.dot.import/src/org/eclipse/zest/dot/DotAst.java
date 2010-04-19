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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.mwe.utils.StandaloneSetup;
import org.openarchitectureware.vis.graphviz.DotStandaloneSetup;

/**
 * Walks the AST of a DOT graph, e.g. to extract the name (used to name and later identify the generated
 * file).
 * @author Fabian Steeg (fsteeg)
 */
final class DotAst {
    private Resource resource;

    /**
     * @param dotFile The DOT file to parse
     */
    DotAst(final File dotFile) {
        this.resource = loadResource(DotImport.fix(dotFile));
    }

    /**
     * @param dotFile The DOT file to parse
     * @return The name of the DOT graph described in the given file
     */
    String graphName() {
        EObject graph = graph();
        Iterator<EAttribute> graphAttributes = graph.eClass().getEAllAttributes().iterator();
        while (graphAttributes.hasNext()) {
            EAttribute a = graphAttributes.next();
            /* We return the name attribute of the graph: */
            if (a.getName().equals("name")) {
                return (String) graph.eGet(a);
            }
        }
        System.err.println("Could not find name attribute in: " + graph);
        return "";
    }

    /**
     * @return The errors reported by the parser when parsing the given file
     */
    List<String> errors() {
        List<String> result = new ArrayList<String>();
        EList<Diagnostic> errors = resource.getErrors();
        Iterator<Diagnostic> i = errors.iterator();
        while (i.hasNext()) {
            Diagnostic next = i.next();
            result.add(String.format("Error in line %s: %s ", next.getLine(), next.getMessage()));
        }
        return result;
    }

    /**
     * @param dotFile The DOT file to parse
     * @return The graph EObjects to walk or inspect
     */
    EObject graph() {
        /* We load the input DOT file: */
        EList<EObject> contents = resource.getContents();
        EObject graphs = contents.iterator().next();
        /* We assume one graph per file, i.e. we take the first only: */
        EObject graph = graphs.eContents().iterator().next();
        return graph;
    }

    private static Resource loadResource(final File file) {
        new StandaloneSetup().setPlatformUri("..");
        DotStandaloneSetup.doSetup();
        ResourceSet set = new ResourceSetImpl();
        Resource res = set.getResource(URI.createURI(file.toURI().toString()), true);
        if (!res.isLoaded()) {
            try {
                res.load(Collections.EMPTY_MAP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * @param eStatementObject The statement object, e.g. the object corresponding to "node[label="hi"]"
     * @param attributeName The name of the attribute to get the value for, e.g. "label"
     * @return The value of the given attribute, e.g. "hi"
     */
    static String getAttributeValue(final EObject eStatementObject, final String attributeName) {
        Iterator<EObject> nodeContents = eStatementObject.eContents().iterator();
        while (nodeContents.hasNext()) {
            EObject nodeContentElement = nodeContents.next();
            if (nodeContentElement.eClass().getName().equals("attr_list")) {
                Iterator<EObject> attributeContents = nodeContentElement.eContents().iterator();
                while (attributeContents.hasNext()) {
                    EObject attributeElement = attributeContents.next();
                    if (attributeElement.eClass().getName().equals("a_list")) {
                        if (getValue(attributeElement, "name").equals(attributeName)) {
                            String label = getValue(attributeElement, "value").replaceAll("\"", "");
                            return label;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param eObject The object to get a attribute vlaue for
     * @param name The name of the attribute
     * @return The value of the given attribute in the given object
     */
    static String getValue(final EObject eObject, final String name) {
        Iterator<EAttribute> graphAttributes = eObject.eClass().getEAllAttributes().iterator();
        while (graphAttributes.hasNext()) {
            EAttribute a = graphAttributes.next();
            if (a.getName().equals(name)) {
                return eObject.eGet(a).toString();
            }
        }
        return null;
    }
}
