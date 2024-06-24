package org.eclipse.gef.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;

import org.junit.Test;

public class CommandStackTest {

	@SuppressWarnings("static-method")
	@Test
	public void testCommandStackEventListenerNotifications() {
		final List<CommandStackEvent> commandStackEvents = new ArrayList<>();

		// capture all notifications in an event list
		CommandStack stack = new CommandStack();
		stack.addCommandStackEventListener(event -> commandStackEvents.add(event));

		Command c = new Command() {
		};

		// test execution pre and post events
		assertEquals(0, commandStackEvents.size());
		stack.execute(c);

		assertEquals(2, commandStackEvents.size());
		assertEquals(c, commandStackEvents.get(0).getCommand());
		assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(0).getSource());
		assertEquals(CommandStack.PRE_EXECUTE, commandStackEvents.get(0).getDetail());
		assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		assertEquals(c, commandStackEvents.get(1).getCommand());
		assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(1).getSource());
		assertEquals(CommandStack.POST_EXECUTE, commandStackEvents.get(1).getDetail());
		assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// // test undo pre and post events
		commandStackEvents.clear();
		assertEquals(0, commandStackEvents.size());
		stack.undo();

		assertEquals(2, commandStackEvents.size());
		assertEquals(c, commandStackEvents.get(0).getCommand());
		assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(0).getSource());
		assertEquals(CommandStack.PRE_UNDO, commandStackEvents.get(0).getDetail());
		assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		assertEquals(c, commandStackEvents.get(1).getCommand());
		assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(1).getSource());
		assertEquals(CommandStack.POST_UNDO, commandStackEvents.get(1).getDetail());
		assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// // test redo pre and post events
		commandStackEvents.clear();
		assertEquals(0, commandStackEvents.size());
		stack.redo();

		assertEquals(2, commandStackEvents.size());
		assertEquals(c, commandStackEvents.get(0).getCommand());
		assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(0).getSource());
		assertEquals(CommandStack.PRE_REDO, commandStackEvents.get(0).getDetail());
		assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		assertEquals(c, commandStackEvents.get(1).getCommand());
		assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(1).getSource());
		assertEquals(CommandStack.POST_REDO, commandStackEvents.get(1).getDetail());
		assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// test flush event
		commandStackEvents.clear();
		assertEquals(0, commandStackEvents.size());
		stack.flush();

		assertEquals(2, commandStackEvents.size());
		assertEquals(null, commandStackEvents.get(0).getCommand());
		assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(0).getSource());
		assertEquals(CommandStack.PRE_FLUSH, commandStackEvents.get(0).getDetail());
		assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		assertEquals(null, commandStackEvents.get(1).getCommand());
		assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(1).getSource());
		assertEquals(CommandStack.POST_FLUSH, commandStackEvents.get(1).getDetail());
		assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// test mark save location event
		commandStackEvents.clear();
		assertEquals(0, commandStackEvents.size());
		stack.redo();
		stack.markSaveLocation();

		assertEquals(2, commandStackEvents.size());
		assertEquals(null, commandStackEvents.get(0).getCommand());
		assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(0).getSource());
		assertEquals(CommandStack.PRE_MARK_SAVE, commandStackEvents.get(0).getDetail());
		assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		assertEquals(null, commandStackEvents.get(1).getCommand());
		assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		assertEquals(stack, commandStackEvents.get(1).getSource());
		assertEquals(CommandStack.POST_MARK_SAVE, commandStackEvents.get(1).getDetail());
		assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

	}
}
