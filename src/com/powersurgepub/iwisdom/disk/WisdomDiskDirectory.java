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

  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import javax.swing.*;
  import java.util.*;

/**
   A directory of disk stores known to this user. 
 */
  
public class WisdomDiskDirectory {
      
  private static final String USER_NAME                   = "user.name";
  private static final String PRIMARY_DISK_STORE          = "primary.disk.store";
  private static final String RECENT_FILE_PREFIX          = "recent.file";
  private static final String RECENT_VIEW_PREFIX          = "recent.view";
  private static final String RECENT_TEMPLATE_PREFIX      = "recent.template";
  private static final String RECENT_PUBLISH_WHEN_PREFIX  = "recent.publish.when";
  private static final String WINDOWS_DOCS                = "My Documents";
  private static final String WINDOWS_WISDOM              = "My Wisdom";
  private static final String MAC_DOCS                    = "Documents";
  private static final String MAC_WISDOM                  = "Wisdom";
  
  private static final int    USER_PREFS_DISK_STORE_MAX   = 5;
  
  public  static final int    OPEN = 1;
  public  static final int    SAVE = 2;
  public  static final int    NEW  = 3;
      
  private FileOpener              opener;
  private String                  userName;
  private UserPrefs               userPrefs;
  private String                  primaryFileName;
  private WisdomDiskStore         primaryDiskStore;
  private WisdomDiskStore         lastDiskStore;
  private ArrayList               dirList;
  private JMenu                   recentFilesMenu;
  
  private ViewList                views;
  
  private Logger                  log;
  
  /**
   * 
   *    Creates a new instance of WisdomDiskDirectory. 
   *   
   *    @param opener Object that implements the FileOpener interface, so
   *                  that it is capable of opening a to do file from a
   *                  passed disk store.
   *   
   */
  public WisdomDiskDirectory (FileOpener opener, Logger log) {
    
    this.opener = opener;
    this.log = log;
    
    userName = System.getProperty (USER_NAME);
    
    dirList = new ArrayList();
    recentFilesMenu = new JMenu ("Open Recent");
    
    // If a primary file for this user is available from the user preferences,
    // then try to load the disk directory from this source; otherwise, try to 
    // load the disk directory directly from the user preferences.
    
    userPrefs = UserPrefs.getShared();
    boolean dirLoaded = false;
    WisdomDiskStore nextStore;
    
    primaryFileName = userPrefs.getPref (userName + "." + PRIMARY_DISK_STORE);
    if (primaryFileName != null
        && primaryFileName.length() > 0) {
      log.recordEvent (LogEvent.NORMAL, 
          "Primary Wisdom Collection = " + primaryFileName,
          false);
      primaryDiskStore = makeDiskStore (new File (primaryFileName));
      primaryDiskStore.setPrimary (true);
      // remember (primaryDiskStore);
      dirLoaded = loadDiskDir (primaryDiskStore);
    } // end if primary disk store available from user preferences
    
    // If we couldn't load the disk directory from the primary disk store,
    // then try to obtain the list directly from the user preferences
    if (! dirLoaded) {
      for (int i = 0; i < USER_PREFS_DISK_STORE_MAX; i++) {
        String recentFileName = getUserPrefFile (i);
        String recentFileView = getUserPrefView (i);
        String templateFileName = getUserPrefTemplate (i);
        int publishWhen = getUserPrefPublishWhen (i);
        if (recentFileName != null
            && recentFileName.length() > 0) {
          File recentFile = new File (recentFileName);
          nextStore = makeDiskStore (recentFile);
          nextStore.setCompareOption (recentFileView);
          if (templateFileName != null 
              && templateFileName.length() > 0) {
            File templateFile = new File (templateFileName);
            nextStore.setTemplate (templateFile);
            nextStore.setPublishWhen (publishWhen);
          }
          // remember (nextStore);
          if (i == 0) {
            lastDiskStore = nextStore;
          }
        } // end if we have a file name
      } // end for every recent file stored in user prefs
    } // end if directory not loaded from primary disk store
    
    views = new ViewList (this);
    if (primaryDiskStore == null) {
      views.setViewIndex 
          (0, iWisdomCommon.VIEW_TRIGGER_NEW_FILE);
    } else {
      views.setViewIndex 
          (primaryDiskStore.getViewIndex(), iWisdomCommon.VIEW_TRIGGER_SELECT_FILE);
    }
    
  } // end constructor
  
