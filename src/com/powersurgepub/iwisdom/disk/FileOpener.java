package com.powersurgepub.iwisdom.disk;

  import java.io.*;

/**
 * A standard interface for a user interfacing program capable
 * of opening a passed file. <p>
 *
 * This code is copyright (c) 2002-2003 by Herb Bowie.
 * All rights reserved. <p>
 *
 * Version History: <ul><li>
 *     </ul>
 *
 * @author Herb Bowie (<a href="mailto:herb@powersurgepub.com">
 *         herb@powersurgepub.com</a>)<br>
 *         of PowerSurge Publishing 
 *         (<a href="http://www.powersurgepub.com">
 *         www.powersurgepub.com</a>)
 *
 * @version 2003/10/20 - Originally written.
 */
public interface FileOpener {
  
  /**      
    Standard way to respond to a request to open a file.
   
    @param inFile File to be opened by this application.
   */
  public void handleOpenFile (File inFile);
  
  /**      
    Standard way to respond to a request to open a file with specified
    viewing options.
   
    @param inFile File to be opened by this application.
    @param view   View options to be used for this file. 
   
  public void handleOpenFile (File inFile, String view);
   */
  
  /**      
    Standard way to respond to a request to open a file with specified
    viewing options and Web publishing template.
   
    @param inFile File to be opened by this application.
    @param view   View options to be used for this file. 
    @param template Template file to be used to publish this file.
   
  public void handleOpenFile (File inFile, String view, File template);
   */
  
  /**      
    Standard way to respond to a request to open a file with specified
    viewing options and Web publishing template and publishing frequency.
   
    @param inFile File to be opened by this application.
    @param view   View options to be used for this file. 
    @param template Template file to be used to publish this file.
    @param publishWhen Indicator of how often file should be published. 
   
  public void handleOpenFile (File inFile, String view, File template, int publish);
   */
  
  /**      
    Standard way to respond to a request to open a file with specified
    viewing options and Web publishing template and publishing frequency.
   
    @param inStore Disk store to be opened by this application.
   */
  public void handleOpenFile (WisdomDiskStore inStore);
  
}
