package com.powersurgepub.iwisdom.data;

/**
   An object representing the rating of a To Do item. <p>
  
   This code is copyright (c) 2003 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/02 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2004/06/08 - Added equals method. 
 */
public class ItemRating {
  
  private int rating = 3;
  
  /**
   * 
   *   Creates a new instance of ItemRating.
   */
  public ItemRating() {
  }
  
  /**
   * 
   *   Creates a new instance of ItemRating.
   */
  public ItemRating(int rating) {
    setRating (rating);
  }
  
  /**
   * 
   *   Creates a new instance of ItemRating.
   */
  public ItemRating(String rating) {
    setRating (rating);
  }
  
  public void setRating (String ratingString) {
    int rating = 3;
    try {
      rating = Integer.parseInt (ratingString);
    } catch (NumberFormatException e) {
    }
    setRating (rating);
  }
  
  public void setRating (int rating) {
    this.rating = rating;
  }
  
  public int getRating () {
    return rating;
  }
  
  public boolean equals (Object object2) {
    ItemRating rating2 = (ItemRating) object2;
    return (rating == rating2.getRating());
  }
  
  public String toString() {
    return String.valueOf(rating);
  }
  
}
