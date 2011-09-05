package com.dylanh.itc.editor;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class TemplateEditor extends EditorPart {

	private TemplateEditorInput input;

	private Image image1, image2, templateImage1, templateImage2;

	private boolean dirty = true;

	private String outFilePath = null;

	private ImageTemplateMapper imageMapper;

	private String browseForSaveFile() {
		FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell());
		fd.setFilterExtensions(new String[] { "*.png" });
		String result = fd.open();
		if (result != null) {
			if (!result.endsWith(".png")) {
				result += ".png";
			}
		}
		return result;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (outFilePath == null) {
			String chosenPath = browseForSaveFile();
			if (chosenPath == null) {
				return;
			} else {
				outFilePath = chosenPath;
			}
		}
		if (outFilePath != null) {
			Image image = imageMapper.getMappedImage();
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] { image.getImageData() };
			loader.save(outFilePath, SWT.IMAGE_PNG);
			dirty = false;
			firePropertyChange(PROP_DIRTY);
		}
	}

	@Override
	public void doSaveAs() {
		String chosenPath = browseForSaveFile();
		if (chosenPath != null) {
			outFilePath = chosenPath;
		} else {
			return;
		}
		doSave(null);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		if (input instanceof TemplateEditorInput) {
			this.input = (TemplateEditorInput) input;
			try {
				image1 = new Image(Display.getCurrent(), this.input.image1.getCanonicalPath());
				image2 = new Image(Display.getCurrent(), this.input.image2.getCanonicalPath());
			} catch (IOException e) {
				throw new PartInitException("Failed to load input images", e);
			}
		} else {
			throw new PartInitException("Editor input was not a TemplateEditorInput");
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(3, false);
		parent.setLayout(layout);

		Composite inputsComposite = new Composite(parent, SWT.NONE);
		inputsComposite.setLayout(new GridLayout(2, false));

		Label image1Label = new Label(inputsComposite, SWT.SINGLE);
		Label template1Label = new Label(inputsComposite, SWT.SINGLE);
		Label image2Label = new Label(inputsComposite, SWT.SINGLE);
		Label template2Label = new Label(inputsComposite, SWT.SINGLE);

		image1Label.setImage(image1);
		image2Label.setImage(image2);

		ColorTemplate template1 = new ColorTemplate(image1);
		ColorTemplate template2 = new ColorTemplate(image2);

		templateImage1 = ColorTemplateImage.getTemplateImage(template1, image1.getBounds());
		templateImage2 = ColorTemplateImage.getTemplateImage(template2, image2.getBounds());

		template1Label.setImage(templateImage1);
		template2Label.setImage(templateImage2);

		ColorMappingComposite mappingComposite = new ColorMappingComposite(template1.pixelsByFrequency(),
				template2.pixelsByFrequency(), ColorMapping.randomStrategy, parent, SWT.BORDER);

		mappingComposite.getMapping().addListener(new ColorMapping.Listener() {
			@Override
			public void mappingChanged(ColorMapping mapRef, RGB key, RGB oldValue, RGB newValue) {
				System.out.println("Mapping for " + key + " just changed to " + newValue);
				dirty = true;
				firePropertyChange(PROP_DIRTY);
			}
		});

		imageMapper = new ImageTemplateMapper(image1, mappingComposite.getMapping());
		imageMapper.createPartControl(parent);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		System.out.println("Disposing editor: " + this + " with inputs " + input.getName());
		super.dispose();

		if (image1 != null)
			image1.dispose();
		if (image2 != null)
			image2.dispose();
	}

}
