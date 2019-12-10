import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public final class Interfaz extends javax.swing.JFrame {
    
    private ObjectContainer db = null;

    int opcion = 5;
    int posicion;
    int codIniciador=0;
    
    //tablas
    DefaultTableModel tablaInsti, tablaClase, tablaProfe, tablaAlumno;
    
    //arrays
    ArrayList<Instituto> arrayInsti;
    ArrayList<Clase> arrayClase;
    ArrayList <Profesor> arrayProfesor;
    ArrayList <Alumno> arrayAlumno;
    
    //Variables Instituto    
    int intiCodInsti;
    String striNombre, striProvincia, striCalle;
    DefaultComboBoxModel dm;
    
    //Variables clase
    String strcAsignatura;
    int strcNumeroAlumnos;
    int intcCodClase;
    int intcInstituto;
    
    //Variables profesor
    int intpCodProfesor;
    String strpNombre;
    String strpApellido;
    String strpDni;
    int intpClase;
    
    //Variables alumno
    int intaCod;
    String straNombre;
    String straApellido;
    String straDni;
    int straProfesor;

      
   //variables locales
     int ultimaPosicion, ultimoID;

    public Interfaz() throws SAXException, IOException {
        
            conectarBD();
        
            //dm = new DefaultComboBoxModel(arrayInsti.toArray());
            initComponents();
            tablaInsti = new DefaultTableModel();
            tablaClase = new DefaultTableModel();
            tablaProfe = new DefaultTableModel();
            tablaAlumno = new DefaultTableModel();
            
            instiTabla.setModel(tablaInsti);
            claseTabla.setModel(tablaClase);
            profeTabla.setModel(tablaProfe);
            alumnoTabla.setModel(tablaAlumno);
            
            arrayInsti = new ArrayList();
            arrayClase = new ArrayList();
            arrayProfesor = new ArrayList();
            arrayAlumno= new ArrayList();

            
            desactivarCamposInsti();
            desactivarCamposClase();
            desactivarCamposProfe();
            desactivarCamposAlumno();

            //llenamos los arrays de la BD
            llenarArrayInsti();
            llenarArrayClase();
            llenarArrayProfe();
            llenarArrayAlumno();
            
            
            
            //inicializamos array y lo metemos en la tabla
            //llenarTablaInsti();
            //llenarTablaClase();
            //llenarTablaProfe();
            //llenarTablaAlumno();
            
            
            
            
            actualizarTablaInsti();
            actualizarTablaClase();
            actualizarTablaProfe();
            actualizarTablaAlumno();
            
            llenarComboBoxes();
            
           
            leerArrayInsti();
            leerArrayClase();
            leerArrayAlumnos();
            leerArrayProfe();
            
            arrayClase2BD();
            arrayProfe2BD();
            arrayAlumno2BD();
           
            
            
 
    }
    
    
    public void llenarComboBoxes(){

          //vaciar
        
        int itemCount = claseInstituto.getItemCount();
        for(int i=0;i<itemCount;i++){
            claseInstituto.removeItemAt(0);
         }
        
        itemCount = profesorClase.getItemCount();
        for(int i=0;i<itemCount;i++){
            profesorClase.removeItemAt(0);
         }
        
        itemCount = alumnoProfesor.getItemCount();
        for(int i=0;i<itemCount;i++){
            alumnoProfesor.removeItemAt(0);
         }
        
        
        //llenar
        for (int i = 0; i < arrayInsti.size(); ++i){
            claseInstituto.addItem(arrayInsti.get(i).getNombre());
        }
        
        for (int i = 0; i < arrayClase.size(); ++i){
            profesorClase.addItem(arrayClase.get(i).getAsignatura());
        }
        
        for (int i = 0; i < arrayProfesor.size(); ++i){
            alumnoProfesor.addItem(arrayProfesor.get(i).getNombre());
        }
        
        
    }
    
    /*----------------------METODOS BD--------------------------------------- */
   public void conectarBD(){
        db = Db4oEmbedded.openFile("basedatos");
    }

    public void desconectarBD(){
        db.close();
    
    }
  
    
    
      
        
    /*----------------------iNSTITUTO--------------------------------------- */
    
    public void llenarTablaInsti(){
        Instituto i1 = new Instituto(codIniciador, "CristoRey", "Granada", "Sangregorioalto");
        codIniciador++;
        Instituto i2 = new Instituto(codIniciador, "ZaidinVergeles", "Granada", "Primavera");
        codIniciador++;
        Instituto i3 = new Instituto(codIniciador, "VicenteEspinel", "Malaga", "Gaona7");
        codIniciador++;
        Instituto i4 = new Instituto(codIniciador, "BenGabriol", "Malaga", "AgustinMartin");
        
        arrayInsti.add(i1);
        arrayInsti.add(i2);
        arrayInsti.add(i3);
        arrayInsti.add(i4);
        
        tablaInsti.addColumn("COD");
        tablaInsti.addColumn("Nombre");
        tablaInsti.addColumn("Provincia");
        tablaInsti.addColumn("Calle");
        
        for(int i = 0; i < arrayInsti.size(); ++i){
        Instituto iaux = new Instituto(arrayInsti.get(i).getCodInsti(), arrayInsti.get(i).getNombre(), arrayInsti.get(i).getProvincia(), arrayInsti.get(i).getCalle());
        db.store(iaux);
        }
        
    }
    
    public void llenarArrayInsti(){
        Query q = db.query();
        q.constrain(Instituto.class);
        ObjectSet result = q.execute();
        
        
        
        while(result.hasNext()) {
            Instituto iaux = new Instituto();
            iaux = (Instituto)result.next();
            arrayInsti.add(iaux);
        }
    }
    
    
    public void añadirInsti(){
    //añadir
        striNombre = instiNombre.getText();
        striProvincia = instiProvincia.getText();
        striCalle= instiCalle.getText();
        
        
        ultimaPosicion = arrayInsti.size() -1;
        ultimoID = arrayInsti.get(ultimaPosicion).getCodInsti() +1;
        Instituto iaux = new Instituto(ultimaPosicion, striNombre,  striProvincia, striCalle);
        arrayInsti.add(iaux);
        db.store(iaux);
        
    }
    
    public void eliminarInsti(){
        striNombre = instiNombre.getText();
        striProvincia = instiProvincia.getText();
        striCalle= instiCalle.getText();
        
        Instituto iaux = new Instituto();
        iaux.setNombre(striNombre);
        iaux.setProvincia(striProvincia);
        iaux.setCalle(striCalle);
        
        ObjectSet result = db.queryByExample(iaux);
        Instituto iencontrado = (Instituto) result.next();
        db.delete(iencontrado);
        arrayInsti.remove(posicion);
        System.out.println("Has borrado" +iencontrado.toString());
        
        
    }
    
    public void editarInsti(String nombre, String provincia, String calle){
        
        
        
        ultimaPosicion = arrayInsti.size() -1;
        ultimoID = arrayInsti.get(ultimaPosicion).getCodInsti() +1;
        Instituto iaux = new Instituto(ultimoID, nombre, provincia, calle);
        
        ObjectSet result = db.queryByExample(iaux);
        Instituto iencontrado = new Instituto();
        iencontrado = (Instituto) result.next();
        System.out.println(iencontrado.toString());
        db.delete(iencontrado);
        arrayInsti.remove(posicion);

        db.store(iaux); /*******************preguntar*******************/
    }

    
    
    
    public void actualizarTablaInsti(){
        int tamano = tablaInsti.getRowCount();
            for (int i=0; i < tamano; i++){
                tablaInsti.removeRow(0);
            }    
        
        String matriz [][] = new String[arrayInsti.size()][4];
        for(int i = 0; i < arrayInsti.size(); ++i){
            matriz[i][0] = Integer.toString(arrayInsti.get(i).getCodInsti());
            matriz[i][1] = arrayInsti.get(i).getNombre();
            matriz[i][2] = arrayInsti.get(i).getProvincia();
            matriz[i][3] = arrayInsti.get(i).getCalle();
        }
        
        instiTabla.setModel(new javax.swing.table.DefaultTableModel(matriz, new String[]{"codInsti", "nombre", "provincia", "calle"}));
        
        
    } 
 
    public void vaciarCamposInsti(){
        instiNombre.setText("");
        instiProvincia.setText("");
        instiCalle.setText("");

    }
    public void activarCamposInsti(){
        instiNombre.setEditable(true);
        instiProvincia.setEditable(true);
        instiCalle.setEditable(true);
        
        claseInstituto.removeAll(); 
        
    }
    public void desactivarCamposInsti(){
        instiNombre.setEditable(false);
        instiProvincia.setEditable(false);
        instiCalle.setEditable(false);
    }
    
    public void desactivarBotonesInsti(){
    instiAñadir.setEnabled(false);
    instiModificar.setEnabled(false);
    instiBorrar.setEnabled(false);
    }
    
    public void activarBotonesInsti(){    
    instiAñadir.setEnabled(true);
    instiModificar.setEnabled(true);
    instiBorrar.setEnabled(true);
    }
    
    
    public void leerArrayInsti(){
        for(int i = 0; i < arrayInsti.size(); ++i){
            System.out.println("********************************************");
            System.out.println(arrayInsti.get(i).toString());
            System.out.println("********************************************");
        }
    }
    

    
    /*----------------------CLASE--------------------------------------- */
    
    
    public void llenarTablaClase(){
        codIniciador = 0;
        Clase c1 = new Clase(codIniciador, 20, "EIE", arrayInsti.get(1).getCodInsti());
        codIniciador++;
        Clase c2 = new Clase(codIniciador, 13, "FOL", arrayInsti.get(0).getCodInsti());
        codIniciador++;
        Clase c3 = new Clase(codIniciador, 18, "PSP", arrayInsti.get(2).getCodInsti());
        codIniciador++;
        Clase c4 = new Clase(codIniciador, 16, "DEINT", arrayInsti.get(2).getCodInsti());
        
        arrayClase.add(c1);
        arrayClase.add(c2);
        arrayClase.add(c3);
        arrayClase.add(c4);
        
        tablaClase.addColumn("COD");
        tablaClase.addColumn("Nº Alumnos");
        tablaClase.addColumn("Asignaturas");
        tablaClase.addColumn("Instituto");
        
    }
    
    public void llenarArrayClase(){
        Query q = db.query();
        q.constrain(Clase.class);
        ObjectSet result = q.execute();
        
        System.out.println(result.size());
        while(result.hasNext()) {
            Clase caux = new Clase();
            caux = (Clase)result.next();
            arrayClase.add(caux);
        }
    }
    
    public void añadirClase(){
    //añadir
        strcNumeroAlumnos = Integer.valueOf(claseAlumnos.getText());
        strcAsignatura = claseAsignaturas.getText();
        intcInstituto= claseInstituto.getSelectedIndex();
        
        
        ultimaPosicion = arrayInsti.size() -1;
        ultimoID = arrayClase.get(ultimaPosicion).getCod_clase() +1;
        Clase caux= new Clase(ultimaPosicion, strcNumeroAlumnos,  strcAsignatura, intcInstituto);
        arrayClase.add(caux);
        db.store(caux);
        
    }
    
    public void eliminarClase(){
        strcNumeroAlumnos = Integer.valueOf(claseAlumnos.getText());
        strcAsignatura = claseAsignaturas.getText();
        intcInstituto= claseInstituto.getSelectedIndex();
        
        Clase caux = new Clase();
        caux.setNumeroAlumnos(strcNumeroAlumnos);
        caux.setAsignatura(strcAsignatura);
        caux.setInstituto(intcInstituto);
        
        ObjectSet result = db.queryByExample(caux);
        Clase cencontrado = (Clase) result.next();
        db.delete(cencontrado);
        arrayClase.remove(posicion);
        System.out.println("Has borrado" +cencontrado.toString());
        
        
    }
    
    public void arrayClase2BD(){
        for (int i = 0; i < arrayClase.size(); ++i){
            db.store(arrayClase.get(i));
        }
    }
    
    
    
    
     public void actualizarTablaClase(){
        int tamano = tablaClase.getRowCount();
        
            for (int i=0; i < tamano; i++){
                tablaClase.removeRow(0);
            }    
            
        
        String matriz [][] = new String[arrayClase.size()][4];
        for(int i = 0; i < arrayClase.size(); ++i){
            
            matriz[i][0] = Integer.toString(arrayClase.get(i).getCod_clase());
            matriz[i][1] = Integer.toString(arrayClase.get(i).getNumeroAlumnos());
            matriz[i][2] = arrayClase.get(i).getAsignatura();
            int codInstiAux = arrayClase.get(i).getInstituto();
            matriz[i][3] = arrayInsti.get(codInstiAux).getNombre();
            
            
            
        }
        
        claseTabla.setModel(new javax.swing.table.DefaultTableModel(matriz, new String[]{"codClase", "numeroAlumnos", "asignatura", "instituto"}));
        
        
    } 
    
    public void vaciarCamposClase(){
        claseAlumnos.setText("");
        claseAsignaturas.setText("");
    }
    public void activarCamposClase(){
        claseAlumnos.setEditable(true);
        claseAsignaturas.setEditable(true);  
    }
    public void desactivarCamposClase(){
       claseAlumnos.setEditable(false);
       claseAsignaturas.setEditable(false);  
    }
    
    public void desactivarBotonesClase(){
        claseAñadir.setEnabled(false);
        claseModificar.setEnabled(false);
        claseBorrar.setEnabled(false);
    }
    
    public void activarBotonesClase(){    
        claseAñadir.setEnabled(true);
        claseModificar.setEnabled(true);
        claseBorrar.setEnabled(true);
    }
    

    
    public void leerArrayClase(){
        for(int i = 0; i < arrayClase.size(); ++i){
            System.out.println("********************************************");
            System.out.println(arrayClase.get(i).toString());
            System.out.println("********************************************");
        }
    }
    
 
    
    
    /*----------------------PROFESOR--------------------------------------- */
    
    public void llenarTablaProfe(){
        codIniciador = 0;
        Profesor p1 = new Profesor (codIniciador, "Alejandro", "Perez", "82731829H", 0);
        codIniciador++;
        Profesor p2 = new Profesor (codIniciador, "Pedro", "Jimenez", "253453457K", 1);
        codIniciador++;
        Profesor p3 = new Profesor (codIniciador, "Luis", "Fernandez", "13523451A", 3);
        codIniciador++;
        Profesor p4 = new Profesor (codIniciador, "Marisa", "Lopez", "39225251J", 0);
        codIniciador++;
        Profesor p5 = new Profesor (codIniciador, "Marta", "Vilchez", "42345239H", 1);
        
        arrayProfesor.add(p1);
        arrayProfesor.add(p2);
        arrayProfesor.add(p3);
        arrayProfesor.add(p4);
        
        
        tablaProfe.addColumn("COD");
        tablaProfe.addColumn("Nombre");
        tablaProfe.addColumn("Apellido");
        tablaProfe.addColumn("DNI");
        tablaProfe.addColumn("Clase");
        
    }
    
    public void actualizarTablaProfe(){
        int tamano = tablaProfe.getRowCount();
            for (int i=0; i < tamano; i++){
                tablaProfe.removeRow(0);
            }    
        
        String matriz [][] = new String[arrayProfesor.size()][5];
        for(int i = 0; i < arrayProfesor.size(); ++i){
            
            matriz[i][0] = Integer.toString(arrayProfesor.get(i).getCodProfe());
            matriz[i][1] = arrayProfesor.get(i).getNombre();
            matriz[i][2] = arrayProfesor.get(i).getApellido();
            matriz[i][3] = arrayProfesor.get(i).getDni();
            //int profeClaseAux = arrayProfesor.get(i).getClase();
            matriz[i][4] = Integer.toString(arrayProfesor.get(i).getClase());
        }
        
        profeTabla.setModel(new javax.swing.table.DefaultTableModel(matriz, new String[]{"codProfe", "nombre", "apellidos", "dni", "clase"}));
        
        
    } 
    
    
    public void añadirProfe(){
    //añadir      
        strpNombre = profeNombre.getText();
        strpApellido = profeApellido.getText();
        strpDni= profeDni.getText();
        intpClase = profesorClase.getSelectedIndex();
        

        ultimaPosicion = arrayProfesor.size() -1;
        ultimoID = arrayProfesor.get(ultimaPosicion).getCodProfe() +1;
        Profesor paux= new Profesor(ultimaPosicion, strpNombre,  strpApellido, strpDni, intpClase);
        arrayProfesor.add(paux);
        db.store(paux);
        
    }
    
    public void eliminarProfe(){
        strpNombre = profeNombre.getText();
        strpApellido = profeApellido.getText();
        strpDni= profeDni.getText();
        intpClase = profesorClase.getSelectedIndex();
        
        Profesor paux = new Profesor();
        paux.setNombre(striNombre);
        paux.setApellido(strpApellido);
        paux.setDni(strpDni);
        
        
        ObjectSet result = db.queryByExample(paux);
        Profesor pencontrado = (Profesor) result.next();
        db.delete(pencontrado);
        arrayProfesor.remove(posicion);
        System.out.println("Has borrado" +pencontrado.toString());
    }
    
    
    
    
    public void llenarArrayProfe(){
        Query q = db.query();
        q.constrain(Profesor.class);
        ObjectSet result = q.execute();
        
        System.out.println(result.size());
        while(result.hasNext()) {
            Profesor paux = new Profesor();
            paux = (Profesor)result.next();
            arrayProfesor.add(paux);
        }
    }
    
    
    public void arrayProfe2BD(){
        for (int i = 0; i < arrayProfesor.size(); ++i){
            db.store(arrayProfesor.get(i));
        }
    }

    
    public void vaciarCamposProfe(){
        profeNombre.setText("");
        profeApellido.setText("");
        profeDni.setText("");
        

    }
    public void activarCamposProfe(){
        profeNombre.setEditable(true);
        profeApellido.setEditable(true);
        profeDni.setEditable(true);
        profesorClase.setEditable(true);
           
    }
    public void desactivarCamposProfe(){
        profeNombre.setEditable(false);
        profeApellido.setEditable(false);
        profeDni.setEditable(false);
        profesorClase.setEditable(false);
        
    }
    
    public void desactivarBotonesProfe(){
    profeAñadir.setEnabled(false);
    profeModificar.setEnabled(false);
    profeBorrar.setEnabled(false);
    }
    
    public void activarBotonesProfe(){    
    profeAñadir.setEnabled(true);
    profeModificar.setEnabled(true);
    profeBorrar.setEnabled(true);
    }
    
    public void leerArrayProfe(){
        for(int i = 0; i < arrayProfesor.size(); ++i){
            System.out.println("********************************************");
            System.out.println(arrayProfesor.get(i).toString());
        }
    }
    

    
     
    
    
    /*----------------------Alumno--------------------------------------- */
    
    public void llenarTablaAlumno(){
        codIniciador = 0;
        
        Alumno a1 = new Alumno (codIniciador, "Alejandro", "Nieto", "7239472347H", 1);
        codIniciador++;
        Alumno a2 = new Alumno (codIniciador, "Antonio", "Olivencia", "234589372G", 0);
        codIniciador++;
        Alumno a3 = new Alumno (codIniciador, "Rosa", "Martin", "23423347H", 2);
        codIniciador++;
        Alumno a4 = new Alumno (codIniciador, "Pepe", "Grillo", "2342342343D", 1);
        codIniciador++;
        Alumno a5 = new Alumno (codIniciador, "Andrea", "Lopez", "24234267724B", 2);
        
        
        arrayAlumno.add(a1);
        arrayAlumno.add(a2);
        arrayAlumno.add(a3);
        arrayAlumno.add(a4);
        
        
        tablaAlumno.addColumn("COD");
        tablaAlumno.addColumn("Nombre");
        tablaAlumno.addColumn("Apellido");
        tablaAlumno.addColumn("DNI");
        tablaAlumno.addColumn("PROFESOR");
    }
    
    public void actualizarTablaAlumno(){
        int tamano = tablaAlumno.getRowCount();
            for (int i=0; i < tamano; i++){
                tablaAlumno.removeRow(0);
            }    
        
        String matriz [][] = new String[arrayAlumno.size()][5];
        for(int i = 0; i < arrayAlumno.size(); ++i){
            
            matriz[i][0] = Integer.toString(arrayAlumno.get(i).getCodAlumn());
            matriz[i][1] = arrayAlumno.get(i).getNombre();
            matriz[i][2] = arrayAlumno.get(i).getApellido();
            matriz[i][3] = arrayAlumno.get(i).getDni();
            matriz[i][4] = Integer.toString(arrayAlumno.get(i).getProfesor());

            
        }
        
        alumnoTabla.setModel(new javax.swing.table.DefaultTableModel(matriz, new String[]{"codAlumno", "nombre", "apellidos", "dni", "profesor"}));
        
        
    }
    
     public void añadirAlumno(){
                
        straNombre = alumnoNombre.getText();
        straApellido = alumnoApellido.getText();
        straDni= alumnoDni.getText();
        straProfesor = alumnoProfesor.getSelectedIndex();
        

        ultimaPosicion = arrayAlumno.size() -1;
        ultimoID = arrayAlumno.get(ultimaPosicion).getCodAlumn() +1;
        Alumno aaux= new Alumno(ultimaPosicion, straNombre,  straApellido, straDni, straProfesor);
        arrayAlumno.add(aaux);
        db.store(aaux);
        
    }
     
     
     public void eliminarAlumno(){
        straNombre = alumnoNombre.getText();
        straApellido = alumnoApellido.getText();
        straDni= alumnoDni.getText();
        straProfesor = alumnoProfesor.getSelectedIndex();
        
        Alumno aaux = new Alumno();
        aaux.setNombre(striNombre);
        aaux.setApellido(strpApellido);
        aaux.setDni(strpDni);
        
        
        ObjectSet result = db.queryByExample(aaux);
        Alumno aencontrado = (Alumno) result.next();
        db.delete(aencontrado);
        arrayAlumno.remove(posicion);
        System.out.println("Has borrado" +aencontrado.toString());
    }
     
     public void llenarArrayAlumno(){
        Query q = db.query();
        q.constrain(Alumno.class);
        ObjectSet result = q.execute();
        
        System.out.println(result.size());
        while(result.hasNext()) {
            Alumno aaux = new Alumno();
            aaux = (Alumno)result.next();
            arrayAlumno.add(aaux);
        }
    }
    
    
    public void arrayAlumno2BD(){
        for (int i = 0; i < arrayAlumno.size(); ++i){
            db.store(arrayAlumno.get(i));
        }
    }

    
    public void vaciarCamposAlumno(){
        alumnoNombre.setText("");
        alumnoApellido.setText("");
        alumnoDni.setText("");
        

    }
    public void activarCamposAlumno(){
        alumnoNombre.setEditable(true);
        alumnoApellido.setEditable(true);
        alumnoDni.setEditable(true);
        profesorClase.setEditable(true);
           
    }
    public void desactivarCamposAlumno(){
        alumnoNombre.setEditable(false);
        alumnoApellido.setEditable(false);
        alumnoDni.setEditable(false);
        profesorClase.setEditable(false);
        
    }
    
    public void desactivarBotonesAlumno(){
    alumnoAñadir.setEnabled(false);
    alumnoModificar.setEnabled(false);
    alumnoBorrar.setEnabled(false);
    }
    
    public void activarBotonesAlumno(){    
    alumnoAñadir.setEnabled(true);
    alumnoModificar.setEnabled(true);
    alumnoBorrar.setEnabled(true);
    }
    
    
    public void leerArrayAlumnos(){
        for(int i = 0; i < arrayAlumno.size(); ++i){
            System.out.println("********************************************");
            System.out.println(arrayAlumno.get(i).toString());
            System.out.println("********************************************");
        }
        
    }
    

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        instiTabla = new javax.swing.JTable();
        instiAñadir = new javax.swing.JButton();
        instiBorrar = new javax.swing.JButton();
        instiModificar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        instiNombre = new javax.swing.JTextField();
        instiProvincia = new javax.swing.JTextField();
        instiCalle = new javax.swing.JTextField();
        instiGuardar = new javax.swing.JButton();
        instiCancelar = new javax.swing.JButton();
        instiSalir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        claseAlumnos = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        claseTabla = new javax.swing.JTable();
        claseAsignaturas = new javax.swing.JTextField();
        claseAñadir = new javax.swing.JButton();
        claseBorrar = new javax.swing.JButton();
        claseGuardar = new javax.swing.JButton();
        claseCancelar = new javax.swing.JButton();
        claseModificar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        claseInstituto = new javax.swing.JComboBox<>();
        claseSalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        profeNombre = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        profeTabla = new javax.swing.JTable();
        profeApellido = new javax.swing.JTextField();
        profeAñadir = new javax.swing.JButton();
        profeDni = new javax.swing.JTextField();
        profeBorrar = new javax.swing.JButton();
        profeGuardar = new javax.swing.JButton();
        profeCancelar = new javax.swing.JButton();
        profeModificar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        profesorClase = new javax.swing.JComboBox<>();
        profeSalir = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        alumnoAñadir = new javax.swing.JButton();
        alumnoDni = new javax.swing.JTextField();
        alumnoBorrar = new javax.swing.JButton();
        alumnoGuardar = new javax.swing.JButton();
        alumnoCancelar = new javax.swing.JButton();
        alumnoModificar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        alumnoNombre = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        alumnoTabla = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        alumnoApellido = new javax.swing.JTextField();
        alumnoProfesor = new javax.swing.JComboBox<>();
        claseSalir1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        instiTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "COD", "NOMBRE", "PROVINCIA", "CALLE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        instiTabla.getTableHeader().setReorderingAllowed(false);
        instiTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                instiTablaMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                instiTablaMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(instiTabla);

        instiAñadir.setText("Añadir");
        instiAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instiAñadirActionPerformed(evt);
            }
        });

        instiBorrar.setText("Borrar");
        instiBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instiBorrarActionPerformed(evt);
            }
        });

        instiModificar.setText("Modificar");
        instiModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instiModificarActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre: ");

        jLabel2.setText("Calle: ");

        jLabel3.setText("Provincia: ");

        instiNombre.setEditable(false);
        instiNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instiNombreActionPerformed(evt);
            }
        });

        instiProvincia.setEditable(false);

        instiCalle.setEditable(false);

        instiGuardar.setText("Guardar");
        instiGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instiGuardarActionPerformed(evt);
            }
        });

        instiCancelar.setText("Cancelar");
        instiCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instiCancelarActionPerformed(evt);
            }
        });

        instiSalir.setText("Salir");
        instiSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instiSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(instiAñadir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(instiBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(instiModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(116, 116, 116))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(instiNombre)
                            .addComponent(instiCalle, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                        .addGap(126, 126, 126)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(instiProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(instiSalir)
                .addGap(28, 28, 28)
                .addComponent(instiCancelar)
                .addGap(56, 56, 56)
                .addComponent(instiGuardar)
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(instiAñadir)
                        .addGap(52, 52, 52)
                        .addComponent(instiBorrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(instiModificar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(instiNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instiProvincia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(instiCalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(instiGuardar)
                    .addComponent(instiCancelar)
                    .addComponent(instiSalir))
                .addGap(51, 51, 51))
        );

        jTabbedPane1.addTab("Instituto", jPanel1);

        claseAlumnos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseAlumnosActionPerformed(evt);
            }
        });

        claseTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "COD", "Nº ALUMNOS", "ASIGNATURAS", "Instituto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        claseTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                claseTablaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(claseTabla);

        claseAñadir.setText("Añadir");
        claseAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseAñadirActionPerformed(evt);
            }
        });

        claseBorrar.setText("Borrar");
        claseBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseBorrarActionPerformed(evt);
            }
        });

        claseGuardar.setText("Guardar");
        claseGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseGuardarActionPerformed(evt);
            }
        });

        claseCancelar.setText("Cancelar");
        claseCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseCancelarActionPerformed(evt);
            }
        });

        claseModificar.setText("Modificar");
        claseModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseModificarActionPerformed(evt);
            }
        });

        jLabel4.setText("Numero de alumnos: ");

        jLabel6.setText("Asignatura: ");

        claseInstituto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseInstitutoActionPerformed(evt);
            }
        });

        claseSalir.setText("Salir");
        claseSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(claseAñadir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(claseBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(claseModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(116, 116, 116))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(claseAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(131, 131, 131)
                        .addComponent(jLabel6)
                        .addGap(36, 36, 36)
                        .addComponent(claseAsignaturas, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(claseInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(claseSalir)
                .addGap(26, 26, 26)
                .addComponent(claseCancelar)
                .addGap(56, 56, 56)
                .addComponent(claseGuardar)
                .addGap(36, 36, 36))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(claseAñadir)
                        .addGap(52, 52, 52)
                        .addComponent(claseBorrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(claseModificar))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(105, 105, 105)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(claseAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(claseAsignaturas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addComponent(claseInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(claseCancelar)
                    .addComponent(claseGuardar)
                    .addComponent(claseSalir))
                .addGap(51, 51, 51))
        );

        jTabbedPane1.addTab("Clase", jPanel2);

        profeTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "COD", "NOMBRE", "APELLIDO", "DNI", "CLASE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        profeTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                profeTablaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(profeTabla);

        profeAñadir.setText("Añadir");
        profeAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profeAñadirActionPerformed(evt);
            }
        });

        profeBorrar.setText("Borrar");
        profeBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profeBorrarActionPerformed(evt);
            }
        });

        profeGuardar.setText("Guardar");
        profeGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profeGuardarActionPerformed(evt);
            }
        });

        profeCancelar.setText("Cancelar");
        profeCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profeCancelarActionPerformed(evt);
            }
        });

        profeModificar.setText("Modificar");
        profeModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profeModificarActionPerformed(evt);
            }
        });

        jLabel7.setText("Nombre: ");

        jLabel8.setText("Dni:");

        jLabel9.setText("Apellido: ");

        profesorClase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profesorClaseActionPerformed(evt);
            }
        });

        profeSalir.setText("Salir");
        profeSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profeSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profeAñadir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profeBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profeModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(116, 116, 116))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(profeNombre)
                            .addComponent(profeDni, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                        .addGap(131, 131, 131)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profeApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profesorClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(profeSalir)
                .addGap(26, 26, 26)
                .addComponent(profeCancelar)
                .addGap(56, 56, 56)
                .addComponent(profeGuardar)
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(profeAñadir)
                        .addGap(52, 52, 52)
                        .addComponent(profeBorrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(profeModificar))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(profeNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profeApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(profeDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profesorClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(profeGuardar)
                    .addComponent(profeCancelar)
                    .addComponent(profeSalir))
                .addGap(51, 51, 51))
        );

        jTabbedPane1.addTab("Profesor", jPanel3);

        alumnoAñadir.setText("Añadir");
        alumnoAñadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alumnoAñadirActionPerformed(evt);
            }
        });

        alumnoBorrar.setText("Borrar");
        alumnoBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alumnoBorrarActionPerformed(evt);
            }
        });

        alumnoGuardar.setText("Guardar");
        alumnoGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alumnoGuardarActionPerformed(evt);
            }
        });

        alumnoCancelar.setText("Cancelar");
        alumnoCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alumnoCancelarActionPerformed(evt);
            }
        });

        alumnoModificar.setText("Modificar");
        alumnoModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alumnoModificarActionPerformed(evt);
            }
        });

        jLabel10.setText("Nombre: ");

        jLabel11.setText("Dni:");

        alumnoTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "COD", "NOMBRE", "APELLIDO", "DNI", "PROFESOR"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        alumnoTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                alumnoTablaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(alumnoTabla);

        jLabel12.setText("Apellido: ");

        claseSalir1.setText("Salir");
        claseSalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claseSalir1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alumnoAñadir, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alumnoBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alumnoModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(116, 116, 116))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(alumnoNombre)
                            .addComponent(alumnoDni, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                        .addGap(131, 131, 131)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alumnoProfesor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alumnoApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(claseSalir1)
                .addGap(26, 26, 26)
                .addComponent(alumnoCancelar)
                .addGap(56, 56, 56)
                .addComponent(alumnoGuardar)
                .addGap(36, 36, 36))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(alumnoAñadir)
                        .addGap(52, 52, 52)
                        .addComponent(alumnoBorrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(alumnoModificar))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(alumnoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alumnoApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(alumnoDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alumnoProfesor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alumnoGuardar)
                    .addComponent(alumnoCancelar)
                    .addComponent(claseSalir1))
                .addGap(51, 51, 51))
        );

        jTabbedPane1.addTab("Alumno", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void instiGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instiGuardarActionPerformed
        switch(opcion){
        case 0:
            {
                añadirInsti();

            }
            
            opcion = 5;
            desactivarCamposInsti();
            actualizarTablaInsti(); 
            llenarComboBoxes();
            vaciarCamposInsti(); 
            activarBotonesInsti();


      
      break;



            
        case 1:
            //modificar
            
            striNombre = instiNombre.getText();
            striProvincia = instiProvincia.getText();
            striCalle= instiCalle.getText();
            editarInsti(striNombre, striProvincia, striCalle);
            
            ultimaPosicion = arrayInsti.size() -1;
            ultimoID = arrayInsti.get(ultimaPosicion).getCodInsti();
             Instituto i = new Instituto(ultimoID + 1, striNombre, striProvincia, striCalle); //el objeto esta creado en case 0.
            arrayInsti.set(posicion, i);
            actualizarTablaInsti();
            desactivarCamposInsti();
            vaciarCamposInsti();
            activarBotonesInsti();
            
            opcion = 5;


            break;
        }
    }//GEN-LAST:event_instiGuardarActionPerformed

    private void profeGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profeGuardarActionPerformed
        switch(opcion){
        case 0:
            

                    //añadir
                    añadirProfe();

                    
            
            opcion = 5;
            desactivarCamposProfe();
            
            actualizarTablaProfe();
            vaciarCamposProfe(); 
            activarBotonesProfe();
            break;

            
            

            
        case 1:
            //modificar
            
           strpNombre = profeNombre.getText();
           strpApellido = profeApellido.getText();
           strpDni = profeDni.getText();
          int x = profesorClase.getSelectedIndex();
            
             Profesor paux= new Profesor(arrayProfesor.size(), strpNombre, strpApellido, strpDni, x); //el bojeto esta creado en case 0.
             
            arrayProfesor.set(posicion, paux);
            desactivarCamposProfe();
            actualizarTablaProfe();
            vaciarCamposProfe(); 
            activarBotonesProfe();
            
            opcion = 5;


            break;
        }
    }//GEN-LAST:event_profeGuardarActionPerformed

    private void profeBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profeBorrarActionPerformed
        eliminarProfe();
        actualizarTablaProfe();
        vaciarCamposProfe();
        actualizarTablaProfe();
    }//GEN-LAST:event_profeBorrarActionPerformed
   
    private void alumnoBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alumnoBorrarActionPerformed
        opcion = 2;
        eliminarAlumno();
        actualizarTablaAlumno();
        vaciarCamposAlumno();
        actualizarTablaAlumno();
            
    }//GEN-LAST:event_alumnoBorrarActionPerformed

    private void alumnoGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alumnoGuardarActionPerformed
        switch(opcion){
        case 0:
            

            añadirAlumno();
            desactivarCamposAlumno();
            actualizarTablaAlumno();
            vaciarCamposAlumno(); 
            activarBotonesAlumno();
            opcion = 5;
            
            break;

            
            

            
        case 1:
            //modificar
            
           straNombre = alumnoNombre.getText();
            straApellido = alumnoApellido.getText();
            straDni = alumnoDni.getText();
            int x = alumnoProfesor.getSelectedIndex();
            Alumno aaux= new Alumno(arrayAlumno.size(), straNombre, straApellido, straDni, x);
             
            arrayAlumno.set(posicion, aaux);
            
            opcion = 5;
            desactivarCamposAlumno();
            actualizarTablaAlumno();
            vaciarCamposAlumno(); 
            activarBotonesAlumno();
            
            opcion = 5;


            break;
        }
    }//GEN-LAST:event_alumnoGuardarActionPerformed

    private void instiAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instiAñadirActionPerformed
        opcion = 0;
        vaciarCamposInsti();
        activarCamposInsti();
        desactivarBotonesInsti();
    }//GEN-LAST:event_instiAñadirActionPerformed

    private void instiBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instiBorrarActionPerformed
        opcion = 2;
        eliminarInsti();
        llenarComboBoxes();
        actualizarTablaInsti();
        vaciarCamposInsti();
        leerArrayInsti();
    }//GEN-LAST:event_instiBorrarActionPerformed

    private void instiModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instiModificarActionPerformed
       /* Boton Modificar */
       
       
        opcion = 1;
        
        activarCamposInsti();
        desactivarBotonesInsti();
    }//GEN-LAST:event_instiModificarActionPerformed

    private void instiNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instiNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_instiNombreActionPerformed

    private void instiTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_instiTablaMouseClicked
        //Mostrar que objeto has seleccionado en la tabla Instituto:
        
        posicion = instiTabla.getSelectedRow();
        instiNombre.setText(arrayInsti.get(posicion).getNombre());
        instiProvincia.setText(arrayInsti.get(posicion).getProvincia());
        instiCalle.setText(arrayInsti.get(posicion).getCalle());
    }//GEN-LAST:event_instiTablaMouseClicked

    private void instiCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instiCancelarActionPerformed
        vaciarCamposInsti();
        activarBotonesInsti();
        desactivarCamposInsti();
        
        posicion = instiTabla.getSelectedRow();
        instiNombre.setText(arrayInsti.get(posicion).getNombre());
        instiProvincia.setText(arrayInsti.get(posicion).getProvincia());
        instiCalle.setText(arrayInsti.get(posicion).getCalle());
    }//GEN-LAST:event_instiCancelarActionPerformed

    private void claseInstitutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseInstitutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_claseInstitutoActionPerformed

    private void claseAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseAñadirActionPerformed
        opcion = 0;
        vaciarCamposClase();
        activarCamposClase();
        desactivarBotonesClase();
    }//GEN-LAST:event_claseAñadirActionPerformed

    private void claseBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseBorrarActionPerformed
        opcion = 2;

        eliminarClase();
        actualizarTablaClase();
        actualizarTablaProfe();
        vaciarCamposClase();
        llenarComboBoxes();
    }//GEN-LAST:event_claseBorrarActionPerformed

    private void claseModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseModificarActionPerformed
       opcion = 1;
        activarCamposClase();
        desactivarBotonesClase();
        llenarComboBoxes();
    }//GEN-LAST:event_claseModificarActionPerformed

    private void claseGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseGuardarActionPerformed
        switch(opcion){
        case 0:
            

                    //añadir
                    añadirClase();

            
            opcion = 5;
            desactivarCamposClase();
            actualizarTablaClase();
            vaciarCamposClase(); 
            activarBotonesClase();
            break;

            
            

            
        case 1:
            //modificar
            
           strcAsignatura = claseAsignaturas.getText();
           int intcNumeroAlumnos = Integer.parseInt(claseAlumnos.getText());
           int codClase = arrayClase.get(posicion).getCod_clase();
           int codClaseInsti = arrayClase.get(posicion).getInstituto();
          int  x = claseInstituto.getSelectedIndex();
            
            Clase caux= new Clase(codClase, intcNumeroAlumnos, strcAsignatura, x); //el bojeto esta creado en case 0.
             
            arrayClase.set(posicion, caux);
            actualizarTablaClase();
            desactivarCamposClase();
            vaciarCamposClase();
            activarBotonesClase();
            
            opcion = 5;


            break;
        }
    }//GEN-LAST:event_claseGuardarActionPerformed

    private void claseTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_claseTablaMouseClicked
        //Mostrar que objeto has seleccionado en la tabla Instituto:
        
        posicion = claseTabla.getSelectedRow();
        claseAlumnos.setText(Integer.toString(arrayClase.get(posicion).getNumeroAlumnos()));
        claseAsignaturas.setText(arrayClase.get(posicion).getAsignatura());
        claseInstituto.setSelectedIndex(arrayClase.get(posicion).getInstituto());
    }//GEN-LAST:event_claseTablaMouseClicked

    private void claseCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseCancelarActionPerformed
        vaciarCamposClase();
        activarBotonesClase();
        desactivarCamposClase();
        
        posicion = claseTabla.getSelectedRow();
        claseAlumnos.setText(Integer.toString(arrayClase.get(posicion).getNumeroAlumnos()));
        claseAsignaturas.setText(arrayClase.get(posicion).getAsignatura());
    }//GEN-LAST:event_claseCancelarActionPerformed

    private void profeAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profeAñadirActionPerformed
        opcion = 0;
        vaciarCamposProfe();
        activarCamposProfe();
        desactivarBotonesProfe();
    }//GEN-LAST:event_profeAñadirActionPerformed

    private void profeModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profeModificarActionPerformed
        opcion = 1;
        activarCamposProfe();
        desactivarBotonesProfe();
    }//GEN-LAST:event_profeModificarActionPerformed

    private void profeCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profeCancelarActionPerformed
        vaciarCamposProfe();
        activarBotonesProfe();
        desactivarCamposProfe();
        
        posicion = profeTabla.getSelectedRow();
        profeApellido.setText(arrayProfesor.get(posicion).getApellido());
        profeNombre.setText(arrayProfesor.get(posicion).getNombre());
        profeDni.setText(arrayProfesor.get(posicion).getDni());
        profesorClase.setSelectedIndex(posicion);
    }//GEN-LAST:event_profeCancelarActionPerformed

    private void profeTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_profeTablaMouseClicked
        //Mostrar que objeto has seleccionado en la tabla Instituto:
        
        posicion = profeTabla.getSelectedRow();
        profeApellido.setText(arrayProfesor.get(posicion).getApellido());
        profeNombre.setText(arrayProfesor.get(posicion).getNombre());
        profeDni.setText(arrayProfesor.get(posicion).getDni());
        profesorClase.setSelectedIndex(arrayProfesor.get(posicion).getClase());
        
    }//GEN-LAST:event_profeTablaMouseClicked

    private void alumnoAñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alumnoAñadirActionPerformed
        opcion = 0;
        vaciarCamposAlumno();
        activarCamposAlumno();
        desactivarBotonesAlumno();
    }//GEN-LAST:event_alumnoAñadirActionPerformed

    private void alumnoModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alumnoModificarActionPerformed
        opcion = 1;
        activarCamposAlumno();
        desactivarBotonesAlumno();
    }//GEN-LAST:event_alumnoModificarActionPerformed

    private void alumnoCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alumnoCancelarActionPerformed
         vaciarCamposAlumno();
        activarBotonesAlumno();
        desactivarCamposAlumno();
        
        posicion = alumnoTabla.getSelectedRow();
        alumnoApellido.setText(arrayAlumno.get(posicion).getApellido());
        alumnoNombre.setText(arrayAlumno.get(posicion).getNombre());
        alumnoDni.setText(arrayAlumno.get(posicion).getDni());
        alumnoProfesor.setSelectedIndex(posicion);
    }//GEN-LAST:event_alumnoCancelarActionPerformed

    private void alumnoTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_alumnoTablaMouseClicked
        //Mostrar que objeto has seleccionado en la tabla Instituto:
        
        posicion = alumnoTabla.getSelectedRow();
        alumnoApellido.setText(arrayAlumno.get(posicion).getApellido());
        alumnoNombre.setText(arrayAlumno.get(posicion).getNombre());
        alumnoDni.setText(arrayAlumno.get(posicion).getDni());
        alumnoProfesor.setSelectedIndex(arrayAlumno.get(posicion).getProfesor());
    }//GEN-LAST:event_alumnoTablaMouseClicked

    private void profesorClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profesorClaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profesorClaseActionPerformed

    private void claseAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseAlumnosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_claseAlumnosActionPerformed

    private void profeSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profeSalirActionPerformed
        
        System.exit(0);
    }//GEN-LAST:event_profeSalirActionPerformed

    private void instiSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instiSalirActionPerformed
        
        System.exit(0);
    }//GEN-LAST:event_instiSalirActionPerformed

    private void claseSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseSalirActionPerformed
        
        System.exit(0);
    }//GEN-LAST:event_claseSalirActionPerformed

    private void claseSalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claseSalir1ActionPerformed
        
        System.exit(0);
    }//GEN-LAST:event_claseSalir1ActionPerformed

    private void instiTablaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_instiTablaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_instiTablaMouseEntered

  
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Interfaz().setVisible(true);
                } catch (SAXException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField alumnoApellido;
    private javax.swing.JButton alumnoAñadir;
    private javax.swing.JButton alumnoBorrar;
    private javax.swing.JButton alumnoCancelar;
    private javax.swing.JTextField alumnoDni;
    private javax.swing.JButton alumnoGuardar;
    private javax.swing.JButton alumnoModificar;
    private javax.swing.JTextField alumnoNombre;
    private javax.swing.JComboBox<String> alumnoProfesor;
    private javax.swing.JTable alumnoTabla;
    private javax.swing.JTextField claseAlumnos;
    private javax.swing.JTextField claseAsignaturas;
    private javax.swing.JButton claseAñadir;
    private javax.swing.JButton claseBorrar;
    private javax.swing.JButton claseCancelar;
    private javax.swing.JButton claseGuardar;
    private javax.swing.JComboBox<String> claseInstituto;
    private javax.swing.JButton claseModificar;
    private javax.swing.JButton claseSalir;
    private javax.swing.JButton claseSalir1;
    private javax.swing.JTable claseTabla;
    private javax.swing.JButton instiAñadir;
    private javax.swing.JButton instiBorrar;
    private javax.swing.JTextField instiCalle;
    private javax.swing.JButton instiCancelar;
    private javax.swing.JButton instiGuardar;
    private javax.swing.JButton instiModificar;
    private javax.swing.JTextField instiNombre;
    private javax.swing.JTextField instiProvincia;
    private javax.swing.JButton instiSalir;
    private javax.swing.JTable instiTabla;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField profeApellido;
    private javax.swing.JButton profeAñadir;
    private javax.swing.JButton profeBorrar;
    private javax.swing.JButton profeCancelar;
    private javax.swing.JTextField profeDni;
    private javax.swing.JButton profeGuardar;
    private javax.swing.JButton profeModificar;
    private javax.swing.JTextField profeNombre;
    private javax.swing.JButton profeSalir;
    private javax.swing.JTable profeTabla;
    private javax.swing.JComboBox<String> profesorClase;
    // End of variables declaration//GEN-END:variables
}
