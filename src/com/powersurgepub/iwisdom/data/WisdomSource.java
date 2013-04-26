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

  import com.powersurgepub.psutils.*;
  import javax.swing.*;

/**
 A source for one or more wisdom items.
 */
public class WisdomSource {
  
  public static final String UNKNOWN = "unknown";
  public static final String LEFT_DOUBLE_QUOTE  = "&#8220;";
  public static final String RIGHT_DOUBLE_QUOTE = "&#8221;";
  
  private String name = UNKNOWN;
  
	/** Type of item. */
	private    	int          		sourceType = 0;
  public  final static int      UNKNOWN_TYPE    = 0;
  public  final static int      ARTICLE         = 1;
  public  final static int      BOOK            = 2;
  public  final static int      CD              = 3;
  public  final static int      ESSAY           = 4;
  public  final static int      FILM            = 5;
  public  final static int      INTERVIEW       = 6;
  public  final static int      LECTURE         = 7;
  public  final static int      LETTER          = 8;
  public  final static int      PLAY            = 9;
  public  final static int      POEM            = 10;
  public  final static int      PREFACE         = 11;
  public  final static int      REMARKS         = 12;
  public  final static int      SONG            = 13;
  public  final static int      SPEECH          = 14;
  public  final static int      STORY           = 15;
  public  final static int      TV_SHOW         = 16;
  public  final static int      WEBLOG          = 17;
  public  final static int      WEBPAGE         = 18;
  public  final static int      SOURCE_TYPE_MAX = 18;
  
  public  final static String[] SOURCE_TYPE_LABEL = {
    "unknown",
    "Article",
    "Book",
    "CD",
    "Essay",
    "Film",
    "Interview",
    "Lecture",
    "Letter",
    "Play",
    "Poem",
    "Preface",
    "Remarks",
    "Song",
    "Speech",
    "Story",
    "Television Show",
    "Web Log",
    "Web Page"
  };
  
  private String minorTitle = "";
  
  private String link = "";
  
  private     String          id = "";
  private     String          publisher = "";
  private     String          city = "";
  private     String          rights = "";
	private    	String          year = "";
  private     String          rightsOwner = "";
  private     String          fileName = UNKNOWN;

  public static void setSourceTypeComboBox (JComboBox box) {
    box.removeAllItems();
    for (int i = 0; i <= SOURCE_TYPE_MAX; i++) {
      box.addItem (SOURCE_TYPE_LABEL [i]);
    }
  }
  
  /** Creates a new instance of WisdomSource */
  public WisdomSource() {
  }
  
  public WisdomSource(String name) {
    this.name = name;
    deriveFileName();
  }
  
  public void setName (String name) {
    setTitle (name);
  }
  
  public void setTitle (String title) {
    if (title.length() > 0) {
      this.name = title;
    } else {
      this.name = UNKNOWN;
    }
    deriveFileName();
  }
  
  public String getName () {
    return name;
  }

  public boolean hasTitle () {
    return (name.length() > 0 && (! name.equalsIgnoreCase(UNKNOWN)));
  }
  
  public String getTitle () {
    return name;
  }
  
  public void setMinorTitle (String minorTitle) {
    this.minorTitle = minorTitle;
  }

  public boolean hasMinorTitle() {
    return (minorTitle.length() > 0);
  }
  
  public String getMinorTitle () {
    return minorTitle;
  }
  
  public void setPublisher (String publisher) {
    this.publisher = publisher;
  }

  public boolean hasPublisher() {
    return (publisher.length() > 0);
  }
  
  public String getPublisher () {
    return publisher;
  }
  
  public void setCity (String city) {
    this.city = city;
  }

  public boolean hasCity() {
    return (city.length() > 0);
  }
  
  public String getCity () {
    return city;
  }
  
  public void setID (String id) {
    int significantCount = 0;
    for (int i = 0; i < id.length(); i++) {
      char c = id.charAt (i);
      if (c == '0' || c == ' ') {
        // insignificant
      } else {
        significantCount++;
      }
    }
    if (significantCount > 0) {
      this.id = id;
    } else {
      this.id = "";
    }
  }

