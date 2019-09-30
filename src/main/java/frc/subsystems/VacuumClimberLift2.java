/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.RobotMap;
import frc.commands.vacuumClimberLift2.DriveVacuumClimberLift2WithJoysticks;

/**
 * Sample subsystem
 * Steps::
 * create variable to interface with hardware (motors, pneumatics, sensors, etc)
 */
public class VacuumClimberLift2 extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private VictorSP vacuumClimbLiftMotor1;
  private VictorSP vacuumClimbLiftMotor2;

  private static VacuumClimberLift2 _instance;

  private VacuumClimberLift2()
  {
    vacuumClimbLiftMotor1 = new VictorSP(RobotMap.CLIMBER_LIFT_MOTOR_1_PDP);
    vacuumClimbLiftMotor2 = new VictorSP(RobotMap.CLIMBER_LIFT_MOTOR_2_PDP);

  }

  public static VacuumClimberLift2 getInstance()
  {
    if (_instance==null)
      _instance = new VacuumClimberLift2();
    return _instance;
  }

  public void driveVacuumClimberLiftMotor1(double speed)
  {
    vacuumClimbLiftMotor1.set(speed);
  }

  public void driveVacuumClimberLiftMotor2(double speed)
  {
    vacuumClimbLiftMotor2.set(speed);
  }

  public void driveVacuumClimberLiftMotors(double speed)
  {
    driveVacuumClimberLiftMotor1(speed);
    driveVacuumClimberLiftMotor2(speed);
  }





  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new DriveVacuumClimberLift2WithJoysticks());
  }
}
