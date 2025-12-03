package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.ui.theme.MutedWhite

@Composable
fun FullSizeImageDialog(
    imageBitmap: ImageBitmap,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize().clickable{ onDismissRequest() }
        ) {
            IconButton(onDismissRequest) {
                Icon(
                    painter = painterResource(R.drawable.baseline_close_24),
                    contentDescription = "閉じる",
                    tint = MutedWhite
                )
            }

            Image(
                bitmap = imageBitmap,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth().clickable(enabled = false, onClick = {}),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}
