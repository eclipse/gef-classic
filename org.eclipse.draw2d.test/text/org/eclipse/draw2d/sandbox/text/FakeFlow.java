package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;
import java.util.*;
import org.eclipse.swt.graphics.Color;

public class FakeFlow
	extends FlowFigure
{

private static int LAST_COLOR_USED = 0;
private static Color colors[] = new Color[] {
	ColorConstants.white,
	ColorConstants.red,
	ColorConstants.yellow,
	ColorConstants.blue,
	ColorConstants.cyan
};
private static int SIZE = 20;
private double scale = 1.0;
private int spans[] = new int[SIZE];
private int ascents[] = new int[SIZE];

public FakeFlow(){
	this(1.0);
}

public FakeFlow(double scale){
	this.scale = scale;
	setBackgroundColor(getAnotherColor());
	for (int i=0; i < SIZE; i++){
		spans[i] = (int)(scale*(25 + random(75)));
		ascents[i] = (int)(scale*(15 + random(15)));
	}

	setLayoutManager(new FakeFlowLayout(spans, ascents, scale));
}

private static Color getAnotherColor(){
	int choice = random(colors.length);
	if (choice == LAST_COLOR_USED)
		choice = ++choice % colors.length;
	return colors[choice];
}

//return [0,max-1]
static private int random(int max){
	return (int)(Math.random()*max);
}

}
