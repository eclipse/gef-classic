/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.tools;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.LayerManager;

/**
 * The base implementation for {@link Tool}s.  The base implementation provides a
 * framework for a <EM>state machine</EM> which processes mouse and keyboard input.  The
 * state machine consists of a series of states identified by <code>int</code>s. Each
 * mouse or keyboard event results in a transition, sometimes to the same state in which
 * the input was received.  The interesting transitions have corresponding actions
 * assigned to them, such as {@link #handleDragStarted()}.
 * <P>
 * The base implementation performs <EM>no</em> state transitions by default, but does
 * route events to different method handlers based on state.  It is up to subclasses to
 * set the appropriate states.
 * <P>
 * There are two broad "categories" of methods on AbstractTool.  There are the methods
 * defined on the {@link Tool} interface which handle the job of receiving raw user input.
 *  For example, {@link #mouseDrag(MouseEvent, EditPartViewer)}.  Then, there are the
 * methods which correspond to higher-level interpretation of these events, such as {@link
 * #handleDragInProgress()}, which is called from <code>mouseMove(...)</code>, but
 * <em>only</em> when the drag threshold has been passed. These methods are generally
 * more subclass-friendly. Subclasses should <em>not</em> override the methods which
 * receive raw input.
 */
public abstract class AbstractTool
	extends org.eclipse.gef.util.FlagSupport
	implements Tool, RequestConstants
{

/**
 * The final state for a tool to be in.  Once a tool reaches this state, it will not
 * change states until it is activated() again.
 */
protected static final int STATE_TERMINAL = 1 << 30;

/**
 * The first state that a tool is in.  The tool will generally be in this state
 * immediately following {@link #activate()}.
 */
protected static final int STATE_INITIAL  = 1;

/**
 * The state indicating that one or more buttons is pressed, but the user has not moved
 * past the drag threshold.  Many tools will do nothing during this state but wait
 * until {@link #STATE_DRAG_IN_PROGRESS} is entered.
 */
protected static final int STATE_DRAG = 2;

/**
 * The state indicating that the drag detection theshold has been passed, and a drag is in
 * progress.
 */
protected static final int STATE_DRAG_IN_PROGRESS = 4;

/**
 * The state indicating that an input event has invalidated the interaction.  For example,
 * during a mouse drag, pressing additional mouse button might invalidate the drag.
 */
protected static final int STATE_INVALID = 8;

/**
 * The state indicating that the keyboard is being used to perform a drag that is normally
 * done using the mouse.
 */
protected static final int STATE_ACCESSIBLE_DRAG = 16;

/**
 * The state indicating that a keyboard drag is in progress.  The threshold for keyboard
 * drags is non-existent, so this state would be entered very quickly.
 */
protected static final int STATE_ACCESSIBLE_DRAG_IN_PROGRESS = 32;

/**
 * The maximum state flag defined by this class
 */
protected static final int MAX_STATE = 32;

/**
 * constant used for mouse button 1.
 */
protected static final int MOUSE_BUTTON1 = SWT.BUTTON1;
/**
 * constant used for mouse button 2.
 */
protected static final int MOUSE_BUTTON2 = SWT.BUTTON2;
/**
 * constant used for mouse button 3.
 */
protected static final int MOUSE_BUTTON3 = SWT.BUTTON3;

/**
 * constant used to indicate any of the 3 mouse buttons.
 */
protected static final int MOUSE_BUTTON_ANY =
	MOUSE_BUTTON1 | MOUSE_BUTTON2 | MOUSE_BUTTON3;

private static final int DRAG_THRESHOLD = 5;
private static final int FLAG_PAST_THRESHOLD = 1;
private static final int FLAG_HOVER = 2;
private static final int FLAG_UNLOAD = 4;
{
	setFlag(FLAG_UNLOAD, true);
}
private static final int FLAG_ACTIVE = 8;

/**
 * The highest-bit flag being used.
 */
protected static final int MAX_FLAG = 8;

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
	extends org.eclipse.gef.util.FlagSupport
{
	int modifiers;
	boolean verifyMouseButtons;
	Point mouse = new Point();

	/**
	 * Returns the event modifiers. Modifiers are defined in {@link MouseEvent#stateMask},
	 * and include things like the mouse buttons and keyboard modifier keys.
	 * @return the event modifiers
	 */
	protected int getModifiers() {
		return modifiers;
	}
	
	/**
	 * Returns the current location of the mouse.
	 * @return the mouse location
	 */
	public Point getMouseLocation() {
		return mouse;
	}

	/**
	 * Returns <code>true</code> if the ALT key is pressed.
	 * @return <code>true</code> if the ALT key is pressed
	 */
	public boolean isAltKeyDown() {
		return (modifiers & SWT.ALT) > 0;
	}
	
	/**
	 * Returns <code>true</code> if any of the 3 mouse buttons are pressed.
	 * @return <code>true</code> if any of the 3 mouse buttons are pressed
	 */
	public boolean isAnyButtonDown() {
		return getFlag(2 | 4 | 8);
	}
	
	/**
	 * Returns <code>true</code> if the CTRL key is pressed.
	 * @return <code>true</code> of CTRL pressed
	 */
	public boolean isControlKeyDown() {
		return (modifiers & SWT.CONTROL) > 0;
	}
	
	/**
	 * Returns <code>true</code> if the specified button is down.
	 * @param which which button
	 * @return <code>true</code> if the button is down
	 */
	public boolean isMouseButtonDown(int which) {
		return getFlag(1 << which);
	}
	
	/**
	 * Returns <code>true</code> if the SHIFT key is pressed.
	 * @return <code>true</code> if SHIFT pressed
	 */
	public boolean isShiftKeyDown() {
		return (modifiers & SWT.SHIFT) > 0;
	}
	
	/**
	 * Sets mouse button # <code>which</code> to be pressed if <code>state</code> is true.
	 * @param which which button
	 * @param state <code>true</code> if button down
	 */
	public void setMouseButton(int which, boolean state) {
		setFlag(1 << which, state);
	}
	
	void setMouseLocation(int x, int y) {
		mouse.setLocation(x, y);
	}
	
	/**
	 * Sets the keyboard input based on the KeyEvent.
	 * @param ke the key event providing the input
	 */
	public void setInput(KeyEvent ke) {
		modifiers = ke.stateMask;
	}

	/**
	 * Sets the mouse and keyboard input based on the MouseEvent.
	 * @param me the mouse event providing the input
	 */
	public void setInput(MouseEvent me) {
		setMouseLocation(me.x, me.y);
		modifiers = me.stateMask;
		if (verifyMouseButtons) {
			setMouseButton(1, (modifiers & MOUSE_BUTTON1) != 0);
			setMouseButton(2, (modifiers & MOUSE_BUTTON2) != 0);
			setMouseButton(3, (modifiers & MOUSE_BUTTON3) != 0);
			verifyMouseButtons = false;
		}
	}
}

boolean acceptAbort(KeyEvent e) {
	return e.character == SWT.ESC;
}

boolean acceptArrowKey(KeyEvent e) {
	int key = e.keyCode;
	if (!(isInState(STATE_INITIAL
	  | STATE_ACCESSIBLE_DRAG
	  | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)))
		return false;
	return (key == SWT.ARROW_UP)
		|| (key == SWT.ARROW_RIGHT)
		|| (key == SWT.ARROW_DOWN)
		|| (key == SWT.ARROW_LEFT);
}

boolean acceptDragCommit(KeyEvent e) {
	return isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS)
		&& e.character == 13;
}

