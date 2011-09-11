package com.dylanh.itc.editor.widgetry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.dylanh.itc.data.ColorMapping;
import com.dylanh.itc.data.ColorMapping.Listener;
import com.dylanh.itc.util.ColorHelper;
import com.dylanh.itc.util.RGBA;

public class ColorMappingComposite extends Composite
{

	private final ColorMapping mapping;
	private final Collection<RGBA> mapDomain;

	public ColorMappingComposite(Collection<RGBA> keyDomain, Collection<RGBA> mapDomain, ColorMapping mapping, Composite parent,
			int style)
	{
		super(parent, style);
		this.mapping = mapping;
		this.mapDomain = mapDomain;

		GridLayout layout = new GridLayout(3, false);
		this.setLayout(layout);

		final Map<RGBA, Button> buttonsForColorKeys = new HashMap<RGBA, Button>();

		for (Entry<RGBA, RGBA> entry : mapping.entrySet())
		{
			Image keySwatch = ColorHelper.getColorSwatch(entry.getKey());
			Image mapSwatch = ColorHelper.getColorSwatch(entry.getValue());

			Label keyLabel = new Label(this, SWT.SINGLE);
			Label arrow = new Label(this, SWT.SINGLE);
			Button mapButton = new Button(this, SWT.PUSH);

			keyLabel.setImage(keySwatch);
			keyLabel.setToolTipText(entry.getKey().toString());
			arrow.setText(" \u00BB "); // 00BB is a >> character
			mapButton.setImage(mapSwatch);
			mapButton.setToolTipText(entry.getValue().toString());
			mapButton.addSelectionListener(new ColorPickerAdapter(entry.getKey(), mapButton));

			buttonsForColorKeys.put(entry.getKey(), mapButton);
		}

		// cause buttons to update when the mapping changes
		mapping.addListener(new Listener()
		{
			@Override
			public void mappingChanged(ColorMapping mapRef, RGBA key, RGBA oldValue, RGBA newValue)
			{
				System.out.println("Mapping changed: updating button");
				Button mappedButton = buttonsForColorKeys.get(key);
				mappedButton.setImage(ColorHelper.getColorSwatch(newValue));
				mappedButton.setToolTipText(newValue.toString());
			}
		});
	}

	protected class ColorPickerAdapter extends SelectionAdapter
	{
		private final RGBA key;
		private final Button button;

		public ColorPickerAdapter(RGBA key, Button button)
		{
			this.key = key;
			this.button = button;
		}

		@Override
		public void widgetSelected(SelectionEvent e)
		{
			ColorSwatchPopup popup = new ColorSwatchPopup(mapDomain, e.display.getActiveShell());
			if (popup.open() == Window.OK)
			{
				RGBA newRGBA = popup.getColorChoice();
				mapping.put(key, newRGBA);

			}
		}
	}

}
