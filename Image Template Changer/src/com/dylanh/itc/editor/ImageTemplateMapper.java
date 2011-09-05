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

import com.dylanh.itc.util.RGBA;

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
			public void mappingChanged(ColorMapping mapRef, RGBA key, RGBA oldValue, RGBA newValue) {
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

		int height = imageData.height;
		int width = imageData.width;

		Image tempImage = new Image(input.getDevice(), bounds.width, bounds.height);
		GC gc = new GC(tempImage);
		byte[] alphaData = new byte[height * width];
		// for (int x = 0; x < bounds.width; ++x) {
		// for (int y = 0; y < bounds.height; ++y) {
		for (int y = 0; y < height; y++) {
			byte[] alphaRow = new byte[width];
			for (int x = 0; x < width; x++) {
				int rawPixel = imageData.getPixel(x, y);
				RGB pixel = imageData.palette.getRGB(rawPixel);
				int alpha = imageData.getAlpha(x, y);
				RGBA mappedPixel = mapping.get(new RGBA(pixel, alpha));
				alphaRow[x] = (byte) mappedPixel.getAlpha();
				// int rawAlpha = imageData.getAlpha(x, y);
				gc.setForeground(ColorHelper.getColor(mappedPixel.getRgb()));
				gc.setAlpha(mappedPixel.getAlpha());
				// System.out.println(".a = " + gc.getAlpha());
				gc.drawPoint(x, y);
			}
			System.arraycopy(alphaRow, 0, alphaData, y * width, width);
		}
		gc.dispose();

		ImageData mappedImageData = tempImage.getImageData();
		mappedImageData.alphaData = alphaData;
		tempImage.dispose();
		mappedImage = new Image(input.getDevice(), mappedImageData);

	}
}
