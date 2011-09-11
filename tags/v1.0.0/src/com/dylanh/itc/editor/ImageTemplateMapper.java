package com.dylanh.itc.editor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

import uky.example.SWTImageCanvas;

import com.dylanh.itc.util.RGBA;

public class ImageTemplateMapper {
	private Image mappedImage = null;
	private final Image input;
	private final ColorMapping mapping;
	private final Set<Label> labelsToUpdate = new HashSet<Label>();
	private final Set<SWTImageCanvas> canvasesToUpdate = new HashSet<SWTImageCanvas>();

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
		for (SWTImageCanvas canvas : canvasesToUpdate) {
			canvas.setImageData(mappedImage.getImageData());
		}
	}

	// public Control createPartControl(Composite parent) {
	// Label label = new Label(parent, SWT.SINGLE);
	// if (mappedImage != null) {
	// label.setImage(mappedImage);
	// }
	// labelsToUpdate.add(label);
	// return label;
	// }

	public Control createPartControl(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		area.setLayout(new GridLayout(2, false));

		final Label zoomLabel = new Label(area, SWT.SINGLE);
		zoomLabel.setText("Zoom: 1.0x  ");

		final Scale scale = new Scale(area, SWT.NONE);
		scale.setMinimum(0);
		scale.setMaximum(6);
		scale.setSelection(3);

		scale.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Label label = new Label(area, SWT.SINGLE);
		final SWTImageCanvas canvas = new SWTImageCanvas(area);
		if (mappedImage != null) {
			label.setImage(mappedImage);
			canvas.setImageData(mappedImage.getImageData());
		}
		labelsToUpdate.add(label);
		canvasesToUpdate.add(canvas);

		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int exp = scale.getSelection() - 3;
				double zoom = Math.pow(2, exp);
				zoomLabel.setText("Zoom: " + zoom + "x  ");
				canvas.zoomTo(zoom);
			}
		});

		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return area;
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
