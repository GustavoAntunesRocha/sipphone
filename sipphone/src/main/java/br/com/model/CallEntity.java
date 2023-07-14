package br.com.model;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsua_call_media_status;

import br.com.controller.AccountController;
import br.com.controller.AppSettingsController;
import br.com.controller.CallController;
import br.com.controller.MainController;
import br.com.view.CallWindow;

public class CallEntity extends Call {

    private volatile boolean connected;
    private volatile boolean rejected;
    private boolean onHold;

    public CallEntity(AccountEntity acc, int call_id) {
        super(acc, call_id);
        connected = false;
        onHold = false;
        rejected = false;
    }

    public CallEntity(AccountEntity acc) {
        super(acc);
        connected = false;
        onHold = false;
        rejected = false;
    }

    @Override
    public void onCallState(OnCallStateParam prm) {
        try {
            CallInfo ci = getInfo();
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_INCOMING) {
                // Create a new ExecutorService with a single thread
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                // Submit the playWavFileInLoop method to the ExecutorService
                executorService.submit(() -> {
                    playWavFileInLoop(AppSettingsController.getInstance().getRingSoundFilePath(),
                            AppSettingsController.getInstance().getRingDevice());
                });

                // Shutdown the ExecutorService when you're done with it
                executorService.shutdown();
            }
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
                CallController.getInstance().showCallingAlert();
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                // Submit the playWavFileInLoop method to the ExecutorService
                executorService.submit(() -> {
                    playWavFileInLoop(AppSettingsController.getInstance().getRingSoundFilePath(),
                            AppSettingsController.getInstance().getRingDevice());
                });

                // Shutdown the ExecutorService when you're done with it
                executorService.shutdown();
            }
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                if (CallController.getInstance().getAlert() != null)
                    CallController.getInstance().closeAlertWindow();
                MainController.getInstance()
                        .addCallHistoryEntry(AccountController.getInstance().addCallHistoryEntry(this));
                CallController.getInstance().closeCallWindow();
                delete();
                setConnected(false);
                setRejected(true);
            } else if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                setConnected(true);
                CallController callController = CallController.getInstance();
                CallWindow callWindow = new CallWindow();
                callWindow.setCallerNumber(ci.getRemoteUri());
                callWindow.setCallerName(ci.getRemoteContact());
                callController.setCallWindow(callWindow, ci.getRemoteUri(), ci.getRemoteContact());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void playWavFileInLoop(String filePath, Mixer.Info selectedDevice) {
        try {
            // Get the audio input stream from the WAV file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

            // Get the audio format of the WAV file
            AudioFormat audioFormat = audioInputStream.getFormat();

            // Get the data line for the selected device and audio format
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            Mixer mixer = AudioSystem.getMixer(selectedDevice);
            SourceDataLine sourceDataLine = (SourceDataLine) mixer.getLine(dataLineInfo);

            // Open the data line and start playing the WAV file
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            // Read the audio data from the input stream and write it to the data line in a
            // loop
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while (!isConnected() && !isRejected()) {
                bytesRead = audioInputStream.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    // End of audio data
                    audioInputStream.close();
                    Thread.sleep(3500);
                    audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
                } else {
                    sourceDataLine.write(buffer, 0, bytesRead);
                }
            }

            // Stop playing the WAV file and close the data line and input stream
            sourceDataLine.stop();
            sourceDataLine.close();
            audioInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        try {
            CallInfo ci = getInfo();
            for (CallMediaInfo mi : ci.getMedia()) {
                if (mi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO
                        && mi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE) {
                    Media media = getMedia(mi.getIndex());
                    AudioMedia audioMedia = AudioMedia.typecastFromMedia(media);
                    AudDevManager audDevManager = Endpoint.instance().audDevManager();
                    audioMedia.startTransmit(audDevManager.getPlaybackDevMedia());
                    audDevManager.getCaptureDevMedia().startTransmit(audioMedia);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static CallEntity makeCall(AccountEntity accountEntity, String number) {
        CallEntity call = new CallEntity(accountEntity);
        CallOpParam prm = new CallOpParam(true);
        try {
            call.makeCall("sip:" + number + "@" + accountEntity.getAccountConfigModel().getDomain(), prm);
            // System.out.println("\n\n\n\n\nCall id: " + call.getId() + "\n\n");
            CallController.getInstance().setCallEntity(call);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return call;
    }

    public synchronized boolean isConnected() {
        return connected;
    }

    public synchronized void setConnected(boolean connected) {
        this.connected = connected;
    }

    public synchronized boolean isRejected() {
        return rejected;
    }

    public synchronized void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

}
