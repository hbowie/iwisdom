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
