/*
 * ListPrinter.java
 *
 * Created on November 3, 2005, 8:19 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.powersurgepub.iwisdom;

  import com.powersurgepub.iwisdom.data.*;
  import javax.swing.*;
  import java.awt.print.*;
  import javax.print.*;
  import javax.print.event.*;
  import javax.print.attribute.*;
  import javax.print.attribute.standard.*;
  import java.io.*;

/**
 *
 * @author hbowie
 */
public class ListPrinter {
  
  /** Creates a new instance of ListPrinter */
  public ListPrinter() {
  }
  
  public void print (SortedItems list) {
    
    // Get a list of all printers that can handle printable objects
    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
    PrintService[] services 
        = PrintServiceLookup.lookupPrintServices (flavor, null);
    
    // Set some printing attributes
    PrintRequestAttributeSet printAttributes
        = new HashPrintRequestAttributeSet();
    printAttributes.add (OrientationRequested.PORTRAIT);
    printAttributes.add (Chromaticity.MONOCHROME);
    
    // Display a dialog that allows the user to select one of the
    // available printers and to edit the default attributes
    PrintService service = ServiceUI.printDialog 
        (null, 
        100, 
        100,
        services,
        null,
        null,
        printAttributes);
    if (service != null) {
      
    }
  }
  
}
