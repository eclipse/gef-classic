/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest;

import org.eclipse.zest.dot.DotImportTestUtils;
import org.eclipse.zest.dot.DotTestUtils;
import org.eclipse.zest.dot.TestAnimationDotImport;
import org.eclipse.zest.dot.TestBasicDotImport;
import org.eclipse.zest.dot.TestBatchDotImport;
import org.eclipse.zest.dot.TestDotAst;
import org.eclipse.zest.dot.TestGraphInstanceDotImport;
import org.eclipse.zest.dot.TestLayoutDotImport;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Main test suite for the {@code org.eclipse.zest.dot.import} bundle.
 * @author Fabian Steeg (fsteeg)
 */
@RunWith( Suite.class )
@Suite.SuiteClasses( { TestBasicDotImport.class, TestLayoutDotImport.class,
        TestAnimationDotImport.class, TestGraphInstanceDotImport.class, TestBatchDotImport.class,
        TestDotAst.class } )
public final class DotImportSuite {
    private DotImportSuite() { /* Enforce non-instantiability */}
    @BeforeClass
    public static void wipe() {
        DotTestUtils.wipeOutput(DotImportTestUtils.OUTPUT, ".java");
    }
}
