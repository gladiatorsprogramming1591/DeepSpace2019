
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.vision.VisionThread;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


/*
   JSON format:
   {
       "team": <team number>,
       "ntmode": <"client" or "server", "client" if unspecified>
       "cameras": [
           {
               "name": <camera name>
               "path": <path, e.g. "/dev/video0">
               "pixel format": <"MJPEG", "YUYV", etc>   // optional
               "width": <video mode width>              // optional
               "height": <video mode height>            // optional
               "fps": <video mode fps>                  // optional
               "brightness": <percentage brightness>    // optional
               "white balance": <"auto", "hold", value> // optional
               "exposure": <"auto", "hold", value>      // optional
               "properties": [                          // optional
                   {
                       "name": <property name>
                       "value": <property value>
                   }
               ],
               "stream": {                              // optional
                   "properties": [
                       {
                           "name": <stream property name>
                           "value": <stream property value>
                       }
                   ]
               }
           }
       ]
   }
 */

public final class Main{
  private static String configFile = "/boot/frc.json";

  @SuppressWarnings("MemberName")
  public static class CameraConfig {
    public String name;
    public String path;
    public JsonObject config;
    public JsonElement streamConfig;
  }

  public static int team;
  public static boolean server;
  public static List<CameraConfig> cameraConfigs = new ArrayList<>();
  public static UsbCamera camera;

  // declare networktables objects
  public static NetworkTable table;
  public static NetworkTableEntry rightXEntry;
  public static NetworkTableEntry rightYEntry;
  public static NetworkTableEntry rightAreaEntry;
  public static NetworkTableEntry leftXEntry;
  public static NetworkTableEntry leftYEntry;
  public static NetworkTableEntry leftAreaEntry;

  private Main() {
  }

  /**
   * Report parse error.
   */
  public static void parseError(String str) {
    System.err.println("config error in '" + configFile + "': " + str);
  }

  /**
   * Read single camera configuration.
   */
  public static boolean readCameraConfig(JsonObject config) {
    CameraConfig cam = new CameraConfig();

    // name
    JsonElement nameElement = config.get("name");
    if (nameElement == null) {
      parseError("could not read camera name");
      return false;
    }
    cam.name = nameElement.getAsString();

    // path
    JsonElement pathElement = config.get("path");
    if (pathElement == null) {
      parseError("camera '" + cam.name + "': could not read path");
      return false;
    }
    cam.path = pathElement.getAsString();

    // stream properties
    cam.streamConfig = config.get("stream");

    cam.config = config;

    cameraConfigs.add(cam);
    return true;
  }

  /**
   * Read configuration file.
   */
  @SuppressWarnings("PMD.CyclomaticComplexity")
  public static boolean readConfig() {
    // parse file
    JsonElement top;
    try {
      top = new JsonParser().parse(Files.newBufferedReader(Paths.get(configFile)));
    } catch (IOException ex) {
      System.err.println("could not open '" + configFile + "': " + ex);
      return false;
    }

    // top level must be an object
    if (!top.isJsonObject()) {
      parseError("must be JSON object");
      return false;
    }
    JsonObject obj = top.getAsJsonObject();

    // team number
    JsonElement teamElement = obj.get("team");
    if (teamElement == null) {
      parseError("could not read team number");
      return false;
    }
    team = teamElement.getAsInt();

    // ntmode (optional)
    if (obj.has("ntmode")) {
      String str = obj.get("ntmode").getAsString();
      if ("client".equalsIgnoreCase(str)) {
        server = false;
      } else if ("server".equalsIgnoreCase(str)) {
        server = true;
      } else {
        parseError("could not understand ntmode value '" + str + "'");
      }
    }

    // cameras
    JsonElement camerasElement = obj.get("cameras");
    if (camerasElement == null) {
      parseError("could not read cameras");
      return false;
    }
    JsonArray cameras = camerasElement.getAsJsonArray();
    for (JsonElement camera : cameras) {
      if (!readCameraConfig(camera.getAsJsonObject())) {
        return false;
      }
    }

    return true;
  }

  /**
   * Start running the camera.
   */
  public static VideoSource startCamera(CameraConfig config) {
    System.out.println("Starting camera '" + config.name + "' on " + config.path);
    CameraServer inst = CameraServer.getInstance();
    camera = new UsbCamera(config.name, config.path);
    MjpegServer server = inst.startAutomaticCapture(camera);

    Gson gson = new GsonBuilder().create();

    camera.setConfigJson(gson.toJson(config.config));
    camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);

    if (config.streamConfig != null) {
      server.setConfigJson(gson.toJson(config.streamConfig));
    }

