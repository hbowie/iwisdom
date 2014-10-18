<!-- Generated using template product-user-guide-template.mdtoc -->
<!-- Generated using template product-user-guide-template.md -->
<h1 id="iwisdom-user-guide">iWisdom User Guide</h1>


<h2 id="table-of-contents">Table of Contents</h2>

<div id="toc">
  <ul>
    <li>
      <a href="#introduction">Introduction</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li>
          <a href="#system-requirements">System Requirements</a>
        </li>
        <li>
          <a href="#rights">Rights</a>
        </li>
        <li>
          <a href="#installation">Installation</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#data-fields">Data Fields</a>
      <ul>
        <li>
          <a href="#collection-info">Collection Info</a>
        </li>
        <li>
          <a href="#content">Content</a>
        </li>
        <li>
          <a href="#author">Author</a>
        </li>
        <li>
          <a href="#work">Work</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#file-operations">File Operations</a>
      <ul>
        <li>
          <a href="#folders-and-files">Folders and Files</a>
        </li>
        <li>
          <a href="#import-quotations">Import Quotations</a>
        </li>
        <li>
          <a href="#import-quotations-from-wikiquote">Import Quotations from WikiQuote</a>
        </li>
        <li>
          <a href="#backing-up-and-restoring-quotations">Backing Up and Restoring Quotations</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#user-interface">User Interface</a>
      <ul>
        <li>
          <a href="#the-tool-bar">The Tool Bar</a>
        </li>
        <li>
          <a href="#main-window">Main Window</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#tips-tricks-and-special-functions">Tips, Tricks and Special Functions</a>
      <ul>
        <li>
          <a href="#copying-last-author-and-work">Copying Last Author and Work</a>
        </li>
        <li>
          <a href="#displaying-wisdom">Displaying Wisdom</a>
        </li>
        <li>
          <a href="#getting-wisdom-out-of-iwisdom">Getting Wisdom out of iWisdom</a>
        </li>
        <li>
          <a href="#publish">Publish</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#preferences">Preferences</a>
      <ul>
        <li>
          <a href="#general-prefs">General Prefs</a>
        </li>
        <li>
          <a href="#tags-export-prefs">Tags Export Prefs</a>
        </li>
      </ul>

    </li>
    <li>
      <a href="#help">Help</a>
    </li>
  </ul>

</div>


<h2 id="introduction">Introduction</h2>


iWisdom is a desktop application for storing, organizing and displaying small bits of wisdom, such as quotations. Some of its special features are:

* a number of related fields, including title, category, author, source, year of initial publication, and rights;

* storage of your wisdom in one of two open, non-proprietary formats: XML or Structured Text;

* multiple sorting and selection fields;

* multiple files with remembered viewing options for each;

* multiple categories and levels of categorization, with a tree view to let you see your items within categories;

* automatic Web Publishing;

* automatic XML publishing (to create RSS feeds, for example);

* a Find function;

* a Category Mass Change function;

* Web Page validation;

* an import capability that can extract existing wisdom from a number of different file formats;

* options to modify the look and feel of iWisdom;

* a transfer function to convert a wisdom item to readable plain text, with a corresponding accept function to convert the plain text (after possible transmission via e-mail, etc.) into a new wisdom item on another iWisdom list.


<h2 id="getting-started">Getting Started</h2>


<h3 id="system-requirements">System Requirements</h3>


