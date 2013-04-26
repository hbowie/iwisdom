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

  import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.*;
  import java.io.*;
  import java.util.*;
  import javax.swing.tree.*;

/**
   A collection of to do items that is sorted by category, and 
   provides the underlying data model for a JTree display of the
   items. 
 */
public class CategoryModel  
    implements ItemsView, ItemNavigator {
      
  private iWisdomCommon            td;
          
  private DefaultTreeModel        tree;
  
  // Array of nodes using each node's number as its location in the array
  // Only the first node for each wisdom item is stored in this array. If an
  // item has multiple categories, then nodes other than the first will be 
  // chained to the first. 
  private ArrayList               nodes;
  
  private WisdomItems             items;
  
  private ItemSelector            select;
  
  private ItemComparator          comp;
  
  private CategoryNode            root;
  
  private CategoryNode            currentNode;
  
  private CategoryNode            nextNode;
  
  private CategoryNode            priorNode;

  private boolean                 removing = false;
  
  /** 
    Creates a new instance of CategoryModel. 
   */
  public CategoryModel(WisdomItems items, 
      ItemComparator comp, 
      ItemSelector select,
      File source) {
    if (source == null) {
      File unknown = new File (System.getProperty (GlobalConstants.USER_DIR), "???");
      root = new CategoryNode(unknown);
    } else {    
      root = new CategoryNode(source);
    }
    tree = new DefaultTreeModel (root, true);
    nodes = new ArrayList();
    setItems (items);
    setComparator (comp);
    setSelector (select);
  }
  
  public void setItems(WisdomItems items) {
    this.items = items;
  }
  
  public void setComparator (ItemComparator comp) {
    this.comp = comp;
  }
  
  public void setSelector (ItemSelector select) {
    this.select = select;
  }
  
  /**
   *    Save a pointer to the iWisdomCommon object.
   *   
   *    @param common Pointer to the iWisdomCommon object.
   */
  public void setCommon(iWisdomCommon common) {
    td = common;
  }
  
  public DefaultTreeModel getModel() {
    return tree;
  }
  
  /**
    Make sure this view is sorted in the proper sequence
    and contains only selected items.
   */
  public void sort() {
    // Make sure each item is selected and in correct location
    for (int i = 0; i < items.size(); i++) {
      modify (items.get(i));
    }
  }
  
  /**
   *    Process a new WisdomItem that has just been modified within the WisdomItems collection.
   *   
   *    @param item   WisdomItem just modified.
   */
  public void modify(WisdomItem item) {
    // System.out.println (" ");
    // System.out.println ("CategoryModel modify " + item.toString());
    remove (item);
    add (item);
  }
  
  /**
   *    Process a WisdomItem that has just been logically deleted from the WisdomItems collection.
   *   
   *    @param item   WisdomItem just logically deleted (by setting Deleted flag).
   */
  public void remove (WisdomItem item) {
    // System.out.println (" ");
    // System.out.println ("CategoryModel remove index = " + item.getItemNumber());
    int index = item.getItemNumber();
    CategoryNode itemNode = null;
    CategoryNode nextNode = null;
    if (index >= 0 && index < nodes.size()) {
      itemNode = (CategoryNode)nodes.get (index);
      nodes.set (index, null);
    }
    while (itemNode != null) {
      // System.out.println ("  Removing node " + itemNode.toString());
      nextNode = itemNode.getNextNodeForItem();
      tree.removeNodeFromParent (itemNode);
      itemNode = nextNode;
    }
  }
  
  public void add(WisdomItem item) {
    // System.out.println (" ");
    // System.out.println ("Category Model add "
    //     + String.valueOf(item.getItemNumber())
    //     + " " + item.toString());
    int index = item.getItemNumber();
    CategoryNode lastNode = null;
    if (index >= 0) {
      if (selected (item)) {
        for ( int categoryIndex = 0; 
              categoryIndex < item.getCategories(); 
              categoryIndex++ ) {
          CategoryNode itemNode = new CategoryNode (item, categoryIndex);
          // System.out.println ("  Storing Category Node "
          //     + String.valueOf(categoryIndex) + ". "+ itemNode.getItemNodeCategory()
          //     + " - " + itemNode.toString());

          // Now store it in the tree
          CategoryNode currentNode = root;
          CategoryNode parentNode = root;
          Category category = item.getCategory();
          // levels = the number of levels in the new item's category
          int levels = category.getLevels (categoryIndex);
          boolean done = false;
          int compass = CategoryNode.BELOW_THIS_NODE;
          // level is used to keep track of our current depth as we walk
          // through the tree. 
          int level = -1;
          // child is used to keep track of our position within the
          // current set of children we are traversing.
          int child = 0;
          // Check for duplicates
          boolean nodeIsDuplicate = false;
          CategoryNode dupe = null;
          // Walk through the tree until we find the right location
          // for the item to be added
          do {
            compass = itemNode.compareToNode (currentNode, comp);
            if (itemNode.isDuplicateOf(currentNode)) {
              dupe = currentNode;
              nodeIsDuplicate = true;
            }
            CategoryNode newNode;
            switch (compass) {
              case (CategoryNode.BELOW_THIS_NODE):
                if (currentNode.getChildCount() > 0) {
                  // New item should be below this one, and children already exist
                  newNode = (CategoryNode)currentNode.getFirstChild();
                  parentNode = currentNode;
                  currentNode = newNode;
                  level++;
                  child = 0;
                } else {
                  // New item should be below this one, but no children yet exist
                  level++;
                  if (levels > level) {
                    // Not yet at desired depth -- create a new category node
                    String levelCat = category.getLevel (categoryIndex, level);
                    newNode = new CategoryNode (levelCat);
                  } else {
                    // we're at desired depth -- add the new item node
                    newNode = itemNode;
                    done = true;
                  }
                  if (! nodeIsDuplicate) {
                    tree.insertNodeInto (newNode, currentNode, 0);
                    parentNode = currentNode;
                    currentNode = newNode;
                    level++;
                    child = 0;
                  }
                } // end go below but no children
                break;
              case (CategoryNode.AFTER_THIS_NODE):
                // new node should be after this one -- keep going
                newNode = (CategoryNode)currentNode.getNextSibling();
                currentNode = newNode;
                child++;
                break;
              case (CategoryNode.BEFORE_THIS_NODE):
                // Don't go any farther -- put it here or add a new category
                if (levels > level) {
                  String levelCat = category.getLevel(categoryIndex, level);
                  newNode = new CategoryNode (levelCat);
                } else {
                  newNode = itemNode;
                  done = true;
                }
                if (! nodeIsDuplicate) {
                  tree.insertNodeInto (newNode, parentNode, child);
                  currentNode = newNode;
                }
                break;
              case (CategoryNode.EQUALS_THIS_NODE):
                if (! nodeIsDuplicate) {
                  newNode = itemNode;
                  tree.insertNodeInto (newNode, parentNode, child);
                  currentNode = newNode;
                }
                done = true;
                break;
              default:
                td.logEvent (LogEvent.MAJOR, 
                    "Category Model add -- hit default in switch", false);
                td.logEvent (LogEvent.NORMAL, "Compass value = " 
                    + String.valueOf (compass), false);
                td.logEvent (LogEvent.NORMAL, "itemNode = " 
                    + String.valueOf (itemNode.getNodeType())  + " - "
                    + itemNode.toString(), false);
                td.logEvent (LogEvent.NORMAL, "Current node = " 
                    + String.valueOf (currentNode.getNodeType())  + " - "
                    + currentNode.toString(), false);
                    break;
            } // end of compass result switch
          } while (! done);

          // Store this node so we can find it later by its index
          if (nodeIsDuplicate) {
            lastNode = dupe;
          } else {
            if (categoryIndex == 0) {
              while (nodes.size() < index) {
                nodes.add (null);
              }
              if (index == nodes.size()) {
                nodes.add (itemNode);
              } else {
                nodes.set (index, itemNode);
              }
            } else {
              lastNode.setNextNodeForItem (itemNode);
            }
            lastNode = itemNode;
          }
        } // end for each category assigned to item
      } // end if item selected
    } // end if index >= 0
  }
  
  /**
    See if item should be included in this view.
   
    @param item Item to be included or excluded from this view.
   */
  public boolean selected (WisdomItem item) {
    return (select.selected (item) && (! item.isDeleted()));
  }
  
  public void firstItem() {
    currentNode = getNextItem (root, 1);
    if (currentNode.getNodeType() == CategoryNode.ITEM) {
      setNextPrior();
      td.setItem ((WisdomItem)currentNode.getUserObject());
    }
  }
  
  public void lastItem() {
    currentNode = getNextItem (root, -1);
    if (currentNode.getNodeType() == CategoryNode.ITEM) {
      setNextPrior();
      td.setItem ((WisdomItem)currentNode.getUserObject());
    }
  }
  
  public void nextItem() {
    if (nextNode.getNodeType() == CategoryNode.ITEM) {
      currentNode = nextNode;
      setNextPrior();
      WisdomItem currentItem = (WisdomItem)currentNode.getUserObject();
      td.setItem (currentItem);
    }
  }
  
  public void priorItem() {
    if (priorNode.getNodeType() == CategoryNode.ITEM) {
      currentNode = priorNode;
      setNextPrior();
      td.setItem ((WisdomItem)currentNode.getUserObject());
    }
  }
  
  /*
   private void checkCurrentNode (WisdomItem item) {
    if (currentNode == null) {
      selectItem (item);
    }
  } */
  
  public void selectItem(WisdomItem item) {
    currentNode = (CategoryNode)nodes.get (item.getItemNumber());
    setNextPrior();
  }
  
  private void setNextPrior () {
    if (currentNode != null) {
      nextNode = getNextItem (currentNode, 1);
      priorNode = getNextItem (currentNode, -1);
    }
  }
  
  private CategoryNode getNextItem (CategoryNode startNode, int increment) {
    CategoryNode currNode = startNode;
    CategoryNode nextNode;
    boolean childrenExhausted = false;
    // Keep grabbing the next node until we have one that is an Item
    // or the Root (not a Category).
    do {
      if ((currNode.getNodeType() == CategoryNode.ITEM)
          || (childrenExhausted)
          || (currNode.getChildCount() == 0)) {
        if (increment >= 0) {
          nextNode = (CategoryNode)currNode.getNextSibling();
        } else {
          nextNode = (CategoryNode)currNode.getPreviousSibling();
        }
        if (nextNode == null) {
          nextNode = (CategoryNode)currNode.getParent();
          if (nextNode == null) {
            td.logEvent (LogEvent.MAJOR, "Current node " 
                + String.valueOf (currNode.getNodeType())  + " - "
                + currNode.toString()
                + " has no parent!", false);
          }
          childrenExhausted = true;
        } else {
          childrenExhausted = false;
        }
      } else {
        // look for children 
        if (increment >= 0) {
          nextNode = (CategoryNode)currNode.getFirstChild();
        } else {
          nextNode = (CategoryNode)currNode.getLastChild();
        }
      }
      if (nextNode == null) {
        td.logEvent (LogEvent.MAJOR, 
            "CategoryModel  getNextItem -- null category node", 
            false);
        td.logEvent (LogEvent.NORMAL, "Starting node = " 
            + String.valueOf (startNode.getNodeType())  + " - "
            + startNode.toString(), false);
        td.logEvent (LogEvent.NORMAL, "Current node = " 
            + String.valueOf (currNode.getNodeType())  + " - "
            + currNode.toString(), false);
      }
      currNode = nextNode;
    } while (currNode.getNodeType() == CategoryNode.CATEGORY);
    return currNode;
  } // end method
  
  /**
    Get the item's position within this collection.
   
    @return Current item's position within this collection, where zero points
            to the first.
   */
  public int getItemNumber () {
    if (currentNode.getNodeType() == CategoryNode.ITEM) {
      WisdomItem currItem = (WisdomItem)currentNode.getUserObject();
      return currItem.getItemNumber();
    } else {
      return 0;
    }
  }
  
  /**
    Returns the size of the collection.
    
    @return Size of the collection.
   */
  public int size () {
    return items.size();
  }
  
	/**
	   Returns the object in string form.
	  
	   @return Name of this class.
	 */
	public String toString() {
    return "CategoryModel";
	}
  
} // end class
