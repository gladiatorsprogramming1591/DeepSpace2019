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

    public final float OFFSET = 2; // PLACEHOLDER

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

    public void rotateToPos(double strafe_, double vertical_, double gyroDeg_, double targetAngle_) {
        double rotation = 0;
        double current = Robot.AHRS.getYaw();
        // System.out.println("strafe_ " + strafe_ + " vertical_ " + vertical_ + " gyroDeg_ " + gyroDeg_ + " targetAngle_ " + targetAngle_ + " current " + current);

        if (Math.abs(Robot.oi.driveStick.getX()) > 0.1){
            rotation = Robot.oi.driveStick.getX();
            // System.out.println("Using joystick. Rotation = " + rotation);
        }
        else {
            double distanceToTarget;
            if (targetAngle_ > current) {
                distanceToTarget = targetAngle_ - current;
            }
            else {
                distanceToTarget = current - targetAngle_;
            }

            // if equal to target angle with offset
            if ( distanceToTarget < OFFSET) {
                rotation = 0;
                // System.out.println("Within offset. Not moving.");
            }
            else {
                // move in the direction we need to get there
                if (current >= 0){
                    double temp = current -180;
                    if(temp < targetAngle_ && targetAngle_ < current){
                        rotation = -0.25;
                        // System.out.println("Moving 1");
                    } 
                    else {
                        rotation = 0.25;
                        // System.out.println("Moving 2");
                    }
                } 
                else {
                    double temp = current + 180;
                    if (current < targetAngle_ && targetAngle_ < temp){
                        rotation = 0.25;
                        // System.out.println("Moving 3");
                    } 
                    else {
                        rotation = -0.25;
                        // System.out.println("Moving 4");
                    }
                }
            }
        }
        Robot.driveTrain.fieldDrive(strafe_, vertical_, rotation, -gyroDeg_);
    }
}

