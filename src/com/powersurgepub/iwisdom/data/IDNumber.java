package com.powersurgepub.iwisdom.data;

  import java.util.*;

/**
 * A sequence number, and possible sub-numbers, indicating the position of a 
 * to do item in an indented list. 
 *
 * @author Herb Bowie
 */
public class IDNumber {
  
  public final static int CHILD = 1;
  public final static int SIBLING = 2;
  
  
  private     IDListHeader header   = null;
  private     IDNumber     priorID  = null;
  private     IDNumber     nextID   = null;
  private     StringBuffer idString = new StringBuffer();
  private     ArrayList    idComp   = new ArrayList();
  
  // Used to walk through the tree
  private     IDNumber     treeCurrent;
  private     IDNumber     treeNext;
  
  /** 
   * Creates a new instance of ItemID with no input data.
   * A new list header will be created, and this will become
   * the first (and only) id in the list.
   */
  public IDNumber() {
    header = new IDListHeader();

    setID ("1");
    header.checkIDNumber (this);
    System.out.println ("Constructing IDNumber 1 " + toString());
  }
  
  /**
   * Creates a new instance of ItemID and add it to the end of the list.
   */
  public IDNumber (IDListHeader header) {
    this.header = header;
    int seq = 1;
    IDNumber last = header.getLast();
    if (last != null) {
      seq = last.getIDComponentAsInt(0) + 1;
    }
    
    addComponentID (0, seq);
    setNextID (header.getFirst());
    header.checkIDNumber (this);
    
    System.out.println ("Constructing IDNumber 2 " + toString());
  } // end constructor
  
  /**
   * Creates a new instance of ItemID with input data.
   */
  public IDNumber (IDListHeader header, IDNumber id, int where) {
    this.header = header;
    
    // If no IDNumber is passed, then make this ID the first one in the list
    if (id == null) {
      addComponentID (0, 1);
      setNextID (header.getFirst());
    }
    else
      
    // Make this id a sibling (next ID at the same level) of the id
    // that was passed.
    if (where == SIBLING) {
      int level = id.getLevel();
      int top = level - 1;
      for (int i = 0; i < top; i++) {
        addComponentID (i, id.getIDComponentAsInt(i));
      }
      int number = id.getIDComponentAsInt (top);
      number++;
      addComponentID (top, number);
      insertInTree (id);
    } // end if sibling
    else
      
    // Make this id a child (next ID at one level down) of the id
    // that was passed
    if (where == CHILD) {
      int level = id.getLevel();
      for (int i = 0; i < level; i++) {
        addComponentID (i, id.getIDComponentAsInt(i));
      }
      addComponentID (level, 1);
      insertInTree (id);
    }
    header.checkIDNumber (this);
    System.out.println ("Constructing IDNumber 3 " + toString());
  } // end constructor
  
  private void insertInTree (IDNumber id) {
    
    // Find the proper insertion point for this record
    treeNext = id;
    climbTree();
    while ((treeCurrent != null)
        && (this.lessThan (treeCurrent))) {
      descendTree();
    }
    while ((treeNext != null)
        && (this.greaterThan (treeNext))) {
      climbTree();
    }
    
    // Now insert in chain
    if (treeCurrent != null) {
      treeCurrent.setNextID (this);
    }
    setPriorID (treeCurrent);
    setNextID (treeNext);
    if (treeNext != null) {
      treeNext.setPriorID (this);
    }
    
    // Now increment records appropriately to keep id numbers unique
    while (treeNext != null
        && (treeNext.getLevel() >= this.getLevel())) {
      treeNext.incrementID (this.getLevel(), 1);
      climbTree();
    }
  }
  
  private void descendTree() {
    treeNext = treeCurrent;
    treeCurrent = treeNext.getPriorID();
  }
  
  private void climbTree() {
    treeCurrent = treeNext;
    treeNext = treeCurrent.getNextID();
  }
  
