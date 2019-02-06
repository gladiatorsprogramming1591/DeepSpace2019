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
            Robot.claw.clawOpenToggle = false;
            if (Robot.claw.clawOpen) {
                Robot.claw.clawOpen = false;
                Robot.claw.closeClaw();
            } else {
                Robot.claw.clawOpen = true;
                Robot.claw.openClaw();
            }   
        } else if (Robot.oi.manipulatorStick.getRawButton(6) == false) {
            Robot.claw.clawOpenToggle = true;
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