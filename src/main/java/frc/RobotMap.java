/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.I2C;
import frc.PID.sensors.AverageEncoder;

/**
 * Add your docs here.
 */
public class RobotMap {

    public static final double MAIN_PERIOD_S = 1.0/50.0; // Main loop 200Hz

    public static final int DRIVER_JOYSTICK_PORT = 0;
    public static final int OPERATOR_JOYSTICK_PORT = 1;

    // Digital IO Channels//////////////////////////////////////////////////////
    // Channels 0-9 on RoboRio
    public static final int RIGHT_DRIVE_ENCODER_A = 0;
    public static final int RIGHT_DRIVE_ENCODER_B = 1;
    public static final int LEFT_DRIVE_ENCODER_B = 2;
    public static final int LEFT_DRIVE_ENCODER_A = 3;



    /*************************************************************************
    *                         CAN DEVICES                                    *
    *************************************************************************/

    //CAN Device IDs///////////////////////////////////////////////////////////
    public static final int PCM_CAN_ID = 0;
    public static final int PDP_CAN_ID = 0;



    /*************************************************************************
	*                         PDP/CAN DEVICES                                 *
    *************************************************************************/
    
    public static final int PLUNGER_PIVOT_MOTOR_PDP = 10; //TODO set
    public static final int DRIVETRAIN_RIGHT_MOTOR_1_PDP = 11; 
    public static final int DRIVETRAIN_LEFT_MOTOR_1_PDP = 12;
    public static final int CARGO_INTAKE_WHEELS_LEFT_PDP = 13;
    public static final int CARGO_INTAKE_WHEELS_RIGHT_PDP = 4;
    public static final int HATCH_INTAKE_WHEELS_PDP = 5;
    public static final int COMPRESSOR_PDP = 6;
    public static final int CLIMBER_LIFT_MOTOR_1_PDP = 0;
    public static final int CLIMBER_LIFT_MOTOR_2_PDP = 1;
    public static final int CLIMBER_PUMP_MOTOR_PDP = 2;
    

    
	public static final int DRIVETRAIN_LEFT_MOTOR_2_PDP = 14;
	public static final int DRIVETRAIN_RIGHT_MOTOR_2_PDP = 15;
	public static final int DRIVETRAIN_RIGHT_MOTOR_3_PDP = 30; //not used in 4 motor DT
    public static final int DRIVETRAIN_LEFT_MOTOR_3_PDP = 31; //not used in 4 motor DT
    
    
    /*************************************************************************
     *                         DRIVETRAIN PARAMETERS                         *
     *************************************************************************/
    // TODO check if the reverse values match the physical robot
    public static final boolean DT_REVERSE_LEFT1 = false;
    public static final boolean DT_REVERSE_LEFT2 = false;
    public static final boolean DT_REVERSE_LEFT3 = false;
    public static final boolean DT_REVERSE_RIGHT1 = true;
    public static final boolean DT_REVERSE_RIGHT2 = true;
    public static final boolean DT_REVERSE_RIGHT3 = true; 

    public static final boolean DT_3_MOTORS_PER_SIDE = false;

    private static final int DRIVE_PULSE_PER_ROTATION = 256; // encoder ticks per rotation

    private static final double DRIVE_GEAR_RATIO = 1.0 / 1.0; // ratio between wheel over encoder
    private static final double DRIVE_WHEEL_DIAMETER = 6.0;   //inches;
    public static final int DRIVE_ENCODER_PULSE_PER_ROT = (int) (DRIVE_PULSE_PER_ROTATION * DRIVE_GEAR_RATIO); // pulse per rotation * gear																					// ratio
    public static final double DRIVE_ENCODER_DIST_PER_TICK = (Math.PI * DRIVE_WHEEL_DIAMETER / DRIVE_ENCODER_PULSE_PER_ROT);
    public static final CounterBase.EncodingType DRIVE_ENCODING_TYPE = CounterBase.EncodingType.k4X; // count rising and falling edges on
    public static final AverageEncoder.PositionReturnType DRIVE_POS_RETURN_TYPE = AverageEncoder.PositionReturnType.INCH;
    public static final AverageEncoder.SpeedReturnType DRIVE_SPEED_RETURN_TYPE = AverageEncoder.SpeedReturnType.IPS;
    public static final int DRIVE_ENCODER_MIN_RATE = 0;
    public static final int DRIVE_ENCODER_MIN_PERIOD = 1;
    public static final boolean LEFT_DRIVE_TRAIN_ENCODER_REVERSE = true;
    public static final boolean RIGHT_DRIVE_TRAIN_ENCODER_REVERSE = true;

    public static final int DRIVE_AVG_ENCODER_VAL = 5;
    public static final double MIN_DRIVE_SPEED = 0.2;
    public static final double AUTO_NORMAL_SPEED = 0.5;
    public static final double WHEEL_BASE = 26; //units must match PositionReturnType (inch)


    /*************************************************************************
	*                         PIVOT PARAMETERS                               *
    *************************************************************************/

    public static final boolean PLUNGER_ARM_PIVOT_REVERSE = false;
    public static final boolean CARGO_INTAKE_LEFT_REVERSE = false;
    public static final boolean CARGO_INTAKE_RIGHT_REVERSE = false;
    public static final boolean HATCH_INTAKE_WHEELS_REVERSE = false;