int accGetStep() {
	return accessibleStep;
}

void accStepIncrement() {
	if (accessibleBegin == -1) {
		accessibleBegin = new Date().getTime();
		accessibleStep = 1;
	} else {
		accessibleStep = 4;
		long elapsed = new Date().getTime() - accessibleBegin;
		if (elapsed > 1000)
			accessibleStep = Math.min(16, (int) (elapsed / 150));
	}
}

void accStepReset() {
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
 * Convenience method to add the given figure to the feedback layer.
 * @param figure the feedback being added
 */
protected void addFeedback(IFigure figure) {
	LayerManager lm = (LayerManager)getCurrentViewer().
		getEditPartRegistry().get(LayerManager.ID);
	if (lm == null)
		return;
	lm.getLayer(LayerConstants.FEEDBACK_LAYER).add(figure);
}

/**
 * Returns the appropriate cursor for the tools current state.  If the tool is in its
 * terminal state, <code>null</code> is returned.  Otherwise, either the default or
 * disabled cursor is returned, based on the existence of a current command, and whether
 * that current command is executable.
 * <P>
 * Subclasses may override or extend this method to calculate the appropriate cursor based
 * on other conditions.
 * @see #getDefaultCursor()
 * @see #getDisabledCursor()
 * @see #getCurrentCommand()
 * @return <code>null</code> or a cursor to be displayed.
 */
protected Cursor calculateCursor() {
	if (isInState(STATE_TERMINAL))
		return null;
	Command command = getCurrentCommand();
	if (command == null || !command.canExecute())
		return getDisabledCursor();
	return getDefaultCursor();
}

/**
 * Added for compatibility.  {@link DragTracker#commitDrag()} was added for accessibility
 * reasons.  Since all tool implementations must inherit from this base class, then
 * implementing this method here avoids breaking subclasses that implemented the {@link
 * DragTracker} interface.
 */
public void commitDrag() { }

/**
 * Returns a new List of editparts that this tool is operating on.  This method is called
 * once during {@link #getOperationSet()}, and its result is cached.
 * <P>
 * By default, the operations set is the current viewer's entire selection.  Subclasses
 * may override this method to filter or alter the operation set as necessary.
 * @return a list of editparts being operated on
 */
protected List createOperationSet() {
	return new ArrayList(getCurrentViewer().getSelectedEditParts());
}

/**
 * Deactivates the tool. This method is called whenever the user switches to another tool.
 * Use this method to do some clean-up when the tool is switched. The abstract tool allows
 * cursors for  viewers to be changed. When the tool is deactivated it must  revert to
 * normal the cursor of the last tool it changed.
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
 * Prints a string in the GEF Debug console if the Tools debug option is selected.
 * @param message a message for the debug trace tool
 */
protected void debug(String message) {
	if (GEF.DebugTools) {
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
 * Returns a new, updated command based on the tool's current properties.  The default
 * implementation returns an unexecutable command.  Some tools do not work commands and
 * the model, but simply change the viewer's state in some way.
 * @return a newly obtained command
 */
protected Command getCommand() {
	return org.eclipse.gef.commands.UnexecutableCommand.INSTANCE;
}

/**
 * Returns the identifier of the command that is being sought.  This name is also the 
 * named that will be logged in the debug view.
 * @return the identifier for the command
 */
protected abstract String getCommandName();

/**
 * Returns the currently cached command.
 * @return the current command
 * @see #setCurrentCommand(Command)
 */
protected Command getCurrentCommand() {
	return command;
}

/**
 * Returns the input object encapsulating the current mouse and keyboard state.
 * @return the current input
 */
protected Input getCurrentInput() {
	if (current == null)
		current = new Input();
	return current;
}

/**
 * Return the viewer that the tool is currently receiving input from, or
 * <code>null</code>.  The last viewer to dispatch an event is defined as the current
 * viewer.  Current viewer is automatically updated as events are received, and is set to
 * <code>null</code> on <code>deactivate()</code>.
 * @return the current viewer
 */
protected EditPartViewer getCurrentViewer() {
	return currentViewer;
}

/**
 * Returns the debug name for this tool.
 * @return the debug name
 */
protected abstract String getDebugName();

/**
 * Returns a String representation of the given state for debug purposes.
 * @param state the state
 * @return the string for the given state
 */
protected String getDebugNameForState(int state) {
	switch (state) {
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
 * Returns the cursor used under normal conditions.
 * @see #setDefaultCursor(Cursor)
 * @return the default cursor
 */
protected Cursor getDefaultCursor() {
	return defaultCursor;
}

/**
 * Returns the cursor used under abnormal conditions.
 * @see #calculateCursor()
 * @see #setDisabledCursor(Cursor)
 * @return the disabled cursor
 */
protected Cursor getDisabledCursor() {
	if (disabledCursor != null)
		return disabledCursor;
	return getDefaultCursor();
}

/**
 * Returns the EditDomain.  A tool is told its EditDomain when it becomes active.  A tool
 * may need to know its edit domain prior to receiving any events from any of that
 * domain's viewers.
 * @return the editdomain
 */
protected EditDomain getDomain() {
	return domain;
}

/**
 * Return the number of pixels that the mouse has been moved since that drag was started.
 * The drag start is determined by where the mouse button was first pressed.
 * @see #getStartLocation()
 * @return the drag delta
 */
protected Dimension getDragMoveDelta() {
	return getLocation().getDifference(getStartLocation());
}

/**
 * Returns the current x, y position of the mouse cursor.
 * @return the mouse location
 */
protected Point getLocation() {
	return new Point(getCurrentInput().getMouseLocation());
}

/**
 * Lazily creates and returns the list of editparts on which the tool operates.  The list
 * is initially <code>null</code>, in which case {@link #createOperationSet()} is called,
 * and its results cached until the tool is deactivated.
 * @return the operation set.
 */
protected List getOperationSet() {
	if (operationSet == null)
		operationSet = createOperationSet();
	return operationSet;
}

/**
 * Returns the starting mouse location for the current tool operation.  This is typically
 * the mouse location where the user first pressed a mouse button.  This is important for
 * tools that interpret mouse drags.
 * @return the start location
 */
protected Point getStartLocation() {
	return new Point(startX, startY);
}

/**
 * Returns the tool's current state.
 * @return the current state
 */
protected int getState() {
	return state;
}

/**
 * Called when the mouse button has been pressed. By default, nothing happens
 * and <code>false</code> is returned. Subclasses may override this method to interpret
 * the meaning of a mouse down. Returning <code>true</code> indicates that the button down
 * was handled in some way.
 * @param button which button went down
 * @return <code>true</code> if the buttonDown was handled
 */
protected boolean handleButtonDown(int button) {
	return false;
}

/**
 * Called when the mouse button has been released.  By default, nothing happens and
 * <code>false</code> is returned. Subclasses may override this method to interpret the
 * mouse up.  Returning <code>true</code> indicates that the mouse up was handled in some
 * way.
 * @see #mouseUp(MouseEvent, EditPartViewer)
 * @param button the button being released
 * @return <code>true</code> if the button up was handled
 */
protected boolean handleButtonUp(int button) {
	return false;
}

/**
 * Called when a mouse double-click occurs.  By default, nothing happens and
 * <code>false</code> is returned.  Subclasses may override this method to interpret
 * double-clicks.  Returning <code>true</code> indicates that the event was handled in
 * some way.
 * @param button which button was double-clicked
 * @return <code>true</code> if the event was handled
 * @see #mouseDoubleClick(MouseEvent, EditPartViewer)
 */
protected boolean handleDoubleClick(int button) {
	return false;
}

/**
 * Called whenever the mouse is being dragged.  This method continues to be called
 * even once {@link #handleDragInProgress()} starts getting called. By default, nothing
 * happens, and <code>false</code> is returned.  Subclasses may override this method to
 * interpret a drag.  Returning <code>true</code> indicates that the drag was handled in
 * some way.
 * @return <code>true</code> if the drag is handled 
 * @see #mouseDrag(MouseEvent, EditPartViewer)
 */
protected boolean handleDrag() {
	return false;
}

/**
 * Called whenever a mouse is being dragged and the drag threshold has been exceeded.
 * Prior to the drag threshold being exceeded, only {@link #handleDrag()} is called.  This
 * method gets called repeatedly for every mouse move during the drag. By default, nothing
 * happens and <code>false</code> is returned.  Subclasses may override this method to
 * interpret the drag.  Returning <code>true</code> indicates that the drag was handled.
 * @see #movedPastThreshold()
 * @see #mouseDrag(MouseEvent, EditPartViewer)
 * @return <code>true</code> if the drag was handled
 */
protected boolean handleDragInProgress() {
	return false;
}

/**
 * Called only one time during a drag when the drag threshold has been exceeded.  By
 * default, nothing happens and <code>false</code> is returned.  Subclasses may override
 * to interpret the drag starting.  Returning <code>true</code> indicates that the event
 * was handled.
 * @see #movedPastThreshold()
 * @see #mouseDrag(MouseEvent, EditPartViewer)
 * @return true if the drag starting was handled
 */
protected boolean handleDragStarted() {
	return false;
}

/**
 * Called when the current tool operation is to be completed.  In other words, the "state
 * machine" and has accepted the sequence of input (i.e. the mouse gesture).  By default,
 * the tool will either reactivate itself, or ask the edit domain to load the default
 * tool.
 * <P>
 * Subclasses should extend this method to first do whatever it is that the tool does, and
 * then call <code>super</code>.
 * @see #unloadWhenFinished()
 */
protected void handleFinished() {
	if (unloadWhenFinished())
		getDomain().loadDefaultTool();
	else
		reactivate();
}

/**
 * Handles high-level processing of a focus gained event.  By default, nothing happens and
 * <code>false</code> is returned.  Subclasses may override this method to interpret the
 * focus gained event.  Return <code>true</code> to indicate that the event was processed.
 * @see #focusGained(FocusEvent, EditPartViewer)
 * @return <code>true</code> if the event was handled
 */
protected boolean handleFocusGained() {
	return false;
}

/**
 * Handles high-level processing of a focus lost event.  By default, nothing happens and
 * <code>false</code> is returned.  Subclasses may override this method to interpret the
 * focus lost event.  Return <code>true</code> to indicate that the event was processed.
 * @see #focusLost(FocusEvent, EditPartViewer)
 * @return <code>true</code> if the event was handled
 */
protected boolean handleFocusLost() {
	return false;
}

/**
 * Handles high-level processing of a mouse hover event.  By default, nothing happens and
 * <code>false</code> is returned.  Subclasses may override this method to interpret the
 * hover.  Return <code>true</code> to indicate that the hover was handled.
 * @see #mouseHover(MouseEvent, EditPartViewer)
 * @return <code>true</code> if the hover was handled
 */
protected boolean handleHover() {
	return false;
}

/**
 * Handles high-level processing of a key down event.  By default, the KeyEvent is
 * checked to see if it is the ESCAPE key.  If so, the domain's default tool is reloaded,
 * and <code>true</code> is returned. Subclasses may extend this method to interpret
 * additional key down events. Returns <code>true</code> if the given key down was
 * handled.
 * @see #keyDown(KeyEvent, EditPartViewer)
 * @param e the key event
 * @return <code>true</code> if the key down was handled.
 */
protected boolean handleKeyDown(KeyEvent e) {
	if (acceptAbort(e)) {
		getDomain().loadDefaultTool();
		return true;
	}
	return false;
}

/**
 * Handles high-level processing of a key up event.  By default, does nothing and returns
 * <code>false</code>.  Subclasses may extend this method to process key up events. 
 * Returns <code>true</code> if the key up was processed in some way.
 * @see #keyUp(KeyEvent, EditPartViewer)
 * @param e the key event
 * @return <code>true</code> if the event was handled
 */
protected boolean handleKeyUp(KeyEvent e) {
	return false;
}

/**
 * Handles high-level processing of a mouse move.  By default, does nothing and returns
 * <code>false</code>.  Subclasses may extend this method to process mouse moves. 
 * Returns <code>true</code> if the mouse move was processed.
 * @see #mouseMove(MouseEvent, EditPartViewer)
 * @return <code>true</code> if the mouse move was handled
 */
protected boolean handleMove() {
	return false;
}

/**
 * Handles when a native drag has ended.  By default, does nothing and returns
 * <code>false</code>. Subclasses may extend this method to process native drags ending.
 * @param event the drag event
 * @return <code>true</code> if the native drag finished was handled
 */
protected boolean handleNativeDragFinished(DragSourceEvent event) {
	return false;
}

/**
 * Handles when a native drag has started.  By default, does nothing and returns
 * <code>false</code>. Subclasses may extend this method to process native drag starts.
 * <P>
 * When a native drag starts, all subsequent mouse events will not be received, including
 * the mouseUp event.  The only event that will be received is the drag finished event.
 * @param event the drag event
 * @return <code>true</code> if the native drag start was handled
 */
protected boolean handleNativeDragStarted(DragSourceEvent event) {
	return false;
}

/**
 * Called when the mouse enters an EditPartViewer. By default, does nothing and returns
 * <code>false</code>. Subclasses may extend this method to process the viewer enter.
 * Returns <code>true</code> to indicate if the viewer entered was process in some way.
 * @return <code>true</code> if the viewer entered was handled
 */
protected boolean handleViewerEntered() {
	return false;
}

/**
 * Called when the mouse exits an EditPartViewer. By default, does nothing and returns
 * <code>false</code>. Subclasses may extend this method to process viewer exits. Returns
 * <code>true</code> to indicate if the viewer exited was process in some way.
 * @return <code>true</code> if the viewer exited was handled
 */
protected boolean handleViewerExited() {
	return false;
}

/**
 * Returns <code>true</code> if the tool is active.
 * @return <code>true</code> if active
 */
protected boolean isActive() {
	return getFlag(FLAG_ACTIVE);
}

/**
 * Returns <code>true</code> if the tool is hovering.
 * @return <code>true</code> if hovering
 */
protected boolean isHoverActive() {
	return getFlag(FLAG_HOVER);
}

/*
 * Returns <code>true</code> if the current {@link Input} is
 * synchronized with the current MouseEvent.
 */
private boolean isInputSynched(MouseEvent event) {
	Input input = getCurrentInput();
	boolean button1ok =
		input.isMouseButtonDown(1) == ((event.stateMask & SWT.BUTTON1) != 0);
	boolean button2ok =
		input.isMouseButtonDown(2) == ((event.stateMask & SWT.BUTTON2) != 0);
	boolean button3ok =
		input.isMouseButtonDown(3) == ((event.stateMask & SWT.BUTTON3) != 0);
	return (button1ok && button2ok && button3ok);
}

boolean isInDragInProgress() {
	return isInState(STATE_DRAG_IN_PROGRESS | STATE_ACCESSIBLE_DRAG_IN_PROGRESS);
}

/**
 * Returns <code>true</code> if the tool is in the given state.
 * @param state the state being queried
 * @return <code>true</code> if the tool is in the given state
 */
protected boolean isInState(int state) {
	return ((getState() & state) != 0);
}

/**
 * Receives a KeyDown event for the given viewer.  Subclasses wanting to handle this
 * event should override {@link #handleKeyDown(KeyEvent)}.
 * @param evt the key event
 * @param viewer the origininating viewer
 */
public void keyDown(KeyEvent evt, EditPartViewer viewer) {
	setViewer(viewer);
	getCurrentInput().setInput(evt);
	debug("Key (" + evt.character + ','//$NON-NLS-1$
		+ evt.keyCode + ") down:\t"); //$NON-NLS-1$
	handleKeyDown(evt);
}

/**
 * Receives a KeyUp event for the given viewer.  Subclasses wanting to handle this event
 * should override {@link #handleKeyUp(KeyEvent)}.
 * @param evt the key event
 * @param viewer the origininating viewer
 */
public void keyUp(KeyEvent evt, EditPartViewer viewer) {
	setViewer(viewer);
	getCurrentInput().setInput(evt);
	debug("Key (" + evt.character + ','//$NON-NLS-1$
		+ evt.keyCode + ") up:\t");//$NON-NLS-1$
	handleKeyUp(evt);
}

/**
 * Returns <code>true</code> if the threshold has been exceeded during a mouse drag.
 * @return <code>true</code> if the threshold has been exceeded
 */
protected boolean movedPastThreshold() {
	if (getFlag(FLAG_PAST_THRESHOLD))
		return true;
	Point start = getStartLocation(),
		  end = getLocation();
	if (Math.abs(start.x - end.x) > DRAG_THRESHOLD
	  || Math.abs(start.y - end.y) > DRAG_THRESHOLD) {
		setFlag(FLAG_PAST_THRESHOLD, true);
		return true;
	}
	return false;
}

/**
 * @see org.eclipse.gef.Tool#nativeDragStarted(DragSourceEvent, EditPartViewer)
 */
public void nativeDragFinished(DragSourceEvent event, EditPartViewer viewer) {
	debug("Native drag finished on " + viewer);//$NON-NLS-1$
	setViewer(viewer);
	handleNativeDragFinished(event);
}

/**
 * @see org.eclipse.gef.Tool#nativeDragStarted(DragSourceEvent, EditPartViewer)
 */
public void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer) {
	debug("Native drag started on " + viewer);//$NON-NLS-1$
	setViewer(viewer);
	handleNativeDragStarted(event);
}

/**
 * Handles mouse double click events within a viewer.  Subclasses wanting to handle this
 * event should override {@link #handleDoubleClick(int)}.
 * @param me the mouse event
 * @param viewer the originating viewer
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
 * Handles mouse down events within a viewer.  Subclasses wanting to handle this event
 * should override {@link #handleButtonDown(int)}.
 * @param me the mouse event
 * @param viewer the originating viewer
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
 * Handles mouse drag events within a viewer.  Subclasses wanting to handle this event
 * should override {@link #handleDrag()} and/or {@link #handleDragInProgress()}.
 * @param me the mouse event
 * @param viewer the originating viewer
 */
public void mouseDrag(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);
	boolean wasDragging = movedPastThreshold();
	getCurrentInput().setInput(me);
	handleDrag();
	if (movedPastThreshold()) {
		if (!wasDragging)
			handleDragStarted();
		handleDragInProgress();
	}
}

/**
 * Handles mouse hover event. within a viewer.  Subclasses wanting to handle this event
 * should override {@link #handleHover()}.
 * @param me the mouse event
 * @param viewer the originating viewer
 * 
 */
public void mouseHover(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);
	getCurrentInput().setInput(me);
	debug("Mouse hover occured");//$NON-NLS-1$
	handleHover();
}

/**
 * Handles mouse moves (if the mouse button is up) within a viewer. Subclasses wanting to
 * handle this event should override  {@link #handleMove()}.
 * @param me the mouse event
 * @param viewer the originating viewer
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
		if (getDomain().getActiveTool() != this)
			return;
		/* 
		 * processing one of the buttonUps may have caused the tool to reactivate itself,
		 * which causes the viewer to get nulled-out.  If we are going to call another
		 * handleXxx method below, we must set the viewer again to be paranoid.
		 */
		setViewer(viewer);
	} else
		getCurrentInput().setInput(me);
	if (isInState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS))
		handleDragInProgress();
	else
		handleMove();
}

