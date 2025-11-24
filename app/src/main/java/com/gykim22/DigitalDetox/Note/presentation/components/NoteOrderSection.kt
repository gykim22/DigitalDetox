package com.gykim22.DigitalDetox.Note.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gykim22.DigitalDetox.Note.domain.util.NoteOrder
import com.gykim22.DigitalDetox.Note.domain.util.OrderType
import com.gykim22.DigitalDetox.Timer.presentation.util.WidthSpacer

@Composable
fun NoteOrderSection(
    sidePaddingValueInt : Int = 16,
    currentOrder: NoteOrder,
    onOrderChange: (NoteOrder) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WidthSpacer(sidePaddingValueInt.dp)
        SortChip(
            tagTitle = "제목",
            selected = currentOrder.orderType,
            onSelected = { selectedType ->
                onOrderChange(NoteOrder.Title(selectedType))
            }
        )

        SortChip(
            tagTitle = "작성 일자",
            selected = currentOrder.orderType,
            onSelected = { selectedType ->
                onOrderChange(NoteOrder.Timestamp(selectedType))
            }
        )

        SortChip(
            tagTitle = "전체 시간",
            selected = currentOrder.orderType,
            onSelected = { selectedType ->
                onOrderChange(NoteOrder.TotalTime(selectedType))
            }
        )

        SortChip(
            tagTitle = "공부 시간",
            selected = currentOrder.orderType,
            onSelected = { selectedType ->
                onOrderChange(NoteOrder.StudyTime(selectedType))
            }
        )

        SortChip(
            tagTitle = "휴식 시간",
            selected = currentOrder.orderType,
            onSelected = { selectedType ->
                onOrderChange(NoteOrder.BreakTime(selectedType))
            }
        )
        WidthSpacer(sidePaddingValueInt.dp)
    }
}

@Preview
@Composable
fun NoteOrderSectionPreview(){
    NoteOrderSection(
        currentOrder = NoteOrder.Timestamp(OrderType.Descending),
        onOrderChange = {}
    )
}