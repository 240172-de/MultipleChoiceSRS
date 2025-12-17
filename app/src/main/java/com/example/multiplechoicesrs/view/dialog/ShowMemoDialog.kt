package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.multiplechoicesrs.R

@Composable
fun ShowMemoDialog(
    memo: String,
    onSubmit: (memo: String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var text by remember { mutableStateOf(memo) }

    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                OutlinedTextField(
                    value = text,
                    label = { Text(stringResource(R.string.memo)) },
                    onValueChange = { text = it },
                    minLines = 5,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    TextButton(
                        onClick = { onSubmit(text) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}