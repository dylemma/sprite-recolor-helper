package com.dylanh.itc.editor;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.dylanh.itc.alg.ColorMappingStrategy;
import com.dylanh.itc.data.ColorMapping;
import com.dylanh.itc.data.ColorTemplate;

public class TemplateEditorInput implements IEditorInput
{

	public final File imageFile1;
	public final File imageFile2;
	private final ColorMappingStrategy mappingStrategy;
	private ColorMapping mapping;

	private Image image1;
	private Image image2;
	private ColorTemplate template1;
	private ColorTemplate template2;

	public TemplateEditorInput(File imageFile1, File imageFile2, ColorMappingStrategy mappingStrategy)
	{
		this.imageFile1 = imageFile1;
		this.imageFile2 = imageFile2;
		this.mappingStrategy = mappingStrategy;

		try
		{
			image1 = new Image(Display.getCurrent(), imageFile1.getCanonicalPath());
			image2 = new Image(Display.getCurrent(), imageFile2.getCanonicalPath());

			template1 = new ColorTemplate(image1);
			template2 = new ColorTemplate(image2);

			mapping = mappingStrategy.createMapping(template1.pixelsByFrequency(), template2.pixelsByFrequency());
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("The provided files must each contain images", e);
		}
	}

	public void disposeImages()
	{
		if (image1 != null)
			image1.dispose();
		if (image2 != null)
			image2.dispose();
	}

	public Image getImage1()
	{
		return image1;
	}

	public Image getImage2()
	{
		return image2;
	}

	public ColorTemplate getTemplate1()
	{
		return template1;
	}

	public ColorTemplate getTemplate2()
	{
		return template2;
	}

	public ColorMapping getColorMapping()
	{
		return mapping;
	}

	@Override
	public Object getAdapter(Class adapter)
	{
		return null;
	}

	@Override
	public boolean exists()
	{
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return imageFile1.getName() + " : " + imageFile2.getName();
	}

	@Override
	public IPersistableElement getPersistable()
	{
		return null;
	}

	@Override
	public String getToolTipText()
	{
		return "Color template input for " + getName();
	}

}
