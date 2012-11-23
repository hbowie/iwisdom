package com.powersurgepub.iwisdom.data;

  import com.powersurgepub.psutils.*;
  import java.util.*;

/**
   An object representing the category of a To Do item. A 
   category may consist of multiple levels, with the first
   level being the primary category and subsequent levels being
   sub-categories. <p>
  
   This code is copyright (c) 2003-2004 by Herb Bowie.
   All rights reserved. <p>
  
   Version History: <ul><li>
      2003/11/17 - Originally written.
       </ul>
  
   @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
           herb@powersurgepub.com</a>)<br>
           of PowerSurge Publishing 
           (<a href="http://www.powersurgepub.com">
           www.powersurgepub.com</a>)
  
   @version 
      2004/01/17 - Modified to allow sub-categories to be set 
                   one level at a time. 
 */
public class Category {
  
  /** Preferred separator character. */
  public static final char     PREFERRED_LEVEL_SEPARATOR = '.';
  
  /** Alternate separator character. */
  public static final char     ALTERNATE_LEVEL_SEPARATOR = '/';
  
  /** Preferred separator between multiple categories. */
  public static final char     PREFERRED_CATEGORY_SEPARATOR = ',';
  
  /** Alternate separator between multiple categories. */
  public static final char     ALTERNATE_CATEGORY_SEPARATOR = ';';
  
  /** Maximum allowable number of category words. */
  public static final int      WORD_MAX = 10;
  
  /** 
    The normalized representation of zero or more nested categories,
    with periods separating each level, with commas separating
    distinct categories, and with no spaces surrounding
    the separators.
   */
  private String category = "";
  
  private int    categoryIndex = 0;
  private int    lastCategoryIndex = 0;
  private int    lastCategoryEnd = 0;
  
  private boolean endOfCategory = false;
  
  /** The number of different categories in the category string. */
  private int categories = 0;

  /** The number of levels in the category string. */
  private int words = 0;
  
  /** The starting word for this category */
  private ArrayList categoryStartingWord;
  
  /** The number of levels for this category */
  private ArrayList categoryWords;
  
  /** The starting position in the category string for each level. */
  private ArrayList wordStart;
  
  /** 
    Creates a new instance of Category with null values. 
   */
  public Category() {
    set ("");
  }
  
  /** 
    Creates a new instance of Category with a particular value.
   
    @param category A string containing one or more nested categories.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periords or slashes from
                    the words.
   */
  public Category (String category) {
    set (category);
  }
  
  /** 
    Sets the category to a particular value.
   
    @param inCat    A string containing one or more nested categories.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periords or slashes from
                    the words.
   */
  public void set(String inCat) {
    category = "";
    words = 0;
    categories = 0;
    categoryStartingWord = new ArrayList();
    categoryWords = new ArrayList();
    wordStart = new ArrayList();
    append (inCat);
  }
  
  /** 
    appends the passed string to whatever categories already exist.
   
    @param inCat    A string containing one or more nested categories.
                    Levels may be separated by periods or slashes, 
                    and spaces may separate the periords or slashes from
                    the words.
   */
  public void append (String inCat) {

    String pureCategory = StringUtils.purify(inCat).trim();
    if (pureCategory.length() > 0) {
      StringBuilder workCat = new StringBuilder(this.category);
      int i = 0;
      int j = workCat.length();
      boolean catSep = true;
      while (i < pureCategory.length()) {

        // skip leading spaces
        while (i < pureCategory.length() 
            && pureCategory.charAt(i) == ' ') {
          i++;
        } // end while next char is a space

        if (i < pureCategory.length()) {
          // Move the preferred separator into the output category string
          if (j > 0) {
            if (catSep) {
              workCat.append (PREFERRED_CATEGORY_SEPARATOR);
              workCat.append (" ");
              j++;
            } else {
              workCat.append (PREFERRED_LEVEL_SEPARATOR);
            }
            j++;
          }
          startWord (j, catSep);
        }

        // extract next category level
        while (i < pureCategory.length() 
            && (! isCategorySeparator (pureCategory.charAt(i)))
            && (! isLevelSeparator (pureCategory.charAt(i)))) {
          workCat.append (pureCategory.charAt(i));
          i++;
          j++;
        } // end of next category level

        // Set type of separator
        if (i < pureCategory.length()) {
          catSep = isCategorySeparator (pureCategory.charAt(i));
        }

        // eliminate any trailing spaces
        while (workCat.length() > 0
            && workCat.charAt (workCat.length() - 1) == ' ') {
          workCat.deleteCharAt (workCat.length() - 1);
          j--;
        }

        // Position pointer beyond delimiter that stopped our scan
        if (i < pureCategory.length()) {
          i++;
        }

      } // end while more category levels to extract

      this.category = workCat.toString();
    } // end if input category is not blank
    
    // setClosingWordStart();
    
    // System.out.println ("Category.set = " + inCat);
    // displayCategory();
  }

