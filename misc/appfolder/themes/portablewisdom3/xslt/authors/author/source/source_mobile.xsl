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
<!-- #bbinclude "header_mobile.html" -->
    <link rel="stylesheet" href="../../../../../styles/mobile.css" type="text/css" title="Portable Wisdom Mobile Style Sheet" />
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
<!-- #bbinclude "footer_mobile.html" -->
    </div>
    <div id="navlinks">
      <a href="../../../../../mobile.html" title="Home Page">
        Home
      </a>
      -
      <a href="../../../../../wisdom/mobile/index.html" title="Wisdom">
        Wisdom
      </a>
      -
      <a href="../../../../../wisdom/mobile/random.html" title="Random">
        Random
      </a>
      <br />
      <a href="../../../../../wisdom/mobile/authors/index.html" title="Authors">
        Authors
      </a>
      -
      <a href="../../../../../wisdom/mobile/categories/index.html" title="Categories">
        Categories
      </a>
      -
      <a href="../../../../../wisdom/mobile/sources.html" title="Sources">
        Works
      </a>
      <br />
      <a href="../../../../../classic.html" title="Classic">
        View in Classic
      </a>
      <br />
      Provided by 
      <a href="http://www.PaganTuna.com/Herb_Bowie">Herb Bowie</a>
    </div>
  </body>
</html>
<!-- end bbinclude -->
</xsl:result-document>
      </xsl:for-each-group>
    </xsl:for-each-group>
  </xsl:template>
  
</xsl:stylesheet>