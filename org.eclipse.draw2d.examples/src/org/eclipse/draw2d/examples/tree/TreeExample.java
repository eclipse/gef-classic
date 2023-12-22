/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.tree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.examples.AbstractExample;

/**
 * @author hudsonr Created on Apr 18, 2003
 */
public class TreeExample extends AbstractExample {

	private static final String NORMAL_STYLE = "Normal Style"; //$NON-NLS-1$

	private static final String CHILD_2_NAME = "Child 2"; //$NON-NLS-1$

	private static final String CHILD_1_NAME = "Child 1"; //$NON-NLS-1$

	private static final String BASIC_CHILD_NAME = "child"; //$NON-NLS-1$

	boolean animate;

	public static void main(String[] args) {
		new TreeExample().run();
	}

	private TreeRoot root;

	private PageNode selected;

	IFigure createPageNode(String title) {
		final PageNode node = new PageNode(title);
		node.addMouseListener(new MouseListener.Stub() {
			@Override
			public void mousePressed(MouseEvent me) {
				setSelected(node);
			}

			@Override
			public void mouseDoubleClicked(MouseEvent me) {
				doExpandCollapse();
			}
		});
		return node;
	}

	void doAddChild() {
		if (selected == null) {
			return;
		}
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.addBranch(new TreeBranch(createPageNode(BASIC_CHILD_NAME), parent.getStyle()));
	}

	void doAlignCenter() {
		if (selected == null) {
			return;
		}
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setAlignment(PositionConstants.CENTER);
	}

	void doAlignLeft() {
		if (selected == null) {
			return;
		}
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setAlignment(PositionConstants.LEFT);
	}

	void doDeleteChild() {
		if (selected == null) {
			return;
		}
		TreeBranch parent = (TreeBranch) selected.getParent();
		IFigure contents = parent.getContentsPane();
		if (contents.getChildren().isEmpty()) {
			return;
		}
		contents.remove(contents.getChildren().get(contents.getChildren().size() - 1));
	}

	void doExpandCollapse() {
		if (selected == null) {
			return;
		}
		TreeBranch parent = (TreeBranch) selected.getParent();
		if (parent.getSubtrees().isEmpty()) {
			return;
		}
		if (animate) {
			if (parent.isExpanded()) {
				parent.collapse();
			} else {
				parent.expand();
			}
		} else {
			parent.setExpanded(!parent.isExpanded());
		}
	}

	void doStyleHanging() {
		if (selected == null) {
			return;
		}
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setStyle(TreeBranch.STYLE_HANGING);
	}

