
## Version 2.50 (2013-04-25)

1. **Released as Open Source**

    Released as Open Source Software under the terms of the Apache 2.0 license.


## Version 2.40 (2012-10-01)

1. **Miscellaneous Bug Fixes**

    Squashed several minor bugs.

2. **Display Prefs Take Effect Immediately**

    When modifying the font, font size, font color or background color in the Display Prefs, the effect of the change is now shown immediately in the Display tab for the currently selected item.

3. **Cleaned up Item Tabs**

    Previous design had Display and Edit tabs on top, with Content, Author and Work tabs below, for editing. These have been combined into a single row of tabs for an item: Display, Content, Author and Work, with the edit mode implicit for the last three tabs.


## Version 2.33 (2011-09-18)

1. **Corrected Source Type Issue**

    Corrected issue that could sometime result in losing the source type for an item.


## Version 2.32 (2011-09-16)

1. **Clarified Handling of Duplicates**

    Provided additional clarification, both in the user interface and in the user guide, to indicate how and when duplicates are handled.


## Version 2.31 (2011-09-01)

1. **Minor Adjustments**

    Several bug fixes and minor interface improvements.


## Version 2.30 (2011-07-24)

1. **Added Backup Prefs**

    A Backup Tab on the iWisdom Preferences now allows the user to choose how much help he or she wants in remembering to perform backups. The app now defaults to occasional reminders.

2. **Multiple Wisdom Storage Options**

    The user now has multiple options for the organization and format of his or her wisdom collection. These are all visible from the Info Window, accessible from the File / Get Info menu. The first option is format. The first possible choice here is &quot;XML&quot;, which is the default, and has historically been the only possibility. The second, and new, possible choice is &quot;Text - Structured&quot;. Selecting this choice will result in files with the &quot;.txt&quot; extension, using a subset of YAML as a storage format. The next option is whether to organize your wisdom items in a series of folders, named for authors and their works, or simply in a single top-level folder. The third option is whether to name your files using the titles of the items, or simply by sequential numbers. <br><br>In particular, if you choose the Structured Text format, without folders, and with files named by Item Number, you will end up with a folder of files that can be pretty easily synced to &quot;the Cloud&quot; and/or to other devices, and then created/modified edited using any text editor. <br>

3. **Added TV Show, Interview and Remarks as potential source types**

    Added Television Show, Interview and Remarks as new source types.

4. **Removed XML Namespace References**

    Removed namespace references from XML representations in order to simplify the XML markup.

5. **header.xml location change**

    The header.xml file, used to store information about a wisdom collection, is now stored directly within the wisdom folder, rather than within the xml folder.


## Version 2.20 (2010-11-14)

1. **Accept Command now the Update Command**

    Renamed the Accept Command to the Update Command. The Update command (with ctrl/cmd U as the shortcut key) is now the command to use to paste an entire item from the system clipboard into your current iWisdom collection. This freed up the P shortcut key for the new Publish function.

2. **Publish Function Improved**

    Added a new Publish command, using ctrl/cmd P as the shortcut key. This new system provides more flexibility than the old system used by iWisdom, allowing you to publish your collection as a Web site or E-book, and publish to any location you like (as opposed to generating Web pages automatically within the Wisdom collection folder itself).


## Version 2.10 (2010-08-22)

1. **Reminder Window added for Unregistered Copies**

    For users with unregistered copies of iWisdom, a new panel pops up when the application is launched, giving the user options to purchase a license, enter a registration code, or continue in demo mode.

2. **Ability to Import from WikiQuote**

    Added the ability to import quotations from WikiQuote.org, using a new item under the File menu. While not all WikiQuote quotations may be able to be imported, due to variations in the way WikiQuote pages are formatted, the new function should work well on most WikiQuote pages.


## Version 2.00 (2010-05-28)

1. **Improved Display Appearance**

    Spacing is now provided between key elements of the wisdom Display in order to make the wisdom item easier to read. Logically related elements are now also combined on a single line, in some cases, rather than using a separate line for each field.

2. **Wrong Item Deleted from Category Tab**

    Corrected a bug that caused problems when deleting an item from the Category Tab.

3. **Added Transfer Prefererences**

    A fourth Tab has been added to the iWisdom Preferences, titled &quot;Transfer.&quot; This refers to the existing Transfer function available from the Item menu. The new Preferences tab allows the user to choose between several different formats for transferring wisdom to the clipboard: Fortune, Markdown, OPML and Text Block (the only choice available previously). The effects of the choice on the currently selected wisdom item will be displayed within the Preferences pane, so that the user can immediately see the effects of their choice.

4. **Added to List of Valid Source Types**

    The list of valid Source Types on the Work tab has been augmented by the addition of Letter as a valid source type.

5. **Fortune added as Export Format**

    Fortune is now available as one of the available formats for Export of a Wisdom list.

