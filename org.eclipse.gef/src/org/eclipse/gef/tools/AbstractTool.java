package org.eclipse.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


import java.util.*;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;


import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.internal.Internal;
import org.eclipse.gef.editparts.LayerManager;

/**
 * Default implementation support for {@link Tool}s.
 */
abstract public class AbstractTool
	extends org.eclipse.gef.internal.FlagSupport
	implements Tool, RequestConstants
{

static protected final int STATE_TERMINAL = 1 << 30;
static protected final int STATE_INITIAL  = 1;
static protected final int STATE_DRAG = 2;
static protected final int STATE_DRAG_IN_PROGRESS = 4;
static protected final int STATE_INVALID = 8;
static protected final int STATE_ACCESSIBLE_DRAG = 16;
static protected final int STATE_ACCESSIBLE_DRAG_IN_PROGRESS = 32;
static protected final int MAX_STATE = 32;

static protected final int MOUSE_BUTTON1 = SWT.BUTTON1;
static protected final int MOUSE_BUTTON2 = SWT.BUTTON2;
static protected final int MOUSE_BUTTON3 = SWT.BUTTON3;
static protected final int MOUSE_BUTTON_ANY =
	MOUSE_BUTTON1 | MOUSE_BUTTON2 | MOUSE_BUTTON3;

static private final int DRAG_THRESHOLD = 5;
static private final int FLAG_PAST_THRESHOLD = 1;
static private final int FLAG_HOVER = 2;
static private final int FLAG_UNLOAD = 4;{
	setFlag(FLAG_UNLOAD, true);
}
static private final int FLAG_ACTIVE = 8;
static protected final int MAX_FLAG = 8;

private EditDomain     domain;
private int            state;
private Command        command;
private EditPartViewer currentViewer;
private List           operationSet;
private int            startX;
private int            startY;
private Input          current;
private Cursor   defaultCursor;
private Cursor   disabledCursor;

private int accessibleStep;
private long accessibleBegin;

/**
 * Allows the user to access mouse and keyboard input.
 */
public static class Input
	extends org.eclipse.gef.internal.FlagSupport
{
	int modifiers;
	boolean verifyMouseButtons;
	Point mouse = new Point();

	protected int getModifiers() {
		return modifiers;
	}
	
	/**
	 * Returns the current location of the mouse.
	 */
	public Point getMouseLocation(){
		return mouse;
	}

	/**
	 * Returns <code>true</code> if the ALT key is pressed.
	 */
	public boolean isAltKeyDown(){
		return (modifiers & SWT.ALT) > 0;
	}
	
	/**
	 * Returns <code>true</code> if any of the 3 mouse buttons are pressed.
	 */
	public boolean isAnyButtonDown(){
		return getFlag(2|4|8);
	}
	
	/**
	 * Returns <code>true</code> if the CTRL key is pressed.
	 */
	public boolean isControlKeyDown(){
		return (modifiers & SWT.CONTROL) > 0;
	}
	
	/**
	 * Returns <code>true</code> if button # <code>which</code> (1, 2, or 3)
	 * is pressed.
	 */
	public boolean isMouseButtonDown(int which){
		return getFlag(1 << which);
	}
	
	/**
	 * Returns <code>true</code> if the SHIFT key is pressed.
	 */
	public boolean isShiftKeyDown(){
		return (modifiers & SWT.SHIFT) > 0;
	}
	
	/**
	 * Sets mouse button # <code>which</code> to be pressed if
	 * <code>state</code> is true.
	 */
	public void setMouseButton(int which, boolean state){
		setFlag(1 << which, state);
	}
	
	void setMouseLocation(int x, int y){
		mouse.setLocation(x,y);
	}

	/**
	 * Sets the mouse and keyboard input based on the MouseEvent.
	 */
	public void setInput(MouseEvent me){
		setMouseLocation(me.x, me.y);
		modifiers = me.stateMask;
		if (verifyMouseButtons){
			setMouseButton(1, (modifiers & MOUSE_BUTTON1) != 0);
			setMouseButton(2, (modifiers & MOUSE_BUTTON2) != 0);
			setMouseButton(3, (modifiers & MOUSE_BUTTON3) != 0);
			verifyMouseButtons = false;
		}
	}
}

boolean acceptAbort(KeyEvent e){
	return e.character == SWT.ESC;
}

boolean acceptArrowKey(KeyEvent e){
	int key = e.keyCode;
	if (!(isInState(STATE_INITIAL |
				STATE_ACCESSIBLE_DRAG |
				STATE_ACCESSIBLE_DRAG_IN_PROGRESS)))
		return false;
	return (key == SWT.ARROW_UP) ||
		(key == SWT.ARROW_RIGHT) ||
		(key == SWT.ARROW_DOWN) ||
		(key == SWT.ARROW_LEFT);
}

boolean acceptDragCommit(KeyEvent e){
	return isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS)&&
		e.character == 13;
}

