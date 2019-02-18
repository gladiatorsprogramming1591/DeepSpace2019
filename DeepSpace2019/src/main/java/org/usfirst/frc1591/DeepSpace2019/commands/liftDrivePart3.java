package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class liftDrivePart3 extends Command {
    public liftDrivePart3() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        Robot.lift.unliftRear();
        setTimeout(1.0);
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
        Robot.lift.disableRear();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