  public IDListHeader getHeader () {
    return header;
  }
  
  public void incrementID (int level, int increment) {
    System.out.println ("incrementID " + String.valueOf(level) + " " + String.valueOf (increment));
    ArrayList oldIDComp = idComp;
    idString = new StringBuffer();
    idComp = new ArrayList();
    int incrementIndex = level - 1;
    for (int i = 0; i < oldIDComp.size(); i++) {
      Integer numberObject = (Integer)oldIDComp.get(i);
      int number = numberObject.intValue();
      if (i == incrementIndex) {
        number = number + increment;
      }
      addComponentID (i, number);
    }
  }
  
  public void setID (String id) {

    this.idString = new StringBuffer();
    idComp = new ArrayList();
    int i = 0;
    int level = 0;
    while (i < id.length()) {
      int idWork = 0;
      char c = ' ';
      while (i < id.length()
          && c != '.') {
        c = id.charAt (i);
        if (Character.isDigit (c)) {
          idWork = (idWork * 10) + Character.getNumericValue (c);
        }
        i++;
      } // end while more id characters in this level
      if (idWork > 0) {
        addComponentID (level, idWork);
        level++;
      }
      
      // If same ID is already on the list, increment until it is unique
      IDNumber matchedID = null;
      do {
        System.out.println ("Setting ID to " + toString());
        matchedID = header.getID (this);
        if (matchedID != null) {
          incrementID (getLevel(), 1);
        }
      } while (matchedID != null);
      
      header.checkIDNumber(this);
      System.out.println ("Setting ID " + toString());
    } // end while more id characters to process
    
  } // end method setID
  
  public void addComponentID (int level, int number) {
    if (level > 0) {
      this.idString.append ('.');
    }
    this.idString.append (String.valueOf (number));
    idComp.add (new Integer (number));
  }
  
  public boolean equals (String id2) {
    return idString.equals (id2);
  }
  
  public boolean equals (IDNumber id2) {
    return (compareTo(id2) == 0);
  }
  
  public boolean lessThan (IDNumber id2) {
    return (compareTo(id2) < 0);
  }
  
  public boolean greaterThan (IDNumber id2) {
    return (compareTo(id2) > 0);
  }
  
  public int compareTo (IDNumber id2) {
    int result = 0;
    int level = 0;
    while ((result == 0)
        && (level < this.getLevel()
            || level < id2.getLevel())) {
      if (level >= this.getLevel()) {
        result = -1;
      }
      else
      if (level >= id2.getLevel()) {
        result = 1;
      } 
      else {
        result = this.getIDComponentAsInteger (level).compareTo
             (id2.getIDComponentAsInteger(level));
      }
      if (result == 0) {
        level++;
      } // end if these two id numbers are equal so far
    } // end while still comparing
    return result;
  } // end method compareTo
  
  public String getIDComponent (int i) {
    return getIDComponentAsInteger(i).toString();
  }
  
  public int getIDComponentAsInt (int i) {
    return getIDComponentAsInteger(i).intValue();
  }
  
  public Integer getIDComponentAsInteger(int i) {
    if (i >= idComp.size()) {
      return new Integer (0);
    } else {
      return ((Integer)idComp.get(i));
    }
  }
  
  public int getLevel () {
    return (idComp.size());
  }
  
  public int getIDComponentsSize() {
    return idComp.size();
  }
  
  public IDNumber setPriorID (IDNumber priorID) {
    IDNumber oldPriorID = this.priorID;
    this.priorID = priorID;
    return oldPriorID;
  }
  
  public IDNumber setNextID (IDNumber nextID) {
    IDNumber oldNextID = this.nextID;
    this.nextID = nextID;
    return oldNextID;
  }
  
  public IDNumber getPriorID () {
    return priorID;
  }
  
  public IDNumber getNextID () {
    return nextID;
  }
  
  public String getID () {
    return idString.toString();
  }
  
  public String toString () {
    return idString.toString();
  }
  
}
