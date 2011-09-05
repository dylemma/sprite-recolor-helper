package com.dylanh.itc.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;

public class ColorTemplate {

	private final Map<RGB, Integer> colorCounts = new HashMap<RGB, Integer>();

	public ColorTemplate(Image image) {
		ImageData imageData = image.getImageData();
		Rectangle imageBounds = image.getBounds();
		for (int i = 0; i < imageBounds.width; ++i) {
			for (int j = 0; j < imageBounds.height; ++j) {
				int pixelRaw = imageData.getPixel(i + imageData.x, j + imageData.y);
				RGB pixel = imageData.palette.getRGB(pixelRaw);
				addPixel(pixel);
			}
		}
	}

	private void addPixel(RGB pixel) {
		int count = colorCounts.containsKey(pixel) ? colorCounts.get(pixel) : 0;
		colorCounts.put(pixel, count + 1);
	}

	public int numUniqueColors() {
		return colorCounts.size();
	}

	public List<RGB> pixelsByFrequency() {
		ArrayList<RGB> list = new ArrayList<RGB>(colorCounts.keySet());
		Comparator<RGB> comparator = new Comparator<RGB>() {
			@Override
			public int compare(RGB arg0, RGB arg1) {
				return colorCounts.get(arg1) - colorCounts.get(arg0);
			}
		};
		Collections.sort(list, comparator);
		return list;
	}
}
