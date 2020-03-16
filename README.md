# SwerveLibrary
This repository contains code to operate a swerve-style robot drivetrain. It is designed to be flexible in it's ability to adapt to the multitude of mechanical designs for swerve drive which are prevalant within our organization, as well as simple to configure so that the time to integration for this library is minimal.

## Table of Contents
<!--ts-->
  * [Requirements](#requirements)
  * [Integration](#integration)
  * [Testing](#testing)
  * [Usage](#usage)
    * [Gyro](#gyro)
    * [PositionVelocitySensor](#positionvelocitysensor)
    * [SensorTransmission](#sensortransmission)
    * [PIDMotor](#pidmotor)
    * [PIDTransmission](#pidtransmission)
    * [MotorCombiner](#motorcombiner)
    * [SwerveModule](#swervemodule)
<!--te-->

## Requirements
SwerveLibrary is designed to run on the NI RoboRIO, due to that hardware's ubiquitous usage in our organization. In particular, it is designed to integrate into GradleRIO projects. As a result of this, a Windows laptop with the FRC Game Tools is required to run the software. Please see the documentation [here](https://docs.wpilib.org/en/latest/) for more information about this ecosystem.

## Integration
The currently recommended method to obtain SwerveLibrary inside of a project is through git submodules. Inside your git-enabled project directory, simply run the following command from the root directory of your project.
```
git submodule add https://github.com/PaulTerrasi/SwerveLibrary
```
To update the library it's most recent version, run the following, again starting from the root directory of your project.
```
cd SwerveLibrary
git fetch
git merge origin/master
```
For more information on using git submodules, see the [official tutorial](https://git-scm.com/book/en/v2/Git-Tools-Submodules).

Once the source code for SwerveLibrary has been downloaded, it must be added to the GradleRIO project. Add the following line to the `dependencies` block of your build.gradle file.
```
dependencies {
    compile wpi.deps.wpilib()
    compile wpi.deps.vendor.java()
    ...
    compile project(':SwerveLibrary')
    ...
}
```
The line `include 'SwerveLibrary'` should also be appended to the end of your settings.gradle file.

There are future plans to upload this library to a Maven repository in the future, which should greatly simplify the integration process for those users who only wish the use the library, but do not plan on developing it.

## Testing
SwerveLibrary runs a suite of tests in JUnit which utilize Mockito. These tests can be run on any computer and operating system that supports Gradle. They do not require RoboRIO hardware. Just run the following from the main directory of the library:
```
./gradlew test
```

## Usage
SwerveLibrary is in a fairly disjoint state. The plan for this library is to consolidate all functions which are useful to the end-user into a single API, with JSON configuration files to describe the mechanical and electrical properties of the swerve drive being controlled. Unfortunately, this API has not been written, so interim use entails manually constructing a `SwerveChassis` class from individual components. The relevant components are described below.

### Gyro
The `Gyro` interface represents a gyroscope which is measuring the yaw of the chassis. The presence of this sensor is key for field-oriented control of a swerve drive. The necessary functions of a gyro are that it must be able to determine both the yaw and the rate of change of the yaw of the chassis, as well as be able to get calibrated such that the current reading is mapped to a particular value. An implementation of the `Gyro` interface is provided for the NavX gyroscope.

### PositionVelocitySensor
The `PositionVelocitySensor` interface represents any sensor which can track both position and velocity. Like a `Gyro`, it must be able to calibrate its positon to map the current reading to a particular value. An implementation of the `PositionVelocitySensor` interface is provided for PWM based absolute encoders which are read through a CANifier.

### SensorTransmission
A `SensorTransmission` object is both an extension of `PositionVelocitySensor` and takes one in as an input. It applies a given gear ratio to the sensor. This affects both the position and velocity readings. This class is useful for representing physical ratios between the sensor input and the output that is actually intended to be measured

### PIDMotor
The `PIDMotor` interface is an extension of the `PositionVelocitySensor` which includes a method to set the velocity of the output. It is expected that implementations of this interface will describe a motor and encoder pairing, and use PID to achieve the desired velocity. An implementation of the `PIDMotor` interface is provided for the NEO motor with its integrated encoder.

### PIDTransmission
The `PIDTransmission` class is an extension of the `SensorTransmission` class which is also an implementation of the `PIDMotor` interface. It not only applies the given gear ratio to the readings from the sensor, but also to the set velocity of the motor.

### MotorCombiner
The `MotorCombiner` class, in general terms, is used to linearly combine multiple motor inputs into an equal number of outputs. It takes `PIDMotor`s as inputs, and returns `PIDMotor`s as outputs. Practically, this is useful to SwerveLibrary in order to describe swerve modules where the wheel spinning and steering are not independent of each other, such as in differential swerves or swerves with planetary mechanisms. It is built by providing input motors one at a time, alongside an array of co-efficients which describe that motor's linear contribution to each output.

### SwerveModule
The `SwerveModule` class takes in a `PIDMotor` which controls wheel speed, a `PIDMotor` which controls the azimuth, and a `PositonVelocitySensor` which measures the absolute azimuth of the module.
