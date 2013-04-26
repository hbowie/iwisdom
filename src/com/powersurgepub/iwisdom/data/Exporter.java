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
  import com.powersurgepub.psdatalib.tabdelim.*;
  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;

/**
 An engine for exporting iWisdom lists in a variety of formats.
 */
public class Exporter {

  public static final int     ALL       = 0;
  public static final int     CATEGORY  = 1;
  public static final int     CURRENT   = 2;

  public static final String  NO_STYLE           = "";
  public static final boolean ONE_PARAGRAPH_ONLY = false;

  /**
   Export some wisdom data to a disk file.

   @param diskStore           The WisdomDiskStore from which the item is to
                              be exported.
   @param collectionWindow    The window representing the collection metadata.
   @param sorted              The sorted list of items to be exported.
   @param item                The currently selected item.
   @param exportFile          The file to receive the formatted output.
   @param ioFormat            The input/output format to be used.
   @param selectionScope      An indicator of whether the all of the list is
                              to be exported, or only one specific category,
                              or only the currently selected item.
   @param selectedCategory    The selected category, if the scope is constrained
                              to a single category.
   @param splitCategories     Should a separate record be written out for each
                              category?
   @return                    A string containing the name of the output file,
                              or an error message.
   */
  public static String export(
      WisdomDiskStore diskStore,
      CollectionWindow collectionWindow,
      SortedItems sorted,
      WisdomItem item,
      File exportFile,
      WisdomIOFormat ioFormat,
      int selectionScope,
      String selectedCategory,
      boolean splitCategories) {
    TextLineWriter writerGen = new FileMaker (exportFile);
    return export (
        diskStore,
        collectionWindow,
        sorted,
        item,
        writerGen,
        ioFormat,
        selectionScope,
        selectedCategory,
        splitCategories);
  }

