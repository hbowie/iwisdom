package com.powersurgepub.iwisdom.data;

  import java.io.*;
  import javax.swing.tree.*;

/**
   One node on a tree of ToDoItems that is organized by ID Number. <p>
  
   This code is copyright (c) 2005 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/16 - Originally written. 
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/16 - Originally written. 
 */
public class IDNode 
    extends DefaultMutableTreeNode {
      
  public static final int   BELOW_THIS_NODE = 10;
  public static final int   ABOVE_THIS_NODE = -10;
  public static final int   AFTER_THIS_NODE = 1;
  public static final int   BEFORE_THIS_NODE = -1;
  public static final int   EQUALS_THIS_NODE = 0;
      
  private int             nodeType;
  public static final int   ROOT = 0;
  public static final int   ITEM = 1;
  
  /** 
    Creates a root node. 
   */
  public IDNode(File file) {
    super (file, true);
    nodeType = ROOT;
  }
  
  /** 
    Creates a to do item node. 
   */
  public IDNode (WisdomItem item) {
    super (item, false);
    nodeType = ITEM;
  }
  
  /**
    Compare two nodes and determine their relative locations 
    in the tree. 
   
    @return Value indicating relationship of the new node (this node) to the
            current node location in the tree. 
            ABOVE_THIS_NODE = The new node should be higher in the tree than
                              the current node.
            BELOW_THIS_NODE = The new node should be below the
                              current node. 
            AFTER_THIS_NODE = The new node should be at the same level as this node,
                              but come after it.
            BEFORE_THIS_NODE = The new node should be at the same level as this
                               node, but come before it.
            EQUALS_THIS_NODE = The two nodes have equal keys. 
   
    @param currNode Node already in the tree, to be compared to this one.
    @param comp Item comparator to use for comparing two items. 
   */
  public int compareToNode (IDNode currNode, 
      ItemComparator comp) {
    WisdomItem item = (WisdomItem)getUserObject();
    // IDNumber id = item.getIDNumber();
    // int levels = id.getLevel();
    int levels = 0;
    int result = 0;
    if (currNode == null) {
      result = BEFORE_THIS_NODE;
    } else {
      int currLevel = currNode.getLevel();
      int newLevel = levels;
      int currType = currNode.getNodeType();
      switch (currType) {
        case (ROOT):
          // we're at the top of the tree -- everything goes below this
          result = BELOW_THIS_NODE;
          break;
        case (ITEM):
          if (currLevel > newLevel) {
            // if this item is at a higher (deeper) level than the one
            // that this new one should go at, then we need to go back
            // up the tree. 
            result = ABOVE_THIS_NODE;
          }
          else
          if (currLevel == newLevel) { 
            // Item should go at this level
            result = comp.compare (item, currNode.getUserObject());
            if (result > 0) {
              // New item sorts after the current item we are on
              result = AFTER_THIS_NODE;
            } 
            else
            if (result < 0) {
              // New item sorts before the current item we are on
              result = BEFORE_THIS_NODE;
            } else {
              // New item has an equal key with the current item we are on:
              // put the new item first. 
              result = BEFORE_THIS_NODE;
            }
          } else {
            // if this item is at a lower (shallower) level than the level
            // that this new one should go at, then put the new item
            // before this node.  
            result = AFTER_THIS_NODE;
          }
          break;
        default:
          // Unexpected node type -- should never arrive here
          break;
      }
    }
    
    return result;
  }
  
  public int getNodeType() {
    return nodeType;
  }
  
}
