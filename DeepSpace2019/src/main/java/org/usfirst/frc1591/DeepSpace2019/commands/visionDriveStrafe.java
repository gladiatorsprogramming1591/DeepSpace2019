package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc1591.DeepSpace2019.Robot;

public class visionDriveStrafe extends Command {

    boolean isFinished = false;

    public visionDriveStrafe() {
        requires(Robot.driveTrain);
        requires(Robot.vision);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(Robot.vision.checkCentered()) {
            isFinished = true;
            Robot.driveTrain.fieldDrive(0, 0, 0, 0); // stop the robot, should eventually be robot oriented
        }
        else {
            double strafeSpeed;
            double STRAFE_SPEED_LIMIT = 0.5;

            strafeSpeed = Math.cbrt(Robot.vision.getStrafeDiff());

            if (strafeSpeed > STRAFE_SPEED_LIMIT){
                strafeSpeed = STRAFE_SPEED_LIMIT;
            }
            else if(strafeSpeed < -STRAFE_SPEED_LIMIT) {
                strafeSpeed = -STRAFE_SPEED_LIMIT;
            }

            // set robot oriented drive strafeSpeed, 0 vert, rotation based on rotate to pos to maintain target pos
        }
        
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return isFinished;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        // robot oriented drive stop
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
