<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="pw:wisdom">
    <rss version="2.0">
      <channel>
        <xsl:apply-templates select="pw:title" mode="channel" />
        <xsl:apply-templates select="pw:link" mode="channel" />
        <xsl:apply-templates select="pw:description" mode="channel" />
    <generator>
      iWisdom
    </generator>
        <xsl:apply-templates select="pw:editor" />
        <xsl:apply-templates select="pw:item">
          <xsl:sort select="pw:date-added" order="descending" />
        </xsl:apply-templates>
      </channel>
    </rss>
  </xsl:template>
  
  <xsl:template match="pw:title" mode="channel">
    <title>
      <xsl:value-of select="."/>
    </title>
  </xsl:template>
  
  <xsl:template match="pw:link" mode="channel">
    <link>
      <xsl:value-of select="."/>
    </link>
  </xsl:template>
  
  <xsl:template match="pw:description" mode="channel">
    <description>
      <xsl:value-of select="."/>
    </description>
  </xsl:template>
  
  <xsl:template match="pw:editor">
    <managingEditor>
      <xsl:value-of select="."/>
    </managingEditor>
  </xsl:template>
  
  <xsl:template match="pw:item">
    <xsl:if test="position() &lt;= 5">
      <item>
        <title>
          <xsl:value-of select="pw:title"/>
        </title>
        <xsl:variable name="wisdomurl" 
                  select="normalize-space(concat(
                      normalize-space(../pw:link), 
                      normalize-space(../pw:path),
                      '/html/authors/', 
                      normalize-space(pw:author/pw:file-name), '/',
                      normalize-space(pw:source/pw:file-name), '/',
                      normalize-space(pw:file-name), '.html'))" />
        <link>
          <xsl:value-of select="$wisdomurl" />
        </link>
        <description>
          <xsl:value-of select="pw:body" />
        </description>
        <pubDate>
          <xsl:value-of select="format-dateTime(pw:date-added,
              '[FNn,3-3], [D01] [MNn,3-3] [Y] [H01]:[m01]:[s01] [Z]')" />
        </pubDate>
        <guid>
          <xsl:value-of select="$wisdomurl" />
        </guid>
      </item>
    </xsl:if>
  </xsl:template>
  
</xsl:stylesheet>