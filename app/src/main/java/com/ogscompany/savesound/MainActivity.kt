package com.ogscompany.savesound

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import java.io.File
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList


private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val REQUEST_WRITE_STORAGE_PERMISSION = 200
private const val REQUEST_STORAGE_PERMISSION = 200

class MainActivity : AppCompatActivity() {
    private val filePath : String = Environment.getExternalStorageDirectory().absolutePath
    private var files : ArrayList<String> = ArrayList()
    private var createDirSuccess : Boolean = false
    private var filePathForSave : String = Environment.getExternalStorageDirectory().absolutePath + "/RecordedAudios/"
    private var recorder: MediaRecorder? = null
    private val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
    private var swipeBackground : ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable
    private var permissionToRecordAccepted = false
    private var permissionToWriteAccepted = false
    private var permissionToReadAccepted = false
    private var permissions: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        permissionToWriteAccepted = if (requestCode == REQUEST_WRITE_STORAGE_PERMISSION) {
            grantResults[1] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        permissionToReadAccepted = if (requestCode == REQUEST_STORAGE_PERMISSION) {
            grantResults[2] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()

        if (!permissionToWriteAccepted) finish()

        if (permissionToWriteAccepted) createDirSuccess = createDir()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        setContentView(R.layout.activity_main)
    }
    override fun onResume()
    {
        super.onResume()
        if (createDirSuccess)
        resumeDir()
    }
    override fun onStop()
    {
        super.onStop()
        if (recorder != null) {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            bottomNavDrawerFragment.stopTimer()
            recyclerViewForAudios.adapter = AudiosAdapter(files, filePathForSave)
        }
    }
    override fun onDestroy()
    {
        super.onDestroy()
        if (recorder != null) {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            bottomNavDrawerFragment.stopTimer()
            recyclerViewForAudios.adapter = AudiosAdapter(files, filePathForSave)
        }

    }
    private fun createDir(): Boolean {
        val folder = File(filePath,"RecordedAudios")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val directory = File(filePathForSave)
        val filesInDir = directory.listFiles()
        for (element in filesInDir) {

            files.add(element.name)
        }
        recyclerViewForAudios.layoutManager = LinearLayoutManager(this)
        recyclerViewForAudios.adapter = AudiosAdapter(files, filePathForSave)
        recyclerViewForAudios.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                AudiosAdapter(files, filePathForSave).removeItem(viewHolder)
                recyclerViewForAudios.adapter = AudiosAdapter(files, filePathForSave)
                Toast.makeText(applicationContext, "Файл удалён из системы", Toast.LENGTH_LONG).show()
                placeHolder()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                val iconHeight = (itemView.height - deleteIcon.intrinsicHeight) / 2


                if (dX > 0) {
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(),itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconHeight,
                        itemView.top + iconHeight,
                        itemView.left + iconHeight + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconHeight)
                } else {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right,itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconHeight - deleteIcon.intrinsicWidth,
                        itemView.top + iconHeight,
                        itemView.right - iconHeight,
                        itemView.bottom - iconHeight)
                }
                swipeBackground.draw(c)

                c.save()

                if (dX > 0) {
                    c.clipRect(itemView.left, itemView.top, dX.toInt(),itemView.bottom)
                } else {
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right,itemView.bottom)
                }

                deleteIcon.draw(c)

                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewForAudios)
        return true
    }

    private fun resumeDir() {
        val folder = File(filePath, "RecordedAudios")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val directory = File(filePathForSave)
        val filesInDir = directory.listFiles()
        files = ArrayList()
        for (element in filesInDir) {

            files.add(element.name)
        }
        recyclerViewForAudios.adapter = AudiosAdapter(files, filePathForSave)
        placeHolder()
    }
    fun startRecording(view: View) {
        bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        val date : Long = Date().time
        Toast.makeText(this, "Record!!!", Toast.LENGTH_LONG).show()
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setAudioChannels(1)
            setAudioSamplingRate(8000)
            setAudioEncodingBitRate(44100)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            files.add("$date.mp3")
            setOutputFile("$filePathForSave$date.mp3")
            prepare()
            start()
        }
    }
    fun stopRecording(view: View) {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        bottomNavDrawerFragment.stopTimer()
        recyclerViewForAudios.adapter = AudiosAdapter(files, filePathForSave)
        Toast.makeText(applicationContext, "Запись сохранена", Toast.LENGTH_LONG).show()
        placeHolder()
    }
    fun placeHolder()
    {
        val placeHolder = findViewById<View>(R.id.placeHolder) as TextView
        placeHolder.isVisible = files.size == 0
    }
}