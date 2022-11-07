// Angel Higueros - 20460
// Proyecto 1 

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Texture {
    // Atributos
    private static ArrayList< ArrayList<ArrayList<Double>> > pixels;
    private static int ImageWidth;
    private static int ImageHeight;

    public static double[] tempColor(double r, double g, double b) {
        return new double[] {
            (double) (int) (((b>0)? b:0) * 255),
            (double) (int) (((g>0)? g:0) * 255),
            (double) (int) (((r>0)? r:0) * 255),
        };
    }
    // Constructor
    public Texture(String filename){

        pixels = new ArrayList< ArrayList<ArrayList<Double>> >();
        ImageHeight = 0;
        ImageWidth = 0;

        loadTexture(filename);
    }


    // Métodos
    private void loadTexture(String filename){
        try {
            File file = new File(filename);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            byte[] bytes = new byte[54];
            bis.read(bytes, 0, 54);
            ByteBuffer bb = ByteBuffer.wrap(bytes);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            int width = bb.getInt(18);
            int height = bb.getInt(22);
            ArrayList<ArrayList<Double>> pixelRows = new ArrayList<ArrayList<Double>>();
            for(int y = 0; y < height; y++) {
                pixelRows= new ArrayList<ArrayList<Double>>();
                for(int x = 0; x < width; x++) {
                    Double b = bis.read()/255.0;
                    Double g = bis.read()/255.0;
                    Double r = bis.read()/255.0;



                    ArrayList<Double> pixel = new ArrayList<Double>();
                    pixel.add(r);
                    pixel.add(g);
                    pixel.add(b);
                    pixelRows.add(pixel);
                    
                }
                pixels.add(pixelRows);
            }
            ImageWidth = width;
            ImageHeight = height;
            bis.close();
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double[] getColor(Double u, Double v){
        if(0<=u && u<1 && 0<=v && v<1){
           
            int x = (int) (u * ImageWidth);
            int y = (int) (v * ImageHeight);
            
            double[] color = new double[]{
                pixels.get(y).get(x).get(0),
                pixels.get(y).get(x).get(1),
                pixels.get(y).get(x).get(2)
            };
          

            return color;
        }
        return null;
    }

}
