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

  import com.powersurgepub.psdatalib.markup.MarkupElement;
  import java.text.*;
  import javax.swing.*;

/**
 *   An object representing one WisdomItem field as displayed in a user interface.
 */
public class WisdomColumn {
  
  public final static   Boolean         BOOLEAN_CLASS     = new Boolean(true);
  
  public final static   String          STRING_CLASS      = "";
  
  public final static   ItemRating    RATING_CLASS    = new ItemRating();
  
  public final static   ImageIcon       IMAGE_ICON_CLASS  = new ImageIcon();
  
  public final static   DateFormat      DATE_FORMAT     
      = new SimpleDateFormat ("MM/dd/yyyy");
  
  private               int             columnNumber;
  
  public  final static  int             NULL_SORT_SEQUENCE = 999;
  private               int             sortSequence = NULL_SORT_SEQUENCE;
  
  private               int             timesUsed = 0;

  
  /**
   * 
   *    Creates a new instance of WisdomColumn. 
   */
  public WisdomColumn(int columnNumber) {
        
    this.columnNumber = columnNumber;
    
  }
  
  /**
    Compare relative importance of two columns of ToDo information.
   
    @result < 0 if this column is less important than the second, or
              0 if the two columns are equally important, or 
            > 0 if this column is more important than the second.
    @param obj2 Second column to compare to this one. 
   */
  public int compareTo (Object object2)
      throws ClassCastException {
    WisdomColumn column2 = (WisdomColumn) object2;
    int c = 0;
    /*
    System.out.println ("WisdomColumn.compareTo ");
    System.out.println ("  Column 1:");
    System.out.println ("    Name = " 
        + this.getDisplayName());
    System.out.println ("    Is Used? " 
        + String.valueOf (this.isUsed()));
    System.out.println ("    Sort Seq " 
        + String.valueOf (this.getSortSequence()));
    System.out.println ("    Display Priority " 
        + String.valueOf (this.getDisplayPriority()));
    System.out.println ("    Column # " 
        + String.valueOf (this.getColumnNumber()));
    
    System.out.println ("  Column 2:");
    System.out.println ("    Name = " 
        + column2.getDisplayName());
    System.out.println ("    Is Used? " 
        + String.valueOf (column2.isUsed()));
    System.out.println ("    Sort Seq " 
        + String.valueOf (column2.getSortSequence()));
    System.out.println ("    Display Priority " 
        + String.valueOf (column2.getDisplayPriority()));
    System.out.println ("    Column # " 
        + String.valueOf (column2.getColumnNumber())); */
    
    // First see whether one field is actually used and the other isn't
    // This takes precedence over all other conditions.
    if (this.isUsed() && (! column2.isUsed())) {
      c = 1;
    }
    else
    if ((! this.isUsed()) && column2.isUsed()) {
      c = -1;
    }
    else
      
    // Now see if one is higher in the sort hierarchy than the other
    if (this.getSortSequence() < column2.getSortSequence()) {
      c = 1;
    }
    else
    if (this.getSortSequence() > column2.getSortSequence()) {
      c = -1;
    }
    else
      
    // Now compare general display priorities for the two columns
    if (this.getDisplayPriority() < column2.getDisplayPriority()) {
      c = 1;
    } 
    else
    if (this.getDisplayPriority() > column2.getDisplayPriority()) {
      c = -1;
    }
    else
      
    // Finally, if all other fields and conditions are equal,
    // compare the two column numbers. 
    if (this.getColumnNumber() < column2.getColumnNumber()) {
      c = 1;
    } 
    else
    if (this.getColumnNumber() > column2.getColumnNumber()) {
      c = -1;
    }
    // System.out.println ("  Compare result = " + String.valueOf (c));
    return c;
  }
  
  public int getColumnNumber () {
    return columnNumber;
  }
  
  public void setSortSequence (int sortSequence) {
    this.sortSequence = sortSequence;
  }
  
  public int getSortSequence () {
    return sortSequence;
  }
  
  public void resetTimesUsed () {
    // Always expect title to be used
    if (columnNumber == WisdomItem.TITLE_INDEX) {
      timesUsed = 1;
    } else {
      timesUsed = 0;
    }
  }
  
  public void checkUsed (WisdomItem item) {
    Object value = getValue (item);
    Object nullOrDefault = getNullOrDefault();
    /*
    if (columnNumber == 5) {
      System.out.println ("For column " + this.toString()
           + " value = " + value.toString()
           + " and default = " + nullOrDefault.toString());
    } */
    if (value == null || nullOrDefault == null) {
      // do nothing
    }
    else
    if (! value.equals (nullOrDefault)) {
      timesUsed++;
    }
    // System.out.println ("  Times used = " + String.valueOf (timesUsed));
  } // end checkUsed method
  
  public boolean isUsed () {
    return (timesUsed > 0);
  }
  
