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

package com.powersurgepub.iwisdom.disk;

  import com.powersurgepub.psdatalib.txbio.strtext.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.pspub.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;

/**
   A parent class for an iWisdom collection.
 */
public class WisdomDiskStore {
  
  public static final String  FILE                        = "file";
  public static final String  PRIMARY                     = "primary";
  public static final String  TEMPLATE                    = "template";
  public static final String  PUBLISH_WHEN                = "publishwhen";
  
  public static final String  VIEW_INDEX                  = "viewindex";
  
  public static final String  FILE_EXT                    = "tdu";
  public static final String  FILE_EXT_OLD                = "txt";
  public static final String  NEW_EXT                     = "tmp";
  public static final String  BACKUP_EXT                  = "bak";
  
  public static final String  TO_DO_FILE_NAME             = "twodue";
  public static final String  TEMPLATE_NAME               = "template.html";
  public static final String  DISK_DIR_NAME               = "files.txt";
  public static final String  DISK_VIEWS_NAME             = "views.txt";
  public static final String  ARCHIVE_FILE_NAME           = "archive";
  public static final String  ARCHIVE_TEMPLATE_FILE_NAME  = "archive_template.html";
  public static final String  UPDATE_LOG_FOLDER_NAME      = "updatelogs";
  public static final String  AUTHORS                     = "authors";
  public static final String  XSLT_FOLDER_NAME            = "xslt";
  public static final String  STYLES_FOLDER_NAME          = "styles";
  public static final String  JAVASCRIPT_FOLDER_NAME      = "javascript";
  public static final String  XSL_SCRIPT_NAME             = "xsl_script.xml";
  
  /** The location of this disk store. */
  protected File            file;
  
  /** Is this the primary disk store for the current user? */
  private boolean           primary = false;
  
  /** Should we forget about this file? */
  protected boolean         forgettable = false;
  
  /** The location of the xml folder. */
  protected File            xmlFolder;
  
  /** The location of the html folder. */
  protected File            htmlFolder;
  
  /** The location of the xml/authors folder. */
  protected File            authorsFolder;
  
  /** The location of the backups folder. */
  protected File            backupsFolder;
  
  /** The location of the export folder. */
  protected File            exportFolder;
  
  /** The location of the disk directory. */
  protected File            diskDirFile;
  
  /** The tab-delimited representation of the disk directory. */
  protected TabDelimFile    diskDirTDF;
  
  /** The location of the views stored on disk. */
  protected File            diskViewsFile;
  
  /** The tab-delimited representation of the list of views. */
  protected TabDelimFile    diskViewsTDF;
  
  /** Indicates whether this to do file has been opened. */
  protected boolean         fileNowOpen = false;
  
  /** A Web Publishing template for these to do items. */
  protected File            templateFile;
  
  /** A sub-folder for update logs. */
  protected File            updateLogFolder;
  
  // protected Template        template;
  
  /** The selection and sequence settings for this file. */
  protected ItemComparator  comp = new ItemComparator();
  
  /** A pointer to the selection and sequence settings for this file. */
  protected int             viewIndex = 0;
  
  /** An indicator of how often the file should be published to the Web. */
  protected int             publishWhen = 0;
  
  public static final int     PUBLISH_ON_CLOSE = 1;
  public static final int     PUBLISH_ON_SAVE  = 2;
  
  protected WisdomXMLIO     xml = new WisdomXMLIO();
  
  private   WisdomIO        wisdomIO = new WisdomIO();
  
  protected CollectionWindow header;
  
  protected Logger          log = new Logger();
  
  protected XOS             xos = XOS.getShared();
  
  protected int             folderDepth = 0;
  protected int             maxFolderDepth = 0;
  protected int             xmlCount = 0;
  protected int             txtCount = 0;
  protected int             maxFileNameLength = 0;
  protected int             filesFound = 0;
  