  public void setLog (Logger log) {
    this.log = log;
  }
  
  public Logger getLog () {
    return log;
  }
  
  /**
   *    Method that returns an instance of WisdomFile
   *    or WisdomFolder, depending on whether the passed data record
   *    refers to a file or a directory.
   *   
   *    @return An instance of WisdomFile if the input is a normal file,
   *            an instance of WisdomFolder if the input is a directory,
   *            or null if the input is neither a normal file or a directory.
   *   
   *    @param  storeRec A data record defining a iWisdom Disk Store.
   */
  public WisdomDiskStore makeDiskStore (DataRecord storeRec) {
    
    WisdomDiskStore store 
        = makeDiskStore (new File (storeRec.getFieldData (WisdomDiskStore.FILE)));
    store.setMultiple (storeRec);
    return store;
    
  }
  
  /**
   *    Method that returns an instance of WisdomFile
   *    or WisdomFolder, depending on whether the passed file
   *    refers to a file or a directory.
   *   
   *    @return An instance of WisdomFile if the input is a normal file,
   *            an instance of WisdomFolder if the input is a directory,
   *            or null if the input is neither a normal file or a directory.
   *   
   *    @param  file A file or directory that has been selected by the user
   *                 as a location to use for storing TwoDue data.
   */
  public WisdomDiskStore makeDiskStore (File file) {
    
    WisdomDiskStore store = new WisdomDiskStore (file);
    
    if (store != null && store.isValid()) {
      store.setLog (log);
      remember (store);
    }
    
    return store;
  } // end method
  
  /**
    Method to ask the user to choose a disk location for a wisdom folder. 
   
    @return A disk storage location.
   
    @param option Indicates whether an existing file/folder is to be opened,
                  or a new one created. 
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public WisdomDiskStore chooseDiskStore 
      (int option, JFrame frame, WisdomDiskStore store) {

    if (option == OPEN) {
      return openDiskStore (frame, store);
    }
    else
    if (option == SAVE) {
      return saveDiskStore (frame, store);
    }
    else
    if (option == NEW) {
      return newDiskStore (frame, store);
    } else {
      return null;
    }
  } // end method
  
  /**
    Method to ask the user to choose an existing disk location (file or folder) 
    to open.
   
    @return A disk storage location.
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public WisdomDiskStore openDiskStore 
      (JFrame frame, WisdomDiskStore store) {
    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
    if (store != null
        && (! store.isUnknown())) {
      chooser.setCurrentDirectory (store.getFile());
    }
    File result = chooser.showOpenDialog (frame);
    if (result != null) {
      File openFile = chooser.getSelectedFile();
      WisdomDiskStore openStore = get (openFile);
      if (openStore == null) {
        openStore = makeDiskStore (openFile);
        if (openStore.isPrimary()) {
          loadDiskDir (openStore);
        }
      }
      return openStore;
    }
    return null;
  } // end method
  
  /**
    Loads the directory of disk files from a previously stored disk file.
   
    @return True if the passed Disk Store's disk directory was read
            successfully.
   
    @param  The disk store from which the saved disk directory is to be read.
   */
  private boolean loadDiskDir (WisdomDiskStore diskStore) {
    boolean dirLoaded = false;
    WisdomDiskStore nextStore;
    if (diskStore.isDiskDirFileValidInput()) {
      dirLoaded = true;
      TabDelimFile dir = diskStore.getDiskDirTDF();
      log.recordEvent (LogEvent.NORMAL, 
          "Known files source = " + diskStore.getDiskDirFile().toString(),
          false);
      dir.setLog (log);
      DataRecord next = null;
      File nextFile = null;
      try {
        dir.openForInput();
      } catch (IOException e) {
        System.out.println ("Trouble opening Disk Directory for "
            + diskStore.toString());
        dirLoaded = false;
      }
      while (! dir.isAtEnd()) {
        try {
          next = dir.nextRecordIn();
        } catch (IOException e) {
          System.out.println ("Trouble reading Disk Directory for "
              + diskStore.toString());
          dirLoaded = false;
        }
        if (next != null) {
          nextFile = new File (next.getFieldData (WisdomDiskStore.FILE));
          if (primaryDiskStore.equals (nextFile)) {
            primaryDiskStore.setMultiple (next);
          } else {
            nextStore = makeDiskStore (next);
            // remember (nextStore);
          }
        } // end if next data rec not null
      } // end while more items in data source
    } // end if disk directory file available
    return dirLoaded;
  } // end method loadDiskDir
  
