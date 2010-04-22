/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.internal.dot;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.runtime.Platform;
import org.eclipse.zest.internal.dot.DotDirStore;
import org.eclipse.zest.internal.dot.DotUiActivator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link DotDirStore}.
 * @author Fabian Steeg (fsteeg)
 */
public final class TestDotDirStore {
    @Before
    public void setup() {
        if (!Platform.isRunning()) {
            Assert.fail("Please run as JUnit Plug-in test");
        }
    }

    @Test
    public void askForDotDir() {
        /* Blocks UI every time, but tests asking even if clearing workspace is disabled: */
        DotUiActivator.getDefault().getPreferenceStore().setValue(DotDirStore.DOTPATH_KEY, "");
        /* If not set, the DOT dir is requested: */
        check(DotDirStore.getDotDirPath());
    }

    @Test
    public void getDotDirFromPrefs() {
        /* If set, the DOT dir is reurned: */
        check(DotDirStore.getDotDirPath());
    }

    public void check(final String path) {
        Assert.assertNotNull("Path to dot directory should not be null", path);
        System.out.println("Dot directory: " + path);
        Assert.assertTrue("Dot path should not be empty", path.trim().length() > 0);
        Assert.assertTrue("Dot directory should exist", new File(path).exists());
        Assert.assertTrue("Dot path should point to a directory", new File(path).isDirectory());
    }
}
