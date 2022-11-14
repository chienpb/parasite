package client;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;

public class test {

    private static String s = "";
    private static NativeKeyListener nativeKeyListener = new NativeKeyListener() {
        @Override
        public void nativeKeyTyped(NativeKeyEvent nativeEvent)
        {
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeEvent)
        {
            String keyText=NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
            s = s + " " + keyText;
            System.out.println(s);
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeEvent)
        {
        }
    };

    public static void main(String[] args) throws NativeHookException {
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(nativeKeyListener);
    }
}
