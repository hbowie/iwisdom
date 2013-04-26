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
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.iwisdom.disk.*;
  import java.io.*;
  import java.text.*;
  import java.util.*;
  import org.xml.sax.*;

/**
 *   An object representing one iWisdom item. 
 */

public class WisdomItem {
  
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
  
  /** Has this item been deleted? */
  private     boolean         deleted         = false;
  
  /** Item Number of this item within a ToDoItems collection. */
  private     int             itemNumber      = -1;
  
  /** Item ID of this item within a particular collection. */
  private     int             itemID          = 0;

	/** General category into which this item falls */
	private    	Category        category = new Category();
  
  /** The author or originator of this piece of wisdom. */
  private     Author          author = new Author();
  
  /** The source (book, etc.) for this piece of wisdom. */
  private     WisdomSource    source = new WisdomSource();

	/** The type of item (action, agenda, etc.) */
	private    	int          		type = 1;

	/** The rating of the item, where 1 is highest and 5 is lowest */
	private    	int          		rating = 3;
  
  public final static String[] RATING_LABEL = {
    "0 N/A",
    "High",
    "Medium-High",
    "Medium",
    "Medium-Low",
    "Low"
  };

	/** True if this is a private item */
	private    	boolean         privateFlag = false;

	/** The title of this item */
	private    	String          titleText = "";
  
  /** Any suffix added to the end of this title to preserve uniqueness */
  private     int             titleSuffix = 0;
  
  /** The file name used to identify this particular piece of wisdom. */
  private     String          titleFileName = "untitled";
  
	/** The full text of this piece of wisdom */
	private    	MarkupElement   body = new MarkupElement();
  
  private     String          signature = "";
  
  public final static int     SIGNATURE_LEADING_LETTERS_PER_WORD = 2;
  public final static int     SIGNATURE_SUFFICIENT_LENGTH = 10;
  
  private     Date            dateAdded = new Date();
  
  private     boolean         quotation = false;
  
  private     String          pages = "";
  
  // Path from Collection Folder to file
  private     String          filePath = "";
  
  // File fileName (without path or extension)
  private     String          fileName = "";
  
  // File extension
  private     String          fileExt = "";
  
  /**
   * Pointer to iWisdomCommon, to use for alerts. 
   */
  private     iWisdomCommon td;

	/**
	   Constructor with minimal arguments.
	 */
	public WisdomItem () {

	}
  
	/**
	   Constructor with data record argument.
 
     @param wisdomRec   DataRecord to be used to populate this item.
	  */
	public WisdomItem (DataRecord wisdomRec) {
		setMultiple (wisdomRec);
	}
  
  public void setCommon (iWisdomCommon td) {
    this.td = td;
  }
  
  /**
    Returns a record definition for a Wisdom item,
    in com.powersurgepub.psdata.RecordDefinition format.
   
    @return A record format definition for a Wisdom item.
   */
  public static RecordDefinition getRecDef() {
    if (recDef == null) {
      recDef = new RecordDefinition();
      recDef.addColumn (COLUMN_NAME [CATEGORY_INDEX]);
      recDef.addColumn (COLUMN_NAME [AUTHOR_INDEX]);
      recDef.addColumn (COLUMN_NAME [SOURCE_INDEX]);
      recDef.addColumn (COLUMN_NAME [RATING_INDEX]);
      recDef.addColumn (COLUMN_NAME [SOURCE_TYPE_INDEX]);
      recDef.addColumn (COLUMN_NAME [RIGHTS_INDEX]);
      recDef.addColumn (COLUMN_NAME [YEAR_INDEX]);
      recDef.addColumn (COLUMN_NAME [SOURCE_ID]);
      recDef.addColumn (COLUMN_NAME [TITLE_INDEX]);
      recDef.addColumn (COLUMN_NAME [BODY_INDEX]);
      recDef.addColumn (COLUMN_NAME [RIGHTS_OWNER_INDEX]);
      recDef.addColumn (COLUMN_NAME [WEB_PAGE]);
      recDef.addColumn (COLUMN_NAME [MINOR_TITLE_INDEX]);
      recDef.addColumn (COLUMN_NAME [SOURCE_LINK_INDEX]);
      recDef.addColumn (COLUMN_NAME [AUTHOR_LINK_INDEX]);
      recDef.addColumn (COLUMN_NAME [DATE_ADDED_INDEX]);
      recDef.addColumn (COLUMN_NAME [AUTHOR_INFO_INDEX]);
      recDef.addColumn (COLUMN_NAME [ITEM_ID_INDEX]);
      recDef.addColumn (COLUMN_NAME [PUBLISHER_INDEX]);
      recDef.addColumn (COLUMN_NAME [CITY_INDEX]);
      recDef.addColumn (COLUMN_NAME [PAGES_INDEX]);
      recDef.addColumn ("authorfilename");
      recDef.addColumn ("authorwikimediapage");
      recDef.addColumn ("authorlastnamefirst");
      recDef.addColumn ("authorlastnamestrong");
      recDef.addColumn ("linkedtags");
      recDef.addColumn ("linkedtags2");
      recDef.addColumn ("sourceline");
      recDef.addColumn ("rightsline");
    }
    return recDef;
  }

  /**
    Sets multiple fields based on contents of passed record.
  
    @param  wisdomRec  A data record containing multiple fields.
   */
  public void setMultiple (DataRecord wisdomRec) {
    wisdomRec.startWithFirstField();
    DataField nextField;
    String name;
    char sepChar = Category.PREFERRED_CATEGORY_SEPARATOR;
    while (wisdomRec.hasMoreFields()) {
      nextField = wisdomRec.nextField();
      name = nextField.getCommonFormOfName();
      if (name.equals("unknown")) {
        // do nothing
      }
      else
      if (name.equals (COLUMN_NAME [DELETED])) {
        setDeleted (nextField.getDataBoolean());
      }
      else
      if (name.equals (COLUMN_NAME [ITEM_ID_INDEX])) {
        setItemID (nextField.getDataInteger());
      }
      if (name.equals (COLUMN_NAME [CATEGORY_INDEX]) || name.equals ("categories")) {
        setCategory (nextField.getData());
        sepChar = Category.PREFERRED_CATEGORY_SEPARATOR;
      }
      else
      if (name.startsWith (COLUMN_NAME [CATEGORY_INDEX])) { 
        appendCategoryWord (sepChar, nextField.getData());
        sepChar = Category.PREFERRED_LEVEL_SEPARATOR;
      }
      else
      if (name.equals (COLUMN_NAME [RATING_INDEX]) || name.equals ("urlpriority")) {
        setRating (nextField.getDataInteger());
      }
      else
      if (name.equals (COLUMN_NAME [AUTHOR_INDEX]) || name.equals ("completename")) {
        setAuthor (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [AUTHOR_INFO_INDEX])) {
        setAuthorInfo (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [SOURCE_INDEX]) || name.equals ("booktitle")) {
        setSource (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [SOURCE_TYPE_INDEX])) {
        setSourceType (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [RIGHTS_INDEX])) {
        setRights (nextField.getData());
      }
      else
      if (name.equals ("copyrightyear") 
          || name.equals (COLUMN_NAME [YEAR_INDEX])) {
        setYear (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [SOURCE_ID])
          || startOfNameEquals (name, "isbn")) {
        setSourceID (nextField.getData());
      }
      else
      if (name.equals ("action") 
          || name.equals ("companyanddepartment")
          || name.equals (COLUMN_NAME [TITLE_INDEX])) {
        setTitle (nextField.getData());
      }
      else
      if (name.equals ("description")
          || name.equals ("details")
          || name.equals ("body")
          || name.equals ("phrase")
          || startOfNameEquals (name, "problem")) {
        body.append (nextField.getData(), true);
        bodyClose();
        ensureTitle();
        ensureSignature();
      }
      else
      if (name.equals ("afterbody")) {
        body.append (nextField.getData(), true);
      }
      else
      if (name.equals (COLUMN_NAME [RIGHTS_OWNER_INDEX])
          || name.equals ("copyrightby")) {
        setRightsOwner (nextField.getData());
        if (getRightsOwner().length() > 0
            && getRights().equals ("")) {
          setRights ("Copyright (c)");
        }
      }
      else
      if (name.equals (COLUMN_NAME [MINOR_TITLE_INDEX])) {
        setMinorTitle (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [SOURCE_LINK_INDEX])) {
        setSourceLink (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [AUTHOR_LINK_INDEX])) {
        setAuthorLink (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [DATE_ADDED_INDEX])) {
        setDateAdded (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [PUBLISHER_INDEX])) {
        setPublisher (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [CITY_INDEX])) {
        setCity (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [PAGES_INDEX])) {
        setPages (nextField.getData());
      }
    
    } // end while
  } // end method
  
