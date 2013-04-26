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
  import java.util.*;

/**

   An object representing an author, or authoring team, for one or more
   works. 

 */
public class Author {
  
  public static final String UNKNOWN    = "anonymous";
  
  private   StringBuffer  lastName      = new StringBuffer();
  private   StringBuffer  firstName     = new StringBuffer();
  private   ArrayList     authors       = new ArrayList();
  private static final String[] suffix = {
    "jr",
    "sr",
    "iii"
  };
  
  /** Link to a site about the author (other than Wikipedia). */
  private     String        link          = "";
  
  /** Brief information concerning the significance of the author. */
  private     String        authorInfo = "";
  
  /** The folder/file name used to identify this author. */
  private     String        fileName      = UNKNOWN;
  
  /** Creates a new instance of Author */
  public Author() {
  }
  
  public Author (String lastName, String firstName) {
    setLastName (lastName);
    setFirstName (firstName);
  }
  
  public Author (String completeName) {
    if (completeName.length() > 0) {
      setCompleteName (completeName);
    }
  }
  
  public void display () {
    System.out.println ("Author: " + toString());
    System.out.println ("Author Info: " + getAuthorInfo());
    System.out.println ("Author Link: " + getLink());
    System.out.println (" ");
  }
  
  /**
   If the two authors have equal keys, then grab any data in the second that
   is not already in this one, and save the data in this object.
   
   @param  The second author object, with the same key but possibly additional
           supplemental information.
   @return String listing the fields that were update, or an empty string if
           no fields were updated.
   */
  public String merge (Author author2) {
    CommaList updatedFields = new CommaList();
    
    if (this.equals (author2)) {
      if (author2.getLink().length() > getLink().length()) {
        setLink (author2.getLink());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.AUTHOR_LINK_INDEX]);
      }