6. **Numeric Title Now Retained**

    In previous versions, attempts to enter numeric titles would result in the title being set to the first few words of the body, as if the title had been blank. This has been changed, and numeric titles will now be retained.

7. **Paragraph Tags No Longer Required**

    The body of a wisdom item no longer displays or requires HTML paragraph tags. Blank lines between paragraphs will now be preserved and have essentially the same effect.

8. **Added an OK Button**

    Added an OK button to the toolbar that can be clicked when a user is done editing an item. This does nothing more than clicking on the Display tab, but the lack of an explicit button to indicate the completion of editing seemed to cause some users confusion.

9. **Improved Views**

    The View list is now initialized with two named Views, By Author and By Title. Also, views are now retained and conserved as new Wisdom lists are opened.

10. **Last Author and Work can now be Copied**

    The Item menu now has a Last Author and Work command, with a shortcut key, that will copy the author and work information from the last item accessed before the one currently being added/edited. This feature makes it easier to add a number of quotations from the same source, since you now need to enter this information only for the first item.

11. **Registration Now Required for Complete Functionality**

    Converted iWisdom from donationware to shareware, so a registration fee is now required to achieve full functionality.

12. **Streamlined Documentation**

    Removed the Tips and am instead providing a single help document to replace the multiple forms of help that were available previously, both to avoid potential user confusion and to make the documentation easier to keep up-to-date.


## Version 1.82 (2008-11-30)

1. **Java 5.0 Now Required**

    The minimum requirement to run iWisdom has been upgraded to 5.0 or better, to avoid potential problems with earlier Java releases.

2. **Added a separate set of HTML files for Mobile use**

    When saving a wisdom item, a separate HTML file for use by mobile devices such as iPhones will be generated.


## Version 1.81 (2008-11-16)

1. **Fixed Bug Affecting New Users**

    Version 1.80 introduced a fatal bug affecting new users. This change corrects that problem.


## Version 1.80 (2008-10-12)

1. **Added Bibliographic Fields**

    Added the name of the work's publisher, the city in which the publisher operates, and the pages of the work from which the extract was taken. All of these fields now appear on the new Work tab. With the addition of these fields, enough data is now maintained to generate a bibliography.

2. **Additional Export Formats Now Supported**

    The Export window has been reformatted, and additional Export formats are now supported. The user may now select between exporting &quot;All Fields&quot; and exporting a Wisdom List. The All Fields option supports formats that contain all fields for each item in a format that can be read by iWisdom or another program, while the Wisdom List option is formatted using HTML, some equivalent markup format, or OPML. Item Scope allows the user to select only the currently selected entry, all items in a selected category, or the entire collection. The available Formats depends on the chosen Field Scope, but now includes Markdown and Textile formats when Wisdom List is selected. Finally, the destination for any of these choices may now be either an output file, or the System Clipboard (allowing the results to be subsequently pasted into another program).

3. **Author may now be Selected from a List**

    When entering the Author(s) for an item, the user is now presented with a list of all authors already part of the collection. A particular author may be entered by scrolling through the list presented, or entering enough letters (either last name first, or first name last) to uniquely identify an entry. Pressing Return/Enter selects an author from the list. Pressing Tab moves to the next field. When selecting an existing author, additional author fields (Info and Author Link) are entered automatically on the new entry, if present on any of the existing entries for the same author.

4. **Source may now be Selected from a List**

    When entering the Source for an item, the user is now presented with a list of all sources already part of the collection. A particular source may be entered by scrolling through the list presented, or entering enough letters to uniquely identify an entry. Pressing Return/Enter selects a source from the list. Pressing Tab moves to the next field. When selecting an existing source, additional source fields are entered automatically on the new item, if present on any of the existing items for the same source.

5. **Split Edit Tab into Three Sections**

    The fields to be entered on the Edit tab have been split into three lower-level tabs: Content, Author, and Work. Only the information on the Content field is required. This change allows all fields to be accessed for data entry without requiring an excessive size for the iWisdom window.

6. **Expanded Category Replace Function**

    Expanded the Category Replace function to allow an existing category to be removed (by leaving the replacement category field blank) and to allow a new category to be added to all items (by leaving the existing category field blank).

7. **Export by Category**

    A new option has been added to the Export function, to allow all items having the specified category to be exported.


## Version 1.70 (2008-06-09)

1. **Enhanced Display Preferences**

    Added several check boxes to the Display Preferences tab of the iWisdom General Preferences to allow unused fields to be suppressed. Title, Source, Source Type, Date Added and ID Number can all now be suppressed if desired from appearing on the Display tab.

2. **Category Selection Made Easier**

    A list of categories already used now drops down automatically when your cursor enters the Category field. You may click on an item in the list to select it, or you may type enough of the desired Category to see it selected on the list, then press Enter/Return to select the currently highlighted entry from the list. The process may be repeated, after typing a comma, to assign multiple Categories to an item.