    /*************************************************************************
	*                         VACUUM CLIMBER PARAMETERS                      *
    *************************************************************************/

    public static final boolean CLIMBER_LIFT_MOTOR_1_REVERSE = false;
    public static final boolean CLIMBER_LIFT_MOTOR_2_REVERSE = true;
    public static final boolean CLIMBER_PUMP_MOTOR_REVERSE = false;
    

    /*************************************************************************
     *                         PDP PARAMETERS                                *
     *************************************************************************/
    public static final long PDPThreadPeriod = 100;
    public static final double WARNING_CURRENT_LIMIT = 20;
    public static final double STALL_CURRENT_LIMIT = 350;
    public static final double CURRENT_LIMIT_TIME_THRESHOLD_SECONDS = 1;

        /*************************************************************************
     *                         PID PARAMETERS                                *
     *************************************************************************/
    // period to run PID loops on drive train
    public static final long DRIVE_TRAIN_PID_PERIOD = 20;// 70ms loop
    public static final int DRIVE_TRAIN_PID_ARRAY_SIZE = 30;

    public static final double DRIVE_TRAIN_MIN_FWD_VOLTAGE = 1.8;//volts
    public static final double DRIVE_TRAIN_MIN_RVD_VOLTAGE = 1.2;//volts

    public static final double DRIVE_TRAIN_MIN_ROT_CLOCKWISE_VOLTAGE = 1.45;//volts
    public static final double DRIVE_TRAIN_MIN_ROT_COUNTCLOCKWISE_VOLTAGE = 1.45;//volts

    // PID Gains for Left & Right Speed and Position
    // Bandwidth =
    // Phase Margin =
    public static final double DRIVE_TRAIN_LEFT_SPEED_P = 0.04779;
    public static final double DRIVE_TRAIN_LEFT_SPEED_I = 0.0010526;
    public static final double DRIVE_TRAIN_LEFT_SPEED_D = 0.0543;

    public static final double DRIVE_TRAIN_RIGHT_SPEED_P = 0.04779;
    public static final double DRIVE_TRAIN_RIGHT_SPEED_I = 0.0010526;
    public static final double DRIVE_TRAIN_RIGHT_SPEED_D = 0.0543;

    public static final double DRIVE_TRAIN_LEFT_POSITION_P = 0.2;
    public static final double DRIVE_TRAIN_LEFT_POSITION_I = 0.0001412646174233;
    public static final double DRIVE_TRAIN_LEFT_POSITION_D = 0.0074778888124088;

    public static final double DRIVE_TRAIN_RIGHT_POSITION_P = 0.25;
    public static final double DRIVE_TRAIN_RIGHT_POSITION_I = 0.0001412646174233;
    public static final double DRIVE_TRAIN_RIGHT_POSITION_D = 0.0074778888124088;

    public static final double ROTATE_POSITION_P = 0.055;
    public static final double ROTATE_POSITION_I = 0.001;
    public static final double ROTATE_POSITION_D = 0.0064778888124088;

    public static final double ROTATE_POSITION_P_Drive_Straight = 0.055; //0.055 comp
    public static final double ROTATE_POSITION_I_Drive_Straight = 0.001; //0.001
    public static final double ROTATE_POSITION_D_Drive_Straight = 0.0064778888124088;


    public static final double INTAKE_PIVOT_P = 0.024;
    public static final double INTAKE_PIVOT_I = 0.027;
    public static final double INTAKE_PIVOT_D = 000000067;

    public static final long LIFT_PID_PERIOD = 20;
    public static final int  LIFT_PID_ARRAY_SIZE = 30;


    /****************************************************************
     *                         TCP Servers (ONLY FOR DEBUGGING)     *
     ****************************************************************/
    public static final int TCP_SERVER_DRIVE_TRAIN_POS = 1180;
    public static final int TCP_SERVER_ROTATE_CONTROLLER = 1181;
    public static final int TCO_SERVER_RIGHT_DRIVE_TRAIN_SPEED = 1182;
    public static final int TCP_SERVER_LEFT_DRIVE_TRAIN_SPEED = 1183;
    public static final int TCP_SERVER_LIFT_POT_CONTROLLER = 1184;
    public static final int TCP_SERVER_ROTATE_CONTROLLER_STRAIGHT = 1185;
    public static final int TCP_SERVER_RIGHT_DRIVE_TRAIN_POSITION = 1186;
    public static final int TCP_SERVER_LEFT_DRIVE_TRAIN_POSITION = 1187;
    public static final int TCP_SERVER_HP_POT_CONTROLLER = 1190;


    /******************************************************************
     *                         ConsolePrinter PARAMETERS              *
     ******************************************************************/
    public static final boolean PRINT_SD_DEBUG_DATA = false;
    public static final long SmartDashThreadPeriod = 200; // ms
    public static final long CONSOLE_PRINTER_LOG_RATE_MS = 200; // ms


    /******************************************************************
     *                         Lights I2C                             *
     ******************************************************************/
    public static final I2C.Port I2C_PORT = I2C.Port.kOnboard;
    public static final int I2C_ADDRESS = 10;






}
