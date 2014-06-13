package puzzle.slider.vn.util;

/**
 * @author thangtb
 * @sine Mar 15, 2013
 */
public class Constant {
	public static final String CUSTOM_SHAREDPREFERNCES = "custom_sharepref";
	public static final String WIDTH_TILE = "WIDTH_TILE";
	public static final String WIDTH_SCREEN = "WIDTH_SCREEN";
	public static final String HEIGHT_SCREEN = "HEIGHT_SCREEN";

	// api url
	public static final String API_URL_ITEMS = "items.json";
	public static final String API_URL_NOTICE = "notice.json";

	public static final boolean IS_TRUST_ALL_HOST = true;
	public static final boolean IS_PrintStackTrace = true;
	// ERROR CODE
	public static final int ERROR_HTTP_PROTOCOL = 1;
	public static final int ERROR_SOCKET_TIME_OUT = 2;
	public static final int ERROR_CAN_NOT_CONNECT_TO_SERVER = 3;
	public static final int CONNECTION_TIMEOUT = 60000;
	public static final String FOLDER_SAVE_DATA = "PuzzleData";

	public static final String FOLDER_TEMPLATE = "templates";	
	public static final String FOLDER_SLIDER = "sliders";
	public static final String FOLDER_BOARD = "boards";
	public static final String FOLDER_ITEM = "items";
	public static final String FOLDER_ITEM_DEFAULT = "default";

	public static final String PATH_NAME = "PathName";
	public static final String IMAGE_PATH = "ImagePath";
//	public static final String IMAGE_ID = "ImageID";
	public static final String GAME_ID = "GameID";
	public static final String GOOGLE_ID = "GoogleId";

	public static final String LEVEL_TIME = "time";
	public static final String LEVEL_DIFF = "diff";
	public static final String LEVEL_GUIDE1 = "guide1";
	public static final String LEVEL_GUIDE2 = "guide2";
	public static final String TEMPLATE = "template";
	public static final String TIME_SECOND = "timeSecond";
	public static final String FINISH_GAME = "finishGame";

	public static final int FINISH1 = 0;
	public static final int FINISH2 = 1;
	public static final int FINISH3 = 2;
	public static final String FINISH_TYPE = "finish_type";
	
	//sound content
	public static final String SOUND_A = "Puzzule_A_BGM1.mp3";
	public static final String SOUND_B = "Puzzule_B_decide.mp3";
	public static final String SOUND_C = "Puzzule_C_decide2.mp3";
	public static final String SOUND_D = "Puzzule_D_cancel.mp3";
	public static final String SOUND_E = "puzzule_e_bgm2.mp3";
	public static final String SOUND_F = "Puzzule_F_piece.mp3";
	public static final String SOUND_G = "puzzule_g_alert.mp3";
	public static final String SOUND_I = "puzzule_i_kansei_applause.mp3";
	public static final String SOUND_J = "puzzule_j_kansei_fanfare.mp3";
	public static final String SOUND_K = "puzzule_k_kansei_kira.mp3";
	public static final String SOUND_L = "puzzule_l_slider_piece.mp3";
	public static final String SOUND_L2 = "puzzule_l_slider_piece2.mp3";
	
	public static final String PATH_DATA = "path_data";
	
	public static final String FLAG_ANIM_ISLEFT = "anim";
	
	//for GCM
	public static final String SENDER_ID = "159037944414";
	public static final String PUSH_MESS = "message";
	
	//google billing
	// The possible states of an in-app purchase, as defined by Android Market.
    public enum PurchaseState {
        // Responses to requestPurchase or restoreTransactions.
        PURCHASED,   // User was charged for the order.
        CANCELED,    // The charge failed on the server.
        REFUNDED;    // User received a refund for the order.

        // Converts from an ordinal value to the PurchaseState
        public static PurchaseState valueOf(int index) {
            PurchaseState[] values = PurchaseState.values();
            if (index < 0 || index >= values.length) {
                return CANCELED;
            }
            return values[index];
        }
    }
    /** This is the action we use to bind to the MarketBillingService. */
    public static final String MARKET_BILLING_SERVICE_ACTION = "com.android.vending.billing.MarketBillingService.BIND";
    // These are the names of the extras that are passed in an intent from
    // Market to this application and cannot be changed.
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String INAPP_SIGNED_DATA = "inapp_signed_data";
    public static final String INAPP_SIGNATURE = "inapp_signature";
    public static final String INAPP_REQUEST_ID = "request_id";
    public static final String INAPP_RESPONSE_CODE = "response_code";

    // These are the names of the fields in the request bundle.
	public static final String ACTION_CONFIRM_NOTIFICATION = "com.example.subscriptions.CONFIRM_NOTIFICATION";
    public static final String ACTION_GET_PURCHASE_INFORMATION = "com.example.subscriptions.GET_PURCHASE_INFORMATION";
    public static final String ACTION_RESTORE_TRANSACTIONS = "com.example.subscriptions.RESTORE_TRANSACTIONS";
	
	public static final String ACTION_NOTIFY = "com.android.vending.billing.IN_APP_NOTIFY";
	public static final String ACTION_RESPONSE_CODE = "com.android.vending.billing.RESPONSE_CODE";
	public static final String ACTION_PURCHASE_STATE_CHANGED = "com.android.vending.billing.PURCHASE_STATE_CHANGED";
	    
	public static final String BILLING_REQUEST_METHOD = "BILLING_REQUEST";
    public static final String BILLING_REQUEST_API_VERSION = "API_VERSION";
    public static final String BILLING_REQUEST_PACKAGE_NAME = "PACKAGE_NAME";
    public static final String BILLING_REQUEST_ITEM_ID = "ITEM_ID";
    public static final String BILLING_REQUEST_ITEM_TYPE = "ITEM_TYPE";
    public static final String BILLING_REQUEST_DEVELOPER_PAYLOAD = "DEVELOPER_PAYLOAD";
    public static final String BILLING_REQUEST_NOTIFY_IDS = "NOTIFY_IDS";
    public static final String BILLING_REQUEST_NONCE = "NONCE";

    public static final String BILLING_RESPONSE_RESPONSE_CODE = "RESPONSE_CODE";
    public static final String BILLING_RESPONSE_PURCHASE_INTENT = "PURCHASE_INTENT";
    public static final String BILLING_RESPONSE_REQUEST_ID = "REQUEST_ID";
    public static long BILLING_RESPONSE_INVALID_REQUEST_ID = -1;

    // These are the types supported in the IAB v2
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBSCRIPTION = "subs";
    
    public static final boolean DEBUG = false;
    public enum ResponseCode {
        RESULT_OK,
        RESULT_USER_CANCELED,
        RESULT_SERVICE_UNAVAILABLE,
        RESULT_BILLING_UNAVAILABLE,
        RESULT_ITEM_UNAVAILABLE,
        RESULT_DEVELOPER_ERROR,
        RESULT_ERROR;

        // Converts from an ordinal value to the ResponseCode
        public static ResponseCode valueOf(int index) {
            ResponseCode[] values = ResponseCode.values();
            if (index < 0 || index >= values.length) {
                return RESULT_ERROR;
            }
            return values[index];
        }
    }
}
