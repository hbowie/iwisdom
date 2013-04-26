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

/**
 *
 */
public class WisdomIOFormat {

  private String    type              = "";
  private String    fileExt           = "";
  private boolean   allFields         = false;
  private boolean   validForExport    = false;
  private boolean   validForTransfer  = false;
  private boolean   validForImport    = false;
  private int       priority          = 0;
  private int       markupFormat      = 0;
  private int       writerNeeded      = 0;
  public static final int LINE_WRITER_ONLY = 0;
  public static final int MARKUP_WRITER = 1;
  public static final int TAB_DELIMITED_WRITER = 2;


  /**
   Construct an iWisdom I/O Format.
  
   @param type
   @param fileExt
   @param allFields
   @param validForExport
   @param validForTransfer
   @param validForImport
   @param priority Higher numbers represent higher priorities, in terms of
                   initial default selection for a Combo Box. 
   @param markupFormat
   @param writerNeeded 
   */
  public WisdomIOFormat (
      String type,
      String fileExt,
      boolean allFields,
      boolean validForExport,
      boolean validForTransfer,
      boolean validForImport,
      int priority,
      int markupFormat,
      int writerNeeded) {
    
    this.type = type;
    this.fileExt = fileExt;
    this.allFields = allFields;
    this.validForExport = validForExport;
    this.validForTransfer = validForTransfer;
    this.validForImport = validForImport;
    this.priority = priority;
    this.markupFormat = markupFormat;
    this.writerNeeded = writerNeeded;
  }

  public String getType() {
    return type;
  }

  public String getFileExt() {
    return fileExt;
  }

  public boolean getAllFields() {
    return allFields;
  }

  public boolean isValidForExport() {
    return validForExport;
  }

  public boolean isValidForTransfer() {
    return validForTransfer;
  }

  public boolean isValidForImport() {
    return validForImport;
  }
  
  public int getPriority() {
    return priority;
  }

  public int getMarkupFormat() {
    return markupFormat;
  }

  public int getWriterNeeded() {
    return writerNeeded;
  }

  public String toString() {
    return type;
  }
}
