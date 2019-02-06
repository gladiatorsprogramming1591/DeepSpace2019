package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

/**
 * 
 */
public class toggleClawFold extends Command { // toggles whether the claw is folded up or not

    public toggleClawFold() {
        
        requires(Robot.claw);

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        if (Robot.claw.clawFoldedToggle && Robot.oi.manipulatorStick.getRawButton(5)) {
            Robot.claw.clawFoldedToggle = false; // Prevents the code from being called again until the Button is released and re-pressed.
            if (Robot.claw.clawFolded == true) { // checks to see if the claw is folded.
                Robot.claw.clawFolded = false;
                Robot.claw.unfoldClaw();
            } else {
                Robot.claw.clawFolded = true;
                Robot.claw.foldClaw();
            }   
        } else if (Robot.oi.manipulatorStick.getRawButton(5) == false) {
            Robot.claw.clawFoldedToggle = true; // Registers that the Button has been released and allows for the code to be run again.
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