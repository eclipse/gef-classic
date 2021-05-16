/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.examples.tree;

import org.eclipse.draw2dl.examples.AbstractExample;
import org.eclipse.draw2dl.ColorConstants;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.PositionConstants;

/**
 * @author hudsonr
 * Created on Apr 18, 2003
 */
public class SimpleTreeExample extends AbstractExample {

public static void main(String[] args) {
	new SimpleTreeExample().run();
}

/**
 * @see AbstractExample#getContents()
 */
protected IFigure getContents() {
	getFigureCanvas().setBackground(ColorConstants.white);
	TreeRoot root = new TreeRoot(new PageNode("Graph Root"));
	root.setAlignment(PositionConstants.LEFT);

	TreeBranch branch;
	root.getContentsPane().add(branch = new TreeBranch(new PageNode("Child 1")));

//	branch.getContentsPane().add(new TreeBranch(new PageNode("Child 1")));
//	branch.getContentsPane().add(new TreeBranch(new PageNode("Child 2")));

//	root.getContentsPane().add(new TreeBranch(new PageNode("Child 2")));
	root.getContentsPane().add(branch = new TreeBranch(new PageNode("Child 2"), TreeBranch.STYLE_HANGING));
	branch.getContentsPane().add(new TreeBranch(new PageNode("Child 1")));
//	branch.getContentsPane().add(new TreeBranch(new PageNode("Child 2")));
	
	return root;
}

}
