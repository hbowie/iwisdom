/*
 * Copyright 2003 - 2013 Herb Bowie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.powersurgepub.iwisdom;

  import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.disk.*;
import com.powersurgepub.iwisdom.iWisdomCommon;
  import com.powersurgepub.xos2.*;
  import javax.swing.*;

/**
  General preferences for iWisdom.
 */
public class GeneralPrefs extends javax.swing.JPanel {
  
  public static final String SELECT_TO_ITEM_TAB_KEY       = "selitem";
  public static final String LANGUAGE_KEY                 = "language";
  public static final String OLDER_PUBLICATION_STYLE      = "oldpub";
  
  
  private iWisdomCommon      td;
  private PrefsWindow        prefsWindow;
  
  private XOS               xos = XOS.getShared();
  
  private ProgramVersion    programVersion = ProgramVersion.getShared();
  
  private boolean           setupComplete = false;
  
  /** Creates new form GeneralPrefs */
  public GeneralPrefs(iWisdomCommon td, PrefsWindow prefsWindow) {
    this.td = td;
    this.prefsWindow = prefsWindow;
    initComponents();
    
    String selToItem = td.userPrefs.getPref (SELECT_TO_ITEM_TAB_KEY, "Yes");
    if (selToItem.toLowerCase().startsWith ("y")) {
      prefsSelItemTabComboBox.setSelectedIndex (0);
      td.setSelItemTab (true);
    } else {
      prefsSelItemTabComboBox.setSelectedIndex (1);
      td.setSelItemTab (false);
    }

    /* String preferredLanguage = td.userPrefs.getPref
        (LANGUAGE_KEY, (String)prefsLocaleComboBox.getItemAt (0));
    boolean langFound = false;
    int langIndex = 0;
    while ((! langFound) && (langIndex < prefsLocaleComboBox.getItemCount())) {
      String lang = (String)prefsLocaleComboBox.getItemAt (langIndex);
      if (lang.equalsIgnoreCase (preferredLanguage)) {
        langFound = true;
        prefsLocaleComboBox.setSelectedIndex (langIndex);
      } else {
        langIndex++;
      }
    }
    Localizer.getShared().setLocale (getLanguage(), getCountry());
    */

    oldPubStyleCheckBox.setSelected
        (td.userPrefs.getPrefAsBoolean (OLDER_PUBLICATION_STYLE, false));
    
    setupComplete = true;
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (WisdomDiskStore store) {
    // No file information used on the Prefs Tab
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    prefsWindow.switchTabs();
  }
  
  public void displayItem() {
  }  
  
  public boolean modIfChanged() {
    return false;
  }  

  /*
  public boolean isLanguageDefault () {
    return (prefsLocaleComboBox.getSelectedIndex() == 0);
  }

  public String getLanguage () {
    switch (prefsLocaleComboBox.getSelectedIndex()) {
      case (0):
        return Locale.getDefault().getLanguage();
      case (1):
        return ("en");
      case (2):
        return ("fr");
      default:
        return Locale.getDefault().getLanguage();
    }
  }

  public String getCountry () {
    switch (prefsLocaleComboBox.getSelectedIndex()) {
      case (0):
        return Locale.getDefault().getCountry();
      case (1):
        return ("US");
      case (2):
        return ("FR");
      default:
        return Locale.getDefault().getCountry();
    }
  }
  */
  
  private void populateLineEndingsList () {

    String lineSepPlatform = xos.getLineSepPlatform ();
    prefsLineEndingsComboBox.addItem ("Unix");
    prefsLineEndingsComboBox.setSelectedIndex (0);
    prefsLineEndingsComboBox.addItem ("PC/Win/Dos");
    prefsLineEndingsComboBox.addItem ("Mac Classic");
    if (lineSepPlatform.equals (xos.LINE_SEP_PLATFORM_DOS)) {
      prefsLineEndingsComboBox.setSelectedIndex (1);
    }
    else
    if (lineSepPlatform.equals (xos.LINE_SEP_PLATFORM_MAC)) {
      prefsLineEndingsComboBox.setSelectedIndex (2);
    }    
  }
  
  private void warnRelaunch() {
    JOptionPane.showMessageDialog (
        td.tabs,
        "You may need to Quit and relaunch "
            + XOS.getShared().getProgramName()
            + " for your preferences to take effect.",
        "Relaunch Warning",
        JOptionPane.WARNING_MESSAGE);
  }

  public void setOlderPublicationStyle (boolean oldPubStyle) {
    oldPubStyleCheckBox.setSelected(oldPubStyle);
    td.userPrefs.setPref(OLDER_PUBLICATION_STYLE, oldPubStyle);
  }

  public boolean getOlderPublicationStyle () {
    return oldPubStyleCheckBox.isSelected();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    prefsLineEndingsLabel = new javax.swing.JLabel();
    prefsLineEndingsComboBox = new javax.swing.JComboBox();
    prefsSelItemTabLabel = new javax.swing.JLabel();
    prefsSelItemTabComboBox = new javax.swing.JComboBox();
    oldStylePubLabel = new javax.swing.JLabel();
    oldPubStyleCheckBox = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    prefsLineEndingsLabel.setText("Line Endings:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(prefsLineEndingsLabel, gridBagConstraints);

    prefsLineEndingsComboBox.setToolTipText("CR/LF Combinations to be used in output files");
    populateLineEndingsList();
    prefsLineEndingsComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        prefsLineEndingsComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(prefsLineEndingsComboBox, gridBagConstraints);

    prefsSelItemTabLabel.setText("Sel to Display Tab:");
    prefsSelItemTabLabel.setToolTipText("");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(prefsSelItemTabLabel, gridBagConstraints);

    prefsSelItemTabComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
    prefsSelItemTabComboBox.setToolTipText("Selecting an Item from a List Transfers to the Item Tab");
    prefsSelItemTabComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        prefsSelItemTabComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(prefsSelItemTabComboBox, gridBagConstraints);

    oldStylePubLabel.setText("Publication Style:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 2, 4, 2);
    add(oldStylePubLabel, gridBagConstraints);

    oldPubStyleCheckBox.setText("Older Publication Style?");
    oldPubStyleCheckBox.setToolTipText("Check this box to preserve the older (pre 2.2) style of HTML generation");
    oldPubStyleCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    oldPubStyleCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
    oldPubStyleCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        oldPubStyleCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 2);
    add(oldPubStyleCheckBox, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void prefsSelItemTabComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefsSelItemTabComboBoxActionPerformed
    int index = prefsSelItemTabComboBox.getSelectedIndex();
    switch (index) {
      case (0):
        td.setSelItemTab(true);
        break;
      case (1):
        td.setSelItemTab(false);
        break;
    }
    td.userPrefs.setPref(SELECT_TO_ITEM_TAB_KEY,
        (String)prefsSelItemTabComboBox.getSelectedItem());
  }//GEN-LAST:event_prefsSelItemTabComboBoxActionPerformed

  private void prefsLineEndingsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefsLineEndingsComboBoxActionPerformed
    int index = prefsLineEndingsComboBox.getSelectedIndex();
    switch (index) {
      case (0):
        xos.setLineSepPlatform(xos.LINE_SEP_PLATFORM_UNIX);
        break;
      case (1):
        xos.setLineSepPlatform(xos.LINE_SEP_PLATFORM_DOS);
        break;
      case (2):
        xos.setLineSepPlatform(xos.LINE_SEP_PLATFORM_MAC);
        break;
    }
  }//GEN-LAST:event_prefsLineEndingsComboBoxActionPerformed

private void oldPubStyleCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldPubStyleCheckBoxActionPerformed
  td.userPrefs.setPref(OLDER_PUBLICATION_STYLE, oldPubStyleCheckBox.isSelected());
}//GEN-LAST:event_oldPubStyleCheckBoxActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox oldPubStyleCheckBox;
  private javax.swing.JLabel oldStylePubLabel;
  private javax.swing.JComboBox prefsLineEndingsComboBox;
  private javax.swing.JLabel prefsLineEndingsLabel;
  private javax.swing.JComboBox prefsSelItemTabComboBox;
  private javax.swing.JLabel prefsSelItemTabLabel;
  // End of variables declaration//GEN-END:variables
  
}
