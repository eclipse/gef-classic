/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.internal.dot;

import junit.framework.Assert;

import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Main test suite for the {@code org.eclipse.zest.dot.ui} bundle.
 * @author Fabian Steeg (fsteeg)
 */
@RunWith( Suite.class )
@Suite.SuiteClasses( { /* TestExperimentalDotImport.class, //not passing and experimental */
TestZestGraphTemplate.class, TestZestGraphWizard.class, TestZestProjectWizard.class,
        TestDotDirStore.class } )
public final class DotUiSuite {
    @Before
    public void setup() {
        if (!Platform.isRunning()) {
            Assert.fail("Please run as JUnit Plug-in test");
        }
    }
}
