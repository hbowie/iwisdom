package com.powersurgepub.iwisdom;

  import com.powersurgepub.psdatalib.markup.*;
	import com.powersurgepub.psutils.*;
  import com.powersurgepub.iwisdom.data.*;
  import com.powersurgepub.iwisdom.disk.*;
  import java.util.*;
  import javax.swing.event.*;
  import javax.swing.text.html.*;
  import java.text.*;

/**
   A panel to display a rotating selection of current to do items. <p>
  
   This code is copyright (c) 2005 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/08/31 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 2003/11/09 - Added Category field. 
 */
public class DisplayTab 
    extends javax.swing.JPanel 
      implements HyperlinkListener, WisdomTab {
  
  private String startCategoryParagraph 
      = "<p><font size=3 face=\"Lucida Grande, Verdana, Arial, sans-serif\"><em>";
  private String horizontalRule = "<hr>";
  private String startTitleParagraph 
      = "<p><font size=4 face=\"Lucida Grande, Verdana, Arial, sans-serif\"><b>";
  private String startNormalParagraph 
      = "<p><font size=3 face=\"Lucida Grande, Verdana, Arial, sans-serif\">";
  private String endCategoryParagraph
      = "</em></font></p>";
  private String endTitleParagraph 
      = "</b></font></p>";
  private String endNormalParagraph
      = "</font></p>";
  
  private Trouble trouble = Trouble.getShared();
  
  private iWisdomCommon td;
  
  String endSpecialTag = "";
  
  private StringBuffer text;
  
  private DateFormat dateFormat = new SimpleDateFormat ("EEEE MMMM d, yyyy");
  
  /** Creates new form DisplayTab */
  public DisplayTab(iWisdomCommon td) {
    this.td = td;
    initComponents();
    rotateEditorPane.addHyperlinkListener (this);
    HTMLEditorKit kit = (HTMLEditorKit) rotateEditorPane.getEditorKit();
    StyleSheet styles = kit.getStyleSheet();
    Enumeration rules = styles.getStyleNames();
   	/* while (rules.hasMoreElements()) {
   	    String name = (String) rules.nextElement();
   	    Style rule = styles.getStyle(name);
   	    Logger.getShared().recordEvent (LogEvent.NORMAL, 
          "Display Stylesheet rule " + rule.toString(),
          false);
   	} */
    // kit.setStyleSheet(styles);
  }
  
  /**
    Prepares the tab for processing of newly opened file.
   
    @param store Disk Store object for the file.
   */
  public void filePrep (WisdomDiskStore store) {
    // No file information used on the About Tab
  }
  
  public void initItems() {

  }
  
  public void displayItem() {

    text = new StringBuffer();
    text.append ("<html>");
    text.append ("<body bgcolor=\"#" 
        + StringUtils.colorToHexString(td.rotateBackgroundColor)
        + "\" text=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" link=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" alink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\" vlink=\"#"
        + StringUtils.colorToHexString(td.rotateTextColor)
        + "\">");
    /* text.append ("<table border=0><tr>");
       text.append ("<td>&nbsp;</td>");
       text.append ("<td>");
    */
    WisdomItem item = td.getItem();
    // itemIDText.setText (item.getID());
    
    // Display Category in italics
    String category = "";
    if (td.getCategories().size() > 0) {
      category = item.getCategory().toString();
    }
    appendParagraph ("em", 0, "", "", category);
    
    // Display Title in bold and increased size
    if (td.displayTitle) {
      appendParagraph ("b", 1, "", "", item.getTitle());
    }
    
    // Display Body, if there is any
    String description = item.getBody(MarkupElement.OLD_HTML, true);
    if (description.length() > 0) {
      startFont(0);
      text.append(description);
      // appendParagraph ("", 0, "", "", description);
      endFont();
    }
    
    // Display divider
    
    text.append ("<br><hr>");
    
    // Display Author, if any
    String authorCompleteName = item.getAuthorCompleteName();
    if (authorCompleteName.length() > 0) {
      Author author = item.getAuthor();
      int numberOfAuthors = author.getNumberOfAuthors();
      if (author.isCompound()) {
        startParagraph ("", 0, "Authors");
        for (int i = 0; i < numberOfAuthors; i++) {
          Author nextAuthor = author.getAuthor (i);
          appendItem (
              nextAuthor.getALink(), 
              nextAuthor.getCompleteName(),  
              nextAuthor.getWikiquoteLink(),
              "Wikiquote",
              i, 
              numberOfAuthors);
        }
      } else {
        startParagraph ("", 0, "Author");
        appendItem (
            author.getALink(), 
            authorCompleteName,  
            author.getWikiquoteLink(),
            "Wikiquote",
            0, 1);
      }
      String authorInfo = item.getAuthorInfo();
      if (authorInfo.length() > 0) {
        appendItem ("", ", " + authorInfo, 0, 1);
      }
      endParagraph();
    }
    
    // Display Source, if any
    if (td.displaySource) {
      String sourceType = item.getSourceTypeLabel();
      String source = item.getSourceAsString();
      String url = item.getASourceLink();
      String minorTitle = item.getMinorTitle();
      String pages = item.getPages();
      if ((source.length() > 0 
            && (! source.equalsIgnoreCase (WisdomSource.UNKNOWN)))
          || (minorTitle.length() > 0)) {

        startParagraph ("", 0, "Source");

        if (td.displayType
            && sourceType.length() > 0
            && (! sourceType.equalsIgnoreCase(WisdomSource.UNKNOWN))) {
          text.append ("the " + item.getSourceTypeLabel().toLowerCase() + ", ");
        }

        if (source.length() > 0 
            && (! source.equalsIgnoreCase (WisdomSource.UNKNOWN))) {
          text.append("<cite>");
          appendItem (url, source, 0, 1);
          text.append("</cite>");
          if (minorTitle.length() > 0) {
            text.append(", ");
          }
        }
        if (minorTitle.length() > 0) {
          text.append("\"" + minorTitle + "\"");
        }
        if (pages.length() > 0) {
          StringBuffer pagesLabel = new StringBuffer("page");
          if (pages.indexOf("-") > 0) {
            pagesLabel.append("s");
          }
          text.append(", " + pagesLabel + " " + pages);
        }

        endParagraph();

        /*
        if (url.length() > 0) {
          appendParagraph ("", 0, url, "Source", source);
        } else {
          appendParagraph ("", 0, "", "Source", source);
        }
        if (td.displayType) {
          appendParagraph ("", 0, "",  "Type", item.getSourceTypeLabel());
        }
        if (item.getPages().length() > 0) {
          appendParagraph ("", 0, "", "Page(s)", item.getPages());
        }
         */
        StringBuffer publisher = new StringBuffer(item.getCity());
        if (publisher.length() > 0) {
          publisher.append (": ");
        }
        publisher.append (item.getPublisher());
        if (publisher.length() > 0) {
          appendParagraph ("", 0, "", "Publisher", publisher.toString());
        }
        
      }

      /*
      if (minorTitle.length() > 0) {
        appendParagraph ("", 0, "", "Minor Title", minorTitle);
      }
       */
    }
    
    // Display Rights / Publication Year
    String yearRightsLabel = "First Published";
    StringBuffer yearRights = new StringBuffer();
    String rights = item.getRights();
    if (rights.length() > 0
        && rights.startsWith ("Copyright")) {
      yearRights.append ("Copyright &copy;");
    } else {
      yearRights.append (item.getRights());
    }
    if (yearRights.length() > 0) {
      yearRightsLabel = "Rights";
    }
    if (! item.getYear().equals("")) {
      if (yearRights.length() > 0) {
        yearRights.append (" ");
      }
      yearRights.append (item.getYear());
    }
    if (item.getRightsOwner().length() > 0) {
      if (yearRights.length() > 0) {
        yearRights.append (" by ");
      }
      yearRights.append (item.getRightsOwner());
      yearRightsLabel = "Rights";
    }
    if (yearRights.length() > 0) {
      appendParagraph ("", 0, "", yearRightsLabel, yearRights.toString());
    }
    
    // Display priority / rating if anything unusual
    int priority = item.getRating();
    if (priority != 3) {
      appendParagraph ("", 0, "", "Rating", 
          String.valueOf (priority) + " " + item.getRatingLabel());
    }
    
    // Display Date Added
    if (td.displayAdded) {
      appendParagraph ("", 0, "", "Added", 
          item.getDateAddedDisplay());
    }
    
    // Display Item ID
    if (td.displayID) {
      appendParagraph ("", 0,
          // item.getItemIDLink(td.collectionWindow),
          "", "ID",
          String.valueOf (item.getItemID()));
    }
    
    // Display link to Web
    /*
    StringBuffer webLink = new StringBuffer (td.collectionWindow.getLink());
    if (webLink.length() > 0) {
      String path = td.collectionWindow.getPath();
      if (path.length() > 0) {
        webLink.append (path);
        webLink.append ("/");
      }
      webLink.append ("html/authors/");
      webLink.append (item.getAuthorFileName());
      webLink.append ("/");
      webLink.append (item.getSourceFileName());
      webLink.append ("/");
      webLink.append (item.getTitleFileName());
      webLink.append (".html");
      appendParagraph ("", 0, item.getItemLink(td.collectionWindow), "Published at", 
          td.collectionWindow.getCollectionTitle());
    } */
    
    /* text.append ("</td>");
       text.append ("<td>&nbsp;</td>");
       text.append ("</tr></table>");
    */
    text.append ("</body>");
    text.append ("</html>");
    // Logger.getShared().recordEvent(LogEvent.NORMAL, text.toString(), false);
    rotateEditorPane.setText (text.toString());
    rotateEditorPane.setCaretPosition(0);
  }  
  
  public void appendParagraph (
      String specialTag, 
      int fontVariance, 
      String href,
      String label, 
      String body) {
    
    startParagraph (specialTag, fontVariance, label);

    appendItem (href, body, 0, 1);
    
    endParagraph();
  }
  
  public void startParagraph (
      String specialTag, 
      int fontVariance, 
      String label) {

    String startSpecialTag = "";
    endSpecialTag = "";
    if (specialTag.length() > 0) {
      startSpecialTag = "<" + specialTag + ">";
      endSpecialTag = "</" + specialTag + ">";
    }
    String intro = "";
    if (label.length() > 0) {
      intro = label + ": ";
    }
    text.append ("<p>");
    startFont(fontVariance);
    text.append (
        startSpecialTag +
        intro);
  }

  public void startFont (int fontVariance) {
    int fontSize = td.rotateNormalFontSize + fontVariance;
    if (fontSize < 1) {
      fontSize = 1;
    }
    if (fontSize > 7) {
      fontSize = 7;
    }
    text.append (
        "<font size=" +
        String.valueOf (fontSize) +
        " face=\"" +
        td.rotateFont +
        ", Verdana, Arial, sans-serif\">");
  }

  public void appendItem (
      String href, 
      String body,
      int listPosition,
      int listLength) {
    
    appendItem (href, body, "", "", listPosition, listLength);
  }
  
  public void appendItem (
      String href, 
      String body,
      String parenHref,
      String parenthetical,
      int listPosition,
      int listLength) {
    
    String startLink = "";
    String endLink = ""; 
    String listSuffix = "";
    if (href != null && href.length() > 0) {
      startLink = "<a href=\"" + href + "\">";
      endLink = "</a>";
    }
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
    String parenPrefix = "";
    String parenSuffix = "";
    String parenStartLink = "";
    String parenEndLink = "";
    if (parenthetical.length() > 0) {
      parenPrefix = " (";
      parenSuffix = ")";
      if (parenHref.length() > 0) {
        parenStartLink = "<a href=\"" + parenHref + "\">";
        parenEndLink = "</a>";
      }
    }
    text.append (
        startLink +
        body +
        endLink +
        parenPrefix +
        parenStartLink +
        parenthetical +
        parenEndLink +
        parenSuffix +
        listSuffix);
  }
  
  public void endParagraph () {
    text.append (endSpecialTag);
    endFont();
    text.append ("</p>");
  }

  public void endFont () {
    text.append ("</font>");
  }
  
  public void hyperlinkUpdate (HyperlinkEvent e) {
    HyperlinkEvent.EventType type = e.getEventType();
    if (type == HyperlinkEvent.EventType.ACTIVATED) {
      td.openURL (e.getURL());
    }
  }
  
  /**
    Prepare to switch tabs and show this one.
   */
  private void showThisTab () {
    td.switchTabs();
  }
  
  /**
   Modifies the td.item if anything on the screen changed. 
   
   @return True if any of the data changed on this tab. 
   */
  public boolean modIfChanged () {
    return false;
  } // end method
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    rotateScrollPane = new javax.swing.JScrollPane();
    rotateEditorPane = new javax.swing.JEditorPane();

    addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent evt) {
        formComponentShown(evt);
      }
    });
    addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        formFocusLost(evt);
      }
    });
    setLayout(new java.awt.GridBagLayout());

    rotateScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    rotateScrollPane.setViewportBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

    rotateEditorPane.setContentType("text/html"); // NOI18N
    rotateEditorPane.setEditable(false);
    rotateEditorPane.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        rotateEditorPaneMouseClicked(evt);
      }
    });
    rotateScrollPane.setViewportView(rotateEditorPane);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    add(rotateScrollPane, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
    // Does nothing useful
  }//GEN-LAST:event_formFocusLost

  private void rotateEditorPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rotateEditorPaneMouseClicked
    // td.activateItemTab();
  }//GEN-LAST:event_rotateEditorPaneMouseClicked

  private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    showThisTab();
    td.endRotation();
    // aboutJavaText.requestFocus();
  }//GEN-LAST:event_formComponentShown
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JEditorPane rotateEditorPane;
  private javax.swing.JScrollPane rotateScrollPane;
  // End of variables declaration//GEN-END:variables
  
}
