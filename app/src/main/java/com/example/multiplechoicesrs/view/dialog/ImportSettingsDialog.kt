package com.example.multiplechoicesrs.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.multiplechoicesrs.R
import com.example.multiplechoicesrs.model.viewmodel.ImportSettingsDialogViewModel

@Composable
fun ImportSettingsDialog(
    onSubmit: () -> Unit,
    onDismissRequest: () -> Unit,
    dialogViewModel: ImportSettingsDialogViewModel
) {
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.settings_import), fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = dialogViewModel.ipAddress,
                    label = { Text(stringResource(R.string.ip_address)) },
                    onValueChange = { dialogViewModel.ipAddress = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = dialogViewModel.port,
                    label = { Text(stringResource(R.string.port)) },
                    onValueChange = { dialogViewModel.port = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                        onClick = {
                            onSubmit()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.apply_changes))
                    }
                }
            }
        }
    }
}