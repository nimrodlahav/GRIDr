package com.example.grider.models

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grider.components.UploadImageView

enum class NodeType { ACTION, RENDER }

data class OptionNode(
    val id: String,
    val label: String,
    val type: NodeType,
    val render: @Composable () -> Unit,
    val dependencies: List<String> = emptyList()
)

// Shared states
lateinit var rowsState: MutableState<Int>
lateinit var colsState: MutableState<Int>
lateinit var imageUriState: MutableState<Uri?>
lateinit var rotationState: MutableState<Float>

// Node composables (placeholders for later)
@Composable fun CutSheetView() { Text("Cut Sheet View") }
@Composable fun GridOnCutSheetView() { Text("Grid on Cut Sheet View") }
@Composable fun SideBySideGridView() { Text("Side by Side Grid View") }
@Composable fun ImageView() { Text("Image View") }
@Composable fun GridOnImageView() { Text("Grid on Image View") }
@Composable fun RotateView() { Text("Rotate Sheet") }

@Composable
fun FillRowsColumnsView(
    rows: Int,
    onRowsChange: (Int) -> Unit,
    cols: Int,
    onColsChange: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = rows.toString(),
            onValueChange = { new -> new.toIntOrNull()?.let { onRowsChange(it) } },
            label = { Text("Rows") },
            modifier = Modifier.width(120.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f),
                unfocusedTextColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f)
            )
        )
        OutlinedTextField(
            value = cols.toString(),
            onValueChange = { new -> new.toIntOrNull()?.let { onColsChange(it) } },
            label = { Text("Columns") },
            modifier = Modifier.width(120.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f),
                unfocusedTextColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f)
            )
        )
    }
}

// 🔹 Graph definition
val optionGraph = listOf(
    OptionNode(
        "1",
        "Upload Image",
        NodeType.ACTION,
        {
            UploadImageView(
                selectedImageUri = imageUriState.value,
                rows = rowsState.value,
                cols = colsState.value,
                paperWidthCm = 21f,   // default A4
                paperHeightCm = 29.7f,
                rotation = rotationState.value
            )
        }
    ),
    OptionNode("2", "Show Cut Sheet", NodeType.RENDER, { CutSheetView() }, listOf("1")),
    OptionNode("3", "Show Grid on Cut Sheet", NodeType.RENDER, { GridOnCutSheetView() }, listOf("2", "7")),
    OptionNode("4", "Show Grids Side by Side", NodeType.RENDER, { SideBySideGridView() }, listOf("3", "6")),
    OptionNode("5", "Show Image", NodeType.RENDER, { ImageView() }, listOf("1")),
    OptionNode("6", "Show Grid on Image", NodeType.RENDER, { GridOnImageView() }, listOf("5", "7")),
    OptionNode("7", "Fill Rows in Columns", NodeType.ACTION, {
        FillRowsColumnsView(
            rowsState.value, { rowsState.value = it },
            colsState.value, { colsState.value = it }
        )
    }, listOf("1")),
    OptionNode("8", "Rotate Sheet", NodeType.ACTION, { RotateView() }, listOf("1"))
)

// 🔹 Topological sort
fun topologicalSort(nodes: List<OptionNode>): List<OptionNode> {
    val graph = nodes.associateBy { it.id }
    val inDegree = nodes.associate { it.id to 0 }.toMutableMap()

    nodes.forEach { node ->
        node.dependencies.forEach { dep ->
            inDegree[node.id] = inDegree[node.id]!! + 1
        }
    }

    val queue = ArrayDeque(inDegree.filterValues { it == 0 }.keys)
    val result = mutableListOf<OptionNode>()

    while (queue.isNotEmpty()) {
        val id = queue.removeFirst()
        val node = graph[id] ?: continue
        result.add(node)

        nodes.filter { it.dependencies.contains(id) }.forEach { neighbor ->
            inDegree[neighbor.id] = inDegree[neighbor.id]!! - 1
            if (inDegree[neighbor.id] == 0) {
                queue.add(neighbor.id)
            }
        }
    }
    return result
}
