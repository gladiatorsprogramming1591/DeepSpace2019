package org.usfirst.frc1591.DeepSpace2019.subsystems;


import org.usfirst.frc1591.DeepSpace2019.Robot;
import org.usfirst.frc1591.DeepSpace2019.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class driveTrain extends Subsystem {

    private WPI_TalonSRX lFmotor;
    private WPI_TalonSRX rFmotor;
    private WPI_TalonSRX rBmotor;
    private WPI_TalonSRX lBmotor;
    private MecanumDrive mecanumDrive;
    private AnalogInput utrasonic;

    public ArrayList<Double> Angles = new ArrayList<Double>();

    public final float OFFSET = 1; // PLACEHOLDER
    public double storedPosition;

    public driveTrain() {
        lFmotor = new WPI_TalonSRX(0);
        lFmotor.setNeutralMode(NeutralMode.Brake);
        

        rFmotor = new WPI_TalonSRX(1);
        rFmotor.setNeutralMode(NeutralMode.Brake);
        
        
        rBmotor = new WPI_TalonSRX(2);
        rBmotor.setNeutralMode(NeutralMode.Brake);
        
        
        lBmotor = new WPI_TalonSRX(3);
        lBmotor.setNeutralMode(NeutralMode.Brake);
        
        
        mecanumDrive = new MecanumDrive(lFmotor, lBmotor,
              rFmotor, rBmotor);
        addChild("mecanumDrive",mecanumDrive);
        mecanumDrive.setSafetyEnabled(true);
        mecanumDrive.setExpiration(0.5);
        mecanumDrive.setMaxOutput(1.0);

        
        utrasonic = new AnalogInput(0);
        addChild("utrasonic",utrasonic);
        
        
        //even index = right angles
        //odd index = rocket angles
        Angles.add(0.0); //0
        Angles.add(28.75); //1
        Angles.add(90.0); //2
        Angles.add(151.25); //3
        Angles.add(180.0); //4
        Angles.add(-151.25); //5
        Angles.add(-90.0); //6
        Angles.add(-28.75); //7

        Robot.AHRS.reset();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new driveManualExp());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    public void fieldDrive(double strafe, double vertical, double rotation, double gyroDeg){
        mecanumDrive.driveCartesian(strafe, vertical, rotation , gyroDeg);
        // System.out.println("Strafe :" + strafe + "vertical :" + vertical + "rotation :" + rotation + "Gyro degrees :" + gyroDeg);
    }

    public void slowfieldDrive(double strafe, double vertical, double rotation, double gyroDeg){
        mecanumDrive.driveCartesian(strafe / 2, vertical / 2, rotation / 3, gyroDeg); // PLACEHOLDER
    }

    public void timedDrive(double speed, double direction, double rotation){
        mecanumDrive.drivePolar(speed, direction, rotation);
    }

    public void resetGyro() {
        Robot.AHRS.reset();
    }

    public void robotDrive(double strafe, double vertical, double rotation){
        mecanumDrive.driveCartesian(strafe, vertical, rotation);
    }

    public void storePos(double TAngle_){
        storedPosition = TAngle_;
    }

    public double getPos(){
        return storedPosition;
    }

    public boolean rotateToPos(double strafe_, double vertical_, double gyroDeg_, double targetAngle_, boolean autoCorrect_, boolean Robotmode_) {
        double rotation = 0;
        double current = Robot.AHRS.getYaw();

        // System.out.println("strafe_ " + strafe_ + " vertical_ " + vertical_ + " gyroDeg_ " + gyroDeg_ + " targetAngle_ " + targetAngle_ + " current " + current);

        if (Math.abs(Robot.oi.driveStick.getX()) > 0.1){ // if joystick is being pushed
            rotation = Robot.oi.driveStick.getX();
            autoCorrect_ = false;
            // System.out.println("Using joystick. Rotation = " + rotation);
        }
        else {
            int direction = 0;
            double distanceToTargetABS;
            double distanceToTarget = 0;
            distanceToTargetABS = Math.abs(targetAngle_ - current);

            if ((targetAngle_ == 180.0) && (current < 0) && (distanceToTargetABS > (360-OFFSET))) {
                rotation = 0;
            }

            // if equal to target angle with offset
            else if ( distanceToTargetABS < OFFSET) {
                rotation = 0;
                // System.out.println("Within offset. Not moving.");
            }
            else if(autoCorrect_ == true) {
                // move in the direction we need to get there
                if (current >= 0){ // if in positive hemisphere
                    double temp = current -180;
                    if(temp < targetAngle_ && targetAngle_ < current){
                        direction = -1;
                    } 
                    else {
                        direction = 1;
                    }
                } 
                else { // if in negative hemisphere
                    double temp = current + 180;
                    if (current < targetAngle_ && targetAngle_ < temp){
                        direction = 1;
                    } 
                    else {
                        direction = -1;
                    }
                }
            }
            
            switch (direction) {
                case 1:
                    distanceToTarget = distanceToTargetABS;
                    break;
                case -1:
                    distanceToTarget = -distanceToTargetABS;
                    break;
            }

            rotation = 0.1 * (Math.cbrt(1.0 * distanceToTarget));
        }

        if(Robotmode_){
            Robot.driveTrain.robotDrive(strafe_, vertical_, rotation);
        } else if (!Robotmode_){
        Robot.driveTrain.fieldDrive(strafe_, vertical_, rotation, -gyroDeg_);
        }
        return autoCorrect_;
    }
}

