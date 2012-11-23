<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.portablewisdom.org/wisdom/20060210"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Generate one index page per author -->
  <xsl:output name="html" method="html" version="4.01"/>
  <xsl:output doctype-system="http://www.w3.org/TR/html4/strict.dtd"/>
  <xsl:output doctype-public="-//W3C//DTD HTML 4.01/EN"/>
  <xsl:template match="pw:wisdom">
    <xsl:for-each-group select="pw:item" group-by="pw:author/pw:name">
      <xsl:variable name="url" 
          select="normalize-space(concat(normalize-space(pw:author/pw:file-name), '/index.html'))"/>
<!-- Begin the index page for each author -->
<xsl:result-document format="html" href="{$url}">
<html>
  <head>
    <title>
      PortableWisdom.org | 
      <xsl:value-of select="current-grouping-key()"/>
    </title>
<!-- #bbinclude "header.html" -->
  <base target="_top" />
  <link rel="stylesheet" href="../../../../styles/html.css" type="text/css" title="Portable Wisdom Standard Style Sheet" />
  <link rel="icon" href="../../../../favicon.ico" />
  <link rel="shortcut icon" href="../../../../favicon.ico" />
  <meta name="REVISIT-AFTER" content="7 days" />
  <meta name="robots" content="All" />
  <meta name="Identifier-URL" content="http://www.portablewisdom.org/" />
  <meta name="Author" content="Herb Bowie" />
  <meta name="description" content="Brief quotations and concepts encapsulating valuable insights" />
  <meta name="keywords" content="Herb Bowie, portable wisdom, wisdom, quotations" />
  <title></title>
</head>
<body>
  <table border="0" cellpadding="0" cellspacing="2" width="100%">
    <tr>
      <td colspan="4">
        <img src="../../../../images/pixel.gif" height="30" alt="" />
      </td>
    </tr>
    <tr>
      <td class="leftside" width="205" valign="bottom">
        <img src="../../../../images/pixel.gif" width="205" height="1" />
      </td>
      <td width="60">
        <img src="../../../../images/pixel.gif" width="60" height="1" alt="" />
      </td>
      <td width="*">
        <p class="logo">
          <a class="logo" href="../../../../index.html">
            Portable Wisdom.org
          </a>
        </p>
      </td>
      <td width="30">
        <img src="../../../../images/pixel.gif" width="30" height="1" alt="" />
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <img src="../../../../images/pixel.gif" height="10" />
      </td>
    </tr>
    <tr>
      <td class="leftside" width="205" valign="top">
        <ul class="leftside">
          <li class="leftside"><a class="leftside" href="../../../../index.html" title="Home Page">Home</a></li>
          <li class="leftside"><a class="leftside" href="../../../../project.html" title="The Project">Project</a></li>
          <li class="leftside"><a class="leftside" href="../../../../format.html" title="Data Format">Format</a></li>
          <li class="leftside"><a class="leftside" href="../../../../wisdom.html" title="Wisdom">Wisdom</a></li>
          <li class="leftside"><a class="leftside" href="../../../../search.html" title="Search">Search</a></li>
          <li class="leftside"><a class="leftside" href="../../../../contribute.html" title="Contribute to the Project">Contribute</a></li>
        </ul>
      </td>
      <td><img src="../../../../images/pixel.gif" width="60" alt="" /></td>
      <td valign="top">
<!-- end bbinclude -->
        <h1>
          <a href="../index.html">
            Authors
          </a>
        </h1>
        <p class="authorIndexTitle">
          <xsl:value-of select="current-grouping-key()"/>
        </p>
        <xsl:variable name="wikipedialink" 
          select="normalize-space(pw:author/pw:wikipedia-link)"/>
        <p>
          <a href="{$wikipedialink}" target="ref">
            Wikipedia Link
          </a>
        </p>
        <xsl:for-each-group select="current-group()" group-by="pw:source/pw:title">
        <xsl:variable name="sourceurl" 
	    select="normalize-space(concat(normalize-space(pw:source/pw:file-name), 
            '/index.html'))"/>
        <p class="sourceTitle">
          <a href="{$sourceurl}">
            <xsl:value-of select="current-grouping-key()"/>
          </a>  
        </p>
        <ul class="mainlist">            
          <xsl:for-each select="current-group()">
            <li>
              <xsl:variable name="wisdomurl" 
                select="normalize-space(concat(normalize-space(pw:source/pw:file-name), 
                    '/', normalize-space(pw:file-name), '.html'))"/>
              <a href="{$wisdomurl}">
                <xsl:value-of select="pw:title"/>
              </a>
            </li>
          </xsl:for-each>
        </ul>  
        </xsl:for-each-group>
<!-- #bbinclude "footer.html" -->
    </td>
    
    <td>
       
    </td>
    
  </tr>
</table>

</body>
</html>
<!-- end bbinclude -->
</xsl:result-document>
    </xsl:for-each-group>
  </xsl:template>
  
</xsl:stylesheet>