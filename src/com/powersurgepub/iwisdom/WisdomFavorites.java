/*
 * WisdomFavorites.java
 *
 * Created on March 17, 2007, 4:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.iwisdom;

  import java.util.*;
  import javax.swing.*;

/**
 *
 * @author hbowie
 */
public class WisdomFavorites {
  
  private   WisdomFavorite      nada = new WisdomFavorite ("", "", false);
  
  private   ArrayList           favorites = new ArrayList();
  
  /** Creates a new instance of WisdomFavorites */
  public WisdomFavorites() {
  }
  
  public void add (String name, String url, boolean importable) {
    WisdomFavorite favorite = new WisdomFavorite (name, url, importable);
    favorites.add (favorite);
  }
  
  public String getName (int i) {
    return get(i).getName();
  }
  
  public String getURL (int i) {
    return get(i).getURL();
  }
  
  public boolean isImportable (int i) {
    return get(i).isImportable();
  }
  
  public void setComboBox (JComboBox combo) {
    combo.removeAllItems();
    for (int i = 0; i < size(); i++) {
      if (isImportable (i)) {
        combo.addItem (get(i));
      } // end if importable
    } // end for each favorite
  } // end method setComboBox
  
  public WisdomFavorite get (int i) {
    if (i >= 0 && i < size()) {
      return (WisdomFavorite)favorites.get(i);
    } else {
      return nada;
    }
  }
  
  public int size () {
    return favorites.size();
  }
  
}