int accGetStep(){
	return accessibleStep;
}

void accStepIncrement(){
	if (accessibleBegin == -1){
		accessibleBegin = new Date().getTime();
		accessibleStep = 1;
	} else {
		accessibleStep = 4;
		long elapsed = new Date().getTime() - accessibleBegin;
		if (elapsed > 1000)
			accessibleStep = Math.min(16, (int)(elapsed/150));
	}
}

void accStepReset(){
	accessibleBegin = -1;
}

/**
 * Activates the tool.  Any initialization should be performed here.
 * This method is called when a tool is selected.
 * 
 * @see #deactivate()
 */
public void activate() {
	resetFlags();
	accessibleBegin = -1;
	getCurrentInput().verifyMouseButtons = true;
	setState(STATE_INITIAL);
	setFlag(FLAG_ACTIVE, true);
	debug("ACTIVATED");//$NON-NLS-1$
}

/**
 * Adds the given figure to the feedback layer.
 */
protected void addFeedback(IFigure figure) {
	LayerManager lm = (LayerManager)getCurrentViewer().
		getEditPartRegistry().get(LayerManager.ID);
	if (lm == null)
		return;
	lm.getLayer(LayerConstants.FEEDBACK_LAYER).add(figure);
}

/**
 * Determines (and returns) the appropriate cursur.
 */
protected Cursor calculateCursor(){
	if (isInState(STATE_TERMINAL))
		return null;
	Command command = getCurrentCommand();
	if (command == null || !command.canExecute())
		return getDisabledCursor();
	return getDefaultCursor();
}

public void commitDrag(){}

/**
 * Creates and returns a List of {@link EditPart}s that the tool
 * will be working with.
 */
protected List createOperationSet(){
	return new ArrayList(getCurrentViewer().getSelectedEditParts());
}

/**
 * Deactivates the tool. This method is called whenever the user
 * switches to another tool. Use this method to do some clean-up
 * when the tool is switched. The abstract tool allows cursors for 
 * viewers to be changed. When the tool is deactivated it must 
 * revert to normal the cursor of the last tool it changed.
 * 
 * @see #activate()
 */
public void deactivate() {
	setFlag(FLAG_ACTIVE, false);
	setViewer(null);
	setCurrentCommand(null);
	operationSet = null;
	current = null;
	debug("DE-ACTIVATED"); //$NON-NLS-1$
}

/**
 * Prints a string in the GEF Debug console if the Tools
 * debug option is selected.
 */
protected void debug(String message){
	if (GEF.DebugTools){
		GEF.debug("TOOL:\t" + getDebugName() + //$NON-NLS-1$
			":\t" + message); //$NON-NLS-1$
	}
}

/**
 * Called when a viewer that the editor controls gains focus.
 * 
 * @param event The SWT focus event 
 * @param viewer The viewer that the focus event is over.
 */
public void focusGained(FocusEvent event, EditPartViewer viewer) {
	setViewer(viewer);
	debug("Focus Gained: " + viewer); //$NON-NLS-1$
	handleFocusGained();
}

/**
 * Called when a viewer that the editor controls loses focus.
 * 
 * @param event The SWT focus event 
 * @param viewer The viewer that the focus event is over.
 */
public void focusLost(FocusEvent event, EditPartViewer viewer) {
	setViewer(viewer);
	debug("Focus Lost: " + viewer); //$NON-NLS-1$
	handleFocusLost();
}

/**
 * Execute the currently active command.
 */
protected void executeCurrentCommand() {
	Command curCommand = getCurrentCommand();
	if (curCommand != null && curCommand.canExecute()) {
		getDomain().getCommandStack().execute(curCommand);
		setCurrentCommand(null);
	}
}

