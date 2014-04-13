/*
 * Copyright 2003 - 2014 Herb Bowie
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

  import com.powersurgepub.pstextio.*;
  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.pspub.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.awt.*;
  import java.awt.datatransfer.*;
  import java.awt.event.*;
  import java.io.*;
  import java.net.*;
  import javax.swing.*;
  import java.text.*;
  import java.util.*;

/**
   An object accessible to most classes within the iwisdom package,
   that intentionally exposes its data for direct access. 
 */
public class iWisdomCommon 
    implements 
      ActionListener,
      AppToBackup,
      ChainLink, 
      ClipboardOwner,
      FileSpecOpener,
      PublishAssistant {
  
  public static final String DONATE_PAGE 
      = "http://www.powersurgepub.com/donations.html";
  
  static final int LIST_TAB_INDEX     = 0;
  static final int TREE_TAB_INDEX     = 1;
  static final int DISPLAY_TAB_INDEX   = 0;
  static final int CONTENT_TAB_INDEX  = 1;
  static final int AUTHOR_TAB_INDEX   = 2;
  static final int WORK_TAB_INDEX     = 3;
  
  static final String LAST_FILE       = "last.file";
  
  static final String SELECT_OPTION = "selectoption";
  static final String SORT_FIELD_1  = "sortfield1";
  static final String SORT_FIELD_2  = "sortfield2";
  static final String SORT_FIELD_3  = "sortfield3";
  static final String SORT_FIELD_4  = "sortfield4";
  static final String SORT_FIELD_5  = "sortfield5";
  
  public static final int VIEW_TRIGGER_PROGRAM_START              = 0;
  public static final int VIEW_TRIGGER_NEW_FILE                   = 1;
  public static final int VIEW_TRIGGER_SELECT_FILE                = 2;
  public static final int VIEW_TRIGGER_LEAVING_VIEW_TAB           = 3;
  public static final int VIEW_TRIGGER_SELECT_VIEW_FROM_MENU      = 4;
  public static final int VIEW_TRIGGER_SELECT_VIEW_FROM_COMBO_BOX = 5;
  public static final int VIEW_TRIGGER_VIEW_NEW                   = 6;
  public static final int VIEW_TRIGGER_VIEW_REMOVE                = 7;
  
  XOS                 xos           = XOS.getShared();
  
  static final String QUICK_START  
      = "userguide/products/iwisdom/quickstart.html";
  
  static final int    ONE_SECOND    = 1000;
  static final int    ONE_MINUTE    = ONE_SECOND * 60;
  static final int    ONE_HOUR      = ONE_MINUTE * 60;
  
  static final String LEFT          = "left";
  static final String TOP           = "top";
  static final String WIDTH         = "width";
  static final String HEIGHT        = "height";
  static final String DIVIDER_LOCATION = "divider-location";
  static final String LIST_TAB_SELECTED = "list-tab-sel";
  
  
  /** Various system properties. */
  String              userName;
  String              userDirString;
  String              fileSeparatorString;
  
  // Global variables
  Trouble             trouble;
  Home                home;
  UserPrefs           userPrefs;
  File                appFolder;
  WisdomDiskDirectory files;
  RecentFiles         recentFiles = null;
  FileSpec            currentFileSpec = null;
  boolean             currentFileModified = false;
  ViewList            views;
  
  // Parameter Info
  boolean             selItemTab = true;
  
  // File stuff
  // File                tdfFile;
  // TabDelimFile        tdf;
  // boolean             fileNowOpen = false;
  WisdomDiskStore     diskStore = null;
  File                importFile;
  WisdomIOFormats            wisdomIO = new WisdomIOFormats();
  // Template            template;
  // int                 publishWhen = 0;
  // static final int    PUBLISH_ON_CLOSE = 1;
  // static final int    PUBLISH_ON_SAVE  = 2;
  
  // GUI stuff
  
  JFrame              frame;
  JMenu               editMenu;
  public JTabbedPane  tabs;
  public JTabbedPane  tabs2;
  private JSplitPane  split;
  private JButton     itemOkButton;
  private JDialog     saveDialog;
  private JProgressBar saveProgress;
  
  javax.swing.Timer   progressTimer;
  javax.swing.Timer   validateURLTimer;
  javax.swing.Timer   midnightTimer;
  javax.swing.Timer   rotateTimer;
  
  ListTab             listTab;
  CategoryTab         treeTab;
  DisplayTab           rotateTab;
  ContentPanel        contentTab;
  AuthorPanel         authorTab;
  WorkPanel           workTab;
  
  boolean             tabSetupComplete = false;
  
  CollectionWindow    collectionWindow;
  ImportWindow        importWindow;
  ImportWikiQuoteWindow importWikiQuoteWindow;
  ExportWindow        exportWindow;
  LogWindow           logWindow;
  AboutWindow         aboutWindow;
  PrefsWindow         prefsWindow;
  PublishWindow       publishWindow;
  
  private StatusBar   statusBar = null;
  
  DateFormat          longDateFormatter;
  DateFormat          dateFormatter = new SimpleDateFormat ("MM/dd/yyyy");
  private DateFormat  backupDateFormatter 
      = new SimpleDateFormat ("yyyy-MM-dd-HH-mm");
  WisdomItems           items         = new WisdomItems(this);
  private CategoryList        categories    = new CategoryList();
  CategoryModel       tree;
  SortedItems         sorted        = new SortedItems(this);
  ItemNavigator       navigator     = sorted;
  AuxList             auxList       = new AuxList(this);
  RotateList          rotateList    = new RotateList(this);
  
  private String      selectSave    = ItemSelector.SHOW_ALL_STR;
  
  WisdomItem          item;
  boolean             newItem = false;
  private boolean     changed = false;
  private WisdomItem  priorItem = null;
  private String      selectedTags = "";
  private String      priorAuthorFileName = "";
  private String      priorSourceFileName = "";
  private String      priorTitleFileName = "";
  
  URL                 pageURL;
  
  Color               rotateBackgroundColor   = new Color (255, 255, 255);
  Color               rotateTextColor         = new Color (0, 0, 0);
  String              rotateFont              = "Verdana";
  int                 rotateNormalFontSize    = 3;
  int                 rotateBigFontSize       = 4;
  int                 rotateSeconds           = 10;
  
  boolean             displayTitle            = true;
  boolean             displaySource           = true;
  boolean             displayType             = true;
  boolean             displayAdded            = true;
  boolean             displayID               = true;

  private int         saveIndex               = 0;
  
  Logger              logger     = Logger.getShared();
  LogOutput           logOutput;
  
  // Fields used to validate Web Page URLs
  private  ThreadGroup webPageGroup;
  private  ArrayList  pages;
  private  ProgressMonitor progressDialog;
  private  int        progressMax = 0;
  private  int        progress = 0;
  private  int        badPages = 0;
  
  JProgressBar        progressBar; 
  
  boolean             initComplete = false;
  
  // System ClipBoard fields
  boolean             clipBoardOwned = false;
  Clipboard           clipBoard = null;
  Transferable        clipContents = null;
  
  WisdomFavorites     favorites = new WisdomFavorites();
  
  private     ChainLink    idParent = null;
  private     ChainLink    idNext   = null;
  private     ChainLink    idPrior  = null;
  private     ChainLink    idFirstChild = null;
  private     ChainLink    idLastChild  = null;
  
  
  
  public ChainLink getLastChildLink () {
    return idLastChild;
  }
  
  public void setNextLink (ChainLink link) {
    // Should not be any next links for the top link
  }
  
  public void addChildLink (ChainLink child) {
    if (idLastChild == null) {
      idFirstChild = child;
    } else {
      idLastChild.setNextLink (child);
    }
    idLastChild = child;
  }
  
  /**
   * 
   *    Creates a new instance of iWisdomCommon 
   */
  public iWisdomCommon(
      JFrame frame,
      JTabbedPane tabs,
      JTabbedPane tabs2,
      JSplitPane split,
      JButton itemOkButton) {
    
    this.frame = frame;
    this.tabs = tabs;
    this.tabs2 = tabs2;
    this.split = split;
    this.itemOkButton = itemOkButton;
    trouble = Trouble.getShared();
    trouble.setParent (tabs);
    home = Home.getShared ();
    
    appFolder = home.getAppFolder();
    if (appFolder == null) {
      trouble.report ("The " + home.getProgramName() 
          + " Folder could not be found", 
          "App Folder Missing");
    } else {
      Logger.getShared().recordEvent (LogEvent.NORMAL, 
        "App Folder = " + appFolder.toString(),
        false);
      try {
        pageURL = appFolder.toURI().toURL(); 
      } catch (MalformedURLException e) {
        trouble.report ("Trouble forming pageURL from " + appFolder.toString(), 
            "URL Problem");
      }
    }
    
    userPrefs = UserPrefs.getShared();
    // userPrefs.setProgramName (PROGRAM_NAME);
    // comp.setSelector (select);
    logWindow = new LogWindow ();
    logOutput = new LogOutputText(logWindow.getTextArea());
    Logger.getShared().setLog (logOutput);
    Logger.getShared().setLogAllData (false);
    Logger.getShared().setLogThreshold (LogEvent.NORMAL);
    WindowMenuManager.getShared().add(logWindow);
    
    // Get System Properties
    userName = System.getProperty ("user.name");
    userDirString = System.getProperty (GlobalConstants.USER_DIR);
    Logger.getShared().recordEvent (LogEvent.NORMAL, 
      "User Directory = " + userDirString,
      false);

    Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Java Virtual Machine = " + System.getProperty("java.vm.name") + 
        " version " + System.getProperty("java.vm.version") +
        " from " + StringUtils.removeQuotes(System.getProperty("java.vm.vendor")),
        false);
    if (xos.isRunningOnMacOS()) {
      Logger.getShared().recordEvent (LogEvent.NORMAL,
          "Mac Runtime for Java = " + System.getProperty("mrj.version"),
          false);      
    }
    Runtime runtime = Runtime.getRuntime();
    runtime.gc();
    NumberFormat numberFormat = NumberFormat.getInstance();
    Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Available Memory = " + numberFormat.format (Runtime.getRuntime().freeMemory()),
        false);
    fileSeparatorString = System.getProperty (GlobalConstants.FILE_SEPARATOR, "\\");

    sorted.setCommon (this);
    
    favorites.add (
        "PortableWisdom",
        "http://www.portablewisdom.org/data/authors.xml",
        true);
    
    recentFiles = new RecentFiles();
    recentFiles.loadFromPrefs();
    
    // set refresh timer
    /*
    midnightTimer = new javax.swing.Timer (ONE_HOUR, this);
    midnightTimer.start();
    */
    
  }
  
  public void initComponents() {

    listTab = new ListTab (this);
    tabs.addTab("List", listTab);
    
    treeTab = new CategoryTab (this);
    tabs.addTab("Categories", treeTab);

    boolean listTabSelected =
        userPrefs.getPrefAsBoolean (LIST_TAB_SELECTED, true);
    if (listTabSelected) {
      tabs.setSelectedComponent (listTab);
    } else {
      tabs.setSelectedComponent (treeTab);
    }
    
    rotateTab = new DisplayTab (this);
    tabs2.addTab ("Display", rotateTab);
    
    contentTab = new ContentPanel (this);
    tabs2.addTab ("Content", contentTab);
    
    authorTab = new AuthorPanel (this);
    tabs2.addTab ("Author", authorTab);
    
    workTab = new WorkPanel (this);
    tabs2.addTab ("Work", workTab);
    
    this.activateDisplayTab();
    
    newUserPrefs();
    
    collectionWindow = new CollectionWindow (this);
    importWindow = new ImportWindow (this);
    importWikiQuoteWindow = new ImportWikiQuoteWindow (this);
    exportWindow = new ExportWindow (this);
    aboutWindow = new AboutWindow (
      false,   // loadFromDisk 
      true,    // jxlUsed
      true,    // pegdownUsed
      true,    // xercesUsed
      true,    // saxonUsed
      "2003"   // copyRightYearFrom
        );
    prefsWindow = new PrefsWindow (this);
    publishWindow = new PublishWindow(this);
    publishWindow.setOnSaveOption(false);
    
    modifyView (-1, iWisdomCommon.VIEW_TRIGGER_PROGRAM_START);
    
    tabSetupComplete = true;
    
  }
  
  public void setStatusBar (StatusBar statusBar) {
    this.statusBar = statusBar;
    publishWindow.setStatusBar(statusBar);
  }
  
  public void savePrefs () {
    userPrefs.setPref (LIST_TAB_SELECTED,
        tabs.getSelectedIndex() == 0);
    prefsWindow.savePrefs();
    recentFiles.savePrefs();
  }
  
  public void setSplit (boolean splitPaneHorizontal) {
    int splitOrientation = JSplitPane.VERTICAL_SPLIT;
    if (splitPaneHorizontal) {
      splitOrientation = JSplitPane.HORIZONTAL_SPLIT;
    }
    split.setOrientation (splitOrientation);
  }
  
  /**
   Initialize a new file.
   */
  protected void fileNew() {
    fileClose();
    collectionWindow.initHeader();
    diskStore = files.chooseDiskStore
        (files.NEW, frame, diskStore);
    if (diskStore == null
        || diskStore.isUnknown()
        || (! diskStore.isValid())) {
      fileOpenDefault();
    } else {
      diskStore.getXMLIO().setOlderPublicationStyle
        (prefsWindow.getGeneralPrefs().getOlderPublicationStyle());
      diskStore.open();
      diskStore.setLog (Logger.getShared());
      diskStore.setHeader (collectionWindow);
      resetComparator();
      // diskStore.setComparator (comp);
      filePrep();
      newItems();
      initItems();
      modifyView (0, VIEW_TRIGGER_NEW_FILE);
      statusBar.setFileName("            ", " ");

      addDefaultItem();

      modifyView (0, VIEW_TRIGGER_LEAVING_VIEW_TAB);

      setUnsavedChanges (false);
      displayFile();
      displayItem();
      activateItemTab();
    } 
  }
  
  public void addDefaultItem () {

    newItem();
    item.setTitle ("Fuzzy Concept");
    item.setAuthor ("Ansel Adams");
    item.setBody 
        ("<p>\"There is nothing worse than a brilliant image of a fuzzy concept.\"</p>");
    item.setCategory ("Design");
    categories.registerValue (item.getCategory().toString());
    displayItem();
    setChanged();
    modIfChanged();
  }
  
  public void initItems() {
    listTab.initItems();
    treeTab.initItems();
    rotateTab.initItems();
    contentTab.initItems();
    authorTab.initItems();
    workTab.initItems();
    collectionWindow.initItems();
    importWikiQuoteWindow.initItems();
    exportWindow.initItems();
  }
  
  /**
    Open an existing file, allowing the user to specify the file.
   */
  protected void fileOpen() {
    modIfChanged();
    fileClose();
    WisdomDiskStore store = files.chooseDiskStore
        (files.OPEN, frame, diskStore);
    if (store != null) {
      store.setLog (Logger.getShared());
      store.setHeader (collectionWindow);
      if (store.isWisdomFileValidInput()) {
        fileOpenFresh (store);
      }
    }
  }
  
  /**
    Open an existing file, passing the file identifier in.
   */
  /*
  protected void fileOpen(File inFile, String inView, 
      File template, int publishWhen) {
    modIfChanged();
    fileClose();
    WisdomDiskStore store = WisdomDiskStore.makeDiskStore (inFile);
    store.setComparator (inView);
    store.setTemplate (template);
    store.setPublishWhen (publishWhen);
    store.setLog (Logger.getShared());
    fileOpen (store);
  }
   */
  
  public void fileOpenDefault () {
    WisdomDiskStore defaultStore = files.getDefaultStore();
    if (defaultStore == null || defaultStore.isInvalid()) {
      fileOpen (files.getUsualLocation());
      // addDefaultItem();
      // modIfChanged();
      // navigator.firstItem();
      // displayItem();
    } else {
      fileOpen (defaultStore);
    }
    if (items.size() == 0) {
      addDefaultItem();
      modIfChanged();
      navigator.firstItem();
      displayItem();
    }
  }
  
  public void handleOpenFile (FileSpec fileSpec) {
    fileOpen (fileSpec.getFile());
  }
  
  /**
    Open an existing file, given a File identifier.
   */
  public void fileOpen (File openFile) {
    WisdomDiskStore openStore = files.get (openFile);
    if (openStore == null) {
      openStore = files.makeDiskStore (openFile);
    }
    openStore.setHeader (collectionWindow);
    fileOpen (openStore);
  }
  
  /**
    Open an existing to do file, or its archive, 
    passing the disk store in along with an archive flag.
   
    @param diskStore Disk Store whose file or archive is to be opened.
    @param processingArchive True if we are to process the archive instead
                             of the current items.
   */
  protected void fileOpen (WisdomDiskStore diskStore) {
    if (diskStore != null && diskStore.isFileNowOpen()) {
      modIfChanged();
      fileClose();
    }
    fileOpenFresh(diskStore);
  }
  
  /**
    Open an existing wisdom folder, passing the disk store in. Any necessary
    closure and cleanup from a previous file is presumed complete.
   
    @param diskStore Disk Store to be opened.
   */
  protected void fileOpenFresh (WisdomDiskStore diskStore) {
    boolean openOK = true;
    if (! diskStore.isWisdomFileValidInput()) {
      openOK = false;
      storeNotFound (diskStore);
    }
    if (openOK) {
      collectionWindow.initHeader();
      this.diskStore = diskStore;
      diskStore.setHeader (collectionWindow);
      diskStore.getXMLIO().setOlderPublicationStyle
        (prefsWindow.getGeneralPrefs().getOlderPublicationStyle());
      openOK = diskStore.open();
      if (diskStore.isPrimary()) {
        // userPrefs.setAltParmsFolder (diskStore.getFile());
        newUserPrefs();
      }
      filePrep();
      newItems();
      try {
        diskStore.populate (collectionWindow, items);
        rememberLastFile (diskStore);
        currentFileSpec = recentFiles.addRecentFile 
            ("file", diskStore.getPath(), "wisdom");
        currentFileModified = false;
      } catch (IOException e) {
        openOK = false;
        storeNotFound (diskStore);
      } // end catch IOException
    }
    if (openOK) {
      views.loadDiskViews(diskStore);
      displayFile();
      // clearActionMsg();
      initItems();
      navigator.firstItem();
      displayItem();
      // activateListTab();
      modifyView (diskStore.getViewIndex(), VIEW_TRIGGER_SELECT_FILE);
      
      // See if collection needs to be brought up to new version level
      String programVersion = Home.getShared().getProgramVersion();
      String collectionVersion = collectionWindow.getCollectionVersion();
      if (collectionVersion.trim().length() == 0) {
        collectionVersion = "--";
      }
      if (! collectionVersion.equals (programVersion)) {
        if (! collectionVersion.equals("--")) {
          FilePrefs.getShared().handleMajorEvent
              (currentFileSpec, recentFiles.getPrefsQualifier(), 0);
        }
        rewrite();
        collectionWindow.setCollectionVersion (programVersion);
        Logger.getShared().recordEvent (LogEvent.NORMAL, 
            "Collection " + diskStore.getShortPath()
            + " upgraded from version " + collectionVersion
            + " to " + programVersion,
        false);
      }
    } else {
      fileNew();
    }
  }
  
  private void storeNotFound (WisdomDiskStore diskStore) {
    
    Object[] options = { "Remember it for later", "Forget about it" };
    int userOption = JOptionPane.showOptionDialog(tabs, "File " 
        + diskStore.toString() + " is not available", 
        "File Open Error",
        JOptionPane.DEFAULT_OPTION, 
        JOptionPane.WARNING_MESSAGE,
        Home.getShared().getIcon(), options, options[0]);
    switch (userOption) {
      case 0:
        // Remember it for later: no action necessary
        break;
      case 1:
        // Forget about it
        diskStore.setForgettable (true);
        files.forget (diskStore);
        break;
      default:
        // Do nothing
        break;
    } // end switch
  }
  
  /**
    Pass the new disk store to any tabs that care, for initialization.
   */
  private void filePrep () {
    collectionWindow.filePrep (diskStore);
    prefsWindow.filePrep (diskStore);
    exportWindow.filePrep (diskStore);
    publishWindow.openSource(diskStore.getFile());
  }
  
  /**
    Create a new items collection and associated objects and 
    link the whole mess together.
   */
  private void newItems () {
    // header = new IDListHeader();
    items = new WisdomItems(this);
    
    categories = new CategoryList();
    categories.registerValue("");
    items.addView (categories);
    
    sorted = new SortedItems 
        (items, diskStore.getComparator(), diskStore.getSelector());
    sorted.setCommon (this);
    navigator = sorted;
    items.addView (sorted);
    sorted.firstItem();
    
    tree = new CategoryModel
        (items, diskStore.getComparator(), diskStore.getSelector(), 
          diskStore.getFile());
    tree.setCommon (this);
    items.addView (tree);
    
    // diskStore = new WisdomDiskUnknown();
    setUnsavedChanges (false);
    
  }
  
  /**
   Remove any files and folders left over from older versions of iWisdom. 
   */
  public void cleanup() {
    if (diskStore != null
        && diskStore.isValid()) {
      attemptToDelete ("iwisdom_sysprefs.xml");
      attemptToDelete ("iwisdom_userprefs.xml");
      Logger.getShared().recordEvent(
          LogEvent.NORMAL, 
          "Cleanup completed for " + diskStore.getFile().toString(), 
          false);
    }
  }
  
  private void attemptToDelete(String nameToDelete) {
    boolean ok = true;
    File fileToDelete = new File(diskStore.getFile(), nameToDelete);
    if (fileToDelete.exists()) {
      if (fileToDelete.isDirectory()) {
        FileUtils.deleteFolderContents(fileToDelete);
      }
      ok = FileUtils.deleteFile(fileToDelete);
      Logger.getShared().recordEvent(
          LogEvent.NORMAL, 
          "Deleted file/folder " + fileToDelete.toString(), 
          false);
    }
  }
  
  /**
   Rewrite the current list to disk, effectively updating the disk 
   representation to the latest storage format.
   */
  public void rewrite() {

    Logger.getShared().recordEvent(LogEvent.NORMAL, 
        "Rewriting collection with" +
        " organize within folders = " +
        String.valueOf(collectionWindow.isOrganizeWithinFolders()) +
        ", naming by title = " + 
        String.valueOf(collectionWindow.isFileNamingByTitle() +
        ", storage format = " + collectionWindow.getStorageFormat()), 
        false);
    WisdomItem nextItem;
    for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
      nextItem = items.get (itemIndex);
      if (! nextItem.isDeleted()) {
        diskStore.save (nextItem);
      }
    } // end for loop
    Logger.getShared().recordEvent(LogEvent.NORMAL, 
        "Rewrite completed for " + diskStore.getFile().toString(), false);

  }
  
  /**
    Import additional records into the current data store from a disk
    file specified by the user. 
   */
  public void fileImport(File importFile) {
    
    handleMajorEvent();
    importWindow.setFile (importFile);
    WindowMenuManager.getShared().makeVisible(importWindow);

  } // end method fileImport
  
  public void handleMajorEvent() {
    FilePrefs.getShared().handleMajorEvent
        (currentFileSpec, recentFiles.getPrefsQualifier(), 0);
  }
  
  public void resetComparator() {
    diskStore.resetComparator();
  }
  
  /**
    Save the current file if there are currently any unsaved changes in memory.
   */
  protected void fileClose() {
    if (diskStore != null) {
      publishWindow.closeSource();
    }
    if (currentFileModified) {
      FilePrefs.getShared().handleClose
          (currentFileSpec, recentFiles.getPrefsQualifier(), 0);
    }
    items.shutDown();
    if (diskStore != null) {
      // diskStore.setTemplate (webTab.getTemplate());
      rememberLastFile (diskStore);
      collectionWindow.saveCollectionHeader();
      rewrite();
      if (diskStore.getPublishWhen() == WisdomDiskStore.PUBLISH_ON_CLOSE) {
        publish ();
      }
      views.saveViews(diskStore);
      diskStore.close();
      currentFileSpec = null;
    } 
  } // end method
  
  /**
    Save the current file from memory to disk.
   */
  protected void fileSave() {
    modIfChanged();
    if (diskStore.isFileNowOpen()) {
      try {
        diskStore.save (items);
        setUnsavedChanges (false);
        // diskStore.setTemplate (webTab.getTemplate());
        rememberLastFile (diskStore);
        boolean prefsOK = userPrefs.savePrefs();
      } catch (IOException e) {
          trouble.report ("iWisdom data could not be saved",
              "File Save Error");
      } // end catch
      // clearActionMsg();
    } else {
      // fileSaveAs();
    }
  } // end method
  
  /**
   Backup without prompting the user. 
  
   @return True if backup was successful. 
  */
  public boolean backupWithoutPrompt() {
    
    boolean ok = true;
    
    File backupFolder = getBackupFolder();
    String backupFileName = getBackupFileName();
    File backupFile = new File 
        (backupFolder, backupFileName);
    ok = backup (backupFile);

    return ok;
  }
  
  /**
    Perform a backup of the wisdom collection that is currently open. 
  
    @return True if backup was performed successfully.
  */
  public boolean promptForBackup () {
    
    boolean ok = false;
    
    XFileChooser chooser = new XFileChooser (); 
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY); 
    
    // Get the default folder to use for backups
    File backupFolder = getBackupFolder();
    chooser.setCurrentDirectory (backupFolder);
    
    // Now build default file name
    String backupFileName = getBackupFileName();
    File defaultBackupFile = new File 
        (backupFolder, backupFileName);
    
    chooser.setFile (defaultBackupFile);
    chooser.setDialogTitle("Create a Backup of your iWisdom Collection");
   
    File result = chooser.showSaveDialog (frame); 
    
    File backupFile = null;
    if (result != null) { 
      backupFile = chooser.getSelectedFile();
      ok = backup (backupFile);
      if (ok) {
        JOptionPane.showMessageDialog(frame,
            "Backup completed successfully",
            "Backup Results",
            JOptionPane.INFORMATION_MESSAGE,
            Home.getShared().getIcon());
      } 
    } // end if result file was selected
    return ok;
  }
  
  /**
   Get the default folder to be used for backups. 
  
   @return Last folder used, or default folder for collection. 
  */
  public File getBackupFolder() {
    File backupFolder = null;
    String backupFolderPath = collectionWindow.getBackupFolder();
    if (backupFolderPath == null || backupFolderPath.equals("")) {
      backupFolder = diskStore.getBackupsFolder();
    } else {
      backupFolder = new File (backupFolderPath);
    }
    return backupFolder;
  }
  
  /**
   Get the default file name to be used for backups. 
  
   @return A default file name to be used for backups. 
  */
  public String getBackupFileName() {
    StringBuilder backupFileName = new StringBuilder ();
    FileName name = new FileName (diskStore.getFile());
    int numberOfFolders = name.getNumberOfFolders();
    int i = numberOfFolders - 1;
    if (i < 0) {
      i = 0;
    }
    while (i <= numberOfFolders) {
      if (backupFileName.length() > 0) {
        backupFileName.append (' ');
      }
      backupFileName.append (name.getFolder (i));
      i++;
    }
    backupFileName.append (" backup ");
    backupFileName.append (backupDateFormatter.format (new Date()));
    backupFileName.append (".xml");
    return backupFileName.toString();
  }
  
  /**
   Backup the collection to the passed file. 
  
   @param backupFile The backup file to be created.
  
   @return True if backup was created successfully.
  */
  public boolean backup (File backupFile) {
    
    boolean ok = true;
    FileName backupName = new FileName (backupFile, FileName.FILE_TYPE);
    FileName backupWisdomName 
        = new FileName (backupName.getPath() + backupName.replaceExt 
          (WisdomXMLIO.WISDOM_FILE_EXTENSION), FileName.FILE_TYPE);
    
    TextLineWriter writer = new FileMaker (backupWisdomName.getFile());

    MarkupWriter markupWriter 
        = new MarkupWriter (writer, MarkupWriter.XML_FORMAT);
    WisdomXMLIO xmlio = diskStore.getXMLIO();
    ok = xmlio.save (markupWriter, collectionWindow, sorted);
    if (ok) {
      Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Collection backed up to " + backupWisdomName.toString(),
          false);
      collectionWindow.setBackupFolder(backupWisdomName.getPath());
      FilePrefs.getShared().saveLastBackupDate
          (currentFileSpec, recentFiles.getPrefsQualifier(), 0);
    } else {
        trouble.report ("iWisdom data could not be backed up",
            "File Backup Error");
    }
    return ok;
  }
  
  /**
   Revert to a backup. Allow the user to select the backup file, delete all 
   the current items, and import the items contained in the backup file. 
   */
  public boolean fileRevert () {
    
    boolean ok = true;
    modIfChanged();
    
    // Ask the user to choose the backup file
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_ONLY); 
    
    // Get the default folder to use for backups
    File backupFolder = null;
    String backupFolderPath = collectionWindow.getBackupFolder();
    if (backupFolderPath == null || backupFolderPath.equals("")) {
      backupFolder = diskStore.getBackupsFolder();
    } else {
      backupFolder = new File (backupFolderPath);
    }
    chooser.setCurrentDirectory (backupFolder);
    File importFile = chooser.showOpenDialog (frame);
    
    if (importFile != null) {

      // Delete everything in the collection
      for (int i = 0; i < items.size(); i++) {
        WisdomItem itemToDelete = items.get (i);
        if (itemToDelete != null
            && itemToDelete.getBody().length() > 0) {
          items.remove (itemToDelete);
        }
      }
      
      newItems();
      Logger.getShared().recordEvent (LogEvent.NORMAL, 
        "Reverting from "  + importFile.toString(),
        false);
      diskStore.importXML (importFile.getPath(), collectionWindow, items);
      int added = (navigator.size());
      WisdomDiskStore currentDiskStore = diskStore;
      fileOpen (currentDiskStore);
      JOptionPane.showMessageDialog(frame,
          String.valueOf (added) 
              + " Items Restored from Backup",
          "Revert Results",
          JOptionPane.INFORMATION_MESSAGE);
      Logger.getShared().recordEvent (LogEvent.NORMAL,
          String.valueOf (added)
            + " Items restored from backup",
          false);
      listTab.setColumnWidths();
    }
    return ok;
  }
  
  public void filePrint() {
    ListPrinter printer = new ListPrinter();
    printer.print (sorted);
  }
  
  /**
   Saves information about last To Do file accessed.
   
   @param lastFile Last file opened or saved with new name.
   
  public void rememberLastFile (File lastFile, ItemComparator compare, File template) {
    
    if (lastFile != null) {
      recentFiles.remember 
          (lastFile, compare.toString(), template, diskStore.getPublishWhen());
    }
    
  } // end method */
  
  /**
   Saves information about last To Do file accessed.
   
   @param lastFile Last file opened or saved with new name.
   */
  public void rememberLastFile (WisdomDiskStore lastStore) {
    
    if (lastStore != null
        && (! lastStore.isUnknown())) {
      files.remember (lastStore);
    }
    
  } // end method
  
  /**
    Saves information about last To Do file accessed.
   */
  public void rememberLastFile () {
    
    if (diskStore != null
        && (! diskStore.isUnknown())) {
      files.remember (diskStore);
    }
    
  } // end method
  
  /**
    Validate URLs for the currently visible list of to do items.
   */
  public void validateURLs () {
    
    // Make sure user is ready to proceed
    Object[] options = { "Continue", "Cancel" };
    int userOption = JOptionPane.showOptionDialog(tabs, 
        "Please ensure your Internet connection is active", 
        "Validate Web Pages",
        JOptionPane.DEFAULT_OPTION, 
        JOptionPane.WARNING_MESSAGE,
        Home.getShared().getIcon(), options, options[0]);
    
    // If User is ready, then proceed
    if (userOption == 0) {
      
      // Prepare Auxiliary List to track invalid URLs
      webPageGroup = new ThreadGroup("WebPage threads");
      pages = new ArrayList();

      // Go through sorted items looking for Web Pages
      WisdomItem next;
      String address;
      WebPage page;
      for (int i = 0; i < sorted.size(); i++) {
        next = sorted.get(i);
        address = next.getAuthorLink();
        if (address.length() > 0) {
          page = new WebPage (webPageGroup, next, Logger.getShared(), this);
          pages.add (page);
        } 
        address = next.getSourceLink();
        if (address.length() > 0) {
          page = new WebPage (webPageGroup, next, Logger.getShared(), this);
          pages.add (page);
        } 
      } // end of sorted items
      
      // Prepare dialog to show validation progress
      progress = 0;
      progressMax = pages.size();
      progressDialog = new ProgressMonitor (tabs,
          "Validating "
              + String.valueOf (progressMax)
              + " Web Page URLs...",
          "                                                  ", // Status Note
          0,              // lower bound of range
          progressMax     // upper bound of range
          );
      progressDialog.setProgress(0);
      progressDialog.setMillisToDecideToPopup(500);
      progressDialog.setMillisToPopup(500);
      
      // Now start threads to check Web pages
      badPages = 0;
      for (int i = 0; i < pages.size(); i++) {
        page = (WebPage)pages.get(i);
        page.start();
      } // end for each page being validated 
      
      // Start timer to give the user a chance to cancel
      if (validateURLTimer == null) {
        validateURLTimer = new javax.swing.Timer (ONE_SECOND, this);
      } else {
        validateURLTimer.setDelay (ONE_SECOND);
      }
      validateURLTimer.start();
    } // continue rather than cancel
  } // end validateURLs method
  
  /**
   *    Record the results each time a WebPage checks in to report that
   *    its URL validation process has been complete. 
   *   
   *    @param item   The WisdomItem whose Web Page URL was being validated.
   *    @param valid  True if the URL was found to be valid. 
   */
  public synchronized void validateURLPageDone (WisdomItem item, boolean valid) {
    progress++;
    progressDialog.setProgress (progress);
    progressDialog.setNote ("Validation complete for "
        + String.valueOf (progress));
    if (! valid) {
      if (badPages == 0) {
        auxList.setItems (items);
        auxList.setSorted (sorted);
        auxList.initList();
      }
      badPages++;
      auxList.add (item.getItemNumber());
    }
    if (progress >= progressMax) {
      validateURLAllDone();
    } // end if all pages checked
  } // end method validateURLPageDone
  
  /**
    Handle GUI events, including the firing of various timers.
   
    @param event The GUI event that fired the action.
   */
  public void actionPerformed (ActionEvent event) {
    
    if (initComplete) {
      Object source = event.getSource();

      // URL Validation Timer
      if (source == validateURLTimer) {
        if (progressDialog.isCanceled()) {
          WebPage page;
          for (int i = 0; i < pages.size(); i++) {
            page = (WebPage)pages.get(i);
            if (! page.isValidationComplete()) {
              logEvent (LogEvent.MEDIUM, 
                  "URL Validation incomplete for "
                  + page.toString(),
                  false);
            }
          } // end for each page being validated 
          webPageGroup.stop(); 
          validateURLAllDone();
        }
      }
      else

      // Progress Timer -- Unused??
      if (source == progressTimer) {
        saveProgress.setValue (items.getItemIndex());
      }
      else

      // Midnight Timer
      if (source == midnightTimer) {
        DateUtils.refresh();
        // todayLabel.setText (longDateFormatter.format (new Date()));
        // sorted.fireTableDataChanged();
      }
      else

      // Rotation Timer
      if (source == rotateTimer) {
        modIfChanged();
        navigator.nextItem();
        displayItem();
      }
    }
     // else
      
    // Action Message Timer
    // if (source == actionMsgTimer) {
    //   actionMsg.setText ("          ");
    // }
  } // end method
  
  /**
    Shut down the URL Validation process and report the results.
   */
  private void validateURLAllDone () {
    if (validateURLTimer != null
        && validateURLTimer.isRunning()) {
      validateURLTimer.stop();
    }
    progressDialog.close();
    JOptionPane.showMessageDialog(tabs,
      String.valueOf (badPages) 
          + " Invalid Web Page(s) Found out of " 
          + String.valueOf (pages.size()),
      "Web Page Validation Results",
      JOptionPane.INFORMATION_MESSAGE);
    if (badPages > 0) {
      navigator = auxList;
      auxList.firstItem();
      displayItem();
      activateItemTab();
    } // end if any bad pages found
  } // end method
  
  public void startRotation () {
    
    activateDisplayTab();
    if (rotateTimer == null) {
      rotateTimer = new javax.swing.Timer (rotateSeconds * ONE_SECOND, this);
    } else {
      rotateTimer.setDelay (rotateSeconds * ONE_SECOND);
    }
    rotateTimer.start();
    
    rotateList.setItems (items);
    rotateList.setSorted (sorted);
    rotateList.start();
    if (rotateList.isRotating()) {
      navigator = rotateList;
      rotateList.firstItem();
      displayItem();
    }
  }
  
  public void endRotation () {
    if (rotateList.isRotating()) {
      rotateTimer.stop();
      rotateList.stop();
      navigator = sorted;
    }
  }
  
  public int replaceCategory (String find, String replace) {
    int mods = 0;
    WisdomItem next;
    Category category;
    String cat;
    for (int i = 0; i < items.size(); i++) {
      next = items.get(i);
      category = next.getCategory();
      boolean modified = false;
      if (find.equals("")) {
        category.append (replace);
        modified = true;
      } else {
        category.startCategoryIteration();
        while (category.hasNextCategory() && (! modified)) {
          cat = category.nextCategory();
          if (cat.equalsIgnoreCase (find)) {
            category.removeCategory();
            if (replace.length() > 0) {
              category.append (replace);
            }
            modified = true; 
          } 
        } // end while this item has more categories
      } // end if we the find category is not blank
      if (modified) {
        mods++;
        setUnsavedChanges (true);
        items.modify (next);
          // In theory, we should be passing prior values of the file name 
          // variables, but since we are only modifying categories, we know 
          // that none of the file names have changed, so we are passing 
          // current values, which will avoid deleting
          // the old file before saving the new one.
      } // end if modified
    } // end of  items
    displayItem();
    return mods;
  }
  
  /**
    Clear the action message.
   */
  /*
  public void clearActionMsg () {
    if (actionMsgTimer == null) {
      actionMsgTimer = new javax.swing.Timer (5000, this);
    } else {
      if (actionMsgTimer.isRunning()) {
        actionMsgTimer.stop();
      }
      actionMsgTimer.setDelay (5000);
    }
    actionMsgTimer.setRepeats (false);
    actionMsgTimer.start();
  } */
  
  /**
    Set the action message.
   */
  /*
  public void setActionMsg (String message) {
    if (actionMsgTimer != null
        && actionMsgTimer.isRunning()) {
      actionMsgTimer.stop();
    }
    actionMsg.setText (message); 
  }
   */
  
  /**
    Make the file name visible to the user.
   */
  protected void displayFile() {
    statusBar.setFileName(diskStore.getShortPath(), diskStore.getPath());
  }
  
  /**
    Changes the active tab to the tab displaying a list of items.
   */
  public void activateListTab () {
    tabs.setSelectedIndex (LIST_TAB_INDEX);
    listTab.grabFocus();
  }
  
  /**
    Changes the active tab to the tab displaying a tree of items 
    organized by category.
   */
  public void activateTreeTab () {
    tabs.setSelectedIndex (TREE_TAB_INDEX);
  }
  
  /**
    Changes the active tab to the tab displaying an individual item.
   */
  public void activateItemTab () {
    tabs2.setSelectedIndex (CONTENT_TAB_INDEX);
    itemOkButton.setEnabled(true);
  }
  
  /**
    Changes the active tab to the tab displaying an individual item.
   */
  public void activateDisplayTab () {
    tabs2.setSelectedIndex (DISPLAY_TAB_INDEX);
    itemOkButton.setEnabled(false);

  }
  
  
  /**
    Displays the About window.
   */
  public void displayAbout () {
    WindowMenuManager.getShared().makeVisible(aboutWindow);
  }
  
  /**
    Changes the active tab to the Prefs tab.
   */
  public void displayPrefs () {
    WindowMenuManager.getShared().makeVisible(prefsWindow);
  }
  
  void switchTabs () {
    if (tabSetupComplete 
        && tabs2.getSelectedIndex() == DISPLAY_TAB_INDEX) {
      modIfChanged();
      displayItem();
    }
    endRotation();
    if (tabs2.getSelectedIndex() == DISPLAY_TAB_INDEX) {
      itemOkButton.setEnabled(false);
    } else {
      itemOkButton.setEnabled(true);
    }
  }
  
  public void findItem (String findString) {
    auxList.setItems (items);
    auxList.setSorted (sorted);
    auxList.find (findString);
    if (auxList.isAuxActive()) {
      navigator = auxList;
      auxList.firstItem();
      displayItem();
      // activateItemTab();
    } else {
      JOptionPane.showMessageDialog(tabs,
          "No items found",
          "OK",
          JOptionPane.WARNING_MESSAGE);
    }
  }
  
  public void findAgain () {
    if (auxList.isAuxActive()) {
      auxList.nextItem();
      displayItem();
      // activateItemTab();
    }
  }
  
  /**
   *    Gets the WisdomItem currently being displayed/modified. 
   *   
   *    @return The WisdomItem currently selected.
   */
  public WisdomItem getItem() {
    if (item == null) {
      item = new WisdomItem();
    }
    return item;
  }
  
  public WisdomDiskStore getDiskStore () {
    return diskStore;
  }

  /**
   User has pressed the OK button to indicate that they are done editing. 
  */
  void doneEditing() {
    if (tabSetupComplete) {
      modIfChanged();
      displayItem();
    }
    activateDisplayTab();
  }
  
  /**
   *    Sets the WisdomItem currently being displayed/modified. 
   *   
   *    @param item The WisdomItem to be stored as the current one.
   */
  public void setItem (WisdomItem item) {
    this.item = item;
    newItem = false;
    displayItemNumber();
  }

  public void saveSelectedTags() {
    selectedTags = "";
    CategoryNode tags = treeTab.getLastSelectedPathComponent();
    if (tags != null) {
      selectedTags = tags.getTagsAsString();
    }
  }
  
  /**
   *    Allocates a new WisdomItem.
   */
  public void newItem() {

    item = new WisdomItem ();
    if (selectedTags.length() > 0) {
      item.setCategory(selectedTags);
    }
    newItem = true;
    displayItemNumber();

  }
  
  /**
    Displays the item number currently being worked on.
   */
  private void displayItemNumber() {
    if (statusBar != null) {
      statusBar.setPosition(navigator.getItemNumber() + 1, navigator.size());
    }
  }
  
  /**
    Selects the passed item and displays it.
   
    @param item Item being selected.
   */
  public void selectItem (WisdomItem item) {
    this.item = item;
    navigator.selectItem (item);
    displayItem();
    // activateItemTab();
  }
  
  /**
   Displays the item at the current index.
   */
  public void displayItem() {
    contentTab.displayItem();
    authorTab.displayItem();
    workTab.displayItem();
    rotateTab.displayItem();
    listTab.displayItem();
    displayRecordCounts();
    changed = false;
    if (item == null) {
      priorAuthorFileName = "";
      priorSourceFileName = "";
      priorTitleFileName  = "";
    } else {
      priorAuthorFileName = item.getAuthorFileName();
      priorSourceFileName = item.getSourceFileName();
      priorTitleFileName  = item.getTitleFileName();
    }
    if (item != null && (! item.isEmpty())) {
      priorItem = item;
    }
  }
  
  public void displayItemOnRotateTab() {
    rotateTab.displayItem();
  }

  public void lastItemCopy() {
    if (! priorItem.equals(item)) {
      contentTab.lastItemCopy(priorItem);
      authorTab.lastItemCopy(priorItem);
      workTab.lastItemCopy(priorItem);
    }
  }
  
  public void displayRecordCounts() {    
    if (statusBar != null) {
      statusBar.setPosition(navigator.getItemNumber() + 1, navigator.size());
    }
  } // end method
  
  public void setChanged () {
    changed = true;
  }
  
  /**
    Modifies the item if anything on the screen changed.  
   */
  public void modIfChanged () {
    if (item != null) {
      boolean contentChanged = contentTab.modIfChanged();
      boolean authorChanged  = authorTab.modIfChanged();
      boolean workChanged    = workTab.modIfChanged();
      if (contentChanged || authorChanged || workChanged) {
        changed = true;
      } 
      if (changed) {
        if (newItem) {
          if (item.getBody().length() > 0) {
            int itemsSize = items.size();
            int i = items.add (item, true, false, false);
            currentFileModified = true;
            if (i == 0) {
              modifyView (0, VIEW_TRIGGER_LEAVING_VIEW_TAB);
            }
            if (i < itemsSize) {
              Object[] options = { "OK" };
              int userOption = JOptionPane.showOptionDialog(
                  tabs, 
                  "Attempted addition merged with existing duplicate", 
                  "Duplicate Items",
                  JOptionPane.DEFAULT_OPTION, 
                  JOptionPane.WARNING_MESSAGE,
                  Home.getShared().getIcon(), 
                  options, 
                  options[0]);
              navigator.selectItem(items.get(i));
              displayItem();
            }
            newItem = false;
          }
        } 
        else
        if (! item.isEmpty()) {
          modifyItem();
        }
        // Shouldn't need to do this here, because SortedItems is doing
        // it internally when it is called by WisdomItems for a modify or add
        // sorted.fireTableDataChanged();
        // listTab.doLayout();
        setUnsavedChanges (true);
        // Following line added on 30-Oct-2004 -- Hope it works!
        changed = false;
      } // end if changed
    } // end if item not null
  } // end method
  
  public void addQuotesToAll () {
    firstItem();
    boolean allDone = false;
    int last = navigator.size() - 1;
    while (! allDone) {
      addQuotes();
      if (navigator.getItemNumber() >= last) {
        allDone = true;
      } else {
        nextItem();
      }
    }
    firstItem();
    activateDisplayTab();
  }
  
  public void removeQuotesFromAll () {
    firstItem();
    boolean allDone = false;
    int last = navigator.size() - 1;
    while (! allDone) {
      removeQuotes();
      if (navigator.getItemNumber() >= last) {
        allDone = true;
      } else {
        nextItem();
      }
    }
    firstItem();
    activateDisplayTab();
  }
  
  public void addQuotes () {
    modIfChanged();
    if (item != null) {
      item.addQuotes();
      modifyItem();
      displayItem();
    }
  }
  
  public void removeQuotes () {
    modIfChanged();
    if (item != null) {
      item.removeQuotes();
      modifyItem();
      displayItem();
    }
  }
  
  public void modifyItem () {
    items.modify (item);
    currentFileModified = true;
  }
  
  public void nextItem() {
    modIfChanged();
    navigator.nextItem();
    displayItem();
  }
  
  public void priorItem() {
    modIfChanged();
    navigator.priorItem();
    displayItem();
  }
  
  public void firstItem() {
    modIfChanged();
    navigator.firstItem();
    displayItem();
  }
  
  public void lastItem() {
    modIfChanged();
    navigator.lastItem();
    displayItem();
  }
  
  public void setUnsavedChanges (boolean unsaved) {
    // xos.setUnsavedChanges (unsaved);
    // fileSaveButton.setEnabled (unsaved);
    // unsavedChanges = unsaved;
  }
  
  public boolean getUnsavedChanges() {
    return xos.getUnsavedChanges();
  }
  
  public void newUserPrefs() {

  }
  
  public void setSelItemTab (boolean selItemTab) {
    this.selItemTab = selItemTab;
  }
  
  /**
    Controller code for modification of view settings.
   
    @param inIndex New value for view index, if applicable. Value less than zero
                 will be ignored. 
    @param trigger Indicator of what event is triggering the modifications. This
           should have one of the following values.
        VIEW_TRIGGER_PROGRAM_START (0) = start of program.
        VIEW_TRIGGER_NEW_FILE (1) = new, empty, file being created.
        VIEW_TRIGGER_SELECT_FILE (2) = different file being selected.
        VIEW_TRIGGER_LEAVING_VIEW_TAB (3) = user is leaving the view tab.
        VIEW_TRIGGER_SELECT_VIEW_FROM_MENU (4) = user selected a different view
                                                 from the View menu.
        VIEW_TRIGGER_SELECT_VIEW_FROM_COMBO_BOX (5) = user selected a different
                                                      view from the View tab
                                                      combo box.
        VIEW_TRIGGER_VIEW_NEW (6) = User created a new view.
        VIEW_TRIGGER_VIEW_REMOVE (7) = User is deleting a view.
        VIEW_TRIGGER_ARCHIVING (8) = User is archiving, so we need to
                                     temporarily ensure that all items are
                                     being selected.
        VIEW_TRIGGER_DONE_ARCHIVING (9) = User is done archiving, so we can 
                                          restore the prior select options.
   */
  public void modifyView (int inIndex, int trigger) {
    
    int index = inIndex;
    
    // Make sure we have captured any user updates to the current view
    if (trigger > VIEW_TRIGGER_PROGRAM_START) {
      prefsWindow.getViewPrefs().updateViewFromForm();
    }
    
    // Handle a request for a new view
    if (trigger == VIEW_TRIGGER_VIEW_NEW) {
      views.newView();
      index = views.getViewIndex();
    }
    
    // Handle a request to delete the current view
    if (trigger == VIEW_TRIGGER_VIEW_REMOVE) {
      views.removeView();
      index = views.getViewIndex();
    }
    
    // If we were passed a view index, then use it
    if (index >= 0) {
      views.setViewIndex (index, trigger);
    }
    
    // Update the file info if necessary
    if ((diskStore != null) 
        && (index >= 0)
        && (trigger > VIEW_TRIGGER_SELECT_FILE)) {
      diskStore.setViewIndex (index);
      diskStore.setComparator (views.getComparator());
    }
    
    // Set the GUI fields on the View Tab
    prefsWindow.getViewPrefs().setOptions (views.getComparator());
    
    // Make sure the list and tree views are using the latest view info
    sorted.setComparator (views.getComparator());
    sorted.sort();
    sorted.fireTableDataChanged();
    if (tree != null) {
      tree.setComparator(views.getComparator());
      tree.sort();
    }
    
    // Display current record counts, including records selected
    displayRecordCounts();
    
    // recalculate column content and widths on the List Tab
    listTab.setColumnWidths();

    // Make sure we retain information about the current file
    rememberLastFile();
  }
  
  public void launchWebPage() {
    if (item != null) {
      String webPage = item.getWebPage();
      if (webPage.length() > 7) {
        openURL (webPage);
      }
    }
  }
  
  public void browseFile (File webFile) {
    home.openURL(webFile);
  }
  
  public void donate () {
    openURL (DONATE_PAGE);
  }
  
  public boolean openURL (URL url) {
    return home.openURL(url);
  }
  
  public boolean openURL (String url) {
    return home.openURL(url);
  }
  
  /**
    Publish a to do list using a Web template or PSTextMerge script file.
   
    @param templateFile A File pointing to the file to be used for Web
                        publication.
   */
  public void publish () {
    saveIndex = views.getViewIndex();
    rewrite();
    diskStore.publish(collectionWindow, sorted);
    modifyView (saveIndex, VIEW_TRIGGER_SELECT_VIEW_FROM_MENU);
  } // end method
  
  public void logEvent (int severity, String msg, boolean dataRelated) {
    Logger.getShared().recordEvent (severity, msg, dataRelated);
  }
  
  public int getFrameWidth () {
    return frame.getWidth();
  }
  
  public void reportFolderTrouble () {

    trouble.report 
        ("Your data store must be opened" + GlobalConstants.LINE_FEED
        + "as a folder to perform this function.", "Save As a Folder First");
    
  }

  /**
   Prepare data to be used in the publishing process by PublishWindow.

   @param publishTo The folder to which we are publishing.
   */
  public void prePub(File publishTo) {
    saveIndex = views.getViewIndex();
    rewrite();
    diskStore.prePub(publishTo, collectionWindow, sorted);
    File dataFolder = new File (publishTo, "data");
    File exportFile = new File (dataFolder, "export.tab");

    Exporter.export(
          diskStore,
          collectionWindow,
          sorted,
          item,
          exportFile,
          wisdomIO.get(WisdomIOFormats.TAB_DELIMITED),
          Exporter.ALL,
          "",
          false);

    exportFile = new File (dataFolder, "split_export.tab");
    Exporter.export(
          diskStore,
          collectionWindow,
          sorted,
          item,
          exportFile,
          wisdomIO.get(WisdomIOFormats.TAB_DELIMITED),
          Exporter.ALL,
          "",
          true);

    exportFile = new File (dataFolder, "info_export.tab");
    collectionWindow.saveToTabDelim(exportFile);
  }

  /**
   Perform a publishing operation when requested by the publishing script.

   @param operand An operand specifying the operation to
                  be performed.
   */
  public boolean pubOperation(File publishTo, String operand) {
    return true;
  }

  /**
   Any post-processing to be done after PublishWindow has completed its
   publication process.

   @param publishTo The folder to which we are publishing.
   */
  public void postPub(File publishTo) {
    modifyView (saveIndex, VIEW_TRIGGER_SELECT_VIEW_FROM_MENU);
  }
  
  public void itemCopy (boolean itemDuplicate) {
    if (item == null) {
      trouble.report ("Select an Item before trying to copy it", 
          "No Item Selected");
    } else {
      TextLineWriter writer = new ClipboardMaker();
      WisdomIOFormats wisdomIO = prefsWindow.getTransferPrefs().getWisdomIO();
      WisdomIOFormat ioFormat 
          = wisdomIO.getSelectedFormat();
      if (itemDuplicate) {
        ioFormat = wisdomIO.get(WisdomIOFormats.TEXTBLOCK);
      }
      Exporter.itemFormat(
          getDiskStore(),
          getItem(),
          writer,
          new TabDelimFile(writer),
          ioFormat,
          prefsWindow.getTransferPrefs().getMarkdownLinksInline(),
          prefsWindow.getTransferPrefs().getAuthorLinksSeparate(),
          false);
    }
  }

  /**
   Look for one or more items on the clip board, and add them as new items,
   or merge them into existing ones.

   @param itemDuplicate If true, then we are explicitly duplicating an
                        existing item.
   */
  public void itemPasteNew (boolean itemDuplicate) {
    modIfChanged();
    int itemsAdded = 0;
    boolean ok = true;
    String blockText = "";
    clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable contents = clipBoard.getContents(null);
    boolean hasTransferableText = (contents != null) 
        && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasTransferableText) {
      try {
        blockText = (String)contents.getTransferData(DataFlavor.stringFlavor);
      } // end trying to get text from clipboard
      catch (UnsupportedFlavorException ex){
        //highly unlikely since we are using a standard DataFlavor
        ok = false;
        trouble.report ("Trouble getting data from Clipboard",
            "Clipboard Problem");
      }
      catch (IOException ex) {
        ok = false;
        trouble.report ("Trouble getting data from Clipboard",
            "Clipboard Problem");
      } // end catch io exception
      if (ok) {
        if (TextBlock.isTextBlockFormat (blockText, WisdomItem.OBJECT_NAME)) {
          TextBlock block = new TextBlock (blockText);
          while (! block.endOfText()) {
            WisdomItem priorItem = item;
            item = new WisdomItem ();
            int numberOfFields = item.setFromTextBlock (block);
            if (numberOfFields > 0) {
              if (itemDuplicate) {
                item.markAsDuplicate();
              }
              int i = items.add (item, true, false, itemDuplicate);
              itemsAdded++;
            } else { // end if number of fields > 0
              item = priorItem;
            }
          } // end while block has more text
        } // end if string is in TextBlock format
        else {
          WisdomItem priorItem = item;
          item = new WisdomItem ();
          int numberOfFields = item.setFromText (blockText);
          if (numberOfFields > 0) {
            int i = items.add (item, true, false, itemDuplicate);
            itemsAdded++;
          } else { // end if number of fields > 0
            item = priorItem;
          } // end if no fields found
        } // end if text is raw text
      } // end if ok so far
      
      if (itemsAdded == 0) {
        JOptionPane.showMessageDialog(tabs,
            "No items found",
            "Accept Results",
            JOptionPane.WARNING_MESSAGE);
      }
      else
      if (itemsAdded == 1) {
        JOptionPane.showMessageDialog(tabs,
            "One item added",
            "Accept Results",
            JOptionPane.INFORMATION_MESSAGE);
        displayItemNumber();
        activateItemTab();
        displayItem();
      }
      else
      if (itemsAdded > 1) {
        JOptionPane.showMessageDialog(tabs,
            String.valueOf(itemsAdded) + " items accepted",
            "Accept Results",
            JOptionPane.INFORMATION_MESSAGE);
        // activateListTab();
      }

    } // end if we've got anything to paste
    else {
      trouble.report ("Trouble getting data from Clipboard",
          "Clipboard Empty");
    }
  } // end method itemPasteNew

  /**
   Import one wisdom item into the collection. 

   @param oneItem The item to be imported.
   @param importFrom A string identifying the source of the imported item.
   */
  public void importOneItem (WisdomItem oneItem, String importFrom) {
    modIfChanged();
    if (oneItem.isEmpty()) {
      System.out.println ("iWisdomCommon importOneItem: item is empty");
      Logger.getShared().recordEvent (LogEvent.MEDIUM,
        "Item to be imported has no title and is considered empty",
        false);
    } else {
      item = oneItem;
      int i = items.add (item, true, false, false);
      JOptionPane.showMessageDialog(tabs,
          "One item added",
          "Accept Results",
          JOptionPane.INFORMATION_MESSAGE);
      Logger.getShared().recordEvent (LogEvent.NORMAL,
        "Imported quotation \"" + oneItem.getTitle()
        + "\" by " + oneItem.getAuthorCompleteName()
        + " from " + importFrom,
        false);
      displayItemNumber();
      activateItemTab();
      displayItem();
    } // end if item has title

  } // end method importOneItem
  
  public void lostOwnership (Clipboard clipBoard, Transferable contents) {
    clipBoardOwned = false;
  }
  
  public CategoryList getCategories () {
    return categories;
  }
  
  public Authors getAuthors () {
    return items.getAuthors();
  }
  
  public WisdomSources getSources () {
    return items.getSources();
  }
  
  public void setPrimary (boolean primary) {
    diskStore.setPrimary (primary);
    rememberLastFile();
    displayFile();
  }
  
  public void displayPublishWindow() {
    displayAuxiliaryWindow(publishWindow);
  }
  
  public void publishNow() {
    if (publishWindow != null) {
      publishWindow.publishNow();
    }
  }

  public void displayAuxiliaryWindow(WindowToManage window) {
    window.setLocation(
        frame.getX() + 60,
        frame.getY() + 60);
    WindowMenuManager.getShared().makeVisible(window);
  }
  
} // end class iWisdomCommon
