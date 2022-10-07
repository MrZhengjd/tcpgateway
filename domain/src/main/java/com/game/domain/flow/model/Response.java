package com.game.domain.flow.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    private int status_code;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    private Map<String ,Object> requestMap = new HashMap<>();

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    public static Response buildResponse(Object result){
        Response response = ResponseInstance.getResponse();
        response.setResult(result);
        return response;
    }
    private static class ResponseInstance{
        private static Response response = new Response();
        public static Response getResponse(){
           return response;
        }
    }
}
