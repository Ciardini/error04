import android.content.Context;

import us.zoom.sdk.MeetingService;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class ZoomVideocall {

    private ZoomSDK mZoomSDK;
    private final Context mContext;

    private ZoomSDKAuthenticationListener authenticationListener;

    public ZoomVideocall(Context context) {
        mZoomSDK = ZoomSDK.getInstance();
        mContext = context;
    }

    public void start() {
        // Phase #1: initialize sdk
        ZoomSDKInitParams initParams = new ZoomSDKInitParams();
        initParams.appKey = mContext.getString(R.string.zoom_app_key);
        initParams.appSecret = mContext.getString(R.string.zoom_app_secret);
        initParams.enableLog = true;
        initParams.domain = mContext.getString(R.string.zoom_domain);


        mZoomSDK.initialize(mContext, new ZoomSDKInitializeListener() {
            @Override
            public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
                if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
                    ...
                } else {
                    // Phase #1 -> #2
                    login();
                }
            }

            @Override
            public void onZoomAuthIdentityExpired() {
                ...
            }
        }, initParams);
    }

    private void login() {
        // Phase #2: login user
        int result = mZoomSDK.loginWithZoom("User", "Psw");
        if (result != ZoomApiError.ZOOM_API_ERROR_SUCCESS) {
            ...
        } else {
            ZoomSDKAuthenticationListener authenticationListener = new ZoomSDKAuthenticationListener() {
                @Override
                public void onZoomSDKLoginResult(long l) {
                    // Phase #2 -> #3
                    meeting();
                }

                @Override
                public void onZoomSDKLogoutResult(long l) {
                    int result = Math.toIntExact(l);
                    if (result != ZoomApiError.ZOOM_API_ERROR_SUCCESS) {
                        ...
                    }
                }

                @Override
                public void onZoomIdentityExpired() {
                    ...
                }

                @Override
                public void onZoomAuthIdentityExpired() {
                    ...
                }
            };
            mZoomSDK.addAuthenticationListener(authenticationListener);

            // Save for remove later
            this.authenticationListener = authenticationListener;
        }

    }

    private void meeting() {
        // Phase #3: start meeting
        MeetingService meetingService = mZoomSDK.getMeetingService();
        if (meetingService != null) {
            StartMeetingOptions options = new StartMeetingOptions();
            options.no_driving_mode = true;
            meetingService.startInstantMeeting(mContext, options);
        } else {
            ...
        }
    }

    public void reset() {
        mZoomSDK.logoutZoom();
    }

    public void destroy() {
        mZoomSDK.removeAuthenticationListener(authenticationListener);
        mZoomSDK = null;
    }
}