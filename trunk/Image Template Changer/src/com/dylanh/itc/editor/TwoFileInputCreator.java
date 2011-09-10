package com.dylanh.itc.editor;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;

import com.dylanh.itc.alg.BestColorMatchStrategy;
import com.dylanh.itc.api.EditorInputCreator;

public class TwoFileInputCreator implements EditorInputCreator
{

	@Override
	public IEditorInput createEditorInput()
	{
		TwoFileDialog dialog = new TwoFileDialog(Display.getCurrent().getActiveShell());
		if (dialog.open() == Window.OK)
		{
			if (dialog.file1 != "" && dialog.file2 != "")
			{
				return new TemplateEditorInput(new File(dialog.file1), new File(dialog.file2), new BestColorMatchStrategy());
			}
		}
		return null;
	}

	private class FileBrowseListener extends SelectionAdapter
	{
		private final Text outText;

		protected FileBrowseListener(Text outText)
		{
			this.outText = outText;
		}

		@Override
		public void widgetSelected(SelectionEvent e)
		{
			FileDialog fd = new FileDialog(e.display.getActiveShell());
			String filename = fd.open();
			if (filename != null)
				outText.setText(filename);
		}
	}

	private class TwoFileDialog extends Dialog
	{

		protected TwoFileDialog(Shell parentShell)
		{
			super(parentShell);
		}

		private Text file1Text, file2Text;
		private Button file1Browse, file2Browse;
		public String file1 = null;
		public String file2 = null;

		@Override
		protected void configureShell(Shell newShell)
		{
			super.configureShell(newShell);
			newShell.setText("Choose 2 Files");
		}

		@Override
		protected Control createDialogArea(Composite outer)
		{
			Composite parent = (Composite) super.createDialogArea(outer);

			parent.setLayout(new GridLayout(2, false));

			file1Text = new Text(parent, SWT.SINGLE | SWT.BORDER);
			file1Browse = new Button(parent, SWT.PUSH);
			file2Text = new Text(parent, SWT.SINGLE | SWT.BORDER);
			file2Browse = new Button(parent, SWT.PUSH);

			file1Text.setLayoutData(makeTextLayoutData());
			file2Text.setLayoutData(makeTextLayoutData());

			file1Browse.setText("Browse...");
			file2Browse.setText("Browse...");

			file1Browse.addSelectionListener(new FileBrowseListener(file1Text));
			file2Browse.addSelectionListener(new FileBrowseListener(file2Text));

			return parent;
		}

		protected GridData makeTextLayoutData()
		{
			GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
			gd.minimumWidth = 250;
			return gd;
		}

		@Override
		protected void okPressed()
		{
			file1 = file1Text.getText();
			file2 = file2Text.getText();
			super.okPressed();
		}
	}

}
