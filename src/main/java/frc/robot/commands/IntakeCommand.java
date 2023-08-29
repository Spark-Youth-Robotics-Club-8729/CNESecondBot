// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Intake set command

package frc.robot.commands;

import frc.robot.subsystems.IntakeSubsystem;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants.IntakeConstants;



/** An example command that uses an example subsystem. */
public class IntakeCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final IntakeSubsystem m_intakeSubsystem;
  private final DoubleSupplier speed;


  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public IntakeCommand(IntakeSubsystem intakeSubsystem, DoubleSupplier speedFunction) {
    this.m_intakeSubsystem = intakeSubsystem;
    this.speed = speedFunction;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if(speed.getAsDouble() < 0.0){

      m_intakeSubsystem.setMotor(speed.getAsDouble());
      m_intakeSubsystem.setStall(true);
      m_intakeSubsystem.logIntake("Intaking");
    }

    else if(speed.getAsDouble() > 0.0){
      m_intakeSubsystem.setMotor(speed.getAsDouble());
      m_intakeSubsystem.setStall(false);
      m_intakeSubsystem.logIntake("Outtaking");
    }

    else if(speed.getAsDouble() == 0.0){

      if(m_intakeSubsystem.getStall() && IntakeConstants.stallAfterIntake){
        m_intakeSubsystem.setMotor(IntakeConstants.intakeStallSpeed);
        m_intakeSubsystem.logIntake("Intake Stalling");
      }
      else{
        m_intakeSubsystem.setMotor(speed.getAsDouble());
  
        m_intakeSubsystem.logIntake("Intake Stopped");
      }
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    
    m_intakeSubsystem.setMotor(0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