  /**
   Export some wisdom data.

   @param diskStore           The WisdomDiskStore from which the item is to
                              be exported.
   @param collectionWindow    The window representing the collection metadata.
   @param sorted              The sorted list of items to be exported.
   @param oneItem             The currently selected item.
   @param writer              The line writer to receive the formatted output.
   @param ioFormat            The input/output format to be used.
   @param selectionScope      An indicator of whether the all of the list is
                              to be exported, or only one specific category,
                              or only the currently selected item.
   @param selectedCategory    The selected category, if the scope is constrained
                              to a single category.
   @param splitCategories     Should a separate record be written out for each
                              category?
   @return                    A string containing the name of the output file,
                              or an error message.
   */
  public static String export(
      WisdomDiskStore diskStore,
      CollectionWindow collectionWindow,
      SortedItems sorted,
      WisdomItem item,
      TextLineWriter writer,
      WisdomIOFormat ioFormat,
      int selectionScope,
      String selectedCategory,
      boolean splitCategories) {

    WisdomXMLIO xmlio = diskStore.getXMLIO();
    String exportType = ioFormat.getType();
    boolean ok = true;

    //
    // Prepare for export
    //

    TabDelimFile tdf = null;
    MarkupWriter markupWriterGen = null;

    // Export in Tab-Delimited format
    if (exportType.equals(WisdomIOFormats.TAB_DELIMITED)) {
      tdf = new TabDelimFile (writer);
      try {
        tdf.openForOutput (WisdomItem.getRecDef());
      } catch (java.io.IOException e) {
        Trouble.getShared().report
            ("I/O Problems encountered during export", "I/O Error");
        ok = false;
      }
    } // end if tab-delimited text file was selected output format
    else
    if (exportType.equals (WisdomIOFormats.TEXTBLOCK)) {
      writer.openForOutput();
    }
    else {
      markupWriterGen = getMarkupWriter
          (writer, ioFormat.getMarkupFormat());
      ok = markupWriterGen.openForOutput();
      if (exportType.equals (WisdomIOFormats.HTML) || exportType.equals (WisdomIOFormats.OPML)) {
        markupWriterGen.startHead();
        markupWriterGen.startTitle();
        markupWriterGen.cleanAndWrite (collectionWindow.getCollectionTitle());
        markupWriterGen.endTitle();
        markupWriterGen.endHead();
        markupWriterGen.startBody();
      }

    }

    //
    // Perform the Export
    //
    if (ok) {
    // Export in XML Format
      if (exportType.equals(WisdomIOFormats.XML)) {

        xmlio = diskStore.getXMLIO();

        if (selectionScope == ALL) {
          ok = xmlio.save (markupWriterGen, collectionWindow, sorted);
        } // end if entire collection is to be output
        else
        if (selectionScope == CATEGORY
            && selectedCategory.length() > 0) {
          ok = xmlio.save (markupWriterGen, collectionWindow,
              sorted, selectedCategory);
        }
        else
        if (selectionScope == CURRENT) {
          ok = xmlio.save (markupWriterGen, item);
        } else {
          Trouble.getShared().report ("No valid scope specified", "Scope Error");
        }
      } else {
        if (selectionScope == ALL) {
          for (int itemIndex = 0; itemIndex < sorted.size(); itemIndex++) {
            WisdomItem nextItem = sorted.get (itemIndex);
            if (nextItem != null && (! nextItem.isDeleted())) {
              exportOneItem (
                  diskStore,
                  nextItem,
                  writer,
                  tdf,
                  markupWriterGen,
                  exportType,
                  false,
                  splitCategories);
            } // end if input record not null
          } // end while more wisdom items in collection
        } // end if entire collection is to be output
        else
        if (selectionScope == CATEGORY) {
          WisdomItem nextItem;
          for (int itemIndex = 0; itemIndex < sorted.size(); itemIndex++) {
            nextItem = sorted.get (itemIndex);
            if (nextItem != null && (! nextItem.isDeleted())) {
              Category itemCategory = nextItem.getCategory();
              itemCategory.startCategoryIteration();
              while (itemCategory.hasNextCategory()) {
                String nextCategory = itemCategory.nextCategory();
                if (nextCategory.equalsIgnoreCase(selectedCategory)) {
                  exportOneItem (
                      diskStore,
                      nextItem,
                      writer,
                      tdf,
                      markupWriterGen,
                      exportType,
                      false,
                      splitCategories);
                } // end if category match
              } // end while more categories for this item
            } // end if not deleted
          } // end for loop
        } // end if selected category is to be output
        else
        if (selectionScope == CURRENT) {
          exportOneItem (
              diskStore,
              item,
              writer,
              tdf,
              markupWriterGen,
              exportType,
              false,
              splitCategories);
        }
      } // end if not xml export
    } // end if ok


    // Finish up export

    // Export in Tab-Delimited format
    if (exportType.equals(WisdomIOFormats.TAB_DELIMITED)) {
      try {
        tdf.close();
      } catch (java.io.IOException e) {
        ok = false;
        Trouble.getShared().report ("Trouble closing tab-delimited export file",
            "Export Error");
      }
    } // end if tab-delimited text file was selected output format
    else
    if (exportType.equals (WisdomIOFormats.TEXTBLOCK)) {
      writer.close();
    }
    else
    if (exportType.equals (WisdomIOFormats.XML)) {
      xmlio.close();
    } else {
      if (exportType.equals (WisdomIOFormats.HTML) || exportType.equals (WisdomIOFormats.OPML)) {
        markupWriterGen.endBody();
      }
      ok = markupWriterGen.close();
    }

    if (ok) {
      return writer.getDestination();
    } else {
      return "Export not completed successfully";
    }
  } // end method export

  /**
   Format a single Wisdom item for some kind of output.

   @param oneItem             The wisdom item to be formatted.
   */
  public static boolean itemFormat(
      WisdomDiskStore diskStore,
      WisdomItem oneItem,
      TextLineWriter writer,
      TabDelimFile tdf,
      WisdomIOFormat ioFormat,
      boolean markdownLinksInline,
      boolean authorLinksSeparate,
      boolean splitCategories)  {

    boolean ok = true;
    int markupFormat = ioFormat.getMarkupFormat();
    MarkupWriter markupWriter = getMarkupWriter(writer, markupFormat);
    if (ioFormat.getWriterNeeded() == WisdomIOFormat.LINE_WRITER_ONLY) {
      ok = writer.openForOutput();
    }
    else
    if (ioFormat.getWriterNeeded() == WisdomIOFormat.MARKUP_WRITER) {
      ok = markupWriter.openForOutput();
      markupWriter.setMarkdownLinksInline(markdownLinksInline);
    }

    if (ok) {
      ok = exportOneItem (
          diskStore,
          oneItem,
          writer,
          tdf,
          markupWriter,
          ioFormat.getType(),
          authorLinksSeparate,
          splitCategories);
    }

    if (ok) {
      if (ioFormat.getWriterNeeded() == WisdomIOFormat.LINE_WRITER_ONLY) {
        writer.close();
      }
      else
      if (ioFormat.getWriterNeeded() == WisdomIOFormat.MARKUP_WRITER) {
        markupWriter.close();
      }
    }
    return ok;
  }
  
