package zoom.test;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScaledGraphics;

public class ComposedZooms extends Figure {

protected void paintFigure(Graphics graphics) {
	ScaledGraphics g = new ScaledGraphics(graphics);
	g.setBackgroundColor(ColorConstants.white);
	g.fillRectangle(getBounds());
	g.translate(10,5);
	g.drawLine(0,0,20,0);
	g.drawLine(0,0,0,20);
	
	g.drawLine(0,20,6,20);
	
	g.scale(1.5f);
	g.drawLine(0,0,4,10);
	g.translate(4,10);
	g.drawLine(0,0,20,0);
	g.drawLine(0,0,0,20);

	g.scale(1.0f/1.5f);
	g.drawLine(0,0,10,4);
	g.translate(10,4);
	g.drawLine(0,0,20,0);
	g.drawLine(0,0,0,20);
	
	g.translate(5,5);
	g.scale(0.3f);
	for (int i=0; i<100; i++){
		g.drawLine(0,0,100,0);
		g.translate(0,1);
	}
}

}
