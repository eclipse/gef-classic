package org.eclipse.draw2d.sandbox.text;

import org.eclipse.draw2d.geometry.*;

public class LineBox
	extends CompositeBox
{

protected int ascent = 0;

public void add(BlockInfo block){
	block.y = y;
	super.add(block);
}

public void commit(){
	int baseline = getBaseline();
	for (int i=0; i<children.size(); i++){
		BlockInfo block = (BlockInfo)children.elementAt(i);
		block.makeBaseline(baseline);
	}
}

public int getAscent(){
	validate();
	return ascent;
}

public int getBaseline(){
	return y+getAscent();
}

public int getDescent(){
	validate();
	return height-ascent;
}

public int getRemainingWidth(){
	if (invalid) validate();
	return recommendedWidth - width;
}

public void makeBaseline(int value){
	super.makeBaseline(value);
	commit();
}

protected void resetInfo(){
	super.resetInfo();
	ascent = 0;
}

protected void unionInfo(BlockInfo blockInfo){
	int descent = height-ascent;
	ascent = Math.max(ascent, blockInfo.getAscent());
	height = ascent+Math.max(descent,blockInfo.getDescent());
	super.unionInfo(blockInfo);
}

}