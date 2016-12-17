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

  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psutils.*;
  import java.awt.event.*;
  import java.io.*;


/**
   Allows the user to define options for the current wisdom collection. 
 */
public class CollectionWindow 
  extends javax.swing.JFrame
      implements WindowToManage {
  
  public static final String HTTP_PREFIX = "http://";
  
  public static final String FILE_NAME    = "File Name";
  public static final String TITLE        = "Title";
  public static final String LINK         = "Link";
  public static final String PATH         = "Path";
  public static final String EDITOR       = "Editor";
  public static final String DESCRIPTION  = "Description";
  public static final String STORAGE_FORMAT = "Storage Format";
  public static final String ORGANIZE_WITHIN_FOLDERS = "Organize Within Folders";
  public static final String FILE_NAMING  = "File Naming";
  public static final String XML          = "XML";
  public static final String XML_EXT      = "xml";
  public static final String STRUCTURED_TEXT  = "Text - Structured";
  public static final String STRUCTURED_TEXT_EXT = "txt";
  public static final String BACKUP_FOLDER = "Backup Folder";

  private static RecordDefinition recDef = null;
  
  private iWisdomCommon  td;
  
  private String         collectionVersion = "";
  
  private File           collectionHeaderFile = null;
  
  private String         backupFolder = "";
  
  private String         storageFormat = XML;
  private boolean        organizeWithinFolders = true;
  private boolean        nameByTitle = true;
  
  /** Creates new form CollectionWindow */
  public CollectionWindow(iWisdomCommon td) {
    this.td = td;
    initComponents();
    this.setTitle (Home.getShared().getProgramName() + " Collection Info");
    this.setBounds (100, 100, 600, 540);
  }
  
  public void filePrep (WisdomDiskStore store) {
    this.setTitle (store.getFile().getName() + " Info");
    fileNameText.setText(store.getPath());
    setPrimary (store.isPrimary());
    setForgettable (false);
  }
  
  /**
    Indicates whether this is the primary to do file for the user.
   
    @param primary True if this is the user's primary to do file.
   */
  public void setPrimary (boolean primary) {
    primaryFileCheckBox.setSelected (primary);
  }
  
  /**
    Indicates whether this file should be forgotten about.
   
    @param forgettable True if we should forget about this to do file.
   */
  public void setForgettable (boolean forgettable) {
    forgetFileCheckBox.setSelected (forgettable);
  }
  
  public void initItems() {
    // td.categories.setComboBox (itemCategoryComboBox);
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
    WisdomItem item = td.getItem();

  } // end method
  
  /**
   Modifies the item if anything on the screen changed. 
   
   @return True if any item fields were modified. 
   */
  public boolean modIfChanged () {
    
    return false;
    
  } // end method
  
  public void saveCollectionHeader () {
    if (td != null
        && td.diskStore != null
        && td.diskStore.isAFolder()) {
      WisdomXMLIO xml = td.diskStore.getXMLIO();
      xml.saveCollectionHeader (td.diskStore, this);
      preserveStore();
    }
  }
  
  public String getCollectionTitle () {
    return titleText.getText();
  }
  
  public String getLink() {
    StringBuffer link = new StringBuffer (linkText.getText());
    if (link.toString().equals (HTTP_PREFIX)) {
      link = new StringBuffer("");
    }
    if ((link.length() > 0) 
        && (! (link.toString().endsWith ("/")))) {
      link.append ("/");
    }
    return link.toString();
  }
  
  public String getPath() {
    return pathText.getText();
  }
  
  public String getDescription () {
    return descriptionText.getText();
  }
  
  public String getEditor () {
    return editorText.getText();
  }
  
  public String getCollectionVersion () {
    return collectionVersion;
  }
  
  public File getCollectionHeaderFile() {
    return collectionHeaderFile;
  }
  
  public String getBackupFolder() {
    return backupFolder;
  }
  
  public String getStorageFormat() {
    return storageFormat;
  }
  
  public String getStorageFormatExt() {
    if (storageFormat.equals(STRUCTURED_TEXT)) {
      return STRUCTURED_TEXT_EXT;
    } else {
      return XML_EXT;
    }
  }
  
  public boolean isOrganizeWithinFolders() {
    return organizeWithinFolders;
  }
  
  public boolean isFileNamingByTitle() {
    return nameByTitle;
  }
  
  public void initHeader () {
    setCollectionTitle ("");
    setLink ("");
    setPath ("");
    setDescription ("");
    setEditor("");
    setCollectionVersion ("");
    setCollectionHeaderFile (null);
    setBackupFolder ("");
    setStorageFormat (XML); 
    setOrganizeWithinFolders (true);
    setFileNamingByTitle (true);
    
    // storageFormat = XML;
    organizeWithinFolders = true;
    nameByTitle = true;
  }
  
  public void setCollectionTitle(String title) {
    titleText.setText (title);
  }
  
  public void setLink(String link) {
    if (link.equals("")) {
      linkText.setText (HTTP_PREFIX);
    } else {
      linkText.setText(link);
    }
  }
  
  public void setPath(String path) {
    pathText.setText(path);
  }
  
  public void setDescription (String description) {
    descriptionText.setText (description);
  }
  
  public void setEditor (String editor) {
    editorText.setText(editor);
  }
  
  public void setAllQuotes (String str) {
    boolean allQuotes = false;
    if (str.length() > 0) {
      char c = Character.toLowerCase (str.charAt (0));
      if (c == 'y' || c == 't' || c == 'x') {
        allQuotes = true;
      }
    }
  }
  
  public void setCollectionVersion (String collectionVersion) {
    this.collectionVersion = collectionVersion;
  }
  
  public void setCollectionHeaderFile (File collectionHeaderFile) {
    this.collectionHeaderFile = collectionHeaderFile;
  }
  
  public void setBackupFolder (File backupFolder) {
    try {
      this.backupFolder = backupFolder.getCanonicalPath();
    } catch (IOException e) {
      Logger.getShared().recordEvent(
          LogEvent.MINOR, 
          "Backup Folder name could not be saved", 
          false);
    }
  }
  
  public void setBackupFolder (String backupFolder) {
    this.backupFolder = backupFolder;
  }
  
  public void setStorageFormat (String storageFormat) {
    if (storageFormat.equalsIgnoreCase(XML)) {
      storageFormatComboBox.setSelectedIndex(0);
      this.storageFormat = XML;
    }
    else
    if (storageFormat.equalsIgnoreCase(STRUCTURED_TEXT)) {
      storageFormatComboBox.setSelectedIndex(1);
      this.storageFormat = STRUCTURED_TEXT;
    }
  }
  
  public void setOrganizeWithinFolders(boolean organizeWithinFolders) {
    organizeWithinFoldersCheckBox.setSelected(organizeWithinFolders);
  }
  
  public void setOrganizeWithinFolders(String organizeWithinFoldersStr) {
    organizeWithinFoldersCheckBox.setSelected(organizeWithinFoldersStr.length() > 0
        && (organizeWithinFoldersStr.charAt(0) == 'Y'
          || organizeWithinFoldersStr.charAt(0) == 'y'
          || organizeWithinFoldersStr.charAt(0) == 'T'
          || organizeWithinFoldersStr.charAt(0) == 't'));
    organizeWithinFolders = organizeWithinFoldersCheckBox.isSelected();
  }
  
  public void setFileNamingByTitle (boolean fileNamingByTitle) {
    nameByTitleButton.setSelected(fileNamingByTitle);
    nameByNumberButton.setSelected(! fileNamingByTitle);
    nameByTitle = fileNamingByTitle;
  }
  
  public void setFileNamingByTitle (String fileNamingByTitle) {

    setFileNamingByTitle (fileNamingByTitle.length() > 0
        && (fileNamingByTitle.charAt(0) == 'Y'
          || fileNamingByTitle.charAt(0) == 'y'
          || fileNamingByTitle.charAt(0) == 'T'
          || fileNamingByTitle.charAt(0) == 't'));
  }
  
  public void preserveStore() {
    td.diskStore.setPrimary (primaryFileCheckBox.isSelected());
    td.diskStore.setForgettable (forgetFileCheckBox.isSelected());
  }

 /**
    Returns a record definition for metadata about a Wisdom collection,
    in com.powersurgepub.psdata.RecordDefinition format.

    @return A record format definition for Collection metadata.
   */
  public static RecordDefinition getRecDef() {
    if (recDef == null) {
      recDef = new RecordDefinition();
      recDef.addColumn (FILE_NAME);
      recDef.addColumn (TITLE);
      recDef.addColumn (LINK);
      recDef.addColumn (PATH);
      recDef.addColumn (EDITOR);
      recDef.addColumn (DESCRIPTION);
      recDef.addColumn (BACKUP_FOLDER);
      recDef.addColumn (STORAGE_FORMAT);
      recDef.addColumn (ORGANIZE_WITHIN_FOLDERS);
      recDef.addColumn (FILE_NAMING);
    }
    return recDef;
  }

  /**
    Return this object, formatted as a DataRecord.

    @param recDef Record Definition to be used in building the record.
    */
  public DataRecord getDataRec () {
    // Create a date formatter
    DataRecord nextRec = new DataRecord();
    nextRec.addField (getRecDef(), fileNameText.getText());
    nextRec.addField (getRecDef(), titleText.getText());
    nextRec.addField (getRecDef(), linkText.getText());
    nextRec.addField (getRecDef(), pathText.getText());
    nextRec.addField (getRecDef(), editorText.getText());
    nextRec.addField (getRecDef(), descriptionText.getText());
    nextRec.addField (getRecDef(), backupFolder);
    nextRec.addField (getRecDef(), getStorageFormat());
    nextRec.addField (getRecDef(), String.valueOf(isOrganizeWithinFolders()));
    nextRec.addField (getRecDef(), String.valueOf(isFileNamingByTitle()));
    return nextRec;
  }

  public boolean saveToTabDelim(File file) {
    boolean ok = true;
    TabDelimFile tdf = new TabDelimFile (file);
    try {
      tdf.openForOutput (getRecDef());
      tdf.nextRecordOut(getDataRec());
      tdf.close();
    } catch (java.io.IOException e) {
      Trouble.getShared().report
        ("I/O Problems encountered while saving collection metadata",
        "I/O Error");
      ok = false;
    }
    return ok;
  }

  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    fileNameButtonGroup = new javax.swing.ButtonGroup();
    fileNameLabel = new javax.swing.JLabel();
    fileNameText = new javax.swing.JLabel();
    storageFormatLabel = new javax.swing.JLabel();
    storageFormatComboBox = new javax.swing.JComboBox();
    organizeWithinFoldersLabel = new javax.swing.JLabel();
    organizeWithinFoldersCheckBox = new javax.swing.JCheckBox();
    fileNamingLabel = new javax.swing.JLabel();
    nameByNumberButton = new javax.swing.JRadioButton();
    nameByTitleButton = new javax.swing.JRadioButton();
    titleLabel = new javax.swing.JLabel();
    titleText = new javax.swing.JTextField();
    linkLabel = new javax.swing.JLabel();
    linkText = new javax.swing.JTextField();
    pathLabel = new javax.swing.JLabel();
    pathText = new javax.swing.JTextField();
    descriptionLabel = new javax.swing.JLabel();
    descriptionScrollPane = new javax.swing.JScrollPane();
    descriptionText = new javax.swing.JTextArea();
    editorLabel = new javax.swing.JLabel();
    editorText = new javax.swing.JTextField();
    primaryFileLabel = new javax.swing.JLabel();
    primaryFileCheckBox = new javax.swing.JCheckBox();
    forgetFileLabel = new javax.swing.JLabel();
    forgetFileCheckBox = new javax.swing.JCheckBox();
    filler = new javax.swing.JLabel();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());

    fileNameLabel.setText("Folder:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
    getContentPane().add(fileNameLabel, gridBagConstraints);

    fileNameText.setText(" ");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
    getContentPane().add(fileNameText, gridBagConstraints);

    storageFormatLabel.setText("Format:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(storageFormatLabel, gridBagConstraints);

    storageFormatComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "XML", "Text - Structured" }));
    storageFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        storageFormatComboBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(storageFormatComboBox, gridBagConstraints);

    organizeWithinFoldersLabel.setText("Organize within Folders?");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
    getContentPane().add(organizeWithinFoldersLabel, gridBagConstraints);

    organizeWithinFoldersCheckBox.setSelected(true);
    organizeWithinFoldersCheckBox.setText("Folders");
    organizeWithinFoldersCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        organizeWithinFoldersCheckBoxActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(organizeWithinFoldersCheckBox, gridBagConstraints);

    fileNamingLabel.setText("Wisdom File Naming:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(fileNamingLabel, gridBagConstraints);

    fileNameButtonGroup.add(nameByNumberButton);
    nameByNumberButton.setText("Name by Item Number");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(nameByNumberButton, gridBagConstraints);

    fileNameButtonGroup.add(nameByTitleButton);
    nameByTitleButton.setSelected(true);
    nameByTitleButton.setText("Name by Title");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(nameByTitleButton, gridBagConstraints);

    titleLabel.setText("Title:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(titleLabel, gridBagConstraints);

    titleText.setToolTipText("Title of this Wisdom collection.");
    titleText.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        titleTextActionPerformed(evt);
      }
    });
    titleText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        titleTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(titleText, gridBagConstraints);

    linkLabel.setText("Link:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(linkLabel, gridBagConstraints);

    linkText.setText("http://");
    linkText.setToolTipText("The URL of the Web site on which this collection will be published");
    linkText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        linkTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(linkText, gridBagConstraints);

    pathLabel.setText("Path:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(pathLabel, gridBagConstraints);

    pathText.setToolTipText("The path from the URL above to the Wisdom folder");
    pathText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        pathTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(pathText, gridBagConstraints);

    descriptionLabel.setText("Description:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(descriptionLabel, gridBagConstraints);

    descriptionText.setColumns(20);
    descriptionText.setLineWrap(true);
    descriptionText.setRows(5);
    descriptionText.setWrapStyleWord(true);
    descriptionText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        descriptionTextFocusLost(evt);
      }
    });
    descriptionScrollPane.setViewportView(descriptionText);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(descriptionScrollPane, gridBagConstraints);

    editorLabel.setText("Editor:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(editorLabel, gridBagConstraints);

    editorText.setToolTipText("Editor's E-mail address");
    editorText.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        editorTextFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(editorText, gridBagConstraints);

    primaryFileLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    primaryFileLabel.setText("Primary?");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(primaryFileLabel, gridBagConstraints);

    primaryFileCheckBox.setText("Make this my Primary Collection");
    primaryFileCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        primaryFileCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(primaryFileCheckBox, gridBagConstraints);

    forgetFileLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    forgetFileLabel.setText("Forget?");
    forgetFileLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 11;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.weightx = 0.1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 4);
    getContentPane().add(forgetFileLabel, gridBagConstraints);

    forgetFileCheckBox.setText("Forget About This Collection");
    forgetFileCheckBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        forgetFileCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 11;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 4);
    getContentPane().add(forgetFileCheckBox, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 99;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(filler, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void forgetFileCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_forgetFileCheckBoxItemStateChanged
    if (evt.getStateChange() == ItemEvent.SELECTED) {
      if (primaryFileCheckBox.isSelected()) {
        td.trouble.report(
            "Please select another primary file before forgetting about this one",
            "Primary File Problem");
        forgetFileCheckBox.setSelected(false);
      } else {
        td.diskStore.setForgettable(true);
        td.files.forget(td.diskStore);
      }
    } else {
      td.diskStore.setForgettable(false);
    }
  }//GEN-LAST:event_forgetFileCheckBoxItemStateChanged

  private void primaryFileCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_primaryFileCheckBoxItemStateChanged
    if (evt.getStateChange() == ItemEvent.SELECTED) {
      if (! td.diskStore.isPrimary()) {
        if (td.diskStore.isAFolder()) {
          td.setPrimary(true);
        } else {
          td.trouble.report(
              "You must specify a folder rather than a file to make it your primary storage location",
              "Primary File Problem");
          primaryFileCheckBox.setSelected(false);
        } // end if current disk store ineligible to become primary
      } // end if current disk store is not yet primary
    } else {
      // Primary Check Box set to false
      if (td.diskStore.isPrimary()) {
        td.trouble.report(
            "Please select another primary file to make this one non-primary",
            "Primary File Problem");
        primaryFileCheckBox.setSelected(true);
      } // end if the current disk store is set to be the primary
    } // end if the primary check box has been unchecked
  }//GEN-LAST:event_primaryFileCheckBoxItemStateChanged

  private void editorTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_editorTextFocusLost
    saveCollectionHeader();
  }//GEN-LAST:event_editorTextFocusLost

  private void descriptionTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionTextFocusLost
    saveCollectionHeader();
  }//GEN-LAST:event_descriptionTextFocusLost

  private void pathTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pathTextFocusLost
    saveCollectionHeader();
  }//GEN-LAST:event_pathTextFocusLost

  private void linkTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_linkTextFocusLost
    saveCollectionHeader();
  }//GEN-LAST:event_linkTextFocusLost

  private void titleTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_titleTextFocusLost
    saveCollectionHeader();
  }//GEN-LAST:event_titleTextFocusLost

  private void titleTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleTextActionPerformed
