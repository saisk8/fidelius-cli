package com.mgrm.fidelius;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgrm.fidelius.decryption.DecryptionController;
import com.mgrm.fidelius.decryption.DecryptionRequest;
import com.mgrm.fidelius.decryption.DecryptionResponse;
import com.mgrm.fidelius.encryption.EncryptionController;
import com.mgrm.fidelius.encryption.EncryptionRequest;
import com.mgrm.fidelius.encryption.EncryptionResponse;
import com.mgrm.fidelius.keypairgen.KeyMaterial;
import com.mgrm.fidelius.keypairgen.KeyPairGenController;

public class FideliusApplication {

	public static void main(String[] args) throws Exception {
		Boolean invalidArgs = Utils.validateCliArguments(args);
		if (invalidArgs) {
			return;
		}

		Gson gson = new GsonBuilder()
			.disableHtmlEscaping()
			.setPrettyPrinting()
			.create();

		try {
			switch (args[0]) {
				case "e":
				case "encrypt":
					{
						int i = 0;
						final String stringToEncrypt = args[++i];
						final String senderNonce = args[++i];
						final String requesterNonce = args[++i];
						final String senderPrivateKey = args[++i];
						final String requesterPublicKey = args[++i];

						final EncryptionRequest encryptionRequest = new EncryptionRequest(
							stringToEncrypt,
							senderNonce,
							requesterNonce,
							senderPrivateKey,
							requesterPublicKey
						);

						EncryptionResponse encryptionResponse = new EncryptionController()
						.encrypt(encryptionRequest);
						System.out.println(gson.toJson(encryptionResponse));
						break;
					}
				case "d":
				case "decrypt":
					{
						int i = 0;
						final String encryptedData = args[++i];
						final String requesterNonce = args[++i];
						final String senderNonce = args[++i];
						final String requesterPrivateKey = args[++i];
						final String senderPublicKey = args[++i];

						final DecryptionRequest decryptionRequest = new DecryptionRequest(
							encryptedData,
							requesterNonce,
							senderNonce,
							requesterPrivateKey,
							senderPublicKey
						);

						DecryptionResponse decryptionResponse = new DecryptionController()
						.decrypt(decryptionRequest);
						System.out.println(gson.toJson(decryptionResponse));
						break;
					}
				case "gkm":
				case "generate-key-material":
					{
						KeyMaterial keyMaterial = new KeyPairGenController()
							.generate();
						System.out.println(gson.toJson(keyMaterial));
						break;
					}
				default:
					throw new IllegalArgumentException("Unsupported argument");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
