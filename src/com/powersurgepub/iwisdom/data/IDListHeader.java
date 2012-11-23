/*
 * IDListHeader.java
 *
 * Created on June 14, 2005, 5:40 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.powersurgepub.iwisdom.data;

/**
 * Header for a list of IDNumber objects.
 *
 * @author Herb Bowie
 */
public class IDListHeader {
  
  private IDNumber    first = null;
  private IDNumber    last  = null;
  
  /** 
   * Creates a new instance of IDListHeader.
   */
  public IDListHeader() {
    System.out.println ("IDListHeader constructed");
  }
  
  public void checkIDNumber (IDNumber check) {
    if (first == null
        || check.lessThan (first)) {
      first = check;
    }
    if (last == null
        || check.greaterThan (last)) {
      last = check;
    }
  }
  
  public IDNumber getFirst () {
    return first;
  }
  
  public IDNumber getLast () {
    return last;
  }
  
  public IDNumber getID (IDNumber id) {
    boolean matched = false;
    IDNumber matchedID = null;
    IDNumber curr = first;
    while ((! matched ) && (curr != null)) {
      if (curr.equals (id)) {
        matched = true;
        matchedID = curr;
      } else {
        curr = curr.getNextID();
      }
    }
    return matchedID;
  }
  
  public IDNumber getID (String id) {
    boolean matched = false;
    IDNumber matchedID = null;
    IDNumber curr = first;
    while ((! matched ) && (curr != null)) {
      if (curr.equals (id)) {
        matched = true;
        matchedID = curr;
      } else {
        curr = curr.getNextID();
      }
    }
    return matchedID;
  }
  
}
