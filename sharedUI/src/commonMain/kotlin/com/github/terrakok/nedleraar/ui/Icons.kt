package com.github.terrakok.nedleraar.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Icons {
    val Back by lazy {
        ImageVector.Builder(
            name = "arrow_back",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Transparent)
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(24f)
                verticalLineToRelative(24f)
                horizontalLineTo(0f)
                verticalLineTo(0f)
                close()
            }
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(19f, 11f)
                horizontalLineTo(7.83f)
                lineToRelative(4.88f, -4.88f)
                curveToRelative(0.39f, -0.39f, 0.39f, -1.03f, 0f, -1.42f)
                curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0f)
                lineToRelative(-6.59f, 6.59f)
                curveToRelative(-0.39f, 0.39f, -0.39f, 1.02f, 0f, 1.41f)
                lineToRelative(6.59f, 6.59f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0f)
                curveToRelative(0.39f, -0.39f, 0.39f, -1.02f, 0f, -1.41f)
                lineTo(7.83f, 13f)
                horizontalLineTo(19f)
                curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
                reflectiveCurveToRelative(-0.45f, -1f, -1f, -1f)
                close()
            }
        }.build()
    }

    val Play by lazy {
        ImageVector.Builder(
            name = "play",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 448f,
            viewportHeight = 512f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(424.4f, 214.7f)
                lineTo(72.4f, 6.6f)
                curveTo(43.8f, -10.3f, 0f, 6.1f, 0f, 47.9f)
                verticalLineTo(464f)
                curveToRelative(0f, 37.5f, 40.7f, 60.1f, 72.4f, 41.3f)
                lineToRelative(352f, -208f)
                curveToRelative(31.4f, -18.5f, 31.5f, -64.1f, 0f, -82.6f)
                close()
            }
        }.build()
    }

    val Bookmark by lazy {
        ImageVector.Builder(
            name = "bookmark",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(17f, 3f)
                horizontalLineTo(7f)
                curveToRelative(-1.1f, 0f, -1.99f, 0.9f, -1.99f, 2f)
                lineTo(5f, 21f)
                lineToRelative(7f, -3f)
                lineToRelative(7f, 3f)
                verticalLineTo(5f)
                curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
                close()
                moveTo(17f, 18f)
                lineToRelative(-5f, -2.18f)
                lineTo(7f, 18f)
                verticalLineTo(5f)
                horizontalLineToRelative(10f)
                verticalLineToRelative(13f)
                close()
            }
        }.build()
    }

    val More by lazy {
        ImageVector.Builder(
            name = "more_horiz",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(6f, 10f)
                curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                reflectiveCurveToRelative(0.9f, 2f, 2f, 2f)
                reflectiveCurveToRelative(2f, -0.9f, 2f, -2f)
                reflectiveCurveToRelative(-0.9f, -2f, -2f, -2f)
                close()
                moveTo(18f, 10f)
                curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                reflectiveCurveToRelative(0.9f, 2f, 2f, 2f)
                reflectiveCurveToRelative(2f, -0.9f, 2f, -2f)
                reflectiveCurveToRelative(-0.9f, -2f, -2f, -2f)
                close()
                moveTo(12f, 10f)
                curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                reflectiveCurveToRelative(0.9f, 2f, 2f, 2f)
                reflectiveCurveToRelative(2f, -0.9f, 2f, -2f)
                reflectiveCurveToRelative(-0.9f, -2f, -2f, -2f)
                close()
            }
        }.build()
    }

    val Flag by lazy {
        ImageVector.Builder(
            name = "flag",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(14.4f, 6f)
                lineTo(14f, 4f)
                horizontalLineTo(5f)
                verticalLineToRelative(17f)
                horizontalLineToRelative(2f)
                verticalLineToRelative(-7f)
                horizontalLineToRelative(5.6f)
                lineToRelative(0.4f, 2f)
                horizontalLineToRelative(7f)
                verticalLineTo(6f)
                horizontalLineToRelative(-5.6f)
                close()
            }
        }.build()
    }

    val Close by lazy {
        ImageVector.Builder(
            name = "close",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(19f, 6.41f)
                lineTo(17.59f, 5f)
                lineTo(12f, 10.59f)
                lineTo(6.41f, 5f)
                lineTo(5f, 6.41f)
                lineTo(10.59f, 12f)
                lineTo(5f, 17.59f)
                lineTo(6.41f, 19f)
                lineTo(12f, 13.41f)
                lineTo(17.59f, 19f)
                lineTo(19f, 17.59f)
                lineTo(13.41f, 12f)
                close()
            }
        }.build()
    }

    val ArrowRight by lazy {
        ImageVector.Builder(
            name = "arrow_right",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(16.01f, 11f)
                horizontalLineTo(4f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(12.01f)
                verticalLineToRelative(3f)
                lineTo(20f, 12f)
                lineToRelative(-3.99f, -4f)
                close()
            }
        }.build()
    }
}