  /**
   For debugging purposes, display all the variables stored in the object.
   */
  public void displayCategory() {
    System.out.println (" ");
    System.out.println ("category = " + category);
    System.out.println ("number of words = " + String.valueOf (words));
    System.out.println ("number of categories = " + String.valueOf (categories));
    for (int i = 0; i < categories; i++) {
      System.out.println ("  category no. " + String.valueOf (i));
      Integer catStartingWordInteger
          = (Integer)categoryStartingWord.get (i);
      int catStartingWord = catStartingWordInteger.intValue();
      System.out.println ("  Starting word = " + String.valueOf (catStartingWord));
      int levels = getLevels (i); 
      System.out.println ("  number of levels/words = " + String.valueOf (levels));
      for (int j = 0; j < levels; j++) {
        System.out.println ("    level no. " + String.valueOf (j));
        int wordIndex = catStartingWord + j;
        System.out.println ("    word no. " + String.valueOf (wordIndex));
        int s = getWordStart (wordIndex);
        System.out.println ("    word starting position = " + String.valueOf (s));
        int e = getWordEnd (wordIndex);
        System.out.println ("    word ending   position = " + String.valueOf (e));
        System.out.println ("    word = " + getLevel (i, j));
      }
    }
  }
  
  /**
    Set one level. Note that this is only allowed if the
    level to be set is a higher number than any previously
    set levels. The bottom line is that this method may be used
    to set one level at a time, but only when the category is initially
    being populated, and only when the levels are set in ascending
    sequence by level number. 
   
    @param  word     Category string to be appended.
   
    @param  level    Level at which category is to be set, with
                     zero indicating the first level.
   */
  public void appendWord (char sepChar, String word) {
    String pureWord = StringUtils.purify(word).trim();
    StringBuilder workCat = new StringBuilder(category);
    if (workCat.length() > 0) {
      if (isLevelSeparator (sepChar)) {
        workCat.append (PREFERRED_LEVEL_SEPARATOR);
      } else {
        workCat.append (PREFERRED_CATEGORY_SEPARATOR);
        workCat.append (" ");
      }      
    }
    startWord (workCat.length(), isCategorySeparator (sepChar));
    // wordStart.add (new Integer (workCat.length()));
    workCat.append (word);
    category = workCat.toString();
    // setClosingWordStart();
    
    // System.out.println ("Category.appendWord " + String.valueOf (sepChar) 
    //     + " " + word);
    // displayCategory();
  }
  
  /**
   We've found a new word and need to set appropriate array entries.  
   
   @param index   The position in the category string at which this word
                  starts.
   
   @param catSep  Is this the beginning of w new category, or just another 
                  level/word within an existing category?
   */
  private void startWord (int index, boolean catSep) {
    words++;
    int word = wordStart.size() - 1;
    int lastWordStart = getWordStart (word);
    if (index > lastWordStart || wordStart.size() == 0) {
      wordStart.add (new Integer(index));
      word++;
    } else {
      wordStart.set (word, new Integer (index));
    }
    if (catSep || categories == 0) {
      categories++;
      categoryStartingWord.add (new Integer (word));
      categoryWords.add (new Integer (1));
    } else {
      Integer cwint = (Integer)categoryWords.get (categoryWords.size() - 1);
      int cw = cwint.intValue();
      cw++;
      cwint = new Integer (cw);
      categoryWords.set (categoryWords.size() - 1, cwint);
    }
  } // end method startWord
  
  public static boolean isLevelSeparator (char sepChar) {
    return (sepChar == PREFERRED_LEVEL_SEPARATOR
          || sepChar == ALTERNATE_LEVEL_SEPARATOR);
  }
  
  public static boolean isCategorySeparator (char sepChar) {
    return (sepChar == PREFERRED_CATEGORY_SEPARATOR
          || sepChar == ALTERNATE_CATEGORY_SEPARATOR); 
  }
  
  /**
    Set one final level start to give us an ending point
   */
  /*
  private void setClosingWordStart() {
    int sepLength = 0;
    if (category.length() > 0) {
      sepLength = 1;
    }
    wordStart.add (new Integer(this.category.length() + sepLength));
    
  } // end set method */
  
  /**
   Returns the number of distinct categories tracked for this item.
   
   @return Number of categories assigned to the item.
   */
  public int getCategories () {
    return categories;
  }
  
  /**
    Return the number of levels in the category.
   
    @return Number of levels in this category.
   */
  public int getWords() {
    return words;
  }

