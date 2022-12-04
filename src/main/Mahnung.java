package main;

import java.io.File;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.JAXBIntrospector;
import jakarta.xml.bind.Unmarshaller;
import rechnungen.AnschriftType;
import rechnungen.RechnungType;
import rechnungen.Rechnungen;

public class Mahnung {
  public static void main(String[] args) throws JAXBException {
    File rechnungenXml = new File("rechnungen.xml");
    for (int i = 0; i <= 3; i++) {
      RechnungType rechnung = getRechnung(rechnungenXml, i);
      print(rechnung);
    }
  }

  public static void print(RechnungType rechnung) {
    String anrede = rechnung.getLeistungsempfaenger().getAnrede();
    String name1 = rechnung.getLeistungsempfaenger().getName();
    double nettoBetrag = rechnung.getEntgelt().getNetto();
    String waehrung = rechnung.getEntgelt().getWaehrung();
    String zahlungsart1 = getZahlungsart(rechnung);

    AnschriftType erbringerAnschrift = rechnung.getLeistungserbringer().getAnschrift();
    String telefon = null;
    if (erbringerAnschrift != null) {
      telefon = erbringerAnschrift.getTelefon();
    }

    String name2 = rechnung.getLeistungserbringer().getName();

    String nachricht = String.format(
        "%s %s\nBitte zahlen sie endlich den geforderten Betrag\nvon %.2f %s auf das Konto %s ein.\n",
        anrede,
        name1,
        nettoBetrag,
        waehrung,
        zahlungsart1);

    if (telefon != null && !telefon.isEmpty()) {
      nachricht += String.format(
          "Falls sie trotzdem noch unverschaemt genug sind\nund Fragen haben, dann koennen Sie mich jederzeit\nunter %s erreichen.\n",
          telefon);
    }

    nachricht += String.format("Hochachtungsvoll\n%s", name2);

    System.out.println("----------------------------------------------------");
    System.out.println(nachricht);
  }

  protected static String getZahlungsart(RechnungType rechnung) {
    String ret = "";
    String institut = rechnung.getZahlungsmoeglichkeiten().getInstitut();
    String bic = rechnung.getZahlungsmoeglichkeiten().getBIC();
    String kontonr = rechnung.getZahlungsmoeglichkeiten().getKontonr();
    if (institut != null && !institut.isEmpty())
      ret += institut + ", ";
    if (bic != null && !bic.isEmpty())
      ret += bic + ", ";
    ret += kontonr;
    return ret;
  }

  public static RechnungType getRechnung(File rechnungenXml, int index) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(Rechnungen.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    Rechnungen value = (Rechnungen) JAXBIntrospector.getValue(unmarshaller.unmarshal(rechnungenXml));
    RechnungType rechnung = value.getRechnung().get(index);
    return rechnung;
  }
}
