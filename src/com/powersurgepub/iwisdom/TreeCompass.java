package com.powersurgepub.iwisdom;

/**
   An object used to navigate through a tree structure. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/19 - Originally written. 
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/19 - Originally written. 
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
