package com.example.application

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
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
import kotlin.reflect.typeOf

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

        try {
            val file = File(filesDir, "user_data.json")

            val writer = FileWriter(file, false) // 'false' ensures overwriting, not appending
            writer.write("") // Pretty-print JSON
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Save JSON data when the button is clicked
        saveButton.setOnClickListener {
            if (inputKennzeichen1.text.isEmpty() or inputKennzeichen2.text.isEmpty() or inputKennzeichen3.text.isEmpty() or inputIdent.text.isEmpty() or inputBrief.text.isEmpty() or inputVorne.text.isEmpty() or inputHinten.text.isEmpty()) {
                Toast.makeText(this, "Feld kann nicht leer sein", Toast.LENGTH_LONG).show()
            } else if (inputKennzeichen1.text.length != 1 && inputKennzeichen1.text.length != 2 && inputKennzeichen1.text.length != 3) {
                Toast.makeText(this, "Landkreis nicht korrekt angegeben", Toast.LENGTH_LONG).show()
            } else if (inputKennzeichen2.text.length != 1 && inputKennzeichen2.text.length != 2) {
                Toast.makeText(this, "Buchstabe der Erkennungsnummer nicht korrekt angegeben", Toast.LENGTH_LONG).show()
            } else if (inputKennzeichen3.text.length != 1 && inputKennzeichen3.text.length != 2 && inputKennzeichen3.text.length != 3 && inputKennzeichen3.text.length != 4) {
                Toast.makeText(this, "Nummer der Erkennungsnummer nicht korrekt angegeben", Toast.LENGTH_LONG).show()
            }
            else if (inputIdent.text.length != 15 && inputIdent.text.length != 16 && inputIdent.text.length != 17){
                Toast.makeText(this, "Fahrzeug-Identifizierungsnummer ist nicht korrekt", Toast.LENGTH_LONG).show()
            }
            else if (inputBrief.text.length != 8) {
                Toast.makeText(this, "Sicherheitscode des Briefes ist nicht korrekt", Toast.LENGTH_LONG).show()
            }
            else if (inputVorne.text.length != 3) {
                Toast.makeText(this, "Code vorne ist nicht korrekt", Toast.LENGTH_LONG).show()
            }
            else if (inputHinten.text.length != 3) {
                Toast.makeText(this, "Code hinten ist nicht korrekt", Toast.LENGTH_LONG).show()
            }
            else {
                val jsonObject = JSONObject()

                var inputKennzeichen : Editable = Editable.Factory.getInstance().newEditable("")
                inputKennzeichen.append(inputKennzeichen1.text)
                inputKennzeichen.append("-")
                inputKennzeichen.append(inputKennzeichen2.text)
                inputKennzeichen.append("-")
                inputKennzeichen.append(inputKennzeichen3.text)

                jsonObject.put("Kennzeichen", inputKennzeichen)
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

        // Read JSON data when the button is clicked
        readButton.setOnClickListener {
            val jsonData = readJsonFromFile()
            textView.text = jsonData ?: "No JSON data found."
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