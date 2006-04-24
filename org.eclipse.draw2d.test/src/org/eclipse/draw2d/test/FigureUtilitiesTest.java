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

package org.eclipse.draw2d.test;

import junit.framework.TestCase;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;


public class FigureUtilitiesTest  extends TestCase {
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void test_findCommonAncestor_happypath() {
        IFigure figureParent = new Figure();
        IFigure figureChild1 = new Figure();
        IFigure figureChild2 = new Figure();
        IFigure figureChild3 = new Figure();
        
        figureParent.add(figureChild1);
        figureChild1.add(figureChild2);
        figureParent.add(figureChild3);
        
        IFigure result = FigureUtilities.findCommonAncestor(figureChild2, figureChild3);
        assertTrue(figureParent == result);
    }
    
    public void test_findCommonAncestor_bugzilla130042() {
        IFigure figureParent = new Figure();
        IFigure figureChild = new Figure();
        figureParent.add(figureChild);
        
        IFigure result = FigureUtilities.findCommonAncestor(figureParent, figureChild);
        assertTrue(figureParent == result);
    }
}
