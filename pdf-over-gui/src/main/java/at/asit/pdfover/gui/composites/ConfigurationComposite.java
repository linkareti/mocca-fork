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
import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.asit.pdfover.gui.Messages;
import at.asit.pdfover.gui.controls.ErrorDialog;
import at.asit.pdfover.gui.exceptions.InvalidEmblemFile;
import at.asit.pdfover.gui.exceptions.InvalidNumberException;
import at.asit.pdfover.gui.exceptions.InvalidPortException;
import at.asit.pdfover.gui.workflow.ConfigManipulator;
import at.asit.pdfover.gui.workflow.ConfigProvider;
import at.asit.pdfover.gui.workflow.ConfigurationContainer;
import at.asit.pdfover.gui.workflow.ConfigurationContainerImpl;
import at.asit.pdfover.gui.workflow.PDFSigner;
import at.asit.pdfover.gui.workflow.states.State;
import at.asit.pdfover.signator.SignaturePosition;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

/**
 * Composite for hosting configuration composites
 */
public class ConfigurationComposite extends StateComposite {
	
	/**
	 * The PDF Signer used to produce signature block preview
	 */
	protected PDFSigner signer;
	
	/**
	 * @return the signer
	 */
	public PDFSigner getSigner() {
		return this.signer;
	}

	/**
	 * @param signer the signer to set
	 */
	public void setSigner(PDFSigner signer) {
		this.signer = signer;
		if (this.simpleConfigComposite != null) {
			this.simpleConfigComposite.setSigner(getSigner());
		}
		if (this.advancedConfigComposite != null) {
			// TODO: not needed
			this.advancedConfigComposite.setSigner(getSigner());
		}
	}

	/**
	 * SLF4J Logger instance
	 **/
	private static final Logger log = LoggerFactory
			.getLogger(ConfigurationComposite.class);

	/**
	 * configuration manipulator
	 */
	ConfigManipulator configManipulator = null;

	/**
	 * configuration provider
	 */
	ConfigProvider configProvider = null;

	/**
	 * simple configuration composite
	 */
	BaseConfigurationComposite simpleConfigComposite;

	/**
	 * advanced configuration composite
	 */
	BaseConfigurationComposite advancedConfigComposite;

	/**
	 * configuration container Keeps state for current configuration changes
	 */
	ConfigurationContainer configurationContainer = new ConfigurationContainerImpl();

	/**
	 * The stack layout
	 */
	StackLayout compositeStack = new StackLayout();

	/**
	 * SWT style
	 */
	int style;

	/**
	 * base configuration container
	 */
	Composite containerComposite;

	/**
	 * checks whether the user is done
	 */
	boolean userDone = false;

	/**
	 * Sets the configuration manipulator
	 * 
	 * @param manipulator
	 */
	public void setConfigManipulator(ConfigManipulator manipulator) {
		this.configManipulator = manipulator;
	}

