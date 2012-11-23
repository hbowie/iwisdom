/*
 * ChainLink.java
 *
 * Created on February 26, 2005, 6:02 AM
 */

package com.powersurgepub.iwisdom.data;

/**
 *
 * @author hbowie
 */
public interface ChainLink {
  
  public void addChildLink (ChainLink child);
  
  public void setNextLink (ChainLink link);
  
  public ChainLink getLastChildLink();
  
}
