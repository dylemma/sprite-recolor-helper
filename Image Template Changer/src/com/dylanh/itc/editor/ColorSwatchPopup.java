package com.dylanh.itc.editor;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class ColorSwatchPopup extends Dialog {

	private final Collection<RGB> colorDomain;
	private RGB colorChoice = null;

	public ColorSwatchPopup(Collection<RGB> colorDomain, Shell parentShell) {
		super(parentShell);
		this.colorDomain = colorDomain;
	}

	public RGB getColorChoice() {
		return colorChoice;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		int numPossible = colorDomain.size();
		int numColumns = (int) Math.round(Math.sqrt(numPossible));

		parent.setLayout(new GridLayout(numColumns, true));

		for (RGB rgb : colorDomain) {
			Button button = new Button(parent, SWT.PUSH);
			button.setImage(ColorHelper.getColorSwatch(ColorHelper.getColor(rgb)));

			button.addSelectionListener(new RGBSelectionAdapter(rgb));
		}

		return super.createDialogArea(parent);
	}

	protected class RGBSelectionAdapter extends SelectionAdapter {
		private final RGB rgb;

		public RGBSelectionAdapter(RGB rgb) {
			this.rgb = rgb;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			colorChoice = rgb;
			okPressed();
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// only create the cancel button
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

}
