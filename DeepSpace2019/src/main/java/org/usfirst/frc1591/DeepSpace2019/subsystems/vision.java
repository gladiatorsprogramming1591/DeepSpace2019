package org.usfirst.frc1591.DeepSpace2019.subsystems;

import org.opencv.core.Rect;

import org.usfirst.frc1591.DeepSpace2019.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class vision extends Subsystem {
        // declare networktables objects
        public static NetworkTableInstance ntinst;
        public static NetworkTable table;
        public static NetworkTableEntry rightXEntry;
        public static NetworkTableEntry rightYEntry;
        public static NetworkTableEntry rightAreaEntry;
        public static NetworkTableEntry leftXEntry;
        public static NetworkTableEntry leftYEntry;
        public static NetworkTableEntry leftAreaEntry;

        // declare variables for network tables data
        public static double rightX;
        public static double rightY;
        public static double rightArea;
        public static double leftX;
        public static double leftY;
        public static double leftArea;

        // camera constants
        public static final double cameraMiddleX = 640; // PLACEHOLDER

    public vision() {
        // start NetworkTables
        ntinst = NetworkTableInstance.getDefault();

        // get table
        table = ntinst.getTable("datatable");

        // get entries
        rightXEntry = table.getEntry("rect");
        rightYEntry = table.getEntry("rect");
        rightAreaEntry = table.getEntry("rect");
        leftXEntry = table.getEntry("rect");
        leftYEntry = table.getEntry("rect");
        leftAreaEntry = table.getEntry("rect");

        System.out.println("VISION STUFF IS HAPPENING");

    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

        // listen for networktables variables
        rightX = rightXEntry.getDouble(0);
        rightY = rightYEntry.getDouble(0);
        rightArea = rightAreaEntry.getDouble(0);
        leftX = leftXEntry.getDouble(0);
        leftY = leftYEntry.getDouble(0);
        leftArea = leftAreaEntry.getDouble(0);
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public boolean checkCentered() {
        boolean centered = false;
        double leftDiff = getLeftDiff();
        double rightDiff = getRightDiff();
        final double CENTER_OFFSET = 20; // amount of pixels off the center line can be in both directions combined

        if (Math.abs(leftDiff - rightDiff) <= CENTER_OFFSET) { // if x values are equal distance from the middle with offset of ?
            centered = true;
        }
        return centered;
    }

    public boolean checkProx() {
        boolean inPosition = false;
        final double targetProx = 200; // PLACEHOLDER

        if (getAvgArea() <= targetProx) {
            inPosition = true;
        }

        return inPosition;
    }

    public double getAvgArea() { // used for finding distnace from target
        return (rightArea + leftArea) / 2;
    }

    public double getStrafeDiff() {
        double leftDiff = getLeftDiff();
        double rightDiff = getRightDiff();

        return leftDiff - rightDiff;
    }
    public double getLeftDiff(){
        return cameraMiddleX - leftX;
    }
    public double getRightDiff(){
        return rightX - cameraMiddleX;
    }

}

