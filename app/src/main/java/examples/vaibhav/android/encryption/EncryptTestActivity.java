
package examples.vaibhav.android.encryption;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptTestActivity extends AppCompatActivity
{
	private static final String kUTF8 = "UTF8";
	private static final String kAES = "AES";
	private static String kCryptoPassKey = "UserVoiceEncrypt";
	private static final String kIVSpecKey = "initialVector123"; // 16 bytes IV;
	static final String kAesTransformation = "AES/CBC/PKCS5Padding";

	private EditText editText;
	private TextView textToShow;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_encrypt_test);
		editText = (EditText)findViewById(R.id.textToEncrypt);
		textToShow = (TextView) findViewById(R.id.showText);
	}


	public void onEncryptClick(View view) {
		String textToEncrypt = editText.getText().toString().trim();
		String encrypted = encryptIt(textToEncrypt);
		addToUserVoiceTicket(encrypted);
		textToShow.setText(encrypted);
	}


	private void addToUserVoiceTicket(String encrypted)
	{
		// TODO: add the UserVoice ticket attachment code here
	}


	public void onDecryptClick(View view) {
		String textToDecrypt = textToShow.getText().toString().trim();
		String decrypted = decryptIt(textToDecrypt);
		textToShow.setText(decrypted);
	}

	public String encryptIt(String encryptedText)
	{
		if (TextUtils.isEmpty(encryptedText)) {
			return encryptedText;
		}

		try
		{
			IvParameterSpec ivParameter = new IvParameterSpec(kIVSpecKey.getBytes(kUTF8));
			SecretKeySpec secretKeySpec = new SecretKeySpec(kCryptoPassKey.getBytes(kUTF8), kAES);
			byte[] clearText = encryptedText.getBytes(kUTF8);
			// Cipher is not backgroundThread safe
			Cipher cipher = Cipher.getInstance(kAesTransformation);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameter);

			byte[] text = cipher.doFinal(clearText);
			return Base64.encodeToString(text, Base64.DEFAULT);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return encryptedText;
	}


	public String decryptIt(String encryptedText)
	{
		if (TextUtils.isEmpty(encryptedText))
		{
			return "";
		}
		try
		{
			IvParameterSpec ivParameter = new IvParameterSpec(kIVSpecKey.getBytes(kUTF8));
			SecretKeySpec secretKeySpec = new SecretKeySpec(kCryptoPassKey.getBytes(kUTF8), kAES);

			byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
			// cipher is not backgroundThread safe
			Cipher cipher = Cipher.getInstance(kAesTransformation);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameter);
			byte[] decryptedBytes = (cipher.doFinal(encryptedBytes));

			return new String(decryptedBytes);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return encryptedText;
	}
}
