/*
 * GeneralPrefs.java
 *
 * Created on August 5, 2007, 12:11 PM
 */

package com.powersurgepub.iwisdom;

  import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import javax.swing.*;

/**
 *
 * @author  hbowie
 */
public class DisplayPrefs extends javax.swing.JPanel {
  
  public static final String ROTATE_BACKGROUND_COLOR_KEY  = "rotatebackcolor";
  public static final String ROTATE_TEXT_COLOR_KEY        = "rotatetextcolor";
  public static final String ROTATE_NORMAL_FONT_SIZE_KEY  = "rotatefontsize";
  public static final String ROTATE_FONT_NAME_KEY         = "rotatefontname";
  public static final String ROTATE_METHOD_KEY            = "rotatemethod";
  public static final String ROTATE_SECONDS_KEY           = "rotateseconds";
  public static final String DISPLAY_TITLE_KEY            = "displaytitle";
  public static final String DISPLAY_SOURCE_KEY           = "displaysource";
  public static final String DISPLAY_TYPE_KEY             = "displaytype";
  public static final String DISPLAY_ADDED_KEY            = "displayadded";
  public static final String DISPLAY_ID_KEY               = "displayid";
  
  private iWisdomCommon      td;
  private PrefsWindow        prefsWindow;
  
  private XOS               xos = XOS.getShared();
  
  private ProgramVersion    programVersion = ProgramVersion.getShared();
  
  private boolean           setupComplete = false;
  
  private String[]          fontList;
  
  /** Creates new form GeneralPrefs */
  public DisplayPrefs(iWisdomCommon td, PrefsWindow prefsWindow) {
    this.td = td;
    this.prefsWindow = prefsWindow;
    initComponents();
    
    td.rotateTextColor = StringUtils.hexStringToColor 
        (td.userPrefs.getPref (ROTATE_TEXT_COLOR_KEY, "000000"));
    
    td.rotateBackgroundColor = StringUtils.hexStringToColor 
        (td.userPrefs.getPref (ROTATE_BACKGROUND_COLOR_KEY, "FFFFFF"));
    
    td.rotateNormalFontSize 
        = td.userPrefs.getPrefAsInt (ROTATE_NORMAL_FONT_SIZE_KEY,  3);
    rotateFontSizeSlider.setValue(td.rotateNormalFontSize);
    if (td.rotateNormalFontSize < 7) {
      td.rotateBigFontSize = td.rotateNormalFontSize + 1;
    } else {
      td.rotateBigFontSize = 7;
    }

    td.rotateFont = td.userPrefs.getPref (ROTATE_FONT_NAME_KEY, "Verdana");
    fontList 
        = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (int i = 0; i < fontList.length; i++) {
      rotateFontComboBox.addItem (fontList [i]);
      if (td.rotateFont.equals (fontList [i])) {
        rotateFontComboBox.setSelectedItem (fontList [i]);
      }
    }

    displayRotateSampleText();
    
    td.rotateList.setRotateMethod 
        (td.userPrefs.getPrefAsInt (ROTATE_METHOD_KEY, RotateList.ROTATE_LINEAR));
    if (td.rotateList.getRotateMethod() != RotateList.ROTATE_LINEAR
        && td.rotateList.getRotateMethod() != RotateList.ROTATE_RANDOM
        && td.rotateList.getRotateMethod() != RotateList.ROTATE_RANDOM_WEIGHTED) {
      td.rotateList.setRotateMethod (RotateList.ROTATE_LINEAR);
    }
    
    td.rotateSeconds
        = td.userPrefs.getPrefAsInt (ROTATE_SECONDS_KEY, 10);
    rotateSecondsText.setText (String.valueOf (td.rotateSeconds));
    
    td.displayTitle
        = td.userPrefs.getPrefAsBoolean (DISPLAY_TITLE_KEY, true);
    displayTitleCheckBox.setSelected (td.displayTitle);
    
    td.displaySource
        = td.userPrefs.getPrefAsBoolean (DISPLAY_SOURCE_KEY, true);
    displaySourceCheckBox.setSelected (td.displaySource);
    
    td.displayType
        = td.userPrefs.getPrefAsBoolean (DISPLAY_TYPE_KEY, true);
    displayTypeCheckBox.setSelected (td.displayType);
    
    td.displayAdded
        = td.userPrefs.getPrefAsBoolean (DISPLAY_ADDED_KEY, true);
    displayAddedCheckBox.setSelected (td.displayAdded);
    
    td.displayID
        = td.userPrefs.getPrefAsBoolean (DISPLAY_ID_KEY, true);
    displayIDCheckBox.setSelected (td.displayID);
    
    setupComplete = true;
  }
  
