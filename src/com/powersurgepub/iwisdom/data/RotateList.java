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

public class RotateList 
        implements ItemsView, ItemNavigator  {
  
  public  static final int ROTATE_LINEAR           = 0;
  public  static final int ROTATE_RANDOM           = 1;
  public  static final int ROTATE_RANDOM_WEIGHTED  = 2;
  
  private WisdomItems       items;
          
  private SortedItems     sorted;
  
  private iWisdomCommon    td;
  
  private int             rotateMethod            = ROTATE_LINEAR;
  
  private boolean         rotating                = false;
      
  private	ArrayList       rotateList;
  
  private Random          randomizer              = new Random();
  
  /** Index pointing to current To Do item within rotateList. */
  private int             currentIndex = 0;
  
  private Debug           debug   = new Debug(false);
  
  private Logger          logger  = new Logger();

	/**
	   Constructor with minimal arguments.
   */
	public RotateList (iWisdomCommon td) {
    this.td = td;
    rotateList = new ArrayList();
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
  
  public void setRotateMethod (int rotateMethod) {
    this.rotateMethod = rotateMethod;
  }
  
  public int getRotateMethod () {
    return rotateMethod;
  }
  
  public boolean isRotating () {
    return rotating;
  }
  
  /**
    Find all items that contain the given search string and store the
    results in the auxiliary list.
   
    @param findString The string to search for.
   */
	public void start () {
    initList();
    for (int i = 0; i < sorted.size(); i++) {
      WisdomItem item = sorted.get (i);
      if (rotateMethod == ROTATE_RANDOM_WEIGHTED) {
        int weight = 3;
        
        if (item.getRating()  < 2) {
          weight++;
        }
        else
        if (item.getRating() > 4) {
          weight--;
        }
        
        for (int j = 1; j <= weight; j++) {
          int itemIndex = item.getItemNumber();
          boolean ok = add (itemIndex);
        }
        
      } else {
        int itemIndex = item.getItemNumber();
        boolean ok = add (itemIndex);
      }
    }

    currentIndex = 0;
    if (size() > 0) {
      rotating = true;
    }
    randomizer = new Random();
	}
  
  public void stop() {
    rotating = false;
  }
  
  public void initList() {
    rotateList = new ArrayList();
  }
  
  public boolean add (int itemNumber) {
    ItemProxy newProxy = new ItemProxy (itemNumber);
    rotating = true;
    return rotateList.add (newProxy);
  }
  
  /**
    Adds another to do item to the list.
    
    @param item The item to be added to the collection.
   */
  public void add (WisdomItem item) {
    rotating = false;
  } // end method
  
  /**
   *   If an item has been modified, then check to make sure it is still in the
   *   correct sort sequence.
   *   
   *   @param item  The WisdomItem after any modifications. 
   */
  public void modify (WisdomItem item) {
    rotating = false;
  }
  
  /**
   *    Process a WisdomItem that has just been logically deleted from the WisdomItems collection.
   *   
   *    @param item   WisdomItem just logically deleted (by setting Deleted flag).
   */
  public void remove (WisdomItem item) {
    rotating = false;
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
    return rotateList.size();
  }
	
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    return "RotateList";
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
    while (i < rotateList.size() && (! found)) {
      proxy = (ItemProxy)rotateList.get (i);
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
   *    Gets an item from the rotation list, given its index position within
   *    the rotation list.
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
      ItemProxy proxy = (ItemProxy)rotateList.get (index);
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
      ItemProxy proxy = (ItemProxy)rotateList.get (index);
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
  public int getRotateIndex (int index) {
    int rotateIndex = -1;
    for (int i = 0; (i < rotateList.size() && rotateIndex < 0); i++) {
      if (index == getIndex(i)) {
        rotateIndex = i;
      }
    }
    return rotateIndex;
  }
  
  // Methods that implement the ItemNavigator interface
  
  public void firstItem() {
    currentIndex = 0;
    if (rotateMethod > ROTATE_LINEAR) {
      nextItem();
    }
    checkIndex();
    getItem();
  }
  
  public void lastItem() {
    currentIndex = (size() - 1);
    checkIndex();
    getItem();
  }
  
  public void nextItem() {
    if (rotateMethod == ROTATE_LINEAR || size() < 3) {
      currentIndex++;
    } else {
      currentIndex = randomizer.nextInt (size());
    }
    checkIndex();
    getItem();
  }
  
  public void priorItem() {
    currentIndex--;
    checkIndex();
    getItem();
  }
  
  public void selectItem (WisdomItem item) {
    currentIndex = getRotateIndex (item.getItemNumber());
  }
  
  public void setIndex (int index) {
    currentIndex = index;
    checkIndex();
    getItem();
  }
  
  private void checkIndex() {
    if (currentIndex >= size()) {
      currentIndex = 0;
    }
    if (currentIndex < 0) {
      currentIndex = size();
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