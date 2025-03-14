package com.example.notesproject.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.notesproject.models.Note
import com.example.notesproject.R
import com.example.notesproject.database.NotesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesAdapter(private val context: Context, private val notes: ArrayList<Note>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return notes.size
    }

    override fun getItem(position: Int): Any {
        return notes[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        val titleTV = view.findViewById<TextView>(R.id.titleTV)
        val textTV = view.findViewById<TextView>(R.id.textTV)
        val deleteBtn = view.findViewById<Button>(R.id.deleteBtn)
        val editBtn = view.findViewById<Button>(R.id.editBtn)
        val dateTV = view.findViewById<TextView>(R.id.dateTV)
        val starIV = view.findViewById<ImageView>(R.id.starIV)


        titleTV.text = notes[position].title
        textTV.text = notes[position].text
        when (notes[position].importance) {
            1 -> starIV.setImageResource(R.drawable.onestar)
            2 -> starIV.setImageResource(R.drawable.twostar)
            3 -> starIV.setImageResource(R.drawable.threestar)
            4 -> starIV.setImageResource(R.drawable.fourstar)
            5 -> starIV.setImageResource(R.drawable.fivestar)
        }
        dateTV.text = notes[position].date

        deleteBtn.setOnClickListener {
        }

        editBtn.setOnClickListener {
            editItem(position)
        }

        return view
    }

    private fun deleteItem(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirmation Alert")
        builder.setMessage("Are you sure you want to delete this note?")
        builder.setPositiveButton("Yes") { dialog, which ->
            val noteToDelete = notes[position]

            CoroutineScope(Dispatchers.IO).launch {
                val db = NotesDatabase.getDatabase(context)
                db.getNoteDao().delete(noteToDelete)

                withContext(Dispatchers.Main) {
                    notes.removeAt(position)
                    notifyDataSetChanged()
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun editItem(position: Int) {
        val inflater = LayoutInflater.from(context)
        val myPopupView = inflater.inflate(R.layout.activity_add_note, null)
        val popupWindow = PopupWindow(myPopupView, 1200, 1500, true)
        popupWindow.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.popup_background
            )
        )
        popupWindow.isOutsideTouchable = true
        popupWindow.showAtLocation(myPopupView, Gravity.CENTER, 0, 0)

        val titleET = myPopupView.findViewById<EditText>(R.id.titleET)
        val noteET = myPopupView.findViewById<EditText>(R.id.noteET)
        val submitBtn = myPopupView.findViewById<Button>(R.id.submitBtn)

        val starIV = myPopupView.findViewById<ImageView>(R.id.starIV)
        val oneBtn = myPopupView.findViewById<Button>(R.id.oneBtn)
        val twoBtn = myPopupView.findViewById<Button>(R.id.twoBtn)
        val threeBtn = myPopupView.findViewById<Button>(R.id.threeBtn)
        val fourBtn = myPopupView.findViewById<Button>(R.id.fourBtn)
        val fiveBtn = myPopupView.findViewById<Button>(R.id.fiveBtn)

        var importance = 0

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

        val currentNote = notes[position]
        titleET.setText(currentNote.title)
        noteET.setText(currentNote.text)



        submitBtn.setOnClickListener {
            val updatedNote = currentNote.copy(
                title = titleET.text.toString(),
                text = noteET.text.toString(),
                importance = importance
            )

            CoroutineScope(Dispatchers.IO).launch {
                val db = NotesDatabase.getDatabase(context)
                db.getNoteDao().updateNote(updatedNote)

                withContext(Dispatchers.Main) {
                    notes[position] = updatedNote
                    notifyDataSetChanged()
                    popupWindow.dismiss()
                }
            }

        }

    }


}