  public void displayRotateSampleText () {
    StringBuffer text = new StringBuffer();
    text.append ("<html>");
    text.append ("<body bgcolor=\"#" 
        + StringUtils.colorToHexString(td.rotateBackgroundColor)
        + "\" text=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" link=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" alink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" vlink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\">");
    
    text.append ("<p><font size="
        + String.valueOf (td.rotateNormalFontSize)
        + " face=\""
        + td.rotateFont
        + ", Verdana, Arial, sans-serif\">" 
        + "&#8220;There is nothing worse than a brilliant image of a fuzzy concept.&#8221;"
        + "</font></p>");
    
    text.append ("</body>");
    text.append ("</html>");
    rotateTextSample.setText (text.toString());
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
  

  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    rotateBackgroundColorLabel = new javax.swing.JLabel();
    rotateBackgroundColorButton = new javax.swing.JButton();
    rotateTextColorLabel = new javax.swing.JLabel();
    rotateTextColorButton = new javax.swing.JButton();
    rotateFontSizeLabel = new javax.swing.JLabel();
    rotateFontSizeSlider = new javax.swing.JSlider();
    rotateTextSample = new javax.swing.JEditorPane();
    rotateFontLabel = new javax.swing.JLabel();
    rotateFontComboBox = new javax.swing.JComboBox();
    rotateSecondsLabel = new javax.swing.JLabel();
    rotateSecondsText = new javax.swing.JTextField();
    rotateSecondsLabel2 = new javax.swing.JLabel();
    rotateMethodLabel = new javax.swing.JLabel();
    rotateMethodComboBox = new javax.swing.JComboBox();
    displayTitleCheckBox = new javax.swing.JCheckBox();
    displayFieldsLabel = new javax.swing.JLabel();
    displaySourceCheckBox = new javax.swing.JCheckBox();
    displayTypeCheckBox = new javax.swing.JCheckBox();
    displayAddedCheckBox = new javax.swing.JCheckBox();
    displayIDCheckBox = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    rotateBackgroundColorLabel.setText("Display Background:");
    rotateBackgroundColorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateBackgroundColorLabel, gridBagConstraints);