/**
 * Handles mouse up within a viewer.  Subclasses wanting to handle this event should
 * override {@link #handleButtonUp(int)}.
 * @param me the mouse event
 * @param viewer the originating viewer
 */
public void mouseUp(MouseEvent me, EditPartViewer viewer) {
	setViewer(viewer);
	getCurrentInput().setInput(me);
	getCurrentInput().setMouseButton(me.button, false);
	debug("B" + me.button + " up on:\t");//$NON-NLS-2$//$NON-NLS-1$
	handleButtonUp(me.button);
}

void placeMouseInViewer(Point p) {
	if (getCurrentViewer() == null)
		return;
	Control c = getCurrentViewer().getControl();
	Rectangle rect;
	if (c instanceof Scrollable)
		rect = ((Scrollable)c).getClientArea();
	else
		rect = c.getBounds();
	if (p.x > rect.x + rect.width - 1)
		p.x = rect.x + rect.width - 1;
	else if (p.x < rect.x)
		p.x = rect.x;
	if (p.y > rect.y + rect.height - 1)
		p.y = rect.y + rect.height - 1;
	else if (p.y < rect.y)
		p.y = rect.y;
	org.eclipse.swt.graphics.Point swt = new org.eclipse.swt.graphics.Point(p.x, p.y);
	swt = c.toDisplay(swt);
	c.getDisplay().setCursorLocation(swt);
}

