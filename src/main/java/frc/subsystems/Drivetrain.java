/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.PID.controllers.PIDPosition;
import frc.PID.controllers.PIDSpeed;
import frc.PID.sensors.ADXRS453Gyro;
import frc.PID.sensors.AverageEncoder;
import frc.PID.sensors.IMU;
import frc.commands.drivetrain.DriveWithJoysticks;
import frc.utils.TCPSocketSender;
import frc.utils.consoleprinter.ConsolePrinter;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.RobotMap;
import frc.Robot;

/**
 * Subsystem class for the Drivetrain
 * 
 * @author Alyssa Soloman
 */
public class Drivetrain extends Subsystem {

  private static VictorSPX _leftMotor1;
  private static VictorSPX _leftMotor2;
  private static VictorSPX _leftMotor3;
  private static VictorSPX _rightMotor1;
  private static VictorSPX _rightMotor2;
  private static VictorSPX _rightMotor3;

  private ADXRS453Gyro _gyroSPI;
  private AverageEncoder _drivetrainLeftEncoder;
  private AverageEncoder _drivetrainRightEncoder;

  //Leave these wihtout _name convention to work with past code base
  private double RightMotor1FPS;
  private double RightMotor2FPS;
  private double leftMotor1FPS;
  private double lefttMotor1FPS;
  public IMU imu;



  // declare position/speed controllers
  public PIDPosition driveTrainPosController;
  public PIDPosition rotateController;
  public PIDPosition rotateDriveStraightController;

  public PIDPosition rightPosController;
  public PIDPosition leftPosController;

  // declare speed controllers
  public PIDSpeed rightSpeedController;
  public PIDSpeed leftSpeedController;

    // declare TCP severs...ONLY FOR DEBUGGING PURPOSES, SHOULD BE REMOVED FOR
  // COMPITITION
  TCPSocketSender TCPdrivePosController;
  TCPSocketSender TCPrightSpeedController;
  TCPSocketSender TCPleftSpeedController;
  TCPSocketSender TCProtateController;
  TCPSocketSender TCPleftPosController;
  TCPSocketSender TCPrightPosController;

  private static Drivetrain instance = null;

  public volatile double leftMotor1Voltage;
  public volatile double leftMotor2Voltage;
  public volatile double leftMotor3Voltage;
  public volatile double rightMotor1Voltage;
  public volatile double rightMotor2Voltage;
  public volatile double rightMotor3Voltage;

  double runTime = Timer.getFPGATimestamp();


