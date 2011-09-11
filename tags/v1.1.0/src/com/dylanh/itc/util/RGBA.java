package com.dylanh.itc.util;

import org.eclipse.swt.graphics.RGB;

public class RGBA {
	private final RGB rgb;
	private final int alpha;

	public RGBA(RGB rgb, int alpha) {
		this.rgb = rgb;
		this.alpha = alpha;
	}

	public RGB getRgb() {
		return rgb;
	}

	public int getAlpha() {
		return alpha;
	}

	@Override
	public int hashCode() {
		return (rgb.hashCode() << 8) | alpha;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RGBA) {
			return equalsRGBA((RGBA) obj);
		}
		return false;
	}

	private boolean equalsRGBA(RGBA that) {
		return this.rgb.red == that.rgb.red && this.rgb.green == that.rgb.green && this.rgb.blue == that.rgb.blue
				&& this.alpha == that.alpha;
	}

	@Override
	public String toString() {
		return "RGBA {" + rgb.red + ", " + rgb.green + ", " + rgb.blue + ", " + alpha + "}";
	}
}
