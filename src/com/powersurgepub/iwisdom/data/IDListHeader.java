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
 * Header for a list of IDNumber objects.
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