3. **Corrected Item/Duplicate Problem**

    Corrected bug in Item/Duplicate processing. Starting with version 1.60, when a new item is added, its text is compared to that of existing entries and, if a duplicate is found, the new entry is merged with the old one, rather than being added. Unfortunately, when an item was requested to be duplicated, the second occurrence of the item was being merged with the first, resulting in no change. This was corrected by changing the body of the new item to some standard text referring to the duplication. This allows the user to duplicate an entry in order to avoid rekeying author and source information for multiple entries from the same source.

4. **Added Support for Multiple Categories**

    The program previously supported multi-level categories (separated by periods) but support has now been added for multiple categories, separated by commas (each of which may have one or more levels). When viewing items from the Categories tab, an item with multiple categories will appear multiple times, once for each assigned category.

5. **Added Horizontal Line Below Body in Display View**

    At the request of a current user, added a horizontal rule between the body text and the remaining information for a wisdom item, on the Display view. This provides some nice visual separation between the primary information above and the secondary information below.


## Version 1.60 (2007-12-21)

1. **Enhanced Import Capabilities**

    Enhanced the Import function to detect duplicate entries, and to add new information from the imported duplicate, if available.


## Version 1.51 (2007-10-18)

1. **Added Linkable ID Number**

    Each item is now assigned a unique ID number when it is added to a collection. When publishing a collection to a Web site, a shortcut file is generated for each item in the collection, with each shortcut file redirecting to the actual HTML file at the longer URL location. The shortcut file is designed to provide a shorter URL that can be used to reference a Wisdom item.

2. **Fixed Spacing Problem**

    Corrected a problem with proper spacing after use of embedded inline HTML within a paragraph.

3. **Added Markdown Export Format**

    Added the ability to export a collection into the Markdown text format.


## Version 1.50 (2007-09-08)

1. **Created Backup and Revert to Backup items on the File Menu**

    New items on the File menu now allow the user to create a backup file, and to revert their current collection to a previous backup file.

2. **Text File Import Improved**

    The import function for text files has been improved in several ways. On a Mac, you may import a file simply by dragging and dropping the file onto the application. The ability to parse the text files and accurately identify authors, quotations, years and categories has been improved, although the extent to which this will work well for any individual text file is hard to predict, due to the variation in formatting used within these sorts of files.

3. **Redesigned User Interface**

    Redesigned the iWisdom User Interface to make it more closely adhere to normal user expectations. Removed several windows from the string of tabs that existed before and put them in more logical locations, with the locations in some cases being dependent on the OS on which the application is run. The changes are described going from right to left along the old string of tabs. <br><br>The About tab is now available on the Mac at the iWisdom menu / About item, and on Windows at the Help menu / About iWisdom item.<br><br>The Log tab is now available from the Window menu / Log item, on all operating systems. <br><br>The Prefs and View tabs are now available from a tabbed Preferences window, which is available on the Mac at the iWisdom menu / Preferences item, and on Windows at the Tools menu / Options item. <br><br>The Export and Import tabs are now available as separate windows from their respective items under the File menu, on all operating systems. <br><br>The four remaining tabs are now split between two different panes. The List and Category tabs are on the left/top pane, and the Display and Edit tabs are on the right/bottom pane (a new check box under General Preferences allow you to choose you to split the panes horizontally or vertically). The first two panes act as selection panes, and the last two allow you to view/edit a single, selected item.  <br><br>What used to be known as the Rotate button is now called the Scan button, and has been moved to the tool bar. Clicking once begins the process of showing a different item in the Display window every few seconds, and clicking again on the same button now ends the scan. <br><br>Additionally, a search field has now been added to the toolbar, followed by a Find Button. <br>


## Version 1.41 (2007-04-20)

1. **Fixed Bug that was Dropping the Author Link**

    Fixed the problem that was causing the author link to be dropped.

2. **Fixed Code that was Generating Malformed HTML**

    Corrected several bugs that were causing malformed HTML to be generated.


## Version 1.40 (2007-04-10)

1. **Added iWisdom Import Sources to Help Menu**

    A new item has been added to the Help Menu, titled iWisdom Import Sources. Selecting this item will cause a Web page to be loaded in your browser. The Web page will list a number of available Import sources for your collection of quotations.

2. **Add and Remove Quotes**

    Menu Items have been added that will allow quotes to be added or removed from the current quotation, or from all quotations in your collection. These functions allow you to fairly easily decide to display your quotations with quotation marks, or without (depending on your personal inclination).

3. **Favorites Added to Import Tab**

    A Favorites drop-down menu has been added to the Import Tab. Select an Internet source for importable wisdom from the drop-down menu, then click on the Import button to import all items from the selected source.

