package com.dylanh.itc.rcp;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

public class UndoRedoHandlerFactory implements IExecutableExtension, IExecutableExtensionFactory
{
	public final static String UNDO = "undo";
	public final static String REDO = "redo";

	private static UndoRedoManager undoRedoManager = null;

	public static UndoRedoManager getUndoRedoManager()
	{
		if (undoRedoManager == null)
		{
			undoRedoManager = new UndoRedoManager();
		}
		return undoRedoManager;
	}

	private String id = "";

	@Override
	public Object create() throws CoreException
	{
		if (UNDO.equals(id))
		{
			return getUndoRedoManager().UNDO;
		}
		if (REDO.equals(id))
		{
			return getUndoRedoManager().REDO;
		}
		return null;
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException
	{
		if (data instanceof String)
		{
			id = (String) data;
		}
	}

}
