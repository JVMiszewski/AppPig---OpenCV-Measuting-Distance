
package imageprocessing;

import android.util.Log;

import com.example.opencvtesting.MainActivity;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Proc {
    private static String LOG_TAG = "class Proc: ";
    private static int pic_count = 0;

    public static  double findMarkerWidth(String imgPath){

        Mat frame = Imgcodecs.imread(imgPath);
        Mat gscale = new Mat();
        Mat blur = new Mat();
        Mat edged = new Mat();

        if(frame.channels()>1)
            Imgproc.cvtColor(frame, gscale, Imgproc.COLOR_BGR2GRAY);
        else
            gscale = frame;

        Imgproc.GaussianBlur(gscale, blur, new Size(5, 5), 0);
        Imgproc.Canny(blur, edged, 35, 125);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat(edged.width(), edged.height(), CvType.CV_8UC1);
        Imgproc.findContours(edged.clone(), contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        int max_idx = 0;

        if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
        {
            double max_area = 0;
            double area;

            for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
            {
                area = Imgproc.contourArea(contours.get(idx));
                if(area > max_area){
                    max_area = area;
                    max_idx = idx;
                }
                Imgproc.drawContours(frame, contours, idx, new Scalar(0, 0, 255));
            }

            byte[] bytes = new byte[ frame.rows() * frame.cols() * frame.channels() ];


            File file = new File(MainActivity.activity.getExternalFilesDir(null), "pic_contour"+ Integer.toString(pic_count) + ".jpg");
            pic_count++;

            Boolean bool = null;
            String filename = file.toString();
            bool = Imgcodecs.imwrite(filename, frame);

            if (bool == true)
                Log.d(LOG_TAG, "Sucesso ao salvar imagem no armazenamento externo!");
            else
                Log.d(LOG_TAG, "Falha ao salvar imagem no armazenamento externo!");

            Log.i(LOG_TAG, "Area Máxima: " + Double.toString(max_area));
        }
        else{
            Log.e(LOG_TAG, "Nenhum contorno encontrado!");
        }

        MatOfPoint2f newPoint = new MatOfPoint2f(contours.get(max_idx).toArray());

        return Imgproc.arcLength(newPoint, true);
    }

    public static double distanceToImage(double focalLength, double knownWidth, double pixelsPerWidth){
        return (knownWidth * focalLength) / pixelsPerWidth;
    }
}
