// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;


/** An example command that uses an example subsystem. */
public class TurnCorrectionCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  Timer timer = new Timer();
  private final DriveSubsystem m_driveSubsystem;
  private final double turnSpeed;
  private final double setpoint;
  private final double tolerance;
  private final double minError;
  private final double maxError;
  private final double stallTime = 0.5;


  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TurnCorrectionCommand(DriveSubsystem driveSubsystem, double turnSpeed, double setpoint, double tolerance) {
    m_driveSubsystem = driveSubsystem;
    this.turnSpeed = turnSpeed;
    this.setpoint = setpoint;
    this.tolerance = tolerance;
    this.minError = this.setpoint - this.tolerance;
    this.maxError = this.setpoint + this.tolerance;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    System.out.println(m_driveSubsystem.getGyroYaw());
    if (m_driveSubsystem.getGyroYaw() > maxError) {
      m_driveSubsystem.setMotor(0.0, turnSpeed);
      timer.reset();
    } else if (m_driveSubsystem.getGyroYaw() < minError) {
      m_driveSubsystem.setMotor(0.0, -turnSpeed);
      timer.reset();
    } else if (timer.get() < stallTime && m_driveSubsystem.getGyroYaw() > minError && m_driveSubsystem.getGyroYaw() < maxError) {
      m_driveSubsystem.setMotor(0.0, 0.0);
    }
    m_driveSubsystem.logDrive("Correcting turn (Automated) ...");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setMotor(0.0, 0.0);
    m_driveSubsystem.logDrive("Stopped - Turn Ended");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(timer.get() > stallTime && m_driveSubsystem.getGyroYaw() > setpoint-tolerance && m_driveSubsystem.getGyroYaw() < setpoint+tolerance){
      return true;
    }
    return false;
  }
}