/**
 * Re-queries the target viewer object for a command
 */
protected Command getCommand(){
	return org.eclipse.gef.commands.UnexecutableCommand.INSTANCE;
}

/**
 * Returns the name identifier of the command that the tool
 * is currently looking for.
 */
abstract protected String getCommandName();

/**
 * Returns the currently active command.
 */
protected Command getCurrentCommand() {
	return command;
}

/**
 * Returns the current mouse and keyboard input.
 */
protected Input getCurrentInput(){
	if (current == null)
		current = new Input();
	return current;
}

/**
 * Return the viewer that the tool is currently operating
 * on.
 */
protected EditPartViewer getCurrentViewer() {
	return currentViewer;
}

/**
 * Returns the debug name for this tool.
 */
abstract protected String getDebugName();

/**
 * Returns a String representation of the given state for 
 * debug purposes.
 */
protected String getDebugNameForState(int state){
	switch (state){
		case STATE_INITIAL:
			return "Initial State";//$NON-NLS-1$
		case STATE_DRAG:
			return "Drag State";//$NON-NLS-1$
		case STATE_DRAG_IN_PROGRESS:
			return "Drag In Progress State";//$NON-NLS-1$
		case STATE_INVALID:
			return "Invalid State"; //$NON-NLS-1$
		case STATE_TERMINAL:
			return "Terminal State"; //$NON-NLS-1$
		case STATE_ACCESSIBLE_DRAG:
			return "Accessible Drag"; //$NON-NLS-1$
		case STATE_ACCESSIBLE_DRAG_IN_PROGRESS:
			return "Accessible Drag In Progress"; //$NON-NLS-1$
	}
	return "Unknown state:";//$NON-NLS-1$
}

/**
 * Returns the default {@link Cursor}.
 */
protected Cursor getDefaultCursor(){
	return defaultCursor;
}

/**
 * Returns the disabled {@link Cursor}.
 */
protected Cursor getDisabledCursor(){
	if (disabledCursor != null)
		return disabledCursor;
	return getDefaultCursor();
}

/**
 * Returns the EditDomain.
 */
protected EditDomain getDomain(){
	return domain;
}

/**
 * Return the number of pixels that the mouse has been moved
 * since that drag was started.
 */
protected Dimension getDragMoveDelta() {
	return getLocation().getDifference(getStartLocation());
}

/**
 * Return the current x, y position of the cursor.
 */
protected Point getLocation(){
	return new Point(getCurrentInput().getMouseLocation());
}

/**
 * Returns the collection of editparts that the drag is
 * operating on.
 */
protected List getOperationSet() {
	if (operationSet == null)
		operationSet = createOperationSet();
	return operationSet;
}

/**
 * Returns the starting location for the current tool operation.
 */
protected Point getStartLocation() {
	return new Point(startX, startY);
}

/**
 * Returns the current state.
 */
protected int getState(){
	return state;
}

/**
 * Called when the mouse button has been pressed.
 * Should be implemented to process the users input correctly.
 * Boolean should be used to indicate to your subclasses if
 * you processed the event (true) or you didn't process it (false)
 * and the subclass might process it.
 * Key presses are a good example of this type of behavior.
 */
protected boolean handleButtonDown(int button){return false;}

/**
 * Handles the high-level processing of a mouse release.
 * @see #mouseUp(MouseEvent, EditPartViewer)
 */
protected boolean handleButtonUp(int button){return false;}

/**
 * Handles high-level processing of a double click.
 * @see #mouseDoubleClick(MouseEvent, EditPartViewer)
 */
protected boolean handleDoubleClick(int button){
	return false;
}

/**
 * Handles high-level processing of a mouse drag.
 * @see #mouseDrag(MouseEvent, EditPartViewer)
 */
protected boolean handleDrag(){return false;}

/**
 * Handles high-level processing of a mouse drag once the
 * threshold has been passed.
 * @see #movedPastThreshold()
 * @see #mouseDrag(MouseEvent, EditPartViewer)
 */
protected boolean handleDragInProgress(){return false;}

/**
 * Called once when the drag threshold has been passed.
 * @see #movedPastThreshold()
 * @see #mouseDrag(MouseEvent, EditPartViewer)
 */
protected boolean handleDragStarted(){return false;}

/**
 * Called when the current tool operation is complete.
 */
