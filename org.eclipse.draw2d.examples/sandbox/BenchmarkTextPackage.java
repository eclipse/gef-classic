import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;

public class BenchmarkTextPackage {

static class StyledLine extends Figure {
	Dimension pref;
	private String text;
	
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (pref == null)
			pref = FigureUtilities.getTextExtents(text, getFont());
		return pref;
	}
	
	protected void paintFigure(Graphics graphics) {
		graphics.setFont(getFont());
		graphics.drawText(text, bounds.x, bounds.y);
	}
	
	public void setText(String text) {
		this.text = text;
		pref = null;
		revalidate();
	}
	public String getText() {
		return text;
	}
}

static Font BIG = new Font(null, "Times", 18, SWT.BOLD);
static StyledLine typing;
public static void main(String[] args) {
	final Display display = Display.getDefault();
	final Shell shell = new Shell(SWT.SHELL_TRIM);
	shell.setLayout(new FillLayout());

	//ImageData data = new ImageData(StyleLines.class.getResourceAsStream("class.gif"));
//	final Image image = new Image(null, data, data.getTransparencyMask());

	FigureCanvas canvas = new FigureCanvas(shell);
	
	Figure page = new Figure();
	ToolbarLayout layout = new ToolbarLayout();
	layout.setStretchMinorAxis(false);
	layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
	page.setLayoutManager(layout);
	
	for (int i = 0; i < 50000; i++) {
		StyledLine line = new StyledLine(); 
		if (i % 20 == 0)
			line.setFont(BIG);
		line.setText("This is line number " + i);
		if (i == 99)
			typing = line;
		page.add(line);
	}
	canvas.setContents(page);
	
	canvas.addKeyListener(new KeyListener() {
		public void keyPressed(KeyEvent e) {
			typing.setText(typing.getText() + e.character);
			typing.revalidate();
			typing.repaint();
		}

		public void keyReleased(KeyEvent e) {}
	});
	
	shell.setSize(400, 300);
	shell.open();

	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}