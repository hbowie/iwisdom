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

package com.powersurgepub.iwisdom.disk;

  import java.io.*;

/**
 * A standard interface for a user interfacing program capable
 * of opening a passed file. 
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
