<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="index">
    <xsl:copy>
      <xsl:apply-templates select="include"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="include">
      <xsl:copy-of select="document(@href)"/>
  </xsl:template>
  
</xsl:stylesheet>