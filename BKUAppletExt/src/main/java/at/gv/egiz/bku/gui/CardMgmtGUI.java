/*
 * Copyright 2008 Federal Chancellery Austria and
 * Graz University of Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.gv.egiz.bku.gui;

import java.awt.Container;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Common superclass for Activation and PinManagement GUIs
 * 
 * @author Clemens Orthacker <clemens.orthacker@iaik.tugraz.at>
 */
public class CardMgmtGUI extends BKUGUIImpl {

  public static final String CARDMGMT_MESSAGES_BUNDLE = "at/gv/egiz/bku/gui/ActivationMessages";

  protected ResourceBundle cardmgmtMessages;

  public CardMgmtGUI(Container contentPane,
          Locale locale,
          Style guiStyle,
          URL backgroundImgURL,
          AbstractHelpListener helpListener) {
    super(contentPane, locale, guiStyle, backgroundImgURL, helpListener);

    if (locale != null) {
        Locale lang = new Locale(locale.getLanguage().substring(0,2));
        log.debug("loading applet resources for language: " + lang.toString());
        cardmgmtMessages = ResourceBundle.getBundle(CARDMGMT_MESSAGES_BUNDLE, lang);
    } else {
        cardmgmtMessages = ResourceBundle.getBundle(CARDMGMT_MESSAGES_BUNDLE);
    }

  }
}