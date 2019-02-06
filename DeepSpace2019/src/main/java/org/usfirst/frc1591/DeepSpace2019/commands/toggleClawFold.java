package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

/**
 * 
 */
public class toggleClawFold extends Command {

    public toggleClawFold() {
        
        requires(Robot.claw);

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        if (Robot.claw.clawFoldedToggle && Robot.oi.manipulatorStick.getRawButton(5)) {
            Robot.claw.clawFoldedToggle = false;
            if (Robot.claw.clawFolded) {
                Robot.claw.clawFolded = false;
                Robot.claw.unfoldClaw();
            } else {
                Robot.claw.clawFolded = true;
                Robot.claw.foldClaw();
            }   
        } else if (Robot.oi.manipulatorStick.getRawButton(5) == false) {
            Robot.claw.clawFoldedToggle = true;
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.claw.disableClawFold();
    }

    @Override
    protected void interrupted() {
        end();
    }
}