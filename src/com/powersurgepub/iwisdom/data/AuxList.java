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

	import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.*;
  import java.io.*;
  import java.text.*;
  import java.util.*;
  import javax.swing.*;
  import javax.swing.table.*;
  
/**
   An auxiliary list of To Do Items. 
 */

public class AuxList 
        implements ItemsView, ItemNavigator  {
  
  private WisdomItems       items;
          
  private SortedItems     sorted;
  
  private iWisdomCommon    td;
  
  private String          findString;
  
  private boolean         auxActive    = false;
      
  private	ArrayList       auxList;
  
  /** Index pointing to current To Do item within auxList. */
  private int                     currentIndex = 0;
  
  private Debug           debug   = new Debug(false);
  
  private Logger          logger     = new Logger();

	/**
	   Constructor with minimal arguments.
   */
	public AuxList (iWisdomCommon td) {
    this.td = td;
    auxList = new ArrayList();
	}
  
  public void setCommon (iWisdomCommon td) {
    this.td = td;
  }
  
  /**
   *    Sets the underlying collection of WisdomItems to be used by this object.
   *   
   *    @param items Underlying collection of WisdomItems (unsorted).
   */
  public void setItems(WisdomItems items) {
    this.items = items;
  }
  
  /**
   *    Sets the sorted collection of WisdomItems to be used by this object.
   *   
   *    @param sorted Sorted collection of WisdomItems.
   */
  public void setSorted (SortedItems sorted) {
    this.sorted = sorted;
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
    Find all items that contain the given search string and store the
    results in the auxiliary list.
   
    @param findString The string to search for.
   */
	public void find (String findString) {
    this.findString = findString;
    String findLower = findString.toLowerCase();
    String findUpper = findString.toUpperCase();
    this.sorted = sorted;
    this.td = td;
    initList();
    WisdomItem next;
    
    for (int i = 0; i < sorted.size(); i++) {
      next = sorted.get(i);
      if ((StringUtils.indexOfIgnoreCase (findLower, findUpper,
          next.getYear(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getCategory().toString(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getSourceTypeLabel().toString(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getSourceAsString().toString(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getRights().toString(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getBody(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getRightsOwner(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getTitle(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper,
              next.getWebPage(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper, 
              next.getAuthorCompleteName(), 0) >= 0)
          || (StringUtils.indexOfIgnoreCase (findLower, findUpper, 
              next.getAuthorInfo(), 0) >= 0)) {
        int itemIndex = next.getItemNumber();
        boolean ok = add (itemIndex);
      } // end if search string found in this item
    } // end of sorted items
    currentIndex = 0;
    if (size() > 0) {
      auxActive = true;
    }
	}
  
  public void initList() {
    auxList = new ArrayList();
  }
  
  public boolean add (int itemNumber) {
    ItemProxy newProxy = new ItemProxy (itemNumber);
    auxActive = true;
    return auxList.add (newProxy);
  }
  
  public boolean isAuxActive () {
    return auxActive;
  }
  
  /**
    Adds another to do item to the sorted list.
    
    @param item The item to be added to the collection.
   */
  public void add (WisdomItem item) {
    auxActive = false;
  } // end method
  
  /**
   *   If an item has been modified, then check to make sure it is still in the
   *   correct sort sequence.
   *   
   *   @param item  The WisdomItem after any modifications. 
   */
  public void modify (WisdomItem item) {
    // auxActive = false;
  }
  
  /**
   *    Process a WisdomItem that has just been logically deleted from the WisdomItems collection.
   *   
   *    @param item   WisdomItem just logically deleted (by setting Deleted flag).
   */
  public void remove (WisdomItem item) {
    // auxActive = false;
  }
  
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
    return auxList.size();
  }
	
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    return "AuxList";
	}
  
  /**
   *    Find the passed index in this list and return its position.
   *   
   *    @return position of passed index, or -1 if not in list.
   *   
   *    @param  index The index pointing to an item in the 
   *                  underlying WisdomItems collection.
   */
  public int find (int index) {
    int i = 0;
    boolean found = false;
    ItemProxy proxy;
    while (i < auxList.size() && (! found)) {
      proxy = (ItemProxy)auxList.get (i);
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
   *    Gets an item from the auxiliary list, given its index position within
   *    the auxiliary list.
   *   
   *    @return The WisdomItem pointed to by the passed index, or null
   *            if the index is out of range.
   *   
   *    @param index Index position of an item within the auxiliar list.
   */
  public WisdomItem get (int index) {
    if (index < 0 || index >= size()) {
      return null;
    } else {
      ItemProxy proxy = (ItemProxy)auxList.get (index);
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
      ItemProxy proxy = (ItemProxy)auxList.get (index);
      return (proxy.getIndex());
    }
  }
  
  /**
   *    Gets an item's position within the auxiliary list, 
   *    given its index position within the underlying list.
   *   
   *    @return The index pointing to a WisdomItem's proxy within
   *            the auxiliary list, or -1 if the index is out of range.
   *   
   *    @param index  Index position of an item within the 
   *                  underlying WisdomItems list.
   */
  public int getAuxIndex (int index) {
    int auxIndex = -1;
    for (int i = 0; (i < auxList.size() && auxIndex < 0); i++) {
      if (index == getIndex(i)) {
        auxIndex = i;
      }
    }
    return auxIndex;
  }
  
  // Methods that implement the ItemNavigator interface
  
  public void firstItem() {
    currentIndex = 0;
    checkIndex();
    getItem();
  }
  
  public void lastItem() {
    currentIndex = (size() - 1);
    checkIndex();
    getItem();
  }
  
  public void nextItem() {
    currentIndex++;
    checkIndex();
    getItem();
  }
  
  public void priorItem() {
    currentIndex--;
    checkIndex();
    getItem();
  }
  
  public void selectItem (WisdomItem item) {
    currentIndex = getAuxIndex (item.getItemNumber());
  }
  
  public void setIndex (int index) {
    currentIndex = index;
    checkIndex();
    getItem();
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
      // td.newItem();
    }
  }
  
} // end of class


