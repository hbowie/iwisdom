<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Generate an index page for all authors -->
  <xsl:output method="html" version="4.01"/>
  <xsl:output doctype-system="http://www.w3.org/TR/html4/strict.dtd"/>
  <xsl:output doctype-public="-//W3C//DTD HTML 4.01/EN"/>
  <xsl:template match="pw:wisdom">
<!-- Begin the index page for all authors -->
<html>
  <head>
    <title>
      PortableWisdom.org | Authors
    </title>
<!-- #bbinclude "header.html" -->
  <base target="_top" />
  <link rel="stylesheet" href="../../styles/html.css" type="text/css" title="Style Sheet" />
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
          <li class="leftside"><a class="leftside" href="../index.html" title="Wisdom">Wisdom</a></li>
          <li class="leftsub"><a class="leftsub" href="../random.html" title="Random">Random</a></li>
        </ul>
      </td>
      <td><img src="../../../images/pixel.gif" width="60" alt="" /></td>
      <td valign="top">
<!-- end bbinclude -->
        <h1>
          <a href="../index.html">
            Wisdom</a>
          |
          Authors
        </h1>
        <ul class="mainlist">
          <xsl:for-each-group select="pw:item" group-by="pw:author/pw:name">
            <xsl:variable name="url" 
                select="normalize-space(concat(normalize-space(pw:author/pw:file-name), '/index.html'))"/>
            <li>
              <a href="{$url}">
                <xsl:value-of select="current-grouping-key()"/>
              </a>
            </li>
          </xsl:for-each-group>
        </ul>
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
  
</xsl:stylesheet>