  public String getLinkedTags (String parent) {
    StringBuilder tags = new StringBuilder();
    for (int i = 0; i < getCategories(); i++) {
      if (i > 0) {
        tags.append(", ");
      }
      tags.append(getLinkedTag (i, parent));
    }
    return tags.toString();
  }
  
  /**
   Return the number of levels that exists for the given category. 
   
   @param categoryIndex An index pointing to the category of interest.
   
   @return The number of levels for the given categoryIndex, if valid,
           otherwise zero.
   */
  public int getLevels (int categoryIndex) {
    if (categoryIndex < 0 || categoryIndex >= categoryWords.size()) {
      return 0;
    } else {
      Integer levels = (Integer)categoryWords.get (categoryIndex);
      return levels.intValue();
    }
  }

  public String getLinkedTag (int categoryIndex, String parent) {
    StringBuilder cat = new StringBuilder();
    StringBuilder link = new StringBuilder();
    int levels = getLevels(categoryIndex);
    for (int levelIndex = 0; levelIndex < levels; levelIndex++) {
      if (levelIndex > 0) {
        cat.append(PREFERRED_LEVEL_SEPARATOR);
        link.append("-");
      }
      String catLevel = getLevel(categoryIndex, levelIndex);
      cat.append(catLevel);
      link.append(StringUtils.makeFileName(catLevel, false));
    }
    return (
        "<a href='"
        + parent
        + link.toString()
        + ".html' rel='tag'>"
        + cat.toString()
        + "</a>");
  }

  public String getTagFileName (int categoryIndex) {
    StringBuilder fileName = new StringBuilder();
    int levels = getLevels(categoryIndex);
    for (int levelIndex = 0; levelIndex < levels; levelIndex++) {
      if (levelIndex > 0) {
        fileName.append("-");
      }
      String catLevel = getLevel(categoryIndex, levelIndex);
      fileName.append(StringUtils.makeFileName(catLevel, false));
    }
    fileName.append(".html");
    return (fileName.toString());
  }

  /**
   Return one of possibly multiple categories stored herein.

   @param categoryIndex The index indicating which category is to be returned.
   @return The String containing the complete category (possibly including
           multiple levels) at the indicated index. 
   */
  public String getCategory (int categoryIndex) {
    StringBuilder cat = new StringBuilder();
    int levels = getLevels(categoryIndex);
    for (int levelIndex = 0; levelIndex < levels; levelIndex++) {
      if (levelIndex > 0) {
        cat.append(PREFERRED_LEVEL_SEPARATOR);
      }
      cat.append(getLevel(categoryIndex, levelIndex));
    }
    return cat.toString();
  }
  
  /**
   Get the word or phrase making up the given level, for the given category
   number, within the entire category string assigned to this item. 
   
   @param categoryIndex Number indicating which category is desired (first is 0).
   @param levelIndex Number indicating which level is desired within given
                     category (first is 0).
   @return Word or phrase making up this category level.
   */
  public String getLevel (int categoryIndex, int levelIndex) {
    if (categoryIndex < 0 || categoryIndex >= categoryWords.size()) {
      return "";
    }
    else
    if (levelIndex < 0 || levelIndex >= getLevels (categoryIndex)) {
      return "";
    } else {
      Integer catStartingWordInteger
          = (Integer)categoryStartingWord.get (categoryIndex);
      int catStartingWord = catStartingWordInteger.intValue();
      int wordIndex = catStartingWord + levelIndex;
      return getWord (wordIndex);
    }
  }
  
  /**
    Return the category string for a particular level.
   
    @return Category string at given level.
   
    @param  level Level at which category is desired, with
                  zero indicating the first level, or an empty string
                  if the requested level is invalid.
   */
  public String getWord (int word) {
    if (word < 0 || word >= words) {
      return "";
    } else {
      int s = getWordStart (word);
      int e = getWordEnd (word);
      if ((e - s - 1) > 0) {
        return category.substring (s, e);
      } else {
        return "";
      }
    }
  }
  
  /**
   Return the starting position of this word in the category string. 
   
   @param word The index identifying the desired word, where the first word = 0. 
   
   @return The starting position of this word in the category string. 
   */
  public int getWordStart (int word) {
    if (word < 0 || word >= words) {
      return 0;
    } else {
      Integer start = (Integer)wordStart.get(word);
      return start.intValue();
    }
  }
  
  /**
   Returns the position immediatly following the last character in this word. 
   
   @param word The index identifying the desired word, wehre the first word = 0.
   
   @return The position immediately following the last character in this word.
   */
  public int getWordEnd (int word) {
    int nextWord = word + 1;
    int e = 0;
    if (word < 0 || (nextWord) >= words) {
      e = category.length();
    } else {
      e = (getWordStart (nextWord) - 1);
      while (e > 0 &&
          (category.charAt(e) == ' '
            || isCategorySeparator(category.charAt(e))
            || isLevelSeparator(category.charAt(e)))) {
        e--;
      }
      e++;
    }
    return e;
  }
  