/**
 * Calls <code>deactivate()</code> and then <code>activate()</code>.
 */
protected void reactivate() {
	deactivate();
	activate();
}

/**
 * Sets the cursor being displayed to the appropriate cursor.  If the tool is active, the
 * current cursor being displayed is updated by calling {@link #calculateCursor()}.
 */
protected void refreshCursor() {
	if (isActive())
		setCursor(calculateCursor());
}

/**
 * Releases tool capture.
 * @see #setToolCapture()
 */
protected void releaseToolCapture() {
	getCurrentViewer().setRouteEventsToEditDomain(false);
}

/**
 * Convenience method to removes a figure from the feedback layer.
 * @param figure the figure being removed
 */
protected void removeFeedback(IFigure figure) {
	LayerManager lm = (LayerManager)getCurrentViewer().
		getEditPartRegistry().get(LayerManager.ID);
	if (lm == null)
		return;
	lm.getLayer(LayerConstants.FEEDBACK_LAYER).remove(figure);
}

/**
 * Resets all flags to their initial values.  Subclasses should extend this method to
 * reset their own custom flags.
 */
protected void resetFlags() {
	setFlag(FLAG_PAST_THRESHOLD, false);
}

/**
 * Used to cache a command obtained from {@link #getCommand()}.
 * @param c the command
 * @see #getCurrentCommand()
 */
