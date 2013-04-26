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

  import com.powersurgepub.psdatalib.markup.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.pstextio.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.iwisdom.*; 
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.net.*;
  import java.util.*;
  import javax.xml.transform.*;
  import org.xml.sax.*;
  import org.xml.sax.helpers.*;

/**
   A Class to read, write and transform xml data. 
 */
public class WisdomXMLIO 
    extends DefaultHandler 
        implements  MarkupTagWriter {
  
  public static final String      WISDOM_FILE_EXTENSION = "xml";
  public static final String      HTML_FILE_EXTENSION   = "html";
  public static final String      HEADER_FILE_NAME      = "header";
  public static final String      XML_VERSION = "1.0";
  public static final String      XML_ENCODING = "UTF-8";
  public static final String      XML_NAME_SPACE_PREFIX = "";
  public static final String      XML_NAME_SPACE_URI 
      = "\"http://www.powersurgepub.com/xml/wisdom\"";
  
  public static final String      WISDOM      = "wisdom";
  public static final String      ITEM        = "item";
  public static final String      ITEM_ID     = "item-id";
  public static final String      CATEGORY    = "category";
  public static final String      TITLE       = "title";
  public static final String      PAGES       = "pages";
  public static final String      RATING      = "rating";
  public static final String      MINOR_TITLE = "minor-title";
  public static final String      BODY        = "body";
  public static final String      AUTHOR      = "author";
  public static final String      AUTHOR_INFO = "author-info";
  public static final String      ONE_AUTHOR  = "single-author";
  public static final String      NAME        = "name";
  public static final String      FIRST_NAME  = "first-name";
  public static final String      LAST_NAME   = "last-name";
  public static final String      FILE_NAME   = "file-name";
  public static final String      WIKIPEDIA_LINK = "wikipedia-link";
  public static final String      SOURCE      = "source";
  public static final String      SOURCE_TYPE = "source-type";
  public static final String      TYPE        = "type";
  public static final String      ID          = "identifier";
  public static final String      PUBLISHER   = "publisher";
  public static final String      CITY        = "city";
  public static final String      YEAR        = "year";
  public static final String      RIGHTS      = "rights";
  public static final String      RIGHTS_OWNER = "owner";
  public static final String      LINK        = "link";
  public static final String      PATH        = "path";
  public static final String      DATE_ADDED  = "date-added";
  public static final String      DESCRIPTION = "description";
  public static final String      EDITOR      = "editor";
  public static final String      VERSION     = "version";
  public static final String      ASSUME_QUOTATION = "assume-quotation";
  public static final String      BACKUP_FOLDER = "backup-folder";
  public static final String      STORAGE_FORMAT = "storage-format";
  public static final String      ORGANIZE_WITHIN_FOLDERS 
      = "organize-within-folders";
  public static final String      FILE_NAMING  = "file-naming";
  
  /** Used by QML Quotations Markup Language */
  public static final String      QUOTATIONS  = "quotations";
  public static final String      QUOTATION   = "quotation";
  public static final String      CONTENT     = "content";
  
  /** Used by QEL Quotations Exchange Language */
  public static final String      PARAGRAPH   = "p";
  public static final String      CITE        = "cite";
  public static final String      PRE         = "pre";
  public static final String      BREAK       = "br";
  public static final String      HEADING_LEVEL_1 = "h1";
  public static final String      NOTE        = "note";
  
  /** Used by xmlquotes format */
  public static final String      QUOTE       = "quote";
  public static final String      BY          = "by";
  public static final String      DATE        = "date";
  
  /** Used by RSS format */
  public static final String      CHANNEL     = "channel";
  
  /** Used to create HTML output */
  public static final String      HTML        = "html";
  public static final String      HEAD        = "head";
  public static final String      DIVISION    = "div";
  public static final String      HTML_CLASS  = "class";
  public static final String      HTML_ID     = "id";
  public static final String      HTML_LINK   = "a";
  public static final String      HREF        = "href";
  
  public static final String      ITEM_TITLE_CLASS = "title";
  public static final String      ITEM_BODY_CLASS  = "body";
  public static final String      EMDASH_CLASS     = "emdash";
  public static final String      AUTHOR_CLASS     = "author";
  public static final String      SOURCE_TYPE_CLASS = "sourceType";
  public static final String      SOURCE_TITLE_CLASS = "quoteSourceTitle";
  public static final String      SOURCE_RIGHTS_CLASS = "sourceRights";
  public static final String      XML_LINK_CLASS   = "xmllink";
  
  public static final String      CONTENT_ID       = "content";
  public static final String      QUOTE_ID         = "quote";
  public static final String      SITE_ID          = "siteid";
  public static final String      NAVLINKS_ID      = "navlinks";
  
  public static final String      EMDASH           = "&#8212;";
  public static final String      LEFT_DOUBLE_QUOTE = "&#8220;";
  public static final String      RIGHT_DOUBLE_QUOTE = "&#8221;";
  
  public static final String      ID_FOLDER        = "id";
  
  public static final int         INDENT_PER_LEVEL = 2;
  
  private     String              programVersion = "";
  
  private     CollectionWindow    collectionWindow = null;
  
  private     WisdomDiskStore     store = null;
  
  private     File                wisdomFile = null;
  
  private     File                xmlSourceAsFile;
  private     int                 maxFolderDepth = 99;
  private     DirectoryReader     directoryReader;

  private     XMLReader           parser = null;
  private     boolean             parserOK = false;
  
  // Depth within XML structure, where 0 = no XML, 1 is within first, etc.
  private     int                 elementLevel = 0;
  
  // Array of element names
  private     ArrayList           elementName = new ArrayList();
  
  // Array of character strings being built as characters are received from parser
  private     ArrayList           chars = new ArrayList();
  
  private     boolean             readFromCatalog = false;
  private     String              xmlAuthorFileName = Author.UNKNOWN;
  private     String              xmlSourceFileName = WisdomSource.UNKNOWN;
  private     String              xmlFileName = "";
  
  private     boolean             ok = true;
  private     boolean             outOK = true;
  private     boolean             saveToDisk = true;
  // private     TextLineWriter      textLineWriter;
  // private     TextWriter          textWriter;
  private     MarkupWriter        markupWriter;
  // private     BufferedWriter      outBuffered;
  private     String              xmlNameSpacePrefixWithColon 
      = "";             
  private     int                 level = 0;
  private     StringBuffer        textOut = new StringBuffer();
  private     WisdomItem          wisdom = null;
  private     Author              author = null;
  private     WisdomItems         items = null;
  
  private     boolean             withinQuotation = false;
  // tags within the body should be passed along as is, for the most part
  private     boolean             withinBody = false;
  
  private     boolean             authorFound = false;
  private     boolean             sourceTitleFound = false;
  private     boolean             respectItemID = false;
  
  private     TransformerFactory  factory = TransformerFactory.newInstance();
  
  // Trouble reporter
  private     Trouble             trouble = Trouble.getShared();
  
  /** Log used to record events. */
  private     Logger              log = Logger.getShared();
  
  /** Used to log imports. */
  // private     ImportWindow        importLog;
  
  private     Home                home;
  
  private     boolean             insertTroubleReported = false;
  
  private     char                categorySeparator 
                                    = Category.PREFERRED_CATEGORY_SEPARATOR;

  private     boolean             olderPublicationStyle = false;
  
  /** Creates a new instance of WisdomXMLIO */
  public WisdomXMLIO() {
    home = Home.getShared();
    programVersion = home.getProgramVersion();
  }

  public void setOlderPublicationStyle (boolean oldPubStyle) {
    this.olderPublicationStyle = oldPubStyle;
  }

  public boolean getOlderPublicationStyle () {
    return olderPublicationStyle;
  }
  
  /* --------------------------------------------------------------
   This section of the program has methods for writing wisdom to the
   catalog. 
     -------------------------------------------------------------- */ 
  
  /**
   Save one Wisdom item to a specified output file.
   */
  public boolean save (MarkupWriter markupWriter, WisdomItem wisdom) {
    
    if (ok) {
      ok = saveOneItemToFile (markupWriter, wisdom);
    } 
    return ok;
  }
  
  /**
   Save an entire wisdom collection to a specified output file.
   */
  public boolean save (MarkupWriter markupWriter, 
      CollectionWindow header, SortedItems items) {
    
    this.markupWriter = markupWriter;
    ok = markupWriter.openForOutput();
    
    // Write the XML header
    if (ok) {
      writeiWisdomComment();
    }
    level = 0;
    
    // Begin the wisdom document
    if (ok) {
      beginWisdomElement();
    }
    
    // Now write the header
    if (ok) {
      writeHeader (header);
    }
    
    // Now write the collection
    if (ok) {
      WisdomItem nextItem;
      for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
        nextItem = items.get (itemIndex);
        if (! nextItem.isDeleted()) {
          ok = saveNextItemToFile (markupWriter, nextItem);
        } // end if not deleted
      } // end for loop
    } // end if open ok
    
    // End the wisdom document
    if (ok) {
      writeEndTag (WISDOM);
    }
    
    if (ok) {
      ok = closeOutput();
    }
    
    return ok;
  }
  
  /**
   Save an all wisdom items having the specified category
   to a specified output file.
   */
  public boolean save (
      MarkupWriter markupWriter, 
      CollectionWindow header, 
      SortedItems items,
      String selectedCategory) {
    
    this.markupWriter = markupWriter;
    ok = markupWriter.openForOutput();
    
    // Write the XML header
    if (ok) {
      writeiWisdomComment();
    }
    level = 0;
    
    // Begin the wisdom document
    if (ok) {
      beginWisdomElement();
    }
    
    // Now write the header
    if (ok) {
      writeHeader (header);
    }
    
    // Now write the collection
    if (ok) {
      WisdomItem nextItem;
      for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
        nextItem = items.get (itemIndex);
        if (! nextItem.isDeleted()) {
          Category itemCategory = nextItem.getCategory();
          itemCategory.startCategoryIteration();
          while (itemCategory.hasNextCategory()) {
            String nextCategory = itemCategory.nextCategory();
            if (nextCategory.equalsIgnoreCase(selectedCategory)) {
              ok = saveNextItemToFile (markupWriter, nextItem);
            } // end if category match
          } // end while more categories for this item
        } // end if not deleted
      } // end for loop
    } // end if open ok
    
    // End the wisdom document
    if (ok) {
      writeEndTag (WISDOM);
    }
    
    if (ok) {
      ok = closeOutput();
    }
    
    return ok;
  }
  
  /**
  
  @param store The disk store from which to load the header. 
  @param collectionWindow The CollectionWindow containing metadata about 
                          the collection. 
  @return True if header was found and loaded successfully, otherwise false. 
  */
  public boolean loadCollectionHeader(
      WisdomDiskStore store, 
      CollectionWindow collectionWindow) {
    
    this.store = store;
    this.collectionWindow = collectionWindow;
    // Load the Collection Header, if one exists
    boolean headerOK = false;
    headerOK = createParser();
    File iWisdomFolder = store.getFile();
    File headerFile = null;
    if (store.isAFolder()) {
      headerFile = new File (iWisdomFolder, 
          HEADER_FILE_NAME + "." + WISDOM_FILE_EXTENSION);
      if (headerFile.exists()) {
        headerOK = parseXMLFile (collectionWindow, null, headerFile, true);
      } else {
        File xmlFolder = new File (iWisdomFolder, "xml");
        if (xmlFolder != null) {
          headerFile = new File (xmlFolder, 
            HEADER_FILE_NAME + "." + WISDOM_FILE_EXTENSION);
          if (headerFile.exists()) {
            headerOK = parseXMLFile (collectionWindow, null, headerFile, true);
          } else {
            log.recordEvent (LogEvent.MINOR, 
              "WisdomXMLIO load header file " + headerFile.toString()
                + " does not exist",
              false);
            headerOK = false;
          }
        } else {
          log.recordEvent (LogEvent.MINOR, 
            "WisdomXMLIO load xml folder is null",
            false);
          headerOK = false;
        }
      } 
    } // end if disk store is a folder
    else {
      log.recordEvent (LogEvent.MINOR, 
        "WisdomXMLIO load diskStore " + store.toString()
              + " is not a folder",
        false);
      headerOK = false;
    }
    collectionWindow.setCollectionHeaderFile(headerFile);
    return headerOK;
  }
  
  /**
   Save header information for a Wisdom collection to its regular 
   location within the passed Wisdom Catalog.
   */
  public boolean saveCollectionHeader (WisdomDiskStore store, 
      CollectionWindow header) {
    
    this.store = store;
    this.collectionWindow = header;
    ok = true;
    outOK = true;
    File iWisdomFolder = store.getFile();
    if (! store.isAFolder()) {
      ok = false;
      trouble.report 
          ("Disk Store "+ store.toString() + " is not a folder", 
              "File Save Error for Header");
    }
    
    // Make sure xml folder exists
    // File xmlFolder = FileUtils.ensureFolder(iWisdomFolder, "xml");
    File outFile = new File (iWisdomFolder, 
          HEADER_FILE_NAME + "." + WISDOM_FILE_EXTENSION);
    markupWriter 
        = new MarkupWriter (new FileMaker (outFile), MarkupWriter.XML_FORMAT);
    ok = markupWriter.openForOutput();
    if (! ok) {
      trouble.report 
          ("Header file "+ outFile.toString() + " could not be opened for output", 
              "File Save Error for Header");
      outOK = false;
    } 
    if (outFile == null) {
      ok = false;
      outOK = false;
    }
    
    if (ok) {
      // Write the XML header
      if (ok) {
        writeiWisdomComment();
      }
      level = 0;

      if (ok) {
        beginWisdomElement();
      }

      // Write the wisdom data to the file
      if (ok) {
        writeHeader (header);
      }

      // End the wisdom document
      if (ok) {
        writeEndTag (WISDOM);
      }

      // Close the file
      if (ok) {
        ok = closeOutput();
      }
    } // end if file opened ok
    
    // If we're now saving the header to a different name or folder, then
    // delete the old one after we've saved the new one
    if (ok) {
      File collectionHeaderFile = header.getCollectionHeaderFile();
      if (collectionHeaderFile != null
          && (! collectionHeaderFile.equals(outFile))) {
        collectionHeaderFile.delete();
      }
    }
    
    return ok;
  }
  
  private void writeHeader (CollectionWindow header) {
    writeElement (TITLE, header.getCollectionTitle());
    writeElement (LINK, header.getLink());
    writeElement (PATH, header.getPath());
    writeElement (DESCRIPTION, header.getDescription());
    writeElement (EDITOR, header.getEditor());
    writeElement (VERSION, header.getCollectionVersion());
    writeElement (BACKUP_FOLDER, header.getBackupFolder());
    writeElement (STORAGE_FORMAT, header.getStorageFormat());
    writeElement (ORGANIZE_WITHIN_FOLDERS, 
        String.valueOf (header.isOrganizeWithinFolders()));
    writeElement (FILE_NAMING, 
        String.valueOf (header.isFileNamingByTitle()));
  }
  
  /**
   Save one item to its regular location within the passed Wisdom Catalog.
   */
  public boolean save (WisdomDiskStore store, 
      CollectionWindow header, WisdomItem wisdom) {
    
    this.store = store;
    this.collectionWindow = header;
    ok = true;
    File iWisdomFolder = store.getFile();
    if (! store.isAFolder()) {
      ok = false;
      trouble.report 
          ("Disk Store "+ store.toString() + " is not a folder", 
              "File Save Error for Item");
    }
    
    // Make sure xml folder exists
    if (header.isOrganizeWithinFolders()) {
      File formatFolder = FileUtils.ensureFolder
          (iWisdomFolder, header.getStorageFormat().toLowerCase());
      File authorsFolder = FileUtils.ensureFolder(formatFolder, "authors");
      File authorFolder = FileUtils.ensureFolder (authorsFolder, wisdom.getAuthorFileName());
      File sourceFolder = FileUtils.ensureFolder (authorFolder,  wisdom.getSourceFileName());
      if (sourceFolder == null) {
        ok = false;
      }
    }
    if (ok) {
      File outFile = wisdom.getFile(iWisdomFolder);
      markupWriter 
          = new MarkupWriter (new FileMaker (outFile), MarkupWriter.XML_FORMAT);
      ok = markupWriter.openForOutput();
    }
    
    if (ok) {
      saveOneItemToFile (markupWriter, wisdom);
    }
    
    if (ok && olderPublicationStyle) {
      ok = saveAsHTML (store, header, wisdom);
    }

    if (ok && olderPublicationStyle) {
      ok = saveAsMobileHTML (store, header, wisdom);
    }
    
    return ok;
  }
  
  /**
   Create an XML file holding a single piece of wisdom. 
   */
  private boolean saveOneItemToFile (MarkupWriter markupWriter, WisdomItem wisdom) {
    
    ok = true;
    this.markupWriter = markupWriter;
    ok = markupWriter.openForOutput ();
    
    // Write the XML header
    if (ok) {
      writeiWisdomComment();
    }
    level = 0;
    
    if (ok) {
      beginWisdomElement();
    }
    
    // Write the wisdom data to the file
    if (ok) {
      saveNextItemToFile (markupWriter, wisdom);
    }
    
    // End the wisdom document
    if (ok) {
      writeEndTag (WISDOM);
    }
    
    // Close the file
    if (ok) {
      ok = closeOutput();
    }
 
    return ok;
  }
  
  /**
   Write the next item to the passed output file. Assume that the opening 
   and closing of the output file, along with writing of headers, 
   is being handled elsewhere.
   */
  public boolean saveNextItemToFile (MarkupWriter markupWriter, WisdomItem wisdom) {
    
    ok = true;
    this.markupWriter = markupWriter;
    
    File storeFolder = null;
    if (store != null) {
      storeFolder = store.getFile();
    }
    ok = wisdom.writeMarkup(markupWriter, storeFolder, false, true);
    
    return ok;
  }
  
  private void writeAuthor (Author author) {

    writeElement (NAME, author.getCompleteName());
    writeElement (LAST_NAME, author.getLastName());
    writeElement (FIRST_NAME, author.getFirstName());
    writeElement (FILE_NAME, author.getFileName());
    if (! author.isCompound()) {
      writeElement (WIKIPEDIA_LINK, author.getWikipediaLink());
    }
  }

 /**
   Save one item to its regular location within the passed Wisdom Catalog.
   */
  public boolean saveAsHTML (WisdomDiskStore store,
      CollectionWindow header, WisdomItem wisdom) {
    this.store = store;
    ok = true;
    File iWisdomFolder = store.getFile();
    if (! store.isAFolder()) {
      ok = false;
      trouble.report
          ("Disk Store "+ store.toString() + " is not a folder",
              "HTML Save Error");
    }

    // Make sure html folder exists
    File htmlFolder = FileUtils.ensureFolder(iWisdomFolder, "html");
    File authorsFolder = FileUtils.ensureFolder(htmlFolder, "authors");
    File authorFolder = FileUtils.ensureFolder (authorsFolder, wisdom.getAuthorFileName());
    File sourceFolder = FileUtils.ensureFolder (authorFolder,  wisdom.getSourceFileName());
    if (sourceFolder == null) {
      ok = false;
    } else {
      File outFile = new File (sourceFolder, wisdom.getTitleFileName()
          + "." + HTML_FILE_EXTENSION);
      markupWriter
          = new MarkupWriter (new FileMaker (outFile), MarkupWriter.HTML_FORMAT);
      ok = markupWriter.openForOutput();
    }
    xmlNameSpacePrefixWithColon = "";

    if (ok) {
      saveOneItemAsHTML (markupWriter, header, wisdom);
    }

    // Save Shortcut
    File idFolder = FileUtils.ensureFolder(iWisdomFolder, ID_FOLDER);
    File idFile = new File (idFolder, StringUtils.stringFromInt (wisdom.getItemID(), 4)
        + "." + HTML_FILE_EXTENSION);
    markupWriter
        = new MarkupWriter (new FileMaker (idFile), MarkupWriter.HTML_FORMAT);
    ok = markupWriter.openForOutput();
    if (ok) {
      saveOneItemAsID (markupWriter, header, wisdom);
    }

    return ok;
  }

  /**
   Create an HTML file holding a single piece of wisdom. The output file
   must have already been created.
   */
  private boolean saveOneItemAsHTML (MarkupWriter markupWriter,
      CollectionWindow header, WisdomItem wisdom) {

    ok = true;
    this.markupWriter = markupWriter;

    level = 0;
    xmlNameSpacePrefixWithColon = "";

    // ok = beginHTML (ok);

    // Write the wisdom data to the file
    if (ok) {
      saveNextItemAsHTML (markupWriter, header, wisdom);
    }

    // End the wisdom document
    ok = endHTML (ok);

    return ok;
  }

  /**
   Write the next item to the passed output file as HTML. Assume that the opening
   and closing of the output file, along with writing of headers,
   is being handled elsewhere.
   */
  private boolean saveNextItemAsHTML (MarkupWriter markupWriter,
      CollectionWindow header, WisdomItem wisdom) {

    ok = true;
    this.markupWriter = markupWriter;
    xmlNameSpacePrefixWithColon = "";

    // Create file content
    writeStartTag (HEAD);
      writeStartTag (TITLE);
        writeContent (header.getCollectionTitle() + " | " + wisdom.getTitle());
      writeEndTag (TITLE);
      writeLine ("<link rel=\"stylesheet\" "
          + "href=\"../../../../styles/html.css\" "
          + "type=\"text/css\" "
          + "title=\"Portable Wisdom Standard Style Sheet\" "
          + "/>");
      insertFile ("head_insert.html");
    writeEndTag (HEAD);

    writeStartTag (BODY);

      writeStartTag (DIVISION, HTML_ID, SITE_ID);
        beginLink ("../../../../../index.html");
          writeContent (header.getCollectionTitle());
        endLink();
      writeEndTag (DIVISION);

      writeStartTag (DIVISION, HTML_ID, NAVLINKS_ID);
        insertFile ("navlinks_insert.html");
      writeEndTag (DIVISION);

      writeStartTag (DIVISION, HTML_ID, CONTENT_ID);

        writeStartTag (DIVISION, HTML_ID, QUOTE_ID);

          for ( int categoryIndex = 0;
                categoryIndex < wisdom.getCategories();
                categoryIndex++) {
            writeStartTag (HEADING_LEVEL_1);
            for ( int levelIndex = 0;
                levelIndex < wisdom.getCategoryLevels(categoryIndex);
                levelIndex++) {
              String cat = wisdom.getCategoryLevel (categoryIndex, levelIndex);
              if (levelIndex == 0) {
                beginLink ("../../../categories/"
                    + StringUtils.makeFileName (cat, false)
                    + "/index.html");
              } else {
                writeContent ("|");
              }
              writeContent (cat);
              if (levelIndex == 0) {
                endLink();
              }
            }
            writeEndTag (HEADING_LEVEL_1);
          } // end for each category assigned to item

          writeStartTag (PARAGRAPH, ITEM_TITLE_CLASS);
            writeContent (wisdom.getTitle());
          writeEndTag (PARAGRAPH);

          wisdom.getBodyAsMarkup().writeMarkup(markupWriter);
          newLine();

          writeStartTag (DIVISION, EMDASH_CLASS);
            writeContent (EMDASH);
          writeEndTag (DIVISION);

          writeStartTag (DIVISION, AUTHOR_CLASS);
            beginLink ("../index.html");
              author = wisdom.getAuthor();
              String completeName = author.getCompleteName();
              String authorInfo = wisdom.getAuthorInfo();
              if (authorInfo.length() == 0) {
                writeContent (completeName);
              } else {
                writeContent (completeName + ", " + authorInfo);
              }
            endLink();
          writeEndTag (DIVISION);

          WisdomSource source = wisdom.getSource();
          String sourceType = source.getTypeLabel();
          String sourceTitle = source.getTitle();
          boolean hasTitle = ((! sourceTitle.equalsIgnoreCase (WisdomSource.UNKNOWN))
              && (! sourceTitle.equals("")));

          writeStartTag (DIVISION, SOURCE_TYPE_CLASS);
          if (! hasTitle) {
            writeContent ("from an unknown source");
          }
          else
          if (sourceType.equalsIgnoreCase (WisdomSource.UNKNOWN)) {
            writeContent ("from");
          } else {
            writeContent ("from the " + sourceType);
          }
          writeEndTag (DIVISION);

          if (hasTitle) {
            writeStartTag (DIVISION, SOURCE_TITLE_CLASS);
              beginLink ("index.html");
                writeStartTag (CITE);
                  writeContent (sourceTitle);
                writeEndTag (CITE);
              endLink();
            writeEndTag (DIVISION);
          }

          String rights = source.getRights();
          if (rights.length() > 0) {
            writeStartTag (DIVISION, SOURCE_RIGHTS_CLASS);
              writeContent (rights);
              writeContent (source.getYear());
              writeContent (source.getRightsOwner());
            writeEndTag (DIVISION);
          }

          writeStartTag (PARAGRAPH);
            writeContent ("No. ");
            beginLink (wisdom.getItemIDLink (header));
            writeContent (String.valueOf (wisdom.getItemID()));
            endLink();
          writeEndTag (PARAGRAPH);

          writeStartTag (DIVISION, XML_LINK_CLASS);
            beginLink ("../../../../xml/authors/"
                + author.getFileName()
                + "/"
                + source.getFileName()
                + "/"
                + wisdom.getTitleFileName()
                + "."
                + WISDOM_FILE_EXTENSION);
              writeContent("&lt;xml&gt;");
            endLink();
          writeEndTag (DIVISION);

        writeEndTag (DIVISION);

      writeEndTag (DIVISION);

    writeEndTag (BODY);

    return ok;
  }

  /**
   Save one item in an HTML format designed for mobile devices to its regular
   location within the passed Wisdom Catalog.
   */
  public boolean saveAsMobileHTML (WisdomDiskStore store,
      CollectionWindow header, WisdomItem wisdom) {
    this.store = store;
    ok = true;
    File iWisdomFolder = store.getFile();
    if (! store.isAFolder()) {
      ok = false;
      trouble.report
          ("Disk Store "+ store.toString() + " is not a folder",
              "HTML Save Error");
    }

    // Make sure html folder exists
    File mobileFolder = FileUtils.ensureFolder(iWisdomFolder, "mobile");
    File authorsFolder = FileUtils.ensureFolder(mobileFolder, "authors");
    File authorFolder = FileUtils.ensureFolder (authorsFolder, wisdom.getAuthorFileName());
    File sourceFolder = FileUtils.ensureFolder (authorFolder,  wisdom.getSourceFileName());
    if (sourceFolder == null) {
      ok = false;
    } else {
      File outFile = new File (sourceFolder, wisdom.getTitleFileName()
          + "." + HTML_FILE_EXTENSION);
      markupWriter
          = new MarkupWriter (new FileMaker (outFile), MarkupWriter.HTML_FORMAT);
      ok = markupWriter.openForOutput();
    }
    xmlNameSpacePrefixWithColon = "";

    if (ok) {
      saveOneItemAsMobileHTML (markupWriter, header, wisdom);
    }

    return ok;
  }

  /**
   Create an HTML file holding a single piece of wisdom in a format designed
   for mobile devices. The output file must have already been created.
   */
  private boolean saveOneItemAsMobileHTML (MarkupWriter markupWriter,
      CollectionWindow header, WisdomItem wisdom) {

    ok = true;
    this.markupWriter = markupWriter;

    level = 0;
    xmlNameSpacePrefixWithColon = "";

    // ok = beginHTML (ok);

    // Write the wisdom data to the file
    if (ok) {
      saveNextItemAsMobileHTML (markupWriter, header, wisdom);
    }

    // End the wisdom document
    ok = endHTML (ok);

    return ok;
  }

  /**
   Write the next item to the passed output file as HTML in a format designed
   for mobile devices. Assume that the opening and closing of the output file,
   along with writing of headers, is being handled elsewhere.
   */
  private boolean saveNextItemAsMobileHTML (MarkupWriter markupWriter,
      CollectionWindow header, WisdomItem wisdom) {

    ok = true;
    this.markupWriter = markupWriter;
    xmlNameSpacePrefixWithColon = "";

    // Create file content
    writeStartTag (HEAD);
      writeStartTag (TITLE);
        writeContent (header.getCollectionTitle() 
                + " | mobile | " + wisdom.getTitle());
      writeEndTag (TITLE);
      writeLine ("<link rel=\"stylesheet\" "
          + "href=\"../../../../styles/mobile.css\" "
          + "type=\"text/css\" "
          + "title=\"Portable Wisdom Mobile Style Sheet\" "
          + "/>");
      insertFile ("mobile_head_insert.html");
    writeEndTag (HEAD);

    writeStartTag (BODY);

      writeStartTag (DIVISION, HTML_ID, SITE_ID);
        beginLink ("../../../../index.html");
          writeContent (header.getCollectionTitle());
        endLink();
      writeEndTag (DIVISION);

      writeStartTag (DIVISION, HTML_ID, CONTENT_ID);

      for ( int categoryIndex = 0;
            categoryIndex < wisdom.getCategories();
            categoryIndex++) {
        writeStartTag (HEADING_LEVEL_1);
        for ( int levelIndex = 0;
            levelIndex < wisdom.getCategoryLevels(categoryIndex);
            levelIndex++) {
          String cat = wisdom.getCategoryLevel (categoryIndex, levelIndex);
          if (levelIndex == 0) {
            beginLink ("../../../categories/"
                + StringUtils.makeFileName (cat, false)
                + "/index.html");
          } else {
            writeContent ("&gt;");
          }
          writeContent (cat);
          if (levelIndex == 0) {
            endLink();
          }
        }
        writeEndTag (HEADING_LEVEL_1);
      } // end for each category assigned to item

      writeStartTag (DIVISION, HTML_ID, QUOTE_ID);

      wisdom.getBodyAsMarkup().writeMarkup(markupWriter);
      newLine();

      writeEndTag (DIVISION);

      writeStartTag (DIVISION, EMDASH_CLASS);
        writeContent (EMDASH);
      writeEndTag (DIVISION);

      writeStartTag (DIVISION, AUTHOR_CLASS);
        beginLink ("../index.html");
          author = wisdom.getAuthor();
          String completeName = author.getCompleteName();
          String authorInfo = wisdom.getAuthorInfo();
          if (authorInfo.length() == 0) {
            writeContent (completeName);
          } else {
            writeContent (completeName + ", " + authorInfo);
          }
        endLink();
      writeEndTag (DIVISION);

      WisdomSource source = wisdom.getSource();
      String sourceType = source.getTypeLabel();
      String sourceTitle = source.getTitle();
      boolean hasTitle = ((! sourceTitle.equalsIgnoreCase (WisdomSource.UNKNOWN))
          && (! sourceTitle.equals("")));

      writeStartTag (DIVISION, SOURCE_TYPE_CLASS);
      if (! hasTitle) {
        writeContent ("from an unknown source");
      }
      else
      if (sourceType.equalsIgnoreCase (WisdomSource.UNKNOWN)) {
        writeContent ("from");
      } else {
        writeContent ("from the " + sourceType);
      }
      writeEndTag (DIVISION);

      if (hasTitle) {
        writeStartTag (DIVISION, SOURCE_TITLE_CLASS);
          beginLink ("index.html");
            writeStartTag (CITE);
              writeContent (sourceTitle);
            writeEndTag (CITE);
          endLink();
        writeEndTag (DIVISION);
      }

      String rights = source.getRights();
      if (rights.length() > 0) {
        writeStartTag (DIVISION, SOURCE_RIGHTS_CLASS);
          writeContent (rights);
          writeContent (source.getYear());
          writeContent (source.getRightsOwner());
        writeEndTag (DIVISION);
      }



      writeEndTag (DIVISION);

      writeStartTag (DIVISION, HTML_ID, NAVLINKS_ID);
        insertFile ("mobile_navlinks_insert.html");
      writeEndTag (DIVISION);

    writeEndTag (BODY);

    return ok;
  }
  
  /**
   Create an HTML file holding a single piece of wisdom. The output file
   must have already been created. 
   */
  public boolean saveOneItemAsID (MarkupWriter markupWriter, 
      CollectionWindow header, WisdomItem wisdom) {
    
    ok = true;
    this.markupWriter = markupWriter;
    
    level = 0;
    xmlNameSpacePrefixWithColon = "";
    
    // ok = beginHTML (ok);
    
    // Write the wisdom data to the file
    if (ok) {
      saveNextItemAsID (markupWriter, header, wisdom);
    }
    
    ok = endHTML (ok);
    
    return ok;
  }
  
  /* private boolean beginHTML (boolean ok) {
    if (ok) {
      writeLine ("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
        + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
      beginHTMLElement();
    }
    return ok;
  } */
  
  private boolean endHTML (boolean ok) {
    // End the wisdom document
    // if (ok) {
      // writeEndTag (HTML);
    // }
    
    // Close the file
    if (ok) {
      ok = closeOutput();
    }
    return ok;
  }
  
  /**
   Write the next item to the passed output file as HTML. Assume that the opening 
   and closing of the output file, along with writing of headers, 
   is being handled elsewhere.
   */
  public boolean saveNextItemAsID (MarkupWriter markupWriter, 
      CollectionWindow header, WisdomItem wisdom) {
    
    ok = true;
    this.markupWriter = markupWriter;
    xmlNameSpacePrefixWithColon = "";
    
    // Create file content
    writeStartTag (HEAD);
      writeStartTag (TITLE);
        writeContent (header.getCollectionTitle() + " | " + wisdom.getTitle());
      writeEndTag (TITLE);
      writeLine ("<meta http-equiv=\"REFRESH\" content=\"0; url="
          + wisdom.getItemLink (header)
          + "\" />");
      writeLine ("<link rel=\"stylesheet\" "
          + "href=\"../../../../../styles/html.css\" "
          + "type=\"text/css\" "
          + "title=\"Portable Wisdom Standard Style Sheet\" "
          + "/>");
      insertFile ("head_insert.html");
    writeEndTag (HEAD);
    
    writeStartTag (BODY);
    
    writeEndTag (BODY);
    
    return ok;
  }
  
  private void beginWisdomElement () {
    
    // xmlNameSpacePrefixWithColon = XML_NAME_SPACE_PREFIX + ":";
    
    startLine();
    indent();
    append ("<" 
        + xmlNameSpacePrefixWithColon 
        + WISDOM 
        // + " xmlns:" 
        // + XML_NAME_SPACE_PREFIX
        // + "="
        // + XML_NAME_SPACE_URI
        + ">");
    writeLine();
    level++;
  }
  
  /*private void beginHTMLElement () {
    
    xmlNameSpacePrefixWithColon = "";
    
    writeStartTag (HTML);
  } */
  
  private void writeiWisdomComment () {
    writeLine ("<!-- Generated by iWisdom "
        + programVersion + " available from PowerSurgePub.com -->");
  }
  
  private void writeElement (String elementName, String content) {
    String trimmed = content.trim();
    // Do not write empty elements
    if (trimmed.length() > 0) {
      writeStartTag (elementName);
      writeContent (content);
      writeEndTag (elementName);
    }
  }
  
  private void beginLink (String address) {
    writeStartTag (HTML_LINK, HREF, address);
  }
  
  private void endLink () {
    writeEndTag (HTML_LINK);
  }
  
  private void writeStartTag (String elementName) {
    writeStartTag (elementName, "", "");
  }
  
  private void writeStartTag (String elementName, String className) {
    writeStartTag(elementName, HTML_CLASS, className);
  }
  
  private void writeStartTag (String elementName, String attr, String value) {
    startLine();
    indent();
    
    append ("<" 
        + xmlNameSpacePrefixWithColon 
        + elementName);
    
    if (attr != null && value != null) {
      if (attr.length() > 0 && value.length() > 0) {
        append (" "
            + attr
            + "=\"" 
            + value 
            + "\"");
      }
    }

    append (">");
    
    writeLine();
    level++;
  }
  
  public void writeStartTag (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes,
      boolean emptyTag) {
    startLine();
    indent();
    
    append ("<");
    append (localName);
    for (int i = 0; i < attributes.getLength(); i++) {
      append (" "
          + attributes.getLocalName(i)
          + "=\""
          + attributes.getValue(i)
          + "\"");
    }
    if (emptyTag) {
      append (" /");
    }
    append (">");
    
    writeLine();
    if (! emptyTag) {
      level++;
    }
  }
  
  public void writeContent (String s) {
    startLine();
    indent();
    append (markupWriter.formatTextForMarkup (s));
    writeLine();
  }
  
  private void writeEndTag (String elementName) {
    startLine();
    level--;
    indent();
    append ("</" 
        + xmlNameSpacePrefixWithColon 
        + elementName + ">");
    writeLine();
  }
  
  public void writeEndTag (
      String namespaceURI,
      String localName,
      String qualifiedName) {
    startLine();
    level--;
    indent();
    append ("</" 
        + localName 
        + ">");
    writeLine();
  }
  
  /**
   Insert the specified file into the output stream.
   */
  private boolean insertFile (String fileName) {
    boolean ok = true;
    
    // Look for the file in the Wisdom data folder, within the XSLT sub-folder
    File iWisdomFolder = store.getFile();
    if (! store.isAFolder()) {
      ok = false;
    }

    File xsltFolder = new File (iWisdomFolder, WisdomDiskStore.XSLT_FOLDER_NAME);
    File inFile = new File (xsltFolder, fileName);
    if (ok
        && inFile.exists()
        && inFile.isFile()
        && inFile.canRead()) {
      // ok
    } else {
      ok = false;
    }
    
    // If we can't find the file in the wisdom data folder, 
    // then try the application folder
    if (! ok) {
      File appFolder = home.getAppFolder();
      File themesFolder = new File (appFolder, "themes");
      File themeFolder = new File (themesFolder, "portablewisdom1");
      xsltFolder = new File (themeFolder, WisdomDiskStore.XSLT_FOLDER_NAME);
      inFile = new File (xsltFolder, fileName);
      if (inFile.exists()
          && inFile.isFile()
          && inFile.canRead()) {
        ok = true;
      } 
    }
    
    // If the file was found, then read and copy it into the output
    if (ok) {
      try {
        BufferedReader inReader = new BufferedReader(new FileReader (inFile));
        String inLine = inReader.readLine();
        while (inLine != null) {
          writeLine (inLine);
          inLine = inReader.readLine();
        }
      } catch (java.io.IOException e) {
        ok = false;
      }
    } 
    
    // If any problems, then gripe about it
    if (! ok) {
      if (! insertTroubleReported) {
        trouble.report 
            ("File named "+ inFile.toString() + " could not be read", 
                "Insert File Error"); 
        insertTroubleReported = true;
      }
    }
    
    return ok;
  }
  
  private void startLine () {
    textOut.setLength(0);
  }
  
  private void indent() {
    xmlAppendSpaces (level * 2);
  }
  
  private void append(String s) {
    textOut.append (s);
  }
  
  /**
    Write the requested number of spaces.
   */
  public void xmlAppendSpaces (int spaces) {
    for (int i = 0; i < spaces; i++) {
      textOut.append (" ");
    }
  }
  
  private boolean closeOutput () {
    if (outOK) {
      outOK = markupWriter.close();
    }
    return outOK;
  } // end method
  
  private void writeLine() {
    writeLine (textOut.toString());
    startLine();
  }
  
  public boolean openForOutput() {
    if (markupWriter == null) {
      return false;
    } else {
      outOK = markupWriter.openForOutput();
    }
    return outOK;
  }
  
  public boolean writeLine (String line) {
    if (outOK) {
      outOK = markupWriter.writeLine (line);
    } // end if ok so far
    else {
      System.out.println ("writeLine but ! outOK");
    }
    return outOK;
  } // end method writeLine
  
  /**
   The following four methods implement the MarkupLineWriter interface.
   */
  
  public void write (StringBuffer s) {
    write (s.toString());
  }
  
  public boolean write (String s) {
    if (outOK) {
      outOK = markupWriter.write (s);
    } // end if ok so far
    return outOK;
  } // end method write
  
  public boolean newLine () {
    if (outOK) {
      outOK = markupWriter.newLine();
    } // end if ok so far
    return outOK;
  } // end method newLine
  
  public boolean flush () {
    outOK = markupWriter.flush();
    return outOK;
  }
  
  public boolean close () {
    outOK = markupWriter.close();
    return outOK;
  }
  
  public boolean isOK () {
    return outOK;
  }
  
  /**
   Return the file path, or other string identifying the output destination.

   @return The file path, or other string identifying the output destination.
   */
  public String getDestination () {
    /* if (markupWriter != null) {
      return markupWriter.
    } */
    return "";
  }

  /**
     Sets a logger to be used for logging operations.
    
     @param log Logger instance.
   */
  public void setLog (Logger log) {
    this.log = log;
  }
  
  /* -----------------------------------------------------------------
   This section of the program has methods for reading wisdom from the
   catalog. 
   ------------------------------------------------------------------- */ 
  
  public void setSaveToDisk (boolean saveToDisk) {
    this.saveToDisk = saveToDisk;
  }

  /**
   Load data from an iWisdom library file or catalog into memory.
   */
  public boolean importFile (
      String importName, 
      CollectionWindow collectionWindow, 
      WisdomItems items) {
    
    saveToDisk = true;
    respectItemID = false;
    this.collectionWindow = collectionWindow;
    // this.importLog = importLog;
    boolean ok = true;
    this.items = items;
    ok = createParser();
    if (ok) {
      ok = parseXMLFileOrFolder(collectionWindow, items, importName, false);
    }
    return ok;
  }
  
  /**
   Load data from the iWisdom catalog into memory
   */
  public boolean load (WisdomDiskStore store, CollectionWindow collectionWindow, 
      WisdomItems items) {
    
    this.store = store;
    this.collectionWindow = collectionWindow;
    saveToDisk = false;
    respectItemID = true;
    boolean ok = true;
    this.items = items;
    ok = createParser();
    
    loadCollectionHeader(store, collectionWindow);
    
    // Parse all the XML files in the folder
    File xmlFolder = null;
    if (ok) {
      xmlFolder = store.getAuthorsFolder();
      ok = parseXMLFileOrFolder
          (collectionWindow, items, xmlFolder.toString(), true);
    }
    saveToDisk = true;
    return ok;
  }
  
  /**
   Create XML Parser.
   */
  private boolean createParser () {
    
    if (parserOK && parser != null) {
      return parserOK;
    }
    parserOK = true;
    try {
      parser = XMLReaderFactory.createXMLReader();
    } catch (SAXException e) {
      log.recordEvent (LogEvent.MINOR, 
          "Generic SAX Parser Not Found",
          false);
      try {
        parser = XMLReaderFactory.createXMLReader
            ("org.apache.xerces.parsers.SAXParser");
      } catch (SAXException eex) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Xerces SAX Parser Not Found",
            false);
        parserOK = false;
      } // end catch specific sax parser not found
    } // end catch generic sax parser exception
    if (parserOK) {
      parser.setContentHandler (this);
    }
    if (! parserOK) {
      ok = false;
    }
    return parserOK;
  } // end method createParser
  
  /**
   Parse a passed file or folder for xml wisdom content. 
   */
  private boolean parseXMLFileOrFolder (
      CollectionWindow collectionWindow,
      WisdomItems items,
      String importName, 
      boolean readingFromCatalog) {
    boolean ok = true;
    FileName importFileName = new FileName (importName);
    File xmlDirEntry = importFileName.getFile();
    if (xmlDirEntry == null) {
      parseXMLFile (importName, readingFromCatalog);
    } else {
      if (ok) {
        if (! xmlDirEntry.exists()) {
          ok = false;
          log.recordEvent (LogEvent.MEDIUM, 
              "XML File or Directory " + xmlDirEntry.toString() + " cannot be found",
              false);
        }
      }
      if (ok) {
        if (! xmlDirEntry.canRead()) {
          ok = false;
          log.recordEvent (LogEvent.MEDIUM, 
              "XML File or Directory " + xmlDirEntry.toString() + " cannot be read",
              false);       
        }
      }

      if (ok) {
        if (xmlDirEntry.isDirectory()) {
          directoryReader = new DirectoryReader (xmlDirEntry);
          directoryReader.setLog (log);
          directoryReader.setMaxDepth (99);
          try {
            directoryReader.openForInput();
            while (! directoryReader.isAtEnd()) {
              File nextFile = directoryReader.nextFileIn();
              if ((nextFile != null) 
                  && (! nextFile.getName().startsWith ("."))
                  && (nextFile.exists())
                  && (nextFile.canRead()) 
                  && (nextFile.isFile())) {
                FileName fileName = new FileName (nextFile);
                String fileExtension = fileName.getExt();
                if ((fileExtension.trim().length() < 1)
                    || (fileName.getExt().equalsIgnoreCase (WISDOM_FILE_EXTENSION))) {
                  parseXMLFile (collectionWindow, items, nextFile, readingFromCatalog);
                } // end if file extension is ok
              } // end if file exists, can be read, etc.
            } // end while more files in specified folder
          } catch (IOException ioe) {
            ok = false;
            log.recordEvent (LogEvent.MEDIUM, 
                "Encountered I/O error while reading XML directory " 
                + xmlDirEntry.toString(),
                false);     
          } // end if caught I/O Error
          directoryReader.close();
        } // end if passed String identified a directory
        else 
        if (xmlDirEntry.isFile()) {
          parseXMLFile (importName, readingFromCatalog);
        }
      } // end if everything still OK
    }
    return ok;
  }
  
  /**
  Parse an xml header file and/or wisdom file. 
  
  @param collectionWindow
  @param items
  @param file
  @param readingFromCatalog
  @return 
  */
  public boolean parseXMLFile (
      CollectionWindow collectionWindow,
      WisdomItems items,
      File file, 
      boolean readingFromCatalog) {
    
    boolean parseXMLFileOK = true;
    saveToDisk = false;
    respectItemID = true;
    this.collectionWindow = collectionWindow;
    this.items = items;
    ok = createParser();
    try {
      parseXMLFile (file.toURI().toURL().toString(), readingFromCatalog);
    } catch (MalformedURLException e) {
      parseXMLFileOK = false;
      log.recordEvent (LogEvent.MEDIUM, 
        "WisdomXMLIO parseXMLFile malformed URL derived from " 
          + file.toString(),
        false); 
    }
    return parseXMLFileOK;
  }
  
  /**
   Parse a specific xml file.
   */
  private void parseXMLFile (String inputString, boolean readingFromCatalog) {

    FileName inputName = new FileName (inputString);
    File inputFile = inputName.getFile();
    wisdomFile = inputFile;
    readFromCatalog = false;
    if (readingFromCatalog) {
      xmlFileName = inputName.getBase();
      if (xmlFileName.length() > 0) {
        File parentFile = inputFile.getParentFile();
        xmlSourceFileName = parentFile.getName();
        if (xmlSourceFileName.length() > 0) {
          File grandparentFile = parentFile.getParentFile();
          xmlAuthorFileName = grandparentFile.getName();
          if (xmlAuthorFileName.length() > 0) {
            readFromCatalog = true;
          } // end if we have an Author folder name
        } // end if we have a Source folder name
      } // end if we have an xml file name
    } // end if we are reading from an iWisdom catalog 
    chars = new ArrayList();
    elementLevel = 0;
    StringBuffer str = new StringBuffer();
    storeField (elementLevel, str, "");
    withinBody = false;
    try {
      parser.parse (inputName.getURLString());
      if (readingFromCatalog) {
        
      }
    } 
    catch (SAXException saxe) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Encountered SAX error while reading XML file " + inputName 
            + saxe.toString(),
            false);   
    } 
    catch (java.io.IOException ioe) {
        log.recordEvent (LogEvent.MEDIUM, 
            "Encountered I/O error while reading XML file " + inputName 
            + ioe.toString(),
            false);   
    }
  }
  
  /**
   Handle the beginning of a new element when parsing XML.
   */
  public void startElement (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes) {
    
    if (localName.equals (CATEGORY)) {
      categorySeparator = Category.PREFERRED_CATEGORY_SEPARATOR;
    }
    
    if (localName.equals (WISDOM)
        || (localName.equals (QUOTATIONS))
        || (localName.equals (CHANNEL))) {
      elementLevel = 1;
      withinBody = false;
      withinQuotation = true;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str, localName);
    }
    else
    if (localName.equals (ITEM)
        || localName.equals (QUOTATION)
        || localName.equals (QUOTE)) {
      elementLevel = 2;
      withinBody = false;
      authorFound = false;
      sourceTitleFound = false;
      wisdom = new WisdomItem ();
      wisdom.setFileName(store, wisdomFile);
      author = null;
      categorySeparator = Category.PREFERRED_CATEGORY_SEPARATOR;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str, localName);
      withinQuotation = (localName.equals(QUOTATION));
      if (localName.equals (QUOTE)) {
        String authorName = attributes.getValue (BY);
        if ((authorName != null)
            && (authorName.length() > 0)) {
          supplyAuthor (authorName);
        }
      }
      if (localName.equals (QUOTATION)) {
        withinBody = true;
      }
    }
    else
    if (withinQuotation
        && (localName.equals (AUTHOR)
            || localName.equals (SOURCE)
            || localName.equals (NOTE))) {
      withinBody = false;
      wisdom.bodyClose();
      elementLevel = 3;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str, localName);
    }
    else
    if (elementLevel >= 0) {
      if (withinBody) {
        if (wisdom != null) {
          if (localName.equals (PRE) 
              || localName.equals (CONTENT)) {
            // skip tags that we don't want to be treated as html
          } else {
            wisdom.bodyStartElement (
                namespaceURI,
                localName,
                qualifiedName,
                attributes);
          }
        } // end if wisdom exists
      }
      else
      if (localName.equals (BODY)
          || localName.equals (CONTENT)
          || localName.equals (PARAGRAPH)) {
        elementLevel++;
        StringBuffer str = new StringBuffer();
        storeField (elementLevel, str, localName);
        withinBody = true;
      } else {
        elementLevel++;
        StringBuffer str = new StringBuffer();
        storeField (elementLevel, str, localName);
      }
    }
  } // end method
  
  /*
  private void harvestAttributes (Attributes attributes, DataField field) {
    for (int i = 0; i < attributes.getLength(); i++) {
      String name = attributes.getLocalName (i);
      DataFieldDefinition def = dict.getDef (name);
      if (def == null) {
        def = new DataFieldDefinition (name);
        dict.putDef (def);
      }
      System.out.println ("  Attribute " + name + " = " + attributes.getValue (i));
      DataField attr = new DataField (def, attributes.getValue (i));
      field.addField (attr);
    }
  }
   */
  
  public void characters (char [] ch, int start, int length) {
    
    StringBuffer xmlchars = new StringBuffer();
    xmlchars.append (ch, start, length);
    
    if (withinBody) {
      if (wisdom != null) {
        wisdom.bodyCharacters (ch, start, length);
        // System.out.println ("WisdomXMLIO.characters withinBody "
        //     + xmlchars.toString());
      }
    } else {
      // StringBuffer xmlchars = new StringBuffer();
      // xmlchars.append (ch, start, length);
      if (elementLevel >= 1 
          && elementLevel < chars.size()) {
        StringBuffer str = (StringBuffer)chars.get (elementLevel);
        boolean lastCharWhiteSpace = false;
        if (str.length() < 1
            || Character.isWhitespace (str.charAt (str.length() - 1))) {
          lastCharWhiteSpace = true;
        }
        char c;
        boolean charWhiteSpace;
        for (int i = start; i < start + length; i++) {
          c = ch [i];
          charWhiteSpace = Character.isWhitespace (c);
          if (charWhiteSpace) {
            if (lastCharWhiteSpace) {
              // do nothing
            } else {
              lastCharWhiteSpace = true;
              str.append (" ");
            }
          } else {
            str.append (c);
            lastCharWhiteSpace = false;
          }
        } // end for each passed character
        // str.append (ch, start, length);
      } // end if we are at a valid element level
    } // end if not within body
  } // end method characters
  
  public void ignorableWhitespace (char [] ch, int start, int length) {
    
  }
  
  public void endElement (
      String namespaceURI,
      String localName,
      String qualifiedName) {
    
    if (elementLevel >= 0) {
      StringBuffer str = (StringBuffer)chars.get (elementLevel);
      String parent = getElementName (elementLevel - 1);
      String grandParent= getElementName (elementLevel - 2);

      if (str.length() > 0
          && str.charAt (str.length() - 1) == ' ') {
        str.deleteCharAt (str.length() - 1);
      }
      if (localName.equals (BODY)) {
        // StringBuffer str = new StringBuffer();
        // storeField (elementLevel, str);
        // wisdom.setBody (str.toString());
        withinBody = false;
        wisdom.bodyClose();
        elementLevel--;
      } 
      else
      if (grandParent.equals (CHANNEL)
            && localName.equals (DESCRIPTION)) {
        // StringBuffer str = new StringBuffer();
        // storeField (elementLevel, str);
        wisdom.setBody (str.toString());
        withinBody = false;
        elementLevel--;
      } 
      else
      if (grandParent.equals (CHANNEL)
          && localName.equals (TITLE)) {
        supplyAuthor (str.toString());
        withinBody = false;
        elementLevel--;
      }
      else
      if (localName.equals (ITEM_ID)) {
        wisdom.setItemID (str.toString());
        elementLevel--;
      }
      else
      if (localName.equals (CONTENT)) {
        // wisdom.setBody (str.toString());
        withinBody = false;
        wisdom.bodyClose();
        elementLevel--;
      }
      /* else
      if (localName.equals (PARAGRAPH) 
          && withinQuotation) {
        // wisdom.setBody (str.toString());
        withinBody = false;
        elementLevel--;
      } */
      else 
      if (withinBody) {
        if (wisdom != null) {
          if (localName.equals (PRE)) {
            // skip it
          } else {
            wisdom.bodyEndElement (
                namespaceURI,
                localName,
                qualifiedName);
          }
        } // end if wisdom exists
      }
      else
      if (localName.equals (ITEM)) {
        if (author != null) {
          wisdom.setAuthor (author);
        }
        if (readFromCatalog) {
          wisdom.setAuthorFileName (xmlAuthorFileName);
          wisdom.setSourceFileName (xmlSourceFileName);
          wisdom.setTitleFileName (xmlFileName);
        }
        int addedAt = items.add (wisdom, saveToDisk, respectItemID, false);
        if (addedAt < 0) {
          ok = false;
        } else { // end if last item not added successfully
          itemImported (wisdom);
        }
        elementLevel = -1;
      }
      else
      if (localName.equals (QUOTE)) {
        wisdom.setBody (str.toString());
        if (author != null) {
          wisdom.setAuthor (author);
        }
        int addedAt = items.add (wisdom, saveToDisk, respectItemID, false);
        if (addedAt < 0) {
          ok = false;
        } else { // end if last item not added successfully
          itemImported (wisdom);
        }
        elementLevel = -1; 
      }
      else
      if (localName.equals (QUOTATION)) {
         if (author != null) {
          wisdom.setAuthor (author);
        }
        int addedAt = items.add (wisdom, saveToDisk, respectItemID, false);
        if (addedAt < 0) {
          ok = false;
        } else { // end if last item not added successfully
          itemImported (wisdom);
        }
        elementLevel = -1; 
        withinQuotation = false;
      } else {
        if (str.length() > 0) {
          if (localName.equals (TITLE)
              && elementLevel == 2
              && collectionWindow.getCollectionTitle().equals("")) {
            collectionWindow.setCollectionTitle (str.toString());
          }
          else
          if (localName.equals (LINK)
              && elementLevel == 2
              && collectionWindow.getLink().equals("")) {
            collectionWindow.setLink (str.toString());
          }
          else
          if (localName.equals (PATH)
              && elementLevel == 2
              && collectionWindow.getPath().equals("")) {
            collectionWindow.setPath (str.toString());
          }
          else
          if (localName.equals (DESCRIPTION)
              && elementLevel == 2
              && collectionWindow.getDescription().equals("")) {
            collectionWindow.setDescription (str.toString());
          }
          else
          if (localName.equals (EDITOR)
              && elementLevel == 2
              && collectionWindow.getEditor().equals("")) {
            collectionWindow.setEditor (str.toString());
          }
          else
          if (localName.equals (VERSION)
              && elementLevel == 2
              && collectionWindow.getCollectionVersion().equals("")) {
            collectionWindow.setCollectionVersion (str.toString());
          }
          else
          if (localName.equals (ASSUME_QUOTATION)
              && elementLevel == 2) {
            collectionWindow.setAllQuotes (str.toString());
          }
          else
          if (localName.equals(BACKUP_FOLDER)
              && elementLevel == 2
              && collectionWindow.getBackupFolder().equals("")) {
            collectionWindow.setBackupFolder(str.toString());
          }
          else
          if (localName.equals(STORAGE_FORMAT)
              && elementLevel == 2) {
            collectionWindow.setStorageFormat(str.toString());
          }
          else
          if (localName.equals(ORGANIZE_WITHIN_FOLDERS)
              && elementLevel == 2) {
            collectionWindow.setOrganizeWithinFolders(str.toString());
          }
          else
          if (localName.equals(FILE_NAMING)
              && elementLevel == 2) {
            collectionWindow.setFileNamingByTitle(str.toString());
          }
          else  
          if (localName.equals (CATEGORY + "1")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (localName.equals (CATEGORY + "2")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (localName.equals (CATEGORY + "3")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (localName.equals (CATEGORY + "4")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (localName.equals (CATEGORY + "5")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (localName.equals (TITLE) 
              && elementLevel == 3
              && (! withinQuotation)) {
            wisdom.setTitle (str.toString());
          }
          else
          if (localName.equals (NAME) && elementLevel == 4) {
            supplyAuthor (str.toString());
          } //end if end of author name element
          else
          if (localName.equals (TYPE) || localName.equals (SOURCE_TYPE)) {
            wisdom.setSourceType (str.toString());
          }
          else
          if (localName.equals (TITLE) 
              && elementLevel == 4
              && (! withinQuotation)) {
            supplySourceTitle(str.toString());
          }
          else
          if (localName.equals (MINOR_TITLE)) {
            wisdom.setMinorTitle (str.toString());
          }
          else
          if (localName.equals (LINK) && parent.equals (AUTHOR)) {
            wisdom.setAuthorLink (str.toString());
            if (author != null) {
              author.setLink (str.toString());
            }
          }
          else
          if (localName.equals (LINK) && elementLevel == 4) {
            wisdom.setSourceLink (str.toString());
          }
          else
          if (localName.equals (ID)) {
            wisdom.setSourceID (str.toString());
          }
          else
          if (localName.equals (RATING)) {
            wisdom.setRating (str.toString());
          }
          else
          if (localName.equals (PAGES)) {
            wisdom.setPages (str.toString());
          }
          else
          if (localName.equals (RIGHTS)) {
            wisdom.setRights (str.toString());
          }
          else
          if (localName.equals (YEAR)) {
            wisdom.setYear (str.toString());
          }
          else
          if (localName.equals (PUBLISHER)) {
            wisdom.setPublisher (str.toString());
          }
          else
          if (localName.equals (CITY)) {
            wisdom.setCity (str.toString());
          }
          else
          if (localName.equals (RIGHTS_OWNER)) {
            wisdom.setRightsOwner (str.toString());
          } 
          else
          if (localName.equals (DATE_ADDED)) {
            wisdom.setDateAdded (str.toString());
          }
          else
          if (localName.equals (AUTHOR_INFO)) {
            wisdom.setAuthorInfo (str.toString());
          }
          else
          if (localName.equals(FILE_NAME)) {
            if (parent.equals (AUTHOR)) {
              wisdom.setAuthorFileName (str.toString());
            }
            else
            if (parent.equals (SOURCE)) {
              wisdom.setSourceFileName (str.toString());
            } else {
              wisdom.setTitleFileName (str.toString());
            }
          }
          else
          if (withinQuotation) {
            if (localName.equals (SOURCE)) {
              if (authorFound) {
                supplySourceTitle (str.toString());
              } else {
                supplyAuthor (str.toString());
              }
            }
            else
            if (localName.equals (AUTHOR)) {
              supplyAuthor (str.toString());
            }
            else
            if (localName.equals (CITE)
                && (! sourceTitleFound)) {
              supplySourceTitle (str.toString());
            } // end if ending cite element
          } // end if within a quotation element
        } // end if we have non-blank content
      elementLevel--;
      } // end if we have an ending element
    } // end if we are within a wisdom element
  } // end method
  
  private void supplySourceTitle (String text) {
    wisdom.setSourceTitle (text);
    sourceTitleFound = true;
  }
  
  private void supplyAuthor (String text) {
    if (author == null) {
      author = new Author (text);
    } else {
      author.setCompleteName (text);
    }
    authorFound = true;
  }
  
  private void itemImported (WisdomItem item) {
    /* if (importLog != null) {
      importLog.log (item);
    } */
  }
  
  private void storeField (int level, StringBuffer str, String elementName) {
    
    if (chars.size() > level) {
      chars.set (level, str);
    } else {
      chars.add (level, str);
    }
    
    if (this.elementName.size() > level) {
      this.elementName.set (level, elementName);
    } else {
      this.elementName.add (level, elementName);
    }
    
  } // end method
  
  private String getElementName (int i) {
    if (i < 0 || i >= elementName.size()) {
      return "";
    } else {
      return (String)elementName.get (i);
    }
  }
  
}

