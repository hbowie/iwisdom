/*
 * Copyright 2003 - 2016 Herb Bowie
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

  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.pstextio.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.psutils.*;

/**
 *
 */
public class TransferPrefs
    extends javax.swing.JPanel {
  
  public final static String TRANSFER_FORMAT = "transfer-format";
  public final static String MARKDOWN_LINKS_INLINE = "markdown-links-inline";
  public final static String AUTHOR_LINKS_SEPARATE = "author-links-separate";

  private iWisdomCommon      td;
  private PrefsWindow        prefsWindow;
  private WisdomIOFormats           wisdomIO = new WisdomIOFormats();

  private boolean setUpComplete = false;

  /** Creates new form TransferPrefs */
  public TransferPrefs(iWisdomCommon td, PrefsWindow prefsWindow) {
    this.td = td;
    this.prefsWindow = prefsWindow;
    initComponents();

    wisdomIO.populateComboBox(
        transferFormatComboBox, 
        0,  // Select for all fields
        0,  // Select for Export
        1,  // Select for Transfer
        0); // Select for Import
    String transferFormat = UserPrefs.getShared().getPref
        (TRANSFER_FORMAT, WisdomIOFormats.TEXTBLOCK);
    wisdomIO.setSelectedComboBox(transferFormatComboBox, transferFormat);

    markdownLinksCheckBox.setSelected
        (UserPrefs.getShared().getPrefAsBoolean(MARKDOWN_LINKS_INLINE, false));
    wisdomIO.setMarkdownLinksInline(getMarkdownLinksInline());
    markdownLinksCheckBox.setEnabled
        (transferFormat.equalsIgnoreCase(WisdomIOFormats.MARKDOWN));

    authorLinksCheckBox.setSelected
        (UserPrefs.getShared().getPrefAsBoolean(AUTHOR_LINKS_SEPARATE, false));
    wisdomIO.setAuthorLinksSeparate(getAuthorLinksSeparate());

    setUpComplete = true;
    setSampleText();
  }

  public String getTransferFormat() {
    WisdomIOFormat ioFormat 
        = (WisdomIOFormat)transferFormatComboBox.getSelectedItem();
    return ioFormat.getType();
  }

  public boolean getMarkdownLinksInline () {
    return markdownLinksCheckBox.isSelected();
  }

  public boolean getAuthorLinksSeparate () {
    return authorLinksCheckBox.isSelected();
  }

  private void setSampleText() {
    WisdomItem item = td.getItem();
    if (item == null || item.isEmpty()) {
      sampleTextArea.setText("No item selected");
    } else {
      TextLineWriter writer = new StringMaker();
      Exporter.itemFormat(
          td.getDiskStore(),
          item,
          writer,
          new TabDelimFile(writer),
          wisdomIO.getSelectedFormat(),
          getMarkdownLinksInline(),
          authorLinksCheckBox.isSelected(),
          false);
      sampleTextArea.setText(writer.toString());
    }
  }

  public WisdomIOFormats getWisdomIO() {
    return wisdomIO;
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    transferFormatLabel = new javax.swing.JLabel();
    transferFormatComboBox = new javax.swing.JComboBox();
    markdownLinksLabel = new javax.swing.JLabel();
    markdownLinksCheckBox = new javax.swing.JCheckBox();
    authorLinksLabel = new javax.swing.JLabel();
    authorLinksCheckBox = new javax.swing.JCheckBox();
    sampleScrollPane = new javax.swing.JScrollPane();
    sampleTextArea = new javax.swing.JTextArea();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });
    setLayout(new java.awt.GridBagLayout());

    transferFormatLabel.setLabelFor(transferFormatComboBox);
    transferFormatLabel.setText("Transfer Format:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(transferFormatLabel, gridBagConstraints);

    transferFormatComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Option 1" }));
    transferFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        transferFormatComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(transferFormatComboBox, gridBagConstraints);

    markdownLinksLabel.setText("Markdown Links:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(markdownLinksLabel, gridBagConstraints);

    markdownLinksCheckBox.setText("Inline?");
    markdownLinksCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        markdownLinksCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(markdownLinksCheckBox, gridBagConstraints);

    authorLinksLabel.setText("Author Wiki Links:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(authorLinksLabel, gridBagConstraints);

    authorLinksCheckBox.setText("Separate from Names?");
    authorLinksCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        authorLinksCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(authorLinksCheckBox, gridBagConstraints);

    sampleScrollPane.setHorizontalScrollBar(null);

    sampleTextArea.setColumns(20);
    sampleTextArea.setEditable(false);
    sampleTextArea.setFont(new java.awt.Font("Monaco", 0, 14)); // NOI18N
    sampleTextArea.setLineWrap(true);
    sampleTextArea.setRows(5);
    sampleTextArea.setText("Sample text.");
    sampleTextArea.setWrapStyleWord(true);
    sampleScrollPane.setViewportView(sampleTextArea);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 99;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(sampleScrollPane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

private void transferFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transferFormatComboBoxActionPerformed
  if (setUpComplete) {
    UserPrefs.getShared().setPref(TRANSFER_FORMAT,
      getTransferFormat());
    markdownLinksCheckBox.setEnabled
        (getTransferFormat().equalsIgnoreCase(WisdomIOFormats.MARKDOWN));
    wisdomIO.select((WisdomIOFormat)transferFormatComboBox.getSelectedItem());
    setSampleText();
  }
}//GEN-LAST:event_transferFormatComboBoxActionPerformed

private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
  setSampleText();
}//GEN-LAST:event_formComponentShown

private void markdownLinksCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markdownLinksCheckBoxActionPerformed
  if (setUpComplete) {
    UserPrefs.getShared().setPref(MARKDOWN_LINKS_INLINE,
        getMarkdownLinksInline());
    wisdomIO.setMarkdownLinksInline(getMarkdownLinksInline());
    setSampleText();
  }
}//GEN-LAST:event_markdownLinksCheckBoxActionPerformed

private void authorLinksCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authorLinksCheckBoxActionPerformed
  if (setUpComplete) {
    UserPrefs.getShared().setPref(AUTHOR_LINKS_SEPARATE,
        getAuthorLinksSeparate());
    wisdomIO.setAuthorLinksSeparate(getAuthorLinksSeparate());
    setSampleText();
  }
}//GEN-LAST:event_authorLinksCheckBoxActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox authorLinksCheckBox;
  private javax.swing.JLabel authorLinksLabel;
  private javax.swing.JCheckBox markdownLinksCheckBox;
  private javax.swing.JLabel markdownLinksLabel;
  private javax.swing.JScrollPane sampleScrollPane;
  private javax.swing.JTextArea sampleTextArea;
  private javax.swing.JComboBox transferFormatComboBox;
  private javax.swing.JLabel transferFormatLabel;
  // End of variables declaration//GEN-END:variables

}
