package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class unliftFront extends Command {
    public unliftFront() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        setTimeout(1);
        Robot.lift.unliftFront();
        Robot.lift.frontExtended = false;
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
        // Robot.lift.disableFront();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