  /**
   Merge information from a second wisdom item into this one. The two items
   are assumed to be attempting to represent the same piece of wisdom. 
   */
  public String merge (WisdomItem item2) {
    
    CommaList updatedFields = new CommaList();
    
    // Only use the new item's category if this one doesn't yet have any
    if (item2.getCategoryWords() > 0
        && getCategoryWords() == 0) {
      System.out.println ("WisdomItem.merge changing category from \""
          + category.toString()
          + "\" to \""
          + item2.getCategory().toString()
          + "\"");
      setCategory (item2.getCategory());
      updatedFields.append (COLUMN_DISPLAY [CATEGORY_INDEX]);
    }
    
    // If length of author name is greater than the one we have now, use it
    Author author1 = getAuthor();
    int author1Length = author1.getCompleteName().length();
    Author author2 = item2.getAuthor();
    int author2Length = author2.getCompleteName().length();
    if (author2Length > author1Length) {
      setAuthor (author2);
      updatedFields.append (COLUMN_DISPLAY [AUTHOR_INDEX]);
      if (! item2.getAuthorInfo().equals (getAuthorInfo())) {
        setAuthorInfo (item2.getAuthorInfo());
        updatedFields.append (COLUMN_DISPLAY [AUTHOR_INFO_INDEX]);
      }
    }
    
    // If length of source title is greater, then use it
    if (item2.getSourceTitle().length() > getSourceTitle().length()) {
      setSourceTitle (item2.getSourceTitle());
      updatedFields.append (COLUMN_DISPLAY [SOURCE_INDEX]);
    }
    
    // If source type is specified, then use it
    if (item2.getSourceType() > WisdomSource.UNKNOWN_TYPE
        && item2.getSourceType() != getSourceType()) {
      setSourceType (item2.getSourceType());
      updatedFields.append (COLUMN_DISPLAY [SOURCE_TYPE_INDEX]);
    }
    
    // If length of source rights is greater, then use it
    if (item2.getRights().length() > getRights().length()) {
      setRights (item2.getRights());
      updatedFields.append (COLUMN_DISPLAY [RIGHTS_INDEX]);
    }
    
    // If length of year is greater, then use it
    if (item2.getYear().length() > getYear().length()) {
      setYear (item2.getYear());
      updatedFields.append (COLUMN_DISPLAY [YEAR_INDEX]);
    }
    
    // If length of publisher is greater, then use it
    if (item2.getPublisher().length() > getPublisher().length()) {
      setPublisher (item2.getPublisher());
      updatedFields.append (COLUMN_DISPLAY [PUBLISHER_INDEX]);
    }
    
    // If length of city is greater, then use it
    if (item2.getCity().length() > getCity().length()) {
      setCity (item2.getCity());
      updatedFields.append (COLUMN_DISPLAY [CITY_INDEX]);
    }
   
    // If length of pages is greater, then use it
    if (item2.getPages().length() > getPages().length()) {
      setPages (item2.getPages());
      updatedFields.append (COLUMN_DISPLAY [PAGES_INDEX]);
    }
    
    // If length of Source ID is greater, then use it
    if (item2.getSourceID().length() > getSourceID().length()) {
      setSourceID (item2.getSourceID());
      updatedFields.append (COLUMN_DISPLAY [SOURCE_ID]);
    }
    
    // If length of body is greater, then use it
    if (item2.getBody().length() > getBody().length()) {
      setBody (item2.getBody());
      updatedFields.append (COLUMN_DISPLAY [BODY_INDEX]);
    }
    
    // If length of rights owner is greater, then use it
    if (item2.getRightsOwner().length() > getRightsOwner().length()) {
      setRightsOwner (item2.getRightsOwner());
      updatedFields.append (COLUMN_DISPLAY [RIGHTS_OWNER_INDEX]);
    }
    
    // If length of minor title is greater, then use it
    if (item2.getMinorTitle().length() > getMinorTitle().length()) {
      setMinorTitle (item2.getMinorTitle());
      updatedFields.append (COLUMN_DISPLAY [MINOR_TITLE_INDEX]);
    }
    
    // If length of source link greater, then use it
    if (item2.getSourceLink().length() > getSourceLink().length()) {
      setSourceLink (item2.getSourceLink());
      updatedFields.append (COLUMN_DISPLAY [SOURCE_LINK_INDEX]);
    }
    
    // If length of author link greater, then use it
    if (item2.getAuthorLink().length() > getAuthorLink().length()) {
      setAuthorLink (item2.getAuthorLink());
      updatedFields.append (COLUMN_DISPLAY [AUTHOR_LINK_INDEX]);
    }
    
    deriveAllFields();
    
    return updatedFields.toString();
  }
  
  /**
    Sets multiple fields based on contents of passed text block.
   
    @param  text  A string formatted as a text block.
    @return       Number of fields found for the next record in the block.
   */
  public int setFromTextBlock (String text) {
    TextBlock block = new TextBlock (text);
    return setFromTextBlock (block);
  }

