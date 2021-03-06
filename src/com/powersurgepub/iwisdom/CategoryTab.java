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

  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import javax.swing.*;
  import javax.swing.event.*;
  import javax.swing.tree.*;

/**
   A panel to display a list of items in a tree form, organized by
   Category. 
 */
public class CategoryTab 
    extends javax.swing.JPanel 
        implements WisdomTab {
          
  private iWisdomCommon   td;
  
  private ItemTreeCellRenderer renderer;
  
  /** 
    Creates new form CategoryTab 
   */
  public CategoryTab (iWisdomCommon td) {
    this.td = td;
    initComponents();
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (WisdomDiskStore store) {
    // No file information used on the Category (Tree) Tab
  }
  
  public void initItems() {

    catTree.setModel (td.tree.getModel());
    catTree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
    renderer = new ItemTreeCellRenderer ();
    catTree.setCellRenderer (renderer);
    catTree.doLayout();
    /*
    td.sorted.setColumnWidths (listTable);
    ItemPriorityRenderer ipr = new ItemPriorityRenderer();
    listTable.setDefaultRenderer (ItemPriority.class, ipr);
    listTable.getColumn(td.sorted.getColumnName(4)).setCellRenderer(ipr);
    td.sorted.fireTableDataChanged();
    
     */
  }
  
  public void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
  }  
  
  public boolean modIfChanged() {
    return false;
  }  
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    catScrollPane = new javax.swing.JScrollPane();
    catTree = new javax.swing.JTree();

    setLayout(new java.awt.BorderLayout());

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });

    TreeSelectionModel treeSM = catTree.getSelectionModel();
    treeSM.setSelectionMode (treeSM.SINGLE_TREE_SELECTION);
    treeSM.addTreeSelectionListener (new TreeSelectionListener() {
      public void valueChanged (TreeSelectionEvent e) {
        itemSelected();
      }
    });
    catScrollPane.setViewportView(catTree);

    add(catScrollPane, java.awt.BorderLayout.CENTER);

  }
  // </editor-fold>//GEN-END:initComponents

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    catScrollPane.requestFocus();
  }//GEN-LAST:event_formComponentShown

  private void itemSelected() {
    td.modIfChanged();
    CategoryNode node = (CategoryNode)
      catTree.getLastSelectedPathComponent();
    if (node == null) {
      // nothing selected
    } 
    else
    if (node.getNodeType() == node.ITEM) {
      td.navigator = td.tree;
      td.selectItem ((WisdomItem)node.getUserObject());
      td.displayItem();
      if (td.selItemTab) {
        td.activateDisplayTab();
      }
    }
    else {
      // Do nothing until an item is selected
    }
  }

  public CategoryNode getLastSelectedPathComponent() {
    return (CategoryNode)catTree.getLastSelectedPathComponent();
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane catScrollPane;
  private javax.swing.JTree catTree;
  // End of variables declaration//GEN-END:variables
  
}
