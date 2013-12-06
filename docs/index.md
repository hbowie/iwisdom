iWisdom User Guide
==================

Table of Contents
-----------------

<div id="toc-div">

<ul>
<li><a href="#intro">Introduction</a></li>
<li><a href="#starting">Getting Started</a>
<ul>
<li><a href="#sysrqmts">System Requirements</a></li>
<li><a href="#rights">Rights</a></li>
<li><a href="#install">Installation</a></li>
<li><a href="#exec">Launching iWisdom</a></li>
<li><a href="#filelocs">File Locations</a></li>
<li><a href="#import">Import Quotations</a></li>
<li><a href="#import-wikiquote">Import Quotations from WikiQuote</a></li>
<li><a href="#backups">Backing Up and Restoring Quotations</a></li>
</ul></li>
<li><a href="#ui">Overall User Interface</a>
<ul>
<li><a href="#tabs-and-windows">Tabs and Windows</a></li>
<li><a href="#tool-bar">Tool Bar</a></li>
<li><a href="#menus">Menus and Keyboard Shortcuts</a></li>
<li><a href="#gen-prefs">Customizing the Look and Feel</a></li>
</ul></li>
<li><a href="#data">Wisdom Data</a>
<ul>
<li><a href="#get-info">Get Info</a></li>
<li><a href="#list-tab">Your Wisdom List</a></li>
<li><a href="#add-edit">Adding and Editing Wisdom</a></li>
<li><a href="#nodupes">Avoiding Duplicates</a></li>
<li><a href="#format-body">Formatting Wisdom</a></li>
<li><a href="#categories">Categories</a></li>
<li><a href="#authors-titles">Authors and Source Titles</a></li>
<li><a href="#links">Book and Author Links</a></li>
<li><a href="#lastaw">Copying Last Author and Work</a></li>
</ul></li>
<li><a href="#display">Displaying Wisdom</a></li>
<li><a href="#output">Getting Wisdom out of iWisdom</a>
<ul>
<li><a href="#transfer">Transfer/Update</a></li>
<li><a href="#export">Export</a></li>
<li><a href="#publish">Publish</a></li>
</ul></li>
<li><a href="#help">Help</a></li>
</ul>

</div>

<h2 id="intro">Introduction</h2>  

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

<h2 id="starting">Getting Started</h2>

<h3 id="sysrqmts">System Requirements</h3> 

iWisdom is written in Java and can run on any reasonably modern operating system, including Mac OS X, Windows and Linux. iWisdom requires a Java Runtime Environment (JRE), also known as a Java Virtual Machine (JVM). The version of this JRE/JVM must be at least 6. Visit [www.java.com][java] to download a recent version for most operating systems. Installation happens a bit differently under Mac OS X, but generally will occur fairly automatically when you try to launch a Java app for the first time.

Because iWisdom may be run on multiple platforms, it may look slightly different on different operating systems, and will obey slightly different conventions (using the CMD key on a Mac, vs. an ALT key on a PC, for example). 

<h3 id="rights">Rights</h3>

iWisdom Copyright &copy; 2003 - 2013 Herb Bowie

As of version 2.50, iWisdom is [open source software][osd]. 

