/**
 Provides a window that allows the user to select an import file and format, 
 and methods to perform the import. 
 */

package com.powersurgepub.iwisdom;

  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.psexcel.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.regcodes.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;

/**
 *
 * @author  hbowie
 */
public class ImportWindow 
  extends javax.swing.JFrame
    implements WindowToManage {
  
  private iWisdomCommon         td;
  
  private WisdomIOFormats       wisdomIO = new WisdomIOFormats();
  
  private File                  importFile = null;
  
  private String                lastURL = "";
  
  private boolean               importing = false;
  
  /** Creates new form ImportWindow */
  public ImportWindow(iWisdomCommon td) {
    this.td = td;
    initComponents();
    this.setTitle (Home.getShared().getProgramName() + " Import");
    this.setBounds (100, 100, 600, 300);
    
    wisdomIO.populateComboBox(
        importTypeComboBox, 
        0,  // select for all fields
        0,  // select for export
        0,  // select for transfer
        1); // select for import
    
    td.favorites.setComboBox (importFavoritesComboBox);
  }
  
  /**
   Allow the user to select a file. 
  
   */
  private void selectFile() {
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY); 
    if (td.diskStore != null
        && (! td.diskStore.isUnknown())) {
      chooser.setCurrentDirectory (td.diskStore.getFile());
    }
    File chosenFile = chooser.showOpenDialog (this);
    if (chosenFile != null) {
      setFile (chosenFile);
    }
  }
  
  /**
   Set the file to be imported to the passed value.
  
   @param importFile The file to be imported. 
   */
  public void setFile (File importFile) {
    if (importFile != null
        && importFile.exists()) {
      this.importFile = importFile;
      try {
        importURLText.setText(importFile.toURI().toURL().toString());
        setTypeFromURL();
      } catch (MalformedURLException e) {
        td.trouble.report ("Malformed URL", "Malformed URL");
      }
    } // end if importFile seems to point to something valid
  } // end setFile method
  
  private void setTypeFromURL() {
    
    String url = importURLText.getText();
    if (! url.equals(lastURL)) {
      int periodPosition = url.lastIndexOf('.');
      String type = "";
      if (periodPosition > 0 && ((periodPosition + 1) < url.length())) {
        String ext = importURLText.getText().substring(periodPosition + 1);
        boolean found = false;
        int i = 0;
        while ((! found) && (i < importTypeComboBox.getItemCount())) {
          WisdomIOFormat format = (WisdomIOFormat)importTypeComboBox.getItemAt(i);
          if (format.getFileExt().equalsIgnoreCase(ext)) {
            found = true;
            importTypeComboBox.setSelectedIndex(i);
            type = format.getType();
          } else {
            i++;
          } // end if not found 
        } // end while still looking for matching file extension
      } // end if we found a file extension at the end of the url
      lastURL = url;
    } // end if the url actually changed
  }
  
  /**
    Save the file to disk, allowing the user to specify a new
    name for the disk file.
   */
  private void importNow () {

    boolean ok = true;
    
    td.modIfChanged();
    
    td.items.resetCounts();
    String importName = importURLText.getText().trim();

    if (importName.length() < 1) {
      ok = false;
      td.trouble.report 
          ("URL is missing",
          "Blank URL");
    } 
    
    URL importURL = null;
    if (ok) {
      try {
        importURL = new URL (importName);
      } catch (MalformedURLException e) {
        ok = false;
        td.trouble.report 
            ("Invalid URL "
            + importName,
            "URL Problem");
      }
    }
    
    FileName importFileName = null;
    if (ok) {
      importFileName = new FileName (importName);
    }
    
    if (ok) {
      importing = true;
       WisdomIOFormat ioFormat 
           = (WisdomIOFormat)importTypeComboBox.getSelectedItem();
      String importType = ioFormat.getType();
      String encoding = importEncodingComboBox.getSelectedItem().toString();
      Logger.getShared().recordEvent (LogEvent.NORMAL, 
        "Importing from "  + importName,
        false);
      Logger.getShared().recordEvent (LogEvent.NORMAL, 
            "Importing as " + importType,
            false);

      // Import XML
      if (importType.equals (WisdomIOFormats.XML)) {
        td.diskStore.importXML 
            (importName, td.collectionWindow, td.items);
      } 
      else
        
      // Import Fortune text
      if (importType.equals(WisdomIOFormats.FORTUNE)) {
        WisdomFortuneImport fortuneImport = new WisdomFortuneImport();
        fortuneImport.importFile(importURL, td.items);
      }
      else
        
      // Import unstructured text
      if (importType.equals (WisdomIOFormats.UNSTRUCTURED_TEXT)) {
        WisdomTXTIO txtio = new WisdomTXTIO();
        txtio.importFile (importURL, td.items);
      } 
      else
        
      // Import structured text
      if (importType.equals (WisdomIOFormats.STRUCTURED_TEXT)) {        
        td.diskStore.importStrText 
            (importName, td.collectionWindow, td.items);
      } else {
        
        // Import HTML
        DataSource importer = null;
        if (importType.equals (WisdomIOFormats.HTML)
            && importFile != null) {
          importer = new HTMLFile (importFile);
          HTMLFile htmlImporter = (HTMLFile)importer;
          htmlImporter.useHeadingsAndLists();
        }
        else
          
        // Import an Excel xls spreadsheet  
        if (importType.equals (WisdomIOFormats.SPREADSHEET)
            && importFile != null) {
          importer = new ExcelFile (importFileName.getFile().toString());
        } 
        else
          
        // Import tab-delimited
        if (importType.equals (WisdomIOFormats.TAB_DELIMITED)
            && importFile != null) {
          TabDelimFile tabDelimImporter = new TabDelimFile (importFile);
          tabDelimImporter.setEncoding(encoding);
          importer = tabDelimImporter;
        }
        
        
        if (importFile != null) {
          importer.setLog (Logger.getShared());

          try {
            td.items.addAll (importer, false); 
          } catch (IOException e) {
            td.trouble.report 
                ("Trouble Reading File "
                + importName,
                "File I/O Problem");
          }
          catch (RegistrationException regExc) {
            td.handleRegistrationException (regExc);
          }
        } // end if importFile available
      } // end if not text or XML

      if (td.items.getAddedCount() > 0
          || td.items.getMergedCount() > 0) {
        td.navigator.firstItem();
        td.displayItem();
      }
      JOptionPane.showMessageDialog(this,
          String.valueOf (td.items.getConsideredCount()
              + " Items considered" 
              + ", "
              + td.items.getAddedCount()) 
              + " Items Imported and "
              + String.valueOf (td.items.getMergedCount()
              + " Items Merged"),
          "Import Results",
          JOptionPane.INFORMATION_MESSAGE);
      
    } // end if user specified a file to import
    
    importing = false;
    td.listTab.setColumnWidths();
    this.setVisible (false);
  } // end method importNow
  
  /*
  public void log (WisdomItem item) {
    if (importing) {
      importCount++;
      importLog.append (String.valueOf(importCount)
          + ". Imported " 
          + item.getTitle() 
          + GlobalConstants.LINE_FEED_STRING);
    }
  } */
  
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    selectFileLabel = new javax.swing.JLabel();
    selectFileButton = new javax.swing.JButton();
    orLabel2 = new javax.swing.JLabel();
    importURLLabel = new javax.swing.JLabel();
    importURLText = new javax.swing.JTextField();
    importButton = new javax.swing.JButton();
    importFavoritesLabel = new javax.swing.JLabel();
    importFavoritesComboBox = new javax.swing.JComboBox();
    thenLabel = new javax.swing.JLabel();
    importTypeLabel = new javax.swing.JLabel();
    importTypeComboBox = new javax.swing.JComboBox();
    orLabel1 = new javax.swing.JLabel();
    andLabel = new javax.swing.JLabel();
    importNowLabel = new javax.swing.JLabel();
    thenLabel2 = new javax.swing.JLabel();
    importEncodingLabel = new javax.swing.JLabel();
    importEncodingComboBox = new javax.swing.JComboBox();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());

    selectFileLabel.setText("Select a file:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(selectFileLabel, gridBagConstraints);

    selectFileButton.setText("Browse...");
    selectFileButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectFileButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(selectFileButton, gridBagConstraints);

    orLabel2.setText("or");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(orLabel2, gridBagConstraints);

    importURLLabel.setText("enter a URL:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importURLLabel, gridBagConstraints);

    importURLText.setText("http://");
    importURLText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importURLTextActionPerformed(evt);
      }
    });
    importURLText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        importURLTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importURLText, gridBagConstraints);

    importButton.setText("Import");
    importButton.setToolTipText("Import one or more wisdom entries from an external file.");
    importButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importButton, gridBagConstraints);

    importFavoritesLabel.setText("select a web location:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importFavoritesLabel, gridBagConstraints);

    importFavoritesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    importFavoritesComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importFavoritesComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importFavoritesComboBox, gridBagConstraints);

    thenLabel.setText("then");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(thenLabel, gridBagConstraints);

    importTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    importTypeLabel.setText("select/validate format:");
    importTypeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importTypeLabel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importTypeComboBox, gridBagConstraints);

    orLabel1.setText("or");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(orLabel1, gridBagConstraints);

    andLabel.setText("and");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(andLabel, gridBagConstraints);

    importNowLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    importNowLabel.setText("confirm selections");
    importNowLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importNowLabel, gridBagConstraints);

    thenLabel2.setText("then");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(thenLabel2, gridBagConstraints);

    importEncodingLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    importEncodingLabel.setText("select/validate encoding:");
    importEncodingLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importEncodingLabel, gridBagConstraints);

    importEncodingComboBox.setEditable(true);
    importEncodingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "UTF-8", "MacRoman", " " }));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importEncodingComboBox, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void importFavoritesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFavoritesComboBoxActionPerformed
    WisdomFavorite favorite = (WisdomFavorite)importFavoritesComboBox.getSelectedItem();
    if (favorite != null) {
      importURLText.setText(favorite.getURL());
      setTypeFromURL();
    }
  }//GEN-LAST:event_importFavoritesComboBoxActionPerformed

  private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
    importNow();
  }//GEN-LAST:event_importButtonActionPerformed

  private void selectFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFileButtonActionPerformed
    selectFile();
  }//GEN-LAST:event_selectFileButtonActionPerformed

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formComponentHidden

private void importURLTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importURLTextActionPerformed
  setTypeFromURL();
}//GEN-LAST:event_importURLTextActionPerformed

private void importURLTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_importURLTextFocusLost
  setTypeFromURL();
}//GEN-LAST:event_importURLTextFocusLost
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel andLabel;
  private javax.swing.JButton importButton;
  private javax.swing.JComboBox importEncodingComboBox;
  private javax.swing.JLabel importEncodingLabel;
  private javax.swing.JComboBox importFavoritesComboBox;
  private javax.swing.JLabel importFavoritesLabel;
  private javax.swing.JLabel importNowLabel;
  private javax.swing.JComboBox importTypeComboBox;
  private javax.swing.JLabel importTypeLabel;
  private javax.swing.JLabel importURLLabel;
  private javax.swing.JTextField importURLText;
  private javax.swing.JLabel orLabel1;
  private javax.swing.JLabel orLabel2;
  private javax.swing.JButton selectFileButton;
  private javax.swing.JLabel selectFileLabel;
  private javax.swing.JLabel thenLabel;
  private javax.swing.JLabel thenLabel2;
  // End of variables declaration//GEN-END:variables
  
}
