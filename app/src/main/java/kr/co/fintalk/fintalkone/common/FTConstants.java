package kr.co.fintalk.fintalkone.common;

/**
 * Created by BeomyongChoi on 6/22/16.
 */
public class FTConstants {
    /**
     * 기본 Action 값.
     */
    public final static String ACTION = "kr.co.fintalk.fintalkone.action";
    /**
     * Network Action.
     */
    public static final String ACTION_NETWORK = ACTION.concat(".NETWORK");

    /**
     * 종료 Action.
     */
    public static final String ACTION_FINISH = ACTION.concat(".FINISH");

    /**
     * 하위 Activity 종료 Action.
     */
    public static final String ACTION_SUB_FINISH = ACTION.concat(".SUB_FINISH");

    /**
     *
     */
    public final static String ACTION_RECEIVE_ALL = ACTION.concat(".RECEIVE_ALL");

    /**
     *
     */
    public final static String ACTION_RECEIVE_ACTIVATION = ACTION.concat(".RECEIVE_ACTIVATION");

    /**
     *
     */
    //public final static String RECEIVE_ALWAYS_ACTION = ACTION.concat(".RECEIVE_ALWAYS");

	/*
	 *
	 */
    public final static String ACTION_ACTIVITY_FINISH = ACTION.concat(".ACTIVITY_FINISH");
    public final static String ACTION_GCM = ACTION.concat(".GCM");
    public final static String ACTION_RECEIVE_SMS = ACTION.concat(".RECEIVE_SMS");
    public final static String ACTION_GOOGLE_STT = ACTION.concat(".GOOGLE_STT");
    public final static String ACTION_ZXING = ACTION.concat(".ZXING");
    public final static String ACTION_READ_PHONE_STATE = ACTION.concat(".READ_PHONE_STATE");
    public final static String ACTION_TELEPHONE_RECEIVER = ACTION.concat(".TELEPHONE_RECEIVER");
    public final static String ACTION_THEME_ACTIVITY = ACTION.concat(".THEME_ACTIVITY");

    /**
     *
     */
    public static final int REQUEST_CODE_CALL_PHONE = 111;
    public static final int REQUEST_CODE_READ_PHONE_STATE = 112;
    public static final int REQUEST_CODE_PROCESS_OUTGOING_CALLS = 113;

    /**
     *
     */
    public static final int REQUEST_CODE_SEND_SMS = 121;
    public static final int REQUEST_CODE_RECEIVE_SMS = 122;
    public static final int REQUEST_CODE_RECEIVE_MMS = 123;
    public static final int REQUEST_CODE_CAMERA = 151;
    public static final int REQUEST_CODE_EXTERNAL_STORAGE = 161;

    /**
     * ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION RequestCode.
     */
    public static final int REQUEST_CODE_LOCATION = 191;

    /**
     * 다른 앱 위에 그리기.
     */
    public static final int REQUEST_CODE_SYSTEM_ALERT_WINDOW = 1101;

    /**
     * 카메라 사진 촬영 RequestCode.
     */
    public static final int REQUEST_CODE_CAPTURE_ACTIVITY = 2101;

    /**
     * Google Voice Search RequestCode.
     */
    public static final int REQUEST_CODE_GOOGLE_STT = 7101;

    /**
     *
     */
    public static final int REQUEST_CODE_LOCATION_SOURCE_SETTINGS = 8111;

    /**
     * User - ReceptionNewAgreement
     */
    public final static int REQUEST_TAKE_PICTURE = 9151;


    /**
     *
     */
    public final static String RESULT_CODE = "resultCode";

    /**
     *
     */
    public final static String INTENT_ACTION = "intentAction";

    /**
     *
     */
    public final static String INTENT_SUB_ACTION = "intentSubAction";

    /**
     *
     */
    public final static String INTENT_KEY = "intentKey";

    /**
     *
     */
    //public final static String INTENT_BUNDLE = "intentBundle";
    public final static String INTENT_DATA = "intentData";
    public final static String INTENT_DATA2 = "intentData2";
    public final static String INTENT_DATA3 = "intentData3";
    public final static String INTENT_DATA4 = "intentData4";
    public final static String INTENT_DATA5 = "intentData5";

    public final static String ITEM_TITLE = "title";
    public final static String ITEM_CONTENTS = "contents";

    public final static int TYPE_SECTION_HEADER = 0;
}
