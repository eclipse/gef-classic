/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.dot;

import java.io.File;

import org.junit.Assert;

/**
 * Util class for the tests of the {@link DotImport} class.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public final class DotImportTestUtils {
	private DotImportTestUtils() { /* Enforce non-instantiability */
	}

	static final String RESOURCES_INPUT = "resources/input/";
	static final String RESOURCES_TESTS = "resources/tests/";
	public static final File OUTPUT = DotImport.DEFAULT_OUTPUT_FOLDER;

	static void importFrom(final File dotFile) {
		Assert.assertTrue("DOT input file must exist: " + dotFile,
				dotFile.exists());
		File zest = new DotImport(dotFile).newGraphSubclass();
		Assert.assertNotNull("Resulting file must not be null", zest);
		Assert.assertTrue("Resulting file must exist", zest.exists());
		/*
		 * The name of the generated file is equal to the name of the DOT graph
		 * (part of the content of the DOT file, NOT the name of the file), plus
		 * the ".java" extension:
		 */
		Assert.assertEquals(zest.getName().split("\\.")[0],
				new DotAst(dotFile).graphName());
		System.out.println(String.format(
				"Transformed DOT in '%s' to Zest in '%s'", dotFile, zest));
	}
}
