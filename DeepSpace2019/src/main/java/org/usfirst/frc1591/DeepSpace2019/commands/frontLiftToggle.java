package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class frontLiftToggle extends Command {
    public frontLiftToggle() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        setTimeout(1);
        if (Robot.lift.frontExtended == true) {
            Robot.lift.frontExtended = false;
            Robot.lift.unliftFront();
            }
            else {
                Robot.lift.frontExtended = true;
                Robot.lift.liftFront();
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
        Robot.lift.disableFront();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
