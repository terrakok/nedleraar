package com.github.terrakok.oefoef.ui

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

    val Translate by lazy {
        ImageVector.Builder(
            name = "translate",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(4.545f, 6.714f)
                lineTo(4.11f, 8f)
                horizontalLineTo(3f)
                lineToRelative(1.862f, -5f)
                horizontalLineToRelative(1.284f)
                lineTo(8f, 8f)
                horizontalLineTo(6.833f)
                lineToRelative(-0.435f, -1.286f)
                close()
                moveToRelative(1.634f, -0.736f)
                lineTo(5.5f, 3.956f)
                horizontalLineToRelative(-0.049f)
                lineToRelative(-0.679f, 2.022f)
                close()
            }
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(0f, 2f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                horizontalLineToRelative(7f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(3f)
                arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                verticalLineToRelative(7f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                horizontalLineTo(7f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                verticalLineToRelative(-3f)
                horizontalLineTo(2f)
                arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                close()
                moveToRelative(2f, -1f)
                arcToRelative(1f, 1f, 0f, false, false, -1f, 1f)
                verticalLineToRelative(7f)
                arcToRelative(1f, 1f, 0f, false, false, 1f, 1f)
                horizontalLineToRelative(7f)
                arcToRelative(1f, 1f, 0f, false, false, 1f, -1f)
                verticalLineTo(2f)
                arcToRelative(1f, 1f, 0f, false, false, -1f, -1f)
                close()
                moveToRelative(7.138f, 9.995f)
                quadToRelative(0.289f, 0.451f, 0.63f, 0.846f)
                curveToRelative(-0.748f, 0.575f, -1.673f, 1.001f, -2.768f, 1.292f)
                curveToRelative(0.178f, 0.217f, 0.451f, 0.635f, 0.555f, 0.867f)
                curveToRelative(1.125f, -0.359f, 2.08f, -0.844f, 2.886f, -1.494f)
                curveToRelative(0.777f, 0.665f, 1.739f, 1.165f, 2.93f, 1.472f)
                curveToRelative(0.133f, -0.254f, 0.414f, -0.673f, 0.629f, -0.89f)
                curveToRelative(-1.125f, -0.253f, -2.057f, -0.694f, -2.82f, -1.284f)
                curveToRelative(0.681f, -0.747f, 1.222f, -1.651f, 1.621f, -2.757f)
                horizontalLineTo(14f)
                verticalLineTo(8f)
                horizontalLineToRelative(-3f)
                verticalLineToRelative(1.047f)
                horizontalLineToRelative(0.765f)
                curveToRelative(-0.318f, 0.844f, -0.74f, 1.546f, -1.272f, 2.13f)
                arcToRelative(6f, 6f, 0f, false, true, -0.415f, -0.492f)
                arcToRelative(2f, 2f, 0f, false, true, -0.94f, 0.31f)
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

    val Check by lazy {
        ImageVector.Builder(
            name = "check",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(10.97f, 4.97f)
                arcToRelative(0.75f, 0.75f, 0f, false, true, 1.07f, 1.05f)
                lineToRelative(-3.99f, 4.99f)
                arcToRelative(0.75f, 0.75f, 0f, false, true, -1.08f, 0.02f)
                lineTo(4.324f, 8.384f)
                arcToRelative(0.75f, 0.75f, 0f, true, true, 1.06f, -1.06f)
                lineToRelative(2.094f, 2.093f)
                lineToRelative(3.473f, -4.425f)
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

    val Github by lazy {
        ImageVector.Builder(
            name = "github",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(8f, 0f)
                curveTo(3.58f, 0f, 0f, 3.58f, 0f, 8f)
                curveToRelative(0f, 3.54f, 2.29f, 6.53f, 5.47f, 7.59f)
                curveToRelative(0.4f, 0.07f, 0.55f, -0.17f, 0.55f, -0.38f)
                curveToRelative(0f, -0.19f, -0.01f, -0.82f, -0.01f, -1.49f)
                curveToRelative(-2.01f, 0.37f, -2.53f, -0.49f, -2.69f, -0.94f)
                curveToRelative(-0.09f, -0.23f, -0.48f, -0.94f, -0.82f, -1.13f)
                curveToRelative(-0.28f, -0.15f, -0.68f, -0.52f, -0.01f, -0.53f)
                curveToRelative(0.63f, -0.01f, 1.08f, 0.58f, 1.23f, 0.82f)
                curveToRelative(0.72f, 1.21f, 1.87f, 0.87f, 2.33f, 0.66f)
                curveToRelative(0.07f, -0.52f, 0.28f, -0.87f, 0.51f, -1.07f)
                curveToRelative(-1.78f, -0.2f, -3.64f, -0.89f, -3.64f, -3.95f)
                curveToRelative(0f, -0.87f, 0.31f, -1.59f, 0.82f, -2.15f)
                curveToRelative(-0.08f, -0.2f, -0.36f, -1.02f, 0.08f, -2.12f)
                curveToRelative(0f, 0f, 0.67f, -0.21f, 2.2f, 0.82f)
                curveToRelative(0.64f, -0.18f, 1.32f, -0.27f, 2f, -0.27f)
                reflectiveCurveToRelative(1.36f, 0.09f, 2f, 0.27f)
                curveToRelative(1.53f, -1.04f, 2.2f, -0.82f, 2.2f, -0.82f)
                curveToRelative(0.44f, 1.1f, 0.16f, 1.92f, 0.08f, 2.12f)
                curveToRelative(0.51f, 0.56f, 0.82f, 1.27f, 0.82f, 2.15f)
                curveToRelative(0f, 3.07f, -1.87f, 3.75f, -3.65f, 3.95f)
                curveToRelative(0.29f, 0.25f, 0.54f, 0.73f, 0.54f, 1.48f)
                curveToRelative(0f, 1.07f, -0.01f, 1.93f, -0.01f, 2.2f)
                curveToRelative(0f, 0.21f, 0.15f, 0.46f, 0.55f, 0.38f)
                arcTo(8.01f, 8.01f, 0f, false, false, 16f, 8f)
                curveToRelative(0f, -4.42f, -3.58f, -8f, -8f, -8f)
            }
        }.build()
    }

    val School by lazy {
        ImageVector.Builder(
            name = "school",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Companion.Black)
            ) {
                moveTo(5f, 13.18f)
                verticalLineToRelative(4f)
                lineTo(12f, 21f)
                lineToRelative(7f, -3.82f)
                verticalLineToRelative(-4f)
                lineTo(12f, 17f)
                lineToRelative(-7f, -3.82f)
                close()
                moveTo(12f, 3f)
                lineTo(1f, 9f)
                lineToRelative(11f, 6f)
                lineToRelative(9f, -4.91f)
                verticalLineTo(17f)
                horizontalLineToRelative(2f)
                verticalLineTo(9f)
                lineTo(12f, 3f)
                close()
            }
        }.build()
    }

    val MenuDots by lazy {
        ImageVector.Builder(
            name = "dots-vertical",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 15f,
            viewportHeight = 15f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(7.5f, 11.375f)
                curveTo(8.12132f, 11.375f, 8.625f, 11.8787f, 8.625f, 12.5f)
                curveTo(8.625f, 13.1213f, 8.12132f, 13.625f, 7.5f, 13.625f)
                curveTo(6.87868f, 13.625f, 6.375f, 13.1213f, 6.375f, 12.5f)
                curveTo(6.375f, 11.8787f, 6.87868f, 11.375f, 7.5f, 11.375f)
                close()
                moveTo(7.5f, 6.375f)
                curveTo(8.12132f, 6.375f, 8.625f, 6.87868f, 8.625f, 7.5f)
                curveTo(8.625f, 8.12132f, 8.12132f, 8.625f, 7.5f, 8.625f)
                curveTo(6.87868f, 8.625f, 6.375f, 8.12132f, 6.375f, 7.5f)
                curveTo(6.375f, 6.87868f, 6.87868f, 6.375f, 7.5f, 6.375f)
                close()
                moveTo(7.5f, 1.375f)
                curveTo(8.12132f, 1.375f, 8.625f, 1.87868f, 8.625f, 2.5f)
                curveTo(8.625f, 3.12132f, 8.12132f, 3.625f, 7.5f, 3.625f)
                curveTo(6.87868f, 3.625f, 6.375f, 3.12132f, 6.375f, 2.5f)
                curveTo(6.375f, 1.87868f, 6.87868f, 1.375f, 7.5f, 1.375f)
                close()
            }
        }.build()
    }
}

