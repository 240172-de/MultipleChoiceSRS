package com.example.multiplechoicesrs.view.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.ui.theme.DarkBlue
import com.example.multiplechoicesrs.ui.theme.LightBlue

@Composable
fun ExpandableBottomView(
    initialIsExpanded: Boolean = true,
    content: @Composable () -> Unit
) {
    val cornerRadius = 10.dp

    var showContent by rememberSaveable { mutableStateOf(initialIsExpanded) }

    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .zIndex(2f)
    ) {
        Column {
            IconButton(
                onClick = {
                    showContent = !showContent
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadius, cornerRadius, 0.dp, 0.dp))
                    .background(LightBlue),
            ) {
                val degree by animateFloatAsState(
                    targetValue = if (showContent) 0f else 180f,
                    label = "ExpandButtonAnimation",
                )

                Icon(
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = stringResource(R.string.expand),
                    tint = DarkBlue,
                    modifier = Modifier
                        .rotate(degree)
                        .size(32.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnimatedVisibility(
                    visible = showContent,
                    enter = expandIn(expandFrom = Alignment.TopStart),
                    exit = shrinkOut(shrinkTowards = Alignment.TopStart)
                ) {
                    Column(modifier = Modifier.padding(5.dp)) {
                        content()
                    }
                }
            }
        }
    }
}