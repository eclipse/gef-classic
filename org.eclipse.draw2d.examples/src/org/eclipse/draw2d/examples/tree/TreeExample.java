package org.eclipse.draw2d.examples.tree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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

Font BOLD;
TreeRoot root;

IFigure selected;

IFigure createPageNode(String title) {
	final PageNode node = new PageNode(title);
	node.addMouseListener(new MouseListener.Stub() {
		public void mouseReleased(MouseEvent me) {
			setSelected(node);
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
	parent.getParent().remove(parent);
	selected = null;
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

	TreeBranch branch;
	root.getContentsPane().add(branch = new TreeBranch(createPageNode("Normal Style")));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 1")));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 2")));

	root.getContentsPane().add(branch = new TreeBranch(createPageNode("Hanging Style"), TreeBranch.STYLE_HANGING));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 1"), TreeBranch.STYLE_HANGING));
	branch.getContentsPane().add(new TreeBranch(createPageNode("Child 2"), TreeBranch.STYLE_HANGING));
	
	return root;
}

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#run()
 */
protected void run() {
	Display d = Display.getDefault();
	Shell localShell = new Shell(d);
	localShell.setText("Test Window");
	
	localShell.setLayout(new GridLayout());
	Group rootGroup = new Group(localShell, 0);
	rootGroup.setText("Root Properties");
	FontData data = rootGroup.getFont().getFontData()[0];
	data.setStyle(SWT.BOLD);
	BOLD = new Font(null, data);
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
	final Slider major = new Slider(rootGroup, 0);
	major.setValues(10,5,50,5,5,10);
	major.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			root.setMajorSpacing(major.getSelection());
			majorLabel.setText("Major Spacing: " + root.getMajorSpacing());
		}
	});
	
	final Label minorLabel = new Label(rootGroup, 0);
	minorLabel.setText("Minor Spacing: 10");
	final Slider minor = new Slider(rootGroup, 0);
	minor.setValues(10,5,50,5,5,10);
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
	addChild.setText("Add Child");
	addChild.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			doAddChild();
		}
	});

	
	Button removeChild = new Button(selectedGroup, 0);
	removeChild.setText("Delete");
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
	
	localShell.pack();
	localShell.setLocation(400,100);
	localShell.open();
	
	super.run();
}

void setSelected(IFigure figure) {
	if (selected != null) {
		selected.invalidateTree();
		selected.setFont(null);
	}
	selected = figure;
	selected.invalidateTree();
	selected.setFont(BOLD);
}

}
