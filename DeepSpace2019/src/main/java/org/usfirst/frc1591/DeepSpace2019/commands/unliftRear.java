package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class unliftRear extends Command {
    public unliftRear() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        setTimeout(1);
        Robot.lift.unliftRear();
        Robot.lift.rearExtended = false;
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
        // Robot.lift.disableRear();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
