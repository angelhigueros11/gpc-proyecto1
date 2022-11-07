// Angel Higueros - 20460
// Proyecto 1 

import java.util.ArrayList;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner; 
import java.util.Collections;


public class Obj {
    // Atributos
    private ArrayList<String> lines;
    private ArrayList< ArrayList<Double> > vertices;
    private ArrayList< ArrayList<Double> > texcoords;
    private ArrayList< ArrayList<Double> > normals;
    private ArrayList< ArrayList<ArrayList<Integer>> > faces;
    private Double maxValue=0.0;

    // Construtor
    // @param filename: nombre del  archivo .obj
    public Obj(String filename){
        // Inicializar atributos
        this.lines = new ArrayList<String>();
        this.vertices = new ArrayList< ArrayList<Double> >();
        this.texcoords = new ArrayList< ArrayList<Double> >();
        this.normals = new ArrayList< ArrayList<Double> >();
        this.faces = new ArrayList< ArrayList<ArrayList<Integer>> >();


        getAttributesFromFile(filename);
    }


    // Methods
    private Double normalize(Double value){
        return value / maxValue;
    }

    /**
     * Funcion que lee el archivo y llenara los arregles de informacion
     * @param filename
     * 
     */
    private void getAttributesFromFile(String filename){
        try{
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                this.lines.add(line);
            }

            myReader.close();

            for(String line: lines){
                try{

                
                    line = String.join(" ", line.split(" "));

                    String[] parts = line.split(" ", 2);
                    String prefix = parts[0];
                    String value = parts[1];

                    switch(prefix){

                        case "v":
                            ArrayList<Double> VerticesArray = new ArrayList<Double>();
                            for(String s : value.split(" ")){
                                VerticesArray.add(Double.parseDouble(s));
                            }
                            Double max = Collections.max(VerticesArray);

                            if(max > this.maxValue){
                                maxValue = max;
                            }
                            this.vertices.add(VerticesArray);
                            break;


                        case "vt":

                            ArrayList<Double> TCArray = new ArrayList<Double>();
                            for(String s : value.split(" ")){
                                TCArray.add(Double.parseDouble(s));
                            }

                            if(TCArray.size() == 2){
                                TCArray.add(0.0);
                            }
                            this.texcoords.add(TCArray);
                            break;


                        case "vn":
                            ArrayList<Double> NormalsArray = new ArrayList<Double>();
                            for(String s : value.split(" ")){
                                NormalsArray.add(Double.parseDouble(s));
                            }
                            this.normals.add(NormalsArray);
                            break;


                        case "f":
                            ArrayList< ArrayList<Integer> > FacesArray = new ArrayList< ArrayList<Integer>>();

                            for(String vert : value.trim().split(" ")){
                                String[] indices = vert.split("/");
                                for(int i=0; i<indices.length; i++){
                                    if(indices[i]==""){
                                        indices[i] = "0";
                                    }
                                }
                                
                                ArrayList<Integer> indicesArray = new ArrayList<Integer>();
                                for(String s : indices){
                                    indicesArray.add(Integer.parseInt(s));
                                }

                                FacesArray.add(indicesArray);

                            }

                            this.faces.add(FacesArray);
                            break;
                        default:
                            break;
                    }
                }catch(Exception e){
                    System.out.println("Error: " + e);
                }

            }

            for(int i=0; i<this.vertices.size(); i++){
                for(int j=0; j<this.vertices.get(i).size(); j++){
                    this.vertices.get(i).set(j, normalize(this.vertices.get(i).get(j)));
                }
            }
            

        } catch (FileNotFoundException e) {
            System.out.println("Error leyendo el OBJ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    // Getters

    public ArrayList< ArrayList<Double> > getVertices(){
        return this.vertices;
    }

    public ArrayList< ArrayList<Double> > getTexcoords(){
        return this.texcoords;
    }

    public ArrayList< ArrayList<Double> > getNormals(){
        return this.normals;
    }

    public ArrayList< ArrayList<ArrayList<Integer>> > getFaces(){
        return this.faces;
    }

    public Double getMaxValue(){
        return this.maxValue;
    }
}