  /**
   Return all levels through the specified level, concatenated,
   with periods separating the levels.
   
   @return Concatenated categories through the specified level. 
           An empthy string will be returned if the number of levels
           requested is less than zero.
   
   @param level The highest level for which categories are requested,
                with zero indicating the first.
   */
  public String getWordsFromThru (int from, int thru) {
    int f = from;
    int t = thru;
    if ((from >= words) || (thru < 0 )) {
      return "";
    }
    else {
      if (f < 0) {
        f = 0;
      }
      if (t >= words) {
        t = (words - 1);
      }
      int s = getWordStart (f);
      int e = getWordEnd (t);
      if ((e - s) > 1) {
        return category.substring (s, e);
      } else {
        return "";
      } // end if length is null
    } // end if valid levels
  } // end method
  
  /**
   Start iteration through the list of categories assigned to this item.
   */
  public void startCategoryIteration () {
    categoryIndex = 0;
    endOfCategory = false;
  }
  
  public String nextWord () {
    String next = "";
    endOfCategory = true;
    if (hasNextWord()) {
      int categoryEnd = category.indexOf 
          (Category.PREFERRED_CATEGORY_SEPARATOR, categoryIndex);
      int wordEnd = category.indexOf
          (Category.PREFERRED_LEVEL_SEPARATOR, categoryIndex);
      int end = category.length();
      if (categoryEnd > 0 && categoryEnd < end) {
        end = categoryEnd;
      }
      if (wordEnd > 0 && wordEnd < end) {
        end = wordEnd;
        endOfCategory = false;
      }
      next = category.substring (categoryIndex, end);
      categoryIndex = end + 1;
      skipSpaces();
    } 
    return next;
  }

  private void skipSpaces() {
    while (categoryIndex < category.length()
        && category.charAt(categoryIndex) == ' ') {
      categoryIndex++;
    }
  }
  
  public boolean isEndOfCategory () {
    return endOfCategory;
  }
  
  public String nextCategory () {
    String next = "";
    if (hasNextCategory()) {
      int end = category.indexOf 
          (Category.PREFERRED_CATEGORY_SEPARATOR, categoryIndex);
      if (end < 0) {
        end = category.length();
      }
      next = category.substring (categoryIndex, end);
      lastCategoryIndex = categoryIndex;
      lastCategoryEnd = end;
      categoryIndex = end + 1;
      skipSpaces();
    } 
    return next;
  }
  
  /**
   Remove the last category returned via nextCategory
   */
  public void removeCategory () {
    StringBuilder work = new StringBuilder (category);
    work.delete (lastCategoryIndex, lastCategoryEnd);
    if (lastCategoryIndex < work.length() 
        && isCategorySeparator (work.charAt (lastCategoryIndex))) {
      work.deleteCharAt (lastCategoryIndex);
    } 
    else
    if (lastCategoryIndex > 0
        && isCategorySeparator (work.charAt (lastCategoryIndex - 1))) {
      work.deleteCharAt (lastCategoryIndex - 1);
    }
    categoryIndex = lastCategoryIndex;
    lastCategoryEnd = lastCategoryIndex;
    set (work.toString());
  }
  
  public boolean hasNextWord () {
    return (categoryIndex < category.length());
  }
  
  public boolean hasNextCategory () {
    return (categoryIndex < category.length());
  }
  
  /**
    Determines if this category is essentially equal to another
    category. The two categories may be different objects.
   
    @return True if the two category strings are equal, ignoring upper-
            or lower- case considerations.
   
    @param  cat2 Second Category to be compared to this one.
   */
  public boolean equals (Category cat2) {
    return (compareTo(cat2) == 0);
  }
  
  /**
    Compares this category to another category, using a non-case-sensitive
    comparison of their two normalized category strings as the basis
    for the comparison. 
   
    @return Zero if the two categories are equal; 
            < 0 if this category is less than the second category; or 
            > 0 if this category is greater than the second category.
   
    @param  cat2 Second Category to be compared to this one.
   */
  public int compareTo (Category cat2) {
    return category.toString().compareToIgnoreCase (cat2.toString());
  }
  
  public boolean isBlank() {
    return (category.length() == 0);
  }
  
  /**
    Return the normalized category string for this object.
   
    @return The normalized category string, with periods separating levels,
            and with no spaces surrounding the periods.
   */
  public String toString() {
    return category;
  }
  
}
