package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D
import org.firstinspires.ftc.robotcore.external.navigation.Position
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.round

fun roundPose3D(robotPose3D: Pose3D, angleUnit: AngleUnit = AngleUnit.DEGREES): Pose3D {
    val originalPosition = robotPose3D.position
    val originalOrientation = robotPose3D.orientation

    return Pose3D(
        Position(
            originalPosition.unit,
            round(originalPosition.x),
            round(originalPosition.y),
            round(originalPosition.z),
            originalPosition.acquisitionTime
        ), YawPitchRollAngles(
            angleUnit,
            round(originalOrientation.yaw),
            round(originalOrientation.pitch),
            round(originalOrientation.roll),
            originalOrientation.acquisitionTime
        )
    )
}

fun addPose3D(lhs: Pose3D, rhs: Pose3D, angleUnit: AngleUnit = AngleUnit.DEGREES): Pose3D {
    return Pose3D(
        Position(
            lhs.position.unit,
            lhs.position.x + rhs.position.x,
            lhs.position.y + rhs.position.y,
            lhs.position.z + rhs.position.z,
            max(lhs.position.acquisitionTime, rhs.position.acquisitionTime)
        ), YawPitchRollAngles(
            angleUnit,
            lhs.orientation.yaw + rhs.orientation.yaw,
            lhs.orientation.pitch + rhs.orientation.pitch,
            lhs.orientation.roll + rhs.orientation.roll,
            max(lhs.orientation.acquisitionTime, rhs.orientation.acquisitionTime)
        )
    )
}

fun subPose3D(lhs: Pose3D, rhs: Pose3D, angleUnit: AngleUnit = AngleUnit.DEGREES): Pose3D {
    return Pose3D(
        Position(
            lhs.position.unit,
            lhs.position.x - rhs.position.x,
            lhs.position.y - rhs.position.y,
            lhs.position.z - rhs.position.z,
            max(lhs.position.acquisitionTime, rhs.position.acquisitionTime)
        ), YawPitchRollAngles(
            angleUnit,
            lhs.orientation.yaw - rhs.orientation.yaw,
            lhs.orientation.pitch - rhs.orientation.pitch,
            lhs.orientation.roll - rhs.orientation.roll,
            max(lhs.orientation.acquisitionTime, rhs.orientation.acquisitionTime)
        )
    )
}

fun magnitudePose3D(
    lhs: Pose3D, rhs: Pose3D, angleUnit: AngleUnit = AngleUnit.DEGREES
): Pair<Double, Double> {
    val difference = subPose3D(lhs, rhs, angleUnit)

    val differencePosition = difference.position
    val differenceOrientation = difference.orientation

    return Pair(
        abs(differencePosition.x) + abs(differencePosition.y) + abs(differencePosition.z),
        abs(differenceOrientation.yaw) + abs(differenceOrientation.pitch) + abs(
            differenceOrientation.roll
        )
    )
}