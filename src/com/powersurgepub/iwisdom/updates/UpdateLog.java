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

package com.powersurgepub.iwisdom.updates;

  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.psdata.*;
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
