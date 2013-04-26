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

  import java.awt.*;
  import javax.swing.*;

/**
   An object capable of rendering the rating of a To Do item
   in a JTable cell. 
 */
public class ItemRatingRenderer 
    extends javax.swing.table.DefaultTableCellRenderer 
        implements javax.swing.table.TableCellRenderer {
          
  public static final Color RATING_1_COLOR = new Color (204, 0, 0);
  public static final Color RATING_2_COLOR = new Color (102, 102, 204);
  public static final Color RATING_3_COLOR = new Color (0, 0, 255);
  public static final Color RATING_4_COLOR = new Color (102, 102, 0);
  public static final Color RATING_5_COLOR = new Color (0, 153, 51);
  public static final Color[] COLORS = { RATING_1_COLOR, RATING_2_COLOR,
      RATING_3_COLOR, RATING_4_COLOR, RATING_5_COLOR };
  
  /**
   * Creates a new instance of ItemRatingRenderer
   */
  public ItemRatingRenderer() {
    this.setHorizontalAlignment (JLabel.CENTER);
  }
  
  public void setText (String text) {
    super.setText(text);
    int ratingIndex;
    try {
      ratingIndex = Integer.parseInt(text);
      ratingIndex--;
      if (ratingIndex >= 0 && ratingIndex <= 4) {
        this.setForeground (COLORS [ratingIndex]);
      }
    } catch (NumberFormatException e) {
    }
  }
  
  /* public java.awt.Component getTableCellRendererComponent
      (javax.swing.JTable jTable, ItemRating ratingObject, 
      boolean param, boolean param3, int param4, int param5) {
    java.awt.Component component 
        = super.getTableCellRendererComponent
            (jTable, ratingObject, param, param3, param4, param5);
    System.out.println ("ItemRatingRenderer working...");
    component.setForeground (COLORS [ratingObject.getRating() - 1]);
    // try {
      JLabel label = (JLabel)component;
      label.setHorizontalAlignment (JLabel.CENTER);
    // } catch (
      
    return component;
  } */
  
}
