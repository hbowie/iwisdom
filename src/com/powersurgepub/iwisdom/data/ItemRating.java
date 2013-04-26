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
   An object representing the rating of a To Do item. 
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