Licensed under the Apache License, Version 2.0 (the &#8220;License&#8221;); you may not use this file except in compliance with the License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an &#8220;AS IS&#8221; BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

iWisdom also incorporates or adapts the following open source software libraries. 

* JExcelAPI &#8212; Copyright 2002 Andrew Khan, used under the terms of the [GNU General Public License][gnu]. 

* parboiled  &#8212; Copyright 2009-2011 Mathias Doenitz, used under the terms of the [Apache License, Version 2.0][apache]. 
	
* pegdown &#8212; Copyright 2010-2011 Mathias Doenitz, used under the terms of the [Apache License, Version 2.0][apache].

* Xerces &#8212; Copyright 1999-2012 The Apache Software Foundation, used under the terms of the [Apache License, Version 2.0][apache].

* Saxon &#8212; Copyright Michael H. Kay, used under the terms of the [Mozilla Public License, Version 1.0][mozilla].

<h3 id="install">Installation</h3>

Download the latest version from [PowerSurgePub.com][downloads]. Decompress the downloaded file. Drag the resulting file or folder into the location where you normally store your applications. If a folder is present, then drag the entire folder, and keep the inner files and folders in the same folder, without changing their names or locations. 

<h3 id="exec">Launching iWisdom</h3>

The iWisdom program can normally be launched by opening (double-clicking on) the file named "iwisdom.jar" or, on a Mac, the application simply named "iWisdom". 

<h3 id="filelocs">File Locations</h3>

The default location for your initial Wisdom collection is in your **home directory**, then in a folder named **Wisdom**, then in a folder named **iWisdom**. This collection will open automatically when you launch iWisdom.

You can create other Wisdom collections by selecting **New** from the File Menu. You can set one collection to be your primary collection by selecting **Get Info** from the File menu and then clicking on the check box to make the current folder your primary collection: your primary collection will then open automatically when launching iWisdom.

Note that an iWisdom collection consists of a folder and a number of files and possibly sub-folders. 

Once you have created an iWisdom collection, you may open it by selecting **Open** from the File Menu, and then selecting the collection's folder. You may also use the **Open Known** item on the File Menu, to be able to select a collection from a list of previously opened collections. 

The name of the current iWisdom collection appears at the bottom left of your main window. This display consists of the collection's folder and its parent folder, followed by "(P)" if this is your primary collection. Hover over the display to see the full path to the collection. 

<h3 id="import">Import Quotations</h3>

To jump-start your Wisdom collection, select **Import** from the File menu, then select the Portable Wisdom collection from the Favorites drop-down, then click on Import. (You will need an active Internet connection to import any of the Favorites.)

iWisdom is able to import wisdom from a number of different formats (including plain text and multiple XML-based formats). When importing, iWisdom will recognize duplicates (based on the actual content of each item's body) and intelligently combine duplicates if they contain differing degrees of detail. 

<h3 id="import-wikiquote">Import Quotations from WikiQuote</h3>

Quotations may also be imported from [www.WikiQuote.org][wikiq]. Select **Import from WikiQuote** from the File menu. In the resulting panel, select your desired language, then enter the name of an author and/or a particular work (book, movie, etc.). You may also optionally enter a word or phrase in the Containing field, to search for a quotation containing the entered text (capitalization is not significant). 

Click on the Search button to initiate the search of the specified WikiQuote page. You may then click on the Next button repeatedly to see each matching quotation, one by one. When you see a quotation you wish to capture for your collection, click on the Import button. Clicking on the Browse button will open the specified WikiQuote page in your Web browser. 

Please note that, due to variability in the way quotations are entered into WikiQuote, not all quotations may appear as candidates for import. If you wish to specific examples of failures to our attention, drop us a note at [support@powersurgepub.com][support].

<h3 id="backups">Backing Up and Restoring Quotations</h3>

The File menu contains a **Backup** command, and a **Revert from Backup** command. When performing a backup, the file location and name will default to a folder in your current iWisdom folder, and a name containing the current date and time. The **Revert from Backup** command will default to the same folder. 

It is recommended that you perform a backup before each **Import** (see the next section for info about this) so that you can easily revert to your prior state if the import creates unwanted results. It is also recommended that you perform a backup before installing a new version of iWisdom, or changing any of the storage options.

The Backup Preferences allow you to choose one of three backup modes:

* **Manual Only** -- Backups will only be performed when they are explicitly initiated by the user, via the **File** / **Backup** menu item. 

* **Occasional Suggestions** -- iWisdom will remind the user to perform a backup every seven days, and prior to major events such as imports and program upgrades. 

* **Automatic Backups** -- Backups will be performed automatically whenever the program quits, as well as prior to major events such as imports and program upgrades. 

The program will default to occasional suggestions. 

<h2 id="ui">Overall User Interface</h2>

<h3 id="tabs-and-windows">Tabs and Windows</h3>

The iWisdom user interface consists of a number of **windows**. The main window shows a split pane. On the left/top you will see your current collection, either in a List or grouped by Categories. On the right/bottom you will see the currently selected piece of wisdom, either in Display mode or in Edit mode. 

Opening the Preferences window will allow you to modify a number of the operating characteristics of the program. The Preferences window can be found under the iWisdom menu on the Mac, and under Tools / Options on other operating systems.

<h3 id="tool-bar">Tool Bar</h3>

The iWisdom interface also contains a **Tool Bar** containing a number of **Buttons**: the "OK" button saves your changes after adding/editing a wisdom item; the "+" and "-" buttons allow you to add a new item or delete an existing one, respectively. The next four buttons move you to the first item in your collection, the next item, the prior item or the last item, again respectively. Two other buttons allow you to quickly add or remove enclosing quotation marks around the Body of a piece of wisdom. The **Scan** button starts a process of briefly showing each item in your collection on a rotating basis. 

The following text box allows you to enter a text string you wish to search for. The following button, initially labeled **Find**, causes the first item containing that text string to be found and displayed. After the first item is found, this button will be relabeled **Again**, and will then allow you to continue forward through the list of found items.

<h3 id="menus">Menus and Keyboard Shortcuts</h3>

There are also a number of **Menus** listed across the top. If a command has a keyboard shortcut, then it will be listed as part of the command's menu item. 

The most commonly used commands are accessible in multiple ways. For example, you may navigate to the first item in the collection by clicking on the Tool Bar Button, or by selecting the command from the Item Menu, or through a keyboard shortcut.

Following is a list of all the menus, with a brief description of each menu's purpose. 

* File -- Functions to open and save external disk files
* Edit -- Standard Cut, Copy and Paste commands.
* List -- Functions that affect the list as a whole.
* Item -- Functions that affect a particular wisdom item.
* Tabs -- Provides Keyboard Shortcuts for accessing Tabs
* View -- List of Named Views.
* Tools -- Access to Options/Preferences.
* Window -- Access to special windows.
* Help -- Access the iWisdom User Guide or Web Site.

Following is a list of all keyboard shortcuts. The command key is used in conjunction with another key to invoke shortcuts on a Mac, while the control key is used on other platforms. 

<table class="graybox">

  <tr>
    <th class="graybox">
      Key
    </th>
    <th class="graybox">
      Menu
    </th>
    <th class="graybox">
      Menu Item
    </th>
  </tr>

  <tr>
    <td class="graybox">
      A
    </td>
    <td class="graybox">
      Edit
    </td>
    <td class="graybox">
      Select All
    </td>
  </tr>

  <tr>
    <td class="graybox">
      B
    </td>
    <td class="graybox">
      File
    </td>
    <td class="graybox">
      Backup
    </td>
  </tr>

  <tr>
    <td class="graybox">
      C
    </td>
    <td class="graybox">
      Edit
    </td>
    <td class="graybox">
      Copy
    </td>
  </tr>

  <tr>
    <td class="graybox">
      D
    </td>
    <td class="graybox">
      Item
    </td>
    <td class="graybox">
      Delete
    </td>
  </tr>

  <tr>
    <td class="graybox">
      E
    </td>
    <td class="graybox">
      File
    </td>
    <td class="graybox">
      Export
    </td>
  </tr>

  <tr>
    <td class="graybox">
      F
    </td>
    <td class="graybox">
      List
    </td>
    <td class="graybox">
      Find
    </td>
  </tr>

  <tr>
    <td class="graybox">
      G
    </td>
    <td class="graybox">
      List
    </td>
    <td class="graybox">
      Find Again
    </td>
  </tr>

  <tr>
    <td class="graybox">
      H
    </td>
    <td class="graybox">
      App (Mac) / <br />Help (Other)
    </td>
    <td class="graybox">
      Hide / <br />User Guide (Other)
    </td>
  </tr>

  <tr>
    <td class="graybox">
      I
    </td>
    <td class="graybox">
      File
    </td>
    <td class="graybox">
      Get Info
    </td>
  </tr>

  <tr>
    <td class="graybox">
      L
    </td>
    <td class="graybox">
      Item
    </td>
    <td class="graybox">
      Last Author and Work
    </td>
  </tr>

  <tr>
    <td class="graybox">
      N
    </td>
    <td class="graybox">
      Item
    </td>
    <td class="graybox">
      New
    </td>
  </tr>

  <tr>
    <td class="graybox">
      O
    </td>
    <td class="graybox">
      File
    </td>
    <td class="graybox">
      Open
    </td>
  </tr>

  <tr>
    <td class="graybox">
      P
    </td>
    <td class="graybox">
      File
    </td>
    <td class="graybox">
      Publish
    </td>
  </tr>

  <tr>
    <td class="graybox">
      Q
    </td>
    <td class="graybox">
      App (Mac) / <br />File (Other)
    </td>
    <td class="graybox">
      Quit / <br />Exit
    </td>
  </tr>

  <tr>
    <td class="graybox">
      T
    </td>
    <td class="graybox">
      Item
    </td>
    <td class="graybox">
      Transfer
    </td>
  </tr>

  <tr>
    <td class="graybox">
      U
    </td>
    <td class="graybox">
      Item
    </td>
    <td class="graybox">
      Update
    </td>
  </tr>

  <tr>
    <td class="graybox">
      V
    </td>
    <td class="graybox">
      Edit
    </td>
    <td class="graybox">
      Paste
    </td>
  </tr>

  <tr>
    <td class="graybox">
      W
    </td>
    <td class="graybox">
      Help
    </td>
    <td class="graybox">
      Reduce Window Size
    </td>
  </tr>

  <tr>
    <td class="graybox">
      X
    </td>
    <td class="graybox">
      Edit
    </td>
    <td class="graybox">
      Cut
    </td>
  </tr>

  <tr>
    <td class="graybox">
      1
    </td>
    <td class="graybox">
      Tabs
    </td>
    <td class="graybox">
      List
    </td>
  </tr>

  <tr>
    <td class="graybox">
      2
    </td>
    <td class="graybox">
      Tabs
    </td>
    <td class="graybox">
      Categories
    </td>
  </tr>

  <tr>
    <td class="graybox">
      3
    </td>
    <td class="graybox">
      Tabs
    </td>
    <td class="graybox">
      Edit
    </td>
  </tr>

  <tr>
    <td class="graybox">
      ,
    </td>
    <td class="graybox">
      App (Mac)
    </td>
    <td class="graybox">
      Preferences
    </td>
  </tr>

  <tr>
    <td class="graybox">
      ]
    </td>
    <td class="graybox">
      Item
    </td>
    <td class="graybox">
      Next
    </td>
  </tr>

  <tr>
    <td class="graybox">
      [
    </td>
    <td class="graybox">
      Item
    </td>
    <td class="graybox">
      Prior
    </td>
  </tr>

</table>

<h3 id="gen-prefs">Customizing the Look and Feel</h3>

The program's Common Preferences contain a number of options for modifying the program's look and feel. Feel free to experiment with these to find your favorite configuration. Some options may require you to quit and re-launch iWisdom before the changes will take effect. 

<h2 id="data">Wisdom Data</h2>

<h3 id="get-info">Get Info</h3>

Selecting **Get Info** from the **File** menu will bring up a window that allows you to enter a number of information items applicable to an entire wisdom collection. 

* Folder: Identifies the folder containing your wisdom collection.

* Format: The default is "XML", but you may also choose "Text - Structured". The latter is easier to edit, and may be preferable if you choose to sync your wisdom collection to "the cloud" or to other devices, using a service such as DropBox. 

* Organize within Folders? If checked, then your wisdom items will be organized within folders named after the authors and their works. If unchecked, your wisdom items will all be stored at the top level of your collection's folder. 

* Wisdom File Naming: Name by Item Number simply names your wisdom files using a sequential numbering scheme. Name by Title names your files based on the titles you assign each item. 

* Title: The overall title of your wisdom collection. This will be used when publishing your collection. 

* Link: A web link, if available, for the location of your published wisdom. 

* Path: The path to your wisdom collection. 

* Editor: Your name, as the editor of your collection.

* Description: A brief description of the intent of this wisdom collection. 

* Primary? If checked, this will become your primary collection, which will be automatically opened whenever you launch iWisdom. 

* Forget? Check this box as a way of telling iWisdom to drop this collection from its list of known collections, available from the **Open Known** menu. 

<h3 id="list-tab">Your Wisdom List</h3>

The **List Tab** will list all the items in your collection. You may change the sequence of the items on the List Tab by selecting the appropriate **View** from the **View Menu**. You may create a new View in the **View Preferences**, by clicking on the + sign and then entering the name of your View and its sort fields.

<h3 id="add-edit">Adding and Editing Wisdom</h3>

The Edit Tab allows you to enter or modify information associated with a Wisdom item. To create a new item, click on the "+" Button on the Tool Bar, or select **New** from the Item Menu. The data fields to be entered are then split between the Content, Author and Work tabs. 

When you are done editing an item, simply click on the Display Tab, or click on the OK button on the Tool Bar, in order to leave the Edit Tab and save all your entries to disk.

<h3 id="nodupes">Avoiding Duplicates</h3>

Note that, when adding or importing Wisdom item(s), iWisdom will attempt to avoid duplicate entries. It does so by comparing the body of new items to existing items, comparing only letters, ignoring spaces, punctuation and numbers. When it finds a match, iWisdom merges data from the new entry into the existing entry, filling in missing data where possible. 

Note also that iWisdom will avoid duplicate titles, appending a digit when necessary to maintain uniqueness. 

<h3 id="format-body">Formatting Wisdom</h3>

On the Edit Tab, the Body field contains the actual wisdom or quotation. This field may be formatted using common HTML commands. If you wish to use quotation marks, enter normal "straight" quotation marks. For an em dash (or long dash), use two consecutive hyphens. Separate paragraphs with a blank line.

<h3 id="categories">Categories</h3>

For each piece of wisdom, you may enter one or more categories and, for each, one or more sub-categories. Each category level is separated from the next by a forward slash or a period. Each separate category (including sub-categories) is separated from others by a comma.

Once a category is established for one piece of wisdom, you may assign it to other pieces of wisdom by selecting the desired category from the drop-down menu. You may do this by scrolling and clicking, or by typing the beginning letters of the desired category and pressing Return/Enter. 

If you don't see an appropriate category on the drop-down list, simply type in the new category that you want. Once you complete your edits for that item, your new category will become available the next time you select the drop-down list. 

If you decide to stop using a category, simply remove it or change it on all the items to which it has been assigned. The next time you quit and relaunch iWisdom unused categories will disappear from the list. 

If you'd like to change a Category name, use the **Add/Replace Category** Menu Item from the **List** menu to change all occurrence of one Category to a new one. 

The Categories Tab organizes your collection by Category. Click on a disclosure triangle to view enclosed items and sub-categories.

<h3 id="authors-titles">Authors and Source Titles</h3>

When entering an author, you may select an existing author from the drop-down menu by entering the beginning characters of their name (either first name first or last name first), or by scrolling down the list, then pressing Return/Enter to select the desired author. 

Selecting an existing source title may be done in the same way.

<h3 id="links">Book and Author Links</h3>

On the Edit tab, enter a book's 10-digit ISBN in the Source ID field, to enable access to the book's entry on Amazon.com from the Display tab. 

Similarly, entering the Author's name on the Edit tab will allow the author's Wikipedia entry to be accessed from the Display tab.

<h3 id="lastaw">Copying Last Author and Work </h3>

When entering multiple quotations from the same source, you can use the **Last Author and Work** command from the **Item** menu to copy information about the Author and Work from the last item you entered to the new item you are currently entering. 

<h2 id="display">Displaying Wisdom</h2>

The **Display Tab** allows you to view a piece of wisdom. Click on an author's name to use your Web browser to view the author's entry in Wikipedia. Click on the name of a book to view the books's entry on Amazon.com.

Note that the Display Preferences allow the display to be customized. You can select a particular font, font size, and colors for the background and the text. 

You may also tailor which fields are displayed along with the wisdom body and its author, with Title, Source, Source Type, Added Date and Time, and iWisdom ID number all being optional. 

<h2 id="output">Getting Wisdom out of iWisdom</h2>

iWisdom offers several different ways to get your wisdom out of iWisdom, in a variety of formats. 

<h3 id="transfer">Transfer/Update</h3>

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

<h3 id="export">Export</h3>

The **Export** command, available from the **File** menu, allows you to export part or all of your collection into an external file that can be used by other programs. 

The following Export options are available, once you select the Export command. 

<h4 id="field_scope">Field Scope</h4>

Allows you to select one of the following options. 

* All Fields -- This options can be used for exporting into a format that preserves all iWisdom data fields as discrete entities in the resulting output file. This option is best when you will be using the output from iWisdom as import to another program (or to another iWisdom collection).

* Wisdom List -- This option creates a list of the desired wisdom entries, but formatted primarily to be read by a human, rather than a computer. 


<h4 id="item_scope">Item Scope</h4>

Allows you to select one of the following options.

* Current Item -- Exports only the single item that is currently selected. 

* Selected Category -- Exports all items in the Category that you select below. 

* Entire Collection -- Exports all items in the currently opened collection. 

<h4 id="format">Format</h4>

If you selected a Field Scope of All Fields, then you can select from one of the following formats. 

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

The publish option allows you to easily publish your entire wisdom collection as a Web site or E-Book. 

See [PortableWisdom.org][pw] for an example of a Web site that can be produced via this mechanism. 

To begin the publication process, select the **Publish&#8230;** command from the **File** menu.

You will then see a window with the following fields available to you. 

<dl>
    <dt>Publish to:</dt>
    <dd>You may use the Browse button above and to the right to select a folder on your computer to which you wish to publish your iWisdom collection. You may also enter or modify the path directly in the text box. When modifying this field, you will be prompted to specify whether you wish to update the existing publication location, or add a new one. By specifying that you wish to add a new one, you may create multiple publications, and then later select the publication of interest by using the drop-down arrow to the right of this field. </dd>
    <dt>Equivalent URL:</dt>
    <dd>If the folder to which you are publishing will be addressable from the World-Wide Web, then enter its Web address here. </dd>
    <dt>Templates:</dt>
    <dd>This is the address of a folder containing one or more publishing templates. This will default to the location of the templates provided along with the iWisdom executable. You may use the Browse button above and to the right to pick a different location, if you have your own templates you wish to use for publishing. </dd>
    <dt>Select:</dt>
    <dd>Use the drop-down list to select the template you wish to use. 
<br /> <br /><strong>E-Book</strong> &#8212; This template will produce a file named &#8216;iwisdom.epub&#8217;. You can drag and drop this file into iTunes, then sync it with your mobile device in order to access your collection from an iPhone, iPod Touch, or iPad.
<br /> <br /><strong>Web Site</strong> &#8212; This template will produce a Web site that can be browsed from a computer or mobile device. See <a href="http://www.portablewisdom.org">PortableWisdom.org</a> for an example. Note that, if you&#8217;re a <a href="http://www.me.com">Mobile Me</a> subscriber, you can easily publish to the Sites folder of your iDisk.
</dd>
    <dt>Apply</dt>
    <dd>Press this button to apply the selected template. This will copy the contents of the template folder to the location specified above as the Publish to location. </dd>
    <dt>Publish Script:</dt>
    <dd>Specify the location of the script to be used. iWisdom now uses the <a href="http://www.powersurgepub.com/products/pstextmerge.html">PSTextMerge</a> templating system for publishing. A PSTextMerge script will usually end with a &#8216;.tcz&#8217; file extension.  </dd>
    <dt>Publish when:</dt>
    <dd>You may specify publication &#8216;On Close&#8217; (whenever you Quit iWisdom or close a Wisdom collection) or &#8216;On Demand&#8217;. Note that when you Quit iWisdom or close a collection, iWisdom will publish to all of the publishing locations that you&#8217;ve said to publish &#8216;On Close&#8217;, if you&#8217;ve specified more than one. </dd>
    <dt>Publish Now</dt>
    <dd>Press this button to publish to the currently displayed location. Note that, if you&#8217;ve specified &#8216;On Demand&#8217;, then this is the only time that publication will occur. </dd>
    <dt>View:</dt>
    <dd>Select the local file location or the equivalent URL location.</dd>
    <dt>View Now</dt>
    <dd>Press this button to view the resulting Web site in your Web browser.</dd>
</dl>


<h2 id="help">Help</h2>

The following commands are available. Note that the first two commands open local documentation installed with your application, while the next group of commands will access the Internet and access the latest program documentation, where applicable. 

* **Program History** -- Opens the program's version history in your preferred Web browser.

* **User Guide** -- Opens the program's user guide in your preferred Web browser.

* **iWisdom Home Page** -- Open's the iWisdom product page on the World-Wide Web. 

* **iWisdom Import Sources** -- Takes you to a Web page where you will find a number of potential iWisdom import sources. 

* **Reduce Window Size** -- Restores the main iWisdom window to its default size and location. Note that this command has a shortcut so that it may be executed even when the iWisdom window is not visible. This command may sometimes prove useful if you use multiple monitors, but occasionally in different configurations. On Windows in particular, this sometimes results in iWisdom opening on a monitor that is no longer present, making it difficult to see.

[wikiq]:     http://www.wikiquote.org
[support]:   mailto:support@powersurgepub.com
[fortune]:   http://en.wikipedia.org/wiki/Fortune_(Unix)
[opml]:      http://en.wikipedia.org/wiki/OPML
[textile]:   http://en.wikipedia.org/wiki/Textile_(markup_language)
[pw]:        http://www.portablewisdom.org

[java]:      http://www.java.com/

[pspub]:     http://www.powersurgepub.com/
[downloads]: http://www.powersurgepub.com/downloads.html
[store]:     http://www.powersurgepub.com/store.html

[markdown]:  http://daringfireball.net/projects/markdown/
[pegdown]:   https://github.com/sirthias/pegdown/blob/master/LICENSE
[parboiled]: https://github.com/sirthias/parboiled/blob/master/LICENSE
[Mathias]:   https://github.com/sirthias

[club]:         clubplanner.html
[filedir]:      filedir.html
[metamarkdown]: metamarkdown.html
[template]:     template.html

[osd]:				http://opensource.org/osd
[gnu]:        http://www.gnu.org/licenses/
[apache]:			http://www.apache.org/licenses/LICENSE-2.0.html
[mozilla]:    http://www.mozilla.org/MPL/2.0/
