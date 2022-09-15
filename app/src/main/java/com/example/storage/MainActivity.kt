package com.example.storage

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.example.storage.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.createFileBtn.setOnClickListener {
            createNewExternalFile()
        }
        binding.readFileBtn.setOnClickListener {
            readFromExternalFile()
        }
        checkExternalStorageState()
    }

    private fun checkExternalStorageState() {
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            Toast.makeText(this, "yes mounted", Toast.LENGTH_SHORT).show()
    }
    private fun readFromInternalFile() {
        try {
            val fileName = binding.fileNameEt.text.toString()
            val fileData = File(filesDir,fileName)

            /**
             * We can use openFileInput or file.read
             */
            /*val fileText = openFileInput(fileName).bufferedReader(StandardCharsets.UTF_8).use {
                it.readText()
            }*/
            binding.fileContentTv.text = fileData.readText()
            Snackbar.make(this,binding.root,"file read",Snackbar.LENGTH_SHORT).show()
        }catch (exception:Exception){
            Snackbar.make(this,binding.root,"file ${exception.localizedMessage}",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun createNewInternalFile() {
        val fileName = binding.fileNameEt.text.toString()
        val file = File(this.filesDir,fileName)
        val os = openFileOutput("",MODE_PRIVATE)

        if(!file.exists()){
            file.createNewFile()
        }
        Snackbar.make(this,binding.root,"file created",Snackbar.LENGTH_SHORT).show()
        file.writeText(binding.fileContentEt.text.toString())

    }
    private fun readFromExternalFile() {
        try {
            val fileName = binding.fileNameEt.text.toString()
            val parentPath = File(Environment.getExternalStorageDirectory(),"Pictures")
            val fileData = File(parentPath,
                fileName)
            /*val fileData = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                fileName)*/
            val uri = FileProvider.getUriForFile(this,"com.example.storage.provider",fileData)
            Log.i("fileloc",uri.toString())
            val intent = ShareCompat.IntentBuilder(this)
                .setChooserTitle("Sharing file")
                .setType("image/*")
                .setStream(uri)
                .createChooserIntent()
            startActivity(intent)
            val bitmap = BitmapFactory.decodeStream(fileData.inputStream())
           binding.imageView.setImageBitmap(bitmap)
            Snackbar.make(this,binding.root,"file read",Snackbar.LENGTH_SHORT).show()
        }catch (exception:Exception){
            binding.fileContentTv.text = exception.localizedMessage
            Log.i("fileloc",exception.localizedMessage)

            Snackbar.make(this,binding.root,"file ${exception.localizedMessage}",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun createNewExternalFile() {
        val fileName = binding.fileNameEt.text.toString()
        val image  = resources.openRawResource(R.raw.logoquest)

        binding.fileContentTv.text = "public "+Environment.getExternalStoragePublicDirectory(Environment
            .DIRECTORY_PICTURES).absolutePath +" \n external"+ Environment
            .getExternalStorageDirectory().absolutePath +"external files dir"+getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName)

        val uri = FileProvider.getUriForFile(this,"${applicationContext.packageName}.provider",file)
        Log.i("fileloc",uri.toString())
        if(!file.exists()){
            file.createNewFile()
        }
        Snackbar.make(this,binding.root,"file created",Snackbar.LENGTH_SHORT).show()
        file.writeBytes(image.readBytes())
        file.writeText(binding.fileContentEt.text.toString())
    }
}