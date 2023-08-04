package kr.sjh.myschedule.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)
//
//val Icons.Rounded.WRITE: ImageVector
//    get() {
//        if (_write != null) {
//            return _write!!
//        }
//        _write = materialIcon(name = "Rounded.WRITE") {
//            materialPath {
//                moveTo(18.0f, 13.0f)
//                horizontalLineToRelative(-5.0f)
//                verticalLineToRelative(5.0f)
//                curveToRelative(0.0f, 0.55f, -0.45f, 1.0f, -1.0f, 1.0f)
//                reflectiveCurveToRelative(-1.0f, -0.45f, -1.0f, -1.0f)
//                verticalLineToRelative(-5.0f)
//                horizontalLineTo(6.0f)
//                curveToRelative(-0.55f, 0.0f, -1.0f, -0.45f, -1.0f, -1.0f)
//                reflectiveCurveToRelative(0.45f, -1.0f, 1.0f, -1.0f)
//                horizontalLineToRelative(5.0f)
//                verticalLineTo(6.0f)
//                curveToRelative(0.0f, -0.55f, 0.45f, -1.0f, 1.0f, -1.0f)
//                reflectiveCurveToRelative(1.0f, 0.45f, 1.0f, 1.0f)
//                verticalLineToRelative(5.0f)
//                horizontalLineToRelative(5.0f)
//                curveToRelative(0.55f, 0.0f, 1.0f, 0.45f, 1.0f, 1.0f)
//                reflectiveCurveToRelative(-0.45f, 1.0f, -1.0f, 1.0f)
//                close()
//            }
//        }
//        return _write!!
//    }
//
//private var _write: ImageVector? = null