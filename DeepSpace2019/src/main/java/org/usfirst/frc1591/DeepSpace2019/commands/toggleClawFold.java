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
        setTimeout(1);
        if (Robot.claw.clawFolded == true) { // checks to see if the claw is folded.
            Robot.claw.clawFolded = false;
            Robot.claw.unfoldClaw();
        } else {
            Robot.claw.clawFolded = true;
            Robot.claw.foldClaw();
        }   
    }

    @Override
    protected void execute() {
       
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }
}