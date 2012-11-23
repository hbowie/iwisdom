<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Generate one index page per category -->
  <xsl:output name="xml" method="xml" indent="yes" 
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />
<!-- Generate one index page for each level 1 category -->
  <xsl:template match="pw:wisdom">
    <xsl:for-each-group select="pw:item" group-by="pw:category/pw:category1">
      <xsl:variable name="url" select="normalize-space(concat(translate(normalize-space(lower-case(current-grouping-key())), ' ', '_'), '/index.html'))"/>
<!-- Begin the index page for each category -->
<xsl:result-document format="xml" href="{$url}">
<html>
  <head>
    <title>
      PortableWisdom.org | 
      <xsl:value-of select="current-grouping-key()"/>
    </title>
<!-- #bbinclude "header.html" -->
    <link rel="stylesheet" href="../../../../styles/html.css" type="text/css" title="Portable Wisdom Standard Style Sheet" />
    <link rel="icon" href="../../../../favicon.ico" />
    <link rel="shortcut icon" href="../../../../favicon.ico" />
    <meta name="generator" content="BBEdit 8.6" />
    <meta name="REVISIT-AFTER" content="7 days" />
    <meta name="robots" content="All" />
    <meta name="Identifier-URL" content="http://www.portablewisdom.org/" />
    <meta name="Author" content="Herb Bowie" />
    <meta name="description" content="Brief quotations and concepts encapsulating valuable insights" />
    <meta name="keywords" content="Herb Bowie, portable wisdom, wisdom, quotations" />
  </head>
  <body>
  
    <div id="siteid">
        <a href="../../../../index.html">
          Portable Wisdom.org
        </a>
    </div>
    
    <div id="navlinks">
      <ul>
        <li><a href="../../../../index.html" title="Home Page">Home</a></li>
        <li><a href="../../../../wisdom/html/index.html" title="Wisdom">Wisdom</a>
          <ul>
            <li><a href="../../../../wisdom/html/authors/index.html" title="Authors">Authors</a></li>
            <li><a href="../../../../wisdom/html/categories/index.html" title="Categories">Categories</a></li>
            <li><a href="../../../../wisdom/html/random.html" title="Random">Random</a></li>
            <li><a href="../../../../wisdom/html/sources.html" title="Sources">Works</a></li>
            <li><a href="../../../../wisdom/xml/authors.xml" title="XML">xml</a></li>
          </ul>
        </li>
        <li><a href="../../../../project.html" title="The Project">Project</a></li>
        <li><a href="../../../../format.html" title="Data Format">Format</a></li>
        <li><a href="../../../../search.html" title="Search">Search</a></li>
        <li><a href="../../../../contribute.html" title="Contribute">Contribute</a></li>
        <li><a href="../../../../mobile.html" title="View PW Mobile Edition">Mobile</a></li>
      </ul>
      <div id="rss">
          <a href="../../../../wisdom/rss.xml" title="RSS 2.0">
            <img src="../../../../images/icon_rss.gif" alt="RSS 2.0" width="36" height="14" />
          </a>
      </div>
      <p>
        Provided by<br />
        <a href="http://www.PaganTuna.com/Herb_Bowie">Herb Bowie</a>
      </p>
    </div>
    
    <div id="content">
<!-- end bbinclude -->
        <h1>
          <a href="../index.html">
            Categories
          </a>
        </h1>
        <div class="authorIndexTitle">
          <xsl:value-of select="current-grouping-key()"/>
        </div>
        <xsl:variable name="groupcat" select="normalize-space(translate(string(current-grouping-key()), ' ', ''))"/>
        <xsl:for-each-group select="current-group()" group-by="pw:category">
        <xsl:variable name="thiscat" select="normalize-space(translate(string(current-grouping-key()), ' ', ''))"/>
        <xsl:if test="starts-with($thiscat, $groupcat)">
        <xsl:apply-templates select="pw:category[pw:category1 = $groupcat]"/>
        <ul class="mainlist">            
          <xsl:for-each select="current-group()">
            <li>
              <xsl:variable name="wisdomurl" 
                select="normalize-space(concat(
                    '../../authors/', 
                    normalize-space(pw:author/pw:file-name), '/',
                    normalize-space(pw:source/pw:file-name), '/', normalize-space(pw:file-name), '.html'))"/>
              <a href="{$wisdomurl}">
                <xsl:value-of select="pw:title"/>
              </a>
            </li>
          </xsl:for-each>
        </ul>  
        </xsl:if>
        </xsl:for-each-group>
<!-- #bbinclude "footer.html" -->
    </div>
  </body>
</html>
<!-- end bbinclude -->
</xsl:result-document>
    </xsl:for-each-group>
  </xsl:template>
    <xsl:template match="pw:category">
    <p class="sourceTitle">
      <xsl:apply-templates select="pw:category1"/>
      <xsl:apply-templates select="pw:category2"/>
      <xsl:apply-templates select="pw:category3"/>
    </p>
  </xsl:template>
  
  <xsl:template match="pw:category1">
      <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="pw:category2">
      | <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="pw:category3">
      | <xsl:value-of select="."/>
  </xsl:template>
  
</xsl:stylesheet>