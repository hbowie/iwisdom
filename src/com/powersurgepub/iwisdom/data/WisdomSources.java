package com.powersurgepub.iwisdom.data;

  import com.powersurgepub.psdatalib.ui.ValueList;
  import java.util.*;

/**
   A collection of all the authors identified in the current 
   collection of WisdomItems.
 */
public class WisdomSources {
  
  private     ArrayList       sources;
  private     ValueList       sourceValues;    
  
  public WisdomSources () {
    clear();
  }
  
  public void display () {
    for (int i = 0; i < size(); i++) {
      WisdomSource source = get (i);
      source.display();
    }
  }
  
  public void clear () {
    sources = new ArrayList();
    sourceValues = new ValueList();
  }
  
  public int size () {
    return sources.size();
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
  public WisdomSource addOrMerge (WisdomSource source2) {
    add (source2);
    int i = indexOf (source2);
    return get (i);
  }
  
  /**
   If the author is not already on the list, then it is added. If it is 
   already on the list, then any additional data available from the new object
   is merged into the existing author object already on the list.
   
   @param author Author to be added or merged.
   @return True if author was added, false if author was already present.
   */
  
  public boolean add (WisdomSource source) {
    int i = indexOf (source);
    if (i < 0) {
      sources.add (source);
      sourceValues.registerValue (source.getTitle());
      return true;
    } else {
      WisdomSource s = get (i);
      s.merge (source);
      return false;
    }
  }
  
  public WisdomSource get (int i) {
    if (i < 0 || i >= size()) {
      return null;
    } else {
      return (WisdomSource) sources.get (i);
    }
  }
  
  public int indexOf (WisdomSource source) {
    boolean found = false;
    int i = 0;
    while (i < size() && (! found)) {
      WisdomSource s = get (i);
      found = s.equals (source);
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
  
  public int indexOf (String sourceName) {
    boolean found = false;
    int i = 0;
    while (i < size() && (! found)) {
      WisdomSource s = get (i);
      found = s.equals (sourceName);
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
  
  public boolean contains (WisdomSource source) {
    return (indexOf (source) >= 0);
  }
  
  public Iterator iterator () {
    return sources.iterator();
  }
  
  public ValueList getValueList () {
    return sourceValues;
  }

}
