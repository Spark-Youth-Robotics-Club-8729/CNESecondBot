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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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
  private final Joystick driverJoystick = new Joystick(2);
  private final Joystick operatorJoystick = new Joystick(1);

  // drive encoder
  private final Encoder leftEncoder = new Encoder(2, 3);

  // gyro
  ADXRS450_Gyro gyro = new ADXRS450_Gyro();

  // pid
  PIDController pidDrive = new PIDController(0.0225, 0, 0.01);
  PIDController pidTurn = new PIDController(0.025, 0.0002, 0.015);

  int counter;
  int counter2;

  int intakeButton;
  int outtakeButton;

  int rotationUpButton;
  int rotationDownButton;

  Timer timer = new Timer();

  // auto routes
  private static final String defaultAuto = "2 Cargo Auto";
  private static final String customAuto = "Balance Auto";
  private static final String newAuto = "1 cargo Timed Auto";

  private String autoSelected;
  private final SendableChooser<String> chooser = new SendableChooser<>();

  public void robotInit() {
    // drivetrain
    rightDrive.setInverted(true);
    frontLeft.setNeutralMode(NeutralMode.Brake);
    backLeft.setNeutralMode(NeutralMode.Brake);
    frontRight.setNeutralMode(NeutralMode.Brake);
    backRight.setNeutralMode(NeutralMode.Brake);
    leftEncoder.setDistancePerPulse(Math.PI * 6 / 360);
    leftEncoder.setReverseDirection(true);
    pidTurn.setTolerance(3);

    // auto chooser
    chooser.setDefaultOption("1 cargo Timed Auto", newAuto);
    chooser.addOption("2 Cargo Auto", defaultAuto);
    chooser.addOption("Balance Auto", customAuto);

    SmartDashboard.putData("Auto Choices", chooser);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Encoder Distance", leftEncoder.getDistance());
    SmartDashboard.putNumber("PID Drive speed", pidDrive.calculate(leftEncoder.getDistance(), 50));
    SmartDashboard.putNumber("Pitch", gyro.getAngle());
    SmartDashboard.putNumber("PID Turn speed", -pidTurn.calculate(gyro.getAngle(), 180));
    SmartDashboard.putData(pidTurn);
    System.out.println(leftEncoder.getDistance());
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
    counter2 = 0;

    autoSelected = chooser.getSelected();
  }

  @Override
  public void autonomousPeriodic() {

    System.out.println(leftEncoder.getDistance());

    if (autoSelected == newAuto) {
      cubeMobilityTimed();
    }

    if (autoSelected == defaultAuto) {
      twoCubeMobilityEncoder();
    }
    if (autoSelected == customAuto) {
      cubeEngageEncoder();
    }
  }

  public void cubeMobilityTimed() {
    if (counter == 0) {
      if (timer.get() < 1) {
        intake.set(0.4); // CHANGE TO 0.95
      } else {
        resetAll();
        timer.reset();
        counter++;
      }
    }

    else if (counter == 1) {

      if (timer.get() < 0.5) { // CHANGE AT COMP
        robotDrive.arcadeDrive(-0.45, 0.0); // CHANGE THIS
      } else {
        resetAll();
        counter++;
      }
    }

    else if (counter == 2) {
      // robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));
      if (counter2 == 0) {

        if (timer.get() < 0.4) {
          rotation.set(-0.4);
        } else {
          rotation.set(0.0);
        }
        if (gyro.getAngle() < 176) {
          robotDrive.arcadeDrive(0.0, -0.70);
        } else {
          counter2++;
        }
      }
      if (counter2 == 1) {
        if (gyro.getAngle() > 180.5) {
          robotDrive.arcadeDrive(0.0, 0.5);
          timer.reset();
        } else if (gyro.getAngle() < 179.5) {
          robotDrive.arcadeDrive(0.0, -0.5);
          timer.reset();
        } else if (timer.get() < 0.5 && gyro.getAngle() > 179.5 && gyro.getAngle() < 180.5) {
          robotDrive.arcadeDrive(0.0, 0.0);

        }

        if (timer.get() > 0.5 && gyro.getAngle() > 179.5 && gyro.getAngle() < 180.5) {
          resetAll();
          counter++;
        }

      }
    }
  }

  public void twoCubeMobilityEncoder() {
    if (counter == 0) {
      if (timer.get() < 1) {
        intake.set(0.4); // CHANGE TO 0.95
      } else {
        resetAll();
        counter++;
      }
    }

    else if (counter == 1) {
      // robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));
      if (counter2 == 0) {

        if (timer.get() < 0.2) {
          rotation.set(-0.3);
        } else {
          rotation.set(0.0);
        }
        if (gyro.getAngle() < 177) {
          robotDrive.arcadeDrive(0.0, -0.80);
        } else {
          counter2++;
        }
      }
      if (counter2 == 1) {
        if (gyro.getAngle() > 180.5) {
          robotDrive.arcadeDrive(0.0, 0.5);
          timer.reset();
        } else if (gyro.getAngle() < 179.5) {
          robotDrive.arcadeDrive(0.0, -0.5);
          timer.reset();
        } else if (timer.get() < 0.5 && gyro.getAngle() > 179.5 && gyro.getAngle() < 180.5) {
          robotDrive.arcadeDrive(0.0, 0.0);

        }

        if (timer.get() > 0.5 && gyro.getAngle() > 179.5 && gyro.getAngle() < 180.5) {
          resetAll();
          counter++;
        }

      }
    }

    else if (counter == 2) {
      // actual field distance is 230
      /* */
      if (pidDrive.calculate(leftEncoder.getDistance(), 50) > 0.45) {
        robotDrive.arcadeDrive(pidDrive.calculate(leftEncoder.getDistance(), 50), 0.0);
      } else if (leftEncoder.getDistance() < 50) {
        robotDrive.arcadeDrive(0.45, 0.0);
        System.out.println(leftEncoder.getDistance() + ", driving");
      }

      else {
        robotDrive.arcadeDrive(0.0, 0.0);
      }

      if (timer.get() < 2) {
        intake.set(-0.5);
      } else {
        intake.set(0.0);
      }

      if (timer.get() > 2) {
        resetAll();
        timer.reset();
        counter++;
      }
    }

    else if (counter == 3) {
      // robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));
      if (counter2 == 0) {

        if (gyro.getAngle() > 3.0) {
          robotDrive.arcadeDrive(0.0, 0.80);
        } else {
          counter2++;
          System.out.println("added counter2");
        }
      }
      if (counter2 == 1) {
        System.out.println("Entered turn 2");
        if (gyro.getAngle() > 0.5) {
          robotDrive.arcadeDrive(0.0, 0.52); // f
          timer.reset();
        } else if (gyro.getAngle() < -0.5) {
          robotDrive.arcadeDrive(0.0, -0.52);
          timer.reset();
        } else if (timer.get() < 0.5 && gyro.getAngle() > -0.5 && gyro.getAngle() < 0.5) {
          robotDrive.arcadeDrive(0.0, 0.0);
        }

        if (timer.get() > 0.5 && gyro.getAngle() > -0.5 && gyro.getAngle() < 0.5) {
          resetAll();
          counter2 = 0;
        }
      }
    } else if (counter == 4) {
      // actual field distance is 230
      System.out.println("Entered 4");
      if (pidDrive.calculate(leftEncoder.getDistance(), 50) > 0.45) {
        robotDrive.arcadeDrive(pidDrive.calculate(leftEncoder.getDistance(), 50), 0.0);
      } else if (leftEncoder.getDistance() < 50) {
        robotDrive.arcadeDrive(0.45, 0.0);
      }

      if (timer.get() > 3) {
        resetAll();
        counter++;
      }
    }

    else if (counter == 5) {

      if (timer.get() < 1) {
        intake.set(0.4);
      } else {
        resetAll();
        counter++;
      }
    }

    else if (counter == 6) {
      // robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));

      if (counter2 == 0) {

        if (gyro.getAngle() < 177) {
          robotDrive.arcadeDrive(0.0, -0.80);
        } else {
          counter2++;
        }

      }
      if (counter2 == 1) {
        if (gyro.getAngle() > 180.5) {
          robotDrive.arcadeDrive(0.0, 0.52);
          timer.reset();
        } else if (gyro.getAngle() < 179.5) {
          robotDrive.arcadeDrive(0.0, -0.52);
          timer.reset();
        } else if (timer.get() < 0.5 && gyro.getAngle() > 179.5 && gyro.getAngle() < 180.5) {
          robotDrive.arcadeDrive(0.0, 0.0);

        }

        if (timer.get() > 0.5 && gyro.getAngle() > 179.5 && gyro.getAngle() < 180.5) {
          resetAll();
          counter++;

        }

      }
    } else if (counter == 7) {
      // actual field distance is 230
      if (pidDrive.calculate(leftEncoder.getDistance(), 50) > 0.45) {
        robotDrive.arcadeDrive(pidDrive.calculate(leftEncoder.getDistance(), 50), 0.0);
      } else if (leftEncoder.getDistance() < 50) {
        robotDrive.arcadeDrive(0.45, 0.0);
      }
      // intake.set(-0.4);

      if (timer.get() > 2) {
        resetAll();
        counter++;
      }
    }
  }

  public void cubeEngageEncoder() {
    if (counter == 0) {
      if (timer.get() < 1) {
        intake.set(0.95);
      } else {
        resetAll();
        counter2 = 0;
      }
    }

    else if (counter == 1) {
      if (timer.get() < 2) {
        robotDrive.arcadeDrive(-0.6, 0.0);
      } else if (leftEncoder.get() > -65) {
        robotDrive.arcadeDrive(-0.5, 0.0);
      } else {
        resetAll();
        counter++;
      }
    }
  }

  public void resetAll() {
    // gyro.reset();
    leftEncoder.reset();
    robotDrive.arcadeDrive(0.0, 0.0);
    intake.set(0.0);
    rotation.set(0.0);
    timer.reset();
    counter2 = 0;
    System.out.println("counter == " + counter + " ended");
    SmartDashboard.putNumber("Auto counter", counter);
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

    robotDrive.arcadeDrive(-driverJoystick.getRawAxis(1), -driverJoystick.getRawAxis(4));

    // Intake commands
    handleIntakeButton(driverJoystick);

    // Rotation commands
    handleRotationButton(operatorJoystick);
  }

  private void handleIntakeButton(Joystick joystick) {
    outtakeButton = 6;
    intakeButton = 5;

    if (joystick.getRawButtonPressed(outtakeButton)) {
      intake.set(0.4);
    } else if (joystick.getRawButtonReleased(outtakeButton)) {
      intake.set(0.0);
    }

    else if (joystick.getRawButtonPressed(intakeButton)) {
      intake.set(-0.5);

    } else if (joystick.getRawButtonReleased(intakeButton)) {
      intake.set(0.0);
    }
  }

  private void handleRotationButton(Joystick joystick) {
    rotationUpButton = 8;
    rotationDownButton = 7;
    if (joystick.getRawButtonPressed(rotationUpButton)) {
      rotation.set(0.4);
    } else if (joystick.getRawButtonReleased(rotationUpButton)) {
      rotation.set(0.0);
    } else if (joystick.getRawButtonPressed(rotationDownButton)) {
      rotation.set(-0.15);
    } else if (joystick.getRawButtonReleased(rotationDownButton)) {
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
    gyro.reset();
    timer.reset();
    timer.start();
    leftEncoder.reset();
    counter2 = 0;
  }

  @Override
  public void testPeriodic() {

    System.out.println(leftEncoder.getDistance());
    /*
     * if (timer.get() > 1) {
     * if (!pidTurn.atSetpoint()) {
     * robotDrive.arcadeDrive(0.0, -pidTurn.calculate(gyro.getAngle(), 180));
     * }
     * else {
     * robotDrive.arcadeDrive(0.0, 0.0);
     * }
     * }
     */

    /*
     * if (counter2 == 0) {
     * if (gyro.getAngle() < 174) {
     * robotDrive.arcadeDrive(0.0, -0.6);
     * }
     * 
     * else {
     * counter2++;
     * }
     * }
     * if (counter2 == 1) {
     * if (gyro.getAngle() > 180.5) {
     * robotDrive.arcadeDrive(0.0, 0.45);
     * }
     * if (gyro.getAngle() < 179.5) {
     * robotDrive.arcadeDrive(0.0, -0.45);
     * } else {
     * robotDrive.arcadeDrive(0.0, 0.0);
     * }
     * 
     * }
     * 
     * }
     */

    if (leftEncoder.getDistance() < 10) {
      robotDrive.arcadeDrive(0.0, 0.5);
    } else {
      robotDrive.arcadeDrive(0.0, 0.0);
    }
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
