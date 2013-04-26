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

/**
   An object pointing to a ToDoItem that is part of a ToDoItems collection. 
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
