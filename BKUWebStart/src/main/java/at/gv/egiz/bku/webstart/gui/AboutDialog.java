/*
 * Copyright 2011 by Graz University of Technology, Austria
 * MOCCA has been developed by the E-Government Innovation Center EGIZ, a joint
 * initiative of the Federal Chancellery Austria and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */



/*
 * AboutDialog.java
 *
 * Created on 18.08.2009, 11:54:44
 */

package at.gv.egiz.bku.webstart.gui;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author clemens
 */
public class AboutDialog extends javax.swing.JDialog {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private final Logger log = LoggerFactory.getLogger(AboutDialog.class);

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel3;
  // End of variables declaration//GEN-END:variables

  private String version;

  protected Integer baseWidth;
  protected Integer baseHeight;
  protected Integer baseFontSize;

    /** Creates new form AboutDialog */
    public AboutDialog(java.awt.Frame parent, boolean modal, String version) {
        super(parent, modal);
        this.version = version;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jPanel3 = new javax.swing.JPanel();
    jButton1 = new javax.swing.JButton();

    baseFontSize = jLabel1.getFont().getSize();
    jButton1.setFont(new Font("Dialog", Font.PLAIN, baseFontSize));

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("at/gv/egiz/bku/webstart/messages"); // NOI18N
    setTitle(bundle.getString("about.frame.title")); // NOI18N

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/at/gv/egiz/bku/webstart/chiperling96.png"))); // NOI18N

    jLabel2.setText(getVersionText(bundle));

    jLabel3.setText(bundle.getString("about.title")); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel3)
          .addComponent(jLabel2))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jLabel1)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel3)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel2))
    );

    jButton1.setText(bundle.getString("button.close")); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        .addContainerGap(163, Short.MAX_VALUE)
        .addComponent(jButton1)
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jButton1)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    pack();

    addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            resize();
            super.componentResized(e);
        }
    });
  }// </editor-fold>//GEN-END:initComponents

    private String getVersionText(ResourceBundle bundle) {
      return MessageFormat.format(bundle.getString("about.version"), version);
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      // TODO add your handling code here:
      setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

	protected float getResizeFactor() {

		if (baseWidth == null || baseHeight == null || baseWidth == 0
				|| baseHeight == 0) {

			// first call - determine base width and height
			baseWidth = getContentPane().getWidth();
			baseHeight = getContentPane().getHeight();
		}

		float factor = (float) getContentPane().getSize().getWidth()
				/ (float) baseWidth;

		return factor;
	}

	public void resize() {
		log.debug("Resizing About dialog...");

		float factor = getResizeFactor();

		if (jButton1 != null) {
			jButton1.setFont(jButton1.getFont().deriveFont(
					(float) (baseFontSize * factor)));
		}

		if (jLabel1 != null) {
			jLabel1.setFont(jLabel1.getFont().deriveFont(
					(float) (baseFontSize * factor)));
		}

		if (jLabel2 != null) {
			jLabel2.setFont(jLabel2.getFont().deriveFont(
					(float) (baseFontSize * factor)));
		}

		if (jLabel3 != null) {
			jLabel3.setFont(jLabel3.getFont().deriveFont(
					(float) (baseFontSize * factor)));
		}
	}

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final AboutDialog dialog = new AboutDialog(new javax.swing.JFrame(), true, "1.2.3");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
}