  public boolean hasID () {
    return (id.length() > 0);
  }
  
  public String getID () {
    return id;
  }
  
  public void setLink (String link) {
    this.link = link;
  }
  
  public String getLink () {
    return link;
  }
  
  public String getBookstoreLink () {
    if (id == null || id.length() == 0) {
      return "";
    } else {
      return 
          "http://www.amazon.com/exec/obidos/ASIN/"
          + id + "/pagantuna-20";
    }
  }
  
  public String getALink () {
    if (link.length() > 0) {
      return getLink();
    } else {
      return getBookstoreLink();
    }
  }

  /**
   Scan the passed string to see if it contains a word identifying a valid
   source type. If a valid type is found, then set this source's type to
   that type, and return that type.

   @param s The string to be scanned
   @return The index of the first valid type found, or a negative number,
           if none found.
   */
  public int scanForType (String s) {
    boolean typeFound = false;
    int i = 0;
    int j = -1;
    int charsInWord = 0;
    while (i < s.length() && (! typeFound)) {
      char c = s.charAt(i);
      if (Character.isLetter(c)) {
        if (charsInWord == 0) {
          j = 0;
          while ((j < SOURCE_TYPE_LABEL.length) && (! typeFound)) {
            if ((i + SOURCE_TYPE_LABEL[j].length()) <= s.length()
                && SOURCE_TYPE_LABEL[j].equalsIgnoreCase(
                  s.substring(i, i + SOURCE_TYPE_LABEL[j].length()))) {
              typeFound = true;
            } else {
              j++;
            }
          } // end of source type label array
        } // end if we're at the beginning of a new word
        charsInWord++;
      } else {
        charsInWord = 0;
      }
      i++;
    } // end of input string
    if (typeFound) {
      setType(j);
      return j;
    } else {
      return -1;
    }
  }
  
  /**
     Sets source type, from label.

   */
  public void setType (String sourceType) {
    String stype = StringUtils.initialCaps (sourceType.trim());
    int i = 0;
    boolean found = false;
    while ((i < SOURCE_TYPE_LABEL.length) && (! found)) {
      if (stype.equals (SOURCE_TYPE_LABEL [i])) {
        found = true;
      } else {
        i++;
      }
    }
    if (found) {
      setType (i);
    }
  }

  /**
     Sets Status of item.
 
     @param  status Status of item.
   */
  public void setType (int sourceType) {
    this.sourceType = sourceType;
  }


  /**
     Returns Status of item.
 
     @return Status of item.
   */
  public int getType () {
    return sourceType;
  }

  public boolean hasType() {
    return (sourceType > UNKNOWN_TYPE);
  }
  
  /**
    Returns a String with a label for the status value.
   
    @return Status value.
   */
  public String getTypeLabel () {
    return getTypeLabel (sourceType);
  }
  
  /**
    Returns a String with a label for the status value.
   
    @return Status value.
   
    @param  status Status integer to be converted to a String label.
   */
  public static String getTypeLabel (int sourceType) {
    if (sourceType < 0 || sourceType >= SOURCE_TYPE_LABEL.length) {
      return String.valueOf (sourceType) + "-Out of Range";
    } else {
      return SOURCE_TYPE_LABEL [sourceType];
    }
  }

  public boolean hasRights() {
    return (rights.length() > 0);
  }
  
  /**
     Returns the type of rights to this wisdom.
 
   */
  public String getRights () {
    return rights;
  }
  
  /**
     Sets the rights.
 
   */
  public void setRights (String rights) {
    this.rights = rights;
  }
  
  /**
     Sets the outcome or result for this item.
 
     @param  outcome The outcome for this item.
   */
  public void setRightsOwner (String rightsOwner) {
    this.rightsOwner = rightsOwner;
  }

  public boolean hasRightsOwner() {
    return (rightsOwner.length() > 0);
  }