  private static MarkupWriter getMarkupWriter(
      TextLineWriter lineWriter, 
      int markupFormat) {
    MarkupWriter markupWriter = new MarkupWriter(lineWriter, markupFormat);
    WisdomItem.tailorMarkupWriter(markupWriter);
    return markupWriter;
  }

  /**
   Export one WisdomItem to an output writer.

   @param diskStore           The WisdomDiskStore from which the item is to
                              be exported.
   @param oneItem             The item to be exported.
   @param writer              The line writer to receive the formatted output.
   @param tdf                 The Tab Delimited File to which the item is
                              to be exported, if tab-delimited output is
                              requested.
   @param markupWriter        A markupwriter, if markup will be used.
   @param exportType          The type of export requested.
   @param authorLinksSeparate Should author links be separated from their names?
   @param splitCategories     Should a separate record be written out for each
                              category?
   @return                    True if all went well.
   */
  public static boolean exportOneItem (
      WisdomDiskStore diskStore,
      WisdomItem oneItem,
      TextLineWriter writer,
      TabDelimFile tdf,
      MarkupWriter markupWriter,
      String exportType,
      boolean authorLinksSeparate,
      boolean splitCategories) {
    boolean ok = true;
    WisdomXMLIO xmlio = diskStore.getXMLIO();
    if (exportType.equals (WisdomIOFormats.TAB_DELIMITED)) {
      if (splitCategories) {
        for (int i = 0; i < oneItem.getCategories(); i++) {
          try {
            tdf.nextRecordOut (oneItem.getDataRec(WisdomItem.getRecDef(), i));
          } catch (java.io.IOException e) {
            ok = false;
            Trouble.getShared().report ("Trouble writing tab-delimited export file",
                "Export Error");
          }
        }
      } else {
        try {
          tdf.nextRecordOut (oneItem.getDataRec(WisdomItem.getRecDef()));
        } catch (java.io.IOException e) {
          ok = false;
          Trouble.getShared().report ("Trouble writing tab-delimited export file",
              "Export Error");
        }
      }
    }
    else
    if (exportType.equals (WisdomIOFormats.TEXTBLOCK)) {
      oneItem.writeTextBlock (writer, false);
      // System.out.println ("ExportWindow exportOneItem TextBlock "
      //     + oneItem.getTitle());
    }
    else
    if (exportType.equals (WisdomIOFormats.XML)) {
      ok = xmlio.saveNextItemToFile (markupWriter, oneItem);
    }
    else
    if (exportType.equals (WisdomIOFormats.OPML)) {
      ok = exportWisdomItemToOutline (oneItem, markupWriter);
    }
    else
    if (exportType.equals (WisdomIOFormats.FORTUNE)) {
      oneItem.getFortuneFormat(writer);
    }
    else
    if (exportType.equals(WisdomIOFormats.STRUCTURED_TEXT)) {
      oneItem.writeMarkup(markupWriter, null, false, false);
    } else {
      ok = exportWisdomItemToMarkup
          (oneItem, markupWriter, exportType, authorLinksSeparate);
    }
    return ok;
  }

