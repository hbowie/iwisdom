<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" version="4.01"/>
  <xsl:output doctype-system="http://www.w3.org/TR/html4/strict.dtd"/>
  <xsl:output doctype-public="-//W3C//DTD HTML 4.01/EN"/>
  <xsl:template match="wisdom">
<html>
  <head>
      <xsl:apply-templates select="title" mode="head"/>
    <style type="text/css">
      body {
        font-family: "Big Caslon", Georgia, serif;
        color: #fff;
        background-color: #6463ae; 
        font-size: 1.0em;
        text-align: left;
        vertical-align: top;
        margin-top: 0;
        margin-left: 0;
        margin-right: 0;
        margin-bottom: 0;
        padding-top: 0;
        padding-left: 0;
        padding-right: 0;
        padding-bottom: 0;
        }
        
      p {
        font-family: "Big Caslon", Georgia, serif;
        color: #fff;
        background-color: #6463ae; 
        font-size: 1.0em;
        text-align: left;
        vertical-align: top;
        margin-top: 0.5em;
        margin-left: 0;
        margin-right: 0;
        margin-bottom: 0.5em;
        padding-top: 0;
        padding-left: 0;
        padding-right: 0;
        padding-bottom: 0;
        }
        
      .logo {
        color: #F7980E;
        font-family: Georgia, serif;
        font-size: 2.0em;
        letter-spacing: 0.1em;
      }
      
      .category {
        font-family: Verdana, "Bitstream Vera Sans", sans-serif;
        color: #ddd;
      }
      
      .title
      {
        font-size: 1.6em;
        letter-spacing: 0.1em;
      }
      
      .body {
        font-size: 1.0em;
        letter-spacing: 0.1em;
        line-height: 1.8em;
      }
      
      .by {
      	text-align: right;
      }
      
      .sourceTitle {
      	font-style: italic;
      }
    </style>
  </head>
  <body>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="3"> </td>
        <td colspan="5">
        		<p class="logo">
    				Portable Wisdom.org
    			</p>
    		</td>
    		<td> </td>
    	</tr>
      <xsl:apply-templates select="category"/>
      <xsl:apply-templates select="title" mode="body"/>
      <xsl:apply-templates select="body"/>
      <xsl:apply-templates select="author"/>
      <xsl:apply-templates select="source"/>
    </table>
  </body>
</html>
  </xsl:template>
  
  <xsl:template match="title" mode="head">
    <title>
      <xsl:value-of select="."/>
    </title>
  </xsl:template>
  
  <xsl:template match="category">
    <tr>
    	<td colspan="3"> </td>
    	<td colspan="6">
    		<p class="category">
      		<xsl:apply-templates select="category1"/>
      		<xsl:apply-templates select="category2"/>
      		<xsl:apply-templates select="category3"/>
      	</p>
    	</td>
    </tr>
  </xsl:template>
  
  <xsl:template match="category1">
      <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="category2">
      | <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="category3">
      | <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="title" mode="body">
    <tr>
    	<td colspan="3"> </td>
    	<td colspan="5">
    		<p class="title">
      		<xsl:value-of select="."/>
      	</p>
      </td>
      <td> </td>
    </tr>
  </xsl:template>
  
  <xsl:template match="body">
    <tr>
    	<td colspan="3"> </td>
    	<td colspan="5"	>
    		<p class="body">
      		<xsl:value-of select="p"/>
      	</p>
      </td>
      <td> </td>
    </tr>
  </xsl:template>
  
  <xsl:template match="author">
    	<tr>
    		<td colspan="3"> </td>
    		<td width="10px"> </td>
    		<td class="by">&#8212;</td>
    		<td width="2px"> </td>
    		<td>
    			<p>
    				<xsl:value-of select="complete_name"/>
    			</p>
    		</td>
    		<td> </td>
      </tr>
  </xsl:template>
  
  <xsl:template match="source">
  		<tr>
  			<td colspan="3"> </td>
  			<td colspan="3"> </td>
  			<td>
    			<p>from the <xsl:value-of select="type"/></p>
    			<p class="sourceTitle"><xsl:value-of select="title"/></p>
    			<p><xsl:value-of select="rights"/></p>
    		</td>
    		<td> </td>
    	</tr>
  </xsl:template>
  
</xsl:stylesheet>