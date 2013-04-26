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

  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.iwisdom.*;
  import java.util.*;
  import javax.swing.*;

/**
 A collection of all possible input/output types supported by iWisdom.
 */
public class WisdomIOFormats {

  public static final String DOT                     = ".";
  public static final String TAB_DELIMITED           = "Tab Delimited Text";
  public static final String TAB_DELIMITED_FILE_EXT  = "tab";
  public static final String XML                     = "XML";
  public static final String XML_FILE_EXT            = "xml";
  public static final String HTML                    = "HTML";
  public static final String HTML_FILE_EXT           = "html";
  public static final String MARKDOWN                = "Markdown";
  public static final String MARKDOWN_FILE_EXT       = "markdown";
  public static final String TEXTBLOCK               = "Text Block";
  public static final String TEXTBLOCK_FILE_EXT      = "txb";
  public static final String TEXTILE1                = "Textile Syntax 1";
  public static final String TEXTILE2                = "Textile Syntax 2";
  public static final String TEXTILE_FILE_EXT        = "textile";
  public static final String OPML                    = "OPML";
  public static final String OPML_FILE_EXT           = "opml";
  public static final String FORTUNE                 = "Fortune";
  public static final String FORTUNE_FILE_EXT        = "ft";
  public static final String TEXT_FILE_EXT           = "txt";
  public static final String UNSTRUCTURED_TEXT       = "Text - Unstructured";
  public static final String UNSTRUCTURED_TEXT_FILE_EXT = "txt";
  public static final String STRUCTURED_TEXT         = "Text - Structured";
  public static final String STRUCTURED_TEXT_FILE_EXT = "txt";
  public static final String SPREADSHEET             = "Spreadsheet";
  public static final String SPREADSHEET_FILE_EXT    = "xls";
  
  private ArrayList ioFormats = new ArrayList();

  private WisdomIOFormat selectedFormat = null;

  private TextLineWriter writer = null;
  private MarkupWriter   markupWriter = null;
  private boolean        markdownLinksInline = false;
  private boolean        authorLinksSeparate = false;

  public WisdomIOFormats () {

    // Create a list of all the quotation formats that iWisdom recognizes

    ioFormats.add(new WisdomIOFormat(FORTUNE, FORTUNE_FILE_EXT,
        false, true, true, true, 5, 
        MarkupWriter.UNDEFINED_FORMAT,
        WisdomIOFormat.LINE_WRITER_ONLY));
    
    ioFormats.add(new WisdomIOFormat(FORTUNE, TEXT_FILE_EXT,
        false, true, true, true, 5, 
        MarkupWriter.UNDEFINED_FORMAT,
        WisdomIOFormat.LINE_WRITER_ONLY));

    ioFormats.add(new WisdomIOFormat(HTML, HTML_FILE_EXT,
        false, true, false, true, 8, 
        MarkupWriter.HTML_FORMAT,
        WisdomIOFormat.MARKUP_WRITER));

    ioFormats.add(new WisdomIOFormat(MARKDOWN, MARKDOWN_FILE_EXT,
        false, true, true, false, 6, 
        MarkupWriter.MARKDOWN_FORMAT,
        WisdomIOFormat.MARKUP_WRITER));

    ioFormats.add(new WisdomIOFormat(OPML, OPML_FILE_EXT,
        false, true, true, false, 0, 
        MarkupWriter.OPML_FORMAT,
        WisdomIOFormat.MARKUP_WRITER));

    ioFormats.add(new WisdomIOFormat(TAB_DELIMITED, TAB_DELIMITED_FILE_EXT,
        true, true, false, true, 0, 
        MarkupWriter.UNDEFINED_FORMAT,
        WisdomIOFormat.TAB_DELIMITED_WRITER));

    ioFormats.add(new WisdomIOFormat(TEXTBLOCK, TEXTBLOCK_FILE_EXT,
        true, true, true, false, 7, 
        MarkupWriter.UNDEFINED_FORMAT,
        WisdomIOFormat.LINE_WRITER_ONLY));

    ioFormats.add(new WisdomIOFormat(TEXTILE1, TEXTILE_FILE_EXT,
        false, true, false, false, 0, 
        MarkupWriter.TEXTILE_SYNTAX_1_FORMAT,
        WisdomIOFormat.MARKUP_WRITER));

    ioFormats.add(new WisdomIOFormat(TEXTILE2, TEXTILE_FILE_EXT,
        false, true, false, false, 0, 
        MarkupWriter.TEXTILE_SYNTAX_2_FORMAT,
        WisdomIOFormat.MARKUP_WRITER));

    ioFormats.add(new WisdomIOFormat(XML, XML_FILE_EXT,
        true, true, true, true, 9,
        MarkupWriter.XML_FORMAT,
        WisdomIOFormat.MARKUP_WRITER));
    
    ioFormats.add(new WisdomIOFormat(UNSTRUCTURED_TEXT, UNSTRUCTURED_TEXT_FILE_EXT,
        false, false, false, true, 6,
        MarkupWriter.UNDEFINED_FORMAT,
        WisdomIOFormat.LINE_WRITER_ONLY));
    
    ioFormats.add(new WisdomIOFormat(STRUCTURED_TEXT, STRUCTURED_TEXT_FILE_EXT,
        true, true, true, true, 8,
        MarkupWriter.STRUCTURED_TEXT_FORMAT,
        WisdomIOFormat.MARKUP_WRITER));
    
    ioFormats.add(new WisdomIOFormat(SPREADSHEET, SPREADSHEET_FILE_EXT,
        false, false, false, true, 0, 
        MarkupWriter.UNDEFINED_FORMAT,
        WisdomIOFormat.TAB_DELIMITED_WRITER));

    selectedFormat = get(ioFormats.size() - 1);
  }