protected void handleFinished(){
	if (unloadWhenFinished())
		getDomain().loadDefaultTool();
	else
		reactivate();
}

/**
 * Handles high-level processing of a focus gained event.
 * @see #focusGained(FocusEvent, EditPartViewer)
 */
protected boolean handleFocusGained() {
	return false;
}

/**
 * Handles high-level processing of a focus lost event.
 * @see #focusLost(FocusEvent, EditPartViewer)
 */
protected boolean handleFocusLost() {
	return false;
}

/**
 * Handles high-level processing of a mouse hover event.
 * @see #mouseHover(MouseEvent, EditPartViewer)
 */
protected boolean handleHover(){
	return false;
}

/**
 * Handles high-level processing of a key down event.
 * @see #keyDown(KeyEvent, EditPartViewer)
 */
protected boolean handleKeyDown(KeyEvent e){
	if (acceptAbort(e)) {
		getDomain().loadDefaultTool();
		return true;
	}
	return false;
}

/**
 * Handles high-level processing of a key up event.
 * @see #keyUp(KeyEvent, EditPartViewer)
 */
protected boolean handleKeyUp(KeyEvent e) {
	return false;
}

/**
 * Handles high-level processing of a mouse move.
 * @see #mouseMove(MouseEvent, EditPartViewer)
 */
protected boolean handleMove(){return false;}

/**
 * Called when the mouse enters an EditPartViewer.
 */
protected boolean handleViewerEntered() {
	return false;
}

/**
 * Called when the mouse exits an EditPartViewer.
 */
protected boolean handleViewerExited() {
	return false;
}

/**
 * Returns <code>true</code> if the tool is active.
 */
protected boolean isActive(){
	return getFlag(FLAG_ACTIVE);
}

/**
 * Returns <code>true</code> if the tool is hovering.
 */
protected boolean isHoverActive(){
	return getFlag(FLAG_HOVER);
}

/*
 * Returns <code>true</code> if the current {@link Input} is
 * synchronized with the current MouseEvent.
 */
private boolean isInputSynched(MouseEvent event) {
	Input input = getCurrentInput();
	boolean button1ok = input.isMouseButtonDown(1) == 
					((event.stateMask & SWT.BUTTON1) != 0);
	boolean button2ok = input.isMouseButtonDown(2) == 
					((event.stateMask & SWT.BUTTON2) != 0);
	boolean button3ok = input.isMouseButtonDown(3) == 
					((event.stateMask & SWT.BUTTON3) != 0);
	return (button1ok && button2ok && button3ok);
}

boolean isInDragInProgress(){
	return isInState(STATE_DRAG_IN_PROGRESS | STATE_ACCESSIBLE_DRAG_IN_PROGRESS);
}

/**
 * Returns <code>true</code> if the tool is in the given state.
 */
protected boolean isInState(int state) {
	return ((getState() & state) != 0);
}

/**
 * Processes a KeyDown event for the given viewer.  Subclasses wanting
 * to handle this event should override {@link #handleKeyDown(KeyEvent)}.
 */
public void keyDown(KeyEvent evt, EditPartViewer viewer) {
	setViewer(viewer);
	debug("Key (" + evt.character + ',' + evt.keyCode+ ") down:\t");//$NON-NLS-2$//$NON-NLS-1$
	handleKeyDown(evt);
}

/**
 * Processes a KeyUp event for the given viewer.  Subclasses wanting
 * to handle this event should override {@link #handleKeyUp(KeyEvent)}.
 */
public void keyUp(KeyEvent evt, EditPartViewer viewer) {
	setViewer(viewer);
	debug("Key (" + evt.character + ',' + evt.keyCode+ ") up:\t");//$NON-NLS-2$//$NON-NLS-1$
	handleKeyUp(evt);
}

/**
 * Returns <code>true</code> if the mouse has been dragged past
 * the drag threshold.
 */
protected boolean movedPastThreshold() {
	if (getFlag(FLAG_PAST_THRESHOLD))
		return true;
	Point start = getStartLocation(),
		  end = getLocation();
	if (Math.abs(start.x - end.x) > DRAG_THRESHOLD ||
	    Math.abs(start.y - end.y) > DRAG_THRESHOLD)
	{
		setFlag(FLAG_PAST_THRESHOLD, true);
		return true;
	}
	return false;
}

