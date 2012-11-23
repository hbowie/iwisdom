package com.powersurgepub.iwisdom.data;

  import com.powersurgepub.psdatalib.pstextio.FileLineReader;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.net.*;

/**
 Import quotations from a fortune file. 

 @author Herb Bowie.
 */
public class WisdomFortuneImport {
  
  public static final String      BLOCK_QUOTE = "blockquote";
  public static final String      PARAGRAPH = "p";
  
  private     FileLineReader      in = null;
  private     String              line = ""; 
  private     boolean             fortuneStart = true;
  private     int                 lineIndex = 0;
  private     int                 indent = 0;
  private     int                 lastIndent = 0;
  private     int                 authorLineCount = 0;
  
  private     WisdomItems         items = null;
  private     WisdomItem          item = new WisdomItem();
  private     StringBuilder       paragraph = new StringBuilder();
  private     StringBuilder       author = new StringBuilder();
  private     StringBuilder       source = new StringBuilder();
  private     StringBuilder       minorTitle = new StringBuilder();
  private     StringBuilder       sourceLink = new StringBuilder();
  private     StringBuilder       word = new StringBuilder();
  
  private     boolean             bodyOpen = false;
  private     boolean             blockQuoteOpen = false;
  private     boolean             paragraphOpen = false;
  
  private     int                 expecting = 0;
  private     int                 BODY = 0;
  private static final int        AUTHOR = 1;
  private static final int        SOURCE = 2;
  private static final int        MINOR_TITLE = 3;
  private static final int        SOURCE_LINK = 4;
  
  private     boolean             ok = true;
  
  public WisdomFortuneImport() {
    
  }
  
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
  private boolean importFile () {
    
    ok = true;
    
    ok = in.open();
    readLine();
    if (ok) {
      while (! in.isAtEnd()) {
        anotherItem();
      }
      in.close();
    }
    return ok;
  }
  
  private void anotherItem() {
    item = new WisdomItem();
    bodyOpen = false;
    blockQuoteOpen = false;
    paragraphOpen = false;
    paragraph = new StringBuilder();
    author = new StringBuilder();
    source = new StringBuilder();
    minorTitle = new StringBuilder();
    sourceLink = new StringBuilder();
    word = new StringBuilder();
    expecting = BODY;
    authorLineCount = 0;
    lastIndent = 0;
    do {
      anotherLine();
    } while ((! in.isAtEnd()) && (! fortuneStart));
    
    closeParagraph();
    closeBlockQuote();
    closeBody();
    
    if (author.length() > 0
        && author.length() < 256) {
      item.setAuthor(author.toString());
    }
    
    if (source.length() > 0) {
      item.setSource(source.toString());
    }
    
    if (sourceLink.length() > 0) {
      item.setSourceLink(sourceLink.toString());
    }
    
    if (minorTitle.length() > 0) {
      item.setMinorTitle(minorTitle.toString());
    }

    if (item.hasBody()) {
      int addedAt = items.add (item, true, false, false);
    }
  }
  
  private void anotherLine() {
    
    lineIndex = 0;
    
    // Bump past the percentage sign, if the line starts with one
    while (lineIndex < line.length() && line.charAt(lineIndex) == '%') {
      lineIndex++;
    }
    if (lineIndex < line.length() 
        && lineIndex > 0
        && line.charAt(lineIndex) == ' ') {
      lineIndex++;
    }
    
    // Now bump past indentation, and measure it
    indent = 0;
    while (lineIndex < line.length() 
        && Character.isWhitespace(line.charAt(lineIndex))) {
      indent++;
      if (line.charAt(lineIndex) == GlobalConstants.TAB) {
        indent++;
      }
      lineIndex++;
    }
    
    // Look for <<< or >>> to indicate start or end of a block quote
    int greaterLesserCount = 0;
    while (lineIndex < line.length() 
         && (line.charAt(lineIndex) == '<'
          || line.charAt(lineIndex) == '>')) {
      greaterLesserCount++;
      lineIndex++;
    }
    if (greaterLesserCount > 0 && greaterLesserCount < 3) {
      lineIndex = lineIndex - greaterLesserCount;
    }
    
    // Look for a dash or two to indicate an author line
    int dashCount = 0;
    while (lineIndex < line.length() 
        && StringUtils.isDash(line.charAt(lineIndex))) {
      dashCount++;
      lineIndex++;
    }
    
    // Now bump past any white space
    while (lineIndex < line.length() 
        && Character.isWhitespace(line.charAt(lineIndex))) {
      lineIndex++;
    }
    
    // Now figure out what kind of line it is and process accordingly
    if ((indent > 0 && dashCount > 0)
        || (dashCount > 1)) {
      closeBody();
      authorLineCount++;
      if (authorLineCount == 1) {
        expecting = AUTHOR;
      } else {
        expecting = SOURCE;
      }
    } 
    if (authorLineCount > 0) {
      parseAuthorLine();
    }
    else
    if (lineIndex >= line.length()) {
      if (greaterLesserCount >= 3) {
        openOrCloseBlockQuote();
      } else {
        processBlankLine();
      }
    } else {
      if (paragraphOpen
          && indent > 0 
          && indent > lastIndent) {
        closeParagraph();
      }
      parseBodyLine();
    }
    
    lastIndent = indent;
    
    readLine();
  }
  