    return camera;
  }

  /**
   * Pipeline generated by GRIP
   */
  public static class GripPipeline implements VisionPipeline {
      
  	//Outputs
  	private Mat hslThresholdOutput = new Mat();
  	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
  	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();
    
  	static {
  		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  	}
  
  	/**
  	 * This is the primary method that runs the entire pipeline and updates the outputs.
  	 */
  	public void process(Mat source0) {
  		// Step HSL_Threshold0:
  		Mat hslThresholdInput = source0;
  		double[] hslThresholdHue = {64.74819835141408, 91.9453885856342};
  		double[] hslThresholdSaturation = {206.3848658431348, 255.0};
  		double[] hslThresholdLuminance = {146.76258446072504, 255.0};
  		hslThreshold(hslThresholdInput, hslThresholdHue, hslThresholdSaturation, hslThresholdLuminance, hslThresholdOutput);
    
  		// Step Find_Contours0:
  		Mat findContoursInput = hslThresholdOutput;
  		boolean findContoursExternalOnly = false;
  		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);
    
  		// Step Filter_Contours0:
  		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
  		double filterContoursMinArea = 0.0;
  		double filterContoursMinPerimeter = 0.0;
  		double filterContoursMinWidth = 0.0;
  		double filterContoursMaxWidth = 1000.0;
  		double filterContoursMinHeight = 0.0;
  		double filterContoursMaxHeight = 1000.0;
  		double[] filterContoursSolidity = {0, 100};
  		double filterContoursMaxVertices = 1000000.0;
  		double filterContoursMinVertices = 0.0;
  		double filterContoursMinRatio = 0.0;
  		double filterContoursMaxRatio = 1000.0;
  		filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter, filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight, filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices, filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio, filterContoursOutput);
    
  	}
  
  	/**
  	 * This method is a generated getter for the output of a HSL_Threshold.
  	 * @return Mat output from HSL_Threshold.
  	 */
  	public Mat hslThresholdOutput() {
  		return hslThresholdOutput;
  	}
  
  	/**
  	 * This method is a generated getter for the output of a Find_Contours.
  	 * @return ArrayList<MatOfPoint> output from Find_Contours.
  	 */
  	public ArrayList<MatOfPoint> findContoursOutput() {
  		return findContoursOutput;
  	}
  
  	/**
  	 * This method is a generated getter for the output of a Filter_Contours.
  	 * @return ArrayList<MatOfPoint> output from Filter_Contours.
  	 */
  	public ArrayList<MatOfPoint> filterContoursOutput() {
  		return filterContoursOutput;
  	}
  
  
  	/**
  	 * Segment an image based on hue, saturation, and luminance ranges.
  	 *
  	 * @param input The image on which to perform the HSL threshold.
  	 * @param hue The min and max hue
  	 * @param sat The min and max saturation
  	 * @param lum The min and max luminance
  	 * @param output The image in which to store the output.
  	 */
  	private void hslThreshold(Mat input, double[] hue, double[] sat, double[] lum,
  		Mat out) {
  		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HLS);
  		Core.inRange(out, new Scalar(hue[0], lum[0], sat[0]),
  			new Scalar(hue[1], lum[1], sat[1]), out);
  	}
  
  	/**
  	 * Sets the values of pixels in a binary image to their distance to the nearest black pixel.
  	 * @param input The image on which to perform the Distance Transform.
  	 * @param type The Transform.
  	 * @param maskSize the size of the mask.
  	 * @param output The image in which to store the output.
  	 */
  	private void findContours(Mat input, boolean externalOnly,
  		List<MatOfPoint> contours) {
  		Mat hierarchy = new Mat();
  		contours.clear();
  		int mode;
  		if (externalOnly) {
  			mode = Imgproc.RETR_EXTERNAL;
  		}
  		else {
  			mode = Imgproc.RETR_LIST;
  		}
  		int method = Imgproc.CHAIN_APPROX_SIMPLE;
  		Imgproc.findContours(input, contours, hierarchy, mode, method);
  	}
  
  
  	/**
  	 * Filters out contours that do not meet certain criteria.
  	 * @param inputContours is the input list of contours
  	 * @param output is the the output list of contours
  	 * @param minArea is the minimum area of a contour that will be kept
  	 * @param minPerimeter is the minimum perimeter of a contour that will be kept
  	 * @param minWidth minimum width of a contour
  	 * @param maxWidth maximum width
  	 * @param minHeight minimum height
  	 * @param maxHeight maximimum height
  	 * @param Solidity the minimum and maximum solidity of a contour
  	 * @param minVertexCount minimum vertex Count of the contours
  	 * @param maxVertexCount maximum vertex Count
  	 * @param minRatio minimum ratio of width to height
  	 * @param maxRatio maximum ratio of width to height
  	 */
  	private void filterContours(List<MatOfPoint> inputContours, double minArea,
  		double minPerimeter, double minWidth, double maxWidth, double minHeight, double
  		maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
  		minRatio, double maxRatio, List<MatOfPoint> output) {
  		final MatOfInt hull = new MatOfInt();
  		output.clear();
  		//operation
  		for (int i = 0; i < inputContours.size(); i++) {
  			final MatOfPoint contour = inputContours.get(i);
  			final Rect bb = Imgproc.boundingRect(contour);
  			if (bb.width < minWidth || bb.width > maxWidth) continue;
  			if (bb.height < minHeight || bb.height > maxHeight) continue;
  			final double area = Imgproc.contourArea(contour);
  			if (area < minArea) continue;
  			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
  			Imgproc.convexHull(contour, hull);
  			MatOfPoint mopHull = new MatOfPoint();
  			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
  			for (int j = 0; j < hull.size().height; j++) {
  				int index = (int)hull.get(j, 0)[0];
  				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
  				mopHull.put(j, 0, point);
  			}
  			final double solid = 100 * area / Imgproc.contourArea(mopHull);
  			if (solid < solidity[0] || solid > solidity[1]) continue;
  			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
  			final double ratio = bb.width / (double)bb.height;
  			if (ratio < minRatio || ratio > maxRatio) continue;
  			output.add(contour);
  		}
  	}
  
  }

  /**
   * Main.
   */
  public static void main(String... args) {
    if (args.length > 0) {
      configFile = args[0];
    }

    // read configuration
    if (!readConfig()) {
      return;
    }

    // start NetworkTables
    NetworkTableInstance ntinst = NetworkTableInstance.getDefault();
    if (server) {
      System.out.println("Setting up NetworkTables server");
      ntinst.startServer();
    } else {
      System.out.println("Setting up NetworkTables client for team " + team);
      ntinst.startClientTeam(team);
    }

    // construct networktables objects
    table = ntinst.getTable("datatable");
    rightXEntry = table.getEntry("rightX");
    rightYEntry = table.getEntry("rightY");
    rightAreaEntry = table.getEntry("rightArea");
    leftXEntry = table.getEntry("leftX");
    leftYEntry = table.getEntry("leftY");
    leftAreaEntry = table.getEntry("leftArea");

    // start cameras
    List<VideoSource> cameras = new ArrayList<>();
    for (CameraConfig cameraConfig : cameraConfigs) {
      cameras.add(startCamera(cameraConfig));
    }

    // start image processing on camera 0 if present
    if (cameras.size() >= 1) {
      // VisionThread visionThread = new VisionThread(cameras.get(0), new MyPipeline(), pipeline -> {
      //   // do something with pipeline results
      // });
      VisionThread visionThread = new VisionThread(cameras.get(0), new GripPipeline(), pipeline -> {
        if (!pipeline.filterContoursOutput().isEmpty()) {
          Point center1;
          Point center2;

          // check that thers 2 things in the array
          if (pipeline.filterContoursOutput().size() != 2) {
            System.out.println("VisionThread Listener: Contours not equal to 2. There are " + pipeline.filterContoursOutput().size() + " countours.");
          }

          else {
            // getting contour info
            Rect r1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
            Rect r2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
            
            // calculate center point of each rect
            center1 = getCenter(r1);
            center2 = getCenter(r2);
            
            // figure out which is right and which is left rect
            if (center1.x < center2.x) {
              sendPoints(center1, center2);
              sendAreas(r1, r2);
            }
            else if(center2.x < center1.x) {
              sendPoints(center2, center1);
              sendAreas(r2, r1);
            }
            else {
              System.out.println("VisionThread Listener: RED ALERT! Code thinks center points are equal!");
            }
          }
        }
      });
      visionThread.start();
      
      // List<MatOfPoint> gripOutput = GripPipelineTest.process();
    }

    // loop forever
    for (;;) {
      try {
        Thread.sleep(10000);
      } catch (InterruptedException ex) {
        return;
      }
    }
  }

  public static Point getCenter (Rect r) {
    Point centerPoint;
    double tlX;
    double tlY;
    double brX;
    double brY;
    double centerX;
    double centerY;

    // get topleft and bottomright points into ints
    tlX = r.tl().x;
    tlY = r.tl().y;
    brX = r.br().x;
    brY = r.br().y;

    // average points to get center
    centerX = (brX - tlX) / 2;
    centerY = (brY - tlY) / 2;

    // construct centerPoint
    centerPoint = new Point(centerX, centerY);
    return centerPoint;
  }

  public static void sendPoints(Point pR, Point pL) {
    rightXEntry.setDouble(pR.x);
    rightYEntry.setDouble(pR.y);
    leftXEntry.setDouble(pL.x);
    leftYEntry.setDouble(pL.y);
  }

  public static void sendAreas(Rect rR, Rect rL) {
    rightAreaEntry.setDouble(rR.area());
    leftAreaEntry.setDouble(rL.area());
  }

  public static void sendCameraResolution() {
    camera.get
  }
}
