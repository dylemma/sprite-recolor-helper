package com.dylanh.itc.api;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenEditorHandler extends AbstractHandler {

	public static final String PARAM_CREATOR_ID = "com.dylanh.itc.openeditor.creatorid";
	public static final String PARAM_EDITOR_ID = "com.dylanh.itc.openeditor.editorid";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// check the param
		String creatorID = event.getParameter(PARAM_CREATOR_ID);
		if (creatorID == null) {
			throw new ExecutionException("no input creator specified");
		}

		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IConfigurationElement[] creators = registry.getConfigurationElementsFor("com.dylanh.itc.editorinputcreator");
		for (IConfigurationElement creator : creators) {
			String id = creator.getAttribute("id");
			if (id.equals(creatorID)) {
				try {
					EditorInputCreator creatorInstance = (EditorInputCreator) creator
							.createExecutableExtension("class");
					IEditorInput input = creatorInstance.createEditorInput();
					if (input != null) {
						IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
						IWorkbenchPage page = window.getActivePage();
						page.openEditor(input, event.getParameter(PARAM_EDITOR_ID));
					}
				} catch (CoreException e) {
					throw new ExecutionException("Failed to instantiate input creator", e);
				} catch (ClassCastException e) {
					throw new ExecutionException("Couldn't cast class properly", e);
				}
			}
		}

		return null;
	}
}
