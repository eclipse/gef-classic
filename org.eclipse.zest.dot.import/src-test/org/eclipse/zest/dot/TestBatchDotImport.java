/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import org.junit.Test;

/**
 * Tests for the main method of the {@link DotImportBatch} class.
 * @author Fabian Steeg (fsteeg)
 */
public class TestBatchDotImport {
    /**
     * If nothing is given, the default input and output files are used.
     */
    @Test
    public void noneGiven() {
        DotImportBatch.main(new String[] {});
    }
    /**
     * If just the input is given, the default output folder is used.
     */
    @Test
    public void inputGiven() {
        DotImportBatch.main(new String[] { DotImport.DEFAULT_INPUT_FOLDER
                .getAbsolutePath() });
    }
    /**
     * Both the input and the output folder can be specified.
     */
    @Test
    public void inputGivenOutputGiven() {
        DotImportBatch.main(new String[] {
                DotImport.DEFAULT_INPUT_FOLDER.getAbsolutePath(),
                DotImport.DEFAULT_OUTPUT_FOLDER.getAbsolutePath() });
    }
}
