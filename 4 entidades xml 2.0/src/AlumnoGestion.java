
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class AlumnoGestion extends DefaultHandler{
    
    String fichero = "";
    Alumno a;
    private ArrayList<Alumno> arrayAlumnoAux;
    
    public AlumnoGestion(ArrayList<Alumno> arrayAlumno) {
        super();
        this.arrayAlumnoAux = arrayAlumno;
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
        
        if(fichero.equals("Alumno")){
            a = new Alumno();
        }
    } 	
    
    @Override
    public void endElement(String uri, String nombre, String nombreC) {
	 //System.out.printf("\tFin Elemento: %s %n", nombre);
         fichero = nombre;
         if (fichero.equals("Alumno")){
             arrayAlumnoAux.add(a);
         }
    }	
    
    @Override
    public void characters(char[] ch, int inicio, int longitud){
        String car=new String(ch, inicio, longitud);
         //q	   cuitar saltos de línea	
        car = car.replaceAll("[\t\n]","");	   
        //System.out.printf ("\t Caracteres: %s %n", car);
        
        
        if(fichero.equals("codAlumn")){
          a.setCodAlumn(Integer.parseInt(car));
        }
        
        if(fichero.equals("nombre")){
            a.setNombre(String.valueOf(car));
        }
    
        if(fichero.equals("apellido")){
           a.setApellido(String.valueOf(car));
        }
        
        if(fichero.equals("dni")){
            a.setDni(String.valueOf(car));
        }
        
        if(fichero.equals("profesor")){
            a.setProfesor(Integer.parseInt(car));
        }
        
    }
    
    
}//fin GestionContenido