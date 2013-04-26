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
 *
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
