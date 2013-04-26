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

  import java.util.*;
  import javax.swing.*;

/**
 *
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