  public String getName () {
    return WisdomItem.COLUMN_NAME [columnNumber];
  }
  
  public String getDisplayName () {
    return WisdomItem.COLUMN_DISPLAY [columnNumber];
  }
  
  public String getBriefName () {
    return WisdomItem.COLUMN_BRIEF [columnNumber];
  }
  
  public int getWidth () {
    return WisdomItem.COLUMN_WIDTH [columnNumber];
  }
  
  public int getDisplayPriority () {
    return WisdomItem.COLUMN_DISPLAY_PRIORITY [columnNumber];
  }
  
    /**
   Returns the Class of a particular column to be displayed in a JTable view.
   
   @return The Class of the objects that will be returned for this column.
   @param column Column number of desired column.
   */
  public Class getColumnClass() {
    Class columnClass;
    int classType = WisdomItem.COLUMN_CLASS_TYPE [columnNumber];
    switch (classType) {
      case 1:
        // Is task done? (Display as check box)
        columnClass = BOOLEAN_CLASS.getClass();
        break;
      case 2:
        // Is due date in the past, or today? (Display graphic)
        columnClass = IMAGE_ICON_CLASS.getClass();
        break;
      case 3:
        // Rating
        columnClass = RATING_CLASS.getClass();
        break;
      default:
        // Everything else is a string
        columnClass = STRING_CLASS.getClass();
        break;
    } // end switch
    return columnClass;
  } // end method
  
  /**
   *    Gets a field from the WisdomItem record, based on the field number.
   *   
   *    @return The requested field.
   *    @param  WisdomItem containing the desired data.
   */
  public Object getValue (WisdomItem item) {
    if (item == null) {
      return "";
    } else {
      switch (columnNumber) {
        case WisdomItem.DELETED:
          return new Boolean (item.isDeleted());
        case WisdomItem.CATEGORY_INDEX:
          return item.getCategory().toString();
        case WisdomItem.AUTHOR_INDEX:
          return item.getAuthorCompleteName();
        case WisdomItem.SOURCE_INDEX:
          return item.getSourceAsString();
        case WisdomItem.RATING_INDEX:
          return new ItemRating (item.getRating ());
        case WisdomItem.SOURCE_TYPE_INDEX:
          return item.getSourceTypeLabel();
        case WisdomItem.RIGHTS_INDEX:
          return item.getRights();
        case WisdomItem.YEAR_INDEX:
          return item.getYear();
        case WisdomItem.SOURCE_ID:
          return item.getSourceID();
        case WisdomItem.TITLE_INDEX:
          return item.getTitle ();
        case WisdomItem.BODY_INDEX:
          return item.getBody (MarkupElement.NO_HTML, false);
        case WisdomItem.RIGHTS_OWNER_INDEX:
          return item.getRightsOwner ();
        case WisdomItem.WEB_PAGE:
          return item.getWebPage ();
        case WisdomItem.MINOR_TITLE_INDEX:
          return item.getMinorTitle();
        case WisdomItem.SOURCE_LINK_INDEX:
          return item.getSourceLink();
        case WisdomItem.AUTHOR_LINK_INDEX:
          return item.getAuthorLink();
        case WisdomItem.DATE_ADDED_INDEX:
          return item.getDateAddedDisplay();
        case WisdomItem.AUTHOR_INFO_INDEX:
          return item.getAuthorInfo();
        /*
        case WisdomItem.ID:
          return item.getID();
        */
        default:
          return "";
      }
    }
   
  } // end method getValue
  
  /**
   *    Gets a field from the WisdomItem record, based on the field number.
   *   
   *    @return The requested field.
   *    @param  WisdomItem containing the desired data.
   */
  public Object getNullOrDefault () {
    switch (columnNumber) {
      case WisdomItem.DELETED:
        return new Boolean (false);
      case WisdomItem.CATEGORY_INDEX:
        return "";
      case WisdomItem.AUTHOR_INDEX:
        return "";
        // return DATE_FORMAT.format (WisdomItem.DEFAULT_DATE.getTime());
      case WisdomItem.SOURCE_INDEX:
        return "";
      case WisdomItem.RATING_INDEX:
        return new ItemRating (3);
      case WisdomItem.SOURCE_TYPE_INDEX:
        return "";
      case WisdomItem.RIGHTS_INDEX:
        return "";
      case WisdomItem.YEAR_INDEX:
        return "";
      case WisdomItem.SOURCE_ID:
        return "";
      case WisdomItem.TITLE_INDEX:
        return "";
      case WisdomItem.BODY_INDEX:
        return "";
      case WisdomItem.RIGHTS_OWNER_INDEX:
        return "";
      case WisdomItem.WEB_PAGE:
        return "";

      default:
        return "";
    }
   
  } // end method getNullOrDefault
  
  /**
    Return columns as some kind of string.
   
    @return Name of column.
   */
  public String toString () {
    return getDisplayName();
  }
  
} // end class WisdomColumn