/**
 * Handles mouse double click events within a viewer.  Subclasses wanting
 * to handle this event should override {@link #handleDoubleClick(int)}.
 */
public void mouseDoubleClick(MouseEvent me, EditPartViewer viewer) {
	if (me.button > 5)
		return;
	setViewer(viewer);
	getCurrentInput().setInput(me);

	debug("B" + me.button + " double click on:\t");//$NON-NLS-2$//$NON-NLS-1$
	handleDoubleClick(me.button);
}

/**
 * Handles mouse down events within a viewer.  Subclasses wanting
 * to handle this event should override {@link #handleButtonDown(int)}.
 */
public void mouseDown(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);

	getCurrentInput().setInput(me);
	getCurrentInput().setMouseButton(me.button, true);

	startX = me.x;
	startY = me.y;

	debug("B" + me.button + " down on:\t");//$NON-NLS-2$//$NON-NLS-1$
	handleButtonDown(me.button);
}

/**
 * Handles mouse drag events within a viewer.  Subclasses wanting
 * to handle this event should override {@link #handleDrag()} and/or
 * {@link #handleDragInProgress()}.
 */
public void mouseDrag(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);
	boolean wasDragging = movedPastThreshold();
	getCurrentInput().setInput(me);
	handleDrag();
	if (movedPastThreshold()){
		if (!wasDragging)
			handleDragStarted();
		handleDragInProgress();
	}
}

/**
 * Handles mouse hover event. within a viewer.  Subclasses wanting
 * to handle this event should override {@link #handleHover()}.
 */
public void mouseHover(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);
	getCurrentInput().setInput(me);
	debug("Mouse hover occured");//$NON-NLS-1$
	handleHover();
}

/**
 * Handles mouse moves (if the mouse button is up) within a viewer.
 * Subclasses wanting to handle this event should override 
 * {@link #handleMove()}.
 */
public void mouseMove(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);
	if (!isInputSynched(me)) {
		boolean b1 = getCurrentInput().isMouseButtonDown(1);
		boolean b2 = getCurrentInput().isMouseButtonDown(2);
		boolean b3 = getCurrentInput().isMouseButtonDown(3);
		getCurrentInput().verifyMouseButtons = true;
		getCurrentInput().setInput(me);
		if (b1) handleButtonUp(1);
		if (b2) handleButtonUp(2);
		if (b3) handleButtonUp(3);
	}
	else
		getCurrentInput().setInput(me);
	if (isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
		handleDragInProgress();
	else
		handleMove();
}

/**
 * Handles mouse up within a viewer.  Subclasses wanting to 
 * handle this event should override {@link #handleButtonUp(int)}.
 */
public void mouseUp(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);
	getCurrentInput().setInput(me);
	getCurrentInput().setMouseButton(me.button, false);
	debug("B" + me.button + " up on:\t");//$NON-NLS-2$//$NON-NLS-1$
	handleButtonUp(me.button);
}

void placeMouseInViewer(Point p){
	if (getCurrentViewer() == null)
		return;
	Control c = getCurrentViewer().getControl();
	org.eclipse.swt.graphics.Point swt = new org.eclipse.swt.graphics.Point(p.x, p.y);
	swt = c.toDisplay(swt);
	c.getDisplay().setCursorLocation(swt);
}

/**
 * {@link #deactivate() Deactivates} and then {@link #activate() activates}
 * this tool.
 */
protected void reactivate(){
	deactivate();
	activate();
}

/**
 * Selects the appropriate cursor.
 * 
 * @see #calculateCursor()
 */
protected void refreshCursor(){
	if (isActive())
		setCursor(calculateCursor());
}

/**
 * Events will also be sent to the {@link IFigure figures} after
 * this method is called.
 * 
 * @see #setToolCapture()
 */
protected void releaseToolCapture() {
	getCurrentViewer().setRouteEventsToEditor(false);
}

/**
 * Removes the given figure from the feedback layer.
 */
protected void removeFeedback(IFigure figure) {
	LayerManager lm = (LayerManager)getCurrentViewer().
		getEditPartRegistry().get(LayerManager.ID);
	if (lm == null)
		return;
	lm.getLayer(LayerConstants.FEEDBACK_LAYER).remove(figure);
}

/**
 * Resets the flags.
 */
