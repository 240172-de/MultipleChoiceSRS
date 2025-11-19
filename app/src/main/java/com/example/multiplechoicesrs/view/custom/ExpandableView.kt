package com.example.multiplechoicesrs.view.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.multiplechoicesrs.R

@Composable
fun ExpandableView(title:String, initialExpandedState: Boolean = true, content: @Composable () -> Unit) {
    var showContent by remember { mutableStateOf(initialExpandedState) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .clickable { showContent = !showContent }
            ) {
                Text(
                    title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                val degree by animateFloatAsState(
                    targetValue = if (showContent) 0f else -90f,
                    label = "ExpandButtonAnimation",
                )

                Icon(
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.rotate(degree).size(32.dp)
                )
            }

            AnimatedVisibility(
                visible = showContent,
                enter = expandIn(expandFrom = Alignment.TopStart),
                exit = shrinkOut(shrinkTowards = Alignment.TopStart)
            ) {
                content()
            }
        }
    }
}