  /**
   * Default constructors for Drivetrain
   */
  private Drivetrain()
  {

    /**
     * Method which automaticallys allows us to switch between 6 wheel and 6 wheel
     * drive, and also allows us to switch between CAN vs PWM control on Drivetrain
     * 
     * 4 to 6 motors is controlled by boolean in RobotMap
     * 
     * CAN/PWM is controlled by jumper on MXP
     * 
     * Also allows us to detect comp chasis vs practice chassis and code for any
     * differences.
     */

    System.out.println("CAN Comp Bot Drivetrain enabled - 4 motors");
    _leftMotor1 = new VictorSPX(RobotMap.DRIVETRAIN_LEFT_MOTOR_1_PDP);
    _leftMotor2 = new VictorSPX(RobotMap.DRIVETRAIN_LEFT_MOTOR_2_PDP);
    _rightMotor1 = new VictorSPX(RobotMap.DRIVETRAIN_RIGHT_MOTOR_1_PDP);
    _rightMotor2 = new VictorSPX(RobotMap.DRIVETRAIN_RIGHT_MOTOR_2_PDP);
    
    
    _drivetrainRightEncoder = new AverageEncoder(
        RobotMap.RIGHT_DRIVE_ENCODER_A,
        RobotMap.RIGHT_DRIVE_ENCODER_B,
        RobotMap.DRIVE_ENCODER_PULSE_PER_ROT,
        RobotMap.DRIVE_ENCODER_DIST_PER_TICK,
        RobotMap.RIGHT_DRIVE_TRAIN_ENCODER_REVERSE,
        RobotMap.DRIVE_ENCODING_TYPE,
        RobotMap.DRIVE_SPEED_RETURN_TYPE,
        RobotMap.DRIVE_POS_RETURN_TYPE,
        RobotMap.DRIVE_AVG_ENCODER_VAL);

    _drivetrainLeftEncoder = new AverageEncoder(
        RobotMap.LEFT_DRIVE_ENCODER_A, 
        RobotMap.LEFT_DRIVE_ENCODER_B,
        RobotMap.DRIVE_ENCODER_PULSE_PER_ROT, 
        RobotMap.DRIVE_ENCODER_DIST_PER_TICK,
        RobotMap.LEFT_DRIVE_TRAIN_ENCODER_REVERSE, 
        RobotMap.DRIVE_ENCODING_TYPE,
        RobotMap.DRIVE_SPEED_RETURN_TYPE, 
        RobotMap.DRIVE_POS_RETURN_TYPE, 
        RobotMap.DRIVE_AVG_ENCODER_VAL);

    _gyroSPI = new ADXRS453Gyro();
    _gyroSPI.startThread();

    imu = new IMU(_drivetrainLeftEncoder, _drivetrainRightEncoder, RobotMap.WHEEL_BASE);

    // DriveStraight Controller
    rotateController = new PIDPosition(
        "RotationController", 
        RobotMap.ROTATE_POSITION_P, 
        RobotMap.ROTATE_POSITION_I,
        RobotMap.ROTATE_POSITION_D, 
        _gyroSPI, 
        RobotMap.DRIVE_TRAIN_PID_PERIOD);

    
    rotateDriveStraightController = new PIDPosition(
        "RotationStraightController",
        RobotMap.ROTATE_POSITION_P_Drive_Straight, 
        RobotMap.ROTATE_POSITION_I_Drive_Straight,
        RobotMap.ROTATE_POSITION_D_Drive_Straight, 
        _gyroSPI, 
        RobotMap.DRIVE_TRAIN_PID_PERIOD);

    driveTrainPosController = new PIDPosition(
        "driveTrainPosController", 
        RobotMap.DRIVE_TRAIN_RIGHT_POSITION_P,
        RobotMap.DRIVE_TRAIN_RIGHT_POSITION_I, 
        RobotMap.DRIVE_TRAIN_RIGHT_POSITION_D, 
        imu,
        RobotMap.DRIVE_TRAIN_PID_PERIOD);

    // Spawn new PID Controller
    rightSpeedController = new PIDSpeed(
        "rightSpeedController", 
        RobotMap.DRIVE_TRAIN_RIGHT_SPEED_P,
        RobotMap.DRIVE_TRAIN_RIGHT_SPEED_I, 
        RobotMap.DRIVE_TRAIN_RIGHT_SPEED_D, 
        1, 
        _drivetrainRightEncoder,
        RobotMap.DRIVE_TRAIN_PID_PERIOD);

    leftSpeedController = new PIDSpeed(
        "leftSpeedController", 
        RobotMap.DRIVE_TRAIN_LEFT_SPEED_P,
        RobotMap.DRIVE_TRAIN_LEFT_SPEED_I, 
        RobotMap.DRIVE_TRAIN_LEFT_SPEED_D, 
        1, 
        _drivetrainLeftEncoder,
        RobotMap.DRIVE_TRAIN_PID_PERIOD);

      // Spawn new PID Controller
    rightPosController = new PIDPosition(
        "rightPosController", 
        RobotMap.DRIVE_TRAIN_RIGHT_POSITION_P,
        RobotMap.DRIVE_TRAIN_RIGHT_POSITION_I, 
        RobotMap.DRIVE_TRAIN_RIGHT_POSITION_D, 
        1, 
        _drivetrainRightEncoder,
        RobotMap.DRIVE_TRAIN_PID_PERIOD);

    leftPosController = new PIDPosition(
        "leftPosController", 
        RobotMap.DRIVE_TRAIN_LEFT_POSITION_P,
        RobotMap.DRIVE_TRAIN_LEFT_POSITION_I, 
        RobotMap.DRIVE_TRAIN_LEFT_POSITION_D, 
        1, 
        _drivetrainLeftEncoder,
        RobotMap.DRIVE_TRAIN_PID_PERIOD);


    // add min and max output defaults and set array size
    rightSpeedController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
    leftSpeedController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
    rightPosController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
    leftPosController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
    driveTrainPosController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
    rotateController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);
    rotateDriveStraightController.setSIZE(RobotMap.DRIVE_TRAIN_PID_ARRAY_SIZE);


