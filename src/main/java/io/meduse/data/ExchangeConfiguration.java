package io.meduse.data;

public class ExchangeConfiguration {

  public static final String AWS_ACCESS_KEY = loadStringValue("AWS_ACCESS_KEY", "accesskey");
  public static final String AWS_ACCESS_PASSWORD = loadStringValue("AWS_ACCESS_PASSWORD",
      "secretkey");
  public static final String AWS_END_POINT = loadStringValue("AWS_END_POINT",
      "http://localhost:4566");
  public static final String AWS_REGION = loadStringValue("AWS_REGION", "eu-central-1");
  public static final String AWS_INCOMMING_QUEUE_NAME = loadStringValue("AWS_INCOMMING_QUEUE_NAME",
      "TRADE_MESSAGE");
  public static final String AWS_OUTGOING_QUEUE_NAME = loadStringValue("AWS_OUTGOING_QUEUE_NAME",
      "TRADE_RESULT");

  public static final String TRY_TO_CREATE_QUEUE = loadStringValue("TRY_TO_CREATE_QUEUE", "yes");

  public static final int WEB_SERVICE_PORT = loadIntValue("WEB_SERVICE_PORT", 8080);
  public static final String CALL_BACK_HOST = loadStringValue("CALL_BACK_HOST", "http://localhost");
  public static final int CALL_BACK_PORT = loadIntValue("CALL_BACK_PORT", 5200);
  public static final String CALL_BACK_PATH = loadStringValue("CALL_BACK_SECRET", "/api/internal/order_book");

  public static final String CALL_BACK_SECRET = loadStringValue("CALL_BACK_SECRET", "CALL_BACK_SECRET");

  private static String loadStringValue(String env, String defaultValue) {
    if (System.getenv(env) == null) {
      System.out.println(
          "WARNING: SYSTEM ENV " + env + " NOT FOUND - SET DEFAULT VALUE: " + defaultValue);
      return defaultValue;
    } else {
      return System.getenv(env);
    }
  }

  private static int loadIntValue(String env, int defaultValue) {
    if (System.getenv(env) == null) {
      System.out.println(
          "WARNING: SYSTEM ENV " + env + " NOT FOUND - SET DEFAULT VALUE: " + defaultValue);
      return defaultValue;
    } else {
      return Integer.parseInt(System.getenv(env));
    }
  }

}
