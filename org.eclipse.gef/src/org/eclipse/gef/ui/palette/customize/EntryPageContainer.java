package org.eclipse.gef.ui.palette.customize;

/**
 * An <code>EntryPageContainer</code> allows an <code>EntryPage</code> to report errors to
 * it.
 * 
 * @author Pratik Shah
 */
public interface EntryPageContainer {

/**
 * Clears the error.
 */
public void clearProblem();

/**
 * Shows the error to the user.
 * 
 * @param description	A description of the problem.  Should be as brief as possible.
 */
public void showProblem(String description);

}
