/*
 * Update.java
 *
 * Created on November 2, 2005, 6:31 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.powersurgepub.iwisdom.updates;

  import com.powersurgepub.psdatalib.psdata.RecordDefinition;
import com.powersurgepub.psdatalib.psdata.DataField;
import com.powersurgepub.psdatalib.psdata.DataRecord;
  import java.text.*;
  import java.util.*;

/**
 *
 * @author hbowie
 */
public class Update {
  
  /** Default date for a new to do item. */
  public final static GregorianCalendar DEFAULT_DATE 
      = new GregorianCalendar (2050, 11, 1);
  
  public final static DateFormat DATE_TIME_FORMAT 
      = DateFormat.getDateTimeInstance();
  
  public  final static int  NUMBER_OF_COLUMNS     = 8;
  
  public  final static int  DONE                  = 0;
  public  final static int  UPDATE_DATE           = 1;
  public  final static int  TRAN_SEQ              = 2;
  public  final static int  TITLE                 = 3;
  public  final static int  DUE_DATE              = 4;
  public  final static int  FIELD_NAME            = 5;
  public  final static int  BEFORE                = 6;
  public  final static int  AFTER                 = 7;
  
  public  final static String[] COLUMN_NAME = {
    "done",
    "updatedate",
    "transeq",
    "title",
    "duedate",
    "fieldname",
    "before",
    "after"
  };
  
  /** Has this change been done (or has it been undone)? */
  private     boolean       done = false;
  
  /** Date and time of update. */
  private     Date          updateDate = new Date();
  
  /** Transaction sequence number */
  private     int           tranSeq = 0;
  
  /** The title of this item */
	private    	String          title;
  
  /** Date on which this item is to be completed. */
  private     Date            dueDate = DEFAULT_DATE.getTime();
  
  /** Name of field being changed. */
  private     String          fieldName;
  
  /** Value before update */
  private     String          before;
  
  /** Value after update */
  private     String          after;
  
  /** Creates a new instance of Update */
  public Update() {
  }
  
	/**
	   Constructor with data record argument.
 
     @param toDoRec   DataRecord to be used to populate this item.
	  */
	public Update (DataRecord tabRec) {

		setMultiple (tabRec);
	}
  
  /**
    Returns a record definition for an Update item, 
    in com.powersurgepub.psdata.RecordDefinition format.
   
    @return A record format definition for a To Do item.
   */
  public static RecordDefinition getRecDef() {
    RecordDefinition recDef = new RecordDefinition();
    for (int i = 1; i <= NUMBER_OF_COLUMNS; i++) {
      recDef.addColumn (COLUMN_NAME [i]);
    }
    return recDef;
  }
  
  /**
    Sets multiple fields based on contents of passed record.
  
    @param  tabRec   A data record containing multiple fields.
   */
  public void setMultiple (DataRecord tabRec) {
    tabRec.startWithFirstField();
    DataField nextField;
    String name;
  
    while (tabRec.hasMoreFields()) {
      nextField = tabRec.nextField();
      name = nextField.getCommonFormOfName();
      if (name.equals (COLUMN_NAME [DONE])) {
        setDone (nextField.getDataBoolean());
      }
      else
      if (name.equals (COLUMN_NAME [UPDATE_DATE])) {
        setUpdateDate (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [TRAN_SEQ])) {
        setTranSeq (nextField.getDataInteger());
      }
      else
      if (name.equals (COLUMN_NAME [TITLE])) {
        setTitle (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [DUE_DATE])) {
        setDueDate (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [FIELD_NAME])) {
        setFieldName (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [BEFORE])) {
        setBefore (nextField.getData());
      }
      else
      if (name.equals (COLUMN_NAME [AFTER])) {
        setAfter (nextField.getData());
      }
    } // end while
  } // end method
  
  public void setDone (boolean done) {
    this.done = done;
  }
  
  public void setTranSeq (int tranSeq) {
    this.tranSeq = tranSeq;
  }
  
  public void setTitle (String title) {
    this.title = title;
  }
  
  public void setFieldName (String fieldName) {
    this.fieldName = fieldName;
  }
  
  public void setBefore (String before) {
    this.before = before;
  }
  
  public void setAfter (String after) {
    this.after = after;
  }
  
  public void setUpdateDate (String date) {
    setUpdateDate (DATE_TIME_FORMAT, date);
  }
  
  /**
     Sets the update date for this item.
 
     @param  fmt  A DateFormat instance to be used to parse the following string.
     @param  date String representation of a date.
   */
  public void setUpdateDate (DateFormat fmt, String date) {
    
    try {
      setUpdateDate (fmt.parse (date));
    } catch (ParseException e) {
    }

  } // end method
  
  /**
     Sets the update date for this item.
 
     @param  date Date representation of a date.
   */
  public void setUpdateDate (Date date) {
    
    updateDate = date;

  } // end method
  
  public void setDueDate (String date) {
    setDueDate (DATE_TIME_FORMAT, date);
  }
  
  /**
     Sets the due date for this item.
 
     @param  fmt  A DateFormat instance to be used to parse the following string.
     @param  date String representation of a date.
   */
  public void setDueDate (DateFormat fmt, String date) {
    
    try {
      setDueDate (fmt.parse (date));
    } catch (ParseException e) {
    }

  } // end method
  
  /**
     Sets the due date for this item.
 
     @param  date Date representation of a date.
   */
  public void setDueDate (Date date) {
    
    dueDate = date;

  } // end method
  
  /**
    Return this object, formatted as a DataRecord.
   
    @param recDef Record Definition to be used in building the record. 
    */
  public DataRecord getDataRec (RecordDefinition recDef) {

    DataRecord nextRec = new DataRecord();

    nextRec.addField (recDef, getDoneAsString());
    nextRec.addField (recDef, getUpdateDateAsString());
    nextRec.addField (recDef, getTranSeqAsString());
    nextRec.addField (recDef, getTitle());
    nextRec.addField (recDef, getDueDateAsString());
    nextRec.addField (recDef, getFieldName());
    nextRec.addField (recDef, getBefore());
    nextRec.addField (recDef, getAfter());
    return nextRec;
  }
  
  public String getDoneAsString () {
    return Boolean.toString (done);
  }
  
  public boolean isDone () {
    return done;
  }
  
  public boolean getDone () {
    return done;
  }
  
  public String getUpdateDateAsString () {
    return DATE_TIME_FORMAT.format (updateDate);
  }
  
  public Date getUpdateDate () {
    return updateDate;
  }
  
  public String getTranSeqAsString () {
    return String.valueOf (tranSeq);
  }
  
  public int getTranSeq () {
    return tranSeq;
  }
  
  public String getTitle () {
    return title;
  }
  
  public String getDueDateAsString () {
    return DATE_TIME_FORMAT.format (dueDate);
  }
  
  public Date getDueDate () {
    return dueDate;
  }
  
  public String getFieldName () {
    return fieldName;
  }
  
  public String getBefore () {
    return before;
  }
  
  public String getAfter () {
    return after;
  }
  
  public String toString() {
    return (getDoneAsString() + " "
        + getUpdateDateAsString() + " "
        + getTranSeqAsString() + " "
        + getTitle() + " "
        + getDueDateAsString() + " "
        + getFieldName() + " "
        + getBefore() + " "
        + getAfter());
  }
}
