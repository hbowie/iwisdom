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

  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  
public class ExportWindow 
  extends javax.swing.JFrame 
    implements WindowToManage {
  
  private             CategoryList     categories     = new CategoryList();
  
  private             WisdomDiskStore  diskStore;
  private             WisdomXMLIO      xmlio;
  
  private             iWisdomCommon  td;

  private             WisdomIOFormats       wisdomIO = new WisdomIOFormats();

  private             TextLineWriter writerGen;
  
  private boolean allFieldsFormatsLoaded = false;
  
  /** Creates new form ExportWindow */
  public ExportWindow(iWisdomCommon td) {
    this.td = td;
    categories.addAll (td.getCategories());
    initComponents();
    this.setTitle (Home.getShared().getProgramName() + " Export");
    this.setBounds (100, 100, 600, 540);
    
    adjustUserOptions();
  }
  
  public void filePrep (WisdomDiskStore store) {
    diskStore = store;
  }
  
  public void initItems() {
    categories = new CategoryList();
    categories.addAll (td.getCategories());
    if (categories.size() > 0) {
      categories.removeElementAt (0);
    }
    categoryComboBox.setModel (categories);
  }
  
  /**
    Prepare to switch tabs and show this one.
   
  private void showThisTab () {
    td.switchTabs();
    exportFileNameText.setText("");
    publishFolderNameText.setText (td.diskStore.getHTMLFolder().toString());
  } 
  
  public void displayItem() {
    WisdomItem item = td.getItem();

  } // end method
   */
  
  /**
   Modifies the item if anything on the screen changed. 
   
   @return True if any item fields were modified. 
   
  public boolean modIfChanged () {
    
    return false;
    
  } // end method
   */
  
  /**
   Adjust related user options based on other options chosen.
   */
  private void adjustUserOptions () {
    
    // See if we can select by category
    selectedCategoryRadioButton.setEnabled (true);
    categoryComboBox.setEnabled (true);
    
    // See whether a category needs to be selected
    if (selectedCategoryRadioButton.isSelected()) {
      categoryComboBox.setEnabled (true);
    } else {
      categoryComboBox.setEnabled (false);
    }
    
    // Adjust format options
    if (allFieldsRadioButton.isSelected() && (! allFieldsFormatsLoaded)) {
      wisdomIO.populateComboBox(exportTypeComboBox, 1, 1, 0, 0);
      allFieldsFormatsLoaded = true;
    }
    else
    if ((! allFieldsRadioButton.isSelected()) && (allFieldsFormatsLoaded)) {
      wisdomIO.populateComboBox(exportTypeComboBox, -1, 1, 0, 0);
      allFieldsFormatsLoaded = false;
    }
  }
  
  public void selectFile () {
    diskStore = td.diskStore;
    boolean ok = true;
    writerGen = null;
    
    wisdomIO.select((WisdomIOFormat)exportTypeComboBox.getSelectedItem());
    XFileChooser chooser = new XFileChooser (); 
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY); 
    chooser.setCurrentDirectory (diskStore.getExportFolder()); 
    File defaultExportFile = new File 
        (diskStore.getExportFolder(),
        wisdomIO.addFileExtension("iWisdom_export"));
    chooser.setFile (defaultExportFile);
    File result = chooser.showSaveDialog (this); 
    if (result != null) {
      System.out.println ("ExportWindow.selectFile result = " + result.toString());
      System.out.println ("  chooser.getSelectedFile      = " 
          + chooser.getSelectedFile().toString());
      writerGen = new FileMaker (chooser.getSelectedFile());
    }
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    scopeButtonGroup = new javax.swing.ButtonGroup();
    fieldScopeButtonGroup = new javax.swing.ButtonGroup();
    destinationButtonGroup = new javax.swing.ButtonGroup();
    fieldScopeLabel = new javax.swing.JLabel();
    allFieldsRadioButton = new javax.swing.JRadioButton();
    wisdomListRadioButton = new javax.swing.JRadioButton();
    exportScopeLabel = new javax.swing.JLabel();
    currentItemRadioButton = new javax.swing.JRadioButton();
    selectedCategoryRadioButton = new javax.swing.JRadioButton();
    categoryComboBox = new javax.swing.JComboBox();
    entireCollectionRadioButton = new javax.swing.JRadioButton();
    exportTypeLabel = new javax.swing.JLabel();
    exportTypeComboBox = new javax.swing.JComboBox();
    categorySplitLabel = new javax.swing.JLabel();
    categorySplitCheckBox = new javax.swing.JCheckBox();
    exportDestinationLabel = new javax.swing.JLabel();
    destinationFileRadioButton = new javax.swing.JRadioButton();
    destinationClipboardRadioButton = new javax.swing.JRadioButton();
    exportButton = new javax.swing.JButton();
    exportFileNameLabel = new javax.swing.JLabel();
    exportFileNameText = new javax.swing.JLabel();
    categoryLabel = new javax.swing.JLabel();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());

    fieldScopeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    fieldScopeLabel.setText("Field Scope:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(fieldScopeLabel, gridBagConstraints);

    fieldScopeButtonGroup.add(allFieldsRadioButton);
    allFieldsRadioButton.setSelected(true);
    allFieldsRadioButton.setText("All Fields");
    allFieldsRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    allFieldsRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    allFieldsRadioButton.setMaximumSize(new java.awt.Dimension(200, 22));
    allFieldsRadioButton.setMinimumSize(new java.awt.Dimension(60, 22));
    allFieldsRadioButton.setPreferredSize(new java.awt.Dimension(80, 22));
    allFieldsRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        allFieldsRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(allFieldsRadioButton, gridBagConstraints);

    fieldScopeButtonGroup.add(wisdomListRadioButton);
    wisdomListRadioButton.setText("Wisdom List");
    wisdomListRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    wisdomListRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    wisdomListRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        wisdomListRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(wisdomListRadioButton, gridBagConstraints);

    exportScopeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    exportScopeLabel.setText("Item Scope:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(exportScopeLabel, gridBagConstraints);

    scopeButtonGroup.add(currentItemRadioButton);
    currentItemRadioButton.setText("Current Item");
    currentItemRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    currentItemRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    currentItemRadioButton.setMaximumSize(new java.awt.Dimension(200, 22));
    currentItemRadioButton.setMinimumSize(new java.awt.Dimension(60, 22));
    currentItemRadioButton.setPreferredSize(new java.awt.Dimension(80, 22));
    currentItemRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        currentItemRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(currentItemRadioButton, gridBagConstraints);

    scopeButtonGroup.add(selectedCategoryRadioButton);
    selectedCategoryRadioButton.setText("Selected Category");
    selectedCategoryRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    selectedCategoryRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    selectedCategoryRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectedCategoryRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(selectedCategoryRadioButton, gridBagConstraints);

    categories.setComboBox(categoryComboBox);
    categoryComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        categoryComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(categoryComboBox, gridBagConstraints);

    scopeButtonGroup.add(entireCollectionRadioButton);
    entireCollectionRadioButton.setSelected(true);
    entireCollectionRadioButton.setText("Entire Collection");
    entireCollectionRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    entireCollectionRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    entireCollectionRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        entireCollectionRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(entireCollectionRadioButton, gridBagConstraints);

    exportTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    exportTypeLabel.setText("Format:");
    exportTypeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
    getContentPane().add(exportTypeLabel, gridBagConstraints);

    exportTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "XML", "Tab Delimited Text", "Plain Text (Markdown)", "Plain Text (Textile)" }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
    getContentPane().add(exportTypeComboBox, gridBagConstraints);

    categorySplitLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    categorySplitLabel.setText("Category Split:");
    categorySplitLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
    getContentPane().add(categorySplitLabel, gridBagConstraints);

    categorySplitCheckBox.setText("Export one record per category?");
    categorySplitCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        categorySplitCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(categorySplitCheckBox, gridBagConstraints);

    exportDestinationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    exportDestinationLabel.setText("Destination:");
    exportDestinationLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
    getContentPane().add(exportDestinationLabel, gridBagConstraints);

    destinationButtonGroup.add(destinationFileRadioButton);
    destinationFileRadioButton.setSelected(true);
    destinationFileRadioButton.setText("File");
    destinationFileRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    destinationFileRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    destinationFileRadioButton.setMaximumSize(new java.awt.Dimension(200, 22));
    destinationFileRadioButton.setMinimumSize(new java.awt.Dimension(60, 22));
    destinationFileRadioButton.setPreferredSize(new java.awt.Dimension(80, 22));
    destinationFileRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        destinationFileRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(destinationFileRadioButton, gridBagConstraints);

    destinationButtonGroup.add(destinationClipboardRadioButton);
    destinationClipboardRadioButton.setText("Clipboard");
    destinationClipboardRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    destinationClipboardRadioButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    destinationClipboardRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        destinationClipboardRadioButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(destinationClipboardRadioButton, gridBagConstraints);

    exportButton.setText("Export...");
    exportButton.setToolTipText("Export one or more wisdom entries to an external file.");
    exportButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 0, 4, 4);
    getContentPane().add(exportButton, gridBagConstraints);

    exportFileNameLabel.setText("To File Name:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
    getContentPane().add(exportFileNameLabel, gridBagConstraints);

    exportFileNameText.setText(" ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(12, 4, 4, 4);
    getContentPane().add(exportFileNameText, gridBagConstraints);

    categoryLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    categoryLabel.setText("Category:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(categoryLabel, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
    writerGen = null;
    if (destinationClipboardRadioButton.isSelected()) {
      writerGen = new ClipboardMaker();
    } else {
      selectFile();
    }
    
    if (writerGen != null) {
  /**
   Export some wisdom data.

   @param diskStore           The WisdomDiskStore from which the item is to
                              be exported.
   @param collectionWindow    The window representing the collection metadata.
   @param sorted              The sorted list of items to be exported.
   @param oneItem             The currently selected item.
   @param writer              The line writer to receive the formatted output.
   @param ioFormat            The input/output format to be used.
   @param selectionScope      An indicator of whether the all of the list is
                              to be exported, or only one specific category,
                              or only the currently selected item.
   @param selectedCategory    The selected category, if the scope is constrained
                              to a single category.
   @param splitCategories     Should a separate record be written out for each
                              category?
   @return                    A string containing the name of the output file,
                              or an error message.
   */
      int selectionScope = Exporter.ALL;
      if (selectedCategoryRadioButton.isSelected()) {
        selectionScope = Exporter.CATEGORY;
      }
      else
      if (currentItemRadioButton.isSelected()) {
        selectionScope = Exporter.CURRENT;
      }
      Exporter.export(
          td.diskStore,
          td.collectionWindow,
          td.sorted,
          td.item,
          writerGen,
          wisdomIO.getSelectedFormat(),
          selectionScope,
          categoryComboBox.getSelectedItem().toString(),
          categorySplitCheckBox.isSelected());
    }
  }//GEN-LAST:event_exportButtonActionPerformed

private void categoryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryComboBoxActionPerformed
  selectedCategoryRadioButton.setSelected (true);
}//GEN-LAST:event_categoryComboBoxActionPerformed

private void allFieldsRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allFieldsRadioButtonActionPerformed
  adjustUserOptions();
}//GEN-LAST:event_allFieldsRadioButtonActionPerformed

