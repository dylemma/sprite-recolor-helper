package com.dylanh.itc.editor;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class TemplateEditorInput implements IEditorInput {

	public final File image1;
	public final File image2;

	public TemplateEditorInput(File image1, File image2){
		this.image1 = image1;
		this.image2 = image2;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return image1.getName() + " : " + image2.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Color template input for " + getName();
	}

}
