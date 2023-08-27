// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands.commandGroups;

import frc.robot.commands.AutoCommands.TurnCorrectionCommand;
import frc.robot.commands.AutoCommands.TurnErrorCommand;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


/** An example command that uses an example subsystem. */
public class TurnSetpointCommand extends SequentialCommandGroup {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  double setpoint, initialSpeed, initialTolerance, correctionSpeed, correctionTolerance;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public TurnSetpointCommand(DriveSubsystem driveSubsystem) {

    addCommands(
    new TurnErrorCommand(driveSubsystem, -0.80 ,180 ,3),
    new TurnCorrectionCommand(driveSubsystem, 0.5, 180, 0.5));
  }
}
