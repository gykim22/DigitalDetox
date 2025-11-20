package com.gykim22.DigitalDetox.Timer.presentation.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gykim22.DigitalDetox.ui.theme.pretendard

/**
 * 세로 간격을 띄우기 위한 Spacer입니다.
 * @param height (Dp)세로 간격을 지정합니다.
 * @author Kim Giyun
 */
@Composable
fun HeightSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

/**
 * 가로 간격을 띄우기 위한 Spacer입니다.
 * @param width (Dp)가로 간격을 지정합니다.
 * @author Kim Giyun
 */
@Composable
fun WidthSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

/**
 * 커스텀 버튼입니다.
 * @param mModifier (Modifier)버튼의 모디파이어입니다.
 * @param text (String)버튼의 텍스트입니다.
 * @param buttonColor (Color)버튼의 배경색입니다.
 * @param textColor (Color)버튼의 텍스트 색입니다.
 * @param textSize (Int)버튼의 텍스트 크기입니다.
 * @param shape (Int)버튼의 모습입니다.
 * @param onClick (함수)버튼을 클릭했을 때 실행되는 함수입니다.
 * @author Kim Giyun
 */
@Composable
fun CustomButton(
    mModifier: Modifier = Modifier,
    text: String = "다음",
    buttonColor: Color = Color(0xFF397CDB),
    textColor: Color = Color.White,
    textSize: Int = 18,
    shape : Int = 16,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = mModifier,
        enabled = enabled,
        shape = RoundedCornerShape(size = shape.dp),
        colors = ButtonDefaults.buttonColors(buttonColor),
    ) {
        Text(
            text = text,
            fontFamily = pretendard,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = textSize.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}