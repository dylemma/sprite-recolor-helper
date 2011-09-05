package com.dylanh.itc.editor;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class ColorTemplateImage {
	public static Image getTemplateImage(Image image) {
		ColorTemplate template = new ColorTemplate(image);
		Rectangle imageBounds = image.getBounds();
		return getTemplateImage(template, imageBounds);
	}

	public static Image getTemplateImage(ColorTemplate template, Rectangle imageBounds) {
		List<RGB> pixels = template.pixelsByFrequency();

		int numPixels = pixels.size();
		int numPixelsHigh = (imageBounds.height - (imageBounds.height % 10)) / 10;
		int numPixelsWide = numPixels / numPixelsHigh + (numPixels % numPixelsHigh == 0 ? 0 : 1);

		Image out = new Image(Display.getDefault(), numPixelsWide * 10, numPixelsHigh * 10);
		GC gc = new GC(out);
		Rectangle rect = new Rectangle(0, 0, 10, 10);
		for (RGB rgb : pixels) {
			Color bg = ColorHelper.getColor(rgb);
			gc.setBackground(bg);
			gc.fillRectangle(rect);

			rect.y += 10;
			if (rect.y >= imageBounds.height) {
				rect.y = 0;
				rect.x += 10;
			}
		}

		gc.dispose();
		return out;
	}
}
