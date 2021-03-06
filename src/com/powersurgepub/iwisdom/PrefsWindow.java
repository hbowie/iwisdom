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

  import com.powersurgepub.psfiles.*;
  import com.powersurgepub.psdatalib.pstags.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.xos2.*;
  import javax.swing.*;

/**
 *
 */
public class PrefsWindow 
  extends javax.swing.JFrame
    implements WindowToManage {
  
  private iWisdomCommon      td;
  
  private XOS               xos = XOS.getShared();
  
  private ProgramVersion    programVersion = ProgramVersion.getShared();
  
  private boolean           setupComplete = false;
  
  private CommonPrefs       commonPrefs;
  private GeneralPrefs      generalPrefs;
  private DisplayPrefs      displayPrefs;
  private ViewPrefs         viewPrefs;
  private TransferPrefs     transferPrefs;
  private FilePrefs         backupPrefs;
  private TagsPrefs         tagsPrefs;
  
  /** Creates new form PrefsWindow */
  public PrefsWindow(iWisdomCommon td) {
    this.td = td;
    initComponents();
    
    this.setTitle (Home.getShared().getProgramName() + " Preferences");
    this.setBounds (100, 100, 600, 540);

    commonPrefs = CommonPrefs.getShared();
    prefsTabs.addTab("General", commonPrefs);
    
    generalPrefs = new GeneralPrefs (td, this);
    prefsTabs.addTab("Extra", generalPrefs);
    
    displayPrefs = new DisplayPrefs (td, this);
    prefsTabs.addTab ("Display", displayPrefs);
    
    viewPrefs = new ViewPrefs(td, this);
    prefsTabs.addTab ("Views", viewPrefs);

    transferPrefs = new TransferPrefs(td, this);
    prefsTabs.addTab ("Transfer", transferPrefs);
    
    backupPrefs = FilePrefs.getShared(td);
    prefsTabs.addTab ("Backups", backupPrefs);
    
    tagsPrefs = new TagsPrefs();
    prefsTabs.addTab ("Tags Export", tagsPrefs);
    
    setupComplete = true;
  }
  
  public GeneralPrefs getGeneralPrefs () {
    return generalPrefs;
  }
  
  public ViewPrefs getViewPrefs () {
    return viewPrefs;
  }

  public TransferPrefs getTransferPrefs() {
    return transferPrefs;
  }
  
  public TagsPrefs getTagsPrefs() {
    return tagsPrefs;
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (WisdomDiskStore store) {
    // No file information used on the Prefs Tab
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  public void displayItem() {
  }  
  
  public boolean modIfChanged() {
    return false;
  }  
  
  public void switchTabs () {
    
  }
  
  private void warnRelaunch() {
    JOptionPane.showMessageDialog (
        td.tabs,
        "You may need to Quit and relaunch "
            + XOS.getShared().getProgramName()
            + " for your preferences to take effect.",
        "Relaunch Warning",
        JOptionPane.WARNING_MESSAGE);
  }

  public void savePrefs() {
    commonPrefs.savePrefs();
    tagsPrefs.savePrefs();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    prefsTabs = new javax.swing.JTabbedPane();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().add(prefsTabs, java.awt.BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  WindowMenuManager.getShared().hideAndRemove(this);
}//GEN-LAST:event_formComponentHidden
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane prefsTabs;
  // End of variables declaration//GEN-END:variables
  
}
