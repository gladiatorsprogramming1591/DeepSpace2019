package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

/**
 * 
 */
public class toggleClaw extends Command {

    public toggleClaw() {
        
        requires(Robot.claw);

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        if (Robot.claw.clawOpenToggle && Robot.oi.manipulatorStick.getRawButton(6)) {
            Robot.claw.clawOpenToggle = false; // Prevents the code from being called again until the Button is released and re-pressed
            if (Robot.claw.clawOpen) { // checks to see if the claw is folded.
                Robot.claw.clawOpen = false;
                Robot.claw.closeClaw();
            } else {
                Robot.claw.clawOpen = true;
                Robot.claw.openClaw();
            }   
        } else if (Robot.oi.manipulatorStick.getRawButton(6) == false) {
            Robot.claw.clawOpenToggle = true; // Registers that the Button has been released and allows for the code to be run again.
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.claw.disableClaw();
    }

    @Override
    protected void interrupted() {
        end();
    }
}