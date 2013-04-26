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

  import com.powersurgepub.psdatalib.ui.ValueList;
  import java.util.*;

/**
   A collection of all the authors identified in the current 
   collection of WisdomItems.
 */
public class Authors {
  
  private     ArrayList       authors;
  private     ValueList       authorValues;    
  
  public Authors () {
    clear();
  }
  
  public void display () {
    for (int i = 0; i < size(); i++) {
      Author author = get (i);
      author.display();
    }
  }
  
  public void clear () {
    authors = new ArrayList();
    authorValues = new ValueList();
  }
  
  public int size () {
    return authors.size();
  }
  
  public boolean isEmpty () {
    return (size() < 1);
  }
  
  /**
   Add if not already there; merge if it is; return the resulting object
   in the list (whether the one passed or one that previously existed).
   @param The new author object
   @return The object to be used for this author. 
   */
  public Author addOrMerge (Author author2) {
    add (author2);
    int i = indexOf (author2);
    return get (i);
  }
  
  /**
   If the author is not already on the list, then it is added. If it is 
   already on the list, then any additional data available from the new object
   is merged into the existing author object already on the list.
   
   @param author Author to be added or merged.
   @return True if author was added, false if author was already present.
   */
  
  public boolean add (Author author) {
    int i = indexOf (author);
    if (i < 0) {
      authors.add (author);
      authorValues.registerValue (author.getCompleteName());
      authorValues.registerValue (author.getCompleteNameLastNamesFirst());
      return true;
    } else {
      Author a = get (i);
      a.merge (author);
      return false;
    }
  }
  
  public Author get (int i) {
    if (i < 0 || i >= size()) {
      return null;
    } else {
      return (Author)authors.get (i);
    }
  }
  
  public int indexOf (Author author) {
    boolean found = false;
    int i = 0;
    while (i < size() && (! found)) {
      Author a = get (i);
      found = a.equals (author);
      if (! found) {
        i++;
      }
    }
    if (found) {
      return i;
    } else {
      return -1;
    }
  }
  
  public int indexOf (String authorName) {
    boolean found = false;
    int i = 0;
    while (i < size() && (! found)) {
      Author a = get (i);
      found = a.equals (authorName);
      if (! found) {
        i++;
      }
    }
    if (found) {
      return i;
    } else {
      return -1;
    }
  }
  
  public boolean contains (Author author) {
    return (indexOf (author) >= 0);
  }
  
  public Iterator iterator () {
    return authors.iterator();
  }
  
  public ValueList getValueList () {
    return authorValues;
  }

}
