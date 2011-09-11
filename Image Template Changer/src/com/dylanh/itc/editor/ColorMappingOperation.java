package com.dylanh.itc.editor;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.dylanh.itc.data.ColorMapping;
import com.dylanh.itc.util.RGBA;

public class ColorMappingOperation extends AbstractOperation
{

	private final ColorMapping mapping;
	private final RGBA key;
	private final RGBA newValue;
	private final RGBA oldValue;
	private final IStatus OK = Status.OK_STATUS;

	public ColorMappingOperation(ColorMapping mapping, RGBA key, RGBA newValue)
	{
		super("Color mapping operation");
		this.mapping = mapping;
		this.key = key;
		this.newValue = newValue;
		this.oldValue = mapping.get(key);
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
	{
		System.out.println("execute " + this);
		mapping.put(key, newValue);
		return OK;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
	{
		System.out.println("redo " + this);
		mapping.put(key, newValue);
		return OK;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException
	{
		System.out.println("undo " + this);
		mapping.put(key, oldValue);
		return OK;
	}

	@Override
	public String toString()
	{
		return "ColorMappingOperation: " + key + " to " + newValue;
	}
}
