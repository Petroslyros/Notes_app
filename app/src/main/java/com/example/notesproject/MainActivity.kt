package com.example.notesproject

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.notesproject.adapter.NotesAdapter
import com.example.notesproject.models.Note
import com.example.notesproject.viewmodels.NotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var notesLV: ListView
    private lateinit var model: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    private var noteList = ArrayList<Note>()
    private var importance : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        model = NotesViewModel(this)

        notesLV = findViewById(R.id.notesLV)

        notesAdapter = NotesAdapter(this, noteList)

        fetchAndPopulateList()


        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener {
            showAddNotePopup()
        }
        val searchET: EditText = findViewById(R.id.searchET)

        searchET.addTextChangedListener {
            val query = searchET.text.toString()
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
            notesLV.adapter = notesAdapter
        }
    }

    private fun showAddNotePopup() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.activity_add_note, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val titleInput = popupView.findViewById<EditText>(R.id.titleET)
        val noteEt = popupView.findViewById<EditText>(R.id.noteET)
        val submitBtn = popupView.findViewById<Button>(R.id.submitBtn)
        val starIV = popupView.findViewById<ImageView>(R.id.starIV)
        val oneBtn = popupView.findViewById<Button>(R.id.oneBtn)
        val twoBtn = popupView.findViewById<Button>(R.id.twoBtn)
        val threeBtn = popupView.findViewById<Button>(R.id.threeBtn)
        val fourBtn = popupView.findViewById<Button>(R.id.fourBtn)
        val fiveBtn = popupView.findViewById<Button>(R.id.fiveBtn)
        importance = 0
        val currentDate = getCurrentDate()


        oneBtn.setOnClickListener {
            starIV.setImageResource(R.drawable.onestar)
            importance = 1
        }
        twoBtn.setOnClickListener {
            starIV.setImageResource(R.drawable.twostar)
            importance = 2
        }
        threeBtn.setOnClickListener {
            starIV.setImageResource(R.drawable.threestar)
            importance = 3
        }
        fourBtn.setOnClickListener {
            starIV.setImageResource(R.drawable.fourstar)
            importance = 4
        }
        fiveBtn.setOnClickListener {
            starIV.setImageResource(R.drawable.fivestar)
            importance = 5
        }

        submitBtn.setOnClickListener {
            val title = titleInput.text.toString()
            val note = noteEt.text.toString()

            try {
                model.addNote(this, title, note, importance, currentDate) {
                    fetchAndPopulateList()
                    popupWindow.dismiss()
                }
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        popupWindow.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.popup_background
            )
        )
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)



    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
    private fun bbSort() {
        var N = noteList.size-1
        var temp : Note

        for(i in 0..N){
            for(j in N downTo i+1){
                if(noteList[j-1].importance < noteList[j].importance){
                    temp = noteList[j-1]
                    noteList[j-1] = noteList[j]
                    noteList[j] = temp

                }
            }
        }
    }
}