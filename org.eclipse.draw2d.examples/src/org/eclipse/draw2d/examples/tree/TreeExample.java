package org.eclipse.draw2d.examples.tree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.examples.AbstractExample;

/**
 * @author hudsonr
 * Created on Apr 18, 2003
 */
public class TreeExample extends AbstractExample {

public static void main(String[] args) {
	new TreeExample().run();
}

TreeRoot root;

PageNode selected;

IFigure createPageNode(String title) {
	final PageNode node = new PageNode(title);
	node.addMouseListener(new MouseListener.Stub() {
		public void mouseReleased(MouseEvent me) {
			setSelected(node);
		}
		public void mouseDoubleClicked(MouseEvent me) {
			doExpandCollapse();
		}
	});
	return node;
}

void doAddChild() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.getContentsPane().add(new TreeBranch(createPageNode("child"), parent.getStyle()));
}

void doAlignCenter() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setAlignment(PositionConstants.CENTER);
}

void doAlignLeft() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setAlignment(PositionConstants.LEFT);
}

void doDeleteChild() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	IFigure contents = parent.getContentsPane();
	if (contents.getChildren().isEmpty())
		return;
	contents.remove(
		(IFigure)contents.getChildren().get(contents.getChildren().size() - 1));
}

void doExpandCollapse() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	if (parent.isExpanded())
		parent.collapse();
	else parent.expand();
}

void doStyleHanging() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setStyle(TreeBranch.STYLE_HANGING);
}

void doStyleNormal() {
	if (selected == null)
		return;
	TreeBranch parent = (TreeBranch)selected.getParent();
	parent.setStyle(TreeBranch.STYLE_NORMAL);
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	getFigureCanvas().setBackground(ColorConstants.white);
	root = new TreeRoot(createPageNode("Graph Root"));

	TreeBranch branch, subbranch;
	root.getContentsPane().add(branch = new TreeBranch(createPageNode("Normal Style")));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 1")));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 2")));
	branch.getContentsPane().add(subbranch = new TreeBranch(createPageNode("Child 3")));

	subbranch.getContentsPane().add(new TreeBranch(createPageNode("child")));
	subbranch.getContentsPane().add(new TreeBranch(createPageNode("child")));

	root.getContentsPane().add(branch = new TreeBranch(createPageNode("Hanging Style"), TreeBranch.STYLE_HANGING));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 1"), TreeBranch.STYLE_HANGING));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 2"), TreeBranch.STYLE_HANGING));

	root.getContentsPane().add(branch = new TreeBranch(createPageNode("Normal Style")));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 1")));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 2")));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 3")));
	
	return root;
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#run()
 */
protected void hookShell() {
	Composite localShell = new Composite(shell, 0);
	localShell.setLayoutData(new GridData(GridData.FILL_VERTICAL));
	
	localShell.setLayout(new GridLayout());
	Group rootGroup = new Group(localShell, 0);
	rootGroup.setText("Root Properties");
	FontData data = rootGroup.getFont().getFontData()[0];
	data.setStyle(SWT.BOLD);
	rootGroup.setLayout(new GridLayout());

	final Button orientation = new Button(rootGroup, SWT.CHECK);
	orientation.setText("Horizontal Orientation");
	orientation.setSelection(true);
	orientation.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setHorizontal(orientation.getSelection());
		}
	});

	final Label majorLabel = new Label(rootGroup, 0);
	majorLabel.setText("Major Spacing: 10");
	final Scale major = new Scale(rootGroup, 0);
	major.setMinimum(5);
	major.setIncrement(5);
	major.setMaximum(50);
	major.setSelection(10);
	major.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setMajorSpacing(major.getSelection());
			majorLabel.setText("Major Spacing: " + root.getMajorSpacing());
		}
	});
	
	final Label minorLabel = new Label(rootGroup, 0);
	minorLabel.setText("Minor Spacing: 10");
	final Scale minor = new Scale(rootGroup, 0);
	minor.setMinimum(5);
	minor.setIncrement(5);
	minor.setMaximum(50);
	minor.setSelection(10);
	minor.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setMinorSpacing(minor.getSelection());
			minorLabel.setText("Minor Spacing: " + root.getMinorSpacing());
		}
	});
	
	Group selectedGroup = new Group(localShell, 0);
	selectedGroup.setText("Selected Node:");
	selectedGroup.setLayout(new GridLayout(2, true));
	
	Button addChild = new Button(selectedGroup, 0);
	addChild.setText("More Children");
	addChild.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doAddChild();
		}
	});

	
	Button removeChild = new Button(selectedGroup, 0);
	removeChild.setText("Fewer Children");
	removeChild.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doDeleteChild();
		}
	});

	Button alignCenter = new Button(selectedGroup, 0);
	alignCenter.setText("Align Center");
	alignCenter.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doAlignCenter();
		}
	});
	Button alignLeft = new Button(selectedGroup, 0);
	alignLeft.setText("Align Top/Left");
	alignLeft.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doAlignLeft();
		}
	});

	Button normal = new Button(selectedGroup, 0);
	normal.setText("Normal");
	normal.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doStyleNormal();
		}
	});
	
	Button hanging = new Button(selectedGroup, 0);
	hanging.setText("Hanging");
	hanging.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doStyleHanging();
		}
	});

	Button expandCollapse = new Button(selectedGroup, 0);
	expandCollapse.setText("expand/collapse");
	expandCollapse.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doExpandCollapse();
		}
	});
}

void setSelected(PageNode node) {
	if (selected != null) {
		selected.setSelected(false);
	}
	selected = node;
	selected.setSelected(true);
}

}
