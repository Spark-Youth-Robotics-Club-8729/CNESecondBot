// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.DriveConstants;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSubsystem extends SubsystemBase {

  private final WPI_VictorSPX frontLeft = new WPI_VictorSPX(DriveConstants.FRONT_LEFT);
  private final WPI_VictorSPX backLeft = new WPI_VictorSPX(DriveConstants.BACK_LEFT);
  private final WPI_VictorSPX frontRight = new WPI_VictorSPX(DriveConstants.FRONT_RIGHT);
  private final WPI_VictorSPX backRight = new WPI_VictorSPX(DriveConstants.BACK_RIGHT);

  private final MotorControllerGroup leftDrive = new MotorControllerGroup(backLeft, frontLeft);
  private final MotorControllerGroup rightDrive = new MotorControllerGroup(backRight, frontRight);

  private final DifferentialDrive robotDrive = new DifferentialDrive(leftDrive, rightDrive);

  // encoder
  private final Encoder leftEncoder = new Encoder(DriveConstants.encoderChannelA, DriveConstants.encoderChannelB);

  // gyro
  ADXRS450_Gyro gyro = new ADXRS450_Gyro();



  /** Creates a new ExampleSubsystem. */
  public DriveSubsystem() {
    rightDrive.setInverted(true);
    frontLeft.setNeutralMode(NeutralMode.Brake);
    backLeft.setNeutralMode(NeutralMode.Brake);
    frontRight.setNeutralMode(NeutralMode.Brake);
    backRight.setNeutralMode(NeutralMode.Brake);
    leftEncoder.setDistancePerPulse(DriveConstants.encoderProportions);
    leftEncoder.setReverseDirection(true);
  }

  public double getEncoderDistance(){
    return leftEncoder.getDistance();
  }

  public void resetEncoder(){
    leftEncoder.reset();
  }

  public double getGyroYaw(){
    return gyro.getAngle();
  }

  public void setMotor(double speed, double turn){
    robotDrive.arcadeDrive(speed, turn);
    //System.out.println("actual: " + speed + ", " + turn);
  }

  public void resetGyro(){
    gyro.reset();
  }

  public void logDrive(String command){
    SmartDashboard.putString("Drive Task:" , command);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Yaw", getGyroYaw());
    SmartDashboard.putNumber("Encoder Left Distance", leftEncoder.getDistance());
    SmartDashboard.putNumber("Left Encoder Distance", leftEncoder.getDistance());
    SmartDashboard.putNumber("Left Drive Speed", leftDrive.get());
    SmartDashboard.putNumber("Right Drive Speed", rightDrive.get());
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public CommandBase exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
