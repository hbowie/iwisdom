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

  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.disk.*;
  import java.io.*;
  import java.util.*;
  import javax.swing.*;

/**
   A list of views. Each view consists of a name, an ItemSelector 
   and an ItemComparator. 
 */
public class ViewList {
  
  public static final String VIEW_NUMBER      = "viewnumber";
  public static final String VIEW_NAME        = "viewname";
  public static final String VIEW_SELECTOR    = "viewselect";
  public static final String VIEW_COMPARATOR  = "viewcompare";
  
  private UserPrefs prefs = UserPrefs.getShared();
  
  private WisdomDiskDirectory files;
  private WisdomDiskStore     primaryStore;
  private ArrayList viewNames = new ArrayList();
  private ArrayList selectors = new ArrayList();
  private ArrayList comparators = new ArrayList();
  
  private JComboBox            comboBox;
  private DefaultComboBoxModel model = new DefaultComboBoxModel();
  private JMenu                menu;
  private int                  currentViewIndex = 0;
  
  private iWisdomCommon td;
  
  /** 
    Creates a new instance of ViewList. 
   */
  public ViewList(WisdomDiskDirectory files) {

    this.files = files;
    
    // Try to load list of views from saved disk file
    primaryStore = files.getPrimaryStore();
    if (primaryStore != null
        && primaryStore.isDiskViewsFileValidInput()) {
      loadDiskViews(primaryStore);
    }
    
    // If we didn't get any views from disk, 
    // then try loading all views used in the list of files
    WisdomDiskStore store;
    ItemComparator comparator;
    ItemSelector selector;
    if (size() < 1) {
      boolean match = false;
      ItemComparator comp2;
      for (int fileNumber = 0; fileNumber < files.size(); fileNumber++) {
        store = files.get (fileNumber);
        comparator = store.getComparator();
        selector = store.getSelector();
        if (comparator != null
            && selector != null) {
          match = false;
          for (int viewNumber = 0; (viewNumber < size() && (! match)); viewNumber++) {
            comp2 = getComparator (viewNumber);
            if (comparator.equals (comp2)) {
              match = true;
              store.setViewIndex (viewNumber);
            } else {
              // System.out.println ("comps do not match");
            }
          }
          if (! match) {
            add (getNextViewName(), selector, comparator);
            store.setViewIndex (size() - 1);
          } // end if this is a new view
        } // end if we have non-null selector and comparator
      } // end for each file
    } // end if loading from list of files
    
    // If we still didn't get any views, create a default view
    if (size() < 1) {
      addDefaultViews();
    }
    
    // Now let's make sure that all files have their comparators set
    for (int fileNumber = 0; fileNumber < files.size(); fileNumber++) {
      store = files.get (fileNumber);
      store.setComparator (getComparator (store.getViewIndex()));
    }
  } // end constructor
  
  public void setCommon (iWisdomCommon td) {
    this.td = td;
  }
  
  /**
    Loads the list of views from a previously stored disk file.
   
    @return True if the passed Disk Store's list of views was read
            successfully.
   
    @param  The disk store from which the saved list of views is to be read.
   */
  public boolean loadDiskViews (WisdomDiskStore diskStore) {
    boolean viewsLoaded = false;
    String viewName;
    String selectorString;
    String comparatorString;
    ItemSelector selector;
    ItemComparator comparator;

    if (diskStore.isDiskViewsFileValidInput()) {
      viewsLoaded = true;
      TabDelimFile viewsTDF = diskStore.getDiskViewsTDF();
      viewsTDF.setLog (files.getLog());
      DataRecord next = null;
      try {
        viewsTDF.openForInput();
      } catch (IOException e) {
        viewsLoaded = false;
      }
      while (! viewsTDF.isAtEnd()) {
        try {
          next = viewsTDF.nextRecordIn();
        } catch (IOException e) {
          viewsLoaded = false;
        }
        if (next != null) {
          viewName = next.getFieldData (VIEW_NAME);
          selectorString = next.getFieldData (VIEW_SELECTOR);
          comparatorString = next.getFieldData (VIEW_COMPARATOR);
          if (viewName.length() > 0
              && selectorString.length() > 0
              && comparatorString.length() > 0) {
            comparator = new ItemComparator ();
            comparator.set (comparatorString);
            comparator.setSelectOption (selectorString);
            selector = comparator.getSelector();
            add (viewName, selector, comparator);
          }
        } // end if next data rec not null
      } // end while more items in data source
    } // end if views file available
    
    return viewsLoaded;
  } // end method loadDiskViews
  
  /*public void setCommon (iWisdomCommon td) {
    this.td = td;
  } */

  /**
   Create a new view with default values.
   */
  public void newView () {
    if (menu != null) {
      JCheckBoxMenuItem menuItem
          = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
      menuItem.setState (false);
    }
    ItemComparator comparator = new ItemComparator();
    ItemSelector selector = comparator.getSelector();
    add (getNextViewName(), selector, comparator);
    currentViewIndex = size() - 1;
    if (menu != null) {
      JCheckBoxMenuItem menuItem
          = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
      menuItem.setState (true);
    }
  }
  
