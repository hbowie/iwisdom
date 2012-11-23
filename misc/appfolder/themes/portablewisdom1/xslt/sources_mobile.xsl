<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Generate an index page for all sources -->
  <xsl:output name="xml" method="xml" indent="yes" 
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />
  <xsl:template match="pw:wisdom">
<!-- Begin the index page for all sources -->
<html>
  <head>
    <title>
      PortableWisdom.org | Works
    </title>
<!-- #bbinclude "header_mobile.html" -->
    <link rel="stylesheet" href="../../styles/mobile.css" type="text/css" title="Portable Wisdom Mobile Style Sheet" />
    <link rel="icon" href="../../favicon.ico" />
    <link rel="shortcut icon" href="../../favicon.ico" />
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
        <a href="../../index.html">
          Portable Wisdom.org
        </a>
    </div>
    
    <div id="content">
<!-- end bbinclude -->
        <h1>
          <a href="index.html">
            Wisdom</a>
          |
          Works
        </h1>
        <ul class="mainlist">
          <xsl:for-each-group select="pw:item" group-by="pw:source/pw:title">
            <xsl:sort select="current-grouping-key()" />
              <xsl:if test="normalize-space(pw:source/pw:title) > ''" >
                <xsl:variable name="url" 
                    select="normalize-space(concat('authors/', normalize-space(pw:author/pw:file-name), '/', normalize-space(pw:source/pw:file-name),  '/index.html'))"/>
                <li>
                  <a href="{$url}">
                    <xsl:value-of select="current-grouping-key()"/>
                  </a>
                </li>
              </xsl:if>
          </xsl:for-each-group>
        </ul>
<!-- #bbinclude "footer_mobile.html" -->
    </div>
    <div id="navlinks">
      <a href="../../mobile.html" title="Home Page">
        Home
      </a>
      -
      <a href="../../wisdom/mobile/index.html" title="Wisdom">
        Wisdom
      </a>
      -
      <a href="../../wisdom/mobile/random.html" title="Random">
        Random
      </a>
      <br />
      <a href="../../wisdom/mobile/authors/index.html" title="Authors">
        Authors
      </a>
      -
      <a href="../../wisdom/mobile/categories/index.html" title="Categories">
        Categories
      </a>
      -
      <a href="../../wisdom/mobile/sources.html" title="Sources">
        Works
      </a>
      <br />
      <a href="../../classic.html" title="Classic">
        View in Classic
      </a>
      <br />
      Provided by 
      <a href="http://www.PaganTuna.com/Herb_Bowie">Herb Bowie</a>
    </div>
  </body>
</html>
<!-- end bbinclude -->
  </xsl:template>
  
</xsl:stylesheet>