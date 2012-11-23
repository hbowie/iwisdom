<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Generate a javascript include file to load an array with all wisdom -->
  <xsl:output method="text"/>
  <xsl:template match="pw:wisdom">
<!-- Define the array and necessary variables -->
var qfilename = new Array();
var ix = 0;
<xsl:apply-templates select="pw:item"/>
var max = ix;
  </xsl:template>
  <xsl:template match="pw:item">
  <xsl:variable name="wisdomurl" 
    select="normalize-space(concat(
      'authors/',
      normalize-space(pw:author/pw:file-name), 
      '/',
      normalize-space(pw:source/pw:file-name),
      '/',
      normalize-space(pw:file-name),
      '.html'))"/>
<xsl:text>qfilename[ix] = "</xsl:text>
<xsl:value-of select="$wisdomurl"/>
<xsl:text>";
</xsl:text>
<xsl:text>ix++;
</xsl:text>
  </xsl:template>
</xsl:stylesheet>