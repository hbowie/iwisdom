/*
 * UpdateLog.java
 *
 * Created on November 5, 2005, 9:41 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.powersurgepub.iwisdom.updates;

import com.powersurgepub.psdatalib.tabdelim.TabDelimFile;
  import com.powersurgepub.psdatalib.psdata.RecordDefinition;
  import com.powersurgepub.iwisdom.disk.*;
  import java.io.*;
  import java.text.*;
  import java.util.*;

/**
 *
 * @author hbowie
 */
public class UpdateLog {
  
  private   boolean               ok;
  private   WisdomDiskStore       folder;
  private   RecordDefinition      recDef;
  private   TabDelimFile          file;
  
  /** Creates a new instance of UpdateLog */
  public UpdateLog(WisdomDiskStore folder) {
    this.folder = folder;
    recDef = Update.getRecDef();
    ok = true;
  }
  
  public boolean startNew () {
    Date now = new Date();
    DateFormat dateFormatter = new SimpleDateFormat ("yyyy-MM-dd HH-mm-ss-SSS");
    if (folder.isAFolder()) {
      file = new TabDelimFile 
          (folder.getUpdateLogFolder(), 
            "log" + dateFormatter.format (now));
      try {
        file.openForOutput (recDef);
      } catch (IOException e) {
        ok = false;
      }
    } else {
      ok = false;
    }
    return ok;
  }
  
}