protected void resetFlags(){
	setFlag(FLAG_PAST_THRESHOLD, false);
}

/**
 * Sets the current command.
 */
protected void setCurrentCommand(Command c){
	command = c;
	refreshCursor();
}

/**
 * Set the cursor on the argument to the not cursor Record the
 * fact we have altered the cursor of this viewer so that when
 * we are deactivated we can go back and set it to normal
 */
protected void setCursor(Cursor cursor){
	if (getCurrentViewer() != null)
		getCurrentViewer().setCursor(cursor);
}

/**
 * Sets the default cursor.
 */
public void setDefaultCursor(Cursor cursor){
	if (defaultCursor == cursor)
		return;
	defaultCursor = cursor;
	refreshCursor();
}

/**
 * Sets the disabled cursor.
 */
public void setDisabledCursor(Cursor cursor){
	if (disabledCursor == cursor)
		return;
	disabledCursor = cursor;
	refreshCursor();
}

/**
 * Sets the EditDomain.
 */
public void setEditDomain(EditDomain domain){
	this.domain = domain;
}

/**
 * Sets the hover flag to true.
 */
protected void setHoverActive(boolean value){
	setFlag(FLAG_HOVER, value);
}

void setMouseCapture(boolean value){
	if (getCurrentViewer() != null
		&& getCurrentViewer().getControl() != null
		&& !getCurrentViewer().getControl().isDisposed())
		getCurrentViewer().getControl().setCapture(value);
}

protected void setStartLocation(Point p){
	startX = p.x;
	startY = p.y;
}

/**
 * Sets the tool state.
 */
protected void setState(int state){
	this.state = state;
	if (GEF.DebugToolStates)
		GEF.debug("STATE CHANGE:\t" + getDebugName() + //$NON-NLS-1$
			":\t" + getDebugNameForState(this.state) + //$NON-NLS-1$
			" <" + state + '>'); //$NON-NLS-1$
}

/**
 * Captures all events and sends them to the tool.
 * Figures are not sent the event before the tool
 * when this capture model is active.
 * 
 * @see #releaseToolCapture()
 */
protected void setToolCapture() {
	getCurrentViewer().setRouteEventsToEditor(true);
}

/**
 * Setting this to <code>true</code> will cause the tool
 * to be unloaded after one operation has finished
 */
public void setUnloadWhenFinished(boolean value){
	setFlag(FLAG_UNLOAD, value);
}

/**
 * Sets the viewer to the given EditPartViewer.
 */
public void setViewer(EditPartViewer viewer){
	if (viewer == currentViewer)
		return;

	setCursor(null);
	currentViewer = viewer;
	if (currentViewer != null){
		org.eclipse.swt.graphics.Point p = currentViewer.getControl().toControl(
			Display.getCurrent().getCursorLocation());
		getCurrentInput().setMouseLocation(p.x, p.y);
	}
	refreshCursor();
}

/**
 * This method attempts to transistion the tool from state 
 * <code>start</code> to state <code>end</code> and returns 
 * <code>true</code> if successful.  If the tool is not in
 * state <code>start</code>, the tool remains in its current
 * state and <code>false</code> is returned to indicate
 * the failure.
 */
protected boolean stateTransition(int start, int end){
	if ((getState() & start) != 0){
		setState(end);
		return true;
	}
	else
		return false;
}

/**
 * Returns <code>true</code> if the tool is set to unload when 
 * its current operation is complete.
 */
final protected boolean unloadWhenFinished(){
	return getFlag(FLAG_UNLOAD);
}

/**
 * Handles the mouse entering a viewer.  Subclasses wanting to 
 * handle this event should override {@link #handleViewerEntered()}.
 */
public void viewerEntered(MouseEvent me, EditPartViewer viewer) {
	//setViewer(viewer);
	getCurrentInput().setInput(me);
	debug("Mouse entered viewer:\t" + viewer.toString());//$NON-NLS-1$
	handleViewerEntered();
}

/**
 * Handles the mouse exiting a viewer.  Subclasses wanting to 
 * handle this event should override {@link #handleViewerExited()}.
 */
public void viewerExited(MouseEvent me, EditPartViewer viewer) {
	//setViewer(viewer);
	getCurrentInput().setInput(me);
	debug("Mouse exited viewer:\t" + viewer.toString());//$NON-NLS-1$
	handleViewerExited();
}

}
