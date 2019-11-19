
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class InstitutoGestion extends DefaultHandler{
    String fichero = "";
    Instituto i;
    private ArrayList<Instituto> arrayInstiAux;
    
    public InstitutoGestion(ArrayList<Instituto> arrayInstituto) {
        super();
        this.arrayInstiAux = arrayInstituto;
    }
    

    
    @Override
    public void startDocument() {
	        //System.out.println("Comienzo del Documento XML");
    }	    
    
    @Override
    public void endDocument() {
	        //System.out.println("Final del Documento XML");
    }	 
    
    @Override
    public void startElement(String uri, String nombre, String nombreC, Attributes atts) {
	//System.out.printf("\t Principio Elemento: %s %n",nombre);
        fichero = nombre;
        
        if(fichero.equals("Instituto")){
            i = new Instituto();
        }
    } 	
    
    @Override
    public void endElement(String uri, String nombre, String nombreC) {
	// System.out.printf("\tFin Elemento: %s %n", nombre);
         fichero = nombre;
         if (fichero.equals("Instituto")){
             arrayInstiAux.add(i);
         }
    }	
    
    @Override
    public void characters(char[] ch, int inicio, int longitud){
        String car=new String(ch, inicio, longitud);
         //q	   cuitar saltos de línea	
        car = car.replaceAll("[\t\n]","");	   
        //System.out.printf ("\t Caracteres: %s %n", car);
        
        
        if(fichero.equals("id")){
            i.setCodInsti(Integer.parseInt(car));
        }
        
        if(fichero.equals("nombre")){
           i.setNombre(String.valueOf(car));
        }
    
        if(fichero.equals("provincia")){
           i.setProvincia(String.valueOf(car));
        }
        
        if(fichero.equals("calle")){
            i.setCalle(String.valueOf(car));
        }   
    }
    
    
}//fin GestionContenido
    
    

