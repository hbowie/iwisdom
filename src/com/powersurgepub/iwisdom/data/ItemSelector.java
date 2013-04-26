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
  import java.util.*;

/**
   A selector for a To Do item. Given a set of selection criteria,
   will determine whether a particular To Do item is selected or not. 
 */
public class ItemSelector {
  
  public static final String SHOW_ALL_STR   = "All";
  public static final String SHOW_OPEN_STR  = "Open & In Work Only";
  public static final String SHOW_MONTH_STR = "Due in Next 30 Days";
  public static final String SHOW_WEEK_STR  = "Due in Next 7 Days";
  public static final String SHOW_TODAY_STR = "Due Today";
  public static final String SHOW_DUE_IN_NEXT_STR = "Due in Next";
  public static final int    SHOW_ALL       = 0;
  public static final int    SHOW_OPEN      = 1;
  public static final int    SHOW_MONTH     = 2;
  public static final int    SHOW_WEEK      = 3;
  public static final int    SHOW_TODAY     = 4;
  public static final int    SHOW_DUE_IN_NEXT = 5;
  
  private static      int    nextSelectorID = 0;
  
  private int     ID;
  
  private int     selectOption        = SHOW_ALL;
  private String  selectOptionString  = SHOW_ALL_STR;
  
  private int     showDueInDays       = -1;
  
  private GregorianCalendar showBefore;
  private Date showBeforeDate;
  
  /** Creates a new instance of ItemSelector */
  public ItemSelector() {
    setSelectDate();
    nextSelectorID++;
    ID = nextSelectorID;
    // System.out.println ("Constructing ItemSelector " + String.valueOf(ID));
  }
  
  public void setSelectOption (String selectOption) {
    // System.out.println ("Setting ItemSelector " + String.valueOf(ID) 
    //     + " to " + selectOption);
    selectOptionString = selectOption;
    if (selectOption.equals (SHOW_OPEN_STR)) {
      this.selectOption = SHOW_OPEN;
      showDueInDays = -1;
    } 
    else
    if (selectOption.equals (SHOW_MONTH_STR)) {
      this.selectOption = SHOW_MONTH;
      showDueInDays = 31;
    } 
    else
    if (selectOption.equals (SHOW_WEEK_STR)) {
      this.selectOption = SHOW_WEEK;
      showDueInDays = 7;
    } 
    else
    if (selectOption.equals (SHOW_TODAY_STR)) {
      this.selectOption = SHOW_TODAY;
      showDueInDays = 1;
    } 
    else
    if (selectOption.equals (SHOW_ALL_STR)) {
      this.selectOption = SHOW_ALL;
      showDueInDays = -1;
    } 
    else
    if (selectOption.startsWith (SHOW_DUE_IN_NEXT_STR)) {
      StringScanner opt = new StringScanner (selectOption);
      this.selectOption = SHOW_DUE_IN_NEXT;
      showDueInDays = opt.extractInteger(9999);
    }
    else {
      this.selectOption = SHOW_ALL;
    }
    setSelectDate();
  }
  
  private void setSelectDate () {
    GregorianCalendar today = new GregorianCalendar();
    showBefore = new GregorianCalendar (
        today.get (Calendar.YEAR), 
        today.get (Calendar.MONTH),
        today.get (Calendar.DAY_OF_MONTH));
    
    switch (selectOption) {
      case SHOW_MONTH:
        showBefore.add (Calendar.MONTH, 1);
        break;
      case SHOW_WEEK:
        showBefore.add (Calendar.DAY_OF_MONTH, 7);
        break;
      case SHOW_TODAY:
        showBefore.add (Calendar.DAY_OF_MONTH, 1);
        break;
      case SHOW_DUE_IN_NEXT:
        showBefore.add (Calendar.DAY_OF_MONTH, showDueInDays);
        break;
      default:
        break;
    }
    
    showBeforeDate = showBefore.getTime();
  }
  
  public boolean showAll () {
    return (selectOption == SHOW_ALL);
  }
  
  public boolean selected (WisdomItem item) {
    return true;
  }
  
  public boolean equals (Object obj2) {
    ItemSelector select2;
    try {
      select2 = (ItemSelector)obj2;
    } catch (ClassCastException e) {
      return false;
    }
    return (select2.selectOption == this.selectOption
        && select2.showDueInDays == this.showDueInDays);
  }
  
  public String toString() {
    return selectOptionString;
  }
  
}
