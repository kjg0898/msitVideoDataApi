import com.google.gson.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoDataApi extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(VideoDataApi.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.post("/api/videoData").handler(this::handleVideoData);

        vertx.createHttpServer().requestHandler(router).listen(9999);
        logger.info("Server is listening on port : 9999 , router post path : {}", router.post());
    }

    private void handleVideoData(RoutingContext context) {
        try {
            String jsonBody = context.getBodyAsString();
            try {
                // 구문분석
                JsonElement element = JsonParser.parseString(jsonBody);
                if (element.isJsonObject() || element.isJsonArray()) { // json 인지 체크
                    // json 일때 json 으로 출력
                    logger.info("Received JSON data: {}", gson.toJson(element));
                } else {
                    throw new JsonSyntaxException("Not a JSON structure");
                }
            } catch (JsonSyntaxException e) {
                // json 이 아닐때 원시 데이터 출력
                logger.info("Received raw data: {}", jsonBody);

                String convertedJson = DataConversionUtil.attemptToConvertToJson(jsonBody);
                if (convertedJson.isEmpty()) { //키-값 쌍에서 JSON으로 변환할 수 없는 경우
                    convertedJson = DataConversionUtil.convertXmlToJson(jsonBody); // XML에서 변환
                    if (convertedJson.isEmpty()) { // XML에서 변환할 수 없는 경우
                        convertedJson = DataConversionUtil.convertCsvToJson(jsonBody); //CSV에서 변환
                    }
                }

                if (!convertedJson.isEmpty()) {
                    logger.info("Converted to JSON-like data: {}", convertedJson);
                } else {
                    logger.info("Received non-JSON data (could not convert): {}", jsonBody);
                }
            }
            // 파라미터 데이터 길이
            int length = DataConversionUtil.calculateDataLength(jsonBody);
            //응답
            String response = String.format("{\"status\":\"received\", \"calculatedLength\":%d}", length);
            context.response()
                    .putHeader("content-type", "application/json")
                    .end(response);
        } catch (Exception e) {
            logger.error("Error handling video data", e);
            context.response().setStatusCode(500).end();
        }
    }
}