      if (author2.getAuthorInfo().length() > getAuthorInfo().length()) {
        setAuthorInfo (author2.getAuthorInfo());
        updatedFields.append (WisdomItem.COLUMN_DISPLAY [WisdomItem.AUTHOR_INFO_INDEX]);
      }
    }
    return updatedFields.toString();
  }
  
  /**
    Set the content of the Author object from a single String containing the 
    complete name(s) of one or more author(s). If the string names multiple
    authors, then it is expected to be in the form "John Doe, Jane Smith and 
    Joe Riley": in other words, with an "and" (or an ampersand) before the last 
    name and with other names separated by commas. 
   */
  public void setCompleteName (String completeName) {
    // Check to see if the string contains multiple names
    // System.out.println ("Author setCompleteName (" + completeName + ")");
    boolean ok = true;
    if (completeName.length() > 150) {
      ok = false;
    }
    int startAnd = completeName.indexOf (" and ");
    int endAnd = -1;
    if (startAnd > 0) {
      endAnd = startAnd + 5;
    } else {
      startAnd = completeName.indexOf (" &amp; "); 
      if (startAnd > 0) {
        endAnd = startAnd + 7;
      } else {
        startAnd = completeName.indexOf (" & ");
        if (startAnd > 0) {
          endAnd = startAnd + 3;
        }
      }
    } 
    if (! ok) {
      setOneCompleteName ("Error");
    }
    else
    if (startAnd > 0) {
      // multiple names: separate them
      int nextNameStart = 0;
      while (nextNameStart < completeName.length()
          && (completeName.charAt(nextNameStart) == '-'
            || completeName.charAt(nextNameStart) == '~'
            || completeName.charAt(nextNameStart) == ' '
            || completeName.charAt(nextNameStart) == '\t')) {
        nextNameStart++;
      }
      int lastComma = 0;
      int nextComma = completeName.indexOf (", ");
      while (nextComma > 0 && nextComma < startAnd && ok) {
        // Get each name preceding a comma
        if (nextNameStart > nextComma
            || ((nextComma - nextNameStart) > 50)) {
          ok = false;
        } 
        else {
          Author nextAuthor 
              = new Author (completeName.substring (nextNameStart, nextComma));
          authors.add (nextAuthor);
          nextNameStart = nextComma + 2;
          nextComma = completeName.indexOf (", ", nextNameStart);
        }
      }
      // Now get the name before the and
      /*System.out.println ("Author.setCompleteName");
      System.out.println ("  Processing "
          + String.valueOf (nextNameStart)
          + " - "
          + String.valueOf (startAnd)
          + " of "
          + completeName); */
      if (nextNameStart > startAnd
          || ((startAnd - nextNameStart) > 50)) {
        ok = false;
      }
      if (ok) {
        Author nextAuthor 
            = new Author (completeName.substring (nextNameStart, startAnd));
        authors.add (nextAuthor);
      }
      // Now get the name after the and
      if (ok) {
        Author nextAuthor 
            = new Author (completeName.substring (endAnd));
        authors.add (nextAuthor);
      }
    } else {
      setOneCompleteName (completeName);
    }
    deriveFileName();
  } // end method setCompleteName
  
  public void setOneCompleteName (String completeNameIn) {
    // System.out.println ("Author setOneCompleteName (" + completeNameIn + ")");
    String completeName = completeNameIn.trim();
    int nameStart = 0;
    while (nameStart < completeName.length()
        && (completeName.charAt(nameStart) == '-'
          || completeName.charAt(nameStart) == '~'
          || completeName.charAt(nameStart) == ' '
          || completeName.charAt(nameStart) == '\t')) {
      nameStart++;
      // System.out.println ("Author setOneCompleteName leading char: " + completeName.charAt(nameStart));
    }
    int firstNameStart = nameStart;
    int firstNameEnd = completeName.length();
    int lastNameStart = nameStart;
    int lastNameEnd = completeName.length();
    boolean lastNameFirst = false;
    int commaStart = completeName.indexOf (",");
    int postCommaStart = commaStart + 1;
    
    if (commaStart > 0) {
      postCommaStart = firstNonSpace (completeName, postCommaStart);
      String postComma = completeName.substring (postCommaStart);
      boolean suffixAfterComma = isSuffix (postComma);
      if (! suffixAfterComma) {
        lastNameFirst = true;
      }
    } // end if we found a comma
    if (lastNameFirst) {
      lastNameEnd = commaStart;
      firstNameStart = firstNonSpace (completeName, commaStart + 1);
    } else {
      // first name first
      int lastSpace = completeName.lastIndexOf (' ');
      if (lastSpace < 0) {
        // No spaces -- last name only 
        firstNameStart = lastNameEnd;
      } else {
        // find beginning of last name
        String suffixTest = completeName.substring (lastSpace + 1);
        boolean suffixAfterLastSpace = isSuffix (suffixTest);
        if (suffixAfterLastSpace) {
          lastSpace = completeName.lastIndexOf (' ', lastSpace - 1);
        } // end if suffix found after presumed last name
        lastNameStart = lastSpace + 1;
        firstNameEnd = lastSpace;
      } // end if first name first, last name last
    } // end if last name last, with or without first name
    firstName = new StringBuffer (StringUtils.substringLenient 
        (completeName, firstNameStart, firstNameEnd));
    lastName = new StringBuffer (StringUtils.substringLenient 
        (completeName, lastNameStart, lastNameEnd));
    deriveFileName();
  } // end setOneCompleteName method
  
  public boolean isSuffix (String s) {
    int suffixEnd = s.length();
    if (suffixEnd > 0 
        && s.charAt (suffixEnd - 1) == '.') {
      suffixEnd--;
    }
    String suffixTest = s.substring(0, suffixEnd).toLowerCase().trim();
    boolean suffixFound = false;
    for (int i = 0; i < suffix.length; i++) {
      if (suffixTest.equals (suffix[i])) {
        suffixFound = true;
      } // end if common suffix found after comma
    } // end for each possible common suffix
    return suffixFound;
  }
  
  public int firstNonSpace (String s, int i) {
    int j = i;
    while (j < s.length()
        && (s.charAt (j) == ' ' 
          || s.charAt (j) == '-' 
          || s.charAt (j) == '~'
          || s.charAt (j) == '\t')) {
      j++;
    }
    return j;
  }
  
  public void setFirstName (String firstName) {
    this.firstName = new StringBuffer (firstName);
    deriveFileName();
  }
  
  public void setLastName (String lastName) {
    this.lastName = new StringBuffer (lastName);
    deriveFileName();
  }
  
  public void addAuthor (String completeName) {
    Author anotherAuthor = new Author (completeName);
    addAuthor (anotherAuthor);
    deriveFileName();
  }
  
  public void addAuthor (Author anotherAuthor) {
    authors.add (anotherAuthor);
    deriveFileName();
  }
  
  public int compareTo (Author author2) {
    int c = 0;
    c = getLastName().compareTo (author2.getLastName());
    if (c == 0) {
      c = getFirstName().compareTo (author2.getFirstName());
    }
    return c;
  }
  
  public boolean equals (Author author2) {
    return getCompleteName().equals (author2.getCompleteName());
  }
  
  public boolean equals (String author2Name) {
    return getCompleteName().equals (author2Name);
  }

  /**
   Get the complete name of the author, including the possibility of
   multiple authors.

   @return The complete name of the author(s), with first name(s) first.
   */
  public String getCompleteName () {
    return getCompleteName(false);
  }

  /**
   Get the complete name of the author, including the possibility of
   multiple authors.

   @param lastNamesStrong If true, emphasize the last names by enclosing them
                          in HTML strong tags; if false, insert no HTML.
   @return The complete name of the author(s), with first name(s) first.
   */
  public String getCompleteName (boolean lastNamesStrong) {
    StringBuffer name = new StringBuffer();
    if (isCompound()) {
      for (int i = 0; i < authors.size(); i++) {
        Author author = getAuthor (i);
        name.append (author.getCompleteName(lastNamesStrong));
        if (i < (authors.size() - 2)) {
          name.append (", ");
        }
        else
        if (i == (authors.size() - 2)) {
          name.append (" and ");
        } // end if penultimate author
      } // end for each author in list of multiple authors
    } else {
      name.append (getFirstName());
      if (name.length() > 0) {
        name.append (" ");
      }
      if (lastNamesStrong) {
        name.append("<strong>");
      }
      name.append (getLastName());
      if (lastNamesStrong) {
        name.append("</strong>");
      }
    }
    return name.toString();
  }

  /**
   Create a formatted string of all author's names, each one appearing with
   their last name first.

   @return All author names, last names before first names. 
   */
  public String getCompleteNameLastNamesFirst () {
    StringBuffer name = new StringBuffer();
    if (isCompound()) {
      for (int i = 0; i < authors.size(); i++) {
        Author author = getAuthor (i);
        name.append (author.getCompleteNameLastNamesFirst());
        if (i < (authors.size() - 2)) {
          name.append (", ");
        }
        else
        if (i == (authors.size() - 2)) {
          name.append (" and ");
        } // end if penultimate author
      } // end for each author in list of multiple authors
    } else {
      name.append (getLastName());
      if (name.length() > 0 && getFirstName().length() > 0) {
        name.append (", ");
      }
      name.append (getFirstName());
    }
    return name.toString();
  }
  
  public int getNumberOfAuthors () {
    return authors.size();
  }
  
  public Author getAuthor (int i) {
    if (i < 0 || i >= authors.size()) {
      return null;
    } else {
      Author author = (Author)authors.get(i);
      return author;
    }
  }
  
  public boolean isCompound () {
    return (authors.size() > 0);
  }
  
  public String getFirstName () {
    return firstName.toString();
  }
  
  public String getLastName() {
    StringBuffer name = new StringBuffer();
    if (isCompound()) {
      for (int i = 0; i < authors.size(); i++) {
        Author author = getAuthor (i);
        name.append (author.getLastName());
        if (i < (authors.size() - 2)) {
          name.append (", ");
        }
        else
        if (i == (authors.size() - 2)) {
          name.append (" and ");
        } // end if penultimate author
      } // end for each author in list of multiple authors
    } else {
      name.append (lastName);
    }
    return name.toString();
  }
  
  public void setLink (String link) {
    this.link = link;
  }
  
  public String getLink () {
    return link;
  }
  
  /**
   Return a link to the author's page on Wikipedia (presuming that one exists)
   */
  public String getWikipediaLink() {
    return "http://en.wikipedia.org/wiki/" + getWikiMediaPage();
  }
  
  public String getWikiquoteLink() {
    return "http://en.wikiquote.org/wiki/" + getWikiMediaPage();
  }

  /**
   Create a URL pointing to the presumed WikiQuote.org page for this author.

   @param language The language to be used (first two characters should match
                   the language codes used by wikiquote.org.
   @return The formatted URL as a String.
   */
  public String getWikiquoteLink(String language) {
    return getWikiquoteLink (language, getCompleteName());
  }

  /**
   Create a URL pointing to the indicated page on WikiQuote.org.

   @param language The language to be used (first two characters should match
                   the language codes used by WikiQuote.org.
   @param pageTitle The title of the page in WikiQuote: generally the name of
                    an author or the name of an author's work.
   @return The formatted URL as a String.
   */
  public static String getWikiquoteLink (String language, String pageTitle) {
    String lang;
    if (language.length() >= 2) {
      lang = language.substring(0, 2).toLowerCase();
    } else {
      lang = language.toLowerCase();
    }
    return "http://" + lang + ".wikiquote.org/wiki/"
      + getWikiMediaPage (pageTitle);
  }
  
  /**
   Format the author's name as the page name within a WikiMedia site (WikiPedia,
   WikiQuote, etc.)
   
   @return The author's page title as we would expect to find it on WikiPedia, 
           etc.
   */
  public String getWikiMediaPage () {
    return getWikiMediaPage (getCompleteName());
  }

  /**
   Format an author's name as the page name within a WikiMedia site (WikiPedia,
   WikiQuote, etc.)

   @param pageTitle The Page title before transformation.
   @return The page title as we would expect to find it on WikiPedia, etc.
   */
  public static String getWikiMediaPage (String pageTitle) {
    return StringUtils.replaceString(
          StringUtils.replaceString(pageTitle, " ", "_"), ",", "%2C");
  }
  
  public String getALink () {
    if (link.length() > 0) {
      return getLink();
    } else {
      return getWikipediaLink();
    }
  }
  
  public void deriveFileName () {
    String authorName = getCompleteName();
    if (authorName.length() < 1) {
      setFileName (UNKNOWN);
    } else {
      setFileName (StringUtils.makeFileName (authorName, false));
    }
  }
  
  /**
   When being read from disk, the file name should be set to the actual
   file name read.
   */
  public void setFileName (String fileName) {
    this.fileName = fileName;
  }
  
  public String getFileName () {
    return fileName;
  }
  
  public void setAuthorInfo (String authorInfo) {
    this.authorInfo = authorInfo;
  }
  
  public String getAuthorInfo () {
    return authorInfo;
  }
  
  public boolean isBlank() {
    return (authors.size() == 0 
        && firstName.length() == 0 
        && lastName.length() == 0);
  }
  
  public String toString() {
    return getCompleteName();
  }
  

  
}