  /**
   * 
   *    Creates a new instance of WisdomDiskStore.
   *   
   *    @param file The location of the iWisdom folder. 
   */
  public WisdomDiskStore (File file) {

    this.file = file;
    if (file != null) {
      xmlFolder = new File (file, "xml");
      htmlFolder = new File (file, "html");
      authorsFolder = new File (xmlFolder, AUTHORS);
      backupsFolder = new File (file, "backups");
      exportFolder = new File (file, "export");
      templateFile = new File (file, TEMPLATE_NAME);
      diskDirFile = new File (file, DISK_DIR_NAME);
      if (diskDirFile != null) {
        diskDirTDF = new TabDelimFile (diskDirFile);
        diskDirTDF.setLog (log);
      }
      diskViewsFile = new File (file, DISK_VIEWS_NAME);
      if (diskViewsFile != null) {
        diskViewsTDF = new TabDelimFile (diskViewsFile);
        diskViewsTDF.setLog (log);
      }
      updateLogFolder = new File (file, UPDATE_LOG_FOLDER_NAME);
      // initFolders();
    } // end if parameter not null
  } // end constructor
  
  /**
    Indicates whether this is an instance of TwoDueUnknown.
   
    @return true if this is an instance of TwoDueUnknown.
   */
  public boolean isUnknown() {
    return false;
  }
  
  /**
   *    Indicates whether this is an instance of TwoDueFile or WisdomFolder.
   *   
   *    @return true if this is an instance of TwoDueFile.
   */
  public boolean isAFile() {
    return false;
  }
  
  /**
   *    Indicates whether this is an instance of TwoDueFile or WisdomFolder.
   *   
   *    @return true if this is an instance of WisdomFolder.
   */
  public boolean isAFolder() {
    return (file != null);
  }
  
  public File getHTMLFolder () {
    return htmlFolder;
  }
  
  public File getBackupsFolder () {
    return backupsFolder;
  }
  
  public File getExportFolder () {
    return exportFolder;
  }
  
  public boolean open() {
    boolean ok = isValid();
    
    if (ok) {
      ok = FileUtils.ensureFolder (backupsFolder);
      ok = FileUtils.ensureFolder (exportFolder);
      if (ok) {
        setFileNowOpen (true);
      } 
    }
    return ok;
  }
  
  public boolean close() {
    setFileNowOpen(false);
    return true;
  }
  
  /**
    Return an abbreviated path to the disk store, to visually
    identify it to the user. 
   
    @return A string identifying the disk store to the user. 
   */
  public String getShortPath() {
    FileName name = new FileName (file, FileName.DIR_TYPE);
    StringBuilder shortPath = new StringBuilder();
    int numberOfFolders = name.getNumberOfFolders();
    int i = numberOfFolders - 1;
    if (i < 0) {
      i = 0;
    }
    while (i <= numberOfFolders) {
      if (shortPath.length() > 0) {
        shortPath.append ('/');
      }
      shortPath.append (name.getFolder (i));
      i++;
    }
    if (isPrimary()) {
      shortPath.append (" (P)");
    }
    return (shortPath.toString()); 
  }
  
