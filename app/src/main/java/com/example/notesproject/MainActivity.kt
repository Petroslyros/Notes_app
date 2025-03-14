package com.example.notesproject

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.notesproject.adapter.NotesAdapter
import com.example.notesproject.databinding.ActivityMainBinding
import com.example.notesproject.databinding.ActivityAddNoteBinding
import com.example.notesproject.models.Note
import com.example.notesproject.viewmodels.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    private var noteList = ArrayList<Note>()
    private var importance: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = NotesViewModel(this)
        notesAdapter = NotesAdapter(this, noteList)
        binding.notesLV.adapter = notesAdapter

        fetchAndPopulateList()

        binding.fab.setOnClickListener {
            showAddNotePopup()
        }

        binding.searchET.addTextChangedListener {
            val query = binding.searchET.text.toString()
            if (query.isNotBlank()) {
                model.searchNotes(this, query) { results ->
                    noteList.clear()
                    noteList.addAll(results)
                    notesAdapter.notifyDataSetChanged()
                }
            } else {
                fetchAndPopulateList()
            }
        }
    }

    private fun fetchAndPopulateList() {
        lifecycleScope.launch {
            val notes = withContext(Dispatchers.IO) {
                model.fetchAllNotes(baseContext)
            }
            noteList.clear()
            noteList.addAll(notes)
            bbSort()
            notesAdapter.notifyDataSetChanged()
        }
    }

    private fun showAddNotePopup() {
        val popupBinding = ActivityAddNoteBinding.inflate(LayoutInflater.from(this))
        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        importance = 0
        val currentDate = getCurrentDate()

        popupBinding.oneBtn.setOnClickListener {
            popupBinding.starIV.setImageResource(R.drawable.onestar)
            importance = 1
        }
        popupBinding.twoBtn.setOnClickListener {
            popupBinding.starIV.setImageResource(R.drawable.twostar)
            importance = 2
        }
        popupBinding.threeBtn.setOnClickListener {
            popupBinding.starIV.setImageResource(R.drawable.threestar)
            importance = 3
        }
        popupBinding.fourBtn.setOnClickListener {
            popupBinding.starIV.setImageResource(R.drawable.fourstar)
            importance = 4
        }
        popupBinding.fiveBtn.setOnClickListener {
            popupBinding.starIV.setImageResource(R.drawable.fivestar)
            importance = 5
        }

        popupBinding.submitBtn.setOnClickListener {
            val title = popupBinding.titleET.text.toString()
            val note = popupBinding.noteET.text.toString()
            try {
                model.addNote(this, title, note, importance, currentDate) {
                    fetchAndPopulateList()
                    popupWindow.dismiss()
                }
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.popup_background))
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(popupBinding.root, Gravity.CENTER, 0, 0)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun bbSort() {
        for (i in 0 until noteList.size - 1) {
            for (j in noteList.size - 1 downTo i + 1) {
                if (noteList[j - 1].importance < noteList[j].importance) {
                    val temp = noteList[j - 1]
                    noteList[j - 1] = noteList[j]
                    noteList[j] = temp
                }
            }
        }
    }
}
