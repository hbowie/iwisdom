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

import com.powersurgepub.pstextio.FileLineReader;
  import com.powersurgepub.psdatalib.markup.*;
  import com.powersurgepub.iwisdom.*;
  import com.powersurgepub.psutils.*;
  import com.powersurgepub.xos2.*;
  import java.io.*;
  import java.net.*;
  import java.util.*;

/**
 *
 * @author b286172
 */
public class WisdomTXTIO {
  
  private              int        textFormat = 1;
  public  final static int        TEXTILE_FORMAT = 2;
  public  final static int        MARKDOWN_FORMAT = 1;
  public  final static String     TEXT_FILE_EXTENSION = "txt";
  
  private static final String     SUPREME_COURT_JUSTICE 
      = "supreme court justice";
  
  private     WisdomItems         items = null;
  private     WisdomItem          item = new WisdomItem();
  private     StringBuffer        field = new StringBuffer();
  private     int                 quoteCount = 0;
  private     StringBuffer        precedingAuthor = new StringBuffer();
  private     StringBuffer        trailingPunctuation = new StringBuffer();
  private     boolean             allNumeric = true;
  private     boolean             initialCaps = true;
  private     boolean             beginningOfLine = true;
  private     boolean             endOfLine = false;
 
  private     boolean             ok = true;
  private     boolean             outOK = false;
  private     BufferedWriter      outBuffered;
  private     FileLineReader      in; 
  private     String              line = ""; 
  private     char                lineChar =' ';
  private     char                nextChar = ' ';
  private     int                 lineIndex = 0;
  private     StringBuffer        textOut = new StringBuffer();
  
  private     int                 nextLink = 0;
  private     ArrayList           links = new ArrayList();
  
  // Trouble reporter
  private     Trouble             trouble = Trouble.getShared();
  
  /** Creates a new instance of WisdomTXTIO */
  public WisdomTXTIO () {
  }
  
  //
  // The following section writes text out
  //
  
  /**
   Save one Wisdom item to a specified output file.
   */
  public boolean save (File outFile, WisdomItem wisdom, int textFormat) {
    
    // System.out.println ("WisdomXMLIO.save (one item)" + wisdom.getTitle());
    this.textFormat = textFormat;
    ok = prepareOutput (outFile);
    if (ok) {
      ok = saveOneItemToFile (outBuffered, wisdom);
    } 
    return ok;
  }
  
  /**
   Save an entire wisdom collection to a specified output file.
   */
  public boolean save (File outFile, CollectionWindow header, 
      SortedItems items, int textFormat) {
    
    // System.out.println ("WisdomXMLIO.save (entire collection)");
    
    this.textFormat = textFormat;
    ok = prepareOutput (outFile);
    
    // Now write the header
    if (ok) {
      writeHeading (1, header.getCollectionTitle());
      writeParagraph ("", header.getDescription(), "");
      writeParagraph ("Published at", header.getLink(), header.getLink());
      writeParagraph ("Editor", header.getEditor(), "");
    }
    
    // Now write the collection
    if (ok) {
      WisdomItem nextItem;
      for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
        nextItem = items.get (itemIndex);
        if (! nextItem.isDeleted()) {
          ok = saveNextItemToFile (outBuffered, nextItem);
        } // end if not deleted
      } // end for loop
    } // end if open ok
    
    if (ok) {
      ok = closeOutput();
    }
    
