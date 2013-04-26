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

package com.powersurgepub.iwisdom;

/**
   An object used to navigate through a tree structure. 
 */
public class TreeCompass {
  
  private int             result = 0;
  public static final int   BELOW_THIS_NODE = 10;
  public static final int   ABOVE_THIS_NODE = -10;
  public static final int   GREATER_THAN_THIS_NODE = 1;
  public static final int   LESS_THAN_THIS_NODE = -1;
  public static final int   EQUALS_THIS_NODE = 0;
  
  private Object          userObjectToAdd;
  
  private boolean         continueAfterAdd = true;
  
  /** 
    Creates a new instance of TreeCompass. 
   */
  public TreeCompass(int result) {
    this.result = result;
  }
  
  /** 
    Creates a new instance of TreeCompass. 
   */
  public TreeCompass(int result, 
      Object userObjectToAdd, 
      boolean continueAfterAdd) {
    this.result = result;
    this.userObjectToAdd = userObjectToAdd;
    this.continueAfterAdd = continueAfterAdd;
  }
  
  /** 
    Sets the result. 
   */
  public void setResult (int result) {
    this.result = result;
  }
  
  /** 
    Sets the user object to be added as part of new node. 
   */
  public void setUserObjectToAdd (Object userObjectToAdd) {
    this.userObjectToAdd = userObjectToAdd;
  }
  
  /** 
    Sets the continue after add flag. 
   */
  public void setContinueAfterAdd (boolean continueAfterAdd) {
    this.continueAfterAdd = continueAfterAdd;
  }
  
  /**
    Gets the direction in which we need to go, relative to where
    we are now.
   */
  public int getResult () {
    return result;
  }
  
  /**
    Gets the direction in which we need to go, relative to where
    we are now.
   */
  public Object getUserObjectToAdd () {
    return userObjectToAdd;
  }
  
  /**
    Gets the direction in which we need to go, relative to where
    we are now.
   */
  public boolean doWeContinueAfterAdd () {
    return continueAfterAdd;
  }
  
}
