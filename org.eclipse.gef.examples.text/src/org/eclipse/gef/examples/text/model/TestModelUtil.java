/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model;

import java.util.List;

import junit.framework.TestCase;

/**
 * @since 3.1
 */
public class TestModelUtil extends TestCase {

static List result;

public void testNestedBegin() {
	Container doc = new Container(Container.TYPE_ROOT);
	Container branch1 = new Container(0);
	TextRun start = new TextRun("12345");

	doc.add(branch1);
	branch1.add(start);

	TextRun end = new TextRun("ABCDE");
	doc.add(end);

	for (int i = 0; i < 3; i++) {
		result = ModelUtil.getModelSpan(start, 0, end, 5);
		compareList(result, new Object[] {branch1, end});

		result = ModelUtil.getModelSpan(start, 1, end, 5);
		compareList(result, new Object[] {end});

		result = ModelUtil.getModelSpan(start, 0, end, 3);
		compareList(result, new Object[] {branch1});

		result = ModelUtil.getModelSpan(start, 1, end, 3);
		assertTrue(result.isEmpty());
		doc.add(new TextRun("bogus"), 0);
	}

	TextRun middle = new TextRun("I'm in the middle");
	doc.add(middle, doc.getChildren().indexOf(end));

	result = ModelUtil.getModelSpan(start, 0, end, 5);
	compareList(result, new Object[] {branch1, middle, end});

	result = ModelUtil.getModelSpan(start, 1, end, 4);
	compareList(result, new Object[] {middle});

}

public void testNestedEnd() {
	Container doc = new Container(Container.TYPE_ROOT);
	Container branch1 = new Container(0);
	TextRun run123 = new TextRun("12345");

	branch1.add(run123);

	TextRun runABC = new TextRun("ABCDE");
	doc.add(runABC);
	doc.add(branch1);

	for (int i = 0; i < 3; i++) {
		result = ModelUtil.getModelSpan(runABC, 0, run123, 5);
		compareList(result, new Object[] {runABC, branch1});

		result = ModelUtil.getModelSpan(runABC, 1, run123, 5);
		compareList(result, new Object[] {branch1});

		result = ModelUtil.getModelSpan(runABC, 0, run123, 4);
		compareList(result, new Object[] {runABC});

		assertTrue(ModelUtil.getModelSpan(runABC, 1, run123, 4).isEmpty());
		doc.add(new TextRun("bogus"), 0);
		doc.add(new TextRun("bogus"), doc.getChildren().size());
	}

	TextRun middle = new TextRun("I'm in the middle");
	doc.add(middle, doc.getChildren().indexOf(branch1));

	result = ModelUtil.getModelSpan(runABC, 0, run123, 5);
	compareList(result, new Object[] {runABC, middle, branch1});

	result = ModelUtil.getModelSpan(runABC, 1, run123, 4);
	compareList(result, new Object[] {middle});
}

private void compareList(List result, Object array[]) {
	Object compare[] = result.toArray();
	assertEquals(array.length, compare.length);
	for (int i = 0; i < compare.length; i++)
		assertEquals(compare[i], array[i]);
}

}