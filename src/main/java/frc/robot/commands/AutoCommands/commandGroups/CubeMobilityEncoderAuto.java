// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands.commandGroups;

import frc.robot.commands.AutoCommands.EncoderDrive;
import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.RotationCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RotationSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;


/** An example command that uses an example subsystem. */
public class CubeMobilityEncoderAuto extends SequentialCommandGroup {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public CubeMobilityEncoderAuto(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem, RotationSubsystem rotationSubsystem) {

    addCommands(
      new IntakeCommand(intakeSubsystem, () -> 0.95).withTimeout(0.6),
      new EncoderDrive(driveSubsystem, () -> -0.70, () -> 0.0, 45, true),
      new ParallelCommandGroup(
        new TurnSetpointCommand(driveSubsystem)
        //new RotationCommand(rotationSubsystem, () -> -0.4).withTimeout(0.4)
      ),
      new ParallelCommandGroup(
        new EncoderDrive(driveSubsystem, () -> -0.65, () -> 0.0, 5.0, true),
        new IntakeCommand(intakeSubsystem, () -> -0.9).withTimeout(2)
      ),
      new TurnSetpointCommand(driveSubsystem),
      new EncoderDrive(driveSubsystem, () -> 0.70, () -> 0.0, 50.0, true),
      new IntakeCommand(intakeSubsystem, () -> 0.95).withTimeout(0.6),
      new ArcadeDriveCommand(driveSubsystem,  () -> 0.80, () -> 0.0).withTimeout(3),
      new TurnSetpointCommand(driveSubsystem)
      );
  }
}
