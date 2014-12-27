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

import com.powersurgepub.psdatalib.psdata.widgets.TextSelector;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psdatalib.markup.*;
  import com.powersurgepub.psdatalib.ui.*;
	import com.powersurgepub.psutils.*;

/**
  Allow user to edit fields related to the work from which the item is taken. 
 */
public class WorkPanel 
  extends javax.swing.JPanel 
    implements TextHandler, WisdomTab {
  
  private iWisdomCommon td;
  private WisdomItem    item;
  private int           holdSourceTypeIndex = 0;
  private boolean       changed = false;
  private TextSelector  sourceTextSelector;

  /**
   Creates new form WorkPanel
   */
  public WorkPanel(iWisdomCommon td) {
    
    this.td = td;
    
    initComponents();
    
    java.awt.GridBagConstraints gridBagConstraints;
    
    sourceTextSelector = new TextSelector();
    sourceTextSelector.setEditable(true);
    sourceTextSelector.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        // sourceActionPerformed(evt);
      }
    });
    sourceTextSelector.addTextHandler (this);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    this.add (sourceTextSelector, gridBagConstraints);
        
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (WisdomDiskStore store) {
    // No file information used on the Item Tab
  }
  
  public void initItems() {

    sourceTextSelector.setValueList (td.items.getSources().getValueList());
    WisdomSource.setSourceTypeComboBox (itemSourceTypeComboBox);
    
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
    item = td.getItem();

    int sourceType = item.getSourceType();
    holdSourceTypeIndex = sourceType;
    itemSourceTypeComboBox.setSelectedIndex (holdSourceTypeIndex);
    itemSourceIDText.setText (item.getSourceID());
    sourceTextSelector.setText (item.getSourceAsString());
    itemRightsText.setText (item.getRights());
    itemYearText.setText (item.getYear());
    itemRightsOwnerText.setText (item.getRightsOwner());
    minorTitleText.setText (item.getMinorTitle());
    itemWebPageText.setText (item.getSourceLink());
    publisherText.setText (item.getPublisher());
    cityText.setText (item.getCity());
    pagesText.setText (item.getPages());
    changed = false;

  } // end method

  public void lastItemCopy(WisdomItem priorItem) {

    int sourceType = priorItem.getSourceType();
    holdSourceTypeIndex = sourceType;
    itemSourceTypeComboBox.setSelectedIndex (holdSourceTypeIndex);
    itemSourceIDText.setText (priorItem.getSourceID());
    sourceTextSelector.setText (priorItem.getSourceAsString());
    itemRightsText.setText (priorItem.getRights());
    itemYearText.setText (priorItem.getYear());
    itemRightsOwnerText.setText (priorItem.getRightsOwner());
    minorTitleText.setText (priorItem.getMinorTitle());
    itemWebPageText.setText (priorItem.getSourceLink());
    publisherText.setText (priorItem.getPublisher());
    cityText.setText (priorItem.getCity());
    changed = true;
  }
  
  public void textSelectionComplete () {
    
    if (! sourceTextSelector.getText().equals(item.getSourceAsString())) {
      int sourceIndex = td.items.getSources().indexOf (sourceTextSelector.getText());
      if (sourceIndex >= 0) {
        WisdomSource source = td.items.getSources().get (sourceIndex);
        itemSourceTypeComboBox.setSelectedIndex (source.getType());
        itemSourceIDText.setText (source.getID());
        itemRightsText.setText (source.getRights());
        itemYearText.setText (source.getYear());
        itemRightsOwnerText.setText (source.getRightsOwner());
        minorTitleText.setText (source.getMinorTitle());
        publisherText.setText (source.getPublisher());
        cityText.setText (source.getCity());
      }
    } // end if source text has been changed by user 
  } // end method
  
  /**
   Modifies the item if anything on the screen changed. 
   
   @return True if any item fields were modified. 
   */
  public boolean modIfChanged () {

    item = td.getItem();
    if (item != null) {
      
      // Check Pages
      if (! pagesText.getText().equals (item.getPages())) {
        changed = true;
        item.setPages (pagesText.getText());
      }
      
      // Check Source Title
      modSourceIfChanged();

      // Check Source Type
      if (itemSourceTypeComboBox.getSelectedIndex() != holdSourceTypeIndex) {
        changed = true;
        item.setSourceType (itemSourceTypeComboBox.getSelectedIndex());
      }

      // Check Year
      if (! itemYearText.getText().equals (item.getYear())) {
        changed = true;
        item.setYear (itemYearText.getText());
      }
      
      // Check source ID
      if (! itemSourceIDText.getText().equals (item.getSourceID())) {
        changed = true;
        item.setSourceID (itemSourceIDText.getText());
      }
      
      // Check Minor Title
      if (! minorTitleText.getText().equals (item.getMinorTitle())) {
        changed = true;
        item.setMinorTitle (minorTitleText.getText());
      }
      
      // Check Source Link
      if (! itemWebPageText.getText().equals (item.getSourceLink())) {
        changed = true;
        item.setSourceLink (itemWebPageText.getText());
      }
        
      // Check Rights
      if (! itemRightsText.getText().equals (item.getRights())) {
        changed = true;
        item.setRights (itemRightsText.getText());
      }
      
      // Check Rights Owner
      if (! itemRightsOwnerText.getText().equals (item.getRightsOwner())) {
        changed = true;
        item.setRightsOwner (itemRightsOwnerText.getText());
      }
      
      // Check Publisher
      if (! publisherText.getText().equals (item.getPublisher())) {
        changed = true;
        item.setPublisher (publisherText.getText());
      }
      
      // Check City 
      if (! cityText.getText().equals (item.getCity())) {
        changed = true;
        item.setCity (cityText.getText());
      }
      
    } // end if item not null
    
    return changed;
    
  } // end method
  
  public void modSourceIfChanged () {
    item = td.getItem();
    if (item != null) {
      String itemSource = item.getSourceAsString();
      String guiSource = sourceTextSelector.getText();
      if (guiSource != null) {
        if (! itemSource.equals (guiSource)) {
          changed = true;
          WisdomSources sources = td.getSources();
          WisdomSource source;
          int sourceIndex = sources.indexOf (guiSource);
          if (sourceIndex < 0) {
            source = new WisdomSource (guiSource);
          } else {
            source = sources.get (sourceIndex);
          }
          item.setSource (source);
        } // end if the user modified the source
      } // end if we have a valid source string entered by the user
    } // end if we have a valid item
  } // end method modSourceIfChanged
  
  private void cleanWebPage () {
    String in = itemWebPageText.getText();
    if (in.length() > 7) {
      String out = StringUtils.cleanURLString (in);
      if (! in.equals(out)) {
        itemWebPageText.setText (out);
      }
    }
  }
  
  private void sourceActionPerformed (java.awt.event.ActionEvent evt) {
    
  }

  /**
   This method is called from within the constructor to initialize the form.
   WARNING: Do NOT modify this code. The content of this method is always
   regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    itemSourceTypeComboBox = new javax.swing.JComboBox();
    itemSourceTypeLabel = new javax.swing.JLabel();
    itemYearLabel = new javax.swing.JLabel();
    itemYearText = new javax.swing.JTextField();
    sourceLabel = new javax.swing.JLabel();
    minorTitleLabel = new javax.swing.JLabel();
    minorTitleText = new javax.swing.JTextField();
    sourceLinklabel = new javax.swing.JLabel();
    itemWebPageText = new javax.swing.JTextField();
    authorFiller1 = new javax.swing.JLabel();
    pagesLabel = new javax.swing.JLabel();
    pagesText = new javax.swing.JTextField();
    itemSourceIDLabel = new javax.swing.JLabel();
    itemSourceIDText = new javax.swing.JTextField();
    itemRightsLabel = new javax.swing.JLabel();
    itemRightsText = new javax.swing.JTextField();
    itemRightsOwnerLabel = new javax.swing.JLabel();
    itemRightsOwnerText = new javax.swing.JTextField();
    publisherLabel = new javax.swing.JLabel();
    publisherText = new javax.swing.JTextField();
    cityLabel = new javax.swing.JLabel();
    cityText = new javax.swing.JTextField();

    setLayout(new java.awt.GridBagLayout());

    itemSourceTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "unknown", "Article", "Book", "CD", "Essay", "Film", "Play", "Poem", "Song", "Story", "Web Log", "Web Page" }));
    itemSourceTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemSourceTypeComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
    add(itemSourceTypeComboBox, gridBagConstraints);

    itemSourceTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    itemSourceTypeLabel.setText("Source Type:");
    itemSourceTypeLabel.setMaximumSize(new java.awt.Dimension(200, 16));
    itemSourceTypeLabel.setPreferredSize(new java.awt.Dimension(100, 16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemSourceTypeLabel, gridBagConstraints);

    itemYearLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    itemYearLabel.setText("Date:");
    itemYearLabel.setMaximumSize(new java.awt.Dimension(120, 20));
    itemYearLabel.setMinimumSize(new java.awt.Dimension(50, 16));
    itemYearLabel.setPreferredSize(new java.awt.Dimension(50, 16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemYearLabel, gridBagConstraints);

    itemYearText.setText("    ");
    itemYearText.setToolTipText("Publication Date");
    itemYearText.setMinimumSize(new java.awt.Dimension(100, 28));
    itemYearText.setPreferredSize(new java.awt.Dimension(120, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(itemYearText, gridBagConstraints);

    sourceLabel.setText("Source:");
    sourceLabel.setMaximumSize(new java.awt.Dimension(100, 20));
    sourceLabel.setPreferredSize(new java.awt.Dimension(60, 16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sourceLabel, gridBagConstraints);

    minorTitleLabel.setText("Minor Title:");
    minorTitleLabel.setMaximumSize(new java.awt.Dimension(150, 20));
    minorTitleLabel.setPreferredSize(new java.awt.Dimension(90, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(minorTitleLabel, gridBagConstraints);

    minorTitleText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    minorTitleText.setMinimumSize(new java.awt.Dimension(120, 28));
    minorTitleText.setPreferredSize(new java.awt.Dimension(300, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(minorTitleText, gridBagConstraints);

    sourceLinklabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    sourceLinklabel.setText("Source Link:");
    sourceLinklabel.setMaximumSize(new java.awt.Dimension(150, 20));
    sourceLinklabel.setMinimumSize(new java.awt.Dimension(80, 16));
    sourceLinklabel.setPreferredSize(new java.awt.Dimension(100, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sourceLinklabel, gridBagConstraints);

    itemWebPageText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    itemWebPageText.setMinimumSize(new java.awt.Dimension(100, 28));
    itemWebPageText.setPreferredSize(new java.awt.Dimension(300, 28));
    itemWebPageText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        itemWebPageTextActionPerformed(evt);
      }
    });
    itemWebPageText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        itemWebPageTextFocusLost(evt);
      }
    });
    itemWebPageText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        itemWebPageTextKeyTyped(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.9;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(itemWebPageText, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(authorFiller1, gridBagConstraints);

    pagesLabel.setText("Pages:");
    pagesLabel.setMaximumSize(new java.awt.Dimension(120, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(pagesLabel, gridBagConstraints);

    pagesText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    pagesText.setToolTipText("Enter first name first (i.e. \"Albert Einstein\", not \"Einstein, Albert\")");
    pagesText.setMinimumSize(new java.awt.Dimension(120, 28));
    pagesText.setPreferredSize(new java.awt.Dimension(240, 28));
    pagesText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        pagesTextKeyTyped(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(pagesText, gridBagConstraints);

    itemSourceIDLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    itemSourceIDLabel.setText("Source ID:");
    itemSourceIDLabel.setMaximumSize(new java.awt.Dimension(150, 20));
    itemSourceIDLabel.setPreferredSize(new java.awt.Dimension(80, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemSourceIDLabel, gridBagConstraints);

    itemSourceIDText.setToolTipText("Enter an ISBN for a book, or other identifying number for the source text");
    itemSourceIDText.setMinimumSize(new java.awt.Dimension(100, 28));
    itemSourceIDText.setPreferredSize(new java.awt.Dimension(300, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(itemSourceIDText, gridBagConstraints);

    itemRightsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    itemRightsLabel.setText("Rights:");
    itemRightsLabel.setMaximumSize(new java.awt.Dimension(120, 20));
    itemRightsLabel.setMinimumSize(new java.awt.Dimension(70, 18));
    itemRightsLabel.setPreferredSize(new java.awt.Dimension(100, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemRightsLabel, gridBagConstraints);

    itemRightsText.setText("    ");
    itemRightsText.setMinimumSize(new java.awt.Dimension(100, 28));
    itemRightsText.setPreferredSize(new java.awt.Dimension(150, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(itemRightsText, gridBagConstraints);

    itemRightsOwnerLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    itemRightsOwnerLabel.setText("By:");
    itemRightsOwnerLabel.setMaximumSize(new java.awt.Dimension(120, 20));
    itemRightsOwnerLabel.setMinimumSize(new java.awt.Dimension(40, 18));
    itemRightsOwnerLabel.setPreferredSize(new java.awt.Dimension(60, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(itemRightsOwnerLabel, gridBagConstraints);

    itemRightsOwnerText.setText("    ");
    itemRightsOwnerText.setMinimumSize(new java.awt.Dimension(100, 28));
    itemRightsOwnerText.setPreferredSize(new java.awt.Dimension(200, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(itemRightsOwnerText, gridBagConstraints);

    publisherLabel.setText("Publisher:");
    publisherLabel.setMaximumSize(new java.awt.Dimension(120, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(publisherLabel, gridBagConstraints);

    publisherText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    publisherText.setToolTipText("Enter first name first (i.e. \"Albert Einstein\", not \"Einstein, Albert\")");
    publisherText.setMinimumSize(new java.awt.Dimension(120, 28));
    publisherText.setPreferredSize(new java.awt.Dimension(240, 28));
    publisherText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        publisherTextKeyTyped(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(publisherText, gridBagConstraints);

    cityLabel.setText("City:");
    cityLabel.setMaximumSize(new java.awt.Dimension(120, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(cityLabel, gridBagConstraints);

    cityText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    cityText.setToolTipText("Enter first name first (i.e. \"Albert Einstein\", not \"Einstein, Albert\")");
    cityText.setMinimumSize(new java.awt.Dimension(120, 28));
    cityText.setPreferredSize(new java.awt.Dimension(240, 28));
    cityText.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        cityTextKeyTyped(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(cityText, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void itemSourceTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSourceTypeComboBoxActionPerformed
    int index = itemSourceTypeComboBox.getSelectedIndex();
  }//GEN-LAST:event_itemSourceTypeComboBoxActionPerformed

  private void itemWebPageTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemWebPageTextActionPerformed
    cleanWebPage();
  }//GEN-LAST:event_itemWebPageTextActionPerformed

  private void itemWebPageTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemWebPageTextFocusLost
    cleanWebPage();
  }//GEN-LAST:event_itemWebPageTextFocusLost

  private void itemWebPageTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemWebPageTextKeyTyped
    //
  }//GEN-LAST:event_itemWebPageTextKeyTyped

  private void pagesTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pagesTextKeyTyped
    // TODO add your handling code here:
  }//GEN-LAST:event_pagesTextKeyTyped

  private void publisherTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_publisherTextKeyTyped
    // TODO add your handling code here:
  }//GEN-LAST:event_publisherTextKeyTyped

  private void cityTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityTextKeyTyped
    // TODO add your handling code here:
  }//GEN-LAST:event_cityTextKeyTyped

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel authorFiller1;
  private javax.swing.JLabel cityLabel;
  private javax.swing.JTextField cityText;
  private javax.swing.JLabel itemRightsLabel;
  private javax.swing.JLabel itemRightsOwnerLabel;
  private javax.swing.JTextField itemRightsOwnerText;
  private javax.swing.JTextField itemRightsText;
  private javax.swing.JLabel itemSourceIDLabel;
  private javax.swing.JTextField itemSourceIDText;
  private javax.swing.JComboBox itemSourceTypeComboBox;
  private javax.swing.JLabel itemSourceTypeLabel;
  private javax.swing.JTextField itemWebPageText;
  private javax.swing.JLabel itemYearLabel;
  private javax.swing.JTextField itemYearText;
  private javax.swing.JLabel minorTitleLabel;
  private javax.swing.JTextField minorTitleText;
  private javax.swing.JLabel pagesLabel;
  private javax.swing.JTextField pagesText;
  private javax.swing.JLabel publisherLabel;
  private javax.swing.JTextField publisherText;
  private javax.swing.JLabel sourceLabel;
  private javax.swing.JLabel sourceLinklabel;
  // End of variables declaration//GEN-END:variables
}