	void doStyleNormal() {
		if (selected == null) {
			return;
		}
		TreeBranch parent = (TreeBranch) selected.getParent();
		parent.setStyle(TreeBranch.STYLE_NORMAL);
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#createContents()
	 */
	@Override
	protected IFigure createContents() {
		getFigureCanvas().setBackground(ColorConstants.white);
		root = new TreeRoot(createPageNode("Graph Root")); //$NON-NLS-1$

		TreeBranch branch = new TreeBranch(createPageNode(NORMAL_STYLE));
		root.addBranch(branch);
		root.addBranch(new TreeBranch(createPageNode("Child"))); //$NON-NLS-1$
		branch.addBranch(new TreeBranch(createPageNode(CHILD_1_NAME)));
		branch.addBranch(new TreeBranch(createPageNode(CHILD_2_NAME)));
		TreeBranch subbranch = new TreeBranch(createPageNode("Child 3")); //$NON-NLS-1$
		branch.addBranch(subbranch);

		subbranch.addBranch(new TreeBranch(createPageNode(BASIC_CHILD_NAME)));
		subbranch.addBranch(new TreeBranch(createPageNode(BASIC_CHILD_NAME)));
		subbranch.addBranch(new TreeBranch(createPageNode(BASIC_CHILD_NAME)));

		branch = new TreeBranch(createPageNode(NORMAL_STYLE), TreeBranch.STYLE_NORMAL);
		root.addBranch(branch);
		root.addBranch(new TreeBranch(createPageNode("Child"))); //$NON-NLS-1$
		branch.addBranch(new TreeBranch(createPageNode(CHILD_1_NAME), TreeBranch.STYLE_HANGING));
		subbranch = new TreeBranch(createPageNode(CHILD_2_NAME), TreeBranch.STYLE_HANGING);
		branch.addBranch(subbranch);
		subbranch.addBranch(new TreeBranch(createPageNode(BASIC_CHILD_NAME)));
		subbranch.addBranch(new TreeBranch(createPageNode(BASIC_CHILD_NAME)));

		branch = new TreeBranch(createPageNode(NORMAL_STYLE));
		root.addBranch(branch);
		branch.addBranch(new TreeBranch(createPageNode(CHILD_1_NAME)));
		branch.addBranch(new TreeBranch(createPageNode(CHILD_2_NAME)));
		branch.addBranch(new TreeBranch(createPageNode("Child 3"))); //$NON-NLS-1$

		return root;
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#run()
	 */
	@Override
	protected void hookShell(Shell shell) {
		Composite localShell = new Composite(shell, 0);
		localShell.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		localShell.setLayout(new GridLayout());
		Group rootGroup = new Group(localShell, 0);
		rootGroup.setText("Root Properties"); //$NON-NLS-1$
		FontData data = rootGroup.getFont().getFontData()[0];
		data.setStyle(SWT.BOLD);
		rootGroup.setLayout(new GridLayout());

		final Button orientation = new Button(rootGroup, SWT.CHECK);
		orientation.setText("Horizontal Orientation"); //$NON-NLS-1$
		orientation.setSelection(true);
		orientation.addListener(SWT.Selection, e -> root.setHorizontal(orientation.getSelection()));

		final Button useAnimation = new Button(rootGroup, SWT.CHECK);
		useAnimation.setText("Use Animation"); //$NON-NLS-1$
		useAnimation.setSelection(false);
		useAnimation.addListener(SWT.Selection, e -> animate = useAnimation.getSelection());

		final Button compress = new Button(rootGroup, SWT.CHECK);
		compress.setText("Compress Tree"); //$NON-NLS-1$
		compress.setSelection(false);
		compress.addListener(SWT.Selection, e -> {
			root.setCompression(compress.getSelection());
			root.invalidateTree();
			root.revalidate();
		});

		final Label majorLabel = new Label(rootGroup, 0);
		majorLabel.setText("Major Spacing: 10"); //$NON-NLS-1$
		final Scale major = new Scale(rootGroup, 0);
		major.setMinimum(5);
		major.setIncrement(5);
		major.setMaximum(50);
		major.setSelection(10);
		major.addListener(SWT.Selection, e -> {
			root.setMajorSpacing(major.getSelection());
			majorLabel.setText("Major Spacing: " + root.getMajorSpacing()); //$NON-NLS-1$
		});

		final Label minorLabel = new Label(rootGroup, 0);
		minorLabel.setText("Minor Spacing: 10"); //$NON-NLS-1$
		final Scale minor = new Scale(rootGroup, 0);
		minor.setMinimum(5);
		minor.setIncrement(5);
		minor.setMaximum(50);
		minor.setSelection(10);
		minor.addListener(SWT.Selection, e -> {
			root.setMinorSpacing(minor.getSelection());
			minorLabel.setText("Minor Spacing: " + root.getMinorSpacing()); //$NON-NLS-1$
		});

		Group selectedGroup = new Group(localShell, 0);
		selectedGroup.setText("Selected Node:"); //$NON-NLS-1$
		selectedGroup.setLayout(new GridLayout(2, true));

		Button addChild = new Button(selectedGroup, 0);
		addChild.setText("More Children"); //$NON-NLS-1$
		addChild.addListener(SWT.Selection, e -> doAddChild());

		Button removeChild = new Button(selectedGroup, 0);
		removeChild.setText("Fewer Children"); //$NON-NLS-1$
		removeChild.addListener(SWT.Selection, e -> doDeleteChild());

		Button alignCenter = new Button(selectedGroup, 0);
		alignCenter.setText("Align Center"); //$NON-NLS-1$
		alignCenter.addListener(SWT.Selection, e -> doAlignCenter());
		Button alignLeft = new Button(selectedGroup, 0);
		alignLeft.setText("Align Top/Left"); //$NON-NLS-1$
		alignLeft.addListener(SWT.Selection, e -> doAlignLeft());

		Button normal = new Button(selectedGroup, 0);
		normal.setText("Normal"); //$NON-NLS-1$
		normal.addListener(SWT.Selection, e -> doStyleNormal());

		Button hanging = new Button(selectedGroup, 0);
		hanging.setText("Hanging"); //$NON-NLS-1$
		hanging.addListener(SWT.Selection, e -> doStyleHanging());

		Button expandCollapse = new Button(selectedGroup, 0);
		expandCollapse.setText("expand/collapse"); //$NON-NLS-1$
		expandCollapse.addListener(SWT.Selection, e -> doExpandCollapse());
	}

	void setSelected(PageNode node) {
		if (selected != null) {
			selected.setSelected(false);
		}
		selected = node;
		selected.setSelected(true);
	}

}