  /**
    Uses a series of xslt template files to attempt to publish 
    the supplied data source as a series of web pages.
   
    @return true if everything went ok, or false if there were any problems.
   
    @param source The data to be published, using the template
                  previously identified. 
   */
  public boolean publish (CollectionWindow collectionWindow, SortedItems items) {
    
    boolean ok = true;
    
    // Create a consolidated xml file sorted by author last name
    // xml/authors/authors.xml
    // File xmlFolder = new File (file, "xml");
    // File htmlFolder = new File (file, "html");
    File jsFolder = new File (file, JAVASCRIPT_FOLDER_NAME);
    File xmlAuthors = new File (xmlFolder, "authors.xml");
    File htmlAuthorsFolder = new File (htmlFolder, AUTHORS);
    File htmlAuthors = new File (htmlAuthorsFolder, "index.html");
    ItemComparator saveComp = items.getComparator();
    ItemComparator authorComp = new ItemComparator();
    authorComp.setItems (items.getItems());
    authorComp.setSortFields (
        ItemSelector.SHOW_ALL_STR, 
        WisdomItem.COLUMN_DISPLAY [WisdomItem.AUTHOR_INDEX],      ItemComparator.ASCENDING, 
        WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_TYPE_INDEX], ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_INDEX],      ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.TITLE_INDEX],       ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.RATING_INDEX],    ItemComparator.ASCENDING,
        ItemComparator.LOW
        );
    items.setComparator (authorComp);
    items.sort();
    TextLineWriter writer = new FileMaker (xmlAuthors);
    MarkupWriter markupWriter = new MarkupWriter (writer, MarkupWriter.XML_FORMAT);
    ok = xml.save (markupWriter, collectionWindow, items);
    log.recordEvent (LogEvent.NORMAL, "Publish", false);
    log.recordEvent (LogEvent.NORMAL, "  Created file " + xmlAuthors.toString(), false);
    items.setComparator (saveComp);
    items.sort();
    items.fireTableDataChanged();
    
    // initFolders();
    
    File xsltFolder = new File (file, "xslt");

    
    // Process XSL Script File
    XMLTransformer tf = new XMLTransformer();
    
    File xslScript = new File (xsltFolder, XSL_SCRIPT_NAME);
    if (ok) {
      ok = tf.transform (xslScript, file);
      if (ok) {
        log.recordEvent (LogEvent.NORMAL, 
            "  xslScript processed " + xslScript.toString(), false);
      } else {
        log.recordEvent (LogEvent.MINOR, 
            "  xslScript error processing " + xslScript.toString(), false);
      }
    }
    
    return ok;
  }

  public boolean prePub(
      File publishTo,
      CollectionWindow collectionWindow,
      SortedItems items) {

    boolean ok = true;

    // Create a consolidated xml file sorted by author last name
    File dataFolder = new File (publishTo, "data");
    File xmlAuthors = new File (dataFolder, "authors.xml");
    ItemComparator saveComp = items.getComparator();
    ItemComparator authorComp = new ItemComparator();
    authorComp.setItems (items.getItems());
    authorComp.setSortFields (
        ItemSelector.SHOW_ALL_STR,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.AUTHOR_INDEX],      ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_TYPE_INDEX], ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_INDEX],      ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.TITLE_INDEX],       ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.RATING_INDEX],    ItemComparator.ASCENDING,
        ItemComparator.LOW
        );
    items.setComparator (authorComp);
    items.sort();
    TextLineWriter writer = new FileMaker (xmlAuthors);
    MarkupWriter markupWriter = new MarkupWriter (writer, MarkupWriter.XML_FORMAT);
    ok = xml.save (markupWriter, collectionWindow, items);
    log.recordEvent (LogEvent.NORMAL, "Publish", false);
    log.recordEvent (LogEvent.NORMAL, "  Created file " + xmlAuthors.toString(), false);
    items.setComparator (saveComp);
    items.sort();
    items.fireTableDataChanged();

    return ok;
  }

  public boolean postPub(File publishTo) {
    boolean ok = true;
    return ok;

  }
  
  public boolean themeFoldersExist () {
    File wisdomSubFolder = new File (file, XSLT_FOLDER_NAME);
    return (wisdomSubFolder.exists());
  }
  
  public void applyDefaultTheme () {
    File themes = new File (getAppFolder(), "themes");
    File defaultTheme = new File (themes, "portablewisdom1");
    applyTheme (defaultTheme);
  }
  
  private void applyTheme (File theme) {
    populateFolder (theme, JAVASCRIPT_FOLDER_NAME);
    populateFolder (theme, STYLES_FOLDER_NAME);
    populateFolder (theme, XSLT_FOLDER_NAME);
  }
  
  /**
   Initialize a sub folder in the Wisdom folder by copying
   over files from the corresponding sub folder in the Application folder.
   */
  public void populateFolder (File theme, String folderName) {
    
    File themeSubFolder = new File (theme, folderName);
    File wisdomSubFolder = new File (file, folderName);

    FileUtils.deleteFolderContents (wisdomSubFolder);
    FileUtils.copyFolder (themeSubFolder, wisdomSubFolder);
  }
  
  private File getAppFolder () {
    Home home = Home.getShared();
    return (home.getAppFolder());
  }
  
  /**
    Returns a record definition for a TwoDue disk store, 
    in com.powersurgepub.psdata.RecordDefinition format.
   
    @return A record format definition for a iWisdom disk store.
   */
  public static RecordDefinition getRecDef() {
    RecordDefinition recDef = new RecordDefinition();
    recDef.addColumn (FILE);
    recDef.addColumn (PRIMARY);
    ItemComparator.addColumnNames (recDef);
    recDef.addColumn (TEMPLATE);
    recDef.addColumn (PUBLISH_WHEN);
    recDef.addColumn (VIEW_INDEX);
    return recDef;
  }
  
  /**
    Sets multiple fields based on contents of passed record.
  
    @param  storeRec  A data record containing multiple fields.
   */
  public void setMultiple (DataRecord storeRec) {
    
    if (! isPrimary()) {
      setPrimary (storeRec.getFieldAsBoolean (PRIMARY));
    }
    comp.setSortFields (
        storeRec.getFieldData (ItemComparator.SHOW),
        storeRec.getFieldData (ItemComparator.FIELD1),
        storeRec.getFieldData (ItemComparator.SEQ1),
        storeRec.getFieldData (ItemComparator.FIELD2),
        storeRec.getFieldData (ItemComparator.SEQ2),
        storeRec.getFieldData (ItemComparator.FIELD3),
        storeRec.getFieldData (ItemComparator.SEQ3),
        storeRec.getFieldData (ItemComparator.FIELD4),
        storeRec.getFieldData (ItemComparator.SEQ4),
        storeRec.getFieldData (ItemComparator.FIELD5),
        storeRec.getFieldData (ItemComparator.SEQ5),
        storeRec.getFieldData (ItemComparator.UNDATED)
      );
    String viewIndexString = storeRec.getFieldData (VIEW_INDEX).trim();
    if (viewIndexString != null
        && viewIndexString.length() > 0) {
      setViewIndex (viewIndexString);
    }
    setTemplate (new File (storeRec.getFieldData (TEMPLATE)));
    setPublishWhen (Integer.parseInt (storeRec.getFieldData (PUBLISH_WHEN)));

  } // end method
  
  /**
    Return this object, formatted as a DataRecord.
   
    @param recDef Record Definition to be used in building the record. 
    */
  public DataRecord getDataRec (RecordDefinition recDef) {
    // Create a date formatter
    DataRecord nextRec = new DataRecord();
    nextRec.addField (recDef, getFile().toString());
    nextRec.addField (recDef, String.valueOf (isPrimary ()));
    nextRec.addField (recDef, comp.getSelectString());
    for (int i = 0; i < 5; i++) {
      nextRec.addField (recDef, comp.getSortField (i));
      nextRec.addField (recDef, comp.getSortSeq (i));
    }
    nextRec.addField (recDef, comp.getUndatedString());
    nextRec.addField (recDef, getTemplateString());
    nextRec.addField (recDef, String.valueOf (getPublishWhen ()));
    nextRec.addField (recDef, getViewIndexAsString());
    return nextRec;
  }
  
  public boolean isInvalid () {
    return (! isValid());
  }
  
  public boolean isValid() {
    boolean ok = false;
    if (file == null) {
      ok = false;
    } else {
      ok = isAFolder();
      if (ok) {
        if (file.exists() && file.canRead()) {
          // still ok
        } else {
          ok = false;
        }
      }
    }
    return ok;
  }
  
  public File getFile() {
    return file;
  }
  
  public void setPrimary (String primary) {
    setPrimary (Boolean.getBoolean (primary));
  }
  
  public void setPrimary (boolean primary) {
    this.primary = primary;
  }
  
  public boolean isPrimary () {
    return primary;
  }
  
  public void setForgettable (boolean forgettable) {
    this.forgettable = forgettable;
  }
  
  public boolean isForgettable () {
    return forgettable;
  }
  
  /**
    Return full path to file or folder.
   
    @return Path to file or folder.
   */
  public String getPath () {
    if (isUnknown()) {
      return ("???");
    } else {
      return file.getPath();
    }
  }
  
  public boolean isWisdomFileValidInput () {
    return ((! isUnknown())
        && file.exists()
        && file.isDirectory()
        && file.canRead());
  }
  
  public boolean isWisdomFileValidOutput () {
    return ((! isUnknown())
        && file.exists()
        && file.isDirectory()
        && file.canWrite());
  }
  
  /**
    Returns the file of To Do Items.
   
    @return The file of To Do Items.
   */
  public File getAuthorsFolder() {
    return authorsFolder;
  }
  
  public WisdomXMLIO getXMLIO () {
    return xml;
  }
  
  public boolean isDiskDirFileValidInput () {
    return (diskDirFile != null
        && (isAFolder())
        && diskDirFile.exists()
        && diskDirFile.isFile()
        && diskDirFile.canRead());
  }
  
  public File getDiskDirFile () {
    return diskDirFile;
  }
  
  /**
    Returns the tab-delimited file of disk stores.
   
    @return The tab-delimited file of disk stores.
   */
  public TabDelimFile getDiskDirTDF() {
    return diskDirTDF;
  }
  
  public boolean isDiskViewsFileValidInput () {
    return (diskViewsFile != null
        && (isAFolder())
        && diskViewsFile.exists()
        && diskViewsFile.isFile()
        && diskViewsFile.canRead());
  }
  
  /**
    Returns the tab-delimited file of disk stores.
   
    @return The tab-delimited file of disk stores.
   */
  public TabDelimFile getDiskViewsTDF() {
    return diskViewsTDF;
  }
  
  public void setLog (Logger log) {
    this.log = log;
  }
  
  /**
    Populate a WisdomItems collection from this disk store. 

    @param collectionWindow The window used to store metadata about the 
                            collection.
    @param items            The WisdomItems list to be populated.
    @throws IOException     If we can't find a disk store. 
  */
  public boolean populate (
      CollectionWindow collectionWindow, 
      WisdomItems items) 
        throws IOException {

    boolean ok = false;
    if (! isWisdomFileValidInput()) {
      throw new IOException ("File not found");
    }
   
    xml.loadCollectionHeader(this, collectionWindow);
      
    ok = true;
    folderDepth = 0;
    maxFolderDepth = 0;
    xmlCount = 0;
    txtCount = 0;
    maxFileNameLength = 0;
    filesFound = 0;
    
    File folder = file;
    
    if (! folder.exists()) {
      ok = false;
      log.recordEvent (LogEvent.MEDIUM, 
          "Wisdom folder " + folder.toString() + " cannot be found",
          false);
    }

    if (ok) {
      if (! folder.canRead()) {
        ok = false;
        log.recordEvent (LogEvent.MEDIUM, 
            "Wisdom folder " + folder.toString() + " cannot be read",
            false);       
      }
    }

    if (ok) {
      if (folder.isDirectory()) {
        if (header != null
          && header.isOrganizeWithinFolders()) {
        ok = FileUtils.ensureFolder(xmlFolder);
        ok = FileUtils.ensureFolder(authorsFolder);
      }
        populateFromFolder(collectionWindow, items, folder);
      } else {
        ok = false;
        log.recordEvent (LogEvent.MEDIUM, 
            "Wisdom folder " + folder.toString() + " is not a folder",
            false);       
      }
    } // end if everything still OK
    
    setFileNowOpen (true);
    if (filesFound > 0) {
      collectionWindow.setFileNamingByTitle(maxFileNameLength > 6);
      collectionWindow.setOrganizeWithinFolders(maxFolderDepth > 1);
      String storageFormat = "";
      if (xmlCount > 0) {
        storageFormat = CollectionWindow.XML;
      }
      else
      if (txtCount > 0) {
        storageFormat = CollectionWindow.STRUCTURED_TEXT;
      }
      collectionWindow.setStorageFormat(storageFormat);
    }
    
    return ok;

  }
  
  /**
    Populate a WisdomItems folder. Note that this method calls itself
    recursively in order to process nested folders. 
  
    @param  collectionWindow The window used to store metadata about the 
                             collection.
    @param  items            The WisdomItems list to be populated.
    @param  folder           The folder to be processed. 
    @return True if successful, false otherwise. 
  */
  private boolean populateFromFolder (
      CollectionWindow collectionWindow, 
      WisdomItems items, 
      File folder) {

    boolean folderPopulateOK = true;
    if (folder.exists()
        && folder.isDirectory()
        && folder.canRead()) {
      DirectoryReader directoryReader = new DirectoryReader (folder);
      directoryReader.setLog (log);
      try {
        directoryReader.openForInput();
        while (! directoryReader.isAtEnd()) {
          File nextFile = directoryReader.nextFileIn();
          if ((nextFile != null) 
              && (! nextFile.getName().startsWith ("."))
              && (nextFile.exists())
              && (nextFile.canRead())) {
            if (nextFile.isDirectory()) {
              if (nextFile.getName().equalsIgnoreCase("backups")
                  || nextFile.getName().equalsIgnoreCase("export")
                  || nextFile.getName().equalsIgnoreCase("html")
                  || nextFile.getName().equalsIgnoreCase("id")
                  || nextFile.getName().equalsIgnoreCase("mobile")) {
                // ignore these utility folders
              } else {
                folderDepth++;
                populateFromFolder(collectionWindow, items, nextFile);
                folderDepth--;
              }
            }
            else
            if (nextFile.isFile()) {
              if (nextFile.getName().equalsIgnoreCase("files.txt")
                  || nextFile.getName().equalsIgnoreCase("header.xml")
                  || nextFile.getName().equalsIgnoreCase("iwisdom_sysprefs.xml")
                  || nextFile.getName().equalsIgnoreCase("iwisdom_userprefs.xml")
                  || nextFile.getName().equalsIgnoreCase("pspub_source_parms.xml")
                  || nextFile.getName().equalsIgnoreCase("views.txt")
                  || nextFile.getName().equalsIgnoreCase("authors.xml")) {
                // ignore these utility files
              } else {
                filesFound++;
                FileName fileName = new FileName (nextFile);
                int fileNameLength = fileName.getBase().length();
                if (fileNameLength > maxFileNameLength) {
                  maxFileNameLength = fileNameLength;
                }
                if (folderDepth > maxFolderDepth) {
                  maxFolderDepth = folderDepth;
                }
                String fileExtension = fileName.getExt();
                if ((fileExtension.trim().length() < 1)
                    || (fileName.getExt().equalsIgnoreCase 
                          (WisdomXMLIO.WISDOM_FILE_EXTENSION))) {
                  xml.parseXMLFile (collectionWindow, items, nextFile, true);
                  xmlCount++;
                } // end if file extension is ok
                else
                if (fileName.getExt().equalsIgnoreCase
                    (TextIOStrText.PREFERRED_FILE_EXTENSION)) {
                  TextIOStrText strText = new TextIOStrText(nextFile);
                  strText.setStreamTag(WisdomItem.WISDOM);
                  strText.setDocumentTag(WisdomItem.ITEM);
                  strText.setMarkupTag(WisdomItem.BODY);
                  strText.setUseMarkdown(true);
                  wisdomIO.setCollectionWindow(collectionWindow);
                  wisdomIO.setItems(items);
                  wisdomIO.setStore(this);
                  wisdomIO.setSource(nextFile);
                  wisdomIO.setImporting(false);
                  strText.parse(wisdomIO);
                  txtCount++;
                }
              } // end if file name is not a utility used for some other purpose
            } // end if this is a file
          } // end if file exists, can be read, etc.
        } // end while more files in specified folder
      } catch (IOException ioe) {
        folderPopulateOK = false;
        log.recordEvent (LogEvent.MEDIUM, 
            "Encountered I/O error while reading Wisdom entries from " 
            + folder.toString(),
            false);     
      } // end if caught I/O Error
      directoryReader.close();
    } else {
      folderPopulateOK = false;
    }
    return folderPopulateOK;
  }
  
  /*
  private void load(CollectionWindow collectionWindow, WisdomItems items) {
    xml.load (this, collectionWindow, items);
  } */
  
  public void importXML (
      String importName, 
      CollectionWindow collectionWindow, 
      WisdomItems items) {
    xml.importFile (importName, collectionWindow, items);
  }
  
  public void importStrText (
      String importName, 
      CollectionWindow collectionWindow, 
      WisdomItems items) {
    
    TextIOStrText strText = new TextIOStrText(importName);
    strText.setStreamTag(WisdomItem.WISDOM);
    strText.setDocumentTag(WisdomItem.ITEM);
    strText.setMarkupTag(WisdomItem.BODY);
    strText.setUseMarkdown(true);
    wisdomIO.setCollectionWindow(collectionWindow);
    wisdomIO.setItems(items);
    wisdomIO.setStore(this);
    wisdomIO.setSource(null);
    wisdomIO.setImporting(true);
    try {
      strText.parse(wisdomIO);
    } catch (IOException e) {
      Trouble.getShared().report(
          "Trouble importing from " + importName, 
          "Import Error");
    }
  }
  
  public void save (WisdomItems items)
      throws IOException {
    /*
    items.getAll (getNewToDoTDF());
      toDoFile = backupRename (getToDoFile());
      XOS xos = XOS.getShared();
      boolean typeOK = xos.setFileTypeText (getToDoFile());
      boolean creatorOK = xos.setFileCreatorFromString (file, TWO_DUE_FILE_CREATOR);
      constructToDoTDF();
      */
    setFileNowOpen (true);
  }
  
  /**
   Save one item to disk. 
  
   @param item The item to be saved.
   @param olderPublicationStyle
   @return 
   */
  public boolean save (WisdomItem item) {
    
    boolean ok = true;
    
    boolean olderPublicationStyle = UserPrefs.getShared().getPrefAsBoolean(
        GeneralPrefs.OLDER_PUBLICATION_STYLE, false);
    
    // Save information about old storage location
    String oldFilePathNameAndExt = item.getFilePathNameAndExt();
    File oldWisdomFile = item.getFile(file);
    
    // Now get the new file location
    item.deriveAllFields();
    item.setFileName(header);
    
    // If the file is moving, then delete the old file
    if (oldFilePathNameAndExt.equals("")
        || oldWisdomFile == null
        || oldFilePathNameAndExt.equals(item.getFilePathNameAndExt())
        || (! oldWisdomFile.exists())) {
      // nothing to delete
    } else {
      FileUtils.deleteFileAndEmptyParents(oldWisdomFile);
    } // end if old file exists
    
    // Now save the item
    
    // Make sure xml folder exists
    if (header.isOrganizeWithinFolders()) {
      File formatFolder = FileUtils.ensureFolder
          (file, header.getStorageFormatExt());
      File authorsFolder = FileUtils.ensureFolder(formatFolder, "authors");
      File authorFolder = FileUtils.ensureFolder (authorsFolder, item.getAuthorFileName());
      File sourceFolder = FileUtils.ensureFolder (authorFolder,  item.getSourceFileName());
      if (sourceFolder == null) {
        ok = false;
      }
    }
    
    MarkupWriter markupWriter = new MarkupWriter();
    int markupFormat = MarkupWriter.XML_FORMAT;
    if (header.getStorageFormat().equals(header.STRUCTURED_TEXT)) {
      markupFormat = MarkupWriter.STRUCTURED_TEXT_FORMAT;
    }
    markupWriter.setMarkupFormat(markupFormat);
    if (ok) {
      WisdomItem.tailorMarkupWriter(markupWriter);
      item.writeMarkup(markupWriter, file, true, true);
    }
    
    if (ok && olderPublicationStyle) {
      ok = xml.saveAsHTML (this, header, item);
    }

    if (ok && olderPublicationStyle) {
      ok = xml.saveAsMobileHTML (this, header, item);
    }
    // xml.save (this, header, item);
    return ok;
  }
  
  public boolean remove (WisdomItem itemToRemove) {
    return FileUtils.deleteFileAndEmptyParents(itemToRemove.getFile(file));
  }
  
  public boolean isFileNowOpen () {
    return fileNowOpen;
  }
  
  protected void setFileNowOpen (boolean fileNowOpen) {
    this.fileNowOpen = fileNowOpen;
    String openOrClosed = "closed";
    if (fileNowOpen) {
      openOrClosed = "open";
    }
    if (isValid()) {
      Logger.getShared().recordEvent(LogEvent.NORMAL, 
          "Disk Store " + getPath() + " now " + openOrClosed, false);
    } else {
      Logger.getShared().recordEvent(LogEvent.NORMAL, 
          "Invalid Disk Store now " + openOrClosed, false);
    }
  }
  
  public boolean templateIsAvailable () {
    return true;
    // return (template != null);
  }
  
  public String getTemplateString () {
    if (templateFile == null) {
      return "";
    } else {
      return templateFile.toString();
    }
  }
  
  public File getTemplate () {
    return templateFile;
  }
  
  public void setTemplate (File templateFile) {
    this.templateFile = templateFile;
  }
  
  /*
  public boolean comparatorIsAvailable () {
    return (comp != null);
  }
   */
  
  public void resetComparator() {
    comp.setSortFields (ItemSelector.SHOW_ALL_STR,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.CATEGORY_INDEX], ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.AUTHOR_INDEX],   ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.RATING_INDEX], ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.TITLE_INDEX],    ItemComparator.ASCENDING,
        WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_INDEX],   ItemComparator.ASCENDING,
        ItemComparator.LOW);
  }
  
  public void setCompareOption (String compareOption) {
    comp.set (compareOption);
  }
  
  public void setComparator (ItemComparator comp) {
    this.comp = comp;
  }
  
  public ItemComparator getComparator () {
    return comp;
  }
  
  public String getCompareString () {
    return comp.toString();
  }
  
  public void setViewIndex (String viewIndexString) {

    try {
      int viewIndex = Integer.parseInt (viewIndexString);
      setViewIndex (viewIndex);
    } catch (NumberFormatException e) {
      System.out.println ("WisdomDiskStore.setViewIndex NumberFormatException:");
      System.out.println ("  input = " + viewIndexString);
    }
  }
  
  /**
    Set the index pointing to the view to be used by this file. 
   
    @param viewIndex Index pointing to the view to be used by this file.
   */
  public void setViewIndex (int viewIndex) {

    this.viewIndex = viewIndex;
  }
  
  public String getViewIndexAsString () {

    return String.valueOf (getViewIndex());
  }
  
  /**
    Get the index pointing to the view to be used by this file.
   
    @return Index pointing to the view to be used by this file.
   */
  public int getViewIndex () {

    return viewIndex;
  }
  
  /*
  public void setComparator (ItemComparator comp) {
    this.comp = comp;
  }
   */
  
  /*
  public void setComparator (String bundle) {
    comp.set (bundle);
  }
   */
  
  public void setSelectOption (String selectOption) {
    comp.setSelectOption (selectOption);
  }
  
  public ItemSelector getSelector () {
    if (comp == null) {
      return null;
    } else {
      return comp.getSelector();
    }
  }
  
  public String getSelectString() {
    return comp.getSelectString();
  }
  
  public int getPublishWhen () {
    return publishWhen;
  }
  
  public void setPublishWhen (int publishWhen) {
    if (publishWhen >= 0
        && publishWhen <= 2) {
      this.publishWhen = publishWhen;
    }
  }
  
  public void setHeader (CollectionWindow header) {
    this.header = header;
  }
  
  public CollectionWindow getHeader () {
    return header;
  }
  
  public File getUpdateLogFolder () {
    return updateLogFolder;
  }
  
  /**
    Two disk stores are considered equal if they point to the same
    location on disk.
   
    @return True if the two disk store instances point to the same file
            or folder as their data source.
   
   */
  public boolean equals (WisdomDiskStore store2) {
    boolean same = (file.equals (store2.getFile()));
    
    return (file.equals (store2.getFile()));
  }
  
  /**
    This disk store is considered equal to the passed File if they both point 
    to the same location on disk.
   
    @return True if this disk store points
    to the same disk location as the passed File.
   
   */
  public boolean equals (File file2) {
    return (file.equals (file2));
  }
  
  /**
  Compares this disk store to a second one. 
  
  @param store2 A second disk store to be compared to this one. 
  
  @return a negative integer indicates that this store is less than the 
          specified store; zero indicates that this store is equal to the 
          specified store; a positive integer indicates that this store is 
          greater than the specified store, or that the specified store
          is null.
  */
  public int compareTo (WisdomDiskStore store2) {
    if (store2 == null) {
      return 1;
    } else {
      return file.compareTo (store2.getFile());
    }
  }
  
  public String toString() {
    if (file == null) {
      return "??? (Unknown)";
    } else {
      return file.toString();
    }
  }
  
  public void display(String header) {
    System.out.println (header);
    System.out.println ("WisdomDiskStore " + toString());
    if (isUnknown()) {
      System.out.println ("  Disk location unknown");
    }
    if (isAFile()) {
      System.out.println ("  Disk location is a single file");
    }
    if (isAFolder()) {
      System.out.println ("  Disk location is a folder");
    }
  }
  
}
