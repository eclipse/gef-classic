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

/**
 * @since 3.1
 */
public class RemoveRange extends MiniEdit {

private ModelElement removed[];

private int removalIndices[];

private Container removedFrom[];

MiniEdit clipBeginning;

MiniEdit clipEnding;

/**
 * This class removes a range in the model starting from a given TextRun and offset and
 * terminating at another TextRun and offset location. Removing the range in the model may
 * result in the merging of 2 or more pieces of the model. After this edit is applied, the
 * resulting location in the merged model may be obtained. This location should be used
 * for subsequent insertions.
 * @since 3.1
 */
public RemoveRange(TextRun begin, int so, TextRun end, int eo) {
	if (begin == end) {
		clipBeginning = new RemoveText(begin, so, eo);
	} else {
		List remove = ModelUtil.getModelSpan(begin, so, end, eo);

		removed = new ModelElement[remove.size()];
		removedFrom = new Container[removed.length];

		for (int i = remove.size() - 1; i >= 0; i--) {
			removed[i] = (ModelElement)remove.get(i);
			removedFrom[i] = removed[i].getContainer();
		}

		if (so > 0) clipBeginning = new RemoveText(begin, so, begin.size());
		if (eo < end.size()) clipEnding = new RemoveText(end, 0, eo);
	}
}

public void apply() {
	if (removed != null) {
		removalIndices = new int[removed.length];
		for (int i = removed.length - 1; i >= 0; i--) {
			removalIndices[i] = removedFrom[i].remove(removed[i]);
		}
	}
	if (clipBeginning != null) clipBeginning.apply();
	if (clipEnding != null) clipEnding.apply();
}

public ModelLocation getResultingLocation() {
	return null;
}

/**
 * @see org.eclipse.gef.examples.text.model.MiniEdit#reapply()
 */
public void reapply() {
	apply();
}

public void rollback() {
	if (removed != null) for (int i = 0; i < removed.length; i++)
		removedFrom[i].add(removed[i], removalIndices[i]);

	if (clipBeginning != null) clipBeginning.rollback();
	if (clipEnding != null) clipEnding.rollback();
}

}