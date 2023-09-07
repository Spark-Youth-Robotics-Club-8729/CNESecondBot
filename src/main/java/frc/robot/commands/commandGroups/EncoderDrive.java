// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.commandGroups;

import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.math.controller.PIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.DoubleSupplier;

/** An example command that uses an example subsystem. */
public class EncoderDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveSubsystem m_driveSubsystem;
  private final DoubleSupplier speedFunction, turnFunction;
  private final double setpoint;
  private final boolean reset;
  private boolean finished;

  PIDController pidDrive = new PIDController(0.0225, 0, 0.01);
  PIDController pidTurn = new PIDController(0.025, 0.0002, 0.015);

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public EncoderDrive(DriveSubsystem driveSubsystem, DoubleSupplier speedFunction, DoubleSupplier turnFunction, double setpoint, boolean reset) {
    this.m_driveSubsystem = driveSubsystem;
    this.speedFunction = speedFunction;
    this.turnFunction = turnFunction;
    this.setpoint = setpoint;
    this.reset = reset;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    finished = false;
    if(reset){
    m_driveSubsystem.resetEncoder();
    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (pidDrive.calculate(m_driveSubsystem.getEncoderDistance(), setpoint) > 0.45) { // actual setpoint is 230
      m_driveSubsystem.setMotor(pidDrive.calculate(m_driveSubsystem.getEncoderDistance(), setpoint), turnFunction.getAsDouble());
    } else if (m_driveSubsystem.getEncoderDistance() < 50) {
      m_driveSubsystem.setMotor(speedFunction.getAsDouble(), turnFunction.getAsDouble());
      System.out.println(m_driveSubsystem.getEncoderDistance() + ", driving");
    }
    else {
      m_driveSubsystem.setMotor(0.0, 0.0);
      finished = true;
    }

    SmartDashboard.putNumber("PID Drive speed", pidDrive.calculate(m_driveSubsystem.getEncoderDistance(), setpoint));
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveSubsystem.setMotor(0.0, 0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
