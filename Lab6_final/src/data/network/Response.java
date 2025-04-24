package data.network;

import java.io.Serializable;

public class Response implements Serializable {
    private String info;

    public Response(String info) {
        this.info = info;
    }


    @Override
    public String toString() {
        return info;
    }
}