4. **Added Tips**

    When first installed, iWisdom will now show Tips in a separate window each time that it is launched.  These can also be requested from the Help Menu. Automatic display of these upon launch can be turned off (or back on) on the Prefs tab, as well as in the Tips window itself. The next tip will be displayed each time iWisdom is launched. The user may scroll through the tips in either direction.

5. **HTML character entities now generated automatically.**

    When generating HTML, certain characters appearing in the body of an item are now converted to their appropriate HTML character entities. Pairs of consecutive hyphens are now changed to em dash characters; double quotation marks are converted to &quot;curly quotes&quot;; and apostrophes/single quotation marks are converted to their curly equivalents.

6. **Body of iWisdom Item is now fully editable HTML**

    The body of the iWisdom item is now stored, displayed and edited as XHTML.

7. **Changed Method of Generating HTML**

    iWisdom now automatically generates the HTML equivalent for each wisdom item directly, without use of XSLT. This change provides greater ability to precisely control the HTML presentation.

8. **Corrected Source Link Problem**

    Fixed a problem that was preventing the Source Link field from being editable.

9. **Added Web Log and Web Page as Source Types**

    Added Web Log and Web Page as Source Type values.

10. **Prevented Program Crash when iWisdom is removed from its folder**

    Program will now launch successfully even if it is removed from its enclosing iWisdom folder.


## Version 1.30 (2007-01-31)

1. **Added Author Info Field**

    Added a new &quot;Author Info&quot; field. This will be appended to the name of the author so that, if you wanted the author to appear as &quot;Steve Jobs, CEO of Apple&quot;, you would enter &quot;Steve Jobs&quot; in the Author field, and &quot;CEO of Apple&quot; in the Author Info field.

2. **Change title of body from Description to Body**

    Changed some references to the Body of the item from &quot;Description&quot; to &quot;Body&quot;.

3. **Corrected Duplication Problem**

    Corrected a problem that caused some items to continually duplicate themselves. This was caused by an inconsistency in the way files were named. The I/O routine has been corrected to always use the actual file name (rather than a calculated one).

4. **Fix Invalid UTF Characters**

    The XML files created are now stored in UTF-8 format, and non-ASCII characters are converted appropriately.

5. **Alternate ways to indicate direct quotations**

    The collection tab now allows you to specify that all new items are assumed to be direct quotations. The Edit Tab now includes a check box allowing you to specify that the text entered represents a direct quotation.

6. **Make First Name First clearer**

    Added a Tool Tip to Author field on the Edit Tab, to clarify that names are to be entered first name first.


## Version 1.21 (2006-12-04)

1. **Corrected Null File Import Error**

    Corrected a &quot;Null File&quot; message that was erroneously being generated when trying to import an XML file from a Web URL.

2. **Added Ctrl-W or Cmd-W command to resize window**

    In some cases, when using a computer with varying display configurations, iWisdom could &quot;remember&quot; where it was last positioned and end up not visible with the current screen configuration. In these cases, pressing Ctrl-W (on Windows systems) or Cmd-W (on Mac systems) will return the window to a standard size and location so that it will be visible.


## Version 1.20 (2006-12-02)

1. **Added Ability to Generate RSS Feed**

    Added the ability to generate an RSS Feed. A new Collection tab allows you to specify information about the collection as a whole: information needed as part of an RSS feed. The date created is also captured for each wisdom item, allowing the wisdom to be sorted by this creation date, and adding another field needed for an RSS Feed. The default theme, portablewisdom1, now generates an RSS feed of the most recent five items added to the collection.


## Version 1.10 (2006-11-11)

1. **Automatically Check for Later Version**

    The program can now check to see if a later version of iWisdom is available. On the Prefs Tab you may now check or uncheck a box to check this automatically whenever the program is launched. Just below the check box, a button is available to request that the version be checked immediately. An active Internet connection is required to check the latest version. If a newer version is available, you will be given an option to visit the Version History page in your Web browser. From there, you may proceed to the Downloads page if you would like to download the newer version.

2. **Import Options Expanded**

    An import tab has been added. Two new XML import formats are now supported: the Quotation Markup Language (QML), and the Quotation Exchange Language (QEL). In addition, xml formats can now be imported from a Web URL, as well as from a local file.

3. **Added Speech as a Source Type**

    Added &quot;Speech&quot; as a valid Source Type.


## Version 1.01 (2006-10-22)

1. **Corrected Intermittent Launch Error**

    iWisdom sometime refused to launch due to an initialization problem. This has been corrected.

2. **Corrected JVM Dependencies**

    Version 1.00 could only be run on Java 1.5. For Mac users, this meant it could only be run on Mac OS X 10.4. This has been corrected, and iWisdom can now be run on Java 1.4, and on Mac OS X 10.3.


## Version 1.0 (2006-10-03)

1. **Initial Release**

    This was the initial release of iWisdom.

