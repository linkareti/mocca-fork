/*
 * Copyright 2012 by A-SIT, Secure Information Technology Center Austria
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package at.asit.pdfover.gui.composites;

// Imports
import java.awt.Desktop;
import org.eclipse.swt.widgets.FileDialog;
import java.io.File;
import java.io.FileOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.asit.pdfover.gui.workflow.states.State;
import at.asit.pdfover.signator.DocumentSource;

/**
 * GUI component for Output State
 */
public class OutputComposite extends StateComposite {
	
	/**
	 * Selection Listner for save button
	 */
	private final class SaveSelectionListener implements SelectionListener {
		
		/**
		 * Default constructor 
		 */
		public SaveSelectionListener() {
			// Nothing to do
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				FileDialog save = new FileDialog(OutputComposite.this.getShell(), SWT.SAVE | SWT.NATIVE);
				save.setFilterExtensions(new String[] {"*.pdf"});
				save.setFilterNames(new String[] {"PDF Dateien"});
				
				String target = save.open();
				
				File targetFile = new File(target);
				
				DocumentSource source = OutputComposite.this
						.getSignedDocument();
				
				FileOutputStream outstream = new FileOutputStream(targetFile);
				outstream.write(source.getByteArray(), 0,
						source.getByteArray().length);
				outstream.close();
				
				OutputComposite.this.savedFile = targetFile;
				
			} catch (Exception ex) {
				log.error("SaveSelectionListener: ", ex); //$NON-NLS-1$
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// Nothing todo
		}
	}

	/**
	 * Selection Listner for open button
	 */
	private final class OpenSelectionListener implements SelectionListener {
		
		/**
		 * Default constructor
		 */
		public OpenSelectionListener() {
			// Nothing to do
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				DocumentSource source = OutputComposite.this
						.getSignedDocument();

				if (source != null) {
					File open = OutputComposite.this.savedFile;
					if (open == null) {
						// Save as temp file ...
						open = new File("tmp_signed.pdf");
						FileOutputStream outstream = new FileOutputStream(open);
						outstream.write(source.getByteArray(), 0,
								source.getByteArray().length);
						outstream.close();
					}

					if (open.exists()) {
						// Desktop supported check allready done in constructor
						Desktop.getDesktop().open(open);
						return;
					}
				} else {
					// TODO: Handle exception ...
				}
			} catch (Exception ex) {
				log.error("OpenSelectionListener: ", ex); //$NON-NLS-1$
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// Nothing todo
		}
	}

	/**
	 * SLF4J Logger instance
	 **/
	private static final Logger log = LoggerFactory
			.getLogger(OutputComposite.class);

	File savedFile = null;

	private DocumentSource signedDocument;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 *            The parent composite
	 * @param style
	 *            The swt style
	 * @param state
	 *            The owning state
	 */
	public OutputComposite(Composite parent, int style, State state) {
		super(parent, style, state);

		this.setLayout(new FormLayout());

		Button btn_open = new Button(this, SWT.NATIVE | SWT.RESIZE);
		btn_open.setText("OPEN");
		// Point mobile_size = btn_mobile.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		FormData fd_btn_open = new FormData();
		fd_btn_open.left = new FormAttachment(40, 0);
		fd_btn_open.right = new FormAttachment(50, 0);
		fd_btn_open.top = new FormAttachment(45, 0);
		fd_btn_open.bottom = new FormAttachment(55, 0);
		btn_open.setLayoutData(fd_btn_open);
		btn_open.addSelectionListener(new OpenSelectionListener());

		if (!Desktop.isDesktopSupported()) {
			btn_open.setEnabled(false);
		}

		Button btn_save = new Button(this, SWT.NATIVE | SWT.RESIZE);
		btn_save.setText("SAVE");
		// Point card_size = btn_card.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		FormData fd_btn_save = new FormData();
		fd_btn_save.left = new FormAttachment(50, 0);
		fd_btn_save.right = new FormAttachment(60, 0);
		fd_btn_save.top = new FormAttachment(45, 0);
		fd_btn_save.bottom = new FormAttachment(55, 0);
		btn_save.setLayoutData(fd_btn_save);
		btn_save.addSelectionListener(new SaveSelectionListener());

		this.pack();
	}

	/**
	 * Gets the signed document
	 * @return the signed document
	 */
	public DocumentSource getSignedDocument() {
		return this.signedDocument;
	}

	/**
	 * Sets the signed document
	 * @param signedDocument the signed document
	 */
	public void setSignedDocument(final DocumentSource signedDocument) {
		this.signedDocument = signedDocument;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.asit.pdfover.gui.composites.StateComposite#doLayout()
	 */
	@Override
	public void doLayout() {
		// TODO Auto-generated method stub

	}

}