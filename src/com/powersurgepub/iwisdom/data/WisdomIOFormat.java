package com.powersurgepub.iwisdom.data;

/**
 *
 * @author hbowie
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
