package swt.bugs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/*
 * Shows that expansion comes after key events, but before mouse events.
 */
public class GTKTreeItemAccess {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell();
	shell.setLayout(new GridLayout());

	Text text = new Text(shell, SWT.MULTI);
	text.setText("blah");
	text.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			System.out.println("widget selected");
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			System.out.println("widget default selected");
		}
	});
	
	Tree tree = new Tree(shell, 0);
	new TreeItem(tree, 0).setText("item 1");
	new TreeItem(tree, 0).setText("item 2");
	new TreeItem(new TreeItem(tree, 0), 0).setText("blah");
	
	tree.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			System.out.println("widget selected");
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			System.out.println("widget default selected");
		}
	});
	
	tree.addMouseListener(new MouseAdapter() {
		public void mouseDown(MouseEvent e) {
			System.out.println(e.time + " mouse down");
		}
	});
	
	tree.addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			//e.doit = false;
			System.out.println("key pressed");
		}
	});
	
	tree.addTreeListener(new TreeListener() {
		public void treeCollapsed(TreeEvent e) {
			System.out.println("collapse");
		}

		public void treeExpanded(TreeEvent e) {
			System.out.println("expand");
		}
	});

	shell.setSize(400,300);
	shell.open();
	
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}