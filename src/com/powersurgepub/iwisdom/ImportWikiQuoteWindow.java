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

package com.powersurgepub.iwisdom;

import com.powersurgepub.psdatalib.psdata.values.Author;
import com.powersurgepub.psdatalib.psdata.widgets.TextSelector;
  import com.powersurgepub.psdatalib.psdata.*;
  import com.powersurgepub.psdatalib.ui.*;
  import com.powersurgepub.psdatalib.txbio.*;
  import com.powersurgepub.psdatalib.txbmodel.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.psutils.*;
  import java.io.*;
  import java.net.*;

/**
 A window that will allow the user to specify a quotation to be imported
 from the www.WikiQuote.org Web site.
 */
public class ImportWikiQuoteWindow 
    extends javax.swing.JFrame
      implements 
        TextHandler,
        WindowToManage {

  private static final String HREF_ISBN_PREFIX = "/wiki/Special:BookSources/";

  private iWisdomCommon   td;
  private TextSelector    authorTextSelector;
  private Author          author;
  private String          urlString = "";
  private HTMLFile        html;
  private HTMLTag         tag;

  private int             whereAreWe  = JUST_STARTING;
  private static final int  JUST_STARTING   =  0;
  private static final int  END_OF_PAGE     = -1;
  private static final int  IN_QUOTES       =  1;
  private static final int  IN_QUOTE        =  2;
  private static final int  IN_QUOTE_TEXT   =  3;
  private static final int  QUOTE_COMPLETE  =  5;

  private int             whereAreWeInSource = NO_SOURCE;
  private static final int  NO_SOURCE       = 0;
  private static final int  SOURCE_STARTED  = 1;
  private static final int  SOURCE_TITLE    = 2;
  private int             whereAreWeInMinorSource = NO_MINOR_SOURCE;
  private static final int  NO_MINOR_SOURCE = 0;
  private static final int  MINOR_SOURCE_STARTED = 1;
  private boolean         sourced     = false;
  private boolean         pageOpen    = false;
  private String          containing  = "";
  private StringBuilder   sourceTitle = new StringBuilder();
  private String          isbn = "";
  private StringBuilder   minorSourceText = new StringBuilder();
  private StringBuilder   text        = new StringBuilder();
  private String          textTag        = "";
  private String          lastLanguage   = "";
  private String          lastAuthor     = "";
  private String          lastContaining = "";

  /** Creates new form ImportWikiQuoteWindow */
  public ImportWikiQuoteWindow(iWisdomCommon td) {
    this.td = td;
    initComponents();
    this.setTitle (Home.getShared().getProgramName() + " Import from WikiQuote");
    this.setBounds (100, 100, 600, 300);

    authorTextSelector = new TextSelector();
    authorTextSelector.setEditable(true);
    /*authorTextSelector.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        authorActionPerformed(evt);
      }
    }); */

    java.awt.GridBagConstraints gridBagConstraints;
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 2;
    gridBagConstraints.ipady = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    this.add(authorTextSelector, gridBagConstraints);
    authorTextSelector.addTextHandler (this);
  }

  public void initItems() {

    authorTextSelector.setValueList (td.items.getAuthors().getValueList());

  }

  public void textSelectionComplete () {
    checkReadyForSearch();
  } // end method

  private void checkReadyForSearch() {
    boolean readyForSearch =
        (workTextField.getText().length() > 0
          || authorTextSelector.getText().length() > 0);
    searchButton.setEnabled(readyForSearch);
    nextButton.setEnabled(false);
    importButton.setEnabled(false);
    browseButton.setEnabled(readyForSearch);
  }

  /**
   Initiate a search of a particular author's page on WikiQuote.org.
   */
  private void startSearch() {
    urlString = formURL();
    author = new Author(authorTextSelector.getText());
    // System.out.println ("WikiQuote URL is " + urlString);
    containing = containingTextField.getText();
    lastLanguage = (String)languageComboBox.getSelectedItem();
    lastAuthor = authorTextSelector.getText();
    lastContaining = containingTextField.getText();
    pageOpen = false;

    try {
      URL url = new URL(urlString);
      html = new HTMLFile(url);
      html.openForInput();
      pageOpen = true;
      whereAreWe = JUST_STARTING;
      whereAreWeInSource = NO_SOURCE;
      sourced = false;
      text = new StringBuilder();
    } catch (MalformedURLException e) {
      pageOpen = false;
      quoteTextArea.setText("Malformed URL: " + urlString);
      Logger.getShared().recordEvent(
          LogEvent.MEDIUM,
          "WikiQuote Malformed URL: " + urlString,
          false);
    } catch (IOException e) {
      pageOpen = false;
      quoteTextArea.setText("Page not found: " + urlString);
      Logger.getShared().recordEvent(
          LogEvent.MINOR,
          "WikiQuote Page not found: " + urlString,
          false);
    }
  }

  /**
   Search for the next quote on the page that matches the search criteria.
   Stop when you've found one, or when you've reached end of page.
   */
  private void nextQuote() {
    quoteTextArea.setText("");
    if (whereAreWe > IN_QUOTES) {
      whereAreWe = IN_QUOTES;
    }
    boolean contains = false;
    try {
      tag = html.readTag();
      while (whereAreWe > END_OF_PAGE 
          && (whereAreWe < QUOTE_COMPLETE || (! contains))) {
        if (tag == null) {
          whereAreWe = END_OF_PAGE;
        } else {

          // System.out.println ("*** Next Tag ***");
          // System.out.println (tag.toString());

          // Extract frequently used fields
          String tagName = tag.getName();
          String klass = tag.getAttributeValue(TextType.CLASS);
          if (klass == null) {
            klass = "";
          }
          String id = tag.getAttributeValue(TextType.ID);
          if (id == null) {
            id = "";
          }
          String href = tag.getAttributeValue(TextType.HREF);
          if (href == null) {
            href = "";
          }

          // Look for indicator that we are in the quotations 
          if (tagName.equalsIgnoreCase(TextType.SPAN)
              && (! tag.isEnding())
              && klass.equals("mw-headline")) {
            whereAreWe = IN_QUOTES;
            if (id.equals("Sourced")) {
              sourced = true;
            }
            else
            if (id.equals("Unsourced")) {
              sourced = false;
            }
          }

          // Look for beginning of a major source definition
          if (sourced
              && whereAreWe >= IN_QUOTES
              && tag.isHeadingTag()
              && tag.getHeadingLevel() == 3
              && (! tag.isEnding())) {
            sourceTitle = new StringBuilder();
            minorSourceText = new StringBuilder();
            isbn = "";
            whereAreWeInSource = SOURCE_STARTED;
          }

          // Look for start of source title
          if (tagName.equalsIgnoreCase(TextType.SPAN)
              && (! tag.isEnding())
              && klass.equals("mw-headline")
              && whereAreWeInSource >= SOURCE_STARTED) {
            whereAreWeInSource = SOURCE_TITLE;
          }

          // Look for source title text
          if (whereAreWeInSource >= SOURCE_TITLE) {
            sourceTitle.append(tag.getPrecedingText());
          }

          // Look for end of a major source definition
          if (sourced
              && whereAreWeInSource >= SOURCE_STARTED
              && tag.isHeadingTag()
              && tag.getHeadingLevel() == 3
              && (tag.isEnding())) {
            // System.out.println("Source Title: [" + sourceTitle.toString() + "]");
            whereAreWeInSource = NO_SOURCE;
          }

          // Look for ISBN of source
          if (whereAreWe >= IN_QUOTES
              && sourced
              && tagName.equalsIgnoreCase(TextType.ANCHOR)
              && (! tag.isEnding())
              && sourceTitle.length() > 0
              && href.startsWith(HREF_ISBN_PREFIX)
              && href.length() >= (HREF_ISBN_PREFIX.length() + 10)) {
            isbn = href.substring(HREF_ISBN_PREFIX.length());
            // System.out.println ("ISBN: " + isbn);
          }

          // Look for the beginning of a quotation
          if (whereAreWe >= IN_QUOTES
              && (! tag.isEnding())
              && ((tagName.equalsIgnoreCase(TextType.LIST_ITEM)
                  && tag.getListLevel() == 1))
                || (tagName.equalsIgnoreCase(TextType.SPAN)
                  && klass.equalsIgnoreCase("citation"))) {
            text = new StringBuilder();
            minorSourceText = new StringBuilder();
            textTag = tagName;
            whereAreWe = IN_QUOTE_TEXT;
          }

          // Collect the text of the quotation into a string buffer
          if (whereAreWe == IN_QUOTE_TEXT) {
            text.append(tag.getPrecedingText());
            if (tag.isListTag()
                || tagName.equalsIgnoreCase(TextType.ANCHOR)
                || tagName.equalsIgnoreCase(TextType.SPAN)) {
              // Ignore these tags
            } else {
              text.append(tag.getHTMLTag());
            }
          }

          // Look for end of quotation text and beginning of
          // minor source citation following the quotation
          if (sourced
              && whereAreWe >= IN_QUOTE
              && tagName.equalsIgnoreCase(TextType.LIST_ITEM)
              && (! tag.isEnding())
              && tag.getListLevel() == 2) {
            whereAreWe = IN_QUOTE;
            whereAreWeInMinorSource = MINOR_SOURCE_STARTED;
            minorSourceText = new StringBuilder();
            // System.out.println ("Start of Minor Source Citation");
          }

          // Collect the text of the minor source citation into a string buffer
          if (whereAreWeInMinorSource >= MINOR_SOURCE_STARTED) {
            minorSourceText.append(tag.getPrecedingText());
            // System.out.println ("  text = " + tag.getPrecedingText());
            if (tag.isListTag()
                || tagName.equalsIgnoreCase(TextType.ANCHOR)) {
              // Ignore these tags
            } else {
              minorSourceText.append(tag.getHTMLTag());
            }
          }

          // Look for end of minor source citation
          if (whereAreWeInMinorSource >= MINOR_SOURCE_STARTED
              && tagName.equalsIgnoreCase(TextType.LIST_ITEM)
              && tag.isEnding()
              && tag.getListLevel() == 2) {
            // System.out.println ("End of minor source citation");
            // System.out.println ("Gathered text: " + minorSourceText.toString());
            
            whereAreWeInMinorSource = NO_MINOR_SOURCE;
          } // end if we found a closing list item for the minor source

          // Look for end of quotation
          if (whereAreWe >= IN_QUOTE
              && tag.isEnding()
              && tagName.equalsIgnoreCase(textTag)
              && ((! textTag.equalsIgnoreCase(TextType.LIST_ITEM))
                || tag.getListLevel() == 1)) {
            whereAreWe = QUOTE_COMPLETE;
            if (containing.length() < 1) {
              contains = true;
            } else {
              int at = text.indexOf(containing);
              contains = (at >= 0);
            }
            if (contains) {
              // If quotation contains bolded phrases, then remove the
              // bold tags
              int i = 0;
              while (i >= 0) {
                i = text.indexOf("<b>");
                if (i >= 0) {
                  int j = text.indexOf("</b>", i + 3);
                  if (j >= 0) {
                    text.delete(j, j + 4);
                    text.delete(i, i + 3);
                  }
                }
              }
              // Remove any enclosing quotation marks
              if (text.length() > 2
                  && text.charAt(0) == '"'
                  && text.charAt(text.length() - 1) == '"') {
                text.deleteCharAt(0);
                text.deleteCharAt(text.length() - 1);
              }
              // for (int i = 1; i < text.length(); i++) {
              //   char ch = text.charAt(i);
              //   System.out.println ("  " + ch + " - " + String.valueOf((int) ch));
              // }
              // wisdom.setBody((HTMLEntities.htmlentities(text.toString())));
              
              // wisdom.getBodyAsMarkup().displayEverything();
              quoteTextArea.setText(text.toString());
            }
          } // end if end of quotation

          // Look for end of all quotations on the page
          if (tagName.equalsIgnoreCase(TextType.SPAN)
              && (! tag.isEnding())
              && id.equals("External_links")) {
            whereAreWe = END_OF_PAGE;
            whereAreWeInSource = NO_SOURCE;
          }

          // Read another HTML tag
          tag = html.readTag();
        } // end if last tag not null
      } // end while still looking for next quote
    } catch (IOException e) {
      System.out.println ("trouble opening url");
    }

    if (whereAreWe == QUOTE_COMPLETE) {
      importButton.setEnabled(true);
    } else {
      importButton.setEnabled(false);
    }
    if (whereAreWe >= IN_QUOTES) {
      nextButton.setEnabled(true);
    } else {
      nextButton.setEnabled(false);
      closePage();
    }
  }

  private void importQuote () {
    
    if (text.length() > 0) {
      WisdomSource source = new WisdomSource();
      WisdomItem wisdom = new WisdomItem();
      wisdom.setAuthor(author.toString());
      wisdom.setBody(text.toString());

      // Scan major source citation, if there is one
      if (sourceTitle.length() > 0) {
        int i = sourceTitle.lastIndexOf("(");
        if (i >= 0) {
          int j = sourceTitle.lastIndexOf(")");
          if (j > (i + 2)) {
            source.setYear(sourceTitle.substring(i + 1, j));
            sourceTitle.delete(i, j + 1);
            source.setTitle(sourceTitle.toString());
          }
        }
        if (isbn.length() > 0) {
          source.setID(isbn);
          source.setType(WisdomSource.BOOK);
        }
      } // end if we have a major source title

      if (minorSourceText.length() > 0) {
        extractMinorSourceInfo(source);
      }
      wisdom.setSource(source);
      td.importOneItem(wisdom, "WikiQuote.org");
    }
  }

  private void extractMinorSourceInfo (WisdomSource source) {

    // Look for a major title
    int startOfLeftDelimiter = minorSourceText.indexOf("<i>");
    int endOfLeftDelimiter = startOfLeftDelimiter + 3;
    if (startOfLeftDelimiter < 0) {
      startOfLeftDelimiter = minorSourceText.indexOf("<cite>");
      endOfLeftDelimiter = startOfLeftDelimiter + 6;

    }
    int startOfRightDelimiter = -1;
    int endOfRightDelimiter = -1;
    if (startOfLeftDelimiter >= 0) {
      startOfRightDelimiter = minorSourceText.indexOf("</i>", endOfLeftDelimiter);
      endOfRightDelimiter = startOfRightDelimiter + 4;
      if (startOfRightDelimiter < 0) {
        startOfRightDelimiter = minorSourceText.indexOf("</cite>", endOfLeftDelimiter);
        endOfRightDelimiter = startOfRightDelimiter + 7;
      }
      if (startOfRightDelimiter > endOfLeftDelimiter) {
        source.setTitle(minorSourceText.substring
            (endOfLeftDelimiter, startOfRightDelimiter));
        // System.out.println("Major Title: " + minorSourceText.substring
        //     (endOfLeftDelimiter, startOfRightDelimiter));
        minorSourceText.delete(startOfLeftDelimiter, endOfRightDelimiter);
      } // end if we found a suitable right parenthesis
    } // end if we found a left parenthesis

    // Look for a year
    int i = 0;
    boolean yearFound = false;
    while (i >= 0 && (! yearFound)) {
      startOfLeftDelimiter = minorSourceText.indexOf("(", i);
      i = startOfLeftDelimiter;
      if (startOfLeftDelimiter >= 0) {
        endOfLeftDelimiter = startOfLeftDelimiter + 1;
        i = endOfLeftDelimiter;
        startOfRightDelimiter = minorSourceText.indexOf(")", endOfLeftDelimiter);
        if (startOfRightDelimiter > endOfLeftDelimiter) {
          endOfRightDelimiter = startOfRightDelimiter + 1;
          i = endOfRightDelimiter;
          int trailingDigits = 0;
          for (
              int k = startOfRightDelimiter - 1;
              k >= endOfLeftDelimiter
                && Character.isDigit(minorSourceText.charAt(k));
              k--) {
            trailingDigits++;
          } // end for each digit at end of string between parentheses
          if (trailingDigits >= 2 && trailingDigits <= 4) {
            yearFound = true;
            source.setYear(minorSourceText.substring
                (endOfLeftDelimiter, startOfRightDelimiter));
            // System.out.println ("Year: " + minorSourceText.substring
            //     (endOfLeftDelimiter, startOfRightDelimiter));
            minorSourceText.delete
                (startOfLeftDelimiter, endOfRightDelimiter);
          }
        } // end if we found a suitable right parenthesis
      } // end if we found a left parenthesis
    } // End of search for a left paren with a year

    // Look for a minor title
    startOfLeftDelimiter = minorSourceText.indexOf("\"");
    if (startOfLeftDelimiter >= 0) {
      endOfLeftDelimiter = startOfLeftDelimiter + 1;
      startOfRightDelimiter = minorSourceText.indexOf("\"", endOfLeftDelimiter);
      if (startOfRightDelimiter > endOfLeftDelimiter) {
        endOfRightDelimiter = startOfRightDelimiter + 1;
        source.setMinorTitle(minorSourceText.substring
            (endOfLeftDelimiter, startOfRightDelimiter));
        // System.out.println ("Minor Title: " + minorSourceText.substring
        //     (endOfLeftDelimiter, startOfRightDelimiter));
        minorSourceText.delete
            (startOfLeftDelimiter, endOfRightDelimiter);
      } // end if we found a suitable right parenthesis
    } // end if we found a left parenthesis

    // Extract source type, if present
    int sourceTypeIndex = source.scanForType(minorSourceText.toString());
    /* if (sourceTypeIndex >= 0) {
      System.out.println ("Source Type: " + WisdomSource.getTypeLabel(sourceTypeIndex));
    } else {
      System.out.println ("Source Type Not Found");
    } */

    // If letter, use the to identifer as the minor title
    if (source.getType() == WisdomSource.LETTER
        && source.getMinorTitle().length() == 0) {
      startOfLeftDelimiter = minorSourceText.indexOf(" to ");
      if (startOfLeftDelimiter >= 0) {
        endOfLeftDelimiter = startOfLeftDelimiter + 1;
        startOfRightDelimiter = startOfLeftDelimiter + 4;
        while (i < minorSourceText.length()
            && (Character.isLetter(minorSourceText.charAt(startOfRightDelimiter))
              || Character.isDigit(minorSourceText.charAt(startOfRightDelimiter))
              || Character.isWhitespace(minorSourceText.charAt(startOfRightDelimiter)))) {
          startOfRightDelimiter++;
        }
        startOfRightDelimiter--;
        while (Character.isWhitespace(
            minorSourceText.charAt(startOfRightDelimiter))) {
          startOfRightDelimiter--;
        }
        startOfRightDelimiter++;
        if (startOfRightDelimiter > (startOfLeftDelimiter + 4)) {
          endOfRightDelimiter = startOfRightDelimiter;
          if (startOfLeftDelimiter < minorSourceText.length()) {
            endOfRightDelimiter++;
          }
          source.setMinorTitle(minorSourceText.substring
            (endOfLeftDelimiter, startOfRightDelimiter));
          // System.out.println ("Minor Title: " + minorSourceText.substring
          //   (endOfLeftDelimiter, startOfRightDelimiter));
          minorSourceText.delete
              (startOfLeftDelimiter, endOfRightDelimiter);
        } // end if we found something following the to
      } // end if we found a to
    } // end if a letter and no minor title yet

  }

  /**
   Given a string in StringBuilder format, and a starting point, extract the
   tag name.

   @param s The string from which the tag name is to be extracted.
   @param index The starting position of the tag.
   
   @return The tag name, converted to lower case.
   */
  private String getTagName (StringBuilder s, int index) {
    int i = index;
    if (s.charAt(i) == '<') {
      i++;
    }
    if (s.charAt(i) == '/') {
      i++;
    }
    int j = s.indexOf(">", i);
    if (j < 0) {
      return "";
    } else {
      int k = i + 1;
      while (k < j
          && (! Character.isWhitespace(s.charAt(k)))) {
        k++;
      }
      return s.substring(i, k).toLowerCase();
    }
  }

  private void browseWikiQuote() {
    td.openURL(formURL());
  }

  private String formURL() {
    String language = (String)languageComboBox.getSelectedItem();
    author = new Author(authorTextSelector.getText());
    if (workTextField.getText().length() > 0) {
      return Author.getWikiquoteLink(
          language,
          workTextField.getText());
    } else {
      return author.getWikiquoteLink(language);
    }
  }

  private void closePage() {
    if (pageOpen) {
      html.close();
      pageOpen = false;
    }
    nextButton.setEnabled(false);
    importButton.setEnabled(false);
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    languageLabel = new javax.swing.JLabel();
    languageComboBox = new javax.swing.JComboBox();
    authorLabel = new javax.swing.JLabel();
    workLabel = new javax.swing.JLabel();
    workTextField = new javax.swing.JTextField();
    containingLabel = new javax.swing.JLabel();
    containingTextField = new javax.swing.JTextField();
    searchButton = new javax.swing.JButton();
    nextButton = new javax.swing.JButton();
    importButton = new javax.swing.JButton();
    browseButton = new javax.swing.JButton();
    quoteLabel = new javax.swing.JLabel();
    quoteScrollPane = new javax.swing.JScrollPane();
    quoteTextArea = new javax.swing.JTextArea();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentHidden(java.awt.event.ComponentEvent evt) {
        formComponentHidden(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());

    languageLabel.setText("Language:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(languageLabel, gridBagConstraints);

    languageComboBox.setEditable(true);
    languageComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "de - German", "en - English", "es - Spanish", "fr - French", "it - Italian", "pl - Polish", "pt - Portugese", "ru", "sk", "tr" }));
    languageComboBox.setSelectedIndex(1);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(languageComboBox, gridBagConstraints);

    authorLabel.setText("Author:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(authorLabel, gridBagConstraints);

    workLabel.setText("Work:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(workLabel, gridBagConstraints);

    workTextField.setToolTipText("If a work has its own page in WikiQuote, then enter it here. ");
    workTextField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        workTextFieldActionPerformed(evt);
      }
    });
    workTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        workTextFieldFocusLost(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(workTextField, gridBagConstraints);

    containingLabel.setText("Containing:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(containingLabel, gridBagConstraints);

    containingTextField.setToolTipText("Look for a quote containing the text entered here, or leave blank to browse all quotes");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(containingTextField, gridBagConstraints);

    searchButton.setText("Search");
    searchButton.setEnabled(false);
    searchButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        searchButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(searchButton, gridBagConstraints);

    nextButton.setText("Next");
    nextButton.setEnabled(false);
    nextButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        nextButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(nextButton, gridBagConstraints);

    importButton.setText("Import");
    importButton.setEnabled(false);
    importButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        importButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(importButton, gridBagConstraints);

    browseButton.setText("Browse");
    browseButton.setEnabled(false);
    browseButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        browseButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(browseButton, gridBagConstraints);

    quoteLabel.setText("Quotation:");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(quoteLabel, gridBagConstraints);

    quoteTextArea.setColumns(20);
    quoteTextArea.setLineWrap(true);
    quoteTextArea.setRows(5);
    quoteTextArea.setWrapStyleWord(true);
    quoteScrollPane.setViewportView(quoteTextArea);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    getContentPane().add(quoteScrollPane, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
  startSearch();
  if (pageOpen) {
    nextQuote();
  }
}//GEN-LAST:event_searchButtonActionPerformed

private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
  closePage();
  WindowMenuManager.getShared().hide(this);
}//GEN-LAST:event_formComponentHidden

private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
  if (pageOpen
      && html != null
      && lastLanguage.equals((String)languageComboBox.getSelectedItem())
      && lastAuthor.equals(authorTextSelector.getText())
      && lastContaining.equals(containingTextField.getText())
      && whereAreWe > END_OF_PAGE) {
    nextQuote();
  } else {
    nextButton.setEnabled(false);
  }
}//GEN-LAST:event_nextButtonActionPerformed

private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
  importQuote();
}//GEN-LAST:event_importButtonActionPerformed

private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
  browseWikiQuote();
}//GEN-LAST:event_browseButtonActionPerformed

private void workTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workTextFieldActionPerformed
  checkReadyForSearch();
}//GEN-LAST:event_workTextFieldActionPerformed

private void workTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_workTextFieldFocusLost
  checkReadyForSearch();
}//GEN-LAST:event_workTextFieldFocusLost


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel authorLabel;
  private javax.swing.JButton browseButton;
  private javax.swing.JLabel containingLabel;
  private javax.swing.JTextField containingTextField;
  private javax.swing.JButton importButton;
  private javax.swing.JComboBox languageComboBox;
  private javax.swing.JLabel languageLabel;
  private javax.swing.JButton nextButton;
  private javax.swing.JLabel quoteLabel;
  private javax.swing.JScrollPane quoteScrollPane;
  private javax.swing.JTextArea quoteTextArea;
  private javax.swing.JButton searchButton;
  private javax.swing.JLabel workLabel;
  private javax.swing.JTextField workTextField;
  // End of variables declaration//GEN-END:variables

}
