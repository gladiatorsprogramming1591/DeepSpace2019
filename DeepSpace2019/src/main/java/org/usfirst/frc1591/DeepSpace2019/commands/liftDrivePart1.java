package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class liftDrivePart1 extends Command {
    final double highFrontHeight = 70.0;

    public liftDrivePart1() {
        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        Robot.lift.checkPneumatics();
        //Robot.lift.getFrontHeight();
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return Robot.lift.getFrontHeight() < highFrontHeight;
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
