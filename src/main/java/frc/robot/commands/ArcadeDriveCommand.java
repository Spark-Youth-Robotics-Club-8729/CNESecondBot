// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Drive command

package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.DoubleSupplier;

/** An example command that uses an example subsystem. */
public class ArcadeDriveCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveSubsystem m_driveSubsystem;
  private final DoubleSupplier speedFunction, turnFunction;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ArcadeDriveCommand(DriveSubsystem driveSubsystem, DoubleSupplier speedFunction, DoubleSupplier turnFunction) {
    this.m_driveSubsystem = driveSubsystem;
    this.speedFunction = speedFunction;
    this.turnFunction = turnFunction;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    //System.out.println(speedFunction.getAsDouble() + ", " + turnFunction.getAsDouble());
    m_driveSubsystem.setMotor(speedFunction.getAsDouble(), turnFunction.getAsDouble());

    if(speedFunction.getAsDouble() < 0.0 || speedFunction.getAsDouble() > 0.0){
      m_driveSubsystem.logDrive("Driving");
    }
    else if(speedFunction.getAsDouble() == 0.0){
      m_driveSubsystem.logDrive("Stopped");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setMotor(0.0, 0.0);
    m_driveSubsystem.logDrive("Driving Ended");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
