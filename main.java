// Angel Higueros - 20460
// Proyecto 1 

public class main {

    public static void main(String[] args) {

        final long startTime = System.nanoTime();
        // Obj obj = new Obj("mario.obj");
        Renderer renderer = new Renderer(1920, 1080);
        renderer.background = new Texture("fondoo.bmp");
        renderer.glClearBackground();

        renderer.active_texture = new Texture("blood.bmp");
        renderer.active_shader_string = "glowCustomShader";

        renderer.glLoadModel("MK.obj",
                new Point3(0.0, 2.0, -5.0),
                new Point3(90.0, 0.0, 0.0),
                new Point3(1.0, 1.0, 1.0));

        renderer.active_texture = null;
        renderer.active_shader_string = "metalShader";
        renderer.glLoadModel("model.obj", 
        new Point3(-0.5, -0.5, -2.0), 
        new Point3(0.0, 0.0, 0.0),
        new Point3(1.0, 1.0, 1.0));

        renderer.active_texture = new Texture("spidermanT.bmp");
        renderer.active_shader_string = "negativeShader";
        renderer.glLoadModel("spiderman.obj", 
        new Point3(1.5, -0.5, -2.0), 
        new Point3(0.0, -90.0, 0.0),
        new Point3(1.0, 1.0, 1.0));

        renderer.active_texture = new Texture("marioT.bmp");
        renderer.active_shader_string = "sinFunShader";
        renderer.glLoadModel("mario.obj",
                new Point3(0.7, -0.5, -4.0),
                new Point3(0.0, 0.0, 0.0),
                new Point3(1.0, 1.0, 1.0));

        renderer.active_texture = new Texture("venomM.bmp");
        renderer.active_shader_string = "flatShader";
        renderer.glLoadModel("venom.obj",
                new Point3(-0.8, -0.5, -1.0),
                new Point3(0.0, 125.0, 0.0),
                new Point3(1.0, 1.0, 1.0));


        renderer.glFinish("output.bmp");

        final long duration = System.nanoTime() - startTime;
        System.out.println("Tiempo de ejecucion: " + duration / 1000000000.0 + " segundos");

    }

}
