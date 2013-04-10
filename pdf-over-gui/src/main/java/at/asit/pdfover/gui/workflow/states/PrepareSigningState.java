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
package at.asit.pdfover.gui.workflow.states;

//Imports
import org.eclipse.swt.SWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.asit.pdfover.gui.MainWindow.Buttons;
import at.asit.pdfover.gui.MainWindowBehavior;
import at.asit.pdfover.gui.composites.WaitingComposite;
import at.asit.pdfover.gui.workflow.StateMachine;
import at.asit.pdfover.gui.workflow.Status;
import at.asit.pdfover.gui.workflow.states.BKUSelectionState.BKUs;
import at.asit.pdfover.signator.PDFFileDocumentSource;
import at.asit.pdfover.signator.SignatureParameter;
import at.asit.pdfover.signator.Signer;

/**
 * User waiting state, wait for PDF Signator library to prepare document for signing.
 */
public class PrepareSigningState extends State {

	/**
	 * @param stateMachine
	 */
	public PrepareSigningState(StateMachine stateMachine) {
		super(stateMachine);
	}

	private final class PrepareDocumentThread implements Runnable {
		
		private PrepareSigningState state;
		
		/**
		 * Default constructor
		 * @param state
		 */
		public PrepareDocumentThread(PrepareSigningState state) {
			this.state = state;
		}
		
		@Override
		public void run() {
			try {
				if(this.state.signer == null) {
					this.state.signer = this.state.stateMachine.getPDFSigner().getPDFSigner();
				}
				
				if(this.state.signatureParameter == null) {
					this.state.signatureParameter = this.state.signer.newParameter();
				}
				
				this.state.signatureParameter.setInputDocument(new PDFFileDocumentSource(this.state.stateMachine.getStatus().getDocument()));
				
				this.state.signatureParameter.setSignaturePosition(this.state.stateMachine.getStatus().getSignaturePosition());
				
				// TODO: Fill library specific signature Parameters ...
				// TODO: setEmblem etc.
				
				this.state.signingState = this.state.signer.prepare(this.state.signatureParameter);
				
			} catch (Exception e) {
				log.error("PrepareDocumentThread: ", e); //$NON-NLS-1$
				this.state.threadException = e;
			}
			finally {
				this.state.stateMachine.invokeUpdate();
			}
		}
	}
	
	/**
	 * SFL4J Logger instance
	 **/
	static final Logger log = LoggerFactory.getLogger(PrepareSigningState.class);
	
	SignatureParameter signatureParameter;
	
	private WaitingComposite waitingComposite = null;

	private WaitingComposite getSelectionComposite() {
		if (this.waitingComposite == null) {
			this.waitingComposite =
					this.stateMachine.getGUIProvider().createComposite(WaitingComposite.class, SWT.RESIZE, this);
		}

		return this.waitingComposite;
	}
	
	at.asit.pdfover.signator.SigningState signingState  = null;

	Signer signer;
	
	Exception threadException = null;
	
	@Override
	public void run() {
		WaitingComposite waiting = this.getSelectionComposite();

		this.stateMachine.getGUIProvider().display(waiting);
		
		this.signer = this.stateMachine.getPDFSigner().getPDFSigner();
		
		Status status = this.stateMachine.getStatus();
		
		if(this.signatureParameter == null) {
			this.signatureParameter = this.signer.newParameter(); 
		}
		
		if(this.signingState == null && this.threadException == null) {
			Thread t = new Thread(new PrepareDocumentThread(this));
			t.start();
			return;
		} 
		
		if(this.threadException != null) {
			// TODO: Jump to error state!
		}
		
		if(this.signingState == null || this.signingState.getSignatureRequest() == null) {
			// This shouldnot happen!! PrepareDocumentThread allready performed, either we have a valid signingState or an exception!!
			// TODO: Jump to error state!
		}
		
		// We got the Request set it into status and move on to next state ...
		status.setSigningState(this.signingState);
		
		if(this.stateMachine.getStatus().getBKU() == BKUs.LOCAL) {
			this.setNextState(new LocalBKUState(this.stateMachine));
		} else if(this.stateMachine.getStatus().getBKU() == BKUs.MOBILE) {
			this.setNextState(new MobileBKUState(this.stateMachine));
		} else {
			log.error("Invalid selected BKU Value \"NONE\" in PrepareSigningState!"); //$NON-NLS-1$
			this.setNextState(new BKUSelectionState(this.stateMachine));
		}
	}

	/* (non-Javadoc)
	 * @see at.asit.pdfover.gui.workflow.states.State#cleanUp()
	 */
	@Override
	public void cleanUp() {
		if (this.waitingComposite != null)
			this.waitingComposite.dispose();
	}

	/* (non-Javadoc)
	 * @see at.asit.pdfover.gui.workflow.states.State#setMainWindowBehavior()
	 */
	@Override
	public void updateMainWindowBehavior() {
		MainWindowBehavior behavior = this.stateMachine.getStatus().getBehavior();
		behavior.reset();
		behavior.setActive(Buttons.OPEN, true);
		behavior.setActive(Buttons.POSITION, true);
		behavior.setActive(Buttons.SIGN, true);
	}

	@Override
	public String toString()  {
		return this.getClass().getName();
	}
}
