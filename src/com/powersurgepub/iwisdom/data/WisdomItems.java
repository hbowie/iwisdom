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

package com.powersurgepub.iwisdom.data;

  import com.powersurgepub.psdatalib.psdata.*;
	import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.*;
  import java.io.*;
  import java.text.*;
  import java.util.*;
  
/**
   A collection of wisdom items. New items are added to the end of the
   table. Deleted records are flagged as deleted, but are not removed
   from the table. An index pointing to an item in the table should always
   point to the same item (it should not move).
 */

public class WisdomItems 
        implements 
          DataSource {
      
  private	ArrayList       items;

  private boolean         demoLimitExceeded = false;
  
  private int             itemIndex = 0;
  
  private int             highestItemID = 0;
  
  private int             consideredCount = 0;
  
  private int             addedCount = 0;
  
  private int             mergedCount = 0;
  
  private RecordDefinition recDef;
  
  private int             dsIndex = 0;
  
  private Debug           debug   = new Debug(false);
  
  private Logger          logger     = Logger.getShared();
  
  private ArrayList       views;
  
  private iWisdomCommon    td;
  
  private Authors          authors   = new Authors();
  
  private WisdomSources    sources   = new WisdomSources();

	/**
	 * 	   Constructor with minimal arguments.
	 *   
	 *     @param td    Instance of iWisdomCommon that can be used 
	 *                  to anchor alert dialogs.
	 */
	public WisdomItems (iWisdomCommon td) {
    this.td = td;
    recDef = WisdomItem.getRecDef();
		items = new ArrayList();
    views = new ArrayList();
    authors = new Authors();
    sources = new WisdomSources();
	}
  
	/**
	 * 	   Constructor with data source argument.
	 *     
	 *     @param fmt   A DateFormat instance to be used to parse date strings.
	 *     @param td    Instance of iWisdomCommon that can be used 
	 *                  to anchor alert dialogs.
	 *     @param items A collection of items to be added to this collection. 
	 */
	public WisdomItems (IDListHeader header, DateFormat fmt, iWisdomCommon td, 
      DataSource items, boolean respectItemID) 
        throws 
          IOException {
    super();
    this.td = td;
    recDef = WisdomItem.getRecDef();
    this.items = new ArrayList();
    authors = new Authors();
    sources = new WisdomSources();
    views = new ArrayList();
		addAll (items, respectItemID);
	}
  
  /**
   *    Add another view of the data to be kept synchronized with the 
   *    underlying collection of WisdomItems.
   *   
   *    @param view Another ItemsView to be added.
   */
  public void addView (ItemsView view) {
    boolean ok = views.add (view);
    view.setItems (this);
    for (int i = 0; i < items.size(); i++) {
      view.add ((WisdomItem)items.get(i));
    }
  }
  
  /**
    Adds another collection of items to this collection.
    
    @return True if the items were added successfully.
    @param  items A collection of items to be added to this collection.
   */
  public boolean addAll (DataSource items, boolean respectItemID) 
      throws IOException {
    boolean ok = true;
    DataRecord next;
    int addedAt = 0;
    items.openForInput();
    while (! items.isAtEnd()) {
      next = items.nextRecordIn();
      if (next != null) {
        WisdomItem newItem = new WisdomItem (next);
        if (newItem.getBody().length() > 0
            && newItem.getTitle().length() > 0) {
          addedAt = add (newItem, true, respectItemID, false);
          if (addedAt < 0) {
            ok = false;
          } // end if last item not added successfully
        }
      } // end if next data rec not null
    } // end while more items in data source
    return ok;
  } // end addAll method
  
  public void resetCounts () {
    resetConsideredCount();
    resetMergedCount();
    resetAddedCount();
  }
  
  public void resetConsideredCount () {
    consideredCount = 0;
  }
  
  public void resetMergedCount () {
    mergedCount = 0;
  }
  
  public void resetAddedCount () {
    addedCount = 0;
  }
  
  public int getConsideredCount () {
    return consideredCount;
  }
  
  public int getMergedCount () {
    return mergedCount;
  }
  
  public int getAddedCount () {
    return addedCount;
  }

  public boolean wasDemoLimitExceeded() {
    return demoLimitExceeded;
  }

  /**
    Adds another wisdom item to the collection.
    
    @return Index position of item after add.
    
    @param item The item to be added to the collection.
   
    @param saveToDisk
   
    @param respectItemID Should Item ID be respected, or assigned as new? When
                         loading items from a primary data store, existing item 
                         ids should be preserved; when importing/adding new 
                         items to an existing collection, existing ids should 
                         be ignored (so that a new one will be assigned).

    @param itemDuplicate If true, force this item to be added as a duplicate,
                         instead of being merged with any existing item that
                         has the same body content.
   */
  public int add (
      WisdomItem newItem,
      boolean saveToDisk,
      boolean respectItemID,
      boolean itemDuplicate) {

    // Make sure we are not adding a duplicate item.
    // If we are, merge the two items together instead of creating a new one. 
    
    consideredCount++;
    int index = -1;
    boolean ok = true;
    String priorAuthorFileName = "";
    String priorSourceFileName = "";
    String priorTitleFileName = "";
    WisdomItem existingItem = null;
    
    // Adjust any item content that we can based on known information
    newItem.ensureTitle();
    newItem.assignCategoryIfBlank();
    if (! respectItemID) {
      newItem.setItemID (0);
    }
    
    checkAuthor(newItem);
    checkSource (newItem);
    
    // Look through current items, to see if another exists that has the same
    // words, or the same title. 
    String updatedFields = "";
    String t = newItem.getTitleFileName();
    boolean merged = false;
    boolean added = false;
    for (int i = 0; ((i < size()) && (! merged)); i++) {
      existingItem = get (i);
      if (existingItem.hasSameWordsAs (newItem) && (! itemDuplicate)) {
        priorAuthorFileName = existingItem.getAuthorFileName();
        priorSourceFileName = existingItem.getSourceFileName();
        priorTitleFileName = existingItem.getTitleFileName();
        updatedFields = existingItem.merge (newItem);
        if (existingItem.isDeleted()) {
          existingItem.setDeleted (false);
          ensureItemID (existingItem);
          added = true;
          addedCount++;
          logger.recordEvent(LogEvent.NORMAL,
            "Added " + String.valueOf (existingItem.getItemID())
            + ": " + existingItem.getTitle(), false);
        }
        merged = true;
        index = i;
        if (updatedFields.length() > 0) {
          if (! added) {
            mergedCount++;
          }
          logger.recordEvent (
              LogEvent.NORMAL,
              existingItem.getTitle() + " had the following fields updated: " + updatedFields,
              false);
        } else {
          logger.recordEvent (
              LogEvent.MINOR,
              "The new item was merged with existing item " + existingItem.getTitle(),
              false);
        }
      } else {
        if ((existingItem.getTitleText().equalsIgnoreCase (newItem.getTitleText()))
            || (existingItem.getTitleFileName().equals (t))) {
          if (newItem.getTitleSuffix() <= existingItem.getTitleSuffix()) {
            newItem.setTitleSuffix (existingItem.getTitleSuffix() + 1);
          }
        } // end if titles are the same
      } // end if not merged with current item
    } // end while still looking

    if (merged) {
      if (ok && saveToDisk) {
        modify (existingItem);
      }
    } else {
      ensureItemID (newItem);
      ok = items.add (newItem);
      if (ok) {
        index = items.size() - 1;
        newItem.setItemNumber (index);
        newItem.setCommon (td);
        addedCount++;
        if (saveToDisk) {
          td.getDiskStore().save (newItem);
        } // end if saving to disk
        if (! respectItemID) {
          logger.recordEvent(LogEvent.NORMAL, 
              "Added " + String.valueOf (newItem.getItemID()) 
              + ": " + newItem.getTitle(), false);
        }
        // Update Views
        for (int v = 0; v < views.size(); v++) {
          ItemsView view = (ItemsView)views.get(v);
          view.add (newItem);
        } // end for each view
      } // end if add was ok
    } // end if added, not merged

    return index;
  } // end method add
  
  private void ensureItemID (WisdomItem item) {
    if (item.getItemID() < 1) {
      highestItemID++;
      item.setItemID (highestItemID);
    }
    else
    if (item.getItemID() > highestItemID) {
      highestItemID = item.getItemID();
    }
  }
  
  /**
    Indicates that an item in the list has been modified.
    
    @param item The item being modified.
   */
  public void modify (WisdomItem item) {
    
    checkAuthor(item);
    checkSource (item);
    
    td.getDiskStore().save (item);
    for (int v = 0; v < views.size(); v++) {
      ItemsView view = (ItemsView)views.get(v);
      view.modify (item);
    }
  } // end method
  
  /**
   Make sure we have only one author per author name.
   
   @param item Item whose author is to be checked against current
               author list. 
   */
  private void checkAuthor (WisdomItem item) {
    Author authorFromItem = item.getAuthor();
    int authorIndex = authors.indexOf (authorFromItem);
    if (authorIndex < 0) {
      authors.add (authorFromItem);
    } else {
      Author authorFromList = authors.get (authorIndex);
      authorFromList.merge (authorFromItem);
      item.setAuthor (authorFromList);
    }
  }
  
  /**
   Make sure we have only one source per title.
   
   @param item Item whose source is to be checked against current
               source list. 
   */
  private void checkSource (WisdomItem item) {
    WisdomSource sourceFromItem = item.getSource();
    int sourceIndex = sources.indexOf (sourceFromItem);
    if (sourceIndex < 0) {
      sources.add (sourceFromItem);
    } else {
      WisdomSource sourceFromList = sources.get (sourceIndex);
      sourceFromList.merge (sourceFromItem);
      item.setSource (sourceFromList);
    }
  }
  
  public void remove (WisdomItem item) {
    
    td.getDiskStore().remove (item);
    item.setDeleted();
    for (int v = 0; v < views.size(); v++) {
      ItemsView view = (ItemsView)views.get(v);
      view.remove (item);
    }
  }
  
  /**
    Gets this collection of items and passes it to a data store, dropping
    deleted items.
    
    @return True if the items were stored successfully.
    @param items A data store to receive the collection.
   */
  public boolean getAll (DataStore dataOut) 
      throws IOException {
        
    boolean ok = true;
        
    // Build the record definition
    try {
      dataOut.openForOutput (recDef);
    } catch (IOException e) {
      ok = false;
    }
    
    if (ok) {
      WisdomItem nextItem;
      for (itemIndex = 0; itemIndex < size(); itemIndex++) {
        nextItem = get (itemIndex);
        if (! nextItem.isDeleted()) {
          DataRecord nextRec = nextItem.getDataRec (recDef);
          try {
            dataOut.nextRecordOut (nextRec);
          } catch (IOException e2) {
            ok = false;
          }
        } // end if not deleted
      } // end for loop
    } // end if open ok
    dataOut.close();
    return ok;
  } // end getAll method
  
  public int getItemIndex() {
    return itemIndex;
  }
  
  /**
   Return the index location of the current item (if any) that would have
   an identical file path to the identified item. 
   */
  public int get (WisdomItem item) {
    boolean found = false;
    int i = 0;
    String a = item.getAuthorFileName();
    String s = item.getSourceFileName();
    String t = item.getTitleFileName();
    while ((! found) && (i < size())) {
      WisdomItem item2 = get (i);
      if (a.equals (item2.getAuthorFileName())
          && s.equals (item2.getSourceFileName())
          && t.equals (item2.getTitleFileName())) {
        found = true;
      } else {
        i++;
      } // end if not yet found
    } // end while still looking
    if (! found) {
      i = -1;
    }
    return i;
  } // end method get by key
  
   /**
    Gets a to do item from the collection.
    
    @return To do item.
    
    @param index The index to the desired position in the collection.
   */
  public WisdomItem get (int index) {
    if (index >= 0 && index < size()) {
      return (WisdomItem)items.get (index);
    } else {
      return null;
    }
  }
  
   /**
    Returns the size of the collection.
    
    @return Size of the collection.
   */
  public int size () {
    return items.size();
  }
  
  public Authors getAuthors () {
    return authors;
  }
  
  public WisdomSources getSources () {
    return sources;
  }
	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return "ToDoItems";
	}
  
  /*
    The following methods allow an object of this class to act
    as a DataSource.
   */
  
  /**
     Opens the reader for input.
    
     @param inDict A data dictionary to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (DataDictionary inDict)
      throws IOException {
    dsIndex = 0;
  }
      
  /**
     Opens the reader for input.
    
     @param inRecDef A record definition to use.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput (RecordDefinition inRecDef)
      throws IOException {
    dsIndex = 0;
  }
  
  /**
     Opens the reader for input.
    
     @throws IOException If there is trouble opening a disk file.
   */
  public void openForInput ()
      throws IOException {
    dsIndex = 0;
  }
  
  /**
     Returns the next input data record.
    
     @return Next data record.
    
     @throws IOException If reading from a source that might generate
                         these.
   */
  public DataRecord nextRecordIn () 
      throws IOException {
    if (isAtEnd()) {
      return null;
    } else {
      WisdomItem item;
      boolean goodRec = false;
      do {
        item = (WisdomItem)items.get (dsIndex);
        if (! item.isDeleted()) {
          goodRec = true;
        }
        dsIndex++;
      } while ((! isAtEnd()) && (item.isDeleted()));
      if (goodRec) {
        return item.getDataRec(recDef);
      }
    }
    return null;
  }
    
  /**
     Returns the record definition for the reader.
    
     @return Record definition.
   */
  public RecordDefinition getRecDef () {
    return recDef;
  }
  
  /**
     Returns the sequential record number of the last record returned.
    
     @return Sequential record number of the last record returned via 
             nextRecordIn, where 1 identifies the first record.
   */
  public int getRecordNumber () {
    return dsIndex;  
  }

  /**
     Indicates whether there are more records to return.
    
     @return True if no more records to return.
   */
  public boolean isAtEnd() {
    return (dsIndex >= items.size());
  }
  
  /**
     Closes the reader.
    
     @throws IOException If there is trouble closing the file.
   */
  public void close () 
      throws IOException {
  }
    
  /**
     Sets a log to be used by the reader to record events.
    
     @param  logger A logger object to use.
   */
  public void setLog (Logger logger) {
    this.logger = logger;
  }
  
  /**
     Sets the debug instance to the passed value.
    
     @param debug Debug instance. 
   */
  public void setDebug (Debug debug) {
    this.debug = debug;
  }
  
  /**
     Indicates whether all data records are to be logged.
    
     @param  dataLogging True if all data records are to be logged.
   */
  public void setDataLogging (boolean dataLogging) {
    logger.setLogAllData (dataLogging);
  }
  
  /**
     Sets a file ID to be used to identify this reader in the log.
    
     @param  fileId An identifier for this reader.
   */
  public void setFileId (String fileId) {
    
  }
  
  /**
     Sets the maximum directory explosion depth.
    
     @param maxDepth Desired directory/sub-directory explosion depth.
   */
  public void setMaxDepth (int maxDepth) {
  }
  
  /**
     Retrieves the path to the original source file (if any).
    
     @return Path to the original source file (if any).
   */
  public String getDataParent () {
    return "";
  }
  
  /**
    Close up shop for this collection. Stops any alerts that may have been
    set.

   */
  public void shutDown () {
        
    WisdomItem nextItem;

  } // end shutDown method
  
} // end of class