protected void setCurrentCommand(Command c) {
	command = c;
	refreshCursor();
}

/**
 * Shows the given cursor on the current viewer.
 * @param cursor the cursor to display
 */
protected void setCursor(Cursor cursor) {
	if (getCurrentViewer() != null)
		getCurrentViewer().setCursor(cursor);
}

/**
 * Sets the default cursor.
 * @param cursor the cursor
 * @see #getDefaultCursor()
 */
public void setDefaultCursor(Cursor cursor) {
	if (defaultCursor == cursor)
		return;
	defaultCursor = cursor;
	refreshCursor();
}

/**
 * Sets the disabled cursor.
 * @param cursor the cursor
 * @see #getDisabledCursor()
 */
public void setDisabledCursor(Cursor cursor) {
	if (disabledCursor == cursor)
		return;
	disabledCursor = cursor;
	refreshCursor();
}

/**
 * Sets the EditDomain.
 * @param domain the edit domain
 * @see #getDomain()
 */
public void setEditDomain(EditDomain domain) {
	this.domain = domain;
}

/**
 * Sets whether the hover flag is true or false.  Subclasses which do something on hover
 * can use this flag to track whether they have received a hover or not.
 * @param value whether hover is active
 */
protected void setHoverActive(boolean value) {
	setFlag(FLAG_HOVER, value);
}