  /**
    Delete the current view. Do not allow the last view left to be deleted
    (need to keep at least one).
   */
  public void removeView () {
    if (size() > 1 
        && currentViewIndex >= 0 
        && currentViewIndex < size()) {
      viewNames.remove (currentViewIndex);
      model.removeElementAt (currentViewIndex);
      if (menu != null) {
        JCheckBoxMenuItem menuItem 
            = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
        menuItem.setState (false);
        menu.remove (menuItem);
      }
      selectors.remove (currentViewIndex);
      comparators.remove (currentViewIndex);
      int index = currentViewIndex;
      if (currentViewIndex >= size()) {
        index--;
      }
      
      // Now adjust files' view indices to still point to right views
      WisdomDiskStore file;
      int viewIndex = 0;
      for (int fileNumber = 0; fileNumber < files.size(); fileNumber++) {
        file = files.get (fileNumber);
        viewIndex = file.getViewIndex();
        if (viewIndex > currentViewIndex) {
          file.setViewIndex (viewIndex - 1);
        } // end if view index for this file needs to be adjusted downwards
        else
        if (viewIndex == currentViewIndex) {
          file.setViewIndex (index);
        }
      } // end of list of all files
      
      currentViewIndex = index;
      
      if (menu != null) {
        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)menu.getItem (index);
        menuItem.setState (true);
      }
    } // end if we have a view to remove
  } // end removeView method
  
  private void addDefaultViews () {
    if (menu != null) {
      JCheckBoxMenuItem menuItem 
          = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
      menuItem.setState (false);
    }

    // Add a View by Author
    ItemComparator comparator = new ItemComparator();
    comparator.set
        ("(All,Author,Ascending,Title,Ascending,Source,Ascending,Source Type,Ascending,Rating,Ascending,high)");
    ItemSelector selector = comparator.getSelector();
    add ("By Author", selector, comparator);
    currentViewIndex = size() - 1;
    if (menu != null) {
      JCheckBoxMenuItem menuItem 
          = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
      menuItem.setState (true);
    }

    // Add a View by Title
    comparator = new ItemComparator();
    comparator.set
        ("(All,Title,Ascending,Source Type,Ascending,Source,Ascending,Author,Ascending,Rating,Ascending,high)");
    selector = comparator.getSelector();
    add ("By Title", selector, comparator);
    currentViewIndex = size() - 1;
    if (menu != null) {
      JCheckBoxMenuItem menuItem
          = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
      menuItem.setState (true);
    }

  }
  
  public String getNextViewName () {
    return "View " + String.valueOf (size() + 1);
  }
  
  public void saveViews () {

    
    if (primaryStore != null) {
      saveViews(primaryStore);
    } // end if primary folder available
    
  } // end method saveViews

  public void saveViews (WisdomDiskStore diskStore) {

    if (diskStore != null) {
      DataRecord nextRec;
      RecordDefinition recDef = new RecordDefinition();
      recDef.addColumn (VIEW_NAME);
      recDef.addColumn (VIEW_SELECTOR);
      recDef.addColumn (VIEW_COMPARATOR);
      boolean ok = true;
      TabDelimFile viewsTDF = diskStore.getDiskViewsTDF();
      viewsTDF.setLog (files.getLog());
      try {
        viewsTDF.openForOutput (recDef);
      } catch (IOException e) {
        ok = false;
      }
      if (ok) {
        for (int viewNumber = 0; viewNumber < size(); viewNumber++) {
          nextRec = new DataRecord();
          nextRec.addField (recDef, getViewName(viewNumber));
          nextRec.addField (recDef, getSelector(viewNumber).toString());
          nextRec.addField (recDef, getComparator(viewNumber).toString());
          try {
            viewsTDF.nextRecordOut (nextRec);
          } catch (IOException e2) {
            ok = false;
          } // end io exception handling
        } // end for each view
      } // end if open ok
      try {
        viewsTDF.close();
      } catch (IOException e) {
        ok = false;
      }
    } // end if primary folder available

  } // end method saveViews

  /**
   Adds a new View to the list, or replaces an existing view, if one already
   exists with the same name.

   @param viewName   The name of the View.
   @param selector   A selector used to filter items.
   @param comparator A comparator used to sequence items.
   */
  private void add (String viewName,
      ItemSelector selector, ItemComparator comparator) {
    boolean found = false;
    int i = 0;
    while (i < viewNames.size() && (! found)) {
      if (viewName.equalsIgnoreCase(getViewName(i))) {
        found = true;
      } else {
        i++;
      }
    }
    if (found) {
      viewNames.set(i, viewName);
      selectors.set(i, selector);
      comparators.set(i, comparator);
    } else {
      viewNames.add (viewName);
      model.addElement(viewName);
      if (menu != null) {
        menu.add (makeMenuItem (size() - 1));
      }
      selectors.add (selector);
      comparators.add (comparator);
    }
  }
  
  public void setViewName (int index, String viewName) {
    if (index >= 0 && index < viewNames.size()) {
      viewNames.set (index, viewName);
      model.removeElementAt (index);
      model.insertElementAt (viewName, index); 
      if (menu != null) {
        JMenuItem menuItem = menu.getItem (index);
        menuItem.setText (viewName);
      } // end if menu has been supplied
    } // end if index in valid range
  } // end method
  
  public String getViewName (int index) {
    if (index >= 0 && index < viewNames.size()) {
      return (String)viewNames.get (index);
    } else {
      return null;
    }
  }
  
  public ItemSelector getSelector () {
    return getSelector (currentViewIndex);
  }
  
  public ItemSelector getSelector (int index) {
    if (index >= 0 && index < selectors.size()) {
      return (ItemSelector)selectors.get (index);
    } else {
      return null;
    }
  }
  
  /**
    Return currently selected view index.
   
    @return currentViewIndex
   */
  public int getViewIndex () {
    return currentViewIndex;
  }
  
  public ItemComparator getComparator () {
    return getComparator (currentViewIndex);
  }
  
  public ItemComparator getComparator (int index) {
    if (index >= 0 && index < comparators.size()) {
      return (ItemComparator)comparators.get (index);
    } else {
      return null;
    }
  }
  
  /**
     Passes in an optional JComboBox for the field.
 
     @param  comboBox A user interface element that allows a user to 
                      select a value from a list.
   */
  public void setComboBox (JComboBox comboBox) {
    this.comboBox = comboBox;
    // comboBox.removeAllItems();
    // for (int i = 0; i < values.size(); i++) {
    //   comboBox.addItem (values.get (i));
    // }
    comboBox.setModel (model);
  }
  
  public void setMenu (JMenu menu) {
    this.menu = menu;
    menu.removeAll();
    for (int viewNumber = 0; viewNumber < size(); viewNumber++) {
      menu.add (makeMenuItem (viewNumber)); 
    }
  }
  
  /**
    Make a new Menu item for a view.
   
    @param  store Definition of recent disk store accessed.
   */
  private JCheckBoxMenuItem makeMenuItem (int index) {
    
    String viewName = getViewName (index);
    JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(viewName);
    menuItem.setActionCommand (String.valueOf (index));
    menuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItemActionPerformed(evt);
      }
    });
    return menuItem;   
  } // end method
  
  /**
    Action listener for view menu items.
   
    @param evt = Action event.
   */
  private void menuItemActionPerformed(java.awt.event.ActionEvent evt) {
    String indexString = evt.getActionCommand();
    try {
      int index = Integer.parseInt (indexString);
      td.modifyView (index, iWisdomCommon.VIEW_TRIGGER_SELECT_VIEW_FROM_MENU);
      // td.setView (index);
    } catch (NumberFormatException e) {
      System.out.println ("ViewList menuItemActionPerformed invalid number ("
          + indexString + ")");
    }
  } // end method
  
  public void setViewIndex (int index, int trigger) {
    
    if (index >= 0 && index < size()) {
      if ((menu != null) 
          && (trigger != iWisdomCommon.VIEW_TRIGGER_SELECT_VIEW_FROM_MENU)) {
        if (currentViewIndex >= 0
            && currentViewIndex < size()) {
          JCheckBoxMenuItem menuItem 
              = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
          menuItem.setState (false);
        }
        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)menu.getItem (index);
        menuItem.setState (true);
      } // end if menu available and not coming from menu change

      if ((comboBox != null) 
          && (trigger != iWisdomCommon.VIEW_TRIGGER_SELECT_VIEW_FROM_COMBO_BOX)) {
         comboBox.setSelectedIndex (index);
      } // end if combo box available
    
      currentViewIndex = index;
    
    } // end if new index in valid range

  } // end method setViewIndex
  
  /**
    Sets the view index as specified by the user.
   */
  
  /*
   public void setViewIndexUserChoice (int index) {
    
    // Unselect previous menu selection and select new one
    if (menu != null) {
      if (currentViewIndex >= 0
          && currentViewIndex < size()) {
        JCheckBoxMenuItem menuItem 
            = (JCheckBoxMenuItem)menu.getItem (currentViewIndex);
        menuItem.setState (false);
      }
      if (index >= 0
          && index < size()) {
        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)menu.getItem (index);
        menuItem.setState (true);
        currentViewIndex = index;
      }
    } // end if menu available
    
    // Change the combo box selection
    if (comboBox != null) {
      if (index >= 0
          && index < size()) {
        comboBox.setSelectedIndex (index);
      } // end if index valid
    } // end if combo box available
    
  } // end method setViewIndexUserChoice
   */
  
  public String toString() {
    StringBuffer buff = new StringBuffer("ViewList:\n");
    for (int viewNumber = 0; viewNumber < size(); viewNumber++) {
      buff.append ("  ");
      buff.append (viewNumber);
      buff.append (": ");
      buff.append (getViewName (viewNumber));
      buff.append ("\n");
      buff.append ("    ");
      buff.append (getComparator(viewNumber).toString());
      buff.append ("\n");
    }
    if (size() < 1) {
      buff.append ("  (empty)");
    }
    buff.append ("\n");
    return buff.toString();
  }
  
  public int size() {
    return viewNames.size();
  }
  
}