// TODO add your handling code here:
  }//GEN-LAST:event_titleTextActionPerformed

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden

  String newStorageFormat = storageFormatComboBox.getSelectedItem().toString();
  boolean newOrganizeWithinFolders = organizeWithinFoldersCheckBox.isSelected();
  boolean newNameByTitle = nameByTitleButton.isSelected();
  if (storageFormat.equalsIgnoreCase(newStorageFormat)
      && organizeWithinFolders == newOrganizeWithinFolders
      && nameByTitle == newNameByTitle) {
    // None of the storage options changed -- nothing needs to be done
  } else {
    storageFormat = newStorageFormat;
    organizeWithinFolders = newOrganizeWithinFolders;
    nameByTitle = newNameByTitle;
    if (organizeWithinFolders) {
      File formatFolder = FileUtils.ensureFolder
          (td.getDiskStore().getFile(), getStorageFormat().toLowerCase());
      FileUtils.ensureFolder(formatFolder, "authors");
    }
    saveCollectionHeader();
    td.rewrite();
  }
  WindowMenuManager.getShared().hideAndRemove(this);
}//GEN-LAST:event_formComponentHidden

private void storageFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storageFormatComboBoxActionPerformed

}//GEN-LAST:event_storageFormatComboBoxActionPerformed

private void organizeWithinFoldersCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_organizeWithinFoldersCheckBoxActionPerformed

}//GEN-LAST:event_organizeWithinFoldersCheckBoxActionPerformed
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel descriptionLabel;
  private javax.swing.JScrollPane descriptionScrollPane;
  private javax.swing.JTextArea descriptionText;
  private javax.swing.JLabel editorLabel;
  private javax.swing.JTextField editorText;
  private javax.swing.ButtonGroup fileNameButtonGroup;
  private javax.swing.JLabel fileNameLabel;
  private javax.swing.JLabel fileNameText;
  private javax.swing.JLabel fileNamingLabel;
  private javax.swing.JLabel filler;
  private javax.swing.JCheckBox forgetFileCheckBox;
  private javax.swing.JLabel forgetFileLabel;
  private javax.swing.JLabel linkLabel;
  private javax.swing.JTextField linkText;
  private javax.swing.JRadioButton nameByNumberButton;
  private javax.swing.JRadioButton nameByTitleButton;
  private javax.swing.JCheckBox organizeWithinFoldersCheckBox;
  private javax.swing.JLabel organizeWithinFoldersLabel;
  private javax.swing.JLabel pathLabel;
  private javax.swing.JTextField pathText;
  private javax.swing.JCheckBox primaryFileCheckBox;
  private javax.swing.JLabel primaryFileLabel;
  private javax.swing.JComboBox storageFormatComboBox;
  private javax.swing.JLabel storageFormatLabel;
  private javax.swing.JLabel titleLabel;
  private javax.swing.JTextField titleText;
  // End of variables declaration//GEN-END:variables
  
}
