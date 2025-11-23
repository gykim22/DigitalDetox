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

@Composable
fun NoteOrderSection(
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
        SortChip(
            tagTitle = "제목",
            isSelected = currentOrder is NoteOrder.Title,
            selected = currentOrder.orderType,
            onSelected = {
                onOrderChange(NoteOrder.Title(currentOrder.orderType))
            },
            modifier = Modifier
        )
        SortChip(
            tagTitle = "작성 일자",
            isSelected = currentOrder is NoteOrder.Timestamp,
            selected = currentOrder.orderType,
            onSelected = {
                onOrderChange(NoteOrder.Timestamp(currentOrder.orderType))
            },
            modifier = Modifier
        )
        SortChip(
            tagTitle = "카테고리",
            isSelected = currentOrder is NoteOrder.Category,
            selected = currentOrder.orderType,
            onSelected = {
                onOrderChange(NoteOrder.Category(currentOrder.orderType))
            },
            modifier = Modifier
        )
        SortChip(
            tagTitle = "전체 시간",
            isSelected = currentOrder is NoteOrder.TotalTime,
            selected = currentOrder.orderType,
            onSelected = {
                onOrderChange(NoteOrder.TotalTime(currentOrder.orderType))
            },
            modifier = Modifier
        )
        SortChip(
            tagTitle = "공부 시간",
            isSelected = currentOrder is NoteOrder.StudyTime,
            selected = currentOrder.orderType,
            onSelected = {
                onOrderChange(NoteOrder.StudyTime(currentOrder.orderType))
            },
            modifier = Modifier
        )
        SortChip(
            tagTitle = "휴식 시간",
            isSelected = currentOrder is NoteOrder.BreakTime,
            selected = currentOrder.orderType,
            onSelected = {
                onOrderChange(NoteOrder.BreakTime(currentOrder.orderType))
            },
            modifier = Modifier
        )
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