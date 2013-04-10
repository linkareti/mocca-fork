package at.asit.pdfover.signer.pdfas;

import at.asit.pdfover.signator.ByteArrayDocumentSource;
import at.asit.pdfover.signator.SignatureException;
import at.asit.pdfover.signator.Signer;
import at.asit.pdfover.signator.SignResult;
import at.asit.pdfover.signator.SignResultImpl;
import at.asit.pdfover.signator.SignatureParameter;
import at.asit.pdfover.signator.SignaturePosition;
import at.asit.pdfover.signator.SigningState;
import at.gv.egiz.pdfas.api.PdfAs;
import at.gv.egiz.pdfas.api.sign.SignParameters;
import at.gv.egiz.pdfas.api.sign.SignatureDetailInformation;
import at.gv.egiz.pdfas.io.ByteArrayDataSink;
import at.gv.egiz.pdfas.api.commons.Constants;
import at.gv.egiz.pdfas.api.exceptions.PdfAsException;
import at.gv.egiz.pdfas.api.internal.LocalBKUParams;
import at.gv.egiz.pdfas.api.internal.PdfAsInternal;

/**
 * PDF AS Signer Implementation
 */
public class PDFASSigner implements Signer {

	/**
	 * The profile ID
	 */
	protected static final String PROFILE_ID = "SIGNATURBLOCK_DE";

	/**
	 * The template URL
	 */
	protected static final String URL_TEMPLATE = "http://pdfover.4.gv.at/template";

	@Override
	public SigningState prepare(SignatureParameter parameter)
			throws SignatureException {
		try {
			PdfAsSignatureParameter sign_para = null;

			if (PdfAsSignatureParameter.class.isInstance(parameter)) {
				sign_para = PdfAsSignatureParameter.class.cast(parameter);
			}

			if (sign_para == null) {
				throw new SignatureException("Incorrect SignatureParameter!");
			}

			PdfAs pdfas = PDFASHelper.getPdfAs();

			PDFASSigningState state = new PDFASSigningState();

			SignParameters params = new SignParameters();
			params.setSignaturePositioning(sign_para.getPDFASPositioning());
			params.setSignatureDevice(Constants.SIGNATURE_DEVICE_BKU);
			params.setSignatureType(Constants.SIGNATURE_TYPE_BINARY);
			params.setSignatureProfileId(PROFILE_ID);

			if (parameter.getEmblem() != null) {
				params.setProfileOverrideValue("SIG_LABEL", parameter
						.getEmblem().getFileName());
			}

			params.setDocument(sign_para.getPDFASDataSource());

			state.setSignParameters(params);

			PdfAsInternal pdfasInternal = PDFASHelper.getPdfAsInternal();

			// Prepares the document
			SignatureDetailInformation sdi = pdfas.prepareSign(params);

			state.setSignatureDetailInformation(sdi);

			// Retrieve the SL Request
			String slRequest = pdfasInternal.prepareLocalSignRequest(params,
					false, URL_TEMPLATE, sdi);

			PDFASSLRequest request = new PDFASSLRequest(slRequest);

			state.setSignatureRequest(request);

			return state;
		} catch (PdfAsException e) {
			throw new SignatureException(e);
		}
	}

	@Override
	public SignResult sign(SigningState state) throws SignatureException {
		try {
			PDFASSigningState sstate = null;

			if (PDFASSigningState.class.isInstance(state)) {
				sstate = PDFASSigningState.class.cast(state);
			}

			if (sstate == null) {
				throw new SignatureException("Incorrect SigningState!");
			}

			// Retrieve objects
			PdfAs pdfas = PDFASHelper.getPdfAs();

			PdfAsInternal pdfasInternal = PDFASHelper.getPdfAsInternal();

			SignParameters params = sstate.getSignParameters();

			// Prepare Output sink
			ByteArrayDataSink data = new ByteArrayDataSink();
			params.setOutput(data);

			SignatureDetailInformation sdi = sstate
					.getSignatureDetailInformation();

			LocalBKUParams bkuParams = new LocalBKUParams(null, null, null);

			// Perform signature
			at.gv.egiz.pdfas.api.sign.SignResult signResult = pdfasInternal
					.finishLocalSign(pdfas, params, sdi, bkuParams, false,
							sstate.getSignatureResponse().getSLRespone());

			// Preparing Result Response
			SignResultImpl result = new SignResultImpl();

			// Set Signer Certificate
			result.setSignerCertificate(signResult.getSignerCertificate());
			at.gv.egiz.pdfas.api.sign.pos.SignaturePosition pdfasPos = signResult
					.getSignaturePosition();

			// Set Signature position
			SignaturePosition pos = new SignaturePosition(pdfasPos.getX(),
					pdfasPos.getY(), pdfasPos.getPage());
/*			pos.SetAuto(sstate.getPDFAsSignatureParameter()
					.getSignaturePosition().useAutoPositioning());*/
			result.setSignaturePosition(pos);

			// Set signed Document
			result.setSignedDocument(new ByteArrayDocumentSource(data.getData()));

			return result;
		} catch (PdfAsException e) {
			throw new SignatureException(e);
		}
	}

	@Override
	public SignatureParameter newParameter() {
		return new PdfAsSignatureParameter();
	}
}