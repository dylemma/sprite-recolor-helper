package com.dylanh.itc.rcp;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.Assert;

public class UndoRedoManager
{
	private IUndoContext undoContext;
	private final IOperationHistory opHistory;
	public final UndoHandler UNDO = new UndoHandler();
	public final RedoHandler REDO = new RedoHandler();

	public UndoRedoManager()
	{
		opHistory = OperationHistoryFactory.getOperationHistory();
		Assert.isNotNull(opHistory);

		opHistory.addOperationHistoryListener(new HistoryListener());
		updateHandlerStatus();
	}

	public IUndoContext getUndoContext()
	{
		return undoContext;
	}

	public void setUndoContext(IUndoContext undoContext)
	{
		this.undoContext = undoContext;
		updateHandlerStatus();
	}

	private class HistoryListener implements IOperationHistoryListener
	{
		@Override
		public void historyNotification(OperationHistoryEvent event)
		{
			updateHandlerStatus();
		}
	}

	private void updateHandlerStatus()
	{
		if (undoContext != null)
		{
			UNDO.setBaseEnabled(opHistory.canUndo(undoContext));
			REDO.setBaseEnabled(opHistory.canRedo(undoContext));
		}
		else
		{
			UNDO.setBaseEnabled(false);
			REDO.setBaseEnabled(false);
		}
	}

	private class UndoHandler extends AbstractHandler
	{
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException
		{
			if (undoContext != null)
			{
				System.out.println("Executing UNDO");
				opHistory.undo(undoContext, null, null);
			}
			else
			{
				System.out.println("(undo context was null. not undoing anything)");
			}
			return null;
		}

		@Override
		public void setBaseEnabled(boolean state)
		{// overrode protected to make it public here
			super.setBaseEnabled(state);
		}
	}

	private class RedoHandler extends AbstractHandler
	{

		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException
		{
			if (undoContext != null)
			{
				System.out.println("Executing REDO");
				opHistory.redo(undoContext, null, null);
			}
			else
			{
				System.out.println("(undo context was null. not redoing anything)");
			}
			return null;
		}

		@Override
		public void setBaseEnabled(boolean state)
		{// overrode protected to make it public here
			super.setBaseEnabled(state);
		}
	}

}
