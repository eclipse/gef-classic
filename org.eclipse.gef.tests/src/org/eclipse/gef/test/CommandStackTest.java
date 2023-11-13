package org.eclipse.gef.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;

import org.junit.Assert;
import org.junit.Test;

public class CommandStackTest extends Assert {

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
		Assert.assertEquals(0, commandStackEvents.size());
		stack.execute(c);

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(c, commandStackEvents.get(0).getCommand());
		Assert.assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		Assert.assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(0).getSource());
		Assert.assertEquals(CommandStack.PRE_EXECUTE, commandStackEvents.get(0).getDetail());
		Assert.assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		Assert.assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		Assert.assertEquals(c, commandStackEvents.get(1).getCommand());
		Assert.assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		Assert.assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(1).getSource());
		Assert.assertEquals(CommandStack.POST_EXECUTE, commandStackEvents.get(1).getDetail());
		Assert.assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		Assert.assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// // test undo pre and post events
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.undo();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(c, commandStackEvents.get(0).getCommand());
		Assert.assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		Assert.assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(0).getSource());
		Assert.assertEquals(CommandStack.PRE_UNDO, commandStackEvents.get(0).getDetail());
		Assert.assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		Assert.assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		Assert.assertEquals(c, commandStackEvents.get(1).getCommand());
		Assert.assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		Assert.assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(1).getSource());
		Assert.assertEquals(CommandStack.POST_UNDO, commandStackEvents.get(1).getDetail());
		Assert.assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		Assert.assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// // test redo pre and post events
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.redo();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(c, commandStackEvents.get(0).getCommand());
		Assert.assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		Assert.assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(0).getSource());
		Assert.assertEquals(CommandStack.PRE_REDO, commandStackEvents.get(0).getDetail());
		Assert.assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		Assert.assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		Assert.assertEquals(c, commandStackEvents.get(1).getCommand());
		Assert.assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		Assert.assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(1).getSource());
		Assert.assertEquals(CommandStack.POST_REDO, commandStackEvents.get(1).getDetail());
		Assert.assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		Assert.assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// test flush event
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.flush();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(null, commandStackEvents.get(0).getCommand());
		Assert.assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		Assert.assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(0).getSource());
		Assert.assertEquals(CommandStack.PRE_FLUSH, commandStackEvents.get(0).getDetail());
		Assert.assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		Assert.assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		Assert.assertEquals(null, commandStackEvents.get(1).getCommand());
		Assert.assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		Assert.assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(1).getSource());
		Assert.assertEquals(CommandStack.POST_FLUSH, commandStackEvents.get(1).getDetail());
		Assert.assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		Assert.assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

		// test mark save location event
		commandStackEvents.clear();
		Assert.assertEquals(0, commandStackEvents.size());
		stack.redo();
		stack.markSaveLocation();

		Assert.assertEquals(2, commandStackEvents.size());
		Assert.assertEquals(null, commandStackEvents.get(0).getCommand());
		Assert.assertTrue(commandStackEvents.get(0).isPreChangeEvent());
		Assert.assertFalse(commandStackEvents.get(0).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(0).getSource());
		Assert.assertEquals(CommandStack.PRE_MARK_SAVE, commandStackEvents.get(0).getDetail());
		Assert.assertNotEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.PRE_MASK);
		Assert.assertEquals(0, commandStackEvents.get(0).getDetail() & CommandStack.POST_MASK);

		Assert.assertEquals(null, commandStackEvents.get(1).getCommand());
		Assert.assertFalse(commandStackEvents.get(1).isPreChangeEvent());
		Assert.assertTrue(commandStackEvents.get(1).isPostChangeEvent());
		Assert.assertEquals(stack, commandStackEvents.get(1).getSource());
		Assert.assertEquals(CommandStack.POST_MARK_SAVE, commandStackEvents.get(1).getDetail());
		Assert.assertEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.PRE_MASK);
		Assert.assertNotEquals(0, commandStackEvents.get(1).getDetail() & CommandStack.POST_MASK);

	}
}
