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
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.*;

/**
   A comparator for a To Do item. Given a set of sort fields,
   will determine whether a particular To Do item is less than, greater than,
   or equal to, another To Do item. 
 */
public class ItemComparator
      implements java.util.Comparator {
        
  public static final String SHOW           = "show";
  public static final String FIELD1         = "field1";
  public static final String SEQ1           = "seq1";
  public static final String FIELD2         = "field2";
  public static final String SEQ2           = "seq2";
  public static final String FIELD3         = "field3";
  public static final String SEQ3           = "seq3";
  public static final String FIELD4         = "field4";
  public static final String SEQ4           = "seq4";
  public static final String FIELD5         = "field5";
  public static final String SEQ5           = "seq5";
  public static final String UNDATED        = "undated";
        
  public static final String ASCENDING  = "ascending";
  public static final String DESCENDING = "descending";
  public static final String HIGH       = "high";
  public static final String LOW        = "low";
        
  private WisdomItems     items;
        
  private ItemSelector  select = new ItemSelector();
  
  private String[]      sortFields    
      = {  
          WisdomItem.COLUMN_DISPLAY [WisdomItem.AUTHOR_INDEX],           
          WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_TYPE_INDEX],
          WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_INDEX],
          WisdomItem.COLUMN_DISPLAY [WisdomItem.TITLE_INDEX], 
          WisdomItem.COLUMN_DISPLAY [WisdomItem.RATING_INDEX],
          WisdomItem.COLUMN_DISPLAY [WisdomItem.YEAR_INDEX],
          WisdomItem.COLUMN_DISPLAY [WisdomItem.CATEGORY_INDEX],
          WisdomItem.COLUMN_DISPLAY [WisdomItem.BODY_INDEX],
          WisdomItem.COLUMN_DISPLAY [WisdomItem.RIGHTS_OWNER_INDEX],
          WisdomItem.COLUMN_DISPLAY [WisdomItem.WEB_PAGE]
        };
          
  private boolean       undatedHigh = false;
          
  private int[]         sortSeqs = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
          
  private int index = 0;
  
  private int nextIndex = -1;
  
  /** Creates a new instance of ItemComparator */
  public ItemComparator() {
    
  }
  
  /*
  public void setSelector (ItemSelector select) {
    this.select = select;
  }
   */
  
  /**
   *    Sets the underlying collection of WisdomItems to be used by this object.
   *   
   *    @param items Underlying collection of WisdomItems (unsorted).
   */
  public void setItems(WisdomItems items) {
    this.items = items;
  }
  
  /**
    Set all of the comparator's options from a single string.
   
    @param bundle All of this comparator's options, bundled into a single string,
                  using commas to separate fields, and enclosed in parentheses.
   */
  public void set (String bundle) {
    nextIndex = -1;
    setSortFields 
       (nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle),
        nextField (bundle));
  }

  /**
   Extract the next value from the bundle.

   @param bundle A single string containing all the options for this comparator.
   @return The next field, assuming that the entire bundle may begin with a
           left parenthesis, and that a comma or a right parenthesis will
           act as a terminator for each field. 
   */
  private String nextField (String bundle) {
    char c = nextChar (bundle);
    while (moreChars (bundle)
        && (c == ',' || c == '(' || c == ' ')) {
      c = nextChar (bundle);
    }
    StringBuffer field = new StringBuffer();
    while (moreChars (bundle)
        && c != ',' && c != ')') {
      field.append (c);
      c = nextChar (bundle);
    }
    return field.toString();
  }
  
  /**
    Extract the next character from the bundle of options.
   
    @param bundle A single string containing all the options for this comparator.
   */
  private char nextChar (String bundle) {
    char c = ')';
    nextIndex++;
    if (moreChars (bundle)) {
      c = bundle.charAt (nextIndex);
    } 
    return c;
  }
  
  /**
    Are there more characters left in the bundle?
   
    @param bundle A single string containing all the options for this comparator.
   */
  private boolean moreChars (String bundle) {
    return (nextIndex < bundle.length());
  }
  
  public void setSortFields 
      (String showOption,
       String sort1Field, String sort1Seq,
       String sort2Field, String sort2Seq,
       String sort3Field, String sort3Seq,
       String sort4Field, String sort4Seq,
       String sort5Field, String sort5Seq,
       String undatedHigh) {
    select.setSelectOption (showOption);
    index = 0;
    // if (! showOption.equals ("All")) {
    //   setNextSortField (WisdomItem.COLUMN_DISPLAY [WisdomItem.STATUS], 
    //       ViewTab.ASCENDING);
    // } 
    for (int i = 0; i < sortFields.length; i++) {
      sortSeqs [i] = 1;
    }
    setNextSortField (sort1Field, sort1Seq);
    setNextSortField (sort2Field, sort2Seq);
    setNextSortField (sort3Field, sort3Seq);
    setNextSortField (sort4Field, sort4Seq);
    setNextSortField (sort5Field, sort5Seq);
    setUndated (undatedHigh);
  }
  
  private void setUndated (String undatedHighString) {
    if (undatedHighString.equalsIgnoreCase (HIGH)) {
      undatedHigh = true;
    }
    else
    if (undatedHighString.equalsIgnoreCase (LOW)) {
      undatedHigh = false;
    }
  }
  
  private void setNextSortField (String sortField, String seq) {
    
    // Ignoring fields already set, look for desired field in table
    int i = index;
    while (i < sortFields.length
        && (! sortField.equals (sortFields [i]))) {
      i++;
    }
    
    // If we found it, and it is currently lower on list, move it up
    if (i < sortFields.length) {
      while (i > index) {
        sortFields [i] = sortFields [i - 1];
        i--;
      }
      sortFields [index] = sortField;
      if (seq.substring(0,1).equalsIgnoreCase("d")) {
        sortSeqs [index] = -1;
      } else {
        sortSeqs [index] = 1;
      }
      index++;
    } // end if sortField found
  }
  
  /**
   *    Compare two WisdomItem objects and determine which is lower in a sort sequence,
   *    based on sort fields supplied by the user.
   *   
   *    @result <0 if the first item is less than the second, or
   *             0 if the two items are equal on all significant keys, or 
   *            >0 if the first item is great than the second.
   *    @param obj  First to do item to be compared.
   *    @param obj1 Second to do item to be compared. 
   */
  public int compare(Object obj, Object obj1) 
      throws ClassCastException {
    WisdomItem item1 = (WisdomItem)obj;
    WisdomItem item2 = (WisdomItem)obj1;

    int c = 0;
    if (select.selected (item1)
        && (! select.selected (item2))) {
      c = -1;
    }
    else
    if (select.selected (item2)
        && (! select.selected (item1))) {
      c = 1;
    }

    int i = 0;
    String sortField;
    int sortSeq;
    while (c == 0 && i < sortFields.length) {
      sortField = sortFields [i];
      sortSeq = sortSeqs [i];
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.NO_FIELD])) {
        // no affect on comparison
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_TYPE_INDEX])) {
        if (item1.getSourceType() < item2.getSourceType()) {
          c = -1;
        }
        else
        if (item1.getSourceType() > item2.getSourceType()) {
          c = 1;
        }
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_INDEX])) {
        c = item1.getSourceTitle().compareToIgnoreCase (item2.getSourceTitle());
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.AUTHOR_INDEX])) {
        c = item1.getAuthor().compareTo (item2.getAuthor());
      }
      else 
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.RATING_INDEX])) {
        if (item1.getRating() < item2.getRating()) {
          c = -1;
        }
        else
        if (item1.getRating() > item2.getRating()) {
          c = 1;
        }
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_ID])) {
        c = item1.getSourceID().compareToIgnoreCase (item2.getSourceID());
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.TITLE_INDEX])) {
        c = item1.getTitle().compareToIgnoreCase (item2.getTitle());
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.CATEGORY_INDEX])) {
        c = item1.getCategory().compareTo (item2.getCategory());
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.BODY_INDEX])) {
        c = item1.getBody().compareToIgnoreCase (item2.getBody());
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.RIGHTS_OWNER_INDEX])) {
        c = item1.getRightsOwner().compareToIgnoreCase (item2.getRightsOwner());
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.WEB_PAGE])) {
        c = item1.getWebPage().compareToIgnoreCase (item2.getWebPage());
      }
      else
      if (sortField.equals (WisdomItem.COLUMN_DISPLAY [WisdomItem.YEAR_INDEX])) {
        c = item1.getYear().compareToIgnoreCase (item2.getYear());
      }
      
      if ((c != 0) && (sortSeq < 1)) {
        c = c * sortSeq;
      }
      i++;
    } // end while still equal and more fields to go
    return c;
  }
  
  public int compareInts (int int1, int int2) {
    if (int1 < int2) {
      return -1;
    }
    else
    if (int1 > int2) {
      return +1;
    } else {
      return 0;
    }
  }
  
  /**
    Add column names for the fields defined by this class.
   
    @param recDef Record Definition to have column names added to it.
   */
  public static void addColumnNames (RecordDefinition recDef) {
    recDef.addColumn (SHOW);
    recDef.addColumn (FIELD1);
    recDef.addColumn (SEQ1);
    recDef.addColumn (FIELD2);
    recDef.addColumn (SEQ2);
    recDef.addColumn (FIELD3);
    recDef.addColumn (SEQ3);
    recDef.addColumn (FIELD4);
    recDef.addColumn (SEQ4);
    recDef.addColumn (FIELD5);
    recDef.addColumn (SEQ5);
    recDef.addColumn (UNDATED);
  }
  
  /**
    Return the options for this comparator as a single String.
   
    @return Options for this comparator bundled into a single String. The entire 
            bundle is enclosed in parentheses. Eleven fields are enclosed, with
            commas separating the fields. The first field is the Selection option.
            The remaining ten fields consist of five value pairs, one pair for each
            sort field, from most significant to least. Within each pair, the first
            value identifies the field, and the second identifies the sort sequence.
   */
  public String toString() {
    return ("(" + select.toString() + ","
        + sortFields [0] + "," + getSortSeq (0) + ","
        + sortFields [1] + "," + getSortSeq (1) + ","
        + sortFields [2] + "," + getSortSeq (2) + ","
        + sortFields [3] + "," + getSortSeq (3) + ","
        + sortFields [4] + "," + getSortSeq (4) + ","
        + getUndatedString()
        + ")");
  }
  
  public void setSelectOption (String selectOption) {
    select.setSelectOption (selectOption);
  }
  
  public ItemSelector getSelector () {
    return select;
  }
  
  public String getSelectString() {
    return select.toString();
  }
  
  public boolean getUndated () {
    return undatedHigh;
  }
  
  public String getUndatedString () {
    return (undatedHigh ? HIGH : LOW);
  }
  
  public String getSortField (int i) {
    return sortFields [i];
  }
  
  public String getSortSeq (int i) {
    if (sortSeqs [i] > 0) {
      return ViewPrefs.ASCENDING;
    } else {
      return ViewPrefs.DESCENDING;
    }
  }
  
  /**
    Compare this comparator to another one.
   
    @return True if the two comparators have equal values.
    @param  obj2 A second comparator to compare to this one.
   */
  public boolean equals(Object obj2) {
    boolean match = true;
    ItemComparator comp2 = null;
    try {
      comp2 = (ItemComparator)obj2;
    } catch (ClassCastException e) {
      match = false;
    }
    if (! match) {
      return match;
    }
    match = (comp2.getSelector().equals (this.getSelector()));
    if (! match) {
      return match;
    }
    match = (comp2.undatedHigh == this.undatedHigh);
    if (! match) {
      return match;
    }
    for (int i = 0; ((i < sortFields.length) && (match)); i++) {
      match = ((comp2.sortFields[i].equals (this.sortFields[i]))
          && (comp2.sortSeqs[i] == this.sortSeqs[i]));
    }
    return match;
  }
  
}
