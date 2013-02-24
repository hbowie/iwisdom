<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
    xmlns:pw="http://www.powersurgepub.com/xml/wisdom"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Generate an index page for all Wisdom -->
  <xsl:output method="html" version="4.01"/>
  <xsl:output doctype-system="http://www.w3.org/TR/html4/strict.dtd"/>
  <xsl:output doctype-public="-//W3C//DTD HTML 4.01/EN"/>
  <xsl:template match="pw:wisdom">
<!-- Begin the index page for all sources -->
<html>
  <head>
    <title>
      PortableWisdom.org | Works
    </title>
<!-- #bbinclude "header.html" -->
  <base target="_top" />
  <link rel="stylesheet" href="./../styles/html.css" type="text/css" title="Style Sheet" />
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
          <li class="leftside"><a class="leftside" href="./index.html" title="Wisdom">Wisdom</a></li>
          <li class="leftsub"><a class="leftsub" href="./random.html" title="Random">Random</a></li>
        </ul>
      </td>
      <td><img src="./../../images/pixel.gif" width="60" alt="" /></td>
      <td valign="top">
<!-- end bbinclude -->
      <h1>The Wisdom Collection</h1>

      <p class="first">This is my particular collection of wisdom.  All of the HTML has been completely generated by the <a href="../../iwisdom.html">iWisdom</a> software, using XSL stylesheets. The collection can be accessed in a number of ways. </p>
      
      <h1><a href="random.html">Random</a></h1>

      <p class="first">View a <a href="random.html">random</a> piece of wisdom. </p>

      <h1><a href="authors/index.html">Authors</a></h1>

      <p class="first">See a list of <a href="authors/index.html">all authors</a> represented in the collection. </p>
      
      <h1><a href="sources.html">Works</a></h1>

      <p class="first">See a list of <a href="sources.html">all works</a> (such as books) represented in this collection. </p>
      
      <h1><a href="categories/index.html">Categories</a></h1>

      <p class="first">See a list of wisdom <a href="categories/index.html">categories</a>. </p>

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