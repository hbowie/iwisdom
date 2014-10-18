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
  import javax.swing.*;
  import javax.swing.table.*;
  
/**
 *   A sorted collection of to do items. 
 */

public class SortedItems 
    extends AbstractTableModel 
        implements DataSource, ItemsView, ItemNavigator  {
          
  public  static final int  REMOVE = -1;
  public  static final int  MODIFY = 0;
  public  static final int  ADD    = 1;
  
  public  static final int  LIST_WIDTH = 1000;
  public  static final int  LIST_WIDTH_EXTRA = 20;
  
  private WisdomItems       items;
  
  private iWisdomCommon    td;
      
  private	ArrayList       sortedItems;
  
  /** Index pointing to current To Do item within SortedItems. */
  private int                     currentIndex = 0;
  
  /** Index pointing to the next To Do item within SortedItems. */
  private int                     nextIndex = 1;
  
  /** Index pointing to the prior To Do item within SortedItems. */
  private int                     priorIndex = -1;
  
  private ItemComparator  comp;
  
  private ItemSelector    select;
  
  private WisdomColumns     columns;
  
  private ArrayList       displayColumns;
  
  private DateFormat      dateFormatter = new SimpleDateFormat ("MM/dd/yyyy");
  
  private RecordDefinition recDef;
  
  private int             dsIndex = 0;
  
  private Debug           debug   = new Debug(false);
  
  private Logger          logger     = new Logger();

	/**
	   Constructor with minimal arguments.
   */
	
	public SortedItems (iWisdomCommon td) {
    this.td = td;
    recDef = WisdomItem.getRecDef();
		items = new WisdomItems(td);
    sortedItems = new ArrayList();
    columns = new WisdomColumns ();
    currentIndex = 0;
    setNextPrior();
	}
  
	/**
	 *    Constructor with starting arguments.
	 *     
	 *    @param items  A WisdomItems collection.
	 *    @param comp   An ItemComparator to be used for sorting items. 
	 *    @param select An ItemSelector to be used for selecting items. 
	 */
	public SortedItems (WisdomItems items, ItemComparator comp, ItemSelector select) {
    recDef = WisdomItem.getRecDef();
    columns = new WisdomColumns ();
    setItems (items);
    setComparator (comp);
    setSelector (select);
    sort();
	}
  
  /**
   *    Sets the underlying collection of WisdomItems to be used by this object.
   *   
   *    @param items Underlying collection of WisdomItems (unsorted).
   */
  public void setItems(WisdomItems items) {
    this.items = items;
  }
  
  public WisdomItems getItems () {
    return items;
  }
  
  public void setComparator (ItemComparator comp) {
    if (comp != null) {
      this.comp = comp;
      setSelector (comp.getSelector());
      if (columns != null) {
        columns.setComparator (comp);
        getDisplayColumns();
      }
    }
  }
  
  public ItemComparator getComparator () {
    return comp;
  }
  
  public void setSelector (ItemSelector select) {
    this.select = select;
  }
  
  /**
   *    Save a pointer to the iWisdomCommon object.
   *   
   *    @param common Pointer to the iWisdomCommon object.
   */
  public void setCommon(iWisdomCommon common) {
    this.td = common;
  }
  
  /**
    Make sure this view is sorted in the proper sequence
    and contains only selected items.
   */
  public void sort() {
    // Initialize the view and add each item to it
    sortedItems = new ArrayList();
    if (columns != null) {
      columns.resetTimesUsed();
    }
    for (int i = 0; i < items.size(); i++) {
      addInternal (items.get(i));
      if (columns != null) {
        columns.checkUsed (items.get(i));
      }
    }
    getDisplayColumns ();
    // setColumnWidths();
  }
  
  /**
    Get display columns for JTable.
    */
  public void getDisplayColumns () {
    if (columns != null) {
      int width = LIST_WIDTH;
      if (td != null) {
        width = td.getFrameWidth() + LIST_WIDTH_EXTRA;
      }
      displayColumns = columns.getDisplayColumns (width);
      fireTableStructureChanged();
    }
  }
  
  public int getRatingColumn () {
    int r = -1;
    if (displayColumns != null) {
      for (int i = 0; i < displayColumns.size() && r < 0; i++) {
        WisdomColumn column = (WisdomColumn) displayColumns.get (i);
        if (column.getColumnNumber() == WisdomItem.RATING_INDEX) {
          r = i;
        }
      }
    }
    return r;
  }
  
  /**
    Adds another to do item to the sorted list.
    
    @param item The item to be added to the collection.
   */
  public void add (WisdomItem item) {
    currentIndex = addInternal (item);
    setNextPrior();
    fireTableDataChanged();
    if (columns != null) {
      columns.checkUsed (item);
    }
  } // end method
  
  /**
    Adds another to do item to the sorted list.
    
    @return Index pointing to the location of the newly added item.
   
    @param item The item to be added to the collection.
   */
  private int addInternal (WisdomItem item) {
    return (doItem (ADD, item));
  } // end method
  
  /**
   *   If an item has been modified, then check to make sure it is still in the
   *   correct sort sequence.
   *   
   *   @param item  The WisdomItem after any modifications. 
   */
  public void modify (WisdomItem item) {
    
    int newIndex = doItem (MODIFY, item);
    if (newIndex < currentIndex) {
      nextIndex++;
      priorIndex++;
    }
    else
    if (newIndex > currentIndex) {
      nextIndex--;
      priorIndex--;
    }
    fireTableDataChanged();
  }
  
  /**
   *    Process a WisdomItem that has just been logically deleted from the WisdomItems collection.
   *   
   *    @param item   WisdomItem just logically deleted (by setting Deleted flag).
   */
  public void remove (WisdomItem item) {
    int oldIndex = doItem (REMOVE, item);
    // currentIndex = nextIndex;
    // setNextPrior();
    // getItem();
    fireTableDataChanged();
  }
  
  /**
    Processes one item, either adding it to the list, removing it from
    the list, or moving it within the list, depending on the desired 
    operation, the item's selection status, and the item's prior
    existence within the list.
    
    @return Index position (within sorted list) of item before operation,
            for a removal, or the position after the operation, for an addition
            or modification.
    
    @param  operation Operation to be performed: <ul>
                        <li> +1 - add; 
                        <li> -1 - remove; 
                        <li>  0 - modify. </ul>
    @param item The item to be added to the collection.
   */
  public int doItem (int operation, WisdomItem item) {
    
    // Go through the list, looking for where item is now and where it should be
    int itemIndex = item.getItemNumber();
    boolean currentFound = false;
    boolean newFound = false;
    boolean done = false;
    int op = operation;
    if (! selected (item)) {
      op = REMOVE;
    }
    int currentIndex = -1;
    int newIndex = size();
    for (int i = 0; (i < size() && (! done)); i++) {
      if (! currentFound) {
        if (itemIndex == getIndex(i)) {
          currentIndex = i;
          currentFound = true;
        } // end if this is current position for item
      } // end if current position not yet found
      if (op != REMOVE) {
        if ((! newFound) && (i != currentIndex)){
          
          int c = comp.compare (item, get (i));
          if (c <= 0) {
            newIndex = i;
            newFound = true;
          }
        } // end if new position not yet found
      } // end if item should end up as part of view
      if ((currentFound)
          && (newFound || (op == REMOVE))) {
        done = true;
      }
    } // end for each item in view
    
    // Now that we've collected needed info, decide what to do
    
    if ((currentIndex < 0) && (op == REMOVE)) {
      // Item not now in list, and doesn't belong there -- we're done
    }
    else
    if (currentIndex < 0) {
      // item is not now in list, but needs to be there -- add it
      ItemProxy newProxy = new ItemProxy (itemIndex);
      if ((newIndex >= size()) || (newIndex < 0)) {
        newIndex = sortedItems.size();
        boolean ok = sortedItems.add (newProxy);
      } else {
        sortedItems.add (newIndex, newProxy);
      }
    } 
    else 
    if ((currentIndex >= 0) && (op == REMOVE)) {
      // Item is in list but needs to be removed
      ItemProxy removed = (ItemProxy)sortedItems.remove (currentIndex);
    } else {
      int inc = 0;
      if (newIndex > currentIndex) {
        inc = 1;
      } 
      else 
      if (newIndex < currentIndex) {
        inc = -1;
      }
      newIndex = newIndex - inc;
      if (currentIndex == newIndex) {
        // item is already where it belongs -- no change necessary
      } else {
        // Item is in list but needs to be moved
        ItemProxy itemProxy = (ItemProxy)sortedItems.get(currentIndex);
        ItemProxy holdProxy;
        ItemProxy nextProxy;
        for (int i = currentIndex; i != newIndex; i = i + inc) {
          nextProxy = (ItemProxy)sortedItems.get (i + inc);
          holdProxy = (ItemProxy)sortedItems.set (i, nextProxy);
        }
        holdProxy = (ItemProxy)sortedItems.set (newIndex, itemProxy);
      } // end if item needs to be moved
    } // end if item has old and new location
    if (operation == REMOVE) {
      return currentIndex;
    } else {
      return newIndex;
    }
  } // end doItem method
  
  /**
    Gets this collection of items and passes it to a data store.
    
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
      // Create a date formatter
      DateFormat fmt = new SimpleDateFormat ("MM/dd/yyyy"); 
    
      WisdomItem nextItem;
      for (int itemIndex = 0; itemIndex < size(); itemIndex++) {
        nextItem = get (itemIndex);
        DataRecord nextRec = nextItem.getDataRec (recDef, null);
        try {
          dataOut.nextRecordOut (nextRec);
        } catch (IOException e2) {
          ok = false;
        }
      } // end for loop
    } // end if open ok
    dataOut.close();
    return ok;
  } // end getAll method
  
  /**
    Get the item's position within this collection.
   
    @return Current item's position within this collection, where zero points
            to the first.
   */
  public int getItemNumber () {
    return currentIndex;
  }
  
  /**
    Returns the size of the collection.
    
    @return Size of the collection.
   */
  public int size () {
    return sortedItems.size();
  }
	
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    return "SortedItems";
	}
  
  /*
   The following methods implement the AbstractTableModel interface,
   which allow this Class to be used to populate a JTable view.
   */
  
  /**
   Returns number of columns to be displayed in a JTable view.
   
   @return Number of columns to display.
   */
  public int getColumnCount() {
    if (displayColumns == null) {
      return 0;
    } else {
      return displayColumns.size();
    }
  }
  
  /** 
   Returns number of rows to display in a JTable view. Note that non-selected
   items are not included in the count, based on currently set View parameters.
   
   @return Number of currently visible rows in table.
   */
  public int getRowCount() {
    return size();
  }
  
  /**
   Get the name of the column to be displayed in a JTable view.
   
   @return Name of a particular column.
   @param column Index pointing to a particular column.
   */
  public String getColumnName (int column) {
    WisdomColumn tdcolumn = (WisdomColumn) displayColumns.get (column);
    return tdcolumn.getBriefName();
  }
  
  /**
   Get the desired width of the column to be displayed in a JTable view.
   
   @return Width of a particular column, in pixels.
   @param column Index pointing to a particular column.
   */
  public int getColumnWidth (int column) {
    WisdomColumn tdcolumn = (WisdomColumn) displayColumns.get (column);
    return tdcolumn.getWidth();
  }
  
  
  /**
    Set the preferred column widths for the passed JTable.
   
    @param table JTable to have its column widths set. 
   */
  public void setColumnWidths (JTable table) {
    TableColumn column;
    for (int i = 0; i < getColumnCount(); i++) {
      column = table.getColumnModel().getColumn (i);
      column.setPreferredWidth (getColumnWidth (i));
    }
  }

  /**
   Returns the Class of a particular column to be displayed in a JTable view.
   
   @return The Class of the objects that will be returned for this column.
   @param column Column number of desired column.
   */
  public Class getColumnClass(int column) {
    WisdomColumn tdcolumn = (WisdomColumn) displayColumns.get (column);
    return tdcolumn.getColumnClass();
  } // end method
  
  /**
   Return the actual value stored in a particular cell of the table.
   
   @return  Object inhabiting this cell of the table (hopefully of the class
            indicated by the GetColumnClass method).
   @param   row     The desired row in the table.
   @param   column  The desired column in the table.
   */
  public Object getValueAt(int row, int column) {
    WisdomItem item = get (row);
    WisdomColumn tdcolumn = (WisdomColumn) displayColumns.get (column);
    return tdcolumn.getValue(item);
  } // end method
  
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
      WisdomItem item = get (dsIndex);
      dsIndex++;
      return item.getDataRec(recDef, null);
    }
  }
  
  /**
    See if item should be included in this view.
   
    @param item Item to be included or excluded from this view.
   */
  public boolean selected (WisdomItem item) {
    return (select.selected (item) && (! item.isDeleted()));
  }
  
  /**
   *    Find the passed index in this list and return its position.
   *   
   *    @return position of passed index, or -1 if not in list.
   *   
   *    @param  index The index pointing to an item in the underlying WisdomItems collection.
   */
  public int find (int index) {
    int i = 0;
    boolean found = false;
    ItemProxy proxy;
    while (i < sortedItems.size() && (! found)) {
      proxy = (ItemProxy)sortedItems.get (i);
      if (index == proxy.getIndex()) {
        found = true;
      } else {
        i++;
      }
    }
    if (! found) {
      i = -1;
    }
    return i;
  }
  
  /**
   *    Gets an item from the sorted list, given its index position within
   *    the sorted list.
   *   
   *    @return The WisdomItem pointed to by the passed index, or null
   *            if the index is out of range.
   *   
   *    @param index Index position of an item within the sorted list.
   */
  public WisdomItem get (int index) {
    if (index < 0 || index >= size()) {
      return null;
    } else {
      ItemProxy proxy = (ItemProxy)sortedItems.get (index);
      return (WisdomItem)items.get(proxy.getIndex());
    }
  }
  
  /**
   *    Gets an item's underlying index from the sorted list, 
   *    given its index position within the sorted list.
   *   
   *    @return The underlying index pointing to a WisdomItem, or -1
   *            if the index is out of range.
   *   
   *    @param index Index position of an item within the sorted list.
   */
  public int getIndex (int index) {
    if (index < 0 || index >= size()) {
      return -1;
    } else {
      ItemProxy proxy = (ItemProxy)sortedItems.get (index);
      return (proxy.getIndex());
    }
  }
  
  /**
   *    Gets an item's position within the sorted list, 
   *    given its index position within the underlying list.
   *   
   *    @return The index pointing to a WisdomItem's proxy within
   *            the sorted list, or -1 if the index is out of range.
   *   
   *    @param index  Index position of an item within the 
   *                  underlying WisdomItems list.
   */
  public int getSortedIndex (int index) {
    int sortedIndex = -1;
    for (int i = 0; (i < sortedItems.size() && sortedIndex < 0); i++) {
      if (index == getIndex(i)) {
        sortedIndex = i;
      }
    }
    return sortedIndex;
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
    return (dsIndex >= size());
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
  
  // Methods that implement the ItemNavigator interface
  
  public void firstItem() {
    currentIndex = 0;
    checkIndex();
    setNextPrior();
    getItem();
  }
  
  public void lastItem() {
    currentIndex = (size() - 1);
    checkIndex();
    setNextPrior();
    getItem();
  }
  
  public void nextItem() {
    currentIndex = nextIndex;
    checkIndex();
    setNextPrior();
    getItem();
  }
  
  public void priorItem() {
    currentIndex = priorIndex;
    checkIndex();
    setNextPrior();
    getItem();
  }
  
  public void selectItem(WisdomItem item) {
    currentIndex = getSortedIndex (item.getItemNumber());
    setNextPrior();
  }
  
  public void setIndex (int index) {
    currentIndex = index;
    checkIndex();
    setNextPrior();
    getItem();
  }
  
  private void setNextPrior () {
    nextIndex = currentIndex + 1;
    priorIndex = currentIndex - 1;
  }
  
  private void checkIndex() {
    if (currentIndex >= size()) {
      currentIndex = (size() - 1);
    }
    if (currentIndex < 0) {
      currentIndex = 0;
    }
  }
  
  public boolean indexIsValid() {
    return ((currentIndex < size()) 
        && (currentIndex >= 0));
  }
  
  private void getItem() {
    if (td == null) {
      // Nothing we can do (shouldn't ever happen)
    }
    if (currentIndex < size()) {
      td.setItem (get (currentIndex));
    } else {
      td.newItem();
    }
  }
  
} // end of class