void setMouseCapture(boolean value) {
	if (getCurrentViewer() != null
		&& getCurrentViewer().getControl() != null
		&& !getCurrentViewer().getControl().isDisposed())
		getCurrentViewer().getControl().setCapture(value);
}

/**
 * Sets the start mouse location, typically for a drag operation.
 * @param p the start location
 */
protected void setStartLocation(Point p) {
	startX = p.x;
	startY = p.y;
}

/**
 * Sets the tools state.
 * @param state the new state
 */
protected void setState(int state) {
	this.state = state;
	if (GEF.DebugToolStates)
		GEF.debug("STATE CHANGE:\t" + getDebugName() + //$NON-NLS-1$
			":\t" + getDebugNameForState(this.state) + //$NON-NLS-1$
			" <" + state + '>'); //$NON-NLS-1$
}

/**
 * Sets tool capture.  When a tool has capture, viewers will make every effort to send
 * events through the editdomain to the tool.  Therefore, the default handling of some
 * events is bypassed.
 */
protected void setToolCapture() {
	getCurrentViewer().setRouteEventsToEditDomain(true);
}

/**
 * Setting this to <code>true</code> will cause the tool to be unloaded after one
 * operation has completed.  The default value is <code>true</code>.  The tool is
 * unloaded, and the edit domains default tool will be activated.
 * @param value whether the tool should be unloaded on completion
 */
