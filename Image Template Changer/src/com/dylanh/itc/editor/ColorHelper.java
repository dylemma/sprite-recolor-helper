package com.dylanh.itc.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorHelper {
	public static RGB rgba(int pixel) {
		int r = (pixel >> 24) & 0xFF;
		int g = (pixel >> 16) & 0xFF;
		int b = (pixel >> 8) & 0xFF;
		// System.out.println(String.format("%s  ->  r=%d, g=%d, b=%d", Integer.toHexString(pixel),
		// r, g, b));
		return new RGB(r, g, b);
	}

	public static RGB rgb(int pixel) {
		int r = (pixel >> 16) & 0xFF;
		int g = (pixel >> 8) & 0xFF;
		int b = (pixel) & 0xFF;
		// System.out.println(String.format("%s  ->  r=%d, g=%d, b=%d", Integer.toHexString(pixel),
		// r, g, b));
		return new RGB(r, g, b);
	}

	private static final Map<RGB, Color> colorCache = new HashMap<RGB, Color>();

	public static Color getColor(RGB rgb) {
		if (colorCache.containsKey(rgb)) {
			return colorCache.get(rgb);
		}
		Color color = new Color(Display.getDefault(), rgb);
		colorCache.put(rgb, color);
		return color;
	}

	private static final Map<Color, Image> colorSwatches = new HashMap<Color, Image>();
	private static final int colorSwatchSize = 15;

	public static Image getColorSwatch(Color color) {
		if (colorSwatches.containsKey(color)) {
			return colorSwatches.get(color);
		}
		Image img = new Image(Display.getDefault(), colorSwatchSize, colorSwatchSize);
		GC gc = new GC(img);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, colorSwatchSize, colorSwatchSize);
		gc.dispose();
		colorSwatches.put(color, img);
		return img;
	}

	public static void dispose() {
		for (Color color : colorCache.values()) {
			color.dispose();
		}
		colorCache.clear();

		for (Image image : colorSwatches.values()) {
			image.dispose();
		}
	}
}
