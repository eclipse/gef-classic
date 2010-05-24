/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.internal.dot;

import junit.framework.Assert;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;

/**
 * Tests for the {@link ZestGraphTemplate} class.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public final class TestZestGraphTemplate {
	@Test
	public void availableTemplates() {
		// FIXME: requires running platform
		if (Platform.isRunning()) {
			String[] contents = ZestGraphTemplate.availableTemplateContents();
			String[] names = ZestGraphTemplate.availableTemplateNames();
			Assert.assertTrue("Templates must be larger than 0;",
					names.length > 0);
			Assert.assertEquals(contents.length, names.length);
			System.out.println(names.length + " templates");
		} else {
			Assert.fail("Please run as JUnit Plug-in test");
		}
	}
}
