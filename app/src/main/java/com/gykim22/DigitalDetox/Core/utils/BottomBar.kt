package com.gykim22.DigitalDetox.Core.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gykim22.DigitalDetox.Timer.presentation.util.noRippleClickable
import com.gykim22.DigitalDetox.ui.theme.pretendard
import com.gykim22.DigitalDetox.R
import com.gykim22.DigitalDetox.ui.theme.gray300

enum class BottomTab(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    Home("타이머", R.drawable.btn_blue_bottom_bar_home, R.drawable.btn_gray_bottom_bar_home),
    Calendar("노트", R.drawable.btn_blue_bottom_bar_calendar, R.drawable.btn_gray_bottom_bar_calendar),
}

@Composable
fun BottomBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                spotColor = Color.Black,
                ambientColor = Color.Black,
                clip = false
            )
            .background(Color.White)
            .padding(top = 14.dp, bottom = 22.dp)
            .navigationBarsPadding(),
    ) {
        BottomTab.values().forEach { tab ->
            BottomBarItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                mModifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BottomBarItem(
    tab: BottomTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    mModifier: Modifier = Modifier
) {
    val icon = if (isSelected) tab.selectedIcon else tab.unselectedIcon
    val textColor = if (isSelected) Color(0xFF4E74FF) else gray300

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = mModifier.noRippleClickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = tab.label,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = tab.label,
            fontSize = 12.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center,
            letterSpacing = 0.6.sp
        )
    }
}

@Preview
@Composable
fun PreviewPage() {
    BottomBar(
        selectedTab = BottomTab.Home,
        onTabSelected = {}
    )
}