  /**
    Sets multiple fields based on contents of passed text block.
   
    @param  block A formatted text block.
    @return       Number of fields found for the next record in the block.
   */
  public int setFromTextBlock (TextBlock block) {
    DateFormat fmt = new SimpleDateFormat ("MM/dd/yyyy"); 
    int numberOfFields = 0;
    block.startBlockIn (OBJECT_NAME, COLUMN_DISPLAY);
    String name;
    String field;
    boolean anotherField = false;
    char sepChar = Category.PREFERRED_CATEGORY_SEPARATOR;
    do {
      anotherField = block.findNextField();
      if (anotherField) {
        numberOfFields++;
        name = block.getNextLabel();
        field = block.getNextField();
        if (name.equals (COLUMN_DISPLAY [DELETED])) {
          // setDeleted (nextField.getDataBoolean());
        }
        else
        if (name.equals (COLUMN_DISPLAY [ITEM_ID_INDEX])) {
          setItemID (field);
        }
        if (name.equals (COLUMN_DISPLAY [CATEGORY_INDEX])) {
          setCategory (field);
          sepChar = Category.PREFERRED_CATEGORY_SEPARATOR;
        }
        else
        if (name.startsWith (COLUMN_DISPLAY [CATEGORY_INDEX])) {
          appendCategoryWord (sepChar, field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [RATING_INDEX])) {
          setRating (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [AUTHOR_INDEX])) {
          setAuthor (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [AUTHOR_INFO_INDEX])) {
          setAuthorInfo (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [SOURCE_INDEX])) {
          setSource (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [SOURCE_TYPE_INDEX])) {
          setSourceType (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [RIGHTS_INDEX])) {
          setRights (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [YEAR_INDEX])) {
          setYear (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [SOURCE_ID])) {
          setSourceID (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [TITLE_INDEX])) {
          setTitle (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [BODY_INDEX])
            || name.equals ("Description")) {
          setBody (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [RIGHTS_OWNER_INDEX])) {
          setRightsOwner (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [MINOR_TITLE_INDEX])) {
          setMinorTitle (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [SOURCE_LINK_INDEX])) {
          setSourceLink (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [AUTHOR_LINK_INDEX])) {
          setAuthorLink (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [PUBLISHER_INDEX])) {
          setPublisher (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [CITY_INDEX])) {
          setCity (field);
        }
        else
        if (name.equals (COLUMN_DISPLAY [PAGES_INDEX])) {
          setPages (field);
        }
      } // end if another field
    } while (! block.endOfBlock());
    return numberOfFields;
  } // end method
  
  public int setFromText (String text) {
    int numberOfFields = 0;
    if (text.trim().length() > 0) {
      setBody (text);
      numberOfFields++;
    }
    return numberOfFields;
  }
  
