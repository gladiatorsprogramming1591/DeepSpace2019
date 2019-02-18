package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc1591.DeepSpace2019.Robot;

public class driveManualExp extends Command {

    double targetAngle = 0;
    double currentPOV = 0;

    public driveManualExp() {
        targetAngle = 0;
        requires(Robot.driveTrain);
    }

    public driveManualExp(double target) {
        targetAngle = target;
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (!Robot.lift.checkPneumatics()) {
            double strafe = Robot.oi.driveStick.getRawAxis(2);
            double vertical = -Robot.oi.driveStick.getRawAxis(3);
            double gyroDeg = Robot.AHRS.getAngle();
            
            
            if (Robot.oi.driveStick.getPOV() != currentPOV && Robot.oi.driveStick.getPOV()!= -1){
                int targetIndex = Robot.oi.driveStick.getPOV() / 45;
                targetAngle = Robot.driveTrain.Angles.get(targetIndex);
                currentPOV = targetIndex * 45;
            }
        
            Robot.driveTrain.rotateToPos(strafe, vertical, gyroDeg, targetAngle);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.driveTrain.fieldDrive(0, 0, 0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