    // start controller threads
    rightSpeedController.startThread();
    leftSpeedController.startThread();
    rightPosController.startThread();
    leftPosController.startThread();
    driveTrainPosController.startThread();
    rotateController.startThread();
    rotateDriveStraightController.startThread();


    // start TCP Servers for DEBUGING ONLY
    TCPdrivePosController = new TCPSocketSender(RobotMap.TCP_SERVER_DRIVE_TRAIN_POS, driveTrainPosController);
    TCPdrivePosController.start();

    TCPrightSpeedController = new TCPSocketSender(RobotMap.TCO_SERVER_RIGHT_DRIVE_TRAIN_SPEED, rightSpeedController);
    TCPrightSpeedController.start();

    TCPleftSpeedController = new TCPSocketSender(RobotMap.TCP_SERVER_LEFT_DRIVE_TRAIN_SPEED, leftSpeedController);
    TCPleftSpeedController.start();

    TCPrightPosController = new TCPSocketSender(RobotMap.TCP_SERVER_RIGHT_DRIVE_TRAIN_POSITION, rightPosController);
    TCPrightPosController.start();

    TCPleftPosController = new TCPSocketSender(RobotMap.TCP_SERVER_LEFT_DRIVE_TRAIN_POSITION, leftPosController);
    TCPleftPosController.start();

    TCProtateController = new TCPSocketSender(RobotMap.TCP_SERVER_ROTATE_CONTROLLER, rotateController);
    TCProtateController.start();

    TCProtateController = new TCPSocketSender(RobotMap.TCP_SERVER_ROTATE_CONTROLLER_STRAIGHT,rotateDriveStraightController);
    TCProtateController.start();

    leftMotor1Voltage = 0;
    leftMotor2Voltage = 0;

    rightMotor1Voltage = 0;
    rightMotor2Voltage = 0;
    

