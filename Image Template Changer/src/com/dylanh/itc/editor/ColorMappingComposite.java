package com.dylanh.itc.editor;

import java.util.Collection;
import java.util.Map.Entry;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ColorMappingComposite extends Composite {

	private final ColorMapping mapping;
	private final Collection<RGB> mapDomain;

	public ColorMappingComposite(Collection<RGB> keyDomain, Collection<RGB> mapDomain,
			ColorMappingStrategy mappingStrategy, Composite parent, int style) {
		super(parent, style);
		this.mapping = mappingStrategy.createMapping(keyDomain, mapDomain);
		this.mapDomain = mapDomain;

		GridLayout layout = new GridLayout(3, false);
		this.setLayout(layout);

		for (Entry<RGB, RGB> entry : mapping.entrySet()) {
			Image keySwatch = ColorHelper.getColorSwatch(ColorHelper.getColor(entry.getKey()));
			Image mapSwatch = ColorHelper.getColorSwatch(ColorHelper.getColor(entry.getValue()));

			Label keyLabel = new Label(this, SWT.SINGLE);
			Label arrow = new Label(this, SWT.SINGLE);
			Button mapButton = new Button(this, SWT.PUSH);

			keyLabel.setImage(keySwatch);
			arrow.setText(" \u00BB "); // 00BB is a >> character
			mapButton.setImage(mapSwatch);

			mapButton.addSelectionListener(new ColorPickerAdapter(entry.getKey(), mapButton));
		}
	}

	public ColorMapping getMapping() {
		return mapping;
	}

	protected class ColorPickerAdapter extends SelectionAdapter {
		private final RGB key;
		private final Button button;

		public ColorPickerAdapter(RGB key, Button button) {
			this.key = key;
			this.button = button;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			ColorSwatchPopup popup = new ColorSwatchPopup(mapDomain, e.display.getActiveShell());
			if (popup.open() == Window.OK) {
				RGB newRGB = popup.getColorChoice();
				mapping.put(key, newRGB);
				button.setImage(ColorHelper.getColorSwatch(ColorHelper.getColor(newRGB)));
			}
		}
	}

}
