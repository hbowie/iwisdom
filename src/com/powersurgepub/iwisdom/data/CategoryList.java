package com.powersurgepub.iwisdom.data;

  import com.powersurgepub.psdatalib.ui.ValueList;

/**
   A collection of values that are used as categories for to do items. 
   New values are added to the list. The
   list is maintained in alphabetical order. A JComboBox is maintained
   and kept synchronized with the list.<p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/18 - Originally written.
 */
public class CategoryList 
    extends ValueList 
        implements ItemsView {
  
  /** Creates a new instance of CategoryList */
  public CategoryList() {
  }
  
  public void add(WisdomItem item) {
    item.startCategoryIteration();
    while (item.hasNextCategory()) {
      registerValue (item.nextCategory());
    }
  }
  
  public void modify(WisdomItem item) {
    item.startCategoryIteration();
    while (item.hasNextCategory()) {
      registerValue (item.nextCategory());
    }
  }
  
  public void remove(WisdomItem item) {
    // No need to do anything
  }
  
  public void setItems(WisdomItems items) {
    // No need to save this for anything
  }
  
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    return "CategoryList";
	}
  
}
