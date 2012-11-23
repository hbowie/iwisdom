<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" version="4.01"/>
  <xsl:output doctype-system="http://www.w3.org/TR/html4/strict.dtd"/>
  <xsl:output doctype-public="-//W3C//DTD HTML 4.01/EN"/>
  <xsl:template match="pw:item">
<html>
  <head>
      <xsl:apply-templates select="pw:title" mode="head"/>
<!-- #bbinclude "header.html" -->
  <base target="_top" />
  <link rel="stylesheet" href="../../../../styles/html.css" type="text/css" title="Style Sheet" />
</head>
<body>
  <table border="0" cellpadding="0" cellspacing="2" width="100%">
    <tr>
      <td colspan="4">
         
      </td>
    </tr>
    <tr>
      <td class="leftside" width="205" valign="bottom">
        
      </td>
      <td width="60">
        
      </td>
      <td width="*">
        <p class="logo">

        </p>
      </td>
      <td width="30">
        
      </td>
    </tr>
    <tr>
      <td colspan="4">
        
      </td>
    </tr>
    <tr>
      <td class="leftside" width="205" valign="top">
        <ul class="leftside">
          <li class="leftside"><a class="leftside" href="../../../index.html" title="Wisdom">Wisdom</a></li>
          <li class="leftsub"><a class="leftsub" href="../../../random.html" title="Random">Random</a></li>
        </ul>
      </td>
      <td><img src="../../../../../images/pixel.gif" width="60" alt="" /></td>
      <td valign="top">
<!-- end bbinclude -->
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <xsl:apply-templates select="pw:category"/>
          <xsl:apply-templates select="pw:title" mode="body"/>
          <xsl:apply-templates select="pw:body"/>
          <xsl:apply-templates select="pw:author"/>
          <xsl:apply-templates select="pw:source"/>
          <tr>
    	    <td colspan="5"	>
    	      <p class="body">
    	        <xsl:variable name="xmlurl" 
                  select="normalize-space(concat(
                    '../../../../xml/authors/',
                    normalize-space(pw:author/pw:file-name), 
                    '/',
                    normalize-space(pw:source/pw:file-name),
                    '/',
                    normalize-space(pw:file-name),
                    '.xml'))"/>
                <a href="{$xmlurl}" target="xml">
                  &lt;xml&gt;
                </a>
      	      </p>
            </td>
          </tr>
        </table>
<!-- #bbinclude "footer.html" -->
    </td>
    
    <td>
       
    </td>
    
  </tr>
</table>

</body>
</html>
<!-- end bbinclude -->
  </xsl:template>
  
  <xsl:template match="pw:title" mode="head">
    <title>
      PortableWisdom.org | <xsl:value-of select="."/>
    </title>
  </xsl:template>
  
  <xsl:template match="pw:category">
    <tr>
    	<td align="left" colspan="5">
    	  <h1>
      		<xsl:apply-templates select="pw:category1"/>
      		<xsl:apply-templates select="pw:category2"/>
      		<xsl:apply-templates select="pw:category3"/>
      	  </h1>
    	</td>
    </tr>
  </xsl:template>
  
  <xsl:template match="pw:category1">
    <xsl:variable name="caturl" 
        select="normalize-space(concat(
          '../../../categories/', translate(normalize-space(lower-case(.)), ' ', '_'), '/index.html'))"/>
      <a href="{$caturl}">  
        <xsl:value-of select="."/>
      </a>
  </xsl:template>
  
  <xsl:template match="pw:category2">
      | <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="pw:category3">
      | <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="pw:title" mode="body">
    <tr>
    	<td colspan="5">
    		<p class="title">
      		<xsl:value-of select="."/>
      	</p>
      </td>
    </tr>
  </xsl:template>
  
  <xsl:template match="pw:body">
    <tr>
    	<td colspan="5"	>
    		<p class="body">
      		<xsl:value-of select="p"/>
      	</p>
      </td>
    </tr>
  </xsl:template>
  
  <xsl:template match="pw:author">
    	<tr>
    		<td width="10px"> </td>
    		<td align="right" valign="top">
    		  <p class="by" valign="top">
    		    &#8212;
    		  </p>    
    		</td>
    		<td width="4px"> </td>
    		<td>
    			<p class="author" valign="top">
    			  <a href="../index.html">
    				  <xsl:value-of select="pw:name"/>
    				</a>
    			</p>
    		</td>
    		<td width="10px"> </td>
      </tr>
  </xsl:template>
  
  <xsl:template match="pw:source">
  		<tr>
  			<td colspan="3"> </td>
  			<td>
    			<p class="sourceType">from the <xsl:value-of select="pw:type"/></p>
    			<p class="sourceTitle">
                          <a href="index.html">
    			    <xsl:value-of select="pw:title"/>
    			  </a>
    			</p>
    			<p class="sourceRights">
    			   <xsl:value-of select="pw:rights"/>
    			   <xsl:value-of select="pw:year"/>
    			   <xsl:value-of select="pw:owner"/>
    			</p>
    		</td>
    		<td> </td>
    	</tr>
  </xsl:template>
  
</xsl:stylesheet>