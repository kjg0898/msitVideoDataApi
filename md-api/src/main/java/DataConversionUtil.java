import com.google.gson.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * packageName    : PACKAGE_NAME
 * fileName       : DataConversionUtil.java
 * author         : kjg08
 * date           : 2023-12-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-27        kjg08           최초 생성
 */
public class DataConversionUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // value 값의 길이 반환
    public static int calculateDataLength(String data) {
        return data.length(); // Returns the length of the string
    }

    public static String attemptToConvertToJson(String rawData) {
        // 초기 JSON 객체 생성
        JsonObject json = new JsonObject();

        // "&"로 분리하여 각 key-value 쌍을 얻습니다.
        String[] pairs = rawData.split("&");
        for (String pair : pairs) {
            // "="를 사용하여 key와 value를 분리합니다.
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) { // key와 value가 모두 있는 경우에만 처리
                json.addProperty(keyValue[0], keyValue[1]); // JSON 객체에 추가
            }
        }

        // 변환된 JSON 객체를 문자열로 변환하여 반환
        // 만약 아무런 key-value 쌍도 처리되지 않았다면, 빈 JSON 객체가 반환됩니다.
        return json.toString();
    }



    // XML 데이터를 JSON으로 변환
    public static String convertXmlToJson(String xmlData) {
        if (isValidXml(xmlData)) { // XML이 유효한지 체크
            JSONObject jsonObj = XML.toJSONObject(xmlData);
            return jsonObj.toString(4); // 예쁘게 포맷된 JSON 문자열 반환
        } else {
            // 유효하지 않은 XML 처리
            // 유효하지 않은 XML 시나리오에 대한 로그 또는 처리
            return ""; // 빈 문자열 또는 오류 표시 반환
        }
    }

    private static boolean isValidXml(String xml) {
        return true;
    }


    // CSV 데이터를 JSON으로 변환하는 메서드
    public static String convertCsvToJson(String csvData) {
        String[] lines = csvData.split("\n");
        JsonArray jsonArray = new JsonArray();

        // 첫 번째 행을 헤더(열 이름)로 가정
        String[] headers = lines[0].split(",");

        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            JsonObject jsonObject = new JsonObject();
            for (int j = 0; j < headers.length; j++) {
                jsonObject.addProperty(headers[j], values[j]);
            }
            jsonArray.add(jsonObject);
        }

        return gson.toJson(jsonArray); // JSON 배열로 변환된 문자열 반환
    }

}