iWisdom is written in Java and can run on any reasonably modern operating system, including Mac OS X, Windows and Linux. iWisdom requires a Java Runtime Environment (JRE), also known as a Java Virtual Machine (JVM). The version of this JRE/JVM must be at least 6. Visit [www.java.com](http://www.java.com) to download a recent version for most operating systems. Installation happens a bit differently under Mac OS X, but generally will occur fairly automatically when you try to launch a Java app for the first time.

Because iWisdom may be run on multiple platforms, it may look slightly different on different operating systems, and will obey slightly different conventions (using the CMD key on a Mac, vs. an ALT key on a PC, for example).

<h3 id="rights">Rights</h3>


iWisdom Copyright 2003 - 2014 by Herb Bowie

iWisdom is [open source software](http://opensource.org/osd). Source code is available at [GitHub](http://github.com/hbowie/iwisdom).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

  [www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

iWisdom also incorporates or adapts the following open source software libraries.

* JExcelAPI — Copyright 2002 Andrew Khan, used under the terms of the [GNU General Public License](http://www.gnu.org/licenses/).

* parboiled — Copyright 2009-2011 Mathias Doenitz, used under the terms of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

* pegdown — Copyright 2009-2011 Mathias Doenitz, used under the terms of the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

* Xerces &#8212; Copyright 1999-2012 The Apache Software Foundation, used under the terms of the [Apache License, Version 2.0][apache].

* Saxon &#8212; Copyright Michael H. Kay, used under the terms of the [Mozilla Public License, Version 1.0][mozilla].


<h3 id="installation">Installation</h3>


Download the latest version from [PowerSurgePub.com](http://www.powersurgepub.com/downloads.html). Decompress the downloaded file. Drag the resulting file or folder into the location where you normally store your applications. Double-click on the jar file (or the application, if you've downloaded the Mac app) to launch.


<h2 id="data-fields">Data Fields</h2>


iWisdom works with chunks of wisdom and associated data, organized into collections.

<h3 id="collection-info">Collection Info</h3>


Selecting **Get Info** from the **File** menu will bring up a window that allows you to enter a number of information items applicable to an entire wisdom collection.

Folder
:    Identifies the folder containing your wisdom collection.

Format
:    The default is "XML", but you may also choose "Text - Structured". The latter is easier to edit, and may be preferable if you choose to sync your wisdom collection to "the cloud" or to other devices, using a service such as DropBox.

Organize within Folders?
:    If checked, then your wisdom items will be organized within folders named after the authors and their works. If unchecked, your wisdom items will all be stored at the top level of your collection's folder.

Wisdom File Naming
:    Name by Item Number simply names your wisdom files using a sequential numbering scheme. Name by Title names your files based on the titles you assign each item.

Title
:    The overall title of your wisdom collection. This will be used when publishing your collection.

Link
:    A web link, if available, for the location of your published wisdom.

Path
:    The path to your wisdom collection.

Editor
:    Your name, as the editor of your collection.

Description
:    A brief description of the intent of this wisdom collection.

Primary?
:    If checked, this will become your primary collection, which will be automatically opened whenever you launch iWisdom.

Forget?
:    Check this box as a way of telling iWisdom to drop this collection from its list of known collections, available from the **Open Recent** menu.

<h3 id="content">Content</h3>


These fields pertain to the content of each wisdom item.

Title
:    Each piece of wisdom within a collection must have a unique title. This can be any brief line that summarizes the gist of the wisdom. When importing dissimilar quotations, a digit may be added to the end of a title to ensure uniqueness.

Category
:    For each piece of wisdom, you may enter one or more categories and, for each, one or more sub-categories. Each category level is separated from the next by a forward slash or a period. Each separate category string (including sub-categories) is separated from others by a comma.

    Once a category is established for one piece of wisdom, you may assign it to other pieces of wisdom by selecting the desired category from the drop-down menu. You may do this by scrolling and clicking, or by typing the beginning letters of the desired category and pressing Return/Enter.

	If you don't see an appropriate category on the drop-down list, simply type in the new category that you want. Once you complete your edits for that item, your new category will become available the next time you select the drop-down list.

	If you decide to stop using a category, simply remove it or change it on all the items to which it has been assigned. The next time you quit and relaunch iWisdom unused categories will disappear from the list.

	If you'd like to change a Category name, use the **Add/Replace Category** Menu Item from the **List** menu to change all occurrence of one Category to a new one.

	The Categories Tab organizes your collection by Category. Click on a disclosure triangle to view enclosed items and sub-categories.

Rating
:    You may assign each piece of wisdom a rating (you may think of it as a number of stars) in the range of 1 - 5. The default is 3.

Body
:    This is the actual piece of wisdom. This field may be formatted using common HTML commands. If you wish to use quotation marks, enter normal "straight" quotation marks. For an em dash (or long dash), use two consecutive hyphens. Separate paragraphs with a blank line. Note that, when adding or importing Wisdom item(s), iWisdom will attempt to avoid duplicate entries. It does so by comparing the body of new items to existing items, comparing only letters, ignoring spaces, punctuation and numbers. When it finds a match, iWisdom merges data from the new entry into the existing entry, filling in missing data where possible.

<h3 id="author">Author</h3>


These fields pertain to the author(s) of the wisdom item.

Author(s)
:    The name(s) of the author or authors of the work from which the wisdom was taken. Identify authors by their first and last names, in that order, with commas separating authors, and the word "and" separating the last author from those preceding.

	When entering an author, you may select an existing author from the drop-down menu by entering the beginning characters of their name (either first name first or last name first), or by scrolling down the list, then pressing Return/Enter to select the desired author.

	Entering the Author's name will allow the author's Wikipedia entry to be accessed from the Display tab

Info
:    Some optional brief information about the author.

Author Link
:    An optional URL to a site containing more information about the author(s).

<h3 id="work">Work</h3>


These fields pertain to the work from which the wisdom was taken.

Source
:    The major title of the source work. When entering a source title, you may select an existing source title from the drop-down menu by entering the beginning characters, or by scrolling down the list, then pressing Return/Enter to select the desired title.

Source Type
:    The type of the source work: Book, Film, Letter, etc.

Date
:    The year in which the work was first published.

Minor Title
:    The name of a chapter, or an article, or a poem, if the  Source title above refers to a larger work.

Source Link
:    An optional link to the source, or to a location with more information about the source.

Source ID
:    An identifier to uniquely identify the source. Enter a book's 10-digit ISBN to enable access to the book's entry on Amazon.com from the Display tab.

Pages
:    The page number(s) on which a quotation can be found.

Rights
:    Usually a copyright.

By
:    The owner of the rights.

Publisher
:    The name of the publisher of the work.

City
:    The primary city in which the publisher does business.

<h2 id="file-operations">File Operations</h2>


File operations may be accessed via the File menu.

<h3 id="folders-and-files">Folders and Files</h3>


The default location for your initial Wisdom collection is in your **home directory**, then in a folder named **Wisdom**, then in a folder named **iWisdom**. This collection will open automatically when you launch iWisdom.

You can create other Wisdom collections by selecting **New** from the File Menu. You can set one collection to be your primary collection by selecting **Get Info** from the File menu and then clicking on the check box to make the current folder your primary collection: your primary collection will then open automatically when launching iWisdom.

Note that an iWisdom collection consists of a folder and a number of files and possibly sub-folders.

Once you have created an iWisdom collection, you may open it by selecting **Open** from the File Menu, and then selecting the collection's folder. You may also use the **Open Recent** item on the File Menu, to be able to select a collection from a list of previously opened collections.

The name of the current iWisdom collection appears at the bottom left of your main window. This display consists of the collection's folder and its parent folder, followed by "(P)" if this is your primary collection. Hover over the display to see the full path to the collection.

<h3 id="import-quotations">Import Quotations</h3>


To jump-start your Wisdom collection, select **Import** from the File menu, then select the Portable Wisdom collection from the Favorites drop-down, then click on Import. (You will need an active Internet connection to import any of the Favorites.)

iWisdom is able to import wisdom from a number of different formats (including plain text and multiple XML-based formats). When importing, iWisdom will recognize duplicates (based on the actual content of each item's body) and intelligently combine duplicates if they contain differing degrees of detail.

<h3 id="import-quotations-from-wikiquote">Import Quotations from WikiQuote</h3>


Quotations may also be imported from [www.WikiQuote.org](wikiq). Select **Import from WikiQuote** from the File menu. In the resulting panel, select your desired language, then enter the name of an author and/or a particular work (book, movie, etc.). You may also optionally enter a word or phrase in the Containing field, to search for a quotation containing the entered text (capitalization is not significant).

Click on the Search button to initiate the search of the specified WikiQuote page. You may then click on the Next button repeatedly to see each matching quotation, one by one. When you see a quotation you wish to capture for your collection, click on the Import button. Clicking on the Browse button will open the specified WikiQuote page in your Web browser.

Please note that, due to variability in the way quotations are entered into WikiQuote, not all quotations may appear as candidates for import. If you wish to specific examples of failures to our attention, drop us a note at [support@powersurgepub.com][support].

<h3 id="backing-up-and-restoring-quotations">Backing Up and Restoring Quotations</h3>


The File menu contains a **Backup** command, and a **Revert from Backup** command. When performing a backup, the file location and name will default to a folder in your current iWisdom folder, and a name containing the current date and time. The **Revert from Backup** command will default to the same folder.

It is recommended that you perform a backup before each **Import** so that you can easily revert to your prior state if the import creates unwanted results. It is also recommended that you perform a backup before installing a new version of iWisdom, or changing any of the storage options.

The Backup Preferences allow you to choose one of three backup modes:

* **Manual Only** -- Backups will only be performed when they are explicitly initiated by the user, via the **File** / **Backup** menu item.

* **Occasional Suggestions** -- iWisdom will remind the user to perform a backup every seven days, and prior to major events such as imports and program upgrades.

* **Automatic Backups** -- Backups will be performed automatically whenever the program quits, as well as prior to major events such as imports and program upgrades.

The program will default to occasional suggestions.

<h2 id="user-interface">User Interface</h2>



<h3 id="the-tool-bar">The Tool Bar</h3>


A toolbar with multiple buttons appears at the top of the user interface.

* **OK** -- Indicates that you have completed adding/editing the fields for the current Wisdom Item.
* **+** -- Clear the data fields and prepare to add a new Wisdom Item to the collection.
* **-** -- Delete the current Wisdom Item.
* **&lt;&lt;** -- Display the first Wisdom Item in the collection.
* **&lt;** -- Display the next Wisdom Item in the collection.
* **&gt;** -- Display the next Wisdom Item in the collection.
* **&gt;&gt;** -- Display the last Wisdom Item in the collection.
* **Launch** -- Launch the current Wisdom Item in your Web browser. (This may also be accomplished by clicking the arrow that appears just to the left of the URL itself.)
* **Find** -- Looks for the text entered in the field just to the left of this button, and displays the first Wisdom Item containing this text in any field, ignoring case. After finding the first occurrence, this button's text changes to **Again**, to allow you to search again for the next Wisdom Item containing the specified text.
* **Add "** -- Add quotation marks around the body of the item.
* **Remove "** -- Remove quotations marks from the body of the item.
* **Scan** -- Start a process of briefly showing each item in your collection on a rotating basis.

<h3 id="main-window">Main Window</h3>


The main window contains three different panes.

<h4 id="the-list">The List</h4>


On the first half of the main window, you'll see two tabs. The first of these displays the **List**. This is just a simple list of all your Wisdom Items. You can rearrange/resize columns. You can't sort by other columns. Click on a row to select that Wisdom Item for display on the other half of the main window. Use the entries on the **View** menu to select a different sorting/filtering option. Use the **View Preferences** to modify your view options.

<h4 id="categories">Categories</h4>


The second Tab on the first half of the main window displays the **Categories**. This is an indented list of all your Categories, with Wisdom Items appearing under as many Categories as have been assigned to them, and with Wisdom Items with no Categories displaying at the very top. Click to the left of a Category to expand it, showing Wisdom Items and/or sub-tags contained within it.

Note that Categories that were once used, but that are used no more, will stick around until you close the iWisdom file and re-open it. If you wish, you may accelerate this process by selecting **Reload** from the **File** menu.

<h4 id="details">Details</h4>


The detailed data for the currently selected Wisdom Item appears on the second half of the main window.


<h2 id="tips-tricks-and-special-functions">Tips, Tricks and Special Functions</h2>


<h3 id="copying-last-author-and-work">Copying Last Author and Work</h3>


When entering multiple quotations from the same source, you can use the **Last Author and Work** command from the **Item** menu to copy information about the Author and Work from the last item you entered to the new item you are currently entering.

<h3 id="displaying-wisdom">Displaying Wisdom</h3>


The **Display Tab** allows you to view a piece of wisdom. Click on an author's name to use your Web browser to view the author's entry in Wikipedia. Click on the name of a book to view the books's entry on Amazon.com.

Note that the Display Preferences allow the display to be customized. You can select a particular font, font size, and colors for the background and the text.

You may also tailor which fields are displayed along with the wisdom body and its author, with Title, Source, Source Type, Added Date and Time, and iWisdom ID number all being optional.

<h3 id="getting-wisdom-out-of-iwisdom">Getting Wisdom out of iWisdom</h3>


iWisdom offers several different ways to get your wisdom out of iWisdom, in a variety of formats.

<h4 id="transferupdate">Transfer/Update</h4>


A Transfer works like a Copy, with a couple of differences:

* An entire wisdom item is copied, and not just one particular wisdom field;

* You may select the format you wish to use for the copied data.

Use the Transfer Preferences to specify the format you wish to use. The following options are currently available:

* [Fortune][] -- A simple text format, with minimal data and minimal markup.

* [Markdown][] -- A plain text format, using simplified markup, designed for easy conversion to HTML.

* [OPML][opml] -- An XML format designed for outlines.

* Text Block -- A simple format that will allow the wisdom item to easily be Updated (i.e., pasted) back into iWisdom (on a different computer or in a different collection), using the **Upate** command from the **Item** Menu.

* XML -- A format using XML markup.

* Text - Structured -- A subset of YAML that is easy for humans to read and edit.

Note that Transfer and Update both make use of the System clipboard -- the same area used by Cut/Copy/Paste commands. So, for example, the following sequence of commands, using the Text Block format, will allow you to move a Wisdom item from one collection on one computer to a different collection on a different computer.

1. From within iWisdom, select the desired item in the first collection.

2. Select Transfer from the Item Menu.

3. Compose an e-mail to be sent to the owner of the second computer.

4. Perform a normal Paste command in your E-mail program to paste the wisdom item into the body of your e-mail.

5. Send the e-mail.

6. Upon receipt of the e-mail, select the entire text block containing the wisdom item and perform a normal copy.

7. Fire up iWisdom, and select Update from the Item menu.

<h4 id="export">Export</h4>


The **Export** command, available from the **File** menu, allows you to export part or all of your collection into an external file that can be used by other programs.

The following Export options are available, once you select the Export command.

Field Scope
:    Allows you to select one of the following options.

	* All Fields -- This options can be used for exporting into a format that preserves all iWisdom data fields as discrete entities in the resulting output file. This option is best when you will be using the output from iWisdom as import to another program (or to another iWisdom collection).

	* Wisdom List -- This option creates a list of the desired wisdom entries, but formatted primarily to be read by a human, rather than a computer.


Item Scope
:    Allows you to select one of the following options.

	* Current Item -- Exports only the single item that is currently selected.

	* Selected Category -- Exports all items in the Category that you select below.

	* Entire Collection -- Exports all items in the currently opened collection.

Format
:    If you selected a Field Scope of All Fields, then you can select from one of the following formats.

	* XML -- Using the format used internally by iWisdom.

	* Tab-Delimited Text -- Formatted in rows and columns, with tabs between each column. Suitable for importing into spreadsheets and databases.

	* Text Block -- A format that can be easily read by humans and by iWisdom.

	* Text - Structured -- A subset of YAML that is easily human-readable and easy for humans to update.

	If you selected a field scope of Wisdom List, then you may select from any of the following output formats.

	* [Fortune][] -- A simple text format, with minimal data and minimal markup.

	* HTML -- The native language of the World-Wide Web.

	* [Markdown][] -- A plain text format, using simplified markup, designed for easy conversion to HTML.

	* [OPML][] -- An XML format designed for outlines.

	* [Textile][] Syntax 1 -- Another lightweight markup language.

	* [Textile][] Syntax 2 -- Textile again, but with links encoded differently.

<h3 id="publish">Publish</h3>


The publish option allows you to easily publish your Wisdom Items in a variety of useful formats.

To begin the publication process, select the **Publish...** command from the **File** menu.

You will then see a window with the following fields available to you.

Publish to
:    You may use the Browse button above and to the right to select a folder on your computer to which you wish to publish your Wisdom Items. You may also enter or modify the path directly in the text box. When modifying this field, you will be prompted to specify whether you wish to update the existing publication location, or add a new one. By specifying that you wish to add a new one, you may create multiple publications, and then later select the publication of interest by using the drop-down arrow to the right of this field.

Equivalent URL
:    If the folder to which you are publishing will be addressable from the World-Wide Web, then enter its Web address here.

Templates
:    This is the address of a folder containing one or more publishing templates. This will default to the location of the templates provided along with the application executable. You may use the Browse button above and to the right to pick a different location, if you have your own templates you wish to use for publishing.

Select
:    Use the drop-down list to select the template you wish to use.

	**Favorites Plus**: This template will produce the following files and formats.

	1. index.html -- This file is an index file with links to the other files. You can browse this locally by selecting **Browse local index** from the **File** menu.
	 2. favorites.html -- This file tries to arrange all of the Wisdom Items you have tagged as "Favorites" into a four-column format that will fit on a single page.
	 3. bookmark.html -- This file formats your URLs in the time-honored Netscape bookmarks format, suitable for import into almost any Web browser or URL manager.
	 4. outline.html -- This is a dynamic html file that organizes your URLs within your tags, allowing you to reveal/disclose selected tags.

Apply
:    Press this button to apply the selected template. This will copy the contents of the template folder to the location specified above as the Publish to location.

Publish Script
:    Specify the location of the script to be used. The PSTextMerge templating system is the primary scripting language used for publishing. A PSTextMerge script will usually end with a '.tcz' file extension.

Publish when
:    You may specify publication 'On Close' (whenever you Quit the application or close a data collection), 'On Save' (whenever you save the data collection to disk), or 'On Demand'.

Publish Now
:    Press this button to publish to the currently displayed location. Note that, if you've specified 'On Demand', then this is the only time that publication will occur.

View
:    Select the local file location or the equivalent URL location.

View Now
:    Press this button to view the resulting Web site in your Web browser.

<h2 id="preferences">Preferences</h2>


The following preference tabs are available.

<h3 id="general-prefs">General Prefs</h3>


The program's General Preferences contain a number of options for modifying the program's look and feel. Feel free to experiment with these to find your favorite configuration. Some options may require you to quit and re-launch iWisdom before the changes will take effect.

SplitPane: Horizontal Split?
:    Check the box to have the **List** and **Tags** appear on the left of the main screen, rather than the top.

Deletion: Confirm Deletes?
:    Check the box to have a confirmation dialog shown whenever you attempt to delete the selected Wisdom Item.

Software Updates: Check Automatically?
:    Check the box to have iWisdom check for newer versions whenever it launches.

Check Now
:    Click this button to check for a new version immediately.

File Chooser
:    If running on a Mac, you may wish to select AWT rather than Swing, to make your Open and Save dialogs appear more Mac-like. However, Swing dialogs may still appear to handle options that can't be handled by the native AWT chooser.

Look and Feel
:    Select from one of the available options to change the overall look and feel of the application.

Menu Location
:    If running on a Mac, you may wish to have the menus appear at the top of the screen, rather than at the top of the window.

<h3 id="tags-export-prefs">Tags Export Prefs</h3>


Tags to Select
:    Leave this blank to select all tags on any export, including a data export performed as part of a Publish process. Specifying one or more tags here will limit the content of the export to items containing at least one of those tags.

Tags to Suppress
:    Any tags specified here will be removed from all tags fields appearing on exports. This may be useful to suppress tags used for selection at Publish time, as opposed to tags that will appear in the eventual output being created.


<h2 id="help">Help</h2>


The following commands are available. Note that the first two commands open local documentation installed with your application, while the next group of commands will access the Internet and access the latest program documentation, where applicable.

* **Program History** -- Opens the program's version history in your preferred Web browser.

* **User Guide** -- Opens the program's user guide in your preferred Web browser.

* **Check for Updates** -- Checks the PowerSurgePub web site to see if you're running the latest version of the application.

* **iWisdom Home Page** -- Open's the iWisdom product page on the World-Wide Web.

* **Reduce Window Size** -- Restores the main iWisdom window to its default size and location. Note that this command has a shortcut so that it may be executed even when the iWisdom window is not visible. This command may sometimes prove useful if you use multiple monitors, but occasionally in different configurations. On Windows in particular, this sometimes results in iWisdom opening on a monitor that is no longer present, making it difficult to see.

* **iWisdom Import Sources** -- Takes you to a Web page where you will find a number of potential iWisdom import sources.



[java]:       http://www.java.com/
[pspub]:      http://www.powersurgepub.com/
[downloads]:  http://www.powersurgepub.com/downloads.html
[osd]:		  http://opensource.org/osd
[gnu]:        http://www.gnu.org/licenses/
[apache]:	     http://www.apache.org/licenses/LICENSE-2.0.html
[markdown]:		http://daringfireball.net/projects/markdown/
[multimarkdown]:  http://fletcher.github.com/peg-multimarkdown/

[wikiq]:     http://www.wikiquote.org
[support]:   mailto:support@powersurgepub.com
[fortune]:   http://en.wikipedia.org/wiki/Fortune_(Unix)
[opml]:      http://en.wikipedia.org/wiki/OPML
[textile]:   http://en.wikipedia.org/wiki/Textile_(markup_language)
[pw]:        http://www.portablewisdom.org

[store]:     http://www.powersurgepub.com/store.html

[pegdown]:   https://github.com/sirthias/pegdown/blob/master/LICENSE
[parboiled]: https://github.com/sirthias/parboiled/blob/master/LICENSE
[Mathias]:   https://github.com/sirthias

[club]:         clubplanner.html
[filedir]:      filedir.html
[metamarkdown]: metamarkdown.html
[template]:     template.html

[mozilla]:    http://www.mozilla.org/MPL/2.0/