  /**
   Retrieve the format with a matching type. 
  
   @param type The desired type label.
  
   @return The requested I/O format, if a match could be found, otherwise null.
   */
  public WisdomIOFormat get(String type) {
    int i = 0;
    WisdomIOFormat format = null;
    while (i < ioFormats.size()) {
      format = get(i);
      if (format.getType().equalsIgnoreCase(type)) {
        return format;
      }
      i++;
    } // end while more formats to check
    return null;
  }

  /**
   Retrieve the I/O format at the specified location in the list. 
  
   @param i The list position to be accessed. 
  
   @return The desired I/O format. 
   */
  public WisdomIOFormat get(int i) {
    return (WisdomIOFormat)ioFormats.get(i);
  }

  /**
   Populate the provided Combo Box based on the provided parameters, selecting
   the appropriate I/O formats from the list. 
  
   @param comboBox           The combo box to be populated.
   @param selectForAllFields +1 means select only those that provide all fields,
                             -1 means select only those that don't provide all fields,
                              0 means indifference about this attribute. 
   @param selectForExport    +1 means select only those suitable for export,
                             -1 means select only those not suitable for export,
                              0 means indifference concerning this attribute. 
   @param selectForTransfer  +1 means select only those suitable for transfer,
                             -1 means select only those not suitable for transfer,
                              0 means indifference concerning this attribute.
   @param selectForImport    +1 means select only those suitable for import,
                             -1 means select only those not suitable for import,
                              0 means indifference concerning this attribute.
   */
  public void populateComboBox (
      JComboBox comboBox,
      int selectForAllFields,
      int selectForExport,
      int selectForTransfer,
      int selectForImport) {

    comboBox.removeAllItems();
    int highestPriority = 0;

    for (int i = 0; i < ioFormats.size(); i++) {
      boolean selected = true;
      WisdomIOFormat format = (WisdomIOFormat)ioFormats.get(i);

      if (selectForAllFields > 0 && (! format.getAllFields())) {
        selected = false;
      }
      if (selectForAllFields < 0 && format.getAllFields()) {
        selected = false;
      }

      if (selectForExport > 0 && (! format.isValidForExport())) {
        selected = false;
      }
      if (selectForExport < 0 && format.isValidForExport()) {
        selected = false;
      }

      if (selectForTransfer > 0 && (! format.isValidForTransfer())) {
        selected = false;
      }
      if (selectForTransfer < 0 && format.isValidForTransfer()) {
        selected = false;
      }

      if (selectForImport > 0 && (! format.isValidForImport())) {
        selected = false;
      }
      if (selectForImport < 0 && (format.isValidForImport())) {
        selected = false;
      }

      if (selected) {
        comboBox.addItem(format);
        if (format.getPriority() > highestPriority) {
          highestPriority = format.getPriority();
          comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
          selectedFormat = format;
        }
      }
    } // end for each available format
  } // end method populateComboBox

  /**
   Set the passed combo box to select the I/O format matching the passed type. 
  
   @param comboBox The combo box whose selected item is to be updated. 
  
   @param type The type to be selected. 
   */
  public void setSelectedComboBox (JComboBox comboBox, String type) {
    boolean found = false;
    int i = 0;
    while (! found && i < comboBox.getItemCount()) {
      WisdomIOFormat ioFormat = (WisdomIOFormat)comboBox.getItemAt(i);
      if (ioFormat.getType().equalsIgnoreCase(type)) {
        found = true;
        comboBox.setSelectedIndex(i);
        selectedFormat = ioFormat;
      } else {
        i++;
      }
    } // end while looking for a match
  } // end method setSelectedComboBox

  public void select(WisdomIOFormat selectedFormat) {
    this.selectedFormat = selectedFormat;
  }

  public String getSelectedType() {
    return getSelectedFormat().getType();
  }

  public WisdomIOFormat getSelectedFormat() {
    return selectedFormat;
  }

  public int getMarkupFormat(String type) {
    for (int i = 0; i < ioFormats.size(); i++) {
      if (get(i).getType().equalsIgnoreCase(type)) {
        return get(i).getMarkupFormat();
      } // end if a match
    } // end for every format in list
    return MarkupWriter.UNDEFINED_FORMAT;
  } // end getMarkupFormat method

  public String addFileExtension(String fileName) {
    return fileName + "." + selectedFormat.getFileExt();
  }

  public void setLineWriter (TextLineWriter writer) {
    this.writer = writer;
  }

  /**
   Set flag to indicate whether markdown links should be inline.

   @param markdownLinksInline If output is markdown, should links be inline?
   */
  public void setMarkdownLinksInline (boolean markdownLinksInline) {
    this.markdownLinksInline = markdownLinksInline;
  }

  /**
   Set flag to separate author links from author names

   @param authorLinksSeparate Should author links be separated from names?
   */
  public void setAuthorLinksSeparate (boolean authorLinksSeparate) {
    this.authorLinksSeparate = authorLinksSeparate;
  }

}
