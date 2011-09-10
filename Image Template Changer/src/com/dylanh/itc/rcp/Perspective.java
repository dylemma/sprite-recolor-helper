package com.dylanh.itc.rcp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory
{

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "Image_Template_Changer.perspective";

	@Override
	public void createInitialLayout(IPageLayout layout)
	{
		layout.setEditorAreaVisible(true);
	}
}
