// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */

public final class Constants {

  public static class controllerConstants {
    public static final int kDriverControllerPort = 1;
    public static final int kOperatorControllerport = 2;
  }

  /****************************************************************/
  public static class DriveConstants {

    // Drive Ports/ID
    public static final int FRONT_LEFT = 1;
    public static final int BACK_LEFT = 2;
    public static final int FRONT_RIGHT = 3;
    public static final int BACK_RIGHT = 4;

    public static final int encoderChannelA = 2;
    public static final int encoderChannelB = 3;
    public static final double encoderProportions = Math.PI * 6 / 360;

    // Driver Constants
    public static final int kDriveAxis = 1;
    public static final int kTurnAxis = 4;

    public static final double driveProportions = 1.0; // drivespeed multiplier
    public static final double turnProportions = 1.0; // turn speed multiplier

    // Operator Constants
    // outtake speed = axis * outtakeProportions
    public static final int outtakeButton = 3; // THIS IS AN AXIS.

    public static final int intakeButton = 6;

    public static final int rotationUpButton = 5;

    // rotation down speed = axis * rotationDownProportions
    public static final int rotationDownButton = 2; // THIS IS AN AXIS

  }

  /****************************************************************/
  public static class IntakeConstants {

    // Intake motor ports/ID
    public static final int INTAKE_MOTOR_ID = 5;

    // teleop intake speeds
    public static final double outtakeProportions = 2.0;
    public static final double intakeSpeed = -0.95;

  }

  /****************************************************************/
  public static class RotationConstants {

    // Rotation motor ports/ID
    public static final int ROTATION_MOTOR_ID = 6;

    // teleop rotation speeds
    public static final double rotationUpSpeed = 0.65;
    public static final double rotationDownProportions = 0.17;
  }

  /****************************************************************/
  public static class AutoConstants {

  }

}
