package pjsipphone.entity;

import org.pjsip.pjsua2.AudDevManager;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsua_call_media_status;

public class CallEntity extends Call{

    private boolean connected;
    private boolean onHold;

    public CallEntity(AccountEntity acc, int call_id) {
        super(acc, call_id);
        connected = false;
        onHold = false;
    }
    
    @Override
    public void onCallState(OnCallStateParam prm) {
        try {
            CallInfo ci = getInfo();
            connected = ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED;
            System.out.println("Call " + ci.getCallIdString() + " state=" + ci.getStateText());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onCallMediaState(OnCallMediaStateParam prm){
        try {
            CallInfo ci = getInfo();
            for (CallMediaInfo mi : ci.getMedia()) {
                if(mi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO && mi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE){
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
    
}
