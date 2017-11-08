package de.test.themistestapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.cossacklabs.themis.NullArgumentException;
import com.cossacklabs.themis.PrivateKey;
import com.cossacklabs.themis.PublicKey;
import com.cossacklabs.themis.SecureMessage;
import com.cossacklabs.themis.SecureMessageWrapException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainActivitySecureMessage extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Check secure message
            try {

                //SecMessageExampleClient.SMessageCIClientTest();

                runSimpleLocalCheck();

            } catch (NullArgumentException e) {
                e.printStackTrace();
            } catch (SecureMessageWrapException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void runSimpleLocalCheck() throws UnsupportedEncodingException, NullArgumentException, SecureMessageWrapException {

            Charset charset = StandardCharsets.UTF_8;
            String clientPrivateKey = "UkVDMgAAAC1EvnquAPUxxwJsoJxoMAkEF7c06Fo7dVwnWPnmNX5afyjEEGmG";
            String serverPublicKey = "VUVDMgAAAC1FJv/DAmg8/L1Pl5l6ypyRqXUU9xQQaAgzfRZ+/gsjqgEdwXhc";

            String message = "some weird message here";

            PrivateKey privateKey = new PrivateKey(Base64.decode(clientPrivateKey.getBytes(charset.name()), Base64.NO_WRAP));
            PublicKey publicKey = new PublicKey(Base64.decode(serverPublicKey.getBytes(charset.name()), Base64.NO_WRAP));
            Log.d("SMC", "privateKey1 = " + Arrays.toString(privateKey.toByteArray()));
            Log.d("SMC", "publicKey1 = " + Arrays.toString(publicKey.toByteArray()));

            final SecureMessage sm = new SecureMessage(privateKey, publicKey);

            byte[] wrappedMessage = sm.wrap(message.getBytes(charset.name()));
            String encodedMessage = Base64.encodeToString(wrappedMessage, Base64.NO_WRAP);
            Log.d("SMC", "EncodedMessage = " + encodedMessage);

            byte[] wrappedMessageFromB64 = Base64.decode(encodedMessage, Base64.NO_WRAP);
            String decodedMessage = new String(sm.unwrap(wrappedMessageFromB64), charset);
            Log.d("SMC", "DecodedMessageFromOwnCode = " + decodedMessage);

            String encodedMessageFromExternal = "ICcEJksAAAAAAQFADAAAABAAAAAXAAAAZH8poysspqhDVpjH3LPLOH88S5YlRsxinK/q+tbhgm7REwFjcf2rfE5K3SjxgK1HS9bg";
            byte[] wrappedMessageFromB64External = Base64.decode(encodedMessageFromExternal, Base64.NO_WRAP);
            String decodedMessageExternal = new String(sm.unwrap(wrappedMessageFromB64External), charset);
            Log.d("SMC", "DecodedMessageFromExternal = " + decodedMessageExternal);
        }
    }