public void setUnloadWhenFinished(boolean value) {
	setFlag(FLAG_UNLOAD, value);
}

/**
 * Sets the active EditPartViewer.  The active viewer is the viewer from which the last
 * event was received.
 * @param viewer the viewer
 */
public void setViewer(EditPartViewer viewer) {
	if (viewer == currentViewer)
		return;

	setCursor(null);
	currentViewer = viewer;
	if (currentViewer != null) {
		org.eclipse.swt.graphics.Point p = currentViewer.getControl().toControl(
			Display.getCurrent().getCursorLocation());
		getCurrentInput().setMouseLocation(p.x, p.y);
	}
	refreshCursor();
}

/**
 * Returns <code>true</code> if the give state transition succeeds. This is a "test and
 * set" operation, where the tool is tested to be in the specified start state, and if so,
 * is set to the given end state.  The method returns the result of the first test.
 * @param start the start state being tested
 * @param end the end state
 * @return <code>true</code> if the state transition is successful
 */
protected boolean stateTransition(int start, int end) {
	if ((getState() & start) != 0) {
		setState(end);
		return true;
	} else
		return false;
}

/**
 * Returns <code>true</code> if the tool is set to unload when its current operation is
 * complete.
 * @return <code>true</code> if the tool should be unloaded when finished
 */
