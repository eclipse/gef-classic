import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class AlphaImageData {

public static void main(String[] args) {
	Display display = Display.getDefault();
	Shell shell = new Shell(display);
	
	PaletteData pData = new PaletteData(0xFF, 0xFF00, 0xFF0000);
	RGB rgb = new RGB(80,0,150);
	int fillColor = pData.getPixel(rgb);
	ImageData iData = new ImageData(1, 1, 24, pData);
	iData.setPixel(0, 0, fillColor);
	iData.setAlpha(0, 0, 55);
	final Image image = new Image(display, iData);
	
	shell.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event event) {
			event.gc.drawImage(image, 0,0,1,1,40,40,40,40);
		}
	});
	
	shell.open();
	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}
