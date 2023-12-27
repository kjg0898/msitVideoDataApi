import io.vertx.core.Vertx;

/**
 * packageName    : PACKAGE_NAME
 * fileName       : mainAPI.java
 * author         : kjg08
 * date           : 2023-12-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-27        kjg08           최초 생성
 */
public class mainAPI {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VideoDataApi());
    }
}
