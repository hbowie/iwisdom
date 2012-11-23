<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Generate one index page per source -->
  <xsl:output name="xml" method="xml" indent="yes" 
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />
  <xsl:template match="pw:wisdom">
    <xsl:for-each-group select="pw:item" group-by="pw:author/pw:name">
      <xsl:for-each-group select="current-group()" group-by="pw:source/pw:title">
      <xsl:variable name="url" 
          select="normalize-space(concat(normalize-space(pw:author/pw:file-name),
            '/', normalize-space(pw:source/pw:file-name), '/index.html'))"/>
<!-- Begin the index page for each source -->
<xsl:result-document format="xml" href="{$url}">
<html>
  <head>
    <title>
      PortableWisdom.org | 
      <xsl:value-of select="current-grouping-key()"/>
    </title>
<!-- #bbinclude "header.html" -->
    <link rel="stylesheet" href="../../../../../styles/html.css" type="text/css" title="Portable Wisdom Standard Style Sheet" />
    <link rel="icon" href="../../../../../favicon.ico" />
    <link rel="shortcut icon" href="../../../../../favicon.ico" />
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
        <a href="../../../../../index.html">
          Portable Wisdom.org
        </a>
    </div>
    
    <div id="navlinks">
      <ul>
        <li><a href="../../../../../index.html" title="Home Page">Home</a></li>
        <li><a href="../../../../../wisdom/html/index.html" title="Wisdom">Wisdom</a>
          <ul>
            <li><a href="../../../../../wisdom/html/authors/index.html" title="Authors">Authors</a></li>
            <li><a href="../../../../../wisdom/html/categories/index.html" title="Categories">Categories</a></li>
            <li><a href="../../../../../wisdom/html/random.html" title="Random">Random</a></li>
            <li><a href="../../../../../wisdom/html/sources.html" title="Sources">Works</a></li>
            <li><a href="../../../../../wisdom/xml/authors.xml" title="XML">xml</a></li>
          </ul>
        </li>
        <li><a href="../../../../../project.html" title="The Project">Project</a></li>
        <li><a href="../../../../../format.html" title="Data Format">Format</a></li>
        <li><a href="../../../../../search.html" title="Search">Search</a></li>
        <li><a href="../../../../../contribute.html" title="Contribute">Contribute</a></li>
        <li><a href="../../../../../mobile.html" title="View PW Mobile Edition">Mobile</a></li>
      </ul>
      <div id="rss">
          <a href="../../../../../wisdom/rss.xml" title="RSS 2.0">
            <img src="../../../../../images/icon_rss.gif" alt="RSS 2.0" width="36" height="14" />
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
          <a href="../../index.html">
            Authors
          </a>
        </h1>
        <div class="authorIndexTitle">
          <a href="../index.html">
            <xsl:value-of select="pw:author/pw:name"/>
          </a>  
        </div>        
        <div class="sourceTitle">
          <xsl:value-of select="current-grouping-key()"/>
        </div>
        <xsl:variable name="amazonlink" 
          select="normalize-space(concat('http://www.amazon.com/exec/obidos/ASIN/',
            normalize-space(pw:source/pw:identifier), '/portablewisdo-20'))"/>
        <div class="amazon">
          <a href="{$amazonlink}">
            Amazon.com
          </a>
        </div>
        <ul class="mainlist">            
          <xsl:for-each select="current-group()">
            <li>
              <xsl:variable name="wisdomurl" 
                select="normalize-space(concat(normalize-space(pw:file-name), '.html'))"/>
              <a href="{$wisdomurl}">
                <xsl:value-of select="pw:title"/>
              </a>
            </li>
          </xsl:for-each>
        </ul>  
<!-- #bbinclude "footer.html" -->
    </div>
  </body>
</html>
<!-- end bbinclude -->
</xsl:result-document>
      </xsl:for-each-group>
    </xsl:for-each-group>
  </xsl:template>
  
</xsl:stylesheet>