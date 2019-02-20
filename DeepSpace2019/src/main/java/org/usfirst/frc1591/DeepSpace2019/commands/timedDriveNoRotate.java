/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc1591.DeepSpace2019.commands;

import org.usfirst.frc1591.DeepSpace2019.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class timedDriveNoRotate extends Command {
  private double m_timeout;
  private double m_speed;
  final double DIR_FWD = 0;
  final double NO_ROTATE = 0;

  public timedDriveNoRotate(double time, double speed) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    m_timeout = time;
    m_speed = speed;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    setTimeout(m_timeout);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.driveTrain.timedDrive(m_speed, DIR_FWD, NO_ROTATE);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return isTimedOut();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
