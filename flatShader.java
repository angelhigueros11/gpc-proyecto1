// Angel Higueros - 20460
// Proyecto 1 

public class flatShader extends Shader{
    
    public flatShader(double u, double v, double w, double[] tA, double[] tB, double[] tC, double[] triangleNormal, Renderer r, Point3[] normals) {
        super(u, v, w, tA, tB, tC, triangleNormal, r,normals);
    }
    public flatShader(){
        super();
    }
    
    @Override
    public double[] getColor(){

        

        super.r /= 255.0;
        super.g /= 255.0;
        super.b /= 255.0;

    
        if(super.renderer.active_texture != null){
            double tU = super.u * super.tA[0] + super.v * super.tB[0] + super.w * super.tC[0];
            double tV = super.u * super.tA[1] + super.v * super.tB[1] + super.w * super.tC[1];

    
            double[] color = super.renderer.active_texture.getColor(tU, tV);
            if(color != null){
                super.r *= color[0];
                super.g *= color[1];
                super.b *= color[2];


            }
            
        }
        double[] light = new double[]{ super.renderer.dirLight.x, super.renderer.dirLight.y, super.renderer.dirLight.z };
        double intensity = math.dot_product(super.triangleNormal, math.negation(light));
        super.r *= intensity;
        super.g *= intensity;
        super.b *= intensity;

        if(intensity>0)
            return new double[]{super.r, super.g, super.b};
        else
            return new double[]{0.0,0.0,0.0};
    
    }
    

}
