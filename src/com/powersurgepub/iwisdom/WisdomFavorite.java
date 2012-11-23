/*
 * WisdomFavorite.java
 *
 * Created on March 17, 2007, 4:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.powersurgepub.iwisdom;

/**
 *
 * @author hbowie
 */
public class WisdomFavorite {
  
  private   String          name;
  private   String          url;
  private   boolean         importable;
  
  /** Creates a new instance of WisdomFavorite */
  public WisdomFavorite() {
  }
  
  public WisdomFavorite (String name, String url, boolean importable) {
    setName (name);
    setURL (url);
    setImportable (importable);
  }
  
  public void setName (String name) {
    this.name = name;
  }
  
  public String getName () {
    return name;
  }
  
  public void setURL (String url) {
    this.url = url;
  }
  
  public String getURL () {
    return url;
  }
  
  public void setImportable (boolean importable) {
    this.importable = importable;
  }
  
  public boolean isImportable () {
    return importable;
  }
  
  public String toString () {
    return name;
  }
  
}