    return ok;
  }
  
  /**
   Save a selected category to a specified output file.
   */
  public boolean save (File outFile, CollectionWindow header, 
      SortedItems items, String selectedCategory, int textFormat) {
    
    // System.out.println ("WisdomXMLIO.save (entire collection)");
    this.textFormat = textFormat;
    ok = prepareOutput (outFile);
    
    // Now write the header
    if (ok) {
      writeHeading (1, header.getCollectionTitle());
      writeParagraph ("", header.getDescription(), "");
      writeParagraph ("Published at", header.getLink(), header.getLink());
      writeParagraph ("Editor", header.getEditor(), "");
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
              ok = saveNextItemToFile (outBuffered, nextItem);
            } // end if category match
          } // end while more categories for this item
        } // end if not deleted
      } // end for loop
    } // end if open ok
    
    if (ok) {
      ok = closeOutput();
    }
    
    return ok;
  }
  
  private boolean prepareOutput (File outFile) {
    outOK = true;
    try {
      FileOutputStream outStream = new FileOutputStream (outFile);
      OutputStreamWriter outWriter = new OutputStreamWriter (outStream, "UTF-8");
      outBuffered = new BufferedWriter (outWriter);
    } catch (IOException e) {
      outOK = false;
      trouble.report 
          ("File "+ outFile.toString() + " could not be opened for output", 
              "File Save Error");  
    }
    return outOK;
  }
  
 /**
   Create an XML file holding a single piece of wisdom. The XTextFile
   must have already been created. 
   */
  private boolean saveOneItemToFile (BufferedWriter outBuffered, WisdomItem wisdom) {
    
    // System.out.println ("WisdomXMLIO.saveOneItemToFile " + wisdom.getTitle());
    
    ok = true;
    this.outBuffered = outBuffered;
    
    // Write the wisdom data to the file
    if (ok) {
      saveNextItemToFile (outBuffered, wisdom);
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
  private boolean saveNextItemToFile (BufferedWriter outBuffered, WisdomItem wisdom) {
    
    ok = true;
    this.outBuffered = outBuffered;
    
    // Create file content
    
    // Write Title as Heading
    writeHeading (2, wisdom.getTitle());
    
    // Write Category Info
    startParagraph ("Category");
    emphasis (wisdom.getCategory().toString());
    endParagraph();
    
    // Write Body
    writeParagraph ("", wisdom.getBody(MarkupElement.NO_HTML, false), "");
    
    // Write Author(s), if any
    String authorCompleteName = wisdom.getAuthorCompleteName();
    if (authorCompleteName.length() > 0) {
      Author author = wisdom.getAuthor();
      int numberOfAuthors = author.getNumberOfAuthors();
      if (author.isCompound()) {
        startParagraph ("--");
        for (int i = 0; i < numberOfAuthors; i++) {
          Author nextAuthor = author.getAuthor (i);
          appendItem (
              nextAuthor.getCompleteName(),
              nextAuthor.getALink(),  
              i, 
              numberOfAuthors);
        }
      } else {
        startParagraph ("--");
        appendItem (authorCompleteName, author.getALink(), 0, 1);
      }
      String authorInfo = wisdom.getAuthorInfo();
      if (authorInfo.length() > 0) {
        appendItem (", " + authorInfo, "",  0, 1);
      }
      endParagraph();
    }
    
    // Display Source, if any
    writeParagraph ("Source", wisdom.getSourceAsString(), wisdom.getASourceLink());
    writeParagraph ("Type", wisdom.getSourceTypeLabel(), "");
    writeParagraph ("Minor Title", wisdom.getMinorTitle(), "");
    
    // Display Rights / Publication Year
    String yearRightsLabel = "Year First Published";
    StringBuffer yearRights = new StringBuffer();
    String rights = wisdom.getRights();
    if (rights.length() > 0
        && rights.startsWith ("Copyright")) {
      yearRights.append ("Copyright &copy;");
    } else {
      yearRights.append (wisdom.getRights());
    }
    if (yearRights.length() > 0) {
      yearRightsLabel = "Rights";
    }
    if (! wisdom.getYear().equals("")) {
      if (yearRights.length() > 0) {
        yearRights.append (" ");
      }
      yearRights.append (wisdom.getYear());
    }
    if (wisdom.getRightsOwner().length() > 0) {
      if (yearRights.length() > 0) {
        yearRights.append (" by ");
      }
      yearRights.append (wisdom.getRightsOwner());
      yearRightsLabel = "Rights";
    }
    if (yearRights.length() > 0) {
      writeParagraph (yearRightsLabel, yearRights.toString(), "");
    }
    
    writeLinks();
    
    return ok;
  }
  
  private void writeHeading (int level, String heading) {
    startHeading (level, heading);
    append (heading);
    endHeading (level, heading);
  }
  
  private void startHeading (int level, String heading) {
    switch (textFormat) {
      case MARKDOWN_FORMAT:
        if (level > 2) {
          for (int i = 0; i < level; i++) {
            append ('#');
          }
        }
        break;
      case TEXTILE_FORMAT:
        append ('h');
        append (String.valueOf (level));
        append (". ");
        break;
      default:
        break;
    }
  }
  
  private void endHeading (int level, String heading) {
    switch (textFormat) {
      case MARKDOWN_FORMAT:
        writeLine();
        StringBuffer underline = new StringBuffer();
        char underchar = '-';
        if (level == 1) {
          underchar = '=';
        }
        for (int i = 0; i < heading.length(); i++) {
          underline.append (underchar);
        }
        writeLine (underline.toString());
        writeLine ();
        break;
      case TEXTILE_FORMAT:
        writeLine();
        writeLine();
        break;
      default:
        break;
    } 
  }
  
  private void emphasis (String text) {
    startEmphasis ();
    append (text);
    endEmphasis ();
  }
  
  private void startEmphasis () {
    switch (textFormat) {
      case MARKDOWN_FORMAT:
        append ('*');
        break;
      case TEXTILE_FORMAT:
        append ('_');
        break;
      default:
        break;
    }
  }
  
  private void endEmphasis () {
    switch (textFormat) {
      case MARKDOWN_FORMAT:
        append ('*');
        break;
      case TEXTILE_FORMAT:
        append ('_');
        break;
      default:
        break;
    } 
  }
  
  private void writeParagraph (String desc, String text, String link) {
    if (text.length() == 0 || text.equalsIgnoreCase ("unknown")) {
      // skip it if nothing to write
    } else {
      startParagraph (desc);
      appendTextWithLink (text, link);
      endParagraph();
    }
  }
  
  private void startParagraph (String desc) {
    startLine();
    if (desc.length() > 0) {
      append (desc);
      if (desc.equals ("--")) {
        // no colon
      } else {
        append (":");
      }
      append (" ");
    }
  }
  
  private void endParagraph () {
    writeLine();
    writeLine();
  }
  
  public void appendItem (
      String text, 
      String link,
      int listPosition,
      int listLength) {
    
    appendTextWithLink (text, link);
    String listSuffix = "";
    if (listLength > 1) {
      int listEndProximity = listLength - listPosition - 1;
      if (listEndProximity == 1) {
        listSuffix = " and ";
      }
      else
      if (listEndProximity > 1) {
        listSuffix = ", ";
      }
    }
    append (listSuffix);
  }
  
  private void appendTextWithLink (String text, String link) {
    switch (textFormat) {
      case MARKDOWN_FORMAT:
        if (link.length() > 0) {
          append ("[");
        }
        append (text);
        if (link.length() > 0) {
          append ("][");
          append (String.valueOf(links.size()));
          append ("]");
          links.add (link);
        }
        break;
      case TEXTILE_FORMAT:
        if (link.length() > 0) {
          append ("[");
        }
        append (text);
        if (link.length() > 0) {
          append ("|");
          append (link);
          append ("]");
        }
        break;
      default:
        break;
    }
  }
  
  private void writeLinks () {
    switch (textFormat) {
      case MARKDOWN_FORMAT:
        int l = 0;
        while (nextLink < links.size()) {
          append ("[");
          append (String.valueOf (nextLink));
          append ("]: ");
          append ((String)links.get(nextLink));
          nextLink++;
          writeLine();
          l++;
        }
        if (l > 0) {
          writeLine();
        }
        break;
      case TEXTILE_FORMAT:
        break;
      default:
        break;
    }
  }
  
  private void startLine () {
    textOut.setLength(0);
  }
  
  private void append(String s) {
    textOut.append (s);
  }
  
  private void append (char c) {
    textOut.append (c);
  }
  
  private void writeLine() {
    writeLine (textOut.toString());
    startLine();
  }
  
  private void writeLine (String line) {
    if (outOK) {
      try {
        outBuffered.write (line);
        outBuffered.newLine();
      } catch (java.io.IOException e) {
        outOK = false;
        trouble.report 
            ("Trouble writing to file "+ outBuffered.toString(), 
                "File Save Error");  
      }
    } // end if ok so far
  } // end method writeLine
  
  private boolean closeOutput () {
    if (outOK) {
      try {
        outBuffered.close();
      } catch (java.io.IOException e) {
        outOK = false;
        trouble.report 
            ("Trouble writing to file "+ outBuffered.toString(), 
                "File Save Error");  
      } // end exception processing
    } // end if output ok so far
    return outOK;
  } // end method
  
  //
  // The following section reads text in
  //
  
  public boolean importFile (URL inURL, WisdomItems items) {
    in = new FileLineReader(inURL);
    this.items = items;
    return importFile();
  }
  
  public boolean importFile (String importName, WisdomItems items) {
   
    in = new FileLineReader (importName);
    this.items = items;
    return importFile();
  }
  
  /**
   Load data from the an iWisdom library file or catalog into memory.
   */
  public boolean importFile (File inFile, WisdomItems items) {
    
    in = new FileLineReader (inFile); 
    this.items = items;
    return importFile();
  }
  
  /**
   Load data from the reader into memory.
   */
  public boolean importFile () {
    
    ok = true;
    initField();
    precedingAuthor = new StringBuffer();
    line = "";
    lineIndex = 1;
    ok = in.open();
    if (ok) {
      getNextChar(); 
      while (! in.isAtEnd()) { 
        processNextChar();
        getNextChar(); 
      } // while more input
      checkField();
      in.close();
    }
    return ok;
  }
  
  private void processNextChar () {
    if (beginningOfLine
        && (StringUtils.isDoubleQuote (lineChar) || endOfLine)) {
      checkField();
    }
    else
    if (StringUtils.isDoubleQuote (lineChar) 
        && quoteCount == 2
        && field.length() > 10) {
      checkField();
    }
    if (beginningOfLine
        && field.length() == 0
        && lineChar == 'ã') {
      lineChar = '"';
    }
    if (beginningOfLine 
        && lineChar == '>'
        && nextChar != ' ') {
      // ignore it
    } else {
      appendToField (lineChar);
    }
    if (! Character.isWhitespace(lineChar)) {
      beginningOfLine = false;
    } 
  }
  
  private void appendToField (char c) {
    if (Character.isWhitespace (c)
        && ((field.length() == 0)
          || (Character.isWhitespace (field.charAt (field.length() - 1))))) {
      // skip unnecessary white space
    } 
    else
    if (StringUtils.isDoubleQuote (c)) {
      field.append ('"');
      quoteCount++;
    } 
    else
    if (c == 'Õ') {
      field.append('\'');
    }  else {
      field.append (c);
    } 
  } // end method appendToField
  
  private void checkField () {
    
    // System.out.println ("checkField " + field.toString());
    // Remove any trash at the beginning of the quotation
    boolean validStart = false;
    while (field.length() > 0
        && (! validStart)) {
      char d = field.charAt (0);
      if (StringUtils.isDoubleQuote (d) || Character.isLetterOrDigit (d)) {
        validStart = true;
      } else {
        field.deleteCharAt (0);
      }
    }

    // Eliminate any trailing white space
    while (field.length() > 0
        && Character.isWhitespace (field.charAt (field.length() - 1))) {
      field.deleteCharAt (field.length() - 1);
    }
    
    // See if this line contains an author's name, followed by a colon
    boolean authorLine = false;
    if (field.length() > 0 
        && field.charAt (field.length() - 1) == ':') {
      // System.out.println ("  field ends with a colon");
      int upperWords = 0;
      int lowerWords = 0;
      precedingAuthor = new StringBuffer();
      int i = field.length() - 2;
      boolean nameFound = false;
      while (i >= 0 && (! nameFound)) {
        while (i > 0 
            && Character.isWhitespace(field.charAt(i))) {
          i--;
        }
        int wordEnd = i + 1;
        while (i >= 0 
            && (! Character.isWhitespace (field.charAt(i)))) {
          i--;
        }
        int wordStart = i + 1;
        String word = field.substring (wordStart, wordEnd);
        boolean nameWord = false;
        if (Character.isLowerCase (word.charAt (0))) {
          lowerWords++;
          if (upperWords == 1) {
            nameWord = true;
          } 
          else 
          if (upperWords > 1) {
            nameFound = true;
          } 
        } else {
          // Initial capital
          nameWord = true;
          upperWords++;
        }

        // If it looks like part of an author's name, add it to the string
        if (nameWord) {
          if (precedingAuthor.length() > 0) {
            precedingAuthor.insert (0, " ");
          }
          precedingAuthor.insert (0, word);
        } // end if a word that is part of an author's name
      } // end while looking for more words to put in the name
      // System.out.println ("  preceding author = " + precedingAuthor.toString());
      if (upperWords > 0 && precedingAuthor.length() > 4) {
        authorLine = true;
        initField();
      } else {
        precedingAuthor = new StringBuffer();
      }
      // System.out.println ("  author line = " + String.valueOf (authorLine));
    } // end if we found a trailing colon
    
    if (! authorLine) {
      checkEndOfField();
      
      // Skip junk at end of field
      while (field.length() > 0
          && (Character.isWhitespace (field.charAt (field.length() - 1))
            || field.charAt (field.length() - 1) == ')'
            || StringUtils.isDash (field.charAt (field.length() - 1)))) {
        field.deleteCharAt (field.length() - 1);
      }

      // If we've got something, then add it
      if (field.length() > 5) {
        item.setBody (field.toString());
        if (precedingAuthor.length() > 4) {
          item.setAuthor (precedingAuthor.toString());
        }
        int addedAt = items.add (item, true, false, false);
        item = new WisdomItem();
        initField();
      }
    }
  }
  
  /**
   Check the end of the quotation field to see if we have an author, a year, etc.
   */
  private void checkEndOfField () {
    
    // Look for a year
    int year = 0;
    StringBuffer yearStringBuffer = new StringBuffer();
    String yearString = "";
    int nonYearEnd = field.length() - 1;
    while (nonYearEnd > 5
        && (! Character.isLetter (field.charAt (nonYearEnd)))
        && (! StringUtils.isDoubleQuote (field.charAt(nonYearEnd)))) {
      if (Character.isDigit (field.charAt (nonYearEnd))) {
        yearStringBuffer.insert (0, field.charAt (nonYearEnd));
      }
      nonYearEnd--;
    }
    if (yearStringBuffer.length() == 4) {
      yearString = yearStringBuffer.toString();
      try {
        year = Integer.parseInt (yearString);
      } catch (NumberFormatException e) {
        year = 0;
      }
    }
    if (year > 999 && year < 2200) {
      item.setYear (yearString);
      field.delete (nonYearEnd + 1, field.length());
    }
    
    // Look for an author
    int i = field.length() - 1;
    int authorStart = 0;
    int authorEnd = field.length();
    int bodyEnd = 0;
    int authorLetters = 0;
    int quoteChars = 0;
    StringBuffer word = new StringBuffer();
    
    while (i > 5 && (authorStart == 0)) {
      char c = field.charAt (i);
      char b = field.charAt (i - 1);
      if (StringUtils.isDash (c)
          && b == ' '
          && authorLetters > 5) {
        authorStart = i + 1;
        bodyEnd = i - 1;
        while (bodyEnd > 0
            && (StringUtils.isDash (field.charAt (bodyEnd))
              || Character.isWhitespace (field.charAt (bodyEnd)))) {
          bodyEnd--;
        }
      }
      else
      if (StringUtils.isDoubleQuote (c)) {
        if ((quoteChars == 0 || quoteChars == 2)
            && authorLetters >= 5) {
          authorStart = i + 1;
          bodyEnd = i;
        } else {
          quoteChars++;
        }
      }
      else
      if (c == '(' && authorLetters >= 5
          && (! isInfoWord (word))) {
        authorStart = i + 1;
        bodyEnd = i - 1;
      }
      else
      if (Character.isLetter (c)) {
        authorLetters++;
        word.append (c);
      }
      i--;
    }
    
    // Skip junk at beginning of author's name
    if (authorStart > 0) {
      while (authorStart < field.length()
          && (Character.isWhitespace (field.charAt (authorStart))
            || field.charAt (authorStart) == '('
            || StringUtils.isDash (field.charAt (authorStart)))) {
        authorStart++;
      } // end skipping white space before author's name
    }
    
    // Skip junk at end of author's name
    if (authorStart > 0) {
      authorEnd--;
      while (authorEnd > authorStart
          && (Character.isWhitespace (field.charAt (authorEnd))
          || field.charAt (authorEnd) == ')'
          || StringUtils.isDash (field.charAt (authorEnd)))) {
        authorEnd--;
      }
      authorEnd++;
    }
    
    if (bodyEnd > 5
        && (field.length() - authorStart) > 4
        && authorStart > 0) {
      checkAuthor (field.substring (authorStart, authorEnd));
      field.delete (bodyEnd + 1, field.length());
    }
    
  } // end method checkEndOfField
  

  
  private void checkAuthor (String authorIn) {
    // System.out.println ("checkAuthor in " + authorIn);
    StringBuffer author = new StringBuffer (authorIn);
    StringBuffer authorInfo = new StringBuffer ();
    boolean beginsWith = false;
    boolean endsWith = false;
    int length = 0;
    StringBuffer word = new StringBuffer();
    int i = 0;
    int authorStart = 0;
    int authorEnd = author.length();
    boolean infoWord = true;
    while (infoWord && i < (author.length() - 1)) {
      word = new StringBuffer();
      authorStart = i;
      while (i < author.length()
          && (! Character.isWhitespace(author.charAt (i)))) {
        if (Character.isLetter (author.charAt (i))) {
          word.append (author.charAt (i));
        }
        i++;
      }
      infoWord = isInfoWord (word);
      if (infoWord) {
        if (authorInfo.length() > 0) {
          authorInfo.append (" ");
        }
        authorInfo.append (word);
        beginsWith = true;
        while (i < author.length()
            && Character.isWhitespace(author.charAt(i))) {
          i++;
        }
      } // end if infoWord
    } // end while more info words
    if (beginsWith) {
      author.delete (0, authorStart);
      trimFront (author);
      item.setAuthorInfo (authorInfo.toString());
    } else {
      infoWord = true;
      i = author.length() - 1;
      while (infoWord && i > 0) {
        word = new StringBuffer();
        authorEnd = i;
        while (i > 0
            && (! Character.isWhitespace(author.charAt (i)))) {
          if (Character.isLetter (author.charAt (i))) {
            word.insert (0, author.charAt (i));
          }
          i--;
        }
        infoWord = isInfoWord (word);
        if (infoWord) {
          if (authorInfo.length() > 0) {
            authorInfo.insert (0, " ");
          }
          authorInfo.insert (0, word);
          endsWith = true;
          while (i > 0
              && Character.isWhitespace(author.charAt(i))) {
            i--;
          }
        } // end if infoWord
      } // end while more info words
      if (endsWith) {
        author.delete (authorEnd + 1, author.length());;
        trimBack (author);
        item.setAuthorInfo (authorInfo.toString());
      }
    } // end if not info words found at beginning of author field

    if (author.length() > 0) {
      // System.out.println ("checkAuthor setting author to (" + author.toString() + ")");
      item.setAuthor (author.toString());
      precedingAuthor = new StringBuffer();
    }
    // System.out.println ("checkAuthor author = " + author.toString() 
    //     + " info = " + authorInfo.toString());
  }
  
  private void trim (StringBuffer str) {
    trimFront (str);
    trimBack (str);
  }
  
  private void trimFront (StringBuffer str) {
    while (str.length() > 0
        && (Character.isWhitespace (str.charAt (0))
          || str.charAt (0) == '(')
          || str.charAt (0) == ',') {
      str.deleteCharAt (0);
    } // end skipping white space before author's name
  }
  
  private void trimBack (StringBuffer str) {
    while (str.length() > 0
        && (Character.isWhitespace (str.charAt (str.length() - 1))
          || str.charAt (str.length() - 1) == ')')
          || str.charAt (str.length() - 1) == ',') {
      str.deleteCharAt (str.length() - 1);
    } // end skipping white space before author's name
  }
  
  /**
   Initialize the field used to store the quotation.
  */
  private void initField () {
    field = new StringBuffer();
    quoteCount = 0;
  }
  
  /*
  private void checkAuthor() {
    if (field.length() > 0) {
      item.setAuthor (field.toString());
      field = new StringBuffer();
    }
  }
  
  private void checkItem () {
    if (item.getBody().length() > 5) {
      int addedAt = items.add (item, true);
      item = new WisdomItem();
    }
  } */
  
  private boolean isInfoWord (StringBuffer word) {
    String lower = word.toString().toLowerCase();
    return (lower.equals ("supreme")
        || lower.equals ("court")
        || lower.equals ("justice")
        || lower.equals ("president")
        || lower.equals ("vice")
        || lower.equals ("poet")
        || lower.equals ("laureate")
        || lower.equals ("british")
        || lower.equals ("feminist")
        || lower.equals ("writer")
        || lower.equals ("astronomer")
        || lower.equals ("scientist")
        || lower.equals ("irish")
        || lower.equals ("scottish")
        || lower.equals ("french")
        || lower.equals ("philosopher")
        || lower.equals ("senate")
        || lower.equals ("minority")
        || lower.equals ("leader")
        || lower.equals ("senator")
        || lower.equals ("sen")
        || lower.equals ("author")
        || lower.equals ("representative")
        || lower.equals ("rep")
        || lower.equals ("judge")
        || lower.equals ("arkansas")
        || lower.equals ("governor")
        || lower.equals ("gov")
        || lower.equals ("&")
        || lower.equals ("and")
        || lower.equals ("of")
        || lower.equals ("secretary")
        || lower.equals ("state")
        || lower.equals ("founding")
        || lower.equals ("father")
        || lower.equals ("editor")
        || lower.equals ("nobel")
        || lower.equals ("historian")
        || lower.equals ("pbs"));
  }
  
  /**
   Get next character from input file. Adds one space at the end of each line
   */
  private void getNextChar () {
    if (lineIndex > line.length()) {
      getNextLine();
    }
    if (lineIndex >= line.length()) {
      lineChar = ' ';
      endOfLine = true;
    } else {
      lineChar = line.charAt (lineIndex);
      endOfLine = false;
    }
    
    lineIndex++;

    if (lineIndex < line.length()) {
      nextChar = line.charAt (lineIndex);
    } else {
      nextChar = ' ';
    }
  }
  
  private void getNextLine () {
    line = in.readLine();
    if (ok && line != null) {
      // System.out.println ("WisdomTXTIO.importFile line = (" + line + ")");
      boolean blankLine = true;
      for (int j = 0; j < line.length(); j++) {
        char d = line.charAt (j);
        if (Character.isLetterOrDigit (d)) {
          blankLine = false;
        }
      }
      if (blankLine) {
        line = "";
      }
    }
    lineIndex = 0;
    beginningOfLine = true;
  }
  
}