 /**
   Export one wisdom item to a markup format (HTML, Textile, Markdown, etc.)

   @param oneItem The item to be written.
   @return True if the item was written successfully.
   */
  public static boolean exportWisdomItemToMarkup (
      WisdomItem oneItem,
      MarkupWriter markupWriter,
      String exportType,
      boolean authorLinksSeparate) {

    boolean ok = true;
    if (markupWriter == null) {
      System.out.println
          ("ExportWindow exportWisdomItemToMarkup markupWriter is null");
    }

    // Write Title as Heading
    markupWriter.writeHeading (2, oneItem.getTitle(), NO_STYLE);

    // Write Category Info
    startParagraphWithDescription (markupWriter, "Category");
    markupWriter.emphasis (oneItem.getCategory().toString());
    markupWriter.endParagraph();

    // Write Body
    markupWriter.startBlockQuote("", true);
    oneItem.getBodyAsMarkup().writeMarkup (markupWriter);
    markupWriter.endBlockQuote();

    // Write Author(s), if any
    String authorCompleteName = oneItem.getAuthorCompleteName();
    if (authorCompleteName.length() > 0) {
      Author author = oneItem.getAuthor();
      String authorLink;
      int numberOfAuthors = author.getNumberOfAuthors();
      if (author.isCompound()) {
        startParagraphWithDescription (markupWriter, "--");
        for (int i = 0; i < numberOfAuthors; i++) {
          Author nextAuthor = author.getAuthor (i);
          if (authorLinksSeparate) {
            authorLink = "";
          } else {
            authorLink = nextAuthor.getALink();
          }
          markupWriter.writeItemInFlatList (
              nextAuthor.getCompleteName(),
              authorLink,
              i,
              numberOfAuthors);
        }
      } else {
        startParagraphWithDescription (markupWriter, "--");
        if (authorLinksSeparate) {
            authorLink = "";
          } else {
            authorLink = author.getALink();
          }
        markupWriter.writeItemInFlatList
            (authorCompleteName, authorLink, 0, 1);
      }
      String authorInfo = oneItem.getAuthorInfo();
      if (authorInfo.length() > 0) {
        markupWriter.writeItemInFlatList (", " + authorInfo, "",  0, 1);
      }
      markupWriter.endParagraph();

      if (authorLinksSeparate) {
        startParagraphWithDescription
            (markupWriter, "Additional author information");
        if (author.isCompound()) {
          for (int i = 0; i < numberOfAuthors; i++) {
            Author nextAuthor = author.getAuthor (i);
            markupWriter.writeItemInFlatList (
                nextAuthor.getCompleteName(),
                nextAuthor.getALink(),
                i,
                numberOfAuthors);
          }
        } else {
          markupWriter.writeItemInFlatList
              (authorCompleteName, author.getALink(), 0, 1);
        } // end if only a single author
        markupWriter.endParagraph();
      } // end if author links separate
    } // end if we have any author names at all



    // Display Source, if any
    if (oneItem.getSourceAsString().length() > 0
        && (! oneItem.getSourceAsString().equalsIgnoreCase("unknown"))) {
      markupWriter.startParagraph (NO_STYLE, ONE_PARAGRAPH_ONLY);
      markupWriter.write ("From ");
      if (oneItem.getSourceType() != WisdomSource.UNKNOWN_TYPE) {
        markupWriter.write ("the ");
        markupWriter.write (oneItem.getSourceTypeLabel().toLowerCase());
        markupWriter.write (" ");
      }
      markupWriter.startCitation ("");
      markupWriter.writeLink
          (oneItem.getSourceAsString(), oneItem.getASourceLink());
      markupWriter.endCitation();
      if (oneItem.getMinorTitle().length() > 0) {
        markupWriter.write(", ");
        StringBuffer minorTitle = new StringBuffer(oneItem.getMinorTitle());
        if (minorTitle.charAt(0) != '"') {
          minorTitle.insert(0, '"');
          minorTitle.append('"');
        }
        markupWriter.cleanAndWrite(minorTitle.toString());
      }
      if (oneItem.getRights().length() == 0) {
        if (oneItem.getYear().length() > 0) {
          markupWriter.write (" (" + oneItem.getYear() + ")");
        }
      }
      markupWriter.endParagraph();
    }

    // Display Rights / Publication Year
    if (oneItem.getRights().length() > 0) {
      StringBuffer yearRights = new StringBuffer();
      String rights = oneItem.getRights();
      if (rights.length() > 0
          && rights.startsWith ("Copyright")) {
        yearRights.append ("Copyright &copy;");
      } else {
        yearRights.append (rights);
      }
      if (! oneItem.getYear().equals("")) {
        if (yearRights.length() > 0) {
          yearRights.append (" ");
        }
        yearRights.append (oneItem.getYear());
      }
      if (oneItem.getRightsOwner().length() > 0) {
        if (yearRights.length() > 0) {
          yearRights.append (" by ");
        }
        yearRights.append (oneItem.getRightsOwner());
      }
      if (yearRights.length() > 0) {
        markupWriter.startParagraph (NO_STYLE, ONE_PARAGRAPH_ONLY);
        markupWriter.cleanAndWrite (yearRights.toString());
        markupWriter.endParagraph();
      }
    }

    markupWriter.writeLinks();

    return ok;
  }

