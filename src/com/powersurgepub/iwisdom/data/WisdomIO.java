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

import com.powersurgepub.psdatalib.psdata.values.Author;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.disk.*;
  import com.powersurgepub.psdatalib.markup.*;
  import java.io.*;
  import java.text.*;
  import java.util.*;

/**
 Methods to get wisdom items from permanent storage (disk) and to store 
 them to permanent storage. 
 */
public class WisdomIO
    implements 
      ElementHandler {
  
  public  final static String OBJECT_NAME         = "WisdomItem";
  
  public  static final String ITEM                = "item";
  public  static final String WISDOM              = "wisdom";
  public  static final String ITEM_ID             = "item-id";
  public  static final String CATEGORY            = "category";
  public  static final String TITLE               = "title";
  public  static final String FILE_NAME           = "file-name";
  public  static final String BODY                = "body";
  public  static final String RATING              = "rating";
  public  static final String PAGES               = "pages";
  public  static final String AUTHOR              = "author";
  public  static final String NAME                = "name";
  public  static final String FIRST_NAME          = "first-name";
  public  static final String LAST_NAME           = "last-name";
  public  static final String WIKIPEDIA_LINK      = "wikipedia-link";
  public  static final String ONE_AUTHOR          = "single-author";
  public  static final String AUTHOR_INFO         = "author-info";
  public  static final String AUTHOR_LINK         = "author-link";
  public  static final String LINK                = "link";
  public  static final String SOURCE              = "source";
  public  static final String SOURCE_TYPE         = "source-type";
  public  static final String TYPE                = "type";
  public  static final String MINOR_TITLE         = "minor-title";
  public  static final String SOURCE_LINK         = "source-link";
  public  static final String PUBLISHER           = "publisher";
  public  static final String CITY                = "city";
  public  static final String ID                  = "identifier";
  public  static final String RIGHTS              = "rights";
  public  static final String YEAR                = "year";
  public  static final String RIGHTS_OWNER        = "owner";
  public  static final String DATE_ADDED          = "date-added";
  
  public static final String      DESCRIPTION = "description";
  public static final String      PATH        = "path";
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
  
  public  final static String NA_STRING           = "N/A";
  
  public  final static int  NUMBER_OF_COLUMNS     = 19;
  
  public  final static int  DELETED               = 0;
  public  final static int  CATEGORY_INDEX        = 1;
  public  final static int  AUTHOR_INDEX          = 2;
  public  final static int  SOURCE_INDEX          = 3;
  public  final static int  RATING_INDEX          = 4;
  public  final static int  SOURCE_TYPE_INDEX     = 5;
  public  final static int  RIGHTS_INDEX          = 6;
  public  final static int  YEAR_INDEX            = 7;
  public  final static int  SOURCE_ID             = 8;
  public  final static int  TITLE_INDEX           = 9;
  public  final static int  BODY_INDEX            = 10;
  public  final static int  RIGHTS_OWNER_INDEX    = 11;
  public  final static int  WEB_PAGE              = 12;
  public  final static int  MINOR_TITLE_INDEX     = 13;
  public  final static int  SOURCE_LINK_INDEX           = 14;
  public  final static int  AUTHOR_LINK_INDEX     = 15;
  public  final static int  DATE_ADDED_INDEX      = 16;
  public  final static int  AUTHOR_INFO_INDEX     = 17;
  public  final static int  ITEM_ID_INDEX         = 18;
  public  final static int  PUBLISHER_INDEX       = 19;
  public  final static int  CITY_INDEX            = 20;
  public  final static int  PAGES_INDEX           = 21;
  public  final static int  NO_FIELD              = 22;
  
  public  final static String[] COLUMN_NAME = {
    "deleted",
    "category",
    "author",
    "source",
    "rating",
    "sourcetype",
    "rights",
    "year",
    "sourceid",
    "title",
    "body",
    "rightsowner",
    "webpage",
    "minortitle",
    "sourcelink",
    "authorlink",
    "dateadded",
    "authorinfo",
    "itemid",
    "publisher",
    "city",
    "pages",
    "nofield"
  };
  
  /**
    Array of strings that can be used for column headings, and also as a list
    of field names that can be used with the TextBlock class. 
   */
  public  final static String[] COLUMN_DISPLAY = {
    "Deleted",
    "Category",
    "Author",
    "Source",
    "Rating",
    "Source Type",
    "Rights",
    "Year",
    "Source ID",
    "Title",
    "Body",
    "Rights Owner",
    "Web Page",
    "Minor Title",
    "Source Link",
    "Author Link",
    "Date Added",
    "Author Information",
    "Item ID",
    "Publisher",
    "City",
    "Pages",
    "No Field",
    "Category1",
    "Category2",
    "Category3",
    "Category4",
    "Category5",
    "Category6",
    "Category7",
    "Category8",
    "Category9",
    "Category10",
    "Description" // Old field name for Body
  };
  
  public  final static String[] COLUMN_BRIEF = {
    "Deleted",
    "Category",
    "Author",
    "Source",
    "Rating",
    "Type",
    "Rights",
    "Year",
    "ID",
    "Title",
    "Body",
    "Owner",
    "Web Page",
    "Minor Title",
    "Source Link",
    "Author Link",
    "Added",
    "Author Info",
    "ID",
    "Publisher",
    "City",
    "Pages",
    "N/A"
  };
  
  public  final static int[] COLUMN_WIDTH = {
    40,   // Deleted
   	120,  // Category
    150,  // Author
    200,  // Source
    70,   // Rating
    100,  // Source Type
    120,  // Rights
    050,  // Year
    100,  // Source ID
    250,  // Title
    300,  // Body
    150,  // Owner
    200,  // Web Page
    200,  // Minor Title
    200,  // Source Link
    200,  // Author Link
    80,   // Date Added
    150,  // Author Info
    50,   // Item ID
    100,  // Publisher
    80,   // City
    30,   // Pages
    0     // No Field
  };
  
  /**
   0 - String
   1 - Boolean
   3 - Integer
   */
  public  final static int[] COLUMN_CLASS_TYPE = {
    1, // Deleted: Check Box
   	0, // Category
    0, // Author
    0, // Source
    3, // ItemRating
    0, // Source Type
    0, // Rights
    0, // Year
    0, // Source ID
    0, // Title
    0, // Body
    0, // Owner
    0, // Web Page
    0, // Minor Title
    0, // Source Link
    0, // Author Link
    0, // Date Added
    0, // Author Info
    3, // Item ID
    0, // Publisher
    0, // City
    0, // Pages
    0  // No Field
  }; 
  
  public  final static int[] COLUMN_DISPLAY_PRIORITY = {
    // (lower numbers are higher priorities for display)
    900, // Deleted: Check Box
   	 40, // Category
     20, // Author
     30, // Source
     60, // Item Rating
    800, // Source Type
    900, // Rights
    700, // Year
    900, // Source ID
     10, // Title
    100, // Body
    900, // Rights Owner
    900, // Web Page
    200, // Minor Title
    900, // Source Link
    900, // Author Link
    300, // Date Added
    400, // Author Info
    900, // Item ID
    900, // Publisher
    900, // City
    900, // Pages
    900  // No Field
  };

  private static RecordDefinition recDef = null;
	
  public  final static int     CATEGORY_MAX          = 5;
  
  public final static String   DATE_FORMAT_STRING_DISPLAY = "EEE dd-MMM-yyyy hh:mm:ss aa zzz";
  public final static String   DATE_FORMAT_STRING_INT = "yyyy-MM-dd'T'HH:mm:ssZZZ";
  
  public final static DateFormat DATE_FORMAT_DISPLAY 
    = new SimpleDateFormat (DATE_FORMAT_STRING_DISPLAY);
  
  public final static DateFormat DATE_FORMAT_INT 
    = new SimpleDateFormat (DATE_FORMAT_STRING_INT);
  
  public final static String[] STOCK_CATEGORY = {
    "America", "America",
    "Americans", "America",
    "Apple", "Apple",
    "art", "art",
    "Bible", "Bible",
    "change", "change",
    "children", "children",
    "Christ", "Christianity",
    "Christianity", "Christianity",
    "Christians", "Christianity",
    "decisions", "decisions",
    "democracy", "democracy",
    "democratic", "democracy",
    "design", "design",
    "education", "education",
    "environment", "environment",
    "ethics", "ethics",
    "equality", "equality",
    "freedom", "freedom",
    "genius", "genius",
    "God", "God",
    "god", "God",
    "gods", "God",
    "Gospel", "Bible",
    "government", "government",
    "independence", "independence",
    "Jesus", "Christianity",
    "learning", "learning",
    "liberty", "liberty",
    "Mac", "Macintosh",
    "Macintosh", "Macintosh",
    "Microsoft", "Microsoft",
    "planning", "planning",
    "problem", "problems",
    "tax", "tax",
    "taxation", "tax",
    "taxes", "tax",
    "thinking", "thought",
    "thought", "thought",
    "work", "work"
  };
  
  public final static String[] RATING_LABEL = {
    "0 N/A",
    "High",
    "Medium-High",
    "Medium",
    "Medium-Low",
    "Low"
  };
  
  private     boolean             ok = true;
  
  private     CollectionWindow    collectionWindow = null;
  
  private     WisdomDiskStore     store = null;
  
  private     WisdomItems         items = null;
  
  private     File                wisdomFile = null;
  
  private     boolean             importing = false;
  
  private     WisdomItem          wisdom = null;
  private     Author              author = null;
  
  // Depth within element structure, where 0 = no elements, 
  // 1 is within first, etc.
  private     int                 elementLevel = 0;
  
  private     boolean             withinQuotation = false;
  
  // tags within the body should be passed along as is, for the most part
  private     boolean             withinBody = false;
  
  private     boolean             authorFound = false;
  private     boolean             sourceTitleFound = false;
  
  // Array of character strings being built as characters are received from parser
  private     ArrayList           chars = null;
  
  // Array of element names
  private     ArrayList           elementNames = null;
  
  private     char                categorySeparator 
                                    = Category.PREFERRED_CATEGORY_SEPARATOR;
  
  public WisdomIO() {
    
  }
  
  public void setCollectionWindow(CollectionWindow collectionWindow) {
    this.collectionWindow = collectionWindow;
  }
  
  public void setItems(WisdomItems items) {
    this.items = items;
  }
  
  public void setStore(WisdomDiskStore store) {
    this.store = store;
  }
  
  public void setSource (File file) {
    wisdomFile = file;
  }
  
  public void setImporting (boolean importing) {
    this.importing = importing;
  }
  
  /**
   Indicate the start of a new document. 
   */
  public void startDocument() {
    chars = new ArrayList();
    elementNames = new ArrayList();
    elementLevel = 0;
    StringBuffer str = new StringBuffer();
    storeField (elementLevel, str, "");
    withinBody = false;
  }
  
  /**
   Indicate the start of a new element (aka field) within a document.
  
   @param name The name of the element. 
  
   @param isAttribute Attributes are treated as a special type of attribute,
                      so attributes are identified with this flag. 
   */
  public void startElement (String name, boolean isAttribute) {
    
    if (name.equals (CATEGORY)) {
      categorySeparator = Category.PREFERRED_CATEGORY_SEPARATOR;
    }
    
    if (name.equals (WISDOM)
        || (name.equals (QUOTATIONS))
        || (name.equals (CHANNEL))) {
      elementLevel = 1;
      withinBody = false;
      withinQuotation = true;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str, name);
    }
    else
    if (name.equals (ITEM)
        || name.equals (QUOTATION)
        || name.equals (QUOTE)) {
      elementLevel = 2;
      withinBody = false;
      authorFound = false;
      sourceTitleFound = false;
      wisdom = new WisdomItem ();
      if (wisdomFile != null && store != null) {
        wisdom.setFileName(store, wisdomFile);
      }
      author = null;
      categorySeparator = Category.PREFERRED_CATEGORY_SEPARATOR;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str, name);
      withinQuotation = (name.equals(QUOTATION));
      if (name.equals (QUOTATION)) {
        withinBody = true;
      }
    }
    else
    if (withinQuotation
        && (name.equals (AUTHOR)
            || name.equals (SOURCE)
            || name.equals (NOTE))) {
      withinBody = false;
      wisdom.bodyClose();
      elementLevel = 3;
      StringBuffer str = new StringBuffer();
      storeField (elementLevel, str, name);
    }
    else
    if (elementLevel >= 0) {
      if (withinBody) {
        if (wisdom != null) {
          if (name.equals (PRE) 
              || name.equals (CONTENT)) {
            // skip tags that we don't want to be treated as html
          } else {
            wisdom.bodyStartElement (name, isAttribute);
          }
        } // end if wisdom exists
      }
      else
      if (name.equals (BODY)
          || name.equals (CONTENT)
          || name.equals (PARAGRAPH)) {
        elementLevel++;
        StringBuffer str = new StringBuffer();
        storeField (elementLevel, str, name);
        withinBody = true;
      } else {
        elementLevel++;
        StringBuffer str = new StringBuffer();
        storeField (elementLevel, str, name);
      }
    }
    
  }
  
  public void characters (char [] ch, int start, int length) {
    
    StringBuffer xmlchars = new StringBuffer();
    xmlchars.append (ch, start, length);
    data (xmlchars.toString(), false);
  
  } // end method characters
  
  public void data (String moreData) {
    data (moreData, false);
  }
  
  /**
   Pass a data value contained within the last element started. This may be a 
   complete data value, or it may be incomplete, and only a part of the entire 
   data value. It is assumed that the handler will concatenate multiple data 
   occurrences within an element as needed. 
  
   @param str A string of data contained within the last element started.
   @param useMarkdown Indicates whether to use markdown parser. 
   */
  public void data (String moreData, boolean useMarkdown) {
    
   if (withinBody) {
      if (wisdom != null) {
        wisdom.getBodyAsMarkupElement().append(moreData, useMarkdown);
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
        for (int i = 0; i < moreData.length(); i++) {
          c = moreData.charAt(i);
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
    
  }
  
  /**
   Indicate the end of the named element. It is assumed that the handler will
   close lower-level elements as needed.
  
   @param name The name of the previously started element to be closed. 
   */
  public void endElement (String name) {
    
    if (elementLevel >= 0) {
      StringBuffer str = (StringBuffer)chars.get (elementLevel);
      String parent = getElementName (elementLevel - 1);
      String grandParent= getElementName (elementLevel - 2);

      if (str.length() > 0
          && str.charAt (str.length() - 1) == ' ') {
        str.deleteCharAt (str.length() - 1);
      }
      if (name.equals (BODY)) {
        // StringBuffer str = new StringBuffer();
        // storeField (elementLevel, str);
        // wisdom.setBody (str.toString());
        withinBody = false;
        wisdom.bodyClose();
        elementLevel--;
      } 
      else
      if (grandParent.equals (CHANNEL)
            && name.equals (DESCRIPTION)) {
        // StringBuffer str = new StringBuffer();
        // storeField (elementLevel, str);
        wisdom.setBody (str.toString());
        withinBody = false;
        elementLevel--;
      } 
      else
      if (grandParent.equals (CHANNEL)
          && name.equals (TITLE)) {
        supplyAuthor (str.toString());
        withinBody = false;
        elementLevel--;
      }
      else
      if (name.equals (ITEM_ID)) {
        wisdom.setItemID (str.toString());
        elementLevel--;
      }
      else
      if (name.equals (CONTENT)) {
        // wisdom.setBody (str.toString());
        withinBody = false;
        wisdom.bodyClose();
        elementLevel--;
      }
      /* else
      if (name.equals (PARAGRAPH) 
          && withinQuotation) {
        // wisdom.setBody (str.toString());
        withinBody = false;
        elementLevel--;
      } */
      else 
      if (withinBody) {
        if (wisdom != null) {
          if (name.equals (PRE)) {
            // skip it
          } else {
            wisdom.bodyEndElement (name);
          }
        } // end if wisdom exists
      }
      else
      if (name.equals (ITEM)) {
        if (author != null) {
          wisdom.setAuthor (author);
        }
        wisdom.deriveAllFields();
        int addedAt = items.add (wisdom, importing, (! importing), false);
        if (addedAt < 0) {
          ok = false;
        } 
        elementLevel = -1;
      }
      else
      if (name.equals (QUOTE)) {
        wisdom.setBody (str.toString());
        if (author != null) {
          wisdom.setAuthor (author);
        }
        int addedAt = items.add (wisdom, importing, (! importing), false);
        if (addedAt < 0) {
          ok = false;
        } 
        elementLevel = -1; 
      }
      else
      if (name.equals (QUOTATION)) {
         if (author != null) {
          wisdom.setAuthor (author);
        }
        int addedAt = items.add (wisdom, importing, (! importing), false);
        if (addedAt < 0) {
          ok = false;
        } 
        elementLevel = -1; 
        withinQuotation = false;
      } else {
        if (str.length() > 0) {
          if (name.equals (TITLE)
              && elementLevel == 2
              && collectionWindow.getCollectionTitle().equals("")) {
            collectionWindow.setCollectionTitle (str.toString());
          }
          else
          if (name.equals (LINK)
              && elementLevel == 2
              && collectionWindow.getLink().equals("")) {
            collectionWindow.setLink (str.toString());
          }
          else
          if (name.equals (PATH)
              && elementLevel == 2
              && collectionWindow.getPath().equals("")) {
            collectionWindow.setPath (str.toString());
          }
          else
          if (name.equals (DESCRIPTION)
              && elementLevel == 2
              && collectionWindow.getDescription().equals("")) {
            collectionWindow.setDescription (str.toString());
          }
          else
          if (name.equals (EDITOR)
              && elementLevel == 2
              && collectionWindow.getEditor().equals("")) {
            collectionWindow.setEditor (str.toString());
          }
          else
          if (name.equals (VERSION)
              && elementLevel == 2
              && collectionWindow.getCollectionVersion().equals("")) {
            collectionWindow.setCollectionVersion (str.toString());
          }
          else
          if (name.equals (ASSUME_QUOTATION)
              && elementLevel == 2) {
            collectionWindow.setAllQuotes (str.toString());
          }
          else
          if (name.equals(BACKUP_FOLDER)
              && elementLevel == 2
              && collectionWindow.getBackupFolder().equals("")) {
            collectionWindow.setBackupFolder(str.toString());
          }
          else
          if (name.equals(STORAGE_FORMAT)
              && elementLevel == 2) {
            collectionWindow.setStorageFormat(str.toString());
          }
          else
          if (name.equals(ORGANIZE_WITHIN_FOLDERS)
              && elementLevel == 2) {
            collectionWindow.setOrganizeWithinFolders(str.toString());
          }
          else
          if (name.equals(FILE_NAMING)
              && elementLevel == 2) {
            collectionWindow.setFileNamingByTitle(str.toString());
          }
          else  
          if (name.equals (CATEGORY + "1")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (name.equals (CATEGORY + "2")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (name.equals (CATEGORY + "3")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (name.equals (CATEGORY + "4")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (name.equals (CATEGORY + "5")) {
            wisdom.appendCategoryWord (categorySeparator, str.toString());
            categorySeparator = Category.PREFERRED_LEVEL_SEPARATOR;
          }
          else
          if (name.equals (TITLE) 
              && elementLevel == 3
              && (! withinQuotation)) {
            wisdom.setTitle (str.toString());
          }
          else
          if (name.equals (NAME) && elementLevel == 4) {
            supplyAuthor (str.toString());
          } //end if end of author name element
          else
          if (name.equals (TYPE) || name.equals (SOURCE_TYPE)) {
            wisdom.setSourceType (str.toString());
          }
          else
          if (name.equals (TITLE) 
              && elementLevel == 4
              && (! withinQuotation)) {
            supplySourceTitle(str.toString());
          }
          else
          if (name.equals (MINOR_TITLE)) {
            wisdom.setMinorTitle (str.toString());
          }
          else
          if (name.equals (LINK) && parent.equals (AUTHOR)) {
            wisdom.setAuthorLink (str.toString());
            if (author != null) {
              author.setLink (str.toString());
            }
          }
          else
          if (name.equals (LINK) && elementLevel == 4) {
            wisdom.setSourceLink (str.toString());
          }
          else
          if (name.equals (ID)) {
            wisdom.setSourceID (str.toString());
          }
          else
          if (name.equals (RATING)) {
            wisdom.setRating (str.toString());
          }
          else
          if (name.equals (PAGES)) {
            wisdom.setPages (str.toString());
          }
          else
          if (name.equals (RIGHTS)) {
            wisdom.setRights (str.toString());
          }
          else
          if (name.equals (YEAR)) {
            wisdom.setYear (str.toString());
          }
          else
          if (name.equals (PUBLISHER)) {
            wisdom.setPublisher (str.toString());
          }
          else
          if (name.equals (CITY)) {
            wisdom.setCity (str.toString());
          }
          else
          if (name.equals (RIGHTS_OWNER)) {
            wisdom.setRightsOwner (str.toString());
          } 
          else
          if (name.equals (DATE_ADDED)) {
            wisdom.setDateAdded (str.toString());
          }
          else
          if (name.equals (AUTHOR_INFO)) {
            wisdom.setAuthorInfo (str.toString());
          }
          else
          if (parent.equals (QUOTE) && name.equals (BY)) {
            supplyAuthor (str.toString());
          }
          else
          if (name.equals(FILE_NAME)) {
            if (parent.equals (ITEM)) {
              wisdom.setTitleFileName (str.toString());
            }
            else
            if (parent.equals (AUTHOR)) {
              wisdom.setAuthorFileName (str.toString());
            }
            else
            if (parent.equals (SOURCE)) {
              wisdom.setSourceFileName (str.toString());
            } 
          }
          else
          if (withinQuotation) {
            if (name.equals (SOURCE)) {
              if (authorFound) {
                supplySourceTitle (str.toString());
              } else {
                supplyAuthor (str.toString());
              }
            }
            else
            if (name.equals (AUTHOR)) {
              supplyAuthor (str.toString());
            }
            else
            if (name.equals (CITE)
                && (! sourceTitleFound)) {
              supplySourceTitle (str.toString());
            } // end if ending cite element
          } // end if within a quotation element
        } // end if we have non-blank content
      elementLevel--;
      } // end if we have an ending element
    } // end if we are within a wisdom element
    
  }
  
  /**
   Indicates the end of a document.
   */
  public void endDocument() {
    
  }
  
  private void supplyAuthor (String text) {
    if (author == null) {
      author = new Author (text);
    } else {
      author.setCompleteName (text);
    }
    authorFound = true;
  }
  
  private void supplySourceTitle (String text) {
    wisdom.setSourceTitle (text);
    sourceTitleFound = true;
  }
  
  private void storeField (int level, StringBuffer str, String elementName) {
    
    if (chars.size() > level) {
      chars.set (level, str);
    } else {
      chars.add (level, str);
    }
    
    if (elementNames.size() > level) {
      elementNames.set (level, elementName);
    } else {
      elementNames.add (level, elementName);
    }
    
  } // end method
  
  private String getElementName (int i) {
    if (i < 0 || i >= elementNames.size()) {
      return "";
    } else {
      return (String)elementNames.get (i);
    }
  }
  
}
