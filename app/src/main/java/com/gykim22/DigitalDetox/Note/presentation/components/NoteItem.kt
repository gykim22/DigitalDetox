package com.gykim22.DigitalDetox.Note.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gykim22.DigitalDetox.Note.domain.model.Note
import com.gykim22.DigitalDetox.Note.presentation.utils.dateFormat
import com.gykim22.DigitalDetox.Timer.presentation.util.HeightSpacer
import com.gykim22.DigitalDetox.Timer.presentation.util.WidthSpacer
import com.gykim22.DigitalDetox.Timer.presentation.util.noRippleClickable
import com.gykim22.DigitalDetox.Timer.presentation.util.parseToHMS
import com.gykim22.DigitalDetox.ui.theme.blue100
import com.gykim22.DigitalDetox.ui.theme.pretendard

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(2.dp, blue100, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .noRippleClickable {
                onEditClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = note.title,
                modifier = Modifier.weight(1f),
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.Black,
                maxLines = 1
            )

            WidthSpacer(5.dp)

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "삭제",
                modifier = Modifier.noRippleClickable(onClick = onDeleteClick),
                tint = blue100
            )
        }
        HeightSpacer(10.dp)
        Text(
            text = dateFormat(note.timestamp),
            modifier = Modifier,
            fontFamily = pretendard,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        HeightSpacer(10.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "전체 시간 : ${parseToHMS(note.total_time)}",
                modifier = Modifier,
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 16.sp,
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "더보기",
                modifier = Modifier
                    .noRippleClickable(onClick = { expanded = !expanded })
                    .rotate(rotation),
                tint = blue100
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 5.dp)) {
                Text(
                    text = "공부 시간 : ${parseToHMS(note.study_time)}",
                    fontFamily = pretendard,
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                HeightSpacer(4.dp)
                Text(
                    text = "휴식 시간 : ${parseToHMS(note.break_time)}",
                    fontFamily = pretendard,
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        HeightSpacer(10.dp)

        Text(
            text = note.contents,
            modifier = Modifier,
            fontFamily = pretendard,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun NoteItemPreview() {
    NoteItem(
        note = Note(
            title = "제목제목제목제목",
            contents = "내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
            category = "공부",
            timestamp = 1763941200000,
            total_time = 1000,
            study_time = 500,
            break_time = 500
        ),
        onDeleteClick = {},
        onEditClick = {}
    )
}