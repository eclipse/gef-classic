/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 3.1
 */
public class ModelUtil {

public static List getModelSpan(ModelElement start, int startIndex, ModelElement end,
		int endIndex) {
	Container ancestor = start.getContainer();
	while (!ancestor.contains(end))
		ancestor = ancestor.getContainer();

	List result = new ArrayList();


	List children;

	Container branch = start.getContainer();
	while (branch != ancestor) {
		children = branch.getChildren();
		int branchIndex = children.indexOf(start);
		if (startIndex == 0) {
			//We should either delete the child, or the entire branch
			if (branchIndex != 0) {
				result.addAll(children.subList(branchIndex, children.size()));
				start = branch;
				branch = branch.getContainer();
				startIndex = branchIndex + 1;
			} else {
				start = branch;
				branch = branch.getContainer();
				startIndex = branchIndex;
			}
		} else {
			result.addAll(children.subList(branchIndex + 1, children.size()));
			start = branch;
			branch = branch.getContainer();
			startIndex = branchIndex + 1;
		}
	}

	List rightSide = new ArrayList();
	branch = end.getContainer();

	while (branch != ancestor) {
		children = branch.getChildren();
		//The index to the right side of end.
		int branchIndex = children.indexOf(end) + 1;
		if (endIndex == end.size()) {
			//Need to either delete the child or entire branch
			if (branchIndex < branch.size()) {
				rightSide.addAll(0, children.subList(0, branchIndex));
				end = branch;
				branch = branch.getContainer();
				endIndex = branchIndex - 1;
			} else {
				end = branch;
				branch = branch.getContainer();
				endIndex = branchIndex;
			}
		} else {
			result.addAll(children.subList(0, branchIndex - 1));
			end = branch;
			branch = branch.getContainer();
			endIndex = branchIndex - 1;
		}
	}
	children = ancestor.getChildren();
	if (startIndex == 0)
		startIndex = children.indexOf(start);
	else
		startIndex = children.indexOf(start) + 1;

	if (endIndex == end.size())
		endIndex = children.indexOf(end) + 1;
	else
		endIndex = children.indexOf(end);
	if (endIndex > startIndex)
			result.addAll(ancestor.getChildren().subList(startIndex, endIndex));
	result.addAll(rightSide);

	return result;
}

}