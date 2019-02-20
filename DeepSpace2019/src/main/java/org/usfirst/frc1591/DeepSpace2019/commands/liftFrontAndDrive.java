package org.usfirst.frc1591.DeepSpace2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1591.DeepSpace2019.Robot;

public class liftFrontAndDrive extends Command {
    private double m_speed;
    private double m_time;

    public liftFrontAndDrive(double speed, double time) {
        requires(Robot.lift);
        m_speed = speed;
        m_time = time;
    }

    @Override
    protected void initialize() {
        setTimeout(m_time);
        Robot.lift.liftFrontAndDrive(m_speed);
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
        end();
    }
}