	/**
	 * Sets the configuration provider
	 * 
	 * @param provider
	 */
	public void setConfigProvider(ConfigProvider provider) {
		this.configProvider = provider;
		if (this.configProvider != null) {

			// Initialize Configuration Container
			if (this.configProvider.getDefaultSignaturePosition() != null) {
				this.configurationContainer
						.setAutomaticPosition(this.configProvider
								.getDefaultSignaturePosition()
								.useAutoPositioning());
			}

			this.configurationContainer.setPlaceholderTransparency(
					this.configProvider.getPlaceholderTransparency());

			this.configurationContainer.setBKUSelection(this.configProvider
					.getDefaultBKU());
			try {
				this.configurationContainer.setEmblem(this.configProvider
						.getDefaultEmblem());
			} catch (InvalidEmblemFile e) {
				log.error("Failed to set emblem!", e); //$NON-NLS-1$
			}
			try {
				this.configurationContainer.setNumber(this.configProvider
						.getDefaultMobileNumber());
			} catch (InvalidNumberException e) {
				log.error("Failed to set mobile phone number!", e); //$NON-NLS-1$
			}

			this.configurationContainer.setOutputFolder(this.configProvider
					.getDefaultOutputFolder());

			this.configurationContainer.setProxyHost(this.configProvider
					.getProxyHost());
			try {
				this.configurationContainer.setProxyPort(this.configProvider
						.getProxyPort());
			} catch (InvalidPortException e) {
				log.error("Failed to set proxy port!", e); //$NON-NLS-1$
			}

			this.simpleConfigComposite.loadConfiguration();
			this.advancedConfigComposite.loadConfiguration();
		}
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 * @param state
	 */
	public ConfigurationComposite(Composite parent, int style, State state) {
		super(parent, SWT.FILL | style, state);
		this.style = SWT.FILL | style;

		this.setLayout(new FormLayout());

		this.containerComposite = new Composite(this, SWT.FILL | SWT.RESIZE);

		TabFolder tabFolder = new TabFolder(this.containerComposite, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.bottom = new FormAttachment(100, -5);
		fd_tabFolder.right = new FormAttachment(100, -5);
		fd_tabFolder.top = new FormAttachment(0, 5);
		fd_tabFolder.left = new FormAttachment(0, 5);
		tabFolder.setLayoutData(fd_tabFolder);

		FontData[] fD_tabFolder = tabFolder.getFont().getFontData();
		fD_tabFolder[0].setHeight(TEXT_SIZE_NORMAL);
		tabFolder.setFont(new Font(Display.getCurrent(), fD_tabFolder[0]));

		TabItem simpleTabItem = new TabItem(tabFolder, SWT.NONE);
		simpleTabItem.setText(Messages.getString("config.Simple")); //$NON-NLS-1$

		ScrolledComposite simpleCompositeScr = new ScrolledComposite(
				tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		simpleTabItem.setControl(simpleCompositeScr);
		this.simpleConfigComposite = new SimpleConfigurationComposite(
				simpleCompositeScr, SWT.NONE, state, this.configurationContainer);
		simpleCompositeScr.setContent(this.simpleConfigComposite);
		simpleCompositeScr.setExpandHorizontal(true);
		simpleCompositeScr.setExpandVertical(true);
		simpleCompositeScr.setMinSize(
				this.simpleConfigComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		TabItem advancedTabItem = new TabItem(tabFolder, SWT.NONE);
		advancedTabItem.setText(Messages.getString("config.Advanced")); //$NON-NLS-1$

		ScrolledComposite advancedCompositeScr = new ScrolledComposite(
				tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		advancedTabItem.setControl(advancedCompositeScr);
		this.advancedConfigComposite = new AdvancedConfigurationComposite(
				advancedCompositeScr, SWT.NONE, state, this.configurationContainer);
		advancedCompositeScr.setContent(this.advancedConfigComposite);
		advancedCompositeScr.setExpandHorizontal(true);
		advancedCompositeScr.setExpandVertical(true);
		advancedCompositeScr.setMinSize(
				this.advancedConfigComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		tabFolder.setSelection(simpleTabItem);

		Button btnSpeichern = new Button(this, SWT.NONE);
		FormData fd_btnSpeichern = new FormData();
		fd_btnSpeichern.right = new FormAttachment(100, -5);
		fd_btnSpeichern.bottom = new FormAttachment(100);
		btnSpeichern.setLayoutData(fd_btnSpeichern);
		btnSpeichern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ConfigurationComposite.this.storeConfiguration()) {
					ConfigurationComposite.this.userDone = true;
					ConfigurationComposite.this.state.updateStateMachine();
				}
			}
		});
		btnSpeichern.setText(Messages.getString("common.Save")); //$NON-NLS-1$

		FontData[] fD_btnSpeichern = btnSpeichern.getFont().getFontData();
		fD_btnSpeichern[0].setHeight(TEXT_SIZE_BUTTON);
		btnSpeichern.setFont(new Font(Display.getCurrent(), fD_btnSpeichern[0]));
		
		Button btnAbbrechen = new Button(this, SWT.NONE);
		FormData fd_btnAbrechen = new FormData();
		fd_btnAbrechen.right = new FormAttachment(btnSpeichern, -10);
		fd_btnAbrechen.bottom = new FormAttachment(btnSpeichern, 0, SWT.BOTTOM);
		btnAbbrechen.setLayoutData(fd_btnAbrechen);
		btnAbbrechen.setText(Messages.getString("common.Cancel")); //$NON-NLS-1$
		btnAbbrechen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ConfigurationComposite.this.userDone = true;
				ConfigurationComposite.this.state.updateStateMachine();
			}
		});

