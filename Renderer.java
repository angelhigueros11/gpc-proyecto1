// Angel Higueros - 20460
// Proyecto 1 

import java.io.Console;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Renderer {
    private int width;
    private int height;
    private int vpWidth;
    private int vpHeight;
    private int vpX;
    private int vpY;

    private double[] clearColor;
    private double[] currColor;

    private Shader active_shader;
    public String active_shader_string;
    public Texture active_texture;

    private ArrayList<ArrayList<double[]>> pixels;

    public Texture background;
    public Point3 dirLight;

    public double[][] camMatrix;
    private double[][] viewMatrix;
    private double[][] viewportMatrix;
    private double[][] projectionMatrix;
    private double[][] zbuffer;

    private CustomMath math;

    public static byte[] color(double r, double g, double b) {
        return new byte[] {
                (byte) (int) (((b > 0) ? b : 0) * 255),
                (byte) (int) (((g > 0) ? g : 0) * 255),
                (byte) (int) (((r > 0) ? r : 0) * 255),
        };
    }

    public static double[] tempColor(double r, double g, double b) {
        return new double[] {
                (double) (int) (((b > 0) ? b : 0) * 255),
                (double) (int) (((g > 0) ? g : 0) * 255),
                (double) (int) (((r > 0) ? r : 0) * 255),
        };
    }

    public Shader chooseShader(String shader) {
        Shader res = null;
        switch (shader) {
            case "flatShader":
                res = new flatShader();
                break;
            case "metalShader":
                res = new metalShader();
                break;
            case "negativeShader":
                res = new negativeShader();
                break;
            case "sinFunShader":
                res = new sinFunShader();
                break;
            case "glowCustomShader":
                res = new glowCustomShader();
                break;
        }

        return res;
    }

    public static byte[] word(int value) {
        return new byte[] {
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
        };
    }

    public static byte[] dword(int value) {
        return new byte[] {
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
                (byte) ((value >> 16) & 0xff),
                (byte) ((value >> 24) & 0xff),
        };
    }

    public Renderer(int width, int height) {
        math = new CustomMath();
        this.width = width;
        this.height = height;
        this.clearColor = tempColor(0.2, 0.2, 0.2);
        this.currColor = tempColor(1, 1, 1);
        this.active_shader = null;
        this.active_texture = null;
        this.background = null;
        this.dirLight = new Point3(1.0, 0.0, -5.0);
        this.viewMatrix = new double[4][4];
        this.pixels = new ArrayList<ArrayList<double[]>>();
        this.zbuffer = new double[width][height];

        glViewMatrix(null, null);
        glViewport(0, 0, width, height);
        glClear();

    }

    private void glViewport(int posX, int posY, int width, int height) {
        this.vpWidth = width;
        this.vpHeight = height;
        this.vpX = posX;
        this.vpY = posY;

        this.viewportMatrix = new double[][] {
                { width / 2.0, 0.0, 0.0, posX + width / 2.0 },
                { 0.0, height / 2.0, 0.0, posY + height / 2.0 },
                { 0.0, 0.0, 0.5, 0.5 },
                { 0.0, 0.0, 0.0, 1.0 }
        };

        glProjectionMatrix();

    }

    private void glViewMatrix(Point3 translate, Point3 rotate) {
        if (translate == null) {
            translate = new Point3(0.0, 0.0, 0.0);
        }
        if (rotate == null) {
            rotate = new Point3(0.0, 0.0, 0.0);
        }
        this.camMatrix = this.glCreateObjectMatrix(translate, rotate, null);
        this.viewMatrix = math.invert(this.camMatrix);

    }

    private double convertToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    private double[][] glCreateRotationMatrix(Point3 rotate) {
        if (rotate == null) {
            rotate = new Point3(0.0, 0.0, 0.0);
        }

        double[][] rx = new double[][] {
                { 1.0, 0.0, 0.0, 0.0 },
                { 0.0, Math.cos(convertToRadians(rotate.x)), -Math.sin(convertToRadians(rotate.x)), 0.0 },
                { 0.0, Math.sin(convertToRadians(rotate.x)), Math.cos(convertToRadians(rotate.x)), 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
        };

        double[][] ry = new double[][] {
                { Math.cos(convertToRadians(rotate.y)), 0.0, Math.sin(convertToRadians(rotate.y)), 0.0 },
                { 0.0, 1.0, 0.0, 0.0 },
                { -Math.sin(convertToRadians(rotate.y)), 0.0, Math.cos(convertToRadians(rotate.y)), 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
        };

        double[][] rz = new double[][] {
                { Math.cos(convertToRadians(rotate.z)), -Math.sin(convertToRadians(rotate.z)), 0.0, 0.0 },
                { Math.sin(convertToRadians(rotate.z)), Math.cos(convertToRadians(rotate.z)), 0.0, 0.0 },
                { 0.0, 0.0, 1.0, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
        };

        double[][] rxry = math.matrixMultiplication(rx, ry);
        double[][] rotation = math.matrixMultiplication(rxry, rz);

        return rotation;
    }

    private double[][] glCreateObjectMatrix(Point3 translate, Point3 rotate, Point3 scale) {
        if (translate == null) {
            translate = new Point3(0.0, 0.0, 0.0);
        }
        if (rotate == null) {
            rotate = new Point3(0.0, 0.0, 0.0);
        }
        if (scale == null) {
            scale = new Point3(1.0, 1.0, 1.0);
        }

        double[][] translation = new double[][] {
                { 1.0, 0.0, 0.0, translate.x },
                { 0.0, 1.0, 0.0, translate.y },
                { 0.0, 0.0, 1.0, translate.z },
                { 0.0, 0.0, 0.0, 1.0 }
        };

        double[][] rotation = glCreateRotationMatrix(rotate);

        double[][] scaleM = new double[][] {
                { scale.x, 0.0, 0.0, 0.0 },
                { 0.0, scale.y, 0.0, 0.0 },
                { 0.0, 0.0, scale.z, 0.0 },
                { 0.0, 0.0, 0.0, 1.0 }
        };

        double[][] pre = math.matrixMultiplication(translation, rotation);
        double[][] post = math.matrixMultiplication(pre, scaleM);

        return post;

    }

    private void glProjectionMatrix() {
        double n = 0.1;
        double f = 1000.0;
        int fov = 60;

        double asPectRatio = (double) this.vpWidth / (double) this.vpHeight;

        double t = Math.tan(convertToRadians(fov) / 2.0) * n;
        double r = t * asPectRatio;

        this.projectionMatrix = new double[][] {
                { n / r, 0.0, 0.0, 0.0 },
                { 0.0, n / t, 0.0, 0.0 },
                { 0.0, 0.0, -(f + n) / (f - n), -(2 * f * n) / (f - n) },
                { 0.0, 0.0, -1.0, 0.0 }
        };

    }

    private void glClearColor(double r, double g, double b) {
        this.clearColor = tempColor(r, g, b);
    }

    private void glColor(double r, double g, double b) {
        this.currColor = tempColor(r, g, b);
    }

    private void glClear() {
        for (int x = 0; x < width; x++) {
            ArrayList<double[]> pixelRow = new ArrayList<double[]>();
            for (int y = 0; y < height; y++) {
                pixelRow.add(this.clearColor);
                this.zbuffer[x][y] = Float.POSITIVE_INFINITY;
            }
            this.pixels.add(pixelRow);
        }

    }

    public void glClearBackground() {
        if (this.background != null) {
            for (int x = this.vpX; x < this.vpX + this.vpWidth + 1; x++) {

                for (int y = this.vpY; y < this.vpY + this.vpHeight + 1; y++) {

                    double tU = (double) (x - this.vpX) / (double) this.vpWidth;
                    double tV = (double) (y - this.vpY) / (double) this.vpHeight;

                    double[] texColor = this.background.getColor(tU, tV);

                    if (texColor != null) {

                        glPoint(x, y, texColor);
                    }

                }
            }
        }
    }

    private void glPoint(int x, int y, double[] color) {
        if (0 <= x && x < this.width && 0 <= y && y < this.height) {
            if (color == null) {
                color = this.currColor;
            }

            this.pixels.get(x).set(y, color);
        }
    }

    private void glClearViewport(double[] clr) {
        for (int x = this.vpX; x < this.vpX + this.vpWidth; x++) {
            for (int y = this.vpY; y < this.vpY + this.vpHeight; y++) {
                glPoint(x, y, clr);
            }
        }

    }

    private void gl_Point_vp(double ndcX, double ndcY, double[] clr) {
        if (ndcX < -1 || ndcX > 1 || ndcY < -1 || ndcY > 1) {
            return;
        }

        int x = (int) ((ndcX + 1.0) * (this.vpWidth / 2.0) + this.vpX);
        int y = (int) ((ndcY + 1.0) * (this.vpHeight / 2.0) + this.vpY);

        glPoint(x, y, clr);
    }

    private Point3 glTransform(double[] vertex, double[][] matrix) {

        double[] v = new double[] { vertex[0], vertex[1], vertex[2], 1.0 };
        double[] vt = math.multiplyMatrixVector(matrix, v);

        Point3 vf = new Point3(
                vt[0] / vt[3],
                vt[1] / vt[3],
                vt[2] / vt[3]);

        return vf;
    }

    private Point3 glDirTransform(double[] dirVector, double[][] rotMatrix) {
        double[] v = new double[] { dirVector[0], dirVector[1], dirVector[2], 0.0 };
        double[] vt = math.multiplyMatrixVector(rotMatrix, v);

        Point3 vf = new Point3(
                vt[0],
                vt[1],
                vt[2]);

        return vf;
    }

    private Point3 glCamTransform(double[] vertex) {
        double[] v = new double[] { vertex[0], vertex[1], vertex[2], 1.0 };
        double[][] tempMatrix = math.matrixMultiplication(this.viewportMatrix, this.projectionMatrix);

        tempMatrix = math.matrixMultiplication(tempMatrix, this.viewMatrix);
        double[] vt = math.multiplyMatrixVector(tempMatrix, v);

        Point3 vf = new Point3(
                vt[0] / vt[3],
                vt[1] / vt[3],
                vt[2] / vt[3]);



        return vf;
    }

    public void glLoadModel(String filename, Point3 translate, Point3 rotate, Point3 scale) {
        if (translate == null) {
            translate = new Point3(0.0, 0.0, 0.0);
        }
        if (rotate == null) {
            rotate = new Point3(0.0, 0.0, 0.0);
        }
        if (scale == null) {
            scale = new Point3(1.0, 1.0, 1.0);
        }

        Obj model = new Obj(filename);
        double[][] modelMatrix = glCreateObjectMatrix(translate, rotate, scale);
        double[][] rotMatrix = glCreateRotationMatrix(rotate);

        for (ArrayList<ArrayList<Integer>> face : model.getFaces()) {
            int vertCount = face.size();

            ArrayList<Double> _0 = model.getVertices().get(face.get(0).get(0) - 1);
            double[] v0 = new double[] { _0.get(0), _0.get(1), _0.get(2) };

            ArrayList<Double> _1 = model.getVertices().get(face.get(1).get(0) - 1);
            double[] v1 = new double[] { _1.get(0), _1.get(1), _1.get(2) };

            ArrayList<Double> _2 = model.getVertices().get(face.get(2).get(0) - 1);
            double[] v2 = new double[] { _2.get(0), _2.get(1), _2.get(2) };

            Point3 V0 = glTransform(v0, modelMatrix);

            Point3 V1 = glTransform(v1, modelMatrix);
            Point3 V2 = glTransform(v2, modelMatrix);

            Point3 A = glCamTransform(new double[] { V0.x, V0.y, V0.z });
            Point3 B = glCamTransform(new double[] { V1.x, V1.y, V1.z });

            Point3 C = glCamTransform(new double[] { V2.x, V2.y, V2.z });

            ArrayList<Double> __0 = model.getTexcoords().get(face.get(0).get(1) - 1);
            double[] vt0 = new double[] { __0.get(0), __0.get(1), __0.get(2) };

            ArrayList<Double> __1 = model.getTexcoords().get(face.get(1).get(1) - 1);
            double[] vt1 = new double[] { __1.get(0), __1.get(1), __1.get(2) };

            ArrayList<Double> __2 = model.getTexcoords().get(face.get(2).get(1) - 1);
            double[] vt2 = new double[] { __2.get(0), __2.get(1), __2.get(2) };


            ArrayList<Double> n_0 = model.getNormals().get(face.get(0).get(2) - 1);
            double[] vn0 = new double[] { n_0.get(0), n_0.get(1), n_0.get(2) };

            ArrayList<Double> n_1 = model.getNormals().get(face.get(1).get(2) - 1);
            double[] vn1 = new double[] { n_1.get(0), n_1.get(1), n_1.get(2) };

            ArrayList<Double> n_2 = model.getNormals().get(face.get(2).get(2) - 1);
            double[] vn2 = new double[] { n_2.get(0), n_2.get(1), n_2.get(2) };

            Point3 N0 = glDirTransform(vn0, rotMatrix);
            Point3 N1 = glDirTransform(vn1, rotMatrix);
            Point3 N2 = glDirTransform(vn2, rotMatrix);

            Point3[] verts = new Point3[] { V0, V1, V2 };
            Point3[] normals = new Point3[] { N0, N1, N2 };
            double[][] texcoords = new double[][] { vt0, vt1, vt2 };
            glTriangle_bc(A, B, C, verts, normals, texcoords, null);

            if (vertCount == 4) {
                ArrayList<Double> _3 = model.getVertices().get(face.get(3).get(0) - 1);
                double[] v3 = new double[] { _3.get(0), _3.get(1), _3.get(2) };
                Point3 V3 = glTransform(v3, modelMatrix);
                Point3 D = glCamTransform(new double[] { V3.x, V3.y, V3.z });

                ArrayList<Double> __3 = model.getTexcoords().get(face.get(3).get(1) - 1);
                double[] vt3 = new double[] { __3.get(0), __3.get(1), __3.get(2) };

                ArrayList<Double> n_3 = model.getNormals().get(face.get(3).get(2) - 1);
                double[] vn3 = new double[] { n_3.get(0), n_3.get(1), n_3.get(2) };
                Point3 N3 = glDirTransform(vn3, rotMatrix);

                Point3[] verts2 = new Point3[] { V0, V2, V3 };
                Point3[] normals2 = new Point3[] { N0, N2, N3 };
                double[][] texcoords2 = new double[][] { vt0, vt2, vt3 };

                glTriangle_bc(A, C, D, verts2, normals2, texcoords2, null);
            }
        }

    }

    private void glTriangle_bc(Point3 A, Point3 B, Point3 C, Point3[] verts, Point3[] normals, double[][] texcoords,
            double[] clr) {

        int minX = (int) Math.round(Math.min(A.x, Math.min(B.x, C.x)));
        int minY = (int) Math.round(Math.min(A.y, Math.min(B.y, C.y)));
        int maxX = (int) Math.round(Math.max(A.x, Math.max(B.x, C.x)));
        int maxY = (int) Math.round(Math.max(A.y, Math.max(B.y, C.y)));

        double[][] edgea = math.matrixSubtraction(
                new double[][] {
                        { verts[1].x, verts[1].y, verts[1].z },
                },
                new double[][] {
                        { verts[0].x, verts[0].y, verts[0].z },
                });

        double[][] edgeb = math.matrixSubtraction(
                new double[][] {
                        { verts[2].x, verts[2].y, verts[2].z },

                },
                new double[][] {
                        { verts[0].x, verts[0].y, verts[0].z },

                });

        double[] edge1 = edgea[0];
        double[] edge2 = edgeb[0];

        double[] triangleNormala = math.cross_product(edge1, edge2);

        double maxV = Math.max(triangleNormala[0], Math.max(triangleNormala[1], triangleNormala[2]));

        for (int i = 0; i < triangleNormala.length; i++) {
            triangleNormala[i] /= maxV;
        }

        double[] t2 = texcoords[2];
        double[] t1 = texcoords[1];
        double[] t0 = texcoords[0];

        double[][] deltaUV1 = math.matrixSubtraction(
                new double[][] {
                        t1
                },
                new double[][] {
                        t0
                });

        double[][] deltaUV2 = math.matrixSubtraction(
                new double[][] {
                        t2
                },
                new double[][] {
                        t0
                });

        double[] DELTAUV1 = deltaUV1[0];
        double[] DELTAUV2 = deltaUV2[0];

        double rr = (DELTAUV1[0] * DELTAUV2[1] - DELTAUV2[0] * DELTAUV1[1]);
        double f = (rr != 0.0) ? 1 / rr : 1 / 0.00001;

        double[] tangent = new double[3];
        tangent[0] = f * (DELTAUV2[1] * edge1[0] - DELTAUV1[1] * edge2[0]);
        tangent[1] = f * (DELTAUV2[1] * edge1[1] - DELTAUV1[1] * edge2[1]);
        tangent[2] = f * (DELTAUV2[1] * edge1[2] - DELTAUV1[1] * edge2[2]);

        double maxT = Math.max(tangent[0], Math.max(tangent[1], tangent[2]));
        for (int i = 0; i < tangent.length; i++) {
            tangent[i] /= maxT;
        }

        double[] bitangent = math.cross_product(triangleNormala, tangent);
        double maxB = Math.max(bitangent[0], Math.max(bitangent[1], bitangent[2]));
        for (int i = 0; i < bitangent.length; i++) {
            bitangent[i] /= maxB;
        }

        for (int x = minX; x < maxX + 1; x++)
            for (int y = minY; y < maxY + 1; y++) {
                double[] bC = math.baryCoords(A, B, C, new Point3((double) x, (double) y, 0.0));
                double u = bC[0];
                double v = bC[1];
                double w = bC[2];


                if (0 <= u && 0 <= v && 0 <= w) {
                    double z = A.z * u + B.z * v + C.z * w;
                    if (0 <= x && x < this.width && 0 <= y && y < this.height) {

                        if (z < this.zbuffer[x][y] && -1 <= z && z <= 1) {
                            this.zbuffer[x][y] = z;

                            if (this.active_shader_string != null) {
                                Shader shader = this.chooseShader(active_shader_string);

                                double[] cc = (clr == null) ? this.currColor : clr;
                                shader.setAttributes(u, v, w, t0, t1, t2, triangleNormala, this, cc, normals);
                                double[] color = shader.getColor();

                                glPoint(x, y, color);

                            } else {
                                glPoint(x, y, clr);
                            }

                        }

                    }

                }

            }
    }

    public void glFinish(String outputFileName) {
        try {
            FileOutputStream file = new FileOutputStream(outputFileName);
            // Header
            file.write("B".getBytes());
            file.write("M".getBytes());
            file.write(dword(14 + 40 + (this.width * this.height * 3)));
            file.write(dword(0));
            file.write(dword(14 + 40));

            // InfoHeader
            file.write(dword(40));
            file.write(dword(this.width));
            file.write(dword(this.height));
            file.write(word((short) 1));
            file.write(word((short) 24));
            file.write(dword(0));
            file.write(dword(this.width * this.height * 3));
            file.write(dword(0));
            file.write(dword(0));
            file.write(dword(0));
            file.write(dword(0));
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    file.write(color(
                            (double) pixels.get(x).get(y)[0],
                            (double) pixels.get(x).get(y)[1],
                            (double) pixels.get(x).get(y)[2]));

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
