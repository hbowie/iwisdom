package com.powersurgepub.iwisdom.data;

  import com.powersurgepub.iwisdom.*;

/**
   An interface for any class that allows navigation through an ordered
   list of to do items.<p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/22 - Originally written.
 */
public interface ItemNavigator {
  
  /**
   *    Save a pointer to the underlying WisdomItems collection.
   *   
   *    @param items Pointer to the WisdomItems collection.
   */
  void setItems(WisdomItems items);
  
  /**
   *    Save a pointer to the iWisdomCommon object.
   *   
   *    @param common Pointer to the iWisdomCommon object.
   */
  void setCommon (iWisdomCommon common);
  
  void firstItem();
  
  void nextItem();
  
  void priorItem();
  
  void lastItem();
  
  void selectItem(WisdomItem item);
  
  /**
    Get the item's position within this collection.
   
    @return Current item's position within this collection, where zero points
            to the first.
   */
  public int getItemNumber ();
  
  /**
    Returns the size of the collection.
    
    @return Size of the collection.
   */
  public int size ();
  
}