		FontData[] fD_btnAbbrechen = btnAbbrechen.getFont().getFontData();
		fD_btnAbbrechen[0].setHeight(TEXT_SIZE_BUTTON);
		btnAbbrechen.setFont(new Font(Display.getCurrent(), fD_btnAbbrechen[0]));

		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 5);
		fd_composite.bottom = new FormAttachment(btnSpeichern, -10);
		fd_composite.left = new FormAttachment(0, 5);
		fd_composite.right = new FormAttachment(100, -5);
		this.containerComposite.setLayoutData(fd_composite);
		this.containerComposite.setLayout(this.compositeStack);
		this.compositeStack.topControl = tabFolder;

		this.doLayout();
}

	boolean storeConfiguration() {

		try {
			this.simpleConfigComposite.validateSettings();
			this.advancedConfigComposite.validateSettings();

			// Write current Configuration
			this.configManipulator.setDefaultBKU(this.configurationContainer
					.getBKUSelection());
			this.configManipulator
					.setDefaultMobileNumber(this.configurationContainer
							.getNumber());
			if (this.configurationContainer.getAutomaticPosition()) {
				this.configManipulator
						.setDefaultSignaturePosition(new SignaturePosition());
			} else {
				this.configManipulator.setDefaultSignaturePosition(null);
			}

			this.configManipulator.setPlaceholderTransparency(
					this.configurationContainer.getPlaceholderTransparency());

			this.configManipulator
					.setDefaultOutputFolder(this.configurationContainer
							.getOutputFolder());

			this.configManipulator.setProxyHost(this.configurationContainer
					.getProxyHost());
			this.configManipulator.setProxyPort(this.configurationContainer
					.getProxyPort());
			this.configManipulator.setDefaultEmblem(this.configurationContainer
					.getEmblem());

		} catch (Exception e) {
			log.error("Settings validation failed!", e); //$NON-NLS-1$
			ErrorDialog dialog = new ErrorDialog(
					getShell(),
					e.getMessage(), //$NON-NLS-1$ 
					false);
			dialog.open();
			return false;
		}

		boolean status = false;
		boolean redo = false;
		do {
			// Save current config to file
			try {
				this.configManipulator.saveCurrentConfiguration();
				redo = false;
				status = true;
			} catch (IOException e) {
				log.error("Failed to save configuration to file!", e); //$NON-NLS-1$
				ErrorDialog dialog = new ErrorDialog(getShell(), 
						Messages.getString("error.FailedToSaveSettings"), true); //$NON-NLS-1$
				redo = dialog.open();
				
				//return false;
			}
		} while (redo);
		return status;
	}

	/**
	 * Checks if the user has finished working with the configuration composite
	 * 
	 * @return if the user is done
	 */
	public boolean isUserDone() {
		return this.userDone;
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
		Control ctrl = this.compositeStack.topControl;
		this.containerComposite.layout(true, true);
		this.getShell().layout(true, true);
		// Note: SWT only layouts children! No grandchildren!
		if (ctrl instanceof StateComposite) {
			((StateComposite) ctrl).doLayout();
		}
	}
}