private void wisdomListRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wisdomListRadioButtonActionPerformed
  adjustUserOptions();
}//GEN-LAST:event_wisdomListRadioButtonActionPerformed

private void currentItemRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentItemRadioButtonActionPerformed
  adjustUserOptions();
}//GEN-LAST:event_currentItemRadioButtonActionPerformed

private void selectedCategoryRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedCategoryRadioButtonActionPerformed
  adjustUserOptions();
}//GEN-LAST:event_selectedCategoryRadioButtonActionPerformed

private void entireCollectionRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entireCollectionRadioButtonActionPerformed
  adjustUserOptions();
}//GEN-LAST:event_entireCollectionRadioButtonActionPerformed

private void destinationFileRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinationFileRadioButtonActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_destinationFileRadioButtonActionPerformed

private void destinationClipboardRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destinationClipboardRadioButtonActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_destinationClipboardRadioButtonActionPerformed

private void categorySplitCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorySplitCheckBoxActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_categorySplitCheckBoxActionPerformed

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formComponentHidden
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JRadioButton allFieldsRadioButton;
  private javax.swing.JComboBox categoryComboBox;
  private javax.swing.JLabel categoryLabel;
  private javax.swing.JCheckBox categorySplitCheckBox;
  private javax.swing.JLabel categorySplitLabel;
  private javax.swing.JRadioButton currentItemRadioButton;
  private javax.swing.ButtonGroup destinationButtonGroup;
  private javax.swing.JRadioButton destinationClipboardRadioButton;
  private javax.swing.JRadioButton destinationFileRadioButton;
  private javax.swing.JRadioButton entireCollectionRadioButton;
  private javax.swing.JButton exportButton;
  private javax.swing.JLabel exportDestinationLabel;
  private javax.swing.JLabel exportFileNameLabel;
  private javax.swing.JLabel exportFileNameText;
  private javax.swing.JLabel exportScopeLabel;
  private javax.swing.JComboBox exportTypeComboBox;
  private javax.swing.JLabel exportTypeLabel;
  private javax.swing.ButtonGroup fieldScopeButtonGroup;
  private javax.swing.JLabel fieldScopeLabel;
  private javax.swing.ButtonGroup scopeButtonGroup;
  private javax.swing.JRadioButton selectedCategoryRadioButton;
  private javax.swing.JRadioButton wisdomListRadioButton;
  // End of variables declaration//GEN-END:variables
  
}