  private void openOrCloseBlockQuote() {
    if (blockQuoteOpen) {
      closeBlockQuote();
    } else {
      closeParagraph();
      openBlockQuote();
      
    }
  }
  
  private void processBlankLine() {
    closeParagraph();
  }
  
  private void parseBodyLine() {

    openBody();
    openParagraph();
    if (paragraph.length() > 0 
        && (! Character.isWhitespace(paragraph.charAt(paragraph.length() - 1)))) {
      paragraph.append(' ');
    }
    paragraph.append(line.substring(lineIndex - indent));
    
  }
  
  private void parseAuthorLine() {
    while (lineIndex < line.length()) {
      anotherWord();
    }
  }
  
  private void anotherWord() {
    word = new StringBuilder();
    
    // Look for beginning of next word
    while (lineIndex < line.length() 
        && Character.isWhitespace(line.charAt(lineIndex))) {
      lineIndex++;
    }
    
    // Buld next word
    boolean more = true;
    char c = ' ';
    while (lineIndex < line.length() && more) {
      c = line.charAt(lineIndex);
      if (Character.isWhitespace(c)) {
        more = false;
      }
      else 
      if (StringUtils.isDoubleQuote(c)) {
        more = false;
      }
      else
      if (c == '_') {
        more = false;
      }
      else
      if (c == '(') {
        more = false;
      }
      else
      if (c == ')') {
        more = false;
      } 
      else
      if (c == ',' && (expecting == AUTHOR)) {
        more = false;
      } else {
        word.append(c);
        lineIndex++;
      }
    } // end while more word characters
    if ((word.length() > 0) && word.charAt(word.length() - 1) == '.') {
      word.deleteCharAt(word.length() - 1);
    }
    
    if (word.length() == 0) {
      // do nothing
    } else {
      if (word.length() > 7 && word.substring(0, 7).equals("http://")) {
        expecting = SOURCE_LINK;
      }
      switch (expecting) {
        case AUTHOR: 
          if (word.toString().equalsIgnoreCase("by")) {
            // skip it
          } else {
            if (author.length() > 0) {
              author.append(' ');
            }
            author.append(word);
          }
          break;
        case SOURCE:
          if (word.toString().equals("in") 
              || word.toString().equals("of")
              || word.toString().equals("on")
              || word.toString().equals("the")
              || word.toString().equals("from")
              || word.toString().equals("at")
              || word.toString().equals("quoted")) {
            // skip it
          } else {
            if (source.length() > 0) {
              source.append(' ');
            }
            source.append(word);
          }
          break;
        case MINOR_TITLE: 
          if (minorTitle.length() > 0) {
            minorTitle.append(' ');
          }
          minorTitle.append(word);
          break;
        case SOURCE_LINK:
          sourceLink.append(word);
          break;
      }
    } // end if word non-blank
    
    if (lineIndex < line.length()) {
      if (StringUtils.isDoubleQuote(c)) {
        if (minorTitle.length() == 0) {
          expecting = MINOR_TITLE;
        } else {
          expecting = AUTHOR;
        }
        lineIndex++;
      }
      else
      if (c == '_') {
        if (source.length() == 0) {
          expecting = SOURCE;
        } else {
          expecting = AUTHOR;
        }
        lineIndex++;
      }
      else
      if (c == '(') {
        expecting = SOURCE_LINK;
        lineIndex++;
      }
      else
      if (c == ')') {
        expecting = AUTHOR;
        lineIndex++;
      }
      else
      if (c == ',') {
        expecting = SOURCE;
        lineIndex++;
      }
    } // if we haven't run out of characters
  } // end method anotherWord
  
  private void openBody() {
    if (! bodyOpen) {
      bodyOpen = true;
      openParagraph();
    }
  }
  
  private void openBlockQuote() {
    if (! blockQuoteOpen) {
      item.bodyStartElement(BLOCK_QUOTE, false);
      blockQuoteOpen = true;
    }
  }
  
  private void openParagraph() {
    if (! paragraphOpen) {
      // item.bodyStartElement(PARAGRAPH, false);
      paragraphOpen = true;
      paragraph = new StringBuilder();
    }
  }
  
  private void closeBody() {
    if (bodyOpen) {
      closeParagraph();
      closeBlockQuote();
      item.bodyClose();
      bodyOpen = false;
    }
  }
  
  private void closeBlockQuote() {
    if (blockQuoteOpen) {
      closeParagraph();
      item.bodyEndElement(BLOCK_QUOTE);
      blockQuoteOpen = false;
    }
  }
  
  private void closeParagraph() {
    if (paragraphOpen) {
      if (paragraph.length() > 0) {
        item.bodyAppend(paragraph.toString(), true);
      }
      item.bodyEndElement(PARAGRAPH);
      paragraphOpen = false;
    }
  } 
  
  private void readLine() {
    line = in.readLine();
    fortuneStart = (line.length() > 0 && line.charAt(0) == '%');
  }
  
}