    // Log sensor data
    ConsolePrinter.putNumber("Left Encoder Distance", () -> {return Robot.drivetrain.getLeftPosition();}, true, false);
    ConsolePrinter.putNumber("Right Encoder Distance:", () -> {return Robot.drivetrain.getRightPosition();}, true, false);
    ConsolePrinter.putNumber("Average Drive Encoder Distance", () -> {return Robot.drivetrain.getAverageDistance();}, true, false);
    ConsolePrinter.putNumber("Right Drive Encoder Rate", () -> {return Robot.drivetrain.getRightEncoderRate();}, true, false);
    ConsolePrinter.putNumber("Left Drive Encoder Rate", () -> {return Robot.drivetrain.getLeftEncoderRate();}, true, false);
    ConsolePrinter.putNumber("Average Drive Encoder Rate", () -> {return Robot.drivetrain.getAverageEncoderRate();}, true, false);
    ConsolePrinter.putNumber("Gyro Angle:", () -> {return Robot.drivetrain.getHeading();}, true, false);	
    ConsolePrinter.putNumber("Gunstyle X Value", () -> {return Robot.oi.getGunStyleXValue();}, true, false);
    ConsolePrinter.putNumber("Gunstyle Y Value", () -> {return Robot.oi.getGunStyleYValue();}, true, false);
    ConsolePrinter.putNumber("DTLeft1MotorVoltage", () -> {return Robot.drivetrain.getleftMotor1Voltage();}, true, false);
    ConsolePrinter.putNumber("DTLeft2MotorVoltage", () -> {return Robot.drivetrain.getleftMotor2Voltage();}, true, false);
    ConsolePrinter.putNumber("DTRight1MotorVoltage", () -> {return Robot.drivetrain.getrightMotor1Voltage();}, true, false);
    ConsolePrinter.putNumber("DTRight2MotorVoltage", () -> {return Robot.drivetrain.getrightMotor2Voltage();}, true, false);
    ConsolePrinter.putNumber("DTRight1MotorCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR_1_PDP);}, true, false);
    ConsolePrinter.putNumber("DTRight2MotorCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR_2_PDP);}, true, false);
    ConsolePrinter.putNumber("DTLeft1MotorCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR_1_PDP);}, true, false);
    ConsolePrinter.putNumber("DTLeft2MotorCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR_2_PDP);}, true, false);
    ConsolePrinter.putNumber("PID right motor 1 voltage", () -> {return this.PIDVoltagefeedRightMotor1();}, true, false);
    ConsolePrinter.putNumber("PID right motor 2 voltage", () -> {return this.PIDVoltagefeedRightMotor2();}, true, false);
    ConsolePrinter.putNumber("PID left motor 1 voltage", () -> {return this.PIDVoltagefeedLeftMotor1();}, true, false);
    ConsolePrinter.putNumber("PID left motor 2 voltage", () -> {return this.PIDVoltagefeedLeftMotor2();}, true, false);
    
    ConsolePrinter.putNumber("DTLeft2MotorCurrent", () -> {return Robot.pdp.getChannelCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR_2_PDP);}, true, false);
    ConsolePrinter.putNumber("GYRO Driftrate:", () -> {return Robot.drivetrain._gyroSPI.driftRate;}, true, false);
    ConsolePrinter.putNumber("GYRO Rate:", () -> {return Robot.drivetrain._gyroSPI.getRate();}, true, false);
    ConsolePrinter.putNumber("GYRO Angle SPI:", () -> {return Robot.drivetrain._gyroSPI.getAngle();}, true, false);
    ConsolePrinter.putNumber("GYRO reInits:", () -> {return (double) Robot.gyroReinits;}, true, false);
    ConsolePrinter.putBoolean("Gyro Cal Status", () -> {return !Robot.gyroCalibrating;}, true, false);
    ConsolePrinter.putNumber("GYRO Status:", () -> {return (double) Robot.drivetrain._gyroSPI.getStatus();}, true, false);
    ConsolePrinter.putNumber("GYRO Temp:", () -> {return Robot.drivetrain._gyroSPI.getTemp();}, true, false);
    
    
    ConsolePrinter.putBoolean("Left Motor One Trip", () -> {return !Robot.pdp.isLeftMotorOneTrip();}, true, false);
    ConsolePrinter.putBoolean("Left Motor Two Trip", () -> {return !Robot.pdp.isLeftMotorTwoTrip();}, true, false);
    ConsolePrinter.putBoolean("Right Motor One Trip", () -> {return !Robot.pdp.isRightMotorOneTrip();}, true, false);
    ConsolePrinter.putBoolean("Right Motor Two Trip", () -> {return !Robot.pdp.isRightMotorTwoTrip();}, true, false);
    
    
  }

  /**
   * Calls instance object and makes it a singleton object of type Drivetrain
   * 
   * @returns Drivetrain object "instance"
   */
  public static Drivetrain getInstance()
  {
    if (instance == null)
      instance = new Drivetrain();

    return instance;
  }

  /**
   * Calls left motor 1 and creates a local variable "speed" Refers to boolean in
   * Robot map and if true, speed = - speed Uses set() command to assign the new
   * speed to left motor 1
   * 
   * @param double speed between -1 and 1 negative is reverse, positive if
   *        forward, 0 is stationary
   */
  private void driveleftMotor1(double speed)
  {
    if (RobotMap.DT_REVERSE_LEFT1)
      speed = -speed;

    _leftMotor1.set(ControlMode.PercentOutput, speed);
    leftMotor1Voltage = Robot.pdp.getBatteryVoltage() * speed;

  }

  /**
   * Calls left motor 2 and creates a local variable "speed" Refers to boolean in
   * Robot map and if true, speed = - speed Uses set() command to assign the new
   * speed to left motor 2
   * 
   * @param double speed between -1 and 1 negative is reverse, positive if
   *        forward, 0 is stationary
   */
  private void driveleftMotor2(double speed)
  {
    if (RobotMap.DT_REVERSE_LEFT2)
      speed = -speed;

    _leftMotor2.set(ControlMode.PercentOutput, speed);
    leftMotor2Voltage = Robot.pdp.getBatteryVoltage() * speed;
  }

  private void driveleftMotor3(double speed)
  {
    if (RobotMap.DT_REVERSE_LEFT3)
      speed = -speed;

    _leftMotor3.set(ControlMode.PercentOutput, speed);
    leftMotor3Voltage = Robot.pdp.getBatteryVoltage() * speed;
  }

  /**
   * Take in double speed and sets it to left motors 1, 2, and 3
   * 
   * @param speed is a double between -1 and 1 negative is reverse, positive if
   *              forward, 0 is stationary
   */
  public void driveLeft(double speed)
  {
    if (RobotMap.DT_3_MOTORS_PER_SIDE)
    {
      driveleftMotor1(speed);
      driveleftMotor2(speed);
      driveleftMotor3(speed);
    }
    else
    {
      driveleftMotor1(speed);
      driveleftMotor2(speed);
    }
  }

  /**
   * Calls right motor 1 and creates a local variable "speed" Refers to boolean in
   * Robot map and if true, speed = - speed Uses set() command to assign the new
   * speed to right motor 1
   * 
   * @param double speed between -1 and 1 negative is reverse, positive if
   *        forward, 0 is stationary
   */
  private void driverightMotor1(double speed)
  {
    if (RobotMap.DT_REVERSE_RIGHT1)
      speed = -speed;

    _rightMotor1.set(ControlMode.PercentOutput, speed);
    rightMotor1Voltage = Robot.pdp.getBatteryVoltage() * speed;
  }

  /**
   * Calls right motor 2 and creates a local variable "speed" Refers to boolean in
   * Robot map and if true, speed = - speed Uses set() command to assign the new
   * speed to right motor 2
   * 
   * @param double speed between -1 and 1 negative is reverse, positive if
   *        forward, 0 is stationary
   */
  private void driverightMotor2(double speed)
  {
    if (RobotMap.DT_REVERSE_RIGHT2)
      speed = -speed;

    _rightMotor2.set(ControlMode.PercentOutput, speed);
    rightMotor2Voltage = Robot.pdp.getBatteryVoltage() * speed;
  }

  /**
   * Calls right motor 3 and creates a local variable "speed" Refers to boolean in
   * Robot map and if true, speed = - speed Uses set() command to assign the new
   * speed to right motor 3
   * 
   * @param double speed between -1 and 1 negative is reverse, positive if
   *        forward, 0 is stationary
   */
  private void driverightMotor3(double speed)
  {
    if (RobotMap.DT_REVERSE_RIGHT3)
      speed = -speed;

    _rightMotor3.set(ControlMode.PercentOutput, speed);
    rightMotor3Voltage = Robot.pdp.getBatteryVoltage() * speed;
  }

  /**
   * Takes in a double speed and sets it to their right motors 1, 2, and 3
   * 
   * @param speed is a double between -1 and 1 negative is reverse, positive if
   *              forward, 0 is stationary
   */
  public void driveRight(double speed)
  {
    if (RobotMap.DT_3_MOTORS_PER_SIDE)
    {
      driverightMotor1(speed);
      driverightMotor2(speed);
      driverightMotor3(speed);
    }
    else
    {
      driverightMotor1(speed);
      driverightMotor2(speed);
    }
  }

  /**
   * Takes in speed for right and speed for left and sets them to their respective
   * sides
   * 
   * @param leftSpeed  is a double between -1 and 1
   * @param rightSpeed is a double between -1 and 1 negative is reverse, positive
   *                   if forward, 0 is stationary
   */
  public void tankDrive(double leftSpeed, double rightSpeed)
  {
    driveLeft(leftSpeed);
    driveRight(rightSpeed);

    runTime = Timer.getFPGATimestamp();
    driveLeft(leftSpeed);
    driveRight(rightSpeed);
    SmartDashboard.putNumber("TankDriveSetCanTime", Timer.getFPGATimestamp() - runTime);

  }

  /**
   * returns total distance traveled by right side of drivetrain
   * 
   * @return double in feet of total distance traveled by right encoder
   */
  public double getRightPosition()
  {
    return _drivetrainRightEncoder.getPos();
  }

  /**
   * returns total distance traveled by left side of drivetrain
   * 
   * @return double in feet of total distance traveled by left encoder
   */
  public double getLeftPosition()
  {
    return _drivetrainLeftEncoder.getPos();
  }

  /**
   * returns total distance traveled by drivetrain
   * 
   * @return double in inches of average distance traveled by both encoders
   */
  public double getAverageDistance()
  {
    return imu.getPos();
  }


  /**
   * resets position of right encoder to 0 inches
   */
  public void resetRightPosition()
  {
    _drivetrainRightEncoder.reset();
  }

  /**
   * resets position of left encoder to 0 inches
   */
  public void resetLeftPosition()
  {
    _drivetrainLeftEncoder.reset();
  }

  /**
   * resets position of both Encoders to 0 inches
   */
  public void resetPosition()
  {
    resetLeftPosition();
    resetRightPosition();
  }


  /**
   * returns heading of robot
   * 
   * @return double between 0 degrees and 360 degrees
   */
  public double getHeading()
  {
    return _gyroSPI.getPos();
  }

  /**
   * Reset robot heading to zero.
   */
  public void resetGyro()
  {
    _gyroSPI.reset();
  }

  /**
   * Calibrate gyro. This should only be called if the robot will be stationary
   * for the calibration period.
   */
  public void calibrateGyro()
  {
    _gyroSPI.calibrate();
  }

  /**
   * @return true if the gyro completed its previous calibration sequence.
   */
  public boolean isGyroCalibrated()
  {
    return _gyroSPI.hasCompletedCalibration();
  }

  /**
   * @return true if the gyro is being calibrated.
   */
  public boolean isGyroCalibrating()
  {
    return _gyroSPI.isCalibrating();
  }

  /**
   * Call to stop an active gyro calibration sequence.
   */
  public void stopGyroCalibrating()
  {
    _gyroSPI.stopCalibrating();
  }

  /**
   * Returns the last commanded voltage of left Motor 1
   * 
   * @return Double in volts between 0 and 12
   */
  public double getleftMotor1Voltage()
  {
    return leftMotor1Voltage;
  }

  /**
   * Returns the last commanded voltage of left Motor 2
   * 
   * @return Double in volts between 0 and 12
   */
  public double getleftMotor2Voltage()
  {
    return leftMotor2Voltage;
  }

  /**
   * Returns the last commanded voltage of right Motor 1
   * 
   * @return Double in volts between 0 and 12
   */
  public double getrightMotor1Voltage()
  {
    return rightMotor1Voltage;
  }

  /**
   * Returns the last commanded voltage of right Motor 2
   * 
   * @return Double in volts between 0 and 12
   */
  public double getrightMotor2Voltage()
  {
    return rightMotor2Voltage;
  }

  public double getRightEncoderRate()
  {
    return _drivetrainRightEncoder.getRate();
  }

  public double getLeftEncoderRate()
  {
    return _drivetrainLeftEncoder.getRate();
  }

  public double getAverageEncoderRate()
  {
    return ((getRightEncoderRate() + getLeftEncoderRate()) / 2);
  }

  /**
   * Returns the current position of the gun style controller interpolated
   * 
   * @param x is voltage
   * @return Potentiometer position
   */

  /**
   * Call to start an active gyro calibration sequence.
   */
  public void startGyroCalibrating()
  {
    _gyroSPI.startCalibrating();
  }

  public double returnRightEncoderRate()
  {
    return getRightEncoderRate();
  }

  public double returnLeftEncoderRate()
  {
    return getLeftEncoderRate();
  }

  public double PIDVoltagefeedRightMotor1()
  {
    if (getRightEncoderRate() != 0)
      return this.getrightMotor1Voltage() / this.getRightEncoderRate();
    else
      return 0.0;
  }

  public double PIDVoltagefeedRightMotor2()
  {
    if (getRightEncoderRate() != 0)
      return this.getrightMotor2Voltage() / this.getRightEncoderRate();
    else
      return 0.0;
  }

  public double PIDVoltagefeedLeftMotor1()
  {
    if (getLeftEncoderRate() != 0)
      return this.getleftMotor1Voltage() / this.getLeftEncoderRate();
    else
      return 0.0;
  }

  public double PIDVoltagefeedLeftMotor2()
  {
    if (getLeftEncoderRate() != 0)
      return this.getleftMotor2Voltage() / this.getLeftEncoderRate();
    else
      return 0.0;
  }

  /**
   * Calls for default command of the drivetrain to be DriveWithJoystick
   */
  public void initDefaultCommand()
  {
    setDefaultCommand(new DriveWithJoysticks(0));
  }
}








