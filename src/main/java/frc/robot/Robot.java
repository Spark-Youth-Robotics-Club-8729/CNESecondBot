// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */

  // drivetrain
  private final WPI_VictorSPX frontLeft = new WPI_VictorSPX(1);
  private final WPI_VictorSPX backLeft = new WPI_VictorSPX(2);
  private final WPI_VictorSPX frontRight = new WPI_VictorSPX(3);
  private final WPI_VictorSPX backRight = new WPI_VictorSPX(4);
  private final MotorControllerGroup leftDrive = new MotorControllerGroup(backLeft, frontLeft);
  private final MotorControllerGroup rightDrive = new MotorControllerGroup(backRight, frontRight);
  private final DifferentialDrive robotDrive = new DifferentialDrive(leftDrive, rightDrive);

  // intake mechanism
  private final CANSparkMax intake = new CANSparkMax(5, MotorType.kBrushless);
  private final CANSparkMax rotation = new CANSparkMax(6, MotorType.kBrushless);

  // joystick
  private final Joystick driverJoystick = new Joystick(0);
  private final Joystick operatorJoystick = new Joystick(1);

  // drive encoder
  private final Encoder leftEncoder = new Encoder(0, 1);

  // gyro
  ADXRS450_Gyro gyro = new ADXRS450_Gyro();

  // pid
  PIDController pidDrive = new PIDController(0.015, 0, 0.01);
  PIDController pidTurn = new PIDController(0.07, 0.0, 0.005);

  int counter;

  Timer timer = new Timer();

  public void robotInit() {
    // drivetrain
    rightDrive.setInverted(true);
    frontLeft.setNeutralMode(NeutralMode.Brake);
    backLeft.setNeutralMode(NeutralMode.Brake);
    frontRight.setNeutralMode(NeutralMode.Brake);
    backRight.setNeutralMode(NeutralMode.Brake);
    leftEncoder.setDistancePerPulse(Math.PI * 6 / 360);
    leftEncoder.setReverseDirection(true);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Encoder Distance", leftEncoder.getDistance());
    SmartDashboard.putNumber("PID Drive speed", pidDrive.calculate(leftEncoder.getDistance(), 50));
    SmartDashboard.putNumber("Pitch", gyro.getAngle());
    SmartDashboard.putNumber("PID Turn speed", -pidTurn.calculate(gyro.getAngle(), 180));
  }

  @Override
  public void autonomousInit() {
    leftEncoder.reset();
    gyro.reset();
    counter = 0;
    timer.reset();
    timer.start();
    intake.set(0.0);
    rotation.set(0.0);
    robotDrive.arcadeDrive(0.0, 0.0);
  }

  @Override
  public void autonomousPeriodic() {

    if (counter == 0) {
      if (timer.get() < 1) {
        intake.set(0.75);
      } else {
        intake.set(0.0);
        timer.reset();
        counter++;
      }
    }

    else if (counter == 1) {
      robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));

      if (timer.get() > 2) {
        gyro.reset();
        leftEncoder.reset();
        robotDrive.arcadeDrive(0.0, 0.0);
        timer.reset();
        counter++;
      }
    }

    else if (counter == 2) {
      // actual field distance is 230
      robotDrive.arcadeDrive(pidDrive.calculate(leftEncoder.getDistance(), 50), 0.0);
      intake.set(-0.4);

      if (timer.get() > 2) {
        intake.set(0.0);
        robotDrive.arcadeDrive(0.0, 0.0);
        leftEncoder.reset();
        gyro.reset();
        timer.reset();
        counter++;
      }
    }

    else if (counter == 3) {
      robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));

      if (timer.get() > 2) {
        gyro.reset();
        leftEncoder.reset();
        robotDrive.arcadeDrive(0.0, 0.0);
        timer.reset();
        counter++;
      }
    }

    else if (counter == 4) {
      // actual field distance is 230
      robotDrive.arcadeDrive(pidDrive.calculate(leftEncoder.getDistance(), 50), 0.0);
      intake.set(-0.4);

      if (timer.get() > 2) {
        intake.set(0.0);
        robotDrive.arcadeDrive(0.0, 0.0);
        leftEncoder.reset();
        gyro.reset();
        timer.reset();
        counter++;
      }
    }

    else if (counter == 5) {
      if (timer.get() < 1) {
        intake.set(0.75);
      } else {
        intake.set(0.0);
        timer.reset();
        counter++;
      }
    }

    else if (counter == 6) {
      robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));

      if (timer.get() > 2) {
        gyro.reset();
        leftEncoder.reset();
        robotDrive.arcadeDrive(0.0, 0.0);
        timer.reset();
        counter++;
      }
    }

    else if (counter == 7) {
      // actual field distance is 230
      robotDrive.arcadeDrive(pidDrive.calculate(leftEncoder.getDistance(), 50), 0.0);
      intake.set(-0.4);

      if (timer.get() > 2) {
        intake.set(0.0);
        robotDrive.arcadeDrive(0.0, 0.0);
        leftEncoder.reset();
        gyro.reset();
        timer.reset();
        counter++;
      }
    }
  }

  @Override
  public void teleopInit() {
    // intializing speeds
    robotDrive.arcadeDrive(0.0, 0.0);
    intake.set(0.0);
    rotation.set(0.0);
  }

  @Override
  public void teleopPeriodic() {
    // Drive command
    robotDrive.arcadeDrive(-driverJoystick.getRawAxis(1), -driverJoystick.getRawAxis(0));
    robotDrive.arcadeDrive(-driverJoystick.getRawAxis(1), driverJoystick.getRawAxis(0));

    // Intake commands
    handleIntakeButton(operatorJoystick);

    // Rotation commands
    handleRotationButton(operatorJoystick);
  }

  private void handleIntakeButton(Joystick joystick) {
    if (joystick.getRawButtonPressed(6)) {
      intake.set(0.4);
    } else if (joystick.getRawButtonReleased(6)) {
      intake.set(0.0);
    } else if (joystick.getRawButtonPressed(8)) {
      intake.set(-0.5);
    } else if (joystick.getRawButtonReleased(8)) {
      intake.set(0.0);
    }
  }

  private void handleRotationButton(Joystick joystick) {
    if (joystick.getRawButtonPressed(5)) {
      rotation.set(0.3);
    } else if (joystick.getRawButtonReleased(5)) {
      rotation.set(0.0);
    } else if (joystick.getRawButtonPressed(7)) {
      rotation.set(-0.3);
    } else if (joystick.getRawButtonReleased(7)) {
      rotation.set(0.0);
    }
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
