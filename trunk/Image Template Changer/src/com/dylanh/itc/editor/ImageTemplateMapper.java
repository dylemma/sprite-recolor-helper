package com.dylanh.itc.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ImageTemplateMapper {
	private Image mappedImage = null;
	private final Image input;
	private final ColorMapping mapping;
	private final Set<Label> labelsToUpdate = new HashSet<Label>();

	public ImageTemplateMapper(Image input, ColorMapping mapping) {
		this.input = input;
		this.mapping = mapping;

		mapping.addListener(new ColorMapping.Listener() {

			@Override
			public void mappingChanged(ColorMapping mapRef, RGB key, RGB oldValue, RGB newValue) {
				remapImage();
			}
		});

		mapImage();
	}

	public Image getMappedImage() {
		return mappedImage;
	}

	private void remapImage() {
		if (mappedImage != null) {
			mappedImage.dispose();
		}
		mapImage();
		for (Label label : labelsToUpdate) {
			label.setImage(mappedImage);
		}
	}

	public Control createPartControl(Composite parent) {
		Label label = new Label(parent, SWT.SINGLE);
		if (mappedImage != null) {
			label.setImage(mappedImage);
		}
		labelsToUpdate.add(label);
		return label;
	}

	private void mapImage() {
		Rectangle bounds = input.getBounds();
		ImageData imageData = input.getImageData();
		mappedImage = new Image(input.getDevice(), bounds.width, bounds.height);
		GC gc = new GC(mappedImage);

		for (int x = 0; x < bounds.width; ++x) {
			for (int y = 0; y < bounds.height; ++y) {
				int rawPixel = imageData.getPixel(x, y);
				RGB pixel = imageData.palette.getRGB(rawPixel);
				RGB mappedPixel = mapping.get(pixel);
				// int rawAlpha = imageData.getAlpha(x, y);
				gc.setForeground(ColorHelper.getColor(mappedPixel));
				gc.drawPoint(x, y);
			}
		}
		gc.dispose();

		System.out.println("mapped new image");
	}
}
