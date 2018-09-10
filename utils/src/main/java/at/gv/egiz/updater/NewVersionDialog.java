package at.gv.egiz.updater;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;



public class NewVersionDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7913669490723497774L;
	private final JPanel contentPanel = new JPanel();
	private final Logger log = LoggerFactory.getLogger(NewVersionDialog.class);

	
	private Locale getCurrenLocale() {
		
		String language = Locale.getDefault().getLanguage(); 
		if (language.contains("en")) {
			return Locale.ENGLISH;
		}
		
		return Locale.GERMAN;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewVersionDialog dialog = new NewVersionDialog("dummy");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public NewVersionDialog(String version) throws IOException {
		
		ResourceBundle bundle = ResourceBundle.getBundle("at/gv/egiz/updater/Messages", getCurrenLocale());
		setTitle(bundle.getString("title"));
		setResizable(false);
		setBounds(100, 100, 375, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		BufferedImage myPicture = ImageIO.read(getClass().getResource("/at/gv/egiz/updater/information-icon.png"));
		ImageIcon icon = new ImageIcon(myPicture.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		{
			String pattern = bundle.getString("mocca.version");
			String message = MessageFormat.format(pattern, version);
			JLabel lblNewVersion = new JLabel(message);
			lblNewVersion.setBounds(76, 36, 283, 27);
			contentPanel.add(lblNewVersion);
		}
		{
			JLabel lblNewLabel = new JLabel(bundle.getString("mocca.download"));
			lblNewLabel.setBounds(76, 55, 283, 27);
			contentPanel.add(lblNewLabel);
		}
		{
			
			JLabel lblIcon = new JLabel(icon);
			lblIcon.setBounds(10, 36, 56, 49);
			contentPanel.add(lblIcon);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					
				}
			});
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						openLink();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						close();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void close() {
		this.dispose();
	}
	
	
	private void openLink() {
		if (Desktop.isDesktopSupported()) {
		    try {
				Desktop.getDesktop().browse(new URI(Constants.WEBSTART_URL+Constants.MOCCA_STR));
			} catch (IOException | URISyntaxException e) {
				log.error(e.getMessage());
			}
		}
	}
	
}
