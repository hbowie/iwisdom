<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes" 
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />
  <xsl:template match="pw:item">
<html>
  <head>
      <xsl:apply-templates select="pw:title" mode="head"/>
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
        <div id="quote">
          <xsl:apply-templates select="pw:category"/>
          <xsl:apply-templates select="pw:title" mode="body"/>
          <xsl:apply-templates select="pw:body"/>
          <xsl:apply-templates select="pw:author"/>
          <xsl:apply-templates select="pw:source"/>
          <div class="xmllink">
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
            </div>
        </div>
<!-- #bbinclude "footer.html" -->
    </div>
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
    	  <h1>
      		<xsl:apply-templates select="pw:category1"/>
      		<xsl:apply-templates select="pw:category2"/>
      		<xsl:apply-templates select="pw:category3"/>
      	</h1>
  </xsl:template>
  
  <xsl:template match="pw:category1">
    <xsl:variable name="caturl" 
        select="normalize-space(concat(
          '../../../categories/', translate(normalize-space(lower-case(.)), ' ', '_'), '/index.html'))"/>
      <a href="{$caturl}">  
        <xsl:value-of select="."/></a>
  </xsl:template>
  
  <xsl:template match="pw:category2">
      | <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="pw:category3">
      | <xsl:value-of select="."/>
  </xsl:template>
  
  <xsl:template match="pw:title" mode="body">
    		<p class="title">
      		<xsl:value-of select="."/>
      	</p>
  </xsl:template>
  
  <xsl:template match="pw:body">
    	  <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="p">
    		<p class="body">
      		<xsl:value-of select="."/>
      	</p>
  </xsl:template>
  
  <xsl:template match="ol">
    		<ol>
      		<xsl:apply-templates select="li"/>
      	</ol>
  </xsl:template>
  
  <xsl:template match="ul">
    		<ul>
      		<xsl:apply-templates select="li"/>
      	</ul>
  </xsl:template>
  
  <xsl:template match="li">
    		<li>
      		<xsl:value-of select="."/>
      	</li>
  </xsl:template>
  
  <xsl:template match="pw:author">
    		  <div class="emdash">
              --
    		  </div>    
    			<div class="author">
    			  <a href="../index.html">
    				  <xsl:value-of select="pw:name"/></a> 
    				  <xsl:value-of select="pw:author-info"/>
    			</div>
  </xsl:template>
  
  <xsl:template match="pw:source">
          <xsl:variable name="titlew" select="normalize-space(pw:title)"/>
          <xsl:if test="($titlew != 'Unknown') and ($titlew != '')">
            <div class="sourceType">from the <xsl:value-of select="pw:type"/></div>
            <div class="quoteSourceTitle">
              
              <a href="index.html">
                <xsl:value-of select="pw:title"/>
              </a>
            </div>
            <div class="sourceRights">
               <xsl:value-of select="pw:rights"/>
               <xsl:value-of select="pw:year"/>
               <xsl:value-of select="pw:owner"/>
            </div>
            <xsl:apply-templates select="pw:link"/>
          </xsl:if>
          
          <xsl:if test="($titlew = 'Unknown') or ($titlew = '')">
            <div class="sourceType">from an unknown source</div>
          </xsl:if>
  </xsl:template>
  
  <xsl:template match="pw:link">
    			<p class="sourceRights">
            <xsl:variable name="sourceurl" select="normalize-space(.)"/>
              <a href="{$sourceurl}">
    			      &lt;Link to Source&gt;
    			    </a>
    			</p>
  </xsl:template>
  
</xsl:stylesheet>