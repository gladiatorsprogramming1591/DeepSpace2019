package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc1591.DeepSpace2019.Robot;

public class driveManualExp extends Command {

    double targetAngle;

    boolean rocketAnglesMode = false;

    public boolean autoCorrect = true;

    boolean slowMode = false;

    final double slowDivisor = 3.5;

    int targetIndex = 0;

    public driveManualExp() {
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        targetAngle = Robot.driveTrain.getPos();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        final int RESOLUTION = 45;

        if (Robot.oi.driveStick.getRawButtonPressed(1)) {
            slowMode = true;
        }
        else if(Robot.oi.driveStick.getRawButtonPressed(4)) {
            slowMode = false;
        }

        if (Robot.oi.driveStick.getRawButtonPressed(2)) {
            rocketAnglesMode = false;
            targetAngle = Robot.AHRS.getYaw();
            autoCorrect = true;
        }
        else if (Robot.oi.driveStick.getRawButtonPressed(3)) {
            rocketAnglesMode = true;
            targetAngle = Robot.AHRS.getYaw();
            autoCorrect = true;
        }

        if (!Robot.lift.checkPneumatics()) {
            double strafe = Robot.oi.driveStick.getRawAxis(2);
            double vertical = -Robot.oi.driveStick.getRawAxis(3);
            double gyroDeg = Robot.AHRS.getAngle();
            boolean setTargetAngle = false;

            if (Robot.oi.driveStick.getPOV() != -1) {
                targetIndex = Robot.oi.driveStick.getPOV() / RESOLUTION;
            }
            
            if (rocketAnglesMode == false) {
                switch (targetIndex) {
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                        setTargetAngle = true;
                        break;
                }
            }
            else if(rocketAnglesMode == true) {
                switch (targetIndex) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                        setTargetAngle = true;
                        break;
                }
            }
            
            if (setTargetAngle) {
                targetAngle = Robot.driveTrain.Angles.get(targetIndex);
                
                System.out.println("Rocket angles mode: " + rocketAnglesMode + " Setting target index to " + targetIndex);
            }
        
            boolean robotDrive = false;
            if (slowMode == true) {
                autoCorrect = Robot.driveTrain.rotateToPos(strafe / 2.0, vertical / slowDivisor, gyroDeg, targetAngle, autoCorrect, robotDrive);
            }
            else {
                autoCorrect = Robot.driveTrain.rotateToPos(strafe, vertical, gyroDeg, targetAngle, autoCorrect, robotDrive);
            }
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
        Robot.driveTrain.storePos(targetAngle);
        end();
    }
}