  /**
    Method to ask the user to choose a new disk location (folder only) 
    for a list of wisdom items.
   
    @return A new disk storage location.
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public WisdomDiskStore saveDiskStore 
      (JFrame frame, WisdomDiskStore store) {
        
    Trouble trouble = Trouble.getShared();
        
    String newFolderDefault = "";
    File folder = new File(System.getProperty (GlobalConstants.USER_DIR));
    File home = new File(System.getProperty (GlobalConstants.USER_HOME)); 
    File docs = new File(home, "My Documents");
    if (docs == null 
        || (! docs.exists())) {
      docs = new File(home, "Documents");
    }
    WisdomDiskStore newStore = null;

    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.FILES_AND_DIRECTORIES); 
    if (store != null
        && (! store.isUnknown())) {
      chooser.setCurrentDirectory (store.getFile());
    } 
    else
    if (docs != null
        && docs.exists()
        && docs.isDirectory()
        && docs.canRead()) {
      chooser.setCurrentDirectory (docs);
      newFolderDefault = "iWisdom";
    } else {
      chooser.setCurrentDirectory (home);
      newFolderDefault = "iWisdom";
    }
    int progress = 1;
    int fileFolder = 0;
    int result = 0;
    
    while (progress > 0 && progress < 9) {
      switch (progress) {
        
        case 1:
          // See if user wants to save as a file or a folder
          Object[] fileFolderOptions = { "Folder", "File", "Cancel" };
          fileFolder = JOptionPane.showOptionDialog
              (frame, 
              "Save As a File or a Folder?"
              + GlobalConstants.LINE_FEED
              + GlobalConstants.LINE_FEED
              + "(Folders are recommended)", 
              "Save As / Step 1",
              JOptionPane.YES_NO_CANCEL_OPTION, 
              JOptionPane.QUESTION_MESSAGE,
              null, fileFolderOptions, fileFolderOptions[0]);
          switch (fileFolder) {
            case 0:
              // Save as folder
              progress = 2;
              break;
            case 1:
              // Save as file
              progress = 8;
              break;
            case 2:
              // Cancel 
              progress = 0;
              break;
          }
          break;
          
        case 2:
          // Save as a folder -- let user choose closest parent
          // or actual target
          chooser.setFileSelectionMode (XFileChooser.DIRECTORIES_ONLY);
          chooser.setDialogTitle ("Save As / Step 2 of 3 / Specify Parent or Target Folder");
          folder = chooser.showOpenDialog (frame);
          if (folder != null) {
            progress = 3;
          } else {
            progress = 1;
          }
          break;
          
        case 3:
          // See if user wants to create a new folder
          File testFolder = null;
          String startingDefaultFolder = newFolderDefault;
          String suffix = "";
          int counter = 0;
          do {
            newFolderDefault = startingDefaultFolder + suffix;
            testFolder = new File (folder, newFolderDefault);
            counter++;
            suffix = String.valueOf(counter).trim();
          } while (testFolder.exists());
          String newFolderString = (String)JOptionPane.showInputDialog (
              frame,
              "Specify new folder, or leave blank to use as is:"
              + GlobalConstants.LINE_FEED
              + folder.toString(),
              // "Save As / Step 3 of 3 / Optionally Specify New Folder",
              // JOptionPane.QUESTION_MESSAGE
              newFolderDefault
              );
          if (newFolderString == null) {
            progress--;
          } 
          else 
          if (newFolderString.length() > 0) {
            File newFolder = new File (folder, newFolderString);
            try {
              if (newFolder.isDirectory()) {
                // use as is
                folder = newFolder;
                progress = 9;
              } else {
                boolean ok = newFolder.mkdir();
                if (ok) {
                  folder = newFolder;
                  progress = 9;
                } else {
                  trouble.report ("Trouble creating new folder", "Mkdir Problem");
                  progress--;
                }
              }
            } catch (SecurityException sx) {
              trouble.report ("Access Denied", "Security Problem");
              progress--;
            }
          } else {
            // Use existing folder as specified
            progress = 9;
          }
          break;
          
        case 8:
          // Save as a file
          chooser.setFileSelectionMode (XFileChooser.FILES_ONLY);
          chooser.setDialogTitle ("Save As / Step 2 of 2 / Specify New Output File");
          File file = chooser.showSaveDialog (frame);
          if (file != null) {
            FileName fileName = new FileName (file);
            if (fileName.getExt().length() == 0) {
              file = new File (fileName.getPath() 
                + "/" + fileName.replaceExt (WisdomDiskStore.FILE_EXT));
            }
            newStore = makeDiskStore (chooser.getSelectedFile());
            progress = 9;
          } else {
            progress = 1;
          }
          break;
      } // end primary switch
    } // end while loop
    
    if (progress > 0) {
      if (fileFolder == 0) {
        newStore = makeDiskStore (folder);
      }
      newStore.setCompareOption (store.getCompareString());
      newStore.setSelectOption  (store.getSelectString());
      newStore.setPublishWhen (store.getPublishWhen());
      newStore.setTemplate (store.getTemplate());
    }
    
    return newStore;
    
  } // end method

  
  /**
    Method to ask the user to choose a new disk location for a wisdom folder.
   
    @return A new disk storage location.
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public WisdomDiskStore newDiskStore 
      (JFrame frame, WisdomDiskStore store) {
        
    Trouble trouble = Trouble.getShared();
        
    String newFolderDefault = "";
    File folder = new File(System.getProperty (GlobalConstants.USER_DIR));
    File home = new File(System.getProperty (GlobalConstants.USER_HOME)); 
    WisdomDiskStore newStore = null;
    
    File docs = new File(home, WINDOWS_DOCS);
    File wisdom = new File(home, WINDOWS_WISDOM);
    if (docs == null 
        || (! docs.exists())) {
      docs = new File(home, MAC_DOCS);
      wisdom = new File(home, MAC_WISDOM);
    }

    File iWisdom = new File (wisdom, "iWisdom");

    XFileChooser chooser = new XFileChooser ();
    chooser.setFileSelectionMode(XFileChooser.DIRECTORIES_ONLY); 
    if (wisdom != null
        && wisdom.exists()) {
      chooser.setCurrentDirectory (wisdom);
    }
    else
    if (store != null
        && (! store.isUnknown())) {
      chooser.setCurrentDirectory (store.getFile());
    } else {
      chooser.setCurrentDirectory (home);
      // newFolderDefault = "Wisdom/iWisdom";
    }
    
    chooser.setDialogTitle ("Designate a new Wisdom Directory");
    folder = chooser.showSaveDialog(frame);
    
    /*
    int progress = 2;
    int fileFolder = 0;
    int result = 0;
    
    while (progress > 0 && progress < 9) {
      switch (progress) {
          
        case 2:
          // Save as a folder -- let user choose closest parent
          // or actual target
          chooser.setFileSelectionMode (XFileChooser.DIRECTORIES_ONLY);
          chooser.setDialogTitle ("Save As / Step 1 of 2 / Specify Parent or Target Folder");
          folder = chooser.showOpenDialog (frame);
          if (folder != null) {
            progress = 3;
          } else {
            progress = 0;
          }
          break;
          
        case 3:
          // See if user wants to create a new folder
          File testFolder = null;
          String startingDefaultFolder = newFolderDefault;
          String suffix = "";
          int counter = 0;
          do {
            newFolderDefault = startingDefaultFolder + suffix;
            testFolder = new File (folder, newFolderDefault);
            counter++;
            suffix = String.valueOf(counter).trim();
          } while (testFolder.exists());
          String newFolderString = (String)JOptionPane.showInputDialog (
              frame,
              "Specify new folder, or leave blank to use as is:"
              + GlobalConstants.LINE_FEED
              + folder.toString(),
              // "Save As / Step 3 of 3 / Optionally Specify New Folder",
              // JOptionPane.QUESTION_MESSAGE
              newFolderDefault
              );
          if (newFolderString == null) {
            progress--;
          } 
          else 
          if (newFolderString.length() > 0) {
            File newFolder = new File (folder, newFolderString);
            try {
              if (newFolder.isDirectory()) {
                // use as is
                folder = newFolder;
                progress = 9;
              } else {
                boolean ok = newFolder.mkdir();
                if (ok) {
                  folder = newFolder;
                  progress = 9;
                } else {
                  trouble.report ("Trouble creating new folder", "Mkdir Problem");
                  progress--;
                }
              }
            } catch (SecurityException sx) {
              trouble.report ("Access Denied", "Security Problem");
              progress--;
            }
          } else {
            // Use existing folder as specified
            progress = 9;
          }
          break;
          
      } // end primary switch
    } // end while loop
    
    if (progress > 0) {
      if (fileFolder == 0) {
        newStore = makeDiskStore (folder);
      }
      newStore.setCompareOption (store.getCompareString());
      newStore.setSelectOption  (store.getSelectString());
      newStore.setPublishWhen (store.getPublishWhen());
      newStore.setTemplate (store.getTemplate());
    }
    */
    
