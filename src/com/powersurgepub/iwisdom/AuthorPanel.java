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


import com.powersurgepub.psdatalib.psdata.values.Author;
import com.powersurgepub.psdatalib.psdata.widgets.TextSelector;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psdatalib.markup.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.ui.*;
	import com.powersurgepub.psutils.*;

/**
 Allow user to edit fields related to the item's author. 
 */
public class AuthorPanel 
  extends javax.swing.JPanel 
    implements TextHandler, WisdomTab  {
  
  private iWisdomCommon td;
  private WisdomItem    item;
  private int           holdSourceTypeIndex = 0;
  private boolean       changed = false;
  private TextSelector  authorTextSelector;

  /**
   Creates new form AuthorPanel
   */
  public AuthorPanel(iWisdomCommon td) {
    this.td = td;
    
    initComponents();
    
    java.awt.GridBagConstraints gridBagConstraints;
    
    authorTextSelector = new TextSelector();
    authorTextSelector.setEditable(true);
    authorTextSelector.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        // authorActionPerformed(evt);
      }
    });
    authorTextSelector.addTextHandler (this);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    this.add(authorTextSelector, gridBagConstraints);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (WisdomDiskStore store) {
    // No file information used on the Item Tab
  }
  
  public void initItems() {

    authorTextSelector.setValueList (td.items.getAuthors().getValueList());

  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
    
    item = td.getItem();
    
    authorTextSelector.setText (item.getAuthorCompleteName());
    authorInfoText.setText (item.getAuthorInfo());
    authorLinkText.setText (item.getAuthorLink());
    changed = false;
    
  } // end method

  public void lastItemCopy(WisdomItem priorItem) {

    authorTextSelector.setText (priorItem.getAuthorCompleteName());
    authorInfoText.setText (priorItem.getAuthorInfo());
    authorLinkText.setText (priorItem.getAuthorLink());
    changed = true;
  }
  
  public void textSelectionComplete () {
    
    if (! authorTextSelector.getText().equals(item.getAuthorCompleteName())) {
      Author author = new Author (authorTextSelector.getText());
      int authorIndex = td.getAuthors().indexOf (author);
      if (authorIndex >= 0) {
        author = td.getAuthors().get (authorIndex);
        authorInfoText.setText (author.getAuthorInfo());
        authorLinkText.setText (author.getLink());
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
      
      // Check author
      modAuthorIfChanged();
      
      // Check author info
      if (! authorInfoText.getText().equals (item.getAuthorInfo())) {
        changed = true;
        item.setAuthorInfo (authorInfoText.getText());
      }
      
      // Check Author Link
      if (! authorLinkText.getText().equals (item.getAuthorLink())) {
        changed = true;
        item.setAuthorLink (authorLinkText.getText());
      }
      
    } // end if item not null
    
    return changed;
    
  } // end method
  
  public void modAuthorIfChanged () {
    item = td.getItem();
    if (item != null) {
      String itemAuthor = item.getAuthorCompleteName();
      String guiAuthor = authorTextSelector.getText();
      if (guiAuthor != null) {
        if (! itemAuthor.equals (guiAuthor)) {
          changed = true;
          Authors authors = td.getAuthors();
          Author author;
          int authorIndex = authors.indexOf (guiAuthor);
          if (authorIndex < 0) {
            author = new Author (guiAuthor);
          } else {
            author = authors.get (authorIndex);
          }
          item.setAuthor (author);
        } // end if the user modified the source
      } // end if we have a valid source string entered by the user
    } // end if we have a valid item
  } // end method modSourceIfChanged

  /**
   This method is called from within the constructor to initialize the form.
   WARNING: Do NOT modify this code. The content of this method is always
   regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    authorLabel = new javax.swing.JLabel();
    authorInfoLabel = new javax.swing.JLabel();
    authorInfoText = new javax.swing.JTextField();
    authorLinkLabel = new javax.swing.JLabel();
    authorLinkText = new javax.swing.JTextField();
    authorFiller = new javax.swing.JLabel();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });
    setLayout(new java.awt.GridBagLayout());

    authorLabel.setText("Author(s):");
    authorLabel.setMaximumSize(new java.awt.Dimension(120, 18));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(authorLabel, gridBagConstraints);

    authorInfoLabel.setText("Info:");
    authorInfoLabel.setMaximumSize(new java.awt.Dimension(150, 16));
    authorInfoLabel.setMinimumSize(new java.awt.Dimension(50, 16));
    authorInfoLabel.setPreferredSize(new java.awt.Dimension(50, 16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(authorInfoLabel, gridBagConstraints);

    authorInfoText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    authorInfoText.setToolTipText("If desired, enter some brief information concerning the author's significance");
    authorInfoText.setMinimumSize(new java.awt.Dimension(120, 28));
    authorInfoText.setPreferredSize(new java.awt.Dimension(240, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(authorInfoText, gridBagConstraints);

    authorLinkLabel.setText("Author Link:");
    authorLinkLabel.setMaximumSize(new java.awt.Dimension(150, 16));
    authorLinkLabel.setMinimumSize(new java.awt.Dimension(80, 16));
    authorLinkLabel.setPreferredSize(new java.awt.Dimension(100, 16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(authorLinkLabel, gridBagConstraints);

    authorLinkText.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    authorLinkText.setMinimumSize(new java.awt.Dimension(200, 28));
    authorLinkText.setPreferredSize(new java.awt.Dimension(600, 28));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
    add(authorLinkText, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    add(authorFiller, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    // itemBodyText.requestFocus();    // TODO add your handling code here:
  }//GEN-LAST:event_formComponentShown

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel authorFiller;
  private javax.swing.JLabel authorInfoLabel;
  private javax.swing.JTextField authorInfoText;
  private javax.swing.JLabel authorLabel;
  private javax.swing.JLabel authorLinkLabel;
  private javax.swing.JTextField authorLinkText;
  // End of variables declaration//GEN-END:variables
}
