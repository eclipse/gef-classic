package org.eclipse.draw2d.sandbox.text;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Color;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import java.util.Vector;
import java.text.BreakIterator;

public class TextFigure
	extends FlowFigure
{

{
	setLayoutManager(new TextLayout());
}

private String text;

public TextFigure(){this("");}
public TextFigure(String s){text = s;}

public String getText(){return text;}

protected void paintFigure(Graphics g){
	TextFragmentBox frag;
	Rectangle r;
	Color bgColor = g.getBackgroundColor();
	for (int i=0; i< fragments.size(); i++){
		frag = (TextFragmentBox)fragments.get(i);
		r = frag.getBounds();
		g.fillRectangle(r);
		if (SHOW_BASELINE){
			g.setBackgroundColor(FigureUtilities.darker(bgColor));
			g.fillRectangle(r.x,r.y+frag.getAscent(), r.width, r.height-frag.getAscent());
			g.setBackgroundColor(bgColor);
		}
		g.drawString(
			text.substring(frag.offset, frag.offset+frag.length),
			new Point(frag.x, frag.y),
			g.TRANSPARENT);
		//FigureUtilities.paintEtchedBorder(g,r);
	}
}

public void setText(String s){
	text = s;
	revalidate();
}

}
