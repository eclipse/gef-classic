package org.eclipse.draw2d.examples.tree;

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

/**
 * @see org.eclipse.draw2d.examples.AbstractExample#getContents()
 */
protected IFigure getContents() {
	getFigureCanvas().setBackground(ColorConstants.white);
	TreeRoot root = new TreeRoot(new PageNode("Graph Root"));

	root.getContentsPane().add(new TreeBranch(new PageNode("Child 1")));
	
	TreeBranch grandchild;
	TreeBranch subTree = new TreeBranch(new PageNode("Child 2 "));
	
	Button button = new Button("GrandChild A");
//	button.setPreferredSize(80,22);
	final TreeBranch gcb = new TreeBranch(button);
	button.setBorder(ButtonBorder.BUTTON);
	button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			gcb.getContentsPane().add(new TreeBranch(new PageNode("new child")));
		}
	});
	subTree.getContentsPane().add(gcb);
	subTree.getContentsPane().add(grandchild = new TreeBranch(new PageNode("Grandchild C\ntwo lines")));
	subTree.getContentsPane().add(grandchild = new TreeBranch(new PageNode("Grandchild D")));
	subTree.getContentsPane().add(grandchild = new TreeBranch(new PageNode("Grandchild E Is really wide")));
	root.getContentsPane().add(subTree);

	grandchild.getContentsPane().add(grandchild = new TreeBranch(new PageNode("GGC 1")));

	grandchild.getContentsPane().add(new TreeBranch(new PageNode("GGGC A")));
	grandchild.getContentsPane().add(new TreeBranch(new PageNode("GGGC B")));

	root.getContentsPane().add(new TreeBranch(new PageNode("Child 3")));
	root.getContentsPane().add(new TreeBranch(new PageNode("Child 4")));
	root.getContentsPane().add(subTree = new TreeBranch(new PageNode("Child 5"), TreeBranch.STYLE_HANGING));
	subTree.getContentsPane().add(new TreeBranch(new PageNode("Grandchild A")));
	subTree.getContentsPane().add(new TreeBranch(new PageNode("Grandchild C")));
	subTree.getContentsPane().add(new TreeBranch(new PageNode("Grandchild D")));
	subTree.getContentsPane().add(new TreeBranch(new PageNode("Grandchild E")));
	subTree.getContentsPane().add(subTree = new TreeBranch(new PageNode("Grandchild B")), 1);
	((TreeBranch)subTree).setAlignment(PositionConstants.LEFT);
	subTree.getContentsPane().add(new TreeBranch(new PageNode("GGC 1")));
	subTree.getContentsPane().add(new TreeBranch(new PageNode("GGC 2")));
	subTree.getContentsPane().add(new TreeBranch(new PageNode("GGC 3")));
	subTree.getContentsPane().add(new TreeBranch(new PageNode("GGC 4")));
	return root;
}

}
