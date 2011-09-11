package com.dylanh.itc.editor;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IStatus;

import com.dylanh.itc.data.ColorMapping;
import com.dylanh.itc.util.RGBA;

public class ColorMappingWithOperations extends ColorMapping
{

	private final ColorMapping mapping;
	private final IUndoContext undoContext;
	private final IOperationHistory operationHistory;

	public ColorMappingWithOperations(ColorMapping colorMapping, IUndoContext undoContext, IOperationHistory operationHistory)
	{
		this.mapping = colorMapping;
		this.undoContext = undoContext;
		this.operationHistory = operationHistory;
	}

	@Override
	public RGBA put(RGBA key, RGBA newValue)
	{
		ColorMappingOperation op = new ColorMappingOperation(mapping, key, newValue);
		op.addContext(undoContext);
		RGBA oldValue = mapping.get(key);
		try
		{
			IStatus status = operationHistory.execute(op, null, null);
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		return oldValue;
	}

	@Override
	public Set<Entry<RGBA, RGBA>> entrySet()
	{
		return mapping.entrySet();
	}

	@Override
	public RGBA get(RGBA key)
	{
		return mapping.get(key);
	}

	@Override
	public void addListener(Listener listener)
	{
		mapping.addListener(listener);
	}

	@Override
	public void removeListener(Listener listener)
	{
		mapping.removeListener(listener);
	}

	@Override
	public void fireMappingChanged(RGBA key, RGBA oldVal, RGBA newVal)
	{
		mapping.fireMappingChanged(key, oldVal, newVal);
	}
}
