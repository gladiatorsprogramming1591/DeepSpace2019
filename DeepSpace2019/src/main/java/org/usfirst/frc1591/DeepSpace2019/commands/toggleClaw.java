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
        setTimeout(1);
        if (Robot.claw.clawOpen == true) { // checks to see if the claw is open.
            Robot.claw.clawOpen = false;
            Robot.claw.closeClaw();
        } else {
            Robot.claw.clawOpen = true;
            Robot.claw.openClaw();
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