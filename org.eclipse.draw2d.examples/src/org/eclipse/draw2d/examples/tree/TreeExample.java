package org.eclipse.draw2d.examples.tree;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.examples.AbstractExample;

/**
 * 
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
	TreeBranch root = new TreeBranch(new Label("Graph Root"));

	root.getContentsPane().add(new TreeBranch(new Label("Child 1")));
	
	TreeBranch grandchild;
	TreeBranch subTree = new TreeBranch(new Label("Child 2 "));
	
	Button button = new Button("GrandChild A");
	button.setPreferredSize(80,22);
	final TreeBranch gcb = new TreeBranch(button);
	button.setBorder(ButtonBorder.BUTTON);
	button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			gcb.getContentsPane().add(new TreeBranch(new Label("new child")));
		}
	});
	subTree.getContentsPane().add(gcb);
	subTree.getContentsPane().add(grandchild = new TreeBranch(new Label("Grandchild C")));
	subTree.getContentsPane().add(grandchild = new TreeBranch(new Label("Grandchild D")));
	subTree.getContentsPane().add(grandchild = new TreeBranch(new Label("Grandchild E")));
	root.getContentsPane().add(subTree);

	grandchild.getContentsPane().add(grandchild = new TreeBranch(new Label("GGC 1")));

	grandchild.getContentsPane().add(new TreeBranch(new Label("GGGC A")));
	grandchild.getContentsPane().add(new TreeBranch(new Label("GGGC B")));

	root.getContentsPane().add(new TreeBranch(new Label("Child 3")));
	root.getContentsPane().add(new TreeBranch(new Label("Child 4")));
	root.getContentsPane().add(subTree = new TreeBranch(new Label("Child 5")));
	subTree.getContentsPane().add(new TreeBranch(new Label("Grandchild A")));
	subTree.getContentsPane().add(new TreeBranch(new Label("Grandchild B")));
	return root;
}

}