protected final boolean unloadWhenFinished() {
	return getFlag(FLAG_UNLOAD);
}

/**
 * Receives the mouse entered event.  Subclasses wanting to handle this event should 
 * override {@link #handleViewerEntered()}.
 * <p>
 * FEATURE in SWT: mouseExit comes after mouseEntered on the new . Therefore, if the
 * current viewer is not <code>null</code>, it means the exit has not been sent yet by
 * SWT. To maintain proper ordering, GEF fakes the exit and calls {@link
 * #handleViewerExited()}. The real exit will then be ignored.
 * @param me the mouse event
 * @param viewer the originating viewer
 */
public void viewerEntered(MouseEvent me, EditPartViewer viewer) {
	getCurrentInput().setInput(me);
	if (getCurrentViewer() != null && getCurrentViewer() != viewer) {
		debug("Mouse exited viewer:\t" + getCurrentViewer().toString()); //$NON-NLS-1$
		handleViewerExited();
	}
	setViewer(viewer);
	debug("Mouse entered viewer:\t" + getCurrentViewer().toString()); //$NON-NLS-1$
	handleViewerEntered();
}

/**
 * Handles the mouse exited event.  Subclasses wanting to handle this event should
 * override {@link #handleViewerExited()}.
 * @param me the mouse event
 * @param viewer the originating viewer
 */
public void viewerExited(MouseEvent me, EditPartViewer viewer) {
	/*
	 * FEATURE in SWT. mouseExited comes after mouseEntered. So only call handle exit if
	 * we didn't previously fake it on viewer entered.
	 */
	if (viewer == getCurrentViewer()) {
		getCurrentInput().setInput(me);
		debug("Mouse exited viewer:\t" + viewer.toString());//$NON-NLS-1$
		handleViewerExited();
		setViewer(null);
	}
}

}
