package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public Result index() {
        return ok(Json.toJson("welcome"));
    }

    protected static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }

    protected static JsonNode buildJsonErrorResponse(String type, JsonNode fields) {
        ObjectNode wrapper = Json.newObject();
        wrapper.set(type, fields);

        return wrapper;
    }

}
