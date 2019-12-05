
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ClaseGestion extends DefaultHandler{
    
    String fichero = "";
    Clase c;
    private ArrayList<Clase> arrayClaseAux;
    
    public ClaseGestion(ArrayList<Clase> arrayClase) {
        super();
        this.arrayClaseAux = arrayClase;
    }
    
    
    @Override
    public void startDocument() {
	       // System.out.println("Comienzo del Documento XML");
    }	    
    
    @Override
    public void endDocument() {
	        //System.out.println("Final del Documento XML");
    }	 
    
    @Override
    public void startElement(String uri, String nombre, String nombreC, Attributes atts) {
	//System.out.printf("\t Principio Elemento: %s %n",nombre);
        fichero = nombre;
        
        if(fichero.equals("Clase")){
            c = new Clase();
        }
    } 	
    
    @Override
    public void endElement(String uri, String nombre, String nombreC) {
	 //System.out.printf("\tFin Elemento: %s %n", nombre);
         fichero = nombre;
         if (fichero.equals("Clase")){
             arrayClaseAux.add(c);
         }
    }	
    
    @Override
    public void characters(char[] ch, int inicio, int longitud){
        String car=new String(ch, inicio, longitud);
         //q	   cuitar saltos de línea	
        car = car.replaceAll("[\t\n]","");	   
        //System.out.printf ("\t Caracteres: %s %n", car);
        
        
        if(fichero.equals("codClase")){
          c.setCod_clase(Integer.parseInt(car));
        }
        
        if(fichero.equals("numAlumnos")){
            c.setNumeroAlumnos(Integer.parseInt(car));
        }
    
        if(fichero.equals("asignatura")){
           c.setAsignatura(String.valueOf(car));
        }
        
        if(fichero.equals("Instituto")){
            c.setInstituto(Integer.parseInt(car));
        }
        
    }
    
    
}//fin GestionContenido
    
    


