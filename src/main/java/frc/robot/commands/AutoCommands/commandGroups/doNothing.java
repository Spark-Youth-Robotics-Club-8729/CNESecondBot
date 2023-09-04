// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoCommands.commandGroups;

import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.RotationCommand;
import frc.robot.commands.AutoCommands.TurnCorrectionCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RotationSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;


/** An example command that uses an example subsystem. */
public class doNothing extends SequentialCommandGroup {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  /**
   * Creates a new ExampleCommand.
   * 
   * @param subsystem The subsystem used by this command.
   */
  public doNothing(DriveSubsystem driveSubsystem, IntakeSubsystem intakeSubsystem, RotationSubsystem rotationSubsystem) {


     // intake = negative, outtake = positive
    addCommands(
      new ParallelCommandGroup(
        //new RotationCommand(rotationSubsystem, () -> 0.6).withTimeout(0.8),
        new ArcadeDriveCommand(driveSubsystem, () -> -0.85, () -> 0.0).withTimeout(0.3) // drive backwards, push cone into node. Intake falls down at the same time
      )
    );
  }
}
