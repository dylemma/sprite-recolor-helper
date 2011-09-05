package com.dylanh.itc.editor;

import static com.dylanh.itc.util.Rand.rand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.RGB;

public class ColorMapping extends HashMap<RGB, RGB> {
	private static final long serialVersionUID = 8504032073542856018L;

	public static interface Listener {
		public void mappingChanged(ColorMapping mapRef, RGB key, RGB oldValue, RGB newValue);
	}

	private final Set<Listener> listeners = new HashSet<Listener>();

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	public void fireMappingChanged(RGB key, RGB oldVal, RGB newVal) {
		for (Listener listener : listeners) {
			listener.mappingChanged(this, key, oldVal, newVal);
		}
	}

	@Override
	public RGB put(RGB key, RGB newValue) {
		RGB oldValue = super.put(key, newValue);
		fireMappingChanged(key, oldValue, newValue);
		return oldValue;
	}

	public static ColorMappingStrategy randomStrategy = new ColorMappingStrategy() {
		@Override
		public ColorMapping createMapping(java.util.Collection<RGB> keyDomain, java.util.Collection<RGB> mapDomain) {
			RGB[] lefts = keyDomain.toArray(new RGB[keyDomain.size()]);
			RGB[] rights = mapDomain.toArray(new RGB[mapDomain.size()]);
			ColorMapping cm = new ColorMapping();
			for (RGB key : lefts) {
				RGB value = rights[rand.nextInt(rights.length)];
				cm.put(key, value);
			}

			return cm;
		}
	};
}
