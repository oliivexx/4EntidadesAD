
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ProfesorGestion extends DefaultHandler{
    String fichero = "";
    Profesor p;
    private ArrayList<Profesor> arrayProfesorAux;
    
    public ProfesorGestion(ArrayList<Profesor> arrayProfesor) {
        super();
        this.arrayProfesorAux = arrayProfesor;
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
        
        if(fichero.equals("Profesor")){
            p = new Profesor();
        }
    } 	
    
    @Override
    public void endElement(String uri, String nombre, String nombreC) {
	 //System.out.printf("\tFin Elemento: %s %n", nombre);
         fichero = nombre;
         if (fichero.equals("Profesor")){
             arrayProfesorAux.add(p);
         }
    }	
    
    @Override
    public void characters(char[] ch, int inicio, int longitud){
        String car=new String(ch, inicio, longitud);
         //q	   cuitar saltos de línea	
        car = car.replaceAll("[\t\n]","");	   
        //System.out.printf ("\t Caracteres: %s %n", car);
        
        
        if(fichero.equals("codProfe")){
            p.setCodProfe(Integer.parseInt(car));
        }
        
        if(fichero.equals("nombre")){
           p.setNombre(String.valueOf(car));
        }
    
        if(fichero.equals("apellido")){
           p.setApellido(String.valueOf(car));
        }
        
        if(fichero.equals("dni")){
            p.setDni(String.valueOf(car));
        }
        
        if(fichero.equals("clase")){
            p.setClase(Integer.parseInt(car));
        }
        
        
        
        
    }
    
    
}//fin GestionContenido
    
    

