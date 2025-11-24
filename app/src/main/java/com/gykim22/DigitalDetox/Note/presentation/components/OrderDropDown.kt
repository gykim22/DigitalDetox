package com.gykim22.DigitalDetox.Note.presentation.components

import android.R.attr.onClick
import android.R.attr.text
import android.R.attr.textColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gykim22.DigitalDetox.Note.domain.util.OrderType
import com.gykim22.DigitalDetox.ui.theme.blue100

@Composable
fun SortChip(
    tagTitle: String,
    selected: OrderType,
    onSelected: (OrderType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    AssistChip(
        onClick = { expanded = true },
        label = { Text(tagTitle) },
        trailingIcon = {
            when (selected) {
                OrderType.Ascending -> Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color.Black
                )

                OrderType.Descending -> Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.White,
            labelColor = Color.Black
        ),
        border = BorderStroke(width = 1.dp, blue100),
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    )
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(surface = Color.White),
        shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(10))
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                colors = MenuItemColors(
                    textColor = Color.Black,
                    leadingIconColor = Color.Transparent,
                    trailingIconColor = Color.Transparent,
                    disabledLeadingIconColor = Color.Transparent,
                    disabledTrailingIconColor = Color.Black,
                    disabledTextColor = Color.Black
                ),
                modifier = Modifier.background(Color.White),
                text = {
                    Row {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Text("  오름차순")
                    }
                },
                onClick = {
                    onSelected(OrderType.Ascending)
                    expanded = false
                },
            )
            DropdownMenuItem(
                modifier = Modifier.background(Color.White),
                colors = MenuItemColors(
                    textColor = Color.Black,
                    leadingIconColor = Color.Transparent,
                    trailingIconColor = Color.Transparent,
                    disabledLeadingIconColor = Color.Transparent,
                    disabledTrailingIconColor = Color.Black,
                    disabledTextColor = Color.Black
                ),
                text = {
                    Row {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Text("  내림차순")
                    }
                },
                onClick = {
                    onSelected(OrderType.Descending)
                    expanded = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortDropdownPreview() {
    var order by remember { mutableStateOf<OrderType>(OrderType.Ascending) }
    SortChip(
        tagTitle = "작성 일자",
        selected = order,
        onSelected = { order = it },
        modifier = Modifier.padding(16.dp)
    )
}