  /**
     Returns the owner of the rights for this item.
 
     @return The owner of the rights for this item.
   */
  public String getRightsOwner () {
    return rightsOwner;
  }
  
  /**
     Sets the year that this item was originally published.
 
     @param  year The year that this item was originally published.
   */
  public void setYear (String year) {
    this.year = year;
  }

  public boolean hasYear() {
    return (year.length() > 0);
  }

  /**
     Returns the year that this item was originally published.
 
     @return The year that this item was originally published.
   */
  public String getYear () {
    return year;
  }
  
  public void deriveFileName () {
    if (isBlank()) {
      setFileName (UNKNOWN);
    } else {
      setFileName (StringUtils.makeFileName (getName(), false));
    }
  }
  
  public boolean isUnknown() {
    return (name.length() == 0 || name.equals (UNKNOWN));
  }
  
  public boolean isBlank() {
    return (name.length() == 0 || name.equals (UNKNOWN));
  }
  
  public void setFileName (String fileName) {
    this.fileName = fileName;
  }
  
  public String getFileName () {
    return fileName;
  }
  
  public String merge (WisdomSource source2) {
    CommaList updatedFields = new CommaList();
    
    if (this.equals (source2)) {
      if (source2.getLink().length() > getLink().length()) {
        setLink (source2.getLink());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_LINK_INDEX]);
      }

