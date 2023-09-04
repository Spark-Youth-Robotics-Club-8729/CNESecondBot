// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands;

import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;


/** An example command that uses an example subsystem. */
public class TurnErrorCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  Timer timer = new Timer();
  private final DriveSubsystem m_driveSubsystem;
  private final double turnSpeed;
  private final double setpoint;
  private final double tolerance;
  private final double minError;


  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TurnErrorCommand(DriveSubsystem driveSubsystem, double turnSpeed, double setpoint, double tolerance) {
    m_driveSubsystem = driveSubsystem;
    this.turnSpeed = turnSpeed;
    this.setpoint = setpoint;
    this.tolerance = tolerance;
    this.minError = this.setpoint - this.tolerance;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
    m_driveSubsystem.resetGyro(); // resets ONCE when the TurnSetpoint command group is called
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      m_driveSubsystem.setMotor(0.0, turnSpeed);
      m_driveSubsystem.logDrive("Turning (Automated)");

      System.out.println(m_driveSubsystem.getGyroYaw());
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setMotor(0.0, 0.0);
    m_driveSubsystem.logDrive("Stopped - Turning Ended");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {   if(Math.abs(m_driveSubsystem.getGyroYaw()) > Math.abs(minError)){
      return true;
    }
    else{
      return false;
    }
  }
}
