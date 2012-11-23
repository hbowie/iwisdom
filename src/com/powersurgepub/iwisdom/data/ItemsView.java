package com.powersurgepub.iwisdom.data;

/**
 *   An interface for any class that presents a view of the data stored
 *   in a WisdomItems list.<p>
 *  
 *   This code is copyright (c) 2003 by Herb Bowie.
 *   All rights reserved. <p>
 *  
 *   Version History: <ul><li>
 *       </ul>
 *  
 *   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
 *           herb@powersurgepub.com</a>)<br>
 *           of PowerSurge Publishing 
 *           (<a href="http://www.powersurgepub.com">
 *           www.powersurgepub.com</a>)
 *  
 *   @version 2003/11/10 - Originally written.
 */
public interface ItemsView {
  
  /**
   *    Save a pointer to the underlying WisdomItems collection.
   *   
   *    @param items Pointer to the WisdomItems collection.
   */
  void setItems(WisdomItems items);
  
  /**
   *    Process a new WisdomItem that has just been added to the WisdomItems collection.
   *   
   *    @param item   WisdomItem just added.
   */
  void add(WisdomItem item);
  
  /**
   *    Process a new WisdomItem that has just been modified within the WisdomItems collection.
   *   
   *    @param item   WisdomItem just modified.
   */
  void modify(WisdomItem item);
  
  /**
   *    Process a WisdomItem that has just been logically deleted from the WisdomItems collection.
   *   
   *    @param item   WisdomItem just logically deleted (by setting Deleted flag).
   */
  void remove(WisdomItem item);
  
}