    newStore = makeDiskStore (folder);
    // newStore.applyDefaultTheme();
    
    newStore.setCompareOption (store.getCompareString());
    newStore.setSelectOption  (store.getSelectString());
    newStore.setPublishWhen (store.getPublishWhen());
    newStore.setTemplate (store.getTemplate());
    
    return newStore;
    
  } // end method
  
  /**
    Method to ask the user to choose a new disk location for a wisdom folder.
   
    @return A new disk storage location.
   
    @param frame  Frame providing the context for the file chooser. 
   
    @param store  The existing disk storage location to use as a starting
                  point for the user's navigation, and for existing
                  comparator and selector options. 
   */
  public WisdomDiskStore getUsualLocation () {
        
    Trouble trouble = Trouble.getShared();
        
    File home = new File(System.getProperty (GlobalConstants.USER_HOME)); 
    WisdomDiskStore newStore = null;
    File docs = new File(home, WINDOWS_DOCS);
    File wisdom = new File(home, WINDOWS_WISDOM);
    if (docs == null 
        || (! docs.exists())) {
      docs = new File(home, MAC_DOCS);
      wisdom = new File(home, MAC_WISDOM);
    }
    if (! wisdom.exists()) {
      try {
        boolean ok = wisdom.mkdir();
        if (! ok) {
          trouble.report ("Trouble creating new folder " + wisdom, "Mkdir Problem");
          return newStore;
        }
      } catch (SecurityException sx) {
        trouble.report ("Access Denied trying to create new folder " + wisdom, 
            "Security Problem");
        return newStore;
      }
    }
    File iWisdom = new File (wisdom, "iWisdom");
    if (! iWisdom.exists()) {
      try {
        boolean ok = iWisdom.mkdir();
        if (! ok) {
          trouble.report ("Trouble creating new folder " + iWisdom, "Mkdir Problem");
          return newStore;
        }
      } catch (SecurityException sx) {
        trouble.report ("Access Denied trying to create new folder " + iWisdom, 
            "Security Problem");
        return newStore;
      }
    }
    newStore = makeDiskStore (iWisdom);
    
    return newStore;
    
  } // end method
  
  /**
    Get a JMenu object of all known files (files in the directory list).
   
    @return Menu of all known files.
   */
  public JMenu getRecentFilesMenu () {
    return recentFilesMenu;
  }

  /**
    Make sure this disk store
    is saved in the directory list. 
   
    @param store Disk store to be added, or used to update an existing 
                 disk store already in the list.
  */
  public void remember (WisdomDiskStore store) {
    int i = 0;
    if (store.isForgettable()) {
      // don't remember it if we said to forget about it
    } else {
      if (store.isAFolder()
          && primaryDiskStore == null) {
        store.setPrimary (true);
      }
      i = 0;
      while (i < dirList.size()
         && (store.compareTo(get(i)) > 0)) {
       i++;
      }
      if (i >= dirList.size()) {
        dirList.add (store);
        recentFilesMenu.add (makeMenuItem (store));
      }
      else
      if (store.compareTo(get(i)) < 0) {
        dirList.add (i, store);
        recentFilesMenu.insert (makeMenuItem (store), i);
      } 
      /*
      else {
        WisdomDiskStore old = get(i);
        old.update(store);
      }
      */
      if (store.isPrimary()) {
        if (primaryDiskStore == null) {
          primaryDiskStore = store;
        }
        else
        if (! primaryDiskStore.equals (store)) {
          primaryDiskStore.setPrimary (false);
          primaryDiskStore = store;
        } // end if primary disk store points to a different disk location
        for (i = 0; i < dirList.size(); i++) {
          JMenuItem menuItem = recentFilesMenu.getItem(i);
          menuItem.setText(get(i).getShortPath());
        }
      } // end if this store claims to be primary
    } // end if this disk store not forgettable
  } // end method remember
   
  /**
    Make a new Menu item for a new disk store.
   
    @param  store Definition of recent disk store accessed.
   */
  private JMenuItem makeMenuItem (WisdomDiskStore store) {
    
    JMenuItem storeMenuItem = new JMenuItem(store.getShortPath());
    storeMenuItem.setActionCommand (store.getPath());
    storeMenuItem.setToolTipText (store.getPath());
    storeMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fileRecentMenuItemActionPerformed(evt);
      }
    });
    return storeMenuItem;   
  } // end method
   
   /**
    Drop this disk store from the directory list. 
   
    @param store Disk store to be added, or used to update an existing 
                 disk store already in the list.
   */
   public void forget (WisdomDiskStore store) {
     int i = 0;
     while (i < dirList.size()
        && (store.compareTo(get(i)) > 0)) {
      i++;
     }
     if (i < dirList.size()
        && store.equals (get(i))) {
       dirList.remove (i);
       recentFilesMenu.remove (i);
     }

     if ((primaryDiskStore != null)
           && (primaryDiskStore.equals (store))) {
         primaryDiskStore = null;
     } // end if this store was primary
   } // end method forget
  
   /**
      Save disk directory to disk (either as a tab-delimited file, or
      in user prefs).
    */
  public void saveToDisk () {

    if (primaryDiskStore == null) {
      for (int i = 0; i < USER_PREFS_DISK_STORE_MAX; i++) {
        if (i < dirList.size()
            && (! get(i).isForgettable())) {
          setUserPrefFile (get (i), i);
        } // end if disk store is available
      } // end for each disk store slot available in user prefs
      userPrefs.savePrefs();
    } else { // primary disk store is available
      WisdomDiskStore nextStore;
      DataRecord nextRec;
      RecordDefinition recDef = WisdomDiskStore.getRecDef();
      boolean ok = true;
      TabDelimFile dir = primaryDiskStore.getDiskDirTDF();
      try {
        dir.openForOutput (recDef);
      } catch (IOException e) {
        ok = false;
      }
      if (ok) {
        for (int i = 0; i < dirList.size(); i++) {
          nextStore = get (i);
          if (! get(i).isForgettable()) {
            nextRec = nextStore.getDataRec (recDef);
            try {
              dir.nextRecordOut (nextRec);
            } catch (IOException e2) {
              ok = false;
            }
          }
        } // end for loop
      } // end if open ok
      try {
        dir.close();
      } catch (IOException e) {
      }
      if (ok) {
        userPrefs.setPref (
          userName + "." + PRIMARY_DISK_STORE, 
          primaryDiskStore.getPath());
      }
      
      views.saveViews();
      
    } // end if primary disk store is available
    
  } // end saveToDisk method
  
  /**
    Get default data store -- primary, if one is identified, or first one 
    loaded from user prefs, or first file in list.
   
    @return Default data store to be opened automatically at startup.
   */
  public WisdomDiskStore getDefaultStore() {
    if (primaryDiskStore != null) {
      return primaryDiskStore;
    }
    else
    if (lastDiskStore != null) {
      return lastDiskStore;
    }
    else
    if (dirList.size() > 0) {
      return get(0);
    } else {
      return null;
    }
  }
  
  /**
    Get primary file.
   
    @return Primary to do disk store for this user, or null if none available.
   */
  public WisdomDiskStore getPrimaryStore() {
    return primaryDiskStore;
  }
  
  /**
    Get disk store, given a File identifier.
   
    @return The disk store with this path in the directory list, or 
            null if no known disk store has this path.
   
    @param  file Pointer to the disk store location.
   */
  public WisdomDiskStore get (File file) {
    int i = 0;
    while (i < dirList.size()
        && (! (get(i).equals(file)))) {
      i++;
    }
    if (i < dirList.size()) {
      return get(i);
    } else {
      return null;
    }
  }
  
  /**
    Get disk store, given a disk store path.
   
    @return The disk store with this path in the directory list, or 
            null if no known disk store has this path.
   
    @param  path Path to the disk store location.
   */
  public WisdomDiskStore get (String path) {
    int i = 0;
    while (i < dirList.size()
        && (! ((get(i)).getPath().equals(path)))) {
      i++;
    }
    if (i < dirList.size()) {
      return get(i);
    } else {
      return null;
    }
  }
  
  /**
    Get disk store, given an index value.
   
    @return The disk store at this location in the directory list, or 
            null if the index does not point to a valid location.
   
    @param  index Index value pointing to a location in the directory list.
   */
  public WisdomDiskStore get (int index) {
    if (index < 0 || index >= dirList.size()) {
      return null;
    } else {
      return (WisdomDiskStore)dirList.get(index);
    }
  }
  
  /**
    Get size of directory (number of disk stores).
   
    @return Number of disk stores in the directory.
   */
  public int size() {
    return dirList.size();
  }
  
  /** 
    Get recent file from user prefs, given an index value.
   
    @return Name of file from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private String getUserPrefFile (int index) {
    String digit = String.valueOf(index).trim();
    return userPrefs.getPref (RECENT_FILE_PREFIX + digit);
  }
  
  /** 
    Get recent view options from user prefs, given an index value.
   
    @return View options, from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private String getUserPrefView (int index) {
    String digit = String.valueOf(index).trim();
    return userPrefs.getPref (RECENT_VIEW_PREFIX + digit);
  }
  
  public ViewList getViews () {
    return views;
  }
  
  /** 
    Get template for recent file from user prefs, given an index value.
   
    @return Name of template file from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private String getUserPrefTemplate (int index) {
    String digit = String.valueOf(index).trim();
    return userPrefs.getPref (RECENT_TEMPLATE_PREFIX + digit);
  }
  
  /** 
    Get publishing frequency for recent file from user prefs, 
    given an index value.
   
    @return Indicator of publishing frequency from user prefs.
    @param  index Pointer to an entry on recent files list. 
   */
  private int getUserPrefPublishWhen (int index) {
    String digit = String.valueOf(index).trim();
    String pubStr = userPrefs.getPref (RECENT_PUBLISH_WHEN_PREFIX + digit);
    int pub;
    try {
      pub = Integer.parseInt (pubStr);
    } catch (NumberFormatException e) {
      pub = 0;
    }
    return pub;
  }
  
  /**
    Set the contents of the user preferences, based on the contents
    of a disk store object.
   
    @param store Disk Store to be saved in user preferences.
    @param index Index position within user prefs to be used
                 for storage.
   */
  private void setUserPrefFile (WisdomDiskStore store, int index) {
    setUserPrefFile (store.getFile(), index);
    setUserPrefView (store.getComparator().toString(), index);
    setUserPrefTemplate (store.getTemplate(), index);
    setUserPrefPublishWhen (store.getPublishWhen(), index);
  }
  
  /** 
    Store recent file in user prefs, given an index value.
   
    @param  file  File definition of recent file accessed.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefFile (File file, int index) {
    String digit = String.valueOf(index).trim();
    userPrefs.setPref (RECENT_FILE_PREFIX + digit, file.getAbsolutePath());
  }
  
  /** 
    Store recent view options in user prefs, given an index value.
   
    @param  view  View options for recent file accessed.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefView (String view, int index) {
    String digit = String.valueOf(index).trim();
    userPrefs.setPref (RECENT_VIEW_PREFIX + digit, view);
  }
  
  /** 
    Store recent template in user prefs, given an index value.
   
    @param  template  File definition of recent template accessed.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefTemplate (File template, int index) {
    String digit = String.valueOf(index).trim();
    String templateName;
    if (template == null) {
      templateName = "";
    } else {
      templateName = template.getAbsolutePath();
    }
    userPrefs.setPref (RECENT_TEMPLATE_PREFIX + digit, templateName);
  }
  
  /** 
    Store recent publication frequency in user prefs, 
    given an index value.
   
    @param  publishWhen  Indicator of publishing frequency 
                         for recently accessed file.
    @param  index Pointer to an entry on recent files list. 
   */
  private void setUserPrefPublishWhen (int publishWhen, int index) {
    String digit = String.valueOf(index).trim();
    userPrefs.setPref (RECENT_PUBLISH_WHEN_PREFIX + digit, 
        String.valueOf(publishWhen));
  }
  
  /**
    Action listener for recent file menu items.
   
    @param evt = Action event.
   */
  private void fileRecentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    String path = evt.getActionCommand();
    WisdomDiskStore store = get (path);
    opener.handleOpenFile (store);
  } // end method
  
} // end class

