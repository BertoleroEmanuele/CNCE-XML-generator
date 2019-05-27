package Iubar.CNCE;

import java.io.*;
import java.util.Properties;
import java.util.logging.*;
import org.jdom2.*;
import org.jdom2.output.*;

public class FlussoOutput {
	private static final Logger LOGGER = Logger.getLogger(FlussoOutput.class.getName());

	public static Element generateCNCE_DatiCantiere() {
		Namespace ns_CNCE_DatiCantiere = Namespace.getNamespace("CNCE_DatiCantiere",
				"http://mut.cnce.it/schemas/denunce/daticantiere");
		Element elem_CNCE_DatiCantiere = new Element("CNCE_DatiCantiere", ns_CNCE_DatiCantiere);

		elem_CNCE_DatiCantiere.addContent(FlussoInput.generateIndirizzoCantiere());
		elem_CNCE_DatiCantiere.addContent(FlussoInput.generateDatiOpera());
		//test
		
		return elem_CNCE_DatiCantiere;
	}

	static void updateNamespace(Element e) {
		Namespace ns = e.getNamespace();
		for (Element child : e.getChildren()) {
			if (child.getNamespace().getPrefix().equals("")) {
				child.setNamespace(ns);
			}
			updateNamespace(child);
		}
	}

	public static void main(String[] args) {

		String outPath = readConfig();
		
		try {

			Namespace ns_FlussoOutput = Namespace.getNamespace("CNCE_FlussoOutput",
					"http://mut.cnce.it/schemas/denunce/flussooutput");
			Element elem_CNCE_FlussoOutput = new Element("CNCE_FlussoOutput", ns_FlussoOutput);
			Document doc = new Document(elem_CNCE_FlussoOutput);

			Element elem_DataCreazione = new Element("DataCreazione");
			elem_CNCE_FlussoOutput.addContent(elem_DataCreazione);
			Element elem_VersioneFlusso = new Element("VersioneFlusso");
			elem_CNCE_FlussoOutput.addContent(elem_VersioneFlusso);
			Element elem_TipoFlusso = new Element("TipoFlusso");
			elem_CNCE_FlussoOutput.addContent(elem_TipoFlusso);

			elem_CNCE_FlussoOutput.addContent(generateCNCE_DatiCantiere());

			updateNamespace(doc.getRootElement());

			XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());

			// display nice nice

			xmlOutput.output(doc, new FileWriter(outPath + "/FlussoInput.xml"));

			LOGGER.info(xmlOutput.outputString(doc));
			LOGGER.info("File Saved!");

		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
	}

	public static String readConfig() {
		String outPath = null;
		try (InputStream input = new FileInputStream("config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			outPath = prop.getProperty("out.path");
			System.out.println(outPath);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outPath;
	}

}
