<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
  
  <xsl:template match="/Rechnungen">
    <xsl:element name="Rechnungen">
      <xsl:for-each select="Rechnung[number(translate(Austellungsdatum, '-', '')) > 20100000]"> 
        <xsl:copy>
          <xsl:copy-of select="Austellungsdatum"/>
          <xsl:copy-of select="Rechnungsnummer"/>
          <xsl:copy-of select="Entgelt/Brutto"/>
          <xsl:copy-of select="Leistungsempfaenger[starts-with(Anrede, 'Herr') or starts-with(Anrede, 'Frau')]"/>
          <xsl:apply-templates select="Positionen"/>
        </xsl:copy>
      </xsl:for-each> 
    </xsl:element>
  </xsl:template>
  <xsl:template match="Positionen">
    <xsl:copy>
      <xsl:for-each select="Leistung">
        <xsl:copy>
          <xsl:copy-of select="Art"/>
          <xsl:attribute name="Positionsnummer">
            <xsl:value-of select="position()" />
          </xsl:attribute>
          <xsl:attribute name="Menge">
            <xsl:value-of select="//@Menge"/>
          </xsl:attribute>
          <xsl:copy-of select="Preis/Gesamt"/>
        </xsl:copy> 
      </xsl:for-each>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>