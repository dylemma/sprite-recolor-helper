package com.dylanh.itc.editor.widgetry;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.dylanh.itc.util.ColorHelper;
import com.dylanh.itc.util.RGBA;

public class ColorSwatchPopup extends Dialog {

	private final Collection<RGBA> colorDomain;
	private RGBA colorChoice = null;

	public ColorSwatchPopup(Collection<RGBA> colorDomain, Shell parentShell) {
		super(parentShell);
		this.colorDomain = colorDomain;
	}

	public RGBA getColorChoice() {
		return colorChoice;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		int numPossible = colorDomain.size();
		int numColumns = (int) Math.round(Math.sqrt(numPossible));

		parent.setLayout(new GridLayout(numColumns, true));

		for (RGBA rgba : colorDomain) {
			Button button = new Button(parent, SWT.PUSH);
			button.setImage(ColorHelper.getColorSwatch(rgba));
			button.setText(rgba.toString());
			button.addSelectionListener(new RGBSelectionAdapter(rgba));
		}

		return super.createDialogArea(parent);
	}

	protected class RGBSelectionAdapter extends SelectionAdapter {
		private final RGBA rgba;

		public RGBSelectionAdapter(RGBA rgba) {
			this.rgba = rgba;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			colorChoice = rgba;
			okPressed();
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// only create the cancel button
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

}
