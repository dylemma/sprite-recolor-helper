package com.dylanh.itc.data;

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

import com.dylanh.itc.util.RGBA;

public class ColorTemplate {

	private final Map<RGBA, Integer> colorCounts = new HashMap<RGBA, Integer>();

	public ColorTemplate(Image image) {
		ImageData imageData = image.getImageData();
		Rectangle imageBounds = image.getBounds();
		for (int i = 0; i < imageBounds.width; ++i) {
			for (int j = 0; j < imageBounds.height; ++j) {
				int pixelRaw = imageData.getPixel(i + imageData.x, j + imageData.y);
				int alpha = imageData.getAlpha(i + imageData.x, j + imageData.y);
				RGB pixel = imageData.palette.getRGB(pixelRaw);
				RGBA rgba = new RGBA(pixel, alpha);
				addPixel(rgba);
			}
		}
	}

	private void addPixel(RGBA pixel) {
		int count = colorCounts.containsKey(pixel) ? colorCounts.get(pixel) : 0;
		colorCounts.put(pixel, count + 1);
	}

	public int numUniqueColors() {
		return colorCounts.size();
	}

	public List<RGBA> pixelsByFrequency() {
		ArrayList<RGBA> list = new ArrayList<RGBA>(colorCounts.keySet());
		Comparator<RGBA> comparator = new Comparator<RGBA>() {
			@Override
			public int compare(RGBA arg0, RGBA arg1) {
				return colorCounts.get(arg1) - colorCounts.get(arg0);
			}
		};
		Collections.sort(list, comparator);
		return list;
	}
}
