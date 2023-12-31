// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands.commandGroups;

import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RotationSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


/** An example command that uses an example subsystem. */
public class CubeEngageTimedAuto extends SequentialCommandGroup {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public CubeEngageTimedAuto(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem, RotationSubsystem rotationSubsystem) {

    addCommands(
      new IntakeCommand(intakeSubsystem, () -> 0.95).withTimeout(0.6),
      new ArcadeDriveCommand(driveSubsystem, () -> -0.65, () -> 0.0).withTimeout(2.0),
      new ArcadeDriveCommand(driveSubsystem, () -> -0.50, () -> 0.0).withTimeout(0.6)
      );

  }
}
