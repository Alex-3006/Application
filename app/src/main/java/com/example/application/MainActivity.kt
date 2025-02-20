package com.example.application

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val fileName = "user_data.json"
    private lateinit var imageCameraKennzeichen: ImageView
    private lateinit var imageCameraIdent: ImageView
    private lateinit var imageCameraBrief: ImageView
    private lateinit var imageCameraVorne: ImageView
    private lateinit var imageCameraHinten: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputKennzeichen1 = findViewById<EditText>(R.id.inputKennzeichen1)
        val inputKennzeichen2 = findViewById<EditText>(R.id.inputKennzeichen2)
        val inputKennzeichen3 = findViewById<EditText>(R.id.inputKennzeichen3)
        val inputIdent = findViewById<EditText>(R.id.inputIdent)
        val inputBrief = findViewById<EditText>(R.id.inputBrief)
        val inputVorne = findViewById<EditText>(R.id.inputVorne)
        val inputHinten = findViewById<EditText>(R.id.inputHinten)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val readButton = findViewById<Button>(R.id.readButton)
        val textView = findViewById<TextView>(R.id.textView)

        // Save JSON data when the button is clicked
        saveButton.setOnClickListener {
            if (inputKennzeichen.text.isEmpty() or inputIdent.text.isEmpty() or inputBrief.text.isEmpty() or inputVorne.text.isEmpty() or inputHinten.text.isEmpty()) {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else {
                val jsonObject = JSONObject()
                jsonObject.put("Kennzeichen", inputKennzeichen.text)
                jsonObject.put("imageKennzeichen", imageCameraKennzeichen.toString())
                jsonObject.put("Ident", inputIdent.text)
                jsonObject.put("imageIdent", imageCameraIdent.toString())
                jsonObject.put("Brief", inputBrief.text)
                jsonObject.put("imageBrief", imageCameraBrief.toString())
                jsonObject.put("Vorne", inputVorne.text)
                jsonObject.put("imageVorne", imageCameraVorne.toString())
                jsonObject.put("Hinten", inputHinten.text)
                jsonObject.put("imageHinten", imageCameraHinten.toString())

                if (writeJsonToFile(jsonObject)) {
                    textView.text = "JSON saved successfully!"
                } else {
                    textView.text = "Failed to save JSON."
                }
            }
        }

        var countOpened = 0

        val infoKennzeichen = findViewById<Button>(R.id.infoKennzeichen)
        var isKennzeichenToggled = false
        val imageKennzeichen = findViewById<ImageView>(R.id.imageKennzeichen)
        imageKennzeichen.isVisible = false
        infoKennzeichen.setOnClickListener{
            if(isKennzeichenToggled) {
                imageKennzeichen.isVisible = false
                isKennzeichenToggled = false
                countOpened--
            } else if (!isKennzeichenToggled && countOpened == 0) {
                imageKennzeichen.isVisible = true
                isKennzeichenToggled = true
                countOpened++
            }
        }

        val infoIdent = findViewById<Button>(R.id.infoIdent)
        var isIdentToggled = false
        val imageIdent = findViewById<ImageView>(R.id.imageIdent)
        imageIdent.isVisible = false
        infoIdent.setOnClickListener{
            if(isIdentToggled) {
                imageIdent.isVisible = false
                isIdentToggled = false
                countOpened--
            } else if (!isIdentToggled && countOpened == 0) {
                imageIdent.isVisible = true
                isIdentToggled = true
                countOpened++
            }
        }

        val infoBrief = findViewById<Button>(R.id.infoBrief)
        var isBriefToggled = false
        val imageBrief = findViewById<ImageView>(R.id.imageBrief)
        imageBrief.isVisible = false
        infoBrief.setOnClickListener{
            if(isBriefToggled) {
                imageBrief.isVisible = false
                isBriefToggled = false
                countOpened--
            } else if (!isBriefToggled && countOpened == 0) {
                imageBrief.isVisible = true
                isBriefToggled = true
                countOpened++
            }
        }

        val infoVorne = findViewById<Button>(R.id.infoVorne)
        var isVorneToggled = false
        val imageVorne = findViewById<ImageView>(R.id.imageVorne)
        imageVorne.isVisible = false
        infoVorne.setOnClickListener{
            if(isVorneToggled) {
                imageVorne.isVisible = false
                isVorneToggled = false
                countOpened--
            } else if (!isVorneToggled && countOpened == 0) {
                imageVorne.isVisible = true
                isVorneToggled = true
                countOpened++
            }
        }

        val infoHinten = findViewById<Button>(R.id.infoHinten)
        var isHintenToggled = false
        val imageHinten = findViewById<ImageView>(R.id.imageHinten)
        imageHinten.isVisible = false
        infoHinten.setOnClickListener{
            if(isHintenToggled) {
                imageHinten.isVisible = false
                isHintenToggled = false
                countOpened--
            } else if (!isHintenToggled && countOpened == 0) {
                imageHinten.isVisible = true
                isHintenToggled = true
                countOpened++
            }
        }

        // Read JSON data when the button is clicked
        readButton.setOnClickListener {
            val jsonData = readJsonFromFile()
            textView.text = jsonData ?: "No JSON data found."
        }

        var cameraKennzeichen: Button
        cameraKennzeichen = findViewById(R.id.cameraKennzeichen)
        imageCameraKennzeichen = findViewById(R.id.imageCameraKennzeichen)
        imageCameraKennzeichen.isVisible = false

        cameraKennzeichen.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraResultLauncherKennzeichen.launch(intent)
        }

        var cameraIdent: Button
        cameraIdent = findViewById(R.id.cameraIdent)
        imageCameraIdent = findViewById(R.id.imageCameraIdent)
        imageCameraIdent.isVisible = false

        cameraIdent.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraResultLauncherIdent.launch(intent)
        }

        var cameraBrief: Button
        cameraBrief = findViewById(R.id.cameraBrief)
        imageCameraBrief = findViewById(R.id.imageCameraBrief)
        imageCameraBrief.isVisible = false

        cameraBrief.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraResultLauncherBrief.launch(intent)
        }

        var cameraVorne: Button
        cameraVorne = findViewById(R.id.cameraVorne)
        imageCameraVorne = findViewById(R.id.imageCameraVorne)
        imageCameraVorne.isVisible = false

        cameraVorne.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraResultLauncherVorne.launch(intent)
        }

        var cameraHinten: Button
        cameraHinten = findViewById(R.id.cameraHinten)
        imageCameraHinten = findViewById(R.id.imageCameraHinten)
        imageCameraHinten.isVisible = false

        cameraHinten.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraResultLauncherHinten.launch(intent)
        }
    }

    // Register for activity result
    private val cameraResultLauncherKennzeichen = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val capturedImageUriString = result.data?.getStringExtra("capturedImageUri")
            if (!capturedImageUriString.isNullOrEmpty()) {
                val capturedImageUri = Uri.parse(capturedImageUriString)
                imageCameraKennzeichen.setImageURI(capturedImageUri) // Show image in ImageView
            }
        }
    }

    private val cameraResultLauncherIdent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val capturedImageUriString = result.data?.getStringExtra("capturedImageUri")
            if (!capturedImageUriString.isNullOrEmpty()) {
                val capturedImageUri = Uri.parse(capturedImageUriString)
                imageCameraIdent.setImageURI(capturedImageUri) // Show image in ImageView
            }
        }
    }

    private val cameraResultLauncherBrief = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val capturedImageUriString = result.data?.getStringExtra("capturedImageUri")
            if (!capturedImageUriString.isNullOrEmpty()) {
                val capturedImageUri = Uri.parse(capturedImageUriString)
                imageCameraBrief.setImageURI(capturedImageUri) // Show image in ImageView
            }
        }
    }

    private val cameraResultLauncherVorne = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val capturedImageUriString = result.data?.getStringExtra("capturedImageUri")
            if (!capturedImageUriString.isNullOrEmpty()) {
                val capturedImageUri = Uri.parse(capturedImageUriString)
                imageCameraVorne.setImageURI(capturedImageUri) // Show image in ImageView
            }
        }
    }

    private val cameraResultLauncherHinten = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val capturedImageUriString = result.data?.getStringExtra("capturedImageUri")
            if (!capturedImageUriString.isNullOrEmpty()) {
                val capturedImageUri = Uri.parse(capturedImageUriString)
                imageCameraHinten.setImageURI(capturedImageUri) // Show image in ImageView
            }
        }
    }

    private fun writeJsonToFile(jsonObject: JSONObject): Boolean {
        return try {
            val file = File(filesDir, "user_data.json")

            val writer = FileWriter(file, false) // 'false' ensures overwriting, not appending
            writer.write(jsonObject.toString(4)) // Pretty-print JSON
            writer.flush()
            writer.close()

            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    // Function to read JSON data from the file
    private fun readJsonFromFile(): String? {
        val file = File(filesDir, fileName)
        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }
}