    rotateBackgroundColorButton.setText("Select...");
    rotateBackgroundColorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateBackgroundColorButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateBackgroundColorButton, gridBagConstraints);

    rotateTextColorLabel.setText("Display Text:");
    rotateTextColorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateTextColorLabel, gridBagConstraints);

    rotateTextColorButton.setText("Select...");
    rotateTextColorButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateTextColorButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateTextColorButton, gridBagConstraints);

    rotateFontSizeLabel.setText("Display Font Size:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 2, 2, 2);
    add(rotateFontSizeLabel, gridBagConstraints);

    rotateFontSizeSlider.setMajorTickSpacing(1);
    rotateFontSizeSlider.setMaximum(7);
    rotateFontSizeSlider.setMinimum(1);
    rotateFontSizeSlider.setPaintLabels(true);
    rotateFontSizeSlider.setPaintTicks(true);
    rotateFontSizeSlider.setSnapToTicks(true);
    rotateFontSizeSlider.setValue(3);
    rotateFontSizeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        rotateFontSizeSliderStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateFontSizeSlider, gridBagConstraints);

    rotateTextSample.setContentType("text/html");
    rotateTextSample.setEditable(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateTextSample, gridBagConstraints);

    rotateFontLabel.setText("Display Font:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateFontLabel, gridBagConstraints);

    rotateFontComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateFontComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateFontComboBox, gridBagConstraints);

    rotateSecondsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    rotateSecondsLabel.setText("Rotate Every:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateSecondsLabel, gridBagConstraints);

    rotateSecondsText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
    rotateSecondsText.setText("10");
    rotateSecondsText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateSecondsTextActionPerformed(evt);
      }
    });
    rotateSecondsText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        rotateSecondsTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateSecondsText, gridBagConstraints);

    rotateSecondsLabel2.setText("seconds");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 8;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateSecondsLabel2, gridBagConstraints);

    rotateMethodLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    rotateMethodLabel.setText("Rotate Method:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateMethodLabel, gridBagConstraints);

    rotateMethodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Linear", "Random", "Random, Weighted" }));
    rotateMethodComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rotateMethodComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(rotateMethodComboBox, gridBagConstraints);

    displayTitleCheckBox.setText("Display Title?");
    displayTitleCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayTitleCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayTitleCheckBox, gridBagConstraints);

    displayFieldsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    displayFieldsLabel.setText("Optional Fields");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayFieldsLabel, gridBagConstraints);

    displaySourceCheckBox.setText("Display Source?");
    displaySourceCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displaySourceCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displaySourceCheckBox, gridBagConstraints);

    displayTypeCheckBox.setText("Display Source Type?");
    displayTypeCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayTypeCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 11;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayTypeCheckBox, gridBagConstraints);

    displayAddedCheckBox.setText("Display Added Date and Time?");
    displayAddedCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayAddedCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 12;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayAddedCheckBox, gridBagConstraints);

    displayIDCheckBox.setText("Display ID Number?");
    displayIDCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        displayIDCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 7;
    gridBagConstraints.gridy = 13;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
    add(displayIDCheckBox, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void rotateMethodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateMethodComboBoxActionPerformed
    td.rotateList.setRotateMethod(rotateMethodComboBox.getSelectedIndex());
    td.userPrefs.setPref(ROTATE_METHOD_KEY, td.rotateList.getRotateMethod());
  }//GEN-LAST:event_rotateMethodComboBoxActionPerformed

  private void rotateSecondsTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rotateSecondsTextFocusLost
    try {
      td.rotateSeconds = Integer.parseInt(rotateSecondsText.getText());
      td.userPrefs.setPref(ROTATE_SECONDS_KEY, td.rotateSeconds);
    } catch (NumberFormatException e) {
      rotateSecondsText.setText(String.valueOf(td.rotateSeconds));
    }
  }//GEN-LAST:event_rotateSecondsTextFocusLost

  private void rotateSecondsTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateSecondsTextActionPerformed
    try {
      td.rotateSeconds = Integer.parseInt(rotateSecondsText.getText());
      td.userPrefs.setPref(ROTATE_SECONDS_KEY, td.rotateSeconds);
    } catch (NumberFormatException e) {
      rotateSecondsText.setText(String.valueOf(td.rotateSeconds));
    }
  }//GEN-LAST:event_rotateSecondsTextActionPerformed

  private void rotateFontComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateFontComboBoxActionPerformed
    if (setupComplete) {
      td.rotateFont = (String)rotateFontComboBox.getSelectedItem();
      td.userPrefs.setPref(ROTATE_FONT_NAME_KEY, td.rotateFont);
      displayRotateSampleText();
      td.displayItemOnRotateTab();
    }
  }//GEN-LAST:event_rotateFontComboBoxActionPerformed

  private void rotateFontSizeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rotateFontSizeSliderStateChanged
    if (! rotateFontSizeSlider.getValueIsAdjusting()) {
      td.rotateNormalFontSize = rotateFontSizeSlider.getValue();
      td.userPrefs.setPref(ROTATE_NORMAL_FONT_SIZE_KEY, td.rotateNormalFontSize);
      displayRotateSampleText();
      td.displayItemOnRotateTab();
    }
  }//GEN-LAST:event_rotateFontSizeSliderStateChanged

  private void rotateTextColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateTextColorButtonActionPerformed
    Color oldColor = td.rotateTextColor;
    td.rotateTextColor = JColorChooser.showDialog(
        this,
        "Choose Text Color for Rotate Tab",
        oldColor);
    td.userPrefs.setPref(ROTATE_TEXT_COLOR_KEY,
        StringUtils.colorToHexString(td.rotateTextColor));
    displayRotateSampleText();
    td.displayItemOnRotateTab();
  }//GEN-LAST:event_rotateTextColorButtonActionPerformed

  private void rotateBackgroundColorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rotateBackgroundColorButtonActionPerformed
    Color oldColor = td.rotateBackgroundColor;
    td.rotateBackgroundColor = JColorChooser.showDialog(
        this,
        "Choose Background Color for Rotate Tab",
        oldColor);
    td.userPrefs.setPref(ROTATE_BACKGROUND_COLOR_KEY,
        StringUtils.colorToHexString(td.rotateBackgroundColor));
    displayRotateSampleText();
    td.displayItemOnRotateTab();
  }//GEN-LAST:event_rotateBackgroundColorButtonActionPerformed