  private static boolean exportWisdomItemToOutline (
      WisdomItem oneItem,
      MarkupWriter markupWriter) {

    boolean ok = true;

    // Write Title as Heading
    markupWriter.startOutline (WisdomXMLIO.ITEM, oneItem.getTitle(), "", false);

    Category category = oneItem.getCategory();
    category.startCategoryIteration();
    while (category.hasNextCategory()) {
      String nextCategory = category.nextCategory();
      markupWriter.startOutline (WisdomXMLIO.CATEGORY, nextCategory, "", true);
    }

    // markupWriter.startOutline (WisdomXMLIO.BODY, "", false);
    oneItem.getBodyAsMarkupElement().writeMarkup (markupWriter);
    // markupWriter.endOutline();

    // Write Author(s), if any
    StringBuffer work = new StringBuffer();
    String authorCompleteName = oneItem.getAuthorCompleteName();
    String authorInfo = oneItem.getAuthorInfo();
    if (authorCompleteName.length() > 0) {
      Author author = oneItem.getAuthor();
      int numberOfAuthors = author.getNumberOfAuthors();
      if (author.isCompound()) {
        for (int i = 0; i < numberOfAuthors; i++) {
          Author nextAuthor = author.getAuthor (i);
          work = new StringBuffer ("-- ");
          work.append (nextAuthor.getCompleteName());
          markupWriter.startOutline
            (WisdomXMLIO.AUTHOR, work.toString(), nextAuthor.getALink(), true);
        }
      } else {
        work = new StringBuffer ("-- ");
        work.append (authorCompleteName);
        if (authorInfo.length() > 0) {
          work.append (", " + authorInfo);
        }
        markupWriter.startOutline
            (WisdomXMLIO.AUTHOR, work.toString(), author.getALink(), true);
      }
    }

    // Display Source, if any
    String source = oneItem.getSourceAsString();
    if (source.length() > 0 && (! source.equalsIgnoreCase (WisdomSource.UNKNOWN))) {
      markupWriter.startOutline
          (oneItem.getSourceTypeLabel(), oneItem.getSourceAsString(),
          oneItem.getASourceLink(), true);
    }

    // Display Rights / Publication Year
    work = new StringBuffer();
    String rights = oneItem.getRights();
    if (rights.length() > 0
        && rights.startsWith ("Copyright")) {
      work.append ("Copyright &copy;");
    } else {
      work.append (rights);
    }

    if (! oneItem.getYear().equals("")) {
      if (work.length() > 0) {
        work.append (" ");
      }
      work.append (oneItem.getYear());
    }
    if (oneItem.getRightsOwner().length() > 0) {
      if (work.length() > 0) {
        work.append (" by ");
      }
      work.append (oneItem.getRightsOwner());
    }
    if (work.length() > 0) {
      markupWriter.startOutline
          (WisdomXMLIO.RIGHTS, work.toString(), "", true);
    }
    markupWriter.endOutline();

    return ok;
  }

  private static void writeParagraphWithDescription
      (MarkupWriter markupWriter, String description, String text, String link) {
    if (text.length() == 0 || text.equalsIgnoreCase ("unknown")) {
      // skip it if nothing to write
    } else {
      startParagraphWithDescription (markupWriter, description);
      markupWriter.writeLink (text, link);
      markupWriter.endParagraph();
    }
  }

  private static void startParagraphWithDescription (MarkupWriter markupWriter,
      String description) {
    markupWriter.startParagraph (NO_STYLE, ONE_PARAGRAPH_ONLY);
    if (description.length() > 0) {
      markupWriter.cleanAndWrite (description);
      if (description.equals ("--")) {
        // no colon
      } else {
        markupWriter.write (":");
      }
      markupWriter.write (" ");
    }
  }

}
