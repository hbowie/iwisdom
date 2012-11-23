package com.powersurgepub.iwisdom.data;

/**
   An object pointing to a ToDoItem that is part of a ToDoItems collection. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/11 - Originally written. 
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/11 - Originally written. 
 */
public class ItemProxy {
  
  private int index = -1;
  
  /** 
    Creates a new instance of ItemProxy. 
   */
  public ItemProxy() {
  }
  
  /** 
    Creates a new instance of ItemProxy. 
   
    @param Index Pointer to a ToDoItem stored in a ToDoItems collection.
   */
  public ItemProxy(int index) {
    setIndex (index);
  }
  
  /** 
    Creates a new instance of ItemProxy. 
   
    @param Index Pointer to a ToDoItem stored in a ToDoItems collection.
   */
  public void setIndex (int index) {
    this.index = index;
  }
  
  /**
    Determines if this proxy is equal to another.
   
    @param proxy2 Another ItemProxy to compare to this one.
   */
  public boolean equals (ItemProxy proxy2) {
    return (index == proxy2.getIndex());
  }
  
  /** 
    Gets the index pointing to a ToDoItem in a ToDoItems collection. 
   
    @return Index Pointer to a ToDoItem stored in a ToDoItems collection.
   */
  public int getIndex () {
    return index;
  }
  
}
