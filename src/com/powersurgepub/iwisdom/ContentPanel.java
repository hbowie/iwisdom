/*
 * Copyright 2003 - 2015 Herb Bowie
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

  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psdatalib.markup.*;
  import com.powersurgepub.psdatalib.psdata.widgets.*;
  import com.powersurgepub.psdatalib.ui.*;
	import com.powersurgepub.psutils.*;


/**
 Allow user to edit fields related to the item's content. 
 */
public class ContentPanel 
  extends javax.swing.JPanel 
    implements TextHandler, WisdomTab {
  
  private iWisdomCommon td;
  private WisdomItem    item;
  private String        originalBodyText = "";
  private boolean       changed = false;
  private TextSelector  itemCategoryTextSelector;

  /**
   Creates new form ContentPanel
   */
  public ContentPanel(iWisdomCommon td) {
    this.td = td;
    
    initComponents();
    
    EditMenuItemMaker editMenuItemMaker 
        = new EditMenuItemMaker (itemBodyText);
    editMenuItemMaker.addCutCopyPaste (td.editMenu);
    
    java.awt.GridBagConstraints gridBagConstraints;
    
    itemCategoryTextSelector = new TextSelector();
    itemCategoryTextSelector.setEditable(true);
    itemCategoryTextSelector.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        // itemCategoryActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    this.add(itemCategoryTextSelector, gridBagConstraints);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (WisdomDiskStore store) {
    // No file information used on the Item Tab
  }
  
  public void initItems() {

    itemCategoryTextSelector.setValueList (td.getCategories());
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
    item = td.getItem();
    itemCategoryTextSelector.setText (item.getCategoryString());
    itemTitleText.setText (item.getTitle());
    itemRatingSlider.setValue (item.getRating());
    
    originalBodyText = item.getBody(MarkupElement.MINIMAL_HTML, false);
    itemBodyText.setText (originalBodyText);
    
    changed = false;
    itemBodyText.requestFocusInWindow();
  } // end method
  
  public void lastItemCopy(WisdomItem priorItem) {

  }
  
  public void textSelectionComplete () {
    
  } // end method
  
  /**
   Modifies the item if anything on the screen changed. 
   
   @return True if any item fields were modified. 
   */
  public boolean modIfChanged () {

    item = td.getItem();
    if (item != null) {
      
      // Check category
      modCategoryIfChanged();

      // Check title
      if (! itemTitleText.getText().equals (item.getTitle())) {
        changed = true;
        item.setTitle (itemTitleText.getText());
      }
      
      // Check body
      if (! itemBodyText.getText().equals 
          (originalBodyText)) {
        changed = true;
        item.setBody (itemBodyText.getText());
      }

      // Check Rating / Rating
      if (itemRatingSlider.getValue() != item.getRating()) {
        changed = true;
        item.setRating (itemRatingSlider.getValue());
      }
      
      itemBodyText.requestFocusInWindow();
      
    } // end if item not null
    
    return changed;
    
  } // end method
  
  public void modCategoryIfChanged () {
    item = td.getItem();
    if (item != null) {
      String itemCategory = item.getCategoryString();
      String guiCategory = itemCategoryTextSelector.getText();
      if (guiCategory != null) {
        if (! itemCategory.equals (guiCategory)) {
          changed = true;
          item.setCategory (guiCategory);
          td.getCategories().registerValue (item.getCategory().toString());
        } // end if gui element has changed
      } // end if gui category not null
    } // end if item not null
  } // end method

  /**
   This method is called from within the constructor to initialize the form.
   WARNING: Do NOT modify this code. The content of this method is always
   regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    itemTitleLabel = new javax.swing.JLabel();
    itemTitleText = new javax.swing.JTextField();
    itemCategoryLabel = new javax.swing.JLabel();
    itemRatingLabel = new javax.swing.JLabel();
    itemRatingSlider = new javax.swing.JSlider();
    itemBodyLabel = new javax.swing.JLabel();
    itemBodyScrollPane = new javax.swing.JScrollPane();
    itemBodyText = new javax.swing.JTextArea();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });
    setLayout(new java.awt.GridBagLayout());

    itemTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    itemTitleLabel.setText("Title: ");
    itemTitleLabel.setMaximumSize(new java.awt.Dimension(36, 30));
    itemTitleLabel.setMinimumSize(new java.awt.Dimension(36, 18));
    itemTitleLabel.setPreferredSize(new java.awt.Dimension(36, 24));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemTitleLabel, gridBagConstraints);

    itemTitleText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    itemTitleText.setToolTipText("Enter a brief title for the item, or allow this field to default to the first few words of the body. ");
    itemTitleText.setMinimumSize(new java.awt.Dimension(200, 28));
    itemTitleText.setPreferredSize(new java.awt.Dimension(600, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(itemTitleText, gridBagConstraints);

    itemCategoryLabel.setText("Category:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemCategoryLabel, gridBagConstraints);

    itemRatingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    itemRatingLabel.setText("Rating:");
    itemRatingLabel.setMaximumSize(new java.awt.Dimension(100, 20));
    itemRatingLabel.setMinimumSize(new java.awt.Dimension(44, 18));
    itemRatingLabel.setPreferredSize(new java.awt.Dimension(50, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemRatingLabel, gridBagConstraints);

    itemRatingSlider.setMajorTickSpacing(1);
    itemRatingSlider.setMaximum(5);
    itemRatingSlider.setMinimum(1);
    itemRatingSlider.setMinorTickSpacing(1);
    itemRatingSlider.setPaintLabels(true);
    itemRatingSlider.setPaintTicks(true);
    itemRatingSlider.setSnapToTicks(true);
    itemRatingSlider.setValue(3);
    itemRatingSlider.setMinimumSize(new java.awt.Dimension(72, 40));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemRatingSlider, gridBagConstraints);

    itemBodyLabel.setText("Body");
    itemBodyLabel.setMaximumSize(new java.awt.Dimension(100, 16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
    add(itemBodyLabel, gridBagConstraints);

    itemBodyScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    itemBodyText.setColumns(10);
    itemBodyText.setLineWrap(true);
    itemBodyText.setRows(4);
    itemBodyText.setTabSize(4);
    itemBodyText.setWrapStyleWord(true);
    itemBodyScrollPane.setViewportView(itemBodyText);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemBodyScrollPane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    itemBodyText.requestFocus();    
  }//GEN-LAST:event_formComponentShown

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel itemBodyLabel;
  private javax.swing.JScrollPane itemBodyScrollPane;
  private javax.swing.JTextArea itemBodyText;
  private javax.swing.JLabel itemCategoryLabel;
  private javax.swing.JLabel itemRatingLabel;
  private javax.swing.JSlider itemRatingSlider;
  private javax.swing.JLabel itemTitleLabel;
  private javax.swing.JTextField itemTitleText;
  // End of variables declaration//GEN-END:variables
}