      if (source2.getMinorTitle().length() > getMinorTitle().length()) {
        setMinorTitle (source2.getMinorTitle());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.MINOR_TITLE_INDEX]);
      }
      
      if (source2.getPublisher().length() > getPublisher().length()) {
        setPublisher (source2.getPublisher());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.PUBLISHER_INDEX]);
      }
      
      if (source2.getCity().length() > getCity().length()) {
        setCity(source2.getCity());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.CITY_INDEX]);
      }
      
      if (source2.getID().length() > getID().length()) {
        setID (source2.getID());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_ID]);
      }
      
      if (source2.getRights().length() > getRights().length()) {
        setRights (source2.getRights());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.RIGHTS_INDEX]);
      }
      
      if (source2.getRightsOwner().length() > getRightsOwner().length()) {
        setRightsOwner (source2.getRightsOwner());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.RIGHTS_OWNER_INDEX]);
      }
      
      if (source2.getYear().length() > getYear().length()) {
        setYear (source2.getYear());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.YEAR_INDEX]);
      }
      
      if (source2.getType() > getType()) {
        setType (source2.getType());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.SOURCE_TYPE_INDEX]);
      }
    }
    
    return updatedFields.toString();
  }
  
  public boolean equals (WisdomSource source2) {
    return (getName().equals (source2.getName())
        && (! isUnknown()));
  }
  
  public boolean equals (String source2Name) {
    return (getName().equals (source2Name));
  }
  
  public void display () {
    System.out.println ("Title: " + getTitle());
    System.out.println ("Type: " + getTypeLabel());
    System.out.println ("Minor Title: " + getMinorTitle());
    System.out.println ("Link: " + getLink());
    System.out.println ("ID: " + getID());
    System.out.println ("Publisher: " + getPublisher());
    System.out.println ("City: " + getCity());
    System.out.println ("Rights: " + getRights());
    System.out.println ("Published: " + getYear());
    System.out.println ("Rights by : " + getRightsOwner());
    System.out.println (" ");
  }

  /**
   Get a line of formatted HTML with source information embedded.

   @return
   */
  public String getSourceLine (String pages) {
    StringBuilder line = new StringBuilder();

    // Identify the source type
    if (hasType()) {
      if (hasTitle() || hasMinorTitle()) {
        line.append ("the ");
      } else {
        line.append ("a ");
      }
      line.append ("<span class='sourcetype'>");
      line.append (getTypeLabel());
      line.append ("</span>");
      line.append (" ");
    }

    // Identify the major title of the work
    if (hasTitle()) {
      line.append ("<cite class='majortitle'>");
      boolean linked = false;
      if (link.length() > 0 && (! hasMinorTitle())) {
        line.append ("<a href='" + link + "' rel='source'>");
        linked = true;
      }
      line.append (name);
      if (linked) {
        line.append("</a>");
      }
      line.append ("</cite>");
    }

    // Identify the minor title of the work
    if (hasMinorTitle()) {
      insertSeparatorIfNeeded(line);
      line.append ("<cite class='minortitle'>");
      boolean linked = false;
      if (link.length() > 0 && (! hasTitle())) {
        line.append ("<a href='" + link + "' rel='source'>");
        linked = true;
      }
      line.append ("&#8220;" + minorTitle + "&#8221;");
      if (linked) {
        line.append("</a>");
      }
      line.append ("</cite>");
    }

    // Identify where published and by whom
    if (hasCity()) {
      insertSeparatorIfNeeded(line);
      line.append ("<span class='city'>");
      line.append (StringConverter.getXML().convert(getCity()));
      line.append ("</span>");
      if (hasPublisher()) {
        line.append(": ");
      }
    }

    if (hasPublisher()) {
      insertSeparatorIfNeeded(line);
      line.append ("<span class='publisher'>");
      line.append (StringConverter.getXML().convert(getPublisher()));
      line.append ("</span>");
    }

    // Identify when the quotation was first published
    if (hasYear()) {
      insertSeparatorIfNeeded(line);
      line.append ("<span class='datepublished'>");
      line.append (getYear());
      line.append ("</span>");
    }

    // Identify the pages on which the quotation can be found
    if (pages.length() > 0) {
      insertSeparatorIfNeeded(line);
      line.append ("page");
      boolean punctuationInPages = false;
      int i = 0;
      while (i < pages.length() && (! punctuationInPages)) {
        punctuationInPages = ((! Character.isDigit(pages.charAt(i)))
            && (! Character.isLetter(pages.charAt(i)))
            && (! Character.isWhitespace(pages.charAt(i))));
        i++;
      }
      if (punctuationInPages) {
        line.append ("s");
      }
      line.append ("&nbsp;");
      line.append ("<span class='pages'>");
      line.append (pages);
      line.append ("</span>");
    }

    // Identify the ISBN of the work
    if (hasID()) {
      insertSeparatorIfNeeded(line);
      line.append ("ISBN&nbsp;<a href='http://www.amazon.com/exec/obidos/ASIN/"
          + id + "/portablewisdo-20' class='isbn'>");
      line.append (id);
      line.append ("</a>");
      line.append (" ");
    }

    return line.toString();
  }

  /**
   Get a copyright line.

   @return Copyright statement.
   */
  public String getRightsLine () {
    StringBuilder line = new StringBuilder();

    // Identify the rights type
    if (hasRights()) {
      line.append ("<span class='rights'>");
      line.append (StringConverter.getXML().convert(getRights()));
      line.append ("</span>");
      if (getRights().equalsIgnoreCase("copyright")) {
        line.append(" &copy;");
      }
      line.append (" ");

      // Identify when the quotation was first published
      if (hasYear()) {
        line.append ("<span class='datepublished'>");
        line.append (getYear());
        line.append ("</span>");
        line.append (" ");
      }

      if (hasRightsOwner()) {
        line.append ("by ");
        line.append ("<span class='rightsowner'>");
        line.append (StringConverter.getXML().convert(getRightsOwner()));
        line.append ("</span>");
      }
    }

    return line.toString();
  }

  private void insertSeparatorIfNeeded(StringBuilder str) {
    if (str.length() > 0
        && (! Character.isWhitespace(str.charAt(str.length() - 1)))) {
      if (str.charAt(str.length() - 1) != ':') {
        int rdqpos = str.length() - RIGHT_DOUBLE_QUOTE.length();
        if (rdqpos > 0 && str.substring(rdqpos).equals(RIGHT_DOUBLE_QUOTE)) {
          str.insert(rdqpos, ",");
        } else {
          str.append(",");
        }
      }
      str.append(" ");
    }
  }
  
  public String toString() {
    return getName();
  }
  
}
