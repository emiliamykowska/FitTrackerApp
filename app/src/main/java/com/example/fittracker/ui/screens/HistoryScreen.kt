package com.example.fittracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fittracker.data.ActivityEntry
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.fittracker.ui.components.HistoryActivityCard


@Composable
fun HistoryScreen(
    onNavigate: (String) -> Unit,
    allActivities: List<ActivityEntry>
){
    val db = FirebaseFirestore.getInstance()

    var showDeleteAlert by remember { mutableStateOf(false) }
    var activityToDelete by remember { mutableStateOf<ActivityEntry?>(null) }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        item {
            Text(
                text = "All Activities:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(
            items = allActivities,
            key = { it.id } //by default lazyColumn uses index to differentiate between items, now it uses id
        ){ activity ->
            HistoryActivityCard(
                activityName = activity.activityName,
                duration = activity.duration,
                timestamp = activity.date,
                onDelete = {
                    activityToDelete = activity
                    showDeleteAlert = true
                }
            )
        }


        if (allActivities.isEmpty()) {
            item {
                Text(
                    text = "You haven't add any activities yet, add your first one after a workout!",
                    textAlign = TextAlign.Justify,
                    fontSize = 15.sp
                )
            }
        }
    }


    if (activityToDelete != null && showDeleteAlert){
        val idToDelete = activityToDelete!!.id
        AlertDialog(
            onDismissRequest = { showDeleteAlert = false },
            title = {Text(text = "Delete activity")},
            text = {Text(text = "Are you sure you want to delete this activity?")},
            confirmButton = {TextButton(onClick = {
                db.collection("activities")
                    .document(idToDelete)
                    .delete() //document gets an entry from collection with specified path (here activity.id)
                    .addOnSuccessListener { Toast.makeText(context, "Activity was successfully deleted", Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { exception ->
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()}

                showDeleteAlert = false
//                allActivities = allActivities.filter { it.id != idToDelete }
                    }
            ) {
                    Text(
                        text = "Confirm",
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteAlert = false
                    }
                ) {
                    Text(text = "Dismiss")
                }
            }
        )

    }
}