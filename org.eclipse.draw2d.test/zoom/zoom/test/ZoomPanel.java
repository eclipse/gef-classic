package zoom.test;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.*;

public class ZoomPanel
	extends Panel
{

private float zoom = 1.0f;
private int size = 29;

Color[] colors  = new Color[]{
	ColorConstants.red,
	ColorConstants.blue,
	ColorConstants.green
};

Label l;

public ZoomPanel(){
	for (int i=0; i<3; i++)
		for (int j=0; j<3; j++){
			Shape shape = createShape(j);
			shape.setFill(i > 0);
			shape.setLineWidth(1);
			shape.setOutline(i<2);
			shape.setBackgroundColor(colors[j]);
			shape.setBounds(new Rectangle(
				i*(size), j*(size), size, size));
			add(shape);
		}
		
		l = new Label("Zoom");
		add(l);
		l.setBounds(new Rectangle(5,29,100,50));
		l.setLabelAlignment(l.LEFT);
		Polygon poly = new Polygon();
		poly.addPoint(new Point(100,90));
		poly.addPoint(new Point(105,44));
		poly.addPoint(new Point(120,70));
		poly.setFill(true);
		poly.setBackgroundColor(ColorConstants.orange);
		add(poly);
}

Shape createShape(int c){
	if (c == 1)
		return new RectangleFigure();
	if (c==0)
		return new RoundedRectangle();
	return new Ellipse(){
		protected void outlineShape(Graphics graphics) {
			super.outlineShape(graphics);
			graphics.drawLine(getBounds().getLeft(), getBounds().getRight().translate(-1,0));
			graphics.drawLine(getBounds().getTop(), getBounds().getCenter());
		}
	};
}

public void paint(Graphics graphics) {
	ZoomGraphics g = new ZoomGraphics(graphics);

	Point correction = getClientArea().getLocation();
	graphics.translate(correction);
	g.scale(zoom);
	super.paint(g);
}

public void setZoom(float zoom){
	this.zoom = zoom;
	l.setText("Zoom = " + ((int)(zoom*100))+"%");
	repaint();
}

}
