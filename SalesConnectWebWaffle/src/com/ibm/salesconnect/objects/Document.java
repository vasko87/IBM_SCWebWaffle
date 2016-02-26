/**
 * 
 */
package com.ibm.salesconnect.objects;

import java.util.Vector;

/**
 * @author timlehane
 * @date May 30, 2013
 */
public class Document {
		public String sDocSourcee="";
		public String sDocLocation="";
		public String sExternalFileName="";
		public String sDocName="";
		public String sDocType="";
		public String sPublishDate="";
		public String sExpireDate="";
		public String sDescription="";
		public String sRelatedDoc="";
		public String sLanguage="";
		public String sAssignedTo="";
		public String sStatus="";
		public String sRevision="";
		public boolean bTemplate=false;
		public String sCategory="";
		public String sTags="";
		public String sSubCategory="";
		public String sRelatedDocRevision="";
		public Vector<String> vTeams = new Vector<String>();
		public Vector<String> vUsers = new Vector<String>();
		public Vector<String> vShareWithOptions = new Vector<String>();
}
