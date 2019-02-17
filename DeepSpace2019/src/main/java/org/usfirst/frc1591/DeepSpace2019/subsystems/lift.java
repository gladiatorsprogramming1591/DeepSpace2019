package org.usfirst.frc1591.DeepSpace2019.subsystems;
import org.usfirst.frc1591.DeepSpace2019.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class lift extends Subsystem {

    private DoubleSolenoid frontPistons;
    private DoubleSolenoid backPistons;
    private Spark liftMotor; 
    private AnalogInput frontRangeFinder;
    private AnalogInput rearRangeFinder;

    public lift() {
        frontPistons = new DoubleSolenoid(5, 0, 1);
        addChild("frontPistons",frontPistons);
        
        backPistons = new DoubleSolenoid(5, 2, 3);
        addChild("backPistons",backPistons);
        
        liftMotor = new Spark(0);
        addChild("liftMotor",liftMotor);
        liftMotor.setInverted(false);

        frontRangeFinder = new AnalogInput(1);
        addChild("frontRangeFinder",frontRangeFinder);

        rearRangeFinder = new AnalogInput(2);
        addChild("rearRangeFinder",rearRangeFinder);
        
        unliftFront();
        unliftRear();
    }

    @Override
    public void initDefaultCommand() {
    }

    @Override
    public void periodic() {
    }

    public boolean frontExtended = false;
    public boolean rearExtended = false;
    
    public void liftFront() {
        frontPistons.set(DoubleSolenoid.Value.kForward);
    }
    
    public void liftRear() {
        backPistons.set(DoubleSolenoid.Value.kForward);
    }
    
    public void unliftFront() {
        frontPistons.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void unliftRear() {
        backPistons.set(DoubleSolenoid.Value.kReverse);
    }

    public void disableFront() {
        frontPistons.set(DoubleSolenoid.Value.kOff);
    }
   
    public void disableRear() {
        backPistons.set(DoubleSolenoid.Value.kOff);
    }
    
    public void turnWheel() {
        liftMotor.set(.6); //assumes positive is forward
    }
   
    public void stopWheel(){
        liftMotor.set(0);
    }
    
    public void checkPneumatics(){
        if (rearExtended){
            turnWheel(); 
        }
    }

    public double getFrontHeight(){
        return frontRangeFinder.getValue();
    }

    public double getRearHeight(){
        return rearRangeFinder.getValue();
    }
}


