// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.controllerConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.RotationConstants;
import frc.robot.Constants.AutoConstants;

import frc.robot.commands.AutoCommands.commandGroups.CubeMobilityTimedAuto;
import frc.robot.commands.AutoCommands.commandGroups.CubeMobilityEncoderAuto;
import frc.robot.commands.AutoCommands.commandGroups.CubeEngageEncoderAuto;
import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.RotationCommand;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.RotationSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final RotationSubsystem rotationSubsystem = new RotationSubsystem();

  private final Joystick driverController = new Joystick(controllerConstants.kDriverControllerPort);
  private final Joystick operatorController = new Joystick(controllerConstants.kOperatorControllerport);

  SendableChooser<Command> chooser = new SendableChooser<>();

  // Replace with CommandPS4Controller or CommandJoystick if needed

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    driveSubsystem.setDefaultCommand(
        new ArcadeDriveCommand(driveSubsystem,
            () -> -driverController.getRawAxis(DriveConstants.kDriveAxis),
            () -> -driverController.getRawAxis(DriveConstants.kTurnAxis)));

    intakeSubsystem.setDefaultCommand(
        new IntakeCommand(intakeSubsystem,
            () -> (operatorController.getRawAxis(DriveConstants.outtakeButton) * IntakeConstants.outtakeProportions)) // outtake
    );

    rotationSubsystem.setDefaultCommand(
        new RotationCommand(rotationSubsystem,
            () -> (-operatorController.getRawAxis(DriveConstants.rotationDownButton) * RotationConstants.rotationDownProportions) // rotation down
        ));

    chooser.setDefaultOption("Cube + Mobility Timed",
        new CubeMobilityTimedAuto(driveSubsystem, intakeSubsystem, rotationSubsystem));
    chooser.addOption("Cube + Engage Timed",
        new CubeMobilityEncoderAuto(driveSubsystem, intakeSubsystem, rotationSubsystem));
    chooser.addOption("2 Cube + Mobility Encoder",
        new CubeEngageEncoderAuto(driveSubsystem, intakeSubsystem, rotationSubsystem));
    chooser.addOption("Cube + Engage Encoder",
        new CubeEngageEncoderAuto(driveSubsystem, intakeSubsystem, rotationSubsystem));

    Shuffleboard.getTab("H202-CMD-BASED").add(chooser);

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    // Intake
    new JoystickButton(operatorController, DriveConstants.intakeButton)
        .whileTrue(new IntakeCommand(intakeSubsystem, () -> (IntakeConstants.intakeSpeed))); // in

    // Rotation
    new JoystickButton(operatorController, DriveConstants.rotationUpButton)
        .whileTrue(new RotationCommand(rotationSubsystem, () -> RotationConstants.rotationUpSpeed)); // up
  }

  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return chooser.getSelected();
  }
}
