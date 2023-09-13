package com.deokar.securitylab

import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class MainActivity : AppCompatActivity() {

    private var encryptedValue: String = ""
    private var decryptedValue: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMessage: EditText = findViewById(R.id.editTextMessage)
        val etPassword: EditText = findViewById(R.id.editTextPassword)
        val tvOutput: TextView = findViewById(R.id.textView)
        val btnEncrypt: Button = findViewById(R.id.btnEncrypt)
        val btnDecrypt: Button = findViewById(R.id.btnDecrypt)

        btnEncrypt.setOnClickListener {
            val message = etMessage.text.toString()
            if (message.isEmpty()) {
                showToast("message is empty")
                return@setOnClickListener
            }
            val password = etPassword.text.toString()
            if (password.isEmpty()) {
                showToast("password is empty")
                return@setOnClickListener
            }

            try {
                val encrypedText = encrypt(message, password)
                tvOutput.text = encrypedText
            } catch (e: java.lang.Exception) {
                showToast("could not encrypt")
            }
        }

        btnDecrypt.setOnClickListener {
            val encryptedMessage = tvOutput.text.toString()
            if (encryptedMessage.isEmpty()) {
                showToast("encrypted value is empty")
                return@setOnClickListener
            }
            val password = etPassword.text.toString()
            if (password.isEmpty()) {
                showToast("password is empty")
                return@setOnClickListener
            }

            try {
                val decrypedText = decrypt(encryptedMessage, password)
                tvOutput.text = decrypedText
            } catch (e: java.lang.Exception) {
                showToast("Incorrect password, try again please")
            }

        }
    }

    @Throws(java.lang.Exception::class)
    private fun encrypt(data: String, pass: String): String? {
        val key: SecretKeySpec? = generateKey(pass)
        val c: Cipher = Cipher.getInstance("AES")
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal: ByteArray = c.doFinal(data.toByteArray())
        encryptedValue = Base64.encodeToString(
            encVal,
            Base64.DEFAULT
        )
        return encryptedValue
    }

    @Throws(java.lang.Exception::class)
    private fun decrypt(outputString: String, pass: String): String? {
        val key: SecretKeySpec? = generateKey(pass)
        val c: Cipher = Cipher.getInstance("AES")
        c.init(Cipher.DECRYPT_MODE, key)
        val decodedValue: ByteArray = Base64.decode(
            outputString,
            Base64.DEFAULT
        )
        val decValue: ByteArray = c.doFinal(decodedValue)
        decryptedValue = String(decValue)
        return decryptedValue
    }

    @Throws(Exception::class)
    private fun generateKey(pass: String): SecretKeySpec? {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = pass.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key: ByteArray = digest.digest()
        return SecretKeySpec(key, "AES")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}