private void displayTitleCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayTitleCheckBoxItemStateChanged
  if (setupComplete) {
    td.displayTitle = displayTitleCheckBox.isSelected();
    td.userPrefs.setPref (DISPLAY_TITLE_KEY, td.displayTitle);
  }
}//GEN-LAST:event_displayTitleCheckBoxItemStateChanged

private void displaySourceCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displaySourceCheckBoxItemStateChanged
  if (setupComplete) {
    td.displaySource = displaySourceCheckBox.isSelected();
    td.userPrefs.setPref (DISPLAY_SOURCE_KEY, td.displaySource);
  }
}//GEN-LAST:event_displaySourceCheckBoxItemStateChanged

private void displayTypeCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayTypeCheckBoxItemStateChanged
  if (setupComplete) {
    td.displayType = displayTypeCheckBox.isSelected();
    td.userPrefs.setPref (DISPLAY_TYPE_KEY, td.displayType);
  }
}//GEN-LAST:event_displayTypeCheckBoxItemStateChanged

private void displayAddedCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayAddedCheckBoxItemStateChanged
  if (setupComplete) {
    td.displayAdded = displayAddedCheckBox.isSelected();
    td.userPrefs.setPref (DISPLAY_ADDED_KEY, td.displayAdded);
  }
}//GEN-LAST:event_displayAddedCheckBoxItemStateChanged

private void displayIDCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_displayIDCheckBoxItemStateChanged
  if (setupComplete) {
    td.displayID = displayIDCheckBox.isSelected();
    td.userPrefs.setPref (DISPLAY_ID_KEY, td.displayID);
  }
}//GEN-LAST:event_displayIDCheckBoxItemStateChanged
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox displayAddedCheckBox;
  private javax.swing.JLabel displayFieldsLabel;
  private javax.swing.JCheckBox displayIDCheckBox;
  private javax.swing.JCheckBox displaySourceCheckBox;
  private javax.swing.JCheckBox displayTitleCheckBox;
  private javax.swing.JCheckBox displayTypeCheckBox;
  private javax.swing.JButton rotateBackgroundColorButton;
  private javax.swing.JLabel rotateBackgroundColorLabel;
  private javax.swing.JComboBox rotateFontComboBox;
  private javax.swing.JLabel rotateFontLabel;
  private javax.swing.JLabel rotateFontSizeLabel;
  private javax.swing.JSlider rotateFontSizeSlider;
  private javax.swing.JComboBox rotateMethodComboBox;
  private javax.swing.JLabel rotateMethodLabel;
  private javax.swing.JLabel rotateSecondsLabel;
  private javax.swing.JLabel rotateSecondsLabel2;
  private javax.swing.JTextField rotateSecondsText;
  private javax.swing.JButton rotateTextColorButton;
  private javax.swing.JLabel rotateTextColorLabel;
  private javax.swing.JEditorPane rotateTextSample;
  // End of variables declaration//GEN-END:variables
  
}
