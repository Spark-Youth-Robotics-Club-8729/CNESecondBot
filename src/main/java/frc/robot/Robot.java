// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

  // drivetrain
  private final WPI_VictorSPX frontLeft = new WPI_VictorSPX (1);
  private final WPI_VictorSPX backLeft = new WPI_VictorSPX (2);
  private final WPI_VictorSPX frontRight = new WPI_VictorSPX (3);
  private final WPI_VictorSPX backRight = new WPI_VictorSPX (4);
  private final MotorControllerGroup leftDrive = new MotorControllerGroup(backLeft, frontLeft);   
  private final MotorControllerGroup rightDrive = new MotorControllerGroup(backRight, frontRight);
  private final DifferentialDrive robotDrive = new DifferentialDrive(leftDrive, rightDrive);

  // intake mechanism
  private final CANSparkMax intake = new CANSparkMax(5, MotorType.kBrushless);
  private final CANSparkMax rotation = new CANSparkMax(6, MotorType.kBrushless);


  // joystick
  private final Joystick driverJoystick = new Joystick(0);
  private final Joystick operatorJoystick = new Joystick(1);
  

  public void robotInit() {
    // drivetrain
    rightDrive.setInverted(true);
    frontLeft.setNeutralMode(NeutralMode.Brake);
    backLeft.setNeutralMode(NeutralMode.Brake);
    frontRight.setNeutralMode(NeutralMode.Brake);
    backRight.setNeutralMode(NeutralMode.Brake);
  }


  @Override
  public void robotPeriodic() {}


  @Override
  public void autonomousInit() {}


  @Override
  public void autonomousPeriodic() {}


  @Override
  public void teleopInit() {
    // intializing speeds
    robotDrive.arcadeDrive(0.0, 0.0);
    intake.set(0.0);
    rotation.set(0.0);
  }


  @Override
  public void teleopPeriodic() {
    // arcadedrive command
    robotDrive.arcadeDrive(-driverJoystick.getRawAxis(1), driverJoystick.getRawAxis(0));

    //intake command
    if (operatorJoystick.getRawButtonPressed(0)){
      intake.set(0.4);
    } 

    if (operatorJoystick.getRawButtonReleased(0)) {
      intake.set(0.0);
    }

    if (operatorJoystick.getRawButtonPressed(8)){
      intake.set(-0.5);
    }

    if (operatorJoystick.getRawButtonReleased(8)) {
      intake.set(0.0);
    }
    
    if (operatorJoystick.getRawButtonPressed(5)){
      rotation.set(0.3);
    }

    if (operatorJoystick.getRawButtonReleased(5)) {
      rotation.set(0.0);
    }

    if (operatorJoystick.getRawButtonPressed(7)){
      rotation.set(-0.3);
    }

    if (operatorJoystick.getRawButtonReleased(7)) {
      rotation.set(0.0);
    }
  }


  @Override
  public void disabledInit() {}


  @Override
  public void disabledPeriodic() {}


  @Override
  public void testInit() {}


  @Override
  public void testPeriodic() {}


  @Override
  public void simulationInit() {}


  @Override
  public void simulationPeriodic() {}
}
