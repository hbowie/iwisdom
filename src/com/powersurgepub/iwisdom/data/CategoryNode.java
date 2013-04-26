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

  import java.io.*;
  import javax.swing.tree.*;

/**
   One node on a tree of ToDoItems that is organized by category. 
 */
public class CategoryNode 
    extends DefaultMutableTreeNode {
      
  public static final int   BELOW_THIS_NODE = 10;
  public static final int   ABOVE_THIS_NODE = -10;
  public static final int   AFTER_THIS_NODE = 1;
  public static final int   BEFORE_THIS_NODE = -1;
  public static final int   EQUALS_THIS_NODE = 0;
      
  private int             nodeType = -1;
  public static final int   ROOT = 0;
  public static final int   CATEGORY = 1;
  public static final int   TAG = 1;
  public static final int   ITEM = 2;
  
  private int             categoryIndex = 0;
  
  private CategoryNode    nextNodeForItem = null;
  
  // Show items before categories with identical category parents?
  private             boolean itemsBeforeCategories = true;
  
  /** 
    Creates a root node. 
   */
  public CategoryNode(File file) {
    super (file, true);
    nodeType = ROOT;
  }
  
  /** 
    Creates a category node. 
   */
  public CategoryNode (String category) {
    super (category, true);
    nodeType = CATEGORY;
  }
  
  /** 
    Creates a to do item node. 
   */
  public CategoryNode (WisdomItem item, int categoryIndex) {
    super (item, false);
    nodeType = ITEM;
    this.categoryIndex = categoryIndex;
  }
  
  public void setNextNodeForItem (CategoryNode nextNodeForItem) {
    this.nextNodeForItem = nextNodeForItem;
  }
  
  public boolean hasNextNodeForItem () {
    return nextNodeForItem != null;
  }
  
  public CategoryNode getNextNodeForItem () {
    return nextNodeForItem;
  }

  public boolean isDuplicateOf(CategoryNode node2) {
    if (node2 == null) {
      return false;
    }
    if (this.getNodeType() != node2.getNodeType()) {
      return false;
    }
    switch (nodeType) {
      case (ROOT):
        return true;
      case (CATEGORY):
        return (this.getCategoryString().equals(node2.getCategoryString()));
      case (ITEM):
        return (this.getWisdomItem().getItemNumber()
              == node2.getWisdomItem().getItemNumber()
            && this.getWisdomItem().getTitle().equals
              (node2.getWisdomItem().getTitle())
            && this.getItemNodeCategory().equals
              (node2.getItemNodeCategory()));
      default:
        return false;
    }
  }
  
  /**
    Compare two category nodes and determine their relative locations 
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
  public int compareToNode (CategoryNode currNode, 
      ItemComparator comp) {
    int levels = getLevels();
    int result = 0;
    if (currNode == null) {
      result = BEFORE_THIS_NODE;
    } else {
      int currLevel = currNode.getLevel();
      // Since this new node is an item, it should go below its category node, hence
      // at its category level + 1.
      int newLevel = levels + 1;
      int currType = currNode.getNodeType();
      switch (currType) {
        case (ROOT):
          // we're at the top of the tree -- everything goes below this
          result = BELOW_THIS_NODE;
          break;
        case (CATEGORY):
          if (currLevel > newLevel) {
            // Since this new node is an item, it should go below its category node, hence
            // at its category level + 1. If the category node we are now on is at a lower
            // level, then we need to go back up the tree to find the right position
            // for this new item. 
            result = ABOVE_THIS_NODE;
          }
          else
          if (currLevel == newLevel) { 
            // If the category node we are now on is at the same level
            // as the item we are trying to place, then this item should go
            // after it (if items go after sub-categories at the same level).
            if (itemsBeforeCategories) {
              result = BEFORE_THIS_NODE; 
            } else {
              result = AFTER_THIS_NODE;
            }
          }
          else {
            // We are currently on a category node with a number of levels
            // less than or equal to the number of levels of the item
            // we are trying to place. 
            String levelCat = getLevel (currLevel - 1);
            result = levelCat.compareToIgnoreCase(currNode.toString());
            if (result > 0) {
              // New item's sub-category at this level is greater than
              // this category node's sub-category value. 
              result = AFTER_THIS_NODE;
            } 
            else
            if (result < 0) {
              // New item's sub-category at this level is less than
              // this category node's sub-category value. 
              result = BEFORE_THIS_NODE;
            } else {
              // Sub-categories at this level are equal, so item
              // goes below the matching category node. 
              result = BELOW_THIS_NODE;
            }
          }
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
            result = comp.compare (getWisdomItem(), currNode.getWisdomItem());
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
            if (itemsBeforeCategories) {
              result = AFTER_THIS_NODE;
            } else {
              result = BEFORE_THIS_NODE;
            }
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
  
  public String getLevel (int levelIndex) {
    if (nodeType == ITEM) {
      if (levelIndex >= 0 && levelIndex < getLevels()) {
        return getCategory().getLevel(categoryIndex, levelIndex);
      } else {
        return "";
      }
    } else {
      return "";
    }
  }
  
  public int getLevels () {
    if (nodeType == ITEM) {
      return getCategory().getLevels (categoryIndex);
    } else {
      return 0;
    }
  }

  /**
   Return the tags as a String, whether the node type is an item or tags.

   @return Tags as a string.
   */
  public String getTagsAsString () {
    if (nodeType == ITEM) {
      return ((WisdomItem)getUserObject()).getCategoryString();
    }
    else
    if (nodeType == TAG) {
      StringBuilder tags = new StringBuilder();
      CategoryNode tagNode = this;
      while (tagNode != null && tagNode.getNodeType() == TAG) {
        if (tags.length() > 0) {
          tags.insert(0, Category.PREFERRED_LEVEL_SEPARATOR);
        }
        tags.insert(0, (String)tagNode.getUserObject());
        tagNode = (CategoryNode)tagNode.getParent();
      }
      return tags.toString();
    } else {
      return "";
    }
  }
  
  public Category getCategory () {
    if (nodeType == ITEM) {
      return getWisdomItem().getCategory();
    } else {
      return null;
    }
  }

  public String getCategoryString() {
    if (nodeType == CATEGORY) {
      return (String)getUserObject();
    } else {
      return null;
    }
  }

  public String getItemNodeCategory() {
    if (nodeType == ITEM) {
      return getWisdomItem().getCategory().getCategory(categoryIndex);
    } else {
      return null;
    }
  }
  
  public WisdomItem getWisdomItem () {
    if (nodeType == ITEM) {
      return (WisdomItem)getUserObject();
    } else {
      return null;
    }
  }
  
}
