import java.io.*;
import java.util.LinkedList;
import java.util.regex.Pattern;

class Main {

	// http://chuwiki.chuidiang.org/index.php?title=Leer_y_escribir_ficheros_de_texto_con_java_8

	/**
	 * Busca todos los ficheros que cumplen la máscara que se le pasa y los mete
	 * en la listaFicheros que se le pasa.
	 * 
	 * @param pathInicial
	 *            Path inicial de búsqueda. Debe ser un directorio que exista y
	 *            con permisos de lectura.
	 * @param mascara
	 *            Una máscara válida para la clase Pattern de java.
	 * @param listaFicheros
	 *            Una lista de ficheros a la que se añadirán los File que
	 *            cumplan la máscara. No puede ser null. El método no la vacía.
	 * @param busquedaRecursiva
	 *            Si la búsqueda debe ser recursiva en todos los subdirectorios
	 *            por debajo del pathInicial.
	 */
	public static void dameFicheros(String pathInicial, String mascara, LinkedList<File> listaFicheros,
			boolean busquedaRecursiva) {
		File directorioInicial = new File(pathInicial);
		if (directorioInicial.isDirectory()) {
			File[] ficheros = directorioInicial.listFiles();
			for (int i = 0; i < ficheros.length; i++) {
				if (ficheros[i].isDirectory() && busquedaRecursiva)
					dameFicheros(ficheros[i].getAbsolutePath(), mascara, listaFicheros, busquedaRecursiva);
				else if (Pattern.matches(mascara, ficheros[i].getName()))
					listaFicheros.add(ficheros[i]);
			}
		}
	}

	/**
	 * Se le pasa una máscara de fichero típica de ficheros con * e ? y devuelve
	 * la cadena regex que entiende la clase Pattern.
	 * 
	 * @param mascara
	 *            Un String que no sea null.
	 * @return Una máscara regex válida para Pattern.
	 */
	public static String dameRegex(String mascara) {
		mascara = mascara.replace(".", "\\.");
		mascara = mascara.replace("*", ".*");
		mascara = mascara.replace("?", ".");
		return mascara;
	}

	private static void tratarFichero(File archivo, String cad_buscada,int lineaDeseada) {

		

		FileReader fr = null;
		BufferedReader br = null;

		try {

			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			int nlinea = 1;
			String textoBueno = null;
			while ((linea = br.readLine()) != null) {
				
				if (nlinea==lineaDeseada)
				{
					 textoBueno=linea;
					
				}

				if (linea.contains(cad_buscada))

				{

					if ((linea = br.readLine()) != null) {
						nlinea++;
						// System.out.println(nlinea + ":" +
						// linea+":"+archivo.getAbsolutePath());
						
					if (lineaDeseada<nlinea) linea=textoBueno;

						int ini = linea.indexOf(">");
						int fin = linea.indexOf("<", ini + 1);
						String msg = linea.substring(ini + 1, fin);
						System.out.println(new String(msg.getBytes(), "UTF-8"));
					}

				}
				nlinea++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static void main(String[] arg) {

		String cad_buscada = "<AttributeValue>N</AttributeValue>";

		cad_buscada="&lt;AttributeValue&gt;Y&lt;/AttributeValue&gt;";
		
		//cad_buscada = "258973COMP";
		String rutaInicial = "C:\\Users\\xe66670\\Desktop\\Junio 2020\\Ats Nuevos y _locks PROD KLYO\\CDUF prod_15_16_junio";

		String expReg = "*.*";
		boolean recursivo = true;

		LinkedList<File> ficherosJava = new LinkedList<File>();

		dameFicheros(rutaInicial, dameRegex(expReg), ficherosJava, recursivo);
		for (int i = 0; i < ficherosJava.size(); i++) {
			// System.out.println(ficherosJava.get(i).getAbsolutePath());
			tratarFichero(ficherosJava.get(i), cad_buscada,99); //99

		}

		System.out.println("\n\nFIN");
	}

}