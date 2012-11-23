iWisdom Design Notes
====================

Views
-----

### Ancestry ###

The code for the Views in iWisdom was adapted from a similar implementation for Two Due. 

## Defaults ##

If no views are available (as when iWisdom is installed and opened for the first time), then the list of views will be initialized with a View by Author and a View by Title.

### Disk Storage ###

The views are stored within each Wisdom folder as a tab-delimited text file named 'views.txt'.

### Class Structure ###

data.ItemComparator -- Used to compare two wisdom items and determine their desired sequence. 
data.ItemSelector -- Used to filter wisdom items (suspected to be unused for iWisdom).
data.ViewList -- Stores the current list of views (names, comparators and selectors).
disk.WisdomDiskDirectory -- Creates the views ViewList during construction. (This seems to be the only place where the ViewList is actually created.)