  private boolean startOfNameEquals (String name, String s) {
    if (name.length() < s.length()) {
      return false;
    } 
    else
    if (name.substring (0, s.length()).equals (s)) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
    Return this object, formatted as a DataRecord.
   
    @param recDef Record Definition to be used in building the record. 
    */
  public DataRecord getDataRec (RecordDefinition recDef) {
    return getDataRec (recDef, -1);
  }

  /**
    Return this object, formatted as a DataRecord.

    @param recDef Record Definition to be used in building the record.
    */
  public DataRecord getDataRec (RecordDefinition recDef, int categoryIndex) {
    // Create a date formatter
    DateFormat fmt = new SimpleDateFormat ("MM/dd/yyyy");
    DataRecord nextRec = new DataRecord();
    if (categoryIndex < 0 || categoryIndex >= getCategories()) {
      nextRec.addField (recDef, getCategory().toString());
    } else {
      nextRec.addField (recDef, getCategory(categoryIndex));
    }
    nextRec.addField (recDef, getAuthorCompleteName ());
    nextRec.addField (recDef, getSourceAsString());
    nextRec.addField (recDef, String.valueOf (getRating ()));
    nextRec.addField (recDef, getSourceTypeLabel ());
    nextRec.addField (recDef, getRights ());
    nextRec.addField (recDef, getYear ());
    nextRec.addField (recDef, getSourceID ());
    nextRec.addField (recDef, getTitle ());
    nextRec.addField (recDef, getBody ());
    nextRec.addField (recDef, getRightsOwner ());
    nextRec.addField (recDef, getWebPage ());
    nextRec.addField (recDef, getMinorTitle ());
    nextRec.addField (recDef, getSourceLink());
    nextRec.addField (recDef, getAuthorLink());
    nextRec.addField (recDef, getDateAddedInternal());
    nextRec.addField (recDef, getAuthorInfo());
    nextRec.addField (recDef, String.valueOf (getItemID()));
    nextRec.addField (recDef, getPublisher());
    nextRec.addField (recDef, getCity());
    nextRec.addField (recDef, getPages());
    nextRec.addField (recDef, getAuthorFileName());
    nextRec.addField (recDef, getAuthor().getWikiMediaPage());
    nextRec.addField (recDef, getAuthor().getCompleteNameLastNamesFirst());
    nextRec.addField (recDef, getAuthor().getCompleteName(true));
    if (categoryIndex < 0 || categoryIndex >= getCategories()) {
      nextRec.addField (recDef, getCategory().getLinkedTags("../tags/"));
    } else {
      nextRec.addField (recDef, getCategory().getTagFileName(categoryIndex));
    }
    if (categoryIndex < 0 || categoryIndex >= getCategories()) {
      nextRec.addField (recDef, getCategory().getLinkedTags("../wisdom/tags/"));
    } else {
      nextRec.addField (recDef, getCategory().getTagFileName(categoryIndex));
    }
    nextRec.addField (recDef, getSourceLine());
    nextRec.addField (recDef, getRightsLine());
    return nextRec;
  }

  public void getFortuneFormat (TextLineWriter writer) {
    writer.writeLine("%  ");
    writer.writeLine(this.getBody(MarkupElement.NO_HTML, false));
    StringBuffer authorLine = new StringBuffer("        -- ");
    authorLine.append(this.getAuthorCompleteName());
    if (getAuthorInfo().length() > 0) {
      authorLine.append(", " + getAuthorInfo());
    }
    if (getSourceAsString().length() > 0) {
      authorLine.append(", \"" + getSourceAsString() + "\"");
    }
    if (getMinorTitle().length() > 0) {
      authorLine.append(", \"" + getMinorTitle() + "\"");
    }
    if (getYear().length() > 0) {
      authorLine.append(", " + getYear());
    }
    if (getPages().length() > 0) {
      authorLine.append(", pg " + getPages());
    }
    writer.writeLine(authorLine.toString());
  }
  
  /**
  Return this object, formatted as a block of self-evident text.

  @return String with each important, non-null field in it, with
          each field preceded by the field's label and a colon, and
          followed by a line feed character.
  */
  public String getTextBlock (boolean itemDuplicate) {
    TextBlock block = new TextBlock();
    writeTextBlock (block, itemDuplicate);
    return block.toString();
  }
  
  public boolean writeTextBlock (TextLineWriter writer, boolean itemDuplicate) {
    boolean ok = true;
    TextBlock block = new TextBlock (writer);
    ok = writeTextBlock (block, itemDuplicate);
    return ok;
  }
  
  public boolean writeTextBlock (TextBlock block, boolean itemDuplicate) {
    boolean ok = true;
    block.startBlockOut (OBJECT_NAME);
    // Create a date formatter
    DateFormat fmt = new SimpleDateFormat ("MM/dd/yyyy"); 
    block.addField ("From", System.getProperty ("user.name"));
    block.addField (COLUMN_DISPLAY [CATEGORY_INDEX], category.toString());
    String first50 = StringUtils.purify(body.getFirstFifty()).trim();
    if (! titleText.equals (first50)) {
      block.addField (COLUMN_DISPLAY [TITLE_INDEX], getTitle ());
    }
    block.addField (COLUMN_DISPLAY [RATING_INDEX], String.valueOf (getRating ()));
    if (! author.isBlank()) {
      block.addField (COLUMN_DISPLAY [AUTHOR_INDEX], author.getCompleteName());
      block.addField (COLUMN_DISPLAY [AUTHOR_INFO_INDEX], getAuthorInfo());
      block.addField (COLUMN_DISPLAY [AUTHOR_LINK_INDEX], getAuthorLink());
    }
    if (! source.isBlank()) {
      block.addField (COLUMN_DISPLAY [SOURCE_INDEX], getSourceAsString());
    }
    if (getMinorTitle().length() == 0) {
      block.addField (COLUMN_DISPLAY [MINOR_TITLE_INDEX], getMinorTitle());
    }
    if (getSourceType() != WisdomSource.UNKNOWN_TYPE) {
      block.addField (COLUMN_DISPLAY [SOURCE_TYPE_INDEX], getSourceTypeLabel());
    }
    block.addField (COLUMN_DISPLAY [SOURCE_LINK_INDEX], getSourceLink());
    block.addField (COLUMN_DISPLAY [SOURCE_ID], getSourceID ());
    if (itemDuplicate) {
      block.addField (COLUMN_DISPLAY [BODY_INDEX], 
        "Duplicated Wisdom Item");
    } else {
      block.addField (COLUMN_DISPLAY [BODY_INDEX], 
        getBody(MarkupElement.NEW_HTML, false));
    }
    block.addField (COLUMN_DISPLAY [RIGHTS_INDEX], getRights ());
    block.addField (COLUMN_DISPLAY [YEAR_INDEX], getYear());
    block.addField (COLUMN_DISPLAY [RIGHTS_OWNER_INDEX], getRightsOwner ());
    block.addField (COLUMN_DISPLAY [WEB_PAGE], getWebPage ());
    block.addField (COLUMN_DISPLAY [PUBLISHER_INDEX], getPublisher());
    block.addField (COLUMN_DISPLAY [CITY_INDEX], getCity());
    block.addField (COLUMN_DISPLAY [PAGES_INDEX], getPages());
    block.endBlockOut();
    return ok;
  }
  
  /**
   Return URL to this item on a Collection's Web site. 
   */
  public String getItemLink (CollectionWindow header) {
    StringBuffer webLink = new StringBuffer (header.getLink());
    if (webLink.length() > 0) {
      String path = header.getPath();
      if (path.length() > 0) {
        webLink.append (path);
        webLink.append ("/");
      }
      webLink.append ("html/authors/");
      webLink.append (getAuthorFileName());
      webLink.append ("/");
      webLink.append (getSourceFileName());
      webLink.append ("/");
      webLink.append (getTitleFileName());
      webLink.append (".html");
    }
    return webLink.toString();
  }
  
  /**
    Sets the deleted flag on for this item.
   */
  public void setDeleted () {
    setDeleted (true);
  }
  
  /**
    Sets the deleted flag for this item.
   
    @param deleted True if this record has been deleted by the user.
   */
  public void setDeleted (boolean deleted) {
    this.deleted = deleted;
  }
  
  /**
    Gets the deleted flag for this item.
   
    @return True if this record has been deleted by the user.
   */
  public boolean isDeleted () {
    return deleted;
  }
  
  public void assignCategoryIfBlank () {
    if (category.isBlank()) {
      assignCategory();
    }
  }
  
  private void assignCategory () {
    boolean assigned = false;
    int i = 0;
    int wordStart = 0;
    int wordEnd = 0;
    String text = body.toString();
    while ((! assigned)
        && i < text.length()) {
      while (i < text.length()
          && (! Character.isLetter(text.charAt(i)))) {
        i++;
      }
      wordStart = i;
      while (i < text.length()
          && ((Character.isLetter (text.charAt(i)))
            || text.charAt(i) == '\''
            || text.charAt(i) == '-')) {
        i++;
      }
      wordEnd = i;
      if (wordEnd > wordStart) {
        String word = text.substring (wordStart, wordEnd);
        String wordLower = word.toLowerCase();
        int j = 0;
        while (j < STOCK_CATEGORY.length
            && (! assigned)) {
          if (word.equals (STOCK_CATEGORY[j])
              || wordLower.equals (STOCK_CATEGORY[j])) {
            setCategory (STOCK_CATEGORY[j+1]);
            assigned = true;
          } else {
            j = j + 2;
          }
        } // while more stock categories
      } // if we have a word
    } // while more words
  } // end of method
  
  /**
    Sets the category for this item.
   
    @param  category  A string representing the category 
                      into which this item should be placed.
   */
  public void setCategory (String category) {
    this.category.set (category);
  }
  
  /**
    Sets the category for this item.
   
    @param  category The category into which this item should be placed.
   */
  public void setCategory (Category category) {
    this.category = category;
  }
  
  /**
    Set one category level. Note that this is only allowed if the
    level to be set is a higher number than any previously
    set levels. The bottom line is that this method may be used
    to set one level at a time, but only when the category is initially
    being populated, and only when the levels are set in ascending
    sequence by level number. 
   
    @param  inSubCat Category string at given level.
   
    @param  level    Level at which category is to be set, with
                     zero indicating the first level.
   */
  public void appendCategoryWord (char sepChar, String word) {
    category.appendWord (sepChar, word);
  }
  
  /**
    Returns the category for this item.
   
    @return Category for this item.
   */
  public Category getCategory() {
    return category;
  }
  
  /**
   Return the categories assigned to this item, represented as a string. 
   
   @return String representation of categories assigned to this item.
   */
  public String getCategoryString () {
    return category.toString();
  }

  /**
   Return one of possibly multiple categories stored herein.

   @param categoryIndex The index indicating which category is to be returned.
   @return The String containing the complete category (possibly including
           multiple levels) at the indicated index.
   */
  public String getCategory(int categoryIndex) {
    return category.getCategory(categoryIndex);
  }
  
  /**
   Returns the number of categories assigned to this item.
   
   @return number of categories assigned to this item. 
   */
  public int getCategories () {
    return category.getCategories();
  }
  
  /**
   Return the number of levels that exists for the given category. 
   
   @param categoryIndex An index pointing to the category of interest.
   
   @return The number of levels for the given categoryIndex, if valid,
           otherwise zero.
   */
  public int getCategoryLevels (int categoryIndex) {
    return category.getLevels (categoryIndex);
  }
  
  /**
   Get the word or phrase making up the given level, for the given category
   number, within the entire category string assigned to this item. 
   
   @param categoryIndex Number indicating which category is desired (first is 0).
   @param levelIndex Number indicating which level is desired within given
                     category (first is 0).
   @return Word or phrase making up this category level.
   */
  public String getCategoryLevel (int categoryIndex, int levelIndex) {
    return category.getLevel (categoryIndex, levelIndex);
  }
  
  /**
    Return the number of levels in the category.
   
    @return Number of levels in this category.
   */
  public int getCategoryWords() {
    return category.getWords();
  }
  
  /**
    Returns a particular category level for this item.
   
    @return Category level for this item.
   
    @param  level Level to be obtained, with the first denoted by 0.
   */
  public String getCategoryWord (int word) {
    return category.getWord (word);
  }
  
  public void startCategoryIteration () {
    category.startCategoryIteration();
  }
  
  public boolean hasNextWord () {
    return category.hasNextWord();
  }
  
  public boolean hasNextCategory () {
    return (category.hasNextCategory());
  }
  
  public String nextCategoryWord () {
    return category.nextWord();
  }
  
  public boolean isEndOfCategory () {
    return category.isEndOfCategory();
  }
  
  public String nextCategory () {
    return category.nextCategory();
  }
  
  public void deriveAllFields () {
    deriveAuthorFileName();
    deriveSourceFileName();
    deriveTitleFileName();
  }
  
  /**
     Sets the author for this item.
 
     @param  author An author object.
   */
  public void setAuthor (Author author) {
    this.author = author;
  } // end method
  
  /**
     Sets the author for this item.
 
     @param  author A string containing the complete name of an author.
   */
  public void setAuthor (String author) {
    this.author = new Author (author);
  } // end method
  
  /**
     Gets the due date for this item, formatted as a string.
 
     @return  Author object.

   */
  public Author getAuthor () {
    
    return author;

  } // end method
  
  /**
     Gets the due date for this item, formatted as a string.
 
     @return  Author object.

   */
  public String getAuthorCompleteName () {
    
    return author.getCompleteName();

  } // end method
  
  public String getAuthorWikipediaLink () {
    
    return author.getWikipediaLink ();
    
  }
  
  public void deriveAuthorFileName () {
    if (author == null) {
      author = new Author();
    }
    author.deriveFileName();
  }
  
  public void setAuthorFileName (String authorFileName) {
    if (author == null) {
      author = new Author();
    }
    author.setFileName (authorFileName);
  }
  
  public String getAuthorFileName () {
    if (author == null) {
      return Author.UNKNOWN;
    } else {
      return author.getFileName();
    }
  }
  
  public void setAuthorLink (String authorLink) {
    author.setLink (authorLink);
  }
  
  public String getAuthorLink () {
    return author.getLink();
  }
  
  public String getAnAuthorLink() {
    return author.getALink();
  }
  
  public void setAuthorInfo (String authorInfo) {
    author.setAuthorInfo (authorInfo);
  }
  
  public String getAuthorInfo () {
    return author.getAuthorInfo();
  }
  
  /**
     Sets the source for this item.
 
     @param  source A WisdomSource object.
   */
  public void setSource (WisdomSource source) {
    
    this.source = source;

  } // end method
  
  /**
     Sets the source for this item.
 
     @param  source A string containing the complete name or title of a source.
   */
  public void setSource (String source) {
    
    this.source = new WisdomSource (source);

  } // end method
  
  public void setMinorTitle (String minorTitle) {
    setSourceMinorTitle (minorTitle);
  }
  
  public void setSourceMinorTitle (String minorTitle) {
    source.setMinorTitle (minorTitle);
  }
  
  public String getMinorTitle () {
    return source.getMinorTitle();
  }
  
  public void setPublisher (String publisher) {
    source.setPublisher (publisher);
  }
  
  public String getPublisher () {
    return source.getPublisher();
  }
  
  public void setCity (String city) {
    source.setCity (city);
  }
  
  public String getCity () {
    return source.getCity();
  }
  
  public void setPages (String pages) {
    this.pages = pages;
  }
  
  public String getPages () {
    return pages;
  }
  
  public void setSourceID (String sourceID) {
    source.setID (sourceID);
  }
  
  /**
     Gets the source for this item, formatted as an object.
 
     @return  WisdomSource object.

   */
  public WisdomSource getSource () {
    
    return source;

  } // end method
  
  /**
     Gets the source for this item, formatted as a string.
 
     @return  Name or title of source.

   */
  public String getSourceAsString () {
    
    return source.getName();

  } // end method
  
  public void setSourceFileName (String sourceFileName) {
    if (source == null) {
      source = new WisdomSource ();
    }
    source.setFileName (sourceFileName);
  }
  
  public void deriveSourceFileName () {
    if (source == null) {
      source = new WisdomSource();
    }
    source.deriveFileName();
  }
  
  public String getSourceFileName () {
    if (source == null) {
      return WisdomSource.UNKNOWN;
    } else {
      return source.getFileName();
    }
  }
  
  public String getSourceID () {
    return source.getID();
  }

  /**
     Sets the type of item (action, agenda, etc.).
 
     @param  type The type of item (action, agenda, etc.).
   */
  public void setType (int type) {
    this.type = type;
  }

  /**
     Returns the type of item (action, agenda, etc.).
 
     @return The type of item (action, agenda, etc.).
   */
  public int getType () {
    return type;
  }
  
  /**
     Sets the rating of the item, where 1 is highest and 5 is lowest.
 
     @param  ratingString The rating of the item, 
                            where 1 is highest and 5 is lowest.
   */
  public void setRating (String ratingString) {
    try {
      int p = Integer.parseInt (ratingString);
      setRating (p);
    } catch (NumberFormatException e) {
      boolean found = false;
      int i = 0;
      while ((! found) && (i < RATING_LABEL.length)) {
        found = ratingString.equalsIgnoreCase (RATING_LABEL [i]);
        if (! found) {
          i++;
        }
      }
      if (found) {
        setRating (i);
      } else {
        System.out.println ("Trouble parsing rating " + ratingString);
      }
    }
  }

  /**
     Sets the rating of the item, where 1 is highest and 5 is lowest.
 
     @param  rating The rating of the item, where 1 is highest and 5 is lowest.
   */
  public void setRating (int rating) {
    if (rating >= 1 && rating <= 5) { 
      this.rating = rating;
    }
  }

  /**
     Returns the rating of the item, where 1 is highest and 5 is lowest.
 
     @return The rating of the item, where 1 is highest and 5 is lowest.
   */
  public int getRating () {
    return rating;
  }
  
  /**
    Returns a String with a label for the status value.
   
    @return Status value.
   */
  public String getRatingLabel () {
    return getRatingLabel (rating);
  }
  
  /**
    Returns a String with a label for the rating value.
   
    @return Rating value.
   
    @param  status Rating integer to be converted to a String label.
   */
  public static String getRatingLabel (int rating) {
    if (rating < 1 || rating >= RATING_LABEL.length) {
      return String.valueOf (rating) + "-Out of Range";
    } else {
      return RATING_LABEL [rating];
    }
  }
  
  /**
     Sets the source type code.
 
   */
  public void setSourceType (String sourceType) {
    source.setType (sourceType);
  }

  /**
     Sets Status of item.
 
     @param  status Status of item.
   */
  public void setSourceType (int sourceType) {
    source.setType (sourceType);
  }


  /**
     Returns Status of item.
 
     @return Status of item.
   */
  public int getSourceType () {
    return source.getType();
  }
  
  /**
    Returns a String with a label for the status value.
   
    @return Status value.
   */
  public String getSourceTypeLabel () {
    return source.getTypeLabel ();
  }
  
  /**
    Returns a String with a label for the status value.
   
    @return Status value.
   
    @param  status Status integer to be converted to a String label.
   */
  public static String getSourceTypeLabel (int sourceType) {
    return WisdomSource.getTypeLabel (sourceType);
  }
  
  public void setSourceTitle (String title) {
    source.setTitle (title);
  }
  
  public String getSourceTitle () {
    return source.getTitle();
  }

  public String getSourceLine() {
    return source.getSourceLine(pages);
  }

  public String getRightsLine() {
    return source.getRightsLine();
  }
  
  /**
     Indicates whether item is still pending.
 
     @return True if item is canceled or closed.
   */
  public String getRights () {
    return source.getRights();
  }
  
  /**
     Indicates whether item is still pending.
 
     @return True if item is open or in-work.
   */
  public void setRights (String rights) {
    source.setRights (rights);
  }

  /**
     Sets true if this is a private item.
 
     @param  privateFlag True if this is a private item.
   */
  public void setPrivateFlag (boolean privateFlag) {
    this.privateFlag = privateFlag;
  }

  /**
     Returns true if this is a private item.
 
     @return True if this is a private item.
   */
  public boolean isPrivate () {
    return privateFlag;
  }

  /**
     Sets the year that this item was originally published.
 
     @param  year The year that this item was originally published.
   */
  public void setYear (String year) {
    source.setYear (year);
  }

  /**
     Returns the year that this item was originally published.
 
     @return The year that this item was originally published.
   */
  public String getYear () {
    return source.getYear();
  }
  
  /**
   See if this wisdom item contains the same words as another. Ignore case of
   the letters and any punctuation or html tags. 
   */
  public boolean hasSameWordsAs (WisdomItem item2) {
    boolean same = (signature.equals (item2.getSignature()));
    if (same) {
      same = (getWords().equals (item2.getWords()));
    }
    return same;
  }
  
  public void ensureTitle() {
    if (titleText.length() < 1) {
      setTitle (body.getFirstFifty()); 
    } // end if title is blank
  }
  
  public void ensureSignature () {
    signature = body.getSignature (
        SIGNATURE_LEADING_LETTERS_PER_WORD, 
        SIGNATURE_SUFFICIENT_LENGTH);
  }
  
  public String getSignature () {
    return signature;
  }
  
  /**
   Build a string that contains lower-case representations of all words in all
   text, concatenated without separators. Intended to help identify equality. 
   */
  public String getWords () {
    StringBuffer words = new StringBuffer ();
    body.getWords (words);
    return words.toString();
  }

  /**
     Sets the title of this item.
 
     @param  title The title of this item.
   */
  public void setTitle (String title) {
    String pureTitle = StringUtils.purify(title).trim();
    titleSuffix = 0;
    StringBuffer titleSuffixBuffer = new StringBuffer();
    int i = pureTitle.length() - 1;
    if (pureTitle.length() > 0 && (! Character.isDigit(pureTitle.charAt(0)))) {
      while (i >= 0 && Character.isDigit (pureTitle.charAt(i))) {
        titleSuffixBuffer.insert (0, pureTitle.charAt (i));
        i--;
      }
    }
    if (titleSuffixBuffer.length() > 0) {
      try {
        titleSuffix = Integer.parseInt (titleSuffixBuffer.toString());
        int titleTextLength = pureTitle.length() - titleSuffixBuffer.length();
        this.titleText 
            = pureTitle.substring(0, titleTextLength).trim();
      } catch (NumberFormatException e) {
        this.titleText = pureTitle;
      }
    } else {
      this.titleText = pureTitle;
    }
    deriveTitleFileName();
  }
  
  /**
   Explicitly set the title text without changing the suffix.
   */
  public void setTitleText (String titleText) {
    this.titleText = StringUtils.purify(titleText).trim();
  }

  public void markAsDuplicate() {
    titleText = titleText + " Duplicate";
    deriveTitleFileName();
  }
  
  public void setTitleSuffix (int titleSuffix) {
    this.titleSuffix = titleSuffix;
  }
  
  public void incrementTitleSuffix () {
    this.titleSuffix++;
  }

  /**
     Returns the title of this item.
 
     @return The title of this item, including a possible generated 
             numeric suffix to ensure uniqueness. 
   */
  public String getTitle () {
    if (titleHasSuffix()) {
      return (titleText + " " + String.valueOf (titleSuffix));
    } else {
      return titleText;
    }
  }

  /**
   Does this item have a title? 
  
   @return True if item has some kind of title, false if the title text is 
           missing (which implies this is a null item). 
  */
  public boolean hasTitle() {
    return (titleText.length() > 0);
  }

  /**
   Does the item have a title? If not, consider it to be empty.
   
   @return True if the title length is zero; otherwise false.
   */
  public boolean isEmpty() {
    return (titleText.length() == 0);
  }
  
  /**
   Get the text of the title without any numeric suffix. 
  
   @return The text of the title without any numeric suffix.
  */
  public String getTitleText () {
    return titleText;
  }
  
  public int getTitleSuffix () {
    return titleSuffix;
  }
  
  /**
   Does the item have a title suffix?
  
   @return True if the title suffix is greater than zero, otherwise false.
  */
  public boolean titleHasSuffix () {
    return (titleSuffix > 0);
  }
  
  public void deriveTitleFileName () {
    setTitleFileName (StringUtils.makeFileName (getTitle(), false));
  }
  
  public void setTitleFileName (String titleFileName) {
    this.titleFileName = titleFileName;
  }
  
  public String getTitleFileName () {
    return titleFileName;
  }
  
  public void bodyStartElement (String name, boolean isAttribute) {
    body.startElement (name, isAttribute);
  } // end method 
  
  public void bodyStartElement (
      String namespaceURI,
      String localName,
      String qualifiedName,
      Attributes attributes) {
    body.startElement (
        namespaceURI,
        localName,
        qualifiedName,
        attributes);
  } // end method 
  
  public void bodyCharacters (char [] ch, int start, int length) {
    
    body.characters (ch, start, length);

  } // end method 
  
  public void bodyData (String data) {
    
    body.data(data);
    
  }
  
  public void bodyAppend (String in, boolean useMarkdown) {

    body.append(
        // StringUtils.stupefy(
        in
        // )
        , useMarkdown);

  } // end method append
  
  public void bodyEndElement (String localName) {
    body.endElement(localName);
  }
  
  public void bodyEndElement (
      String namespaceURI,
      String localName,
      String qualifiedName) {
    body.endElement (namespaceURI, localName, qualifiedName);
  } // end method
  
  public void bodyClose () {
    body.close();
    ensureTitle();
    ensureSignature();
  }
  
  /**
     Sets the full body of this item.
 
     @param  body The full body of this item.
   */
  public void setBody (String body) {

    this.body.set (
        // StringUtils.stupefy(
        body
        // )
        , true);
    ensureTitle();
    ensureSignature();
  }
  
  public void addQuotes () {
    MarkupQuoter quoter = new MarkupQuoter();
    body.addQuotes (quoter);
  }
  
  public void removeQuotes () {
    MarkupQuoter quoter = new MarkupQuoter();
    body.removeQuotes (quoter);
  }
  
  public boolean hasBody() {
    return (! body.isEmpty());
  }
  
  public MarkupElement getBodyAsMarkupElement () {
    return body;
  }
  
  public String getBody() {
    return body.toString();
  }
  
  public String getBody (int htmlLevel, boolean htmlEntities) {
    return body.toString (htmlLevel, htmlEntities);
  }
  
  /**
     Returns the full body of this item, but with CR/LFs replacing HTML
     br tags.
 
     @return The full body of this item, with HTML replaced by CR/LFs.
   */
  
  public MarkupElement getBodyAsMarkup() {
    return body;
  }
  
  public void writeBodyMarkup (
      TextLineWriter writer,
      int htmlLevel,
      boolean htmlEntities,
      int startingIndent,
      int indentPerLevel) {
    body.writeMarkupLines (
        writer,
        htmlLevel,
        htmlEntities,
        startingIndent,
        indentPerLevel);
  }
  /**
     Sets owner of the rights to this item.
 
     @param  rightsOwner The owner of the rights to this item.
   */
  public void setRightsOwner (String rightsOwner) {
    source.setRightsOwner (rightsOwner);
  }

  /**
     Returns the outcome for this item.
 
     @return The outcome for this item.
   */
  public String getRightsOwner () {
    return source.getRightsOwner();
  }
  
  public void setSourceLink (String sourceLink) {
    source.setLink (sourceLink);
  }
  
  public String getSourceLink () {
    return source.getLink();
  }

  /**
     Returns the Web page for this item.
 
     @return The Web page for this item.
   */
  public String getWebPage () {
    return source.getLink();
  }
  
  public String getASourceLink () {
    return source.getALink();
  }
  
  /**
     Sets the date added for this item.
 
     @param  date String representation of a date.
   */
  public void setDateAdded (String date) {
    
    StringBuffer str = new StringBuffer (date);
    str.deleteCharAt (date.length() - 3);
    try {
      setDateAdded (DATE_FORMAT_INT.parse (str.toString()));
    } catch (ParseException e) {
      System.out.println ("Cannot parse date added " + date);
    }

  } // end method
  
  /**
     Sets the date added for this item.
 
     @param  fmt  A DateFormat instance to be used to parse the following string.
     @param  date String representation of a date.
   */
  public void setDateAdded (DateFormat fmt, String date) {
    
    try {
      setDateAdded (fmt.parse (date));
    } catch (ParseException e) {
      System.out.println ("Cannot parse date added " + date);
    }

  } // end method
  
  /**
     Sets the due date for this item.
 
     @param  date Date representation of a date.
   */
  public void setDateAdded (Date date) {
    
    dateAdded = date;

  } // end method
  
  /**
     Gets the date added for this item, formatted as a string 
     in display format.
 
     @return  String representation of a date in yyyy/mm/dd format.
   */
  public String getDateAddedDisplay () {
    
    return getDateAdded (DATE_FORMAT_DISPLAY);

  } // end method
  
  /**
     Gets the due date for this item, formatted as a string 
     in yyyy/mm/dd format.
 
     @return  String representation of a date in yyyy/mm/dd format.
   */
  public String getDateAddedInternal () {
    
    StringBuffer str = new StringBuffer (getDateAdded (DATE_FORMAT_INT));
    str.insert ((str.length() - 2), ':');
    return str.toString();

  } // end method
  
  /**
     Gets the due date for this item, formatted as a string.
 
     @return  String representation of a date.
     @param   fmt  A DateFormat instance to be used to format the date as a string.

   */
  public String getDateAdded (DateFormat fmt) {
    
    return fmt.format (dateAdded);

  } // end method
  
  
  /**
     Gets the due date for this item.
 
     @return  date Date representation of a date.
   */
  public Date getDateAdded () {
    
    return dateAdded;

  } // end method
  
  /**
     Sets unique ID for this item.
 
     @param  itemIDStr Unique number identifying this item within 
                       a particular collection.
   */
  public void setItemID (String itemIDStr) {
    try {
      setItemID (Integer.parseInt (itemIDStr));
    } catch (NumberFormatException e) {
      // leave itemID alone if not a valid number
    }
  }
  
  /**
     Sets unique ID for this item.
 
     @param  itemID Unique number identifying this item within 
                    a particular collection.
   */
  public void setItemID (int itemID) {
    this.itemID = itemID;
  }

  /**
     Sets item number that uniquely identifies this item within a collection.
 
     @param  itemNumber Unique, identifying number to be assigned to this item.
   */
  public void setItemNumber (int itemNumber) {
    this.itemNumber = itemNumber;
  }
  
  public String getItemIDLink (CollectionWindow header) {
    StringBuffer webLink = new StringBuffer (header.getLink());
    if (webLink.length() > 0) {
      String path = header.getPath();
      if (path.length() > 0) {
        webLink.append (path);
        webLink.append ("/");
      }
      webLink.append (WisdomXMLIO.ID_FOLDER);
      webLink.append ("/");
      webLink.append (getItemIDString());
      webLink.append (".html");
    }
    return webLink.toString();
  }
  
  public String getItemIDString () {
    return StringUtils.stringFromInt (getItemID(), 4);
  }
  
  /**
     Returns number of this item within a ToDoItems collection.
 
     @return Item Number of this to do item, or -1 if not yet identified. 
   */
  public int getItemID () {
    return itemID;
  }
  
  /**
     Returns number of this item within a ToDoItems collection.
 
     @return Item Number of this to do item, or -1 if not yet identified. 
   */
  public int getItemNumber () {
    return itemNumber;
  }
  
  public void setQuotation (boolean quotation) {
    this.quotation = quotation;
  }
  
  public boolean getQuotation () {
    return quotation;
  }
  
  /**
  Sets the path, file name and file extension for this wisdom item.
  
  @param store  The disk store for the wisdom collection.
  @param file   The file that stores this particular wisdom item on disk.
  */
  public void setFileName (WisdomDiskStore store, File file) {
    if (store != null && store.isAFolder() && file != null) {
      setFileName (store.getFile(), file);
    }
  }
  
  /**
  Sets the path, file name and file extension for this wisdom item.
  
  @param folder The enclosing folder for the wisdom collection.
  @param file   The file that stores this particular wisdom item on disk.
  */
  public void setFileName (File folder, File file) {
    if (folder != null && file != null) {
      String folderStr;
      String fileStr;
      try {
        folderStr = folder.getCanonicalPath();
        fileStr = file.getCanonicalPath();
      } catch (IOException e) {
        folderStr = folder.getAbsolutePath();
        fileStr = file.getAbsolutePath();
      }
      if (fileStr.startsWith(folderStr)) {
        int i = folderStr.length();
        int j = fileStr.lastIndexOf(File.separatorChar);
        if (j < 0) {
          j = i;
        }
        int k = fileStr.lastIndexOf('.');
        int l = fileStr.length();
        if (k < 0) {
          k = l;
        }
        if (j > i) {
          filePath = fileStr.substring(i + 1, j);
        } else {
          filePath = "";
        }
        fileName = fileStr.substring(j + 1, k);
        fileExt = fileStr.substring(k + 1, l);
      } 
    }
  }
  
  public void setFileName(CollectionWindow collectionWindow) {
    
    // Determine folders in which to store item (if any)
    if (collectionWindow.isOrganizeWithinFolders()) {
      filePath = 
          "xml" + File.separator +
          "authors" + File.separator +
          getAuthorFileName() + File.separator + 
          getSourceFileName();
    } else {
      filePath = "";
    }
    
    // Determine file name (minus path and extension)
    if (collectionWindow.isFileNamingByTitle()) {
      fileName = getTitleFileName();
    } else {
      fileName = "w" + getItemIDString();
    }
    
    // Determine file extension
    fileExt = collectionWindow.getStorageFormatExt();
    String storageFormat = collectionWindow.getStorageFormat();
  }
  
  public String getFilePath() {
    return filePath;
  }
  
  public String getFileName() {
    return fileName;
  }
  
  public String getFileExt() {
    return fileExt;
  }
  
  public File getFile(File storeFile) {
    if (fileName == null || fileName.length() == 0) {
      return null;
    } else {
      File folder;
      if (filePath.length() > 0) {
        folder = new File (storeFile, filePath);
      } else {
        folder = storeFile;
      }
      return new File (folder, fileName + "." + fileExt);
    }
  }
  
  public String getFilePathNameAndExt() {
    if (fileName == null) {
      return "";
    }
    else
    if (filePath.length() > 0) {
      return filePath + File.separator + fileName + "." + fileExt;
    } 
    else
    if (fileName.length() > 0) {
      return                             fileName + "." + fileExt;
    } else {
      return "";
    }
  }
  
  /*
  public IDNumber getIDNumber () {
    return id;
  }
  
  public String getID () {
    return id.toString();
  }
  
  public void setID (String id) {
    this.id.setID (id);
  } // end method setID
  
  public int getIDComponentAsInt (int i) {
    return (id.getIDComponentAsInt (i));
  }
  
  public Integer getIDComponentAsInteger(int i) {
    return (id.getIDComponentAsInteger (i));
  }
  
  public int getIDComponentsSize() {
    return (id.getIDComponentsSize());
  }
  */
	
	/**
	   Returns the object in string form.
	  
	   @return object formatted as a string
	 */
	public String toString() {
    return getTitle();
	}
  
  /**
   Write the data from this wisdom item to an external format using 
   a Markup Writer as an output writer. 
  
   @param writer       The markup writer to be used for output. 
   @param folder       The folder to which the item is to be written.
   @param openAndCloseSingleFile If true, then this method will open and 
                                 close the Markup Writer; if false, this
                                 method will simply add the data for this
                                 wisdom item to the output stream. 
   @param verbose      True if all possible fields are to be written, false
                       if a more concise (but still complete) representation
                       is desired. 
   @return True if everything went OK. 
   */
  public boolean writeMarkup(
      MarkupWriter writer, 
      File folder, 
      boolean openAndCloseSingleFile,
      boolean verbose) {
    
    boolean ok = true;
    
    if (openAndCloseSingleFile) {
      writer.setFile(getFile(folder));
      ok = writer.openForOutput();
      ok = writer.writeComment(
        "Generated by iWisdom " +
        Home.getShared().getProgramVersion() + 
        " available from PowerSurgePub.com");
      ok = writer.start(WISDOM);
    }
    
    ok = writer.start (ITEM);

    ok = writer.write (ITEM_ID, String.valueOf (getItemID()));

    if (verbose) {
      for ( int categoryIndex = 0;
            categoryIndex < getCategories();
            categoryIndex++) {
        ok = writer.start (CATEGORY);
        for ( int levelIndex = 0; 
              levelIndex < getCategoryLevels(categoryIndex); 
              levelIndex++) {
          ok = writer.start (CATEGORY + String.valueOf (levelIndex + 1));
          ok = writer.write (getCategoryLevel (categoryIndex, levelIndex));
          ok = writer.end (CATEGORY + String.valueOf (levelIndex + 1));
        }
        ok = writer.end (CATEGORY);
      } // end for each category
    } else {
      ok = writer.write(CATEGORY, this.getCategoryString());
    }

    ok = writer.write (TITLE, getTitle());
    
    if (verbose) {
      ok = writer.write (FILE_NAME, getTitleFileName());
    }
    
    ok = writer.start (BODY);
    ok = writer.blockValueFollows(true);
    body.writeMarkup(writer);
    ok = writer.end (BODY);
    
    ok = writer.write (RATING, getRatingLabel());
    ok = writer.write (PAGES, getPages());

    if (verbose) {
      writer.start (AUTHOR);
      author = getAuthor();
      writeAuthorNodes (writer, author);
      if (author.isCompound()) {
        for (int i = 0; i < author.getNumberOfAuthors(); i++) {
          writer.start (ONE_AUTHOR);
          Author subAuthor = author.getAuthor (i);
          writeAuthorNodes (writer, subAuthor);
          ok = writer.end (ONE_AUTHOR);
        }
      } 
    } else {
      ok = writer.write (AUTHOR, author.getCompleteName());
    }
    ok = writer.write (AUTHOR_INFO, getAuthorInfo());
    ok = writer.write (AUTHOR_LINK, getAuthorLink());
    if (verbose) {
      ok = writer.end (AUTHOR);
    }

    WisdomSource source = getSource();
    if (verbose) {
      writer.start (SOURCE);
      ok = writer.write (SOURCE_TYPE, source.getTypeLabel());
      ok = writer.write (TITLE, source.getTitle());
      ok = writer.write (MINOR_TITLE, source.getMinorTitle());
      ok = writer.write (PUBLISHER, source.getPublisher());
      ok = writer.write (CITY, source.getCity());
      ok = writer.write (ID, source.getID());
      ok = writer.write (RIGHTS, source.getRights());
      ok = writer.write (YEAR, source.getYear());
      ok = writer.write (RIGHTS_OWNER, source.getRightsOwner());
      ok = writer.write (LINK, source.getLink());
      ok = writer.write (FILE_NAME,  source.getFileName());
      ok = writer.end (SOURCE);
    } else {
      ok = writer.write (SOURCE, source.getTitle());
      ok = writer.write (SOURCE_TYPE, source.getTypeLabel());
      ok = writer.write (MINOR_TITLE, source.getMinorTitle());
      ok = writer.write (PUBLISHER, source.getPublisher());
      ok = writer.write (CITY, source.getCity());
      ok = writer.write (ID, source.getID());
      ok = writer.write (RIGHTS, source.getRights());
      ok = writer.write (YEAR, source.getYear());
      ok = writer.write (RIGHTS_OWNER, source.getRightsOwner());
      ok = writer.write (SOURCE_LINK, source.getLink());
    }

    ok = writer.write (DATE_ADDED, getDateAddedInternal());

    ok = writer.end (ITEM);
  
    if (openAndCloseSingleFile) {
      ok = writer.end(WISDOM);
      ok = writer.close();
    }
    return ok;
  }
  
  public static void tailorMarkupWriter (MarkupWriter markupWriter) {
    if (markupWriter.getMarkupFormat() == MarkupWriter.STRUCTURED_TEXT_FORMAT) {
      markupWriter.setStreamTag(WisdomItem.WISDOM);
      markupWriter.setDocumentTag(WisdomItem.ITEM);
      markupWriter.setIndenting(true);
      markupWriter.setIndentPerLevel(2);
      markupWriter.setWriteBlankValues(false);
      markupWriter.setMinimumCharsToColon(12);
    }
  }
  
  private boolean writeAuthorNodes (MarkupWriter writer, Author author) {

    boolean ok = false;
    ok = writer.write (NAME, author.getCompleteName());
    ok = writer.write (LAST_NAME, author.getLastName());
    ok = writer.write (FIRST_NAME, author.getFirstName());
    ok = writer.write (FILE_NAME, author.getFileName());
    if (! author.isCompound()) {
      ok = writer.write (WIKIPEDIA_LINK, author.getWikipediaLink());
    }
    return ok;
  }
  
} // end of class


