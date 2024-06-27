package com.dynamicFool.pauseMate.controllers;


import com.dynamicFool.pauseMate.components.schemas.ControlRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;


@RestController
@RequestMapping("/api")
public class MediaControlController {

    static {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
                e.printStackTrace();
        }
    }

    private void simulateKeyPress(int keyCode) {
        try {
            NativeKeyEvent keyPress = new NativeKeyEvent(
                    NativeKeyEvent.NATIVE_KEY_PRESSED,
                    (int) System.currentTimeMillis(),
                    179,
                    keyCode,
                    NativeKeyEvent.CHAR_UNDEFINED
            );
            GlobalScreen.postNativeEvent(keyPress);

            NativeKeyEvent keyRelease = new NativeKeyEvent(
                    NativeKeyEvent.NATIVE_KEY_RELEASED,
                    (int) System.currentTimeMillis(),
                    179,
                    keyCode,
                    NativeKeyEvent.CHAR_UNDEFINED
            );
            GlobalScreen.postNativeEvent(keyRelease);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

@PostMapping(value = "/control")
    public ResponseEntity<String> control(@RequestBody ControlRequest controlRequest) {
    String action  = controlRequest.getAction();
    System.out.println("Action : "+ action);
    switch (action) {
        case "play" :
        case "pause" :
            simulateKeyPress(NativeKeyEvent.VC_MEDIA_PLAY);
            break;
        case "stop" :
            simulateKeyPress(NativeKeyEvent.VC_MEDIA_STOP);
            break;
        default:
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Action");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Success");
    }
}

