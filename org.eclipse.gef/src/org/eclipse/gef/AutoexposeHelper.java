package org.eclipse.gef;

import org.eclipse.draw2d.geometry.Point;

/**
 * @author hudsonr
 */
public interface AutoexposeHelper {

boolean detect(Point where);

boolean step(Point where);

/**
 * Used with EditPartViewers to find the AutoexposeHelper at a Point. Clients can
 * instantiate the search, call {@link EditPartViewer#findObjectAtExcluding(Point,
 * Collection, Conditional)}, and then check the {@link #result} field.
 */
class Search implements EditPartViewer.Conditional {
	public Search(Point pt){
		where = pt;
	}
	/**
	 * the result of the search.
	 */
	private Point where;
	public AutoexposeHelper result;
	public boolean evaluate(EditPart editpart) {
		result = (AutoexposeHelper)editpart.getAdapter(AutoexposeHelper.class);
		if (result != null && result.detect(where))
			return true;
		result = null;
		return false;
	}
}

}
